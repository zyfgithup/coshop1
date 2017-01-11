package com.systop.amol.database.webapp;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.systop.amol.database.DatabaseConstants;
import com.systop.amol.database.model.DataBackup;
import com.systop.amol.database.service.DataBackupManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.Constants;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 经销商数据备份操作action
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DataBackupAction extends
		DefaultCrudAction<DataBackup, DataBackupManager> {
	// 开始时间
	private String startDate;

	// 结束时间
	private String endDate;

	/** 数据备份存放路径 */
	private static final String DATA_BACKUP_FOLDER = Constants.DATA_BACKUP_FOLDER;

	/** 数据备份文件后缀 */
	private static final String DATA_BACKUP_SUFFIX = Constants.DATA_BACKUP_SUFFIX;
	
	// 异步请求，返回值
	private Map<String, Object> result;
	
	/**
	 * 查询经销商数据备份列表
	 */
	@Override
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "用户不允许为空");
		Assert.notNull(getModel());
		
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from DataBackup db where db.user.id = ? and db.sign = ?");
		List args = new ArrayList();
		args.add(user.getId());
		//备份经销商数据标示
		args.add(DatabaseConstants.DATA_BACKUP_PART);
		
		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and db.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and db.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil
						.convertStringToDate("yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sql.append(" order by db.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		if (StringUtils.isNotBlank(getModel().getSign()) && getModel().getSign().equals(DatabaseConstants.DATA_BACKUP_ALL)) {
			return "backupAllIndex";
		}else{
			return INDEX;			
		}
	}

	/**
	 * 异步：保存数据备份(经销商备份)
	 * 
	 * @return String
	 */
	public String saveDataBackup() {
		result = new HashMap<String, Object>();
		try{
			User user = UserUtil.getPrincipal(getRequest());
			Assert.notNull(user, "用户不允许为空");
			Assert.notNull(getModel());
			if (getModel().getId() == null) {
				// 备份操作人
				getModel().setUser(user);
			}
			//保存路径
			getModel().setDataUrl(doDataUrl(user));
			//保存备份名称
			getModel().setDataFileName(doDataFileName(user));
			getModel().setCreateTime(new Date());
			//经销商备份
			getModel().setSign(DatabaseConstants.DATA_BACKUP_PART);
			getManager().saveDataBackup(getServletContext(),getModel());
			result.put("success", true);
			result.put("msg", "数据备份成功！");
		}catch(Exception e){
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "备份失败,请与管理员联系！");		
		}
		return "jsonSuccess";
	}

	/**
	 * 异步：数据恢复(只恢复某经销商备份)
	 * @return String
	 */
	public String restoreData(){
		result = new HashMap<String, Object>();
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "用户不允许为空");
		Assert.notNull(getModel());
		if (getModel().getId() == null) {
			result.put("success", false);
			result.put("msg", "还原失败,请与管理员联系！");		
		}
		try {
			getManager().restoreData(getServletContext(),getModel().getId());
			result.put("success", true);
			result.put("msg", "数据还原成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "还原失败,请与管理员联系！");		
		}
		return "jsonSuccess";
	}
	
	@Override
	public String remove() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "用户不允许为空");
		Assert.notNull(getModel());
		getModel().setDataUrl(getServletContext().getRealPath(getModel().getDataUrl())+File.separatorChar);
		return super.remove();
	}

	/**
	 * 得到数据备份url
	 * 
	 * @return String
	 */
	private String doDataUrl(User user) {
		ServletContext sctx = getServletContext();
		String folderPath = "";
		String url = "";
		if(user != null){
			folderPath = sctx.getRealPath(DATA_BACKUP_FOLDER+File.separatorChar + user.getId());
			url = DATA_BACKUP_FOLDER + user.getId();
		}else{
			folderPath = sctx.getRealPath(DATA_BACKUP_FOLDER);	
			url = DATA_BACKUP_FOLDER;
		}		
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return url;
	}

	/**
	 * 得到数据库备份名称
	 * @param user
	 * @return String
	 */
	private String doDataFileName(User user){
		final int randomLength = 3;
		String fileName = DateUtil.getDateTime("yyyyMMddhhmmss", new Date())
		+ RandomStringUtils.randomNumeric(randomLength)
		+ DATA_BACKUP_SUFFIX;
		if(user != null){
			return user.getId() + "_" + fileName;
		}
		return fileName;
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
	
	public Map<String, Object> getResult() {
		return result;
	}
}
