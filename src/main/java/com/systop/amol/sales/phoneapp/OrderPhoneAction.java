package com.systop.amol.sales.phoneapp;

import com.alipay.AlipayEnum;
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
import com.systop.amol.user.agent.model.InfomationRecord;
import com.systop.amol.user.agent.phone.PhoneAgentAction;
import com.systop.amol.user.agent.service.ChargeRecordManager;
import com.systop.amol.user.agent.service.FxgzManager;
import com.systop.amol.user.agent.service.InfoMationRecordManager;
import com.systop.amol.user.agent.service.ReceiveAddressManager;
import com.systop.amol.util.NetTool;
import com.systop.amol.util.XMLUtil;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import com.weixin.tenpay.CommonUtil;
import com.weixin.tenpay.PackageRequestHandler;
import com.weixin.tenpay.PayCommonUtil;
import com.weixin.tenpay.PrepayIdRequestHandler;
import com.weixin.tenpay.util.ConstantUtil;
import com.weixin.tenpay.util.WXUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 手机端对接支付宝Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrderPhoneAction
	extends DefaultCrudAction<Sales, SalesOrderManager> {
	private final String   APPID="wxb8f14b099a9201d0";
	private final String   MUCHID="1382445602";
	@Resource
	private CustomerManager customerManager;
	@Resource
	private ProductsManager productsManager;
	@Autowired
	private ChargeRecordManager chargeRecordManager;
	@Autowired
	private ReceiveAddressManager receiveAddressManager;
	//@Resource 
	//PartnersManager partnersManager;
	@Resource
	private SalesDetailManager salesDetailManager;
	@Resource
	private InfoMationRecordManager infoMationRecordManager;
	@Resource
	FxgzManager fxgzManager;
	@Resource
	private UserManager userManager;
	@Resource
	private SalesManager salesManager;
	/** app用户id */
	private Integer userId;
	private String goodsJf;

	/** 返回状态 */
	private String result = INPUT;
	private JdbcTemplate jdbcTemplate;

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

	/** 商品集合 */
	private List<SalesDetail> salesDetailList;
	private List tempList = new ArrayList();
	public String minusJf(){
		String msg="";
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		if(Integer.parseInt(goodsJf)>user.getIntegral().intValue()){
			msg="error";
		}else{
			user.setIntegral(user.getIntegral().intValue()-Integer.parseInt(goodsJf));
			msg="ok";
		}
		userManager.update(user);
		return msg;
	}
	/**
	 * 退货 支付宝
	 * 提交参数：userId 用户id；orderId 订单号
	 * 
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
//				returnOrder.setCount(Integer.valueOf(counts));
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
	/**
	 * 微信异步回调地址，手机端填写此URL
	 * /orderPhoneAlipay/wxNotify.do
	 * @throws Exception 
	 */
	public String wxNotify() throws Exception {
	System.out.println("微信异步回调地址...");
	Map map = new HashMap();
	map = this.parseXml(getRequest());
	System.out.println("map = " + map);
	Set set = map.keySet();
	Iterator iterator = set.iterator();
	String key = null;
	while (iterator.hasNext()) {
		key = (String) iterator.next();
		System.out.println(key + " = " + map.get(key));
	}
	String out_trade_no = (String) map.get("out_trade_no");
	String transactionId = (String) map.get("transaction_id");
	String resultCode = (String) map.get("result_code");
	String returnCode = (String) map.get("return_code");
	String total_fee = (String) map.get("total_fee");
	Sales order = getManager().findOrder(out_trade_no);
	if (null != order && !"SUCCESS".equals(order.getReturn_code())) {
		// 更新订单的微信返回信息
		order.setTransaction_id(transactionId);
		order.setReturn_code(returnCode);
		order.setResult_code(resultCode);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		order.setAlipay_gmt_payment(formatter.format(new Date()));
		order.setPayment(Payment.WXPAY);
		System.out.println("-----order.getSamount()--------"+order.getSamount());
		System.out.println("-----total_fee--------"+total_fee);
		boolean flag =	AlipayEnum.TRADE_SUCCESS.toString().equals(returnCode);
		System.out.println("--------flag="+flag);
		System.out.println(AlipayEnum.TRADE_SUCCESS.toString());
		System.out.println("----------------returnCode--------"+returnCode);
		//if (order.getSamount().doubleValue()*100 == Double.parseDouble(total_fee)//***************************************金额比对，正式上线需放开注释
			//&& "SUCCESS".toString().equals(returnCode)) {
			System.out.println("校验成功,待支付。。。。。。");
		order.setSpayamount(Double.parseDouble(total_fee)/100);// 更新实收金额
		order.setPayState(true);//true支付状态为成功
		getManager().update(order);
		if(null!=order.getUser() && null!=order.getUser().getTjmUser()) {
			salesManager.ctrlCzFx(order,order.getUser().getTjmUser().getId() + "", Double.parseDouble(total_fee), "1");
		}
		try {
			getResponse().getWriter().write("success");//给支付宝返回success，通知支付宝服务器已成功处理支付宝的请求信息
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	//}
	return null;
	}
	/**
	 * 充值微信回调
	 * /orderPhoneAlipay/alipayNotify.do
	 * @throws Exception
	 */
	public String zxWxNotify() throws Exception {
		System.out.println("充值微信异步回调地址...");
		Map map = new HashMap();
		map = this.parseXml(getRequest());
		System.out.println("map = " + map);
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		String key = null;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			System.out.println(key + " = " + map.get(key));
		}
		String out_trade_no = (String) map.get("out_trade_no");
		String transactionId = (String) map.get("transaction_id");
		String resultCode = (String) map.get("result_code");
		String returnCode = (String) map.get("return_code");
		String total_fee = (String) map.get("total_fee");
		Sales order = getManager().findOrder(out_trade_no);
		if (null != order && !"SUCCESS".equals(order.getReturn_code())) {
			// 更新订单的微信返回信息
			order.setTransaction_id(transactionId);
			order.setReturn_code(returnCode);
			order.setResult_code(resultCode);
			order.setPayment(Payment.WXPAY);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			order.setAlipay_gmt_payment(formatter.format(new Date()));
			System.out.println("-----order.getSamount()--------"+order.getSamount());
			System.out.println("-----total_fee--------"+total_fee);
			boolean flag =	AlipayEnum.TRADE_SUCCESS.toString().equals(returnCode);
			System.out.println("--------flag="+flag);
			System.out.println(AlipayEnum.TRADE_SUCCESS.toString());
			System.out.println("----------------returnCode--------"+returnCode);
			//if (order.getSamount().doubleValue()*100 == Double.parseDouble(total_fee)//***************************************金额比对，正式上线需放开注释
			//&& "SUCCESS".toString().equals(returnCode)) {
			System.out.println("校验成功,待支付。。。。。。");
			order.setSpayamount(Double.parseDouble(total_fee)/100);// 更新实收金额
			order.setPayState(true);//true支付状态为成功
			getManager().update(order);
			User user = order.getUser();
			if(null!=user && null!=user.getAllMoney()){
				user.setAllMoney(user.getAllMoney()+Double.parseDouble(total_fee)/100);
			}else{
				user.setAllMoney(Double.parseDouble(total_fee)/100);
			}
			if(null!=user && null!=user.getChargeAccount()){
				user.setChargeAccount(user.getChargeAccount()+Double.parseDouble(total_fee)/100);
			}else{
				user.setChargeAccount(Double.parseDouble(total_fee)/100);
			}
			userManager.update(user);
			salesManager.ctrlCzFx(order,user.getId()+"",Double.parseDouble(total_fee)/100,"0");
			try {
				getResponse().getWriter().write("success");//给支付宝返回success，通知支付宝服务器已成功处理支付宝的请求信息
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//}
		return null;
	}
	public String wxCzInter(){
		String orderNo = getRequest().getParameter("oderNo");
		String total_fee = getRequest().getParameter("total_fee");
		Sales order = getManager().findOrder(orderNo);
		order.setPayment(Payment.WXPAY);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		order.setAlipay_gmt_payment(formatter.format(new Date()));
		order.setSpayamount(Double.parseDouble(total_fee));// 更新实收金额
		order.setPayState(true);//true支付状态为成功
		getManager().update(order);
		User user = order.getUser();
		if(null!=user && null!=user.getAllMoney()){
			user.setAllMoney(user.getAllMoney()+Double.parseDouble(total_fee));
		}else{
			user.setAllMoney(Double.parseDouble(total_fee));
		}
		userManager.update(user);
		salesManager.ctrlCzFx(order,user.getId()+"",Double.parseDouble(total_fee),"0");
		resultMessage.put("msg",SUCCESS);
		return "save";
	}



	//根据商家查询该用户需要开发票的订单
	public String getKfpOrders(){
		String userId = getRequest().getParameter("userId");
		String merId = getRequest().getParameter("merId");
		StringBuffer sql = new StringBuffer(
				"from Sales s where  s.payState = "+true+" and s.sqkfp='0' ");
		if(StringUtils.isNotBlank(userId)){
			sql.append(" and s.user.id="+Integer.parseInt(userId)+" ");
		}
		if(StringUtils.isNotBlank(merId)){
			sql.append(" and s.merUser.id="+Integer.parseInt(merId)+" ");
		}
		list = getManager().query(sql.toString());
		for(Sales sales : list){
			sales.setCreateTimeStr(String.valueOf(sales.getCreateTime()).replace("T"," "));
		}
		return INDEX;
	}
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public String uuindex() {
		String userId = getRequest().getParameter("userId");
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct(b.id) as userId,b.login_id as loginId,b.address,b.name,b.nick_name as nickName,b.phone,b.image_url as imageURL from sales a LEFT JOIN users b on a.meruser_id=b.id ");
		sb.append(" where a.pay_state=1 and a.sq_kfp='0' and a.user_id="+Integer.parseInt(userId)+" ");
		System.out.println("-----------------------------"+sb.toString());
		tempList = jdbcTemplate.queryForList(sb.toString());
		return "uuindex";
	}
	public static Map<String, String> parseXml(HttpServletRequest request)
			throws Exception {
		// 解析结果存储在HashMap
		InputStream inputStream = request.getInputStream();
		String str = NetTool.getTextContent(inputStream, "UTF-8");

		Map map = XMLUtil.doXMLParse(str);

		return map;
	}
	//app微信支付前调用的接口

	/**
	 * timestamp 时间戳  appid  公众账号id(固定字段)   mch_id 商户号(固定字段)  nonce_str 随机字符串  prepay_id  预支付id   sign 签名
	 * @return
	 * APPID：wxb8f14b099a9201d0     商户号：1382445602
     */
	public String beforeWxPay() throws Exception{
		String salesId = getRequest().getParameter("salesId");
		String orderNo = getRequest().getParameter("orderNo");
		Sales sales = null;
		if(StringUtils.isNotBlank(salesId)) {
			sales=getManager().get(Integer.parseInt(salesId));
		}
		if(StringUtils.isNotBlank(orderNo)) {
			sales = this.getManager().findObject(
					"from Sales o where o.salesNo = ?",orderNo);
		}
		String timestamp = WXUtil.getTimeStamp();
		String appId = APPID;
		String mchId = MUCHID;
		String nonce_str = WXUtil.getNonceStr();
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(getRequest(), getResponse());//获取prepayid的请求类
		PackageRequestHandler packageReqHandler = new PackageRequestHandler(getRequest(), getResponse());//生成package的请求类
		////设置获取prepayid支付参数
		//获取package包
		String packageValue = packageReqHandler.getRequestURL();
		String traceid = "";
		prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID);
		prepayReqHandler.setParameter("appkey", ConstantUtil.APP_KEY);
		prepayReqHandler.setParameter("noncestr", nonce_str);
		prepayReqHandler.setParameter("package", packageValue);
		prepayReqHandler.setParameter("timestamp", timestamp);
		prepayReqHandler.setParameter("traceid", traceid);
		//获取prepayId
		String prepayid = prepayReqHandler.sendPrepay();
		String sign = prepayReqHandler.createSHA1Sign();
		resultMessage =CommonUtil.generateOrderInfoByWeiXinPay(sales,getRequest());
		return "save";

	}
	private String generateOrderInfoByWeiXinPay(String orderId,HttpServletRequest request,PrepayIdRequestHandler prepayReqHandler) throws Exception {
//		String notify_url = propertiesService.WEI_XIN_NOTIFY_URL;//回调地址
//		String uuid = IdGen.uuid();
//		System.out.print("uuid" + uuid);
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		signParams.put("appid", ConstantUtil.APP_ID);//app_id
		signParams.put("body", "测试");//商品参数信息
		signParams.put("mch_id", MUCHID);//微信商户账号
//		signParams.put("nonce_str", uuid);//32位不重复的编号
		signParams.put("notify_url", ConstantUtil.notify_url);//回调页面
		signParams.put("out_trade_no", orderId);//订单编号
		signParams.put("spbill_create_ip",request.getRemoteAddr() );//请求的实际ip地址
//		signParams.put("total_fee","1");//支付金额 单位为分
		signParams.put("trade_type", "APP");//付款类型为APP
//		String sign = PayCommonUtil.createSign("UTF-8", signParams);//生成签名
		String sign = prepayReqHandler.createSHA1Sign();
		signParams.put("sign", sign);
		signParams.remove("key");//调用统一下单无需key（商户应用密钥）
		String requestXml = PayCommonUtil.getRequestXml(signParams);//生成Xml格式的字符串
		String result = CommonUtil.httpsRequest(ConstantUtil.UNIFIED_ORDER_URL, "POST", requestXml);//以post请求的方式调用统一下单接口

		/*（注：ConstantUtil.UNIFIED_ORDER_URL=https://api.mch.weixin.qq.com/pay/unifiedorder;

		）*/


		//返回的result成功结果取出prepay_id：

		Map map = XMLUtil.doXMLParse(result);
		String return_code = (String) map.get("return_code");
		String prepay_id = null;
		String returnSign = null;
		String returnNonce_str = null;
		if (return_code.contains("SUCCESS")) {
			prepay_id = (String) map.get("prepay_id");//获取到prepay_id
		}
		StringBuffer weiXinVo = new StringBuffer();
		long currentTimeMillis = System.currentTimeMillis();//生成时间戳
		long second = currentTimeMillis / 1000L;//（转换成秒）
		String seconds = String.valueOf(second).substring(0, 10);//（截取前10位）
		SortedMap<String, String> signParam = new TreeMap<String, String>();
		signParam.put("appid", ConstantUtil.APP_ID);//app_id
		signParam.put("partnerid", ConstantUtil.PARTNER);//微信商户账号
		signParam.put("prepayid", prepay_id);//预付订单id
		signParam.put("package", "Sign=WXPay");//默认sign=WXPay
//		signParam.put("noncestr", uud);//uuid自定义不重复的长度不长于32位
		signParam.put("timestamp", seconds);//北京时间时间戳
//		String signAgain = PayCommon"Util.createSign("UTF-8", signParam);//再次生成签名
		String signAgain = prepayReqHandler.createSHA1Sign();
		signParams.put("sign", signAgain);
		weiXinVo.append("appid=").append(ConstantUtil.APP_ID).append("&partnerid=").append(ConstantUtil.PARTNER).append("&prepayid=").append(prepay_id).append("&package=Sign=WXPay").append("&noncestr=").append("&timestamp=").append(seconds).append("&sign=").append(signAgain);//拼接参数返回给移动端
		 return weiXinVo.toString();
	}
	public String updatePayState(){
		String orderNo = getRequest().getParameter("orderNo");
		String payType = getRequest().getParameter("payType");
		Sales sale = null;
		if(StringUtils.isNotBlank(orderNo)){
			sale =  getManager().findOrder(orderNo);
			if(null!=sale){
				sale.setSpayamount(sale.getSamount());// 更新实收金额
				//已经支付状态
				sale.setStatus("2");
				sale.setPayState(true);//true支付状态为成功p
				if(StringUtils.isNotBlank(payType)) {
					if(payType.equals("wxpay")) {
						sale.setPayment(Payment.WXPAY);
					}
					if(payType.equals("alipay")) {
						sale.setPayment(Payment.ALIPAY);
					}
				}
				salesManager.update(sale);
				if(null!=sale.getUser() && null!=sale.getUser().getTjmUser()) {
					salesManager.ctrlCzFx(sale,sale.getUser().getTjmUser().getId() + "", sale.getSamount(), "1");
				}
			}
			User merUser = sale.getMerUser();
			User appUser = merUser.getShopOfUser();
			if(null!=merUser && null!=merUser.getAllMoney()){
				merUser.setAllMoney(merUser.getAllMoney()+sale.getSpayamount());
			}else{
				merUser.setAllMoney(sale.getSpayamount());
			}
			//更新收入金额
			if(null!=merUser && null!=merUser.getIncomeAll()){
				merUser.setIncomeAll(merUser.getIncomeAll()+sale.getSpayamount());
			}else{
				merUser.setIncomeAll(sale.getSpayamount());
			}
			if(null!=appUser && null!=appUser.getAllMoney()){
				appUser.setAllMoney(appUser.getAllMoney()+sale.getSpayamount());
			}else{
				appUser.setAllMoney(sale.getSpayamount());
			}
			if(null!=appUser && null!=appUser.getIncomeAll()){
				appUser.setIncomeAll(sale.getSpayamount()+appUser.getIncomeAll());
			}else{
				appUser.setIncomeAll(sale.getSpayamount());
			}
			userManager.update(merUser);
			userManager.update(appUser);
			resultMessage.put("msg","success");
			PhoneAgentAction paa = new PhoneAgentAction();
			sendTsong(sale);
//			paa.pushUser(sale.getMerUser().getShopOfUser().getId()+"","用户"+sale.getUser().getId()+"已对订单"+sale.getSalesNo()+"付款完成");
			InfomationRecord ir = new InfomationRecord();
			ir.setSendContent("用户"+sale.getUser().getId()+"已对订单"+sale.getSalesNo()+"付款完成");
			ir.setCreateTime(new Date());
			ir.setTstype("3");
			ir.setXxtype("2");
			ir.setReceiveUser(sale.getMerUser());
			ir.setSendUser(sale.getUser());
			infoMationRecordManager.save(ir);
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
	 * 支付宝异步回调地址，手机端填写此URL
	 * /orderPhoneAlipay/alipayNotify.do
	 */
	public String alipayNotify() {
	System.out.println("支付宝异步回调地址...");
	getResponse().setContentType("text/html;charset=UTF-8");
	String trade_no = getRequest().getParameter("trade_no");// 支付宝交易号
	String buyer_email = getRequest().getParameter("buyer_email");// 买家支付宝账号
	String gmt_create = getRequest().getParameter("gmt_create");// 交易创建时间
	String gmt_payment = getRequest().getParameter("gmt_payment");// 交易付款时间
	String out_trade_no = getRequest().getParameter("out_trade_no");// 商户网站唯一订单号
	String trade_status = getRequest().getParameter("trade_status");// 交易状态
	String total_fee = getRequest().getParameter("total_fee");// 交易金额
	Sales order = getManager().findOrder(out_trade_no);
	if (null != order &&AlipayEnum.TRADE_SUCCESS.toString().equals(trade_status)&& !AlipayEnum.TRADE_SUCCESS.toString().equals(order.getAlipay_trade_status())) {
		// 更新订单的支付宝返回信息
		order.setAlipay_trade_no(trade_no);
		order.setAlipay_buyer_email(buyer_email);
		order.setAlipay_gmt_create(gmt_create);
		order.setAlipay_gmt_payment(gmt_payment);
		order.setAlipay_out_trade_no(out_trade_no);
		order.setAlipay_trade_status(trade_status);
		order.setAlipay_total_fee(total_fee);
		order.setPayment(Payment.ALIPAY);
		// 返回的支付宝交易金额与订单中的应收金额一致，并且支付状态为成功
		System.out.println(order.getSamount()+"H"+total_fee);
		System.out.println("order.getSamount().toString().equals(total_fee) = "+order.getSamount().toString().equals(total_fee));
		System.out.println("11111111111oooooooooookkkkkkkkkkkkkkkkk"+(order.getSamount().doubleValue() == Double.parseDouble(total_fee)));
		System.out.println("22222222222oooooooooookkkkkkkkkkkkkkkkk"+order.getSamount().doubleValue());
		System.out.println("33333333333oooooooooookkkkkkkkkkkkkkkkk"+Double.parseDouble(total_fee));
		System.out.println("323243242342343");
		//if (order.getSamount().doubleValue() == Double.parseDouble(total_fee)&& AlipayEnum.TRADE_SUCCESS.toString().equals(trade_status)) {
		System.out.println("111111111111111111111111111111111111");
		order.setSpayamount(Double.valueOf(total_fee));// 更新实收金额
		order.setPayState(true);//true支付状态为成功
		getManager().update(order);
		if(null!=order.getUser() && null!=order.getUser().getTjmUser()) {
			salesManager.ctrlCzFx(order,order.getUser().getTjmUser().getId() + "", Double.parseDouble(total_fee), "1");
		}
		try {
			getResponse().getWriter().write("success");//给支付宝返回success，通知支付宝服务器已成功处理支付宝的请求信息
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	return null;
	}
	public String alipayCzNotify() {
		System.out.println("支付宝异步回调地址...");
		getResponse().setContentType("text/html;charset=UTF-8");
		String trade_no = getRequest().getParameter("trade_no");// 支付宝交易号
		String buyer_email = getRequest().getParameter("buyer_email");// 买家支付宝账号
		String gmt_create = getRequest().getParameter("gmt_create");// 交易创建时间
		String gmt_payment = getRequest().getParameter("gmt_payment");// 交易付款时间
		String out_trade_no = getRequest().getParameter("out_trade_no");// 商户网站唯一订单号
		String trade_status = getRequest().getParameter("trade_status");// 交易状态
		String total_fee = getRequest().getParameter("total_fee");// 交易金额
		Sales order = getManager().findOrder(out_trade_no);
		System.out.println("----订单号="+out_trade_no+"trade_status"+trade_status);
		System.out.println("---------------order="+order);
		System.out.println((null != order)+"-----------!AlipayEnum.TRADE_SUCCESS.toString(="+!AlipayEnum.TRADE_SUCCESS.toString().equals(order.getAlipay_trade_status())+"-----"+AlipayEnum.TRADE_SUCCESS.toString().equals(trade_status));
		System.out.println("-=-=-=-能不能进判断"+(null != order &&AlipayEnum.TRADE_SUCCESS.toString().equals(trade_status)&& !AlipayEnum.TRADE_SUCCESS.toString().equals(order.getAlipay_trade_status())));
		if (null != order &&AlipayEnum.TRADE_SUCCESS.toString().equals(trade_status)&& !AlipayEnum.TRADE_SUCCESS.toString().equals(order.getAlipay_trade_status())) {
			// 更新订单的支付宝返回信息
			System.out.println("--------------------trade_no"+trade_no);
			order.setAlipay_trade_no(trade_no);
			System.out.println("--------------------buyer_email"+buyer_email);
			order.setAlipay_buyer_email(buyer_email);
			System.out.println("--------------------gmt_create"+gmt_create);
			order.setAlipay_gmt_create(gmt_create);
			System.out.println("--------------------gmt_payment"+gmt_payment);
			order.setAlipay_gmt_payment(gmt_payment);
			System.out.println("--------------------out_trade_no"+out_trade_no);
			order.setAlipay_out_trade_no(out_trade_no);
			System.out.println("--------------------trade_status"+trade_status);
			order.setAlipay_trade_status(trade_status);
			System.out.println("--------------------total_fee"+total_fee);
			order.setAlipay_total_fee(total_fee);
			// 返回的支付宝交易金额与订单中的应收金额一致，并且支付状态为成功
			order.setSpayamount(Double.valueOf(total_fee));// p更新实收金额
			order.setPayState(true);//true支付状态为成功
			order.setPayment(Payment.ALIPAY);
			getManager().update(order);
			System.out.println("订单更新成功");
			User user = order.getUser();
			System.out.println("更新前余额为="+user.getAllMoney());
			if(null!=user && null!=user.getAllMoney()){
				user.setAllMoney(user.getAllMoney()+Double.parseDouble(total_fee));
			}else{
				user.setAllMoney(Double.parseDouble(total_fee));
			}
			if(null!=user && null!=user.getChargeAccount()){
				user.setChargeAccount(user.getChargeAccount()+Double.parseDouble(total_fee));
			}else{
				user.setChargeAccount(Double.parseDouble(total_fee));
			}
			userManager.update(user);
			salesManager.ctrlCzFx(order,user.getId()+"",Double.parseDouble(total_fee),"0");
			System.out.println("用户余额更新成功现在余额为="+user.getAllMoney());
			try {
				getResponse().getWriter().write("success");//给支付宝返回success，通知支付宝服务器已成功处理支付宝的请求信息
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//}

		return null;
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

	public void setGoodsJf(String goodsJf) {
		this.goodsJf = goodsJf;
	}


	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List getTempList() {
		return tempList;
	}

	public void setTempList(List tempList) {
		this.tempList = tempList;
	}
}