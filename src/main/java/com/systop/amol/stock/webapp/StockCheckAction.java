package com.systop.amol.stock.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.systop.amol.stock.model.StockCheck;
import com.systop.amol.stock.service.StockCheckManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 库存盘点action
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StockCheckAction extends
		DefaultCrudAction<StockCheck, StockCheckManager> {

	// 开始时间
	private String startDate;

	// 结束时间
	private String endDate;

	// 仓库管理Manager
	@Autowired
	private StorageManager storageManager;

	// 异步请求，返回值
	private Map<String, Object> result;
	
	/**
	 * 查询订单列表
	 */
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}

		if(StringUtils.isNotBlank(user.getBeginningInit()) && user.getBeginningInit().equals("0")){
			this.addActionError("请先完成初始化,再作单据！ ");
		}
		
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from StockCheck sc where sc.user.id = ? ");
		List args = new ArrayList();
		args.add(user.getId());

		if (StringUtils.isNotBlank(getModel().getCheckNo())) {
			sql.append(" and sc.checkNo like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCheckNo()));
		}

		if (getModel() != null && getModel().getStorage() != null
				&& getModel().getStorage().getId() != null) {
			sql.append(" and sc.storage.id = ?");
			args.add(getModel().getStorage().getId());
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and sc.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and sc.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sql.append(" order by sc.createTime desc, sc.checkNo desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}

	/**
	 * 只作查询订单列表，不做业务
	 * 只有经销商管理员可以查询
	 */
	public String checkIndex() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}

		if(StringUtils.isNotBlank(user.getBeginningInit()) && user.getBeginningInit().equals("0")){
			this.addActionError("请先完成初始化,再作单据！ ");
		}
		
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from StockCheck sc where sc.user in (from User u where u.id = ? or u.superior.id = ?) ");
		List args = new ArrayList();
		args.add(user.getId());
		args.add(user.getId());

		if (StringUtils.isNotBlank(getModel().getCheckNo())) {
			sql.append(" and sc.checkNo like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCheckNo()));
		}

		if (getModel() != null && getModel().getStorage() != null
				&& getModel().getStorage().getId() != null) {
			sql.append(" and sc.storage.id = ?");
			args.add(getModel().getStorage().getId());
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and sc.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and sc.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sql.append(" order by sc.createTime desc, sc.checkNo desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return "checkIndex";
	}

	
	@Override
	public String view() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前登录用户为空");
		Assert.notNull(getModel());
		// 得到当前盘点单
		if (getModel() != null && getModel().getId() != null) {
			StockCheck stockCheck = getManager().get(getModel().getId());
			setModel(stockCheck);
		}
		return super.view();
	}
	
	@Override
	public String remove() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前登录用户为空");
		Assert.notNull(getModel());
		if(getModel().getId() != null ){
			getManager().removeStockCheck(getModel().getId());
			addActionMessage("删除成功");
		}else{
			addActionError("删除失败");
		}
		return SUCCESS;
	}
	
	/**
	 * 修改库存盘点单中的状态为2(冻结【录入实际库存】按钮)
	 * @return
	 */
	public String updateStockCheckStatus(){
		result = new HashMap<String, Object>();
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前登录用户为空");
		Assert.notNull(getModel());
		result.put("success", false);

		if(getModel().getId() != null && getModel().getStatus() != null){
			//
			getManager().updateStatus(getModel().getId(), getModel().getStatus());
			result.put("success", true);
		}		
		return "jsonSuccess";	
	}
	
	
	/**
	 * 导出到excel；
	 * 
	 * @return
	 */
	public String exportExcel() {
		this.view();
		return "exportExcel";
	}

	/**
	 * 仓库的下拉列表
	 */
	public Map getStorageMap() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			return storageManager.getStorageMap(user);
		}
		return null;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
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
