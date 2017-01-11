package com.systop.amol.stock.webapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.systop.amol.base.storage.service.StorageManager;
import com.systop.amol.stock.StockConstants;
import com.systop.amol.stock.model.StockTrac;
import com.systop.amol.stock.model.StockTracDetail;
import com.systop.amol.stock.service.StockTracDetailManager;
import com.systop.amol.stock.service.StockTracManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 库存调拨详单action
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StockTracDetailAction extends
		DefaultCrudAction<StockTracDetail, StockTracDetailManager> {
	@Autowired
	private StorageManager storageManager;
	@Autowired
	private StockTracManager stockTracManager;

	// 开始时间
	private String startDate;

	// 结束时间
	private String endDate;

	// 出货库
	// private Map outStorageMap;

	// 入货库
	// private Map inStorageMap;

	/**
	 * 查询库存调拨列表
	 */
	@Override
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前用户不存在");
		Assert.notNull(getModel());
		if(user.getBeginningInit().equals("0")){
			this.addActionError("请先完成初始化,再作单据！ ");
		}
		getModel().setUser(user);
		String su = getRequest().getParameter("su");
		if(su.equals("true")){
			this.addActionMessage("调拨成功！");
		}
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from StockTrac s where s.user.id = ? ");
		List args = new ArrayList();
		args.add(user.getId());
		
		sql.append(" order by date(s.createTime) desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}

	@Override
	public String save() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前用户不存在");
		Assert.notNull(getModel());
		getModel().setUser(user);
		
		String[] pIds = this.getRequest().getParameterValues("pid");//商品ID
		String[] unitids = getRequest().getParameterValues("unitid");//单位ID
		String[] ncounts = this.getRequest().getParameterValues("outCount");//调拨单位数量
		String[] counts = this.getRequest().getParameterValues("counts");//调拨基础数量
		String[] remarks = this.getRequest().getParameterValues("sremark");//备注
		try {
			// 保存库存库存调拨单以及调拨详单
			getManager().saveStockTracDetail(getModel(),pIds,unitids,counts,ncounts,remarks);
		} catch (Exception e) {
			this.addActionMessage("发生错误请与管理员联系");
			return INPUT;
		}
		return SUCCESS;
	}

	@Override
	public String edit() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前用户不存在");
		Assert.notNull(getModel());
		if(user.getBeginningInit().equals("0")){
			this.addActionError("请先完成初始化,再作单据！ ");
			return index();
		}
		StockTrac stockTrac = new StockTrac();
		stockTrac.setCheckNo(StockConstants.TRAC_DB + "-"
				+ stockTracManager.getOrder(new Date(), user));
		getModel().setStockTrac(stockTrac);
		return super.edit();
	}

	/**
	 * 出货仓库
	 * 
	 * @return
	 */
	public Map getOutStorageMap() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
				user = getManager().getDao().get(User.class, user.getId());
				return storageManager.getAllStorage(user.getSuperior());
			}else{
				return storageManager.getAllStorage(user);
			}				
		}
		return null;
	}

	/**
	 * 入货仓库
	 * 
	 * @return
	 */
	public Map getInStorageMap() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
				user = getManager().getDao().get(User.class, user.getId());
				return storageManager.getAllStorage(user.getSuperior());
			}else{
				return storageManager.getAllStorage(user);
			}				
		}
		return null;
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
