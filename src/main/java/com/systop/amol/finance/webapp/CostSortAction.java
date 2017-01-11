package com.systop.amol.finance.webapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.systop.amol.finance.model.CostDetail;
import com.systop.amol.finance.model.CostSort;
import com.systop.amol.finance.service.CostSortManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 收支类别管理Action
 * 
 * @author songbaojie
 */
@SuppressWarnings({ "serial" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CostSortAction extends DefaultCrudAction<CostSort, CostSortManager> {
	// 异步查询返回值
	private CostSort costSort;

	private Map<String, Object> result;

	// AJAX调用返回变量(JSON)
	private List<Map<String, Object>> costSorts;

	
	
	@Override
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前用户不存在");
		getModel().setUser(user);
		if(StringUtils.isNotBlank(user.getBeginningInit()) && user.getBeginningInit().equals("0")){
			this.addActionError("请先完成初始化,再作单据！ ");
		}
		return super.index();
	}

	/**
	 * 保存数据
	 */
	@Override
	public String save() {
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
					CostSort parent = getManager().get(
							Integer.valueOf(parentId));
					getModel().setParentSort(parent);
				}else{
					getModel().setParentSort(null);
				}
				getModel().setCreateTime(new Date());// 创建日期
			}

			costSort = getModel();// 从model中得到对象
			if (getManager().isSpendSortByName(costSort)) {// 判断商品名称是否重复录入
				this.addActionError("收支类别名称重复请重新录入");
				return INPUT;
			} else {
				return super.save();
			}
			
		} catch (ApplicationException e) {
			e.printStackTrace();
			this.addActionError("添加失败！请与管理员联系！");
			return INPUT;
		}
	}

	/**
	 * 保存数据，异步调用
	 */
	public String asynchronousSave() {
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
					CostSort parent = getManager().get(
							Integer.valueOf(parentId));
					getModel().setParentSort(parent);
				}
				getModel().setCreateTime(new Date());// 创建日期
			}

			costSort = getModel();// 从model中得到对象
			if (getManager().isSpendSortByName(costSort)) {// 判断商品名称是否重复录入
				result.put("info", "收支类别名称重复请重新录入");
			} else {
				getManager().save(costSort);
			}
			result.put("success", true);
			result.put("id", costSort.getId());
			result.put("text", costSort.getName());
			result.put("descn", costSort.getDescn());
			result.put("type", costSort.getType());
		} catch (ApplicationException e) {
			result.put("failure", true);
			result.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return "complete";
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
				List<CostDetail> costDetails = getManager().getCostsByCostsortId(getModel().getId());
				if(costDetails != null && costDetails.size() > 0){
					result.put("success", false);
					result.put("msg", "该类别已用,不可删除!");
				}else{
					getManager().remove(getModel());
					result.put("success", true);
					result.put("msg", "删除成功!");
				}
			} else {
				result.put("success", false);
				result.put("msg", "删除的对象已不存在");
			}
		}
		return "complete";
	}

	/**
	 * 构建收支类别
	 * 
	 * @return
	 */
	public String costSortTree() {
		List<CostSort> tops = null;
		// 如果type为1则只查看收入类别,2只查看支出类别，null查看所有
		String type = getRequest().getParameter("type");// (String)getRequest().getAttribute("status");
		User u = UserUtil.getPrincipal(getRequest());
		// if (u == null) {
		// tops = getManager().getSpendSortsByParnetId(null, type, null);
		// } else {
		tops = getManager().getCostSortsByParnetId(null, type, u);
		// }

		costSorts = new ArrayList<Map<String, Object>>();
		for (CostSort costSort : tops) {
			Map<String, Object> top = toMap(costSort);
			top = buildTreeByParent(top, true, type, u);
			costSorts.add(top);
		}
		return "tree";
	}

	/**
	 * 根据父ID构建JSON数据
	 * 
	 * @param parent
	 * @param nested
	 *            是否嵌套
	 * @param type
	 *            收支类型（1收2支）
	 * @param u
	 *            当前登录用户
	 * @return
	 */
	private Map<String, Object> buildTreeByParent(Map<String, Object> parent,
			boolean nested, String type, User u) {
		if (parent == null || parent.get("id") == null) {
			return null;
		}
		List<CostSort> subs = getManager().getCostSortsByParnetId(
				(Integer) parent.get("id"), type, u);
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		for (CostSort sub : subs) {
			Map<String, Object> child = toMap(sub);
			if (nested) {
				child = buildTreeByParent(child, nested, type, u);
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
	 * 将商品类型中的部分内容存储到MAP中
	 * 
	 * @param p
	 * @return
	 */
	private Map<String, Object> toMap(CostSort s) {
		Map<String, Object> map = null;
		if (s != null) {
			map = new HashMap<String, Object>();
			map.put("id", s.getId());
			// 判断并添加后缀
			if (s.getType() != null && s.getType().equals("1")) {
				map.put("text", s.getName() + " [收入]");
			} else {
				map.put("text", s.getName() + " [支出]");
			}

			map.put("descn", s.getDescn());
			map.put("type", s.getType());
		}
		return map;
	}

	public CostSort getCostSort() {
		return costSort;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public List<Map<String, Object>> getCostSorts() {
		return costSorts;
	}

}
