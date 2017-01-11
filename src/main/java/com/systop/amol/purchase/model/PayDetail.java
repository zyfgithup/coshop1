package com.systop.amol.purchase.model;

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

import com.systop.core.model.BaseModel;

/**
 * 应付账款初始化详单
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "pay_details")
public class PayDetail extends BaseModel {

	private Integer id;

	// 付款单主表
	private Pay pay;

	// 应付现金金额
	private Double amount;

	// 对应的入库单
	private Purchase purchase;

	// 当时的已付金额
	private Double payAmount;

	// 对应的期初应收
	private PayInit payInit;

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

	@Column(precision = 10, scale = 2)
	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	@Column(precision = 10, scale = 2)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "pay_id")
	public Pay getPay() {
		return pay;
	}

	public void setPay(Pay pay) {
		this.pay = pay;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_id")
	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "payInit_id")
	public PayInit getPayInit() {
		return payInit;
	}

	public void setPayInit(PayInit payInit) {
		this.payInit = payInit;
	}
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into pay_details (id,amount,payAmount,pay_id,payInit_id,purchase_id) VALUES(");
		sql.append(id);
		sql.append(", ");
		sql.append(amount);
		sql.append(",");
		sql.append(this.payAmount);
		sql.append(", ");
		sql.append(pay.getId());
		sql.append(",");
		if(this.payInit==null){
		sql.append("null");
		}else{
			sql.append(payInit.getId());
		}
		sql.append(",");
		if(this.purchase==null){
			sql.append("null");
			}else{
				sql.append(purchase.getId());
			}
		sql.append(")");
		return sql.toString();
	}

}
