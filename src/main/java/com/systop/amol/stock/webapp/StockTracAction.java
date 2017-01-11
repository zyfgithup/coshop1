package com.systop.amol.stock.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.systop.amol.base.storage.service.StorageManager;
import com.systop.amol.stock.StockConstants;
import com.systop.amol.stock.model.StockTrac;
import com.systop.amol.stock.service.StockTracManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 库存调拨action
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StockTracAction extends
		DefaultCrudAction<StockTrac, StockTracManager> {

	@Autowired
	private StorageManager storageManager;

	// 开始时间
	private String startDate;

	// 结束时间
	private String endDate;

	// 出货库
	// private Map outStorageMap;

	// 入货库
	// private Map inStorageMap;

	/**
	 * 查询库存【调拨】列表
	 */
	@Override
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前用户不存在");
		Assert.notNull(getModel());
		if(StringUtils.isNotBlank(user.getBeginningInit()) && user.getBeginningInit().equals("0")){
			this.addActionError("请先完成初始化,再作单据！ ");
		}
		getModel().setUser(user);
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from StockTrac s where s.user.id = ? ");
		List args = new ArrayList();
		args.add(user.getId());

		if(StringUtils.isNotBlank(getModel().getCheckNo())){
			sql.append(" and s.checkNo like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCheckNo()));
		}
		
		// 条件:出货库
		if (getModel().getOutStorage() != null && getModel().getOutStorage().getId() != null) {
			sql.append(" and s.outStorage.id = ?");
			args.add(getModel().getOutStorage().getId());
		}
		// 条件:入货库
		if (getModel().getInStorage() != null && getModel().getInStorage().getId() != null) {
			sql.append(" and s.inStorage.id = ?");
			args.add(getModel().getInStorage().getId());
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and s.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and s.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sql.append(" order by s.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}

	/**
	 * 查询库存【调拨】列表,只作查询不做业务
	 * 为经销商查询所有信息
	 */
	public String tracIndex() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前用户不存在");
		Assert.notNull(getModel());
		if(StringUtils.isNotBlank(user.getBeginningInit()) && user.getBeginningInit().equals("0")){
			this.addActionError("请先完成初始化,再作单据！ ");
		}
		getModel().setUser(user);
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from StockTrac s where s.user in (from User u where u.id = ? or u.superior.id = ?) ");
		List args = new ArrayList();
		args.add(user.getId());
		args.add(user.getId());
		
		if(StringUtils.isNotBlank(getModel().getCheckNo())){
			sql.append(" and s.checkNo like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCheckNo()));
		}
		
		// 条件:出货库
		if (getModel().getOutStorage() != null && getModel().getOutStorage().getId() != null) {
			sql.append(" and s.outStorage.id = ?");
			args.add(getModel().getOutStorage().getId());
		}
		// 条件:入货库
		if (getModel().getInStorage() != null && getModel().getInStorage().getId() != null) {
			sql.append(" and s.inStorage.id = ?");
			args.add(getModel().getInStorage().getId());
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and s.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and s.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sql.append(" order by s.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return "tracIndex";
	}
	
	@Override
	public String edit() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前用户不存在");
		Assert.notNull(getModel());
		
		//生成库存调拨单编号
		getModel().setCheckNo(StockConstants.TRAC_DB + "-" + getManager().getOrder(new Date(), user));
		
		return super.edit();
	}

	@Override
	public String view() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前登录用户为空");
		Assert.notNull(getModel());
		// 得到当前盘点单
		if (getModel() != null && getModel().getId() != null) {
			StockTrac stockTrac = getManager().get(getModel().getId());
			setModel(stockTrac);
		}
		return super.view();
	}
	
	/**
	 * 导出到excel
	 * 
	 * @return
	 */
	public String exportExcel() {
		this.view();
		return "exportExcel";
	}
	
	/**
	 * 出货仓库
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
