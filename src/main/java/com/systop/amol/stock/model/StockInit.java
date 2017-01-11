package com.systop.amol.stock.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.storage.model.Storage;
import com.systop.amol.base.units.model.Units;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 库存初期
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "stock_inits")
public class StockInit extends BaseModel {

	private Integer id;

	// 用户对象
	private User user;

	// 商品
	private Products products;

	// 单位
	private Units units;

	// 基本数量
	private Integer count;

	// 单位数量
	private Float ncount;

	// 库存期初商品价钱
	private Double price;

	// 金额
	private Double amount;

	// 仓库
	private Storage storage;

	// 创建日期
	private Date createTime = new Date();
	
	//状态
	//0:该条期初未完成
	//1:该条期初数据已完成
	private String status;
	
	//包装单位，不存在于数据库中
	private String unitPack;
	
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

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id")
	public Units getUnits() {
		return units;
	}

	public void setUnits(Units units) {
		this.units = units;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	@Column(name = "status", columnDefinition = "char(1) default '0'")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Float getNcount() {
		return ncount;
	}

	public void setNcount(Float ncount) {
		this.ncount = ncount;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Column(precision = 10, scale = 2)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "storage_id")
	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Transient
	public String getUnitPack() {
		return unitPack;
	}

	public void setUnitPack(String unitPack) {
		this.unitPack = unitPack;
	}
	
	/**
	 * 返回model生成的inser语句
	 * @return String sql语句
	 */
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO `stock_inits` (`id`, `amount`, `count`, `create_time`, `ncount`, `price`, `product_id`, `storage_id`, `unit_id`, `user_id`, `status`) VALUES(");
		sql.append(id);
		sql.append(", ");
		sql.append(amount);
		sql.append(", '");
		sql.append(count);
		sql.append("', '");
		sql.append(createTime);
		sql.append("', '");
		sql.append(ncount);
		sql.append("', "); 
		sql.append(price);		
		sql.append(", ");
		sql.append(products.getId());
		sql.append(", ");
		sql.append(storage.getId());
		sql.append(", ");
		sql.append(units.getId());		
		sql.append(", ");
		sql.append(user.getId());
		sql.append(", ");
		if(StringUtils.isNotBlank(status)){
			sql.append("'"); 
			sql.append(status);
			sql.append("'"); 
		}else{
			sql.append("default");
		}
		sql.append(")");
		return sql.toString();
	}
}
