package com.systop.amol.stock.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.amol.stock.StockConstants;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.model.StockCheck;
import com.systop.amol.stock.model.StockCheckDetail;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 库存盘点管理
 * 
 * @author songbaojie
 * 
 */
@Service
@SuppressWarnings({ "rawtypes" })
public class StockCheckManager extends BaseGenericsManager<StockCheck> {
	
	@Autowired
	private StockCheckDetailManager stockCheckDetailManager;

	/**
	 * 生成库存盘点号，库存盘点号的组成：两位年份两位月份两位日期+（流水号，不够8位前面补零），如：201106230001
	 * 
	 * @return
	 */
	public String getOrder(Date date, User u) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		StringBuilder sb = new StringBuilder(dateFormat.format(date));
		List list = null;
		try {
			u = getDao().get(User.class, u.getId());
			String hql = "select max(s.checkNo) from StockCheck s where DATE_FORMAT( s.createTime , '%Y%m%d' )>= DATE_FORMAT( now()  , '%Y%m%d' ) and (s.user.superior.id = ? or s.user.id = ?)";
			// 判断是否为职员操作
			if(u.getSuperior() != null && u.getType().equals(AmolUserConstants.EMPLOYEE)){
					list = query(hql.toString(),u.getSuperior().getId(),u.getSuperior().getId());
			}else{
				list = query(hql.toString(),u.getId(),u.getId());
			}
			long count = 1l;
			if (list != null && list.size() > 0 && list.get(0) != null) {
				String s = list.get(0).toString();
				count = new Long(s.substring(s.length() - 4)) + 1;
			}

			sb.append(fillZero(4, String.valueOf(count)));
		} catch (Exception e) {
			throw new ApplicationException("生成订单号失败");
		}
		return sb.toString();
	}

	/**
	 * 补零
	 * 
	 * @param length
	 *            补零后的长度
	 * @param source
	 *            需要补零的字符串
	 * @return
	 */
	private String fillZero(int length, String source) {
		StringBuilder result = new StringBuilder(source);
		for (int i = result.length(); i < length; i++) {
			result.insert(0, '0');
		}
		return result.toString();
	}

	/**
	 * 库存盘点中保存没有记录的仓库信息,同时生成报损单
	 * 
	 * @param list
	 * @param stockCheckDetail
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveStockIsNull(List<Stock> list,
			StockCheckDetail stockCheckDetail) {
		// List<StockCheckDetail> listStockCheckDetails = new
		// ArrayList<StockCheckDetail>();
		for (Stock s : list) {
			// 将各项数据SET到新对象中
			StockCheckDetail entity = new StockCheckDetail();
			entity.setCount(0);
			entity.setCheckCount(0 - s.getCount());
			entity.setStock(s);
			entity.setEmployee(stockCheckDetail.getEmployee());
			entity.setUser(stockCheckDetail.getUser());
			entity.setStockCheck(stockCheckDetail.getStockCheck());
			entity.setCreateTime(stockCheckDetail.getCreateTime());
			// 保存新对象
			getDao().save(entity);
			// 保存后将对象存到集合中，准备生成报损单
			// listStockCheckDetails.add(entity);
		}
		// 调用生成报损单
		// this.saveStockCheckLossAndProfit(listStockCheckDetails, lossNo, "0");
	}

	// /**
	// * 通过库存盘点详单中损失的信息列表，生成报损单
	// * @param list 库存盘点详单损失信息列表
	// * @param no 报损(盈)单编号
	// * @param sign 标示（0:损 1:盈）
	// */
	// @Transactional(propagation = Propagation.REQUIRED)
	// public void saveStockCheckLossAndProfit(List<StockCheckDetail> list,
	// String no, String sign) {
	// for (StockCheckDetail s : list) {
	// StockCheckLP entity = new StockCheckLP();
	// entity.setCreateTime(new Date()); // 创建日期
	// entity.setCheckNo(no);// 报损(盈)单编号
	// entity.setSign(sign);// 标示
	// entity.setStockCheckDetail(s);
	// getDao().save(entity);
	// }
	// }

	/**
	 * 修改库存盘点状态:清算
	 * 
	 * @param stockCheck
	 *            库存盘点对象
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public void stockCheckLiquidateStatus(StockCheck stockCheck) {
		Assert.notNull(stockCheck, "库存盘点对象为空");
		stockCheck.setStatus(StockConstants.LIQUIDATE);
		update(stockCheck);
	}

	/**
	 * 修改库存盘点状态:未清算
	 * 
	 * @param scID
	 *            库存盘点ID
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public void stockCheckNotLiquidateStatus(StockCheck stockCheck) {
		Assert.notNull(stockCheck, "库存盘点对象为空");
		stockCheck.setStatus(StockConstants.NOT_LIQUIDATE);
		update(stockCheck);
	}
	/**
	 * 当盘点单状态为0时(未生成清查数据) 删除单据
	 * @param id
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public void removeStockCheck(Integer id){
		stockCheckDetailManager.removeByCheckId(id);
		getDao().delete(StockCheck.class, id);
	}
	/**
	 * 修改盘点单状态
	 * @param id
	 * @param status
	 */
	@Transactional
	public void updateStatus(Integer id,String status){
		StockCheck sc = get(id);
		if(sc != null){
			sc.setStatus(status);
			getDao().update(sc);	
		}
		
	}
}
