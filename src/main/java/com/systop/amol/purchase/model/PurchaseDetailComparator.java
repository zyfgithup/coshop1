package com.systop.amol.purchase.model;

import java.util.Comparator;

/**
 * 应付统计排序
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class PurchaseDetailComparator implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		PurchaseDetail user0 = (PurchaseDetail) arg0;
		PurchaseDetail user1 = (PurchaseDetail) arg1;
		// 比较单号
		return user0.getProducts().getId().compareTo(user1.getProducts().getId());

	}

}
