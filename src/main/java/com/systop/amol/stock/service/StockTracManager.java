package com.systop.amol.stock.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.systop.amol.stock.model.StockTrac;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 库存【调拨】管理
 * 
 * @author songbaojie
 * 
 */
@Service
@SuppressWarnings({ "rawtypes" })
public class StockTracManager extends BaseGenericsManager<StockTrac> {

	/**
	 * 生成库存盘点号，库存盘点号的组成：两位年份两位月份两位日期+（流水号，不够8位前面补零），如：201106230001
	 * 
	 * @return
	 */

	public String getOrder(Date date, User user) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		StringBuilder sb = new StringBuilder(dateFormat.format(date));
		try {
			List list = null;
			user = getDao().get(User.class, user.getId());
			String hql = "select max(s.checkNo) from StockTrac s where DATE_FORMAT( s.createTime , '%Y%m%d' )>= DATE_FORMAT( now()  , '%Y%m%d' ) and (s.user.id = ? or s.user.superior.id = ?)";
			// 判断是否为职员操作
			if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
				list = query(hql.toString(),user.getSuperior().getId(),user.getSuperior().getId());	
			}else{
				list = query(hql.toString(),user.getId(),user.getId());
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
}
