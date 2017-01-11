package com.systop.amol.purchase.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ibm.icu.text.SimpleDateFormat;
import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.purchase.model.PurchaseDetail;
import com.systop.amol.purchase.model.PurchaseDetailComparator;
import com.systop.amol.purchase.service.PurchaseDetailManager;
import com.systop.amol.stock.StockConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 入库明细Manager
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PurchaseDetailAction extends
		DefaultCrudAction<PurchaseDetail, PurchaseDetailManager> {
	
	@Autowired
	private SupplierManager supplierManager;
	//结束日期
	private String endDate;
	//开始日期
	private String startDate;

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	

	/**
	 * 查询列表
	 */
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		
		StringBuffer sql = new StringBuffer(
				"from PurchaseDetail pd where  pd.purchase.status=0 and pd.purchase.user.id=? and pd.purchase.billType=1");
		List args = new ArrayList();
		args.add(user.getId());
		if (getModel() != null && getModel().getPurchase() != null
				&& !getModel().getPurchase().getSno().equals("")) {
			sql.append(" and pd.purchase.sno like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getPurchase()
					.getSno()));
		}
		if (getModel() != null && getModel().getPurchase()!=null && getModel().getPurchase().getSupplier() != null
				&& getModel().getPurchase().getSupplier().getName() != null
				&& !getModel().getPurchase().getSupplier().getName().equals("")) {
			sql.append(" and pd.purchase.supplier.name like ?");
			args.add("%" + getModel().getPurchase().getSupplier().getName() + "%");
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
			if (startDate != null && !startDate.equals("")) {
				sql.append(" and pd.purchase.sdate >= ?");
				args.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(startDate + " 00:00:00"));
			}
			if (endDate != null && !endDate.equals("")) {
				sql.append(" and pd.purchase.sdate <= ?");
				args.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(endDate + " 23:59:59"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sql.append(" order by pd.purchase.sno desc");
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

		getRequest().setAttribute("countTotal", countTotal);
		getRequest().setAttribute("amoutTotal", amoutTotal);
		return INDEX;
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
