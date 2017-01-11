package com.systop.amol.base.product.phone;

import com.systop.amol.base.product.ProductConstants;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.product.service.ProductsManager;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * 手机端 Action
 * @author zhuyanfeng
 *
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneProductsAction
	extends DefaultCrudAction<Products, ProductsManager> {

	private static final long serialVersionUID = 6008091643652935277L;

	private List<Products> list;
	/** false普通商品，true团购商品 */
	private String productType;
	/** 商品类别Id */
	private String productSortId;
	/** 商品名称 */
	private String productName;
	
	private JdbcTemplate jdbcTemplate;
	
	/** 分页，页码 */
	private String pageNumber = "1";
	
	/** 每页显示条数 */
	private String pageCount = "10";
	
	/** 销量排序 */
	private String salesVolumeSort;
	/** 价格排序 */
	private String priceSort;
	
	/** 地区id */
	private String regionId;
	private String flag;
	@Autowired
	private RegionManager regionManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private ProductSortManager productSortManager;
	private Products product;
	private List<Map<Object, Object>> userList;

	private Map<String,Object> result = new HashMap<String, Object>();
	/**
	 * 商品详情接口
	 * @return
	 */
	public String goodsDetail() {
		int id=Integer.parseInt(getRequest().getParameter("proId"));
		product=getManager().getProductsById(id);
		product.setMerId(product.getUser().getId());
		/*if(getModel().getProductType()){
			sql.append(" and c.productType = ?");
			args.add(true);
		}*/
		/*if (user.getSuperior() != null) {
			Integer superid = user.getSuperior().getId();
		
			if (superid != null && superid.intValue() > 0 && user.getSuperior().getSuperior() == null) {
				sql.append(" and (c.user.id = ? or c.user.id = ?)");
				args.add(superid);
				args.add(user.getId());
			}else if(superid != null && superid.intValue() > 0 && user.getSuperior().getSuperior() != null){
				sql.append(" and (c.user.id = ? or c.user.id = ? or c.user.id = ?)");
				args.add(user.getSuperior().getSuperior().getId());
				args.add(superid);
				args.add(user.getId());
			}

		} else {
			sql.append(" and c.user.id = ? ");
			args.add(user.getId());
		}*/
		// 判断是否是顶级商品类别，如果不是顶级商品类别则加查询条件，否则查询全部
		return "prodetail";
	}
	
	/**
	 * 手机客户端通过获取商家下的商品列表接口
	 * @return
	 */
	public String goodsIndex() {
		page = PageUtil.getPage(Integer.parseInt(pageNumber),Integer.parseInt(pageCount));
		List args = new ArrayList();
		StringBuffer sql = new StringBuffer(" from Products c where c.visible='1' ");
		String userId=getRequest().getParameter("userId");
		String proSorId=getRequest().getParameter("proSorId");
		if(StringUtils.isNotBlank(proSorId)){
			sql.append(" and c.prosort.id=? ");
			args.add(Integer.parseInt(proSorId));
		}
		if(null!=userId&&!"".equals(userId)){
			sql.append(" and c.user.id=? ");
			args.add(Integer.parseInt(userId));
		}
		sql.append(" and c.prosort.status = ? ");
		args.add("1");
		// 判断是否是顶级商品类别，如果不是顶级商品类别则加查询条件，否则查询全部
		sql.append(" order by c.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		list=page.getData();
		for (Products product : list) {
			product.setMerId(product.getUser().getId());
		}
		return "goodsIndex";
	}
	public String goodsIndex2() {
		String userId=getRequest().getParameter("userId");
		String proSorId=getRequest().getParameter("proSortId");
		List<Products> list1 = getManager().getByType("1",proSorId,userId);
		List<Products> list2 = getManager().getByType("0",proSorId,userId);
		result.put("upProducts",list1);
		result.put("downProducts",list2);
		return "result";
	}
	/**
	 * 商品接口
	 * 请求参数
	 * productType，false普通商品，true团购商品；
	 * productSortId，商品类别Id；
	 * productName，商品名称；
	 * pageNumber，分页，页码；
	 * pageCount，每页显示条数；
	 * salesVolumeSort销量排序，销量升序0，销量降序1；
	 * priceSort，价格排序，价格升序0，价格降序1；
	 * regionId，地区id
	 * 返回数据
	 * json数组，key值对应看Products类
	 */
	@Override
	public String index() {
	page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
	StringBuffer sql = new StringBuffer("from Products s where 1=1 and s.visible='1' and (s.upDownGoodshelf is null or s.upDownGoodshelf='1') ");
	List args = new ArrayList();
	//团购或普通商品
	if(StringUtils.isNotBlank(productType)){
		sql.append(" and s.productType = ?");
		args.add(Boolean.valueOf(productType));
	}
//	if(StringUtils.isNotBlank(regionId)){
//		sql.append(" and (s.user.region.id = ? or s.belonging = ?)");
//		args.add(Integer.valueOf(regionId));
//		args.add(ProductConstants.PLATFORM);
//	}
	System.out.println("productType = "+productType+", regionId="+regionId);
	//regionId = "13172756";//修改此类，团购商品需要传用户地区id(12-10号)
	if(StringUtils.isNotBlank(regionId)){
		Region region = regionManager.get(Integer.valueOf(regionId));
		if(Boolean.valueOf(productType)){
			sql.append("  and s.tgfxs.region.id = ? and s.belonging = ?");
			args.add(region.getParent().getParent().getId());
			args.add(ProductConstants.PLATFORM);
		}else{
			sql.append(" and (s.user.region.id = ? or s.belonging = ?) and s.tgfxs is null");
			args.add(Integer.valueOf(regionId));
			args.add(ProductConstants.PLATFORM);
		}
	}
	if (StringUtils.isNotBlank(flag)) {
		sql.append(" and s.integral is not null ");
	}
	if (StringUtils.isNotBlank(productSortId)) {
		sql.append(" and s.prosort.id = ?");
		args.add(Integer.valueOf(productSortId));
	}
	if (StringUtils.isNotBlank(productName)) {
		sql.append(" and s.name like ?");
		args.add(MatchMode.ANYWHERE.toMatchString(productName));
	}
	
	if(ProductConstants.SALES_VOLUME_ASC.equals(salesVolumeSort)){
		sql.append(" order by s.salesVolume asc");
	}
	if(ProductConstants.SALES_VOLUME_DES.equals(salesVolumeSort)){
		sql.append(" order by s.salesVolume desc");
	}
	if(ProductConstants.PRICE_ASC.equals(priceSort)){
		sql.append(" order by s.presentPrice asc");
	}
	if(ProductConstants.PRICE_DES.equals(priceSort)){
		sql.append(" order by s.presentPrice desc");
	}
	if(StringUtils.isBlank(salesVolumeSort) && StringUtils.isBlank(priceSort)){
		sql.append(" order by s.createTime desc");
	}
	
	System.out.println("phone product --->"+sql);
	
	page = getManager().pageQuery(page, sql.toString(), args.toArray());
	
	list = page.getData();
	for (Products product : list) {
		product.setMerId(product.getUser().getId());
	}
	System.out.println("-=-=-=-=-====================="+product.getMerId());
	System.out.println("------------------page.getData().size() = "+page.getData().size());

	return INDEX;
	}
	public String delProduct(){
		String proId = getRequest().getParameter("proId");
		if(StringUtils.isNotBlank(proId)){
			Products products = getManager().get(Integer.parseInt(proId));
			getManager().remove(products);
		}
		result.put("msg",SUCCESS);
		return "result";
	}
	public String addBxProducts(){
		try{String bxName = getRequest().getParameter("bxName");
			String userId = getRequest().getParameter("userId");
			String content = getRequest().getParameter("content");
			String price = getRequest().getParameter("price");
			String proId = getRequest().getParameter("proId");
			String proSortId = getRequest().getParameter("proSortId");
			Products products = null;
			if(StringUtils.isNotBlank(proId)){
				products = getManager().get(Integer.parseInt(proId));
			}else{
				products = new Products();
			}
			if(StringUtils.isNotBlank(bxName)){
				products.setName(bxName);
			}
			if(StringUtils.isNotBlank(content)){
				products.setRemark(content);
			}
			if(StringUtils.isNotBlank(price)){
				products.setPresentPrice(Float.valueOf(price));
			}
			if(StringUtils.isNotBlank(proId)){
				getManager().update(products);
				result.put("msg","修改成功");
			}else{
				if(StringUtils.isNotBlank(userId)){
					products.setUser(userManager.get(Integer.parseInt(userId)));
				}
				if(StringUtils.isNotBlank(proSortId)){
					products.setProsort(productSortManager.get(Integer.parseInt(proSortId)));
				}
				products.setCreateTime(new Date());
				products.setUpDownGoodshelf("1");
				products.setVisible("1");
				products.setBelonging(1);
				getManager().save(products);
				result.put("msg","提交成功");
			}
		}
		catch (Exception e){
			result.put("msg","success");
			e.printStackTrace();
		}
		return "result";
	}
	public String upOrdownProducts(){
		try{
			String type = getRequest().getParameter("type");
			String proId = getRequest().getParameter("proId");
			String[] proIdArray = proId.split(",");
			List<Products> proList = new ArrayList<Products>();
			if(null!=proIdArray && proIdArray.length>0){
				for (String str : proIdArray){
					Products product = getManager().get(Integer.parseInt(str));
					proList.add(product);
				}
			}
			if(null!=type && type.equals("up")){
				for(Products pro : proList){
					pro.setUpDownGoodshelf("1");
				}
			}
			if(null!=type && type.equals("down")){
				for(Products pro : proList){
					pro.setUpDownGoodshelf("0");
				}
			}
			for (Products products : proList){
				getManager().update(products);
			}
			result.put("msg","success");
		}
		catch (Exception e){
			result.put("msg","error");
			e.printStackTrace();
		}
		return "result";
	}
	public List<Products> getList() {
	return list;
	}

	public void setList(List<Products> list) {
	this.list = list;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductSortId() {
		return productSortId;
	}

	public void setProductSortId(String productSortId) {
		this.productSortId = productSortId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public String getSalesVolumeSort() {
		return salesVolumeSort;
	}

	public void setSalesVolumeSort(String salesVolumeSort) {
		this.salesVolumeSort = salesVolumeSort;
	}

	public String getPriceSort() {
		return priceSort;
	}

	public void setPriceSort(String priceSort) {
		this.priceSort = priceSort;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Products getProduct() {
		return product;
	}
	public void setProduct(Products product) {
		this.product = product;
	}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public List<Map<Object, Object>> getUserList() {
		return userList;
	}
	public void setUserList(List<Map<Object, Object>> userList) {
		this.userList = userList;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
}
