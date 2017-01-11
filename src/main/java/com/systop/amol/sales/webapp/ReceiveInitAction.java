package com.systop.amol.sales.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.customer.model.Customer;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.ReceiveInit;
import com.systop.amol.sales.service.ReceiveInitManager;
import com.systop.amol.util.DoubleFormatUtil;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 应收初始化管理Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReceiveInitAction extends
		DefaultCrudAction<ReceiveInit, ReceiveInitManager> {

	/**
	 * 访问数据库
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/** 开始时间 */
	private String startDate;

	/** 结束时间 */
	private String endDate;
	
	private String rel;

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
	 * 异步删除
	 * @return
	 */
	public String delete(){
		String receiveInitIdStr = getRequest().getParameter("receiveInitId");
		if(StringUtils.isNotBlank(receiveInitIdStr)){
				try {
					getManager().delete(new Integer(receiveInitIdStr));
				} catch (Exception e) {
					rel = e.getMessage();
				}
		}
		return "delete";
	}

	/**
	 * 修改或添加页面
	 */
	public String edit() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}

		List<ReceiveInit> receiveList = this.getManager().query(
				"from ReceiveInit where user.id=?", user.getId());

		if (receiveList.size() > 0) {
			this.getRequest().setAttribute("list", receiveList);
		}

		return INPUT;
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
			String ids[] = this.getRequest().getParameterValues("ids");
			String customerIds[] = this.getRequest().getParameterValues("customerId");
			String amounts[] = this.getRequest().getParameterValues("amount");
			List<ReceiveInit> riList = new ArrayList<ReceiveInit>();
			for (int i = 1; i < customerIds.length; i++) {
				if (!amounts[i].trim().equals("") && Float.parseFloat(amounts[i]) != 0) {
					ReceiveInit receiveInit = new ReceiveInit();
					receiveInit.setAmount(Double.parseDouble(amounts[i]));
					receiveInit.setAmountReceived(0.0d);
					receiveInit.setUser(user);
					receiveInit.setCustomer(this.getManager().getDao()
							.get(Customer.class, Integer.parseInt(customerIds[i])));
					if(!ids[i].trim().equals("")){
						receiveInit.setId(new Integer(ids[i]));
						receiveInit.setStatus(SalesConstants.INIT_LOCKING);
					}
					riList.add(receiveInit);
				}
			}

			getManager().save(riList);
			
			this.addActionMessage("保存成功！");
			return SUCCESS;
		} catch (ApplicationException e) {
			e.printStackTrace();
			this.addActionMessage("保存成功！");
			return INPUT;
		}
	}

	/**
	 * 查询列表
	 */
	public String index() {
		try {
			User user = UserUtil.getPrincipal(getRequest());
			if (user != null) {
				getModel().setUser(user);
			} else {
				this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
				return INPUT;
			}

			Page page = PageUtil.getPage(getPageNo(), getPageSize());
			List args = new ArrayList();
			StringBuffer sql = new StringBuffer(
					"from ReceiveInit ri where ri.user.id=? ");

			args.add(user.getId());

			if (getModel().getCustomer() != null
					&& StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and ri.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}

			try {
				if (StringUtils.isNotBlank(startDate)) {
					sql.append(" and ri.createTime >= ?");
					args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
							"yyyy-MM-dd", startDate)));
				}
				if (StringUtils.isNotBlank(endDate)) {
					sql.append(" and ri.createTime <= ?");
					args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
							"yyyy-MM-dd", endDate)));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			sql.append(" order by ri.createTime desc, ri.id desc");
			page = getManager().pageQuery(page, sql.toString(), args.toArray());
			restorePageData(page);

			String nsql = "select sum(amount),sum(amountReceived) " + sql;
			List list = getManager().query(nsql, args.toArray());
			Double total = 0.0d;
			Double amountReceivedSum = 0.0d;
			if (list.size() > 0) {
				for (Object o : list) {
					Object[] oo = (Object[]) o;
					if (oo[0] != null) {
						total = DoubleFormatUtil.format(new Double(oo[0].toString()));
					}
					if (oo[1] != null) {
						amountReceivedSum = DoubleFormatUtil.format(new Double(oo[1].toString()));
					}
				}
			}
			getRequest().setAttribute("amountTotal", total);
			getRequest().setAttribute("amountReceivedSum", amountReceivedSum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return INDEX;
	}

	/**
	 * 清空初始化数据，条形码     （ 订单  出库单  退货单）详情      回款单详情      回款单    （ 订单  出库单  退货单）单子      期初应收单
	 * @return
	 */
	public String clear() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return SUCCESS;
		}
	  // 条形码
		jdbcTemplate.execute("delete b from barcodes b,sales_details sd where b.sales_detail_id = sd.id and user_id="
				+ user.getId());
	  // 销售明细（订单  出库单  退货单）
		jdbcTemplate.execute("delete sd from sales_details sd,sales s where sd.sales_id = s.id and user_id="
				+ user.getId());
  	// 回款详情
		jdbcTemplate.execute("delete rd from receive_details rd,receives r where rd.receive_id = r.id and user_id="
				+ user.getId());
	  // 回款单
		jdbcTemplate.execute("delete from receives where user_id="
				+ user.getId());
		// 订单  出库单  退货单
		jdbcTemplate.execute("delete from sales where user_id="
				+ user.getId());
	  // 期初应收单
		jdbcTemplate.execute("delete from receive_init where user_id="
				+ user.getId());
		this.getManager().getDao().getHibernateTemplate().clear();
		this.addActionMessage("清除完毕！");
		return SUCCESS;
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

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}
}