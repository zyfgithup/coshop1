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

import com.systop.amol.base.supplier.model.Supplier;
import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.purchase.PurchaseConstants;
import com.systop.amol.purchase.model.Pay;
import com.systop.amol.purchase.model.PayDetail;
import com.systop.amol.purchase.model.PayInit;
import com.systop.amol.purchase.model.Purchase;
import com.systop.amol.purchase.service.OrderManager;
import com.systop.amol.purchase.service.PayDetailManager;
import com.systop.amol.purchase.service.PayManager;
import com.systop.amol.user.AmolUserConstants;
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
@SuppressWarnings({"serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayAction extends DefaultCrudAction<Pay, PayManager> {

	@Autowired
	private SupplierManager supplierManager;
	// 付款单明细
	@Autowired
	private PayDetailManager payDetailManager;
	
	//开始时间
	private String startDate;
	
	//结束时间
	private String endDate;
	
	@Autowired
	private OrderManager orderManager;

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
		StringBuffer sql = new StringBuffer("from Pay c where c.user.id=?");

		List args = new ArrayList();
		args.add(user.getId());
		if (getModel() != null && getModel().getStatus() != null
				&& getModel().getStatus()!= 9) {
			sql.append(" and c.status = ?");
			args.add(getModel().getStatus());
		}
		if(getModel() == null || getModel().getStatus() == null){	
			getModel().setStatus(0);
			sql.append(" and c.status = ?");
			args.add(0);
		}
		if (getModel() != null && getModel().getNo() != null
				&& !getModel().getNo().equals("")) {
			sql.append(" and c.no like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getNo()));
		}
		if (getModel() != null && getModel().getSupplier() != null
				&& getModel().getSupplier().getName() != null
				&& !getModel().getSupplier().getName().equals("")) {
			sql.append(" and c.supplier.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSupplier().getName()));
		}
		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and c.outDate >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and c.outDate <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		   sql.append("  order by c.no desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
	
		String nsql="select sum(amount) "+sql.toString();
		List list=getManager().query(nsql, args.toArray());
		String total="";
		if(list.size()>0){
			for (Object o : list) {
				if(o != null){
				 total=o.toString();
				}
			}
		}
	
		getRequest().setAttribute("amountTotal", String.valueOf(total));
		
		return INDEX;
	}

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
			String jno=orderManager.getNo(PurchaseConstants.PAYTYPE, getModel().getUser());
			getModel().setNo(jno);
			getModel().setOutDate(DateUtil.getCurrentDate());
			
		} else {
			List<PayDetail> payDetailList = payDetailManager
					.getPayDetails(getModel().getId());
			for(PayDetail pd:payDetailList){
				if(pd.getPayInit()!=null){
					PayInit s=pd.getPayInit();
					Purchase p=new Purchase();
					p.setSno("期初");
				    p.setSdate(s.getPdate());
					p.setBillType(0);
					p.setSamount(s.getAmount());
					p.setSpayTotal(s.getPayamount());
					pd.setPurchase(p);
				}
			}
			
			this.getRequest().setAttribute("list", payDetailList);
			
		}


		
		return INPUT;
	}

	
	public String view(){
		this.edit();
		return "view";
	}
	// 保存
	@Override
	public String save() {

		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		//try {

			// 如果用户没有输入操作员则设置为空
			if (getModel().getEmployee().getName().trim().equals("")) {
				getModel().setEmployee(null);
			}
			if(getModel().getAmount() == null){
				getModel().setAmount(0.0d);
			}
			if(getModel().getStatus()==null){
				getModel().setStatus(0);
			}
			getModel().setSupplier(
					this.getManager().getDao()
							.get(Supplier.class, getModel().getSupplier().getId()));

			String stockid[] = this.getRequest().getParameterValues("stockid");
			String pid[] = this.getRequest().getParameterValues("payinitid");
			String amounts[] = this.getRequest().getParameterValues("amount1");
			String payAmounts[] = this.getRequest().getParameterValues("payAmount");
			List<PayDetail> paylist = new ArrayList<PayDetail>();
			for (int i = 0; i < stockid.length; i++) {
				if (!amounts[i].trim().equals("") && Double.parseDouble(amounts[i]) != 0 ) {
					PayDetail od = new PayDetail();
					od.setAmount(Double.parseDouble(amounts[i]));
					od.setPayAmount(Double.parseDouble(payAmounts[i]));
					od.setPay(getModel());
					if(stockid[i].equals("null")||Integer.parseInt(stockid[i])==0){
						od.setPurchase(null);//期初
						od.setPayInit(this.getManager().getDao()
								.get(PayInit.class, Integer.parseInt(pid[i])));
					}else{
						od.setPurchase(this.getManager().getDao()
								.get(Purchase.class, Integer.parseInt(stockid[i])));
					}
					paylist.add(od);

				}
			}

			getManager().save(getModel(), paylist);

			return SUCCESS;
		//} catch (Exception e) {
		//	e.printStackTrace();
		//	addActionError("保存失败"+e.getMessage());
		//	return INPUT;
		//}
	}

	/*
	 * 页面上点按钮加载入库单数据的时候调用
	 */
	public String getPurchaseValue() {

		if (getModel().getSupplier() != null) {
			User user = UserUtil.getPrincipal(getRequest());
			List<PayDetail> payDetailList = payDetailManager.getPurchaseValue(
					user, getModel().getSupplier().getId());
			this.getRequest().setAttribute("list", payDetailList);
		}
		return INPUT;
	}

	// 删除
	public String remove() {
		this.getManager().remove(getModel());

		return SUCCESS;
	}
	/**
	 * 导出到excel；
	 * @return
	 */
	public String exportExcel(){
	  	this.edit();
	  	return "exportExcel";
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
			Pay p=this.getManager().get(id);
			Pay pay=new Pay();
			pay.setAmount(-p.getAmount());
			pay.setEmployee(p.getEmployee());
			pay.setLinkAmount(-p.getLinkAmount());
			pay.setRemark("冲红单号："+p.getNo());
			pay.setStatus(2);
			pay.setSupplier(p.getSupplier());
			pay.setUser(p.getUser());
			String jno=orderManager.getNo(PurchaseConstants.PAYTYPE, getModel().getUser());
			pay.setNo(jno);
			pay.setOutDate(DateUtil.getCurrentDate());
			
            p.setStatus(1);//已冲红
            this.getManager().save(p);
            List<PayDetail>  pdlist=payDetailManager.getPayDetails(id);
            List<PayDetail>  npdlist=new ArrayList<PayDetail>();
            
            for(PayDetail pd:pdlist){
            	PayDetail npd=new PayDetail();
            	npd.setAmount(-pd.getAmount());
            	npd.setPay(pay);
            	npd.setPayAmount(pd.getPayAmount());
            	if(pd.getPurchase()!=null && pd.getPurchase().getId()!=0){
            	npd.setPurchase(this.getManager().getDao().get(Purchase.class,pd.getPurchase().getId()));
            	}
            	if(pd.getPayInit()!=null && pd.getPayInit().getId()!=0){
                	npd.setPayInit(this.getManager().getDao().get(PayInit.class,pd.getPayInit().getId()));
                	}
                npdlist.add(npd);
            }
            this.getManager().save(pay, npdlist);
		         
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INPUT;
}

}
