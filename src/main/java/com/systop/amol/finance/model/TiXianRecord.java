package com.systop.amol.finance.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;


/**
 * 阿姨提现记录
 */
@Entity
@Table(name = "ti_xian_record")
public class TiXianRecord extends BaseModel implements Serializable{

	private static final long serialVersionUID = 5333534154097707625L;

	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "id", nullable = false)
	private Integer id;
	
	/** 提现用户 商户或者是app用户*/
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "merchant_id")
	private User merchant;
	
	/** 创建时间（阿姨申请提现时间） */
	@Column(name="create_time")
	private Date createTime;
	
	/** 给分销商汇款的员工 */
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name="employee_id")
	private User employee;

	/** 给阿姨汇款的时间 */
	@Column(name="hk_time")
	private Date hkTime;
	
	/** 是否提现成功,true成功，false失败 */
	private Boolean isSuccess = false;
	
	/** 提示信息 */
	@Transient
	private String message;
	
	/** 申请提现，false失败，true成功 */
	@Transient
	private boolean sqtx = false;
	
	/** 提现金额 */
	@Column(name = "ti_xian_money",precision = 10, scale = 2)
	private Double tiXianMoney;
	
	/** 余额 */
	@Column(name = "balance",precision = 10, scale = 2)
	private Double balance;
	
	/** 收入 */
	@Column(name = "income_all",precision = 10, scale = 2)
	private Double incomeAll;

	public boolean isSqtx() {
		return sqtx;
	}

	public void setSqtx(boolean sqtx) {
		this.sqtx = sqtx;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@JSON(serialize = false)
	public User getMerchant() {
		return merchant;
	}

	public void setMerchant(User merchant) {
		this.merchant = merchant;
	}
	@JSON(serialize = false)
	public User getEmployee() {
		return employee;
	}

	public void setEmployee(User employee) {
		this.employee = employee;
	}

	public Date getHkTime() {
		return hkTime;
	}

	public void setHkTime(Date hkTime) {
		this.hkTime = hkTime;
	}

	public Double getTiXianMoney() {
		return tiXianMoney;
	}

	public void setTiXianMoney(Double tiXianMoney) {
		this.tiXianMoney = tiXianMoney;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getIncomeAll() {
		return incomeAll;
	}

	public void setIncomeAll(Double incomeAll) {
		this.incomeAll = incomeAll;
	}
	
}
