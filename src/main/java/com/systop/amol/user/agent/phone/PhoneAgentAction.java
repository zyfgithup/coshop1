package com.systop.amol.user.agent.phone;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.systop.amol.app.push.model.PushMessage;
import com.systop.amol.app.push.service.PushMessageManager;
import com.systop.amol.base.product.ProductConstants;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.service.SalesManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.amol.user.agent.model.*;
import com.systop.amol.user.agent.service.*;
import com.systop.amol.util.SignUtil;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.util.bankcheck.BankCardBin;
import com.systop.core.util.bankcheck.checkBankCard;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;
import net.sf.json.JSONArray;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@SuppressWarnings({ "serial", "unused", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneAgentAction extends DefaultCrudAction<User, UserManager> {
	@Autowired
	private RegionManager regionManager;
	@Autowired
	private DistanceManager distanceManager;
	@Autowired
	private FanKuiToAppManager fanKuiToAppManager;
	@Autowired
	private ZhaoPinManager zhaoPinManager;
	@Autowired
	private CheLiangManager cheLiangManager;
	@Autowired
	private RzFileManager rzFileManager;
	@Autowired
	private JiayouSetManager jiayouSetManager;
	@Autowired
	private RenZhengManager renZhengManager;
	@Autowired
	InfoMationRecordManager infoMationRecordManager;
	@Autowired
	private CarPicManager carPicManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private SalesManager salesManager;
	@Autowired
	private MerchantValidationManager merchantValidationManager;
	@Autowired
	private  YhShenqingManager yhShenqingManager;
	private List<User> list;
	/**
	 * 分页，页码
	 */
	private String pageNumber = "1";
	private User user;
	private YhShenqing yhShenqing;
	/**
	 * 每页显示条数
	 */
	private String pageCount = "10";
	private JdbcTemplate jdbcTemplate;
	private List<Map<Object, Object>> userList;
	private String jfProductsStrs;
	private Map<String, Object> result = new HashMap<String, Object>();
	private JsonArray jfSortAndProduts = new JsonArray();
	@Autowired
	private PushMessageManager pushMessageManager;
	private File[] carFile;
	private String[] carFileFileName;
	private File[] wxFiles; //上传维修厂产品图片
	private String[] wxFilesFileName;
	private File[] files; //上传的文件
	private String[] filesFileName; //文件名称
	private String[] filesContentType; //文件类型
	private String attchFolder = "/uploadFiles/fileAttch/";
	private Map<String, Object> reMap = new HashMap<String, Object>();
	private List rzfhList = new ArrayList();
	@Autowired
	private ProductSortManager productSortManager;
	@Autowired
	private CaculateCCTimesManager caculateCCTimesManager;
	@Autowired
	MyBankManager myBankManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	ZhuanZhangRecordManager zhuanZhangRecordManager;
	private MyBank mb;
	private File attch;
	private String attchFileName;
	private RenZheng renZheng;
	private final static  String appKey ="99e00c56584d0d266e8dc360";
	private final static  String masterSecret = "18dd15d6b96b95fdde7fdd10";
	private List<YhShenqing> sqList = new ArrayList<YhShenqing>();

	/**
	 * 申请记录：二手车，返回二手车id，一张图片，名称，浏览量，发布时间，价格 ；
	 * 招聘的话，返回   id，名称，浏览量，应聘要求，发布时间，福利待遇
	 * @return
     */
	public String getMySqRecord(){
		String type = getRequest().getParameter("type");
		String userId = getRequest().getParameter("userId");
		String sql = "";
		if(type.equals("zp")){
			//hitTimes，createTime，daiyu，ypyaoqiu，zwName
			sql += "select a.id as sqId,b.id,b.zw_name as zwName,b.hit_times as hitTimes,b.yao_qiu as ypyaoqiu,b.create_time as createTime,b.dai_yu as daiyu from yh_shenqing a  inner join zhaopin b on a.zp_id = b.id where a.user_id= " + Integer.parseInt(userId);
			String zwName = getRequest().getParameter("zwName");
			sql += " and b.zw_name like '%"+zwName+"%' ";
		}else{
			//clName，hitTimes，createTime，clPrice，imageUrl
			sql += "select a.id as sqId,b.id,b.cl_name as clName,b.hit_times as hitTimes,b.create_time as createTime,b.cl_price as clPrice from yh_shenqing a  inner join che_liang b on a.cl_id = b.id  where a.user_id= " + Integer.parseInt(userId);
			String priceQj = getRequest().getParameter("priceQj");
			if(org.apache.commons.lang.StringUtils.isNotBlank(priceQj)){
				String[] prices = priceQj.split("-");
				sql +=" and  b.cl_price between "+Double.valueOf(prices[0])+" and "+Double.valueOf(prices[1])+" ";
			}
			//使用年限区间
			String usedYears = getRequest().getParameter("usedYears");
			if(org.apache.commons.lang.StringUtils.isNotBlank(usedYears)){
				String[] usedYearss = usedYears.split("-");
				sql+=" and  b.used_years between "+Double.valueOf(usedYearss[0])+" and "+Double.valueOf(usedYearss[1])+" ";
			}
			//品牌搜索
			String grand = getRequest().getParameter("grand");
			if(org.apache.commons.lang.StringUtils.isNotBlank(grand)){
				sql +=" and  b.grand like '%"+grand+"%' ";
			}
			//品牌搜索
			String clXh = getRequest().getParameter("clXh");
			if(org.apache.commons.lang.StringUtils.isNotBlank(grand)){
				sql +=" and  u.clXh like '%"+clXh+"%' ";
			}
		}
		System.out.println("------------------sql="+sql);
		userList = jdbcTemplate.queryForList(sql);
		if(type.equals("cl")){
			for(Map<Object,Object> map : userList){
				System.out.println("----------------------map.get(\"id\")="+map.get("id"));
				List<RzFile> rzFiles = rzFileManager.getCheliangFiles(Integer.parseInt(String.valueOf(map.get("id"))));
				if(null!=rzFiles && rzFiles.size()>0) {
					map.put("imageUrl", rzFiles.get(0).getImageUrl());
				}
			}
		}
		return "nearList";
	}
	public String getJiaYouSetByCity(){
		String regionId = getRequest().getParameter("regionId");
		String sql = "select id,gr_price,kind_name,region_name from jiayou_set where region_id="+Integer.parseInt(regionId);
		userList = jdbcTemplate.queryForList(sql);
		return "nearList";
	}
	public String updateJycState(){
		String jycId = getRequest().getParameter("jycId");
		String state = getRequest().getParameter("state");
		CaculateCCTimes ccc = new CaculateCCTimes();
		User user = getManager().get(Integer.parseInt(jycId));
		ccc.setUser(user);
		ccc.setCreateTime(new Date());
		ccc.setType(state);
		caculateCCTimesManager.save(ccc);
		if(null!=state && state.equals("cc")){
			user.setJycstate("1");
		}
		if(null!=state && state.equals("sc")){
			user.setJycstate("0");
		}
		getManager().update(user);
		result.put("msg", "success");
		return "addSuc";

	}
	public String getFyManager(){
		String userId = getRequest().getParameter("userId");
		if(StringUtils.isNotBlank(userId)){
			String sql = " select id as userId,fy_money as yhMoney,total_xf as total from users where tjm_user= "+Integer.parseInt(userId);
			rzfhList = jdbcTemplate.queryForList(sql);
		}
		return "rzsb";

	}
	public String getDetailOfRz(){
		String rzId = getRequest().getParameter("rzId");
		if(StringUtils.isNotBlank(rzId)) {
			renZheng = renZhengManager.get(Integer.parseInt(rzId));
			if(null!=renZheng){
				List<RzFile> rzList = rzFileManager.getRenzhengFiles(renZheng.getId());
				renZheng.setRzFileList(rzList);
			}
		}
		return "renZheng";
	}
	public String getMyTjYj(){
		String userId = getRequest().getParameter("userId");
		String sql = "select id,ewm_image as ewImage from users where id="+Integer.parseInt(userId)+"";
		userList = jdbcTemplate.queryForList(sql);
		return "nearList";
	}
	public String updateAllMoney(){
		String txJe = getRequest().getParameter("txJe");
		String userId = getRequest().getParameter("userId");
		User user = null;
		if(null!=userId){
			user = getManager().get(Integer.parseInt(userId));
		}
		if(null!=user){
			if(null!=user.getAllMoney()){
				user.setAllMoney(user.getAllMoney()+Double.valueOf(txJe));
			}else{
				user.setAllMoney(Double.valueOf(txJe));
			}
			getManager().update(user);
		}
		result.put("msg", "success");
		return "addSuc";
	}
	//商家到达目的地接口
	public String toDestination(){
		try {
			String merId = getRequest().getParameter("merId");
			User merUser = getManager().get(Integer.parseInt(merId));
			String userId = getRequest().getParameter("userId");
			String pushContent = "";
			if(null!=merUser && null!=userId){
				pushContent = "加油车"+merUser.getLoginId()+"已经到达目的地";
			}
			pushUser(userId,pushContent);
			InfomationRecord ir = new InfomationRecord();
			ir.setSendContent(pushContent);
			ir.setCreateTime(new Date());
			ir.setTstype("3");
			ir.setXxtype("3");
			ir.setReceiveUser(userManager.get(Integer.parseInt(userId)));
			ir.setSendUser(merUser);
			infoMationRecordManager.save(ir);
			result.put("msg", "success");
		} catch (Exception e) {
			result.put("msg", "error");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "addSuc";
	}
	public static void main(String[] args){

		pushUser("36634631","您此次加油账单金额为200.0订单号为XD-2016090709000001");
	}
	//给用户发送账单
	public String toUserAccount(){
		try {
			String allMoneys = getRequest().getParameter("allMoneys");
			String count = getRequest().getParameter("count");
			String orderNo = getRequest().getParameter("orderNo");
			String name = getRequest().getParameter("name");
			String ypPrice = getRequest().getParameter("ypPrice");
			Sales order = salesManager.findObject(
					"from Sales o where o.salesNo = ?", orderNo);
			if(StringUtils.isNotBlank(allMoneys)){
				order.setSamount(Double.valueOf(allMoneys));
			}
			order.setYpPrice(ypPrice);
			if(StringUtils.isNotBlank(count)){
				order.setCount(Double.valueOf(count));
			}
			if(StringUtils.isNotBlank(name)){
				order.setRemark(name);
			}
			order.setStatus("1");
			salesManager.update(order);
			String pushContent = "您此次加油账单金额为"+allMoneys+","+order.getRemark()+","+count+"升,订单号为"+order.getSalesNo();
			System.out.println("--------------------pushContent="+pushContent);
			pushUser(order.getUser().getId()+"",pushContent);
			InfomationRecord ir = new InfomationRecord();
			ir.setSendContent(pushContent);
			ir.setCreateTime(new Date());
			ir.setTstype("3");
			ir.setXxtype("4");
			ir.setReceiveUser(order.getUser());
			ir.setSendUser(order.getMerUser());
			infoMationRecordManager.save(ir);
			result.put("msg", "success");
		} catch (Exception e) {
			result.put("msg", "error");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "addSuc";
	}

	public String setPayPass() {
		String userId = getRequest().getParameter("userId");
		User user = getManager().get(Integer.parseInt(userId));
		String payPass = getRequest().getParameter("payPass");
		user.setPayPass(passwordEncoder.encodePassword(payPass.trim(), null));
		getManager().update(user);
		result.put("msg", "success");
		return "addSuc";
	}

	public String ifSetPayPass(){
		String userId = getRequest().getParameter("userId");
		User user = getManager().get(Integer.parseInt(userId));
		if(null==user.getPayPass()){
			result.put("msg","还未设置密码");
		}else{
			result.put("msg","已经设置支付密码");
		}
		return "addSuc";
	}
	public String JyPayPass() {
		String passWord = getRequest().getParameter("password");
		String userId = getRequest().getParameter("userId");
		User user = getManager().get(Integer.parseInt(userId));
		if (null != user && null != user.getPayPass() && user.getPayPass().equals(passwordEncoder.encodePassword(passWord.trim(), null))) {
			result.put("msg", "success");
		} else {
			result.put("msg", "error");
		}
		return "addSuc";
	}

	public String zhuanzhangCtrl() {
		//fromUser userId
		String msg = "";
		String userId = getRequest().getParameter("userId");
		String content = getRequest().getParameter("content");
		String zzJe = getRequest().getParameter("zzJe");
		User fromUser = getManager().get(Integer.parseInt(userId));
		double fxAccount = 0.0;
		double chargeAccount = 0.0;
		if(null!=fromUser.getFxMoney()){
			fxAccount = fromUser.getFxMoney();
		}
		if(null!=fromUser.getChargeAccount()){
			chargeAccount = fromUser.getChargeAccount();
		}
		double keyong = chargeAccount+fxAccount;
		if (keyong < Double.valueOf(zzJe)) {
			msg = "账户余额不足";
		} else {
			ZhuanZhangRecord zzr = new ZhuanZhangRecord();
			zzr.setCreateTime(new Date());
			zzr.setContent(content);
			zzr.setType("3");
			//toUser userPhone
			String phone = getRequest().getParameter("phone");
			User toUser = userManager.getUserPhone(phone);
			zzr.setToUser(toUser);
			zzr.setUser(fromUser);
			zzr.setZzJe(Double.valueOf(zzJe));
			zhuanZhangRecordManager.save(zzr);
			fromUser.setAllMoney(fromUser.getAllMoney() - Double.valueOf(zzJe));
			if(fxAccount>Double.valueOf(zzJe) || fxAccount==Double.valueOf(zzJe)){
				fromUser.setFxMoney(fxAccount - Double.valueOf(zzJe));
			}else{
				fromUser.setFxMoney(0.0);
				fromUser.setChargeAccount(keyong - Double.valueOf(zzJe));
			}
			if (null == toUser.getAllMoney()) {
				toUser.setAllMoney(Double.valueOf(zzJe));
			} else {
				toUser.setAllMoney(Double.valueOf(zzJe) + toUser.getAllMoney());
			}
			if(null!=toUser.getIncomeAll()){
				toUser.setIncomeAll(toUser.getIncomeAll() + Double.valueOf(zzJe));
			}else{
				toUser.setIncomeAll(Double.valueOf(zzJe));
			}
			getManager().update(fromUser);
			System.out.println("toUser-------------"+toUser.getAllMoney());
			getManager().update(toUser);
			msg = "转账成功";
		}
		result.put("msg", msg);
		return "addSuc";

	}

	public String getByPhone() {
		String phone = getRequest().getParameter("phone");
		user = userManager.getUserPhone(phone);
		return "detailSuc";
	}

	public String getBankByCard() {
		String cardNumber = getRequest().getParameter("cardNo");
		cardNumber = cardNumber.replaceAll(" ", "");
		//位数校验
		if (cardNumber.length() == 16 || cardNumber.length() == 19) {
		} else {
			result.put("msg", "卡号尾数无效");
			System.out.println("卡号位数无效");
			return "addSuc";
		}
		//校验
		if (checkBankCard.checkBankCard(cardNumber) == true) {
		} else {
			result.put("msg", "卡号校验失败");
			return "addSuc";
		}
		result.put("msg", BankCardBin.getNameOfBank(cardNumber.substring(0, 6), 0));
		return "addSuc";
}

	public String getDetailOfBank(){
		String bankId = getRequest().getParameter("bankId");
		 mb = myBankManager.findBank(bankId);
		return "mb";
	}

	public String getMyBanks(){
		System.out.println("-=-=-=--=--=-=--=--=");
		String type = getRequest().getParameter("type");
		String userId = getRequest().getParameter("userId");
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from MyBank u where u.visible='1' and u.user.id=? ");
		args.add(Integer.parseInt(userId));
		if(null!=type&& type.equals("mine")){
			hql.append(" and comName is null ");
		}else{
			hql.append(" and comName is not null ");
		}
		hql.append(" order by u.createTime desc");
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		userList = page.getData();
		return "nearList";

	}
	public String getUserByPhone(){
		String phone = getRequest().getParameter("phone");
		String merSrotId = getRequest().getParameter("merSrotId");
		User user =  userManager.getUserByPhoneAndSort(phone,merSrotId);
		if(null!=user){
			result.put("msg","error");
		}else{
			result.put("msg","success");
		}
		return "addSuc";
	}
	public String saveMyBank(){
		MyBank mb = new MyBank();
		String comName = getRequest().getParameter("comName");
		if(StringUtils.isNotBlank(comName)){
			mb.setComName(comName);
		}
		attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
		if (attch != null) {
			String filePath = UpLoadUtil.doUpload(attch,
					attchFileName, attchFolder, getServletContext());
			mb.setImageURL(filePath);
		}
		String userId = getRequest().getParameter("userId");
		User user = userManager.get(Integer.parseInt(userId));
		mb.setUser(user);
		String ckrName = getRequest().getParameter("ckrName");
		mb.setCkrName(ckrName);
		String ckrPhone = getRequest().getParameter("ckrPhone");
		mb.setCkrPhone(ckrPhone);
		String idCard = getRequest().getParameter("idCard");
		mb.setIdCard(idCard);
		String bankNo = getRequest().getParameter("bankNo");
		mb.setBankNo(bankNo);
		String bankName = getRequest().getParameter("bankName");
		mb.setBankName(bankName);
		mb.setVisible("1");
		mb.setCreateTime(new Date());
		myBankManager.save(mb);
		result.put("msg","success");
		return "addSuc";
	}
	public String releaseCard(){
		try {
			String cardId = getRequest().getParameter("cardId");
			MyBank mb = null;
			if(StringUtils.isNotBlank(cardId)){
				mb = myBankManager.get(Integer.parseInt(cardId));
				myBankManager.remove(mb);
				reMap.put("msg","ok");
			}
		}
		catch (Exception e){
			reMap.put("msg","error");
			e.printStackTrace();
		}
		return "reMsg";
	}
	public String updateNickName(){
		String userId = getRequest().getParameter("userId");
		String nickName = getRequest().getParameter("nickName");
		User user = null;
		if(null!=userId){
			user = getManager().get(Integer.parseInt(userId));
		}
		if(null!=nickName){
			user.setNickName(nickName);
			getManager().update(user);
		}
		result.put("msg","success");
		return "addSuc";
	}
	public String addFkToApp(){
		FanKuiToApp fkt = new FanKuiToApp();
		String userId = getRequest().getParameter("userId");
		User user = null;
		if(StringUtils.isNotBlank(userId)){
			user = userManager.get(Integer.parseInt(userId));
			fkt.setUser(user);
		}
		String content = getRequest().getParameter("content");
		fkt.setContent(content);
		fkt.setCreateTime(new Date());
		fanKuiToAppManager.save(fkt);
		result.put("msg","success");
		return "addSuc";
	}
	public String updatePhone(){
		String phone = getRequest().getParameter("phone");
		String userId = getRequest().getParameter("userId");
		User user = null;
		if(StringUtils.isNotBlank(userId)){
			user = getManager().get(Integer.parseInt(userId));
		}
		if(null!=user){
			user.setPhone(phone);
			getManager().update(user);
		}
		result.put("msg","success");
		return "addSuc";
	}
	//获取用户余额和信用金额接口
	public String getRestMoney(){
		String merId = getRequest().getParameter("merId");
		String sql = "select all_money as allMoney,income_all as incomeAll,fx_money as fxMoney,charge_account as chargeAccount from users where id=?";
		List<PushMessage> pushList = pushMessageManager.getAdvertise();
		userList = jdbcTemplate.queryForList(sql,new Object[]{Integer.parseInt(merId)});
		Map<Object,Object> map = null;
		for (PushMessage pm : pushList){
			map = new HashedMap();
			map.put("url",pm.getAdvUrl());
			map.put("imageUrl",pm.getImageURL());
			userList.add(map);
		}
		return "nearList";
	}
	//用户是否认证接口
	public String ifAppRz(){
		String result = null;
		String rzType = getRequest().getParameter("rzType");
		String userId = getRequest().getParameter("userId");
		StringBuffer sb = new StringBuffer(" select * from ren_zheng where user_id="+Integer.parseInt(userId)+" and if_tg='1' and rz_type='"+rzType+"' ");
		list = jdbcTemplate.queryForList(sb.toString());
		if(null!=list&&list.size()>0){
			String type=getRequest().getParameter("type");
			page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
			StringBuffer sql = new StringBuffer("from ProductSort c where c.type='"+type+"' and c.status='1' and c.parentProsort.id is null and c.hidePhoneClient is null AND c.name!='手机'  ");
			sql.append(" order by c.rowNum");
			page = getManager().pageQuery(page, sql.toString());
			rzfhList=page.getData();
			result = "rzcg";
		}else{
			result = "rzsb";
		}
		return result;
	}
	//判断用户是否认证
	public String judgeIfRz(){
		String type = getRequest().getParameter("type");
		String userId = getRequest().getParameter("userId");
		StringBuffer sb = new StringBuffer();
		sb.append(" select iftg from ren_zheng where user_id="+Integer.parseInt(userId)+" and rz_type='"+type+"' ");
		userList = jdbcTemplate.queryForList(sb.toString());
		return "nearList";
	}
	public String phoneUploadD(){
		String merId = getRequest().getParameter("merId");
		User dpUser = null;
		if(StringUtils.isNotBlank(merId)){
			dpUser = getManager().get(Integer.parseInt(merId));
		}else{
			dpUser = new User();
		}
		String noUpdateIds = getRequest().getParameter("noUpdateIds");
		if (StringUtils.isNotBlank(merId)) {
			pushMessageManager.deleteUpdateFile(merId, noUpdateIds);
		}
		//经度
		String log = getRequest().getParameter("log");
		if(null!=log){
			dpUser.setLocX(bcLw(log)+"");
		}
		//维度
		String lng= getRequest().getParameter("lng");
		if(null!=lng){
			dpUser.setLocY(bcLw(lng)+"");
		}
		dpUser.setType("agent");
		String proSortId = getRequest().getParameter("proSortId");
		dpUser.setProductSort(productSortManager.get(Integer.parseInt(proSortId)));
		String merSortNames = getRequest().getParameter("merSortNames");
		dpUser.setMerSortNameStr(merSortNames);
		String merSortIds = getRequest().getParameter("merSortIds");
		dpUser.setMerSortStr(merSortIds);
		PushMessage ps = null;
		String userId = getRequest().getParameter("userId");
		String name = getRequest().getParameter("name");
		dpUser.setName(name);
		String phone = getRequest().getParameter("phone");
		dpUser.setPhone(phone);
//		dpUser.setLoginId(phone);
		String address = getRequest().getParameter("address");
		String bxyName = getRequest().getParameter("bxyName");
		dpUser.setBxyName(bxyName);
		dpUser.setVisible("1");
		String regionId = getRequest().getParameter("regionId");
		if(StringUtils.isNotBlank(regionId)){
			Region region = regionManager.get(Integer.parseInt(regionId));
			dpUser.setRegion(region);
			dpUser.setAddress(region.getParent().getName()+" "+region.getName()+" "+address);
		}
		User user = userManager.get(Integer.parseInt(userId));
		dpUser.setShopOfUser(user);
		if(StringUtils.isNotBlank(proSortId)&&Integer.parseInt(proSortId)==41418754){
			dpUser.setAddress(address);
			dpUser.setFxsjb("jyc");
		}else {
			dpUser.setFxsjb("agent_level_village");
		}
		System.out.println("------------开始上传图片---------------");
		List<PushMessage> pushList = new ArrayList<PushMessage>();
		if(null!=wxFiles&& wxFiles.length>0){
			System.out.println("-----------------------维修产品图片个数为----"+wxFiles.length);
			for (int i=0;i<wxFiles.length;i++){
				ps = new PushMessage();
				attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
				if (wxFiles[i] != null) {
					ps.setCreateTime(new Date());
					String filePath = UpLoadUtil.doUpload(wxFiles[i], wxFilesFileName[i], attchFolder,
							getServletContext());
					ps.setImageURL(filePath);
					ps.setTitle(wxFiles[i].getName());
					ps.setType(4);
					ps.setProductSort(productSortManager.get(Integer.parseInt(proSortId)));
					pushList.add(ps);
				}
			}
		}
		if(null!=files&& files.length>0){
			System.out.println("---------------------------"+files.length);
			for (int i=0;i<files.length;i++){
				System.out.println("---------------------------files"+files[i]);
				System.out.println("---------------------------filesFileName"+filesFileName[i]);
				ps = new PushMessage();
				attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
				if (files[i] != null) {
					ps.setCreateTime(new Date());
					String filePath = UpLoadUtil.doUpload(files[i], filesFileName[i], attchFolder,
							getServletContext());
					if(i==0){
						dpUser.setImageURL(filePath);
					}
					ps.setImageURL(filePath);
					ps.setTitle(files[i].getName());
					ps.setType(2);
					ps.setProductSort(productSortManager.get(Integer.parseInt(proSortId)));
					pushList.add(ps);
				}
			}
		}
		getManager().save(dpUser);
		User setU = getManager().getUserByShopIdAndSort(userId,proSortId);
		for(PushMessage pm : pushList){
			pm.setPicOfUser(setU);
			pushMessageManager.save(pm);
		}
//		String absolutPath = getRequest().getSession().getServletContext().getRealPath("/uploadFiles/ewmPic/"+setU.getId()+".png");
//		String path = getRequest().getContextPath();
//		String basePath = getRequest().getScheme() + "://"
//				+ getRequest().getServerName() + ":" + getRequest().getServerPort()
//				+ path + "/";
//		String content = basePath+"pages/amol/reglogin/regist.jsp?yqm="+setU.getId();
//		QRcode handler = new QRcode();
//		handler.encoderQRCode(content,absolutPath);
//		getModel().setEwmImageURl("/uploadFiles/ewmPic/"+setU.getId()+".png");
//		getManager().getDao().clear();
//		getManager().update(setU);
//		System.out.println("解析的内容为"+content);
//		System.out.println("========encoder success");
//		String decoderContent = handler.decoderQRCode(absolutPath);
//		System.out.println("解析结果如下：");
//		System.out.println(decoderContent);
//		System.out.println("========decoder success!!!");
		reMap.put("msg","ok");
		reMap.put("wxMerId",setU.getId());
		return "reMsg";
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
	public String getDetailOfJyc(){
		String jycId = getRequest().getParameter("jycId");
		 user = getManager().get(Integer.parseInt(jycId));
		List<CarPicture> list = carPicManager.getListCarPics(Integer.parseInt(jycId));
		List<PushMessage> pushList = pushMessageManager.getByMerid(jycId);
		List<Map<String,Object>> jsList = jiayouSetManager.getByRegionId(Integer.parseInt(jycId));
		user.setJsList(jsList);
		user.setPushList(pushList);
		String regionIds = "";
		String reigonNames = "";
		String address = user.getAddress();
		if(null!=user && null!=user.getRegion()){
			regionIds += user.getRegion().getId()+",";
			reigonNames += user.getRegion().getName()+",";
			address = address.replace(user.getRegion().getName(),"");
			if(null!=user.getRegion().getParent()){
				regionIds += user.getRegion().getParent().getId()+",";
				reigonNames += user.getRegion().getParent().getName()+",";
				address = address.replace(user.getRegion().getParent().getName(),"");
			}
			if(null!=user.getRegion().getParent().getParent()){
				regionIds += user.getRegion().getParent().getParent().getId()+",";
				reigonNames += user.getRegion().getParent().getParent().getName()+",";
				address = address.replace(user.getRegion().getParent().getParent().getName(),"");
			}
		}
		if(null!=address) {
			user.setAddress(address.replaceAll(" ", ""));
		}
		System.out.println("-------------------------regionIds= "+regionIds);
		if(StringUtils.isNotBlank(regionIds)) {
			user.setReigonIds(regionIds.substring(0, regionIds.length() - 1));
		}
		if(StringUtils.isNotBlank(reigonNames)) {
			user.setReigonNames(reigonNames.substring(0, reigonNames.length() - 1));
		}
		if(null!=list &&list.size()>0) {
			for (CarPicture cp : list) {
				user.getMyJyc().add(cp.getImagerURL());
			}
		}
		return "detailSuc";
	}
	public String updateAddress(){
		String address = getRequest().getParameter("address");
		String log = getRequest().getParameter("log");
		String lat = getRequest().getParameter("lat");
		String userId = getRequest().getParameter("userId");
		User user = null;
		if(StringUtils.isNotBlank(userId)){
			user = getManager().get(Integer.parseInt(userId));
			user.setAddress(address);
			if(null!=log){
				user.setLocX(bcLw(log)+"");
			}
			if(null!=lat){
				user.setLocY(bcLw(lat)+"");
			}
			getManager().update(user);
		}
		result.put("msg","地址修改成功");
		return "addSuc";
	}
	//查看我的加油车
	public String getMyJyc(){
		String userId = getRequest().getParameter("userId");
		list = getManager().getMyJycs(userId);
		List<String> strList = null;
		for (User uu:  list){
			strList = new ArrayList<String>();
			List<CarPicture> list = carPicManager.getListCarPics(uu.getId());
			for (CarPicture cp : list){
				strList.add(cp.getImagerURL());
			}
			uu.setMyJyc(strList);
		}
		return "success";
	}
	public String getIfSh(){
		String merId = getRequest().getParameter("merId");
		String  sql = " select if_recommend,sh_idea from users where id= "+Integer.parseInt(merId);
		userList = jdbcTemplate.queryForList(sql);
		return "nearList";
	}
	public String jieBangphone(){
		String userId = getRequest().getParameter("userId");
		User user = getManager().get(Integer.parseInt(userId));
		user.setPhone(null);
		getManager().update(user);
		result.put("msg","接触绑定成功");
		return "addSuc";
	}
	//用户增加加油车
	public String createCar(){
		String carNo = getRequest().getParameter("carNo");
		getModel().setCode(carNo);
		getModel().setJycstate("0");
		String name = getRequest().getParameter("name");
		String password = getRequest().getParameter("password");
		if(null!=password && !"".equals(password)) {
			getModel().setPassword(password.trim());
		}
		String userId = getRequest().getParameter("userId");
		User user = getManager().get(Integer.parseInt(userId));
		getModel().setSuperior(user);
		getModel().setName(name);
		String phone = getRequest().getParameter("phone");
		getModel().setLoginId(phone);
		getModel().setPhone(phone);
		getModel().setType("app_user");
		getModel().setFxsjb("jyc");
		getModel().setVisible("1");
		User temUser = getManager().getUser(phone);
		if(null!=temUser){
			result.put("msg","手机号已经存在");
		}else{
			System.out.println("-=-=-------开始添加加油车-------------");
			getManager().save(getModel());
			attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
			List<CarPicture> list = new ArrayList<CarPicture>();
			CarPicture cp = null;
			System.out.println("-------------carFile="+carFile);
			System.out.println("-----------------开始保存图片------------------");
			if(null!=carFile && carFile.length>0) {
				System.out.println("--=-=-=-=-=--"+carFile.length);
				System.out.println("--=-=-=-=-=--carFileFileName="+carFileFileName+"---------------"+carFileFileName.length);
				for (int i = 0; i < carFile.length; i++) {
					System.out.println("111111111111111111111111111111");
					cp = new CarPicture();
					System.out.println("2222222222222222222222222222222");
					cp.setCreateTime(new Date());
					System.out.println("333333333333333333333333333333");
					cp.setUser(getModel());
					System.out.println("444444444444444444444444444444");
					if (carFile[i] != null) {
						System.out.println("5555555555555555555555555555");
						String filePath = UpLoadUtil.doUpload(carFile[i], carFileFileName[i], attchFolder,
								getServletContext());
						System.out.println("66666666666666666666666666666666666");
						System.out.println(carFile[i]+"-=-==-==-=-=-"+carFileFileName[i]);
						System.out.println("77777777777777777777777777777777777");
						cp.setImagerURL(filePath);
						System.out.println("8888888888888888888888888888888888");
					}
					list.add(cp);
				}
			}
			for (CarPicture pp : list) {
				carPicManager.save(pp);
			}
			result.put("msg","success");
		}
		return "addSuc";
	}
	// 客户端传来的regionid
	private String regionId;

	public String appMerIndex() {
		Double latitude =0.0;
		Double longitude =0.0;
		String regionName =getRequest().getParameter("regionName");
		System.out.println("----------------------------------regionName="+regionName);
		String type = getRequest().getParameter("type");
		if(!getRequest().getParameter("latitude").equals("")){
			latitude = Double.valueOf(getRequest().getParameter("latitude"));
			longitude = Double.valueOf(getRequest().getParameter("longitude"));
		}
		String proSortId = getRequest().getParameter("proId");
		String name = getRequest().getParameter("name");
			userList = getMerIndex(pageCount, pageNumber, latitude, longitude,type,regionName,proSortId,name);
		for (Map<Object,Object> map:userList){
			Integer merId = Integer.parseInt(String.valueOf(map.get("id")));
			System.out.println("------------------------------merId="+merId);
			int count = getManager().getCntValidate(merId);
			System.out.println("------------------------------count="+count);
			double avg = getManager().getAvgScore(merId);
			System.out.println("------------------------------avg="+avg);
			map.put("count",count);
			map.put("avg",avg);
		}
		return "nearList";
	}
	//查询商家附近10km的滴滴加油订单
	public String getNearDdJyOfMerchant(){
		Double longitude = Double.valueOf(getRequest().getParameter("longitude"));
		Double latitude = Double.valueOf(getRequest().getParameter("latitude"));
		String proName = getRequest().getParameter("proName");
		System.out.println("经度："+longitude+"纬度："+latitude);
		String sql = "select a.count,a.samount,DATE_FORMAT(a.create_time,'%Y-%m-%d %H:%i:%s') as createTimeStr,a.position,b.phone,a.sales_no as salesNo,b.login_Id as loginId,a.remark as remark,a.loc_x as locX,a.loc_y as locY,b.phone,b.nick_name as name,b.login_id,"
				+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-a.loc_x*PI()/180)/2),2) + COS(?*PI()/180)* COS(a.loc_x*PI()/180)*POW(SIN((?*PI()/180-a.loc_y*PI()/180)/2),2)))*1000)AS juli "
				+ " from sales a left join users b on a.user_id=b.id where a.mer_id is null and a.remark ='"+proName+"' and  a.status='0' HAVING  juli<? ORDER BY juli asc,a.create_time desc limit ?,?";
		System.out.println("------------sql="+sql);
		Double distance = distanceManager.getDistance();
		userList = jdbcTemplate
				.queryForList(sql,
						new Object[] { longitude, longitude, latitude, distance,
								(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
								fromStringToInt(pageCount) });
		Map<Object,Object> map = new HashMap<Object, Object>();
		map.put("distance",distance);
		userList.add(map);
		return "nearList";
	}
	public String getInfomations(){
		String userId = getRequest().getParameter("userId");
		if(StringUtils.isNotBlank(userId)){
			page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
			List<Object> args = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer("from InfomationRecord u where u.receiveUser.id=? and (u.xxtype='0' or u.xxtype='1') ");
			args.add(Integer.parseInt(userId));
			hql.append(" order by u.createTime desc");
			page = getManager().pageQuery(page, hql.toString(), args.toArray());
			List<InfomationRecord> iList = page.getData();
			for(InfomationRecord ir : iList){
				if(null!=ir.getCreateTime()) {
					ir.setCreateTimeStr(String.valueOf(ir.getCreateTime()).replace("T"," "));
				}
			}
			userList = page.getData();
		}
		return "nearList";
	}
	public String getDetailOfYhInfo(){
		String ysId = getRequest().getParameter("ysId");
		yhShenqing = yhShenqingManager.get(Integer.parseInt(ysId));
		return "yhShenqing";
	}
	public String getkcQzInfos(){
		String what = getRequest().getParameter("what");
		String merId = getRequest().getParameter("merId");
		String hql = "";
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		List<Object> args = new ArrayList<Object>();
		if(StringUtils.isNotBlank(what)){
			if(what.equals("qz")){
				hql = " from  YhShenqing where zhaoPin.user.id="+Integer.parseInt(merId);
			}
			if(what.equals("kc")){
				hql = " from  YhShenqing where cheLiang.user.id="+Integer.parseInt(merId);
			}
			hql+="  order by createTime desc";
			page = getManager().pageQuery(page, hql, args.toArray());
			List<YhShenqing> iList = page.getData();
			for(YhShenqing ir : iList){
				ir.setImageURL(ir.getUser().getImageURL());
				ir.setNickName(ir.getUser().getNickName());
				if(null!=ir.getCreateTime()) {
					ir.setCreateTimeStr(String.valueOf(ir.getCreateTime()).replace("T"," "));
				}
			}
			userList = page.getData();
		}
		return "nearList";
	}
	public String saveShInfo(){
		try {
			YhShenqing ys = new YhShenqing();
			//申请人姓名
			String name = getRequest().getParameter("name");
			ys.setName(name);
			ys.setCreateTime(new Date());
			//申请人id
			String userId = getRequest().getParameter("userId");
			if(StringUtils.isNotBlank(userId)){
				ys.setUser(userManager.get(Integer.parseInt(userId)));
			}
			String zpId = getRequest().getParameter("zpId");
			if(StringUtils.isNotBlank(zpId)){
				ys.setZhaoPin(zhaoPinManager.get(Integer.parseInt(zpId)));
			}
			String clId = getRequest().getParameter("clId");
			if(StringUtils.isNotBlank(clId)){
				ys.setCheLiang(cheLiangManager.get(Integer.parseInt(clId)));
			}
			String sex = getRequest().getParameter("sex");
			ys.setSex(sex);
			String age = getRequest().getParameter("age");
			ys.setAge(age);
			String qzyx = getRequest().getParameter("qzyx");
			ys.setQzyx(qzyx);
			String relatePhone = getRequest().getParameter("relatePhone");
			ys.setRelatePhone(relatePhone);
			String zwpj = getRequest().getParameter("zwpj");
			ys.setZwpj(zwpj);
			yhShenqingManager.save(ys);
			result.put("msg","success");
		}
		catch (Exception e){
			result.put("msg","error");
			e.printStackTrace();
		}
		return "addSuc";
	}
	//加油车司机抢单接口(接单操作)
	public String jycDriverQd(){
		String salesNo = getRequest().getParameter("salesNo");
		String merId = getRequest().getParameter("merId");
//		int qdNum = getManager().getCntOfQd(Integer.parseInt(merId));
		int qdNum = 1;
		if(qdNum>3||qdNum==3){
			result.put("msg","当前最多接三单，请完成后再抢单");
		}else {
			Sales order = salesManager.findObject(
					"from Sales o where o.salesNo = ? and status='0' ", salesNo);
			if (null != order) {
				User merUser = userManager.get(Integer.parseInt(merId));
				order.setStatus("1");
				order.setMerUser(merUser);
				order.setMerId(Integer.parseInt(merId));
				salesManager.update(order);
				//调用推送api 推送给用户商家已经接单
				User user = getManager().get(order.getUser().getId());
				System.out.println("-------------------order.getUser().getId()=" + order.getUser().getId());
				System.out.println("推送内容为。。。。" + "手机号为" + merUser.getPhone() + "的加油车司机已经接单,订单号为" + order.getSalesNo());
				pushUser(order.getUser().getId() + "", "手机号为" + merUser.getPhone() + "的加油车司机已经接单,订单号为" + order.getSalesNo());
				InfomationRecord it = new InfomationRecord();
				it.setCreateTime(new Date());
				it.setXxtype("5");
				it.setTstype("3");
				it.setSendContent("手机号为" + merUser.getPhone() + "的加油车司机已经接单");
				it.setSendUser(merUser);
				it.setReceiveUser(user);
				infoMationRecordManager.save(it);
				result.put("msg", "恭喜您抢单成功");
			} else {
				result.put("msg", "很遗憾！晚了一步！");
			}
		}
	return "addSuc";
	}
	public static void pushUser(String alias,String alert){
		ClientConfig config = ClientConfig.getInstance();
		// Setup the custom hostname
		//1加油 2 维修 3 保险 4 后台
		String type = "4";
		String typeName = "后台";
		if(StringUtils.isNotBlank(alert)){
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
	public static PushPayload buildPushObject_android_and_ios(String alias, String alert) {
		return PushPayload.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.tag("tag1"))
				.setNotification(Notification.newBuilder()
						.setAlert(alert)
						.addPlatformNotification(AndroidNotification.newBuilder()
								.setTitle("Android Title").build())
						.addPlatformNotification(IosNotification.newBuilder()
								.incrBadge(1)
								.addExtra("extra_key", "extra_value").build())
						.build())
				.build();
	}
		//客户取消订单
		public String cancelOrder(){
			String salesNo = getRequest().getParameter("salesNo");
			String content = getRequest().getParameter("content");
			Sales order = salesManager.findObject(
					"from Sales o where o.salesNo = ? ", salesNo);
			order.setStatus("-1");
			salesManager.update(order);
			result.put("msg","success");
			return "addSuc";
		}
	// 根据区域查询推荐商家
	public List<Map<Object,Object>> getMerIndex(String pageCount, String pageNumber, Double latitude,
			Double longitude,String type,String regionName,String proSortId,String name) {
		String sql = "select a.id,a.bxy_name as bxyName,a.name,a.address,a.phone,a.legal_person_name,a.loc_x,a.loc_y,c.name as port_name,a.image_url,"
				//+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-a.loc_x*PI()/180)/2),2) + COS(?*PI()/180)* COS(a.loc_x*PI()/180)*POW(SIN((?*PI()/180-a.loc_y*PI()/180)/2),2)))*1000)AS juli "
				+ "  sqrt(  \n" + //loc_x是数据库中得经度  loc_y是数据库中的维度
				"    (  \n" +
				"     ((?-loc_x)*PI()*12656*cos(((?+loc_y)/2)*PI()/180)/180)  \n" +
				"     *  \n" +
				"     ((?-loc_x)*PI()*12656*cos (((?+loc_y)/2)*PI()/180)/180)  \n" +
				"    )  \n" +
				"    +  \n" +
				"    (  \n" +
				"     ((?-loc_y)*PI()*12656/180)  \n" +
				"     *  \n" +
				"     ((?-loc_y)*PI()*12656/180)  \n" +
				"    )  \n" +
				")/2 AS juli "
				+ " from users a  left join products_sort c on a.prosort_id=c.id where a.type='agent' and a.status='1' and (a.fxsjb='agent_level_village' or a.fxsjb='jyc') and a.visible='1' and a.address like '%"+regionName+"%' and a.if_recommend='1'   ";
		if(StringUtils.isNotBlank(proSortId)){
			if(proSortId.equals("all")){
				sql += "  and (a.prosort_id=41418753 or (a.prosort_id=41418754 and  a.jyc_state='1' ))  ";
			}else{
				sql += " and a.prosort_id = "+Integer.parseInt(proSortId) ;
			}
		}
		if(StringUtils.isNotBlank(name)){
			sql += " and a.name like '%"+name+"%'  ";
		}
		System.out.println("-----------------------------"+sql);
		if(null!=type){
			if(type.equals("hits")){
				sql += " order by a.hit_times desc,a.createTime desc  ";
			}
			if(type.equals("score")){
				sql += " order by a.all_score desc,a.createTime desc  ";
			}
			if(type.equals("distance")){
				sql += " order by juli asc,a.createTime desc  ";
			}
		}else{
				sql += " order BY a.createTime desc ";
		}
		sql += " limit ?,?";
		//private List<Map<Object, Object>> userList;
		userList = jdbcTemplate  //经度 维度 经度 维度  维度 维度
				.queryForList(sql,
						new Object[] { longitude, latitude, longitude,latitude,latitude,latitude,
								(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
								fromStringToInt(pageCount) });
		String tsql = "select count(*) as cnt,AVG(score) as avgScore from merchant_validation where meruser_id=";
		List<Map<String,Object>> list = null;
		for (Map<Object,Object> map : userList){
			/*Object juli = map.get("juli");
			String tempJuli = subStr(String.valueOf(juli));
			map.put("juli",tempJuli);*/
			Object id = map.get("id");
			if(null!=id){
				list = jdbcTemplate.queryForList(tsql +=Integer.parseInt(String.valueOf(id)));
				if(null!=list && list.size()>0) {
					map.put("avgScore", list.get(0).get("avgScore"));
					map.put("pjrs",list.get(0).get("cnt"));
				}
			}
		}
		return userList;
	}
	public String subStr(String str){
		String[] strs = str.split("\\.");
		return strs[0]+"."+strs[1].substring(0, 2);
	}
	/**
	 * 根据地区查询 云便利接口 商品类型id 云便利 38010880 购物商家类型id 31850506
	 * 
	 * @param pageCount
	 * @param pageNumber
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public List getAppyblIndex(String pageCount, String pageNumber, Double latitude, Double longitude) {
		String sql = "select a.id,a.name,a.address,a.phone,a.legal_person_name,a.loc_x,a.loc_y,a.image_url as imageURL,"
				+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-a.loc_x*PI()/180)/2),2) + COS(?*PI()/180)* COS(a.loc_x*PI()/180)*POW(SIN((?*PI()/180-a.loc_y*PI()/180)/2),2)))*1000)AS juli "
				+ " from users a left join products_sort b on a.prosort_id=b.id where a.type='agent' and a.status='1' and a.region_id=? and a.fxsjb='agent_level_village'and a.visible='1' and b.id = 31850506 ORDER BY juli asc limit ?,?";
		userList = jdbcTemplate
				.queryForList(sql,
						new Object[] { longitude, longitude, latitude, Integer.parseInt(regionId),
								(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
								fromStringToInt(pageCount) });
		return userList;
	}

	// 根据商家类别查询商家列表    35258368  114.488633   38.066868
	public String getMerByProsortId() {
		if(!"".equals(getRequest().getParameter("latitude"))){ 
			String proSortId = getRequest().getParameter("proSortId");
			Double latitude = Double.valueOf(getRequest().getParameter("latitude"));
			Double longitude = Double.valueOf(getRequest().getParameter("longitude"));
			String sql = "select id,name,address,phone,legal_person_name,loc_x,loc_y,image_url,"
					+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-loc_x*PI()/180)/2),2) + COS(?*PI()/180)* COS(loc_x*PI()/180)*POW(SIN((?*PI()/180-loc_y*PI()/180)/2),2)))*1000)AS juli "
					+ " from users where type='agent' and fxsjb='agent_level_village'and visible='1' ";
			if(null!=proSortId && proSortId.equals("all")){
				sql += "  and prosort_id=35258368 or prosort_id=44498944  ";
			}else{
				sql += " and prosort_id="+Integer.parseInt(proSortId)+" ";
			}
			if (null != regionId && !"".equals(regionId)) {
				sql += " and region_id=?  ORDER BY juli asc  limit ?,?";
			} else {
				sql+=" ORDER BY juli asc  limit ?,? ";
				// asc limit ?,?";
				//sql += " having juli < " + AmolUserConstants.JULI + " ORDER BY juli asc  limit ?,?";
			}
			if (null != regionId && !"".equals(regionId)) {
				userList = jdbcTemplate.queryForList(sql,
						new Object[] { longitude, longitude, latitude, Integer.parseInt(regionId),
								(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
								fromStringToInt(pageCount) });
			} else {
				userList = jdbcTemplate.queryForList(sql,
						new Object[] { longitude, longitude, latitude,
								(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
								fromStringToInt(pageCount) });
			}
			Integer merId = null;
			int count = 0;
			String avgScore ="";
			String  totalNumberSql = " select count(*) from merchant_validation where meruser_id= ?";
			String  avgScoreSql = " select avg(score) as score from merchant_validation where meruser_id= ?";
			for (Map<Object,Object> map: userList) {
				/*Object juli = map.get("juli");
				String tempJuli = subStr(String.valueOf(juli));
				map.put("juli",tempJuli);*/
				merId = Integer.parseInt(String.valueOf(map.get("id")));
				count = jdbcTemplate.queryForInt(totalNumberSql,new Object[]{merId});
				map.put("pjrs",count);
				avgScore =  (String)jdbcTemplate.queryForObject(avgScoreSql,new Object[]{merId},java.lang.String.class);
				map.put("avgScore",avgScore);

			}
		}
		return "nearList";
	}
	//微信认证接口
	public void wxRz() throws  Exception{
		// 微信加密签名
		String signature = getRequest().getParameter("signature");
		System.out.println("微信加密签名为="+signature);
		// 时间戮
		String timestamp = getRequest().getParameter("timestamp");
		System.out.println("时间戮="+timestamp);
		// 随机数
		String nonce = getRequest().getParameter("nonce");
		System.out.println("随机数="+nonce);
		// 随机字符串
		String echostr = getRequest().getParameter("echostr");
		System.out.println("随机字符串="+echostr);
		PrintWriter out = getResponse().getWriter();
		if(SignUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);
		}
		out.close();
		out = null;
	}

	public List getMerbyProSort(String pageCount, String pageNumber, Double latitude, Double longitude,
			String proSortId) {
		String sql = "select id,name,address,phone,legal_person_name,loc_x,loc_y,image_url,"
				+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-loc_x*PI()/180)/2),2) + COS(?*PI()/180)* COS(loc_x*PI()/180)*POW(SIN((?*PI()/180-loc_y*PI()/180)/2),2)))*1000)AS juli "
				+ " from users where type='agent' and prosort_id=? and fxsjb='agent_level_village'and visible='1' and region_id=?  ORDER BY juli asc limit ?,?";
		userList = jdbcTemplate
				.queryForList(sql,
						new Object[] { longitude, longitude, latitude, proSortId, Integer.parseInt(regionId),
								(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
								fromStringToInt(pageCount) });
		return userList;
	}
//商家点击次数记录
	public String addHitTimes(){
		String merId = getRequest().getParameter("merId");
		User merUser = getManager().get(Integer.parseInt(merId));
		if(null!=merUser.getHitTimes()){
			merUser.setHitTimes(merUser.getHitTimes()+1);
		}else{
			merUser.setHitTimes(1);
		}
		getManager().update(merUser);
		result.put("msg", "ok");
		return "addSuc";
	}
	// 根据商家名称模糊查询
	public String getMerWithMajor() throws Exception {
	   String merName=getRequest().getParameter("name");
		Double latitude = Double.valueOf(getRequest().getParameter("latitude"));
		Double longitude = Double.valueOf(getRequest().getParameter("longitude"));
		String sql = "select id,name,address,phone,legal_person_name,loc_x,loc_y,image_url,"
				+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-loc_x*PI()/180)/2),2) + COS(?*PI()/180)* COS(loc_x*PI()/180)*POW(SIN((?*PI()/180-loc_y*PI()/180)/2),2)))*1000)AS juli "
				+ " from users where type='agent' and  fxsjb='agent_level_village'and visible='1' ";
		if(null!=merName&&!"".equals(merName)){
			sql += " and name LIKE '%"+merName+"%'";
		}
		if (null != regionId && !"".equals(regionId)) {
			sql += " and region_id=?  ORDER BY juli asc  limit ?,?";
		} else {
			// sql+=" having juli < 1 ORDER BY juli asc limit ?,?";
			sql += " having juli < " + AmolUserConstants.JULI + " ORDER BY juli asc  limit ?,?";
		}
		if (null != regionId && !"".equals(regionId)) {
			userList = jdbcTemplate.queryForList(sql,
					new Object[] { longitude, longitude, latitude, Integer.parseInt(regionId),
							(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
							fromStringToInt(pageCount) });
		} else {
			userList = jdbcTemplate.queryForList(sql,
					new Object[] { longitude, longitude, latitude,
							(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
							fromStringToInt(pageCount) });
		}
		return "nearList";
	}
	public String appMerDetail() {
		String merId = getRequest().getParameter("id");
		List<Object> args = new ArrayList<Object>();
		user = getManager().getUserById(Integer.parseInt(merId));
		String sql = " select type,imageURL FROM PUSH_MESSAGE WHERE USER_ID = ? AND PROSORT_ID = ? ";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,new Object[]{Integer.parseInt(merId),user.getProductSort().getId()});
		user.setPicList(list);
		if(null!=user && null!=user.getMerSortStr()&& StringUtils.isNotBlank(user.getMerSortStr())) {
			String priceStr="";
			String[] idArray = user.getMerSortStr().split(",");
			ProductSort ps = null;
			for (String id : idArray){
				if(StringUtils.isNotBlank(id)) {
					ps = productSortManager.get(Integer.parseInt(id));
					if(null!=ps) {
						priceStr = priceStr + ps.getPrice() + ",";
					}
				}
			}
			if(StringUtils.isNotBlank(priceStr)) {
				user.setPriceStr(priceStr.substring(0, priceStr.length() - 1));
			}
		}
		return "detailSuc";
	}

	public int fromStringToInt(String str) {

		return Integer.parseInt(str);
	}

	// 添加商家评价接口
	public String addVsToMer() {
		String userId = getRequest().getParameter("userId");
		String salesId = getRequest().getParameter("salesId");
		Sales sales = null;
		if(StringUtils.isNotBlank(salesId)){
			sales = salesManager.get(Integer.parseInt(salesId));
		}
		String merId = getRequest().getParameter("merId");
		User appUser = userManager.getUserById(Integer.parseInt(userId));
		User merUser = userManager.getUserById(Integer.parseInt(merId));
		String content = getRequest().getParameter("content");
		int score = Integer.parseInt(getRequest().getParameter("score"));
		//更新用户分数
		if(null!=merUser.getAllSocres()){
			merUser.setAllSocres(Integer.parseInt(merUser.getAllSocres())+score+"");
		}else{
			merUser.setAllSocres(score+"");
		}
		getManager().update(merUser);
		MerchantValidations mv = new MerchantValidations();
		if(null!=sales){
			mv.setSales(sales);
		}
		mv.setAppUser(appUser);
		mv.setMerchantUser(merUser);
		mv.setContent(content);
		mv.setCreateTime(new Date());
		mv.setScore(score);
		merchantValidationManager.save(mv);
		result.put("msg", "ok");
		return "addSuc";
	}

	// 商家评论列表接口
	public String getVsOfMerchant() {
		Integer merId = Integer.parseInt(getRequest().getParameter("merId"));
		StringBuffer hql = new StringBuffer("select a.content,b.phone,a.create_time as createTime from merchant_validation a LEFT JOIN users b on a.appuser_id = b.id where a.meruser_id=? ORDER BY a.create_time desc limit ?,?  ");
		userList = jdbcTemplate.queryForList(hql.toString(),new Object[] { merId,
						(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
						fromStringToInt(pageCount) });
		return "nearList";
	}

	// 附近商家接口
	/**
	 * @param orderId
	 *            订单id
	 * @param latitude
	 *            纬度
	 * @param longitude
	 *            经度
	 * @return
	 */
	public String getNearMerchant() {
		Double latitude = Double.valueOf(getRequest().getParameter("latitude"));
		Double longitude = Double.valueOf(getRequest().getParameter("longitude"));
		String sql = "select id,name,address,phone,legal_person_name,loc_x,loc_y,image_url,"
				+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-loc_x*PI()/180)/2),2) + COS(?*PI()/180)* COS(loc_x*PI()/180)*POW(SIN((?*PI()/180-loc_y*PI()/180)/2),2)))*1000)AS juli "
				+ " from users where type='agent' and fxsjb='agent_level_village'and visible='1'  having juli < "
				+ AmolUserConstants.JULI + "    ORDER BY juli asc";
		userList = jdbcTemplate.queryForList(sql, new Object[] { longitude, longitude, latitude });
		return "nearList";

	}
	/**
	 * 特价购（商品信息） 特价购的商品类型id是38010881
	 * 
	 * @return
	 */
	public String getTjgProducts() {
		if(!"".equals(getRequest().getParameter("latitude"))){
			Double latitude = Double.valueOf(getRequest().getParameter("latitude"));
			Double longitude = Double.valueOf(getRequest().getParameter("longitude"));
			String sql = "select b.id,b.name,b.remark,cast(b.original_price as char(20))original_price,cast(b.present_price as char(20))present_price,b.imageUrl,"
					+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-a.loc_x*PI()/180)/2),2) + COS(?*PI()/180)* COS(a.loc_x*PI()/180)*POW(SIN((?*PI()/180-a.loc_y*PI()/180)/2),2)))*1000)AS juli "
					+ " from users a left join Products b on a.id=b.user_id left join products_sort c on b.prosort_id=c.id  where a.type='agent' and (b.up_down_goodshelf is null or b.up_down_goodshelf='1') and a.fxsjb='agent_level_village'and a.visible='1' and b.visible='1' and c.id=38010881  ";
			if (null != regionId && !"".equals(regionId)) {
				sql += " and a.region_id=? ORDER BY juli asc  limit ?,?";
			} else {
				sql += " having juli < " + AmolUserConstants.JULI + " ORDER BY juli asc  limit ?,?";
				// sql+=" having juli < 1 ORDER BY juli asc limit ?,?";
			}
	
			if (null != regionId && !"".equals(regionId)) {
				userList = jdbcTemplate.queryForList(sql,
						new Object[] { longitude, longitude, latitude, Integer.parseInt(regionId),
								(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
								fromStringToInt(pageCount) });
			} else {
				userList = jdbcTemplate.queryForList(sql,
						new Object[] { longitude, longitude, latitude,
								(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
								fromStringToInt(pageCount) });
			}
		}
		return "nearList";
	}

	/**
	 * 附近云便利接口 商品类型id 云便利 38010880 购物商家类型id 31850506
	 * 
	 * @return
	 */
	public String appYblIndex() {
		// 获取购物类型商家
		// Double latitude =
		// Double.valueOf(getRequest().getParameter("latitude"));
		// Double longitude =
		// Double.valueOf(getRequest().getParameter("longitude"));
		// if (null == regionId || "".equals(regionId)) {
		// String sql = "select
		// a.id,a.name,a.address,a.phone,a.legal_person_name,a.loc_x,a.loc_y,a.image_url
		// as imageURL,"
		// +
		// "ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-a.loc_x*PI()/180)/2),2)
		// + COS(?*PI()/180)*
		// COS(a.loc_x*PI()/180)*POW(SIN((?*PI()/180-a.loc_y*PI()/180)/2),2)))*1000)AS
		// juli "
		// + " from users a left join products_sort b on a.prosort_id=b.id where
		// a.type='agent' and a.status='1' and a.fxsjb='agent_level_village'and
		// a.visible='1' and b.id = 31850506 and having juli <
		// "+AmolUserConstants.JULI+" ORDER BY juli asc limit ?,?";
		// userList = jdbcTemplate.queryForList(sql,
		// new Object[] { longitude, longitude, latitude,
		// (fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
		// fromStringToInt(pageCount) });
		// } else {
		// userList = getAppyblIndex(pageCount, pageNumber, latitude,
		// longitude);
		// }
		if(!"".equals(getRequest().getParameter("latitude"))){
			Double latitude = Double.valueOf(getRequest().getParameter("latitude"));
			Double longitude = Double.valueOf(getRequest().getParameter("longitude"));
			String sql = "select b.id,b.name,b.remark,cast(b.original_price as char(20))original_price,cast(b.present_price as char(20))present_price,b.imageUrl,"
					+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-a.loc_x*PI()/180)/2),2) + COS(?*PI()/180)* COS(a.loc_x*PI()/180)*POW(SIN((?*PI()/180-a.loc_y*PI()/180)/2),2)))*1000)AS juli "
					+ " from users a left join Products b on a.id=b.user_id left join products_sort c on b.prosort_id=c.id  where a.type='agent' and (b.up_down_goodshelf is null or b.up_down_goodshelf='1') and a.fxsjb='agent_level_village'and a.visible='1' and b.visible='1' and c.id=38010880  ";
			if (null != regionId && !"".equals(regionId)) {
				sql += " and a.region_id=? ORDER BY juli asc  limit ?,?";
			} else {
				sql += " having juli < " + AmolUserConstants.JULI + " ORDER BY juli asc  limit ?,?";
			}
	
			if (null != regionId && !"".equals(regionId)) {
				userList = jdbcTemplate.queryForList(sql,
						new Object[] { longitude, longitude, latitude, Integer.parseInt(regionId),
								(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
								fromStringToInt(pageCount) });
			} else {
				userList = jdbcTemplate.queryForList(sql,
						new Object[] { longitude, longitude, latitude,
								(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
								fromStringToInt(pageCount) });
			}
		}
		return "nearList";
	}

	public void phoneJfProducts() throws IOException {
		// 一级商品分类id
		Integer prosortId = Integer.parseInt(getRequest().getParameter("proSortId"));
		if(!"".equals(getRequest().getParameter("latitude"))){
			Double latitude = Double.valueOf(getRequest().getParameter("latitude"));
			Double longitude = Double.valueOf(getRequest().getParameter("longitude"));
			// 根据一级分类查询二级分类
			String sql = "select ps.id,ps.name from products_sort ps where ps.parent_id=? and type=1 and status='1' ";
			JsonObject maps = new JsonObject();
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { prosortId });
			for (Map<String, Object> m : list){
				m.put("id","");
				m.put("name","全部");
			}
			JsonObject jsonObject = new JsonObject();
			JsonArray jsonArray = new JsonArray();
			for (Map<String, Object> map : list) {
				List<Map<String, Object>> myList = this.getJfProductsByErJFl(latitude, longitude,
						Integer.parseInt(String.valueOf(map.get("id"))));
				if (null != myList && myList.size() > 0) {
					// maps.put("proName:"+String.valueOf(map.get("name")),
					// "detail:"+myList.toString());
					maps.addProperty("proName", String.valueOf(map.get("name")));
					Set set = null;
					Iterator<String> setKey = null;
					String keyName = null;
					for (Map mapObj : myList) {
						set = mapObj.keySet();
						setKey = set.iterator();
						while (setKey.hasNext()) {
							keyName = setKey.next();
							jsonObject.addProperty(keyName, String.valueOf(mapObj.get(keyName)));
						}
						jsonArray.add(jsonObject);
						jsonObject = new JsonObject();
					}
					maps.add("detail", jsonArray);
					// maps.put("detail", myList);
					jsonArray = new JsonArray();
					jfSortAndProduts.add(maps);
					maps = new JsonObject();
				}
			}
		}
		if (null != jfSortAndProduts) {
			PrintWriter out = getResponse().getWriter();
			getResponse().setHeader("content-type", "text/html;charset=UTF-8");
			jfProductsStrs = jfSortAndProduts.toString();
			out.print(jfProductsStrs);
			System.out.println(jfProductsStrs);
		}
	}
	/**
	 * 积分商品列表接口
	 * @throws IOException
	 */
	public void phoneJfProductsList()throws IOException {
		Integer prosortId = Integer.parseInt(getRequest().getParameter("proSortId"));
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		String sortStr = getRequest().getParameter("sortStr").toString();
		if(!"".equals(getRequest().getParameter("latitude"))){
			Double latitude = Double.valueOf(getRequest().getParameter("latitude"));
			Double longitude = Double.valueOf(getRequest().getParameter("longitude"));
			String sql = "select b.id,b.name,b.remark,b.integral,b.imageUrl,"
					+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN(("+longitude+"*PI()/180-a.loc_x*PI()/180)/2),2) + COS("+longitude+"*PI()/180)* COS(a.loc_x*PI()/180)*POW(SIN(("+latitude+"*PI()/180-a.loc_y*PI()/180)/2),2)))*1000)AS juli "
					+ " from users a left join Products b on a.id=b.user_id left join products_sort c on b.prosort_id=c.id  where a.type='agent' and (b.up_down_goodshelf is null or b.up_down_goodshelf='1') and a.fxsjb='agent_level_county' and b.integral is not null";
			System.out.println("1========"+getRequest().getParameter("erjiTypeId"));
			if(getRequest().getParameter("erjiTypeId")!=null&&!"".equals(getRequest().getParameter("erjiTypeId"))){
				Integer erjiTypeId = Integer.parseInt(getRequest().getParameter("erjiTypeId"));
				sql += "  and c.id= "+erjiTypeId+" ";
			}else{
				sql += "  and c.id in (select ps.id  from products_sort ps where ps.parent_id= "+prosortId+" and type=1 and status='1' )  ";
			}
			if (null != regionId && !"".equals(regionId)) {
				sql += " and a.region_id= "+Integer.parseInt(regionId)+" ";
			} else {
				sql += " having juli < " + AmolUserConstants.JULI + "  ";
			}
			if(null!=sortStr&&!"".equals(sortStr)){
				sql += " order by "+sortStr+" " ;
			}else{
				sql += " order by  ORDER BY juli asc ";
			}
			sql += "  limit "+(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount)+","+fromStringToInt(pageCount)+" ";
			JsonObject maps = new JsonObject();
			System.out.println("2====="+sql);
			List<Map<String, Object>> myList=jdbcTemplate.queryForList(sql);
			PrintWriter out = getResponse().getWriter();
			getResponse().setHeader("content-type", "text/html;charset=UTF-8");
			out.print(JSONArray.fromObject(myList).toString());
		}
	}
	
	/**
	 * 手机客户端访问特价购商品列表接口
	 * @throws IOException
	 */
	public void phoneTjProductsList()throws IOException {
		
		Integer prosortId = Integer.parseInt(getRequest().getParameter("proSortId"));
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		String sortStr = getRequest().getParameter("sortStr").toString();
		String sql = "select b.id,b.name,b.remark,cast(b.original_price as char(20)) as original_price,cast(b.present_price as char(20)) as present_price,b.imageUrl "
				+ " from users a left join Products b on a.id=b.user_id left join products_sort c on b.prosort_id=c.id where a.type='agent' and (b.up_down_goodshelf is null or b.up_down_goodshelf='1') and a.fxsjb='"+AmolUserConstants.AGENT_LEVEL_VILLAGE+"' "
				+ " and b.integral is null "
				+ " and b.original_price is not null and b.present_price is not null and is_normal = '"+ProductConstants.SPECIAL+"' "
				+ " and b.visible = '1'";
		
		System.out.println("1========"+getRequest().getParameter("erjiTypeId"));
		if(getRequest().getParameter("erjiTypeId")!=null&&!"".equals(getRequest().getParameter("erjiTypeId"))){
			Integer erjiTypeId = Integer.parseInt(getRequest().getParameter("erjiTypeId"));
			sql += "  and c.id= "+erjiTypeId+" ";//查询二级分类下的商品
		}else{//查询一级分类下的商品
			sql += "  and c.id in (select ps.id  from products_sort ps where ps.parent_id= "+prosortId+" and type=1 and status='1' )  ";
		}
//		if (null != regionId && !"".equals(regionId)) {//根据地区查询商家
//			sql += " and a.region_id= "+Integer.parseInt(regionId)+" ";
//		}
		if(null!=sortStr&&!"".equals(sortStr)){//按照售价降序、升序排序
			sql += " order by b."+sortStr+" " ;
		}else{
			sql += " ORDER BY b.present_price asc ";
		}
		sql += "  limit "+(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount)+","+fromStringToInt(pageCount)+" ";
		JsonObject maps = new JsonObject();
		System.out.println("2====="+sql);
		List<Map<String, Object>> myList=jdbcTemplate.queryForList(sql);
		PrintWriter out = getResponse().getWriter();
		getResponse().setHeader("content-type", "text/html;charset=UTF-8");
		out.print(JSONArray.fromObject(myList).toString());
		
	}
	// 附近商家的积分商品
	public List<Map<String, Object>> getJfProductsByErJFl(Double latitude, Double longitude, Integer ejProsortId) {
		List<Map<String, Object>> userList = null;
		String sql = "select b.id,b.name,b.remark,b.integral,b.imageUrl,"
				+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-a.loc_x*PI()/180)/2),2) + COS(?*PI()/180)* COS(a.loc_x*PI()/180)*POW(SIN((?*PI()/180-a.loc_y*PI()/180)/2),2)))*1000)AS juli "
				+ " from users a left join Products b on a.id=b.user_id left join products_sort c on b.prosort_id=c.id  where a.type='agent' and (b.up_down_goodshelf is null or b.up_down_goodshelf='1') and a.fxsjb='agent_level_county' and b.integral is not null  and c.id=?  ";
		if (null != regionId && !"".equals(regionId)) {
			sql += " and a.region_id=?  ORDER BY juli asc limit ?,?";
		} else {
			sql += " having juli < " + AmolUserConstants.JULI + "    ORDER BY juli asc limit ?,?";
			// sql+=" having juli < 1 ORDER BY juli asc limit ?,?";
		}
		System.out.println("-----------regionId=" + regionId);
		if (null != regionId && !"".equals(regionId)) {
			userList = jdbcTemplate.queryForList(sql,
					new Object[] { longitude, longitude, latitude, ejProsortId, Integer.parseInt(regionId),
							(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
							fromStringToInt(pageCount) });
		} else {
			userList = jdbcTemplate.queryForList(sql,
					new Object[] { longitude, longitude, latitude, ejProsortId,
							(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
							fromStringToInt(pageCount) });
		}
		return userList;
	}

	// 附近摇一摇
	public String getYyyOfNear() {
		if(!"".equals(getRequest().getParameter("latitude"))){
			Double latitude = Double.valueOf(getRequest().getParameter("latitude"));
			Double longitude = Double.valueOf(getRequest().getParameter("longitude"));
			String sql = "select a.id,a.start_time,a.end_time,a.image_url,a.event_name,a.minus_integal,"
					+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-b.loc_x*PI()/180)/2),2) + COS(?*PI()/180)* COS(b.loc_x*PI()/180)*POW(SIN((?*PI()/180-b.loc_y*PI()/180)/2),2)))*1000)AS juli "
					+ " from yyy_event a left join users b on a.user_id=b.id  where b.type='agent' and a.is_used='1' and b.fxsjb='agent_level_county'  ";
			if (null != regionId && !"".equals(regionId)) {
				sql += " and b.region_id=? ORDER BY juli asc  limit ?,?";
			} else {
				sql += " having juli < " + AmolUserConstants.JULI + " ORDER BY juli asc  limit ?,?";
				// sql+=" having juli < 1 ORDER BY juli asc limit ?,?";
			}
			if (null != regionId && !"".equals(regionId)) {
				userList = jdbcTemplate.queryForList(sql,
						new Object[] { longitude, longitude, latitude, Integer.parseInt(regionId),
								(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
								fromStringToInt(pageCount) });
			} else {
				userList = jdbcTemplate.queryForList(sql,
						new Object[] { longitude, longitude, latitude,
								(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount),
								fromStringToInt(pageCount) });
			}
		}
		return "nearList";
	}

	// 附近商家的积分商品(模糊查询)
	public String getJfProductsWithMajor() {
		Double latitude = Double.valueOf(getRequest().getParameter("latitude"));
		Double longitude = Double.valueOf(getRequest().getParameter("longitude"));
		String proName = getRequest().getParameter("proName");
		String sql = "select b.id,b.name,b.remark,b.integral,b.imageUrl,"
				+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-a.loc_x*PI()/180)/2),2) + COS(?*PI()/180)* COS(a.loc_x*PI()/180)*POW(SIN((?*PI()/180-a.loc_y*PI()/180)/2),2)))*1000)AS juli "
				+ " from users a left join Products b on a.id=b.user_id left join products_sort c on b.prosort_id=c.id  where a.type='agent' and (b.up_down_goodshelf is null or b.up_down_goodshelf='1') and a.fxsjb='agent_level_county' and b.integral is not null  and b.name like '%"
				+ proName + "%'  having juli < " + AmolUserConstants.JULI + "    ORDER BY juli asc limit ?,?";
		userList = jdbcTemplate.queryForList(sql, new Object[] { longitude, longitude, latitude,
				(fromStringToInt(pageNumber) - 1) * fromStringToInt(pageCount), fromStringToInt(pageCount) });
		return "nearList";
	}

	public RegionManager getRegionManager() {
		return regionManager;
	}

	public void setRegionManager(RegionManager regionManager) {
		this.regionManager = regionManager;
	}

	public List<User> getList() {
		return list;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Map<Object, Object>> getUserList() {
		return userList;
	}

	public void setUserList(List<Map<Object, Object>> userList) {
		this.userList = userList;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public JsonArray getJfSortAndProduts() {
		return jfSortAndProduts;
	}

	public void setJfSortAndProduts(JsonArray jfSortAndProduts) {
		this.jfSortAndProduts = jfSortAndProduts;
	}

	public String getJfProductsStrs() {
		return jfProductsStrs;
	}

	public void setJfProductsStrs(String jfProductsStrs) {
		this.jfProductsStrs = jfProductsStrs;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}


	public void setList(List list) {
		this.list = list;
	}

	public Map<String, Object> getReMap() {
		return reMap;
	}

	public void setReMap(Map<String, Object> reMap) {
		this.reMap = reMap;
	}

	public List getRzfhList() {
		return rzfhList;
	}

	public void setRzfhList(List rzfhList) {
		this.rzfhList = rzfhList;
	}

	public MyBank getMb() {
		return mb;
	}

	public void setMb(MyBank mb) {
		this.mb = mb;
	}



	public String[] getFilesFileName() {
		return filesFileName;
	}

	public void setFilesFileName(String[] filesFileName) {
		this.filesFileName = filesFileName;
	}

	public String[] getCarFileFileName() {
		return carFileFileName;
	}

	public void setCarFileFileName(String[] carFileFileName) {
		this.carFileFileName = carFileFileName;
	}

	public void setCarFile(File[] carFile) {
		this.carFile = carFile;
	}

	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}

	public String[] getFilesContentType() {
		return filesContentType;
	}

	public void setFilesContentType(String[] filesContentType) {
		this.filesContentType = filesContentType;
	}

	public File[] getWxFiles() {
		return wxFiles;
	}

	public void setWxFiles(File[] wxFiles) {
		this.wxFiles = wxFiles;
	}

	public String[] getWxFilesFileName() {
		return wxFilesFileName;
	}

	public void setWxFilesFileName(String[] wxFilesFileName) {
		this.wxFilesFileName = wxFilesFileName;
	}

	public RenZheng getRenZheng() {
		return renZheng;
	}

	public void setRenZheng(RenZheng renZheng) {
		this.renZheng = renZheng;
	}

	public YhShenqing getYhShenqing() {
		return yhShenqing;
	}

	public void setYhShenqing(YhShenqing yhShenqing) {
		this.yhShenqing = yhShenqing;
	}
	public List<YhShenqing> getSqList() {
		return sqList;
	}

	public void setSqList(List<YhShenqing> sqList) {
		this.sqList = sqList;
	}

}


