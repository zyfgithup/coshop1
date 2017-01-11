package com.systop.common.modules.register.webapp;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.common.modules.register.model.RegMemo;
import com.systop.common.modules.register.service.RegMemoManager;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegMemoAction extends DefaultCrudAction<RegMemo, RegMemoManager> {
	
	public String edit(){
		RegMemo memo = getManager().getMemo();
		if (memo == null){
			memo = new RegMemo();
			memo.setContent("初始化信息,请编写具体说明信息.");
			getManager().save(memo);
		}
		setModel(memo);
		return INPUT;
	}
	
	public String save(){
		getManager().save(getModel());
		addActionMessage("信息修改成功");
		return SUCCESS;
	}
	
	public String view(){
		RegMemo memo = getManager().getMemo();
		if (memo == null){
			memo = new RegMemo();
			memo.setContent("信息尚未配置...");
		}
		setModel(memo);
		return VIEW;
	}
	
}
