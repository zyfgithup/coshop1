package com.systop.amol.sales.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.systop.amol.base.customer.model.Customer;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.utils.Payment;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 销售回款单
 * @author 王会璞
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "receives")
public class Receive extends BaseModel implements Cloneable{

	/** 主键 */
	private Integer id;
	
	/** 回款单号 */
	private String receiveNumber;
	
	/** 用户 */
	private User user;
	
  /** 员工 */
	private User employee;
	
	/** 添加时间 */
	private Date createTime;
	
	/** 客户 */
	private Customer customer;
	
	/** 实收金额 */
	private Double spayamount;
	
	/** 冲红 */
	private Integer redRed = SalesConstants.NORMAL;
	
	/** 备注 */
	private String remark;
	
	/** 原始收款方式（代币卡   现金）两种 */
	private Payment payment;
	
	/**
	 * 克隆一个与自己一样的对象
	 */
	@Override
	public Object clone() {
		Object o = null;
		try {
			o = (Receive) super.clone();// Object中的clone()识别出你要复制的是哪一个对象。
		} catch (CloneNotSupportedException e) {
			System.out.println(e.toString());
		}
		return o;
	}

	@Enumerated(EnumType.STRING)
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public Integer getRedRed() {
		return redRed;
	}
	public void setRedRed(Integer redRed) {
		this.redRed = redRed;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id")
	public User getEmployee() {
		return employee;
	}

	public void setEmployee(User employee) {
		this.employee = employee;
	}

	@Column(precision = 10, scale = 2)
	public Double getSpayamount() {
		return spayamount;
	}

	public void setSpayamount(Double spayamount) {
		this.spayamount = spayamount;
	}

	@Column(name = "receive_number")
	public String getReceiveNumber() {
		return receiveNumber;
	}
	public void setReceiveNumber(String receiveNumber) {
		this.receiveNumber = receiveNumber;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public void setId(Integer id) {
		this.id = id;
	}	

	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "id", nullable = false)
	public Integer getId() {
		return id;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "remark", length = 2000)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String toInserSql() {
			StringBuffer sql = new StringBuffer();
			sql.append("insert into receives(id,receive_number,user_id,employee_id,create_time,customer_id,spayamount,redRed,remark,payment) VALUES(");
			sql.append(id);
			sql.append(",'");
			sql.append(receiveNumber);
			sql.append("',");
			sql.append(user.getId());
			sql.append(",");
			if(employee != null){
				sql.append(employee.getId());
			}else{
				sql.append(employee);
			}
			sql.append(",'");
			sql.append(createTime);
			sql.append("',");
			sql.append(customer.getId());
			sql.append(",");
			sql.append(spayamount);
			sql.append(",");
			sql.append(redRed);
			sql.append(",'");
			sql.append(remark);
			sql.append("',");
			if(payment != null){
				sql.append("'"+payment+"'");
			}else{
				sql.append(payment);
			}
			sql.append(")");
			return sql.toString();
	}
}