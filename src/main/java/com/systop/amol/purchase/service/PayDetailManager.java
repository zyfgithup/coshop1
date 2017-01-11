package com.systop.amol.purchase.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.systop.amol.purchase.model.PayDetail;
import com.systop.amol.purchase.model.PayInit;
import com.systop.amol.purchase.model.Purchase;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.service.BaseGenericsManager;

/**
 * 付款表详单管理
 * 
 * @author WangHaiYan
 * 
 */
@Service
public class PayDetailManager extends BaseGenericsManager<PayDetail> {
	
	//入库单
	@Autowired
	private PurchaseManager purchaseManager;
	
	//期初
	@Autowired
	private PayInitManager payInitManager;

	//付款表详单
	public List<PayDetail> getPayDetails(Integer payId) {
		return this.query("from PayDetail pd where pd.pay.id=?", payId);
	}

	/*
	 * 通过用户和供应商得到有应付的入库单
	 */
	public List<PayDetail> getPurchaseValue(User user, int supplierId) {
		List<Purchase> purchaselist = purchaseManager.getPurchaseNoOver(user, supplierId);
		List<PayDetail> outlist = new ArrayList<PayDetail>();
		//期初
		List<PayInit> pilist=payInitManager.getNoOver(user,supplierId);
		for (PayInit s : pilist) {
			PayDetail od = new PayDetail();
			Purchase p=new Purchase();
			p.setSno("期初");
		    p.setSdate(s.getPdate());
			p.setBillType(0);
			p.setSamount(s.getAmount());
			p.setSpayTotal(s.getPayamount());
			p.setOrderid(s.getId());
			od.setPurchase(p);
			od.setAmount(0d);
			outlist.add(od);
		}
		for (Purchase s : purchaselist) {
			PayDetail od = new PayDetail();
			od.setPurchase(s);
			od.setAmount(0.0d);
			outlist.add(od);
		}
		return outlist;

	}

	public void del(Integer payId) {
		// 删除明细
		List<PayDetail> detailList = query(
				"from PayDetail pd where pd.pay.id = ?", payId);

		for (PayDetail od : detailList) {
			// 修改的应付金额减少，
			Purchase purchase = od.getPurchase();
			purchase.setSpayTotal(purchase.getSpayTotal() - Math.abs(od.getAmount()));
			purchase.setIsover(purchase.getIsover()-1);
			this.getDao().save(purchase);
			remove(od);
		}
	}

	public void save(Integer supplierid,User user,List<PayDetail> paylist) {
		for (PayDetail od : paylist) {
			
			// 修改stock的应付金额，如果是保存就增加，如果是冲红就减少
			Purchase purchase = od.getPurchase();
			if(od.getPay().getStatus()==0){//保存
				//期初
				if(purchase==null){
					
					PayInit pi=od.getPayInit();
					//if(user.getSuperior()==null){
					//pi=payInitManager.query("from PayInit where supplier.id=? and user.id=?", supplierid,user.getId()).get(0);
					//}else{
					//	pi=payInitManager.query("from PayInit where supplier.id=? and (user.id=? or user.id=?)", supplierid,user.getId(),user.getSuperior().getId()).get(0);	
					//}
					if(pi.getPayamount()==null){
						pi.setPayamount(0d);
					}
					pi.setPayamount(pi.getPayamount()+ od.getAmount());
					//od.setPayInit(pi);
					this.getDao().save(pi);
				}else{
				purchase.setSpayTotal(purchase.getSpayTotal() + Math.abs(od.getAmount()));
				purchase.setIsover(purchase.getIsover()+1);
				this.getDao().save(purchase);
				}
			}else if(od.getPay().getStatus()==2){//冲红单
				//期初
				if(purchase==null){
					PayInit pi=od.getPayInit();
					//if(user.getSuperior()==null){
					// pi=payInitManager.query("from PayInit where supplier.id=? and user.id=? ", supplierid,user.getId()).get(0);
					//}else{
					//	 pi=payInitManager.query("from PayInit where supplier.id=? and (user.id=? or user.id=?)", supplierid,user.getId(),user.getSuperior().getId()).get(0);	
					//}
					 pi.setPayamount(pi.getPayamount()+od.getAmount());
					this.getDao().save(pi);
				}else{
				purchase.setSpayTotal(purchase.getSpayTotal() - Math.abs(od.getAmount()));
				purchase.setIsover(purchase.getIsover()-1);
				this.getDao().save(purchase);
				}
			}
			this.save(od);
		}
	}

}
