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

import org.hibernate.annotations.GenericGenerator;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 代币卡消费明细表
 * @author lee
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "card_spends")
public class CardSpend extends BaseModel {

	//主键
	private Integer id;
	
	//消费日期
	private Date spendDate;
	
	//消费金额
	private Double spendMoney;
	
	//代币卡
	private CardGrant cardGrant;
	
	/** 经销商 */
	private User user;

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
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

	public Date getSpendDate() {
		return spendDate;
	}

	public void setSpendDate(Date srendDate) {
		this.spendDate = srendDate;
	}

	@Column(precision = 10, scale = 2)
	public Double getSpendMoney() {
		return spendMoney;
	}

	public void setSpendMoney(Double spendMoney) {
		this.spendMoney = spendMoney;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "cardGrant_id")
	public CardGrant getCardGrant() {
		return cardGrant;
	}
	
	public void setCardGrant(CardGrant cardGrant) {
		this.cardGrant = cardGrant;
	}

	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into card_spends (id,spendDate,spendMoney,cardGrant_id,user_id) VALUES(");
		sql.append(id);
		sql.append(", '");
		sql.append(spendDate);
		sql.append("', ");
		sql.append(spendMoney);
		sql.append(", ");
		sql.append(cardGrant.getId());
		sql.append(", ");
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}
	
}
