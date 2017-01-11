package com.systop.amol.sales.model;

import com.systop.core.model.BaseModel;

@SuppressWarnings("serial")
public class GroupSum extends BaseModel{
//团购总的金额，总的数量，商品名称，规格，单位，县级分销，分销比例
	
	private Double groupMoneySum;
	private Integer groupNumSum;
	private String goodsName;
	private String goodStandard;
	private String goodsUnits;
	private String xjFxName;
	private Integer xjFxBl;
	private Double xjFxMoney;
	private Double adminFxMoney;
	
	
	public Double getAdminFxMoney() {
		return adminFxMoney;
	}
	public void setAdminFxMoney(Double adminFxMoney) {
		this.adminFxMoney = adminFxMoney;
	}
	public Double getXjFxMoney() {
		return xjFxMoney;
	}
	public void setXjFxMoney(Double xjFxMoney) {
		this.xjFxMoney = xjFxMoney;
	}
	public Double getGroupMoneySum() {
		return groupMoneySum;
	}
	public void setGroupMoneySum(Double groupMoneySum) {
		this.groupMoneySum = groupMoneySum;
	}
	public Integer getGroupNumSum() {
		return groupNumSum;
	}
	public void setGroupNumSum(Integer groupNumSum) {
		this.groupNumSum = groupNumSum;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodStandard() {
		return goodStandard;
	}
	public void setGoodStandard(String goodStandard) {
		this.goodStandard = goodStandard;
	}
	public String getGoodsUnits() {
		return goodsUnits;
	}
	public void setGoodsUnits(String goodsUnits) {
		this.goodsUnits = goodsUnits;
	}
	public String getXjFxName() {
		return xjFxName;
	}
	public void setXjFxName(String xjFxName) {
		this.xjFxName = xjFxName;
	}
	public Integer getXjFxBl() {
		return xjFxBl;
	}
	public void setXjFxBl(Integer xjFxBl) {
		this.xjFxBl = xjFxBl;
	}
	
	
}
