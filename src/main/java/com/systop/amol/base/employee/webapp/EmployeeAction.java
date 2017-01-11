package com.systop.amol.base.employee.webapp;

import com.systop.amol.user.AmolUserConstants;
import com.systop.amol.user.agent.service.AgentManager;
import com.systop.common.modules.dept.model.Dept;
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
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings({ "serial" , "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeAction extends DefaultCrudAction<User, AgentManager> {
	
	/**
	 * 用于加密密码
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private RegionManager regionManager;
	/** 地区id */
	private Integer regionId;
	
	/**
	 * 查询列表
	 */
	public String index(){
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from User u where u.type = ? and visible='1' ");
		List args = new ArrayList();
		args.add(AmolUserConstants.ADMIN);
		User user = UserUtil.getPrincipal(getRequest());
		if (StringUtils.isNotBlank(getModel().getStatus())) {
			sql.append(" and u.status = ?");
			args.add(getModel().getStatus());
		}
		System.out.println("-------------------regionId="+regionId);
		if(null != regionId && 0!=regionId){
			Region region = regionManager.get(regionId);
			sql.append(" and u.region.code like ?  ");
			args.add(MatchMode.START.toMatchString(region.getCode()));
		}
		if (StringUtils.isNotBlank(getModel().getName())) {
			sql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if(StringUtils.isNotBlank(getModel().getCode())){
			sql.append(" and u.code like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCode()));
		}
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}
	
	/**
	 * 预览职员信息
	 */
	public String view(){
		super.view();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(getModel().getBirthday() != null){
			String birthday = dateFormat.format(getModel().getBirthday());
			getRequest().setAttribute("birthday", birthday);
		}
		if(getModel().getCreateTime() != null){
			String joinTime =  dateFormat.format(getModel().getCreateTime());			
			getRequest().setAttribute("joinTime", joinTime);
		}
		
		return "view";
	}
	
	/**
	 * 职员选择选择列表
	 * @return
	 */
	public String showIndex() {

		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			Page page = PageUtil.getPage(getPageNo(), getPageSize());
			StringBuffer sql = new StringBuffer("from User u where u.superior.id = ? and u.status=? and u.type=?");
			List args = new ArrayList();
			if(user.getSuperior() != null){
				args.add(user.getSuperior().getId());
			}else{
				args.add(user.getId());
			}
			args.add(Constants.YES);
			args.add(AmolUserConstants.EMPLOYEE);
			if (StringUtils.isNotBlank(getModel().getName())) {
				sql.append(" and u.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
			}
		
			// 判断是否是顶级部门，如果不是顶级部门则加查询条件，否则查询全部
			if (getModel() != null && getModel().getDept() != null
					&& getModel().getDept().getId() != null) {
				Dept dept = getManager().getDao().get(
						Dept.class, getModel().getDept().getId());
				if (dept != null) {
					sql.append("and u.dept.serialNo like ? ");
					args.add(MatchMode.START.toMatchString(dept.getSerialNo()));
				}
	
				// 为了返回查询中的条件数据需要将EmpDept存入到MODEL中
				getModel().setDept(dept);
			}
			
			page = getManager().pageQuery(page, sql.toString(), args.toArray());
			restorePageData(page);
		}
		return "showIndex";
	}
	/**
	 * 返回性别Map
	 */
	public Map<String, String> getSexMap() {
		return UserConstants.SEX_MAP;
	}
	
	/**
	 * 保存员工
	 */
	@Override
	@Transactional
	public String save() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setSuperior(user);
			getModel().setType(AmolUserConstants.ADMIN);
			getModel().setVisible("1");
			getManager().getDao().getHibernateTemplate().clear();
			if(this.getManager().getDao().exists(this.getModel(),"code","superior.id","type")){
				this.addActionMessage("编号为【" + getModel().getCode() + "】的职员已存在。");
				return INPUT;
			}
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		if (getModel().getId() == null) {
			getModel().setBeginningInit(getModel().getSuperior().getBeginningInit());
			getModel().setCreateTime(new Date());// 设置注册时间
			getModel().setVisible("1");
//			getModel().setType(AmolUserConstants.EMPLOYEE);
			getModel().setStatus(UserConstants.USER_STATUS_USABLE);
		}
		getManager().getDao().getHibernateTemplate().clear();
		System.out.println("------------------getmodel().getId = "+getModel().getId());
		if(null==getModel().getId()) {
			Role role = roleManager.get(1);
			role.getUsers().add(getModel());
			getModel().getRoles().add(role);
			getManager().save(getModel());
		}else{
			getManager().update(getModel());
		}
		return SUCCESS;
	}
	
	/**
	 * 编辑
	 */
	public String edit(){
		getRequest().setAttribute("loginUser",UserUtil.getPrincipal(getRequest()));
		getModel().setCreateTime(DateUtil.getCurrentDate());
		return INPUT;
	}
	public Map getRList() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			return roleManager.getRoles();
		}
		return null;
	}
	/**
	 * 禁用用户
	 */
	public String remove() {
		if (getModel().getId() != null) {			
			getManager().remove(getModel());
		}
		return SUCCESS;
	}
public String removeUser(){
	if(getModel().getId()!=null){
		User user = getManager().get(getModel().getId());
		user.setVisible("0");
		getManager().update(user);
	}
	return SUCCESS;
}
	/**
	 * 启用用户
	 * 
	 * @return
	 */
	public String unsealUser() {
		if (getModel().getId() != null) {
			getManager().unsealUser(getModel());
		}
		return SUCCESS;
	}
	
	/**
	 * 重置密码
	 * @return
	 */
	public String restPassword(){
		getModel().setPassword(passwordEncoder.encodePassword(getModel().getPassword(),null));
		getManager().update(getModel());
		return "restPassword";
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
}
