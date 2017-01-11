package com.systop.amol.purchase.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.systop.amol.base.units.model.UnitsItem;
import com.systop.amol.base.units.service.UnitsItemManager;
import com.systop.amol.purchase.model.Purchase;
import com.systop.amol.purchase.model.PurchaseDetail;
import com.systop.core.service.BaseGenericsManager;

/**
 * 入库明细管理
 * 
 * @author WangHaiYan
 * 
 */
@Service
public class PurchaseDetailManager extends BaseGenericsManager<PurchaseDetail> {
	@Autowired
	private UnitsItemManager unitsItemManager;
	/*
	 * 获取主表对应的所有详细信息
	 */
	public List<PurchaseDetail> getDetails(Integer supperId) {
		List<PurchaseDetail> detailList = query(
				"from PurchaseDetail pd where pd.purchase.id = ?", supperId);
		return detailList;
	}
	//修改订单关联明细数量增加
	public void addOrder(PurchaseDetail pd){
		List<PurchaseDetail> detailList = query(
				"from PurchaseDetail pd where pd.id = ?", pd.getId());
		PurchaseDetail pd1=detailList.get(0);
		int lc=0;
		if(pd1.getLinkCount()!=null){
			lc=pd1.getLinkCount();
		}
		if(lc+pd.getCount()>pd1.getCount()){
			pd1.setLinkCount(pd1.getCount());
		}else{
		pd1.setLinkCount(lc+pd.getCount());
		}
		this.save(pd1);
		
	}
	//修改订单关联明细数量减少
	public void delOrder(PurchaseDetail pd){
		List<PurchaseDetail> detailList = query(
				"from PurchaseDetail pd where pd.id = ?", pd.getId());
		PurchaseDetail pd1=detailList.get(0);
		if(pd1.getLinkCount()-pd.getCount()<0){
			pd1.setLinkCount(0);
		}else{
		pd1.setLinkCount(pd1.getLinkCount()-pd.getCount());
		}
		this.save(pd1);
		
	}
	/**
	 * 判断订单是否全部关联完成
	 * @param orderid
	 */
	public void orderIsOver(Integer orderid){
		List<PurchaseDetail> detailList = query(
				"from PurchaseDetail pd where pd.purchase.id = ?", orderid);
		int flag=1;//判断是否完成
		int flag1=0;//判断是否开始下推
		for(PurchaseDetail pd:detailList){
			if(pd.getCount()-pd.getLinkCount()!= 0){
				flag=0;//未完成
			}
			if(pd.getLinkCount()>0){
				flag1=1;//已经生成入库单了
			}
		}
		Purchase p=this.getDao().get(Purchase.class,orderid);
		if(p!=null){
		if(flag1==1){
			if(flag==1){
			p.setIsover(1);	//完成
			}else{
			p.setIsover(3);//部分完成
			}
		}else{
			p.setIsover(0);//一点也没有生产入库单
		}
		this.getDao().save(p);
		}
	}
	
	public  String getUnitPack(Integer pid,
			Integer count) {
		List<UnitsItem> list= unitsItemManager.getUnitsItemOrderDesc(pid);
		StringBuffer str = new StringBuffer();
		int i = 0; //计数器
		for(UnitsItem ui : list){
			i++;
			// 得到换算个数
			int uc = ui.getCount();
			// 判断是否需要折合
			if(ui.getConversion() != null && ui.getConversion() != 1){
				// 折合数量
				int scc = count/uc;
				//这个后剩余
				count = count - scc*uc;
				// 如果折合到最后不为0，就录入信息
				if(scc != 0 || (count != 0 && i == list.size())){
					str.append(scc).append(ui.getUnits().getName());	
				}						
			}
		}
		return str.toString();
	}

}
