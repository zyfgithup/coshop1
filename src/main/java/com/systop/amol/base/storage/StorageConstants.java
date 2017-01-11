package com.systop.amol.base.storage;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import com.systop.core.Constants;

/**
 * 仓库管理及相关模块的常量类
 * 
 * @author Sam Lee
 * 
 */
public final class StorageConstants {
	/**
	 * 状态常量，1-可用
	 */
	public static final String RUN = Constants.STATUS_AVAILABLE;

	/**
	 * 状态常量，0-停用
	 */
	public static final String STOP = Constants.STATUS_UNAVAILABLE;

	/**
	 * 状态常量Map
	 */
	public static final Map<String, String> STATUS_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		STATUS_MAP.put(RUN, "可用");
		STATUS_MAP.put(STOP, "停用");
	}

}
