package com.systop.common.modules.security.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.systop.common.modules.security.acegi.resourcedetails.ResourceDetails;
import com.systop.common.modules.security.user.model.Role;

/**
 * 用户管理及相关模块的常量类
 * 
 * @author Sam Lee
 * 
 */
public final class UserConstants {
	/**
	 * 在Web目录下存放照片的路径
	 */
	public static final String USER_PHOTO_PATH = "/uploaded/photo/";
	/**
	 * 权限操作－URL
	 */
	public static final String PERMISSION_OPERATION_URL = "target_url";
	/**
	 * 权限操作－函数
	 */
	public static final String PERMISSION_OPERATION_FUNC = "target_function";
	
	/**
   * 第一个用户的编号
   */
  public static final String FIRST_CODE = "01";

	/**
	 * 用户类型－系统用户
	 */
	public static final String USER_TYPE_SYS = "1";
	
	/**
	 * 用户类型－高级经销商用户
	 */
	public static final String USER_TYPE_AGENT = "2";
	
	/**
	 * 用户类型－普通经销商用户
	 */
	public static final String USER_TYPE_AGENT_GENERAL = "3";

	/**
	 * 用户状态－可用
	 */
	public static final String USER_STATUS_USABLE = "1";
	
	/**
	 * 用户状态－禁用
	 */
	public static final String USER_STATUS_DISABLE = "0";
	
	/**
	 * 用户状态－注册未审核
	 */
	public static final String USER_STATUS_REG_UNVERIFY = "2";

	/**
	 * 性别常量，M-男性
	 */
	public static final String GENT = "M";

	/**
	 * 性别常量，F-女性
	 */
	public static final String LADY = "F";

	/**
	 * 超级管理员角色
	 */
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	/**
	 * 系统管理员角色
	 */
	public static final String ROLE_SYSTEM = "ROLE_SYSTEM";
  
	/**
	 * 银行用户角色
	 */
	public static final String ROLE_BANK = "ROLE_BANK";
	
	/**
	 * 生产厂家角色
	 */
	public static final String ROLE_SUPPLIER = "ROLE_SUPPLIER";
	
	/**
	 * 顶级经销商角色【高级】
	 */
	public static final String ROLE_TOP_DEALER = "ROLE_TOP_DEALER";
	
	/**
	 * 顶级经销商角色【普通】
	 */
	public static final String ROLE_TOP_DEALER_GENERAL = "ROLE_TOP_DEALER_GENERAL";
	
	/**
	 * 末端经销商角色【高级】
	 */
	public static final String ROLE_END_DEALER = "ROLE_END_DEALER";
	/**
	 * 末端经销商角色【普通】
	 */
	public static final String ROLE_END_DEALER_GENERAL = "ROLE_END_DEALER_GENERAL";
	
	/**
	 * 顶级经销商采购角色
	 */
	public static final String ROLE_EMPLOYEE_PURCHASE = "ROLE_EMPLOYEE_PURCHASE";
	
	/**
	 * 顶级经销商库管角色
	 */
	public static final String ROLE_EMPLOYEE_STOCK = "ROLE_EMPLOYEE_STOCK";
	
	/**
	 * 顶级经销商销售角色
	 */
	public static final String ROLE_EMPLOYEE_SALE = "ROLE_EMPLOYEE_SALE";
	
	/**
	 * 顶级经销商财务角色
	 */
	public static final String ROLE_EMPLOYEE_FINANCE = "ROLE_EMPLOYEE_FINANCE";
	
	/**
	 * 系统角色列表
	 */
	public static final List<Role> SYS_ROLES = new ArrayList<Role>();

	private static Role sysRole(String roleName, String descn) {
		Role role = new Role();
		role.setName(roleName);
		role.setDescn(descn);
		role.setIsSys(USER_TYPE_SYS); // 标记为系统角色
		return role;
	}

	private static Role agentRole(String roleName, String descn) {
		Role role = new Role();
		role.setName(roleName);
		role.setDescn(descn);
		role.setIsSys(USER_TYPE_AGENT); // 标记为经销商角色
		return role;
	}
	
	static {
		SYS_ROLES.add(sysRole(ROLE_ADMIN, "超级管理员角色"));
		SYS_ROLES.add(sysRole(ROLE_SYSTEM, "系统管理员角色"));	
		SYS_ROLES.add(sysRole(ROLE_TOP_DEALER, "区域负责人"));	
		SYS_ROLES.add(sysRole(ROLE_TOP_DEALER_GENERAL, "注册经销商普通角色"));	
			
	}

	/**
	 * 权限操作列表
	 */
	public static final Map<String, String> PERMISSION_OPERATIONS = Collections
			.synchronizedMap(new HashMap<String, String>());
	static {
		PERMISSION_OPERATIONS.put(PERMISSION_OPERATION_FUNC, "函数权限");
		PERMISSION_OPERATIONS.put(PERMISSION_OPERATION_URL, "URL权限");
	}

	/**
	 * 性别常量Map
	 */
	public static final Map<String, String> SEX_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		SEX_MAP.put(GENT, "男");
		SEX_MAP.put(LADY, "女");
	}

	/**
	 * URL资源
	 */
	public static final String RESOURCE_TYPE_URL = ResourceDetails.RES_TYPE_URL;

	/**
	 * 函数资源
	 */
	public static final String RESOURCE_TYPE_FUNCTION = ResourceDetails.RES_TYPE_FUNCTION;

	/**
	 * 权限操作列表
	 */
	public static final Map<String, String> RESOURCE_TYPES = Collections
			.synchronizedMap(new HashMap<String, String>());
	static {
		RESOURCE_TYPES.put(RESOURCE_TYPE_URL, "URL资源");
		RESOURCE_TYPES.put(RESOURCE_TYPE_FUNCTION, "函数资源");
	}
	/**
	 * 缺省密码，在修改的时候，如果输入缺省密码，则表示使用原来的密码。
	 */
	public static final String DEFAULT_PASSWORD = "*********";

	/**
	 * 防止实例化
	 */
	private UserConstants() {
	}
}
