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

import org.hibernate.annotations.GenericGenerator;

import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.storage.model.Storage;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 即时库存
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "stocks")
public class Stock extends BaseModel {

	private Integer id;

	// 用户对象
	private User user;

	// 商品名称
	private Products products;

	// 剩余数量
	private Integer count;

	// 仓库
	private Storage storage;
	
	// 日期时间
	private Date createTime = new Date();
	
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
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
		sql.append("INSERT INTO `stocks` (`id`, `count`, `create_time`, `product_id`, `storage_id`, `user_id`) VALUES(");
		sql.append(id);
		sql.append(", ");
		sql.append(count);
		sql.append(", '");
		sql.append(createTime);
		sql.append("', ");
		sql.append(products.getId());
		sql.append(", ");
		sql.append(storage.getId());
		sql.append(", "); 
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}
}
