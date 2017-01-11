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

import org.hibernate.annotations.GenericGenerator;

import com.systop.amol.base.storage.model.Storage;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 库存盘点
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "stock_checks")
public class StockCheck extends BaseModel {

	private Integer id;

	// 库存盘点编号
	private String checkNo;

	// 库
	private Storage storage;

	// 状态
	// 0未清算可以编辑
	// 1清算状态不可编辑
	// 2录入盘点数据期间(冻结【录入实际库存】按钮)
	private String status;

	// 库存第一次盘点生成日期时间
	private Date createTime;

	// 用户对象(录入人)
	private User user;

	// 员工（盘点人）
	private User employee;

	private Set<StockCheckDetail> stockCheckDetails = new HashSet<StockCheckDetail>(0);

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
	@JoinColumn(name = "storage_id")
	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
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

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "stockCheck")
	public Set<StockCheckDetail> getStockCheckDetails() {
		return stockCheckDetails;
	}

	public void setStockCheckDetails(Set<StockCheckDetail> stockCheckDetails) {
		this.stockCheckDetails = stockCheckDetails;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	public User getEmployee() {
		return employee;
	}

	public void setEmployee(User employee) {
		this.employee = employee;
	}
	
	/**
	 * 返回model生成的inser语句
	 * @return String sql语句
	 */
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO `stock_checks` (`id`, `no`, `create_time`, `status`, `employee_id`, `storage_id`, `user_id`) VALUES(");
		sql.append(id);
		sql.append(", '");
		sql.append(checkNo);
		sql.append("', '");
		sql.append(createTime);
		sql.append("', '");
		sql.append(status);
		sql.append("', ");
		if(employee != null && employee.getId() != null){
			sql.append(employee.getId());
		}else{
			sql.append("NULL");
		}
		sql.append(", "); 
		sql.append(storage.getId());		
		sql.append(", ");
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}
}
