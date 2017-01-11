package com.systop.amol.purchase.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.storage.model.Storage;
import com.systop.amol.base.storage.service.StorageManager;
import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.base.units.model.Units;
import com.systop.amol.base.units.model.UnitsItem;
import com.systop.amol.base.units.service.UnitsItemManager;
import com.systop.amol.purchase.PurchaseConstants;
import com.systop.amol.purchase.model.Purchase;
import com.systop.amol.purchase.model.PurchaseDetail;
import com.systop.amol.purchase.service.OrderManager;
import com.systop.amol.purchase.service.PurchaseDetailManager;
import com.systop.amol.purchase.service.PurchaseManager;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.service.StockManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.JsonCrudAction;

/**
 * 入库管理Manager
 * 
 * @author WangHaiYan
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PurchaseAction extends JsonCrudAction<Purchase, PurchaseManager> {

	@Autowired
	private PurchaseDetailManager purchaseDetailManager;
	@Autowired
	private SupplierManager supplierManager;
	// 仓库管理Manager
	@Autowired
	private StorageManager storageManager;
	@Autowired
	private StockManager stockManager;

	@Autowired
	private OrderManager orderManager;

	// JSon数据
	private Map<String, String> jsonResult;
	
	/** JSON数据 */
	private Map<String, String> jsonResultSales;

	// 单位相关信息AJAX加载
	@Autowired
	private UnitsItemManager unitsItemManager;

	// 开始时间
	private String startDate;

	// 结束时间
	private String endDate;

	// 单号类型
	private String notype;

	public String getNotype() {
		return notype;
	}

	public void setNotype(String notype) {
		this.notype = notype;
	}

	/**
	 * 查询入库单信息列表
	 */
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		if(user.getType().equals(AmolUserConstants.EMPLOYEE)){
			user.setSuperior(this.getManager().getDao().get(User.class,user.getSuperior().getId()));
		if(user.getSuperior().getBeginningInit().equals("0")){
			this.addActionError("请先完成初始化，再做业务单据");
			return INDEX;
		}
		}else{
			if(user.getBeginningInit().equals("0")){
				this.addActionError("请先完成初始化，再做业务单据");
				return INDEX;
			}	
		}
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from Purchase p where billType=? and  p.user.id=? ");
		List args = new ArrayList();
		args.add(PurchaseConstants.PURCHASETYPE);
		args.add(user.getId());
		if (getModel() != null && getModel().getStatus() != null
				&& getModel().getStatus() != 9) {
			sql.append(" and p.status = ?");
			args.add(getModel().getStatus());
		}
		if (getModel() == null || getModel().getStatus() == null) {
			getModel().setStatus(0);
			sql.append(" and p.status = ?");
			args.add(0);
		}
		if (getModel() != null && getModel().getSno() != null
				&& !getModel().getSno().equals("")) {
			if (notype.equals("0")) {
				sql.append(" and p.sno like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSno()));
			} else {
				sql.append(" and p.orderno like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSno()));
			}
		}
		if (getModel() != null && getModel().getSupplier() != null
				&& getModel().getSupplier().getName() != null
				&& !getModel().getSupplier().getName().equals("")
				) {
			sql.append(" and p.supplier.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSupplier().getName()));
		}
		if (getModel() != null && getModel().getSupplier() != null
				&& getModel().getSupplier().getName() != null
				&& !getModel().getSupplier().getName().equals("")) {
			sql.append(" and p.supplier.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSupplier()
					.getName()));
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and p.sdate >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and p.sdate <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		sql.append("order by p.sno desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());

		restorePageData(page);
		// 计算合计列
		String nsql = "select sum(samount),sum(spayTotal),sum(spayamount) "
				+ sql.toString();
		List list = getManager().query(nsql, args.toArray());
		String sfTotal = "";
		String bcpayTotal = "";
		String payTotal = "";
		if (list.size() > 0) {
			for (Object o : list) {
				Object[] oo = (Object[]) o;
				if (oo[0] != null) {
					sfTotal = oo[0].toString();
				}
				if (oo[1] != null) {
					payTotal = oo[1].toString();
				}
				if (oo[2] != null) {
					bcpayTotal = oo[2].toString();
				}
			}
		}

		getRequest().setAttribute("sfTotal", sfTotal);
		getRequest().setAttribute("bcpayTotal", bcpayTotal);
		getRequest().setAttribute("payTotal", payTotal);

		return INDEX;
	}

	public String find() {
		this.index();
		return "find";
	}

	/**
	 * 编辑入库单信息
	 */
	public String edit() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		if(user.getType().equals(AmolUserConstants.EMPLOYEE)){
			user.setSuperior(this.getManager().getDao().get(User.class,user.getSuperior().getId()));
		if(user.getSuperior().getBeginningInit().equals("0")){
			this.addActionError("请先完成初始化，再做业务单据");
			return INDEX;
		}
		}else{
			if(user.getBeginningInit().equals("0")){
				this.addActionError("请先完成初始化，再做业务单据");
				return INDEX;
			}	
		}
		if (getModel().getId() == null) {
			String jno = orderManager.getNo(PurchaseConstants.PURCHASETYPE,
					getModel().getUser());
			getModel().setSno(jno);
			getModel().setSdate(DateUtil.getCurrentDate());
		}
		return INPUT;
	}

	/**
	 * 查看
	 */
	public String view() {
		this.edit();
		return "view";
	}

	/**
	 * 保存入库单信息
	 */
	@Override
	public String save() {

		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		try {

			// 如果用户没有输入操作员则设置为空
			if (getModel().getEmployee().getName().trim().equals("")) {
				getModel().setEmployee(null);
			} else {
				getModel().setEmployee(
						this.getManager()
								.getDao()
								.get(User.class,
										this.getModel().getEmployee().getId()));
			}
			// 如果用户没有选仓库，设置为空
			if (getModel().getStorage() == null
					|| getModel().getStorage().getId() == null) {
				getModel().setStorage(null);
			} else {
				getModel().setStorage(
						this.getManager()
								.getDao()
								.get(Storage.class,
										getModel().getStorage().getId()));
			}

			getModel().setBillType(PurchaseConstants.PURCHASETYPE);
			if (getModel().getIsover() == null) {
				getModel().setIsover(PurchaseConstants.ORDER_NOTOVER_INT);
			}
			if (getModel().getStatus() == null) {
				getModel().setStatus(0);
			}
			if (getModel().getSpayamount() == null) {
				getModel().setSpayamount(0d);
			}
			getModel().setSpayTotal(getModel().getSpayamount());

			// 保存入库单明细
			String[] pids = this.getRequest().getParameterValues("pid");
			String[] unitids = this.getRequest().getParameterValues("unitid");
			String[] inprices = this.getRequest().getParameterValues("inprice");
			String[] ncounts = this.getRequest().getParameterValues("ncount");
			String[] counts = this.getRequest().getParameterValues("count");
			String[] moneys = this.getRequest().getParameterValues("money");
			String[] linkids = this.getRequest().getParameterValues("linkid");
			String[] remarks = this.getRequest().getParameterValues("sremark");

			List<PurchaseDetail> sdlist = new ArrayList<PurchaseDetail>();
			for (int i = 1; i < pids.length; i++) {
				PurchaseDetail pd = new PurchaseDetail();
				pd.setAmount(Double.parseDouble(moneys[i]));
				pd.setNcount(Float.parseFloat(ncounts[i]));
				pd.setCount(Integer.parseInt(counts[i]));
				pd.setPrice(Float.parseFloat(inprices[i]));
				pd.setProducts(this.getManager().getDao()
						.get(Products.class, Integer.parseInt(pids[i])));
				pd.setRemark(remarks[i]);
				String[] uids = unitids[i].split(",");
				pd.setUnits(this.getManager().getDao()
						.get(Units.class, Integer.parseInt(uids[0])));
				pd.setPurchase(getModel());
				pd.setLinkCount(Integer.parseInt(linkids[i]));
				sdlist.add(pd);
			}
			this.getManager().save(getModel(), sdlist);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			addActionError(e.getMessage());
			return INPUT;
		}
	}

	/**
	 * 删除入库单信息
	 */
	public String remove() {
		try {
			this.getManager().remove(getModel());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			this.addActionMessage("当前单据已生成应付款单货退货单不能删除！");
		}
		return INPUT;
	}

	/*
	 * 把明细传到页面
	 */
	public List<PurchaseDetail> getPurchaseDetail() {
		String oid = getRequest().getParameter("oid");
		List<PurchaseDetail> sd = new ArrayList<PurchaseDetail>();
		if (oid == null) {
			sd = this.getManager().getPurchaseDetail(getModel());
		} else {
			String hql = "from PurchaseDetail pd where  pd.purchase.id = ?";
			List<PurchaseDetail> orderDs = (List<PurchaseDetail>) this
					.getManager().getDao().query(hql, Integer.parseInt(oid));
			double amount = 0;
			for (PurchaseDetail od : orderDs) {
				PurchaseDetail pd = new PurchaseDetail();
				int lcount = 0;
				if (od.getLinkCount() != null) {
					lcount = od.getLinkCount();
				}
				// 已经完成下推时
				if (od.getCount() - lcount <= 0) {
					pd.setUnits(od.getProducts().getUnits());
					pd.setLinkCount(0);
				} else {
					pd.setUnits(od.getUnits());
					pd.setLinkCount(od.getId());// 订单对应的明细id
				}
				pd.setCount(od.getCount() - lcount);
				pd.setNcount(od.getNcount()
						- (lcount * od.getNcount() / od.getCount()));
				pd.setPrice(od.getPrice());
				pd.setAmount((double)Math.round(pd.getNcount() * pd.getPrice()*100/100));
				pd.setProducts(od.getProducts());
				amount = amount + pd.getAmount();
				sd.add(pd);
			}
			getModel().setSamount(amount);
		}
		this.getRequest().setAttribute("purchaseDetail", sd);
		return sd;
	}

	/**
	 * AjAX应用编辑页面，通过商品得到商品单位信息
	 * 
	 * @return
	 */
	public String getUnitsItem() {
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		int productid = Integer
				.parseInt(getRequest().getParameter("productid"));

		try {
			User user = UserUtil.getPrincipal(getRequest());
			Integer uid;
			if (user.getSuperior() != null) {
				uid = user.getSuperior().getId();
			} else {
				uid = user.getId();
			}
			List<UnitsItem> items = this.unitsItemManager.getUnitsItem(
					user.getId(), uid, productid);
			StringBuffer st = new StringBuffer();
			for (UnitsItem ui : items) {
				st.append(ui.getUnits().getId() + "," + ui.getUnits().getName()
						+ "," + ui.getCount() + "," + ui.getInprice() + ":");
			}
			st.substring(0, st.length() - 1);

			jsonResult.put("result", st.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "jsonResult";
	}
	
	/**
	 * @author 王会璞
	 * <p>
	 * AjAX应用编辑页面，通过商品得到商品单位信息
	 * </p>
	 * @return
	 */
	public String getUnitsItemSales() {
		jsonResultSales = Collections.synchronizedMap(new HashMap<String, String>());
		int productid = Integer
				.parseInt(getRequest().getParameter("productid"));

		try {
			User user = UserUtil.getPrincipal(getRequest());
			Integer uid;
			if (user.getSuperior() != null) {
				uid = user.getSuperior().getId();
			} else {
				uid = user.getId();
			}
			List<UnitsItem> items = this.unitsItemManager.getUnitsItem(
					user.getId(), uid, productid);
			StringBuffer st = new StringBuffer();
			for (UnitsItem ui : items) {
				st.append(ui.getUnits().getId() + "," + ui.getUnits().getName()
						+ "," + ui.getCount() + "," + ui.getOutprice() + ":");
			}
			st.substring(0, st.length() - 1);

			jsonResultSales.put("result", st.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "jsonResultSales";
	}

	/*
	 * 订单自动生成入库单
	 */
	public String convertPurchase() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		try {
			String jno = orderManager.getNo(PurchaseConstants.PURCHASETYPE,
					getModel().getUser());
			getModel().setSno(jno);
			getModel().setSdate(DateUtil.getCurrentDate());
			Integer orderid = Integer
					.parseInt(getRequest().getParameter("oid"));
			// 得到订单数据
			String hql = "from Purchase o where o.id = ?";
			List order = this.getManager().getDao().query(hql, orderid);
			Purchase o = (Purchase) order.get(0);
			getModel().setEmployee(o.getEmployee());
			getModel().setSpayamount(0.0d);
			getModel().setSamount(o.getSamount());
			getModel().setSupplier(o.getSupplier());
			getModel().setOrderid(o.getId());
			getModel().setOrderno(o.getSno());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INPUT;
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
	 * 
	 */
	public Map<String, String> getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(Map<String, String> jsonResult) {
		this.jsonResult = jsonResult;
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

	/**
	 * 导出到excel；
	 * 
	 * @return
	 */
	public String exportExcel() {
		this.edit();
		return "exportExcel";
	}

	/**
	 * 显示正常和作废的map
	 * 
	 * @return
	 */
	public Map<Integer, String> getStatusMap() {
		return PurchaseConstants.STATUS_MAP;
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

	/**
	 * 冲红
	 * 
	 * @return
	 */
	public String red() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		try {
			Integer id = getModel().getId();
			Purchase p = this.getManager().get(id);
			List<PurchaseDetail> pdlist = purchaseDetailManager.getDetails(id);
			//冲红前的判断，判断仓库中的数量-当前数量>0则可以冲红。
		    if(p.getStorage()!=null && p.getStorage().getId()>0){
		    	for(PurchaseDetail pd:pdlist){
		    		Stock stock=stockManager.findStock(p.getStorage().getId(),pd.getProducts().getId(), user.getId());
		    		if(stock != null){
		    			if(stock.getCount()-pd.getCount()<0){
		    				this.addActionMessage("商品【"+pd.getProducts().getName()+
		    						"】在仓库【"+p.getStorage().getName()+"】中数量为"+stock.getCount()
		    						+"小于入库数量"+pd.getCount()+"不能冲红");
		    				return SUCCESS;
		    			}
		    		}
		    	}
		    	
		    	
		    	
		    	
		    	
		    }
			
			
			
			Purchase purchase = new Purchase();
			purchase.setIsover(0);
			purchase.setStatus(2);// 冲红单
			purchase.setRemark("冲红单号：" + p.getSno());
			String jno = orderManager.getNo(PurchaseConstants.PURCHASETYPE,
					getModel().getUser());
			purchase.setSno(jno);
			purchase.setSdate(DateUtil.getCurrentDate());
			purchase.setSamount(-p.getSamount());
			purchase.setSpayamount(-p.getSpayamount());
			purchase.setSpayTotal(-p.getSpayTotal());
			purchase.setBillType(1);
			purchase.setEmployee(p.getEmployee());
			purchase.setPaytype(p.getPaytype());
			purchase.setStorage(p.getStorage());
			purchase.setSupplier(p.getSupplier());
			purchase.setUser(p.getUser());
			purchase.setOrderid(p.getOrderid());
			purchase.setOrderno(p.getOrderno());
			p.setStatus(1);// 已冲红
			this.getManager().save(p);
			
			List<PurchaseDetail> npdlist = new ArrayList<PurchaseDetail>();

			for (PurchaseDetail pd : pdlist) {
				PurchaseDetail npd = new PurchaseDetail();
				npd.setAmount(-pd.getAmount());
				npd.setCount(-pd.getCount());
				npd.setNcount(-pd.getNcount());
				npd.setPrice(pd.getPrice());
				npd.setProducts(pd.getProducts());
				npd.setPurchase(purchase);
				npd.setUnits(pd.getUnits());
				npd.setRemark("");
				npd.setLinkCount(pd.getLinkCount());
				npdlist.add(npd);
			}
			this.getManager().save(purchase, npdlist);

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INPUT;
	}
	public Map<String, String> getJsonResultSales() {
		return jsonResultSales;
	}

	public void setJsonResultSales(Map<String, String> jsonResultSales) {
		this.jsonResultSales = jsonResultSales;
	}
}
