package com.systop.amol.purchase.model;

import java.util.Comparator;

/**
 * 应付统计排序
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class PayTotalComparator implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		PayTotal user0 = (PayTotal) arg0;
		PayTotal user1 = (PayTotal) arg1;
		// 比较单号
		return user0.getBillNo().compareTo(user1.getBillNo());

	}

}
