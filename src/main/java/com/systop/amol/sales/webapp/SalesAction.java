package com.systop.amol.sales.webapp;

import com.systop.amol.base.customer.model.Customer;
import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.storage.service.StorageManager;
import com.systop.amol.base.units.model.Units;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.*;
import com.systop.amol.sales.service.ReceiveInitManager;
import com.systop.amol.sales.service.SalesDetailManager;
import com.systop.amol.sales.service.SalesManager;
import com.systop.amol.sales.utils.Payment;
import com.systop.amol.user.AmolUserConstants;
import com.systop.amol.util.DoubleFormatUtil;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库单管理Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SalesAction extends DefaultCrudAction<Sales, SalesManager> {

	/** 客户Manager */
	@Resource
	private CustomerManager customerManager;
	/** 仓库Manager */
	@Resource
	private StorageManager storageManager;
	/** 出库单详情明细 */
	@Resource
	private SalesDetailManager salesDetailManager;

	@Resource
	private ReceiveInitManager receiveInitManager;

	/** 销售订单id */
	private Integer orderId;
	/** 销售订单编号 */
	private String orderNo;

	/** 顶级经销商名称 */
	private String name;

	/** 顶级经销商id */
	private Integer id;

	/** 该顶级经销商下的客户名称 */
	private String customerName;

	/** 开始时间 */
	private String startDate;

	/** 结束时间 */
	private String endDate;
	/** 收款情况 */
	private String receive;

	public String showIndex(){
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Sales s where s.status = "+SalesConstants.SALES+" and s.ckzt!="+SalesConstants.FULL_RETURN);
		List args = new ArrayList();

		sql.append(" and s.payment in (?, ?) ");
		args.add(Payment.CASH);
		args.add(Payment.CASHADVANCE);
		
	  // 如果是顶级经销商，他可以查看自己以及自己下属的分销商的所有销售信息情况
		User user = UserUtil.getPrincipal(getRequest());
		if (user.getSuperior() == null) {
			Integer superid = user.getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (s.user.superior.id = ? or s.user.id = ?) and (s.user.type=? and s.user.id != null) ");
				args.add(superid);
				args.add(superid);
				args.add(AmolUserConstants.AGENT);
			}
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			Integer superid = user.getSuperior().getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (s.user.superior.id = ? or s.user.id = ?) and s.user.type=? ");
				args.add(superid);
				args.add(superid);
				args.add(AmolUserConstants.EMPLOYEE);
			}
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)){// 如何是分销商，只能查看自己的销售信息情况
			sql.append(" and s.user.id = ? ");
			args.add(user.getId());
		}
		
		if (user.getBeginningInit().equals("0")) {
			this.addActionError("请先完成初始化，再做业务单据");
		}
		getModel().setUser(user);

		if (StringUtils.isNotBlank(getModel().getCkzt())) {
			sql.append(" and s.ckzt = ? ");
			args.add(getModel().getCkzt());
		}

		if (getModel().getPayment() != null) {
			sql.append(" and s.payment = ? ");
			args.add(getModel().getPayment());
		}

		if (StringUtils.isNotBlank(getModel().getSalesNo())) {
			if (SalesConstants.ORDERS.equals(getModel().getStatus())) {
				sql.append(" and s.single.salesNo like ? ");
			} else if (SalesConstants.SALES.equals(getModel().getStatus())) {
				sql.append(" and s.salesNo like ? ");
			}
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
		}

		if (null != getModel().getCustomer()) {
			if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and s.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}
			if (StringUtils.isNotBlank(getModel().getCustomer().getPhone())) {
				sql.append(" and s.customer.phone = ?");
				args.add(getModel().getCustomer().getPhone());
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
		return "showIndex";
	}
	
	/**
	 * 及时应收 cash
	 * 
	 * @return
	 */
	public String receiveAble() {
		try {
			getManager().getDao().clear();
			Page page = PageUtil.getPage(getPageNo(), getPageSize());

			Map<String, Object> paramCustomer = this.getCustomerCondition();
			page = customerManager.pageQuery(page, paramCustomer.get("sql")
					.toString(), ((List) paramCustomer.get("param")).toArray());
			List listCustomer = page.getData();
			List<ReceiveAble> raList = new ArrayList<ReceiveAble>();
			for (int i = 0; i < listCustomer.size(); i++) {
				Integer customerId = ((Customer) listCustomer.get(i)).getId();
				Map<String, Object> paramSale = this.getReceiveAbleCash(customerId);
				// 出库单
				Object ObjSales = getManager().findObject(
						paramSale.get("sql").toString(),
						((List) paramSale.get("param")).toArray());
				// 期初
				Map<String, Object> paramRInit = this.getReceiveAbleInit(customerId);
				Object ObjRInit = getManager().findObject(
						paramRInit.get("sql").toString(),
						((List) paramRInit.get("param")).toArray());
				ReceiveAble receiveAble = new ReceiveAble();
				if (ObjSales != null) {
					Object[] objs = (Object[]) ObjSales; // o对象是Object数组
					receiveAble.setRegionName(objs[0].toString());
					receiveAble.setCustomerName(objs[1].toString());
					receiveAble.setIdCard(objs[2].toString());
					receiveAble.setMobile(objs[3].toString());
					receiveAble.setAmount(Double.parseDouble(objs[4].toString()));
					raList.add(receiveAble);
				}
				if (ObjRInit != null) {
					Object[] objs = (Object[]) ObjRInit; // o对象是Object数组
					if (ObjSales == null) {
						receiveAble.setRegionName(objs[0].toString());
						receiveAble.setCustomerName(objs[1].toString());
						receiveAble.setIdCard(objs[2].toString());
						receiveAble.setMobile(objs[3].toString());
						receiveAble.setAmount(Double.parseDouble(objs[4].toString()));
						raList.add(receiveAble);
					} else {
						receiveAble.setAmount(DoubleFormatUtil.format(receiveAble
								.getAmount()) + Double.parseDouble(objs[4].toString()));
					}
				}
			}
			page.setData(raList);
			restorePageData(page);

			// 得到检索条件 出库单
			Map<String, Object> paramSum = this.getReceiveAbleSum("cash", null);
			List list = getManager().query(paramSum.get("sql").toString(),
					((List) paramSum.get("param")).toArray());
			// 期初
			Map<String, Object> paramInitSum = this.getReceiveAbleInitSum();
			List listInit = getManager().query(paramInitSum.get("sql").toString(),
					((List) paramInitSum.get("param")).toArray());
			double amountSum = 0.0d;
			if (list.size() > 0) {
				for (Object o : list) {
					if (o != null) {
						amountSum = Double.parseDouble(o.toString());
					}
				}
			}
			if (listInit.size() > 0) {
				for (Object o : listInit) {
					if (o != null) {
						amountSum += Double.parseDouble(o.toString());
					}
				}
			}
			getRequest().setAttribute("amountSum", amountSum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "receiveAble";
	}

	/**
	 * 查出出客户信息
	 * 
	 * @return
	 */
	private Map<String, Object> getCustomerCondition() {

		Map<String, Object> result = new HashMap<String, Object>();

		StringBuffer sql = new StringBuffer("from Customer c where 1=1");
		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		if (user.getSuperior() == null) {
			Integer superid = user.getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (c.owner.superior.id = ? or c.owner.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if (user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			Integer superid = user.getSuperior().getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (c.owner.superior.id = ? or c.owner.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		}else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)){
			sql.append(" and c.owner.id = ? ");
			args.add(user.getId());
		}

		if (getModel().getCustomer() != null
				&& StringUtils.isNotBlank(getModel().getCustomer().getName())) {
			sql.append(" and c.customer.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
					.getName()));
		}

		sql.append(" order by c.id desc");
		result.put("sql", sql);
		result.put("param", args);

		return result;
	}

	/**
	 * 向ralist中添加Receive对象
	 * 
	 * @param raList
	 * @param listSQL
	 */
	private void addReceive(List<ReceiveAble> raList, List listSQL) {
		for (Object o : listSQL) {
			Object[] objs = (Object[]) o; // o对象是Object数组
			ReceiveAble receiveAble = new ReceiveAble();
			receiveAble.setRegionName(objs[0].toString());
			receiveAble.setCustomerName(objs[1].toString());
			receiveAble.setIdCard(objs[2].toString());
			receiveAble.setMobile(objs[3].toString());
			receiveAble.setAmount(Double.parseDouble(objs[4].toString()));
			raList.add(receiveAble);
		}
	}

	/**
	 * 及时应收 card
	 * 
	 * @return
	 */
	public String receiveAbleCard() {
		try {
			Page page = PageUtil.getPage(getPageNo(), getPageSize());

			// 得到检索条件
			Map<String, Object> param = this.getReceiveAble(null, "card");
			page = getManager().pageQuery(page, param.get("sql").toString(),
					((List) param.get("param")).toArray());

			List<ReceiveAble> raList = new ArrayList<ReceiveAble>();

			List listSQL = page.getData();
			addReceive(raList, listSQL);
			page.setData(raList);
			restorePageData(page);

			// 得到检索条件
			Map<String, Object> paramSum = this.getReceiveAbleSum(null, "card");
			List list = getManager().query(paramSum.get("sql").toString(),
					((List) paramSum.get("param")).toArray());
			double amountSum = 0.0d;
			if (list.size() > 0) {
				for (Object o : list) {
					if (o != null) {
						amountSum = Double.parseDouble(o.toString());
					}
				}
			}
			getRequest().setAttribute("amountSum", amountSum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "receiveAbleCard";
	}

	/**
	 * 查询客户的期初未收金额 List
	 * 
	 * @return
	 */
	private Map<String, Object> getReceiveAbleInit() {

		Map<String, Object> result = new HashMap<String, Object>();

		StringBuffer sql = new StringBuffer("from ReceiveInit ri where 1=1");
		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		if (user.getSuperior() == null) {
			Integer superid = user.getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (ri.user.superior.id = ? or ri.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			Integer superid = user.getSuperior().getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (ri.user.superior.id = ? or ri.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)){
			sql.append(" and ri.user.id = ? ");
			args.add(user.getId());
		}

		if (StringUtils.isBlank(receive)
				|| receive.equals(SalesConstants.NOT_RECEIVED)) {
			sql.append(" and ri.amount != ri.amountReceived");
		}

		if (StringUtils.isNotBlank(getModel().getSalesNo())) {
			sql.append(" and ri.id = ?");
			args.add(null);
		}

		if (null != getModel().getCustomer()) {
			if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and ri.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and ri.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and ri.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		result.put("sql", sql);
		result.put("param", args);

		return result;
	}

	/**
	 * 查询客户的期初未收金额
	 * 
	 * @param customerId
	 *          客户id
	 * @return
	 */
	private Map<String, Object> getReceiveAbleInit(Integer customerId) {

		Map<String, Object> result = new HashMap<String, Object>();

		StringBuffer sql = new StringBuffer(
				"select ri.customer.region.name,ri.customer.name,ri.customer.idCard,ri.customer.mobile,sum(ri.amount - ri.amountReceived) from ReceiveInit ri where ri.amount != ri.amountReceived and ri.customer.id="
						+ customerId);
		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		if (user.getSuperior() == null) {
			Integer superid = user.getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (ri.user.superior.id = ? or ri.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if (user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			Integer superid = user.getSuperior().getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (ri.user.superior.id = ? or ri.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)){
			sql.append(" and ri.user.id = ?) ");
			args.add(user.getId());
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and ri.createTime >= ? ");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and ri.createTime <= ? ");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		sql.append(" group by ri.customer.id");
		result.put("sql", sql);
		result.put("param", args);

		return result;
	}

	/**
	 * 查询客户的期初未收金额 Sum
	 * 
	 * @return
	 */
	private Map<String, Object> getReceiveAbleInitSum() {

		Map<String, Object> result = new HashMap<String, Object>();

		StringBuffer sql = new StringBuffer(
				"select sum(ri.amount - ri.amountReceived) from ReceiveInit ri where ri.amount != ri.amountReceived");
		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		if (user.getSuperior() == null) {
			Integer superid = user.getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (ri.user.superior.id = ? or ri.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		}  else if (user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			Integer superid = user.getSuperior().getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (ri.user.superior.id = ? or ri.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)){
			sql.append(" and s.user.id = ?) ");
			args.add(user.getId());
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and ri.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and ri.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		result.put("sql", sql);
		result.put("param", args);

		return result;
	}

	private Map<String, Object> getSalesCash() {

		Map<String, Object> result = new HashMap<String, Object>();

		StringBuffer sql = new StringBuffer("from Sales s where s.status = "
				+ SalesConstants.SALES + " and s.redRed = " + SalesConstants.NORMAL);

		List args = new ArrayList();

		sql.append(" and s.payment = ? ");
		args.add(Payment.CASHADVANCE);

		User user = UserUtil.getPrincipal(getRequest());
		if (user.getSuperior() == null) {
			Integer superid = user.getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (s.user.superior.id = ? or s.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			Integer superid = user.getSuperior().getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (s.user.superior.id = ? or s.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)){
			sql.append(" and s.user.id = ? ");
			args.add(user.getId());
		}

		if (StringUtils.isBlank(receive)
				|| receive.equals(SalesConstants.NOT_RECEIVED)) {
			sql.append(" and (samount  - rttao - (spayamount  - realReturnMoney)) > 0");
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

		sql.append(" order by s.createTime desc,s.salesNo desc");

		result.put("sql", sql);
		result.put("param", args);

		return result;
	}

	private Map<String, Object> getReceiveAbleCash(Integer customerId) {

		Map<String, Object> result = new HashMap<String, Object>();

		StringBuffer sql = new StringBuffer(
				"select s.customer.region.name,s.customer.name,s.customer.idCard,s.customer.mobile, sum(s.samount  - s.rttao - (s.spayamount  - s.realReturnMoney)) from Sales s where s.status = "
						+ SalesConstants.SALES
						+ " and s.redRed = "
						+ SalesConstants.NORMAL
						+ " and s.customer.id = " + customerId);

		List args = new ArrayList();

		sql.append(" and s.payment = ? ");
		args.add(Payment.CASHADVANCE);

		User user = UserUtil.getPrincipal(getRequest());
		if (user.getSuperior() == null) {
			Integer superid = user.getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (s.user.superior.id = ? or s.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if (user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			Integer superid = user.getSuperior().getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (s.user.superior.id = ? or s.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)){
			sql.append(" and s.user.id = ?) ");
			args.add(user.getId());
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

		sql.append(" and (s.samount  - s.rttao - (s.spayamount  - s.realReturnMoney))<>0 group by s.customer.id");
		result.put("sql", sql);
		result.put("param", args);

		return result;
	}

	private Map<String, Object> getReceiveAble(String cash, String card) {

		Map<String, Object> result = new HashMap<String, Object>();

		StringBuffer sql = new StringBuffer(
				"select s.customer.region.name,s.customer.name,s.customer.idCard,s.customer.mobile,sum(s.samount - s.spayamount - s.rttao) from Sales s where s.status = "
						+ SalesConstants.SALES + " and s.redRed = " + SalesConstants.NORMAL);

		List args = new ArrayList();

		if (StringUtils.isNotBlank(cash)) {
			sql.append(" and s.payment = ? ");
			args.add(Payment.CASHADVANCE);
		} else if (StringUtils.isNotBlank(card)) {
			sql.append(" and s.payment in(?,?) ");
			args.add(Payment.CARD);
			args.add(Payment.CARDADVANCE);
		}

		User user = UserUtil.getPrincipal(getRequest());
		if (user.getSuperior() == null) {
			Integer superid = user.getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (s.user.superior.id = ? or s.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			Integer superid = user.getSuperior().getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (s.user.superior.id = ? or s.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)) {
			sql.append(" and s.user.id = ? ");
			args.add(user.getId());
		}

		if (null != getModel().getCustomer()) {
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

		sql.append(" and (s.samount - s.spayamount - s.rttao)<>0 group by s.customer.id order by s.createTime desc, s.id desc");
		result.put("sql", sql);
		result.put("param", args);

		return result;
	}

	private Map<String, Object> getReceiveAbleSum(String cash, String card) {

		Map<String, Object> result = new HashMap<String, Object>();

		StringBuffer sql = new StringBuffer(
				"select sum(s.samount  - s.rttao - (s.spayamount  - s.realReturnMoney)) from Sales s where s.status = "
						+ SalesConstants.SALES + " and s.redRed = " + SalesConstants.NORMAL);

		List args = new ArrayList();

		if (StringUtils.isNotBlank(cash)) {
			sql.append(" and s.payment = ? ");
			args.add(Payment.CASHADVANCE);
		} else if (StringUtils.isNotBlank(card)) {
			sql.append(" and s.payment in(?,?) ");
			args.add(Payment.CARD);
			args.add(Payment.CARDADVANCE);
		}

		User user = UserUtil.getPrincipal(getRequest());
		if (user.getSuperior() == null) {
			Integer superid = user.getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (s.user.superior.id = ? or s.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if (user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			Integer superid = user.getSuperior().getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (s.user.superior.id = ? or s.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)){
			sql.append(" and s.user.id = ? ");
			args.add(user.getId());
		}

		if (null != getModel().getCustomer()) {
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

		result.put("sql", sql);
		result.put("param", args);

		return result;
	}

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
	 * @author 王会璞 将出库单冲红
	 * @return
	 */
	public String redRed() {

//		getManager().redRed(getModel().getId(),
//				UserUtil.getPrincipal(getRequest()).getId());

		return SUCCESS;
	}

	/**
	 * 冲红map
	 * 
	 * @return
	 */
	public Map<Integer, String> getStatusMap() {
		return SalesConstants.STATUS_MAP;
	}

	/**
	 * 查询销售出库单信息 Cash
	 */
	@Override
	public String index() {
		query(false);
		return INDEX;
	}

	/**
	 * 应收款 query cash
	 * 
	 * @return
	 */
	public String queryCashIndex() {
		this.receiveAbleDetails();
		return "queryCashIndex";
	}

	/**
	 * 及时应收 cash
	 * 
	 * @return
	 */
	public String receiveAbleDetails() {

		try {

			Page page = PageUtil.getPage(getPageNo(), getPageSize());

			// 出库单
			Map<String, Object> paramSale = this.getSalesCash();
			List<Sales> salesList = getManager().query(
					paramSale.get("sql").toString(),
					((List) paramSale.get("param")).toArray());

			// 期初
			Map<String, Object> paramRInit = this.getReceiveAbleInit();
			List<ReceiveInit> receiveInits = receiveInitManager.query(
					paramRInit.get("sql").toString(),
					((List) paramRInit.get("param")).toArray());

			List<Sales> raList = new ArrayList<Sales>();
			int salesSize = salesList.size();// 出库单
			int initSize = receiveInits.size();// 期初
			for (int i = 0; i < initSize; i++) {
				Sales sales = new Sales();
				sales.setCustomer(receiveInits.get(i).getCustomer());
				sales.setSalesNo("期初");
				sales.setCreateTime(receiveInits.get(i).getCreateTime());
				sales.setSamount(receiveInits.get(i).getAmount());
				sales.setSpayamount(receiveInits.get(i).getAmountReceived());
				sales.setUser(receiveInits.get(i).getUser());
				raList.add(sales);
			}
			for (int i = 0; i < salesSize; i++) {
				raList.add(salesList.get(i));
			}

			List list = raList;

			double samount = 0.0d;
			double spayamount = 0.0d;
			double rttao = 0.0d;
			double realReturnMoney = 0.0d;
			for (Sales sales : raList) {
				if (sales.getSamount() != null) {
					samount += DoubleFormatUtil.format(sales.getSamount());
				}
				if (sales.getSpayamount() != null) {
					spayamount += DoubleFormatUtil.format(sales.getSpayamount());
				}
				if (sales.getRttao() != null) {
					rttao += DoubleFormatUtil.format(sales.getRttao());
				}
				if(sales.getRealReturnMoney() != null){
					realReturnMoney += DoubleFormatUtil.format(sales.getRealReturnMoney());
				}
			}

			page.setData(list);
			restorePageData(page);

			getRequest().setAttribute("samount", samount);
			getRequest().setAttribute("spayamount", spayamount);
			getRequest().setAttribute("rttao", rttao);
			getRequest().setAttribute("realReturnMoney", realReturnMoney);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "receiveAble";
	}

	/**
	 * 出库单查询 card cash
	 * 
	 * @param isCardCash
	 *          如何查询card出库单就true
	 */
	private void query(boolean isCardCash) {

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Sales s where s.status = 1 ");
		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		if (user.getId() != null && user.getId() > 0) {
			sql.append(" and s.user.id = ? ");
			args.add(user.getId());
		}
		if (user.getBeginningInit().equals("0")) {
			this.addActionError("请先完成初始化，再做业务单据");
		}
		getModel().setUser(user);

		if (StringUtils.isNotBlank(getModel().getCkzt())) {
			sql.append(" and s.ckzt = ? ");
			args.add(getModel().getCkzt());
		}

		if (getModel().getPayment() != null) {
			sql.append(" and s.payment = ? ");
			args.add(getModel().getPayment());
		}

		if (StringUtils.isNotBlank(getModel().getSalesNo())) {
			if (SalesConstants.ORDERS.equals(getModel().getStatus())) {
				sql.append(" and s.single.salesNo like ? ");
			} else if (SalesConstants.SALES.equals(getModel().getStatus())) {
				sql.append(" and s.salesNo like ? ");
			}
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
		}

		if (null != getModel().getCustomer()) {
			if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and s.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}
			if (StringUtils.isNotBlank(getModel().getCustomer().getPhone())) {
				sql.append(" and s.customer.phone = ?");
				args.add(getModel().getCustomer().getPhone());
			}
		}

		if (isCardCash) {
			sql.append(" and s.payment in (?, ?) ");
			args.add(Payment.CARD);
			args.add(Payment.CARDADVANCE);
		} else {
			sql.append(" and s.payment in (?, ?) ");
			args.add(Payment.CASH);
			args.add(Payment.CASHADVANCE);
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

	/**
	 * 新建或者编辑出库单信息
	 */
	@Override
	public String edit() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		if (getModel().getId() != null) {
			getSalesDetail(getModel().getId());
			getRequest().setAttribute("status",
					getBillStatusMap().get(getModel().getStatus()));
		} else {
			// 生成出库单号
			getModel().setSalesNo(
					getManager().getSalesNumber(SalesConstants.SALES, user.getId()));
			// 时间
			getModel().setCreateTime(DateUtil.getCurrentDate());
		}
		return super.edit();
	}

	/**
	 * 订单将要生成出库单的预览界面 cash 现金
	 * 
	 * @return
	 */
	public String orderToEditUI() {
		String orderId = getRequest().getParameter("orderId");
		Sales sales = getManager().get(Integer.valueOf(orderId));
		this.orderId = Integer.parseInt(orderId);
		this.orderNo = sales.getSalesNo();
		sales.setSalesNo(getManager().getSalesNumber(SalesConstants.SALES,
				UserUtil.getPrincipal(getRequest()).getId()));
		setModel(sales);
		// 得到出库单详情明细
		getSalesDetail(getModel().getId());
		getModel().setId(null);

		return "orderToStUI";
	}

	/**
	 * 保存出库单
	 */
	@Override
	public String save() {
		// 出库单详情明细
		List<SalesDetail> sdlist = null;
		try {
			User user = UserUtil.getPrincipal(getRequest());
			getModel().setUser(user);
			getModel().setStatus(SalesConstants.SALES);
			getModel().setCkzt(SalesConstants.NOT_RETURN);
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

			getModel().setRttao(0.0d);

			// 商品总数量
			int count = 0;
			// 出库单明细
			String[] pids = this.getRequest().getParameterValues("pid");
			String[] unitids = this.getRequest().getParameterValues("unitid");
			String[] outprices = this.getRequest().getParameterValues("outprice");
			String[] ncounts = this.getRequest().getParameterValues("ncount");
			String[] counts = this.getRequest().getParameterValues("counts");
			String[] moneys = this.getRequest().getParameterValues("money");
			String[] remarks = this.getRequest().getParameterValues("sremark");
			String[] codes = this.getRequest().getParameterValues("codes");
			String[] osdis = null;
			if (orderId != null && orderId > 0) {
				osdis = this.getRequest().getParameterValues("orderSalesDetailId");
			}
			// 出库单详情明细
			sdlist = new ArrayList<SalesDetail>();
			// 商品条形码集合
			List<Object> objectList = new ArrayList<Object>();
			for (int i = 0; i < pids.length; i++) {
				if (StringUtils.isNotBlank(pids[i])) {
					SalesDetail sd = new SalesDetail();
					sd.setAmount(Double.parseDouble(moneys[i]));
					sd.setOutPrice(Double.parseDouble(outprices[i]));
					sd.setNcount(new Float(ncounts[i]));
					sd.setCount(Integer.parseInt(counts[i]));
					sd.setTnorootl(sd.getCount());
					count += sd.getCount().intValue();// 累加所有出库商品
					sd.setProducts(this.getManager().getDao()
							.get(Products.class, Integer.parseInt(pids[i])));
					sd.setRemark(remarks[i]);
					if (osdis != null) {
						if (StringUtils.isNotBlank(osdis[i])) {
							SalesDetail detail = new SalesDetail();
							detail.setId(Integer.parseInt(osdis[i]));
							sd.setOORSalesDetailId(detail);
						}
					}
					String[] uids = unitids[i].split(",");
					sd.setUnits(this.getManager().getDao()
							.get(Units.class, Integer.parseInt(uids[0])));
					sd.setSales(getModel());
					// 商品条形码集合
					List<Barcode> barcodes = new ArrayList<Barcode>();
					String code = codes[i];
					sd.setCodes(code);//将用逗号隔开的条形码存储到商品记录中，商品的codes字段不存储到数据库中
					String[] stra = code.split(",");
					if (codes != null && stra.length > 0) {
						for (String str : stra) {
							Barcode barcode = new Barcode();
							barcode.setBarcode(str);
							barcode.setSalesDetail(sd);
							// 将条形码记录添加到一个集合中
							barcodes.add(barcode);
						}
					} else {
						throw new ApplicationException("商品出库时必须对其进行扫码！");
					}

					// 将商品条码保存到一个集合中
					objectList.add(barcodes);

					// 将商品记录与添加到一个集合中
					sdlist.add(sd);
				}
			}

			// 将累加的出库商品总数放到出库单中
//			getModel().setCount(count);
			// 初始化出库商品对象中的出库真实数量字段
			getModel().setTtno(getModel().getCount());

			// 如果是订单生产出库单，将生产此出库单的订单与出库单关联
			if (orderId != null && orderId > 0) {
				Sales saleso = new Sales();
				saleso.setId(orderId);
				getModel().setSingle(saleso);
			}

			getModel().setRealReturnMoney(0.0d);
			
			// 保存出库单
			getManager().saveStorehouse(getModel(), sdlist, objectList);

			// 出库单详情明细
			getRequest().setAttribute("sd", sdlist);
			return "isPrint";
		} catch (ApplicationException e) {
			saveException(sdlist);
			addActionError(e.getMessage());
			if(getModel().getSingle() != null){
				return "orderToStUI";
			}else{
				return INPUT;
			}
		} catch (NumberFormatException e) {
			saveException(sdlist);
			addActionError("商品的最小单位数量必须是整数！");
			return INPUT;
		}
	}

	/**
	 * 保存出库单时报的异常
	 */
	private void saveException(List<SalesDetail> sdlist) {
		Integer id = null;
		if (getModel().getSingle() != null) {
			id = getModel().getSingle().getId();
		}
		if (id != null) {
			// 得到订单商品详情明细
			List<SalesDetail> list = getSalesDetail(id);
			
			
			for (int i = 0;i<list.size();i++) {
				for (SalesDetail s : sdlist) {
					if ((list.get(i).getProducts().getId().intValue() == s.getProducts().getId()
							.intValue()) && !(list.get(i).getNcount().floatValue() == s.getNcount().floatValue())) {
						list.get(i).setTnorootl(s.getCount());
						break;
					}
				}
			}
			
			
			String deleteProduct = getRequest().getParameter("deleteProduct");
			String[] deleteProductArr = deleteProduct.split(",");
			if (deleteProductArr != null && deleteProductArr.length > 0) {
				for (String str : deleteProductArr) {
					if (StringUtils.isNotBlank(str)) {
						list.remove(Integer.parseInt(str));
					}
				}
				getRequest().setAttribute("deleteProduct", deleteProduct);
			}
		} else {
			this.getRequest().setAttribute("sd", sdlist);
		}
	}

	@Override
	public String view() {
		try {
			super.view();
			getRequest().setAttribute("status",
					getBillStatusMap().get(getModel().getStatus()));
			getSalesDetail(getModel().getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return VIEW;
	}
	
	/**
	 * 打印
	 * @return
	 */
	public String print() {
		view();
		return "print";
	}

	/**
	 * 仓库的下拉列表
	 */
	public Map getStorageMap() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			Map map = storageManager.getStorageMap(user);
			if (map.size() > 0) {
				return map;
			}
			return null;
		}
		return null;
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
	 * 把销售订单明细传到页面
	 */
	public List<SalesDetail> getSalesDetail(Integer id) {
		List<SalesDetail> sd = salesDetailManager.getDetails(id);
		this.getRequest().setAttribute("sd", sd);
		return sd;
	}

	/**
	 * 收款方式Map query
	 */
	public Map<String, String> getHYPaymentMap() {
		return Payment.PAYMENT_MAP;
	}

	/**
	 * 收款方式Map Cash
	 */
	public Map<String, String> getPaymentCashMap() {
		return Payment.PAYMENT_CASH_MAP;
	}

	/**
	 * 收款情况Map Cash
	 */
	public Map<String, String> getReceiveMap() {
		return SalesConstants.RECEIVE_MAP;
	}

	/**
	 * 收款方式Map Card
	 */
	public Map<String, String> getPaymentCardMap() {
		return Payment.PAYMENT_CARD_MAP;
	}

	/**
	 * 退货状态Map
	 * 
	 * @author 王会璞
	 */
	public Map<String, String> getReturnStatusMap() {
		return SalesConstants.RETURN_STATUS_MAP;
	}

	/**
	 * 退货状态Map S
	 * 
	 * @author 王会璞
	 */
	public Map<String, String> getReturnStatusMapS() {
		return SalesConstants.RETURN_STATUS_MAP_S;
	}

	/**
	 * 订单 出库单Map
	 * 
	 * @author 王会璞
	 */
	public Map<String, String> getSalesOrderMap() {
		return SalesConstants.SALES_ORDER_MAP;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}
	

}