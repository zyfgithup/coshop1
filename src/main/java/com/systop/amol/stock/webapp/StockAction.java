package com.systop.amol.stock.webapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.storage.model.Storage;
import com.systop.amol.base.storage.service.StorageManager;
import com.systop.amol.base.storage.service.StorageSecondManager;
import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.stock.StockConstants;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.service.StockManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 即时库存管理action
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StockAction extends DefaultCrudAction<Stock, StockManager> {

	// 仓库管理Manager
	@Autowired
	private StorageManager storageManager;
	// 分销商库存管理Manager
	@Autowired
	private StorageSecondManager storageSecondManager;
	// 供应商管理Manager
	@Autowired
	private SupplierManager supplierManager;
	
	/**
	 * 经销商名称
	 */
	private String conditionName;
	
	/**
	 * 经销商名ID
	 */
	private String conditionId;

	/**
	 * 经销商级别（1:经销商2：分销商）
	 */
	private String conditionSuperiorSign;

	/**
	 * 商品名称
	 */
	private String conditionProductName;

	/**
	 * 商品编码
	 */
	private String conditionProductCode;
	/**
	 * 库存盘点单号
	 */
	private String checkNo;
	
	/**
	 * 仓库MAP
	 */
	private Map storageMap;
	
	/** JSON数据 */
	private Boolean isStock = false;

	@Override
	public String save() {
		return super.save();
	}

	/**
	 * 查询列表
	 */
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			user = getManager().getDao().get(User.class, user.getId());
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		Assert.notNull(getModel());

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Stock c where c.count != 0");
		List args = new ArrayList();
		
		// 查询所有分销商仓库
		if(conditionSuperiorSign != null && !conditionSuperiorSign.equals("") && conditionSuperiorSign.equals("2")){
			sql.append(" and c.user in (from User u where u.superior.id = ? )"); 
			// 得到所有状态为可用的分销商仓库
			// 判断是否为职员操作
			if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
				storageMap = storageSecondManager.getSecondStorageMapRun(user.getSuperior());	
			}else{
				storageMap = storageSecondManager.getSecondStorageMapRun(user);	
			}
			
		}else{		// 查询所有经销商仓库
			sql.append(" and c.user.id=?");
			// 得到所有状态为可用的经销商仓库
			// 判断是否为职员操作
			if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
				storageMap = storageManager.getStorageMapRun(user.getSuperior());	
			}else{
				storageMap = storageManager.getStorageMapRun(user);	
			}
			
		}
		// 判断是否为职员操作
		if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			args.add(user.getSuperior().getId());
		}else{
			args.add(user.getId());	
		}

		// if (getModel() != null && getModel().getProducts()!= null
		// && getModel().getProducts().getSupplier() != null
		// && getModel().getProducts().getSupplier().getId() != null
		// && getModel().getProducts().getSupplier().getId()>0) {
		// sql.append(" and c.products.supplier.id=?");
		// args.add(getModel().getProducts().getSupplier().getId());
		//		}
				
		if (getModel().getProducts() != null) {
			//商品名称
			if(StringUtils.isNotBlank(getModel().getProducts().getName())){
				sql.append(" and c.products.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getProducts().getName()));
			}
			//商品编码
			if(StringUtils.isNotBlank(getModel().getProducts().getCode())){
				sql.append(" and c.products.code like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getProducts().getCode()));
			}
			//供应商
			if(getModel().getProducts().getSupplier() != null
					&& StringUtils.isNotBlank(getModel().getProducts().getSupplier().getName())){
				sql.append(" and c.products.supplier.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getProducts().getSupplier().getName()));				
			}
		}

		if (getModel().getStorage() != null && getModel().getStorage().getId() != null) {
				sql.append(" and c.storage.id = ?");
				args.add(getModel().getStorage().getId());	
		}
		
		sql.append(" order by c.storage.id ");
		
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		
		
		Integer uid;
		if (user.getSuperior() != null) {
			uid = user.getSuperior().getId();
		} else {
			uid = user.getId();
		}
		// 即时库存包装单位
		List <Stock> stocks = getManager().getUnitsItems(user.getId(),uid,page.getData());
		page.setData(stocks);
		restorePageData(page);
		
		
		
		return INDEX;
	}

	/**
	 * 销售中分销商查询列表
	 */
	public String salesStockIndex() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			user = getManager().getDao().get(User.class, user.getId());
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return "salesStockIndex";
		}
		Assert.notNull(getModel());

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Stock c where c.count != 0 and c.user.id=?");
		List args = new ArrayList();
		args.add(user.getId());	
		
		if (getModel().getProducts() != null) {
			//判断商品名称
			if(StringUtils.isNotBlank(getModel().getProducts().getName())){
				sql.append(" and c.products.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getProducts().getName()));
			}
			//判断商品编码
			if(StringUtils.isNotBlank(getModel().getProducts().getCode())){
				sql.append(" and c.products.code like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getProducts().getCode()));
			}
			//判断供应商
			if(getModel().getProducts().getSupplier() != null
					&& StringUtils.isNotBlank(getModel().getProducts().getSupplier().getName())){
				sql.append(" and c.products.supplier.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getProducts().getSupplier().getName()));				
			}
		}

		sql.append(" order by c.count desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		
		Integer uid;
		if (user.getSuperior() != null) {
			uid = user.getSuperior().getId();
		} else {
			uid = user.getId();
		}
		// 即时库存包装单位
		List <Stock> stocks = getManager().getUnitsItems(user.getId(),uid,page.getData());
		page.setData(stocks);
		restorePageData(page);
		
		return "salesStockIndex";
	}	
	
	/**
	 * 生产厂商,库存状态
	 * 
	 * @author songbaojie
	 * @return
	 */
	public String stockIndex() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "用户不允许为空");

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"select c.user,c.products,sum(c.count) from Stock c where c.products.supplier.name = ? ");

		List args = new ArrayList();
		args.add(user.getName());
		sql.append(" and c.user.superior is null and c.user.type = 'agent'");
		
		if (StringUtils.isNotBlank(conditionId)) {
			sql.append(" and c.user.id = ?");
			args.add(new Integer(conditionId));
		}
		
		if (StringUtils.isNotBlank(conditionProductName)) {
			sql.append(" and c.products.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(conditionProductName));
		}

		if (StringUtils.isNotBlank(conditionProductCode)) {
			sql.append(" and c.products.code like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(conditionProductCode));
		}
		
		sql.append(" group by c.user.id,c.products");

		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		List<Object[]> list = page.getData();
		List<Map> stocks = new ArrayList<Map>();
		// 计算商品总数量
		Long countTotal = 0l;
		if(list != null){
			for (Object[] objs : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				User u = (User) objs[0];
				Products product = (Products) objs[1];
				Long count = 0l;
				if(objs[2] != null){
					count = (Long) objs[2];	
				}
				// 查询出来的商品数量包括经销商下的所有分销商货品数量
				count += getManager().getChildSuperiorsCount(u.getId(),product.getId());
				map.put("user", u);
				map.put("product", product);
				map.put("count", count);
				//包装单位换算
				String unitPack = StockConstants.getUnitPack(getServletContext(), product.getId(), count.intValue());
				map.put("unitPack", unitPack);//存储包装单位
				countTotal += count;
				stocks.add(map);
			}
		}
		page.setData(stocks);
		getRequest().setAttribute("countTotal", String.valueOf(countTotal));
		restorePageData(page);

		return "stockIndex";
	}
	
	/**
	 * 生产厂商,库存状态详情信息
	 * 
	 * @author songbaojie
	 * @return
	 */
	public String stockIndexView(){
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "用户不允许为空");
		Assert.notNull(getModel());
		if(getModel().getUser() == null || getModel().getUser().getId() == null){
			this.addActionError("异常错误，经销商不存在！");
			return this.stockIndex();
		}
		if(getModel().getProducts() == null || getModel().getProducts().getId() == null){
			this.addActionError("异常错误，商品不存在！");
			return this.stockIndex();
		}
		// 计算商品总数量
		Long countTotal = 0l;

		//经销商
		List<Object[]> list = getManager().getDao().query("select s.user,s.products,sum(s.count) from Stock s where s.user.id = ? and s.products.id = ? and s.user.type = 'agent' group by s.user.id,s.products"
				, getModel().getUser().getId(),getModel().getProducts().getId() );
		List<Map> stocks = new ArrayList<Map>();
		if(list != null){
			for (Object[] objs : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				User u = (User) objs[0];
				Products product = (Products) objs[1];
				Long count = 0l;
				if(objs[2] != null){
					count = (Long) objs[2];	
				}
				map.put("user", u);
				map.put("product", product);
				map.put("count", count);
				countTotal += count;
				stocks.add(map);
			}
		}
		//分销商
		List<Object[]> list2 = getManager().getDao().query("select s.user,s.products,sum(s.count) from Stock s where s.user.superior.id = ? and s.products.id = ? and s.user.type = 'agent' group by s.user.id,s.products"
				, getModel().getUser().getId(),getModel().getProducts().getId() );
		List<Map> stocks2 = new ArrayList<Map>();
		if(list != null){
			for (Object[] objs : list2) {
				Map<String, Object> map = new HashMap<String, Object>();
				User u = (User) objs[0];
				Products product = (Products) objs[1];
				Long count = 0l;
				if(objs[2] != null){
					count = (Long) objs[2];	
				}
				map.put("user", u);
				map.put("product", product);
				map.put("count", count);
				countTotal += count;
				stocks2.add(map);
			}
		}
		
		getRequest().setAttribute("countTotal", String.valueOf(countTotal));
		getRequest().setAttribute("distributor1", stocks);
		getRequest().setAttribute("distributor2", stocks2);
		return "stockIndexView";
	}
	
	/**
	 * 得到即时库存数据(库存调拨)
	 * 
	 * @author songbaojie
	 * @return
	 */
	public String stockShowIndex() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(getModel(), "");
		Assert.notNull(user, "用户不允许为空");
		user = getManager().getDao().get(User.class, user.getId());
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		List args = new ArrayList();
		StringBuffer sql = new StringBuffer(
				"from Stock s where s.products.prosort.status=1 and s.user.id = ? and s.count != 0");
//		// 判断是否为职员操作
//		if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
//			args.add(user.getSuperior().getId());
//		}else{
//			args.add(user.getId());	
//		}
		//判断仓库是否存在
		if (getModel().getStorage() != null
				&& getModel().getStorage().getId() != null) {
			//得到仓库对象
			Storage storage = getManager().getDao().get(Storage.class,getModel().getStorage().getId());
			//如果仓库属于分销商,则根据分销商用户ID查询即时库存,否则根据经销商
			if(storage.getCreator().getSuperior() != null){
				args.add(storage.getCreator().getId());
			}else{
				if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
					args.add(user.getSuperior().getId());
				}else{
					args.add(user.getId());	
				}
			}
			sql.append(" and s.storage.id = ?");
			args.add(getModel().getStorage().getId());
		} else{
			if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
				args.add(user.getSuperior().getId());
			}else{
				args.add(user.getId());	
			}	
		}

		if (getModel().getProducts() != null
				&& getModel().getProducts().getName() != null) {
			sql.append(" and s.products.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getProducts()
					.getName()));
		}

		// 判断是否是顶级商品类别，如果不是顶级商品类别则加查询条件，否则查询全部
		if (getModel().getProducts() != null
				&& getModel().getProducts().getProsort() != null
				&& getModel().getProducts().getProsort().getId() != null) {
			ProductSort productSort = getManager().getDao().get(
					ProductSort.class,
					getModel().getProducts().getProsort().getId());
			if (productSort != null) {
				sql.append("and s.products.prosort.serialNo like ? ");
				args.add(MatchMode.START.toMatchString(productSort
						.getSerialNo()));
			}

			// 为了返回查询中的条件数据需要将ProductSort存入到MODEL中
			getModel().getProducts().setProsort(productSort);

		}

		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		// 即时库存包装单位
		List<Stock> stocks = page.getData();
		List<Stock> listStocks = new ArrayList<Stock>();
		for(Stock s : stocks){
			//得到包装单位
			String temp = StockConstants.getUnitPack(getServletContext(), s.getProducts().getId(), s.getCount());
			s.setUnitPack(temp);
			listStocks.add(s);
		}
		page.setData(listStocks);
		restorePageData(page);
		return "stockShowIndex";
	}

	/**
	 * 得到即时库存商品选项(适用于库存盘点)
	 * @return
	 */
	public String selectorProIndex(){
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(getModel(), "");
		Assert.notNull(user, "用户不允许为空");
		user = getManager().getDao().get(User.class,user.getId());
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		List args = new ArrayList();
		StringBuffer sql = new StringBuffer(
				"from Stock s where s.products.prosort.status=1 and s.user.id = ?");
		// 判断是否为职员操作
		if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			args.add(user.getSuperior().getId());
		}else{
			args.add(user.getId());	
		}

		checkNo = getRequest().getParameter("checkNo");
		// 判断库存单号是否存在
		if(StringUtils.isNotBlank(checkNo)){
			// 去掉该盘点单中已经存在的库存商品
			sql.append(" and s not in (select st.stock from StockCheckDetail st where st.stockCheck.checkNo = ? and st.stockCheck.status = '0')");
			args.add(checkNo);
		}
		String productCode = getRequest().getParameter("proCode");
		if(StringUtils.isNotBlank(productCode)){
			String[] codes = productCode.trim().split(",");
			//拼接查询字符串
			sql.append(" and s.products.code not in (" );
			for(int i = 0; i < codes.length; i++){
				if(i == codes.length - 1){
					sql.append("'").append(codes[i]).append("'");
					continue;
				}
				sql.append("'").append(codes[i]).append("',");	
			}
			sql.append(" )");
			getRequest().setAttribute("proCode", productCode);
		}
		
		if (getModel().getStorage() != null
				&& getModel().getStorage().getId() != null) {
			sql.append(" and s.storage.id = ?");
			args.add(getModel().getStorage().getId());
		} 

		if (getModel().getProducts() != null) {
			//商品名称
			if(StringUtils.isNotBlank(getModel().getProducts().getName())){
				sql.append(" and s.products.name like ? ");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getProducts().getName()));	
			}
			//商品编码
			if (StringUtils.isNotBlank(getModel().getProducts().getCode())) {
				sql.append(" and s.products.code like ? ");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getProducts().getCode()));
			}
		}
		// 判断是否是顶级商品类别，如果不是顶级商品类别则加查询条件，否则查询全部
		if (getModel().getProducts() != null
				&& getModel().getProducts().getProsort() != null
				&& getModel().getProducts().getProsort().getId() != null) {
			ProductSort productSort = getManager().getDao().get(
					ProductSort.class,
					getModel().getProducts().getProsort().getId());
			if (productSort != null) {
				sql.append("and s.products.prosort.serialNo like ? ");
				args.add(MatchMode.START.toMatchString(productSort
						.getSerialNo()));
			}

			// 为了返回查询中的条件数据需要将ProductSort存入到MODEL中
			getModel().getProducts().setProsort(productSort);

		}

		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return "selectorProIndex";
	}
	
	/**
	 * @author 王会璞 异步交互，判断选择的商品是否在仓库中
	 * @return
	 */
	public String judgeStockByProduct() {
		try {
			User user = UserUtil.getPrincipal(getRequest());
			if (user != null) {
				getModel().setUser(user);
			} else {
				this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
				return INPUT;
			}
			String stockIdS = getRequest().getParameter("stockId");
			String productIdStr = getRequest().getParameter(
			"productId");
			Integer productId = null;
			if(StringUtils.isNumeric(productIdStr)){
				productId = Integer.parseInt(productIdStr);
			}
			//Integer userId = Integer.parseInt(getRequest().getParameter("userId"));

			if (StringUtils.isNotBlank(stockIdS) && StringUtils.isNumeric(productIdStr)) {
				if (getManager().findStock(Integer.parseInt(stockIdS), productId,
						user.getId()) == null) {
					isStock = false;
				} else {
					isStock = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "jsonRst";
	}
	
	/**
	 * 仓库的下拉列表
	 */
	public Map getStorageMap() {
		return storageMap;
	}

	public void setStorageMap(Map storageMap) {
		this.storageMap = storageMap;
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
	
	public Boolean getIsStock() {
		return isStock;
	}

	public void setIsStock(Boolean isStock) {
		this.isStock = isStock;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public String getConditionId() {
		return conditionId;
	}

	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}

	public String getConditionProductName() {
		return conditionProductName;
	}

	public void setConditionProductName(String conditionProductName) {
		this.conditionProductName = conditionProductName;
	}

	public String getConditionSuperiorSign() {
		return conditionSuperiorSign;
	}

	public void setConditionSuperiorSign(String conditionSuperiorSign) {
		this.conditionSuperiorSign = conditionSuperiorSign;
	}

	public String getConditionProductCode() {
		return conditionProductCode;
	}

	public void setConditionProductCode(String conditionProductCode) {
		this.conditionProductCode = conditionProductCode;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

}
