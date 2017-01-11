package com.systop.amol.purchase.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;
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
import com.systop.amol.purchase.PurchaseConstants;
import com.systop.amol.purchase.model.Purchase;
import com.systop.amol.purchase.model.PurchaseDetail;
import com.systop.amol.purchase.service.OrderManager;
import com.systop.amol.purchase.service.ReturnsDetailManager;
import com.systop.amol.purchase.service.ReturnsManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 入库管理Manager
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReturnsAction extends DefaultCrudAction<Purchase, ReturnsManager> {

	@Autowired
	private ReturnsDetailManager returnsDetailManager;
	@Autowired
	private SupplierManager supplierManager;
	// 开始时间
	private String startDate;

	// 结束时间
	private String endDate;

	@Autowired
	private OrderManager orderManager;
	// 仓库
	@Autowired
	private StorageManager storageManager;

	// 删除
	public String remove() {
		this.getManager().remove(getModel());
		return SUCCESS;
	}

	/**
	 * 保存
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
			getModel().setBillType(PurchaseConstants.RETURNTYPE);
			if (getModel().getIsover() == null) {
				getModel().setIsover(PurchaseConstants.ORDER_NOTOVER_INT);
			}
			if(getModel().getStatus()==null){
				getModel().setStatus(0);
			}
			if(getModel().getSpayamount()==null){
				getModel().setSpayamount(0d);
			}
              getModel().setSpayTotal(getModel().getSpayamount());
			// 保存明细
			String[] pids = this.getRequest().getParameterValues("pid");
			String[] unitids = this.getRequest().getParameterValues("unitid");
			String[] inprices = this.getRequest().getParameterValues("inprice");
			String[] ncounts = this.getRequest().getParameterValues("ncount");
			String[] counts = this.getRequest().getParameterValues("count");
			String[] moneys = this.getRequest().getParameterValues("money");
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
    * 修改
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
			String jno = orderManager.getNo(PurchaseConstants.RETURNTYPE,
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
		/*User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		
		if (getModel().getId() == null) {
			String jno = orderManager.getNo(PurchaseConstants.RETURNTYPE,
					getModel().getUser().getId());
			getModel().setSno(jno);
			getModel().setSdate(DateUtil.getCurrentDate());
		}*/
		return "view";
	}

	/*
	 * 把明细传到页面
	 */
	public List<PurchaseDetail> getPurchaseDetail() {
		String orderid = getRequest().getParameter("oid");
		List<PurchaseDetail> sd = new ArrayList<PurchaseDetail>();
		if (orderid == null) {
			sd = this.getManager().getPurchaseDetail(getModel());
		} else {
			String hql = "from PurchaseDetail pd where  pd.purchase.id = ?";
			List<PurchaseDetail> orderDs = (List<PurchaseDetail>) this
					.getManager().getDao()
					.query(hql, Integer.parseInt(orderid));
			for (PurchaseDetail od : orderDs) {
				PurchaseDetail pd = new PurchaseDetail();
				pd.setAmount(od.getAmount());
				pd.setCount(od.getCount());
				pd.setLinkCount(od.getCount());
				pd.setNcount(od.getNcount());
				pd.setPrice(od.getPrice());
				pd.setProducts(od.getProducts());
				pd.setUnits(od.getUnits());
				sd.add(pd);
			}
		}
		this.getRequest().setAttribute("purchaseDetail", sd);
		return sd;
	}

	/**
	 * 查询列表
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
		args.add(PurchaseConstants.RETURNTYPE);
		args.add(user.getId());
		if (getModel() != null && getModel().getStatus() != null
				&& getModel().getStatus()!= 9) {
			sql.append(" and p.status = ?");
			args.add(getModel().getStatus());
		}
		if(getModel() == null || getModel().getStatus() == null){	
			getModel().setStatus(0);
			sql.append(" and p.status = ?");
			args.add(0);
		}
		if (getModel() != null && getModel().getSno() != null
				&& !getModel().getSno().equals("")) {
			sql.append(" and p.sno like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSno()));
		}
		if (getModel() != null && getModel().getSupplier() != null
				&& getModel().getSupplier().getName() != null
				&& !getModel().getSupplier().getName().equals("")
				) {
			sql.append(" and p.supplier.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSupplier().getName()));
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
		sql.append(" order by p.sno desc ");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
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

	/*
	 * 入库单自动生成退库单
	 */
	public String convertPurchase() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		String jno = orderManager.getNo(2, getModel().getUser());
		getModel().setSno(jno);
		getModel().setSdate(DateUtil.getCurrentDate());
		Integer orderid = Integer.parseInt(getRequest().getParameter("oid"));
		// 得到入库单数据
		String hql = "from Purchase o where o.id = ?";
		List order = this.getManager().getDao().query(hql, orderid);
		Purchase o = (Purchase) order.get(0);
		getModel().setSamount(o.getSamount());
		getModel().setSupplier(o.getSupplier());
		getModel().setOrderid(o.getId());
		getModel().setOrderno(o.getSno());
		getModel().setStorage(o.getStorage());
		getModel().setEmployee(o.getEmployee());
		return INPUT;
	}
	/**
	 * 显示正常和作废的map
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
	 * @return
	 */
	public String red(){
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		try {
			Integer id=getModel().getId();
			Purchase p=this.getManager().get(id);
			Purchase purchase=new Purchase();
			purchase.setIsover(0);
			purchase.setStatus(2);//冲红单
			purchase.setRemark("冲红单号："+p.getSno());
			String jno = orderManager.getNo(PurchaseConstants.RETURNTYPE,
					getModel().getUser());
			purchase.setSno(jno);
			purchase.setSdate(DateUtil.getCurrentDate());
			purchase.setSamount(-p.getSamount());
			purchase.setSpayamount(-p.getSpayamount());
			purchase.setSpayTotal(-p.getSpayTotal());
			purchase.setBillType(2);
			purchase.setEmployee(p.getEmployee());
			purchase.setPaytype(p.getPaytype());
			purchase.setStorage(p.getStorage());
			purchase.setSupplier(p.getSupplier());
			purchase.setUser(p.getUser());
            p.setStatus(1);//已冲红
            this.getManager().save(p);
            List<PurchaseDetail>  pdlist=returnsDetailManager.getDetails(id);
            List<PurchaseDetail>  npdlist=new ArrayList<PurchaseDetail>();
            
            for(PurchaseDetail pd:pdlist){
            	PurchaseDetail npd=new PurchaseDetail();
            	npd.setAmount(-pd.getAmount());
            	npd.setCount(-pd.getCount());
            	npd.setNcount(-pd.getNcount());
            	npd.setPrice(pd.getPrice());
            	npd.setProducts(pd.getProducts());
            	npd.setPurchase(purchase);
            	npd.setUnits(pd.getUnits());
            	npd.setRemark("");
                npdlist.add(npd);
            }
            this.getManager().save(purchase, npdlist);
		         
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INPUT;
}

}
