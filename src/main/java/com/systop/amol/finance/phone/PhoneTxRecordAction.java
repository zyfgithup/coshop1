package com.systop.amol.finance.phone;

import com.systop.amol.finance.model.TiXianRecord;
import com.systop.amol.finance.service.TiXianRecordManager;
import com.systop.amol.sales.service.SalesManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.*;
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneTxRecordAction extends DefaultCrudAction<TiXianRecord, TiXianRecordManager>{
	@Autowired
	private UserManager userManager;
	private Map<String,Object> result=new HashMap<String,Object>();
	private List<TiXianRecord> list=new ArrayList<TiXianRecord>();
	@Autowired
	private SalesManager salesManager;
	private String pageNumber = "1";
	private String pageCount = "10";
	public String addTxRecord(){
		Double txJe=Double.parseDouble(getRequest().getParameter("txJe"));
		User user= userManager.get(Integer.parseInt(getRequest().getParameter("userId")));
		//计算app用户15天内从间接合伙人获得的收益
		//计算app用户15天内从直接合伙人获得的收益
		double allSy=salesManager.getIncomeInFifteenDays(user.getId());
		if(null!=user&&null!=user.getAllMoney()){
			double ktxJe=user.getAllMoney()-allSy;
			 if(txJe>user.getAllMoney()){
				result.put("msg","您的余额不足");
			}
			 else if(txJe>ktxJe){
				result.put("msg","15内获得的收益不能提现");
			}
			 else if(0 >= txJe){
				result.put("msg","请输入正确提现金额");
			}else{
				getModel().setMerchant(user);
				getModel().setCreateTime(new Date());
				getModel().setTiXianMoney(txJe);
				getModel().setBalance(user.getAllMoney());
				getModel().setIncomeAll(user.getIncomeAll());
				getManager().save(getModel());
				result.put("msg","提现申请成功");
			}
			
		}else{
			result.put("msg", "你目前收入不存在");
		}
		return SUCCESS;
	}
	public String getTxRecords(){
		int userId=Integer.parseInt(getRequest().getParameter("userId"));
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from TiXianRecord rs where  rs.merchant.id=?");
		args.add(userId);
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		list=page.getData();
		return INDEX;
	}
	public Map<String, Object> getResult() {
		return result;
	}
	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public List<TiXianRecord> getList() {
		return list;
	}

	public void setList(List<TiXianRecord> list) {
		this.list = list;
	}

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	

}
