package com.systop.amol.purchase.webapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.purchase.model.PayDetail;
import com.systop.amol.purchase.model.PayInit;
import com.systop.amol.purchase.model.PayTotal;
import com.systop.amol.purchase.model.PayTotalComparator;
import com.systop.amol.purchase.model.Purchase;
import com.systop.amol.purchase.service.PayDetailManager;
import com.systop.amol.purchase.service.PayInitManager;
import com.systop.amol.purchase.service.PayTotalManager;
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
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayTotalAction extends
		DefaultCrudAction<PayTotal, PayTotalManager> {
	@Autowired
	private SupplierManager supplierManager;
	@Autowired
	private PurchaseManager purchaseManager;
	@Autowired
	private PayInitManager payInitManager;
	@Autowired
	private PayDetailManager payDetailManager;
	// 开始日期
	private String startDate;
	// 结束日期
	private String endDate;
	// 单价编号
	private String billNo;
	// 供应商名称
	private String supplier;
	// 状态0全部1未完成付款
	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
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

	@Override
	public String index() {
		try {
			User user = UserUtil.getPrincipal(getRequest());
			if (user != null) {

			} else {
				this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
				return INDEX;
			}
			// 进货
			List args = new ArrayList();
			List initargs = new ArrayList();// 期初
			List yfargs = new ArrayList();
			StringBuffer hql = null;
			StringBuffer inithql = null;
			StringBuffer yfhql = null;
			
				
				if(user.getType().equals("employee")){
					hql = new StringBuffer(
							"from Purchase pd where pd.status=0 "
									+ "and (pd.billType=1 or pd.billType=2)  and (pd.user.id=? or pd.user.superior.id=?)");
					args.add(user.getSuperior().getId());
					args.add(user.getSuperior().getId());
					yfhql = new StringBuffer(
							"from PayDetail pd where pd.pay.status=0 "
									+ "  and (pd.pay.user.id=? or pd.pay.user.superior.id=?)");
					yfargs.add(user.getSuperior().getId());
				yfargs.add(user.getSuperior().getId());
					inithql = new StringBuffer(
					"from PayInit pd where  (pd.user.id=?) ");
					initargs.add(user.getSuperior().getId());
				}else{	
					hql = new StringBuffer(
							"from Purchase pd where pd.status=0 "
									+ "and (pd.billType=1 or pd.billType=2)  and (pd.user.id=? or pd.user.superior.id=?)");
					args.add(user.getId());
					args.add(user.getId());
					
					yfhql = new StringBuffer(
							"from PayDetail pd where pd.pay.status=0 "
									+ "  and (pd.pay.user.id=? or pd.pay.user.superior.id=?)");
					yfargs.add(user.getId());	
				yfargs.add(user.getId());
					
				inithql = new StringBuffer(
						"from PayInit pd where  (pd.user.id=? or pd.user.superior.id=?) ");
				initargs.add(user.getId());
				initargs.add(user.getId());
				}
				
			if (billNo != null && !billNo.equals("")) {
				hql.append(" and pd.sno like ?");
				yfhql.append(" and pd.purchase.sno like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(billNo));
				yfargs.add(MatchMode.ANYWHERE.toMatchString(billNo));
			}
			if (supplier != null && !supplier.equals("")) {
				hql.append(" and pd.supplier.name like ?");
				inithql.append(" and pd.supplier.name  like ?");
				yfhql.append(" and pd.pay.supplier.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(supplier));
				initargs.add(MatchMode.ANYWHERE.toMatchString(supplier));
				yfargs.add(MatchMode.ANYWHERE.toMatchString(supplier));
			}
			if (status == null || status == 1) {
				status = 1;
				hql.append(" and pd.samount-pd.spayTotal<>0 ");
				inithql.append(" and pd.amount-pd.payamount<>0");
			}
			try {
				if (StringUtils.isNotBlank(startDate)) {
					hql.append(" and pd.sdate >= ?");
					inithql.append(" and pd.pdate >= ?");
					yfhql.append(" and pd.pay.outDate >= ?");
					yfargs.add(DateUtil.firstSecondOfDate(DateUtil
							.convertStringToDate("yyyy-MM-dd", startDate)));
					initargs.add(DateUtil.firstSecondOfDate(DateUtil
							.convertStringToDate("yyyy-MM-dd", startDate)));
					args.add(DateUtil.firstSecondOfDate(DateUtil
							.convertStringToDate("yyyy-MM-dd", startDate)));
				}
				if (StringUtils.isNotBlank(endDate)) {
					hql.append(" and pd.sdate <= ?");
					inithql.append(" and pd.pdate <= ?");
					yfhql.append(" and pd.pay.outDate <= ?");
					args.add(DateUtil.lastSecondOfDate(DateUtil
							.convertStringToDate("yyyy-MM-dd", endDate)));
					initargs.add(DateUtil.lastSecondOfDate(DateUtil
							.convertStringToDate("yyyy-MM-dd", endDate)));
					yfargs.add(DateUtil.lastSecondOfDate(DateUtil
							.convertStringToDate("yyyy-MM-dd", endDate)));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			List<Purchase> pdlist = purchaseManager.query(hql.toString(),
					args.toArray());
			List<PayTotal> ptlist = new ArrayList<PayTotal>();
			for (Purchase pd : pdlist) {
				PayTotal pt = new PayTotal();
				pt.setSupplier(pd.getSupplier().getName());
				pt.setId(pd.getId());
				pt.setBillNo(pd.getSno());
				if (pd.getBillType() == 1) {
					pt.setBillType("入库");
					pt.setPay(pd.getSamount());
					pt.setNowpay(pd.getSpayTotal());
					pt.setBcpay(pd.getSpayamount());
				} else {
					pt.setSupplier(pd.getSupplier().getName());
					pt.setBillType("退货");
					pt.setPay(-pd.getSamount());
					pt.setNowpay(-pd.getSpayTotal());
					pt.setBcpay(-pd.getSpayamount());
				}
				pt.setDate(new SimpleDateFormat("yyyy-MM-dd").format(pd
						.getSdate()));
				ptlist.add(pt);
			}
			List<PayInit> pilist = payInitManager.query(inithql.toString(),
					initargs.toArray());
			for (PayInit pd : pilist) {
				PayTotal pt = new PayTotal();
				pt.setSupplier(pd.getSupplier().getName());
				pt.setId(pd.getId());
				pt.setBillNo(" 期初" + pd.getId());
				pt.setBillType("期初");
				pt.setDate(new SimpleDateFormat("yyyy-MM-dd").format(pd
						.getPdate()));
				pt.setPay(pd.getAmount());
				pt.setNowpay(pd.getPayamount());
				pt.setBcpay(0d);
				ptlist.add(pt);
			}

			List<PayDetail> paylist = payDetailManager.query(yfhql.toString(),
					yfargs.toArray());
			for (PayDetail pd : paylist) {
				PayTotal pt = new PayTotal();
				if (pd.getPayInit() != null) {
					pt.setBillNo(" 期初" + pd.getPayInit().getId());
				} else {
					pt.setBillNo(pd.getPurchase().getSno());
				}
				// 未付款时，只出现入库单对应的付款单
				if (status != null && status == 1) {
					boolean flag = false;
					for (PayTotal ptl : ptlist) {
						if (ptl.getBillNo().equals(pt.getBillNo())) {
							flag = true;
							break;
						}
					}
					if (flag) {
						pt.setSupplier(pd.getPay().getSupplier().getName());
						pt.setId(pd.getPay().getId());
						pt.setBillType("付款");
						pt.setDate(new SimpleDateFormat("yyyy-MM-dd").format(pd
								.getPay().getOutDate()));
						pt.setPay(0d);
						pt.setNowpay(0d);
						pt.setBcpay(pd.getAmount());
						ptlist.add(pt);
					}
				} else {

					pt.setSupplier(pd.getPay().getSupplier().getName());
					pt.setId(pd.getPay().getId());
					pt.setBillType("付款");
					pt.setDate(new SimpleDateFormat("yyyy-MM-dd").format(pd
							.getPay().getOutDate()));
					pt.setPay(0d);
					pt.setNowpay(0d);
					pt.setBcpay(pd.getAmount());
					ptlist.add(pt);
				}
			}
			PayTotalComparator ptc = new PayTotalComparator();
			Collections.sort(ptlist, ptc);

			Page page = PageUtil.getPage(getPageNo(), getPageSize());
			page.setData(ptlist);
			restorePageData(page);

			List<PayTotal> list = new ArrayList<PayTotal>();
			list = page.getData();
			Double payTotal = 0d;
			Double bcTotal = 0d;
			Double nowTotal = 0d;
			for (PayTotal p : list) {
				payTotal += p.getPay();
				bcTotal += p.getBcpay();
				nowTotal += p.getNowpay();
			}
			getRequest().setAttribute("payTotal", String.valueOf(payTotal));
			getRequest().setAttribute("bcTotal", String.valueOf(bcTotal));
			getRequest().setAttribute("nowTotal", String.valueOf(nowTotal));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
