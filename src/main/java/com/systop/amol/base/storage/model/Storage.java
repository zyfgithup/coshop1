package com.systop.amol.base.storage.model;

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
import org.hibernate.annotations.Type;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 仓库
 * 
 * @author songbaojie
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "storage")
public class Storage extends BaseModel {

	// 主键
	private Integer id;

	// 仓库名称
	private String name;

	// 创建者
	private User creator;

	// 创建时间
	private Date createTime;

	// 仓库地址
	private String address;

	// 仓库状态（可用1，停用0）
	private String status;

	// 仓库备注描述
	private String descn;

	/**
	 * 默认构造
	 */
	public Storage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 有参构造
	 * 
	 * @param id
	 * @param name
	 * @param creator
	 * @param createTime
	 * @param descn
	 */

	public Storage(Integer id, String name, User creator, Date createTime,
			String address, String status, String descn) {
		super();
		this.id = id;
		this.name = name;
		this.creator = creator;
		this.createTime = createTime;
		this.address = address;
		this.status = status;
		this.descn = descn;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id")
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "descn")
	@Type(type = "text")
	public String getDescn() {
		return descn;
	}

	public void setDescn(String descn) {
		this.descn = descn;
	}
	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "status",columnDefinition="char(1) default '1'")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 返回model生成的inser语句
	 * @return String sql语句
	 */
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO `storage` (`id`, `address`, `create_time`, `descn`, `name`, `status`, `creator_id`) VALUES(");
		sql.append(id);
		sql.append(", '");
		sql.append(address);
		sql.append("', '");
		sql.append(createTime);
		sql.append("', '");
		sql.append(descn);
		sql.append("', '");
		sql.append(name);
		sql.append("', ");
		if(StringUtils.isNotBlank(status)){
			sql.append("'"); 
			sql.append(status);
			sql.append("'"); 
		}else{
			sql.append("default");
		}
		sql.append(", ");
		sql.append(creator.getId());
		sql.append(")");
		return sql.toString();
	}
}
