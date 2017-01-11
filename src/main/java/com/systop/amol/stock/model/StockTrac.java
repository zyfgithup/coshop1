package com.systop.amol.stock.model;

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

import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.systop.amol.base.storage.model.Storage;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 库存调拨
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "stock_tracs")
public class StockTrac extends BaseModel {

	private Integer id;

	// 库存盘点编号
	private String checkNo;

	// 出货仓库
	private Storage outStorage;

	// 入货仓库
	private Storage inStorage;

	// 状态
	private String status;

	// 库存第一次盘点生成日期时间
	private Date createTime;

	// 用户对象(录入人)
	private User user;

	// 备注
	private String remark;

	//
	private Set<StockTracDetail> stockTracDetails = new HashSet<StockTracDetail>(0);

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

	@Column(name = "status", columnDefinition = "char(1) default '0'")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "no")
	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "out_storage_id")
	public Storage getOutStorage() {
		return outStorage;
	}

	public void setOutStorage(Storage outStorage) {
		this.outStorage = outStorage;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "in_storage_id")
	public Storage getInStorage() {
		return inStorage;
	}

	public void setInStorage(Storage inStorage) {
		this.inStorage = inStorage;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "stockTrac")
	public Set<StockTracDetail> getStockTracDetails() {
		return stockTracDetails;
	}

	public void setStockTracDetails(Set<StockTracDetail> stockTracDetails) {
		this.stockTracDetails = stockTracDetails;
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
	 * @return String sql语句
	 */
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into stock_tracs (id,no,create_time,remark,status,in_storage_id,out_storage_id,user_id) VALUES(");
		sql.append(id);
		sql.append(", '");
		sql.append(checkNo);
		sql.append("', '");
		sql.append(createTime);
		sql.append("', '");
		if(StringUtils.isNotBlank(remark)){
			sql.append(remark);	
		}else{
			sql.append("");
		}
		sql.append("',");
		if(StringUtils.isNotBlank(status)){
			sql.append("'"); 
			sql.append(status);
			sql.append("'"); 
		}else{
			sql.append("default");
		}
		sql.append(", "); 
		sql.append(inStorage.getId());
		sql.append(", ");
		sql.append(outStorage.getId());		
		sql.append(", ");
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}
}
