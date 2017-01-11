package com.systop.amol.base.product.webapp;

import com.systop.amol.base.jfproduct.service.JfPrincepleManager;
import com.systop.amol.base.product.ProductConstants;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.product.service.ProductsManager;
import com.systop.amol.base.product.service.ProductsXlsImportHelperFactory;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.base.units.model.Units;
import com.systop.amol.base.units.service.UnitsManager;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.service.StockManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.util.XlsImportHelper;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
/**
 * 分销商商品管理Action
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductsDistributorAction extends
		DefaultCrudAction<Products,ProductsManager> {

	/**
	 * 计量单位管理
	 */
	@Autowired
	private UnitsManager unitsManager;

	/**
	 * 导入数据文件
	 */
	private File data;

	/**
	 * 导入数据文件名称
	 */
	private String dataFileName;

	/**
	 * 及即时库存管理
	 */
	@Autowired
	private StockManager stockManager;
	@Autowired
	private ProductsManager productsManager;
	/**
	 * 存放导入过程中错误信息
	 */
	private String errorInfo;
	
	@Autowired
	private JfPrincepleManager jfPrincepleManager;
	/**
	 * 经销商管理
	 */
	@Autowired
	private SupplierManager supplierManager;
	
	/** 用户管理 */
	@Autowired
	private UserManager userManager;
	
	@Autowired
	private ProductSortManager productSortManager; 
	
	/** 地区名称 */
	private String regionName;
	
	/** 地区id */
	private Integer regionId;
	
	/** 地区管理 */
	private RegionManager regionManager;
	
	private File attch;

	private String attchFileName;

	private String attchFolder = "/uploadFiles/fileAttch/";
	/**
	 * 计量单位下拉列表
	 */
	public Map getUnitsMap() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			return unitsManager.getUnitsMap(user.getId());
		}
		return null;
	}
	/**
	 * 通过模板添加商品
	 */
	public String templateProductUI(){
		setModel(getManager().get(getModel().getId()));
		return "templateProductUI";
	}
	public String importEdit(){
		return "importEdit";
	}
	/**
	 * 从Excel中导入商品信息到数据库
	 */
	public String importData() {
		User user = UserUtil.getPrincipal(getRequest());
		// 判断上传文件是否存在
		if (StringUtils.isBlank(dataFileName)) {
			errorInfo="请选择要导入的文件!";
			return "importEdit";
		}
		if (StringUtils.isNotBlank(dataFileName)) {
			// 判断后缀是否是Xls文件
			if (!XlsImportHelper.isAllowedXls(dataFileName)) {
				errorInfo="只能上传以”.xls“为后缀的文件!";
				return "importEdit";
			}
		}
		XlsImportHelper xih = ProductsXlsImportHelperFactory.create();
		System.out.println("xih  --------------->>>  "+xih);
		// 读取数据存放在此list中
		List<String> list = new ArrayList<String>();
		InputStream is = null;
		Workbook rwb = null;
		Sheet rs = null;
		try {
			List<Products> productList = new ArrayList<Products>();
			 is = new FileInputStream(data);
			 System.out.println("is  --------------->>>  "+is);
			 rwb = Workbook.getWorkbook(is);
			 System.out.println("rwb  --------------->>>  "+rwb);
			// 获取第一张Sheet表
			 rs = rwb.getSheet(0);
			 System.out.println("rs rows  --------------->>>  "+rs.getRows());
			 //col排  row列
			 Products products=null;
			for (int i = 1; i < rs.getRows(); i++) {
				products=new Products();
				Map<String, String> map = new HashMap<String,String>();
				// 得到导入数据map
				xih.importCommonProperties(map, rs, i);
				System.out.println("map  --------------->>>  "+map);
				String remark=map.get("remark");
				String name=map.get("name");
				String ImageUrl=map.get("ImageUrl");
				String proSortId=map.get("proSortId");
				String guige=map.get("guige");
				String units=map.get("units");
				String scPrice=map.get("scPrice");
				String outPrice=map.get("outPrice");
				String upShelfNum=map.get("upShelfNum");
				Units units2=unitsManager.get(Integer.parseInt(units));
				int j = i + 1 ;
				// 年月为空不进行导入
				if (StringUtils.isBlank(name)) {
					this.addActionMessage("Excel表中第【" + j + "】行商品名称为空，不做导入处理!");
					continue;
				}
				if (StringUtils.isBlank(scPrice)) {
					this.addActionMessage("Excel表中第【" + j + "】市场价为空，不做导入处理!");
					continue;
				}
				if (StringUtils.isBlank(outPrice)) {
					this.addActionMessage("Excel表中第【" + j + "】售价为空，不做导入处理!");
					continue;
				}
				ProductSort productSort=productSortManager.get(Integer.parseInt(proSortId));
				if(null==productSort){
					errorInfo="当前商品类别不存在,请检查"+j+"列";
					return "importEdit";
				}
				products.setProsort(productSort);
				products.setPresentPrice(Float.parseFloat(scPrice));
				products.setOutprice(Float.parseFloat(outPrice));
				products.setMaxCount(Integer.parseInt(upShelfNum));
				products.setUnits(units2);
				products.setName(name);
				products.setUser(user);
				products.setCreateTime(new Date());
				products.setStardard(guige);
				products.setUpDownGoodshelf("1");
				products.setRemark(remark);
				attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
				products.setImageURL(attchFolder+ImageUrl);
				products.setVisible("1");//1积分商品可见，0不可见
				products.setBelonging(1);
				productList.add(products);
			}
			rs=null;
			 rwb=null;
			 is.close();    
		} catch (Exception e) {
			this.addActionMessage(e.getMessage());
			return INPUT;
		}finally {
			if (rwb != null) {
				rwb.close();
			}
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
		}
		if (list.size() > 0) {
			//errorInfo.addAll(list);
		    
		}else{
		this.addActionMessage("导入成功！");
		}
		//List<PayInit> payList =payInitManager.query("from PayInit where user.id=?", getLoginUser().getId());
		//if(payList.size()>0){
	   // this.getRequest().setAttribute("list", payList);
	
		//}
		return SUCCESS;
	}

	
	/**
	 * 通过模板添加商品
	 */
	public String managerTemplateProductUI(){
		
		setModel(getManager().get(getModel().getId()));
		
		return "managerTemplateProductUI";
	}
	/**
	 * 保存通过模板商品添加的商品
	 * @return
	 */
	public String saveTemplateProduct(){
		try {
		Products productDB = getManager().get(getModel().getId());
		Products productsClone = (Products)productDB.clone();
		productsClone.setId(null);
		productsClone.setInprice(getModel().getInprice());//进价
		productsClone.setOriginalPrice(getModel().getOriginalPrice());//市场价
		productsClone.setPresentPrice(getModel().getPresentPrice());//售价
		productsClone.setSuperProduct(productDB);//生成此商品部分数据的模板商品
		productsClone.setSupplier(supplierManager.get(getModel().getSupplier().getId()));//关联供应商
		productsClone.setRemark(getModel().getRemark());//备注
		productsClone.setUser(userManager.get(UserUtil.getPrincipal(getRequest()).getId()));//添加商品的用户
		getManager().save(productsClone);
	} catch (Exception e) {
		System.out.println("clone---------"+e.getMessage());
	}
		return "saveTemplateProduct";
	}
	  public String managerZyedit() {
	    return "managerZyedit";
	  }
	  public String managerSave() {
			Products productsClone = null;
			if(null!=getModel().getId()){
				Integer id=getModel().getId();
				Integer userId=getModel().getUser().getId();
				System.out.println("id = "+id);
				System.out.println(productsManager.get(id));
				getManager().getDao().clear();
				productsClone=(Products)productsManager.get(id).clone();
				System.out.println("productsClone = "+productsClone);
				System.out.println(productsClone.getName());
				productsClone.setVisible("0");
				productsClone.setId(null);
				getModel().setSuperProduct(productsClone);
			//	getManager().getDao().clear();
				//productsManager.save(products);
			}
			User user = UserUtil.getPrincipal(getRequest());
			if(null!=user){
				if (null!=user.getRegion()) {
					getModel().setUser(user);
				}
		}else {
				this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
				return INPUT;
			}
			Assert.notNull(getModel());
//			if (getModel().getSupplier() == null
//					|| getModel().getSupplier().getId() == null) {
//				this.addActionMessage("请选择供应商！");
//				return INPUT;
//			}

			try {
				// getManager().getDao().getHibernateTemplate().clear();
				// getManager().save(getModel());
				ProductSort productSort = null; // 创建商品类型

				if (getModel() != null && getModel().getProsort() != null
						&& getModel().getProsort().getId() != null) {
					productSort = getManager().getDao().get(ProductSort.class,
							getModel().getProsort().getId());
				}
				System.out.println("presentprice--------------"+getModel().getPresentPrice());
				System.out.println("getOriginalPrice--------------"+getModel().getOriginalPrice());
				// 判断商品类型是否存在
				if (productSort == null) {
					this.addActionMessage("当前商品类型不存在，请您重新选择商品类型！");
					return INPUT;
				}
				System.out.println("价格----------------"+getModel().getPresentPrice()+"-=-=-=-="+getModel().getDistributionPrice());
				getModel().setProsort(productSort);
				attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
				if (attch != null) {
					String filePath = UpLoadUtil.doUpload(attch,
							attchFileName, attchFolder, getServletContext());
					getModel().setImageURL(filePath);
				}
				getManager().getDao().getHibernateTemplate().clear();
				getModel().setVisible("1");
				getManager().save(getModel(),productsClone);
				// 判断是否为平台还是分销商自营的商品
				if(null != user.getType() && !AmolUserConstants.EMPLOYEE.equals(user.getType())){
					getModel().setBelonging(ProductConstants.MERCHANT);
				}
				// 同时保存该商品的基本单位设置信息
				getManager().unitsItemSave(getModel().getId());
				return "managerSave";
			} catch (Exception e) {
				addActionError(e.getMessage());
				return INPUT;
			}
		}
	/**
	 * 保存商品
	 */
	@Override
	public String save() {
		Products productsClone = null;
		if(null!=getModel().getId()){
			Integer id=getModel().getId();
			System.out.println("id = "+id);
			System.out.println(productsManager.get(id));
			getManager().getDao().clear();
			productsClone=(Products)productsManager.get(id).clone();
			System.out.println("productsClone = "+productsClone);
			System.out.println(productsClone.getName());
			productsClone.setVisible("0");
			productsClone.setId(null);
			getModel().setSuperProduct(productsClone);
		//	getManager().getDao().clear();
			//productsManamger.save(products);
		}
		User user = UserUtil.getPrincipal(getRequest());
		if(null!=user){
			String userId = getRequest().getParameter("selectUser");
			System.out.println("----------------------selectUser = "+userId);
			if(StringUtils.isNotBlank(userId)){
				User merUser = userManager.get(Integer.parseInt(userId));
				if (null!=merUser) {
					getModel().setUser(merUser);
				}
			}
		}else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		Assert.notNull(getModel());
//		if (getModel().getSupplier() == null
//				|| getModel().getSupplier().getId() == null) {
//			this.addActionMessage("请选择供应商！");
//			return INPUT;
//		}

		try {

			// getManager().getDao().getHibernateTemplate().clear();
			// getManager().save(getModel());
			ProductSort productSort = null; // 创建商品类型

			if (getModel() != null && getModel().getProsort() != null
					&& getModel().getProsort().getId() != null) {
				productSort = getManager().getDao().get(ProductSort.class,
						getModel().getProsort().getId());
			}
			// 判断商品类型是否存在
			if (productSort == null) {
				this.addActionMessage("当前商品类型不存在，请您重新选择商品类型！");
				return INPUT;
			}
			System.out.println("价格----------------"+getModel().getPresentPrice()+"-=-=-=-="+getModel().getDistributionPrice());
			getModel().setProsort(productSort);
			attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
			if (attch != null) {
				String filePath = UpLoadUtil.doUpload(attch,
						attchFileName, attchFolder, getServletContext());
				getModel().setImageURL(filePath);
			}
			getManager().getDao().getHibernateTemplate().clear();
			getModel().setVisible("1");
			getManager().save(getModel(),productsClone);
			
			// 判断是否为平台还是分销商自营的商品
			if(null != user.getType() && !AmolUserConstants.EMPLOYEE.equals(user.getType())){
				getModel().setBelonging(ProductConstants.MERCHANT);
			}
			// 同时保存该商品的基本单位设置信息
			getManager().unitsItemSave(getModel().getId());
			return "adminSuc";
		} catch (Exception e) {
			addActionError(e.getMessage());
			return INPUT;
		}
	}
  public String xjEdit() {
    return "XJiNPUT";
  }
  public String tjEdit() {
	  return "TJiNPUT";
  }
	public String cjzybc() {
		Products productsClone = null;
		if(null!=getModel().getId()){
			Integer id=getModel().getId();
			System.out.println("---------------------------"+getModel().getUser().getId());
			System.out.println("id = "+id);
			System.out.println(productsManager.get(id));
			getManager().getDao().clear();
			productsClone=(Products)productsManager.get(id).clone();
			System.out.println("productsClone = "+productsClone);
			System.out.println(productsClone.getName());
			productsClone.setVisible("0");
			productsClone.setId(null);
			getModel().setSuperProduct(productsClone);
			//getManager().getDao().clear();
			//productsManager.save(products);
		}
		User user = UserUtil.getPrincipal(getRequest());
		if(null!=user){
			getModel().setUser(user);
		}
		else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		Assert.notNull(getModel());
//		if (getModel().getSupplier() == null
//				|| getModel().getSupplier().getId() == null) {
//			this.addActionMessage("请选择供应商！");
//			return INPUT;
//		}

		try {
			// getManager().getDao().getHibernateTemplate().clear();
			// getManager().save(getModel());
			ProductSort productSort = null; // 创建商品类型
			if (getModel() != null && getModel().getProsort() != null
					&& getModel().getProsort().getId() != null) {
				productSort = getManager().getDao().get(ProductSort.class,
						getModel().getProsort().getId());
			}
			// 判断商品类型是否存在
			if (productSort == null) {
				this.addActionMessage("当前商品类型不存在，请您重新选择商品类型！");
				return INPUT;
			}
			getModel().setProsort(productSort);
			attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
			if (attch != null) {
				String filePath = UpLoadUtil.doUpload(attch,
						attchFileName, attchFolder, getServletContext());
				getModel().setImageURL(filePath);
			}
			getManager().getDao().getHibernateTemplate().clear();
			getModel().setVisible("1");
			getModel().setIsNormal(ProductConstants.NORMAL);
			getManager().save(getModel(),productsClone);
			
			// 判断是否为平台还是分销商自营的商品
			if(null != user.getType() && !AmolUserConstants.EMPLOYEE.equals(user.getType())){
				getModel().setBelonging(ProductConstants.MERCHANT);
			}
			// 同时保存该商品的基本单位设置信息
			getManager().unitsItemSave(getModel().getId());
			return "XJSuc";
		} catch (Exception e) {
			addActionError(e.getMessage());
			return INPUT;
		}
	}
	public String tjcjzybc() {
		Products productsClone = null;
		if(null!=getModel().getId()){
			Integer id=getModel().getId();
			System.out.println(productsManager.get(id));
			getManager().getDao().clear();
			productsClone=(Products)productsManager.get(id).clone();
			System.out.println("productsClone = "+productsClone);
			System.out.println(productsClone.getName());
			productsClone.setVisible("0");
			productsClone.setId(null);
			getModel().setSuperProduct(productsClone);
		}
		User user = UserUtil.getPrincipal(getRequest());
		if(null!=user){
			getModel().setUser(user);
		}
		else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		Assert.notNull(getModel());

		try {
			ProductSort productSort = null; // 创建商品类型
			if (getModel() != null && getModel().getProsort() != null
					&& getModel().getProsort().getId() != null) {
				productSort = getManager().getDao().get(ProductSort.class,
						getModel().getProsort().getId());
			}
			// 判断商品类型是否存在
			if (productSort == null) {
				this.addActionMessage("当前商品类型不存在，请您重新选择商品类型！");
				return INPUT;
			}
			getModel().setProsort(productSort);
			attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
			if (attch != null) {
				String filePath = UpLoadUtil.doUpload(attch,
						attchFileName, attchFolder, getServletContext());
				getModel().setImageURL(filePath);
			}
			getManager().getDao().getHibernateTemplate().clear();
			getModel().setVisible("1");
			getModel().setIsNormal(ProductConstants.SPECIAL);
			getManager().save(getModel(),productsClone);
			
			// 判断是否为平台还是分销商自营的商品
			if(null != user.getType() && !AmolUserConstants.EMPLOYEE.equals(user.getType())){
				getModel().setBelonging(ProductConstants.MERCHANT);
			}
			// 同时保存该商品的基本单位设置信息
			getManager().unitsItemSave(getModel().getId());
			return "tjXJSuc";
		} catch (Exception e) {
			addActionError(e.getMessage());
			return INPUT;
		}
	}
	/**
	 * 查询自营商品
	 */
	public String myIndex() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		User user= userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		List args = new ArrayList();
		StringBuffer sql = new StringBuffer("from Products c where c.visible='1'and (c.isNormal='0' or c.isNormal is null) and c.belonging=1 and c.integral is null ");
		if(null!=user){
			sql.append(" and c.user.id=?");
			args.add(user.getId());
		}
		if(getModel().getProductType()){
			sql.append(" and c.productType = ?");
			args.add(true);
		}
		if (getModel().getName() != null
				&& !getModel().getName().trim().equals("")) {
			sql.append(" and c.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (getModel().getCode() != null
				&& !getModel().getCode().trim().equals("")) {
			sql.append(" and c.code like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCode()));
		}
		sql.append(" and c.prosort.status = ?");
		args.add("1");
		// 判断是否是顶级商品类别，如果不是顶级商品类别则加查询条件，否则查询全部
		if (getModel() != null && getModel().getProsort() != null
				&& getModel().getProsort().getId() != null) {
			ProductSort productSort = getManager().getDao().get(
					ProductSort.class, getModel().getProsort().getId());
			if (productSort != null) {
				sql.append("and c.prosort.serialNo like ? ");
				args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
			}
			// 为了返回查询中的条件数据需要将ProductSort存入到MODEL中
			getModel().setProsort(productSort);
		}
		sql.append(" order by c.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return "myIndex";
	}
	/**
	 * 查询特价商品
	 * 
	 * **/
	public String myTjIndex() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		User user= userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		List args = new ArrayList();
		StringBuffer sql = new StringBuffer("from Products c where c.visible='1' and c.isNormal='1' and c.belonging=1 and c.integral is null ");
		if(null!=user){
			sql.append(" and c.user.id=?");
			args.add(user.getId());
		}
		if(getModel().getProductType()){
			sql.append(" and c.productType = ?");
			args.add(true);
		}
		if (getModel().getName() != null
				&& !getModel().getName().trim().equals("")) {
			sql.append(" and c.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (getModel().getCode() != null
				&& !getModel().getCode().trim().equals("")) {
			sql.append(" and c.code like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCode()));
		}
		sql.append(" and c.prosort.status = ?");
		args.add("1");
		// 判断是否是顶级商品类别，如果不是顶级商品类别则加查询条件，否则查询全部
		if (getModel() != null && getModel().getProsort() != null
				&& getModel().getProsort().getId() != null) {
			ProductSort productSort = getManager().getDao().get(
					ProductSort.class, getModel().getProsort().getId());
			if (productSort != null) {
				sql.append("and c.prosort.serialNo like ? ");
				args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
			}
			// 为了返回查询中的条件数据需要将ProductSort存入到MODEL中
			getModel().setProsort(productSort);
		}
		sql.append(" order by c.createTime desc");
		System.out.println("sql ============ "+sql);
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return "myTjIndex";
	}
	/**
	 * 查询县公司查询自营商品
	 */
	public String index() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		List args = new ArrayList();
		StringBuffer sql = new StringBuffer("from Products c where c.visible='1' and (c.isNormal is null or c.isNormal='0') and c.integral is null ");
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		if(getModel().getProductType()){
			sql.append(" and c.productType = ?");
			args.add(true);
		}
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
		//根据地区查询
    if(null == regionId || 0 == regionId){
    	if(null != user.getRegion()){
    		sql.append(" and c.user.region.code like ?");
    	  args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
    	  regionName = user.getRegion().getName();
    	}
    }else{
    	Region region = regionManager.get(Integer.valueOf(regionId));
    	sql.append(" and c.user.region.code like ?");
    	args.add(MatchMode.START.toMatchString(region.getCode()));
    	regionName = region.getName();
    }
		if (getModel().getName() != null
				&& !getModel().getName().trim().equals("")) {
			sql.append(" and c.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (getModel().getCode() != null
				&& !getModel().getCode().trim().equals("")) {
			sql.append(" and c.code like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCode()));
		}
		if (getModel().getSupplier() != null
				&& getModel().getSupplier().getName() != null 
				&&  !getModel().getSupplier().getName().equals("")) {
			sql.append(" and c.supplier.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSupplier().getName()));
		}

		sql.append(" and c.prosort.status = ?");
		args.add("1");
		
		// 判断是否是顶级商品类别，如果不是顶级商品类别则加查询条件，否则查询全部
		if (getModel() != null && getModel().getProsort() != null
				&& getModel().getProsort().getId() != null) {
			ProductSort productSort = getManager().getDao().get(
					ProductSort.class, getModel().getProsort().getId());
			if (productSort != null) {
				// sql.append("and (c.prosort.id = ? or c.prosort.parentProsort.id = ?) ");
				// args.add(getModel().getProsort().getId());
				// args.add(getModel().getProsort().getId());
				sql.append("and c.prosort.serialNo like ? ");
				args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
			}

			// 为了返回查询中的条件数据需要将ProductSort存入到MODEL中
			getModel().setProsort(productSort);

		}
		sql.append(" order by c.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		getRequest().setAttribute("loginUser", userManager.get(UserUtil.getPrincipal(getRequest()).getId()));
		return INDEX;
	}
	public String tjindex() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		List args = new ArrayList();
		StringBuffer sql = new StringBuffer("from Products c where c.visible='1' and  c.isNormal='1' and c.integral is null ");
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		sql.append(" and c.user.region.id = ? ");
		args.add(user.getRegion().getId());
		if(getModel().getProductType()){
			sql.append(" and c.productType = ?");
			args.add(true);
		}
		if (getModel().getName() != null
				&& !getModel().getName().trim().equals("")) {
			sql.append(" and c.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (getModel().getCode() != null
				&& !getModel().getCode().trim().equals("")) {
			sql.append(" and c.code like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCode()));
		}
		if (getModel().getSupplier() != null
				&& getModel().getSupplier().getName() != null 
				&&  !getModel().getSupplier().getName().equals("")) {
			sql.append(" and c.supplier.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSupplier().getName()));
		}

		sql.append(" and c.prosort.status = ?");
		args.add("1");
		
		// 判断是否是顶级商品类别，如果不是顶级商品类别则加查询条件，否则查询全部
		if (getModel() != null && getModel().getProsort() != null
				&& getModel().getProsort().getId() != null) {
			ProductSort productSort = getManager().getDao().get(
					ProductSort.class, getModel().getProsort().getId());
			if (productSort != null) {
				sql.append("and c.prosort.serialNo like ? ");
				args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
			}

			// 为了返回查询中的条件数据需要将ProductSort存入到MODEL中
			getModel().setProsort(productSort);

		}
		sql.append(" order by c.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		getRequest().setAttribute("loginUser", userManager.get(UserUtil.getPrincipal(getRequest()).getId()));
		return "tjindex";
	}
	public String managerIndexProductCun() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		List args = new ArrayList();
		StringBuffer sql = new StringBuffer("from Products c where c.visible='1' and c.integral is  null ");
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
			sql.append(" and c.productType = ? and c.belonging = ?");
			args.add(false);
			args.add(ProductConstants.MERCHANT);
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
		
		//根据地区查询
    if(null == regionId || 0 == regionId){
    	if(null != user.getRegion()){
    		sql.append(" and c.user.region.code like ?");
    	  args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
    	  regionName = user.getRegion().getName();
    	}
    }else{
    	Region region = regionManager.get(Integer.valueOf(regionId));
    	sql.append(" and c.user.region.code like ?");
    	args.add(MatchMode.START.toMatchString(region.getCode()));
    	regionName = region.getName();
    }
		if (getModel().getName() != null
				&& !getModel().getName().trim().equals("")) {
			sql.append(" and c.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (getModel().getCode() != null
				&& !getModel().getCode().trim().equals("")) {
			sql.append(" and c.code like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCode()));
		}
		if (getModel().getSupplier() != null
				&& getModel().getSupplier().getName() != null 
				&&  !getModel().getSupplier().getName().equals("")) {
			sql.append(" and c.supplier.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSupplier().getName()));
		}

		sql.append(" and c.prosort.status = ?");
		args.add("1");
		
		// 判断是否是顶级商品类别，如果不是顶级商品类别则加查询条件，否则查询全部
		if (getModel() != null && getModel().getProsort() != null
				&& getModel().getProsort().getId() != null) {
			ProductSort productSort = getManager().getDao().get(
					ProductSort.class, getModel().getProsort().getId());
			if (productSort != null) {
				// sql.append("and (c.prosort.id = ? or c.prosort.parentProsort.id = ?) ");
				// args.add(getModel().getProsort().getId());
				// args.add(getModel().getProsort().getId());
				sql.append("and c.prosort.serialNo like ? ");
				args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
			}

			// 为了返回查询中的条件数据需要将ProductSort存入到MODEL中
			getModel().setProsort(productSort);

		}
		sql.append(" order by c.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return "managerIndexProductCun";
	}
	 public String plUpdownjia(){
		  String[] strArray=null;
		  String strs=getRequest().getParameter("strs");
		  String type=getRequest().getParameter("type");
		  System.out.println("----------------"+strs);
		  strArray=strs.split(",");
		  for (String string : strArray) {
			  Products product=getManager().get(Integer.parseInt(string));
			  if(product.getUpDownGoodshelf()==null||product.getUpDownGoodshelf().equals("1")){
				  product.setUpDownGoodshelf("0");
			  }else{
				  product.setUpDownGoodshelf("1");
			  }
			  getManager().update(product);
		}
		  return type;
	  }
	public String plRemove(){
		String[] strArray=null;
		String strs=getRequest().getParameter("strs");
		String type=getRequest().getParameter("type");
		System.out.println("----------------"+strs);
		strArray=strs.split(",");
		for (String string : strArray) {
			Products product=getManager().get(Integer.parseInt(string));
			product.setVisible("0");
			getManager().update(product);
		}
		return type;
	}
	 	public String upOrDownShelf(){
			Products products=getManager().get(getModel().getId());
			products.setUpDownGoodshelf(getModel().getUpDownGoodshelf());
			getManager().update(products);
			return "managerSave";
		}
		public String upOrDownShelf2(){
			Products products=getManager().get(getModel().getId());
			products.setUpDownGoodshelf(getModel().getUpDownGoodshelf());
			getManager().update(products);
			return "XJSuc";
		}
		/**特价商品上下架*/
		public String tjupOrDownShelf(){
			Products products=getManager().get(getModel().getId());
			products.setUpDownGoodshelf(getModel().getUpDownGoodshelf());
			getManager().update(products);
			return "tjXJSuc";
		}
		/**特价商品上下架index*/
		public String tjupOrDownShelf1(){
			Products products=getManager().get(getModel().getId());
			products.setUpDownGoodshelf(getModel().getUpDownGoodshelf());
			getManager().update(products);
			return "tjXJSuc1";
		}
		public String upOrDownShelf1(){
			Products products=getManager().get(getModel().getId());
			products.setUpDownGoodshelf(getModel().getUpDownGoodshelf());
			getManager().update(products);
			return SUCCESS;
		}
	/**
	 * 查询平台商品
	 */
	public String indexPlatformProduct() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());

		List args = new ArrayList();
		StringBuffer sql = new StringBuffer("from Products c where c.visible='1' ");
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
			sql.append(" and c.productType = ? and c.belonging = ?");
			args.add(false);
			args.add(ProductConstants.PLATFORM);
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
		
//		//根据地区查询
//    if(null == regionId || 0 == regionId){
//    	if(null != user.getRegion()){
//    		sql.append(" and c.user.region.code like ?");
//    	  args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
//    	  regionName = user.getRegion().getName();
//    	}
//    }else{
//    	Region region = regionManager.get(Integer.valueOf(regionId));
//    	sql.append(" and c.user.region.code like ?");
//    	args.add(MatchMode.START.toMatchString(region.getCode()));
//    	regionName = region.getName();
//    }
		if (getModel().getName() != null
				&& !getModel().getName().trim().equals("")) {
			sql.append(" and c.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (getModel().getCode() != null
				&& !getModel().getCode().trim().equals("")) {
			sql.append(" and c.code like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCode()));
		}
		if (getModel().getSupplier() != null
				&& getModel().getSupplier().getName() != null 
				&&  !getModel().getSupplier().getName().equals("")) {
			sql.append(" and c.supplier.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSupplier().getName()));
		}

		sql.append(" and c.prosort.status = ?");
		args.add("1");
		
		// 判断是否是顶级商品类别，如果不是顶级商品类别则加查询条件，否则查询全部
		if (getModel() != null && getModel().getProsort() != null
				&& getModel().getProsort().getId() != null) {
			ProductSort productSort = getManager().getDao().get(
					ProductSort.class, getModel().getProsort().getId());
			if (productSort != null) {
				// sql.append("and (c.prosort.id = ? or c.prosort.parentProsort.id = ?) ");
				// args.add(getModel().getProsort().getId());
				// args.add(getModel().getProsort().getId());
				sql.append("and c.prosort.serialNo like ? ");
				args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
			}

			// 为了返回查询中的条件数据需要将ProductSort存入到MODEL中
			getModel().setProsort(productSort);

		}
		sql.append(" order by c.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return "indexPlatformProduct";
	}
	
	

	/**
	 * 期初库存：导出模板(导出的是所有商品信息)
	 * @author songbaojie
	 */
	public String stockInitTemplate(){
		index();
		return "stockInitTemplate";
	}
	
	/**
	 * 删除商品以及商品单位换算表
	 */
		public String managerCunremove() {
			Products products=null;
			if (getModel().getId() != null) {
				 products = getManager().get(getModel().getId());
				 products.setVisible("0");
			}
			try{
				getManager().update(products);		
			}catch(Exception e){
				this.addActionMessage("删除失败，" +
						"名称为【"+products.getName()+"】的商品已经使用，不能删除！");
			}
			return "managerSave";
		}
	 
	 
	@Override
	public String remove() {
		Products products=null;
		if (getModel().getId() != null) {
			 products = getManager().get(getModel().getId());
			 products.setVisible("0");
		}
		try{
			getManager().update(products);	
		}catch(Exception e){
			this.addActionMessage("删除失败，" +
					"名称为【"+products.getName()+"】的商品已经使用，不能删除！");
		}
		return "myIndex";
	}
	public String tjremove() {
		Products products=null;
		if (getModel().getId() != null) {
			 products = getManager().get(getModel().getId());
			 products.setVisible("0");
		}
		try{
			getManager().update(products);	
		}catch(Exception e){
			this.addActionMessage("删除失败，"+"名称为【"+products.getName()+"】的商品已经使用，不能删除！");
		}
		return "myTjIndex";
	}
	public String cunRemove() {
		Products products=null;
		if (getModel().getId() != null) {
			 products = getManager().get(getModel().getId());
			 products.setVisible("0");
		}
		try{
			getManager().update(products);		
		}catch(Exception e){
			this.addActionMessage("删除失败，" +
					"名称为【"+products.getName()+"】的商品已经使用，不能删除！");
		}
		return "XJSuc";
	}
	/**
	 * 进销存采购管理中使用的商品选择列表
	 * 
	 * @return
	 */
	public String showIndex() {
        index();
		return "showIndex";
	}
	/**
	 * 采购管理中使用的商品选择列表此供应商对应的商品
	 * 
	 * @return
	 */
	public String purchaseshowIndex() {
		try{
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Products c where c.prosort.status=1 ");
		List args = new ArrayList();
        
		// 根据当前登录用户查询商品
		User u = UserUtil.getPrincipal(getRequest());
		if (u != null) {
			sql.append(" and (user.id = ? or user.id=?)");
			args.add(u.getId());
			if (u.getSuperior() != null) {
				args.add(u.getSuperior().getId());
			} else {
				args.add(u.getId());
			}
		}

		String status = getRequest().getParameter("status");

		if (status != null && !status.equals("")) {
			sql.append(" and c.prosort.status = ?");
			args.add(status);
		}
		if (getModel()!=null && getModel().getSupplier()!=null && getModel().getSupplier().getId()!=null) {
			sql.append(" and  c.supplier.id=? ");
			args.add(getModel().getSupplier().getId());
		
		}
		
		if (getModel().getName() != null
				&& !getModel().getName().trim().equals("")) {
			sql.append(" and c.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (getModel().getCode() != null
				&& !getModel().getCode().trim().equals("")) {
			sql.append(" and c.code like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCode()));
		}

		// 判断是否是顶级商品类别，如果不是顶级商品类别则加查询条件，否则查询全部
		if (getModel() != null && getModel().getProsort() != null
				&& getModel().getProsort().getId() != null) {
			ProductSort productSort = getManager().getDao().get(
					ProductSort.class, getModel().getProsort().getId());
			if (productSort != null) {
				sql.append("and c.prosort.serialNo like ? ");
				args.add(MatchMode.START.toMatchString(productSort
						.getSerialNo()));
			}

			// 为了返回查询中的条件数据需要将ProductSort存入到MODEL中
			getModel().setProsort(productSort);

		}
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		List<Products> plist=page.getData();
		//加入即时库存
	       String storageid=this.getRequest().getParameter("storageid");
	        if(storageid!=null && !storageid.equals("0") && !storageid.equals("")){
	        	this.getRequest().setAttribute("storageid",storageid);
	        	for(Products product:plist){
	        		Stock stock=stockManager.findStockPurchase(Integer.parseInt(storageid),
	        				product.getId());
	        		if(stock==null){
	        			product.setOutprice(0f);
	        		}else{
	        			product.setOutprice(stock.getCount()*1.0f);
	        		}
	        	}
	        }
		restorePageData(page);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "purchaseshowIndex";
	}

	/**
	 * 供应商的下拉列表
	 */
	public Map getSupplierMap() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			// 得到所有状态为可用的供应商
			return supplierManager.getSupplierMapByUser(user);
		}
		return null;
	}

	public File getAttch() {
		return attch;
	}

	public void setAttch(File attch) {
		this.attch = attch;
	}

	public String getAttchFileName() {
		return attchFileName;
	}

	public void setAttchFileName(String attchFileName) {
		this.attchFileName = attchFileName;
	}

	public String getAttchFolder() {
		return attchFolder;
	}

	public void setAttchFolder(String attchFolder) {
		this.attchFolder = attchFolder;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public RegionManager getRegionManager() {
		return regionManager;
	}

	public void setRegionManager(RegionManager regionManager) {
		this.regionManager = regionManager;
	}
	public File getData() {
		return data;
	}
	public void setData(File data) {
		this.data = data;
	}
	public String getDataFileName() {
		return dataFileName;
	}
	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
}
