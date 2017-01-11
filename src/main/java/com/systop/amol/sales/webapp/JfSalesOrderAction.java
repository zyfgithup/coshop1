package com.systop.amol.sales.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.product.ProductConstants;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.JfSummary;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.service.SalesManager;
import com.systop.amol.sales.utils.Payment;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JfSalesOrderAction  extends DefaultCrudAction<Sales, SalesManager>{
	/** 开始时间 */
	private String name;

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public String index() {
		String result = INDEX;
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		Map<String, Object> param = this.getQuery();
		page = getManager().pageQuery(page, param.get("sql").toString(),
				((List) param.get("param")).toArray());
		List<JfSummary> myList=new ArrayList<JfSummary>();
		List list=page.getData();
		for (Object o : list) {
			Object[] objs = (Object[]) o;
			JfSummary js=new JfSummary();
			if(null != objs[0]){js.setCount(Integer.parseInt(String.valueOf(objs[0])));}
			if(null!=objs[1]){js.setNum(Integer.parseInt(String.valueOf(objs[1])));}
			if(null!=objs[2]) {js.setName(String.valueOf(objs[2]));}
			myList.add(js);
		}
		List args = new ArrayList();
		/*
		 * if(user.getBeginningInit() == null){ System.out.println("user："+user);
		 * System.out.println("user.getSuperior()："+user.getSuperior());
		 * System.out.println
		 * ("user.getSuperior().getBeginningInit()："+user.getSuperior
		 * ().getBeginningInit());
		 * if(user.getSuperior().getBeginningInit().equals("0")){
		 * this.addActionError("您的上级经销商还没有完成初始化，所以您现在不能正式使用该平台。"); } }
		 */
	//	page = getManager().pageQuery(page, sql.toString(), args.toArray());
	///	restorePageData(page);
		page.setData(myList);
		restorePageData(page);
		return result;
	}
	private Map<String,Object> getQuery() {
		
		Map<String, Object> result = new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer(
				"select sum(s.products.integral),sum(s.count),s.sales.user.region.name as name from SalesDetail s ");
		sql.append(" where s.products.integral is not null ");
		List args = new ArrayList();
//		// 如果是顶级经销商，他可以查看自己以及自己下属的分销商的所有销售信息情况
//		User user = UserUtil.getPrincipal(getRequest());
//		if (user.getSuperior() == null) {
//			Integer superid = user.getId();
//			if (superid != null && superid.intValue() > 0) {
//				sql.append(" and (sd.sales.user.superior.id = ? or sd.sales.user.id = ?) ");
//				args.add(superid);
//				args.add(superid);
//			}
//		} /*else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
//			Integer superid = user.getSuperior().getId();
//			if (superid != null && superid.intValue() > 0) {
//				sql.append(" and (sd.sales.user.superior.id = ? or sd.sales.user.id = ?) ");
//				args.add(superid);
//				args.add(superid);
//			}
//		} */else/* if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT))*/{// 如何是分销商，只能查看自己的销售信息情况
//			sql.append(" and sd.sales.user.id = ? ");
//			args.add(user.getId());
//		}
/*		if (StringUtils.isNotBlank(productName)) {
			sql.append(" and sd.products.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(productName));
		}

		if (StringUtils.isNotBlank(productCode)) {
			sql.append(" and sd.products.code like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(productCode));
		}
*/
		String name = getRequest().getParameter("name");
		if (StringUtils.isNotBlank(name) ) {
			sql.append(" and s.sales.user.region.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(name));
		}
		sql.append(" group by s.sales.user.region.code order by sum(s.products.integral) desc ");
		result.put("sql", sql);
		result.put("param", args);

		return result;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	


}
