package com.systop.amol.purchase.webapp;

import java.text.ParseException;
import java.util.ArrayList;
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
import com.systop.amol.purchase.model.Purchase;
import com.systop.amol.purchase.service.PurchaseManager;
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
public class SelectAction extends DefaultCrudAction<Purchase, PurchaseManager> {
	@Autowired
	private SupplierManager supplierManager;


	// 单据类型
	private String findType;
	// 单据所有者
	private String users;
	// 开始日期
	private String startDate;
	// 结束日期
	private String endDate;
	//订单入库类型
	private String isover;

	public String getIsover() {
		return isover;
	}

	public void setIsover(String isover) {
		this.isover = isover;
	}

	public String index() {
		if (findType != null && !findType.equals("")) {
			this.purchaseIndex();
		}
		return "index";
	}

	public String suplierindex() {
		if (findType != null && !findType.equals("")) {
			this.suplierpurchaseIndex();
		}
		return "suplierindex";
	}

	/**
	 * 厂家查询
	 * 
	 * @return
	 */
	public String suplierpurchaseIndex() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		String sname = user.getName();
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Purchase p where p.billType="
				+ findType + " and p.status=0 and p.supplier.name=?");
		
		List args = new ArrayList();
		args.add(sname);
		if (users != null && !users.equals("")) {
			sql.append(" and p.user.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(users));
		}
		try {
			
			if(findType !=null && findType.equals("0")){
				if(StringUtils.isNotBlank(isover) && !isover.equals("2")){
					sql.append(" and p.isover=? ");
					args.add(Integer.parseInt(isover));
				}
			}
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
		
		String nsql = "select sum(samount),sum(spayTotal) "
			+ sql.toString();
	List list = getManager().query(nsql, args.toArray());
	String sfTotal = "";
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
			
		}
	}

		getRequest().setAttribute("sfTotal", sfTotal);
		getRequest().setAttribute("payTotal", payTotal);
		return INDEX;
	}

	/**
	 * 采购查询
	 * 
	 * @return
	 */
	public String purchaseIndex() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		
		
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		List args = new ArrayList();
		// 判断登录的是顶级经销商还是末级经销商，如果是顶级可以看自己的和他的末级的，如果是末级只能看自己的。
		StringBuffer sql = new StringBuffer("from Purchase p where p.billType="
				+ findType + " and p.status=0 ");
		if(user.getSuperior()!=null){
			sql.append(" and p.user.id=?");
			args.add(user.getId());
		}else{
			sql.append(" and (p.user.id = ? or p.user.superior.id = ?) ");
			args.add(user.getId());
			args.add(user.getId());
		}

		
		if (StringUtils.isNotBlank(users)) {
			sql.append(" and p.user.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(users));
		}
		if (getModel() != null && getModel().getSupplier() != null
				&& getModel().getSupplier().getName() != null
				&& !getModel().getSupplier().getName().equals("")) {
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
		sql.append("order by p.sno desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		String nsql = "select sum(samount),sum(spayTotal) "
			+ sql.toString();
	List list = getManager().query(nsql, args.toArray());
	String sfTotal = "";
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
			
		}
	}

		getRequest().setAttribute("sfTotal", sfTotal);
		getRequest().setAttribute("payTotal", payTotal);
		return INDEX;
	}
   /**
    * 页面的选择列表
    * @return
    */
	public Map<String, String> getPselectMap() {
		return PurchaseConstants.PSELECT_MAP;
	}
	public Map<String, String> getSelectMap() {
		return PurchaseConstants.SELECT_MAP;
	}
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
	    * index页面上的订单入库种类
	    * @return
	    */
		public Map<Integer, String> getOrderMap() {
			return PurchaseConstants.ORDER_MAP;
		}
}
