package com.systop.amol.card.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.systop.amol.card.model.Card;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.service.BaseGenericsManager;

/**
 * 代币卡Manager
 * @author 
 */
@Service
public class CardManager extends BaseGenericsManager<Card> {
	
	/**
	 * 自动匹配卡号
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getCards(User user) {
		List<Card> list = this.query("from Card c where c.creator.id = " + user.getId());
		List cardsRst = new ArrayList();
		if (CollectionUtils.isNotEmpty(list)) {
			int arraySize = list.size();
			String[] cards = new String[arraySize];
			for (int i = 0; i < arraySize; i++) {
				Card card = (Card) list.get(i);
				cards[i] = (String) card.getCardNo();
				cardsRst.add(cards[i]);
			}
		}
		return cardsRst;
	}
	
}
