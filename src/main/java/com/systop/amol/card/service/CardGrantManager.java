package com.systop.amol.card.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.systop.amol.base.customer.CustomerConstants;
import com.systop.amol.card.CardConstants;
import com.systop.amol.card.model.CardGrant;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;
import com.systop.core.dao.support.Page;
import com.systop.core.service.BaseGenericsManager;

/**
 * 
 * 发卡管理Sercice
 * 
 * @author lee
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class CardGrantManager extends BaseGenericsManager<CardGrant> {

	/**
	 * 用于加密密码
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Resource
	private UserManager userManager;

	/**
	 * 根据客户身份证获取该客户已发放的卡的 list方法
	 */
	public List getCardList(String idCard,User user) {
		List<CardGrant> cards = null;
		if(user != null){
			if(user.getType().equals(AmolUserConstants.BANK)){
				cards = this.query(
						"from CardGrant cg where cg.customer.idCard = ? and (cg.card.cardState = ? or cg.card.cardState = ?) and cg.creator.id = ?",
						new Object[] { idCard, CardConstants.CARD_AVAILABLE,
								CardConstants.CARD_LOSS,user.getId() });
			}else{
				cards = this.query(
						"from CardGrant cg where cg.customer.idCard = ? and (cg.card.cardState = ? or cg.card.cardState = ?)",
						new Object[] { idCard, CardConstants.CARD_AVAILABLE,
								CardConstants.CARD_LOSS });
			}
		}
		List cardList = new ArrayList();
		for (CardGrant cardGrant : cards) {
			Map item = new HashMap();
			item.put("cardId", cardGrant.getCard().getId());
			item.put("cardNo", cardGrant.getCard().getCardNo());
			cardList.add(item);
		}
		return cardList;
	}

	/**
	 * @author 王会璞
	 *         <p>
	 *         判断扫描到的代币卡和输入的密码是否合法
	 *         </p>
	 * @param cardNo
	 *          代币卡号
	 * @param cardPassword
	 *          密码
	 * @return 代币卡对象密码正确 null密码错误
	 */
	public CardGrant getCardGrant(String cardNo, String cardPassword)
			throws ApplicationException {
		if (StringUtils.isNotBlank(cardNo)) {
			CardGrant cardGrant = this.findObject(
					"from CardGrant cg where cg.card.cardNo = ?", cardNo);
			if (cardGrant != null) {
				if (CardConstants.CARD_AVAILABLE.equals(cardGrant.getCard()
						.getCardState())) {
					if (StringUtils.isNotBlank(cardNo)
							&& StringUtils.isNotBlank(cardPassword)) {
						if ((passwordEncoder.encodePassword(cardPassword, null))
								.equals(cardGrant.getPassword())) {
							return cardGrant;
						}
					}
				} else {
					throw new ApplicationException(
							"<font color='red'>此代币卡状态不正常不能使用。</font>");
				}
			} else {
				throw new ApplicationException("<font color='red'>代币卡不存在！！</font>");
			}
		} else {
			throw new ApplicationException(
					"<font color='red'>请扫描代币卡并输入代币卡的密码。</font>");
		}
		return null;
	}

	/**
	 * 自动匹配已发代币卡卡号
	 * 
	 * @return
	 */
	public List getCards(User user) {
		
		List<CardGrant> list = null;
		if(user != null){
			if(user.getType().equals(AmolUserConstants.BANK)){
				list = this.query("from CardGrant cg where cg.creator.id = " + user.getId());
			}
			if(user.getType().equals(AmolUserConstants.AGENT)){
				list = this.query("from CardGrant cg where cg.customer.owner.id = " + user.getId());
			}
		}
		List cardsRst = new ArrayList();
		if (CollectionUtils.isNotEmpty(list)) {
			int arraySize = list.size();
			String[] cardGrants = new String[arraySize];
			for (int i = 0; i < arraySize; i++) {
				CardGrant cardGrant = (CardGrant) list.get(i);
				cardGrants[i] = (String) cardGrant.getCard().getCardNo();
				cardsRst.add(cardGrants[i]);
			}
		}
		return cardsRst;
	}

	/**
	 * 
	 *
	public CardGrant getCardGrantByCustomer(Customer customer) {
		CardGrant cardGrant = findObject(
				"from CardGrant cg where cg.customer = ? and cg.card.cardState != ?",
				new Object[] { customer, CardConstants.CARD_OTHER });
		return cardGrant;
	}
	*/

	/**
	 * @author 王会璞
	 *         <p>
	 *         根据代币卡号取出该持卡人姓名确定客户身份
	 *         </P>
	 * @param cardNo
	 *          代币卡号
	 * @param orderCustomerId
	 *          如果是订单生成出库，该表示下订单的客户id
	 * @param userId
	 *          要卖货的经销商id
	 * @return
	 */
	public List getCard(String cardNo, String userId, String orderCustomerId) {
		CardGrant cardGrant = this.findObject(
				"from CardGrant cg where cg.card.cardNo = ?", cardNo);
		List cardList = new ArrayList();
		if (cardGrant != null) {
			Map item = new HashMap();
			item.put("available", false);
			item.put("jxs", false);
			item.put("orderCustomer", false);
			// 代币卡状态是否是正常的 true正常 false不正常（冻结 挂失...）
			if (CardConstants.CARD_AVAILABLE.equals(cardGrant.getCard()
					.getCardState())
					&& CustomerConstants.satrt
							.equals(cardGrant.getCustomer().getStatus())) {
				item.put("available", true);
				if (StringUtils.isNotBlank(orderCustomerId)
						&& StringUtils.isNotBlank(userId)) {
					
					Boolean isJxs = userManager.getOwner(userId,cardGrant.getCustomer().getOwner().getId());
					item.put("jxs", isJxs.booleanValue());

					if(Integer.parseInt(orderCustomerId) == cardGrant.getCustomer()
							.getId().intValue()){
						item.put("orderCustomer", true);
					}
					if (isJxs.booleanValue() && Integer.parseInt(orderCustomerId) == cardGrant.getCustomer()
							.getId().intValue() ) {
						mapPut(cardGrant, item);
					}
				} else if (StringUtils.isNotBlank(userId)) {
					
						// 代币卡是否属于卖货的经销商是 true 不是false
						Boolean isJxs = userManager.getOwner(userId,cardGrant.getCustomer().getOwner().getId());
						item.put("jxs", isJxs.booleanValue());
						mapPut(cardGrant, item);
				}
			}
			cardList.add(item);
		} else {
			cardList = null;
		}
		return cardList;
	}

	/**
	 * @author 王会璞
	 *         <p>
	 *         根据代币卡号取出卡号对应的id
	 *         </P>
	 * @param cardNo
	 *          代币卡号
	 * @return
	 */
	public Integer getCardCardId(String cardNo) {
		CardGrant cardGrant = this.findObject("from CardGrant cg where cg.card.cardNo = ?)", cardNo);
		if(cardGrant != null){
			return cardGrant.getId();
		}
		return null;
	}
	
	/**
	 * @author 王会璞
	 *         <p>
	 *         根据代币卡号取出卡号对应的id
	 *         </P>
	 * @param cardNo
	 *          代币卡号
	 * @return
	 */
	public CardGrant getCardGrant(String cardNo) {
		CardGrant cardGrant = this.findObject("from CardGrant cg where cg.card.cardNo = ?)", cardNo);
		if(cardGrant != null){
			return cardGrant;
		}
		return null;
	}
	
	/**
	 * @author 王会璞
	 * <p>
	 * 	向map中放值
	 * </p>
	 * @param cardGrant
	 * @param item
	 */
	private void mapPut(CardGrant cardGrant, Map item) {
		item.put("cardNo", cardGrant.getCard().getCardNo());// 代币卡卡号
		item.put("customerId", cardGrant.getCustomer().getId());// 客户id
		item.put("customerName", cardGrant.getCustomer().getName()); // 客户姓名
		item.put("cardGrantId", cardGrant.getId());// CardGrant 的id
		item.put("balance", cardGrant.getBalance());// CardGrant 的代币卡余额
	}

	/**
	 * @author 王会璞
	 * <p>
	 * 	根据客户id得到客户的所有代币卡
	 * </p>
	 * @param page
	 * @param customerId
	 * @param ownerId 
	 * @return
	 */
	public Page getCardGrantList(Page page,Integer customerId, Integer ownerId) {
		return this.pageQuery(page, "from CardGrant cg where cg.customer.id = ? and (cg.customer.owner.id = ? or cg.customer.owner.superior.id = ?)", customerId ,ownerId ,ownerId);
	}
}
