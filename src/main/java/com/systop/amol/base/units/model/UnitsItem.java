package com.systop.amol.base.units.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.systop.amol.base.product.model.Products;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 商品的计量单位换算
 * @author WangHaiYan
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "units_item")
public class UnitsItem extends BaseModel {
	
	private Integer id;

	// 商品id
	private Products products;

	// 换算个数
	private Integer count;

	// 计量单位对象
	private Units units;
	
	// 进货参考价
	private Float inprice;

	// 出库参考价
	private Float outprice;
	
	//折合单位0折合1不折合
	private Integer conversion;

	public Integer getConversion() {
		return conversion;
	}

	public void setConversion(Integer conversion) {
		this.conversion = conversion;
	}

	// 用户对象
	private User user;

	/** 构造方法 */
	public UnitsItem() {
	}

	/** 构造方法 */
	public UnitsItem(Integer id) {
		this.id = id;
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

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "units_id")
	public Units getUnits() {
		return units;
	}

	public void setUnits(Units units) {
		this.units = units;
	}

	@Column(name = "count")
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	@Column(name = "inprice")
	public Float getInprice() {
		return inprice;
	}

	public void setInprice(Float inprice) {
		this.inprice = inprice;
	}

	@Column(name = "outprice")
	public Float getOutprice() {
		return outprice;
	}

	public void setOutprice(Float outprice) {
		this.outprice = outprice;
	}
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into units_item (Id,conversion,count,inprice,outprice,product_id,units_id,user_id) VALUES(");
		sql.append(id);
		sql.append(",");
		sql.append(this.conversion);
		sql.append(",");
		sql.append(this.count);
		sql.append(",");
		sql.append(this.inprice);
		sql.append(",");
		sql.append(this.outprice);
		sql.append(",");
		sql.append(this.products.getId());
		sql.append(",");
		sql.append(this.units.getId());
		sql.append(",");
		sql.append(this.user.getId());
		sql.append(")");
		return sql.toString();
	}
}
