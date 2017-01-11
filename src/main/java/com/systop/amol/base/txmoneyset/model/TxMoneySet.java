package com.systop.amol.base.txmoneyset.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.systop.core.model.BaseModel;

@SuppressWarnings("serial")
@Entity
@Table(name = "tx_money_set")
public class TxMoneySet extends BaseModel{
	
	private Integer id;
	private Double money;
	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "Id", nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "money",precision = 10, scale = 2)
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into tx_money_set (Id,money) values (");
		sql.append(id );
		sql.append(",'");
		sql.append(this.money);
		sql.append(")");
		return sql.toString();
	}
	
}
