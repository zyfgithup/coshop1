package com.systop.amol.base.units.model;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 商品的计量单位
 * @author WangHaiYan
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "units")
public class Units extends BaseModel{
	
	private Integer id;
 
	// 用户对象
	private User user;
	
	//单位名称
	private String name;
	
	private Set<UnitsItem> unitsItem = new HashSet<UnitsItem>(0);

	public Units() {
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
	@Column(name = "name", length = 255)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "units")
	public Set<UnitsItem> getUnitsItem() {
		return unitsItem;
	}
	public void setUnitsItem(Set<UnitsItem> unitsItem) {
		this.unitsItem = unitsItem;
	}
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into units (Id,name,user_id) VALUES(");
		sql.append(id);
		sql.append(",'");
		sql.append(this.name);
		sql.append("',");
		sql.append(this.user.getId());
		sql.append(")");
		return sql.toString();
	}

}
