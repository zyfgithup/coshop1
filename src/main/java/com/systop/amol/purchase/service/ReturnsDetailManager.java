package com.systop.amol.purchase.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.systop.amol.purchase.model.PurchaseDetail;
import com.systop.core.service.BaseGenericsManager;

/**
 * 采购退货明细管理
 * 
 * @author WangHaiYan
 * 
 */
@Service
public class ReturnsDetailManager extends BaseGenericsManager<PurchaseDetail> {

	/*
	 * 获取主表对应的所有详细信息
	 */
	public List<PurchaseDetail> getDetails(Integer supperId) {
		List<PurchaseDetail> detailList = query(
				"from PurchaseDetail pd where pd.purchase.id = ?", supperId);
		return detailList;
	}

}
