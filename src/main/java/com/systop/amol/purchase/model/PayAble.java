package com.systop.amol.purchase.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.systop.amol.base.supplier.model.Supplier;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 及时应付
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "pay_ables")
public class PayAble extends BaseModel {

	private Integer id;

	// 用户对象
	private User user;

	// 供应商
	private Supplier supplier;

	// 应付
	private Double amount;

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
		sql.append("insert into pay_ables(id,amount,supplier_id,user_id) VALUES(");
		sql.append(id);
		sql.append(", ");
		sql.append(amount);
		sql.append(",");
		sql.append(this.supplier.getId());
		sql.append(",");
		sql.append(this.user.getId());
		sql.append(")");
		return sql.toString();
	}

}
