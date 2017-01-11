package com.systop.amol.purchase.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.base.storage.model.Storage;
import com.systop.amol.purchase.model.PayAble;
import com.systop.amol.purchase.model.Purchase;
import com.systop.amol.purchase.model.PurchaseDetail;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.service.StockManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 入库管理管理
 * 
 * @author WangHaiYan
 * 
 */
@Service
public class PurchaseManager extends BaseGenericsManager<Purchase> {


	// 订单明细管理
	@Autowired
	private PurchaseDetailManager purchaseDetailManager;

	/**
	 * 临时访问数据库的对象，不从缓存中取
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	// 及时库存对象
	@Autowired
	private StockManager stockManager;


	// 及时应付对象
	@Autowired
	private PayAbleManager payAbleManager;

	/*
	 * 页面中取得详单
	 */
	public List<PurchaseDetail> getPurchaseDetail(Purchase purchase) {
		List<PurchaseDetail> sd = purchaseDetailManager.getDetails(purchase.getId());
		return sd;
	}

	/*
	 *  删除入库单，同时删除详单，修改及时库存，和应付
	 * 
	 */
	@Transactional
	public void remove(Purchase purchase) {
		try {
			// 删除入库单明细，并且修改库存，应付
			del(purchase,true);
			// 删除入库单
			purchase.setStatus(1);//作废
			super.save(purchase);
			//super.remove(purchase);
			//判断订单是否完成
			if(purchase.getOrderid()!=null && purchase.getOrderid()>0){
			purchaseDetailManager.orderIsOver(purchase.getOrderid());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ApplicationException("删除失败");
		}
	}

	/**
	 * 返回数据库的入库单信息，修改及时应付用
	 */
	private Float getOldPassword(Integer Id) {

		return (Float) jdbcTemplate.query(
				"select s.samount-s.spayamount from purchases s where s.id=?",
				new Object[] { Id }, new ResultSetExtractor() {
					public Object extractData(ResultSet rs) throws SQLException {
						rs.next();
						return rs.getFloat(1);
					}
				});
	}

	/**
	 * 返回数据库的入库单信息，修改及时库存用
	 */
	private Integer getOldStock(Integer Id) {

		return (Integer) jdbcTemplate.query(
				"select s.storage_id from purchases s where s.id=?",
				new Object[] { Id }, new ResultSetExtractor() {
					public Object extractData(ResultSet rs) throws SQLException {
						rs.next();
						return rs.getInt(1);
					}
				});
	}
	


	/*
	 * 删除入库单明细，并且修改库存，应付
	 * isInvalid 是否作废
	 */
	public void del(Purchase purchase,boolean isInvalid) {
		// 删除明细
		getDao().getHibernateTemplate().clear();
		List<PurchaseDetail> detailList = purchaseDetailManager.query(
				"from PurchaseDetail pd where pd.purchase.id = ?", purchase.getId());
		//查数据库中的仓库：
		int oldstock=getOldStock(purchase.getId());
		for (PurchaseDetail sd : detailList) {
			if(oldstock > 0){
			Stock s = new Stock();
			s.setCount(sd.getCount());
			s.setProducts(sd.getProducts());
			s.setUser(purchase.getUser());
			s.setStorage(getDao().get(Storage.class,oldstock));
			stockManager.delIn(s);
			}
			if(isInvalid==false){
			purchaseDetailManager.remove(sd);
			}
			//修改订单关联数量
			if(sd.getLinkCount() != null && sd.getLinkCount()>0){
				PurchaseDetail pdorder=new PurchaseDetail();
				pdorder.setId(sd.getLinkCount());
				pdorder.setCount(sd.getCount());
				purchaseDetailManager.delOrder(pdorder);
			}
		}

		Float jiu = getOldPassword(purchase.getId());
		if (jiu != 0) {
			PayAble payAble = new PayAble();   
			payAble.setSupplier(purchase.getSupplier());
			payAble.setUser(purchase.getUser());
			payAble.setAmount(jiu.doubleValue());
			payAbleManager.delIn(payAble);
		}
		
	}

	/*
	 *  保存应付信息
	 */
	public void saveyf(Purchase purchase) {
		PayAble p = new PayAble();
		p.setSupplier(purchase.getSupplier());
		p.setUser(purchase.getUser());
		// 修改应付，如果有应付就增加应付。
		if(purchase.getSpayamount() == null){
			purchase.setSpayamount(0.0d);
		}
		Double yf = purchase.getSamount() - purchase.getSpayamount();
		if (yf != 0) {
			p.setAmount(yf);
			payAbleManager.saveIn(p);
		}
	}

	

	@Transactional
	public void save(Purchase purchase, List<PurchaseDetail> list) {

		try {
			// 如果是修改就先删除旧的，再保存新的
			if (purchase.getId() != null && purchase.getId() > 0) {
				del(purchase,false);
			}
			
			// 保存应付信息
			saveyf(purchase);
			// 保存入库主表
	
			super.save(purchase);
			// 保存入库详单
			for (PurchaseDetail sd : list) {
				purchaseDetailManager.save(sd);
				// 修改库存
				if(purchase.getStorage()!=null){
				Stock s = new Stock();
				s.setUser(purchase.getUser());
				s.setStorage(purchase.getStorage());
				s.setCount(sd.getCount());
				s.setProducts(sd.getProducts());
				stockManager.saveIn(s);
				}
				//修改订单数量
				if(sd.getLinkCount() != null && sd.getLinkCount()>0){
					PurchaseDetail pdorder=new PurchaseDetail();
					pdorder.setId(sd.getLinkCount());
					pdorder.setCount(sd.getCount());
					purchaseDetailManager.addOrder(pdorder);
				}
			}
			//判断订单是否完成
			if(purchase.getOrderid()!=null && purchase.getOrderid()>0){
			purchaseDetailManager.orderIsOver(purchase.getOrderid());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ApplicationException("保存失败");
		}

	}

	/**
	 * 应付款单上调用的未结账的入库单信息
	 */
	public List<Purchase> getPurchaseNoOver(User user, Integer supplierId) {
		//一级经销商
		if(user.getSuperior()==null){
			return query(
					"from Purchase p where (p.user.superior.id=? or p.user.id=?) and p.supplier.id=?  " +
					"and (p.billType=1 or p.billType=2) and p.samount - p.spayTotal > 0 and p.status=0 order by p.billType desc,p.sno",
					user.getId(),user.getId(), supplierId);
			}else{
				return query(
						"from Purchase p where (p.user.superior.id=? or p.user.id=?) and p.supplier.id=?  " +
						"and (p.billType=1 or p.billType=2) and p.samount - p.spayTotal > 0 and p.status=0 order by p.billType desc,p.sno",
						user.getSuperior().getId(),user.getSuperior().getId(), supplierId);
			}
	}

}
