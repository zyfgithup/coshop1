package com.systop.amol.sales.model;

import javax.persistence.Column;

import com.systop.core.model.BaseModel;

/**
 * 销售汇总
 * @author 王会璞
 *
 */
@SuppressWarnings("serial")
public class SalesSummary extends BaseModel {

	//商品id
	private Integer productId;
	//商品名称
	private String productName;
  //商品规格
	private String stardard;
	//商品编码
	private String code;
	//单位名称
	private String unitName;

	//出库数量
	private Integer outCount;

	//出库金额
	private Double outAmount;

	//退货数量
	private Integer returnCount;

	//退货金额
	private Double returnAmount;
	
  //出库数量包装单位
	private String outUnitPack;
	
  //退货数量包装单位
	private String returnUnitPack;
	
  //最终出库总数量包装单位
	private String allUnitPack;
	
	/** 县级分销佣金 */
	private Float distributionCommission;

	/** 村级分销商佣金 */
	private Float villageDistributionCommission;
	
	/** 成本 */
	private Float inprice;
	
	private Integer ktNum;
	
	private Integer belong;
	private boolean type;
	private String imageUrl;
	
	

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getBelong() {
		return belong;
	}

	public void setBelong(Integer belong) {
		this.belong = belong;
	}

	public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public Integer getKtNum() {
		return ktNum;
	}

	public void setKtNum(Integer ktNum) {
		this.ktNum = ktNum;
	}

	public String getAllUnitPack() {
		return allUnitPack;
	}

	public void setAllUnitPack(String allUnitPack) {
		this.allUnitPack = allUnitPack;
	}

	public String getOutUnitPack() {
		return outUnitPack;
	}

	public void setOutUnitPack(String outUnitPack) {
		this.outUnitPack = outUnitPack;
	}

	public String getReturnUnitPack() {
		return returnUnitPack;
	}

	public void setReturnUnitPack(String returnUnitPack) {
		this.returnUnitPack = returnUnitPack;
	}

	public Integer getOutCount() {
		return outCount;
	}

	public void setOutCount(Integer outCount) {
		this.outCount = outCount;
	}

	public Integer getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(Integer returnCount) {
		this.returnCount = returnCount;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getStardard() {
		return stardard;
	}

	public void setStardard(String stardard) {
		this.stardard = stardard;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	@Column(precision = 10, scale = 2)
	public Double getOutAmount() {
		return outAmount;
	}

	public void setOutAmount(Double outAmount) {
		this.outAmount = outAmount;
	}
	@Column(precision = 10, scale = 2)
	public Double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Float getDistributionCommission() {
		return distributionCommission;
	}

	public void setDistributionCommission(Float distributionCommission) {
		this.distributionCommission = distributionCommission;
	}

	public Float getVillageDistributionCommission() {
		return villageDistributionCommission;
	}

	public void setVillageDistributionCommission(
		Float villageDistributionCommission) {
		this.villageDistributionCommission = villageDistributionCommission;
	}

	public Float getInprice() {
		return inprice;
	}

	public void setInprice(Float inprice) {
		this.inprice = inprice;
	}
}
