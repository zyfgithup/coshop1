package com.systop.common.modules.dept.webapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.dept.service.DeptManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 部门管理Action
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings( { "serial" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DeptAction extends DefaultCrudAction<Dept, DeptManager> {
	// 异步查询返回值
	private Dept dept;

	private Map<String, Object> result;

	// AJAX调用返回变量(JSON)
	private List<Map<String, Object>> depts;

	@Autowired
	private UserManager userManager;

	/**
	 * 保存数据，异步调用
	 */
	public String save() {
		result = new HashMap<String, Object>();
		try {
			User u = UserUtil.getPrincipal(getRequest());
			if (u != null) {
				getModel().setUser(u);
			} else {
				throw new ApplicationException("该用户未正确登录,请重新登录后再次录入！");
			}
			if (getModel().getId() == null) {// 添加,需要赋值上级类型
				String parentId = getRequest().getParameter("parentId");
				if (StringUtils.isNumeric(parentId)) {
					Dept parent = getManager()
							.get(Integer.valueOf(parentId));
					getModel().setParent(parent);
				}
			}

			dept = getModel();// 从model中得到对象
			if (getManager().isDeptByName(dept)) {// 判断商品名称是否重复录入
				result.put("info", "员工部门名称重复请重新录入");
			} else {
				getManager().save(dept);
			}
			result.put("success", true);
			result.put("id", dept.getId());
			result.put("text", dept.getName());
			result.put("descn", dept.getDescn());
		} catch (ApplicationException e) {
			result.put("failure", true);
			result.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 删除数据，异步调用
	 */
	@Override
	public String remove() {
		result = new HashMap<String, Object>();
		User u = UserUtil.getPrincipal(getRequest());
		if (u != null) {
			if (getModel() != null && getModel().getId() != null) {
				// 或得此类型下的所有商品
				List<User> list = userManager.getUsersByDeptId(getModel().getId());
				// 判断此类型下是否有商品存在如果有不可删除
				if (list != null && list.size() > 0) {
					result.put("success", false);
					result.put("msg", "该部门下已经绑定员工,不可删除!");
				} else {
					getManager().remove(getModel());
					result.put("success", true);
					result.put("msg", "删除成功!");
				}
			} else {
				result.put("success", false);
				result.put("msg", "删除的对象已不存在");
			}
		}
		return SUCCESS;
	}

	/**
	 * 构建员工部门
	 * 
	 * @return
	 */
	public String deptTree() {
		List<Dept> tops = null;

		User u = UserUtil.getPrincipal(getRequest());
		tops = getManager().getDeptsByParnetId(null, u);

		depts = new ArrayList<Map<String, Object>>();
		for (Dept dept : tops) {
			Map<String, Object> top = toMap(dept);
			top = buildTreeByParent(top, true, u);
			depts.add(top);
		}
		return "tree";
	}

	/**
	 * 根据父ID构建JSON数据
	 * 
	 * @param parent
	 * @param nested
	 *            是否嵌套
	 * @param status
	 *            状态类型
	 * @param u
	 *            当前登录用户
	 * @return
	 */
	private Map<String, Object> buildTreeByParent(Map<String, Object> parent,
			boolean nested, User u) {
		if (parent == null || parent.get("id") == null) {
			return null;
		}
		List<Dept> subs = getManager().getDeptsByParnetId(
				(Integer) parent.get("id"), u);
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		for (Dept sub : subs) {
			Map<String, Object> child = toMap(sub);
			if (nested) {
				child = buildTreeByParent(child, nested, u);
			}
			children.add(child);
		}
		if (!children.isEmpty()) {
			parent.put("children", children);
			parent.put("leaf", false);
		} else {
			parent.put("leaf", true);
		}
		return parent;
	}

	/**
	 * 将员工部门中的部分内容存储到MAP中
	 * 
	 * @param p
	 * @return
	 */
	private Map<String, Object> toMap(Dept p) {
		Map<String, Object> map = null;
		if (p != null) {
			map = new HashMap<String, Object>();
			map.put("id", p.getId());
			map.put("text", p.getName());
			map.put("descn", p.getDescn());
		}
		return map;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public Dept getDept() {
		return dept;
	}

	public List<Map<String, Object>> getDepts() {
		return depts;
	}

	public void setDepts(List<Map<String, Object>> depts) {
		this.depts = depts;
	}

}
