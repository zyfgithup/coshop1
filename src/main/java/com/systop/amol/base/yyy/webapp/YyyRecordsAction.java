package com.systop.amol.base.yyy.webapp;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.yyy.model.YyyRecords;
import com.systop.amol.base.yyy.service.YyyRecordsManager;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.JsonCrudAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class YyyRecordsAction extends
JsonCrudAction<YyyRecords, YyyRecordsManager>{
	@Autowired
	private UserManager userManager;
	@Autowired
	private RegionManager regionManager;
	private String regionName;
	private Integer regionId;
	private String eventName;
	private String loginId;
	private String isZj;
	public String index() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from YyyRecords c where 1=1 ");
		List args = new ArrayList();
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		if(null!=user.getRegion()){
			Region region = regionManager.get(Integer.valueOf(user.getRegion().getId()));
	    	sql.append(" and c.region.code like ?");
	    	args.add(MatchMode.START.toMatchString(region.getCode()));
	    	regionName = region.getName();
		}
		 if(null == regionId||regionId==0){
	    	if(null != user.getRegion()){
	    	  sql.append(" and c.region.code like ?");
	    	  args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
	    	  regionName = user.getRegion().getName();
	    	}
		 }else{
	    	Region region = regionManager.get(Integer.valueOf(regionId));
	    	sql.append(" and c.user.region.code like ?");
	    	args.add(MatchMode.START.toMatchString(region.getCode()));
	    	regionName = region.getName();
	    	
	    }
		if (null!=eventName&&!"".equals(eventName)) {
			sql.append(" and c.yyyEvent.eventName like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(eventName));
		}
		if (loginId != null
				&& !loginId.trim().equals("")) {
			sql.append(" and c.user.loginId like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(loginId));
		}
		if (isZj != null
				&& !isZj.trim().equals("")) {
			sql.append(" and c.isZj = ?");
			args.add(isZj);
		}
		sql.append(" order by c.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}
	public String lingQu(){
		YyyRecords yyyRecords=getManager().get(getModel().getId());
		yyyRecords.setFlag("1");
		getManager().update(yyyRecords);
		return SUCCESS;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getIsZj() {
		return isZj;
	}
	public void setIsZj(String isZj) {
		this.isZj = isZj;
	}
	
	

}
