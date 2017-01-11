package com.systop.amol.purchase.model;

import javax.persistence.Column;

import com.systop.amol.base.product.model.Products;
import com.systop.core.model.BaseModel;

/**
 * 采购统计
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings("serial")
public class PurchaseTotal extends BaseModel {

	// 商品
	private Products product;
	// 初始数量
	private Integer initCount;
	//包装数量
	private String binitCount;
	// 初始金额
	private Double initAmount;

	public Integer getInitCount() {
		return initCount;
	}

	public void setInitCount(Integer initCount) {
		this.initCount = initCount;
	}

	@Column(precision = 10, scale = 2)
	public Double getInitAmount() {
		return initAmount;
	}

	public void setInitAmount(Double initAmount) {
		this.initAmount = initAmount;
	}

	// 入库数量
	private Integer inCount;
	//包装数量
	private String binCount;

	// 入库金额
	private Double inAmount;

	// 退货数量
	private Integer returnCount;
	public String getBinitCount() {
		return binitCount;
	}

	public void setBinitCount(String binitCount) {
		this.binitCount = binitCount;
	}

	public String getBinCount() {
		return binCount;
	}

	public void setBinCount(String binCount) {
		this.binCount = binCount;
	}

	public String getBreturnCount() {
		return breturnCount;
	}

	public void setBreturnCount(String breturnCount) {
		this.breturnCount = breturnCount;
	}

	//包装数量
	private String breturnCount;

	// 退货金额
	private Double returnAmount;
	//包装汇总数量
	private String bhzcount;

	public String getBhzcount() {
		return bhzcount;
	}

	public void setBhzcount(String bhzcount) {
		this.bhzcount = bhzcount;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public Integer getInCount() {
		return inCount;
	}

	public void setInCount(Integer inCount) {
		this.inCount = inCount;
	}

	@Column(precision = 10, scale = 2)
	public Double getInAmount() {
		return inAmount;
	}

	public void setInAmount(Double inAmount) {
		this.inAmount = inAmount;
	}

	public Integer getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(Integer returnCount) {
		this.returnCount = returnCount;
	}

	@Column(precision = 10, scale = 2)
	public Double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
	}

}
