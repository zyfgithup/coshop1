package com.systop.amol.sales.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.systop.amol.base.customer.model.Customer;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.utils.Payment;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 销售回款单详情
 * @author 王会璞
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "receive_details")
public class ReceiveDetail extends BaseModel implements Cloneable{

	/** 主键 */
	private Integer id;
	
	/** 出库单 */
	private Sales sales;
	
	/** 期初应收款单 */
	private ReceiveInit receiveInit;
	
	/** 用户 */
	private User user;
	
  /** 员工 */
	private User employee;
	
	/** 添加时间 */
	private Date createTime = new Date();
	
	/** 客户 */
	private Customer customer;
	
	/** 应收金额 	 */
	private Double samount;
	
	/** 实收金额  */
	private Double spayamount;
	
	/** 退款总金额 */
	private Double rttao;
	
	/** 实退金额【客户的消费方式是预收款时用到】 */
	private Double realReturnMoney;
	
	/** 本次收款金额 */
	private Double TheCollection;

	/** 备注 */
	private String remark;
	
	/** 出库单剩余总数量 */
	private Integer ttno;
	
	/** 出库单中的商品总数量 */
	private Integer count;
	
	/** 回款款单 */
	private Receive receive;
	
	/** 冲红 */
	private Integer redRed = SalesConstants.NORMAL;
	
	/** 收款方式 */
	private Payment payment;
	
	/** 该回款明细是由出库单（应收款单）生成的，还是由（期初应收单）产生的*/
	private String history;
	
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	@Enumerated(EnumType.STRING)
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	
	public Integer getRedRed() {
		return redRed;
	}
	public void setRedRed(Integer redRed) {
		this.redRed = redRed;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "sales_id")
	public Sales getSales() {
		return sales;
	}
	public void setSales(Sales sales) {
		this.sales = sales;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "receive_init_id")
	public ReceiveInit getReceiveInit() {
		return receiveInit;
	}
	public void setReceiveInit(ReceiveInit receiveInit) {
		this.receiveInit = receiveInit;
	}
	/**
	 * 克隆一个与自己一样的对象
	 */
	@Override
	public Object clone() {
		Object o = null;
		try {
			o = (ReceiveDetail) super.clone();// Object中的clone()识别出你要复制的是哪一个对象。
		} catch (CloneNotSupportedException e) {
			System.out.println(e.toString());
		}
		return o;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "receive_id")
	public Receive getReceive() {
		return receive;
	}
	public void setReceive(Receive receive) {
		this.receive = receive;
	}

	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getTtno() {
		return ttno;
	}
	public void setTtno(Integer ttno) {
		this.ttno = ttno;
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
	@JoinColumn(name = "customer_id")
	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public void setId(Integer id) {
		this.id = id;
	}	

	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "id", nullable = false)
	public Integer getId() {
		return id;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "remark", length = 2000)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	@Column(precision = 10, scale = 2)
	public Double getRttao() {
		return rttao;
	}
	public void setRttao(Double rttao) {
		this.rttao = rttao;
	}
	@Column(precision = 10, scale = 2)
	public Double getTheCollection() {
		return TheCollection;
	}
	public void setTheCollection(Double theCollection) {
		TheCollection = theCollection;
	}
	public Double getRealReturnMoney() {
		return realReturnMoney;
	}
	public void setRealReturnMoney(Double realReturnMoney) {
		this.realReturnMoney = realReturnMoney;
	}
	public String toInserSql() {
			StringBuffer sql = new StringBuffer();
			sql.append("insert into receive_details(id,sales_id,receive_init_id,user_id,employee_id,create_time,customer_id,samount,spayamount,rttao,realReturnMoney,TheCollection,remark,ttno,count,receive_id,redRed,payment,history) VALUES(");
			sql.append(id);
			sql.append(",");
			if(sales != null){
				sql.append(sales.getId());
			}else{
				sql.append(sales);
			}
			sql.append(",");
			if(receiveInit != null){
				sql.append(receiveInit.getId());
			}else{
				sql.append(receiveInit);
			}
			sql.append(",");
			sql.append(user.getId());
			sql.append(",");
			if(employee != null){
				sql.append(employee.getId());
			}else{
				sql.append(employee);
			}
			sql.append(",'");
			sql.append(createTime);
			sql.append("',");
			sql.append(customer.getId());
			sql.append(",");
			sql.append(samount);
			sql.append(",");
			sql.append(spayamount);
			sql.append(",");
			sql.append(rttao);
			sql.append(",");
			sql.append(realReturnMoney);
			sql.append(",");
			sql.append(TheCollection);
			sql.append(",");
			sql.append(remark);
			sql.append(",");
			sql.append(ttno);
			sql.append(",");
			sql.append(count);
			sql.append(",");
			sql.append(receive.getId());
			sql.append(",");
			sql.append(redRed);
			sql.append(",");
			if(payment != null){
				sql.append("'"+payment+"'");
			}else{
				sql.append(payment);
			}
			sql.append(",");
			sql.append(history);
			sql.append(")");
			return sql.toString();
	}
}