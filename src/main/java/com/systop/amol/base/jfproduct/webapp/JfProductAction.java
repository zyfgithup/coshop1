package com.systop.amol.base.jfproduct.webapp;

import com.systop.amol.base.jfproduct.service.JfPrincepleManager;
import com.systop.amol.base.jfproduct.service.JfProductsXlsImportHelperFactory;
import com.systop.amol.base.product.ProductConstants;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.product.service.ProductsManager;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.amol.base.units.model.Units;
import com.systop.amol.base.units.service.UnitsManager;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.service.StockManager;
import com.systop.amol.user.AmolUserConstants;
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
 * 积分商品Excel导入
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JfProductAction extends
DefaultCrudAction<Products, ProductsManager>{

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
	@Autowired
	private ProductSortManager productSortManager; 

	/**
	 * 及即时库存管理
	 */
	@Autowired
	private StockManager stockManager;
	/** 用户管理 */
	@Autowired
	private UserManager userManager;
	
	private String errorInfo;
	
	private File attch;

	private String attchFileName;

	private String attchFolder = "/uploadFiles/fileAttch/";

	@Autowired
	private JfPrincepleManager jfPrincepleManager;



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
	public String importEdit(){
		return "importEdit";
	}
	/**
	 * 从Excel中导入监管员信息到数据库
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
		XlsImportHelper xih = JfProductsXlsImportHelperFactory.create();
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
				attchFolder="/uploadFiles/fileAttch/";
				products=new Products();
				Map<String, String> map = new HashMap<String, String>();
				// 得到导入数据map
				xih.importCommonProperties(map, rs, i);
				//{remark=口气清新, name=高露洁, ImageUrl=高露洁.png, proSortId=1703936, guige=200g, integral=100}
				String remark=map.get("remark");
				String name=map.get("name");
				String ImageUrl=map.get("ImageUrl");
				String proSortId=map.get("proSortId");
				String guige=map.get("guige");
				String integral=map.get("integral");
				String units=map.get("units");
				products.setVisible("1");//1积分商品可见，0不可见
				Units units2=unitsManager.get(Integer.parseInt(units));
				int j = i + 1 ;
				// 年月为空不进行导入
				if (StringUtils.isBlank(name)) {
					this.addActionMessage("Excel表中第【" + j + "】行商品名称为空，不做导入处理!");
					continue;
				}
				if (StringUtils.isBlank(integral)) {
					this.addActionMessage("Excel表中第【" + j + "】积分为空，不做导入处理!");
					continue;
				}
				if(null==proSortId||"".equals(proSortId)){
					errorInfo="没添加商品类别,请检查"+j+"列";
					return "importEdit";
				}
				if(null!=proSortId&!"".equals(proSortId)){
				ProductSort productSort=productSortManager.get(Integer.parseInt(proSortId));
				if(null==productSort){
					errorInfo="当前商品类别不存在,请检查"+j+"列";
					return "importEdit";
				}
				products.setProsort(productSort);
				}
				products.setUnits(units2);
				products.setName(name);
				products.setRegion(user.getRegion());
				products.setUser(user);
				products.setCreateTime(new Date());
				products.setIntegral(Integer.parseInt(integral));
				products.setStardard(guige);
				products.setUpDownGoodshelf("1");
				products.setRemark(remark);
				attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
				products.setImageURL(attchFolder+ImageUrl);
				productList.add(products);
			}
			jfPrincepleManager.plSaveJfproducts(productList);
			rs=null;
			 rwb=null;
			 is.close();    
		} catch (Exception e) {
			this.addActionMessage(e.getMessage());
			return "importEdit";
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
	 * 保存商品
	 */
	@Override
	public String save() {
		String productTypeStatus = getRequest().getParameter("productTypeStatus");
		if(null == productTypeStatus){
			getModel().setProductType(ProductConstants.ORDINARY);
		}else if(Boolean.valueOf(productTypeStatus)){
			getModel().setProductType(ProductConstants.GROUP_PURCHASE);
		}
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		Assert.notNull(getModel());
		try {

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
			if(null!=user.getType()&&user.getType().equals("agent")){
				getModel().setRegion(user.getRegion());
			}
			getModel().setProsort(productSort);
			getModel().setBelonging(3);
			attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
			if (attch != null) {
				String filePath = UpLoadUtil.doUpload(attch,
						attchFileName, attchFolder, getServletContext());
				getModel().setImageURL(filePath);
			}
			getModel().setUpDownGoodshelf("1");
			if(getModel()!=null&&null!=getModel().getId()){
				getManager().getDao().clear();
				getManager().update(getModel());
			}else{
				getManager().save(getModel());
			}
			// 判断是否为平台还是分销商自营的商品
			if(null != user.getType() && !AmolUserConstants.EMPLOYEE.equals(user.getType())){
				getModel().setBelonging(ProductConstants.MERCHANT);
			}
			// 同时保存该商品的基本单位设置信息
			getManager().unitsItemSave(getModel().getId());
			return SUCCESS;
		} catch (Exception e) {
			addActionError(e.getMessage());
			return INPUT;
		}
	}
	/**
	 * 查询
	 */
	public String index() {
		page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(" from Products c where c.integral is not null and c.visible='1' " );
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		List args = new ArrayList();
		sql.append(" and c.prosort.status = ?");
		args.add("1");
		if (getModel().getName() != null
				&& !getModel().getName().trim().equals("")) {
			sql.append(" and c.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if(null!=user.getRegion()){
		sql.append(" and c.user.region.code like ? ");
		args.add(user.getRegion().getCode());
		}
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
		sql.append("order by c.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		getRequest().setAttribute("logonUser",userManager.get(UserUtil.getPrincipal(getRequest()).getId()));
		return INDEX;
	}
	public String upOrDownShelf(){
		
		Products products=getManager().get(getModel().getId());
		
		products.setUpDownGoodshelf(getModel().getUpDownGoodshelf());
		
		getManager().update(products);
		
		return SUCCESS;
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
	@Override
	public String remove() {
		Products products=null;
		if (getModel().getId() != null) {
			 products = getManager().get(getModel().getId());
		}
		try{
			getManager().remove(products);		
		}catch(Exception e){
			this.addActionMessage("删除失败，" +
					"名称为【"+products.getName()+"】的商品已经使用，不能删除！");
		}
		return SUCCESS;
	}
  public String plUpdownjia(){
	  String[] strArray=null;
	  String strs=getRequest().getParameter("strs");
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
	  return SUCCESS;
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
