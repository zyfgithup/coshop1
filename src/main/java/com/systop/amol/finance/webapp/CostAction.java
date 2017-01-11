package com.systop.amol.finance.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ibm.icu.text.SimpleDateFormat;
import com.systop.amol.finance.CostSortConstants;
import com.systop.amol.finance.model.Cost;
import com.systop.amol.finance.model.CostDetail;
import com.systop.amol.finance.model.CostSort;
import com.systop.amol.finance.service.CostManager;
import com.systop.amol.finance.service.FundsSortManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.JsonCrudAction;

/**
 * 费用主表管理Action
 * @author Administrator
 *
 */
@SuppressWarnings({"serial","rawtypes","unchecked"})
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CostAction extends JsonCrudAction <Cost, CostManager> {
	
	@Autowired
	private FundsSortManager fundsSortManager;
	
	private String employeeName;
	
	private String code;
	/**
	 * Json数据
	 */
	private Map<String, String> jsonResult;
	
	//开始时间
	private String startDate;
	
	//截至时间
	private String endDate;
	
	//判断是收入还是支出
	private String status;
	
	/**
	 * 显示资金类型
	 */
	public Map getFundsSortMap() {
		User user = UserUtil.getPrincipal(getRequest());
		return fundsSortManager.getFundsSortMap(user);
	}
	
	/**
	 * 状态
	 * @return
	 */
	public Map<String, String> getStateMap(){
		Map statesMap = new LinkedHashMap();
		statesMap.put(CostSortConstants.RED, "全部");
		statesMap.put(CostSortConstants.RED_NO, "正常");
		statesMap.put(CostSortConstants.RED_RED, "冲红单");
		statesMap.put(CostSortConstants.RED_YES, "已冲红");
		return statesMap;
	}
	
	/**
	 * 查询资金收入信息列表
	 */
	public String index() {
				
		User user = UserUtil.getPrincipal(getRequest());
		
		if (user != null) {
			getModel().setUser(user);
			if (StringUtils.isNotBlank(user.getBeginningInit())) {
				if (user.getBeginningInit().equals("0")) {
					this.addActionError("请先完成初始化,再作单据！ ");
					return INDEX;
				}
			}			
		}
		StringBuffer sql = new StringBuffer("from Cost c where 1=1 ");
		List args = new ArrayList();
		if (StringUtils.isNotBlank(user.getBeginningInit())) {
			if (user.getBeginningInit().equals("0")) {
				this.addActionError("请先完成初始化,再作单据！ ");
				return INDEX;
			}else {
				if(user.getSuperior() != null){
					sql.append(" and c.user.id = ? ");
					args.add(user.getId());
				}else{
					sql.append(" and (c.user.id = ? or c.user.superior.id = ?)");
					args.add(user.getId());
					args.add(user.getId());
					if(StringUtils.isNotBlank(employeeName)){
						sql.append(" and c.user.name like ? ");
						args.add(MatchMode.ANYWHERE.toMatchString(employeeName));
					}
					if(StringUtils.isNotBlank(code)){
						sql.append(" and c.user.code like ? ");
						args.add(MatchMode.ANYWHERE.toMatchString(code));
					}
				}
			}
		}		
		if (getModel() != null && getModel().getReceipts() != null
				&& !getModel().getReceipts().equals("")) {
			sql.append(" and c.receipts like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getReceipts()));
		}
		
		if (getModel().getRed() != null && !getModel().getRed().equals(CostSortConstants.RED)) {
			sql.append(" and c.red = ?");
			args.add(getModel().getRed());
		}
		
		if (getModel() == null || getModel().getRed() == null){
			getModel().setRed(CostSortConstants.RED_NO);
			sql.append(" and c.red = ?");
			args.add(CostSortConstants.RED_NO);
		}
		
		if (StringUtils.isNotBlank(status)) {
			sql.append(" and c.status = ?");
			args.add(status);
		}
			
		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and c.createDate >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and c.createDate <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
			
		sql.append(" order by c.serialNumber desc");
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, sql.toString(), args.toArray());

		String hql = "select sum(totalMoney) "+sql.toString();
		List list = getManager().query(hql, args.toArray());
		String totalCost = "";
		if(list.size()>0){
			for (Object o : list) {
				if(o != null){
					totalCost = o.toString();
				}
			}
		}
//		List<Cost> list = new ArrayList();
//		list = page.getData();
//		float totalCost = 0;
//		for (Cost cost : list) {
//			 //获取费用
//			float costMoney = cost.getTotalMoney();
//			totalCost += costMoney;
//
//		}
		getRequest().setAttribute("costTotal", String.valueOf(totalCost));
		restorePageData(page);

		return INDEX;

	}

	/***
	 * 冲红
	 * @return
	 */
	public String red(){

		User user = UserUtil.getPrincipal(getRequest());		
		Cost cost=new Cost();
		if (user != null) {
			cost.setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		cost.setReceipts(generateNo());
		cost.setSerialNumber(serialNumber());
		cost.setCreateDate(new Date());
		List<CostDetail> cd = this.getManager().getCostDetails(getModel());
		List<CostDetail> cdList=new ArrayList<CostDetail>();
		for (int i=0;i<cd.size();i++) {
			CostDetail costDetail = new CostDetail();
			costDetail.setCostSort(cd.get(i).getCostSort());
			costDetail.setMoney(-cd.get(i).getMoney());
			costDetail.setNote(cd.get(i).getNote());
			costDetail.setCost(cost);
			cdList.add(costDetail);
		}
		cost.setTotalMoney(-getModel().getTotalMoney());
		cost.setFundsSort(getModel().getFundsSort());
		//cost.setEmployee(getModel().getEmployee());
		cost.setStatus(status);
		cost.setRed(CostSortConstants.RED_RED);
		cost.setRemark("冲红单号："+getModel().getReceipts());
		getModel().setRed(CostSortConstants.RED_YES);
		getModel().setRemark("已冲红");
		this.getManager().save(cost, cdList);
		return SUCCESS;
	}
	
	/**
	 * 自动生成单号
	 */
	public String generateNo(){
		User user = UserUtil.getPrincipal(getRequest());
		// 获得单号
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String jno="";
		if(user.getType().equals(AmolUserConstants.EMPLOYEE)){
			String hql = "select max(c.receipts) from Cost c where c.receipts like ? and (c.user.id = ? or c.user.superior.id = ?)";
			jno = (String) this.getManager().getDao().findObject(hql, new Object[] { date + "%" ,user.getSuperior().getId(),user.getSuperior().getId()});
		}else{
			String hql = "select max(c.receipts) from Cost c where c.receipts like ? and (c.user.id = ? or c.user.superior.id = ?)";
			jno = (String) this.getManager().getDao().findObject(hql, new Object[] { date + "%" ,user.getId(),user.getId()});
		}

		if (jno == null) {
			jno = date + "0001";
		} else {
			Integer no = Integer.parseInt(jno.substring(8)) + 1;
			String no1 = "000" + no;
			jno = date + no1.substring(no1.length() - 4);
		}
		return jno;
	}
	
	/**
	 * 自动生成单据顺序号
	 */
	public String serialNumber(){
		User user = UserUtil.getPrincipal(getRequest());
		String sql = "select max(c.serialNumber) from Cost c where c.serialNumber like ? and c.user.id = ?";
		String dno = (String) this.getManager().getDao().findObject(sql, new Object[] { "%" ,user.getId()});

		if (dno == null) {
			dno = "000001";
		} else {
			Integer no = Integer.parseInt(dno.substring(0)) + 1;
			String no1 = "000000" + no;
			dno = no1.substring(no1.length() - 6);
		}
		return dno;
	}
	
	/**
	 * 编辑资金收入
	 */
	public String edit() {
		User user = UserUtil.getPrincipal(getRequest());
		if (getModel().getId() == null) {
			getModel().setReceipts(generateNo());
			getModel().setSerialNumber(serialNumber());
			getModel().setUser(user);
			getModel().setCreateDate(DateUtil.getCurrentDate());
		}else{
			List<CostDetail> cd = new ArrayList<CostDetail>();
			cd = this.getManager().getCostDetails(getModel());
			this.getRequest().setAttribute("costDetail", cd);
		}
		return INPUT;
	}

	/**
	 * 保存资金收入信息
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
			
			//保存资金收入明细
			String[] costSortIds = this.getRequest().getParameterValues("costSortId");
			String[] notes = this.getRequest().getParameterValues("note");
			String[] moneys = this.getRequest().getParameterValues("money");
			if(costSortIds != null){
				List<CostDetail> cdlist = new ArrayList<CostDetail>();
				for (int i = 1; i < costSortIds.length; i++) {
					if(costSortIds[i] == null || costSortIds[i].equals("")){
						addActionError("请选择费用类别！");
						return INPUT;
					}if(moneys[i] == null || moneys[i].equals("")){
						addActionError("金额不能为空！");
						return INPUT;
					}else{
						CostDetail cd = new CostDetail();
						cd.setMoney(Double.parseDouble(moneys[i]));
						cd.setNote(notes[i]);
						cd.setCostSort(this.getManager().getDao()
								.get(CostSort.class, Integer.parseInt(costSortIds[i])));
						cd.setCost(getModel());
						cdlist.add(cd);
					}
				}
				getModel().setRed(CostSortConstants.RED_NO);//默认未冲红
				getModel().setStatus(status);
				this.getManager().save(getModel(), cdlist);
			}else{
				addActionError("请填写详单");
				return INPUT;
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			addActionError(e.getMessage());
			return INPUT;
		}
	}
	
	/**
	 * 查看信息
	 */
	public String view() {
		this.edit();
		return "view";
	}
	
	/**
	 * 导出
	 * @return
	 */
	public String exportExcel() {
		this.edit();
		return "exportExcel";
	}
	
	/***
	 * 查找生产厂家
	 * @return
	 */
	/*
	public String select(){
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		User user = UserUtil.getPrincipal(getRequest());
		
		if (user == null) {//当前登录用户为空
			jsonResult.put("result", "error");
			return "jsonResult";
		}else{
			try {
				if(user.getCompany() == null){
					jsonResult.put("result", "noCompany");
					return "jsonResult";
				}else{
					User company = this.getManager().getDao().get(User.class,user.getCompany().getId());
					jsonResult.put("company", company.getName());
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return "jsonResult";
	}
	*/
	
	/**
	 * @return
	 */
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
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Map<String, String> getJsonResult() {
		return jsonResult;
	}
	
	public void setJsonResult(Map<String, String> jsonResult) {
		this.jsonResult = jsonResult;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}

