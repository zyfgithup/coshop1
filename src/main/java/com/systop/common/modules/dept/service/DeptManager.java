package com.systop.common.modules.dept.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.service.BaseGenericsManager;

/**
 * 部门管理Manager
 * 
 * @author songbaojie
 * 
 */
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class DeptManager extends BaseGenericsManager<Dept> {
	/**
	 * 用于计算员工部门
	 */
	private DeptSerialNoManager serialNoManager;

	@Autowired(required = true)
	public void setSerialNoManager(DeptSerialNoManager serialNoManager) {
		this.serialNoManager = serialNoManager;
	}

	/**
	 * 保存员工部门信息
	 * 
	 * @see BaseGenericsManager#save(java.lang.Object)
	 */
	@Override
	@Transactional
	public void save(Dept dept) {
		Assert.notNull(dept);
		logger.debug("Parent dept {}", dept.getParent());
		// 查询上级员工部门并建立双向关联
		Dept parent = dept.getParent();
		getDao().evict(parent);
		if (parent != null && parent.getId() != null) {
			logger.debug("Parent Dept Id {}", dept.getParent().getId());
			parent = get(dept.getParent().getId());
			if (parent != null) {
				parent.getChilds().add(dept);
				dept.setParent(parent);
			}
		} else {
			dept.setParent(null); // 处理parentId为null的情况
		}
		if (dept.getId() == null) {// 新建的部门设置员工部门
			dept.setSerialNo(serialNoManager.getSerialNo(dept));
		}
		// FIXME: Clear the session or it will throw an exception with the
		// message:
		// "dentifier of an instance was altered from xxx to xxx"
		getDao().getHibernateTemplate().clear();
		super.save(dept);
	}

	/**
	 * 判断员工部门名称是否重复<br>
	 * true为存在重名false为不存在重名
	 * 
	 * @return
	 */
	public boolean isDeptByName(Dept dept) {
		StringBuffer hql = new StringBuffer(
				"from Dept d where d.name = ? and d.user.id = ?");
		List args = new ArrayList();
		args.add(dept.getName());
		args.add(dept.getUser().getId());
		if (dept.getId() != null) { // 判断如果修改时的名称可以和自己原来的名称重复
			hql.append(" and d.id != ?");
			args.add(dept.getId());
		}
		List list = getDao().query(hql.toString(), args.toArray());
		return list.size() > 0 ? true : false;
	}

	/**
	 * 删除员工部门，解除关联关系
	 */

	// @Override
	// @Transactional
	// public void remove(ProductSort prosort) {
	// Assert.notNull(prosort);
	// // 解除子员工部门关联
	// Set<ProductSort> children = prosort.getChildProductSorts();
	// for (ProductSort child : children) {
	// child.setParentProsort(null);
	// }
	// prosort.setChildProductSorts(Collections.EMPTY_SET);
	//
	// super.remove(prosort);
	// }

	/**
	 * 根据父ID获得员工部门，如果父ID为Null则获得顶级员工部门
	 * 
	 * @param parentId
	 * @param user
	 *            当前用户
	 * @return
	 */
	public List<Dept> getDeptsByParnetId(Integer parentId, User user) {
		List<Dept> depts = null;
		StringBuffer hql = new StringBuffer();
		List args = new ArrayList();
		if (parentId == null) {
			hql.append("from Dept d where d.parent is null ");
		} else {
			hql.append("from Dept d where d.parent.id = ? ");
			args.add(parentId);
		}

		if (user != null) {
			if(user.getSuperior() != null ){
				hql.append("and d.user = ?");
				args.add(user.getSuperior());
			}else{
				hql.append("and d.user = ? ");
				args.add(user);
			}			
		}

		depts = query(hql.toString(), args.toArray());
		return depts;
	}
}
