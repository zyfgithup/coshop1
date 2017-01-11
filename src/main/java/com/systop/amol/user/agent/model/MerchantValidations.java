package com.systop.amol.user.agent.model;

import com.systop.amol.sales.model.Sales;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "merchant_validation")
public class MerchantValidations extends BaseModel implements Cloneable{
	private Integer id;
	//评价人
	private User appUser;
	//被评价的商家
	private User merchantUser;
	private String content;
	private Date createTime;
	private Integer score;
	private String phone;
	private Sales sales;

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
	@JoinColumn(name = "appuser_id")
	@JSON(serialize = false)
	public User getAppUser() {
		return appUser;
	}
	public void setAppUser(User appUser) {
		this.appUser = appUser;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "meruser_id")
	@JSON(serialize = false)
	public User getMerchantUser() {
		return merchantUser;
	}
	public void setMerchantUser(User merchantUser) {
		this.merchantUser = merchantUser;
	}
	@Column(name = "content")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "score")
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	@Transient
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "sales_id")
	@JSON(serialize = false)
	public Sales getSales() {
		return sales;
	}

	public void setSales(Sales sales) {
		this.sales = sales;
	}
}
