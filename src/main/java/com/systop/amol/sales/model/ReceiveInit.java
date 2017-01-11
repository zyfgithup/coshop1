package com.systop.amol.sales.model;

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

import com.systop.amol.base.customer.model.Customer;
import com.systop.amol.sales.SalesConstants;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 应收账款初始化单
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "receive_init")
public class ReceiveInit extends BaseModel {

	private Integer id;

	/** 用户对象 */
	private User user;

	/** 客户*/
	private Customer customer;
	
	/** 应收金额*/
	private Double amount;
	
	/** 已收金额*/
	private Double amountReceived = 0.0d;
	
	/** 日期*/
	private Date createTime = new Date();

  /**状态0正常1封锁**/
	private Integer status = SalesConstants.INIT_NORMAL;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	@Column(precision = 10, scale = 2)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	@Column(precision = 10, scale = 2)
	public Double getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(Double amountReceived) {
		this.amountReceived = amountReceived;
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
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String toInserSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into receive_init(id,user_id,customer_id,amount,amountReceived,createTime,status) VALUES(");
		sql.append(id);
		sql.append(",");
		sql.append(user.getId());
		sql.append(",");
		sql.append(customer.getId());
		sql.append(",");
		sql.append(amount);
		sql.append(",");
		sql.append(amountReceived);
		sql.append(",'");
		sql.append(createTime);
		sql.append("',");
		sql.append(status);
		sql.append(")");
		return sql.toString();
	}
}