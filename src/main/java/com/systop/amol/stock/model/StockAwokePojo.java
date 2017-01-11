package com.systop.amol.stock.model;

import java.io.Serializable;

/**
 * 库存提醒POJO
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings("serial")
public class StockAwokePojo implements Serializable {

	// '商品ID'
	private Integer productId;
	// '商品编码'
	private String productCode;
	// '仓库名称'
	private String storageName;
	// '商品名称'
	private String productName;
	// '规格型号'
	private String productStardard;
	// '单位'
	private String unitName;
	// '包装单位'
	private String unitPack;
	// '上限'
	private Integer productMaxCount;
	// '下限'
	private Integer productMinCount;
	// 实际库存
	private Integer factCount;

	public StockAwokePojo() {
		super();
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductStardard() {
		return productStardard;
	}

	public void setProductStardard(String productStardard) {
		this.productStardard = productStardard;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitPack() {
		return unitPack;
	}

	public void setUnitPack(String unitPack) {
		this.unitPack = unitPack;
	}

	public Integer getProductMaxCount() {
		return productMaxCount;
	}

	public void setProductMaxCount(Integer maxCount) {
		this.productMaxCount = maxCount;
	}

	public Integer getProductMinCount() {
		return productMinCount;
	}

	public void setProductMinCount(Integer minCount) {
		this.productMinCount = minCount;
	}

	public Integer getFactCount() {
		return factCount;
	}

	public void setFactCount(Integer factCount) {
		this.factCount = factCount;
	}

}
