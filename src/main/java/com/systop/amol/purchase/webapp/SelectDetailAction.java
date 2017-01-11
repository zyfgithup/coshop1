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

import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.purchase.PurchaseConstants;
import com.systop.amol.purchase.model.PurchaseDetail;
import com.systop.amol.purchase.model.PurchaseDetailComparator;
import com.systop.amol.purchase.service.PurchaseDetailManager;
import com.systop.amol.stock.StockConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 入库明细Manager
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings({"serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SelectDetailAction extends
		DefaultCrudAction<PurchaseDetail,PurchaseDetailManager> {
	@Autowired
	private SupplierManager supplierManager;
	
	@Autowired
	private UserManager userManager;
	//单据类型
	private String findType;
	//单据所有者
	private String users;
	//开始日期
	private String startDate;
	//结束日期
	private String endDate;
	
	public String getFindType() {
		return findType;
	}
	public void setFindType(String findType) {
		this.findType = findType;
	}
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
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
	 * 采购明细查询
	 * @return
	 */
	public String indexDetail(){
		if(findType != null && !findType.equals("")){
				this.purchaseIndexDetail();
		}
		return "indexDetail";
	}
	/**
	 * 供应商明细查询
	 * @return
	 */
	public String suplierindexDetail(){
		if(findType != null && !findType.equals("")){
				this.suplierpurchaseIndexDetail();
		}
		return "suplierindexDetail";
	}
	/**
	 * 厂家查询详单
	 */
	public void suplierpurchaseIndexDetail(){
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {

		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return;
		}
		//判断登录的是顶级经销商还是末级经销商，如果是顶级可以看自己的和他的末级的，如果是末级只能看自己的。
		String sname=user.getName();
		StringBuffer sql = new StringBuffer(
				"from PurchaseDetail pd where pd.purchase.status=0 and pd.purchase.billType="
				+findType+" and pd.purchase.supplier.name=?");
		List args = new ArrayList();
		args.add(sname);
			
		if(users!=null && !users.equals("")){
			sql.append(" and pd.purchase.user.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(users));
		}
		if (getModel() != null && getModel().getProducts() != null
				&& !getModel().getProducts().getName().equals("")) {
			sql.append(" and pd.products.name like ?");
			args.add("%" + getModel().getProducts().getName() + "%");
		}
		if (getModel() != null && getModel().getProducts() != null
				&& !getModel().getProducts().getCode().equals("")) {
			sql.append(" and pd.products.code like ?");
			args.add("%" + getModel().getProducts().getCode() + "%");
		}
		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and pd.purchase.sdate >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and pd.purchase.sdate <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
        sql.append("order by pd.products.id desc");

	
		//每个商品加上小计
		List<PurchaseDetail> pdlist=this.getManager().query(sql.toString(), args.toArray());
		Map<Integer,PurchaseDetail> pdmap=new HashMap<Integer,PurchaseDetail>();	
		for(PurchaseDetail pd:pdlist){
			pd.setBagCount(StockConstants.getUnitPack(getServletContext(), pd.getProducts().getId(),pd.getCount()));
			if (pdmap.containsKey(pd.getProducts().getId())) {
				PurchaseDetail  p=pdmap.get(pd.getProducts().getId());
				p.setCount(p.getCount()+pd.getCount());
				p.setAmount(p.getAmount()+pd.getAmount());
			}else{
				PurchaseDetail p=new PurchaseDetail();
				p.setProducts(pd.getProducts());
				p.setAmount(pd.getAmount());
				p.setCount(pd.getCount());
				pdmap.put(pd.getProducts().getId(), p);
			}
		}
		java.util.Iterator it = pdmap.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			PurchaseDetail pt = (PurchaseDetail) entry.getValue();
			pt.setBagCount(StockConstants.getUnitPack(getServletContext(), pt.getProducts().getId(),pt.getCount()));
			pdlist.add(pt);
		}
		
		PurchaseDetailComparator ptc=new PurchaseDetailComparator();
		Collections.sort(pdlist, ptc);
		
	
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		page.setData(pdlist);
		restorePageData(page);
		
		
		
		String nsql = "select sum(pd.ncount),sum(pd.amount) "
			+ sql.toString();
	List list = getManager().query(nsql, args.toArray());
	String countTotal = "";
	String amoutTotal = "";
	if (list.size() > 0) {
		for (Object o : list) {
			Object[] oo = (Object[]) o;
			if (oo[0] != null) {
				countTotal = oo[0].toString();
			}
			if (oo[1] != null) {
				amoutTotal = oo[1].toString();
			}
			
		}
	}

		getRequest().setAttribute("countTotal", String.valueOf(countTotal));
		getRequest().setAttribute("amoutTotal", String.valueOf(amoutTotal));
	}
	/**
	 * 详单
	 */
	public void purchaseIndexDetail(){
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {

		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return;
		}
		//判断登录的是顶级经销商还是末级经销商，如果是顶级可以看自己的和他的末级的，如果是末级只能看自己的。
		int u=user.getId();
		String usersid=user.getId().toString();
		List<User> uids=userManager.query("from User u where u.superior="+ u);
		System.out.println(uids.size());
		if(uids.size()>0){
		  for(User o:uids){
			usersid+=","+o.getId();
		  }
		}

		StringBuffer sql = new StringBuffer(
				"from PurchaseDetail pd where  pd.purchase.status=0 and pd.purchase.billType="+findType+" and pd.purchase.user.id in ("+usersid+") ");
		List args = new ArrayList();
		//args.add(user.getId());
			
		if(users!=null && !users.equals("")){
			sql.append(" and pd.purchase.user.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(users));
		}
		if (getModel() != null && getModel().getProducts() != null
				&& !getModel().getProducts().getName().equals("")) {
			sql.append(" and pd.products.name like ?");
			args.add("%" + getModel().getProducts().getName() + "%");
		}
		if (getModel() != null && getModel().getProducts() != null
				&& !getModel().getProducts().getCode().equals("")) {
			sql.append(" and pd.products.code like ?");
			args.add("%" + getModel().getProducts().getCode() + "%");
		}
		if (getModel() != null && getModel().getPurchase().getSupplier() != null
				&& getModel().getPurchase().getSupplier().getName() != null
				&& !getModel().getPurchase().getSupplier().getName().equals("")) {
			sql.append(" and pd.purchase.supplier.name like ?");
			args.add("%"+getModel().getPurchase().getSupplier().getName()+"%");
		}
		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and pd.purchase.sdate >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and pd.purchase.sdate <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
        sql.append("order by pd.purchase.sno desc");
        List<PurchaseDetail> pdlist=this.getManager().query(sql.toString(), args.toArray());
		Map<Integer,PurchaseDetail> pdmap=new HashMap<Integer,PurchaseDetail>();	
		for(PurchaseDetail pd:pdlist){
			pd.setBagCount(StockConstants.getUnitPack(getServletContext(), pd.getProducts().getId(),pd.getCount()));
			if (pdmap.containsKey(pd.getProducts().getId())) {
				PurchaseDetail  p=pdmap.get(pd.getProducts().getId());
				p.setCount(p.getCount()+pd.getCount());
				p.setAmount(p.getAmount()+pd.getAmount());
			}else{
				PurchaseDetail p=new PurchaseDetail();
				p.setProducts(pd.getProducts());
				p.setAmount(pd.getAmount());
				p.setCount(pd.getCount());
				pdmap.put(pd.getProducts().getId(), p);
			}
		}
		java.util.Iterator it = pdmap.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			PurchaseDetail pt = (PurchaseDetail) entry.getValue();
			pt.setBagCount(StockConstants.getUnitPack(getServletContext(), pt.getProducts().getId(),pt.getCount()));
			pdlist.add(pt);
		}
		
		PurchaseDetailComparator ptc=new PurchaseDetailComparator();
		Collections.sort(pdlist, ptc);
		
	
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		page.setData(pdlist);
		restorePageData(page);
		
		

		String nsql = "select sum(pd.count),sum(pd.amount) "
			+ sql.toString();
	List list = getManager().query(nsql, args.toArray());
	String countTotal = "";
	String amoutTotal = "";
	if (list.size() > 0) {
		for (Object o : list) {
			Object[] oo = (Object[]) o;
			if (oo[0] != null) {
				countTotal = oo[0].toString();
			}
			if (oo[1] != null) {
				amoutTotal = oo[1].toString();
			}
			
		}
	}

		getRequest().setAttribute("countTotal", String.valueOf(countTotal));
		getRequest().setAttribute("amoutTotal", String.valueOf(amoutTotal));
	}
	//页面上的查询选项
	public Map<String, String> getPselectMap(){
		return 	PurchaseConstants.PSELECT_MAP;
		}
	public Map<String, String> getSelectMap(){
		return 	PurchaseConstants.SELECT_MAP;
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
}
