package com.systop.amol.sales.webapp;

import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.Receive;
import com.systop.amol.sales.model.ReceiveDetail;
import com.systop.amol.sales.model.ReceiveSummary;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.service.ReceiveDetailManager;
import com.systop.amol.sales.service.ReceiveInitManager;
import com.systop.amol.sales.service.ReceiveManager;
import com.systop.amol.sales.service.SalesManager;
import com.systop.amol.sales.utils.Payment;
import com.systop.amol.user.AmolUserConstants;
import com.systop.amol.util.DoubleFormatUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回款单管理Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReceiveAction extends DefaultCrudAction<Receive, ReceiveManager> {

	/** 出库Manager */
	@Resource
	private SalesManager salesManager;

	/** 客户（农户）Manager */
	@Resource
	private CustomerManager customerManager;
	
	@Resource
	private UserManager userManager;

	/** 回款单明细 */
	@Resource
	private ReceiveDetailManager receiveDetailManager;

	@Resource
	private ReceiveInitManager receiveInitManager;

	/** 开始时间 */
	private String startDate;

	/** 结束时间 */
	private String endDate;

	/** 查询条件 回款单标识 出库单标识 */
	private String receiveSales;

	/** 是否完成了期初设置 **/
	private boolean beginningInit = true;

	/**
	 * excel报表导出    代币卡card
	 * 
	 * @return
	 */
	public String exportExcel() {
		this.edit();
		return "exportExcel";
	}
	
	/**
	 * excel报表导出    现金cash
	 * 
	 * @return
	 */
	public String exportExcelCash() {
		this.edit();
		return "exportExcelCash";
	}

	/**
	 * 新建或者编辑
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
			getSDList(getModel().getId());
		}
		return super.edit();
	}

	/**
	 * 回款单详情查看
	 */
	@Override
	public String view() {
		String singleIdStr = getRequest().getParameter("singleId");
		if (StringUtils.isNotBlank(singleIdStr)) {
			setModel(getManager().get(Integer.parseInt(singleIdStr)));
			getSDList(Integer.parseInt(singleIdStr));
		}
		if(getModel().getPayment().equals(Payment.CASH) || getModel().getPayment().equals(Payment.CASHADVANCE)){
			return "viewCash";
		}
		return VIEW;
	}

	/**
	 * 回款单明细
	 * 
	 * @param singleId
	 */
	private void getSDList(Integer singleId) {
		List<ReceiveDetail> sdList = receiveDetailManager
				.getReceiveDetailList(singleId);
		getRequest().setAttribute("sd", sdList);
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
	 * 回款单查询条件Map 回款单 出库单
	 * 
	 * @author 王会璞
	 */
	public Map<String, String> getReceiveSalesMap() {
		return SalesConstants.RECEIVE_SALES_MAP;
	}

	/**
	 * 回款单查询条件Map 回款单 出库单 期初应收单
	 * 
	 * @author 王会璞
	 */
	public Map<String, String> getReceiveSalesReceiveinitMap() {
		return SalesConstants.RECEIVE_SALES_RECEIVEInit_MAP;
	}

	/**
	 * 保存回款单
	 */
	@Override
	public String save() {

		String SI = SUCCESS;

		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		try {
			// 如果用户没有输入操作员则设置为空
			if (getModel().getEmployee().getName().trim().equals("")) {
				getModel().setEmployee(null);
			}
			String salesNos[] = this.getRequest().getParameterValues("salesNo");// 出库单单号
			String ckdId[] = this.getRequest().getParameterValues("ckdId");// 出库单id 或
																																			// 期初应收款单id
			String amounts[] = this.getRequest().getParameterValues("amount1");// 现金收款额
			List<ReceiveDetail> rdList = new ArrayList<ReceiveDetail>();
			for (int i = 0; i < salesNos.length; i++) {
				ReceiveDetail receiveDetail = new ReceiveDetail();
				receiveDetail.setId(Integer.parseInt(ckdId[i]));
				if (StringUtils.isNotBlank(amounts[i])
						&& Float.parseFloat(amounts[i]) != 0) {
					receiveDetail.setSpayamount(Double.parseDouble(amounts[i]));
					receiveDetail.setReceive(getModel());
				}
				receiveDetail.setTheCollection(receiveDetail.getSpayamount());
				if (StringUtils.isNotBlank(salesNos[i])) {
					receiveDetail.setHistory(SalesConstants.DOCORS);
				} else {
					receiveDetail.setHistory(SalesConstants.BEGINNING);
				}
				rdList.add(receiveDetail);
			}

			// 保存回款单
			getManager().save(getModel(), rdList);

			if (getModel().getPayment().equals(Payment.CASH)) {
			} else if (getModel().getPayment().equals(Payment.CARD)) {
				SI = "cardSuccess";
			}

			return SI;
		} catch (Exception e) {
			e.printStackTrace();
			addActionError("保存失败");
			return INPUT;
		}
	}

	/**
	 * 将正常状态的出库单生成回款单页面 cash
	 * 
	 * @return
	 */
	public String outKToReSiUI() {
		String customerId = getRequest().getParameter("customerId");
		if (StringUtils.isNotBlank(customerId)) {
			// 得到客户（农户信息）
			getModel().setCustomer(customerManager.get(Integer.valueOf(customerId)));
			// 应收款
			if (StringUtils.isNotBlank(customerId)) {
				// 期初应收款
				this.getRequest().setAttribute("receiveInitList",
						receiveInitManager.getWpzmReceive(Integer.valueOf(customerId)));
				// 未平帐的出库单集合 list
				getWpzmSales(Integer.valueOf(customerId));
			} else {
				// 期初应收款
				this.getRequest()
						.setAttribute(
								"receiveInitList",
								receiveInitManager.getWpzmReceive(getModel().getCustomer()
										.getId()));
				// 未平帐的出库单集合 list
				getWpzmSales(getModel().getCustomer().getId());
			}
		}
		messageRe();
		return "salesReceive";
	}

	/**
	 * 将正常状态的出库单生成回款单页面 card
	 * 
	 * @return
	 */
	public String outCardKToReSiUI() {
		String customerId = getRequest().getParameter("customerId");
		if (StringUtils.isNotBlank(customerId)) {
			// 得到客户（农户信息）
			getModel().setCustomer(customerManager.get(Integer.valueOf(customerId)));
			// 未平帐的出库单集合 list
			if (StringUtils.isNotBlank(customerId)) {
				getCardWpzmSales(Integer.valueOf(customerId));
			} else {
				getCardWpzmSales(getModel().getCustomer().getId());
			}
		}
		messageRe();
		return "cardSalesReceive";
	}

	/**
	 * 生成回款单初始信息
	 */
	private void messageRe() {
		// 生成销售回款单号
		getModel().setReceiveNumber(
				getManager().getReceiveNumber(SalesConstants.REIMBURSEMENT_BILL,
						UserUtil.getPrincipal(getRequest()).getId()));
		// 生成销售回款默认时间
		getModel().setCreateTime(DateUtil.getCurrentDate());
	}

	/**
	 * 把给定的用户下的所有未平的出库单传到页面 cash
	 * 
	 * @param 客户
	 *          （农户） id
	 * @return 所有未平帐的出库单 list
	 */
	public List<Sales> getWpzmSales(Integer customerId) {
		List<Sales> salesList = salesManager.getWpzm(customerId);
		this.getRequest().setAttribute("sd", salesList);
		return salesList;
	}

	/**
	 * 把给定的用户下的所有未平的出库单传到页面 card
	 * 
	 * @param 客户
	 *          （农户） id
	 * @return 所有未平帐的出库单 list
	 */
	public List<Sales> getCardWpzmSales(Integer customerId) {
		List<Sales> salesList = salesManager.getCardWpzm(customerId);
		this.getRequest().setAttribute("sd", salesList);
		return salesList;
	}

	/**
	 * 将回款单冲红
	 * 
	 * @author 王会璞
	 * @return
	 */
	public String redRed() {
		String SI = SUCCESS;
		Receive receive = getManager().redRed(getModel().getId(),
				UserUtil.getPrincipal(getRequest()).getId());
		if (receive.getPayment().equals(Payment.CASH)) {
		} else if (receive.getPayment().equals(Payment.CARD)) {
			SI = "cardSuccess";
		}

		return SI;
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
	 * 显示查询列表 cash 现金
	 */
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		if (user.getBeginningInit().equals("0")) {
			this.addActionError("请先完成初始化，再做业务单据");
		}
		if (SalesConstants.REIMBURSEMENT.equals(receiveSales)
				|| receiveSales == null) {
			query(user, "cash", null);
		} else if (SalesConstants.SALES.equals(receiveSales)) {
			if (StringUtils.isNotBlank(getModel().getReceiveNumber())) {
				return queryReciveDetailSales(user, "cash", null);
			}
		} else if (SalesConstants.BEGINNING.equals(receiveSales)) {
			// if (StringUtils.isNotBlank(getModel().getReceiveNumber())) {
			return queryRdRI(user);
			// }
		}
		return INDEX;
	}

	/**
	 * card 代币卡index
	 * 
	 * @return
	 */
	public String cardIndex() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			if (user.getBeginningInit() != null) {
				if (user.getBeginningInit().equals("0")) {
					this.addActionError("请先完成初始化，再做业务单据");
					beginningInit = false;
				}
			}
			user = userManager.get(user.getId());
			if (user.getBeginningInit() == null) {
				if (user.getSuperior().getBeginningInit().equals("0")) {
					this.addActionError("您的上级经销商还没有完成初始化，所以您现在不能正式使用该平台。");
					beginningInit = false;
				}
			}
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		if (SalesConstants.REIMBURSEMENT.equals(receiveSales)
				|| receiveSales == null) {
			query(user, null, "card");
		} else if (SalesConstants.SALES.equals(receiveSales)) {
			if (StringUtils.isNotBlank(getModel().getReceiveNumber())) {
				return queryReciveDetailSales(user, null, "card");
			}
		}
		return "cardIndex";
	}

	/**
	 * 销售回款汇总
	 * 
	 * @return
	 */
	public String querySummary() {

		try {
			Page page = PageUtil.getPage(getPageNo(), getPageSize());

			// 得到检索条件
			Map<String, Object> param = this.getReceiveSummary();
			page = getManager().pageQuery(page, param.get("sql").toString(),
					((List) param.get("param")).toArray());

			List<ReceiveSummary> rsList = new ArrayList<ReceiveSummary>();

			List listSQL = page.getData();
			for (Object o : listSQL) {
				Object[] objs = (Object[]) o; // o对象是Object数组
				ReceiveSummary receiveSummary = new ReceiveSummary();
				receiveSummary.setCustomerName(objs[0].toString());
				receiveSummary.setMoney(Double.parseDouble(objs[1].toString()));
				rsList.add(receiveSummary);
			}
			page.setData(rsList);

			restorePageData(page);

			// 得到检索条件
			Map<String, Object> paramSum = this.getReceiveSummarySum();
			List list = getManager().query(paramSum.get("sql").toString(),
					((List) paramSum.get("param")).toArray());
			String spayamountSum = "";
			if (list.size() > 0) {
				for (Object o : list) {
					if (o != null) {
						spayamountSum = o.toString();
					}
				}
			}
			getRequest().setAttribute("spayamountSum", spayamountSum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "querySummary";
	}

	private Map<String, Object> getReceiveSummarySum() {
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(
				"select sum(r.spayamount) from Receive r where 1 = 1 ");

		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		if (user.getSuperior() == null) {
			Integer superid = user.getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (r.user.superior.id = ? or r.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		}

		if (null != getModel().getCustomer()) {
			if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and r.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and r.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and r.createTime <= ?");
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

	private Map<String, Object> getReceiveSummary() {
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(
				"select r.customer.name, sum(r.spayamount) as spayamountSum from Receive r where 1 = 1 ");

		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		if (user.getSuperior() == null) {
			Integer superid = user.getId();
			if (superid != null && superid.intValue() > 0) {
				sql.append(" and (r.user.superior.id = ? or r.user.id = ?) ");
				args.add(superid);
				args.add(superid);
			}
		}

		if (null != getModel().getCustomer()) {
			if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and r.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and r.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and r.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		sql.append(" group by r.customer.id order by r.createTime desc, r.receiveNumber desc");
		result.put("sql", sql);
		result.put("param", args);

		return result;
	}

	/**
	 * 查询列表显示
	 * 
	 * @param user
	 *          用户
	 * @param cash
	 *          查询现金
	 * @param card
	 *          查询代币卡
	 */
	private void query(User user, String cash, String card) {

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Receive r where 1 = 1 ");
		List args = new ArrayList();

		if (StringUtils.isNotBlank(cash)) {
			sql.append(" and r.payment in(?,?) ");
			args.add(Payment.CASH);
			args.add(Payment.CASHADVANCE);
		} else if (StringUtils.isNotBlank(card)) {
			sql.append(" and r.payment in(?,?) ");
			args.add(Payment.CARD);
			args.add(Payment.CARDADVANCE);
		}

		if (user.getSuperior() == null) {
			sql.append(" and (r.user.id = ? or r.user.superior.id = ?) ");
			args.add(user.getId());
			args.add(user.getId());
		} else if (user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)) {
			sql.append(" and (r.user.id = ? or r.user.superior.id = ?) ");
			args.add(user.getSuperior().getId());
			args.add(user.getSuperior().getId());
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)){
			sql.append(" and r.user.id = ? ");
			args.add(user.getId());
		}

		if (StringUtils.isNotBlank(getModel().getReceiveNumber())) {
			sql.append(" and r.receiveNumber like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getReceiveNumber()));
		}
		if (null != getModel().getCustomer()) {
			if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and r.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and r.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and r.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (getModel() != null && getModel().getRedRed() != null
				&& getModel().getRedRed() != SalesConstants.ALL) {
			sql.append(" and r.redRed = ?");
			args.add(getModel().getRedRed());
		}

		sql.append(" order by r.createTime desc, r.receiveNumber desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);

		String nsql = "select sum(spayamount) " + sql.toString();
		List list = getManager().query(nsql, args.toArray());
		String total = "";
		if (list.size() > 0) {
			for (Object o : list) {
				if (o != null) {
					total = o.toString();
				}
			}
		}
		getRequest().setAttribute("amountTotal", total);
	}

	/**
	 * 查询回款单明细中与一个出库单关联的回款明细
	 * 
	 * @param user
	 * @param cash
	 *          现金
	 * @param card
	 *          代币卡
	 */
	public String queryReciveDetailSales(User user, String cash, String card) {

		String cashCard = "cashRS";

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from ReceiveDetail rd where 1 = 1 ");
		List args = new ArrayList();

		if (StringUtils.isNotBlank(cash)) {
			sql.append(" and rd.payment in(?,?) ");
			args.add(Payment.CASH);
			args.add(Payment.CASHADVANCE);
		} else if (StringUtils.isNotBlank(card)) {
			sql.append(" and rd.payment in(?,?) ");
			cashCard = "cardRS";
			args.add(Payment.CARD);
			args.add(Payment.CARDADVANCE);
		}

		if(user.getSuperior() == null){
			sql.append(" and (rd.user.id = ? or rd.user.superior.id = ?) ");
			args.add(user.getId());
			args.add(user.getId());
		} else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			sql.append(" and (rd.user.id = ? or rd.user.superior.id = ?) ");
			args.add(user.getSuperior().getId());
			args.add(user.getSuperior().getId());
		} else if (user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)) {
			sql.append(" and rd.user.id = ? ");
			args.add(user.getId());
		}

		if (StringUtils.isNotBlank(getModel().getReceiveNumber())) {
			sql.append(" and rd.sales.salesNo = ? ");
			args.add(getModel().getReceiveNumber());
		}

		if (null != getModel().getCustomer()) {
			if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and rd.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and rd.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and rd.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (getModel() != null && getModel().getRedRed() != null
				&& getModel().getRedRed() != SalesConstants.ALL) {
			sql.append(" and rd.redRed = ?");
			args.add(getModel().getRedRed());
		}

		sql.append(" order by rd.createTime desc");
		page = receiveDetailManager.pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);

		List<ReceiveDetail> rdlist = page.getData();
		float amoutTotal = 0;
		for (ReceiveDetail rd : rdlist) {
			amoutTotal += DoubleFormatUtil.format(rd.getSpayamount());
		}
		getRequest().setAttribute("amountTotal", String.valueOf(amoutTotal));

		return cashCard;
	}

	/**
	 * 查询回款单明细中与一个期初应收关联的回款明细
	 * 
	 * @param user
	 * @param cash
	 *          现金
	 * @param card
	 *          代币卡
	 */
	public String queryRdRI(User user) {

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from ReceiveDetail rd where rd.history = " + SalesConstants.BEGINNING);
		List args = new ArrayList();

		if (user.getSuperior() == null) {
			sql.append(" and (rd.user.id = ? or rd.user.superior.id = ?) ");
			args.add(user.getId());
			args.add(user.getId());
		} else if (user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)) {
			sql.append(" and (rd.user.id = ? or rd.user.superior.id = ?) ");
			args.add(user.getSuperior().getId());
			args.add(user.getSuperior().getId());
		} else if (user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)) {
			sql.append(" and rd.user.id = ? ");
			args.add(user.getId());
		}

		// if (StringUtils.isNotBlank(getModel().getReceiveNumber())) {
		// sql.append(" and rd.sales.salesNo = ? ");
		// args.add(getModel().getReceiveNumber());
		// }

		if (null != getModel().getCustomer()) {
			if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and rd.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and rd.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and rd.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (getModel() != null && getModel().getRedRed() != null
				&& getModel().getRedRed() != SalesConstants.ALL) {
			sql.append(" and rd.redRed = ?");
			args.add(getModel().getRedRed());
		}

		sql.append(" order by rd.createTime desc");
		page = receiveDetailManager.pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);

		List<ReceiveDetail> rdlist = page.getData();
		float amoutTotal = 0;
		for (ReceiveDetail rd : rdlist) {
			amoutTotal += DoubleFormatUtil.format(rd.getSpayamount());
		}
		getRequest().setAttribute("amountTotal", String.valueOf(amoutTotal));

		return "beginning";
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

	public String getReceiveSales() {
		return receiveSales;
	}

	public void setReceiveSales(String receiveSales) {
		this.receiveSales = receiveSales;
	}

	public boolean isBeginningInit() {
		return beginningInit;
	}

	public void setBeginningInit(boolean beginningInit) {
		this.beginningInit = beginningInit;
	}
}