package com.systop.amol.user.agent.model;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "merchant_fxgz")
public class Fxgz extends BaseModel implements Cloneable{
	/** 主键 */
	private Integer id;
	//商家对象
	private User user;
	
	private Date createTime;
	//0充值返现 1 分拥返现
	private String type;

	private String fxNum;



	
	private Double startNumber;

	private Double endNumber;

	private Double flMoney;

	private Double integal;

	private Integer hdIntegal;
	
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
	@Column(name = "start_number")
	public Double getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(Double startNumber) {
		this.startNumber = startNumber;
	}
	@Column(name = "end_number")
	public Double getEndNumber() {
		return endNumber;
	}

	public void setEndNumber(Double endNumber) {
		this.endNumber = endNumber;
	}
	@Column(name = "fl_money")
	public Double getFlMoney() {
		return flMoney;
	}

	public void setFlMoney(Double flMoney) {
		this.flMoney = flMoney;
	}
	@Column(name = "integal")

	public Double getIntegal() {
		return integal;
	}

	public void setIntegal(Double integal) {
		this.integal = integal;
	}
	@Column(name = "hd_integal")
	public Integer getHdIntegal() {
		return hdIntegal;
	}

	public void setHdIntegal(Integer hdIntegal) {
		this.hdIntegal = hdIntegal;
	}
	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "fx_num")
	public String getFxNum() {
		return fxNum;
	}

	public void setFxNum(String fxNum) {
		this.fxNum = fxNum;
	}
}
