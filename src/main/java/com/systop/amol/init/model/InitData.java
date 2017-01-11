package com.systop.amol.init.model;

import com.systop.core.model.BaseModel;

/**
 * 销售汇总
 * @author 王会璞
 *
 */
@SuppressWarnings("serial")
public class InitData extends BaseModel {

	//表名
	private String tableName;

	//记录数
	private Integer counts;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getCounts() {
		return counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}

}
