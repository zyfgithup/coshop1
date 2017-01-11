package com.systop.amol.finance.model;

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

import org.hibernate.annotations.GenericGenerator;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 资金类别
 * @author lee
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "funds_sorts")
public class FundsSort extends BaseModel {

	/**主键*/
	private Integer id;
	
	/**名称*/
	private String name;
	
	/**
	 * 创建者
	 */
	private User user;

	/**费用**/
	private Set<Cost> costs = new HashSet<Cost>(0);
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "fundsSort")
	public Set<Cost> getCosts() {
		return costs;
	}

	public void setCosts(Set<Cost> costs) {
		this.costs = costs;
	}
	
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into funds_sorts (id,name,user_id) VALUES(");
		sql.append(id);
		sql.append(", '");
		sql.append(name);
		sql.append("', ");
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}
}
