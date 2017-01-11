package com.systop.amol.base.jfproduct.phone;

import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.product.service.ProductsManager;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneJfProductAction extends
DefaultCrudAction<Products, ProductsManager>{
	/** 用户管理 */
	@Autowired
	private UserManager userManager;
	@Autowired
	private ProductsManager productsManager;
	/** 分页，页码 */
	private String pageNumber = "1";
	private Products products;
	/** 每页显示条数 */
	private String pageCount = "10";

	private String name;
	private List<Products> list=new ArrayList<Products>();
	public String indexWz() {
		page = PageUtil.getPage(Integer.parseInt(pageNumber),Integer.parseInt(pageCount));
		List args = new ArrayList();
		String wzType = getRequest().getParameter("wzType");
		StringBuffer sql = new StringBuffer(" from JfPrinceple c  where 1=1  " );
		if(StringUtils.isNotBlank(wzType)){
			sql.append(" and c.wzType=? ");
		}
		args.add(wzType);
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		list = page.getData();
		return INDEX;
	}
	public String wzIndex() {
		page = PageUtil.getPage(Integer.parseInt(pageNumber),Integer.parseInt(pageCount));
		List args = new ArrayList();
		StringBuffer sql = new StringBuffer(" from Products c  where 1=1 and  " );
		if(StringUtils.isNotBlank(name)){
			sql.append(" and c.name like ?  ");
			args.add(MatchMode.ANYWHERE.toMatchString(name));
		}
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		list = page.getData();
		return INDEX;
	}
	/**
	 * 查询
	 */
	public String index() {
		page = PageUtil.getPage(Integer.parseInt(pageNumber),Integer.parseInt(pageCount));
		String regionId=getRequest().getParameter("regionId");
		StringBuffer sql = new StringBuffer(" from Products c where c.integral is not null and c.visible='1' and (s.upDownGoodshelf is null or s.upDownGoodshelf='1') " );
		List args = new ArrayList();
		if(StringUtils.isNotBlank(regionId)){
			sql.append(" and c.region.id=?");
			args.add(Integer.parseInt(regionId));
		}
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		list=page.getData();
		for (Products product : list) {
			product.setMerId(product.getUser().getId());
		}
		return INDEX;
	}
	public String jfProDetal() {
		String proId=getRequest().getParameter("proId");
	    products=productsManager.getProductsById(Integer.parseInt(proId));
	    products.setMerId(products.getUser().getId());
		return "prodetail";
	}
	public UserManager getUserManager() {
		return userManager;
	}
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	public String getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getPageCount() {
		return pageCount;
	}
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	public List<Products> getList() {
		return list;
	}
	public void setList(List<Products> list) {
		this.list = list;
	}
	public Products getProducts() {
		return products;
	}
	public void setProducts(Products products) {
		this.products = products;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
