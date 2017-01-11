package com.systop.amol.finance.model;

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

import com.systop.core.model.BaseModel;

/**
 * 费用明细(包含费用收入和费用支出信息)
 * 
 * @author lee
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cost_details")
public class CostDetail extends BaseModel {

	/** 主键 */
	private Integer id;

	/** 收入单 */
	private Cost cost;

	/** 费用类别 */
	private CostSort costSort;

	/** 金额 */
	private Double money;

	/** 摘要 */
	private String note;

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

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "cost_id")
	public Cost getCost() {
		return cost;
	}

	public void setCost(Cost cost) {
		this.cost = cost;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "cost_sort_id")
	public CostSort getCostSort() {
		return costSort;
	}

	public void setCostSort(CostSort costSort) {
		this.costSort = costSort;
	}

	@Column(precision = 10, scale = 2)
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into cost_details (id,money,note,cost_id,cost_sort_id) VALUES(");
		sql.append(id);
		sql.append(", '");
		sql.append(money);
		sql.append("', '");
		if(StringUtils.isNotBlank(note)){
			sql.append(note);	
		}else{
			sql.append("");
		}
		sql.append("', '");
		sql.append(cost.getId());
		sql.append("',");
		sql.append(costSort.getId());
		sql.append(")");
		return sql.toString();
	}
	
}
