package com.systop.amol.sales.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.model.SalesDetail;
import com.systop.core.service.BaseGenericsManager;

/**
 * 销售详细信息Service
 * 
 * @author 王会璞
 */
@Service
public class SalesDetailManager extends BaseGenericsManager<SalesDetail> {

  //获取主表对应的所有详细信息
	public List<SalesDetail> getDetails(Integer salesId) {
		List<SalesDetail> detailList = query("from SalesDetail sd where sd.sales.id = ?", salesId);		
		return detailList;
	}
	//获取salesDetail对象
	public SalesDetail findDetail(int salesId,int productId) {
	return this.findObject("from SalesDetail sd where sd.sales.id = ? and sd.products.id=?",new Object[]{salesId,productId});
	}
  //获取主表对应的所有详细信息（退货）
	public List<SalesDetail> getDetailsReturn(Integer salesId) {
		List<SalesDetail> detailList = query("from SalesDetail sd where sd.sales.id = ? and sd.tnorootl != 0", salesId);		
		return detailList;
	}
}

