package com.systop.amol.user.agent.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
@SuppressWarnings("serial")
@Entity
@Table(name = "user_partners")
public class Partners extends BaseModel{
	private Integer id;
	private User appUser;
	private User partnerUser;
	private Date relativeTime;
	private Integer bringJf;
	private Double bringMoney;
	//直接间接合伙人标识
	private String inDirectOrDirect;
	
	private String partnerName;
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
	@JoinColumn(name = "app_user_id")
	@JSON(serialize = false)
	public User getAppUser() {
		return appUser;
	}
	public void setAppUser(User appUser) {
		this.appUser = appUser;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "app_partner_id")
	@JSON(serialize = false)
	public User getPartnerUser() {
		return partnerUser;
	}
	public void setPartnerUser(User partnerUser) {
		this.partnerUser = partnerUser;
	}
	@Column(name = "relative_time")
	public Date getRelativeTime() {
		return relativeTime;
	}
	public void setRelativeTime(Date relativeTime) {
		this.relativeTime = relativeTime;
	}
	@Column(name = "bring_jf")
	public Integer getBringJf() {
		return bringJf;
	}
	public void setBringJf(Integer bringJf) {
		this.bringJf = bringJf;
	}
	@Column(name = "bring_money")
	public Double getBringMoney() {
		return bringMoney;
	}
	public void setBringMoney(Double bringMoney) {
		this.bringMoney = bringMoney;
	}
	@Column(name = "indirect_direct")
	public String getInDirectOrDirect() {
		return inDirectOrDirect;
	}
	public void setInDirectOrDirect(String inDirectOrDirect) {
		this.inDirectOrDirect = inDirectOrDirect;
	}
	@Transient
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	
}
