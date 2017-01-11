package com.systop.amol.stock.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.systop.amol.base.storage.model.Storage;
import com.systop.amol.base.storage.service.StorageManager;
import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.stock.StockConstants;
import com.systop.amol.stock.model.StockInit;
import com.systop.amol.stock.service.StockInitManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 库存期初设置action
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StockInitAction extends
		DefaultCrudAction<StockInit, StockInitManager> {
	@Autowired
	private StorageManager storageManager;
	
	@Autowired
	private SupplierManager supplierManager;

	// 开始时间
	private String startDate;

	// 结束时间
	private String endDate;

	// 出货库
	// private Map outStorageMap;

	// 入货库
	// private Map inStorageMap;

	// 异步请求，返回值
	private Map<String, Object> result;	
	
	public String init() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		if (user.getBeginningInit() == null || user.getBeginningInit().equals("0")) {
			return edit();
		} else {
			return index();
		}
	}
	
	/**
	 * 查询库存调拨列表
	 */
	@Override
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前用户不存在");

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from StockInit s where s.user.id = ? ");
		List args = new ArrayList();
		args.add(user.getId());
		if (getModel() != null && getModel().getStorage() != null
				&& getModel().getStorage().getId() != null) {
			sql.append(" and s.storage.id = ?");
			args.add(getModel().getStorage().getId());
		}
		
		//判断商品是否存在
		if(getModel().getProducts() != null){
			//商品名称
			if( StringUtils.isNotBlank(getModel().getProducts().getName())){
				sql.append(" and s.products.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getProducts().getName()));
			}
			//商品编码
			if( StringUtils.isNotBlank(getModel().getProducts().getCode())){
				sql.append(" and s.products.code like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getProducts().getCode()));	
			}
			//供应商
			if(getModel().getProducts().getSupplier() != null
					&& StringUtils.isNotBlank(getModel().getProducts().getSupplier().getName())){
				sql.append(" and s.products.supplier.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getProducts().getSupplier().getName()));				
			}
		}
		
		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and s.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and s.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		sql.append(" order by s.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		List<StockInit> stockInits = page.getData();
		List<StockInit> listStockInits = new ArrayList<StockInit>();
		for(StockInit s : stockInits){
			//得到包装单位
			String temp = StockConstants.getUnitPack(getServletContext(), s.getProducts().getId(), s.getCount());
			s.setUnitPack(temp);
			listStockInits.add(s);
		}
		page.setData(listStockInits);
		
		// 得到初期库存数量汇总
		String nsql="select sum(s.count) "+sql.toString();
		List list=getManager().query(nsql, args.toArray());
		String total="";
		if(list.size()>0){
			for (Object o : list) {
				if(o != null){
				 total=o.toString();
				}
			}
		}
		getRequest().setAttribute("total", total);
		restorePageData(page);
		return INDEX;
	}

	@Override
	public String save() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前用户不存在");
		Assert.notNull(getModel());
		getModel().setUser(user);
		
		String[] pIds = getRequest().getParameterValues("pid"); // 商品ID列表
		String[] ncounts = getRequest().getParameterValues("ncounts"); // 单位数量列表
		String[] counts = getRequest().getParameterValues("counts"); // 单位数量列表
		String[] prices = getRequest().getParameterValues("prices"); // 单价列表
		String[] amounts = getRequest().getParameterValues("amounts"); // 金额列表
		String[] unitids = getRequest().getParameterValues("unitid"); // 单位ID列表(值[id,name,count,price])
		String[] status = getRequest().getParameterValues("status"); // 状态列表 
		String[] siIds = getRequest().getParameterValues("siId"); // 期初库存ID列表
		 
		try {
			if(getModel().getStorage() != null && getModel().getStorage().getId() != null){
				getModel().setStorage(getManager().getDao().get(Storage.class, getModel().getStorage().getId()));
				// 保存库存库存调拨单以及调拨详单
				getManager().saveInitStockAndStock(siIds,pIds, unitids, ncounts,counts, 
						prices, amounts,status,getModel().getStorage(), user);	
			}else{
				this.addActionError("请正确选择仓库");
				return this.edit() ;
			}
		} catch (Exception e) {
			this.addActionError("发生错误请与管理员联系");
			return this.edit() ;
		}
		this.addActionMessage("保存成功！");
		return SUCCESS;
	}

	@Override
	public String edit() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前用户不存在");
		Assert.notNull(getModel());
		if(StringUtils.isNotBlank(user.getBeginningInit()) && user.getBeginningInit().equals("1")){
			return this.index();
		}
		List<StockInit> list = new ArrayList<StockInit>();
		if(getModel().getStorage()!= null && getModel().getStorage().getId() != null){
			//根据仓库和用户查询期初库存
			list = getManager().query("from StockInit s where s.storage.id = ? and s.user.id = ?", getModel().getStorage().getId(),user.getId());
		}else{
			//根据用户查询期初库存
			list = getManager().query("from StockInit s where s.user.id = ?", user.getId());
		}
		getRequest().setAttribute("stockInits", list);
		return super.edit();
	}
	
	/**
	 * 导出到excel
	 * 
	 * @return
	 */
	@Deprecated
	public String exportExcel() {
		this.view();
		return "exportExcel";
	}
	
	/**
	 * 异步删除(删除【期初库存】同时删除【库存】)一件商品
	 * @return
	 */
	public String delStockInitAndStock(){
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前用户不存在");
		Assert.notNull(getModel());
		
		result = new HashMap<String, Object>();
		result.put("success", true);
		try{
			if(getModel().getProducts() != null &&  getModel().getStorage() != null ){
				getManager().delInitStockAndStock(getModel().getId(),getModel().getProducts(), getModel().getStorage(), user);	
			}else{
				result.put("success", false);
				result.put("msg", "商品和仓库错误查证后在执行操作！");
			}	
		}catch (Exception e) {
			result.put("success", false);
			result.put("msg", "发生异常，删除失败！");
		}
		

		return "jsonSuccess";
	}
	
	/**
	 * 仓库的下拉列表
	 */
	public Map getStorageMap() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			return storageManager.getStorageMap(user);
		}
		return null;
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
	
	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
