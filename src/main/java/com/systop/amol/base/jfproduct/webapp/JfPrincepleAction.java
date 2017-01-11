package com.systop.amol.base.jfproduct.webapp;

import com.systop.amol.base.jfproduct.model.JfPrinceple;
import com.systop.amol.base.jfproduct.service.JfPrincepleManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JfPrincepleAction
		extends DefaultCrudAction<JfPrinceple, JfPrincepleManager> {
	@Autowired
	private UserManager userManager;

	public String index() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(" from JfPrinceple c   ");
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		List args = new ArrayList();
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}
	public String save() {
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		getModel().setUser(user);
		getManager().save(getModel());
		return SUCCESS;
	}
	@Override
	public String remove() {
		String result = INPUT;
		try {
			String pId = getRequest().getParameter("pId");
			if (StringUtils.isNotBlank(pId)) {
				getManager().remove(getManager().get(Integer.valueOf(pId)));
			}
			result = SUCCESS;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return result;
	}
}
