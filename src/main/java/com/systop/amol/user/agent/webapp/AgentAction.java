package com.systop.amol.user.agent.webapp;

import com.systop.amol.base.customer.model.Customer;
import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.amol.user.agent.model.InfomationRecord;
import com.systop.amol.user.agent.phone.PhoneAgentAction;
import com.systop.amol.user.agent.service.AgentManager;
import com.systop.amol.user.agent.service.InfoMationRecordManager;
import com.systop.amol.user.agent.service.MerchantValidationManager;
import com.systop.amol.user.agent.service.YhShenqingManager;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.UserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.RoleManager;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.Constants;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;
import com.thirdParty.yunTongXun.SDKTestSendTemplateSMS;
import com.thirdParty.yunTongXun.SMSConstants;
import junit.framework.Assert;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
/**
 * 分销商用户管理Action
 * 
 */
@SuppressWarnings({ "serial", "unused", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AgentAction extends DefaultCrudAction<User, AgentManager> {
	/**
	 * 用于加密密码
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private CustomerManager customerManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private RegionManager regionManager;
	@Autowired
	private ProductSortManager productSortManager;
	@Autowired
	private InfoMationRecordManager infoMationRecordManager;
	@Autowired
	private YhShenqingManager yhShenqingManager;
	@Autowired
	MerchantValidationManager merchantValidationManager; 
	private List<User> list;
	/** 分页，页码 */
	private String pageNumber = "1";
	
	/** 每页显示条数 */
	private String pageCount = "10";
	
	/** 地区id */
	private Integer regionId;
	/** 地区名称 */
	private String regionNameCun;
	private File attch;

	private String attchFileName;

	private String attchFolder = "/uploadFiles/fileAttch/";
	private String ifRcommend;
	private String proSortName;
	private User user;
	private String strs;
	private String count;


	public String editxx(){

		return "editxx";
	}

	/**
	 * 经销商查询列表，县分销商
	 */
	public String qbIndex() {
		User user = getManager().get(UserUtil.getPrincipal(getRequest()).getId());// 得到当前登录用户
		Assert.assertNotNull("当前登录用户为空", user);
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from User u where u.visible='1' and  u.type='vip' ");
//		hql.append(" and u.superior.id = ?");
//		args.add(user.getId());
		//根据地区查询
		if(null == regionId || 0 == regionId){
			if(null != user.getRegion()){
				hql.append(" and u.region.code like ?");
				args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
				regionNameCun = user.getRegion().getName();
			}
		}else{
			Region region = regionManager.get(Integer.valueOf(regionId));
			hql.append(" and u.region.code like ?");
			args.add(MatchMode.START.toMatchString(region.getCode()));
			regionNameCun = region.getName();
		}
		System.out.println("regionNameCun = "+regionNameCun);

		//用户状态
		if (StringUtils.isNotBlank(getModel().getStatus())) {
			hql.append(" and u.status like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getStatus()));
		}
		if (StringUtils.isNotBlank(getModel().getLoginId())) {
			hql.append(" and u.loginId like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getLoginId()));
		}
		if(null!=getModel().getProductSort()&&null!=getModel().getProductSort().getId()&&0!=getModel().getProductSort().getId()){
			System.out.println("------getModel().getProductSort()-----"+getModel().getProductSort().getId());
			ProductSort proSort=productSortManager.get(getModel().getProductSort().getId());
			hql.append(" and u.productSort.id= ? ");
			args.add(getModel().getProductSort().getId());
			proSortName=proSort.getName();
		}
		if (StringUtils.isNotBlank(getModel().getName())) {
			hql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		hql.append(" order by u.createTime desc");
		System.out.println("hql = "+hql);
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		List<User> uList = page.getData();
		for(User u : uList){
			u.setJyddm(userManager.getMoneyOfWitchTypeorders(u.getId(),"jy"));
			u.setBxddm(userManager.getMoneyOfWitchTypeorders(u.getId(),"wx"));
			u.setWxddm(userManager.getMoneyOfWitchTypeorders(u.getId(),"bx"));
			if(null==u.getAllMoney()||u.getAllMoney()<0){
				u.setAllMoney(0.0);
			}
			if(null==u.getChargeAccount()){
				u.setChargeAccount(0.0);
			}
			if(null==u.getFxMoney()){
				u.setFxMoney(0.0);
			}
			if(null==u.getFyMoney()){
				u.setFyMoney(0.0);
			}
		}
		getRequest().setAttribute("loginUser", userManager.get(UserUtil.getPrincipal(getRequest()).getId()));
		restorePageData(page);
		return "qbIndex";
	}
	public String saveTsRecord(){
		User user = getManager().get(UserUtil.getPrincipal(getRequest()).getId());// 得到当前登录用户
		String xxtype = getRequest().getParameter("xxtype");
		String tstype = getRequest().getParameter("tstype");
		String descn = getRequest().getParameter("descn");
		String tuIds = getRequest().getParameter("tuIds");
		System.out.println("--------------tuIds="+tuIds);
		if(null!=tuIds && StringUtils.isNotBlank(tuIds)){
			String[] idsArrays = tuIds.split(",");
			for(String id : idsArrays){
				PhoneAgentAction phoneAgentAction = new PhoneAgentAction();
				InfomationRecord infor = new InfomationRecord();
				try {
					User toU = userManager.get(Integer.parseInt(id));
					if(tstype.equals("1")) {
						phoneAgentAction.pushUser(id, descn);
					}
					if(tstype.equals("0")){
						SDKTestSendTemplateSMS.sendSmss(toU.getPhone(), SMSConstants.SEND_WEB,"{\"name\":'"+toU.getLoginId()+"'}"
						,"友情提示");
					}
				}catch (Exception e){
					infor.setRsState("0");
					e.printStackTrace();
				}
				User tu = userManager.get(Integer.parseInt(id));
				infor.setSendUser(user);
				infor.setXxtype(xxtype);
				infor.setCreateTime(new Date());
				infor.setTstype(tstype);
				infor.setSendContent(descn);
				infor.setReceiveUser(tu);
				infoMationRecordManager.save(infor);
			}
		}
		return "toInforList";
	}
	public static  void main(String[] args){
		System.out.println("--------------------------------");
		SDKTestSendTemplateSMS.sendSmss("15612930472", SMSConstants.SEND_WEB,"{\"name\":\"15612930472\"}","注册验证"
		);
		System.out.println("--------------------------------");
	}
	public String toInforList() {
		User user = getManager().get(UserUtil.getPrincipal(getRequest()).getId());// 得到当前登录用户
		Assert.assertNotNull("当前登录用户为空", user);
		List<Object> args = new ArrayList<Object>();
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer hql = new StringBuffer("from InfomationRecord ");
		hql.append(" order by createTime desc");
		System.out.println("hql = "+hql);
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		getRequest().setAttribute("loginUser", userManager.get(UserUtil.getPrincipal(getRequest()).getId()));
		restorePageData(page);
		return "toInforListPage";
	}
	//查询会员信息
	public String getVipInfos(){
		System.out.println("----------------pageNumber="+getPageNo());
		System.out.println("----------------pageCount="+getPageCount());
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from User u where u.visible='1' and  u.type='vip'  ");
		if (StringUtils.isNotBlank(getModel().getStatus())) {
			hql.append(" and u.status =?");
			args.add(getModel().getStatus());
		}
		String resource = getRequest().getParameter("resource");
		if (StringUtils.isNotBlank(resource)) {
			hql.append(" and u.loginType = ?");
			args.add(resource);
		}
		if (StringUtils.isNotBlank(getModel().getName())) {
			hql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (StringUtils.isNotBlank(getModel().getPhone())) {
			hql.append(" and u.phone like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getPhone()));
		}
		if (StringUtils.isNotBlank(getModel().getLoginId())) {
			hql.append(" and u.loginId like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getLoginId()+""));
		}
		String regionId = getRequest().getParameter("regionId");
		if (StringUtils.isNotBlank(regionId)) {
			hql.append(" and u.region.id =?");
			args.add(Integer.parseInt(regionId));
		}
		String prosortId = getRequest().getParameter("prosortId");
		if (StringUtils.isNotBlank(prosortId)) {
			hql.append(" and u.vipType.id =?");
			args.add(Integer.parseInt(prosortId));
		}
		/*if (StringUtils.isNotBlank(proSortId)) {
			hql.append(" and u.productSort.id =?");
			args.add(Integer.parseInt(proSortId));
		}*/
		hql.append(" order by u.createTime desc");
		System.out.println("hql = "+hql);
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return "vipIndex";
	}
	//根据类型查询附近商家
	public String getAllMerchat() {
		StringBuffer hql = new StringBuffer("from User u where u.visible='1' and u.type='agent' and u.fxsjb ='agent_level_village' ");
		hql.append(" order by u.createTime desc");
		System.out.println("hql = "+hql);
		list = getManager().query(hql.toString());
		return "MerIndex";
	}
	//根据类型查询附近商家
	public String MerIndex() {
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		Double latitude=Double.valueOf(getRequest().getParameter("latitude"));
		Double longitude=Double.valueOf(getRequest().getParameter("longitude"));
		String proSortId=getRequest().getParameter("proSortId");
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from User u where u.visible='1' ");
		/*if(StringUtils.isNotEmpty(regionId)){
    	Region region = regionManager.get(Integer.valueOf(regionId));
    	hql.append(" and u.region.code like ?");
    	args.add(MatchMode.START.toMatchString(region.getCode()));
    	}*/
    if (StringUtils.isNotBlank(proSortId)) {
			hql.append(" and u.productSort.id =?");
			args.add(Integer.parseInt(proSortId));
		}
		hql.append(" order by u.createTime desc");
		System.out.println("hql = "+hql);
		page = getManager().pageQuery(page, hql.toString(), args.toArray());

		list=page.getData();
		return "MerIndex";
	}
	/**
	 * 经销商查询列表，县分销商
	 */
	public String index() {
		User user = getManager().get(UserUtil.getPrincipal(getRequest()).getId());// 得到当前登录用户
		Assert.assertNotNull("当前登录用户为空", user);
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from User u where  (u.fxsjb='"+AmolUserConstants.AGENT_LEVEL_VILLAGE+"' or u.fxsjb='jyc') and u.type = '"+AmolUserConstants.AGENT+"' and u.visible='1' and u.id != "+user.getId());
//		hql.append(" and u.superior.id = ?");
//		args.add(user.getId());
	  //根据地区查询
    if(null == regionId || 0 == regionId){
    	if(null != user.getRegion()){
    		hql.append(" and u.region.code like ?");
    	  args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
    	  regionNameCun = user.getRegion().getName();
    	}
    }else{
    	Region region = regionManager.get(Integer.valueOf(regionId));
    	hql.append(" and u.region.code like ?");
    	args.add(MatchMode.START.toMatchString(region.getCode()));
    	regionNameCun = region.getName();
    }
    System.out.println("regionNameCun = "+regionNameCun);
    
    //用户状态
    if (StringUtils.isNotBlank(getModel().getStatus())) {
			hql.append(" and u.status like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getStatus()));
		}
		if(null!=getModel().getProductSort()&&null!=getModel().getProductSort().getId()&&0!=getModel().getProductSort().getId()){
			System.out.println("------getModel().getProductSort()-----"+getModel().getProductSort().getId());
			ProductSort proSort=productSortManager.get(getModel().getProductSort().getId());
			hql.append(" and u.productSort.id= ? ");
			args.add(getModel().getProductSort().getId());
			proSortName=proSort.getName();
		}
		if (StringUtils.isNotBlank(getModel().getName())) {
			hql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		hql.append(" order by u.createTime desc");
		System.out.println("hql = "+hql);
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		getRequest().setAttribute("loginUser", userManager.get(UserUtil.getPrincipal(getRequest()).getId()));
		restorePageData(page);
		return INDEX;
	}
	/**
	 * 经销商查询列表，村分销商
	 */
	public String indexCun() {
		User user = getManager().get(UserUtil.getPrincipal(getRequest()).getId());// 得到当前登录用户
		System.out.println("------------UserUtil.getPrincipal(getRequest()).getId()-----"+UserUtil.getPrincipal(getRequest()).getId());
		Assert.assertNotNull("当前登录用户为空", user);
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from User u where u.visible='1' and u.type = ?");
		args.add(AmolUserConstants.AGENT);
		//根据地区查询
    if(null == regionId || 0 == regionId){
    	if(null != user.getRegion()){
    		hql.append(" and u.region.code like ?");
    	  args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
    	  regionNameCun = user.getRegion().getName();
    	}
    }else{
    	Region region = regionManager.get(Integer.valueOf(regionId));
    	hql.append(" and u.region.code like ?");
    	args.add(MatchMode.START.toMatchString(region.getCode()));
    	regionNameCun = region.getName();
    }
    //用户状态
    if (StringUtils.isNotBlank(getModel().getStatus())) {
			hql.append(" and u.status like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getStatus()));
		}
    if(null!=getModel().getProductSort()&&null!=getModel().getProductSort().getId()&&0!=getModel().getProductSort().getId()){
    	System.out.println("------getModel().getProductSort()-----"+getModel().getProductSort());
    	ProductSort proSort=productSortManager.get(getModel().getProductSort().getId());
    	hql.append(" and u.merSortStr like ?");
		args.add(MatchMode.ANYWHERE.toMatchString(getModel().getProductSort().getId()+""));
			proSortName=proSort.getName();
    }
//		if(AmolUserConstants.AGENT.equals(user.getType())){//一级分销商登录查询
//			hql.append(" and u.superior.id = ?");
//			args.add(user.getId());
//		}else if(null == user.getType()){//admin登录查询
//			hql.append(" and (u.superior.superior.id = ? or u.superior.id = ?)");
//			args.add(user.getId());
//			args.add(user.getId());
//		}else if(AmolUserConstants.EMPLOYEE.equals(user.getType())){//平台员工登录查询
//			hql.append(" and u.superior.superior.id = ?");
//			args.add(user.getSuperior().getId());
//		}
		if (StringUtils.isNotBlank(getModel().getName())) {
			hql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		hql.append(" order by u.createTime desc");
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		System.out.println("hql = "+hql);
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		List<User> list=page.getData();
		for (User u : list) {
			String avgScores=merchantValidationManager.getAvgScores(u);
			System.out.println("--------------avgScores------------->"+avgScores);
			if(null!=avgScores&&!"null".equals(avgScores)){
			u.setAllSocres(avgScores.split("\\.")[0]);
		}else{
			u.setAllSocres("0");
			}
		}
		getRequest().setAttribute("loginUser", userManager.get(UserUtil.getPrincipal(getRequest()).getId()));
		return INDEX;
	}
	public String startInitXyJe(){
		String userId = getRequest().getParameter("userId");
		User user = getManager().get(Integer.parseInt(userId));
		if(null!=user){
			user.setIncomeAll(null);
			getManager().update(user);
			getRequest().setAttribute("obj", user);
		}
		return "editVip";
	}
	public String fillXyJe(){
		String userId = getRequest().getParameter("userId");
		String fillMoney = getRequest().getParameter("fillMoney");
		User user = getManager().get(Integer.parseInt(userId));
		if(null!=user){
			if(null!=user.getIncomeAll()){
			user.setIncomeAll(user.getIncomeAll()+Double.parseDouble(fillMoney));
			}else{
				user.setIncomeAll(Double.parseDouble(fillMoney));
			}
			getManager().update(user);
			getRequest().setAttribute("obj", user);

		}
		return "editVip";
	}
	public String fillAccount(){
		String userId = getRequest().getParameter("userId");
		String fillMoney = getRequest().getParameter("fillMoney");
		String fillType = getRequest().getParameter("fillType");
		User user = getManager().get(Integer.parseInt(userId));
		user.setFillType(fillType);
		if(null!=user){
			if(null!=user.getAllMoney()){
				user.setAllMoney(user.getAllMoney()+Double.parseDouble(fillMoney));
			}else{
				user.setAllMoney(Double.parseDouble(fillMoney));
			}
			getManager().update(user);
			getRequest().setAttribute("obj", user);

		}
		return "editVip";
	}
	public String editVip() {
		if(null!=getModel().getId()) {
			getRequest().setAttribute("obj", getManager().get(getModel().getId()));
		}
		return "editVip";
	}
	public String saveChildAccount(){
		User user = new User();
		String loginId = getRequest().getParameter("loginId");
		user.setLoginId(loginId);
		String parentId = getRequest().getParameter("parentId");
		User pUser = getManager().get(Integer.parseInt(parentId));
		user.setSuperior(pUser);
		String password = getRequest().getParameter("password");
		user.setPassword(passwordEncoder.encodePassword(password.trim(),null));
		String name = getRequest().getParameter("name");
		user.setName(name);
		String phone = getRequest().getParameter("phone");
		user.setPhone(phone);
		String folk = getRequest().getParameter("folk");
		user.setFolk(folk);
		String address = getRequest().getParameter("address");
		user.setAddress(address);
		String allMoney = getRequest().getParameter("allMoney");
		if(StringUtils.isNotBlank(allMoney)) {
			user.setAllMoney(Double.parseDouble(allMoney));
		}
		String incomeAll = getRequest().getParameter("incomeAll");
		if(StringUtils.isNotBlank(incomeAll)) {
			user.setIncomeAll(Double.parseDouble(incomeAll));
		}
		if(null!= pUser.getIncomeAll()) {
			pUser.setIncomeAll(pUser.getIncomeAll()+Double.parseDouble(incomeAll));
		}else{
			pUser.setIncomeAll(Double.parseDouble(incomeAll));
		}
		if(null != pUser.getAllMoney()){
			pUser.setAllMoney(pUser.getAllMoney()+Double.parseDouble(allMoney));
		}else{
			pUser.setAllMoney(Double.parseDouble(allMoney));
		}
		attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
		if (attch != null) {
			String filePath = UpLoadUtil.doUpload(attch, attchFileName, attchFolder,
					getServletContext());
			user.setImageURL(filePath);
		}
		getManager().update(pUser);
		getManager().save(user);
		getRequest().setAttribute("obj", pUser);
		return "editVip";
	}
	public String setHyUser(){
		String userId = getRequest().getParameter("userId");
		if(null!=userId && !"".equals(userId)) {
			User user = getManager().get(Integer.parseInt(userId));
			user.setIfHy("1");
			getManager().update(user);
			getRequest().setAttribute("obj", user);
		}
		return "editVip";
	}
	@Override
	public String edit() {
		getRequest().setAttribute("loginUser", userManager.get(UserUtil.getPrincipal(getRequest()).getId()));
		if(null!=getModel().getId()) {
			getRequest().setAttribute("obj", getManager().get(getModel().getId()));
		}
		return super.edit();
	}
	@Transactional
	public String cunSave() {
		User user = UserUtil.getPrincipal(getRequest());
	    user=userManager.get(user.getId());
		System.out.println(user.getType());
		String productSortId = getRequest().getParameter("productSortId");
		ProductSort productSort = productSortManager.get(Integer.parseInt(productSortId));
		getModel().setProductSort(productSort);
		if (user != null) {
			if(org.apache.commons.lang.xwork.StringUtils.isNotBlank(productSortId)&&Integer.parseInt(productSortId)==41418754){
				getModel().setFxsjb("jyc");
			}else {
				getModel().setFxsjb("agent_level_village");
			}
			System.out.print(getModel().getMerSortStr()+"------"+getModel().getMerSortNameStr());
			// 添加,需要赋值上级经销商
			getModel().setSuperior(user);
			getModel().setType(AmolUserConstants.AGENT);
			// 判断身份证号不允许重复（同一经销商下）
//			if (this.getManager().getDao()
//					.exists(this.getModel(), "idCard", "superior.id","type")) {
//				this.addActionMessage("身份证号为【" + getModel().getIdCard() + "】的分销商已存在。");
//				return INPUT;
//			}
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
		if (attch != null) {
			String filePath = UpLoadUtil.doUpload(attch, attchFileName, attchFolder,
					getServletContext());
			getModel().setImageURL(filePath);
		}
		System.out.println("---------------shopOfUserId="+getModel().getShopOfUser()
		.getId());
		if(null!=user.getType()&&user.getType().equals("agent")){
			getModel().setRegion(user.getRegion());
		}
		getModel().setVisible("1");
		if (getModel().getId() == null) {
		  // 给分销商用户默认分配一个"末端经销商"的角色，便于业务操作。
			/*if (getRole() != null) {
				Role role = getRole();
				role.getUsers().add(getModel());
				getModel().getRoles().add(role);
			}*/
			getManager().getDao().clear();
			getManager().save(getModel());
			/*User uu = userManager.getUserPhone(getModel().getPhone());
			String absolutPath = getRequest().getSession().getServletContext().getRealPath("/uploadFiles/ewmPic/"+uu.getId()+".png");
			String path = getRequest().getContextPath();
			String basePath = getRequest().getScheme() + "://"
					+ getRequest().getServerName() + ":" + getRequest().getServerPort()
					+ path + "/";
			String content = basePath+"pages/amol/reglogin/regist.jsp?yqm="+uu.getId();
			QRcode handler = new QRcode();
			handler.encoderQRCode(content,absolutPath);
			getModel().setEwmImageURl("/uploadFiles/ewmPic/"+uu.getId()+".png");
			getManager().update(uu);
			System.out.println("解析的内容为"+content);
			System.out.println("========encoder success");
			String decoderContent = handler.decoderQRCode(absolutPath);
			System.out.println("解析结果如下：");
			System.out.println(decoderContent);
			System.out.println("========decoder success!!!");*/
		}else{
			getManager().getDao().clear();
			getManager().update(getModel());
		}

		return "delSuc";
	}
	/**
	 * 添加分销商信息
	 */
	@Override
	@Transactional
	public String save() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			// 添加,需要赋值上级经销商
			getModel().setSuperior(user);
			getModel().setType(AmolUserConstants.AGENT);
			getModel().setFxsjb(AmolUserConstants.AGENT_LEVEL_VILLAGE);
			// 判断身份证号不允许重复（同一经销商下）
//			if (this.getManager().getDao()
//					.exists(this.getModel(), "idCard", "superior.id","type")) {
//				this.addActionMessage("身份证号为【" + getModel().getIdCard() + "】的分销商已存在。");
//				return INPUT;
//			}
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
		if (attch != null) {
			String filePath = UpLoadUtil.doUpload(attch, attchFileName, attchFolder,
					getServletContext());
			getModel().setImageURL(filePath);
		}
		getModel().setVisible("1");
		if (getModel().getId() == null) {
		  // 给分销商用户默认分配一个"末端经销商"的角色，便于业务操作。
			if (getRole() != null) {
				Role role = getRole();
				role.getUsers().add(getModel());
				getModel().getRoles().add(role);
			}
			getManager().save(getModel());
		}else{
			getManager().getDao().clear();
			getManager().update(getModel());
		}

		return SUCCESS;
	}
	/**
	 * 重置密码
	 * 
	 * @return
	 */
	public String restPassword() {
		getModel().setPassword(
				passwordEncoder.encodePassword(getModel().getPassword(), null));
		getManager().update(getModel());
		return "restPassword";
	}
	/**
	 * 禁用分销商
	 * 
	 * @return
	 */
	@Transactional
	public String remove() {
		if (getModel().getId() != null) {
			if (getModel().getId() != null) {
				List<Customer> customers = customerManager
						.getCustomerByAgent(getModel().getId());
				if (customers.size() != 0) {
					for (Customer customer : customers) {
						customer.setStatus(Constants.NO);
						customerManager.update(customer);
					}
				}
				getManager().remove(getModel());
			}
		}
		return SUCCESS;
	}
	@Transactional
	public String removeVip() {
		if (getModel().getId() != null) {
			if (getModel().getId() != null) {
				List<Customer> customers = customerManager
						.getCustomerByAgent(getModel().getId());
				if (customers.size() != 0) {
					for (Customer customer : customers) {
						customer.setStatus(Constants.NO);
						customerManager.update(customer);
					}
				}
				getManager().remove(getModel());
			}
		}
		return "deleteVip";
	}
	@Transactional
	public String qyVip() {
		if (getModel().getId() != null) {
			User user = getManager().get(getModel().getId());
			user.setStatus("1");
			getManager().update(user);
		}
		return "deleteVip";
	}
	public String deleteVip() {
		if (getModel().getId() != null) {
			User user=userManager.get(getModel().getId());
			user.setVisible("0");
			userManager.update(user);
		}
		return "deleteVip";
	}
	public String delete() {
		if (getModel().getId() != null) {
			User user=userManager.get(getModel().getId());
			user.setVisible("0");
			userManager.update(user);
		}
		return "success";
	}
	public String unsealUserVip(){
		if (getModel().getId() != null) {
			Set<Role> loginUserRoles = getModel().getSuperior().getRoles();
			// 给分销商用户默认分配一个"分销商高级或者普通"的角色，便于业务操作。
			for (Role loginUserRole : loginUserRoles) {
				if (loginUserRole.getName().equals(UserConstants.ROLE_TOP_DEALER)) {
					Role role = getRoles().get(1);
					role.getUsers().add(getModel());
					getModel().getRoles().add(role);
				} else {
					Role role = getRoles().get(0);
					role.getUsers().add(getModel());
					getModel().getRoles().add(role);
				}
				List<Customer> customers = customerManager
						.getCustomerByAgent(getModel().getId());
				if (customers.size() != 0) {
					for (Customer customer : customers) {
						customer.setStatus(Constants.YES);
						customerManager.update(customer);
					}
				}
				// 给分销商用户默认分配一个"末端经销商"的角色，便于业务操作。
				if (getRole() != null) {
					Role role = getRole();
					role.getUsers().add(getModel());
					getModel().getRoles().add(role);
				}
				getManager().unsealUser(getModel());
				getRequest().setAttribute("loginUser", userManager.get(UserUtil.getPrincipal(getRequest()).getId()));
			}
		}
		return "deleteVip";
	}
	/**
	 * 启用分销商
	 * 
	 * @return
	 */
	public String unsealUser(){
		if (getModel().getId() != null) {
			Set<Role> loginUserRoles = getModel().getSuperior().getRoles();
			// 给分销商用户默认分配一个"分销商高级或者普通"的角色，便于业务操作。
			for (Role loginUserRole : loginUserRoles) {
				if (loginUserRole.getName().equals(UserConstants.ROLE_TOP_DEALER)) {
					Role role = getRoles().get(1);
					role.getUsers().add(getModel());
					getModel().getRoles().add(role);
				} else {
					Role role = getRoles().get(0);
					role.getUsers().add(getModel());
					getModel().getRoles().add(role);
				}
				List<Customer> customers = customerManager
						.getCustomerByAgent(getModel().getId());
				if (customers.size() != 0) {
					for (Customer customer : customers) {
						customer.setStatus(Constants.YES);
						customerManager.update(customer);
					}
				}
				// 给分销商用户默认分配一个"末端经销商"的角色，便于业务操作。
				if (getRole() != null) {
					Role role = getRole();
					role.getUsers().add(getModel());
					getModel().getRoles().add(role);
				}
				getManager().unsealUser(getModel());
				getRequest().setAttribute("loginUser", userManager.get(UserUtil.getPrincipal(getRequest()).getId()));
			}
		}
		return SUCCESS;
	}

	/**
	 * 选择经销商
	 * 
	 * @return
	 */
	public String selectAgent() {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(
				"from User u where u.type = ? and u.superior.id is null and u.status = ? ");
		args.add(AmolUserConstants.AGENT);
		args.add(Constants.YES);
		if (getModel() != null && getModel().getRegion() != null
				&& getModel().getRegion().getId() != null) {
			Region region = getManager().getDao().get(Region.class,
					getModel().getRegion().getId());
			if (region != null) {
				hql.append(" and u.region.code like ?");
				if (region.getCode().substring(2, 6).equals("0000")) {
					args.add(MatchMode.START.toMatchString(region.getCode().substring(0,
							2)));
				} else {
					if (region.getCode().substring(4, 6).equals("00")) {
						args.add(MatchMode.START.toMatchString(region.getCode().substring(
								0, 4)));
					} else {
						args.add(region.getCode());
					}
				}
			}
			// 为了返回查询中的条件数据需要将Region存入到MODEL中
			getModel().setRegion(region);
		}
		if (StringUtils.isNotBlank(getModel().getName())) {
			hql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		hql.append(" order by u.createTime desc");
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return "showIndex";
	}
	public String recomend() {
		if (getModel().getId() != null) {
			User user=userManager.get(getModel().getId());
			user.setIfRecommend(ifRcommend);
			userManager.update(user);
		}
		return "delSuc";
	}
	/**
	 * 选择分销商
	 * 
	 * @return
	 */
	public String selectEndAgent() {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(
				"from User u where u.type = ? ");
		User user = UserUtil.getPrincipal(getRequest());
		args.add(AmolUserConstants.AGENT);
		
		hql.append(" and u.fxsjb = ?");
		args.add(AmolUserConstants.AGENT_LEVEL_COUNTY);
//		if (user != null) {
//			if (user.getSuperior() == null) {
//				args.add(user.getId());
//			} else if (user.getType().equals(AmolUserConstants.EMPLOYEE)) {
//				args.add(user.getSuperior().getId());
//			}
//		} else {
//			addActionError("当前用户登录错误！请重新登录！");
//		}
//		if (getModel() != null && getModel().getRegion() != null
//				&& getModel().getRegion().getId() != null) {
//			Region region = getManager().getDao().get(Region.class,
//					getModel().getRegion().getId());
//			if (region != null) {
//				hql.append(" and u.region.code like ?");
//				if (region.getCode().substring(2, 6).equals("0000")) {
//					args.add(MatchMode.START.toMatchString(region.getCode().substring(0,
//							2)));
//				} else {
//					if (region.getCode().substring(4, 6).equals("00")) {
//						args.add(MatchMode.START.toMatchString(region.getCode().substring(
//								0, 4)));
//					} else {
//						args.add(region.getCode());
//					}
//				}
//			}
			// 为了返回查询中的条件数据需要将Region存入到MODEL中
//			getModel().setRegion(region);
//		}
		if (StringUtils.isNotBlank(getModel().getName())) {
			hql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		hql.append(" order by u.createTime desc");
		System.out.println("dddddddddddddllll"+hql);
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return "showEndIndex";
	}

	/**
	 * 返回"末端经销商角色"
	 * 
	 * @return
	 */

	public Role getRole() {
		Role endDealer = (Role) getManager().getDao().findObject(
				"from Role r where r.name=?", UserConstants.ROLE_END_DEALER);
		return endDealer;
	}

	/**
	 * 返回"分销商普通角色","分销商高级角色"
	 * 
	 * @return
	 */
	public List<Role> getRoles() {
		Role normal = (Role) getManager().getDao().findObject(
				"from Role r where r.name=?", UserConstants.ROLE_END_DEALER_GENERAL);
		Role senior = (Role) getManager().getDao().findObject(
				"from Role r where r.name=?", UserConstants.ROLE_END_DEALER);
		List<Role> roles = new ArrayList<Role>(2);
		roles.add(normal);
		roles.add(senior);
		return roles;
	}

	/**
	 * 返回用户所具有的角色名称
	 * 
	 * @return
	 */
	private Set getPrincipalRoles() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user == null) {
			return Collections.EMPTY_SET;
		}
		Collection<Role> roles = loadRoles(user);
		if (roles == null || roles.size() == 0) {
			return Collections.EMPTY_SET;
		}
		Set set = new HashSet(roles.size());
		for (Role role : roles) {
			set.add(role.getName());
		}
		return set;
	}
	public String getValidation() {
		User user = getManager().get(UserUtil.getPrincipal(getRequest()).getId());// 得到当前登录用户
		Assert.assertNotNull("当前登录用户为空", user);
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from MerchantValidations u where u.merchantUser.id=?" );
		args.add(getModel().getId());
	  //根据地区查询
/*    if(null == regionId || 0 == regionId){
    	if(null != user.getRegion()){
    		hql.append(" and u.region.code like ?");
    	  args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
    	  regionNameCun = user.getRegion().getName();
    	}
    }else{
    	Region region = regionManager.get(Integer.valueOf(regionId));
    	hql.append(" and u.region.code like ?");
    	args.add(MatchMode.START.toMatchString(region.getCode()));
    	regionNameCun = region.getName();
    }
    System.out.println("regionNameCun = "+regionNameCun);
    //用户状态
    if (StringUtils.isNotBlank(getModel().getStatus())) {
			hql.append(" and u.status like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getStatus()));
		}
		
		if (StringUtils.isNotBlank(getModel().getName())) {
			hql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}*/
		hql.append(" order by u.createTime desc");
		System.out.println("hql = "+hql);
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return "validateInfo";
	}

	/**
	 * 加载用户的角色，处理延迟加载问题
	 */
	private Collection<Role> loadRoles(User user) {
		Set<Role> roles = new HashSet();
		List<Role> roleList = roleManager.query(
				"select r from Role r join r.users u " + "where u.id=?", user.getId());
		for (Role role : roleList) {
			roles.add(role);
		}

		return roles;
	}
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public String getRegionNameCun() {
		return regionNameCun;
	}
	public void setRegionNameCun(String regionNameCun) {
		this.regionNameCun = regionNameCun;
	}
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	public RoleManager getRoleManager() {
		return roleManager;
	}
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}
	public CustomerManager getCustomerManager() {
		return customerManager;
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
	public RegionManager getRegionManager() {
		return regionManager;
	}
	public void setRegionManager(RegionManager regionManager) {
		this.regionManager = regionManager;
	}
	public File getAttch() {
		return attch;
	}
	public void setAttch(File attch) {
		this.attch = attch;
	}
	public String getAttchFileName() {
		return attchFileName;
	}
	public void setAttchFileName(String attchFileName) {
		this.attchFileName = attchFileName;
	}
	public String getAttchFolder() {
		return attchFolder;
	}
	public void setAttchFolder(String attchFolder) {
		this.attchFolder = attchFolder;
	}
	public String getIfRcommend() {
		return ifRcommend;
	}
	public void setIfRcommend(String ifRcommend) {
		this.ifRcommend = ifRcommend;
	}
	public String getProSortName() {
		return proSortName;
	}
	public void setProSortName(String proSortName) {
		this.proSortName = proSortName;
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
	public ProductSortManager getProductSortManager() {
		return productSortManager;
	}
	public void setProductSortManager(ProductSortManager productSortManager) {
		this.productSortManager = productSortManager;
	}
	public List<User> getList() {
		return list;
	}
	public void setList(List<User> list) {
		this.list = list;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStrs() {
		return strs;
	}

	public void setStrs(String strs) {
		this.strs = strs;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public InfoMationRecordManager getInfoMationRecordManager() {
		return infoMationRecordManager;
	}

	public void setInfoMationRecordManager(InfoMationRecordManager infoMationRecordManager) {
		this.infoMationRecordManager = infoMationRecordManager;
	}

	public YhShenqingManager getYhShenqingManager() {
		return yhShenqingManager;
	}

	public void setYhShenqingManager(YhShenqingManager yhShenqingManager) {
		this.yhShenqingManager = yhShenqingManager;
	}

	public MerchantValidationManager getMerchantValidationManager() {
		return merchantValidationManager;
	}

	public void setMerchantValidationManager(MerchantValidationManager merchantValidationManager) {
		this.merchantValidationManager = merchantValidationManager;
	}


}
