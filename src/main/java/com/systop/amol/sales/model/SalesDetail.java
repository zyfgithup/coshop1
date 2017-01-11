package com.systop.amol.sales.model;


import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.units.model.Units;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * 销售详细信息，记录了销售的商品信息
 * @author 王会璞
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "sales_details")
public class SalesDetail extends BaseModel implements Cloneable{
	/** 主键 */
	private Integer id;
	/**
	 * 销售订单
	 * 销售出库
	 * 销售退货
	 * */
	private Sales sales;
	private String isReturn;
	/** 商品 */
	private Products products;
	/** 商品json串 */
	private String productsJSON;
	
	/**  实际出库价 */
	private Double outPrice;
	
	/** 总金额 */
	private Double amount;

	/** 备注 */
	private String remark;
	
	/** 基本单位数量 */
	private Integer count;
	
	/** 
	 * 当该商品明细代表订单商品明细时，该字段表示：商品的剩余出库数量 
	 * 当该商品明细代表出库单商品明细时，该字段表示：商品的未退货数量
	 * */
	private Integer tnorootl = 0;
	
	/** 
	 * 当该商品明细代表订单商品明细时，该字段表示：商品的已出库数量 
	 * 当该商品明细代表出库单商品明细时，该字段表示：商品的已退货数量
	 * */
	private Integer hanod = 0;
	/** 当该商品明细代表出库单商品明细时，该字段表示：商品的已退款金额 */
	private Double rttao = 0.0d;
	/** 单位数量 */
	private Float ncount;
	/** 单位 */
	private Units units;
	/**
	 * 订单（出库单）的商品明细id
	 * 此字段的作用是在客户购买商品减库存时（退货加库存时），修改订单对应商品明细表中的“剩余出库数量”（“未退货数量”）和“已出库数量”（已退货数量）
	 * 冲红时需要用到，以便使冲红后商品数量还原
	*/
	private SalesDetail OORSalesDetailId;
	/** 用英文状态下的逗号分隔开的商品条形码字符串，该字段不存储到数据库中 */
	private String codes = "";
	/** 县级分销佣金 */
	private Double distributionCommission;
	/** 村级分销商佣金 */
	private Double villageDistributionCommission;
	/** 省提成比例(团购) */
	private Integer royaltyRateProvince;
	/** 县提成比例(团购) */
	private Integer royaltyRateCounty;
	//地址id
	private String addrId;
	//订单编号
	private String saleNo;
	//收货姓名
	private String orderName;
	//产地
	private String address;

	//收货电话
	private String phone;
	//订单商家
	private String merId;
	//订单支付类型
	private String payType;
	//订单日期
	private Date orderTime;

	//配件名称
	private String pjName;
	//产地
	private String growPlace;
	//规格
	private String standard;
	/**
	 * 克隆一个与自己一样的对象
	 */
	@Override
	public Object clone() {
		Object o = null;
		try {
			o = (SalesDetail) super.clone();// Object中的clone()识别出你要复制的是哪一个对象。
		} catch (CloneNotSupportedException e) {
			System.out.println(e.toString());
		}
		return o;
	}
	@Transient
	@JSON(serialize = false)
	public String getCodes() {
		return codes;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "oor_salesDetailId_id")
	@JSON(serialize = false)
	public SalesDetail getOORSalesDetailId() {
		return OORSalesDetailId;
	}

	public void setOORSalesDetailId(SalesDetail oORSalesDetailId) {
		OORSalesDetailId = oORSalesDetailId;
	}
	@JSON(serialize = false)
	public Integer getTnorootl() {
		return tnorootl;
	}

	public void setTnorootl(Integer tnorootl) {
		this.tnorootl = tnorootl;
	}

	@JSON(serialize = false)
	public Integer getHanod() {
		return hanod;
	}

	public void setHanod(Integer hanod) {
		this.hanod = hanod;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id")
	@JSON(serialize = false)
	public Units getUnits() {
		return units;
	}

	public void setUnits(Units units) {
		this.units = units;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "products_id")
	@JSON(serialize = false)
	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	@Column(name = "count_")
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "id", nullable = false)
	@JSON(serialize = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}	
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "sales_id")
	@JSON(serialize = false)
	public Sales getSales() {
		return sales;
	}

	public void setSales(Sales sales) {
		this.sales = sales;
	}

	@Column(name = "remark")
	@Type(type = "text")
	@JSON(serialize = false)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(precision = 10, scale = 2)
	public Double getOutPrice() {
		return outPrice;
	}

	public void setOutPrice(Double outPrice) {
		this.outPrice = outPrice;
	}
	@Column(precision = 10, scale = 2)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@JSON(serialize = false)
	public Float getNcount() {
		return ncount;
	}

	public void setNcount(Float ncount) {
		this.ncount = ncount;
	}
	@Column(precision = 10, scale = 2)
	@JSON(serialize = false)
	public Double getRttao() {
		return rttao;
	}
	public void setRttao(Double rttao) {
		this.rttao = rttao;
	}

	@Column(name="distribution_commission")
	@JSON(serialize = false)
	public Double getDistributionCommission() {
		return distributionCommission;
	}

	public void setDistributionCommission(Double distributionCommission) {
		this.distributionCommission = distributionCommission;
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

	public String toInserSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into sales_details(id,sales_id,products_id,outPrice,amount,remark,count_,tnorootl,hanod,rttao,ncount,unit_id,oor_salesDetailId_id) VALUES(");
		sql.append(id);
		sql.append(",");
		sql.append(sales.getId());
		sql.append(",");
		sql.append(products.getId());
		sql.append(",");
		sql.append(outPrice);
		sql.append(",");
		sql.append(sales.getSamount());
		sql.append(",'");
		sql.append(remark);
		sql.append("',");
		sql.append(count);
		sql.append(",");
		sql.append(tnorootl);
		sql.append(",");
		sql.append(hanod);
		sql.append(",");
		sql.append(rttao);
		sql.append(",");
		sql.append(ncount);
		sql.append(",");
		sql.append(units.getId());
		sql.append(",");
		if(OORSalesDetailId != null){
			sql.append(OORSalesDetailId.getId());
		}else{
			sql.append(OORSalesDetailId);
		}
		sql.append(")");
		return sql.toString();
	}

	@Transient
	public String getProductsJSON() {
		return productsJSON;
	}

	public void setProductsJSON(String productsJSON) {
		this.productsJSON = productsJSON;
	}
	@Transient
	@JSON(serialize = false)
	public String getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(String isReturn) {
		this.isReturn = isReturn;
	}
	@Transient
	@JSON(serialize = false)
	public String getAddrId() {
		return addrId;
	}

	public void setAddrId(String addrId) {
		this.addrId = addrId;
	}
	@Transient
	public String getSaleNo() {
		return saleNo;
	}

	public void setSaleNo(String saleNo) {
		this.saleNo = saleNo;
	}
	@Transient
	@JSON(serialize = false)
	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	@Transient
	@JSON(serialize = false)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@Transient
	@JSON(serialize = false)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Transient
	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}
	@Transient
	@JSON(serialize = false)
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	@Transient
	@JSON(serialize = false)
	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	@Column(name="pj_name")
	public String getPjName() {
		return pjName;
	}

	public void setPjName(String pjName) {
		this.pjName = pjName;
	}
	@Column(name="grow_place")
	public String getGrowPlace() {
		return growPlace;
	}

	public void setGrowPlace(String growPlace) {
		this.growPlace = growPlace;
	}
	@Column(name="standard")
	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}
}