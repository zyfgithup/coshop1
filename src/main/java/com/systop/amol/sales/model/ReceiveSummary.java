package com.systop.amol.sales.model;

import javax.persistence.Column;

/**
 * 回款汇总
 * 
 * @author 王会璞
 * 
 */
public class ReceiveSummary {
	
	/** 客户名称 */
	private String customerName;
	/** 回款汇总金额 */
	private Double money;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	@Column(precision = 10, scale = 2)
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}
}
