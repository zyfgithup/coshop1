package com.systop.amol.finance.model;

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

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 费用主表(包含费用收入和费用支出)
 * @author lee
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "costs")
public class Cost extends BaseModel {

	/**id主键*/
	private Integer id;
	
	/**单据号*/
	private String receipts;
	
	/**员工*/
	//private Employee employee;
	
	/**操作员*/
	private User user;
	
	/** 类型0支出1收入 */
	private String status;
	
	/**创建日期*/
	private Date createDate;
	
	/**资金类别*/
	private FundsSort fundsSort;
	
	/**支票号*/
	private String checkNo;
	
	/**是否冲红*/
	private String red;
	
	/**来源*/
	private String source;
	
	/**总收入*/
	private Double totalMoney;
	
	/**单据顺序号*/
	private String serialNumber;
	
	/**备注*/
	private String remark;

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

	public String getReceipts() {
		return receipts;
	}

	public void setReceipts(String receipts) {
		this.receipts = receipts;
	}

//	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
//	@JoinColumn(name = "employee_id")
//	public Employee getEmployee() {
//		return employee;
//	}
//
//	public void setEmployee(Employee employee) {
//		this.employee = employee;
//	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "funds_sort_id")
  public FundsSort getFundsSort() {
		return fundsSort;
	}

	public void setFundsSort(FundsSort fundsSort) {
		this.fundsSort = fundsSort;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public String getRed() {
		return red;
	}

	public void setRed(String red) {
		this.red = red;
	}

	@Column(precision = 10, scale = 2)
	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into costs (id,checkNo,createDate,receipts,red,remark,serialNumber,source,status,totalMoney,funds_sort_id,user_id) VALUES(");
		sql.append(id);
		sql.append(", '");
		if(StringUtils.isNotBlank(checkNo)){
			sql.append(checkNo);	
		}else{
			sql.append("");
		}
		sql.append("', '");
		sql.append(createDate);
		sql.append("', '");
		sql.append(receipts);
		sql.append("', '");
		sql.append(red);
		sql.append("', '");
		if(StringUtils.isNotBlank(remark)){
			sql.append(remark);	
		}else{
			sql.append("");
		}
		sql.append("', '");
		sql.append(serialNumber);
		sql.append("', '");
		if(StringUtils.isNotBlank(source)){
			sql.append(source);	
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
		sql.append(totalMoney);
		sql.append(", ");
		sql.append(fundsSort.getId());		
		sql.append(", ");
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}
	
}
