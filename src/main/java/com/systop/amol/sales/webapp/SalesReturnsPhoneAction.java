package com.systop.amol.sales.webapp;

import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.base.storage.service.StorageManager;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.Barcode;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.model.SalesDetail;
import com.systop.amol.sales.service.BarcodeManager;
import com.systop.amol.sales.service.SalesDetailManager;
import com.systop.amol.sales.service.SalesManager;
import com.systop.amol.sales.service.SalesReturnsManager;
import com.systop.amol.sales.utils.Payment;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售退货管理Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SalesReturnsPhoneAction
	extends DefaultCrudAction<Sales, SalesReturnsManager> {

	/** 客户Manager */
	@Resource
	private CustomerManager customerManager;
	/** 仓库Manager */
	@Resource
	private StorageManager storageManager;
	/** 销售退货单详情明细 */
	@Resource
	private SalesDetailManager salesDetailManager;

	/** 条形码Manager */
	@Resource
	private BarcodeManager barcodeManager;

	@Resource
	private SalesManager salesManager;

	@Autowired
	private UserManager userManager;

	/** 销售出库单id */
	private Integer stId;
	/** 销售出库单编号 */
	private String stNo;

	/** 开始时间 */
	private String startDate;

	/** 结束时间 */
	private String endDate;

	/** 本次实退金额 */
	private Double realReturnMoney;

	/** 刷卡金额【出库单】 */
	private Double cardamountCard;

	/** 实收金额【出库单】 */
	private Double spayamountSalesCard;

	/** 代币卡已退金额【出库单】 */
	private Double rttaoSalesCard;

	/** 返回状态 */
	private String result = INPUT;

	/** 退货单集合 */
	private List<Sales> list;

	/**
	 * 查询销售出库单信息 card
	 * 
	 * @return
	 */
	public String cardIndex() {
	query(null, "card");
	return "cardIndex";
	}

	/**
	 * 查询销售出库单信息 cash
	 */
	@Override
	public String index() {
	query("cash", null);
	return INDEX;
	}

	public void query(String cash, String card) {
	Page page = PageUtil.getPage(getPageNo(), getPageSize());
	StringBuffer sql = new StringBuffer("from Sales s where s.status = 2 ");
	List args = new ArrayList();

	User user = UserUtil.getPrincipal(getRequest());
	if (user.getId() != null && user.getId() > 0) {
		sql.append(" and s.user.id = ? ");
		args.add(user.getId());
	}
	// if (user.getBeginningInit() != null) {
	// if (user.getBeginningInit().equals("0")) {
	// this.addActionError("请先完成初始化，再做业务单据");
	// }
	// }
	getModel().setUser(user);

	if (null != getModel().getCustomer()) {
		if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
		sql.append(" and s.customer.name like ?");
		args.add(MatchMode.ANYWHERE
			.toMatchString(getModel().getCustomer().getName()));
		}
		if (StringUtils.isNotBlank(getModel().getCustomer().getPhone())) {
		sql.append(" and s.customer.phone = ?");
		args.add(getModel().getCustomer().getPhone());
		}
	}

	if (SalesConstants.SALES.equals(getModel().getStatus())) {
		if (StringUtils.isNotBlank(getModel().getSalesNo())) {
		sql.append(" and s.single.salesNo like ? ");
		args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
		} else {
		sql.append(" and s.single.salesNo = ? ");
		args.add("-1");
		}
	} else if (SalesConstants.RETURNS.equals(getModel().getStatus())) {
		if (StringUtils.isNotBlank(getModel().getSalesNo())) {
		sql.append(" and s.salesNo like ? ");
		args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
		}
	}

	if (StringUtils.isNotBlank(card)) {
		sql.append(" and s.payment in (?, ?) ");
		args.add(Payment.CARD);
		args.add(Payment.CARDADVANCE);
	} else if (StringUtils.isNotBlank(cash)) {
		sql.append(" and s.payment in (?, ?) ");
		args.add(Payment.CASH);
		args.add(Payment.CASHADVANCE);
	}

	try {
		if (StringUtils.isNotBlank(startDate)) {
		sql.append(" and s.createTime >= ?");
		args.add(DateUtil.firstSecondOfDate(
			DateUtil.convertStringToDate("yyyy-MM-dd", startDate)));
		}
		if (StringUtils.isNotBlank(endDate)) {
		sql.append(" and s.createTime <= ?");
		args.add(DateUtil.lastSecondOfDate(
			DateUtil.convertStringToDate("yyyy-MM-dd", endDate)));
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

	String nsql = "select sum(rttao),sum(realReturnMoney) " + sql.toString();
	List list = getManager().query(nsql, args.toArray());
	double rttao = 0.0d;
	double realReturnMoney = 0.0d;
	if (list.size() > 0) {
		for (Object o : list) {
		Object[] oo = (Object[]) o;
		if (o != null) {
			if (oo[0] != null) {
			rttao = Double.parseDouble(oo[0].toString());
			}
		}
		if (o != null) {
			if (oo[1] != null) {
			realReturnMoney = Double.parseDouble(oo[1].toString());
			}
		}
		}
	}
	getRequest().setAttribute("rttao", rttao);
	getRequest().setAttribute("realReturnMoney", realReturnMoney);
	}

	/**
	 * 新建或者编辑销售退货单信息
	 */
	@Override
	public String edit() {
	setModel(getManager()
		.get(Integer.valueOf(getRequest().getParameter("orderId"))));// 退货的订单
	User user = userManager
		.get(Integer.valueOf(getRequest().getParameter("id")));// 退货用户
	getModel().setUser(user);
	// 生成销售退货单号
	getModel().setSalesNo(
		getManager().getReturnSales(SalesConstants.RETURNS, user.getId()));
	// 时间
	getModel().setCreateTime(DateUtil.getCurrentDate());
	return super.edit();
	}

	/**
	 * <p>
	 * 代币卡 出库单将要生成退货单的预览编辑界面
	 * </p>
	 * 
	 * @return
	 */
	public String stTosREditUI() {
	String stId = getRequest().getParameter("stId");
	Sales sales = getManager().get(Integer.valueOf(stId));
	this.stId = Integer.parseInt(stId);
	this.stNo = sales.getSalesNo();
	this.cardamountCard = sales.getCardamount();
	this.spayamountSalesCard = sales.getSpayamount();
	this.rttaoSalesCard = sales.getRttao();
	setModel(sales);
	// 出库单号
	getModel().setSalesNo(getManager().getReturnSales(SalesConstants.RETURNS,
		UserUtil.getPrincipal(getRequest()).getId()));
	// 时间
	getModel().setCreateTime(DateUtil.getCurrentDate());

	// 得到出库单详情明细
	getSalesDetailReDetails(getModel().getId());
	getModel().setId(null);
	return "stTosRUI";
	}

	/**
	 * <p>
	 * 代币卡 出库单将要生成退货单的预览编辑界面（从退货单页面添加退货单UI）
	 * </p>
	 * 
	 * @return
	 */
	public String cardReturnUI() {
	String stId = getRequest().getParameter("stId");
	if (StringUtils.isNotBlank(stId)) {
		Sales sales = getManager().get(Integer.valueOf(stId));
		this.stId = Integer.parseInt(stId);
		this.stNo = sales.getSalesNo();
		this.cardamountCard = sales.getCardamount();
		this.spayamountSalesCard = sales.getSpayamount();
		this.rttaoSalesCard = sales.getRttao();
		setModel(sales);
		// 得到出库单详情明细
		getSalesDetailReDetails(getModel().getId());
		getModel().setId(null);
	}

	// 出库单号
	getModel().setSalesNo(getManager().getReturnSales(SalesConstants.RETURNS,
		UserUtil.getPrincipal(getRequest()).getId()));
	// 时间
	getModel().setCreateTime(DateUtil.getCurrentDate());

	return "cardReturnUI";
	}

	/**
	 * <p>
	 * 现金 出库单将要生成退货单的预览编辑界面
	 * </p>
	 * 
	 * @return
	 */
	public String stTosREditUICash() {
	String stId = getRequest().getParameter("stId");
	Sales sales = getManager().get(Integer.valueOf(stId));
	this.stId = Integer.parseInt(stId);
	this.stNo = sales.getSalesNo();
	setModel(sales);
	// 出库单号
	getModel().setSalesNo(getManager().getReturnSales(SalesConstants.RETURNS,
		UserUtil.getPrincipal(getRequest()).getId()));
	// 时间
	getModel().setCreateTime(DateUtil.getCurrentDate());
	// 得到出库单详情明细
	getSalesDetailReDetails(getModel().getId());
	getModel().setId(null);
	return "stTosRUICash";
	}

	/**
	 * <p>
	 * 现金 出库单将要生成退货单的预览编辑界面（从退货单页面添加退货单UI）
	 * </p>
	 * 
	 * @return
	 */
	public String cashReturnUI() {
	String stId = getRequest().getParameter("stId");
	if (StringUtils.isNotBlank(stId)) {
		Sales sales = getManager().get(Integer.valueOf(stId));
		this.stId = Integer.parseInt(stId);
		this.stNo = sales.getSalesNo();
		setModel(sales);
		// 得到出库单详情明细
		getSalesDetailReDetails(getModel().getId());
		getModel().setId(null);
	}
	// 出库单号
	getModel().setSalesNo(getManager().getReturnSales(SalesConstants.RETURNS,
		UserUtil.getPrincipal(getRequest()).getId()));
	// 时间
	getModel().setCreateTime(DateUtil.getCurrentDate());
	return "cashReturnUI";
	}

	/**
	 * @author 王会璞 保存 1.销售退货单 2.退货商品信息明细 3.将退货的商品加到出库的仓库中
	 *         4.将数据库中商品条形码中的退货商品条形码删除
	 */
	@Override
	public String save() {
		
		User user = userManager
				.get(Integer.valueOf(getRequest().getParameter("userId")));// 要货退货用户
		Sales salesSQL = getManager()
				.get(Integer.valueOf(getRequest().getParameter("orderId")));// 要退货的订单
		Sales salesClone = (Sales)salesSQL.clone();
		// 生成销售退货单号
		salesClone.setSalesNo(getManager().getReturnSales(SalesConstants.RETURNS, user.getId()));
		// 退货时间
		salesClone.setCreateTime(DateUtil.getCurrentDate());
		
//				User user = UserUtil.getPrincipal(getRequest());
//				getModel().setUser(user);
		salesClone.setStatus(SalesConstants.RETURNS);
//				if (null != getModel().getCustomer()) {
//					if (null != getModel().getCustomer().getId()
//							&& 0 < getModel().getCustomer().getId()) {
//						getModel().setCustomer(
//								customerManager.get(getModel().getCustomer().getId()));
//					} else {
//						getModel().setCustomer(null);
//					}
//				}
				// 如果用户没有选择销售员则设置为空
//				if (getModel().getEmployee().getName().trim().equals("")) {
//					getModel().setEmployee(null);
//				}

				// 保存退货单明细
//				String[] pids = this.getRequest().getParameterValues("pid");
//				String[] unitids = this.getRequest().getParameterValues("unitid");
//				String[] outprices = this.getRequest().getParameterValues("outprice");
//				String[] ncounts = this.getRequest().getParameterValues("ncount");
//				String[] counts = this.getRequest().getParameterValues("counts");
//				String[] moneys = this.getRequest().getParameterValues("money");
//				String[] remarks = this.getRequest().getParameterValues("sremark");
//				String[] codes = this.getRequest().getParameterValues("codes");
//				String[] osdis = null;
//				if (stId != null && stId > 0) {
//					osdis = this.getRequest().getParameterValues("OORSalesDetailId");
//				}

				// 退货商品总数量
				double count = 0;
				// 退货单详情明细
				List<SalesDetail> sdlistSQL = salesDetailManager.getDetails(salesSQL.getId());
				List<SalesDetail> sdlist = new ArrayList<SalesDetail>();//退货单详情集合
				SalesDetail salesDetailClone;//退货单详情（克隆订单详情）
				for (SalesDetail salesDetail : sdlistSQL) {
					salesDetailClone = (SalesDetail)salesDetail.clone();
					salesDetailClone.setId(null);
					count += salesDetail.getCount().intValue();// 累加所有退货商品
					salesDetailClone.setOORSalesDetailId(salesDetail);
					salesDetailClone.setSales(salesClone);
					salesDetailClone.setHanod(salesDetail.getCount());//退货商品数量
					salesDetailClone.setRttao(salesDetail.getAmount());//退货金额
					sdlist.add(salesDetailClone);
				}

				// 退货商品总数量
				salesClone.setCount(count);

				salesClone.setSingle(salesSQL);
				salesClone.setCardGrant(salesSQL.getCardGrant());
				salesClone.setStorage(salesSQL.getStorage());

				// 本次退货金额
//				String rttaoThe = getRequest().getParameter("rttaoThe");
//				if (StringUtils.isNotBlank(rttaoThe)) {
//					getModel().setRttao(Double.parseDouble(rttaoThe));
//				}
				salesClone.setRttao(salesSQL.getSamount());

//				if (realReturnMoney != null) {
//					getModel().setRealReturnMoney(realReturnMoney);
//				}
				salesClone.setRealReturnMoney(salesSQL.getSpayamount());

				salesClone.setId(null);
				
				System.out.println("--------------->>"+salesClone);
				System.out.println(sdlist);
//				getManager().saveStorehouse(salesClone, sdlist);

				// 退货单明细
//				getRequest().setAttribute("sd", sdlist);
//				if (Payment.CASH.equals(getModel().getPayment())
//						|| Payment.CASHADVANCE.equals(getModel().getPayment())) {
//					SI = "isCashPrint";
//				}

				return SUCCESS;
	}

	/**
	 * 销售退货单详情查看
	 */
	@Override
	public String view() {
	super.view();
	getSalesDetail(getModel().getId());
	return VIEW;
	}

	/**
	 * 销售退货单详情查看 Cash
	 */
	public String viewCash() {
	view();
	return "viewCash";
	}

	/**
	 * 把单子明细传到页面
	 */
	public List<SalesDetail> getSalesDetail(Integer id) {
	List<SalesDetail> sd = salesDetailManager.getDetails(id);
	this.getRequest().setAttribute("sd", sd);
	for (SalesDetail salesDetail : sd) {
		StringBuffer codes = new StringBuffer();
		List<Barcode> barcodeList = barcodeManager
			.getBarcodeList(salesDetail.getId());
		for (Barcode barcode : barcodeList) {
		codes.append(barcode.getBarcode() + ',');
		}
		salesDetail.setCodes(codes.toString());
	}
	return sd;
	}

	/**
	 * 把单子明细传到页面（退货）
	 */
	public List<SalesDetail> getSalesDetailReDetails(Integer id) {
	List<SalesDetail> sd = salesDetailManager.getDetailsReturn(id);
	this.getRequest().setAttribute("sd", sd);
	for (SalesDetail salesDetail : sd) {
		StringBuffer codes = new StringBuffer();
		List<Barcode> barcodeList = barcodeManager
			.getBarcodeList(salesDetail.getId());
		for (Barcode barcode : barcodeList) {
		codes.append(barcode.getBarcode() + ',');
		}
		salesDetail.setCodes(codes.toString());
	}
	return sd;
	}

	public Integer getStId() {
	return stId;
	}

	public void setStId(Integer stId) {
	this.stId = stId;
	}

	public String getStNo() {
	return stNo;
	}

	public void setStNo(String stNo) {
	this.stNo = stNo;
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

	public Double getRealReturnMoney() {
	return realReturnMoney;
	}

	public void setRealReturnMoney(Double realReturnMoney) {
	this.realReturnMoney = realReturnMoney;
	}

	public Double getCardamountCard() {
	return cardamountCard;
	}

	public void setCardamountCard(Double cardamountCard) {
	this.cardamountCard = cardamountCard;
	}

	public Double getSpayamountSalesCard() {
	return spayamountSalesCard;
	}

	public void setSpayamountSalesCard(Double spayamountSalesCard) {
	this.spayamountSalesCard = spayamountSalesCard;
	}

	public Double getRttaoSalesCard() {
	return rttaoSalesCard;
	}

	public void setRttaoSalesCard(Double rttaoSalesCard) {
	this.rttaoSalesCard = rttaoSalesCard;
	}

	public String getResult() {
	return result;
	}

	public void setResult(String result) {
	this.result = result;
	}

	public List<Sales> getList() {
	return list;
	}

	public void setList(List<Sales> list) {
	this.list = list;
	}
}