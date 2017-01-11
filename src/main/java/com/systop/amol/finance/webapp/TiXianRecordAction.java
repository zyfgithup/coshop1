package com.systop.amol.finance.webapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
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
 * 提现记录 商户 Action
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TiXianRecordAction extends DefaultCrudAction<TiXianRecord, TiXianRecordManager> {

	private static final long serialVersionUID = 4920986448165026432L;
	
	private String mobile;
	private String name;
	
	@Resource
	private UserManager userManager;
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public String edit() {
		
		User merchant = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		
		String sql = "SELECT sum(samount) as dongJieTiXianMoney FROM sales WHERE STATUS <> 2 and  is_return <>2 AND "
				+ "pay_state = 1 and alipay_gmt_payment between date_sub(now(),interval 15 day) and now() "
				+ "AND (payment ='WXPAY' or payment='ALIPAY') and mer_id="+merchant.getId();
		Map<String,Double> mapDB = jdbcTemplate.queryForMap(sql);
		//36569149
		
		//冻结提现金额
		merchant.setDongJieTiXianMoney(mapDB.get("dongJieTiXianMoney"));
		//最大提现金额
		if(null != merchant.getDongJieTiXianMoney()){
			merchant.setMaxTiXianMoney(merchant.getAllMoney().doubleValue() - merchant.getDongJieTiXianMoney().doubleValue());
		}else{
			merchant.setMaxTiXianMoney(merchant.getAllMoney());
		}
		
		getRequest().setAttribute("merchant", merchant);
		
		
	return super.edit();
	}
	

	/**
	 * 提现
	 * 
	 * @return
	 */
	public String tiXian() {

		String result = INPUT;
		
		String tiXianMoneyStr = getRequest().getParameter("tiXianMoney");
		int tiXianMoney = Integer.valueOf(tiXianMoneyStr);

		User merchant = UserUtil.getPrincipal(getRequest());

		merchant = userManager.get(merchant.getId());
		
		String sql = "SELECT sum(samount) as dongJieTiXianMoney FROM sales WHERE STATUS <> 2 and  is_return <>2 AND "
				+ "pay_state = 1 and alipay_gmt_payment between date_sub(now(),interval 15 day) and now() "
				+ "AND (payment ='WXPAY' or payment='ALIPAY') and mer_id="+merchant.getId();
		
		//36569149
		
		Map<String,Double> mapDB = jdbcTemplate.queryForMap(sql);
		
		//冻结提现金额
		merchant.setDongJieTiXianMoney(mapDB.get("dongJieTiXianMoney"));
		//最大提现金额
		if(null != merchant.getDongJieTiXianMoney()){
			merchant.setMaxTiXianMoney(merchant.getAllMoney().doubleValue() - merchant.getDongJieTiXianMoney().doubleValue());
		}else{
			merchant.setMaxTiXianMoney(merchant.getAllMoney());
			
		}
		
		TiXianRecord tiXianRecord = userManager.tiXian(tiXianMoney, merchant);
		if(tiXianRecord.isSqtx()){
			result = SUCCESS;
		}else{
			this.addActionMessage(tiXianRecord.getMessage());
			getRequest().setAttribute("merchant", merchant);
		} 
		
		return result;
	}
	
	@Override
	public String index() {

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		// 存放查询参数
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from TiXianRecord o where o.merchant.id = ? ");
		args.add(UserUtil.getPrincipal(getRequest()).getId());
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
