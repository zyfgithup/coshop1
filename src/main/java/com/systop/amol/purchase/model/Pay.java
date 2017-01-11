package com.systop.amol.purchase.model;

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

import com.systop.amol.base.supplier.model.Supplier;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 应付单
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "pays")
public class Pay extends BaseModel {

	private Integer id;

	// 用户对象
	private User user;

	// 编号
	private String no;

	// 日期
	private Date OutDate;

	// 供应商
	private Supplier supplier;

	// 付款金额
	private Double amount;

	// 关联金额
	private Double linkAmount;

	// 摘要
	private String remark;

	// 员工
	private User employee;

	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	@Column(precision = 10, scale = 2)
	public Double getLinkAmount() {
		return linkAmount;
	}

	public void setLinkAmount(Double linkAmount) {
		this.linkAmount = linkAmount;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id")
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Column(precision = 10, scale = 2)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Date getOutDate() {
		return OutDate;
	}

	public void setOutDate(Date outDate) {
		OutDate = outDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	public User getEmployee() {
		return employee;
	}

	public void setEmployee(User employee) {
		this.employee = employee;
	}
	
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into pays (id,amount,linkamount,no,outDate,remark,status,employee_id,supplier_id,user_id) VALUES(");
		sql.append(id);
		sql.append(", ");
		sql.append(amount);
		sql.append(",");
		sql.append(this.linkAmount);
		sql.append(", '");
		sql.append(no);
		sql.append("','");
		sql.append(this.OutDate);
		sql.append("','");
		if(StringUtils.isNotBlank(remark)){
			sql.append(remark);	
		}else{
			sql.append("");
		}
		sql.append("',");
			sql.append(status);
			sql.append(",");
			if(this.employee != null){
				sql.append(employee.getId());
			}else{
				sql.append("null");
			}
		sql.append(", ");
		sql.append(this.supplier.getId());		
		sql.append(", ");
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}
}
