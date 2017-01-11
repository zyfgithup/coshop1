package com.systop.amol.purchase.model;

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

import com.systop.amol.base.supplier.model.Supplier;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 应付账款初始化单
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "pay_init")
public class PayInit extends BaseModel {

	private Integer id;

	// 用户对象
	private User user;

	// 供应商
	private Supplier supplier;

	// 应付金额
	private Double amount;

	// 实付金额
	private Double payamount;

	// 日期
	private Date pdate;

	public Date getPdate() {
		return pdate;
	}

	public void setPdate(Date pdate) {
		this.pdate = pdate;
	}
	//状态0正常1封锁
	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id")
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Column(precision = 10, scale = 2)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Column(precision = 10, scale = 2)
	public Double getPayamount() {
		return payamount;
	}

	public void setPayamount(Double payamount) {
		this.payamount = payamount;
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
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into pay_init (id,amount,payamount,pdate,status,supplier_id,user_id) VALUES(");
		sql.append(id);
		sql.append(", ");
		sql.append(amount);
		sql.append(",");
		sql.append(this.payamount);
		sql.append(", '");
		sql.append(this.pdate);
		sql.append("',");
		sql.append(this.status);
		sql.append(",");
		sql.append(this.supplier.getId());		
		sql.append(", ");
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}

}
