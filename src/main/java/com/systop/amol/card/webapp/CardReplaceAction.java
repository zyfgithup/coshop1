package com.systop.amol.card.webapp;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.customer.CustomerConstants;
import com.systop.amol.base.customer.model.Customer;
import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.card.CardConstants;
import com.systop.amol.card.model.Card;
import com.systop.amol.card.model.CardGrant;
import com.systop.amol.card.service.CardGrantManager;
import com.systop.amol.card.service.CardManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.webapp.struts2.action.JsonCrudAction;

/**
 * 补卡管理Action
 * 
 * @author lee
 * 
 */
@SuppressWarnings({ "serial","rawtypes"})
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CardReplaceAction extends JsonCrudAction<CardGrant, CardGrantManager> {

	/**JSON数据*/
	private Map<String, String> jsonResult;
	
	/**JSON数据*/
	private List cardsRst;
	
	/** JSON数据 */
	private List orderCardRst;
	
	@Autowired
	private CardManager cardManager;
	
	@Autowired
	private CustomerManager customerManager;
	
	/**
	 * 保存补卡信息,补卡继承新卡信息
	 */
	public String save() {
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		Integer oldCardno = Integer.valueOf(getRequest().getParameter("oldCardno"));
		String cardno = getRequest().getParameter("cardno");
		//根据新卡卡号获取新卡
		Card card = cardManager.findObject("from Card c where c.cardNo= ? ", cardno);
		//根据旧卡Id获取旧卡信息
		Card oldCard = cardManager.get(oldCardno);
		card.setCardState(CardConstants.CARD_AVAILABLE);//设置新卡状态为正常
		oldCard.setCardState(CardConstants.CARD_OTHER);//设置旧卡状态为注销
		//card.setEndDate(oldCard.getEndDate());//设置新卡有效期
		cardManager.update(oldCard);
		cardManager.update(card);
		//根据旧卡ID获取旧卡grant信息
		CardGrant oldCardGrant = getManager().findObject("from CardGrant cg where cg.card = ?", oldCard);
		getModel().setBalance(oldCardGrant.getBalance());//继承余额
		getModel().setCustomer(oldCardGrant.getCustomer());//继承持卡人
		getModel().setCredit(oldCardGrant.getCredit());//继承信用额度
		getModel().setPassword(oldCardGrant.getPassword());//继承密码
		getModel().setUpMoney(oldCardGrant.getUpMoney());//继承充值总金额
		getModel().setSpend(oldCardGrant.getSpend());//继承消费总金额
		getModel().setDepositReceipt(oldCardGrant.getDepositReceipt());//继承存款单号
		Date date=new Date();
		getModel().setCreateDate(date);//设置新卡开卡日期，默认当天
		getModel().setEndDate(oldCardGrant.getEndDate());//设置新卡有效期
		getModel().setCreator(user);//添加补卡人
		getModel().setCard(card);
		getManager().save(getModel());
		return SUCCESS;
	}
	
	/**
	 * 编辑页面，通过输入的身份证号验证是否有合法的会员客户信息
	 * @return
	 */
	public String checkcustomer() {
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		String idCard = getRequest().getParameter("idCard");	
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		if (user == null) {
			jsonResult.put("result", "error");
			return "jsonResult";
		}		
		try {
			if (StringUtils.isNotBlank(idCard)) {
				Customer customer = customerManager.searchCustomer(idCard);
					if (customer != null) {
						if(customer.getType().equals(CustomerConstants.PTKH)){
							jsonResult.put("result", "notVIP");//非会员客户
						}
						else{
							jsonResult.put("customerName", customer.getName());
						}
					}else {		
						jsonResult.put("result", "notExist");//客户表不存在客户信息
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "jsonResult";
	}
	
	/**
	 * 根据身份证号显示该客户已发放的所有卡的信息，用于fillCard.jsp显示
	 */
	public String getCardsList() {
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		cardsRst = Collections.EMPTY_LIST;
		String idCard = getRequest().getParameter("idCard");//获取身份证号
		if (StringUtils.isNotBlank(idCard)) {
			cardsRst = getManager().getCardList(idCard,user);	
		}
		return "jsonRst";
	}
	
	public String getCard(){
		String cardNo = getRequest().getParameter("cardNo");//获取代币卡号
		String userId = getRequest().getParameter("userId");//经销商id
		String orderCustomerId = getRequest().getParameter("orderCustomerId");//经销商id
		if(StringUtils.isNotBlank(orderCustomerId) && StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(cardNo)){
				orderCardRst = getManager().getCard(cardNo,userId,orderCustomerId);
		}else{
			if(StringUtils.isNotBlank(cardNo) && StringUtils.isNotBlank(userId)){
				orderCardRst = getManager().getCard(cardNo,userId,null);
			}			
		}
		return "orderCardRst";
	}
	
	/**
	 * 编辑页面，通过输入的卡号验证卡是否可用
	 * @return
	 */
	public String checkCardno(){
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		String cardno = getRequest().getParameter("cardno");//获取卡号
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		if (user == null) {
			jsonResult.put("result", "error");
			return "jsonResult";
		}
		try {
			if (StringUtils.isNotBlank(cardno)) {				
				Card card = cardManager.findObject("from Card c where c.cardNo = ? and c.creator.id = ?", cardno,user.getId());//获取代币卡
				if (card != null) {
					 if(!card.getCardState().equals(CardConstants.CARD_NOTOCCUPY)){
						jsonResult.put("result", "available");//卡已使用
					 }
				}else {
					jsonResult.put("result", "notExist");//不存在卡信息
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "jsonResult";
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(Map<String, String> jsonResult) {
		this.jsonResult = jsonResult;
	}

	public List getCardsRst() {
		return cardsRst;
	}

	public void setCardsRst(List cardsRst) {
		this.cardsRst = cardsRst;
	}

	public List getOrderCardRst() {
		return orderCardRst;
	}

	public void setOrderCardRst(List orderCardRst) {
		this.orderCardRst = orderCardRst;
	}
	
}
