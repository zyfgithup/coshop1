package com.systop.amol.user;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理及相关模块的常量类
 * 
 * @author Sam Lee
 * 
 */
public final class AmolUserConstants {

	/**
	 * 用户距离商家最远位置7公里
	 */
	public final static int JULI = 3000000;
	
	/**
	 * 和user表中的is_sys有区别，是为了标识几种不同用户类型。
	 * 系统管理用户
	 */
	public final static String SYSTEM = "system";
	/**
	 * 系统管理用户
	 */
	public final static String ADMIN = "admin";
	

	/** 生产企业用户  */
	public final static String COMPANY = "company";
	
	/** app用户 */
	public final static String APP_USER = "app_user";
	
	/** 区域负责人 */
	public final static String AGENT_LEVEL_COUNTY = "agent_level_county";
	
	/** 商家 */
	public final static String AGENT_LEVEL_VILLAGE = "agent_level_village";
	
	/** 商家、区域负责人都是这个值 */
	public final static String AGENT = "agent";
	
	/** 分销商最小提现金额 */
	public final static int MIN_MONEY = 500;

	/** 银行用户 */
	public final static String BANK = "bank";
	
	/**职员*/
	public final static String EMPLOYEE = "employee";
	
	/** 注册未审核用户 */
	public final static String UN_VERIFY = "un_verify";

	public final static Map<String, String> TYPE_MAP = new HashMap<String, String>();
	static{
		TYPE_MAP.put(SYSTEM, "系统用户");
		TYPE_MAP.put(AGENT, "区域管理员");
//		TYPE_MAP.put(EMPLOYEE, "职员");
	}
	
	private AmolUserConstants() {
	}
}
