package com.systop.amol.base.supplier.model;

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
import org.hibernate.annotations.Type;

import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 供应商
 * @author 王会璞
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "suppliers")
public class Supplier extends BaseModel {

	/** 主键 */
	private Integer id;
	
	/** 创建人 */
	private User user;
	
	/** 地区 */
	private Region region;
	
	/** 创建时间 */
	private Date createTime;
	
	/** 供应商名称 */
	private String name;
	
	private Integer sid;

	/** 手机 */
	private String mobile;
	
	/** 电话 */
	private String phone;
	
	/** 传真 */
	private String fax;
	
	/** 电子邮件 */
	private String email;
	
	/** 供应商企业法人 */
	private String gysfr;
	
	/** 供应商开户行 */
	private String khh;
	
	/**账号*/
	private String zh;
	
	/**状态*/
	private String status;
	
	/** 地址 */
	private String address;
	
	/** 备注 */
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

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "remark")
	@Type(type = "text")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "create_time")
  	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "gysfr")
	public String getGysfr() {
		return gysfr;
	}

	public void setGysfr(String gysfr) {
		this.gysfr = gysfr;
	}
	
	public String getKhh() {
		return khh;
	}

	public void setKhh(String khh) {
		this.khh = khh;
	}

	public String getZh() {
		return zh;
	}

	public void setZh(String zh) {
		this.zh = zh;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id")
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}
	
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into suppliers (id,address,create_time,email,fax,gysfr,mobile,name,phone,remark,sid,status,region_id,user_id,khh,zh) VALUES(");
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
		if(StringUtils.isNotBlank(gysfr)){
			sql.append(gysfr);	
		}else{
			sql.append("");
		}
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
		sql.append("', ");
		if(sid != null){
			sql.append(sid);
		}else{
			sql.append("null");
		}
		sql.append(", '");
		if(StringUtils.isNotBlank(status)){
			sql.append(status);	
		}else{
			sql.append("");
		}
		sql.append("', ");
		sql.append(region.getId());
		sql.append(", ");
		sql.append(user.getId());
		sql.append(", '");
		if(StringUtils.isNotBlank(khh)){
			sql.append(khh);	
		}else{
			sql.append("");
		}
		sql.append("', '");
		if(StringUtils.isNotBlank(zh)){
			sql.append(zh);	
		}else{
			sql.append("");
		}
		sql.append("'");
		sql.append(")");
		return sql.toString();
	}
	
}