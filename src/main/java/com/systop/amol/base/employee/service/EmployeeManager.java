package com.systop.amol.base.employee.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.service.BaseGenericsManager;

/**
 * 员工管理Manager
 * 
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EmployeeManager extends BaseGenericsManager<User> {

	@Autowired
	private UserManager userManager;
	
	/** 保存 */
	public void save(User user) {
		userManager.save(user);
	}
	
	/**禁用员工*/
	public void remove(User user){
		userManager.remove(user);
	}
	
	/**
	 * 启用员工
	 * 
	 * @param user
	 */
	public void unsealUser(User user){
		userManager.unsealUser(user);
	}

	/**
	 * 返回部门以及子部门下的员工信息
	 * 
	 * @param dept
	 *            部门树形列表
	 */
	public Map getEmployeeTree(Map dept , String roleName) {
		return userManager.getUserTree(dept, roleName);
	}


	/**
	 * 根据员工部门ID,该部门下的所有员工
	 */
	public List<User> getEmployeesByEmpDeptId(Integer id) {
		String hql = "from User u where u.dept.id = ? ";
		List<User> list = getDao().query(hql, id);
		return list;
	}
}
