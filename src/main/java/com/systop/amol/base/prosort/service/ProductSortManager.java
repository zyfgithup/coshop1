package com.systop.amol.base.prosort.service;

import com.systop.amol.app.index.IndexConstants;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.prosort.ProductSortConstants;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品类型管理
 * 
 * @author songbaojie
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class ProductSortManager extends BaseGenericsManager<ProductSort> {

	/**
	 * 用于计算商品类型编号
	 */
	private ProductSortSerialNoManager serialNoManager;
	
	@Autowired
	private UserManager userMessage;

	private JdbcTemplate jdbcTemplate;

	@Autowired(required = true)
	public void setSerialNoManager(ProductSortSerialNoManager serialNoManager) {
		this.serialNoManager = serialNoManager;
	}


	/**
	 * 构建商品类型
	 * 
	 * @return
	 */
	public List<Map<String, Object>> prosortTree() {
		List<ProductSort> tops = null;

		User user = userMessage.get(IndexConstants.ADMIN_ID);
		
		// 如果STATUS为1则只得到可用状态商品类型
		//tops = getProductSortsByParnetId(null, ProductSortConstants.RUN, user);
	
		List<Map<String, Object>> productSorts = new ArrayList<Map<String, Object>>();
		for (ProductSort prosort : tops) {
			Map<String, Object> top = toMap(prosort);
			top = buildTreeByParent(top, true, ProductSortConstants.RUN, user);
			productSorts.add(top);
		}
		
		return productSorts;
	}
	public List getUsersByProSort(String prosortId){
		StringBuffer sb = new StringBuffer("select * from users where prosort_id='"+prosortId+"'");
		return jdbcTemplate.queryForList(sb.toString());
	}
	/**
	 * 根据父ID构建JSON数据
	 * 
	 * @param parent
	 * @param nested
	 *            是否嵌套
	 * @param status
	 *            状态类型：1-可用,0-禁用
	 * @param u
	 *            当前登录用户
	 * @return
	 */
	private Map<String, Object> buildTreeByParent(Map<String, Object> parent,
			boolean nested, String status, User u) {
		if (parent == null || parent.get("id") == null) {
			return null;
		}
		List<ProductSort> subs = null;//getProductSortsByParnetId(
		//		(Integer) parent.get("id"), status, u);
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		for (ProductSort sub : subs) {
			Map<String, Object> child = toMap(sub);
			if (nested) {
				child = buildTreeByParent(child, nested, status, u);
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
	private Map<String, Object> toMap(ProductSort p) {
		Map<String, Object> map = null;
		if (p != null) {
			map = new HashMap<String, Object>();
			map.put("id", p.getId());
			map.put("text", p.getName());
			map.put("descn", p.getDescn());
			map.put("status", p.getStatus());
			if (p.getStatus() != null && p.getStatus().equals("0")) {
				map.put("cls", "isStatusNo");
			}
			//map.put("iconCls", "x-tree-node-icon");
		}
		return map;
	}
	
	/**
	 * 保存商品类型信息
	 * 
	 * @see BaseGenericsManager#save(java.lang.Object)
	 */
	@Override
	@Transactional
	public void save(ProductSort prosort) {
		Assert.notNull(prosort);
		logger.debug("Parent dept {}", prosort.getParentProsort());
		// 查询上级商品类型并建立双向关联
		ProductSort parent = prosort.getParentProsort();
		getDao().evict(parent);
		if (parent != null && parent.getId() != null) {
			logger.debug("Parent ProductSort Id {}", prosort.getParentProsort()
					.getId());
			parent = get(prosort.getParentProsort().getId());
			if (parent != null) {
				parent.getChildProductSorts().add(prosort);
				prosort.setParentProsort(parent);
			}
		} else {
			prosort.setParentProsort(null); // 处理parentId为null的情况
		}
		if (prosort.getId() == null) {// 新建的部门设置商品类型编号
			prosort.setSerialNo(serialNoManager.getSerialNo(prosort));
		}
		// FIXME: Clear the session or it will throw an exception with the
		// message:
		// "dentifier of an instance was altered from xxx to xxx"
		getDao().getHibernateTemplate().clear();
		super.save(prosort);
	}
	public void save1(ProductSort productSort){
		super.save(productSort);
	}
	/**
	 * 判断商品类型名称是否重复<br>
	 * true为存在重名false为不存在重名
	 * @return
	 */
	public boolean isProductSortByName(ProductSort prosort) {
		StringBuffer hql = new StringBuffer("from ProductSort ps where ps.name = ? and ps.creator.id = ?");
		List args = new ArrayList();
		args.add(prosort.getName());
		args.add(prosort.getCreator().getId());
		if(prosort.getId() != null){ //判断如果修改时的名称可以和自己原来的名称重复
			hql.append(" and ps.id != ?");
			args.add(prosort.getId());
		}
		List list = getDao().query(hql.toString(), args.toArray());
		return list.size() > 0 ? true : false;
	}

	/**
	 * 删除商品类型，解除关联关系
	 */

	// @Override
	// @Transactional
	// public void remove(ProductSort prosort) {
	// Assert.notNull(prosort);
	// // 解除子商品类型关联
	// Set<ProductSort> children = prosort.getChildProductSorts();
	// for (ProductSort child : children) {
	// child.setParentProsort(null);
	// }
	// prosort.setChildProductSorts(Collections.EMPTY_SET);
	//
	// super.remove(prosort);
	// }

	/**
	 * 根据商品类型ID,该类型的所有商品
	 */
	public List<Products> getProductsByProsortId(Integer id) {
		String hql = "from Products p where p.prosort.id = ? ";
		List<Products> list = getDao().query(hql, id);
		return list;
	}
	/**
	 * 根据商品类型ID,该类型的所有子类别
	 */
	public List<ProductSort> getProsortsByProsortId(Integer parentId) {
		String hql = "from ProductSort p where p.parentProsort.id = ? ";
		List<ProductSort> list = getDao().query(hql, parentId);
		return list;
	}
	/**
	 * 根据父ID获得商品类型并按日期排序，如果父ID为Null则获得顶级商品类型
	 * 
	 * @param parentId
	 * @param status
	 *            状态1-可用,0-禁用
	 * @return
	 */
	public List<ProductSort> getProductSortsByParnetId(Integer parentId,
			String status, User user,String type) {
		List<ProductSort> productSorts = null;
		StringBuffer hql = new StringBuffer();
		List args = new ArrayList();
		if (parentId == null) {
				hql.append("from ProductSort p where p.parentProsort.id is null and p.type='"+type+"'   ");
		} else {
			hql.append("from ProductSort p where p.type='"+type+"' and  p.parentProsort.id = ?  ");
			args.add(parentId);
		}
		hql.append(" and p.status=1 and p.name!='卖油' ");
		if (status != null) {
			hql.append("and p.status = ? ");
			args.add(status);
		}

//		if (user != null) {
//			//判断是否为分销商
//			if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)){
//				hql.append("and p.creator = ?");
//				args.add(user.getSuperior());
//			}else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){//判断是否为职员操作
//				hql.append("and p.creator = ?");
//				args.add(user.getSuperior());
//			}else{
//				hql.append("and p.creator = ?");
//				args.add(user);
//			}
//		}

		hql.append("order by p.createTime ");
		productSorts = query(hql.toString(), args.toArray());
		return productSorts;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
