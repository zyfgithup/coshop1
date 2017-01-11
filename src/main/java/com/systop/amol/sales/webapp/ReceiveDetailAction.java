package com.systop.amol.sales.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.ReceiveDetail;
import com.systop.amol.sales.service.ReceiveDetailManager;
import com.systop.amol.sales.utils.Payment;
import com.systop.amol.util.DoubleFormatUtil;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 回款单明细管理Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReceiveDetailAction extends
		DefaultCrudAction<ReceiveDetail, ReceiveDetailManager> {

	/** 开始时间 */
	private String startDate;

	/** 结束时间 */
	private String endDate;

	/**
	 * 将回款单明细中的一条回款记录冲红
	 * 
	 * @author 王会璞
	 * @return
	 */
	public String redRed() {

		String SI = INDEX;

		ReceiveDetail receiveDetail = getManager().redRed(getModel().getId(),
				UserUtil.getPrincipal(getRequest()).getId());
		if (Payment.CASH.equals(receiveDetail.getPayment())
				|| Payment.CASHADVANCE.equals(receiveDetail.getPayment())) {
		} else if (Payment.CARD.equals(receiveDetail.getPayment())
				|| Payment.CARDADVANCE.equals(receiveDetail.getPayment())) {
			SI = "cardIndex";
		}
		return SI;
	}

	/**
	 * 显示查询列表 cash 现金
	 */
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		queryReciveDetailSales(user, "cash", null);
		return "cashRS";
	}
	
	/**
	 * 显示查询列表 card 代币卡
	 */
	public String cardIndex() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		queryReciveDetailSales(user, null, "card");
		return "cardRS";
	}
	
	/**
	 * 查询回款单明细中给一个出库单回款的明细
	 * 
	 * @param user
	 * @param cash
	 *          现金
	 * @param card
	 *          代币卡
	 */
	public void queryReciveDetailSales(User user, String cash, String card) {

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from ReceiveDetail rd where 1 = 1 ");
		List args = new ArrayList();

		if (StringUtils.isNotBlank(cash)) {
			sql.append(" and rd.payment in(?,?) ");
			args.add(Payment.CASH);
			args.add(Payment.CASHADVANCE);
		} else if (StringUtils.isNotBlank(card)) {
			sql.append(" and rd.payment in(?,?) ");
			args.add(Payment.CARD);
			args.add(Payment.CARDADVANCE);
		}

		if (user.getId() != null && user.getId() > 0) {
			sql.append(" and rd.user.id = ? ");
			args.add(user.getId());
		}

		if (getModel().getSales() != null && StringUtils.isNotBlank(getModel().getSales().getSalesNo())) {
			sql.append(" and rd.sales.salesNo = ? ");
			args.add(getModel().getSales().getSalesNo());
		}

		if (null != getModel().getCustomer()) {
			if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and rd.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and rd.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and rd.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (getModel() != null && getModel().getRedRed() != null
				&& getModel().getRedRed() != SalesConstants.ALL) {
			sql.append(" and rd.redRed = ?");
			args.add(getModel().getRedRed());
		}

		sql.append(" order by rd.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);

		List<ReceiveDetail> rdlist = page.getData();
		float amoutTotal = 0;
		for (ReceiveDetail rd : rdlist) {
			amoutTotal += DoubleFormatUtil.format(rd.getSpayamount());
		}
		getRequest().setAttribute("amountTotal", String.valueOf(amoutTotal));
	}

	/**
	 * 查询回款单明细中与一个期初应收关联的回款明细
	 * 
	 * @param user
	 * @param cash
	 *          现金
	 * @param card
	 *          代币卡
	 */
	public String queryRdRI() {

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from ReceiveDetail rd where rd.history = "+SalesConstants.BEGINNING);
		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		if (user.getId() != null && user.getId() > 0) {
			sql.append(" and rd.user.id = ? ");
			args.add(user.getId());
		}

//		if (StringUtils.isNotBlank(getModel().getReceiveNumber())) {
//			sql.append(" and rd.sales.salesNo = ? ");
//			args.add(getModel().getReceiveNumber());
//		}

		if (null != getModel().getCustomer()) {
			if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and rd.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and rd.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and rd.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (getModel() != null && getModel().getRedRed() != null
				&& getModel().getRedRed() != SalesConstants.ALL) {
			sql.append(" and rd.redRed = ?");
			args.add(getModel().getRedRed());
		}

		sql.append(" order by rd.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);

		List<ReceiveDetail> rdlist = page.getData();
		float amoutTotal = 0;
		for (ReceiveDetail rd : rdlist) {
			amoutTotal += DoubleFormatUtil.format(rd.getSpayamount());
		}
		getRequest().setAttribute("amountTotal", String.valueOf(amoutTotal));

		return "beginning";
	}
	
	/**
	 * 冲红map
	 * 
	 * @return
	 */
	public Map<Integer, String> getStatusMap() {
		return SalesConstants.STATUS_MAP;
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