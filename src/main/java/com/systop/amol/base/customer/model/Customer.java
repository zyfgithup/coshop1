package com.systop.amol.base.customer.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.systop.amol.card.model.CardGrant;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 客户
 * 
 * @author 王会璞
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "customers")
public class Customer extends BaseModel {

	/** 主键 */
	private Integer id;

	/** 客户名称 */
	private String name;

	/** 身份证号 */
	private String idCard;
	
	/** 手机 */
	private String mobile;

	/** 客户电话 */
	private String phone;
	
	/**状态*/
	private String status;

	/** 传真 */
	private String fax;

	/** 电子邮件 */
	private String email;

	/** 地址 */
	private String address;
	
	/** 邮编 */
	private String zip;

	/** 备注 */
	private String remark;

	/** 创建时间 */
	private Date createTime = new Date();

	/** 客户类别 (普通客户0,会员客户1) */
	private String type;

	/** 归属用户(经销商) */
	private User owner;

	/** 对应的登录用户 */
	private User agent;

	/** 所属地区 */
	private Region region;

	/** 代币卡 */
	private Set<CardGrant> cardGrants = new HashSet<CardGrant>();
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

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "id_card", length = 18)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	@Column(name = "mobile", length = 15)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "phone", length = 15)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "zip", length = 6)
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Column(name = "remark", length = 500)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "fax")
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	@Column(name = "type", length = 1 ,columnDefinition = "char(1) default '0'")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id")
	public User getAgent() {
		return agent;
	}

	public void setAgent(User agent) {
		this.agent = agent;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id")
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "customer")
	public Set<CardGrant> getCardGrants() {
		return cardGrants;
	}

	public void setCardGrants(Set<CardGrant> cardGrants) {
		this.cardGrants = cardGrants;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/** @see Object#equals(Object) */
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Customer)) {
			return false;
		}
		Customer castOther = (Customer) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
	}

	/** @see Object#hashCode() */
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/** @see Object#toString() */
	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}
	
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into customers (id,address,create_time,email,fax,id_card,mobile,name,phone,remark,status,type,zip,agent_id,owner_id,region_id) VALUES(");
		sql.append(id);
		sql.append(", '");
		if(StringUtils.isNotBlank(address)){
			sql.append(address);	
		}else{
			sql.append("");
		}
		sql.append("', '");
		sql.append(createTime);
		sql.append("', '");
		if(StringUtils.isNotBlank(email)){
			sql.append(email);	
		}else{
			sql.append("");
		}
		sql.append("', '");
		if(StringUtils.isNotBlank(fax)){
			sql.append(fax);	
		}else{
			sql.append("");
		}
		sql.append("', '");
		sql.append(idCard);
		sql.append("', '");
		if(StringUtils.isNotBlank(mobile)){
			sql.append(mobile);	
		}else{
			sql.append("");
		}
		sql.append("', '");
		sql.append(name);
		sql.append("', '");
		sql.append(phone);
		sql.append("', '");
		if(StringUtils.isNotBlank(remark)){
			sql.append(remark);	
		}else{
			sql.append("");
		}
		sql.append("', '");
		sql.append(status);
		sql.append("', '");
		sql.append(type);
		sql.append("', '");
		if(StringUtils.isNotBlank(zip)){
			sql.append(zip);	
		}else{
			sql.append("");
		}
		sql.append("', ");
		if(agent != null){
			sql.append(agent.getId());
		}else{
			sql.append("null");
		}
		sql.append(", ");
		sql.append(owner.getId());
		sql.append(", ");
		sql.append(region.getId());
		sql.append(")");
		return sql.toString();
	}
	
}