package com.systop.amol.card.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.card.CardConstants;
import com.systop.amol.card.model.CardSpend;
import com.systop.amol.card.service.CardSpendManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 消费记录Action
 * 
 * @author 王会璞
 *         <p>
 *         客户代币卡消费的记录明细
 *         </p>
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CardSpendAction extends
		DefaultCrudAction<CardSpend, CardSpendManager> {

	/** 身份证号 */
	private String idCard;

	/** 开始时间 */
	private String startDate;

	/** 截至时间 */
	private String endDate;

	/** 标识经销 **/
	private String jxs;

	/**
	 * 查询列表
	 */
	public String index() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from CardSpend cs where 1 = 1");
		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		if (user == null) {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INDEX;
		}
		if (getModel().getCardGrant() != null
				&& getModel().getCardGrant().getCard() != null
				& StringUtils.isNotBlank(getModel().getCardGrant().getCard()
						.getCardNo())) {
			sql.append(" and cs.cardGrant.card.cardNo = ?");
			args.add(getModel().getCardGrant().getCard().getCardNo());
		}
		if (StringUtils.isNotBlank(user.getType())) {
			if (user.getType().equals(AmolUserConstants.BANK)) {
				sql.append(" and cs.cardGrant.creator.id = ?");
				args.add(user.getId());
			}
			if (user.getType().equals(AmolUserConstants.AGENT)) {
				if (StringUtils.isNotBlank(jxs)) {
					if (user.getSuperior() == null) {
						Integer superid = user.getId();
						if (superid != null && superid.intValue() > 0) {
							sql.append(" and (cs.user.superior.id = ? or cs.user.id = ?) ");
							args.add(superid);
							args.add(superid);
						}
					} else if (user.getType().equals(AmolUserConstants.EMPLOYEE)) {
						sql.append(" and (cs.user.superior.id = ? or cs.user.id = ?) ");
						args.add(user.getSuperior().getId());
						args.add(user.getSuperior().getId());
					} else {
						sql.append(" and cs.user.id = ? ");
						args.add(user.getId());
					}
				}
			}
		}

		if (getModel().getCardGrant() != null) {// 卡号
			if (StringUtils.isNotBlank(getModel().getCardGrant().getCard()
					.getCardNo())) {
				sql.append(" and cs.cardGrant.card.cardNo like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCardGrant()
						.getCard().getCardNo()));
			}
		}

		if (StringUtils.isNotBlank(idCard)) {// 身份证号
			sql.append(" and cs.cardGrant.customer.idCard like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(idCard));
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and cs.spendDate >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and cs.spendDate <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sql.append(" order by cs.id desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());

		String hql = "select sum(spendMoney) " + sql.toString();
		List list = getManager().query(hql, args.toArray());
		String totalSpendMoney = "";
		if (list.size() > 0) {
			for (Object o : list) {
				if (o != null) {
					totalSpendMoney = o.toString();
				}
			}
		}
		getRequest().setAttribute("totalSpendMoney",
				String.valueOf(totalSpendMoney));
		restorePageData(page);
		return INDEX;
	}

	/**
	 * 卡状态
	 * 
	 * @return
	 */
	public Map<String, String> getCardStateMap() {
		Map cardStatesMap = new LinkedHashMap();
		cardStatesMap.put(CardConstants.CARD_AVAILABLE, "正常");
		cardStatesMap.put(CardConstants.CARD_FAIL, "过期");
		cardStatesMap.put(CardConstants.CARD_LOSS, "挂失");
		cardStatesMap.put(CardConstants.CARD_FREEZE, "冻结");
		cardStatesMap.put(CardConstants.CARD_OTHER, "注销");
		return cardStatesMap;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getJxs() {
		return jxs;
	}

	public void setJxs(String jxs) {
		this.jxs = jxs;
	}

}
