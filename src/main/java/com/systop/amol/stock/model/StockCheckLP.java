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

import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 库存盘点损益单
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "stock_check_lp")
public class StockCheckLP extends BaseModel {

	private Integer id;

	// 库存盘点损益编号
	private String checkNo;

	// 库存盘点详单
	private StockCheckDetail stockCheckDetail;

	// 生成日期
	private Date createTime = new Date();

	// 盈亏表示0亏 1盈
	private String sign;

	// 生成该单据操作员
	private User user;

	public StockCheckLP() {
		super();
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

	@Column(name = "sign", columnDefinition = "char(1)")
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_check_detail_id")
	public StockCheckDetail getStockCheckDetail() {
		return stockCheckDetail;
	}

	public void setStockCheckDetail(StockCheckDetail stockCheckDetail) {
		this.stockCheckDetail = stockCheckDetail;
	}

	@Column(name = "no")
	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * 返回model生成的inser语句
	 * @return String
	 */
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO `stock_check_lp` (`id`, `no`, `create_time`, `sign`, `stock_check_detail_id`, `user_id`) VALUES(");
		sql.append(id);
		sql.append(", '");
		sql.append(checkNo);
		sql.append("', '");
		sql.append(createTime);
		sql.append("', ");
		if(StringUtils.isNotBlank(sign)){
			sql.append("'"); 
			sql.append(sign);
			sql.append("'"); 
		}else{
			sql.append("default");
		}
		sql.append(", "); 
		sql.append(stockCheckDetail.getId());
		sql.append(", ");
		sql.append(user.getId());
		sql.append(")");
		return sql.toString();
	}
}
