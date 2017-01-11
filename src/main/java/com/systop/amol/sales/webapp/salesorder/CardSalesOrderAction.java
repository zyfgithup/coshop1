package com.systop.amol.sales.webapp.salesorder;

import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.units.model.Units;
import com.systop.amol.card.service.CardGrantManager;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.model.SalesDetail;
import com.systop.amol.sales.service.SalesDetailManager;
import com.systop.amol.sales.service.SalesOrderManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * card 销售订单管理Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CardSalesOrderAction extends
		DefaultCrudAction<Sales, SalesOrderManager> {

	@Resource
	private CustomerManager customerManager;

	@Resource
	private SalesDetailManager salesDetailManager;

	@Resource
	private UserManager userManager;

	@Resource
	private CardGrantManager cardGrantManager;

	/** 开始时间 */
	private String startDate;

	/** 结束时间 */
	private String endDate;

	/**
	 * excel报表导出
	 * 
	 * @return
	 */
	public String exportExcel() {
		this.edit();
		return "exportExcel";
	}

	/**
	 * @author 王会璞 将订单冲红
	 * @return
	 */
	public String redRed() {
		getManager().redRed(getModel().getId(),
				UserUtil.getPrincipal(getRequest()).getId());
		return SUCCESS;
	}

	/**
	 * 显示正常和作废的map
	 * 
	 * @return
	 */
	public Map<Integer, String> getStatusMap() {
		return SalesConstants.STATUS_MAP;
	}

	/**
	 * 删除订单信息
	 */
	public String remove() {
		this.getManager().remove(getModel());
		return SUCCESS;
	}

	/**
	 * 查询订单信息
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String index() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from Sales s where s.status = ? and s.orderCardNo != null ");
		List args = new ArrayList();
		// 设置单子状态为"订单"
		args.add(SalesConstants.ORDERS);
//		User user = UserUtil.getPrincipal(getRequest());
//		if (user != null) {
//			sql.append(" and s.user.id = ? ");
//			args.add(user.getId());
//			if (StringUtils.isNotBlank(getModel().getCkzt())) {
//				sql.append(" and s.ckzt = ? ");
//				args.add(getModel().getCkzt());
//			}
//			if (user.getBeginningInit() != null) {
//				if (user.getBeginningInit().equals("0")) {
//					this.addActionError("请先完成初始化，再做业务单据");
//				}
//			}
//			user = userManager.get(user.getId());
//			if (user.getBeginningInit() == null) {
//				if (user.getSuperior().getBeginningInit().equals("0")) {
//					this.addActionError("您的上级经销商还没有完成初始化，所以您现在不能正式使用该平台。");
//				}
//				user = user.getSuperior();
//			}
//		}
//		getModel().setUser(user);

		if (StringUtils.isNotBlank(getModel().getSalesNo())) {
			sql.append(" and s.salesNo like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
		}

		if (getModel().getCustomer() != null) {
			if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and s.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and s.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and s.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (getModel() != null && getModel().getRedRed() != null
				&& getModel().getRedRed() != SalesConstants.ALL) {
			sql.append(" and s.redRed = ?");
			args.add(getModel().getRedRed());
		}

		sql.append(" order by s.createTime desc, s.salesNo desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
System.out.println("sql --- "+sql);
		String nsql = "select sum(samount) " + sql.toString();
		List list = getManager().query(nsql, args.toArray());
		String total = "";
		if (list.size() > 0) {
			for (Object o : list) {
				if (o != null) {
					total = o.toString();
				}
			}
		}
		getRequest().setAttribute("payTotal", total);
		return INDEX;
	}

	/**
	 * 选择未完成出库的销售订单
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String showIndex() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from Sales s where s.status = ? and s.orderCardNo != null ");
		List args = new ArrayList();
		// 设置单子状态为"订单"
		args.add(SalesConstants.ORDERS);

		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {

			sql.append(" and s.ckzt != ? ");
			args.add(SalesConstants.SALES_COMPLETE);

			if (user.getSuperior() == null) {
				Integer superid = user.getId();
				if (superid != null && superid.intValue() > 0) {
					sql.append(" and (s.user.superior.id = ? or s.user.id = ?) and (s.user.type=? and s.user.id != null) ");
					args.add(superid);
					args.add(superid);
					args.add(AmolUserConstants.AGENT);
				}
			} else if (user.getSuperior() != null
					&& user.getType().equals(AmolUserConstants.EMPLOYEE)) {
				Integer superid = user.getSuperior().getId();
				if (superid != null && superid.intValue() > 0) {
					sql.append(" and (s.user.superior.id = ? or s.user.id = ?) and s.user.type=? ");
					args.add(superid);
					args.add(superid);
					args.add(AmolUserConstants.EMPLOYEE);
				}
			} else if (user.getSuperior() != null
					&& user.getType().equals(AmolUserConstants.AGENT)) {// 如何是分销商，只能查看自己的销售信息情况
				sql.append(" and s.user.id = ? ");
				args.add(user.getId());
			}

			if (user.getBeginningInit() != null) {
				if (user.getBeginningInit().equals("0")) {
					this.addActionError("请先完成初始化，再做业务单据");
				}
			}
			user = userManager.get(user.getId());
			if (user.getBeginningInit() == null) {
				if (user.getSuperior().getBeginningInit().equals("0")) {
					this.addActionError("您的上级经销商还没有完成初始化，所以您现在不能正式使用该平台。");
				}
				user = user.getSuperior();
			}
		}
		getModel().setUser(user);

		if (StringUtils.isNotBlank(getModel().getSalesNo())) {
			sql.append(" and s.salesNo like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
		}

		if (getModel().getCustomer() != null) {
			if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and s.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and s.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and s.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (getModel() != null && getModel().getRedRed() != null
				&& getModel().getRedRed() != SalesConstants.ALL) {
			sql.append(" and s.redRed = ?");
			args.add(getModel().getRedRed());
		}

		sql.append(" order by s.createTime desc, s.salesNo desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);

		String nsql = "select sum(samount) " + sql.toString();
		List list = getManager().query(nsql, args.toArray());
		String total = "";
		if (list.size() > 0) {
			for (Object o : list) {
				if (o != null) {
					total = o.toString();
				}
			}
		}
		getRequest().setAttribute("payTotal", total);
		return "selectOrderUI";
	}

	/**
	 * 查看订单信息
	 */
	@Override
	public String view() {
		super.view();
		getRequest().setAttribute("status",
				getBillStatusMap().get(getModel().getStatus()));
		getSalesDetail();
		return VIEW;
	}

	/**
	 * 保存订单信息 现金
	 */
	@Override
	public String save() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		try {
			getModel().setUser(user);
			getModel().setStatus(SalesConstants.ORDERS);
			getModel().setCkzt(SalesConstants.SALES_NO);
			if (null != getModel().getCustomer()) {
				if (null != getModel().getCustomer().getId()
						&& 0 < getModel().getCustomer().getId()) {
					getModel().setCustomer(
							customerManager.get(getModel().getCustomer().getId()));
				} else {
					getModel().setCustomer(null);
				}
			}

			// 如果用户没有选择销售员则设置为空
			if (getModel().getEmployee().getName().trim().equals("")) {
				getModel().setEmployee(null);
			}

			// 商品总数量
			int count = 0;
			// 保存订单明细
			String[] pids = this.getRequest().getParameterValues("pid");
			String[] unitids = this.getRequest().getParameterValues("unitid");
			String[] outprices = this.getRequest().getParameterValues("outprice");
			String[] ncounts = this.getRequest().getParameterValues("ncount");
			String[] counts = this.getRequest().getParameterValues("counts");
			String[] moneys = this.getRequest().getParameterValues("money");
			String[] remarks = this.getRequest().getParameterValues("sremark");

			List<SalesDetail> sdlist = new ArrayList<SalesDetail>();
			for (int i = 1; i < pids.length; i++) {

				SalesDetail sd = new SalesDetail();
				sd.setAmount(Double.parseDouble(moneys[i]));
				sd.setNcount(Float.parseFloat(ncounts[i]));
				sd.setCount(Integer.parseInt(counts[i]));
				sd.setOutPrice(Double.parseDouble(outprices[i]));
				sd.setProducts(this.getManager().getDao()
						.get(Products.class, Integer.parseInt(pids[i])));
				sd.setRemark(remarks[i]);
				sd.setTnorootl(Integer.parseInt(counts[i]));
				String[] uids = unitids[i].split(",");
				sd.setUnits(this.getManager().getDao()
						.get(Units.class, Integer.parseInt(uids[0])));
				sd.setSales(getModel());
				count += Integer.parseInt(counts[i]);
				sdlist.add(sd);
			}

			// 订单中的商品剩余总数量
//			getModel().setTtno(count);
//
//			// 订单中的商品总数量
//			getModel().setCount(count);

			// 代币卡号不能为，将销售的代币卡与销售单关联
			try {
				if (StringUtils.isNotBlank(getModel().getOrderCardNo())) {
					getModel().setCardGrant(
							cardGrantManager.getCardGrant(getModel().getOrderCardNo()));
				} else {
					addActionError("代币卡号不能为空！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			getManager().validate(getModel());

			this.getManager().save(getModel(), sdlist);

			return SUCCESS;
		} catch (Exception e) {
			addActionError(e.getMessage());
			return INPUT;
		}
	}

	/**
	 * 编辑订单信息
	 */
	@Override
	public String edit() {
		// 获得单号
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		if (getModel().getId() == null && getModel().getStatus() == null) {
			// 生成订单号
			getModel().setSalesNo(
					getManager().getOrderNumber(SalesConstants.ORDERS, user.getId()));
			// 当前时间
			getModel().setCreateTime(DateUtil.getCurrentDate());
		} else {
			getSalesDetail();
		}
		return super.edit();
	}

	/**
	 * 把销售订单明细传到页面
	 */
	public List<SalesDetail> getSalesDetail() {
		List<SalesDetail> sd = salesDetailManager.getDetails(getModel().getId());
		this.getRequest().setAttribute("sd", sd);
		return sd;
	}

	/**
	 * 单子状态Map
	 * 
	 * @author 王会璞
	 */
	public Map<String, String> getBillStatusMap() {
		return SalesConstants.BILL_STATUS_MAP;
	}

	/**
	 * 出库状态Map
	 * 
	 * @author 王会璞
	 */
	public Map<String, String> getSalesStatusMap() {
		return SalesConstants.SALES_STATUS_MAP;
	}

	/**
	 * 出库状态Map S
	 * 
	 * @author 王会璞
	 */
	public Map<String, String> getSalesStatusMapS() {
		return SalesConstants.SALES_STATUS_MAP_S;
	}

	/**
	 * return
	 */
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}