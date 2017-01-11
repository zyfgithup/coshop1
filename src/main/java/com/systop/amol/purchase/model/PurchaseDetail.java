package com.systop.amol.purchase.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.units.model.Units;
import com.systop.core.model.BaseModel;

/**
 * 入库明细
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "purchase_details")
public class PurchaseDetail extends BaseModel {

	private Integer id;

	// 关联主表
	private Purchase purchase;

	// 商品
	private Products products;

	// 基本单位数量
	private Integer count;

	// 单位数量
	private Float ncount;

	// 入库单关联数量
	private Integer linkCount;

	// 单位
	private Units units;

	// 单价
	private Float price;

	// 金额
	private Double amount;

	// 备注
	private String remark;
	//包装数量
	private String bagCount;
    @Transactional
	public String getBagCount() {
		return bagCount;
	}

	public void setBagCount(String bagCount) {
		this.bagCount = bagCount;
	}

	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "id", nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLinkCount() {
		return linkCount;
	}

	public void setLinkCount(Integer linkCount) {
		this.linkCount = linkCount;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_id")
	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	public Products getProducts() {
		return products;
	}

	public Float getNcount() {
		return ncount;
	}

	public void setNcount(Float ncount) {
		this.ncount = ncount;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id")
	public Units getUnits() {
		return units;
	}

	public void setUnits(Units units) {
		this.units = units;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	@Column(precision = 10, scale = 2)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into purchase_details (id,amount,bagCount,count,linkCount,ncount,price,remark,product_id,purchase_id,unit_id)  VALUES(");
		sql.append(id);
		sql.append(", ");
		sql.append(this.amount);
		sql.append(",");
		if(this.bagCount==null){
			sql.append("null");
		}else{
		sql.append("'"+this.bagCount+"'");
		}
		sql.append(",");
		sql.append(this.count);
		sql.append(",");
		sql.append(this.linkCount);
		sql.append(",");
		sql.append(this.ncount);
		sql.append(",");
		sql.append(this.price);
		sql.append(",'");
		sql.append(this.remark);
		sql.append("',");
		sql.append(this.products.getId());
		sql.append(",");
		sql.append(this.purchase.getId());
		sql.append(",");
		sql.append(this.units.getId());
		sql.append(")");
		return sql.toString();
	}
}
