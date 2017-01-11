package com.systop.common.modules.region.model;

import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 地区表 
 * @author NiceLunch
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "regions")
public class Region extends BaseModel {
	
	//主键
	private Integer id;
	
	//地区名称
	private String name;
	
	private String code;
	
	//地区类别
	private Short type;
	
	//排序
	private Integer sortId;
	
	//上级地区
	private Region parent;

	private String ckYname;

	private String ckPrice;
	
	//下级地区
	private Set<Region> childs = new HashSet<Region>(0);
	@JSON(serialize = false)
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parent")
	public Set<Region> getChilds() {
		return this.childs;
	}

	public void setChilds(Set<Region> childs) {
		this.childs = childs;
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

	@Column(name = "name", length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "type", length = 1, nullable = true)
	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	@Column(name = "sort_id")
	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}

	@JSON(serialize = false)
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	public Region getParent() {
		return parent;
	}

	public void setParent(Region parent) {
		this.parent = parent;
	}
	@Column(name = "ck_name")
	public String getCkYname() {
		return ckYname;
	}

	public void setCkYname(String ckYname) {
		this.ckYname = ckYname;
	}
	@Column(name = "ck_price")
	public String getCkPrice() {
		return ckPrice;
	}

	public void setCkPrice(String ckPrice) {
		this.ckPrice = ckPrice;
	}
}
