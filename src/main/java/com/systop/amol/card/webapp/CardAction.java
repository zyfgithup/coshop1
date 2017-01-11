package com.systop.amol.card.webapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.card.CardConstants;
import com.systop.amol.card.model.Card;
import com.systop.amol.card.service.CardManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.JsonCrudAction;

/**
 * 代币卡Action
 * @author lee
 *
 */
@SuppressWarnings({"serial","unchecked","rawtypes"})
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CardAction extends JsonCrudAction<Card, CardManager> {

	/**JSON数据*/
	private List cardsRst;
	
//	@Autowired
//	private CardManager cardManager;
	
	/**
	 * 查询列表
	 */
	public String index(){
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Card c where 1 = 1");
		List args = new ArrayList();
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		if(user != null ){
			sql.append(" and c.creator.id = ?");
			args.add(user.getId());
		}
		if (StringUtils.isNotBlank(getModel().getCardNo())) {//代币卡卡号
			sql.append(" and c.cardNo like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCardNo()));
		}
		if (StringUtils.isNotBlank(getModel().getCardState())) {//状态
			sql.append(" and c.cardState = ?");
			args.add(getModel().getCardState());
		}
		sql.append(" order by c.id desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}
	
	/**
	 * 保存方法
	 */
	@Override
	public String save(){
		try {
			User user = UserUtil.getPrincipal(getRequest());//当前登录用户
			if(user !=null){
				if(this.getManager().getDao().exists(this.getModel(),"cardNo"))
				{
					addActionError("卡号为【" + getModel().getCardNo() + "】的代币卡已存在。");
					return INPUT;
				}
				if (getModel().getId() == null) {
					getModel().setCreator(user);//设置建卡人
					Date date=new Date();
					getModel().setCreateDate(date);//设置建卡日期
					getModel().setCardState(CardConstants.CARD_NOTOCCUPY);//设置卡状态未占用				
					getManager().getDao().clear();
					getManager().save(getModel());
					cardsRst = null;				
				}else{
					getManager().update(getModel());
				}
			}else{
				addActionError("未登录，请登录后访问本页面。");
				return INPUT;
			}
	    } catch (Exception e) {
	      addActionError(e.getMessage());
	      return INPUT;
	    }
	    return SUCCESS;
	}
	
	/**
	 * 自动匹配卡号信息
	 * @return
	 */
	public String getCards() {
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		cardsRst = Collections.EMPTY_LIST;
		if (CollectionUtils.isEmpty(cardsRst)) {
			cardsRst = getManager().getCards(user);
		}
		return "jsonRst";
	}
  
	/**
	 * 代币卡状态MAP
	 * @return
	 */
	public Map<String, String> getCardStates(){
		return CardConstants.CARD_MAP_CARD;
	}
	
//	public String card(){
//		String no = "11111111111111111111111111111";
//		Integer cardno = 111;
//		User user = loginUserService.getLoginUser(getRequest());//当前登录用户
//		for (int i = 1; i <= 101; i++) {			
//			Card card = new Card();
//			card.setCardNo(no+cardno);
//			card.setCardState(CardConstants.CARD_NOTOCCUPY);
//			card.setCreator(user);
//			card.setCreateDate(new Date());
//			cardManager.save(card);
//			cardno++;
//		}
//		return index();
//	}
	
	/**
	 * 
	 * @return
	 */
	public List getCardsRst() {
		return cardsRst;
	}

	public void setCardsRst(List cardsRst) {
		this.cardsRst = cardsRst;
	}
	
}
