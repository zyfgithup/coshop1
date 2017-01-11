package com.systop.amol.finance.webapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.finance.model.TiXianRecord;
import com.systop.amol.finance.service.TiXianRecordManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 提现记录 平台管理者 Action
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TiXianRecordManagerAction extends DefaultCrudAction<TiXianRecord, TiXianRecordManager> {

	private static final long serialVersionUID = 4920986448165026432L;
	
	private String mobile;
	private String name;
	
	@Resource
	private UserManager userManager;
	
	@Override
	public String index() {

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		// 存放查询参数
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from TiXianRecord o where 1=1 ");
		if (StringUtils.isNotBlank(mobile)) {// 根据手机号查询
			hql.append(" and o.merchant.mobile like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(mobile));
		}
		if (StringUtils.isNotBlank(name)) {
			hql.append(" and o.merchant.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(name));
		}
		// try {
		// if (StringUtils.isNotBlank(startTime)) {// 查询该时间之后添加的数据
		// hql.append(" and c.createDate >= ?");
		// args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
		// "yyyy-MM-dd", startTime)));
		// }
		// if (StringUtils.isNotBlank(endTime)) {// 查询该时间之前添加的数据
		// hql.append(" and c.createDate <= ?");
		// args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
		// "yyyy-MM-dd", endTime)));
		// }
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		hql.append(" order by o.createTime desc");
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);

		return INDEX;
	}
	/**
	 * 汇款
	 * @return
	 */
	public String huiKuan(){
		String tiXianRecordIdStr = getRequest().getParameter("tiXianRecordId");
		Integer tiXianRecordId = Integer.valueOf(tiXianRecordIdStr);
		TiXianRecord tiXianRecord = getManager().get(tiXianRecordId);
		tiXianRecord.setBalance(tiXianRecord.getBalance()-tiXianRecord.getTiXianMoney());
		User user=userManager.get(tiXianRecord.getMerchant().getId());
		user.setAllMoney(user.getAllMoney()-tiXianRecord.getTiXianMoney());
		userManager.update(user);
		tiXianRecord.setIsSuccess(true);
		if(null != UserUtil.getPrincipal(getRequest())){
			tiXianRecord.setEmployee(userManager.get(UserUtil.getPrincipal(getRequest()).getId()));
			tiXianRecord.setHkTime(new Date());
		}
		getManager().update(tiXianRecord);
		return "huiKuan";
	}
	
	/**
	 * 汇款UI
	 * @return
	 */
	public String huiKuanUI(){
		return "huiKuanUI";
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
