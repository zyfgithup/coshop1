package com.systop.amol.base.supplier.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.base.supplier.model.Supplier;
import com.systop.amol.card.model.Card;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.Constants;
import com.systop.core.service.BaseGenericsManager;

/**
 * 供应商Service
 * 
 * @author 王会璞
 */
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SupplierManager extends BaseGenericsManager<Supplier> {

  /**
   * 根据供应商名称获取对象
   * @param name
   * @return
   */
  public Supplier getSupplier(String name,Integer userid) {
    String hql = "from Supplier s where s.name = ? and s.user.id=? ";
    return (Supplier) getDao().findObject(hql, new Object[] { name,userid });
  }

  
	@Override
	@Transactional
	public void save(Supplier supplier) {
		if (getDao().exists(supplier, "name", "user.id")) {
			String name = supplier.getName();
			supplier.setName(null);
			supplier.setSid(null);
			throw new ApplicationException("【" + name + "】已存在供应商表中！");
		}
//		if (supplier.getRegion() == null
//				|| supplier.getRegion().getId() == null) {
//			throw new ApplicationException("请选择地区。");
//		}
		super.save(supplier);
	}

	/**
	 * 根据用户，返回相应的供应商MAP
	 * 
	 * @param user
	 * @author songbaojie
	 * @return
	 */
	public Map getSupplierMapByUser(User user) {
		List<Supplier> supplierList = null;
		Map supplierMap = new HashMap();
		if (user != null) {
			if(user.getSuperior()==null){
				supplierList = getDao().query(
						"from Supplier s where s.user.id = ?  and s.status = ?",
						new Object[]{user.getId(),Constants.YES});
			}else{
			supplierList = getDao().query(
					"from Supplier s where (s.user.id = ? or s.user.id=?) and s.status = ?",
					new Object[]{user.getId(),user.getSuperior().getId(),Constants.YES});
			}
			for (Supplier s : supplierList) {
				supplierMap.put(s.getId(), s.getName());
			}
		}
		return supplierMap;
	}
	/**
	 * 自动匹配卡号
	 * @return
	 *//*

	public List getSupplier(User user) {
		List<Supplier> list=null;
		List cardsRst = new ArrayList();
		if (user != null) {
			if(user.getSuperior()==null){
				list = getDao().query(
						"from Supplier s where s.user.id = ?  and s.status = ?",
						new Object[]{user.getId(),Constants.YES});
			}else{
			  list = getDao().query(
					"from Supplier s where (s.user.id = ? or s.user.id=?) and s.status = ?",
					new Object[]{user.getId(),user.getSuperior().getId(),Constants.YES});
			}
		
	    if (CollectionUtils.isNotEmpty(list)) {
	      int arraySize = list.size();
	      String[] cards = new String[arraySize];
	      for (int i = 0; i < arraySize; i++) {
	    	  Supplier card = (Supplier) list.get(i);
	        cards[i] = (String) card.getName();
	        cardsRst.add(cards[i]);
	      }
	    }
	    }
		return cardsRst;
	}*/
}