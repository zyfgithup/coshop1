package com.systop.amol.user.agent.webapp;

import com.systop.amol.base.customer.model.Customer;
import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.service.SalesManager;
import com.systop.amol.user.AmolUserConstants;
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
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import junit.framework.Assert;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
 * app用户管理Action
 * 
 */
@SuppressWarnings({ "serial", "unused", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AppUserAction extends DefaultCrudAction<User, UserManager> {
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
	private SalesManager salesManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private RegionManager regionManager;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private Map<String, Object> checkResult;
	
	/** 地区id */
	private Integer regionId;
	
	private Integer userId;
	private List<User> list;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/** 地区名称 */
	private String regionNameAppUser;
	
	private String inputJf;
	//得到未申请开店的app用户列表
	public String getAppList(){
		User user = UserUtil.getPrincipal(getRequest());// 得到当前登录用户
		Assert.assertNotNull("当前登录用户为空", user);
		list = userManager.getFxsAppUser("app_user");
		return "userJst";
	}
	public String getUserByPhone(){
		String phone=getRequest().getParameter("phone");
		User user=userManager.getUserPhone(phone);
		boolean flag=false;
		if(null!=user){
			flag=true;
		}else{flag=false;}
		checkResult = new HashMap<String,Object>();
		checkResult.put("exist",
				flag);
		return "jsonRst";
	}
	public String showReceiveAddress(){
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from ReceiveAddress u where u.visible='1' and u.user.id = ? ");
		args.add(getModel().getId());
		hql.append(" order by u.createTime desc");
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return "showReceiveAddress";
	}
	public String partnerLink(){
		List<Object> args = new ArrayList<Object>();
		String userId=getRequest().getParameter("appId");
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select login_id,name,case when superior_id="+userId+" ");
		sbsql.append("  then '直接合伙人' else '间接合伙人' end as hhrType,");
		sbsql.append(" case when superior_id="+userId+" then zj_money else jj_money end  as hhrsy");
		sbsql.append(" from(select * from  users  where superior_id="+userId+"");
		sbsql.append(" union all select * from users where superior_id in ");
		sbsql.append(" ( select  id  from  users  where superior_id="+userId+"))aa");
	    
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sbsql.toString());
		Object cont=jdbcTemplate.queryForLong("select count(*) as nums from ("+sbsql.toString()+")tt");
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		page.setData(list);
		page.setRows(Integer.parseInt(cont.toString()));
		restorePageData(page);
		return "partnerLink";
	}
	public String turnUnderUsers(){
		setModel(getManager().get(getModel().getId()));
		return "turnUnderUsers";
	}
	public String updateUnderUsers(){
		System.out.println("1======"+getModel().getId()+"trunto:"+userId);
		jdbcTemplate.execute("update users u set u.superior_id="+userId+",u.zj_money=0 where u.superior_id="+getModel().getId());
		return SUCCESS;
	}
	public String getCheckJyc() throws  Exception{
		String salesId = getRequest().getParameter("salesId");
		getRequest().setAttribute("id",salesId);
		System.out.println("-----------------salesId="+salesId);
		String loginId = getRequest().getParameter("loginId");
		Sales sales =  null;
		System.out.println("---------------sales="+sales);
		if(StringUtils.isNotBlank(salesId)){
			sales = salesManager.get(Integer.parseInt(salesId));
		}
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		List args = new ArrayList();
		StringBuffer sql = new StringBuffer("from User c where c.productSort.id=41418754 and c.jycstate='1' ");
		if(null!=sales){
			sql.append(" and INSTR(c.merSortNameStr,?)>0 ");
			args.add(sales.getRemark());
		}
		if(StringUtils.isNotBlank(loginId)){
			sql.append(" and (c.phone like ? or c.loginId like ?) ");
			args.add(MatchMode.ANYWHERE.toMatchString(loginId));
			args.add(MatchMode.ANYWHERE.toMatchString(loginId));
		}
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		List<User> uList = page.getData();
		if(null!=uList && uList.size()>0){
			for(User user : uList){
				double distance = GetDistance(sales.getLocY(),sales.getLocX(),Double.valueOf(user.getLocY()),Double.valueOf(user.getLocX()));
				user.setShowDistance(distance);
			}
		}
		restorePageData(page);
		return "getCheckJyc";
	}
	public static  double EARTH_RADIUS = 6378.137;

	public static double rad(double d)
	{
		return d * Math.PI / 180.0;
	}

	public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
	{
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.asin(a/2),2) +
				Math.acos(radLat1)*Math.acos(radLat2)*Math.pow(Math.asin(b/2),2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	public String getCheckUsers() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from User c where c.type='jyc' and c.jycState='1'");
		List args = new ArrayList();
		List<Map<String,Object>> list=jdbcTemplate.queryForList("select id from users where FIND_IN_SET(id, queryChildrenUserInfo("+userId+"))");
		StringBuffer ids=new StringBuffer();
		for(Map<String,Object> map:list){
			ids.append(map.get("id")+",");
		}
		String cheks="";
		if(ids.length()>0){
			cheks=ids.toString().substring(0,ids.length()-1);
		}
		sql.append(" and c.id not in("+cheks+")");
		if(regionId!=null){
			sql.append(" and c.loginId like '%"+regionId+"%' ");
		}
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return "getCheckUsers";
	}
	
	@Override
	@Transactional
	public String save() {
		User user=null;
		if(null!=getModel().getId()&&0!=getModel().getId()){
			user=userManager.get(getModel().getId());
			Region region=regionManager.get(getModel().getRegion().getId());
			user.setRegion(region);
			getManager().getDao().clear();
			userManager.update(user);
		}
		return SUCCESS;
	}
	/**
	 * 经销商查询列表，县分销商
	 */
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());// 得到当前登录用户
		Assert.assertNotNull("当前登录用户为空", user);
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from User u where u.type = ?  ");
		args.add(AmolUserConstants.APP_USER);
		user = getManager().get(user.getId());
		//admin
		/*ROLE_SYSTEM
		ROLE_ADMIN
		ROLE_TOP_DEALER_GENERAL
		ROLE_TOP_DEALER
		ROLE_SUPPLIER
		ROLE_BANK*/
		
		//村点
		/*ROLE_END_DEALER_GENERAL
		ROLE_END_DEALER*/
		
		//县
		/*ROLE_END_DEALER_GENERAL
		ROLE_END_DEALER*/
		
		user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		System.out.println(user.getName()+"=========="+user.getRoles().size());
		Set<Role> set = user.getRoles();
		Iterator<Role> it = set.iterator();
		while(it.hasNext()){
			System.out.println(it.next().getName());
		}
	  //根据地区查询
    if(null != regionId && 0 != regionId){
    	Region region = regionManager.get(regionId);
    	regionNameAppUser = region.getName();
    	hql.append("  and u.region.code like ?");
    	args.add(MatchMode.START.toMatchString(region.getCode()));
    }else{
    	if(null != user.getType() && null != user.getRegion()){
    		hql.append("  and u.region.code like ?");
    		System.out.println("------------user.getRegion().getCode()"+user.getRegion().getCode());
      	args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
      	regionNameAppUser = user.getRegion().getName();
    	}
    }
    System.out.println(regionNameAppUser+", "+regionId);
		if (StringUtils.isNotBlank(getModel().getName())) {
			hql.append(" and u.loginId like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		hql.append(" order by u.integral desc");
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}
	/**
	 * 重置密码
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

	/**
	 * 启用分销商
	 * 
	 * @return
	 */
	public String unsealUser() {
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
			}
		}
		return SUCCESS;
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
	@Override
	public String edit() {
		String appId = getRequest().getParameter("appId");
		if (StringUtils.isNotBlank(appId)) {
//			MessageBusiness messageBusiness = AccessConnection.getPushMessageBusiness(new Integer(pushMessageId));
//			setModel(messageBusiness.getPushMessage());
//			getRequest().setAttribute("business", messageBusiness.getBusiness());
			User user= getManager().get(new Integer(appId));
			setModel(user);
			System.out.println("edit = "+user);
		}
		return "jfapp";
	}
	public String appEdit() {
		getRequest().setAttribute("loginUser", userManager.get(UserUtil.getPrincipal(getRequest()).getId()));
		return "appEdit";
	}
	
	//兑换商品后更新用户积分
	public String updateJf() {
		//获得app用户对象
		User user=userManager.get(getModel().getId());
		user.setIntegral(user.getIntegral()-Integer.parseInt(inputJf));
		Region region=user.getRegion();
		System.out.println(user.getRegion().getCode());
		String code=user.getRegion().getCode();
		//获得村级分销用户对象
		User cjfxUser=userManager.findUser(code);
		if(null==cjfxUser.getDhIntegral()){
			cjfxUser.setDhIntegral(Integer.parseInt(inputJf));
		}else{
			cjfxUser.setDhIntegral(cjfxUser.getDhIntegral()+Integer.parseInt(inputJf));
		}
		userManager.update(user);
		userManager.update(cjfxUser);
		return "success";
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

	public String getInputJf() {
		return inputJf;
	}

	public void setInputJf(String inputJf) {
		this.inputJf = inputJf;
	}

	public String getRegionNameAppUser() {
		return regionNameAppUser;
	}

	public void setRegionNameAppUser(String regionNameAppUser) {
		this.regionNameAppUser = regionNameAppUser;
	}
	public Map<String, Object> getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(Map<String, Object> checkResult) {
		this.checkResult = checkResult;
	}

	public List<User> getList() {
		return list;
	}

	public void setList(List<User> list) {
		this.list = list;
	}
}
