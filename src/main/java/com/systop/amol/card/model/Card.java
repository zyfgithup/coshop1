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
import org.hibernate.annotations.Type;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 银行注册基础信息[卡号]
 * @author lee
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cards")
public class Card extends BaseModel {
	
	/**主键*/
	private Integer id;
	
	/**卡号*/
	private String cardNo;
	
	/**建卡时间*/
	private Date createDate;
	
	/**建卡者*/
	private User creator;
		
	/**卡状态*/
	private String cardState;
	
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

	@Column(name="card_no",length=22)
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id")
	public User getCreator() {
		return creator;
	}
	
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name="card_state")
	public String getCardState() {
		return cardState;
	}

	public void setCardState(String cardState) {
		this.cardState = cardState;
	}

	@Column(name="descn")
	@Type(type = "text")
	public String getDescn() {
		return descn;
	}

	public void setDescn(String descn) {
		this.descn = descn;
	}

	public String toUpdateSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("update cards set card_state = ");
		sql.append("'");
		sql.append(cardState);
		sql.append("' where card_no = ");
		sql.append("'");
		sql.append(cardNo);
		sql.append("'");
		return sql.toString();
	}
	
}
