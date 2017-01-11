package com.systop.amol.user.agent.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.units.model.Units;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 分销商代理商品明细
 * @author 王会璞
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "agent_product_details")
public class AgentProductDetail extends BaseModel implements Cloneable{

	/** 主键 */
	private Integer id;
	
	/**
	 * 分销商
	*/
	private User agent;
	
	/** 商品 */
	private Products products;
	
	/** 分销价格 */
	private Double distributionPrice;
	
	/** 分销佣金 */
	private Double distributionCommission;

	/** 备注 */
	private String remark;
	
	/** 基本单位数量 */
	private Integer count;

	/** 单位数量 */
	private Float ncount;
	
	/** 单位 */
	private Units units;
	
	/**
	 * 克隆一个与自己一样的对象
	 */
	@Override
	public Object clone() {
		Object o = null;
		try {
			o = (AgentProductDetail) super.clone();// Object中的clone()识别出你要复制的是哪一个对象。
		} catch (CloneNotSupportedException e) {
			System.out.println(e.toString());
		}
		return o;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id")
	public Units getUnits() {
		return units;
	}

	public void setUnits(Units units) {
		this.units = units;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "products_id")
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
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}	

	@Column(name = "remark")
	@Type(type = "text")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Float getNcount() {
		return ncount;
	}

	public void setNcount(Float ncount) {
		this.ncount = ncount;
	}

	public User getAgent() {
		return agent;
	}

	public void setAgent(User agent) {
		this.agent = agent;
	}

	@Column(name="distribution_price")
	public Double getDistributionPrice() {
		return distributionPrice;
	}

	public void setDistributionPrice(Double distributionPrice) {
		this.distributionPrice = distributionPrice;
	}

	@Column(name="distribution_commission")
	public Double getDistributionCommission() {
		return distributionCommission;
	}

	public void setDistributionCommission(Double distributionCommission) {
		this.distributionCommission = distributionCommission;
	}

	public String toInserSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into agent_product_details(id,sales_id,products_id,outPrice,amount,remark,count_,tnorootl,hanod,rttao,ncount,unit_id,oor_salesDetailId_id) VALUES(");
		sql.append(id);
		sql.append(",");
		sql.append(agent.getId());
		sql.append(",");
		sql.append(products.getId());
		sql.append(",");
		sql.append(remark);
		sql.append("',");
		sql.append(count);
		sql.append(",");
		sql.append(ncount);
		sql.append(",");
		sql.append(units.getId());
		sql.append(")");
		return sql.toString();
	}
}