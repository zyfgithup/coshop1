package com.systop.amol.purchase.webapp;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.purchase.model.PayAble;
import com.systop.amol.purchase.service.PayAbleManager;
import com.systop.amol.util.NumberFormatUtil;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 及时应付管理Manager
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayAbleAction extends DefaultCrudAction<PayAble, PayAbleManager> {

	/**
	 * 查询列表
	 */
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql= new StringBuffer("from PayAble pa where pa.user.id=?");
		List args = new ArrayList();
		if(user.getType().equals("employee")){		 
			args.add(user.getSuperior().getId());
		}else{	
			args.add(user.getId());
		}
		if (getModel() != null && getModel().getSupplier() != null
				&& getModel().getSupplier().getName() != null
				&& !getModel().getSupplier().getName().trim().equals("")) {
			sql.append(" and pa.supplier.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSupplier().getName()));			
		}

		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		
		String nsql="select sum(amount) "+sql.toString();
		List list=getManager().query(nsql, args.toArray());
		String Total="";
		if(list.size()>0){
			for (Object o : list) {
				if(o != null){
				 Total=NumberFormatUtil.format(o,2);
				}
			}
		}
		getRequest().setAttribute("Total", Total);
		
		return INDEX;
	}
	
	

}
