package com.systop.amol.sales.webapp;

import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.base.express.model.ExpressCompany;
import com.systop.amol.base.express.service.ExpressCompanyManager;
import com.systop.amol.base.product.ProductConstants;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.GroupSum;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.model.SalesDetail;
import com.systop.amol.sales.model.SumOfUserPayment;
import com.systop.amol.sales.service.SalesDetailManager;
import com.systop.amol.sales.service.SalesManager;
import com.systop.amol.sales.service.SalesOrderManager;
import com.systop.amol.sales.utils.Payment;
import com.systop.amol.user.AmolUserConstants;
import com.systop.amol.user.agent.model.ReceiveAddress;
import com.systop.amol.user.agent.model.RenZheng;
import com.systop.amol.user.agent.model.TouBaoRen;
import com.systop.amol.user.agent.phone.PhoneAgentAction;
import com.systop.amol.user.agent.service.ReceiveAddressManager;
import com.systop.amol.user.agent.service.RenZhengManager;
import com.systop.amol.user.agent.service.TouBaoRenManager;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.UserConstants;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售订单管理Action
 *
 * @author 王会璞
 *
 */
@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SalesOrderAction extends
        DefaultCrudAction<Sales, SalesOrderManager> {

    @Resource
    private CustomerManager customerManager;
    @Autowired
    private TouBaoRenManager touBaoRenManager;
    @Resource
    JdbcTemplate jdbcTemplate;
    @Autowired
    private SalesManager salesManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ExpressCompanyManager expressCompanyManager;

    private ProductSortManager productSortManager;

    @Resource
    private SalesDetailManager salesDetailManager;
    @Resource
    private ReceiveAddressManager receiveAddressManager;
    @Resource
    private RegionManager regionManager;
    @Resource
    private RenZhengManager renZhengManager;

    @Resource
    private ExpressCompanyManager companyManager;

    /** 开始时间 */
    private String startDate;

    /** 结束时间 */
    private String endDate;
    /** 地区id */
    private Integer regionId;
    /** 支付方式 */
    private static String payment;

    private String  name;

    private String mobile;

    /** 地区名称 */
    private String regionNameCun;

    private String flag;

    private String pment;

    private Integer testNumber;

    private String salesType;

    public String test(){

    	System.out.println(testNumber+"------------>"+getRequest().getParameter("testNumber"));

    	return "en";
    }

    public String testUI(){

    	testNumber = 7;

    	return "test";
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
    public String updateOstate(){
        Sales order=getManager().get(getModel().getId());
        if(null!=flag&&flag.equals("pay")){order.setoState("1");}
        if(null!=flag&&flag.equals("send")){order.setoState("2");}
        getManager().update(order);
        payment=Payment.ALIPAY.toString();
        return SUCCESS;
    }
    public String updategState(){
        Sales order=getManager().get(getModel().getId());
        if(null!=flag&&flag.equals("pay")){order.setoState("1");}
        if(null!=flag&&flag.equals("send")){order.setoState("2");}
        getManager().update(order);
        payment=Payment.ALIPAY.toString();
        return "tgSuc";
    }
    /**
     * 删除订单信息
     */
    public String plRemove(){
        String strs = getRequest().getParameter("strs");
        String type = getRequest().getParameter("type");
        String[] strArray = null;
        if(null!=strs && !"".equals(strs)) {
            strArray = strs.split(",");
        }
        Sales sales = null;
        for (String idStr : strArray){
            sales = getManager().get(Integer.parseInt(idStr));
            this.getManager().remove(sales);
        }
        return type;
    }
    /**
     * 删除订单信息
     */
    public String remove(){
        Sales sales = getManager().get(getModel().getId());
        this.getManager().remove(sales);
        return SUCCESS;
    }
    /**
     * 删除订单信息
     */
    public String removetgObj(){
        Sales sales = getManager().get(getModel().getId());
        this.getManager().remove(sales);
        return "tgSuc";
    }
    /**
     * 选择快递公司ui
     * @return
     */
    public String selectExpressCompany(){

    	String result = "selectExpressCompany";

    	setModel(getManager().get(getModel().getId()));
    	getRequest().setAttribute("expressCompanyList", companyManager.get());

    	return result;
    }

    /**
     * 保存选择的快递公司
     * @return
     */
    public String saveSelectExpressCompany(){

    	String result = SUCCESS;

    	if(0 == getModel().getExpressCompany().getId()){
    		addActionMessage("请选择快递公司");
    		//欲选择快递公司的id
    		getRequest().setAttribute("expressCompanyList", companyManager.get());
    		return "selectExpressCompany";
    	}
    	if(StringUtils.isBlank((getModel().getCourierNumber()))){
    		addActionMessage("请填写快递单号");
    		return "selectExpressCompany";
    	}

    	if(null != getModel().getExpressCompany().getId() && 0 != getModel().getExpressCompany().getId() && StringUtils.isNotBlank((getModel().getCourierNumber()))){
    		Sales orderDB = getManager().get(getModel().getId());
            if(null==orderDB.getSalesType()||"".equals(null==orderDB.getSalesType())){
                result="cashSuc";
            }
    		orderDB.setExpressCompany(new ExpressCompany(getModel().getExpressCompany().getId()));
    		orderDB.setCourierNumber(getModel().getCourierNumber());
    		orderDB.setoState("1");//已配送
    		getManager().update(orderDB);
    		if("jfSale".equals(orderDB.getSalesType())){//如果是积分订单，跳转到积分列表页面
        		result = "qyJfSaleList";//区域负责人查询积分订单列表
        	}
    	}
    	return result;
    }
    public String sureHandMoney(){
        Sales sales=getManager().get(getModel().getId());
        sales.setPayState(true);
        getManager().update(sales);
        return "sure";
    }
    /**
     * 区域负责人查询积分订单
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String qyJfSaleList() {
        User user = UserUtil.getPrincipal(getRequest());
        System.out.println(user);
        user=userManager.get(user.getId());
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        StringBuffer sql = new StringBuffer(
                "from Sales s where s.status = ?  and  s.orderCardNo = null and s.salesType = 'jfSale' ");
        List args = new ArrayList();
        // 设置单子状态为"订单"
        args.add(SalesConstants.ORDERS);
        //支付方式查询
        if(null!=user.getId()){
        	if(!UserConstants.USER_TYPE_SYS.equals(user.getIsSys())){
				sql.append(" and s.merId = ? ");
				args.add(user.getId());
        	}
        }
        if (StringUtils.isNotBlank(getModel().getSalesNo())) {
            sql.append(" and s.salesNo like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
        }
        if (StringUtils.isNotBlank(name)) {
            sql.append(" and s.user.loginId like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(name));
        }

        if (StringUtils.isNotBlank(getModel().getCkzt())) {
            sql.append(" and s.ckzt = ? ");
            args.add(getModel().getCkzt());
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
                        "yyyy-MM-dd HH:mm:ss", startDate)));
            }
            if (StringUtils.isNotBlank(endDate)) {
                sql.append(" and s.createTime <= ?");
                args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
                        "yyyy-MM-dd HH:mm:ss", endDate)));
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

        System.out.println("dddd"+sql);

        page = getManager().pageQuery(page, sql.toString(), args.toArray());
        restorePageData(page);
        System.out.println("111111111111------->"+sql.toString());
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
        return "jfSale";
    }

    /**
     * 商家查询订单信息
     */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String index() {
        String userId = getRequest().getParameter("userId");
        String result = INDEX;
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        StringBuffer sql = new StringBuffer(
                "from Sales s where  s.orderCardNo = null  ");
        List args = new ArrayList();
        // 设置单子状态为"订单"
        System.out.println("--------------------------mobile="+mobile);
        if(StringUtils.isNotBlank(mobile)){
            sql.append(" and s.user.mobile like '%"+mobile+"%' ");
        }
        if(null!=userId && !"".equals(userId)){
            sql.append(" and s.user.id = ? ");
            args.add(Integer.parseInt(userId));
        }
        if(StringUtils.isNotBlank(getModel().getSalesType())){
            sql.append(" and s.salesType = ? ");
            args.add(getModel().getSalesType());
        }
        String regionId = getRequest().getParameter("regionId");
        System.out.println("----------------"+regionId);
        if(StringUtils.isNotBlank(regionId)){
            Region region = regionManager.get(Integer.parseInt(regionId));
            sql.append(" and s.merUser.region.code like ? ");
            args.add(MatchMode.START.toMatchString(region.getCode()));
        }
        User user = UserUtil.getPrincipal(getRequest());
        User tUser = userManager.get(user.getId());
        if(null!=tUser && null!=tUser.getRegion()){
            Region region = tUser.getRegion();
            System.out.println("--------------------region.getCode()="+region.getCode());
            sql.append(" and s.merUser.region.code like '"+region.getCode()+"%' ");
        }
        if (StringUtils.isNotBlank(getModel().getSalesNo())) {
            sql.append(" and s.salesNo like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
        }
        if (StringUtils.isNotBlank(name)) {
            sql.append(" and s.user.loginId like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(name));
        }

        if (StringUtils.isNotBlank(getModel().getCkzt())) {
            sql.append(" and s.ckzt = ? ");
            args.add(getModel().getCkzt());
        }
        if (StringUtils.isNotBlank(getModel().getSqkfp())) {
            if(getModel().getSqkfp().equals("1")){
                sql.append(" and s.sqkfp = '1' ");
            }
            if(getModel().getSqkfp().equals("2")){
                sql.append(" and s.sqkfp = null ");
            }
        }
        try {
            if (StringUtils.isNotBlank(startDate)) {
                sql.append(" and s.createTime >= ?");
                args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
                        "yyyy-MM-dd HH:mm:ss", startDate)));
            }
            if (StringUtils.isNotBlank(endDate)) {
                sql.append(" and s.createTime <= ?");
                args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
                        "yyyy-MM-dd HH:mm:ss", endDate)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sql.append(" order by s.createTime desc, s.salesNo desc");
        System.out.println("dddd"+sql);
        page = getManager().pageQuery(page, sql.toString(), args.toArray());
        restorePageData(page);
        return result;
    }


    //订单统计
    public String indextj() {
        String userId = getRequest().getParameter("userId");
        String result = "indextj";
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        StringBuffer sql = new StringBuffer(
                "from Sales s where s.status = ?  and  s.orderCardNo = null  ");
        List args = new ArrayList();
        // 设置单子状态为"订单"
        System.out.println("--------------------------mobile="+mobile);
        if(StringUtils.isNotBlank(mobile)){
            sql.append(" and s.user.mobile like '%"+mobile+"%' ");
        }
        args.add(SalesConstants.ORDERS);
        if(null!=userId && !"".equals(userId)){
            sql.append(" and s.user.id = ? ");
            args.add(Integer.parseInt(userId));
        }
        if(StringUtils.isNotBlank(getModel().getSalesType())){
            sql.append(" and s.salesType = ? ");
            args.add(getModel().getSalesType());
        }
        String regionId = getRequest().getParameter("regionId");
        System.out.println("----------------"+regionId);
        if(StringUtils.isNotBlank(regionId)){
            Region region = regionManager.get(Integer.parseInt(regionId));
            sql.append(" and s.user.region.code like ? ");
            args.add(MatchMode.START.toMatchString(region.getCode()));
        }
        User user = UserUtil.getPrincipal(getRequest());
        User tUser = userManager.get(user.getId());
        if(null!=tUser && null!=tUser.getRegion()){
            Region region = tUser.getRegion();
            sql.append(" and s.user.region.code like ? ");
            args.add(MatchMode.START.toMatchString(region.getCode()));
        }
        if (StringUtils.isNotBlank(getModel().getSalesNo())) {
            sql.append(" and s.salesNo like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
        }
        if (StringUtils.isNotBlank(name)) {
            sql.append(" and s.user.loginId like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(name));
        }

        if (StringUtils.isNotBlank(getModel().getCkzt())) {
            sql.append(" and s.ckzt = ? ");
            args.add(getModel().getCkzt());
        }
        if (StringUtils.isNotBlank(getModel().getSqkfp())) {
            if(getModel().getSqkfp().equals("1")){
                sql.append(" and s.sqkfp = '1' ");
            }
            if(getModel().getSqkfp().equals("2")){
                sql.append(" and s.sqkfp = null ");
            }
        }
        try {
            if (StringUtils.isNotBlank(startDate)) {
                sql.append(" and s.createTime >= ?");
                args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
                        "yyyy-MM-dd HH:mm:ss", startDate)));
            }
            if (StringUtils.isNotBlank(endDate)) {
                sql.append(" and s.createTime <= ?");
                args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
                        "yyyy-MM-dd HH:mm:ss", endDate)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sql.append(" order by s.createTime desc, s.salesNo desc");
        System.out.println("dddd"+sql);
        page = getManager().pageQuery(page, sql.toString(), args.toArray());
        List<Sales> sList = page.getData();
        for(Sales sales : sList){
           /* System.out.println("--------------------sales.getMerUser().getId()="+sales.getMerUser().getId());
            System.out.println("--------------------sales.getMerUser().getshopuser="+sales.getMerUser().getShopOfUser());*/
            if(null!=sales.getMerUser()) {
                RenZheng rz = renZhengManager.getReZheng(sales.getMerUser().getId(), "4");
                if(null!=rz){
                    sales.setLegalName(rz.getName());
                    sales.setComAddress(rz.getAddress());
                }
            }
        }
        restorePageData(page);
        return result;
    }


    public String seeFp(){
        Sales sales = getManager().get(getModel().getId());
        System.out.print("--------------------sales.getMerUser().getId()="+sales.getMerUser().getId());
        System.out.print("--------------------sales.getMerUser().getId()="+sales.getMerUser().getId());
        ExpressCompany yhCom = expressCompanyManager.getRsByUserId(sales.getUser().getId());
        ExpressCompany sjCom = expressCompanyManager.getRsByUserId(sales.getMerUser().getShopOfUser().getId());
        ReceiveAddress ra = receiveAddressManager.getRsByUserIdMerId(sales.getUser().getId(),sales.getMerUser().getId());
        getRequest().setAttribute("yh",yhCom);
        getRequest().setAttribute("sj",sjCom);
        getRequest().setAttribute("ra",sales.getAddress());
        return "seeFp";
    }
    /**
     * 平台查询订单信息
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String ptIndex() {
    	String result = "ptIndex";
        User user = UserUtil.getPrincipal(getRequest());
        System.out.println("----------------------------");
        System.out.println(user);
        user=userManager.get(user.getId());
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        StringBuffer sql = new StringBuffer(
                "from Sales s where s.status = ?  and  s.orderCardNo = null ");
        List args = new ArrayList();
        // 设置单子状态为"订单"
        args.add(SalesConstants.ORDERS);
        if (StringUtils.isNotBlank(name)) {
            sql.append(" and s.user.loginId like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(name));
        }
        try {
            if (StringUtils.isNotBlank(startDate)) {
                sql.append(" and s.createTime >= ?");
                args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
                        "yyyy-MM-dd HH:mm:ss", startDate)));
            }
            if (StringUtils.isNotBlank(endDate)) {
                sql.append(" and s.createTime <= ?");
                args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
                        "yyyy-MM-dd HH:mm:ss", endDate)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sql.append(" order by s.createTime desc, s.salesNo desc");
        System.out.println("dddd"+sql);
        page = getManager().pageQuery(page, sql.toString(), args.toArray());
        restorePageData(page);
        return result;
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
                "from Sales s where s.status = ?  and s.orderCardNo = null ");
        List args = new ArrayList();
        // 设置单子状态为"订单"
        args.add(SalesConstants.ORDERS);

        User user = UserUtil.getPrincipal(getRequest());
        getModel().setUser(user);
        if (StringUtils.isNotBlank(user.getBeginningInit())) {
            if (user.getBeginningInit().equals("0")) {
                this.addActionError("请先完成初始化，再做业务单据");
            }
        }

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
		/*
		 * if(user.getBeginningInit() == null){ System.out.println("user："+user);
		 * System.out.println("user.getSuperior()："+user.getSuperior());
		 * System.out.println
		 * ("user.getSuperior().getBeginningInit()："+user.getSuperior
		 * ().getBeginningInit());
		 * if(user.getSuperior().getBeginningInit().equals("0")){
		 * this.addActionError("您的上级经销商还没有完成初始化，所以您现在不能正式使用该平台。"); } }
		 */

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
        String type = getRequest().getParameter("type");
        getRequest().setAttribute("type",type);
        getRequest().setAttribute("status",
                getBillStatusMap().get(getModel().getStatus()));
        getSalesDetail();
        return VIEW;
    }
    public String viewWx(){
        super.view();
        String type = getRequest().getParameter("type");
        getRequest().setAttribute("type",type);
        getRequest().setAttribute("status",
                getBillStatusMap().get(getModel().getStatus()));
        getSalesDetail();
        return "viewWx";
    }
    public String viewBx(){
        super.view();
        String type = getRequest().getParameter("type");
        getRequest().setAttribute("type",type);
        getRequest().setAttribute("status",
                getBillStatusMap().get(getModel().getStatus()));
        tbrList();
        return "viewBx";
    }
    public List<TouBaoRen> tbrList(){
        List<TouBaoRen> tbList = touBaoRenManager.getListBySaleId(getModel().getId());
        getRequest().setAttribute("sd",tbList);
        return tbList;
    }
    public String jfOrderView() {
        super.view();
        getRequest().setAttribute("status",
                getBillStatusMap().get(getModel().getStatus()));
        getSalesDetail();
        return "jfOrderView";
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
            Sales sales = getManager().get(getModel().getId());
            sales.setSamount(getModel().getSamount());
            sales.setCount(getModel().getCount());
            getManager().update(sales);
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
     * 查询app用户消费统计
     * @throws Exception
     */
    public String moneySum() throws Exception {
        String result = "sumIndex";
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        Map<String, Object> param = this.getMoneyQuery();
        page = getManager().pageQuery(page, param.get("sql").toString(),
                ((List) param.get("param")).toArray());
        List<SumOfUserPayment> myList=new ArrayList<SumOfUserPayment>();
        List list=page.getData();
        for (Object o : list) {
            Object[] objs = (Object[]) o;
            SumOfUserPayment js=new SumOfUserPayment();
            if(null != objs[0]){
                Double sumMoney=Double.parseDouble(String.valueOf(objs[0]));
                js.setSumMoney(sumMoney);
                if(sumMoney>=250){
                    js.setFxMoney(50.0);
                }else if(sumMoney>=200&&sumMoney<250){
                    js.setFxMoney(40.0);
                }else if(sumMoney>=150&&sumMoney<200){
                    js.setFxMoney(30.0);
                }else if(sumMoney>=100&&sumMoney<150){
                    js.setFxMoney(20.0);
                }else if(sumMoney>=50&&sumMoney<100){
                    js.setFxMoney(10.0);
                }/*else{
					js.setFxMoney(0.0);
				}*/
            }
            if(null!=objs[1]) {js.setName(String.valueOf(objs[1]));}
            if(null!=objs[2]) {js.setLoginId(String.valueOf(objs[2]));}
            if(null!=objs[3]) {
                User user=userManager.findUser(String.valueOf(objs[3]));
                if(null!=user){
                    js.setCdName(user.getName());}
            }
            if(null!=objs[4]) {js.setsName(String.valueOf(objs[4]));}
            if(null!=objs[5]) {js.setxName(String.valueOf(objs[5]));}
            if(null!=objs[6]) {js.setcName(String.valueOf(objs[6]));}
            myList.add(js);
        }
        List args = new ArrayList();
		/*
		 * if(user.getBeginningInit() == null){ System.out.println("user"+user);
		 * System.out.println("user.getSuperior()："+user.getSuperior());
		 * System.out.println
		 * ("user.getSuperior().getBeginningInit()："+user.getSuperior
		 * ().getBeginningInit());
		 * if(user.getSuperior().getBeginningInit().equals("0")){
		 * this.addActionError("您的上级经销商还没有完成初始化，所以您现在不能正式使用该平台。"); } }
		 */
        //	page = getManager().pageQuery(page, sql.toString(), args.toArray());
        ///	restorePageData(page);
        page.setData(myList);
        restorePageData(page);
        return result;
    }
    private Map<String,Object> getMoneyQuery() throws Exception {
        Map<String, Object> result = new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer(" select max(s.spayamount) as sumMoney,s.user.name as realName,s.user.loginId as loginName,s.user.region.code as code,");
        sql.append(" s.user.region.parent.parent.name as sname,s.user.region.parent.name as xname,s.user.region.name as cname ");
        sql.append(" from Sales s where s.status=0 and s.payState=1 and s.spayamount>=50  ");
        List args = new ArrayList();
        String regionId = getRequest().getParameter("regionId");
        if (StringUtils.isNotBlank(regionId) ) {
            Region region=regionManager.get(Integer.parseInt(regionId));
            sql.append(" and s.user.region.code like ? ");
            args.add(MatchMode.START.toMatchString(region.getCode()));
        }
        String productName = getRequest().getParameter("productName");
        if (StringUtils.isNotBlank(productName) ) {
            sql.append(" and s.products.name ? ");
            args.add(MatchMode.START.toMatchString(productName));
        }
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
        if(StringUtils.isBlank(startDate)&&StringUtils.isBlank(endDate)){
            sql.append(" and date(s.createTime)=date(CURDATE()) ");
        }
        sql.append(" group by s.user.id order by sum(s.spayamount) desc");
        result.put("sql", sql);
        result.put("param", args);
        return result;
    }
    //团购消费统计
    /**
     * 查询app用户消费统计
     * @throws Exception
     */
    public String groupSum() throws Exception {
        String result = "groupIndex";
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        Map<String, Object> param = this.getGroupQuery();
        page = getManager().pageQuery(page, param.get("sql").toString(),
                ((List) param.get("param")).toArray());
        System.out.println("-----------------------"+page.getData());
        List<GroupSum> myList=new ArrayList<GroupSum>();
        List list=page.getData();
        //团购总的金额，总的数量，商品名称，规格，单位，县级分销，分销比例
        for (Object o : list) {
            Object[] objs = (Object[]) o;
            GroupSum js=new GroupSum();
            if(null!=objs[0]) {js.setGroupMoneySum(Double.valueOf(String.valueOf(objs[0])));}
            if(null!=objs[1]) {js.setGroupNumSum(Integer.parseInt(String.valueOf(objs[1])));}
            if(null!=objs[2]) {js.setGoodsName(String.valueOf(objs[2]));}
            if(null!=objs[3]) {js.setGoodStandard(String.valueOf(objs[3]));}
            if(null!=objs[4]) {js.setGoodsUnits(String.valueOf(objs[4]));}
            if(null!=objs[5]) {
                js.setXjFxName(String.valueOf(objs[5]));
            }
            if(null!=objs[6]) {
                //每种商品的县级分销比例
                js.setXjFxMoney(Double.valueOf(String.valueOf(objs[0]))*Integer.parseInt(String.valueOf(objs[6]))/100);
            }
            if(null!=objs[7]) {
                //每种商品的县级分销比例
                js.setXjFxMoney(Double.valueOf(String.valueOf(objs[0]))*Integer.parseInt(String.valueOf(objs[7]))/100);
            }
            myList.add(js);
        }
        page.setData(myList);
        restorePageData(page);
        return result;
    }
    //团购总的金额，总的数量，商品名称，规格，单位，县级分销，分销比例
    private Map<String,Object> getGroupQuery() throws Exception {
        Map<String,Object> result = new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer(" select sum(s.amount) as sumMoney,");
        sql.append(" sum(s.tnorootl),s.products.name,s.products.stardard");
        sql.append(" ,s.products.units.name,s.products.tgfxs.region.name,s.products.royaltyRateCounty,s.products.royaltyRateProvince ");
        sql.append(" from SalesDetail s  where s.sales.status=0 and s.products.productType="+ProductConstants.GROUP_PURCHASE+" and s.products.belonging="+ProductConstants.PLATFORM);
        List args = new ArrayList();
        sql.append(" and s.sales.payState=?");
        args.add(true);
        User user=UserUtil.getPrincipal(getRequest());
        user=userManager.get(user.getId());
        if(null!=user.getRegion())
        {
            sql.append(" and s.sales.user.region.code like ? ");
            args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
        }
        String prosortIdStr = getRequest().getParameter("prosortId");
        if (StringUtils.isNotBlank(prosortIdStr)) {
            sql.append(" and sd.products.prosort.serialNo like ? ");
            ProductSort productSort = productSortManager
                    .get(new Integer(prosortIdStr));
            args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
        }
        String regionId = getRequest().getParameter("regionId");
        if (StringUtils.isNotBlank(regionId) ) {
            Region region=regionManager.get(Integer.parseInt(regionId));
            sql.append(" and s.products.tgfxs.region.code like ? ");
            args.add(MatchMode.START.toMatchString(region.getCode()));
        }
        if (StringUtils.isNotBlank(startDate)) {
            sql.append(" and s.sales.createTime >= ?");
            args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
                    "yyyy-MM-dd", startDate)));
        }
        if (StringUtils.isNotBlank(endDate)) {
            sql.append(" and s.sales.createTime <= ?");
            args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
                    "yyyy-MM-dd", endDate)));
        }
        sql.append("group by s.products.tgfxs.id order by  sum(s.amount) desc");
        result.put("sql", sql);
        result.put("param", args);
        return result;
    }
    /**
     * 团购查询
     */
    public String tgIndex() {
        String result = "tgIndex";
        User user = UserUtil.getPrincipal(getRequest());
        System.out.println("----------------------------");
        System.out.println(user);
        user=userManager.get(user.getId());
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        StringBuffer sql = new StringBuffer(
                "from Sales s where  s.salesType='charge' ");
        List args = new ArrayList();
        if(null!=user.getRegion()){
            sql.append(" and s.user.region.code like ? ");
            args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
        }
        if (StringUtils.isNotBlank(getModel().getSalesNo())) {
            sql.append(" and s.salesNo like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
        }
        if(null == regionId || 0 == regionId){
            if(null != user.getRegion()){
                sql.append(" and s.user.region.code like ?");
                args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
                regionNameCun = user.getRegion().getName();
            }
        }else{
            Region region = regionManager.get(Integer.valueOf(regionId));
            sql.append(" and s.user.region.code like ?");
            args.add(MatchMode.START.toMatchString(region.getCode()));
            regionNameCun = region.getName();
        }
        System.out.println("-----------------"+getModel().getUser());
        if (StringUtils.isNotBlank(name)) {
            sql.append(" and s.user.loginId like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(name));
        }

        if (StringUtils.isNotBlank(getModel().getCkzt())) {
            sql.append(" and s.ckzt = ? ");
            args.add(getModel().getCkzt());
        }
        if(null==user.getRegion()){
            sql.append("");
        }
        else{if (StringUtils.isNotBlank(user.getRegion().getCode())) {
            sql.append(" and s.user.region.code like ? ");
            args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
        }

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

        System.out.println("dddd"+sql);

        page = getManager().pageQuery(page, sql.toString(), args.toArray());
        restorePageData(page);
        System.out.println("111111111111------->"+sql.toString());
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
        return result;
    }
    //更新订单为已经接单状态
    public String updateOrderJycJd(){
        //加油车id
        String userId = getRequest().getParameter("userId");
        User merUser = userManager.get(Integer.parseInt(userId));
        int qdNum = userManager.getCntOfQd(Integer.parseInt(userId));
        System.out.println("1======"+getModel().getId()+"trunto:"+userId);
        jdbcTemplate.execute(" update sales set status='1',meruser_id="+Integer.parseInt(userId)+",mer_id="+Integer.parseInt(userId)+" where id="+getModel().getId());
        //调用推送api 推送给用户商家已经接单
        Sales order = salesManager.get(getModel().getId());
        User user =userManager.get(order.getUser().getId());
        System.out.println("-------------------order.getUser().getId()=" + order.getUser().getId());
        PhoneAgentAction pa = new PhoneAgentAction();
        System.out.println("推送内容为。。。。" + "手机号为" + merUser.getPhone() + "的加油车司机已经接单,订单号为" + order.getSalesNo());
        pa.pushUser(order.getUser().getId() + "", "手机号为" + merUser.getPhone() + "的加油车司机已经接单,订单号为" + order.getSalesNo());
        pa.pushUser(order.getMerUser().getShopOfUser().getId() + "", "您好，平台分配给您一个加油订单，请尽快联系"+order.getUser().getPhone());
        return "jyxxSuc";
    }
    /**
     * 加油消息action
     */
    //加油消息订单
    public String indexJyXx() {
        String userId = getRequest().getParameter("userId");
        String result = "indexJyXx";
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        StringBuffer sql = new StringBuffer(
                "from Sales s where s.status = ?   and s.salesType='jy' and  s.orderCardNo = null  ");
        List args = new ArrayList();
        args.add(SalesConstants.ORDERS);
        System.out.println("--------------------------mobile="+mobile);
        if(StringUtils.isNotBlank(mobile)){
            sql.append(" and s.user.mobile like '%"+mobile+"%' ");
        }
        if(null!=userId && !"".equals(userId)){
            sql.append(" and s.user.id = ? ");
            args.add(Integer.parseInt(userId));
        }
        String regionId = getRequest().getParameter("regionId");
        System.out.println("----------------"+regionId);
        if(StringUtils.isNotBlank(regionId)){
            Region region = regionManager.get(Integer.parseInt(regionId));
            sql.append(" and s.user.region.code like ? ");
            args.add(MatchMode.START.toMatchString(region.getCode()));
        }
        User user = UserUtil.getPrincipal(getRequest());
        User tUser = userManager.get(user.getId());
        if(null!=tUser && null!=tUser.getRegion()){
            Region region = tUser.getRegion();
            sql.append(" and s.user.region.code like ? ");
            args.add(MatchMode.START.toMatchString(region.getCode()));
        }
        if (StringUtils.isNotBlank(getModel().getSalesNo())) {
            sql.append(" and s.salesNo like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
        }
        if (StringUtils.isNotBlank(name)) {
            sql.append(" and s.user.loginId like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(name));
        }

        if (StringUtils.isNotBlank(getModel().getCkzt())) {
            sql.append(" and s.ckzt = ? ");
            args.add(getModel().getCkzt());
        }
        if (StringUtils.isNotBlank(getModel().getSqkfp())) {
            if(getModel().getSqkfp().equals("1")){
                sql.append(" and s.sqkfp = '1' ");
            }
            if(getModel().getSqkfp().equals("2")){
                sql.append(" and s.sqkfp = null ");
            }
        }
        try {
            if (StringUtils.isNotBlank(startDate)) {
                sql.append(" and s.createTime >= ?");
                args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
                        "yyyy-MM-dd HH:mm:ss", startDate)));
            }
            if (StringUtils.isNotBlank(endDate)) {
                sql.append(" and s.createTime <= ?");
                args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
                        "yyyy-MM-dd HH:mm:ss", endDate)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sql.append(" order by s.createTime desc, s.salesNo desc");
        System.out.println("dddd"+sql);
        page = getManager().pageQuery(page, sql.toString(), args.toArray());
        List<Sales> sList = page.getData();
        for(Sales sales : sList){
           /* System.out.println("--------------------sales.getMerUser().getId()="+sales.getMerUser().getId());
            System.out.println("--------------------sales.getMerUser().getshopuser="+sales.getMerUser().getShopOfUser());*/
            if(null!=sales.getMerUser()) {
                RenZheng rz = renZhengManager.getReZheng(sales.getMerUser().getId(), "4");
                if(null!=rz){
                    sales.setLegalName(rz.getName());
                    sales.setComAddress(rz.getAddress());
                }
            }
        }
        restorePageData(page);
        return result;
    }
    public String turnOrderJyc(){
        setModel(getManager().get(getModel().getId()));
        return "turnOrderJyc";
    }



















    /**
     * 单子状态Map
     *
     */
    public Map<String, String> getBillStatusMap() {
        return SalesConstants.BILL_STATUS_MAP;
    }

    /**
     * 支付状态Map
     *
     */
    public Map<String, String> getPaymentMap() {
        return SalesConstants.PAYMENT_MAP;
    }

    /**
     * 出库状态Map
     *
     */
    public Map<String, String> getSalesStatusMap() {
        return SalesConstants.SALES_STATUS_MAP;
    }

    /**
     * 出库状态Map S
     *
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

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public CustomerManager getCustomerManager() {
        return customerManager;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public void setCustomerManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public SalesDetailManager getSalesDetailManager() {
        return salesDetailManager;
    }

    public void setSalesDetailManager(SalesDetailManager salesDetailManager) {
        this.salesDetailManager = salesDetailManager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSalesType() {
        return salesType;
    }

    public void setSalesType(String salesType) {
        this.salesType = salesType;
    }

    public String getRegionNameCun() {
        return regionNameCun;
    }

    public void setRegionNameCun(String regionNameCun) {
        this.regionNameCun = regionNameCun;
    }

    public String getPment() {
        return pment;
    }

    public void setPment(String pment) {
        this.pment = pment;
    }

	public Integer getTestNumber() {
		return testNumber;
	}

	public void setTestNumber(Integer testNumber) {
		this.testNumber = testNumber;
	}

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}