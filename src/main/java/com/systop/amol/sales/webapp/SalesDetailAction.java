package com.systop.amol.sales.webapp;


import javax.annotation.Resource;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.sales.model.SalesDetail;
import com.systop.amol.sales.service.BarcodeManager;
import com.systop.amol.sales.service.SalesDetailManager;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 销售详细信息管理Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SalesDetailAction extends DefaultCrudAction<SalesDetail, SalesDetailManager> {

	/** 条形码Manager */
	@Resource
	private BarcodeManager barcodeManager;
	
	@Override
	public String save() {
		if (hasActionMessages() || hasActionErrors()) {
			return INPUT;
		}
		return super.save();
	}
	
	/**
	 * 查看详情
	 */
	@Override
	public String view() {
		if(getModel().getId() != null && getModel().getId() > 0){
			getRequest().setAttribute("barcodeList", barcodeManager.getBarcodeList(getModel().getId()));
		}
		return super.view();
	}
}