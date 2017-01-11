package com.systop.amol.purchase.model;

import javax.persistence.Column;

import com.systop.core.model.BaseModel;

/**
 * 应付统计
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings("serial")
public class PayTotal extends BaseModel {

	// 单据id
	private Integer id;

	// 单据类型
	private String billType;

	// 单据编号
	private String billNo;

	// 日期
	private String date;

	// 供应商
	private String supplier;

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	// 应付
	private Double pay;

	// 实付
	private Double nowpay;

	@Column(precision = 10, scale = 2)
	public Double getBcpay() {
		return bcpay;
	}

	public void setBcpay(Double bcpay) {
		this.bcpay = bcpay;
	}

	// 本次付款
	private Double bcpay;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Column(precision = 10, scale = 2)
	public Double getPay() {
		return pay;
	}

	public void setPay(Double pay) {
		this.pay = pay;
	}

	@Column(precision = 10, scale = 2)
	public Double getNowpay() {
		return nowpay;
	}

	public void setNowpay(Double nowpay) {
		this.nowpay = nowpay;
	}

}
