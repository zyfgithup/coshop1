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
import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.base.units.model.Units;
import com.systop.amol.purchase.PurchaseConstants;
import com.systop.amol.purchase.model.Purchase;
import com.systop.amol.purchase.model.PurchaseDetail;
import com.systop.amol.purchase.service.OrderDetailManager;
import com.systop.amol.purchase.service.OrderManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 采购订单管理Manager
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrderAction extends DefaultCrudAction<Purchase, OrderManager> {

	@Autowired
	private SupplierManager supplierManager;
	// 订单明细信息
	@Autowired
	private OrderDetailManager orderDetailManager;

	// 开始时间
	private String startDate;

	// 结束时间
	private String endDate;

	/**
	 * 查询订单列表
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
				"from Purchase p where p.billType=? and p.user.id=? ");
		List args = new ArrayList();
		args.add(PurchaseConstants.ORDERTYPE);
		args.add(user.getId());
		if (getModel() != null && getModel().getIsover() != null) {
			if (getModel().getIsover() == PurchaseConstants.ORDER_NOTOVER_INT) {
				sql.append(" and p.isover = ? ");
				args.add(PurchaseConstants.ORDER_NOTOVER_INT);
			} else if (getModel().getIsover() == PurchaseConstants.ORDER_OVER_INT) {
				sql.append(" and p.isover = ? ");
				args.add(PurchaseConstants.ORDER_OVER_INT);
			}
			if (getModel().getIsover() == PurchaseConstants.ORDER_PARTOVER_INT) {
				sql.append(" and p.isover = ? ");
				args.add(PurchaseConstants.ORDER_PARTOVER_INT);
			}
		}
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

		
		String nsql="select sum(samount) "+sql.toString();
		List list=getManager().query(nsql, args.toArray());
		String total="";
		if(list.size()>0){
			for (Object o : list) {
				if(o != null){
				 total=o.toString();
				}
			}
		}
		getRequest().setAttribute("payTotal", total);
		

		return INDEX;
	}

	/**
	 * 编辑订单信息
	 */
	public String edit() {
		// 获得单号
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
			String jno = this.getManager().getNo(PurchaseConstants.ORDERTYPE,
					getModel().getUser());
			getModel().setSno(jno);
			getModel().setSdate(DateUtil.getCurrentDate());
		}
		return INPUT;
	}

	/**
	 * 查看订单信息
	 */
	public String view() {
		//判断如果是入库单选择订单时的查看没有返回按钮
		String isorder=this.getRequest().getParameter("isorder");
		if(isorder != null){
			this.getRequest().setAttribute("isorder", isorder);
		}
		return "view";
	}


	public String exportExcel() {
		this.edit();
		return "exportExcel";
	}

	/**
	 * 保存订单信息
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
			}
			getModel().setBillType(PurchaseConstants.ORDERTYPE);// 订单0；
			if (getModel().getIsover() == null) {
				getModel().setIsover(0);
			}
			if(getModel().getStatus()==null){
				getModel().setStatus(0);
			}
			// 保存订单明细
			String[] pids = this.getRequest().getParameterValues("pid");
			String[] unitids = this.getRequest().getParameterValues("unitid");
			String[] inprices = this.getRequest().getParameterValues("inprice");
			String[] ncounts = this.getRequest().getParameterValues("ncount");
			String[] counts = this.getRequest().getParameterValues("count");
			String[] moneys = this.getRequest().getParameterValues("money");
			String[] remarks = this.getRequest().getParameterValues("sremark");

			List<PurchaseDetail> sdlist = new ArrayList<PurchaseDetail>();
			for (int i = 1; i < pids.length; i++) {

				PurchaseDetail sd = new PurchaseDetail();
				sd.setAmount(Double.parseDouble((moneys[i])));
				sd.setNcount(Float.parseFloat(ncounts[i]));
				sd.setCount(Integer.parseInt(counts[i]));
				sd.setPrice(Float.parseFloat(inprices[i]));
				sd.setProducts(this.getManager().getDao()
						.get(Products.class, Integer.parseInt(pids[i])));
				sd.setRemark(remarks[i]);
				sd.setLinkCount(0);
				String[] uids = unitids[i].split(",");
				sd.setUnits(this.getManager().getDao()
						.get(Units.class, Integer.parseInt(uids[0])));
				sd.setPurchase(getModel());
				sdlist.add(sd);
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
	 * 删除订单信息
	 */
	public String remove() {
		this.getManager().remove(getModel());

		return SUCCESS;
	}

	/**
	 * 把订单明细传到页面
	 */
	public List<PurchaseDetail> getPurchaseDetail() {
		List<PurchaseDetail> sd = orderDetailManager.getDetails(getModel()
				.getId());
		this.getRequest().setAttribute("purchaseDetail", sd);
		return sd;
	}

	/**
	 * 查询列表在入库单选择订单时使用
	 */
	public String find() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from Purchase p where p.billType=? and (p.user.id = ? or p.user.superior.id = ?)  and p.isover<>1 and p.status=0");
		List args = new ArrayList();
		args.add(PurchaseConstants.ORDERTYPE);
		//一级登录
		if(user.getSuperior()==null){
		args.add(user.getId());
		args.add(user.getId());
		}else{
			args.add(user.getSuperior().getId());
			args.add(user.getSuperior().getId());
		}
		
		if (getModel() != null && getModel().getSno() != null
				&& !getModel().getSno().equals("")) {
			sql.append(" and p.sno like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSno()));
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
		return "find";
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
    * index页面上的订单入库种类
    * @return
    */
	public Map<Integer, String> getOrderMap() {
		return PurchaseConstants.ORDER_MAP;
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
			String jno = this.getManager().getNo(PurchaseConstants.ORDERTYPE,
					getModel().getUser());
			purchase.setSno(jno);
			purchase.setSdate(DateUtil.getCurrentDate());
			purchase.setSamount(-p.getSamount());
			purchase.setBillType(0);
			purchase.setEmployee(p.getEmployee());
			purchase.setPaytype(p.getPaytype());
			purchase.setStorage(p.getStorage());
			purchase.setSupplier(p.getSupplier());
			purchase.setUser(p.getUser());
            this.getManager().save(purchase);
            p.setStatus(1);//已冲红
            this.getManager().save(p);
            List<PurchaseDetail>  pdlist=orderDetailManager.getDetails(id);
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
            	npd.setLinkCount(0);
            	orderDetailManager.save(npd);
            }
		         
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INPUT;
}
}
