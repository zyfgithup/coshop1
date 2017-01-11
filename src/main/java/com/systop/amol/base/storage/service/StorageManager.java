package com.systop.amol.base.storage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.base.storage.StorageConstants;
import com.systop.amol.base.storage.model.Storage;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.service.StockManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 仓库管理
 * 
 * @author songbaojie
 */

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StorageManager extends BaseGenericsManager<Storage> {

	@Resource
	private StockManager stockManager;

	@Override
	@Transactional
	public void save(Storage storage) {
		if (getDao().exists(storage, "name", "creator.id")) {
			throw new ApplicationException("该仓库已经存在,请您查证后再次录入");
		}
		super.save(storage);
	}

	/**
	 * 根据用户，和状态得到仓库,状态为NULL将返回当前用户所有仓库，用户为NULL将返回仓库为空
	 * 
	 * @param user
	 * @param status
	 * @return
	 */
	private Map getStorageMapByStatus(User user, String status) {
		List<Storage> storageList = null;
		Map storageMap = new HashMap();
		if (user != null) {
			user = getDao().get(User.class,user.getId());
			if(user.getSuperior()==null){
				storageList = getDao().query(
						"from Storage s where s.creator.id = ? and s.status = ?",
						user.getId(), StorageConstants.RUN);
			}else{
				if(user.getType().equals(AmolUserConstants.EMPLOYEE)){ //员工
					storageList = getDao().query(
							"from Storage s where s.creator.id = ? and s.status = ?",
							user.getSuperior().getId(), StorageConstants.RUN);	
				}else if(user.getType().equals(AmolUserConstants.AGENT)){ //分销商
					storageList = getDao().query(
							"from Storage s where s.creator.id = ? and s.status = ?",
							user.getId(), StorageConstants.RUN);
				}
			}
			for (Storage s : storageList) {
				storageMap.put(s.getId(), s.getName());
			}
		}
		return storageMap;
	}

	/**
	 * 根据当前用户得到所有仓库Map集合 （Key=ID,Value=NAME） 如果当前用户为null则将没有库返回
	 * 
	 * @return
	 */
	public Map getStorageMap(User user) {
		return getStorageMapByStatus(user, null);
	}

	/**
	 * 根据当前用户得到所有"可用"状态的仓库Map集合 （Key=ID,Value=NAME） 如果当前用户为null则将没有库返回
	 * 
	 * @return
	 */
	public Map getStorageMapRun(User user) {
		// 判断是否为职员操作
		if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			return getStorageMapByStatus(user.getSuperior(), StorageConstants.RUN);	
		}else{
			return getStorageMapByStatus(user, StorageConstants.RUN);
		}
	}

	/**
	 * 根据当前用户得到所有"停用"状态的仓库Map集合 （Key=ID,Value=NAME） 如果当前用户为null则将没有库返回
	 * 
	 * @return
	 */
	public Map getStorageMapStop(User user) {
		return getStorageMapByStatus(user, StorageConstants.STOP);
	}

	/**
	 * 根据仓库ID判断仓库下是否有商品记录存在 85936200
	 * 
	 * @param storageId
	 * @return true存在,false不存在
	 */
	public boolean isNullProjectsByStorageId(Integer storageId) {
		List<Stock> list = stockManager.listStocksByStorageId(storageId);
		boolean result = false;
		if (list != null && list.size() > 0) {
			result = true;
		}
		return result;
	}

	/**
	 * 得到该用户仓库数量
	 * 
	 * @param user
	 * @return
	 */
	public Long getStorageCount(User user) {
		Long l = (Long) getDao().findObject(
				"select count(s) from Storage s where s.creator.id = ? ",
				user.getId());
		return l;
	}

	/**
	 * 得到用户的所有仓库(如果是顶级经销商包括下级经销商仓库)
	 * 
	 * @param user
	 * @return
	 */
	public Map getAllStorage(User user) {
		List<Storage> list = new ArrayList<Storage>();
		Map storageMap = new LinkedMap();
		Map secondStorageMap = new LinkedMap();
		list = getDao()
				.query("from Storage s where s.creator in (from User u where u.superior.id = ? or u.id = ? ) and s.creator.type = 'agent' ",
						user.getId(), user.getId());
		for (Storage s : list) {
			if (s.getCreator().getSuperior() == null) {
				storageMap.put(s.getId(), s.getName());
			} else {
				secondStorageMap.put(s.getId(), s.getName());
			}
		}
		storageMap.putAll(secondStorageMap);
		return storageMap;
	}
	
	/**
	 * 根据仓库名称和用户ID得到库
	 * @param name
	 * @param userId
	 * @return
	 */
	public Storage getStorageByName(String name,Integer userId){
		List<Storage> list = query("from Storage s where s.name = ? and s.creator.id = ?", name,userId);
		if(list == null || list.isEmpty()){
			return null;
		}
		return list.get(0);
	}
}
