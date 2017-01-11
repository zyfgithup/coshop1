package com.systop.amol.stock.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.model.StockCheck;
import com.systop.amol.stock.model.StockCheckDetail;
import com.systop.core.service.BaseGenericsManager;

/**
 * 库存盘点明细表管理
 * 
 * @author songbaojie
 * 
 */
@Service
@SuppressWarnings("unchecked")
public class StockCheckDetailManager extends
		BaseGenericsManager<StockCheckDetail> {

	// @Autowired
	// private StockCheckManager stockCheckManager;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 通库存过盘点ID得到库存盘点明细中没有盘点到得库存信息
	 * 
	 * @param checkNo
	 * @return
	 */
	public List<StockCheckDetail> getStockCheckIsNull(Integer stockCheckId) {
		List<StockCheckDetail> list = getDao()
				.query("from StockCheckDetail scd where scd.stockCheck.id =  ? and scd.count is null",
						stockCheckId);
		return list;
	}

	/**
	 * 根据库存盘点ID得到该此盘点"亏损"信息
	 * 
	 * @param checkNo
	 * @return
	 */
	public List<StockCheckDetail> getStockCheckLoss(Integer stockCheckId) {
		List<StockCheckDetail> list = getDao()
				.query("from StockCheckDetail s where s.checkCount < 0 and s.stockCheck.id = ?",
						stockCheckId);
		return list;
	}

	/**
	 * 根据库存盘点ID得到该此盘点"盈利"信息
	 * 
	 * @param checkNo
	 * @return
	 */
	public List<StockCheckDetail> getStockCheckProfit(Integer stockCheckId) {
		List<StockCheckDetail> list = getDao()
				.query("from StockCheckDetail s where s.checkCount > 0 and s.stockCheck.id = ?",
						stockCheckId);
		return list;
	}

	/**
	 * 生成库存盘点信息
	 * 
	 * @param model
	 *            库存盘点中的基本信息
	 * @param stocks
	 *            生成库存盘点的库存集合
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void generateStockCheckDetail(StockCheckDetail model,
			List<Stock> stocks) {
		StockCheck stockCheck = model.getStockCheck();
		Assert.notNull(stockCheck);
		// 生成库库盘点单
		// stockCheck.setCheckNo(stockCheckManager.getOrder(new Date(),
		// model.getUser()));
		stockCheck.setCreateTime(model.getCreateTime());
		stockCheck.setUser(model.getUser());
		stockCheck.setStatus("0");
		if (model.getEmployee() != null && model.getEmployee().getId() != null) {
			stockCheck.setEmployee(model.getEmployee());
		}
		getDao().save(stockCheck);
		for (Stock s : stocks) {
			StockCheckDetail detail = new StockCheckDetail();
			detail.setStock(s);
			detail.setStockCount(s.getCount()); //添加原始库存数量
			detail.setStockCheck(stockCheck);
			detail.setCreateTime(new Date());
			// 判断是否有盘点人员
			if (model.getEmployee() != null
					&& model.getEmployee().getId() != null) {
				detail.setEmployee(model.getEmployee());
			}
			detail.setUser(model.getUser());
			this.save(detail);
		}
	}
	
	/**
	 * 追加盘点详单信息
	 * @param id 盘点单ID
	 * @param stocks 追加库存信息列表
	 */
	@Transactional
	public void appGenerateSCD(StockCheckDetail model, List<Stock> stocks){
		StockCheck sc = getDao().get(StockCheck.class,model.getStockCheck().getId());
		Assert.notNull(sc);

		for (Stock s : stocks) {
			StockCheckDetail detail = new StockCheckDetail();
			detail.setStock(s);
			detail.setStockCount(s.getCount()); //添加原始库存数量
			detail.setStockCheck(sc);
			detail.setCreateTime(new Date());
			// 判断是否有盘点人员
			if (sc.getEmployee() != null
					&& sc.getEmployee().getId() != null) {
				detail.setEmployee(sc.getEmployee());
			}
			detail.setUser(model.getUser());
			this.saveOrUpdateSCD(detail);
		}
	}

	/**
	 * 添加或修改盘点明细项
	 * @param stockCheckDetail
	 */
	@Transactional
	public void saveOrUpdateSCD(StockCheckDetail stockCheckDetail){
		String hql = "from StockCheckDetail s where s.stockCheck.id = ? and s.stock.products.id = ?";
		List<StockCheckDetail> list = getDao().query(hql, stockCheckDetail.getStockCheck().getId(),stockCheckDetail.getStock().getProducts().getId());
		if(list != null && list.size() > 0){
			StockCheckDetail oldScd = list.get(0);
			oldScd.setStock(stockCheckDetail.getStock());
			oldScd.setStockCount(stockCheckDetail.getStockCount());
			oldScd.setStockCheck(stockCheckDetail.getStockCheck());
			oldScd.setCreateTime(stockCheckDetail.getCreateTime());
			oldScd.setEmployee(stockCheckDetail.getEmployee());
			oldScd.setUser(stockCheckDetail.getUser());
			oldScd.setCount(stockCheckDetail.getCount());
			oldScd.setCheckCount(stockCheckDetail.getCheckCount());
			this.save(oldScd);
		}else{
			this.save(stockCheckDetail);
		}
	}
	
	/**
	 * 修改库存盘点明细中没有盘点的信息
	 * 
	 * @param list
	 * @param stockCheckDetail
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateStockIsNull(List<StockCheckDetail> list) {
		for (StockCheckDetail scd : list) {
			scd.setCount(0);
			scd.setCheckCount(0 - scd.getStock().getCount());
			update(scd);
		}
	}
	/**
	 * 根据盘点单删除盘点详单
	 * @param parentId
	 */
	public void removeByCheckId(Integer parentId){
		jdbcTemplate.execute("delete from stock_check_details where stock_check_id = " + parentId);
	}
}
