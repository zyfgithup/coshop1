package com.systop.amol.stock.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.stock.StockConstants;
import com.systop.amol.stock.model.StockCheckDetail;
import com.systop.amol.stock.model.StockCheckLP;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 库存盘点损益单管理
 * 
 * @author songbaojie
 * 
 */
@Service
public class StockCheckLPManager extends BaseGenericsManager<StockCheckLP> {

	/**
	 * 生成库存盘点损益单号，库存盘点损益单号的组成：两位年份两位月份两位日期+（流水号，不够8位前面补零），如：201106230001
	 * 
	 * @param date
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public String getOrderByLossAndProfit(Date date, User u) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		StringBuilder sb = new StringBuilder(dateFormat.format(date));
		List list = null;
		// dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// Date now = dateFormat.parse(dateFormat.format(date));
			u = getDao().get(User.class, u.getId());
			// 判断是否为职员操作
			if(u.getSuperior() != null && u.getType().equals(AmolUserConstants.EMPLOYEE)){
				list = query(
						"select max(s.checkNo) from StockCheck s where DATE_FORMAT( s.createTime , '%Y%m%d' )>= DATE_FORMAT( now()  , '%Y%m%d' ) and s.user.superior.id = ?",
						u.getSuperior().getId());
			}else{
				list = query(
						"select max(s.checkNo) from StockCheck s where DATE_FORMAT( s.createTime , '%Y%m%d' )>= DATE_FORMAT( now()  , '%Y%m%d' ) and s.user.id = ?",
						u.getId());
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
	 * 通过库存盘点详单中损失的信息列表，生成报损单
	 * 
	 * @param list
	 *            库存盘点详单损失信息列表
	 * @param no
	 *            报损(盈)单编号
	 * @param sign
	 *            标示（0:损 1:盈）
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveStockCheckLossAndProfit(List<StockCheckDetail> list,
			String no, String sign) {
		for (StockCheckDetail s : list) {
			StockCheckLP entity = new StockCheckLP();
			entity.setCreateTime(new Date()); // 创建日期
			entity.setCheckNo(no);// 报损(盈)单编号
			entity.setSign(sign);// 标示
			entity.setStockCheckDetail(s);
			entity.setUser(s.getUser());
			getDao().save(entity);
		}
	}

	/**
	 * 根据库存盘点ID得到亏损单记录
	 * 
	 * @param stockCheckId
	 * @return
	 */
	public List<StockCheckLP> listLossStockCheckLP(Integer stockCheckId) {
		List<StockCheckLP> checkLPs = new ArrayList<StockCheckLP>();
		checkLPs = query(
				"from StockCheckLP s where s.stockCheckDetail.stockCheck.id = ? and s.sign = ? ",
				stockCheckId, StockConstants.LOSS);
		return checkLPs;
	}

	/**
	 * 根据库存盘点ID得到盈利单记录
	 * 
	 * @param stockCheckId
	 * @return
	 */
	public List<StockCheckLP> listProfitStockCheckLP(Integer stockCheckId) {
		List<StockCheckLP> checkLPs = new ArrayList<StockCheckLP>();
		checkLPs = query(
				"from StockCheckLP s where s.stockCheckDetail.stockCheck.id = ? and s.sign = ? ",
				stockCheckId, StockConstants.PROFIT);
		return checkLPs;
	}
}
