package com.systop.common.modules.security.user.webapp;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.UserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserCodeManager;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;
import com.systop.core.Constants;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.ExtJsCrudAction;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User对象的struts2 action。
 * 
 * @author Sam Lee,Nice
 */
@SuppressWarnings({ "serial", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserAction extends ExtJsCrudAction<User, UserManager> {

	/** 修改密码的时候对应输入的旧密码 */
	private String oldPassword;

	/**
	 * 标识是否是用户自行修改个人信息,如果为[null]则表示 由管理员修改.否则,表示由用户自己修改
	 */
	private String selfEdit;

	/** 检测用户返回的结果 */
	private Map<String, Object> checkResult;

	/** 保存用户树形列表 */
	private List<Map> userTree;

	/** 上级部门ID，用于列出树形列表 */
	private Integer parentDeptId;

	/** 角色名称，用于员工选择器 */
	private String roleName;

//	@Autowired
//	private DeptAction deptAction;

	/** 以下是登录历史记录查询所需定义的 */
	private String loginUsername;

	private String startDate;

	private String endDate;
	
	@Autowired
	private UserCodeManager userCodeManager;
	@Autowired
	RegionManager regionManager;
	@Autowired
	ProductSortManager productSortManager;

	/**
	 * 重置用户编号
	 * @return
	 */
	public String resetAllCode(){
		userCodeManager.updateAllSerialNo();
		return null;
	}
	
	public String index() {
		loadModel();
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		// 存放查询参数
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from User u where u.isSys is null");
//		hql.append(" and u.superior is null  and u.status = ?");
		hql.append(" and u.status = ?");
		args.add(getModel().getStatus());
		if (StringUtils.isNotBlank(getModel().getName())) {// 根据名称查询
			hql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (StringUtils.isNotBlank(getModel().getLoginId())) {// 根据LoginId查询
			hql.append(" and u.loginId like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getLoginId()));
		}
		if (StringUtils.isNotBlank(getModel().getType())) {// 根据类型查询
			hql.append(" and u.type = ?");
			args.add(getModel().getType());
			System.out.println("getModel().getType() = "+getModel().getType());
			if(AmolUserConstants.AGENT.equals(getModel().getType())){
				hql.append(" and u.fxsjb = ?");
				args.add(AmolUserConstants.AGENT_LEVEL_COUNTY);
			}
		}else{
			hql.append(" and u.type != ? and u.type !=? and (u.fxsjb != ? or u.fxsjb is null)");
			args.add(AmolUserConstants.EMPLOYEE);
			args.add(AmolUserConstants.APP_USER);
			args.add(AmolUserConstants.AGENT_LEVEL_VILLAGE);
		}
		
		System.out.println("getModel().getType() = "+getModel().getType()+" AGENT_LEVEL_VILLAGE = "+AmolUserConstants.AGENT_LEVEL_VILLAGE);
		
		hql.append(" order by u.createTime desc");
		
		System.out.println("hql = "+hql);
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}

	/** 对model进行 判断，进行非空处理 */
	private void loadModel() {
		if (getModel() == null) {
			setModel(new User());
		}
		if (StringUtils.isBlank(getModel().getStatus())) {// 默认查询正常用户[已启用]
			getModel().setStatus(UserConstants.USER_STATUS_USABLE);
		}
	}

	@Validations(requiredStrings = {
			@RequiredStringValidator(fieldName = "model.loginId", message = "登录名是必须的."),
			@RequiredStringValidator(fieldName = "model.password", message = "密码是必须的."), }, stringLengthFields = { @StringLengthFieldValidator(fieldName = "password", maxLength = "32", message = "密码应少于32字符", trim = true) }, emails = { @EmailValidator(fieldName = "model.email", message = "请输入正确的e-Mail.") })
	@Override
	public String save() {
		Assert.notNull(getModel());
		Region region = getModel().getRegion();
		if (region != null ) {// 如果地区为空则不关联
			if (region.getId() == null || region.getId() == 0){
				getModel().setRegion(null);	
			}		
		}
		if (getModel().getBeginningInit() == null ||getModel().getBeginningInit().equals("")  ) {
			getModel().setBeginningInit(Constants.NO);
		}
		getManager().getDao().getHibernateTemplate().clear();
		
		getModel().setSuperior(UserUtil.getPrincipal(getRequest()));
		if(AmolUserConstants.AGENT.equals(getModel().getType())){
			getModel().setFxsjb(AmolUserConstants.AGENT_LEVEL_COUNTY);
		}
		System.out.println("ddddddddddddddddddddiiii");
		
		String result = super.save();
		if (SUCCESS.equals(result)) {
			result = "user-success";
		}
		if (isSelfEdit()) {
			addActionMessage("用户信息已经成功修改。");
			return INPUT;
		} else {
			return result;
		}

	}

	/**
	 * 编辑用户
	 */
	@SkipValidation
	@Override
	public String editNew() {
		getModel().setSex(UserConstants.GENT);
		return INPUT;
	}

	public String edit() {
		getRequest().setAttribute("loginUser", getManager().get(UserUtil.getPrincipal(getRequest()).getId()));
		return INPUT;
	}

	/**
	 * 修改密码
	 */
	public String changePassword() {
		if (StringUtils.isBlank(oldPassword) || getModel() == null
				|| getModel().getId() == null) {
			return "changePassword";
		}
		String pwdToShow = getModel().getPassword();
		try {
			getManager().changePassword(getModel(), oldPassword);
			addActionMessage("修改密码成功，新密码是：" + pwdToShow);
		} catch (ApplicationException e) {
			addActionError(e.getMessage());
		}
		return "changePassword";
	}
	
	/**
	 * 修改PDA用户自身密码
	 */
	public String changePasswordPda() {
		if (StringUtils.isBlank(oldPassword) || getModel() == null
				|| getModel().getId() == null) {
			return "changePasswordPda";
		}
		String pwdToShow = getModel().getPassword();
		try {
			getManager().changePassword(getModel(), oldPassword);
			addActionMessage("修改密码成功，新密码是：" + pwdToShow);
		} catch (ApplicationException e) {
			addActionError(e.getMessage());
		}
		return "changePasswordPda";
	}

	/**
	 * 删除用户、禁用用户
	 */
	public String remove() {
		super.remove();
		return "user-success";
	}

	/**
	 * 启用用户
	 * 
	 * @return
	 */
	public String unsealUser() {
		if (ArrayUtils.isEmpty(selectedItems)) {
			if (getModel() != null) {
				Serializable id = extractId(getModel());
				if (id != null) {
					selectedItems = new Serializable[] { id };
				}
			}
		}
		if (selectedItems != null) {
			for (Serializable id : selectedItems) {
				if (id != null) {
					User user = getManager().get(convertId(id));
					if (user != null) {
						getManager().unsealUser(user);
					} else {
						logger.debug("用户信息不存在.");
					}
				}
			}
			logger.debug("{} items usable user.", selectedItems.length);
		}
		return "user-success";
	}

	public String showSelf() {
		if (getModel().getId() != null) {
			setModel(getManager().get(getModel().getId()));
		}
		return "bingo";
	}

	/**
	 * 前台注册用户修改个人信息
	 * 
	 * @return
	 */
	public String editInfo() {
		User user = UserUtil.getPrincipal(getRequest());
		getModel().setLoginId(user.getLoginId());
		getModel().setPassword(user.getPassword());
		/*Region region = getManager().getDao().get(Region.class,getModel().getRegion().getId());
		if (region != null ) {// 如果地区为空则不关联
			getManager().getDao().getHibernateTemplate().clear();
			if (region.getId() == null || region.getId() == 0){
				getModel().setRegion(null);	
			}else{
				getModel().setRegion(region);
			}
		}*/
		getManager().update(getModel());
		addActionMessage("个人信息修改成功!");
		return "bingo";
	}

	/**
	 * Build a tree as JSON format.
	 */
	@SkipValidation
	@Deprecated
	public String userTree() {
//		if (RequestUtil.isJsonRequest(getRequest())) {
//			Dept parent = null;
//			if (parentDeptId != null) {
//				parent = getManager().getDao().get(Dept.class, parentDeptId);
//			} else {
//				parent = (Dept) getManager().getDao().findObject(
//						"from Dept d where d.parentDept is null");
//			}
//			Map<String, Object> parentMap = null;
//			if (parent != null) {
//				parentMap = new HashMap<String, Object>();
//				parentMap.put("id", parent.getId());
//				parentMap.put("text", parent.getName());
//				parentMap.put("type", parent.getType());
//			}
//			Map deptTree = deptAction.getDeptTree(parentMap, true);
//			Map tree = getManager().getUserTree(deptTree, roleName);
//			if (!tree.isEmpty()) {
//				userTree = new ArrayList<Map>();
//				userTree.add(tree);
//			}
//			return "tree";
//		}
		return INDEX;
	}

	/**
	 * 用户登录历史记录查询
	 */
	public String userHistoryList() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		// 存放查询参数
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(
				"from UserLoginHistory ulh where 1 =1 ");
		hql.append(" and ulh.user.loginId != ?");
		args.add("admin");
		if (StringUtils.isNotBlank(loginUsername)) {// 根据名称查询
			hql.append(" and ulh.user.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(loginUsername));
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				hql.append(" and ulh.loginTime >= ?");
				args.add(DateUtil.convertStringToDate("yyyy-MM-dd HH:mm",
						startDate));
			}
			if (StringUtils.isNotBlank(endDate)) {
				hql.append(" and ulh.loginTime <= ?");
				args.add(DateUtil.convertStringToDate("yyyy-MM-dd HH:mm",
						endDate));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		hql.append(" order by ulh.loginTime desc");
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return "userHistoryList";
	}

	/**
	 * AJAX请求，检测登录名是否已存在
	 */
	public String checkName() {
		checkResult = new HashMap<String, Object>();
		checkResult.put("exist",
				getManager().getDao().exists(getModel(), "loginId"));
		return "jsonRst";
	}
	/**
	 * AJAX请求，检测积分商品类别是否为二级类别  
	 */
	public String checkProductsJb() {
		String prosortId=getRequest().getParameter("prosortId");
		ProductSort productSort=productSortManager.get(Integer.parseInt(prosortId));
		checkResult = new HashMap<String, Object>();
		if(null!=productSort.getParentProsort()&&null==productSort.getParentProsort().getParentProsort()){
		checkResult.put("exist",false);
		}else{
			checkResult.put("exist",true);
			checkResult.put("productSort",productSort.getName());
		}
		return "jsonRst";
	}
	/**
	 * AJAX请求，检测区域负责人已存在  findUserByRegion
	 */
	public String checkRegionUser() {
		String regionId=getRequest().getParameter("regionId");
		Region region=regionManager.get(Integer.parseInt(regionId));
		User user=getManager().findUserByRegion(region.getId());
		checkResult = new HashMap<String, Object>();
		if(null!=user){
		checkResult.put("exist",true);
		checkResult.put("regionName",region.getName());
		}else{
			checkResult.put("exist",false);
		}
		return "jsonRst";
	}
	/**
	 * AJAX请求，检测身份证是否已存在
	 */
	public String checkIdCard() {
		checkResult = new HashMap<String, Object>();
		checkResult.put("exist",
				getManager().getDao().exists(getModel(), "idCard"));
		return "jsonRst";
	}

	/**
	 * AJAX请求，检测用户邮箱是否已存在
	 */
	public String checkEmail() {
		checkResult = new HashMap<String, Object>();
		checkResult.put("exist",
				getManager().getDao().exists(getModel(), "email"));
		return "jsonRst";
	}

	/**
	 * 取得用户信息
	 */
	public String userInfo() { 
	User user = getManager().get(UserUtil.getPrincipal(getRequest()).getId()); 
	getRequest().setAttribute("user", user);
	if (StringUtils.isNotBlank(user.getType())) {
		if (user.getType().equals("system")) {
			getRequest().setAttribute("userType", "系统用户");	
		}
//		if (user.getType().equals("bank")) {
//			getRequest().setAttribute("userType", "银行用户");	
//		}
//		if (user.getType().equals("company")) {
//			getRequest().setAttribute("userType", "农资生产商");	
//		}	
		if (user.getType().equals("agent")) {
			getRequest().setAttribute("userType", "区域管理员");
		}
	} else {
		getRequest().setAttribute("userType", "超级管理员");	
	}

	return "userInfo"; 
	}
	
	/**
	 * 选择厂商
	 * 
	 * @return
	 */
	public String selectCompany() {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(
				"from User u where u.type = ? and u.status = ? ");
		args.add(AmolUserConstants.COMPANY);
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
	
	/**
	 * 返回性别Map
	 */
	public Map<String, String> getSexMap() {
		return UserConstants.SEX_MAP;
	}

	/**
	 * 是否用户自己修改信息
	 */
	private boolean isSelfEdit() {
		return StringUtils.isNotBlank(selfEdit);
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getSelfEdit() {
		return selfEdit;
	}

	public void setSelfEdit(String selfEdit) {
		this.selfEdit = selfEdit;
	}

	public List<Map> getUserTree() {
		return userTree;
	}

	public void setUserTree(List<Map> userTree) {
		this.userTree = userTree;
	}

	public Integer getParentDeptId() {
		return parentDeptId;
	}

	public void setParentDeptId(Integer parentDeptId) {
		this.parentDeptId = parentDeptId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Map<String, Object> getCheckResult() {
		return checkResult;
	}

	public String getLoginUsername() {
		return loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
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

}
