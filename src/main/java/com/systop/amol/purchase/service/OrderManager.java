package com.systop.amol.purchase.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.SimpleDateFormat;
import com.systop.amol.purchase.PurchaseConstants;
import com.systop.amol.purchase.model.Purchase;
import com.systop.amol.purchase.model.PurchaseDetail;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 入库管理管理
 * 
 * @author WangHaiYan
 * 
 */
@Service
public class OrderManager extends BaseGenericsManager<Purchase> {
	// 订单明细管理
	@Autowired
	private OrderDetailManager orderDetailManager;

	public OrderDetailManager getOrderDetailManager() {
		return orderDetailManager;
	}

	public void setOrderDetailManager(OrderDetailManager orderDetailManager) {
		this.orderDetailManager = orderDetailManager;
	}

	public void del(int orderid) {
		List<PurchaseDetail> detailList = orderDetailManager.query(
				"from PurchaseDetail u where u.purchase.id = ?", orderid);
		for (PurchaseDetail sd : detailList) {
			orderDetailManager.remove(sd);
		}
	}

	/**
	 * 保存订单
	 * 
	 * @param purchase
	 * @param list
	 */
	@Transactional
	public void save(Purchase purchase, List<PurchaseDetail> list) {

		try {
			// 如果是修改就先删除旧的，再保存新的
			if (purchase.getId() != null && purchase.getId() > 0) {
				del(purchase.getId());
			}
			super.save(purchase);
			// 保存入库详单
			for (PurchaseDetail sd : list) {
				orderDetailManager.save(sd);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("保存失败");
		}

	}

	/**
	 * 删除
	 */
	@Transactional
	public void remove(Purchase purchase) {
		try {
			// 删订单库单明细
			//del(purchase.getId());
			// 删除入库单
			purchase.setStatus(1);//作废
			this.save(purchase);
			//super.remove(purchase);
		} catch (Exception e) {
			throw new ApplicationException("删除失败");
		}
	}

	/**
	 * 得到所有采购管理的单据编号
	 * 
	 * @param billtype
	 * @param userid
	 * @return
	 */
	public String getNo(Integer billtype, User user) {
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		if (billtype == PurchaseConstants.ORDERTYPE) {// 订单
			date = PurchaseConstants.ORDERPREFIX + date;
		} else if (billtype == PurchaseConstants.PURCHASETYPE) {// 入库
			date = PurchaseConstants.PURCHASEPREFIX + date;
		} else if (billtype == PurchaseConstants.RETURNTYPE) {// 退货
			date = PurchaseConstants.RETURNPREFIX + date;
		}
		String jno = "";
		if (billtype == PurchaseConstants.PAYTYPE) {// 应付款单
			date = PurchaseConstants.PAYPREFIX + date;
			String hql= "select max(s.no) from Pay s where no like ? and (s.user.id=? or s.user.superior.id=? )";
			if(user.getType().equals(AmolUserConstants.EMPLOYEE)){
			 jno = (String) getDao().findObject(hql,
						new Object[] { date + "%", user.getSuperior().getId(),user.getSuperior().getId() });
			}else{
			jno = (String) getDao().findObject(hql,
					new Object[] { date + "%", user.getId(),user.getId() });
			}
		} else {
			String hql= "select max(s.sno) from Purchase s where billType=? and sno like ? and (s.user.id=? or s.user.superior.id=?)";
			if(user.getType().equals(AmolUserConstants.EMPLOYEE)){
			jno = (String) getDao().findObject(hql,
					new Object[] { billtype, date + "%", user.getSuperior().getId(),user.getSuperior().getId() });
			}else{
			jno = (String) getDao().findObject(hql,
					new Object[] { billtype, date + "%", user.getId() ,user.getId() });
			}
		}
		if (jno == null || jno.equals("")) {
			jno = date + "0001";
		} else {
			Integer no = Integer.parseInt(jno.substring(8)) + 1;
			String no1 = "000" + no;
			jno = date + no1.substring(no1.length() - 4);
		}
		return jno;
	}

}
