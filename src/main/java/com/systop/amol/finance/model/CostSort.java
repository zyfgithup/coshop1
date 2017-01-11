package com.systop.amol.finance.model;

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 费用类别(包含收入类别和支出类别)
 * 
 * @author songbaojie
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "cost_sorts")
public class CostSort extends BaseModel {

	/**
	 * 主键
	 */
	private Integer id;

	/**
	 * 收支类别名称
	 */
	private String name;

	/**
	 * 上级收支类别
	 */
	private CostSort parentSort;

	/**
	 * 下级收支类别
	 */
	private Set<CostSort> childCostSorts = new HashSet<CostSort>(0);

	/**
	 * 收支类别编号规则：两位数字，从1自动排；
	 */
	private String serialNo;

	/**
	 * 创建者
	 */
	private User user;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 收支类别备注描述
	 */
	private String descn;

	/**
	 * 收支（1收入，2支出）
	 */
	private String type;

	/**
	 * 默认构造
	 */
	public CostSort() {
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

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_sort_id")
	public CostSort getParentSort() {
		return parentSort;
	}

	public void setParentSort(CostSort parentSort) {
		this.parentSort = parentSort;
	}

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parentSort")
	public Set<CostSort> getChildCostSorts() {
		return childCostSorts;
	}

	public void setChildCostSorts(Set<CostSort> childCostSorts) {
		this.childCostSorts = childCostSorts;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "descn")
	public String getDescn() {
		return descn;
	}

	public void setDescn(String descn) {
		this.descn = descn;
	}

	@Column(name = "type", columnDefinition = "char(1) default '1'")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "serial_no")
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	@Transient
	public boolean getHasChild() {
		return this.getChildCostSorts().size() > 0;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CostSort)) {
			return false;
		}
		CostSort castOther = (CostSort) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId())
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}
	
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into cost_sorts (id,create_time,descn,name,serial_no,type,parent_sort_id,user_id) VALUES(");
		sql.append(id);
		sql.append(", '");
		sql.append(createTime);
		sql.append("', '");
		if(StringUtils.isNotBlank(descn)){
			sql.append(descn);	
		}else{
			sql.append("");
		}
		sql.append("', '");
		sql.append(name);
		sql.append("', '");
		sql.append(serialNo);
		sql.append("', '");
		sql.append(type);
		sql.append("', ");
		if(parentSort != null){
			sql.append(parentSort.getId());
		}else{
			sql.append("null");
		}	
		sql.append(", ");
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}
	
}
