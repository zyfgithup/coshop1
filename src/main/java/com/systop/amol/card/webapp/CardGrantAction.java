package com.systop.amol.card.webapp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.customer.CustomerConstants;
import com.systop.amol.base.customer.model.Customer;
import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.card.CardConstants;
import com.systop.amol.card.model.Card;
import com.systop.amol.card.model.CardGrant;
import com.systop.amol.card.service.CardGrantManager;
import com.systop.amol.card.service.CardManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.JsonCrudAction;

/**
 * 发卡管理Action
 * 
 * @author lee
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CardGrantAction extends JsonCrudAction<CardGrant, CardGrantManager> {

	/** 旧密码*/
	private String oldPassword;
	
	/**JSON数据*/
	private List cardsRst;
	
	/**
	 * JdbcTemplate，用户获取密码
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;
		
	/**
	 * 用于加密密码
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 代币卡manager
	 */
	@Autowired
	private CardManager cardManager;

	/**
	 * 客户Manager
	 */
	@Autowired
	private CustomerManager customerManager;

	/**
	 * Json数据
	 */
	private Map<String, String> jsonResult;
	
	/** Json数据 密码是否正确标识 */
	private Boolean isPassword = false;
	
	/** 检测存款单号的结果 */
	private Map<String, Object> checkResult;
	
	/**
	 * 卡状态
	 * @return
	 */
	public Map<String, String> getCardStateMap(){
		Map cardStatesMap = new LinkedHashMap();
		cardStatesMap.put(CardConstants.CARD_AVAILABLE, "正常");
		cardStatesMap.put(CardConstants.CARD_FAIL, "过期");
		cardStatesMap.put(CardConstants.CARD_LOSS, "挂失");
		cardStatesMap.put(CardConstants.CARD_FREEZE, "冻结");
		cardStatesMap.put(CardConstants.CARD_OTHER, "注销");
		return cardStatesMap;
	}
		
	/**
	 * 查询列表
	 */
	public String index() {

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from CardGrant gg where 1 = 1");
		List args = new ArrayList();
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		if(user != null ){
			if(StringUtils.isNotBlank(user.getType())){
				if(user.getType().equals(AmolUserConstants.BANK)){
					sql.append(" and gg.creator.id = ?");
					args.add(user.getId());
				}
			}			
		}
		if (getModel().getCard() != null) {//卡号
			if (StringUtils.isNotBlank(getModel().getCard().getCardNo())){
				sql.append(" and gg.card.cardNo like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCard().getCardNo()));
			}
			if (StringUtils.isNotBlank(getModel().getCard().getCardState())){
				sql.append(" and gg.card.cardState = ?");
				args.add(getModel().getCard().getCardState());
			}
		}
		if (getModel().getCustomer() != null ) {//身份证号
			if(StringUtils.isNotBlank(getModel().getCustomer().getIdCard())){
				sql.append(" and gg.customer.idCard like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer().getIdCard()));
			}
			if(getModel().getCustomer().getId() != null){
				sql.append(" and gg.customer.id = ?");
				args.add(getModel().getCustomer().getId());
			}
		}
		sql.append(" order by gg.id desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}
	
	/**
	 * 代币卡查询（销售）
	 */
	public String indexSales() {
		String returnSales = "bankSales";
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from CardGrant gg where 1 = 1");
		List args = new ArrayList();
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		if(user != null ){
			if(StringUtils.isNotBlank(user.getType())){
				if(user.getType().equals(AmolUserConstants.BANK)){
					sql.append(" and gg.creator.id = ?");
					args.add(user.getId());
				}
				if (user.getType().equals(AmolUserConstants.AGENT)) {
					returnSales = "indexSales";
					if (user.getSuperior() == null) {
						Integer superid = user.getId();
						if (superid != null && superid.intValue() > 0) {
							sql.append(" and (gg.customer.agent.superior.id = ? or gg.customer.agent.id = ?) ");
							args.add(superid);
							args.add(superid);
						}
					} 
				}else if (user.getType().equals(AmolUserConstants.EMPLOYEE)) {
					sql.append(" and (gg.customer.agent.superior.id = ? or gg.customer.agent.id = ?) ");
					args.add(user.getSuperior().getId());
					args.add(user.getSuperior().getId());
				}
			}
		}
		
		if (getModel().getCard() != null) {//卡号
			if (StringUtils.isNotBlank(getModel().getCard().getCardNo())){
				sql.append(" and gg.card.cardNo like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCard().getCardNo()));
			}
			if (StringUtils.isNotBlank(getModel().getCard().getCardState())){
				sql.append(" and gg.card.cardState = ?");
				args.add(getModel().getCard().getCardState());
			}
		}
		if (getModel().getCustomer() != null ) {//身份证号
			if(StringUtils.isNotBlank(getModel().getCustomer().getIdCard())){
				sql.append(" and gg.customer.idCard like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer().getIdCard()));
			}
			if(getModel().getCustomer().getId() != null){
				sql.append(" and gg.customer.id = ?");
				args.add(getModel().getCustomer().getId());
			}
		}
		sql.append(" order by gg.id desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return returnSales;
	}

	/**
	 * 保存方法
	 */
	@Override
	public String save() {
		if (getModel().getId() == null) {
			User user = UserUtil.getPrincipal(getRequest());//当前登录用户
			if (user != null) {

				// 获取卡信息
				Card card = cardManager.findObject("from Card c where c.cardNo=? and c.creator.id = ?", getModel().getCard().getCardNo(),user.getId());
				if (card == null) {
					addActionError("请核实您卡号！");
					return INPUT;
				} else {
					if (card.getCardState().equals(CardConstants.CARD_NOTOCCUPY)) {
						// 根据客户身份证号获取用户
						Customer customer = customerManager.getCustomerInfo(Integer.valueOf(getModel().getCustomer().getOwner().getId()), 
								getModel().getCustomer().getIdCard());
						if (customer == null) {
							addActionError("系统没有对应的客户信息！");
							return INPUT;
						} else {
							if(customer.getType().equals(CustomerConstants.PTKH)){
								addActionError("您是普通客户！请办理会员！");
								return INPUT;
							}
//							else if (getManager().getCardGrantByCustomer(customer) != null) {
//								addActionError("该用户在此供应商下存在卡信息！");
//								return INPUT;
//							}
							else{
//								if(this.getManager().getDao().exists(this.getModel(),"depositReceipt")){
//									addActionError("存款单号为【" + getModel().getDepositReceipt() + "】已经办理代币卡。");
//									return INPUT;
//								}
								card.setCardState(CardConstants.CARD_AVAILABLE); // 设置卡状态可用
								cardManager.update(card);// 更新card表
								getModel().setCard(card);// 设置grant中card信息
								getModel().setCreator(user);// 设置创建者
								getModel().setCustomer(customer);// 设置持卡人
								Date date = new Date();
								getModel().setCreateDate(DateUtil.firstSecondOfDate(date));// 设置开卡时间，默认当天
								getModel().setUpMoney(0.00);// 设置充值总金额,新创建卡是默认为0
								getModel().setSpend(0.00);// 设置消费总金额,新创建卡是默认为0
								if (getModel().getCredit() == null) {
									getModel().setCredit(0.00);// 设置余额,新创建卡是默认和信用额度相同
								}
								getModel().setBalance(getModel().getCredit());
								// 设置密码，加密
								getModel().setPassword(
										passwordEncoder.encodePassword(getModel()
												.getPassword(), null));
								getManager().getDao().clear();
								getManager().save(getModel());
							}
						}
					} else {
						addActionError("该卡已被占用！请重新输入卡号！");
						return INPUT;
					}
				}
			} else {
				addActionError("未登录，请登录后访问本页面。");
				return INPUT;
			}
		} else {
			String pass = getOldPassword(getModel().getId());// 当前卡对应密码
			getModel().setPassword(pass);// 设置密码为原密码
			// 设置余额,新创建卡是默认和信用额度相同
			getModel().setBalance(getModel().getCredit() + getModel().getUpMoney() - getModel().getSpend());
			getManager().update(getModel());// 更新
		}
		return SUCCESS;
	}
	
	/**
	 * 更改卡的状态
	 * 
	 * @return
	 */
	public String updateCard() {
		Card card = getModel().getCard();//得到代币卡
		card.setCardState(getModel().getCard().getCardState());//设置代币卡的状态
		getManager().update(getModel());//更新代币卡状态
		return SUCCESS;
	}

	public String search(){
		index();
		return "search";
	}
	/**
	 * 返回指定cardId的密码
	 */
	private String getOldPassword(Integer Id) {
		return (String) jdbcTemplate.query(
				"select password from cards_grant where id=?", new Object[] { Id },
				new ResultSetExtractor() {
					public Object extractData(ResultSet rs) throws SQLException {
						rs.next();
						return rs.getString(1);
					}
				});
	}
	
	
	/***
	 * 修改密码
	 * @return
	 */
	public String updatePassword() {
		if ((passwordEncoder.encodePassword(oldPassword,null)).equals(getOldPassword(getModel().getId()))) {
			getModel().setPassword(passwordEncoder.encodePassword(getModel().getPassword(),null));
			getManager().update(getModel());
		} else {
			addActionError("请输入正确的原始密码!");
			return "updatePassword";
		}
		return SUCCESS;
	}
	
	/**
	 * 预览客户信息
	 */
	public String view() {
		super.view();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String createDate = dateFormat.format(getModel().getCreateDate());
		getRequest().setAttribute("createDate", createDate);
		String endDate = dateFormat.format(getModel().getEndDate());
		getRequest().setAttribute("endDate", endDate);
		return VIEW;
	}
	
	/**
	 * AJAX请求，检测存款单号是否存在
	 */
	public String check() {
		
		checkResult = new HashMap<String, Object>();
		checkResult.put("exist",
				this.getManager().getDao().exists(this.getModel(),"depositReceipt"));
		return "check";
	}
	
	/**
	 * 查询过期的代币卡
	 */
	public String checkCard(){
		Date date = new Date();
		List<CardGrant> cards = getManager().query("from CardGrant cg where cg.endDate < ? and cg.card.cardState = ?", new Object[]{date, CardConstants.CARD_AVAILABLE });
		for (CardGrant cardGrant : cards) {
			cardGrant.getCard().setCardState(CardConstants.CARD_FAIL);//设置卡状态为过期
			getManager().update(cardGrant);
		}
		return "checkCard";
	}
	
	/**
	 * @author 王会璞
	 * <p>
	 * 验证客户输入的密码是否正确，
	 * 如果正确允许客户使用代币卡消费（如果代币卡的钱够消费的话），并且将代币卡的余额显示在操作页面。
	 * </p>
	 * @return
	 */
	public String validatePassoword(){
		String cardGrantId = getRequest().getParameter("cardGrantId");
		String password = getRequest().getParameter("password");
		if(StringUtils.isNotBlank(cardGrantId) && StringUtils.isNotBlank(password)){
			if((passwordEncoder.encodePassword(password,null)).equals(getOldPassword(Integer.parseInt(cardGrantId)))) {
				isPassword = true;
			}else{
				isPassword = false;
			}
		}
		return "password";
	}
	
	/**
	 * @author 王会璞
	 * <p>
	 * 验证客户输入的密码是否正确，
	 * 如果正确允许客户使用代币卡消费（如果代币卡的钱够消费的话），并且将代币卡的余额显示在操作页面。
	 * </p>
	 * @return
	 */
	public String validatePassowordCardNo(){
		String cardNo = getRequest().getParameter("cardNo");
		String password = getRequest().getParameter("password");
		if(StringUtils.isNotBlank(cardNo) && StringUtils.isNotBlank(password)){
			if((passwordEncoder.encodePassword(password,null)).equals(getOldPassword(getManager().getCardCardId(cardNo)))) {
				isPassword = true;
			}else{
				isPassword = false;
			}
		}
		return "password";
	}

	/**
	 * 编辑页面，通过输入的卡号验证卡是否可用
	 * @return
	 */
	public String selectCardno(){
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		String cardno = getRequest().getParameter("cardno");//获取代币卡卡号
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		if (user == null) {//当前登录用户为空
			jsonResult.put("result", "error");
			return "jsonResult";
		}
		try {
			if (StringUtils.isNotBlank(cardno)) {
				Card cards = cardManager.findObject("from Card c where c.cardNo = ? and c.creator.id = ?", cardno,user.getId());
					if (cards != null) {
						if(!cards.getCardState().equals(CardConstants.CARD_NOTOCCUPY)){
							jsonResult.put("result", "notNull");//卡已占用
						}else{
							jsonResult.put("cardno", cards.getCardNo());	
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
	
	/**
	 * 编辑页面，通过输入的身份证号验证是否有合法的会员客户信息
	 * @return
	 */
	public String selectCustomer() {
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		String idCard = getRequest().getParameter("idCard");//获取身份证号
		String ownerId = getRequest().getParameter("ownerId");
		User user = UserUtil.getPrincipal(getRequest());//当前登录用户
		if (user == null) {//当前用户为空
			jsonResult.put("result", "error");
			return "jsonResult";
		}		
		try {
			if(ownerId.equals("")){
				jsonResult.put("result", "agent");//经销商
			}else if(StringUtils.isNotBlank(idCard)) {
				Customer customer = customerManager.getCustomerInfo(Integer.valueOf(ownerId), idCard);
					if (customer != null) {
						if(customer.getType().equals(CustomerConstants.PTKH)){
							jsonResult.put("result", "notVIP");//非会员客户
						}
//						if(getManager().getCardGrantByCustomer(customer) != null){
//							jsonResult.put("result", "ownerCard");//该用户在此供应商下存在卡信息
//						}
						else{
							jsonResult.put("idCard", idCard);
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
	 * 自动匹配已发代币卡卡号信息
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
	 * @author 王会璞
	 * <p>
	 *	 根据客户，查询出与之有关的代币卡
	 * </p>
	 * @return
	 */
	public String viewBank(){
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		String customerIdStr = (String)getRequest().getParameter("customerId");
		String ownerIdStr = (String)getRequest().getParameter("ownerId");
		if(StringUtils.isNotBlank(customerIdStr) && StringUtils.isNotBlank(ownerIdStr)){
			page = getManager().getCardGrantList(page,new Integer(customerIdStr),new Integer(ownerIdStr));
		}
		restorePageData(page);
		return "viewBank";
	}
	
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public Map<String, String> getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(Map<String, String> jsonResult) {
		this.jsonResult = jsonResult;
	}

	public Boolean getIsPassword() {
		return isPassword;
	}

	public void setIsPassword(Boolean isPassword) {
		this.isPassword = isPassword;
	}

	public List getCardsRst() {
		return cardsRst;
	}

	public void setCardsRst(List cardsRst) {
		this.cardsRst = cardsRst;
	}

	public Map<String, Object> getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(Map<String, Object> checkResult) {
		this.checkResult = checkResult;
	}

}
