package com.systop.amol.base.express.webapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.express.model.ExpressCompany;
import com.systop.amol.base.express.service.ExpressCompanyManager;
import com.systop.amol.sales.service.SalesOrderManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 快递公司Action
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ExpressCompanyAction extends DefaultCrudAction<ExpressCompany, ExpressCompanyManager> {
	
	@Resource
	private SalesOrderManager salesOrderManager;
	
	/**
	 * 查看当前用户的快递公司信息
	 */
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		String sql = "from ExpressCompany o where o.user.id=? ";
		List args = new ArrayList();
		args.add(user.getId());
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}
	
	/**
	 * 供订单选择快递公司
	 */
	public String showIndex() {
		User user = UserUtil.getPrincipal(getRequest());
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		String sql = "from ExpressCompany o where o.user.id=? ";
		List args = new ArrayList();
		args.add(user.getId());
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}

	/**
	 * 保存计量单位
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
			getModel().setCreateTime(new Date());
			System.out.println(getModel().getId()+"  "+getModel().getName()+"  "+getModel().getCreateTime()+"   "+getModel().getUser().getName());
			getManager().save(getModel());
			return SUCCESS;
		} catch (Exception e) {
			addActionError(e.getMessage());
			return INPUT;
		}
	}
	
	@Override
	public String remove() {
		if(null == salesOrderManager.findObject("from Sales o where o.expressCompany.id = ?", getModel().getId())){
			super.remove();
		}else{
			addActionMessage("此快递公司已经使用 ，不能删除！");
			index();
		}
		return SUCCESS;
	}

}
