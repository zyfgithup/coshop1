package com.systop.amol.finance.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.amol.finance.model.CostDetail;
import com.systop.amol.finance.model.CostSort;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.service.BaseGenericsManager;

/**
 * 收支类别管理
 * 
 * @author songbaojie
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class CostSortManager extends BaseGenericsManager<CostSort> {

	/**
	 * 用于计算收支类别编号
	 */
	private CostSortSerialNoManager serialNoManager;

	@Autowired(required = true)
	public void setSerialNoManager(CostSortSerialNoManager serialNoManager) {
		this.serialNoManager = serialNoManager;
	}

	/**
	 * 保存收支类别信息
	 * 
	 * @see BaseGenericsManager#save(java.lang.Object)
	 */
	@Override
	@Transactional
	public void save(CostSort costSort) {
		Assert.notNull(costSort);
		logger.debug("Parent SpendSort {}", costSort.getParentSort());
		// 查询上级收支类别并建立双向关联
		CostSort parent = costSort.getParentSort();
		getDao().evict(parent);
		if (parent != null && parent.getId() != null) {
			parent = get(costSort.getParentSort().getId());
			if (parent != null) {
				parent.getChildCostSorts().add(costSort);
				costSort.setParentSort(parent);
			}
		} else {
			costSort.setParentSort(null); // 处理parentId为null的情况
		}
		if (costSort.getId() == null) {// 新建的收支类别设置收支类别编号
			costSort.setSerialNo(serialNoManager.getSerialNo(costSort));
		}
		getDao().getHibernateTemplate().clear();
		super.save(costSort);
	}

	/**
	 * 判断收支类别名称是否重复<br>
	 * true为存在重名false为不存在重名
	 * @return
	 */
	public boolean isSpendSortByName(CostSort costSort) {
		StringBuffer hql = new StringBuffer("from CostSort s where s.name = ? and s.user.id = ?");
		List args = new ArrayList();
		args.add(costSort.getName());
		args.add(costSort.getUser().getId());
		if(costSort.getId() != null){ //如果修改时的名称可以和自己原来的名称重复
			hql.append(" and s.id != ?");
			args.add(costSort.getId());
		}
		List list = getDao().query(hql.toString(), args.toArray());
		return list.size() > 0 ? true : false;
	}

	/**
	 * 删除收支类别，解除关联关系
	 */

	// @Override
	// @Transactional
	// public void remove(ProductSort prosort) {
	// Assert.notNull(prosort);
	// // 解除子收支类别关联
	// Set<ProductSort> children = prosort.getChildProductSorts();
	// for (ProductSort child : children) {
	// child.setParentProsort(null);
	// }
	// prosort.setChildProductSorts(Collections.EMPTY_SET);
	//
	// super.remove(prosort);
	// }

	/**
	 * 根据收支类型ID,该类型的所有收入支出明细
	 */
	public List<CostDetail> getCostsByCostsortId(Integer id) {
		String hql = "from CostDetail cd where cd.costSort.id = ? ";
		List<CostDetail> list = getDao().query(hql, id);
		return list;
	}
	
	/**
	 * 根据父ID获得收支类别并按日期排序，如果父ID为Null则获得顶级收支类别
	 * 
	 * @param parentId
	 * @param type
	 *            收支类型：1收，2支, null表示所有
	 * @return
	 */
	public List<CostSort> getCostSortsByParnetId(Integer parentId, String type, User user) {
		List<CostSort> costSorts = null;
		StringBuffer hql = new StringBuffer();
		List args = new ArrayList();
		if (parentId == null) {
			hql.append("from CostSort s where s.parentSort is null ");
		} else {
			hql.append("from CostSort s where s.parentSort.id = ? ");
			args.add(parentId);
		}
		if (StringUtils.isNotBlank(type)) {
			hql.append("and s.type = ? ");
			args.add(type);
		}

		if (user != null) {
			if(user.getSuperior() != null){
				hql.append(" and s.user.id = ? ");
				args.add(user.getSuperior().getId());
			}else{
				hql.append(" and s.user.id = ?");
				args.add(user.getId());
			}
		}

		hql.append("order by s.createTime ");
		costSorts = query(hql.toString(), args.toArray());
		return costSorts;
	}
}
