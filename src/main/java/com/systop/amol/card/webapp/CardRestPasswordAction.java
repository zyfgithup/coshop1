package com.systop.amol.card.webapp;

import com.systop.amol.card.CardConstants;
import com.systop.amol.card.model.CardGrant;
import com.systop.amol.card.service.CardGrantManager;
import com.systop.common.modules.security.user.LoginUserService;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.webapp.struts2.action.JsonCrudAction;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 重置密码Action
 * @author lee
 *
 */
@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CardRestPasswordAction extends JsonCrudAction<CardGrant, CardGrantManager> {

	/**
	 * 用于加密密码
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private LoginUserService loginUserService;

	/**
	 * Json数据
	 */
	private Map<String, String> jsonResult;

	/***
	 * 重置密码
	 * @return
	 */
	public String save() {
		CardGrant cardGrant = getManager().findObject("from CardGrant cg where cg.card.cardNo = ?", getModel().getCard().getCardNo() );
		if(cardGrant == null){
			addActionError("请核实您的卡号！");
			return INPUT;
		}else if(!cardGrant.getCard().getCardState().equals(CardConstants.CARD_AVAILABLE)){
			addActionError("您的卡处于非正常状态，不能重设密码！");
			return INPUT;
		}else{
			cardGrant.setPassword(passwordEncoder.encodePassword(getModel().getPassword(),null));
			getManager().update(cardGrant);
		}	
		return SUCCESS;
	}
	/**
	 * 编辑页面，通过输入的卡号验证卡是否可用
	 * @return
	 */
	public String selectCardno(){
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		String cardno = getRequest().getParameter("cardno");//获取卡号
		User user = loginUserService.getLoginUser(getRequest());//获取当前登录用户
		if (user == null) {
			jsonResult.put("result", "error");
			return "jsonResult";
		}
		try {
			if (StringUtils.isNotBlank(cardno)) {
				CardGrant cardGrant = getManager().findObject("from CardGrant cg where cg.card.cardNo = ? and cg.creator.id = ?", cardno,user.getId() );
					if (cardGrant != null) {
						if( cardGrant.getCard().getCardState().equals(CardConstants.CARD_OTHER)){
							jsonResult.put("result", "cardState");				
						}else{
							jsonResult.put("cardno", cardno);
							jsonResult.put("customer", cardGrant.getCustomer().getName());
							jsonResult.put("idCard", cardGrant.getCustomer().getIdCard());
						}
					}else {
						//不存在卡信息
						jsonResult.put("result", "notCard");
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "jsonResult";
	}
	
	public Map<String, String> getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(Map<String, String> jsonResult) {
		this.jsonResult = jsonResult;
	}

}
