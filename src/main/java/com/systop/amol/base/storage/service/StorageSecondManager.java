package com.systop.amol.base.storage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.base.storage.StorageConstants;
import com.systop.amol.base.storage.model.Storage;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 分销商仓库管理
 * 
 * @author songbaojie
 */

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StorageSecondManager extends BaseGenericsManager<Storage> {

	@Override
	@Transactional
	public void save(Storage storage) {
		if (getDao().exists(storage, "creator.id")) {
			throw new ApplicationException("该经销商已经存在仓库,请您查证后再次录入");
		}
		super.save(storage);
	}

	/**
	 * 查询经销商user所属的所有分销商可用仓库
	 * 
	 * @param user 经销商
	 * @return
	 */
	public Map getSecondStorageMapRun(User user) {
		List<Storage> storageList = null;
		Map storageMap = new HashMap();
		if (user != null) {
			storageList = getDao().query(
					"from Storage s where s.creator in (from User u where u.superior.id = ? and u.type = 'agent' ) and s.status = ?",
					user.getId(), StorageConstants.RUN);
			for (Storage s : storageList) {
				storageMap.put(s.getId(), s.getName());
			}
		}
		return storageMap;
	}

}
