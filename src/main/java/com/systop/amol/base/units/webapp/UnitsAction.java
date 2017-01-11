package com.systop.amol.base.units.webapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.units.model.Units;
import com.systop.amol.base.units.service.UnitsManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 计量单位管理Manager
 * @author WangHaiYan
 *
 */
@SuppressWarnings({"serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UnitsAction extends DefaultCrudAction<Units, UnitsManager> {
	/**
	 * 查看当前用户的单位信息
	 */
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		String sql = "from Units u where u.user=? ";
		List args = new ArrayList();
	  args.add(user);
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
  		getManager().save(getModel());
      return SUCCESS;
    } catch (Exception e) {
      addActionError(e.getMessage());
      return INPUT;
    }
	}

}
