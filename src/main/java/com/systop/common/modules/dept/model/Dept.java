package com.systop.common.modules.dept.model;

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
 * The persistent class for the depts database table.
 * 
 * @author songbaojie
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "depts", uniqueConstraints = {})
public class Dept extends BaseModel {


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
	private Dept parent;

	/**
	 * 下级部门
	 */
	private Set<Dept> childs = new HashSet<Dept>(0);

	/**
	 * 部门类别
	 */
	private String type;
	
	/**
	 * 部门描述
	 */
	private String descn;

	/**
	 * 缺省构造
	 */
	public Dept() {
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
	public Dept getParent() {
		return this.parent;
	}

	public void setParent(Dept parent) {
		this.parent = parent;
	}

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parent")
	public Set<Dept> getChilds() {
		return this.childs;
	}

	public void setChilds(Set<Dept> childs) {
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
		if (!(other instanceof Dept)) {
			return false;
		}
		Dept castOther = (Dept) other;
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
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 返回model生成的inser语句
	 * @return String sql语句
	 */
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO `depts` (`id`, `descn`, `name`, `serial_no`, `type`, `parent_id`, `user_id`) VALUES(");
		sql.append(id);
		sql.append(", '");
		sql.append(descn);
		sql.append("', '");
		sql.append(name);
		sql.append("', '");
		sql.append(serialNo);	
		sql.append("',");
		if(StringUtils.isNotBlank(type)){
			sql.append("'"); 
			sql.append(type);
			sql.append("'"); 
		}else{
			sql.append("NULL");
		}
		sql.append(", "); 
		if(parent != null && parent.getId() != null){
			sql.append("'"); 
			sql.append(parent.getId());
			sql.append("'"); 
		}else{
			sql.append("NULL");
		}
		sql.append(", ");
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}
}
