package com.systop.amol.sales.phoneapp;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.product.service.ProductsManager;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.model.SalesDetail;
import com.systop.amol.sales.service.SalesDetailManager;
import com.systop.amol.sales.service.SalesManager;
import com.systop.amol.sales.service.SalesOrderManager;
import com.systop.amol.sales.utils.Payment;
import com.systop.amol.sales.utils.ReceivingState;
import com.systop.amol.user.agent.model.*;
import com.systop.amol.user.agent.phone.PhoneAgentAction;
import com.systop.amol.user.agent.service.*;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;
import com.thirdParty.yunTongXun.SDKTestSendTemplateSMS;
import com.thirdParty.yunTongXun.SMSConstants;
import com.thirdParty.yunTongXun.Scsjs;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.util.*;

/**
 * 手机端订单Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SalesOrderPhoneAction
	extends DefaultCrudAction<Sales,SalesOrderManager> {

	@Resource
	private RzFileManager rzFileManager;
	@Resource
	private TouBaoRenManager touBaoRenManager;
	@Resource
	private CustomerManager customerManager;
	@Autowired
	private JiayouSetManager jiayouSetManager;
	@Resource
	private SalesManager salesManager;
	@Resource
	private SalesDetailManager salesDetailManager;
	@Resource
	private ZhuanZhangRecordManager zhuanZhangRecordManager;
	@Resource
	private UserManager userManager;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private MerchantValidationManager merchantValidationManager;
	@Resource
	FxgzManager fxgzManager;
	private final static  String appKey ="99e00c56584d0d266e8dc360";
	private final static  String masterSecret = "18dd15d6b96b95fdde7fdd10";
	@Resource
	private PartnersManager partnersManager;
	@Resource
	private ReceiveAddressManager receiveAddressManager;
	@Resource
	private ProductsManager productsManager;

	/** app用户id */
	private Integer userId;

	/** 返回状态 */
	private String result = INPUT;
	
	private String payment;
	
	private String addrId;

	/** ajax校验微信用户手机号返回信息 */
	Map<String, Object> resultMessage = new HashMap<String, Object>();

	/** 订单集合 */
	private List<Sales> list;

	/** 分页，页码 */
	private String pageNumber = "1";

	/** 每页显示条数 */
	private String pageCount = "10";

	/** 实收金额，payamount已付款，其他值未付款 */
	private String spayamount;

	/** 付款状态，null未付款，true付款成功，false付款失败 */
	private String payState;

	/** 商品集合 */
	private List<SalesDetail> salesDetailList;

	/** 退款状态 */
	private String ckzt;
	
	/** 订单id字符串格式 */
	private String orderId;

	private List msgList=new ArrayList();

	private String latitude;

	private String longitude;
	private Sales order;
	private List<User> uList = new ArrayList<User>();
	private File[] files; //上传的文件
	private String[] filesFileName; //文件名称
	private String[] filesContentType; //文件类型
	private String attchFolder = "/uploadFiles/fileAttch/";
	private List<Map<String,Object>> userList = new ArrayList<Map<String, Object>>();
	public String getBySaleNo(){
		String salesNo = getRequest().getParameter("salesNo");
		order = this.getManager().findObject(
				"from Sales o where o.salesNo = ?", salesNo);
		List<TouBaoRen> tbList = touBaoRenManager.getListBySaleId(order.getId());
		for(TouBaoRen tb : tbList){
			tb.setCreateTimeStr(String.valueOf(tb.getCreateTime()).replace("T"," "));
		}
		order.setTbList(tbList);
		if(null!=order.getMerUser()) {
			System.out.println("--------------------"+order.getMerUser().getName());
			order.setCreateTimeStr(String.valueOf(order.getCreateTime()).replace("T"," "));
			order.setMerName(order.getMerUser().getName());
			order.setImageURL(order.getMerUser().getImageURL());
			order.setUserPhone(order.getMerUser().getPhone());
			int count = userManager.getCntValidate(order.getMerUser().getId());
			System.out.println("------------------------------count="+count);
			double avg = userManager.getAvgScore(order.getMerUser().getId());
			System.out.println("------------------------------avg="+avg);
			order.setPjrs(count);
			order.setPjfs(avg);
			System.out.println("------------------order.getMerUser().getAddress()="+order.getMerUser().getAddress());
			order.setComAddress(order.getMerUser().getAddress());
		}
		if(null!=order.getUser()){
			order.setYhdh(order.getUser().getPhone());
			order.setYhNickName(order.getUser().getNickName());
			order.setYhtx(order.getUser().getImageURL());
			order.setUserId(order.getUser().getId());
		}
		return "order";
	}
	/**
	 * 支付前校验商品数量与库存作比较
	 */
	public String beforePayJiaoYan(){
		String result="invoke";
		String orderId=getRequest().getParameter("orderId");
		Map<String,Object> map=null;
		List<SalesDetail> list = salesDetailManager.getDetails(Integer.parseInt(orderId));
		if(null!=list&&list.size()>0){
			for (SalesDetail sd : list){
				Products products=productsManager.get(sd.getProducts().getId());
				if(null==products.getMaxCount()&&sd.getCount()>products.getMaxCount()){
					map = new HashMap<String,Object>();
					map.put("msg","订单中["+products.getName()+"]库存不足！");
					result="uninvoke";
					msgList.add(map);
				}
			}
			resultMessage.put("msg",result);
			resultMessage.put("shortProduct",msgList);
		}
		return "save";
	}
	/**
	 * app用户确认收货
	 * @return
	 */
	public String appUserConfirmReceipt(){
		boolean result = false;
		try {
			Sales salesDB = getManager().get(Integer.valueOf(orderId));
			salesDB.setReceivingState(ReceivingState.RECEIVED_GOODS);
			getManager().update(salesDB);
			result = true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		resultMessage.put("result", result);
		
		return "appUserConfirmReceipt";
	}
	//抢到的订单
	public String getMerQdOrders(){
		String merId = getRequest().getParameter("merId");
		Page page = PageUtil.getPage(Integer.valueOf(pageNumber),
				Integer.valueOf(pageCount));
		StringBuffer sql = new StringBuffer(
				"from Sales s where s.merUser.id = ? and s.payState is null and s.status!='3' and sales_type='jy' ");
		List args = new ArrayList();
		args.add(Integer.parseInt(merId));
		try {
			sql.append(" order by s.createTime desc, s.salesNo desc");
			page = getManager().pageQuery(page, sql.toString(), args.toArray());
			list = page.getData();
			for (Sales sales:list){
				sales.setUserPhone(sales.getMerUser().getShopOfUser().getPhone());
				sales.setMerName(sales.getMerUser().getName());
				sales.setImageURL(sales.getMerUser().getImageURL());
				if(null!=sales.getUser()){
					sales.setRemark(sales.getUser().getName());
					sales.setUserId(sales.getUser().getId());
					sales.setYhNickName(sales.getUser().getNickName());
					sales.setYhdh(sales.getUser().getPhone());
					sales.setYhtx(sales.getUser().getImageURL());
				}
				if(null!=sales.getMerUser()){
					String sortIds = sales.getMerUser().getMerSortStr();
					String[] sortArray = sortIds.split(",");
					String priceStr = "";
					for(String id: sortArray){
						JiayouSet js = jiayouSetManager.get(Integer.parseInt(id));
						priceStr += js.getGrPrice()+",";
					}
					sales.setMerSortStr(sales.getMerUser().getMerSortStr());
					sales.setMerSortNameStr(sales.getMerUser().getMerSortNameStr());
					sales.setPriceStr(priceStr.substring(0,priceStr.length()-1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(sql);

		return INDEX;
	}
	/**
	 * 非滴滴加油我的订单
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public String indexUserOrders() {
		Page page = PageUtil.getPage(Integer.valueOf(pageNumber),
				Integer.valueOf(pageCount));
		String userId  = getRequest().getParameter("userId");
		StringBuffer sql = new StringBuffer(
				"from Sales s where  s.user.id = ? ");
		String type = getRequest().getParameter("type");
		List args = new ArrayList();
		args.add(Integer.parseInt(userId));
		if(StringUtils.isNotBlank(type)) {
			sql.append(" and s.payState = ? ");
			args.add(Boolean.valueOf(type));
		}
		String salesType = getRequest().getParameter("salesType");
		if(StringUtils.isNotBlank(salesType)) {
			sql.append(" and s.salesType = ? ");
			args.add(salesType);
		}
		try {
			sql.append(" order by s.createTime desc, s.salesNo desc");
			page = getManager().pageQuery(page, sql.toString(), args.toArray());
			list = page.getData();
			for (Sales sale : list){
				if(null!=sale.getMerUser()){
					sale.setJycName(sale.getMerUser().getName());
				}
				MerchantValidations mv = merchantValidationManager.getByUserIdAndSalesId(sale.getUser().getId(),sale.getId());
				if(null!=mv){
					sale.setYhplsj("true");
				}
				sale.setCreateTimeStr(String.valueOf(sale.getCreateTime()).replace("T"," "));
				List<SalesDetail> sdList = salesDetailManager.getDetails(sale.getId());
				if(null!=sdList && sdList.size()>0) {
					if(null!=sdList.get(0).getProducts()) {
						sale.setProName(sdList.get(0).getProducts().getName());
					}
				}
				sale.setCreateTimeStr(String.valueOf(sale.getCreateTime()).replace("T"," "));
				if(null!=sale&&null!=sale.getMerUser()) {
					System.out.println("-------------------sale.getMerUser()="+sale.getMerUser());
					sale.setJycName(sale.getMerUser().getName());
				}
				if(null!=sale.getUser() && null!=sale.getMerUser()){
					int count = userManager.getCntValidate(sale.getMerUser().getId());
					double avg = userManager.getAvgScore(sale.getMerUser().getId());
					sale.setPjfs(avg);
					sale.setPjrs(count);
					MerchantValidations mv1 = merchantValidationManager.getScoreOfVs(sale.getUser().getId(),sale.getMerUser().getId(),sale.getId());
					MerchantValidations mv2 = merchantValidationManager.getScoreOfVs(sale.getMerUser().getId(),sale.getUser().getId(),sale.getId());
					if(null!=mv1){
						sale.setYhplsjfs(mv1.getScore());
						sale.setYhplsj("true");
						sale.setYhplsjnr(mv1.getContent());
					}else{
						sale.setYhplsjfs(0);
					}
					if(null!=mv2){
						sale.setSjplyhfs(mv2.getScore());
						sale.setSjplyh("true");
						sale.setSjplyhnr(mv2.getContent());
					}else{
						sale.setSjplyhfs(0);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INDEX;
	}
	/**
	 * 根据油的名称获得订单
	 * @return
	 */
	@SuppressWarnings({ "rawtypes","unchecked"})
	public String getOrderByName() {
		Page page = PageUtil.getPage(Integer.valueOf(pageNumber),
				Integer.valueOf(pageCount));
		StringBuffer sql = new StringBuffer(
				"from Sales s where s.pay_state='0' ");
		List args = new ArrayList();
		try {
			sql.append(" order by s.createTime desc, s.salesNo desc");
			page = getManager().pageQuery(page, sql.toString(), args.toArray());
			list = page.getData();
			for (Sales sale : list){
				System.out.println(sale.getUser().getNickName()+"---------------------"+sale.getUser().getImageURL());
				sale.setNickName(sale.getUser().getNickName());
				sale.setImageURL(sale.getUser().getImageURL());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INDEX;
	}
	/**
	 * 非滴滴加油订单未付款 未加油
	 * @return
     */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public String indexOrders() {
		Page page = PageUtil.getPage(Integer.valueOf(pageNumber),
				Integer.valueOf(pageCount));
		String merId  = getRequest().getParameter("merId");
		String type  = getRequest().getParameter("type");
		String record  = getRequest().getParameter("record");
		User vipUser = userManager.get(Integer.parseInt(merId));
		StringBuffer sql = new StringBuffer(
				"from Sales s where  1=1  ");
		if(StringUtils.isNotBlank(type)){

				if(type.equals("jyxx")) {
					sql.append(" and s.spayamount is null and s.status='0'  ");
				}
				if(type.equals("xfjl")){
					sql.append(" and s.spayamount is not null and s.payState='1' ");
				}
		}
		List args = new ArrayList();
		String salesType = getRequest().getParameter("salesType");
		if(StringUtils.isNotBlank(salesType)) {
			sql.append(" and s.salesType = ? and s.merUser.id=? ");
			args.add(salesType);
			args.add(vipUser.getId());
		}else{
			sql.append(" and  s.merUser.shopOfUser.id = ?  ");
			args.add(vipUser.getShopOfUser().getId());
		}
		try {
			sql.append(" order by s.createTime desc, s.salesNo desc");
			page = getManager().pageQuery(page, sql.toString(), args.toArray());
			list = page.getData();
			for (Sales sale : list){
				System.out.println("-----------------locx="+sale.getLocX()+"          id="+sale.getId());
				if(null!=sale.getLocX()) {
					sale.setLocX(bcLw(sale.getLocX() + ""));
				}
				if(null!=sale.getLocY()) {
					sale.setLocY(bcLw(sale.getLocY() + ""));
				}
				List<SalesDetail> sdList = salesDetailManager.getDetails(sale.getId());
				if(null!=sdList && sdList.size()>0){
					if(null!=sdList.get(0).getProducts()) {
						sale.setProName(sdList.get(0).getProducts().getName());
					}
				}
				sale.setNickName(sale.getUser().getNickName());
				sale.setImageURL(sale.getUser().getImageURL());
				sale.setCreateTimeStr(String.valueOf(sale.getCreateTime()).replace("T"," ").replace(".0",""));
				sale.setSdList(salesDetailManager.getDetails(sale.getId()));
//				sale.setMerName(sale.getUser().getName());
				sale.setMerName(sale.getMerUser().getName());
				String merSortIds = sale.getMerUser().getMerSortStr();
				if(null!=merSortIds) {
					String[] ids = merSortIds.split(",");
					System.out.println("-------------------------------ids="+Arrays.asList(ids));
					String priceStr = "";
					for (String id : ids) {
						if(StringUtils.isNotBlank(id))
						{
							JiayouSet js = jiayouSetManager.get(Integer.parseInt(id));
							if(null!=js) {
								priceStr += js.getGrPrice() + ",";
							}
						}
					}
					if (StringUtils.isNotBlank(priceStr)) {
						sale.setPriceStr(priceStr.substring(0, priceStr.length() - 1));
					}
				}
				System.out.println("-----------------------sale.getUser().getPhone()="+sale.getUser().getPhone());
				System.out.println("-----------------------sale.getId()="+sale.getId());
				sale.setUserPhone(sale.getUser().getPhone());
				sale.setMerSortNameStr(sale.getMerUser().getMerSortNameStr());
				sale.setMerSortStr(sale.getMerUser().getMerSortStr());
				MerchantValidations mv1 = merchantValidationManager.getScoreOfVs(sale.getUser().getId(),sale.getMerUser().getId(),sale.getId());
				MerchantValidations mv2 = merchantValidationManager.getScoreOfVs(sale.getMerUser().getId(),sale.getUser().getId(),sale.getId());
				if(null!=mv1){
					sale.setYhplsjfs(mv1.getScore());
					sale.setYhplsj("true");
					sale.setYhplsjnr(mv1.getContent());
				}else{
					sale.setYhplsjfs(0);
				}
				if(null!=mv2){
					sale.setSjplyhfs(mv2.getScore());
					sale.setSjplyh("true");
					sale.setSjplyhnr(mv2.getContent());
				}else{
					sale.setSjplyhfs(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INDEX;
	}
	public double bcLw(String str){
		System.out.println(str);
		String[] strings = str.split("\\.");
		if(strings[1].length()<=6){
			System.out.println("-----------------str="+str);
			return Double.valueOf(str);
		}else {
			System.out.println("-----------------strings[0] + \".\" + strings[1].substring(0, 6)="+strings[0] + "." + strings[1].substring(0, 6));
			return Double.valueOf(strings[0] + "." + strings[1].substring(0, 6));
		}
	}
	/**
	 * 查询订单信息 json 数据，字段对应看Sales对象
	 * /salesOrderPhone/index.do
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public String index() {
	Page page = PageUtil.getPage(Integer.valueOf(pageNumber),
		Integer.valueOf(pageCount));
	StringBuffer sql = new StringBuffer(
		"from Sales s where s.sqkfp = '1' ");
	List args = new ArrayList();
	// 设置单子状态为"订单"
	User user = userManager
		.get(Integer.valueOf(getRequest().getParameter("userId")));
	if (user != null) {
		sql.append(" and s.user.id = ? ");
		args.add(user.getId());

	}
	try {
		if ("true".equals(payState)) {
			System.out.println("===================="+payState);
		sql.append(" and s.payState = ?");
		args.add(Boolean.valueOf(payState));// 付款成功
		} else {
		sql.append(" and (s.payState != true or s.payState=null)");
		;// 未付款
		}
		String salesNo  = getRequest().getParameter("salesNo");
		if (StringUtils.isNotBlank(salesNo)) {
		sql.append(" and s.salesNo like ? ");
		args.add(MatchMode.ANYWHERE.toMatchString(salesNo));
		}
		sql.append(" order by s.createTime desc, s.salesNo desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		list = page.getData();
		for (Sales sales:list){
			if(null!=sales.getAddress()) {
				sales.setAddressForClient(sales.getAddress().getAddress());
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	System.out
		.println("pageNumber = " + pageNumber + ", pageCount = " + pageCount
			+ ", spayamount = " + spayamount + ", userId = " + user.getId());
	System.out.println(sql);

	return INDEX;
	}
	/**
	 * 退货 现金 提交参数：userId 用户id；orderId 订单号
	 * @return
	 */
	public String returnOrder() {
	String userId = getRequest().getParameter("userId");
	String orderId = getRequest().getParameter("orderId");
	if (StringUtils.isNotBlank(orderId) && StringUtils.isNotBlank(userId)) {
		Sales sales = getManager().get(Integer.valueOf(orderId));
		sales.setIsReturn("2");
		salesManager.update(sales);
		Sales returnOrder = (Sales)sales.clone();
		returnOrder.setId(null);
		returnOrder.setStatus(SalesConstants.FULL_RETURN);
		returnOrder.setSingle(sales);
		//提交参数包括：userId；orderId；rpid退款商品id','；hanod单个商品的退货数‘,’；rttao单个商品的退款金额‘,’；count退单退货商品总数量；rttaoAll退款总金额
	    // 退款商品id
			String[] rpid = this.getRequest().getParameter("rpid").split(",");
			
		  //单个商品的退货数
			String[] hanod = this.getRequest().getParameter("hanod").split(",");
		  //单个商品的退款金额
			String[] rttao = this.getRequest().getParameter("rttao").split(",");
			
			//退单退货商品总数量
			String counts = this.getRequest().getParameter("count");
			// 退款总金额
			String moneys = this.getRequest().getParameter("rttaoAll");
//			returnOrder.setCount(Integer.valueOf(counts));
			returnOrder.setRttao(Double.valueOf(moneys));
			
			List<SalesDetail> sdlist = new ArrayList<SalesDetail>();
			for (int i = 0; i < rpid.length; i++) {

			SalesDetail sd = new SalesDetail();
			
			sd.setProducts(this.getManager().getDao().get(Products.class,
					Integer.parseInt(rpid[i])));
			
			sd.setHanod(Integer.valueOf(hanod[i]));//单个商品量退货数量（退货数量字段）
			sd.setCount(Integer.valueOf(hanod[i]));//单个商品退货总金额（商品详情数量字段）
			sd.setRttao(Double.parseDouble(rttao[i]));//单个商品退货总数量（退货总金额字段）
			sd.setAmount(Double.parseDouble(rttao[i]));//单个商品退货总金额（商品详情总金额字段）
			
			sd.setSales(returnOrder);
			sdlist.add(sd);
			}

		
		getManager().save(sales, returnOrder, sdlist);
		result = SUCCESS;
	}

	return "returnOrder";
	}
	public String getSalesDetail(){
		String salesId = getRequest().getParameter("salesId");
		Sales sales = null;
		if(StringUtils.isNotBlank(salesId)){
			sales = getManager().get(Integer.parseInt(salesId));
		}
		if(null!=sales){
			List<SalesDetail> list = salesDetailManager.getDetails(sales.getId());
			if(null!=list && list.size()>0){
				sales.setSdList(list);
			}
		}
		return "sales";
	}
	/**
	 * 退货单 (支付宝、现金) json 数据，字段对应看Sales对象
	 * 
	 * @return
	 */
	public String returnOrderIndex() {

	Page page = PageUtil.getPage(Integer.valueOf(pageNumber),
		Integer.valueOf(pageCount));
	StringBuffer sql = new StringBuffer("from Sales s where s.status = ?");
	List args = new ArrayList();
	// 设置单子状态为"订单"
	args.add(SalesConstants.FULL_RETURN);

	User user = userManager
		.get(Integer.valueOf(getRequest().getParameter("userId")));
	if (user != null) {
		getModel().setUser(user);
		sql.append(" and s.user.id = ? ");
		args.add(user.getId());
	}

	// 退款状态查询
	if (StringUtils.isNotBlank(ckzt)) {
		sql.append(" and s.ckzt = ?");
		args.add(ckzt);

	}

	sql.append(" order by s.createTime desc, s.salesNo desc");
	page = getManager().pageQuery(page, sql.toString(), args.toArray());
	list = page.getData();
	System.out.println(sql);
	return "returnOrderIndex";
	}

	/**
	 * 查看订单信息 请求参数：orderId 订单id 返回数据：json数组，数组中的数据对应看SalesDetail和Products
	 */
	@Override
	public String view() {
	String orderId = getRequest().getParameter("orderId");
	if (StringUtils.isNotBlank(orderId)) {
		salesDetailList = salesDetailManager.getDetails(Integer.valueOf(orderId));
		for (SalesDetail salesDetail : salesDetailList) {
			if(null!=salesDetail.getProducts()) {
				salesDetail.setProductsJSON(salesDetail.getProducts().productsJson());
			}
		salesDetail.setMerId(salesDetail.getSales().getMerId().toString());
		salesDetail.setSaleNo(salesDetail.getSales().getSalesNo());
		salesDetail.setAmount(salesDetail.getSales().getSamount());
		}
	}
	return VIEW;
	}
	/**
	 * 修改订单支付方式和订单地址id
	 * 参数orderid,addrId,payment
	 * */
	public String updateSales(){
		try {
			String orderId = getRequest().getParameter("orderId");
			Sales sale=getManager().get(Integer.parseInt(orderId));
			sale.setStatus("3");
			if(payment!=null&&!"".equals(payment)){
				sale.setPayment(Payment.valueOf(payment));
				if(payment.equals(Payment.CASH.toString())){
					sale.setPayState(true);
				}
			}
			sale.setAddress(receiveAddressManager.get(Integer.parseInt(addrId)));
			getManager().update(sale);
			User merUser = sale.getMerUser();
			User appUser = merUser.getShopOfUser();
			if(null!=merUser && null!=merUser.getAllMoney()){
				merUser.setAllMoney(merUser.getAllMoney()+sale.getSpayamount());
			}else{
				merUser.setAllMoney(sale.getSpayamount());
			}
			if(null!=appUser && null!=appUser.getAllMoney()){
				appUser.setAllMoney(appUser.getAllMoney()+sale.getSpayamount());
			}else{
				appUser.setAllMoney(sale.getSpayamount());
			}
			userManager.update(merUser);
			userManager.update(appUser);
			resultMessage.put("result", true);
			sendTsong(sale);
		} catch (NumberFormatException e) {
			resultMessage.put("result", false);
			e.printStackTrace();
		}
		return "save";
	}
		//余额支付接口
	public String yeZfupdate(){
		try {
			String orderId = getRequest().getParameter("orderId");
			Sales sale=getManager().get(Integer.parseInt(orderId));
			User xfzUser = sale.getUser();
			double fxAccount = 0.0;
			double chargeAccount = 0.0;
			System.out.println("---------------------xfzUser.getId()="+xfzUser.getId());
			if(null!=xfzUser.getFxMoney()){
				fxAccount = xfzUser.getFxMoney();
				System.out.println("---------------------xfzUser.getFxMoney()="+xfzUser.getFxMoney());
			}
			if(null!=xfzUser.getChargeAccount()){
				chargeAccount = xfzUser.getChargeAccount();
				System.out.println("---------------------xfzUser.getChargeAccount()="+xfzUser.getChargeAccount());
			}
			double keyong = chargeAccount+fxAccount;

			//余额不足的情况
			if( sale.getSamount()>keyong && payment.equals(Payment.YEZF.toString())){
				resultMessage.put("result","2");
			}else{
				sendTsong(sale);
				sale.setStatus("2");
				sale.setPayState(true);
				if(payment!=null&&!"".equals(payment)){
					sale.setPayment(Payment.valueOf(payment));
					if(payment.equals(Payment.YEZF.toString())){
						sale.setPayState(true);
						if(null!=xfzUser.getAllMoney()) {
							xfzUser.setAllMoney(xfzUser.getAllMoney() - sale.getSamount());
						}
						if(fxAccount>sale.getSamount() || fxAccount==sale.getSamount()){
							xfzUser.setFxMoney(fxAccount - sale.getSamount());
						}else{
							xfzUser.setFxMoney(0.0);
							xfzUser.setChargeAccount(keyong - sale.getSamount());
						}
					}
				}
				sale.setSpayamount(sale.getSamount());
				if(StringUtils.isNotBlank(addrId)) {
					sale.setAddress(receiveAddressManager.get(Integer.parseInt(addrId)));
				}
				getManager().update(sale);
				//给分拥用户加钱
				if(null!=sale.getUser() && null!=sale.getUser().getTjmUser()) {
					salesManager.ctrlCzFx(sale,sale.getUser().getTjmUser().getId() + "", sale.getSamount(), "1");
				}
				User merUser = sale.getMerUser();
				User appUser = merUser.getShopOfUser();
				if(null!=merUser && null!=merUser.getAllMoney()){
					merUser.setAllMoney(merUser.getAllMoney()+sale.getSpayamount());
				}else{
					merUser.setAllMoney(sale.getSpayamount());
				}
				if(null!=merUser&&null!=merUser.getIncomeAll()){
					merUser.setIncomeAll(merUser.getIncomeAll()+sale.getSpayamount());
				}else{
					merUser.setIncomeAll(sale.getSpayamount());
				}
				if(null!=appUser && null!=appUser.getAllMoney()){
					appUser.setAllMoney(appUser.getAllMoney()+sale.getSpayamount());
				}else{
					appUser.setAllMoney(sale.getSpayamount());
				}
				if(null!=appUser&&null!=appUser.getIncomeAll()){
					appUser.setIncomeAll(appUser.getIncomeAll()+sale.getSpayamount());
				}else{
					appUser.setIncomeAll(sale.getSpayamount());
				}
				userManager.update(merUser);
				userManager.update(appUser);
				resultMessage.put("result", "1");
			}
		} catch (NumberFormatException e) {
			resultMessage.put("result", "3");
			e.printStackTrace();
		}
		return "save";
	}

	public static void sendTsong(Sales sale){
		String salesType = sale.getSalesType();
		PhoneAgentAction pa = new PhoneAgentAction();
		String salesName = "";
		if(salesType.equals("jy")){
			salesName = "加油";
		}
		if(salesType.equals("bx")){
			salesName = "保险";
		}
		if(salesType.equals("wx")){
			salesName = "维修";
		}
		String pushContent = "用户"+sale.getUser().getLoginId()+"已对"+salesName+"订单"+sale.getSalesNo()+"付款完成";
		pa.pushUser(sale.getMerUser().getShopOfUser().getId()+"",pushContent);

	}
	/**
	 * 现金支付
	 * 
	 * @return
	 */
	public String cashPay() {

	boolean validateCodeResult = false;// validateCodeResult
										// false验证码错误，true验证码正确
	String validateCode = getRequest().getParameter("validateCode");// 现金支付商家收到的验证码
//	String userId = getRequest().getParameter("userId");// 用户id
//	User user = userManager.get(Integer.valueOf(userId));// 用户对象

	ServletContext servletContext = getRequest().getSession()
		.getServletContext();
	Object sessionValidateCode = servletContext.getAttribute(orderId);

	System.out.println(orderId +"-------"+sessionValidateCode+"=========================》》》2" + validateCode);

	if (null != sessionValidateCode
		&& validateCode.equals(sessionValidateCode.toString())) {
		validateCodeResult = true;
	}

	resultMessage.put("validateCodeResult", validateCodeResult);// 将验证的状态放入返回信息中

	if (validateCodeResult) {
		if (StringUtils.isNotBlank(orderId)) {
		Sales sales = getManager().get(Integer.valueOf(orderId));
		sales.setPayState(true);
		getManager().update(sales);// 更新付款状态为成功
		resultMessage.put("payState", true);// 将付款状态放入返回信息中
		}
	}
	return "cashPay";
	}

	/**
	 * 现金支付，发送短信验证方法
	 * 
	 * @return
	 */
	public String sendPaySMS() {
	boolean result = true;
	User user = userManager.get(userId);
	// 给用户绑定的地区关联的商家发送短信
	List<User> shangJiaList = userManager
		.queryRegionUser(user.getRegion().getId());
	if (null != shangJiaList && shangJiaList.size() > 0) {

		final String mobile = shangJiaList.get(0).getMobile();// 商家手机号
		String validateCodeStr = Scsjs.scsjs();// 随机生成的验证码
		SDKTestSendTemplateSMS.smsSend(mobile, SMSConstants.VALIDATE_CODE,
			new String[] { validateCodeStr, SMSConstants.EFFECTIVE_MINUTE });
		final ServletContext servletContext = getRequest().getSession()
			.getServletContext();
		servletContext.setAttribute(orderId, validateCodeStr);
		new Thread(new Runnable() {
		@Override
		public void run() {
			try {
			Thread.sleep(1000 * 60 * 60
				* Integer.valueOf(SMSConstants.EFFECTIVE_MINUTE));
			servletContext.removeAttribute(mobile);
			} catch (InterruptedException e) {
			e.printStackTrace();
			} // 10秒
		}
		}).start();
	}
	resultMessage.put("result", result);

	return "sendPaySMS";
	}
	public String upDatewxSales(){
		//配件费用总计
		String pjAllMoney = getRequest().getParameter("pjAllMoney");
		//工时费
		String timeMoney = getRequest().getParameter("timeMoney");
		//总费用
		String allMoneys = getRequest().getParameter("allMoneys");
		String salesId = getRequest().getParameter("salesId");
		String carType = getRequest().getParameter("carType");
		Sales sales = null;
		if(StringUtils.isNotBlank(salesId)){
			sales = getManager().get(Integer.parseInt(salesId));
			sales.setCarType(carType);
			sales.setStatus("1");
			if(StringUtils.isNotBlank(allMoneys)) {
				sales.setSamount(Double.valueOf(allMoneys));
			}
			if(StringUtils.isNotBlank(timeMoney)) {
				sales.setTimeMoneys(Double.valueOf(timeMoney));
			}
			if(StringUtils.isNotBlank(pjAllMoney)) {
				sales.setPjTotalMoney(Double.valueOf(pjAllMoney));
			}
		}
		//配件名称用,逗号隔开
		String pjNames = getRequest().getParameter("pjNames");
		String[] pjNameArray = pjNames.split(",");
		//每种配件产地
		String growPlaces = getRequest().getParameter("growPlaces");
		String[] growPlacesArray = growPlaces.split(",");
		//每种配件规格
		String standards = getRequest().getParameter("standards");
		String[] standardsArray = standards.split(",");
		//每种配件数量
		String counts = getRequest().getParameter("counts");
		String[] countsArray = counts.split(",");
		//每种配件价格
		String moneys = getRequest().getParameter("moneys");
		String[] moneysArray = moneys.split(",");
		List<SalesDetail> sdList = new ArrayList<SalesDetail>();
		for(int i=0;i<pjNameArray.length;i++){
			SalesDetail sd = new SalesDetail();
			sd.setPjName(pjNameArray[i]);
			sd.setCount(Integer.parseInt(countsArray[i]));
			sd.setStandard(standardsArray[i]);
			sd.setGrowPlace(growPlacesArray[i]);
			sd.setOutPrice(Double.valueOf(moneysArray[i]));
			sd.setSales(sales);
			sd.setSaleNo(sales.getSalesNo());
			sdList.add(sd);
		}
		getManager().updateOrderAddDetail(sales,sdList);
		PhoneAgentAction paa = new PhoneAgentAction();
		//您此次维修账单金额为100,订单号为XD-201609271130020001
		paa.pushUser(sales.getUser().getId()+"","您此次维修账单金额为"+allMoneys+",订单号为"+sales.getSalesNo());
		resultMessage.put("msg",SUCCESS);
		return "save";

	}
	/**
	 * 保存订单信息
	 * 提交参数：userId用户id；
	 *  allMoneys充值总金额
	 * /salesOrderPhone/save.do
	 */
	public String saveChargeOrder(){
		String userId = getRequest().getParameter("userId");
		User user = null;
		if(StringUtils.isNotBlank(userId)){
			user = userManager.get(Integer.parseInt(userId));
		}
		if(null!=user){
			getModel().setUser(user);
		}
		getModel().setCreateTime(new Date());
		getModel().setSalesType("charge");
		String spayamount = getRequest().getParameter("spayamount");
		// 生成订单号
		getModel().setSalesNo(getManager().getOrderNumber(SalesConstants.ORDERS,
			/* user.getId() */32768));
		getModel().setSamount(Double.valueOf(spayamount));
		getManager().save(getModel());
		Sales order = this.getManager().findObject(
				"from Sales o where o.salesNo = ?", getModel().getSalesNo());
		resultMessage.put("result", true);
		resultMessage.put("orderNo", order.getSalesNo());
		resultMessage.put("spayamount",order.getSpayamount());
		return "save";

	}
	//返现明细 包含充值返现  和    分拥
	public String getMyFanxianRecord(){
		String userId = getRequest().getParameter("userId");
		String sql = " select id,zzJe as spayamount,date_format(create_time,'%Y.%m.%d') as createTime from zhuan_zhang_record where user_id="+Integer.parseInt(userId)+" and (type = '0' or type='1') order by create_time desc   ";
		userList = jdbcTemplate.queryForList(sql);
		return "userList";
	}
	public String getCzFyByType(){
		String userId = getRequest().getParameter("userId");
		String type = getRequest().getParameter("type");
		String sql = " select id,jyje as spayamount,date_format(create_time,'%Y.%m.%d') as createTime from zhuan_zhang_record where user_id="+Integer.parseInt(userId)+" and type='"+type+"' order by create_time desc   ";
		userList = jdbcTemplate.queryForList(sql);
		return "userList";
	}
	//收入明细 包含订单详细  和    转账明细
	public String getMyIncomeRecord(){
		String userId = getRequest().getParameter("userId");
		String sql = "select * from ( select a.id,a.spayamount,date_format(a.create_time,'%Y.%m.%d') as createTime from sales a left join users b on a.meruser_id=b.id where b.shop_user_id="+Integer.parseInt(userId)+" and a.pay_state = '1' and a.sales_type!='charge'  UNION   ";
		sql += " select id,zzje as spayamount,date_format(create_time,'%Y.%m.%d') as createTime from  zhuan_zhang_record where to_userid = "+Integer.parseInt(userId)+" )a order by a.createTime desc ";
		userList = jdbcTemplate.queryForList(sql);
		return "userList";
	}
	//余额明细 也就是我的充值记录
	public String getMyChargeRecord(){
		String userId = getRequest().getParameter("userId");
		String sql = " select a.id,a.spayamount,date_format(a.create_time,'%Y.%m.%d') as createTime from sales a left join users b on a.user_id=b.id  where b.shop_user_id="+Integer.parseInt(userId)+" and pay_state = '1' and sales_type='charge' order by create_time desc ";
		userList = jdbcTemplate.queryForList(sql);
		return "userList";
	}
	//余额明细 也就是我的充值记录
	public String getMyOrderRecords(){
		String userId = getRequest().getParameter("userId");
		String type = getRequest().getParameter("type");
		String sql = " select a.id,a.spayamount,date_format(a.create_time,'%Y.%m.%d') as createTime from sales a left join users b on a.meruser_id=b.id  where b.shop_user_id="+Integer.parseInt(userId)+" and pay_state = '1' and sales_type='"+type+"' order by create_time desc ";
		System.out.println("------------------sql="+sql);
		userList = jdbcTemplate.queryForList(sql);
		return "userList";
	}
	public String updateBxOrderPrice(){
		String orderId = getRequest().getParameter("orderId");
		String price = getRequest().getParameter("price");
		if(StringUtils.isNotBlank(orderId)){
			Sales sales = getManager().get(Integer.parseInt(orderId));
			sales.setSamount(Double.valueOf(price));
			getManager().update(sales);
			resultMessage.put("msg", true);
		}
		return "save";
	}
	public String updateOrder() {
		boolean checkNumflag=true;
		try {
			String salesNo = getRequest().getParameter("salesNo");
			System.out.println("-----------------salesNo="+salesNo);
			Sales order = this.getManager().findObject(
					"from Sales o where o.salesNo = ?", salesNo);
			System.out.println("--------------order="+order);
			String ifkfp = getRequest().getParameter("ifkfp");
			if(StringUtils.isNotBlank(ifkfp)){
				order.setSqkfp(ifkfp);
			}
			//订单状态  0 开始下单 1 司机接单
			order.setStatus("0");
			order.setCkzt(SalesConstants.SALES_NO);
			order.setRemark(getRequest().getParameter("name"));
			//订单总金额
			String allMoneys = getRequest().getParameter("allMoneys");
			String cnt = getRequest().getParameter("count");
			if(StringUtils.isNotBlank(cnt)){
				double count = Double.valueOf(cnt);
				// 订单中的商品总数量
				order.setCount(count);
				order.setTtno(count);
			}
			// 应收金额
			if(StringUtils.isNotBlank(allMoneys)) {
				order.setSamount(Double.valueOf(allMoneys));
			}
			if(checkNumflag){
				this.getManager().update(order);
				resultMessage.put("result", true);
			}else{
				resultMessage.put("result", false);
				return "save";
			}
		} catch (Exception e) {
			resultMessage.put("result", false);
			e.printStackTrace();
		}
		return "save";
	}
	/**
	 * 保存订单信息
	 * 提交参数：userId用户id； ypId 油品id
	 * 英文逗号隔开的单种商品价格字符串） allMoneys总金额；count 油的升数
	 * name :要加油种类名
	 * 返回json数据：{"result":true,"orderNo":"xxxxxxx","samount":30.01,"counts":2.6(多少升)}
	 * result操作状态true成功，false失败；orderNo订单号，字符串；samount订单金额，浮点型；counts订单商品总数，整型
	 * /salesOrderPhone/save.do
	 */
	@Override
	public String save() {
		boolean checkNumflag=true;
		String checkInfo="";
	try {
		User user = userManager.get(userId);
		if (null == user) {
			resultMessage.put("result", false);
			return "save";
		}
		System.out.println("----------------longitude"+longitude);
		if(null!=longitude){
			getModel().setLocX(bcLw(longitude));
		}
		if(null!=latitude){
			getModel().setLocY(bcLw(latitude));
		}
		System.out.println("----------------latitude"+latitude);
		getModel().setPosition(getRequest().getParameter("position"));
		getModel().setYpPrice(getRequest().getParameter("ypPrice"));
		System.out.println("----------------position"+getRequest().getParameter("position"));
		String mId = getRequest().getParameter("merId");
		System.out.println("----------------mId"+mId);
		User merUser = null;
		if(StringUtils.isNotBlank(mId))
		{
			System.out.println("----------------mId"+mId);
			Integer merId = Integer.parseInt(mId);
			//获取商家对象
			merUser = userManager.get(merId);
			getModel().setMerId(merId);
			getModel().setMerUser(merUser);
		}
		String userId = getRequest().getParameter("userId");
//		String ifkfp = getRequest().getParameter("ifkfp");
//		if(StringUtils.isNotBlank(ifkfp)){
//			getModel().setSqkfp(ifkfp);
//		}
		getModel().setSqkfp("0");
		System.out.println("----------------userId"+userId);
		if(StringUtils.isNotBlank(userId)) {
			System.out.println("----------------userId"+userId);
			getModel().setUser(userManager.get(Integer.parseInt(userId)));
		}
		//订单状态  0 开始下单 1 司机接单
		getModel().setStatus("0");
		getModel().setCkzt(SalesConstants.SALES_NO);
		getModel().setRemark(getRequest().getParameter("name"));
		// 生成订单号
		getModel().setSalesNo(getManager().getOrderNumber(SalesConstants.ORDERS,
			/* user.getId() */32768));
		//订单总金额
		String allMoneys = getRequest().getParameter("allMoneys");
		String cnt = getRequest().getParameter("count");
		if(StringUtils.isNotBlank(cnt)){
			double count = Double.valueOf(cnt);
			// 订单中的商品总数量
			getModel().setCount(count);
			getModel().setTtno(count);
		}
		// 下单时间
		getModel().setCreateTime(new Date());
		String salesType = getRequest().getParameter("salesType");
		getModel().setSalesType(salesType);
		// 应收金额
		if(StringUtils.isNotBlank(allMoneys)) {
			getModel().setSamount(Double.valueOf(allMoneys));
		}
		if(checkNumflag){
			this.getManager().save(getModel());
			System.out.println("---------------------------merUser="+merUser);
			if(null!=merUser) {
				String salesName = "";
				if(salesType.equals("jy")){
					salesName = "加油";
				}
				if(salesType.equals("bx")){
					salesName = "保险";
				}
				if(salesType.equals("wx")){
					salesName = "维修";
				}
				String pushContent = merUser.getShopOfUser().getLoginId()+ "您好用户" + userManager.get(Integer.parseInt(userId)).getLoginId() + "向您提交了"+salesName+"订单";
				pushUser(merUser.getShopOfUser().getId() + "", pushContent);
			}
			Sales order = this.getManager().findObject(
					"from Sales o where o.salesNo = ?", getModel().getSalesNo());
			resultMessage.put("result", true);
			resultMessage.put("orderNo", order.getSalesNo());
		}else{
			resultMessage.put("result", false);
			return "save";
		}
	} catch (Exception e) {
		resultMessage.put("result", false);
		e.printStackTrace();
	}
	return "save";
	}
	public String removeXiaoXi(){
		String salesId = getRequest().getParameter("salesId");
		try {
			if(StringUtils.isNotBlank(salesId)){
				Sales sales = getManager().get(Integer.parseInt(salesId));
				if(null!=sales){
					List<SalesDetail> sdlist = salesDetailManager.getDetails(sales.getId());
					if(null!=sdlist && sdlist.size()>0){
						for(SalesDetail sd : sdlist){
							salesDetailManager.remove(sd);
						}
					}
					List<MerchantValidations> mvList = merchantValidationManager.getMvListBySalesId(sales.getId());
					if(null!=mvList&&mvList.size()>0){
						for(MerchantValidations mv: mvList){
							merchantValidationManager.remove(mv);
						}
					}
					List<ZhuanZhangRecord> zzList = zhuanZhangRecordManager.getZZList(sales.getId());
					if(null!=zzList&&zzList.size()>0){
						for(ZhuanZhangRecord zzr : zzList){
							zhuanZhangRecordManager.remove(zzr);
						}
					}
					getManager().remove(sales);
				}
			}
			resultMessage.put("result", true);
		}
		catch (Exception e){
			resultMessage.put("result", false);
			e.printStackTrace();
		}
		return "save";
	}
	/**
	 * 提交参数：merId 商家id,userId用户id；pid商品id，比如：12321,988123（英文逗号隔开的商品id字符串）；outprice商品单价，比如：
	 * 30.01,20.1（英文逗号隔开的商品单价字符串）
	 * counts商品数量，比如：2,3（英文逗号隔开的单种商品数量字符串）；money商品价格，比如：60.02,60.3（
	 * 英文逗号隔开的单种商品价格字符串） allMoneys总金额；
	 *
	 * 返回json数据：{"result":true,"orderNo":"xxxxxxx","samount":30.01,"counts":2}
	 * result操作状态true成功，false失败；orderNo订单号，字符串；samount订单金额，浮点型；counts订单商品总数，整型
	 */

	public String saveBxd() {
		boolean checkNumflag=true;
		String checkInfo="";
		try {
			User user = userManager.get(userId);
			if (null == user) {
				resultMessage.put("result", false);
				return "save";
			}
			Integer merId=Integer.parseInt(getRequest().getParameter("merId"));
			//获取商家对象
			User merUser=userManager.get(merId);
			getModel().setMerId(merId);
			getModel().setMerUser(merUser);
			getModel().setUser(userManager.get(userId));
			getModel().setStatus(SalesConstants.ORDERS);
			getModel().setCkzt(SalesConstants.SALES_NO);
			// 生成订单号
			getModel().setSalesNo(getManager().getOrderNumber(SalesConstants.ORDERS,
			/* user.getId() */32768));
			// 商品总数量
			int count = 0;
			// 保存订单明细
			// 商品id
			String[] pids = this.getRequest().getParameter("pid").split(",");
			// 商品单位id
			// String[] unitids = this.getRequest().getParameterValues("unitid");
			// 商品实际出库价
			String[] outprices = this.getRequest().getParameter("outprice")
					.split(",");
			// 单个商品的总数量
			// String[] ncounts =
			// this.getRequest().getParameter("ncount").split(",");
			String[] counts = this.getRequest().getParameter("counts").split(",");
			// 单个商品的总价
			String[] moneys = this.getRequest().getParameter("money").split(",");
			// String[] remarks = this.getRequest().getParameterValues("sremark");
			List<SalesDetail> sdlist = new ArrayList<SalesDetail>();
			//订单总金额
			String allMoneys = getRequest().getParameter("allMoneys");
			for (int i = 0; i < pids.length; i++) {
				SalesDetail sd = new SalesDetail();
				sd.setAmount(Double.parseDouble(moneys[i]));
				// sd.setNcount(Float.parseFloat(ncounts[i]));
				sd.setCount(Integer.parseInt(counts[i]));
				sd.setTnorootl(sd.getCount());
				sd.setOutPrice(Double.parseDouble(outprices[i]));
				Products products = this.getManager().getDao().get(Products.class,
						Integer.parseInt(pids[i]));
				sd.setProducts(products);
				sd.setSales(getModel());
				count += Integer.parseInt(counts[i]);
				sdlist.add(sd);
			}
			// 订单中的商品总数量
			getModel().setCount(Double.valueOf(count));
			// 下单时间
			getModel().setCreateTime(new Date());
			// 应收金额
			getModel().setSamount(Double.valueOf(allMoneys));
			String salesType = getRequest().getParameter("salesType");
			getModel().setSalesType(salesType);
			System.out.println("000000000000");
			System.out.println("000000000000000000");
			System.out.println("0000000000000000000000000000000000");
			if(checkNumflag){
				this.getManager().save(getModel(),sdlist);
				Sales order = this.getManager().findObject(
						"from Sales o where o.salesNo = ?", getModel().getSalesNo());
				attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
				System.out.println("添加保险信息11111111111111111111111111");
				System.out.println("22222222222222222222222222222222222222");
				System.out.println("22222222222222222222222222222222222222");
				System.out.println("22222222222222222222222222222222222222");
				System.out.println("22222222222222222222222222222222222222");
				System.out.println("33333333333333333333333333333333333333333");
				System.out.println("33333333333333333333333333333333333333333");
				System.out.println("33333333333333333333333333333333333333333");
				System.out.println("33333333333333333333333333333333333333333");
				System.out.println("33333333333333333333333333333333333333333");
				System.out.println("33333333333333333333333333333333333333333");
				System.out.println("33333333333333333333333333333333333333333");
				System.out.println("33333333333333333333333333333333333333333");

				if(null!=files && files.length>0){
					System.out.println("添加保险信息11111111111111111111111111files.length"+files.length);
					for(int i=0;i<files.length;i++){
						System.out.println("444444444444444444444444444444444");
						RzFile rzFile = new RzFile();
						TouBaoRen tbr = new TouBaoRen();
						tbr.setCreateTime(new Date());
						String filePath = UpLoadUtil.doUpload(files[i], filesFileName[i], attchFolder,
								getServletContext());
						String[] array = filesFileName[i].split(",");
						System.out.println("----------------投保人信息"+filesFileName[i]);
						tbr.setIdCard(array[1]);
						System.out.println("----------------身份证号"+array[1]);
						tbr.setTbName(array[0]);
						System.out.println("----------------姓名"+filesFileName[i]);
						tbr.setUser(userManager.get(userId));
						tbr.setSales(order);
						rzFile.setImageUrl(filePath);
						rzFile.setCreateTime(new Date());
						rzFile.setFileType(array[2]);
						TouBaoRen tempTbr = touBaoRenManager.getByIdCardAndOrderId(array[1],order.getId()+"");
						if(null==tempTbr) {
							touBaoRenManager.save(tbr);
							TouBaoRen tbr1 = touBaoRenManager.getByIdCardAndOrderId(array[1],order.getId()+"");
							rzFile.setTouBaoRen(tbr1);
						}else{
							rzFile.setTouBaoRen(tempTbr);
						}
						rzFileManager.save(rzFile);
					}
				}
				if(null!=merUser) {
					String pushContent = "您好，你有一条新的保险订单，请查看，订单号为："+getModel().getSalesNo();
					pushUser(merUser.getShopOfUser().getId() + "", pushContent);
				}
				resultMessage.put("result", true);
				resultMessage.put("orderId", order.getId());// 订单id
				resultMessage.put("orderNo",getModel().getSalesNo());
				resultMessage.put("totalPrice",allMoneys);
				resultMessage.put("counts",count);
				resultMessage.put("orderTime",getModel().getCreateTime());
			}else{
				resultMessage.put("result", false);
				resultMessage.put("errorInfo", checkInfo);
				return "save";
			}
		} catch (Exception e) {
			resultMessage.put("result", false);
			e.printStackTrace();
		}
		return "save";
	}
	public static  PushPayload buildPushObject_all_alias_alert(String alias, String alert,Map<String,String> extras) {
		return PushPayload.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.alias(alias))
				.setNotification(Notification.newBuilder()
						.setAlert(alert)
						.addPlatformNotification(AndroidNotification.newBuilder()
								.addExtras(extras).build())
						.addPlatformNotification(IosNotification.newBuilder()
								.incrBadge(1)
								.addExtras(extras).build())
						.build())
				.build();
	}
	public static void pushUser(String alias,String alert){
		ClientConfig config = ClientConfig.getInstance();
		// Setup the custom hostname
		//1加油 2 维修 3 保险 4 后台
		String type = "4";
		String typeName = "后台";
		if(org.apache.commons.lang.xwork.StringUtils.isNotBlank(alert)){
			if(alert.indexOf("加油")>-1){
				type = "1";
				typeName = "加油";

			}
			if(alert.indexOf("维修")>-1){
				type = "2";
				typeName = "维修";
			}
			if(alert.indexOf("保险")>-1){
				type = "3";
				typeName = "保险";
			}
		}
		config.setPushHostName("https://api.jpush.cn");
		JPushClient jpushClient = new JPushClient(masterSecret, appKey,3,null,config);
		Map<String,String> extras = new HashMap<String, String>();
		extras.put(type,typeName);
		PushPayload pushPayload = buildPushObject_all_alias_alert(alias,alert,extras);
		try {
			PushResult result = jpushClient.sendPush(pushPayload);
			LOG.info("Got result - " + result);
		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ", e);
		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}
	//获得商家对象
	public User getMerObject(SalesDetail sd){
		return userManager.get(sd.getProducts().getUser().getId());
	}
	public Integer getUserId() {
	return userId;
	}

	public void setUserId(Integer userId) {
	this.userId = userId;
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

	public String getPageNumber() {
	return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
	this.pageNumber = pageNumber;
	}

	public String getPageCount() {
	return pageCount;
	}

	public void setPageCount(String pageCount) {
	this.pageCount = pageCount;
	}

	public String getSpayamount() {
	return spayamount;
	}

	public void setSpayamount(String spayamount) {
	this.spayamount = spayamount;
	}

	public Map<String, Object> getResultMessage() {
	return resultMessage;
	}

	public void setResultMessage(Map<String, Object> resultMessage) {
	this.resultMessage = resultMessage;
	}

	public List<SalesDetail> getSalesDetailList() {
	return salesDetailList;
	}

	public void setSalesDetailList(List<SalesDetail> salesDetailList) {
	this.salesDetailList = salesDetailList;
	}

	public String getPayState() {
	return payState;
	}

	public void setPayState(String payState) {
	this.payState = payState;
	}

	public String getCkzt() {
	return ckzt;
	}

	public void setCkzt(String ckzt) {
	this.ckzt = ckzt;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getAddrId() {
		return addrId;
	}
	public void setAddrId(String addrId) {
		this.addrId = addrId;
	}

	public List getMsgList() {
		return msgList;
	}

	public void setMsgList(List msgList) {
		this.msgList = msgList;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public List<User> getuList() {
		return uList;
	}

	public void setuList(List<User> uList) {
		this.uList = uList;
	}

	public Sales getOrder() {
		return order;
	}

	public void setOrder(Sales order) {
		this.order = order;
	}

	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}

	public String[] getFilesFileName() {
		return filesFileName;
	}

	public void setFilesFileName(String[] filesFileName) {
		this.filesFileName = filesFileName;
	}

	public String[] getFilesContentType() {
		return filesContentType;
	}

	public void setFilesContentType(String[] filesContentType) {
		this.filesContentType = filesContentType;
	}

	public List<Map<String, Object>> getUserList() {
		return userList;
	}

	public void setUserList(List<Map<String, Object>> userList) {
		this.userList = userList;
	}
}