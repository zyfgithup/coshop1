package com.systop.amol.finance.webapp;

import com.systop.amol.finance.CostSortConstants;
import com.systop.amol.finance.model.CostDetail;
import com.systop.amol.finance.model.CostSort;
import com.systop.amol.finance.service.CostDetailManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 财务费用明细管理Action
 * @author Administrator
 *
 */
@SuppressWarnings({"serial","rawtypes","unchecked"})
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CostDetailAction extends DefaultCrudAction <CostDetail, CostDetailManager> {

	private String startDate;
	
	private String endDate;
	
	/**
	 * 查询财务费用明细信息列表
	 */
	public String index() {

		User user = UserUtil.getPrincipal(getRequest());
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from CostDetail cd where cd.cost.red = " + CostSortConstants.RED_NO);
		List args = new ArrayList();
		if (user != null) {
			if(user.getSuperior() != null){
				sql.append(" and cd.cost.user.id = ?  ");
				args.add(user.getId());
			}else{
				sql.append(" and (cd.cost.user.id = ?  or cd.cost.user.superior.id = ?)");
				args.add(user.getId());
				args.add(user.getId());
			}
		}
		if (getModel() != null && getModel().getCost() != null && getModel().getCost().getReceipts()!=null) {
			sql.append(" and cd.cost.receipts like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCost().getReceipts()));
		}
		if (getModel() != null && getModel().getCostSort() != null
				&& getModel().getCostSort().getId() != null) {
			CostSort costSort = getManager().getDao().get(
					CostSort.class, getModel().getCostSort().getId());
			if (costSort != null) {
				sql.append("and cd.costSort.serialNo like ? ");
				args.add(MatchMode.START.toMatchString(costSort.getSerialNo()));
			}
			// 为了返回查询中的条件数据需要将SpendSort存入到MODEL中
			getModel().setCostSort(costSort);

		}
		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and cd.cost.createDate >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and cd.cost.createDate <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sql.append(" order by cd.cost.createDate desc,cd.cost.status asc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());

		restorePageData(page);
		
		List<CostDetail> list = new ArrayList();
		//list = page.getData();
		list = getManager().query(sql.toString(), args.toArray());
		double inCost = 0;
		double outCost = 0;
		for (CostDetail costDetail : list) {
			 //获取费用
			//float costMoney = Math.abs(costDetail.getMoney());
			Double costMoney = costDetail.getMoney();
			if (costDetail.getCost().getStatus().equals("1")) {
				inCost = inCost + costMoney;
			}else {
				outCost = outCost + costMoney;
			}		

		}
		double costTotal = inCost - outCost;
		getRequest().setAttribute("costTotal", String.valueOf(costTotal));
		//System.out.println("@@@@@@@@:" + inCost + "/" + outCost);

		return INDEX;
	}
	
	/**
	 * 收支类别MAP
	 * @return
	 */
	public Map<String, String> getCostSortMap(){
		return CostSortConstants.COST_SORT_MAP;
	}
	
	/**
	 * 
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

}
