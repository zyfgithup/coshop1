package com.systop.amol.base.product.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import com.google.gson.JsonObject;
import com.systop.amol.base.product.ProductConstants;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.supplier.model.Supplier;
import com.systop.amol.base.units.model.Units;
import com.systop.amol.base.units.model.UnitsItem;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 商品
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "products")
public class Products extends BaseModel implements Cloneable{

	private Integer id;

	// 商品编码
	private String code;

	// 用户对象
	private User user;
	//积分商品所属地区||会员所属地区
	private Region region;

	// 商品名称  用户名
	private String name;

	private Date createTime = new Date();
	
	// 副标题
	private String subtitle;

	// 商品类型
	private Integer belonging = ProductConstants.PLATFORM;
	
	/** false普通商品，true团购商品 */
	private Boolean productType = false;
	
	// 商品条码
	private String barcode;

	// 商品规格
	private String stardard;

	// 商品基本单位
	private Units units;

	// 商品类别||账号类型
	// private Integer productTypeId;
	private ProductSort prosort;
	// 会员类型
	// private Integer productTypeId;
	private ProductSort prosortFroVip;
	// 商品数量
	private Integer maxCount;

	// 报警数量 （下限数量）
	private Integer minCount;

	// 供应商
	private Supplier supplier;

	// 备注
	private String remark;

	
	//商品单位
	private Set<UnitsItem> unitsItem = new HashSet<UnitsItem>(0);
	
	/** 图片地址 */
	private String imageURL;
	
	/** 进价 */
	private Float inprice;

	/** 市场价 */
	private Float originalPrice;
	
	/** 售价 */
	private Float presentPrice;
	
	/** 团购价 */
	private Float groupPurchasePrice;
	
	/** 积分 */
	private Integer integral;
	
	/** 县级分销佣金 */
	private Double distributionCommission;

	/** 村级分销商佣金 */
	private Double villageDistributionCommission;
	
	/** 销量 */
	private Integer salesVolume;
	
	/** 生成此商品的模板商品 */
	private Products superProduct;
	
	/** 省提成比例 */
	private Integer royaltyRateProvince;
	
	/** 县提成比例 */
	private Integer royaltyRateCounty;
	
	/** 团购分销商 */
	private User tgfxs;
	
	// 出库参考价
	private Float outprice;
	// 分销价格 
	private Float distributionPrice;
	
	//1可见，0不可见
	private String visible="1";
	
	private Integer merId;
	/**区分商品和会员1商品 2会员*/
	private String isNormal;
	//0 商品下架 1商品上架
	private String upDownGoodshelf="1";

	/**
	 * 克隆一个与自己一样的对象
	 */
	@Override
	public Object clone() {
		Object o = null;
		try {
			o = (Products) super.clone();// Object中的clone()识别出你要复制的是哪一个对象。
		} catch (CloneNotSupportedException e) {
			System.out.println(e.toString());
		}
		return o;
	}
	
	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "Id", nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 客户端商品json串
	 * @param products 商品
	 * @return
	 */
	public String productsJson(){
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", this.id);
		jsonObject.addProperty("barcode", this.barcode);
		jsonObject.addProperty("belonging", this.belonging);
		jsonObject.addProperty("code", this.code);
//		jsonObject.addProperty("createTime", this.createTime);
		jsonObject.addProperty("imageURL", this.imageURL);
		jsonObject.addProperty("name", this.name);
		jsonObject.addProperty("originalPrice", this.originalPrice);
		jsonObject.addProperty("presentPrice", this.presentPrice);
		jsonObject.addProperty("productType", this.productType);
		jsonObject.addProperty("remark", this.remark);
		jsonObject.addProperty("stardard", this.stardard);
		jsonObject.addProperty("subtitle", this.subtitle);
		jsonObject.addProperty("barcode", this.barcode);
		return jsonObject.toString();
	}
	
	@Column(name = "code", length = 255)
	@JSON(serialize = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JSON(serialize = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "tgfxs_id")
	@JSON(serialize = false)
	public User getTgfxs() {
		return tgfxs;
	}

	public void setTgfxs(User tgfxs) {
		this.tgfxs = tgfxs;
	}

	@Column(name = "name", length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@JSON(serialize = false)
	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	@JSON(serialize = false)
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	@Column(name = "stardard", length = 255)
	public String getStardard() {
		return stardard;
	}
	public void setStardard(String stardard) {
		this.stardard = stardard;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "units_id")
	@JSON(serialize = false)
	public Units getUnits() {
		return units;
	}

	public void setUnits(Units units) {
		this.units = units;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "prosort_id")
	@JSON(serialize = false)
	public ProductSort getProsort() {
		return prosort;
	}

	public void setProsort(ProductSort prosort) {
		this.prosort = prosort;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "prosort_vip_id")
	@JSON(serialize = false)
	public ProductSort getProsortFroVip() {
		return prosortFroVip;
	}

	public void setProsortFroVip(ProductSort prosortFroVip) {
		this.prosortFroVip = prosortFroVip;
	}

	@Column(name = "inprice")
	@JSON(serialize = false)
	public Float getInprice() {
		return inprice;
	}

	public void setInprice(Float inprice) {
		this.inprice = inprice;
	}

	@Column(name = "outprice")
	@JSON(serialize = false)
	public Float getOutprice() {
		return outprice;
	}

	public void setOutprice(Float outprice) {
		this.outprice = outprice;
	}

	@Column(name = "min_count")
	@JSON(serialize = false)
	public Integer getMinCount() {
		return minCount;
	}

	public void setMinCount(Integer minCount) {
		this.minCount = minCount;
	}

	@Column(name = "max_count")
	@JSON(serialize = false)
	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}
	@Column(name = "visible")
	@JSON(serialize = false)
	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id")	
	@JSON(serialize = false)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "super_product_id")	
	@JSON(serialize = false)
	public Products getSuperProduct() {
		return superProduct;
	}

	public void setSuperProduct(Products superProduct) {
		this.superProduct = superProduct;
	}

	@Column(name = "remark", length = 300)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "products")
	@JSON(serialize = false)
	public Set<UnitsItem> getUnitsItem() {
		return unitsItem;
	}

	public void setUnitsItem(Set<UnitsItem> unitsItem) {
		this.unitsItem = unitsItem;
	}
	
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	@Column(name="distribution_price")
	@JSON(serialize = false)
	public Float getDistributionPrice() {
		return distributionPrice;
	}

	public void setDistributionPrice(Float distributionPrice) {
		this.distributionPrice = distributionPrice;
	}

	@Column(name="distribution_commission")
	@JSON(serialize = false)
	public Double getDistributionCommission() {
		return distributionCommission;
	}

	public void setDistributionCommission(Double distributionCommission) {
		this.distributionCommission = distributionCommission;
	}
	@Column(name="original_price")
	@JSON(serialize = false)
	public Float getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Float originalPrice) {
		this.originalPrice = originalPrice;
	}

	@Column(name="present_price")
	public Float getPresentPrice() {
		return presentPrice;
	}

	public void setPresentPrice(Float presentPrice) {
		this.presentPrice = presentPrice;
	}

	@Column(name="village_distribution_commission")
	@JSON(serialize = false)
	public Double getVillageDistributionCommission() {
		return villageDistributionCommission;
	}

	public void setVillageDistributionCommission(
			Double villageDistributionCommission) {
		this.villageDistributionCommission = villageDistributionCommission;
	}
	@Column(name="belonging")
	@JSON(serialize = false)
	public Integer getBelonging() {
		return belonging;
	}

	public void setBelonging(Integer belonging) {
		this.belonging = belonging;
	}

	@Column(name="product_type")
	@JSON(serialize = false)
	public Boolean getProductType() {
		return productType;
	}

	public void setProductType(Boolean productType) {
		this.productType = productType;
	}

	@Column(name="create_time")
	@JSON(serialize = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="group_purchase_price")
	@JSON(serialize = false)
	public Float getGroupPurchasePrice() {
		return groupPurchasePrice;
	}

	public void setGroupPurchasePrice(Float groupPurchasePrice) {
		this.groupPurchasePrice = groupPurchasePrice;
	}
	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	@Column(name="sales_volume")
	public Integer getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(Integer salesVolume) {
		this.salesVolume = salesVolume;
	}

	@Column(name="royalty_rate_province")
	@JSON(serialize = false)
	public Integer getRoyaltyRateProvince() {
		return royaltyRateProvince;
	}

	public void setRoyaltyRateProvince(Integer royaltyRateProvince) {
		this.royaltyRateProvince = royaltyRateProvince;
	}

	@Column(name="royalty_rate_county")
	@JSON(serialize = false)
	public Integer getRoyaltyRateCounty() {
		return royaltyRateCounty;
	}

	public void setRoyaltyRateCounty(Integer royaltyRateCounty) {
		this.royaltyRateCounty = royaltyRateCounty;
	}
	@ManyToOne(cascade = {}, fetch=FetchType.LAZY)
	@JoinColumn(name = "region_id" )
	@JSON(serialize = false)
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
	@Transient
	public Integer getMerId() {
		return merId;
	}
	public void setMerId(Integer merId) {
		this.merId = merId;
	}
	@Column(name="is_normal")
	public String getIsNormal() {
		return isNormal;
	}

	public void setIsNormal(String isNormal) {
		this.isNormal = isNormal;
	}

	@Column(name="up_down_goodshelf")
	public String getUpDownGoodshelf() {
		return upDownGoodshelf;
	}

	public void setUpDownGoodshelf(String upDownGoodshelf) {
		this.upDownGoodshelf = upDownGoodshelf;
	}

	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into products (Id,barcode,code,inprice,max_count,"
				+"min_count,name,outprice,remark,stardard,prosort_id,supplier_id,units_id,user_id) VALUES(");
		sql.append(id);
		sql.append(",'");
		sql.append(this.barcode);
		sql.append("','");
		sql.append(this.code);
		sql.append("',");
		sql.append(this.inprice);
		sql.append(",");
		sql.append(this.maxCount);
		sql.append(",");
		sql.append(this.minCount);
		sql.append(",'");
		sql.append(this.name);
		sql.append("',");
		sql.append(this.outprice);
		sql.append(",'");
		sql.append(this.remark);
		sql.append("','");
		sql.append(this.stardard);
		sql.append("',");
		sql.append(this.prosort.getId());
		sql.append(",");
		sql.append(this.supplier.getId());
		sql.append(",");
		sql.append(this.units.getId());
		sql.append(",");
		sql.append(this.getVisible());
		sql.append(",");
		sql.append(this.user.getId());
		sql.append(")");
		return sql.toString();
	}
}
