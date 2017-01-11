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

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 充值记录表
 * @author lee
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "card_ups")
public class CardUp extends BaseModel {

	/**主键*/
	private Integer id;
	
	/**发卡表*/
	private CardGrant cardGrant;
	
	/**充值日期*/
	private Date recharge;
	
	/**充值金额*/
	private Double upMoney;
	
	/**充值人*/
	private User user;
	
	/**是否冲红*/
	private String red;
	
	/**备注*/
	private String remark;

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
	@JoinColumn(name = "cardGrant_id")
	public CardGrant getCardGrant() {
		return cardGrant;
	}

	public void setCardGrant(CardGrant cardGrant) {
		this.cardGrant = cardGrant;
	}

	public Date getRecharge() {
		return recharge;
	}

	public void setRecharge(Date recharge) {
		this.recharge = recharge;
	}

	@Column(precision = 10, scale = 2)
	public Double getUpMoney() {
		return upMoney;
	}

	public void setUpMoney(Double upMoney) {
		this.upMoney = upMoney;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRed() {
		return red;
	}

	public void setRed(String red) {
		this.red = red;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into card_ups (id,recharge,red,remark,upMoney,cardGrant_id,user_id) VALUES(");
		sql.append(id);
		sql.append(", '");
		sql.append(recharge);
		sql.append("', '");
		sql.append(red);
		sql.append("', '");
		if(StringUtils.isNotBlank(remark)){
			sql.append(remark);	
		}else{
			sql.append("");
		}
		sql.append("', ");
		sql.append(upMoney);
		sql.append(", ");
		sql.append(cardGrant.getId());
		sql.append(", ");
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}
	
}
