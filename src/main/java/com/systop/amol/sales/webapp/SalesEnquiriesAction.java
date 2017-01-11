package com.systop.amol.sales.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.service.SalesManager;
import com.systop.amol.sales.utils.Payment;
import com.systop.common.modules.security.user.UserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 销售查询管理Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SalesEnquiriesAction extends DefaultCrudAction<Sales, SalesManager> {

	/** 单子类型 */
	private String listType;
private String payment;
	private	String salesType;
	/** 开始时间 */
	private String startDate;
	@Resource
	private UserManager userManager;
	private String mobile;
	private String pment;
	

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/** 结束时间 */
	private String endDate;
	/**
	 * 销售查询    card
	 * @return
	 */
	public String cardIndex(){
		String SI = "cardIndex";
		String returnValue = query(null,"card");
		if(returnValue != null){
			SI = returnValue;
		}
		return SI;
	}
	
	/**
	 * 销售查询    cash
	 */
	@Override
	public String index() {
		String SI = INDEX;
		String returnValue = query("cash",null);
		if(returnValue != null){
			SI = returnValue;
		}
		System.out.println("-=-=-=-=-=-=-=-=-"+returnValue);
		return SI;
	}
	private String query(String cash,String card){
		String returnValue = null;
		if (StringUtils.isNotBlank(listType)
				&& !(SalesConstants.PLEASESELECT.equals(listType))) {
			Page page = PageUtil.getPage(getPageNo(), getPageSize());
			StringBuffer sql = new StringBuffer("from Sales s where 1 = 1 ");
			List args = new ArrayList();
			User user = UserUtil.getPrincipal(getRequest());
			user=userManager.get(user.getId());
			if(null!=user.getId()){
				if(!UserConstants.USER_TYPE_SYS.equals(user.getIsSys())){
					sql.append(" and s.merId = ? ");
					args.add(user.getId());
	        	}
	        }
			if (StringUtils.isNotBlank(getModel().getSalesType())) {
				System.out.println("-----------"+getModel().getSalesType());
				sql.append(" and s.salesType = ? ");
				args.add(getModel().getSalesType());
			}
			if (StringUtils.isNotBlank(salesType)) {
				System.out.println("-----------"+getModel().getSalesType());
				sql.append(" and s.salesType = ? ");
				args.add(salesType);
			}
			if (getModel() != null && getModel().getRedRed() != null
					&& getModel().getRedRed() != SalesConstants.ALL) {
				sql.append(" and s.redRed = ?");
				args.add(getModel().getRedRed());
			}
		  // 支付方式
			if (StringUtils.isNotBlank(payment)) {
				if(null!=pment&&!"".equals(pment)){
					sql.append(" and (s.payment = 'ALIPAY' or s.payment='WXPAY') ");
				}else{
				sql.append(" and s.payment = ? ");
				args.add(Payment.valueOf(payment));
				}
			}
			
		// 通过用户名查询
					if (null != getModel().getUser() && null != getModel().getUser().getLoginId()) {
					sql.append(" and s.user.loginId like ?");
					args.add(MatchMode.START.toMatchString(getModel().getUser().getLoginId()));
					}

			if(cash != null){
				if (SalesConstants.ORDERS.equals(listType)) {
					sql.append(" and s.orderCardNo = null ");
					returnValue = "order";
				} else if (SalesConstants.SALES.equals(listType)) {
					sql.append(" and s.payment in(?,?) ");
					args.add(Payment.CASH);
					args.add(Payment.ALIPAY);
					returnValue = "sales";
				} else if (SalesConstants.RETURNS.equals(listType)) {
//					sql.append(" and s.payment in(?,?) ");
//					args.add(Payment.CASH);
//					args.add(Payment.ALIPAY);
					returnValue = "returns";
				}
				sql.append(" and s.status = ? ");
				args.add(listType);
			}else if(card != null){
				if (SalesConstants.ORDERS.equals(listType)) {
					sql.append(" and s.orderCardNo = null and (s.payment = 'ALIPAY' or s.payment='WXPAY') ");
					returnValue = "cardOrder";
				} else if (SalesConstants.SALES.equals(listType)) {
					sql.append(" and s.payment in(?,?) ");
					args.add(Payment.CASH);
					args.add(Payment.ALIPAY);
					returnValue = "cardSales";
				} else if (SalesConstants.RETURNS.equals(listType)) {
					sql.append(" and s.payment in(?,?) ");
					args.add(Payment.CASH);
					args.add(Payment.ALIPAY);
					returnValue = "cardReturns";
				}
				sql.append(" and s.status = ? ");
				args.add(listType);
			}

			if (StringUtils.isNotBlank(getModel().getSalesNo())) {
				sql.append(" and s.salesNo like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
			}

			if (null != getModel().getCustomer()) {
				if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
					sql.append(" and s.customer.name like ?");
					args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
							.getName()));
				}
			}
			if(null!=getModel().getUser())
			{
				if (StringUtils.isNotBlank(getModel().getUser().getName())) {	
					System.out.println("-----------------"+getModel().getUser().getLoginId());
					sql.append(" and s.user.name like ?");
					args.add(MatchMode.START.toMatchString(getModel().getUser().getName()));
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
			sql.append(" order by s.createTime desc, s.salesNo desc");
			page = getManager().pageQuery(page, sql.toString(), args.toArray());
			restorePageData(page);
			System.out.println("-------------->"+sql.toString());
		// 计算合计列
			String nsql = "select sum(samount),sum(rttao),sum(spayamount),sum(cardamount),sum(realReturnMoney) "
					+ sql.toString();
			List list = getManager().query(nsql, args.toArray());
			double samount = 0.0d;
			double spayamount = 0.0d;
			double rttao = 0.0d;
			double cardamount = 0.0d;
			double realReturnMoney = 0.0d;
			if (list.size() > 0) {
				for (Object o : list) {
					Object[] oo = (Object[]) o;
					if (oo[0] != null) {
						samount = Double.parseDouble(oo[0].toString());
					}
					if (oo[1] != null) {
						rttao = Double.parseDouble(oo[1].toString());
					}
					if (oo[2] != null) {
						spayamount = Double.parseDouble(oo[2].toString());
					}
					if (oo[3] != null) {
						cardamount = Double.parseDouble(oo[3].toString());
					}
					if (oo[4] != null) {
						realReturnMoney = Double.parseDouble(oo[4].toString());
					}
				}
			}

			getRequest().setAttribute("samount", samount);
			getRequest().setAttribute("spayamount", spayamount);
			getRequest().setAttribute("rttao", rttao);
			getRequest().setAttribute("cardamount", cardamount);
			getRequest().setAttribute("realReturnMoney", realReturnMoney);
		}
		
		return returnValue;
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
	 * 单子状态Map
	 * 
	 * @author 王会璞
	 */
	public Map<String, String> getBillStatusMap() {
		return SalesConstants.BILL_STATUS_MAP;
	}

	/**
	 * 单子类型Map
	 * 
	 * @author 王会璞
	 */
	public Map<String, String> getListTypeMap() {
		return SalesConstants.LIST_TYPE_MAP;
	}
	
	/**
	 * 出库状态Map S
	 * @author 王会璞
	 */
	public Map<String, String> getSalesStatusMapS() {
		return SalesConstants.SALES_STATUS_MAP_S;
	}
	/**
	 * 团购销售查询
	 */
	public String groupCardIndex(){
		String SI = "groupCardIndex";
		String returnValue = groupQuery(null,"card");
		if(returnValue != null){
			SI = returnValue;
		}
		System.out.println("-=-=-=-=-=-=-=-=-"+returnValue);
		return SI;
	}
	private String groupQuery(String cash,String card){
		String returnValue = null;
		if (StringUtils.isNotBlank(listType)
				&& !(SalesConstants.PLEASESELECT.equals(listType))) {
			Page page = PageUtil.getPage(getPageNo(), getPageSize());
			StringBuffer sql = new StringBuffer("from Sales s where 1 = 1 ");
			List args = new ArrayList();
			// 如果是顶级经销商，他可以查看自己以及自己下属的分销商的所有销售信息情况
//			User user = UserUtil.getPrincipal(getRequest());
//			if (user.getSuperior() == null) {
//				Integer superid = user.getId();
//				if (superid != null && superid.intValue() > 0) {
//					sql.append(" and (s.user.superior.id = ? or s.user.id = ?)");
//					args.add(superid);
//					args.add(superid);
//				}
//			} /*else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
//				Integer superid = user.getSuperior().getId();
//				if (superid != null && superid.intValue() > 0) {
//					sql.append(" and (s.user.superior.id = ? or s.user.id = ?)");
//					args.add(superid);
//					args.add(superid);
//				}
//			} */else/* if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT))*/{// 如何是分销商，只能查看自己的销售信息情况
//				sql.append(" and s.user.id = ? ");
//				args.add(user.getId());
//			}
			User user = UserUtil.getPrincipal(getRequest());
			user=userManager.get(user.getId());
			if(null!=user.getRegion()){
				sql.append(" and s.user.region.code like ? ");
				args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
			}
			if (StringUtils.isNotBlank(getModel().getSalesType())) {
				System.out.println("-----------"+getModel().getSalesType());
				sql.append(" and s.salesType = ? ");
				args.add(getModel().getSalesType());
			}
			if (StringUtils.isNotBlank(salesType)) {
				System.out.println("-----------"+getModel().getSalesType());
				sql.append(" and s.salesType = ? ");
				args.add(salesType);
			}
			if (getModel() != null && getModel().getRedRed() != null
					&& getModel().getRedRed() != SalesConstants.ALL) {
				sql.append(" and s.redRed = ?");
				args.add(getModel().getRedRed());
			}
		  // 支付方式
			if (StringUtils.isNotBlank(payment)) {
			sql.append(" and s.payment = ?");
			args.add(Payment.valueOf(payment));
			}
			
		// 通过用户名查询
					if (null != getModel().getUser() && null != getModel().getUser().getLoginId()) {
					sql.append(" and s.user.loginId like ?");
					args.add(MatchMode.START.toMatchString(getModel().getUser().getLoginId()));
					}
			if(cash != null){
				if (SalesConstants.ORDERS.equals(listType)) {
					sql.append(" and s.orderCardNo = null ");
					returnValue = "order";
				} else if (SalesConstants.SALES.equals(listType)) {
					sql.append(" and s.payment in(?,?) ");
					args.add(Payment.CASH);
					args.add(Payment.ALIPAY);
					returnValue = "sales";
				} else if (SalesConstants.RETURNS.equals(listType)) {
//					sql.append(" and s.payment in(?,?) ");
//					args.add(Payment.CASH);
//					args.add(Payment.ALIPAY);
					returnValue = "returns";
				}
				sql.append(" and s.status = ? ");
				args.add(listType);
			}else if(card != null){
				if (SalesConstants.ORDERS.equals(listType)) {
					sql.append(" and s.orderCardNo = null and s.payment='ALIPAY' ");
					returnValue = "groupCardOrder";
				} else if (SalesConstants.SALES.equals(listType)) {
					sql.append(" and s.payment in(?,?) ");
					args.add(Payment.CASH);
					args.add(Payment.ALIPAY);
					returnValue = "groupCardSales";
				} else if (SalesConstants.RETURNS.equals(listType)) {
					sql.append(" and s.payment in(?,?) ");
					args.add(Payment.CASH);
					args.add(Payment.ALIPAY);
					returnValue = "groupCardReturns";
				}
				sql.append(" and s.status = ? ");
				args.add(listType);
			}

			if (StringUtils.isNotBlank(getModel().getSalesNo())) {
				sql.append(" and s.salesNo like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
			}

			if (null != getModel().getCustomer()) {
				if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
					sql.append(" and s.customer.name like ?");
					args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
							.getName()));
				}
			}
			if(null!=getModel().getUser())
			{
				if (StringUtils.isNotBlank(getModel().getUser().getName())) {	
					System.out.println("-----------------"+getModel().getUser().getLoginId());
					sql.append(" and s.user.name like ?");
					args.add(MatchMode.START.toMatchString(getModel().getUser().getName()));
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

			sql.append(" order by s.createTime desc, s.salesNo desc");
			page = getManager().pageQuery(page, sql.toString(), args.toArray());
			restorePageData(page);
			System.out.println("-------------->"+sql.toString());
		// 计算合计列
			String nsql = "select sum(samount),sum(rttao),sum(spayamount),sum(cardamount),sum(realReturnMoney) "
					+ sql.toString();
			List list = getManager().query(nsql, args.toArray());
			double samount = 0.0d;
			double spayamount = 0.0d;
			double rttao = 0.0d;
			double cardamount = 0.0d;
			double realReturnMoney = 0.0d;
			if (list.size() > 0) {
				for (Object o : list) {
					Object[] oo = (Object[]) o;
					if (oo[0] != null) {
						samount = Double.parseDouble(oo[0].toString());
					}
					if (oo[1] != null) {
						rttao = Double.parseDouble(oo[1].toString());
					}
					if (oo[2] != null) {
						spayamount = Double.parseDouble(oo[2].toString());
					}
					if (oo[3] != null) {
						cardamount = Double.parseDouble(oo[3].toString());
					}
					if (oo[4] != null) {
						realReturnMoney = Double.parseDouble(oo[4].toString());
					}
				}
			}

			getRequest().setAttribute("samount", samount);
			getRequest().setAttribute("spayamount", spayamount);
			getRequest().setAttribute("rttao", rttao);
			getRequest().setAttribute("cardamount", cardamount);
			getRequest().setAttribute("realReturnMoney", realReturnMoney);
		}
		
		return returnValue;
	}

	
	
	/**
	 * 退货状态Map S
	 * 
	 * @author 王会璞
	 */
	
	public Map<String, String> getReturnStatusMapS() {
		return SalesConstants.RETURN_STATUS_MAP_S;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

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

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public String getSalesType() {
		return salesType;
	}

	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}

	public String getPment() {
		return pment;
	}

	public void setPment(String pment) {
		this.pment = pment;
	}
	
	
}