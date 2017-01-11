package com.systop.amol.base.customer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import com.systop.core.Constants;

/**
 * 客户管理常量类
 * @author 王会璞
 * 
 */
public final class CustomerConstants {
	/**
	 * 状态常量，1会员客户
	 */
	public static final String HYKH = Constants.YES;
	
	public static final String HYKHS = "会员客户";

	/**
	 * 状态常量，0普通客户
	 */
	public static final String PTKH = Constants.NO;
	
	public static final String PTKHS = "普通客户";
	
	/** 禁用客户 */
	public static final String stop ="0";
	
	/** 启用客户 */
	public static final String satrt = "1";

	/**
	 * 状态常量Map
	 */
	public static final Map<String, String> STATUS_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		STATUS_MAP.put(PTKH, PTKHS);
		STATUS_MAP.put(HYKH, HYKHS);
	}

	public static final String getStatus(String s){
		if(HYKH.equals(s)){
			return HYKHS;
		}else if(PTKH.equals(s)){
			return PTKHS;
		}
		return null;
	}
}
