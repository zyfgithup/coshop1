package com.systop.amol.purchase.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.purchase.model.PayDetail;
import com.systop.amol.purchase.service.PayDetailManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;


/**
 * 付款单管理Manager
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayDetailAction extends DefaultCrudAction<PayDetail, PayDetailManager> {
	@Autowired
	private SupplierManager supplierManager;
	private String endDate;
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

	private String startDate;
	
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

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from PayDetail pd where pd.pay.status=0 and pd.pay.user.id=? ");
	
		List args = new ArrayList();
		args.add(user.getId());
		if (getModel() != null && getModel().getPurchase() != null
				&& !getModel().getPurchase().getSno().equals("")) {
			sql.append(" and pd.purchase.sno like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getPurchase().getSno()));			
		}
		if (getModel() != null && getModel().getPay()!=null && getModel().getPay().getNo()!=null
				&& !getModel().getPay().getNo().equals("")) {
			sql.append(" and pd.pay.no like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getPay().getNo()));			
		}
		if (getModel() != null && getModel().getPay()!=null && getModel().getPay().getSupplier() != null
				&& getModel().getPay().getSupplier().getName() != null
				&& !getModel().getPay().getSupplier().getName().equals("")) {
			sql.append(" and pd.pay.supplier.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getPay().getSupplier().getName()));
		}
		try {
			if (startDate != null && !startDate.equals("")) {
				sql.append(" and pd.purchase.sdate >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", startDate)));

			}
			if (endDate != null && !endDate.equals("")) {
				sql.append(" and pd.purchase.sdate <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
        sql.append("order by pd.pay.no desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());

		restorePageData(page);
		String nsql = "select sum(pd.purchase.samount),sum(pd.amount) "
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
