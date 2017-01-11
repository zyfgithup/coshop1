package com.systop.amol.sales.model;

import com.systop.core.model.BaseModel;

@SuppressWarnings("serial")
public class JfSummary extends BaseModel {
	
	private Integer count;
	private String name;
	private Integer num;
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	

}
