package com.systop.common.modules.register.webapp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.security.user.UserConstants;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.Constants;
import com.systop.core.webapp.struts2.action.JsonCrudAction;

@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegisterAction extends JsonCrudAction<User, UserManager> {
	
	private Map<String, Object> result;
	
	public String edit(){
		return INPUT;
	}
	
	/**
	 * 用户注册，异步保存
	 */
	public String save(){
		result = new HashMap<String, Object>();
		try{
			Region region = getModel().getRegion();
			if (region != null && region.getId() == 0) {// 如果地区为空则不关联
				getModel().setRegion(null);
			}
			if (getModel().getBeginningInit() == null || getModel().getBeginningInit().equals("") ) {
				getModel().setBeginningInit(Constants.NO);
			}
			getModel().setStatus(UserConstants.USER_STATUS_REG_UNVERIFY);
			getModel().setType(AmolUserConstants.AGENT);
			getModel().setCreateTime(new Date());
			getManager().save(getModel());
			if (getModel().getId() != null){
				result.put("success", true);
				result.put("loginId", getModel().getLoginId());
			}else{
				result.put("success", false);
				result.put("msg", "注册保存用户失败");
			}
		}catch(Exception e){
			result.put("success", false);
			result.put("msg", "用户注册失败");
			e.printStackTrace();
		}
		return "ajaxSave";
	}

	public Map<String, Object> getResult() {
		return result;
	}
	
}
