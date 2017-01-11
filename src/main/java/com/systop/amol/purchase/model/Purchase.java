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

import org.hibernate.annotations.GenericGenerator;

import com.systop.amol.base.storage.model.Storage;
import com.systop.amol.base.supplier.model.Supplier;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 入库表
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "purchases")
public class Purchase extends BaseModel {

	private Integer id;

	// 编号
	private String sno;

	// 用户对象
	private User user;

	// 下单日期
	private Date sdate;
	
	// 要货时间
	private Date getDate;

	public Date getGetDate() {
		return getDate;
	}

	public void setGetDate(Date getDate) {
		this.getDate = getDate;
	}

	// 付款类型：0 预付款，1 全款
	private Integer paytype;

	// 仓库
	private Storage storage;

	// 员工
	private User employee;

	// 供应商
	private Supplier supplier;

	// 总金额
	private Double samount;

	// 本次实收金额
	private Double spayamount;

	// 累计实收金额
	private Double spayTotal;

	// 入库单关联应付款单和退货单的数量
	// 是否订单完成0未完成，1完成，3部分完成
	private Integer isover;

	// 订单id
	private Integer orderid;

	// 订单编号
	private String orderno;

	// 类别（采购订单:0 , 入库单:1, 退货单:2）
	private Integer billType;

	// 备注
	private String remark;

	// 状态0正常1作废
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

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public Date getSdate() {
		return sdate;
	}

	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}

	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
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
	@JoinColumn(name = "employee_id")
	public User getEmployee() {
		return employee;
	}

	public void setEmployee(User employee) {
		this.employee = employee;
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
	public Double getSamount() {
		return samount;
	}

	public void setSamount(Double samount) {
		this.samount = samount;
	}

	@Column(precision = 10, scale = 2)
	public Double getSpayamount() {
		return spayamount;
	}

	public void setSpayamount(Double spayamount) {
		this.spayamount = spayamount;
	}

	public Integer getIsover() {
		return isover;
	}

	public void setIsover(Integer isover) {
		this.isover = isover;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(precision = 10, scale = 2)
	public Double getSpayTotal() {
		return spayTotal;
	}

	public void setSpayTotal(Double spayTotal) {
		this.spayTotal = spayTotal;
	}
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into purchases (id,billType,getDate,isover,orderid,orderno,"
				+"paytype,remark,samount,sdate,sno,spayTotal,spayamount,status,employee_id,storage_id,supplier_id,user_id) VALUES(");
		sql.append(id);
		sql.append(",");
		sql.append(this.billType);
		sql.append(",");
		if(this.getGetDate()==null){
			sql.append("null");
		}else{
		sql.append("'"+this.getGetDate()+"'");
		}
		sql.append(",");
		sql.append(this.isover);
		sql.append(",");
		sql.append(this.orderid);
		sql.append(",");
		if(this.orderno==null){
			sql.append("null");
		}else{
		sql.append("'"+this.orderno+"'");
		}
		sql.append(",");
		sql.append(this.paytype);
		sql.append(",'");
		sql.append(this.remark);
		sql.append("',");
		sql.append(this.samount);
		sql.append(",'");
		sql.append(this.sdate);
		sql.append("','");
		sql.append(this.sno);
		sql.append("',");
		sql.append(this.spayTotal);
		sql.append(",");
		sql.append(this.spayamount);
		sql.append(",");
		sql.append(this.status);
		sql.append(",");
		if(this.employee==null){
			sql.append("null");
		}else{
			sql.append(this.employee.getId());
		}
		sql.append(",");
		if(this.storage==null){
			sql.append("null");
		}else{
			sql.append(this.storage.getId());
		}
		sql.append(",");
		sql.append(this.supplier.getId());
		sql.append(",");
		sql.append(this.user.getId());
		sql.append(")");
		return sql.toString();
	}


}
