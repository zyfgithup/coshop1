package com.systop.amol.base.dept.model;

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
import org.hibernate.annotations.GenericGenerator;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 员工部门
 * 
 * @author songbaojie
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "emp_depts", uniqueConstraints = {})
public class EmpDept extends BaseModel {

	/** id */
	private Integer id;

	/**
	 * 部门名称
	 */
	private String name;

	/**
	 * 创建部门用户
	 */
	private User user;

	/**
	 * 部门编号规则：两位数字，从1自动排；
	 */
	private String serialNo;

	/**
	 * 上级部门
	 */
	private EmpDept parent;

	/**
	 * 下级部门
	 */
	private Set<EmpDept> childs = new HashSet<EmpDept>(0);

	/**
	 * 部门描述
	 */
	private String descn;

	/**
	 * 缺省构造
	 */
	public EmpDept() {
	}

	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "id", nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "descn")
	public String getDescn() {
		return this.descn;
	}

	public void setDescn(String descn) {
		this.descn = descn;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "serial_no")
	public String getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	public EmpDept getParent() {
		return this.parent;
	}

	public void setParent(EmpDept parent) {
		this.parent = parent;
	}

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parent")
	public Set<EmpDept> getChilds() {
		return this.childs;
	}

	public void setChilds(Set<EmpDept> childs) {
		this.childs = childs;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Transient
	public boolean getHasChild() {
		return this.getChilds().size() > 0;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EmpDept)) {
			return false;
		}
		EmpDept castOther = (EmpDept) other;
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

}
