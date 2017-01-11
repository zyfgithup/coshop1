package com.systop.amol.base.prosort;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import com.systop.core.Constants;

/**
 * 商品类型管理及相关模块的常量类
 * 
 * @author Sam Lee
 * 
 */
public final class ProductSortConstants {
	
	/**
	 * 不在客户端显示记录标记字段
	 */
	public final static String HIDE_PHONECLIENT = "hidePhoneClient";
	
	/**
	 * 状态常量，1-可用
	 */
	public static final String RUN = Constants.STATUS_AVAILABLE;

	/**
	 * 状态常量，0-禁止
	 */
	public static final String STOP = Constants.STATUS_UNAVAILABLE;

	/**
	 * 状态常量Map
	 */
	public static final Map<String, String> STATUS_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		STATUS_MAP.put(RUN, "可用");
		STATUS_MAP.put(STOP, "禁用");
	}

	/**
	 * 顶级商品类型名称
	 */
	public static final String TOP_DEPT_NAME = "商品类型";

	/**
	 * 顶级商品类型ID
	 */
	public static final Integer TOP_PROSORT_ID = 0;

	/**
	 * 第一个商品的编号
	 */
	public static final String FIRST_SERIAL_NO = "01";

}
