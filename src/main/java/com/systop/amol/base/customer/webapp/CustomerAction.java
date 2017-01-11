package com.systop.amol.base.customer.webapp;

import com.systop.amol.base.customer.CustomerConstants;
import com.systop.amol.base.customer.model.Customer;
import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.Constants;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.JsonCrudAction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户管理Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CustomerAction extends JsonCrudAction<Customer, CustomerManager> {

	/** 地区Manager */
	@Resource
	private RegionManager regionManager;

	/** 检测用户返回的结果 */
	private Map<String, Object> checkResult;

	/**
	 * 查询客户信息
	 */
	@Override
	public String index() {
		query(false);
		return INDEX;
	}
	
	public String initIndex(){
		query(false);
		return "initIndex";
	}

	/**
	 * 编辑客户信息
	 */
	@Override
	public String edit() {
		User user = UserUtil.getPrincipal(getRequest());
		// 创建客户时没有选中状态，那么默认状态为 0
		Integer id = getModel().getId();
		if (id == null && getModel().getType() == null) {
			getModel().setType(CustomerConstants.PTKH);
		}
		if (user != null) {
			List<Customer> customers = getManager().query(
					"from Customer c where c.owner.id = ? order by c.createTime desc",
					user.getId());
			if (customers.size() > 0) {
				Customer customer = customers.get(0);
				Region region = getManager().getDao().get(Region.class,
						customer.getRegion().getId());
				getModel().setRegion(region);
			} else {
				getModel().setRegion(null);
			}
		} else {
			addActionError("未登录，请登录后访问本页面。");
			return INPUT;
		}

		return super.edit();
	}

	/**
	 * 返回状态Map
	 */
	public Map<String, String> getStatusMap() {
		return CustomerConstants.STATUS_MAP;
	}

	/**
	 * 保存客户信息
	 */
	@Override
	public String save() {
		if (hasActionMessages() || hasActionErrors()) {
			return INPUT;
		} else {
			User user = UserUtil.getPrincipal(getRequest());
			if (user != null) {
				getModel().setOwner(user);
			} else {
				addActionError("未登录，请登录后访问本页面。");
				return INPUT;
			}
			User agent = getModel().getAgent();
			if (agent.getId() == null) {// 如果分销商为空则添加默认当前登录用户
				getModel().setAgent(user);
				getModel().setStatus(user.getStatus());
			}else{
				User newUser = getManager().getDao().get(User.class,getModel().getAgent().getId());
				
				getModel().setStatus(newUser.getStatus());
			}
			if (getModel() != null && getModel().getRegion() != null
					&& getModel().getRegion().getId() != null) {
				getModel().setRegion(regionManager.get(getModel().getRegion().getId()));
			}
			if (getModel().getId() != null) {
				if (getModel().getCardGrants().size() > 0) {
					if (getModel().getType().equals("0")) {
						addActionError("您已经拥有代币卡，不能修改为普通客户！");
						return INPUT;
					}
				}
			}
		}
		getManager().getDao().getHibernateTemplate().clear();
		getManager().save(getModel());
		return SUCCESS;
	}

	/**
	 * 预览客户信息
	 */
	public String view() {
		super.view();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (getModel().getCreateTime() != null) {
			String createTime = dateFormat.format(getModel().getCreateTime());
			getRequest().setAttribute("createTime", createTime);
		}
		getRequest().setAttribute("type",
				CustomerConstants.getStatus(getModel().getType()));
		return VIEW;
	}

	/**
	 * 进销存销售管理中使用的客户选择列表 会员和普通客户
	 * 
	 * @return
	 */
	public String showIndex() {
		query(false);
		return "showIndex";
	}

	/**
	 * 进销存销售管理中使用的客户选择列表 会员客户
	 * 
	 * @return
	 */
	public String showIndexHy() {
		query(true);
		return "showIndex";
	}

	/**
	 * AJAX请求，检测身份证号是否已存在
	 */
	public String checkIdCard() {
		checkResult = new HashMap<String, Object>();
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setOwner(user);
			checkResult.put(
					"exist",
					this.getManager().getDao()
							.exists(this.getModel(), "idCard", "owner.id"));
		}
		return "jsonRst";
	}

	/**
	 * 查询
	 * 
	 * @param tf
	 *          ture查询会员 false查询会员和普通用户
	 */
	private void query(boolean tf) {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Customer c where 1=1");
		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			if (user.getSuperior() != null) {
				if (tf) {
					sql.append(" and c.owner.id = ? and c.type = 1 ");
					args.add(user.getSuperior().getId());
				} else {
					sql.append(" and c.owner.id = ? ");
					args.add(user.getSuperior().getId());
					/*
					 * //分销商，能查顶级经销商的会员客户和自己建立的客户
					 * sql.append(" and c.owner.id = ? or (c.owner.id = ? and c.type = 1 )"
					 * ); args.add(user.getId()); args.add(user.getSuperior().getId());
					 */
				}
			} else {
				if (tf) {
					sql.append(" and (c.owner.superior.id = ? or c.owner.id = ?) and c.type = 1 ");
					args.add(user.getId());
					args.add(user.getId());
				} else {
					// 顶级经销商，他可以查看自己以及自己下属的分销商的所有客户
					sql.append(" and (c.owner.superior.id = ? or c.owner.id = ?)");
					args.add(user.getId());
					args.add(user.getId());
					if (StringUtils.isNotBlank(getModel().getType())) {
						sql.append(" and c.type = ? ");
						args.add(getModel().getType());
					}
				}
			}
			/*
			 * if (user.getSuperior() == null) { //顶级经销商，他可以查看自己以及自己下属的分销商的所有客户
			 * sql.append(" and c.owner.superior.id = ? or c.owner.id = ?");
			 * args.add(user.getId()); args.add(user.getId()); } else {//
			 * 如何是分销商，只能查看自己的销售信息情况 sql.append(" and c.owner.id = ? ");
			 * args.add(user.getId()); }
			 */
		}

		if (StringUtils.isNotBlank(getModel().getStatus())) {
			sql.append(" and c.status = ?");
			args.add(getModel().getStatus());
		}
		if (StringUtils.isNotBlank(getModel().getName())) {
			sql.append(" and c.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}

		if (StringUtils.isNotBlank(getModel().getIdCard())) {
			sql.append(" and c.idCard like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getIdCard()));
		}

		if (StringUtils.isNotBlank(getModel().getPhone())) {
			sql.append(" and c.phone like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getPhone()));
		}

		if (getModel() != null && getModel().getRegion() != null
				&& getModel().getRegion().getId() != null) {
			Region region = getManager().getDao().get(Region.class,
					getModel().getRegion().getId());
			if (region != null) {
				sql.append(" and c.region.code like ?");
				if (region.getCode().substring(2, 6).equals("0000")) {
					args.add(MatchMode.START.toMatchString(region.getCode().substring(0,
							2)));
				} else {
					if (region.getCode().substring(4, 6).equals("00")) {
						args.add(MatchMode.START.toMatchString(region.getCode().substring(
								0, 4)));
					} else {
						args.add(region.getCode());
					}
				}
			}
			// 为了返回查询中的条件数据需要将Region存入到MODEL中
			getModel().setRegion(region);
		}

		sql.append(" order by c.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
	}

	/**
	 * 启用客户
	 * 
	 * @return
	 */
	public String unsealCustomer() {
		if (getModel() != null) {
			getModel().setStatus(Constants.YES);
			getManager().update(getModel());
		} else {
			logger.debug("客户信息不存在.");
		}
		return SUCCESS;
	}

	/**
	 * 禁用客户
	 * 
	 * @return
	 */
	@Transactional
	public String remove() {
		if (getModel().getId() != null) {
			getModel().setStatus(Constants.NO);
			getManager().update(getModel());
		}
		return SUCCESS;
	}

	public Map<String, Object> getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(Map<String, Object> checkResult) {
		this.checkResult = checkResult;
	}

}