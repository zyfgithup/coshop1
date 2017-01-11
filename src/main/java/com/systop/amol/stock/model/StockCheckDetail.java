package com.systop.amol.stock.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 库存盘点详单
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "stock_check_details")
public class StockCheckDetail extends BaseModel {

	private Integer id;

	// 用户对象(录入人)
	private User user;

	// 库存盘点
	private StockCheck stockCheck;

	// 即时库存
	private Stock stock;

	// (原有)库存数量
	private Integer stockCount;

	// 实际数量
	private Integer count;

	// 清查数量
	private Integer checkCount;

	// 库存盘点详单日期时间
	private Date createTime = new Date();

	// 员工（盘点人）
	private User employee;

	// 状态
	// 0未清算可以编辑
	// 1清算状态不可编辑
	// private String status;

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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_id")
	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_check_id")
	public StockCheck getStockCheck() {
		return stockCheck;
	}

	public void setStockCheck(StockCheck stockCheck) {
		this.stockCheck = stockCheck;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "stock_count")
	public Integer getStockCount() {
		return stockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}

	@Column(name = "check_count")
	public Integer getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(Integer checkCount) {
		this.checkCount = checkCount;
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
		sql.append("INSERT INTO `stock_check_details` (`id`, `check_count`, `count`, `create_time`, `stock_count`, `employee_id`, `stock_id`, `stock_check_id`, `user_id`) VALUES(");
		sql.append(id);
		sql.append(", ");
		if(checkCount != null){
			sql.append(checkCount);	
		}else{
			sql.append("NULL");	
		}
		sql.append(", ");
		if(count != null){
			sql.append(count);	
		}else{
			sql.append("NULL");	
		}		
		sql.append(", '");
		sql.append(createTime);
		sql.append("', ");
		sql.append(stockCount);
		sql.append(", ");
		if(employee != null && employee.getId() != null){
			sql.append(employee.getId());
		}else{
			sql.append("NULL");
		}
		sql.append(", "); 
		sql.append(stock.getId());		
		sql.append(", ");
		sql.append(stockCheck.getId());		
		sql.append(", ");
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}
}
