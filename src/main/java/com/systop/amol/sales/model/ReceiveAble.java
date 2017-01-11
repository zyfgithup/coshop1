package com.systop.amol.sales.model;

import java.io.Serializable;

import javax.persistence.Column;

/**
 * 及时应收
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings("serial")
public class ReceiveAble implements Serializable{
	
	/** 客户 */
	private String customerName;
	
	/** 客户所属地区 */
	private String RegionName;
	
	/** 身份证 */
	private String idCard;
	
	/** 手机号 */
	private String mobile;

	/** 应收 */
	private Double amount;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	@Column(precision = 10, scale = 2)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRegionName() {
		return RegionName;
	}

	public void setRegionName(String regionName) {
		RegionName = regionName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
