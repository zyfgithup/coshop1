package com.systop.amol.base.prosort.phone;

import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.amol.sales.model.ErjiProSortpro;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;

@SuppressWarnings({ "serial"})
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneProSortAction extends
		DefaultCrudAction<ProductSort, ProductSortManager>{

	/** 分页，页码 */
	private String pageNumber = "1";
	private List<ProductSort> list;
	private List listJfProduct=new ArrayList();
	/** 每页显示条数 */
	private String pageCount = "100";
	private JdbcTemplate jdbcTemplate;
	private List<ErjiProSortpro> ListerjiProSortpro=new ArrayList<ErjiProSortpro>();
	public String indexProsort() {
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		StringBuffer sql = new StringBuffer(" from ProductSort c where c.type='2' and c.parentProsort.id is null  ");
		/*if(type.equals("1")){
			sql.append(" and c.showTjg is not null");
		}
		if(type.equals("2")){
			sql.append(" and c.isYbl is null and c.rowNum is not null ");
		}*/
		sql.append(" order by c.rowNum");
		page = getManager().pageQuery(page, sql.toString());
		list=page.getData();
		return INDEX;
	}
	public String index() {
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		StringBuffer sql = new StringBuffer("from ProductSort c where  c.parentProsort.id =46006272  ");
		/*if(type.equals("1")){
			sql.append(" and c.showTjg is not null");
		}
		if(type.equals("2")){
			sql.append(" and c.isYbl is null and c.rowNum is not null ");
		}*/
		sql.append(" order by c.rowNum");
		page = getManager().pageQuery(page, sql.toString());
		list=page.getData();
//		addAllEjFl(list);
		return INDEX;
	}
	//二手车类型json数据返回
	public String escSortindex() {
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		StringBuffer sql = new StringBuffer("from ProductSort c where  type = '4' ");
		page = getManager().pageQuery(page, sql.toString());
		list=page.getData();
//		addAllEjFl(list);
		return INDEX;
	}
	public String sorIndex() {
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		StringBuffer sql = new StringBuffer("from ProductSort c where c.type='1' and c.status='1' and c.parentProsort.id is null ");
		sql.append(" order by c.rowNum");
		page = getManager().pageQuery(page, sql.toString());
		list=page.getData();
		//addAllEjFl(list);
		return INDEX;
	}
	public List<ProductSort> addAllEjFl(List<ProductSort> list){
		ProductSort productSort=null;
		List<ProductSort> tempList = null;
		for (ProductSort proSort : list){
			tempList = new ArrayList<ProductSort>();
			productSort = new ProductSort();
			productSort.setId(null);
			productSort.setName("全部");
			productSort.setParentProsort(proSort);
			tempList.add(productSort);
			List<ProductSort> childList=proSort.getChildProductSorts();
			tempList.addAll(childList);
			proSort.setChildProductSorts(null);
			proSort.setChildProductSorts(tempList);
		}
		return list;
	}
	//根据商品一级类别查询二级类别以及对应的积分商品
	/**
	 public String getProductsByParentId(){

	 Integer parentId=Integer.parseInt(getRequest().getParameter("parentId"));

	 List<ProductSort> list=getManager().getProsortsByProsortId(parentId);

	 for (ProductSort ps : list) {
	 ErjiProSortpro erjiProSortpro=new ErjiProSortpro();
	 erjiProSortpro.setProSortId(ps.getId());
	 //List<Products> list=

	 }
	 //ListerjiProSortpro.add(arg0);

	 }
	 */
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

	public List<ProductSort> getList() {
		return list;
	}

	public void setList(List<ProductSort> list) {
		this.list = list;
	}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public List getListJfProduct() {
		return listJfProduct;
	}
	public void setListJfProduct(List listJfProduct) {
		this.listJfProduct = listJfProduct;
	}
	public List<ErjiProSortpro> getListerjiProSortpro() {
		return ListerjiProSortpro;
	}
	public void setListerjiProSortpro(List<ErjiProSortpro> listerjiProSortpro) {
		ListerjiProSortpro = listerjiProSortpro;
	}


}
