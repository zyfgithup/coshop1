package com.systop.amol.sales.webapp;

import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.base.jfproduct.model.JfPrinceple;
import com.systop.amol.base.jfproduct.service.JfPrincepleManager;
import com.systop.amol.base.product.ProductConstants;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.product.service.ProductsManager;
import com.systop.amol.base.storage.service.StorageManager;
import com.systop.amol.base.units.model.Units;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.Barcode;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.model.SalesDetail;
import com.systop.amol.sales.service.BarcodeManager;
import com.systop.amol.sales.service.SalesDetailManager;
import com.systop.amol.sales.service.SalesManager;
import com.systop.amol.sales.service.SalesReturnsManager;
import com.systop.amol.sales.utils.MerchantIsRutrn;
import com.systop.amol.sales.utils.Payment;
import com.systop.amol.user.agent.model.Fxgz;
import com.systop.amol.user.agent.service.FxgzManager;
import com.systop.amol.user.agent.service.PartnersManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
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
import java.util.List;
import java.util.Map;

/**
 * 销售退货管理ActionsalesReturns
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SalesReturnsAction extends DefaultCrudAction<Sales, SalesReturnsManager> {

	/** 客户Manager */
	@Resource
	private CustomerManager customerManager;
	/** 仓库Manager */
	@Resource
	private StorageManager storageManager;
	/** 销售退货单详情明细 */
	@Resource
	private SalesDetailManager salesDetailManager;
	@Resource
	FxgzManager fxgzManager;
	@Resource
	PartnersManager partnersManager;
	@Resource
	ProductsManager productsManager;
	/** 条形码Manager */
	@Resource
	private BarcodeManager barcodeManager;

	@Resource
	private UserManager userManager;

	@Resource
	private SalesManager salesManager;
	@Resource
	private JfPrincepleManager jfPrincepleManager;

	/** 销售出库单id */
	private Integer stId;
	/** 销售出库单编号 */
	private String stNo;

	private String salesType;
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

	private String name;

	private String flag;
	/** 商户名称 */
	private String shname;

	/**
	 * excel报表导出 代币卡
	 * 
	 * @return
	 */
	public String exportExcel() {
		this.edit();
		return "exportExcel";
	}

	/**
	 * excel报表导出 现金
	 * 
	 * @return
	 */
	public String exportExcelCash() {
		this.edit();
		return "exportExcelCash";
	}

	/**
	 * @author 王会璞
	 *         <p>
	 *         冲红退货单
	 *         </p>
	 * @return
	 */
	public String redRed() {
		String SI = SUCCESS;
		try {
			Sales salesReturn = getManager().redRed(getModel().getId(), UserUtil.getPrincipal(getRequest()).getId());
			if (salesReturn.getPayment().equals(Payment.CASH) || salesReturn.getPayment().equals(Payment.CASHADVANCE)) {
			} else if (salesReturn.getPayment().equals(Payment.CARD)
					|| salesReturn.getPayment().equals(Payment.CARDADVANCE)) {
				SI = "cardSuccess";
			}
		} catch (Exception e) {
			e.printStackTrace();
			addActionError(e.getMessage());
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
	 * 商家同意给用户退货、退款
	 * @return
	 */
	public void shangJiaTytt(){
		// 退货单对象
		Sales returnSales = salesManager.get(getModel().getId());
		returnSales.setMerchantIsRutrn(MerchantIsRutrn.MERCHANT_YES_TUI_HUO);;
		getManager().update(returnSales);
		cardIndex();
	}

	/**
	 * 确认退货，平台操作
	 * @return
	 */
	public String sure() {
		// 退货单对象
		Sales sales = salesManager.get(getModel().getId());
		sales.setCkzt("1");
		salesManager.update(sales);
		// 获取退货单详细信息用户更新订单里面的数据
		List<SalesDetail> sds = salesDetailManager.getDetails(sales.getId());
		// 获取与退货单对应的订单对象
		Sales returnSales = salesManager.findSales(sales.getSalesNo());
		// 退单原订单先清除分销信息
		User app = sales.getUser();
		Fxgz fxgz = fxgzManager.getFxgzByMerId(returnSales.getMerId(), returnSales.getSamount());
		if (fxgz == null) {
			Integer nums = fxgzManager.getMaxFxgzByMerId(returnSales.getMerId());
			if (returnSales.getSamount() > nums) {
				fxgz = fxgzManager.getMaxFxgzByEndNums(Double.valueOf(nums), returnSales.getMerId());
			}
		}
		salesManager.getSalesOrderManager().updateOrderOrUserOrFx(returnSales, app, fxgz, SalesConstants.fxReturn);
		// 退单清除分销信息后原订单修改成最新的状态（金额，商品数，退款金额等信息）
		Sales nowSales = salesManager.findSales(sales.getSalesNo());
		nowSales.setSpayamount(nowSales.getSpayamount() - sales.getRttao());
		nowSales.setSamount(nowSales.getSamount() - sales.getRttao());
		if (nowSales.getRttao() == null) {
			nowSales.setRttao(sales.getRttao());
		} else {
			nowSales.setRttao(sales.getRttao() + nowSales.getRttao());
		}
		nowSales.setTtno(nowSales.getCount() - sales.getCount());
		if (nowSales.getRealReturnMoney() == null) {
			nowSales.setRealReturnMoney(sales.getRttao());
		} else {
			nowSales.setRealReturnMoney(sales.getRttao() + nowSales.getRttao());
		}
		Fxgz fxgznow = fxgzManager.getFxgzByMerId(nowSales.getMerId(), nowSales.getSamount());
		if (fxgznow == null) {
			Integer nums = fxgzManager.getMaxFxgzByMerId(nowSales.getMerId());
			if (nowSales.getSamount() > nums) {
				fxgznow = fxgzManager.getMaxFxgzByEndNums(Double.valueOf(nums), returnSales.getMerId());
			}
		}
		// 最新状态订单走分销
		salesManager.getSalesOrderManager().updateOrderOrUserOrFx(nowSales, app, fxgznow, SalesConstants.fxNormal);
		Sales ddSales = salesManager.findSales(sales.getSalesNo());
		// 便利退货单详情
		double rttao = 0;
		User merUser = null;
		for (SalesDetail sd : sds) {
			// 根据订单id和退货商品id 得到 订单中相应商品的对象更新数据
			merUser = userManager.get(sd.getProducts().getUser().getId());
			SalesDetail dsd = salesDetailManager.findDetail(ddSales.getId(), sd.getProducts().getId());
			Products pd = sd.getProducts();
			pd.setMaxCount(pd.getMaxCount() + sd.getCount());
			dsd.setHanod(sd.getHanod());
			sd.setOutPrice(dsd.getOutPrice());
			dsd.setTnorootl(dsd.getCount() - sd.getHanod());
			dsd.setHanod(sd.getHanod());
			dsd.setRttao(sd.getHanod() * dsd.getOutPrice());
			sd.setRttao(sd.getHanod() * dsd.getOutPrice());
			dsd.setAmount(dsd.getAmount() - sd.getHanod() * dsd.getOutPrice());
			rttao += dsd.getAmount() - sd.getHanod() * dsd.getOutPrice();
			productsManager.update(pd);
			salesDetailManager.update(dsd);
			salesDetailManager.update(sd);
		}
		// app用户user对象
		// int minusJf=sales.getRttao().intValue();
		// app.setIntegral(app.getIntegral()-minusJf);
		// Double flMoney=0.0;
		// double integal=0;
		// if(null!=fxgz){
		// flMoney=fxgz.getFlMoney();
		// integal=fxgz.getIntegal();
		// }
		// Fxgz
		// fxgz1=fxgzManager.getFxgzByMerId(merUser.getId(),ddSales.getSpayamount()-sales.getRttao());
		// Double flMoney1=0.0;
		// double integal1=0;
		// if(null!=fxgz1){
		// flMoney1=fxgz1.getFlMoney();
		// integal1=fxgz1.getIntegal();
		// }
		// User user=ddSales.getUser();
		// User
		// zjUser=partnersManager.getPartnersByPartnersId(user.getId(),"1");
		// Partners zjPartners=partnersManager.getPartnersById(user.getId(),
		// "1");
		// if(null!=zjPartners){
		// if(null!=zjPartners.getBringMoney()){
		// zjPartners.setBringMoney(zjPartners.getBringMoney()-flMoney+flMoney1);
		// }else{
		// zjPartners.setBringMoney(0.0);
		// }
		// if(null!=zjUser){
		// Double zjAllMoney=zjUser.getAllMoney();
		// Double zjInComeAll=zjUser.getIncomeAll();
		// if(null!=zjAllMoney){
		// zjUser.setAllMoney(zjUser.getAllMoney()-flMoney+flMoney1);
		// }else{
		// zjUser.setAllMoney(0.0);
		// }
		// if(null!=zjInComeAll){
		// zjUser.setIncomeAll(zjUser.getIncomeAll()-flMoney+flMoney1);
		// }else{
		// zjUser.setIncomeAll(0.0);
		// }
		// }
		// userManager.update(zjUser);
		// partnersManager.update(zjPartners);
		// }
		// User
		// jjUser=partnersManager.getPartnersByPartnersId(user.getId(),"0");
		// Partners jjPartners=partnersManager.getPartnersById(user.getId(),
		// "0");
		// if(null!=jjPartners){
		// if(null!=jjPartners.getBringMoney()){
		// jjPartners.setBringMoney(jjPartners.getBringJf()-integal+integal1);
		// }else{
		// jjPartners.setBringMoney(0.0);
		// }
		// if(null!=jjUser){
		// //积分返利
		// Double jjMoney=jjUser.getAllMoney();
		// if(null!=jjMoney){
		// jjUser.setAllMoney(jjUser.getIntegral()-integal+integal1);
		// }
		// else{jjUser.setAllMoney(0.0);}}
		// userManager.update(jjUser);
		// partnersManager.update(jjPartners);
		// }
		// userManager.update(app);
		if (flag.equals("cash")) {
			return "success";
		}
		if (flag.equals("card")) {
			return "cardSuccess";
		}
		return null;
	}

	public String cancel() {
		Sales sales = salesManager.get(getModel().getId());
		sales.setCkzt("2");
		salesManager.update(sales);
		if (flag.equals("cash")) {
			return "success";
		}
		if (flag.equals("card")) {
			return "cardSuccess";
		}
		return null;
	}
	
	/**
	 * 平台查询退货单
	 */
	public String returnSalseManagerIndex() {

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from Sales s where s.ckzt!='2' and  s.status =  " + SalesConstants.RETURNS + " and s.merchantIsRutrn = '"+MerchantIsRutrn.MERCHANT_YES_TUI_HUO.toString()+"'");
		List args = new ArrayList();

		if (StringUtils.isNotBlank(shname)) {
			sql.append(" and s.merUser.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(shname));
		}
		if (StringUtils.isNotBlank(stNo)) {
			sql.append(" and s.salesNo like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(stNo));
		}
		if (StringUtils.isNotBlank(name)) {
			sql.append(" and s.user.loginId like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(name));
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

		
		sql.append(" and (s.payment=?  or s.payment=?) ");
		args.add(Payment.ALIPAY);
		args.add(Payment.WXPAY);

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and s.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and s.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
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
		
		return "return_salse_manager_index";
	}

	/**
	 * 商家查询退货单
	 */
	public String cardIndex() {

		System.out.println("ddddddddd");

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from Sales s where s.ckzt!='2' and  s.status =  " + SalesConstants.RETURNS);
		List args = new ArrayList();
		User user = UserUtil.getPrincipal(getRequest());
		user = userManager.get(user.getId());
		if (null != user.getId()) {
			sql.append(" and s.merUser.id = ? ");
			args.add(user.getId());
		}
		if (StringUtils.isNotBlank(shname)) {
			sql.append(" and s.merUser.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(shname));
		}
		if (StringUtils.isNotBlank(stNo)) {
			sql.append(" and s.salesNo like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(stNo));
		}
		if (StringUtils.isNotBlank(name)) {
			sql.append(" and s.user.loginId like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(name));
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

		
		sql.append(" and (s.payment=?  or s.payment=?) ");
		args.add(Payment.ALIPAY);
		args.add(Payment.WXPAY);

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and s.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and s.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		sql.append(" order by s.createTime desc, s.salesNo desc");

		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);

		System.out.println("sql = =============== " + sql);

		String nsql = "select sum(rttao),sum(realReturnMoney) " + sql.toString();
		System.out.println("sql =------------------ "+sql);
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
		
		return "cardIndex";
	}

	/**
	 * 新建或者编辑销售退货单信息
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
			getRequest().setAttribute("status", getBillStatusMap().get(getModel().getStatus()));
		} else {
			// 生成销售退货单号
			getModel().setSalesNo(getManager().getReturnSales(SalesConstants.RETURNS, user.getId()));
			// 时间
			getModel().setCreateTime(DateUtil.getCurrentDate());
		}
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
		getModel().setSalesNo(
				getManager().getReturnSales(SalesConstants.RETURNS, UserUtil.getPrincipal(getRequest()).getId()));
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
		getModel().setSalesNo(
				getManager().getReturnSales(SalesConstants.RETURNS, UserUtil.getPrincipal(getRequest()).getId()));
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
		getModel().setSalesNo(
				getManager().getReturnSales(SalesConstants.RETURNS, UserUtil.getPrincipal(getRequest()).getId()));
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
		getModel().setSalesNo(
				getManager().getReturnSales(SalesConstants.RETURNS, UserUtil.getPrincipal(getRequest()).getId()));
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
		String SI = "isPrint";
		try {
			User user = UserUtil.getPrincipal(getRequest());
			getModel().setUser(user);
			getModel().setStatus(SalesConstants.RETURNS);
			if (null != getModel().getCustomer()) {
				if (null != getModel().getCustomer().getId() && 0 < getModel().getCustomer().getId()) {
					getModel().setCustomer(customerManager.get(getModel().getCustomer().getId()));
				} else {
					getModel().setCustomer(null);
				}
			}
			// 如果用户没有选择销售员则设置为空
			if (getModel().getEmployee().getName().trim().equals("")) {
				getModel().setEmployee(null);
			}
			// 保存退货单明细
			String[] pids = this.getRequest().getParameterValues("pid");
			String[] unitids = this.getRequest().getParameterValues("unitid");
			String[] outprices = this.getRequest().getParameterValues("outprice");
			String[] ncounts = this.getRequest().getParameterValues("ncount");
			String[] counts = this.getRequest().getParameterValues("counts");
			String[] moneys = this.getRequest().getParameterValues("money");
			String[] remarks = this.getRequest().getParameterValues("sremark");
			String[] codes = this.getRequest().getParameterValues("codes");
			String[] osdis = null;
			if (stId != null && stId > 0) {
				osdis = this.getRequest().getParameterValues("OORSalesDetailId");
			}

			// 退货商品总数量
			double count = 0;
			// 退货单详情明细
			List<SalesDetail> sdlist = new ArrayList<SalesDetail>();
			// 商品条形码集合
			List<Object> objectList = new ArrayList<Object>();
			for (int i = 1; i < pids.length; i++) {

				SalesDetail sd = new SalesDetail();
				sd.setAmount(Double.parseDouble(moneys[i]));
				sd.setOutPrice(Double.parseDouble(outprices[i]));
				sd.setNcount(Float.parseFloat(ncounts[i]));
				sd.setCount(Integer.parseInt(counts[i]));
				count += sd.getCount().intValue();// 累加所有退货商品
				sd.setProducts(this.getManager().getDao().get(Products.class, Integer.parseInt(pids[i])));
				sd.setRemark(remarks[i]);
				if (osdis != null) {
					if (StringUtils.isNotBlank(osdis[i])) {
						SalesDetail detail = new SalesDetail();
						detail.setId(Integer.parseInt(osdis[i]));
						sd.setOORSalesDetailId(detail);
					}
				}
				String[] uids = unitids[i].split(",");
				sd.setUnits(this.getManager().getDao().get(Units.class, Integer.parseInt(uids[0])));
				sd.setSales(getModel());

				// 商品条形码集合
				List<Barcode> barcodes = new ArrayList<Barcode>();
				String code = codes[i];
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
					throw new ApplicationException("商品退货时必须对其进行扫码！");
				}

				// 将商品条码保存到一个集合中
				objectList.add(barcodes);

				// 将商品记录与添加到一个集合中
				sdlist.add(sd);
			}

			// 退货商品总数量
			getModel().setCount(count);

			if (stId != null && stId > 0) {
				Sales salesSQL = salesManager.get(stId);
				getModel().setSingle(salesSQL);
				getModel().setCardGrant(salesSQL.getCardGrant());
				getModel().setStorage(salesSQL.getStorage());
			}

			// 本次退货金额
			String rttaoThe = getRequest().getParameter("rttaoThe");
			if (StringUtils.isNotBlank(rttaoThe)) {
				getModel().setRttao(Double.parseDouble(rttaoThe));
			}

			if (realReturnMoney != null) {
				getModel().setRealReturnMoney(realReturnMoney);
			}

//			getManager().saveStorehouse(getModel(), sdlist, objectList);

			// 退货单明细
			getRequest().setAttribute("sd", sdlist);
			if (Payment.CASH.equals(getModel().getPayment()) || Payment.CASHADVANCE.equals(getModel().getPayment())) {
				SI = "isCashPrint";
			}

			return SI;
		} catch (ApplicationException e) {
			addActionError(e.getMessage());
			Integer id = getModel().getSingle().getId();
			// 得到出库单详情明细
			if (id != null) {
				// 得到订单商品详情明细
				List<SalesDetail> list = getSalesDetail(id);
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
			}
			if (Payment.CARD.equals(getModel().getPayment()) || Payment.CARDADVANCE.equals(getModel().getPayment())) {
				return INPUT;
			} else {
				return "inputCash";
			}
		}
	}

	/**
	 * 销售退货单详情查看
	 */
	@Override
	public String view() {
		super.view();
		getRequest().setAttribute("status", getBillStatusMap().get(getModel().getStatus()));
		getSalesDetail(getModel().getId());
		return VIEW;
	}

	public String groupCardIndex() {
		groupQuery(null, "card");
		return "groupCardIndex";
	}

	public void groupQuery(String cash, String card) {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Sales s where s.status =  " + SalesConstants.RETURNS);
		List args = new ArrayList();
		User user = UserUtil.getPrincipal(getRequest());
		user = userManager.get(user.getId());
		if (null != user.getRegion()) {
			sql.append(" and s.user.region.code like ? ");
			args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
		}
		// User user = UserUtil.getPrincipal(getRequest());
		// if (user.getId() != null && user.getId() > 0) {
		// sql.append(" and s.user.id = ? ");
		// args.add(user.getId());
		// }
		// if (user.getBeginningInit() != null) {
		// if (user.getBeginningInit().equals("0")) {
		// this.addActionError("请先完成初始化，再做业务单据");
		// }
		// }
		// getModel().setUser(user);
		if (StringUtils.isNotBlank(name)) {
			sql.append(" and s.user.loginId like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(name));
		}
		if (StringUtils.isNotBlank(salesType)) {
			sql.append(" and s.salesType=?");
			args.add(salesType);
		}
		if (StringUtils.isNotBlank(getModel().getSalesType())) {
			System.out.println("-----------" + getModel().getSalesType());
			sql.append(" and s.salesType = ? ");
			args.add(getModel().getSalesType());
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
			sql.append(" and s.payment=? ");
			args.add(Payment.ALIPAY);
		} else if (StringUtils.isNotBlank(cash)) {
			sql.append(" and s.payment =? ");
			args.add(Payment.CASH);
		}
		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and s.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and s.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// if (getModel() != null && getModel().getRedRed() != null
		// && getModel().getRedRed() != SalesConstants.ALL) {
		// sql.append(" and s.redRed = ?");
		// args.add(getModel().getRedRed());
		// }

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
	 * 销售退货单详情查看 Cash
	 */
	public String viewCash() {
		view();
		return "viewCash";
	}

	/**
	 * 打印 代币卡
	 * 
	 * @return
	 */
	public String print() {
		view();
		return "print";
	}

	/**
	 * 打印 现金
	 * 
	 * @return
	 */
	public String printCash() {
		view();
		return "cashPrint";
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
	 * 把单子明细传到页面
	 */
	public List<SalesDetail> getSalesDetail(Integer id) {
		List<SalesDetail> sd = salesDetailManager.getDetails(id);
		this.getRequest().setAttribute("sd", sd);
		for (SalesDetail salesDetail : sd) {
			StringBuffer codes = new StringBuffer();
			List<Barcode> barcodeList = barcodeManager.getBarcodeList(salesDetail.getId());
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
			List<Barcode> barcodeList = barcodeManager.getBarcodeList(salesDetail.getId());
			for (Barcode barcode : barcodeList) {
				codes.append(barcode.getBarcode() + ',');
			}
			salesDetail.setCodes(codes.toString());
		}
		return sd;
	}

	/**
	 * 退款方式Map
	 */
	public Map<String, String> getRefundMap() {
		return Payment.REFUND_MAP;
	}

	/**
	 * 退货单 出库单Map
	 * 
	 * @author 王会璞
	 */
	public Map<String, String> getReturnsSalesMap() {
		return SalesConstants.RETURNS_SALES_MAP;
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

	public void setSalesType(String salesType) {
		this.salesType = salesType;
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

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getShname() {
		return shname;
	}

	public void setShname(String shname) {
		this.shname = shname;
	}

	public String groupSure() {
		// 退货单对象
		Sales sales = salesManager.get(getModel().getId());
		sales.setCkzt("1");
		salesManager.update(sales);
		// 获取退货单详细信息用户更新订单里面的数据
		List<SalesDetail> sds = salesDetailManager.getDetails(sales.getId());
		// 获取与退货单对应的订单对象
		Sales ddSales = salesManager.findSales(sales.getSalesNo());
		// 便利退货单详情
		double rttao = 0;
		for (SalesDetail sd : sds) {
			// 根据订单id和退货商品id 得到 订单中相应商品的对象更新数据
			SalesDetail dsd = salesDetailManager.findDetail(ddSales.getId(), sd.getProducts().getId());
			dsd.setHanod(sd.getHanod());
			sd.setOutPrice(dsd.getOutPrice());
			dsd.setTnorootl(dsd.getCount() - sd.getHanod());
			dsd.setHanod(sd.getHanod());
			dsd.setRttao(sd.getHanod() * dsd.getOutPrice());
			sd.setRttao(sd.getHanod() * dsd.getOutPrice());
			dsd.setAmount(dsd.getAmount() - sd.getHanod() * dsd.getOutPrice());
			rttao += dsd.getAmount() - sd.getHanod() * dsd.getOutPrice();
			salesDetailManager.update(dsd);
			salesDetailManager.update(sd);
		}
		ddSales.setRttao(rttao);
		ddSales.setSpayamount(ddSales.getSpayamount() - sales.getRttao());
		System.out.println("订单中货物的数量" + ddSales.getCount() + "---退货数量---->" + sales.getCount());
		ddSales.setTtno(ddSales.getCount() - sales.getCount());
		if (ddSales.getRttao() == null) {
			ddSales.setRttao(sales.getRttao());
		} else {
			ddSales.setRttao(sales.getRttao() + ddSales.getRttao());
		}
		if (ddSales.getRealReturnMoney() == null) {
			ddSales.setRealReturnMoney(sales.getRttao());
		} else {
			ddSales.setRealReturnMoney(sales.getRttao() + ddSales.getRttao());
		}
		salesManager.update(ddSales);
		// app用户user对象
		User app = sales.getUser();
		// 村级分销点对象金额减少
		User user = userManager.findUser(app.getRegion().getCode());
		// 县级user对象
		User xjUser = userManager.findUser(user.getRegion().getParent().getParent().getCode());
		// 根据村级分销账户id获得积分兑换规则
		JfPrinceple prin = jfPrincepleManager.findPrinceple(user.getId());
		// 每一个积分对应的金额数
		int minusJf = 0;
		if (null != prin) {
			//minusJf = (int) ((sales.getRttao() * prin.getMoney()) / prin.getJfNum());
		} else {
			minusJf = sales.getRttao().intValue();
		}
		// 需要给用户减去的积分
		app.setIntegral(app.getIntegral() - minusJf);
		// 给村级分销商减去积分
		user.setIntegral(user.getIntegral() - minusJf);
		if (ddSales.getPayment().equals(Payment.ALIPAY)) {
			for (SalesDetail sd : sds) {
				// 如果是自营商品（只减去村的income 和 allmoney）
				if (sd.getProducts().getBelonging() == ProductConstants.MERCHANT) {
					user.setIncomeAll(user.getIncomeAll() - sd.getAmount());
					user.setAllMoney(user.getAllMoney() - sd.getAmount());
				}
				// 如果是平台普通商品则需要减去 县级 和村级 的佣金
				else if (sd.getProducts().getProductType() == ProductConstants.ORDINARY
						&& sd.getProducts().getBelonging() == ProductConstants.PLATFORM) {
					// 减去村级的佣金
					System.out.println("----------user" + user);
					// 支付宝退货减村佣金
					user.setAllMoney(user.getAllMoney() - sd.getCount() * sd.getVillageDistributionCommission());
					user.setIncomeAll(user.getIncomeAll() - sd.getCount() * sd.getVillageDistributionCommission());
					System.out.println("---------sd----------" + sd + "===" + sd.getProducts());
					// System.out.println("----------sd.getProducts()----------"+sd);
					// 支付宝退货减县佣金
					xjUser.setAllMoney(xjUser.getAllMoney() - sd.getCount() * sd.getDistributionCommission());
					xjUser.setIncomeAll(xjUser.getIncomeAll() - sd.getCount() * sd.getDistributionCommission());
				} else if (sd.getProducts().getProductType() == ProductConstants.GROUP_PURCHASE
						&& sd.getProducts().getBelonging() == ProductConstants.PLATFORM) {
					xjUser.setAllMoney(xjUser.getAllMoney() - sd.getRoyaltyRateCounty() * sd.getAmount() / 100);
				} else {
				}
			}
			userManager.update(xjUser);
		}
		userManager.update(user);
		userManager.update(app);
		if (flag.equals("cash")) {
			return "success";
		}
		if (flag.equals("card")) {
			return "groupCardSuccess";
		}
		return null;
	}

	public String groupCancel() {
		Sales sales = salesManager.get(getModel().getId());
		sales.setCkzt("2");
		salesManager.update(sales);
		if (flag.equals("cash")) {
			return "success";
		}
		if (flag.equals("card")) {
			return "groupCardSuccess";
		}
		return null;
	}
}