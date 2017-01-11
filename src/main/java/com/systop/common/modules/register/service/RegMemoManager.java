package com.systop.common.modules.register.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.systop.common.modules.register.model.RegMemo;
import com.systop.core.service.BaseGenericsManager;

/**
 * 注册说明信息Manager
 * @author Nice
 */
@Service
public class RegMemoManager extends BaseGenericsManager<RegMemo> {
	
	/**
	 * 获得注册说明信息，仅一条记录。
	 */
	public RegMemo getMemo(){
		return CollectionUtils.isNotEmpty(get()) ? get().get(0) : null; 
	}
}
