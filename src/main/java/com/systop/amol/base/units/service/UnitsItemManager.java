package com.systop.amol.base.units.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.base.units.model.UnitsItem;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 计量单位换算管理Manager
 * @author ShangHaiYan
 *
 */
@Service
public class UnitsItemManager extends BaseGenericsManager<UnitsItem> {

	@Transactional
	public void save(UnitsItem unitsItem) {
		if (getDao().exists(unitsItem, "units.id", "user.id", "products.id")) {
			throw new ApplicationException("计量单位为【" + unitsItem.getUnits().getName()  + "】的信息已存在！");
		}
		super.save(unitsItem);
	}
	/**
	 * 当前用户当前商品的计量单位
	 * 如果是分销商，就调用经销商的商品数据
	 * @param userid 当前用户的id
	 * @param userid1 如果当前是分销商，那么用经销商的id，如果是经销商就跟第一个参数一样
	 * @param productid
	 * @return
	 */
	
	public List<UnitsItem> getUnitsItem(int userid,int userid1,int productid){	
	
		String sql = "from UnitsItem c where (c.user.id=? or c.user.id=?) and c.products.id=?";
		return this.query(sql, userid,userid1,productid);	
		
	}
	
	/**
	 * 当前用户当前商品的计量单位
	 * 如果是分销商，就调用经销商的商品数据 
	 * 并且就行倒序
	 * @param userid 当前用户的id
	 * @param userid1 如果当前是分销商，那么用经销商的id，如果是经销商就跟第一个参数一样
	 * @param productid
	 * @return
	 */
	
	public List<UnitsItem> getUnitsItemOrderDesc(int userid,int userid1,int productid){	
	
		String sql = "from UnitsItem c where (c.user.id=? or c.user.id=?) and c.products.id=? order by c.count desc";
		return this.query(sql, userid,userid1,productid);	
		
	}
	/**
	 * 根据商品ID返回商品计量单位
	 * @param productid
	 * @author songbaojie
	 * @return
	 */
	public List<UnitsItem> getUnitsItemOrderDesc(int productid){	
		
		String sql = "from UnitsItem c where c.products.id=? order by c.count desc";
		return this.query(sql,productid);	
		
	}
}
