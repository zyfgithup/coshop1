package com.systop.amol.card.webapp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Controller;

import com.systop.amol.card.CardConstants;
import com.systop.amol.card.model.CardGrant;
import com.systop.amol.card.model.CardUp;
import com.systop.amol.card.service.CardGrantManager;
import com.systop.amol.card.service.CardUpManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.JsonCrudAction;

/**
 * 充值管理Action
 * @author lee
 *
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CardUpAction extends JsonCrudAction<CardUp,CardUpManager> {
	
	/**
	 * Json数据
	 */
	private Map<String, String> jsonResult;
	
	@Autowired
	private CardGrantManager cardGrantManager;
		
	/**
	 * JdbcTemplate，用户获取充值信息
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//开始时间
	private String startDate;
	
	//截至时间
	private String endDate;
	
	/**
	 * 查询列表
	 */
	public String index() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from CardUp cu where 1 = 1");
		List args = new ArrayList();
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		if(user != null ){
			sql.append(" and cu.cardGrant.creator.id = ?");
			args.add(user.getId());
		}
		if (getModel().getCardGrant() != null ) {
			if(StringUtils.isNotBlank(getModel().getCardGrant().getCard().getCardNo())){
				sql.append(" and cu.cardGrant.card.cardNo like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCardGrant().getCard().getCardNo()));
			}			
		}
		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and cu.recharge >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and cu.recharge <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sql.append(" order by cu.id desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		
		String hql = "select sum(upMoney) "+sql.toString();
		List list = getManager().query(hql, args.toArray());
		String totalUpMoney = "";
		if(list.size()>0){
			for (Object o : list) {
				if(o != null){
					totalUpMoney = o.toString();
				}
			}
		}
		getRequest().setAttribute("totalUpMoney", String.valueOf(totalUpMoney));
		
		restorePageData(page);
		return INDEX;
	}
	
	/**
	 * 保存方法
	 */
	@Override
	public String save(){		
		if (getModel().getId() == null) {
			User user = UserUtil.getPrincipal(getRequest());//当前登录用户
			if(user != null){
				CardGrant cardGrant = cardGrantManager.findObject("from CardGrant cg where cg.card.cardNo=? and cg.creator.id = ?", getModel().getCardGrant().getCard().getCardNo(),user.getId());
				if(cardGrant == null){
					addActionError("请核实您的卡号！");
					return INPUT;
				}else{	
					Double total = cardGrant.getBalance()+getModel().getUpMoney();
					if(total > cardGrant.getCredit()){
						addActionError("您当前最多可充值"+(cardGrant.getCredit()-cardGrant.getBalance())+"元");
						return INPUT;
					}else if (cardGrant.getCard().getCardState().equals(CardConstants.CARD_LOSS)) {
						addActionError("您的卡已挂失，不能进行充值！");
						return INPUT;
					}else if (cardGrant.getCard().getCardState().equals(CardConstants.CARD_OTHER)) {
						addActionError("您的卡已注销，不能进行充值！");
						return INPUT;
					}else{
						//设置充值总额
						cardGrant.setUpMoney(cardGrant.getUpMoney()+getModel().getUpMoney());
						//设置余额
						cardGrant.setBalance(total);
						cardGrantManager.update(cardGrant);
						Date recharge=new Date();
						getModel().setRecharge(recharge);//设置充值日期，默认当天
						getModel().setUser(user);
						getModel().setRemark("正常");
						getModel().setRed(CardConstants.RED_NO);
						getModel().setCardGrant(cardGrant);
						getManager().save(getModel());
					}
				}
			}else{
				addActionError("未登录，请登录后访问本页面。");
				return INPUT;
			}		
		}
//		else{
//			CardGrant cardGrant = cardGrantManager.get(getModel().getCardGrant().getId());
//			Float upmoney = getCardUp(getModel().getId());//上次充值金额
//			Float balance = getModel().getCardGrant().getBalance();//当前余额
//			Float allUpMoney=getModel().getCardGrant().getUpMoney();
//			Float newMoney = getModel().getUpMoney();//修改充值金额
//			if((balance-upmoney+newMoney) > getModel().getCardGrant().getCredit()){
//				addActionError("您当前最多可修改充值金额为"+(getModel().getCardGrant().getCredit()- balance + upmoney)+"元");
//				return INPUT;
//			}else{
//				cardGrant.setBalance(balance-upmoney+newMoney);
//				cardGrant.setUpMoney(allUpMoney-upmoney+newMoney);
//				cardGrantManager.update(cardGrant);
//				getModel().setCardGrant(getModel().getCardGrant());
//				getManager().update(getModel());
//			}
//		}
		return SUCCESS;
	}
	
	/***
	 * 冲红
	 * @return
	 */
	public String red(){

		User user = UserUtil.getPrincipal(getRequest());		
		CardUp cardUp = new CardUp();
		if (user != null) {
			cardUp.setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		CardGrant cardGrant = cardGrantManager.get(getModel().getCardGrant().getId());
		
		Double balance = getModel().getCardGrant().getBalance();//当前余额
		Double upmoney = getCardUp(getModel().getId());//上次充值金额
		Double allUpMoney=getModel().getCardGrant().getUpMoney();
		if((balance-upmoney) < 0){
			this.addActionMessage("该记录不能进行冲红操作！冲红后余额为负数");
			return SUCCESS;
		}else{
			cardGrant.setBalance(balance-upmoney);
			cardGrant.setUpMoney(allUpMoney-upmoney);
			cardGrantManager.update(cardGrant);
			getModel().setRed(CardConstants.RED_YES);
			getModel().setRemark("已冲红");
			cardUp.setCardGrant(cardGrant);
			cardUp.setRecharge(new Date());
			cardUp.setUpMoney(-getModel().getUpMoney());
			cardUp.setRed(CardConstants.RED_RED);
			cardUp.setRemark("冲红单");
			getManager().save(cardUp);
		}
		return SUCCESS;
	}
	/**
	 * 返回指定Id的充值金额
	 */
	private Double getCardUp(Integer Id) {
		return (Double) jdbcTemplate.query(
				"select upMoney from card_ups where id=?", new Object[] { Id },
				new ResultSetExtractor() {
					public Object extractData(ResultSet rs) throws SQLException {
						rs.next();
						return rs.getDouble(1);
					}
				});
	}
	
	/**
	 * 编辑页面，通过输入的卡号验证卡是否可用并绑定持卡人信息
	 * @return
	 */
	public String selectCardno(){
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		String cardno = getRequest().getParameter("cardno");//获取卡号
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		if (user == null) {
			jsonResult.put("result", "error");
			return "jsonResult";
		}
		try {
			if (StringUtils.isNotBlank(cardno)) {
				CardGrant cardGrant = cardGrantManager.findObject("from CardGrant cg where cg.card.cardNo = ? and cg.creator.id = ?", cardno,user.getId());
					if (cardGrant != null) {
						if( cardGrant.getCard().getCardState().equals(CardConstants.CARD_OTHER)){
							jsonResult.put("result", "cardState");				
						}else if (cardGrant.getCard().getCardState().equals(CardConstants.CARD_LOSS)) {
							jsonResult.put("result", "fill");
						}else{
							jsonResult.put("cardno", cardGrant.getCard().getCardNo());//
							jsonResult.put("customerName", cardGrant.getCustomer().getName());//绑定持卡人姓名
						}
					}else {
						jsonResult.put("result", "notCard");//不存在卡信息
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
	
}
