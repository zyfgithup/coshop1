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

import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.units.model.Units;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 库存调拨详单
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "stock_trac_details")
public class StockTracDetail extends BaseModel {

	private Integer id;

	// 库存调拨
	private StockTrac stockTrac;

	// // 出货仓库
	// private Storage outStorage;
	//
	// // 入货仓库
	// private Storage inStorage;
	// 调拨商品
	private Products product;

	// 单位
	private Units units;

	// 调拨基础数量
	private Integer count;

	// 调拨单位数量
	private Float ncount;

	// 库存调拨详单日期时间
	private Date createTime = new Date();

	// 用户对象(录入人)
	private User user;

	// 备注
	private String remark;

	// 标示
	private String sign;

	public StockTracDetail() {
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

	// @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	// @JoinColumn(name = "out_storage_id")
	// public Storage getOutStorage() {
	// return outStorage;
	// }
	//
	// public void setOutStorage(Storage outStorage) {
	// this.outStorage = outStorage;
	// }
	//
	// @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	// @JoinColumn(name = "in_storage_id")
	// public Storage getInStorage() {
	// return inStorage;
	// }
	//
	// public void setInStorage(Storage inStorage) {
	// this.inStorage = inStorage;
	// }

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
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

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_trac_id")
	public StockTrac getStockTrac() {
		return stockTrac;
	}

	public void setStockTrac(StockTrac stockTrac) {
		this.stockTrac = stockTrac;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "sign", columnDefinition = "char(1) default '1'")
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Column(name = "remark", length = 500)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * 返回model生成的inser语句
	 * @return String
	 */
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into stock_trac_details (id,count,create_time,remark,ncount,sign,product_id,stock_trac_id,unit_id,user_id) VALUES(");
		sql.append(id);
		sql.append(", '");
		sql.append(count);
		sql.append("', '");
		sql.append(createTime);
		sql.append("', '");
		if(StringUtils.isNotBlank(remark)){
			sql.append(remark);	
		}else{
			sql.append("");
		}
		sql.append("', '");
		sql.append(ncount);
		sql.append("', "); 
		if(StringUtils.isNotBlank(sign)){
			sql.append("'"); 
			sql.append(sign);
			sql.append("'"); 
		}else{
			sql.append("default");
		}
		sql.append(", ");
		sql.append(product.getId());		
		sql.append(", ");
		sql.append(stockTrac.getId());		
		sql.append(", ");
		sql.append(units.getId());		
		sql.append(", ");
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}
}
