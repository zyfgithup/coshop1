package com.systop.amol.purchase.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.systop.amol.purchase.model.PayAble;
import com.systop.core.service.BaseGenericsManager;

/**
 * 及时应付
 * 
 * @author WangHaiYan
 * 
 */
@Service
public class PayAbleManager extends BaseGenericsManager<PayAble> {

	/*
	 *  入库保存的时候修改库存是库存=库存+本次入库库存
	 */
	public void saveIn(PayAble payAble) {
		String hsql = "from PayAble c where c.user.id = ? and c.supplier.id = ?";
		List<PayAble> slist = null;
		if(payAble.getUser().getType().equals("employee")){
		slist = this.query(hsql, new Object[]{payAble.getUser().getSuperior().getId(), payAble.getSupplier().getId()});
		}else{
			slist = this.query(hsql, new Object[]{payAble.getUser().getId(), payAble.getSupplier().getId()});
		}
		// 修改
		if (slist.size() > 0) {
			PayAble oldPayAble = slist.get(0);
			oldPayAble.setAmount(payAble.getAmount() + oldPayAble.getAmount());
			this.save(oldPayAble);
		} else {
			this.save(payAble);
		}
	}

	/*
	 *  入库单删除的时候修改库存=库存-本次库存
	 */
	public void delIn(PayAble payAble) {
		// 查询应付表里有没有此数据
		String hsql = "from PayAble c where c.user.id=? and c.supplier.id=? ";
		List<PayAble> slist = null;
		if(payAble.getUser().getType().equals("employee")){
		slist = this.query(hsql,payAble.getUser().getSuperior().getId(),payAble.getSupplier().getId());
		}else{
			slist = this.query(hsql,payAble.getUser().getId(),payAble.getSupplier().getId());
		}
		// 修改
		if (slist.size() > 0) {
			PayAble oldPayAble = slist.get(0);
			oldPayAble.setAmount(oldPayAble.getAmount() - payAble.getAmount());
			this.save(oldPayAble);
		} else {
			this.save(payAble);
		}
	}

}
