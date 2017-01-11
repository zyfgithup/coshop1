package com.systop.amol.base.express.model;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 公司维护
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "company")
public class ExpressCompany extends BaseModel implements Cloneable{


	private Integer id;
	
	/** 快递公司名称 */
	@Column(name = "name", length = 255)
	private String name;
	
	/** 创建者 */

	private User user;
	
	@Column(name = "create_time")
	private Date createTime;
    //纳税人识别编号
	private String saxNo;
	//公司地址
	private String address;
	//公司电话
	private String phone;
	//开户行
	private String bankName;
	//银行卡编号
	private String cardNo;
	//是否为默认公司
	private String ifMrCompany;

	private String visible;

	public ExpressCompany() {
	}
	
	public ExpressCompany(Integer id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JSON(serialize = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	@Column(name="cardNo")
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	@Column(name="saxNo")
	public String getSaxNo() {
		return saxNo;
	}

	public void setSaxNo(String saxNo) {
		this.saxNo = saxNo;
	}
	@Column(name="address")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name="phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column(name="bankName")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	@Column(name="ifMrCompany")
	public String getIfMrCompany() {
		return ifMrCompany;
	}

	public void setIfMrCompany(String ifMrCompany) {
		this.ifMrCompany = ifMrCompany;
	}
	@Column(name="visible")
	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}
}
