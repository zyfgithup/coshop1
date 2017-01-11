package com.systop.amol.base.supplier.webapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.supplier.model.Supplier;
import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.LoginUserService;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.Constants;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.JsonCrudAction;

/**
 * 供应商管理Action
 * @author 王会璞
 *
 */
@SuppressWarnings({"serial","rawtypes","unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SupplierAction extends JsonCrudAction<Supplier, SupplierManager> {

	/** 地区Manager */
	@Resource private RegionManager regionManager;
	
	/**
	 * 用户管理
	 */
	@Autowired
	private UserManager userManager;
	
	/**
	 * Json数据
	 */
	private Map<String, String> jsonResult;
	
	/**
	 * 当前登录用户
	 */
	@Autowired
	private LoginUserService loginUserService;

  /**
   * 查询供应商信息
   */
	@Override
	public String index() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Supplier s where 1=1 ");
		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		
user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
System.out.println(user.getName()+"=========="+user.getRoles().size());

Set<Role> set = user.getRoles();
Iterator<Role> it = set.iterator();
while(it.hasNext()){
	System.out.println(it.next().getName());
}
		
		if(user != null){
			sql.append(" and s.user.id = ?");
			args.add(user.getId());
		}
				
		if (StringUtils.isNotBlank(getModel().getName())) {
			sql.append(" and s.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		
		if (StringUtils.isNotBlank(getModel().getPhone())) {
			sql.append(" and s.phone like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getPhone()));
		}
		
		if (StringUtils.isNotBlank(getModel().getEmail())) {
			sql.append(" and s.email = ?");
			args.add(getModel().getEmail());
		}
		
		if (getModel() != null && getModel().getRegion() != null
				&& getModel().getRegion().getId() != null) {
			Region region = getManager().getDao().get(Region.class, getModel().getRegion().getId());
			if (region != null) {
				//System.out.println(region.getCode().substring(0, 2));
				//System.out.println(region.getCode().substring(2, 6));
				//System.out.println(region.getCode().substring(4, 6));
				sql.append(" and s.region.code like ?");
				if (region.getCode().substring(2, 6).equals("0000")) {
					args.add(MatchMode.START.toMatchString(region.getCode().substring(0, 2)));
				}else{ 
					if (region.getCode().substring(4, 6).equals("00")) {
						args.add(MatchMode.START.toMatchString(region.getCode().substring(0, 4)));
					}else{
						args.add(region.getCode());
					}
				}
			}
		  // 为了返回查询中的条件数据需要将Region存入到MODEL中
			getModel().setRegion(region);
		}
		sql.append(" order by s.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}
	//期初应收处的查看
	public String initindex(){
		this.index();
		return "init";
	}
	@Override
	public String save() {
		if (hasActionMessages() || hasActionErrors()) {
			return INPUT;
		}else{
			User u = UserUtil.getPrincipal(getRequest());
			getModel().setUser(u);
			
			if (getModel() != null && getModel().getRegion() != null
					&& getModel().getRegion().getId() != null) {
				getModel().setRegion(regionManager.get(getModel().getRegion().getId()));
			}
		}
	  // 添加新的供应商时需要添加创建时间
		if (getModel() != null) {
			Serializable id = extractId(getModel());
			if (id == null) {
				getModel().setCreateTime(new Date());
			}
		}
		getModel().setStatus(Constants.YES);
		return super.save();
	}
	
	/**
	 * 预览
	 */
	public String view() {
		super.view();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(getModel().getCreateTime() != null){
			String createTime = dateFormat.format(getModel().getCreateTime());
			getRequest().setAttribute("createTime", createTime);
		}
		return VIEW;
	}
	
	/**
	 * 进销存采购管理中使用的供应商选择列表
	 * @return
	 */
	public String showIndex() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Supplier s where s.status = ? ");
		List args = new ArrayList();
		args.add(Constants.YES);
		User user = UserUtil.getPrincipal(getRequest());
		
		if(user != null){
			sql.append(" and s.user.id = ?");
			if(null != user.getType() && user.getType().equals("employee")){
				args.add(user.getSuperior().getId());
			}else{
			args.add(user.getId());
			}
		}
				
		if (StringUtils.isNotBlank(getModel().getName())) {
			sql.append(" and s.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (getModel() != null && getModel().getRegion() != null
				&& getModel().getRegion().getId() != null) {
			Region region = getManager().getDao().get(Region.class, getModel().getRegion().getId());
			if (region != null) {
				sql.append(" and s.region.code like ?");
				if (region.getCode().substring(2, 6).equals("0000")) {
					args.add(MatchMode.START.toMatchString(region.getCode().substring(0, 2)));
				}else{ 
					if (region.getCode().substring(4, 6).equals("00")) {
						args.add(MatchMode.START.toMatchString(region.getCode().substring(0, 4)));
					}else{
						args.add(region.getCode());
					}
				}
			}
		  // 为了返回查询中的条件数据需要将Region存入到MODEL中
			getModel().setRegion(region);
		}
		sql.append(" order by s.createTime desc");
		System.out.println("sql---->"+sql);
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return "showIndex";
	}

	/**
	 * 删除供应商
	 */
	@Override
	public String remove(){
		try{
			getManager().remove(getModel());
			return SUCCESS;		
		}catch(Exception e){
			this.getRequest().setAttribute("error", "删除失败,您与供应商"+getModel().getName()+"已存在交易记录，不允许删除");
			this.setModel(new Supplier());
			return index();
		}
	}
	
	/**
	 * 启用禁用供应商
	 * 
	 * @return
	 */
	public String unsealSupplier() {
		if (getModel() != null) {
			getManager().update(getModel());
		} else {
			logger.debug("供应商信息不存在.");
		}
		return SUCCESS;
	}

	/**
	 * 编辑页面，验证供应商名称
	 * @return
	 */
	public String selectSupplier(){
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		String sid = getRequest().getParameter("sid");
		String sname = getRequest().getParameter("sname");
		User user = loginUserService.getLoginUser(getRequest());//当前登录用户
		if (user == null) {//当前登录用户为空
			jsonResult.put("result", "error");
			return "jsonResult";
		}
		if (StringUtils.isNotBlank(sid) && StringUtils.isNotBlank(sname)) {
			User company = userManager.findObject("from User u where u.type = ? and u.id = ?",
					new Object[]{AmolUserConstants.COMPANY, Integer.valueOf(sid)});
			if (!company.getName().equals(sname)) {
				jsonResult.put("result", "noExist");
			}
		}
		if (StringUtils.isBlank(sid) && StringUtils.isNotBlank(sname)) {
			User company = userManager.findObject("from User u where u.type = ? and u.name = ?",
					new Object[]{AmolUserConstants.COMPANY, sname});
			if (company != null) {
				jsonResult.put("result", "exist");
			}
		}
		return "jsonResult";
	}
	
	public Map<String, String> getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(Map<String, String> jsonResult) {
		this.jsonResult = jsonResult;
	}
	
	
}