package com.systop.amol.sales.model;

import java.util.ArrayList;
import java.util.List;

import com.systop.amol.base.product.model.Products;

public class ErjiProSortpro {
	
	private Integer proSortId;
	private String proSortName;
	private List<Products> list=new ArrayList<Products>();
	public Integer getProSortId() {
		return proSortId;
	}
	public void setProSortId(Integer proSortId) {
		this.proSortId = proSortId;
	}
	public String getProSortName() {
		return proSortName;
	}
	public void setProSortName(String proSortName) {
		this.proSortName = proSortName;
	}
	public List<Products> getList() {
		return list;
	}
	public void setList(List<Products> list) {
		this.list = list;
	}

}
