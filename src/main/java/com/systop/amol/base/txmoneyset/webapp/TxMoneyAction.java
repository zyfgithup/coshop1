package com.systop.amol.base.txmoneyset.webapp;

import com.systop.amol.base.txmoneyset.model.TxMoneySet;
import com.systop.amol.base.txmoneyset.service.TxMoneyManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TxMoneyAction extends DefaultCrudAction<TxMoneySet,TxMoneyManager>{
	/**
	 * 查看提现金额设置
	 */
	public String index() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		String sql = "from  TxMoneySet";
		List args = new ArrayList();
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		if(page.getData().size()==1){
			this.getRequest().setAttribute("flag","ok");
		}else{
			this.getRequest().setAttribute("flag", "no");
		}
		restorePageData(page);
		return INDEX;
	}
	/**
	 * 保存提现金额设置
	 */
	@Override
	public String save() {
    try {
  		getManager().save(getModel());
      return SUCCESS;
    } catch (Exception e) {
      addActionError(e.getMessage());
      return INPUT;
    }
	}
	@Override
	public String remove() {
		String result = INPUT;
		try {
			String txsetId = getRequest().getParameter("txsetId");
			if (StringUtils.isNotBlank(txsetId)) {
				getManager().remove(getManager().get(Integer.valueOf(txsetId)));
			}
			result = SUCCESS;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
