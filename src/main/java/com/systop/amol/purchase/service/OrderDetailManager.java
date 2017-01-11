package com.systop.amol.purchase.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.systop.amol.purchase.model.PurchaseDetail;
import com.systop.core.service.BaseGenericsManager;

/**
 * 入库明细管理
 * @author WangHaiYan
 * 
 */
@Service
public class OrderDetailManager extends BaseGenericsManager<PurchaseDetail> {
	
//获取主表对应的所有详细信息
	public List<PurchaseDetail> getDetails(Integer orderid) {
		List<PurchaseDetail> detailList = query("from PurchaseDetail u where u.purchase.id = ?", orderid);		
		return detailList;
	}
	 
}
