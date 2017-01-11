package com.systop.amol.sales.model;

import com.systop.core.model.BaseModel;

@SuppressWarnings("serial")
public class SumOfUserPayment extends BaseModel{
	
	private Double sumMoney;
	private String sName;
	private String xName;
	private String cName;
	private String loginId;
	private String name;
	private String cdName;
	private Double fxMoney;
	
	
	public Double getFxMoney() {
		return fxMoney;
	}
	public void setFxMoney(Double fxMoney) {
		this.fxMoney = fxMoney;
	}
	public String getCdName() {
		return cdName;
	}
	public void setCdName(String cdName) {
		this.cdName = cdName;
	}
	public Double getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(Double sumMoney) {
		this.sumMoney = sumMoney;
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public String getxName() {
		return xName;
	}
	public void setxName(String xName) {
		this.xName = xName;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public SumOfUserPayment(Double sumMoney, String sName, String xName,
			String cName, String loginId, String name, String cdName,
			Double fxMoney) {
		super();
		this.sumMoney = sumMoney;
		this.sName = sName;
		this.xName = xName;
		this.cName = cName;
		this.loginId = loginId;
		this.name = name;
		this.cdName = cdName;
		this.fxMoney = fxMoney;
	}
	public SumOfUserPayment() {
		// TODO Auto-generated constructor stub
	}

}
