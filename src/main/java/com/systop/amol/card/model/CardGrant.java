package com.systop.amol.card.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.systop.amol.base.customer.model.Customer;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 
 * 实际生效代币卡[与客户管理]
 * @author lee
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cards_grant")
public class CardGrant extends BaseModel {

	/**主键*/
	private Integer id;
	
	/**发卡者*/
	private User creator;
	
	/**持卡人[客户]*/
	private Customer customer;
	
	/**代币卡*/
	private Card card;
	
	/**存单*/
	private String depositReceipt;
	
	/**开卡日期*/
	private Date createDate;
	
	/**失效日期*/
	private Date endDate;
	
	/**密码*/
	private String password;
	
	/**初始信用额度*/
	private Double credit;
	
	/**消费额*/
	private Double spend;
	
	/**充值总额*/
	private Double upMoney;
	
	/**余额*/
	private Double balance;
	
	/**描述*/
	private String descn;
	
	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "id", nullable = false)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id")
	public User getCreator() {
		return creator;
	}
	
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "card_id")
	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}
	
	public String getDepositReceipt() {
		return depositReceipt;
	}

	public void setDepositReceipt(String depositReceipt) {
		this.depositReceipt = depositReceipt;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(precision = 10, scale = 2)
	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	@Column(precision = 10, scale = 2)
	public Double getSpend() {
		return spend;
	}

	public void setSpend(Double spend) {
		this.spend = spend;
	}

	public String getDescn() {
		return descn;
	}

	public void setDescn(String descn) {
		this.descn = descn;
	}

	@Column(precision = 10, scale = 2)
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Column(precision = 10, scale = 2)
	public Double getUpMoney() {
		return upMoney;
	}

	public void setUpMoney(Double upMoney) {
		this.upMoney = upMoney;
	}
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into cards_grant (id,balance,createDate,credit,depositReceipt,descn,endDate,password,spend,upMoney,card_id,creator_id,customer_id) VALUES(");
		sql.append(id);
		sql.append(", ");
		sql.append(balance);
		sql.append(", '");
		sql.append(createDate);
		sql.append("', ");
		sql.append(credit);
		sql.append(", '");
		sql.append(depositReceipt);
		sql.append("', '");
		if(StringUtils.isNotBlank(descn)){
			sql.append(descn);	
		}else{
			sql.append("");
		}
		sql.append("', '");
		sql.append(endDate);
		sql.append("', '");
		sql.append(password);
		sql.append("', ");
		sql.append(spend);
		sql.append(", ");
		sql.append(upMoney);
		sql.append(", ");
		sql.append(card.getId());
		sql.append(", ");
		sql.append(creator.getId());
		sql.append(", ");
		sql.append(customer.getId());
		sql.append(")");
		return sql.toString();
	}
}
