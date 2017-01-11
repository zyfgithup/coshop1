package com.systop.amol.purchase.webapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.systop.amol.base.supplier.model.Supplier;
import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.purchase.model.PayInit;
import com.systop.amol.purchase.service.PayInitManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 付款初始化管理Manager
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings({"serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayInitAction extends DefaultCrudAction<PayInit, PayInitManager> {
	@Autowired
	private SupplierManager supplierManager;
	
	private Map<String, Object> result;	
	
	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public String init(){
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
	   if(user.getBeginningInit()==null || user.getBeginningInit().equals("0")){
		   return edit();
	   }else{
		return index();
	}
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
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		List args = new ArrayList();
		args.add(user.getId());

		StringBuffer sql=new StringBuffer("from PayInit p where p.user.id=? ");
	
	
		if (getModel() != null && getModel().getSupplier() != null
				&& getModel().getSupplier().getName() != null
				&& !getModel().getSupplier().getName().equals("")) {
			sql.append(" and p.supplier.name like ?");
			args.add("%"+getModel().getSupplier().getName()+"%");
		}
		page = getManager().pageQuery(page,sql.toString() ,args.toArray());
		
		restorePageData(page);	
		
		String nsql="select sum(amount) "+sql;
		List list=getManager().query(nsql, args.toArray());
		String total="";
		if(list.size()>0){
			for (Object o : list) {
				if(o != null){
				 total=o.toString();
				}
			}
		}
		getRequest().setAttribute("amountTotal", total);

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

		
			List<PayInit> payList =this.getManager().query("from PayInit where user.id=?", user.getId());
			
				if(payList.size()>0){
			this.getRequest().setAttribute("list", payList);
				}



		return INPUT;
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
		try {
			String supplier[] = this.getRequest().getParameterValues("sid");
			String amounts[] = this.getRequest().getParameterValues("amount");
			String status[] = this.getRequest().getParameterValues("status1");
			List<PayInit> paylist = new ArrayList<PayInit>();
			for (int i = 1; i < supplier.length; i++) {
				if (!amounts[i].trim().equals("") && Float.parseFloat(amounts[i]) != 0 ) {
					PayInit od = new PayInit();
					od.setStatus(Integer.parseInt(status[i]));
					od.setPdate(DateUtil.getCurrentDate());
					od.setAmount(Double.parseDouble(amounts[i]));
					od.setPayamount(0d);
					od.setPdate(DateUtil.getCurrentDate());
		            od.setUser(user);
					od.setSupplier(this.getManager().getDao()
							.get(Supplier.class, Integer.parseInt(supplier[i])));
					paylist.add(od);

				}
			}
            
			getManager().save(user.getId(),paylist);
            
			this.addActionMessage("保存成功！");
			return "init";
		} catch (Exception e) {
			e.printStackTrace();
			addActionError("保存失败！");
			return INPUT;
		}
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
	public String delsup(){
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前用户不存在");
		Assert.notNull(getModel());
		
		result = new HashMap<String, Object>();
		result.put("success", true);
		try{
			if(getModel().getSupplier() != null &&  getModel().getSupplier().getId() != null ){
				getManager().del(getModel().getId());	
			}else{
				result.put("success", false);
				
			}	
		}catch (Exception e) {
			result.put("success", false);
			result.put("msg", "发生异常，删除失败！");
			e.printStackTrace();
		}
		

		return "jsonSuccess";
	}

}
