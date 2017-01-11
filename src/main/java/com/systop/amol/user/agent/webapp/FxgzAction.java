package com.systop.amol.user.agent.webapp;

import com.systop.amol.user.agent.model.Fxgz;
import com.systop.amol.user.agent.service.FxgzManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import junit.framework.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({ "serial", "unused", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FxgzAction extends DefaultCrudAction<Fxgz,FxgzManager>{
	
	@Autowired
	private UserManager userManager;
	private String fJfNum;
	private String errorMsg;
	@Transactional
	public String save() {
		User user = UserUtil.getPrincipal(getRequest());
	    user=userManager.get(user.getId());
	    getModel().setUser(user);
	    getModel().setCreateTime(new Date());
		Fxgz fxgz = getManager().getByType(getModel().getType());
		if(null==fxgz) {
			getManager().save(getModel());
			return SUCCESS;
		}else{
			if(getModel().getType().equals("0")) {
				errorMsg = "充值规则已经设定，如需添加请先删除";
			}else{
				errorMsg = "分拥规则已经设定，如需添加请先删除";
			}
			return INPUT;
		}
	}
	public String delete() {
		getManager().remove(getManager().get(getModel().getId()));
		return SUCCESS;
	}
	public String index() {
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());// 得到当前登录用户
		Assert.assertNotNull("当前登录用户为空", user);
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from Fxgz u where 1=1  ");
		hql.append(" order by u.createTime desc");
		System.out.println("hql = "+hql);
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}
	public String getfJfNum() {
		return fJfNum;
	}
	public void setfJfNum(String fJfNum) {
		this.fJfNum = fJfNum;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
