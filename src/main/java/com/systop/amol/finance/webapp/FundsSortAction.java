package com.systop.amol.finance.webapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.finance.model.FundsSort;
import com.systop.amol.finance.service.FundsSortManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 资金类型管理Action
 * @author Administrator
 *
 */
@SuppressWarnings({"serial","rawtypes", "unchecked"})
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FundsSortAction extends DefaultCrudAction<FundsSort,FundsSortManager> {
	
	/**
	 * 保存方法
	 */
	@Override
	public String save() {
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		if (user != null) {
			getModel().setUser(user);
			// 判断是否有重复名称
			if (getManager().getDao().exists(getModel(), "user","name")) {
				addActionError("资金类型名称已存在！");
				return INPUT;
			}
		}else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}		
		return super.save();
	}
	
	/***
	 * index查询
	 */
	public String index(){
		
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from FundsSort fs where 1=1 ");
		List args = new ArrayList();
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		if (user != null) {
			getModel().setUser(user);
			if (StringUtils.isNotBlank(user.getBeginningInit())) {
				if (user.getBeginningInit().equals("0")) {
					this.addActionError("请先完成初始化,再作单据！ ");
					return INDEX;
				}else {
					if(user.getSuperior() != null){
						sql.append(" and fs.user.id = ? ");
						args.add(user.getSuperior().getId());
					}else{
						sql.append(" and fs.user.id = ?");
						args.add(user.getId());
					}				
				}
			}			
		}
		
		sql.append("order by fs.id desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}
	
}
