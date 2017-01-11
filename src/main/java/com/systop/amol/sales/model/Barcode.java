package com.systop.amol.sales.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.systop.core.model.BaseModel;

/**
 * 商品条码
 * @author 王会璞
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "barcodes")
public class Barcode extends BaseModel{

	/** 主键 */
	private Integer id;
	
	/** 商品条码 */
	private String barcode;
	
	/** 销售详细信息 */
	private SalesDetail salesDetail;

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

	@Column(name = "barcode")
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "sales_detail_id")
	public SalesDetail getSalesDetail() {
		return salesDetail;
	}

	public void setSalesDetail(SalesDetail salesDetail) {
		this.salesDetail = salesDetail;
	}
	
	public String toInserSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into barcodes(id,barcode,sales_detail_id) VALUES(");
		sql.append(id);
		sql.append(",");
		sql.append(barcode);
		sql.append(",");
		sql.append(this.salesDetail.getId());
		sql.append(")");
		return sql.toString();
	}
}
