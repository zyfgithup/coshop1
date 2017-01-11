package com.systop.amol.finance;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 收支类别管理及相关模块的常量类
 * 
 * @author Sam Lee
 * 
 */
public final class CostSortConstants {

	/**
	 * 顶级收支类别名称
	 */
	public static final String TOP_SPENDSORT_NAME = "收支类别";

	/**
	 * 顶级收支类别ID
	 */
	public static final Integer TOP_SPENDSORT_ID = 0;

	/**
	 * 第一个收支类别的编号
	 */
	public static final String FIRST_SERIAL_NO = "01";
	
	/**
	 * 未冲红
	 */
	public static final String RED_NO = "0";
	
	/***
	 * 已冲红
	 */
	public static final String RED_YES = "1";
	
	/**
	 * 冲红单
	 */
	public static final String RED_RED = "2"; 
	
	/***
	 * 全部
	 */
	public static final String RED = "3";
	
	/**
	 * 收支类别，1-收入
	 */
	public static final String COST_SORT_IN = "1";

	/**
	 * 收支类别，2-支出
	 */
	public static final String COST_SORT_OUT = "2";

	/**
	 * 收支类别Map
	 */
	public static final Map<String, String> COST_SORT_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		COST_SORT_MAP.put(COST_SORT_IN, "收入");
		COST_SORT_MAP.put(COST_SORT_OUT, "支出");
	}

}
