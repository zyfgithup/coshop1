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

import org.hibernate.annotations.GenericGenerator;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

@SuppressWarnings("serial")
@Entity
@Table(name = "jf_dh_record")
public class JfDhRecord extends BaseModel implements Cloneable{
	/** 主键 */
	private Integer id;
	
	private User user;
	
	private Date createTime;
	
	private Integer dhJfNum;
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
	@Column(name = "dh_count")
	public Integer getDhJfNum() {
		return dhJfNum;
	}

	public void setDhJfNum(Integer dhJfNum) {
		this.dhJfNum = dhJfNum;
	}
	
	
}
