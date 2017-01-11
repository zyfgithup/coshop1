package com.systop.amol.base.yyy.webapp;

import com.systop.amol.base.yyy.model.YyyAward;
import com.systop.amol.base.yyy.model.YyyEvent;
import com.systop.amol.base.yyy.service.YyyAwardManager;
import com.systop.amol.base.yyy.service.YyyManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.JsonCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class YyyAwardAction extends
JsonCrudAction<YyyAward, YyyAwardManager>{
	private File attch;
	private String attchFolder = "/uploadFiles/fileAttch/";
	private String attchFileName;
	@Autowired
	UserManager userManager;
	@Autowired
	YyyManager yyyManager;
	public String edit(){
		return super.edit();
	}
	public String awardIndex() {
		Integer eventId=Integer.parseInt(getRequest().getParameter("id"));
		Page page = PageUtil.getPage(getPageNo(),getPageSize());
		StringBuffer sql = new StringBuffer("from YyyAward c where c.yyyEvent.id=? ");
		List args = new ArrayList();
		args.add(eventId);
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		sql.append(" order by c.createTime");
		getRequest().setAttribute("logonUser", user);
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		List<YyyAward> list = page.getData();
		for (YyyAward ya : list){
			ya.setShowTime(ya.getCreateTime().toString().split(" ")[0]);
		}
		restorePageData(page);
		return "awardIndex";
	}
	public String addAward(){
		String eventId=getRequest().getParameter("eventId");
		getRequest().setAttribute("eventId",eventId);
		return "addAward";
	}
	@Override
	public String save() {
		try {
			System.out.println("------getModel().getId()-----------"+getModel().getId());
			if(null==getModel().getId()){
				Integer eventId=Integer.parseInt(getRequest().getParameter("eventId"));
				YyyEvent yyyEvent=yyyManager.get(eventId);
				User user = UserUtil.getPrincipal(getRequest());
				User uu=userManager.get(user.getId());
				getModel().setCreateUser(uu);
				getModel().setYyyEvent(yyyEvent);
				System.out.print("--------------------跟踪时间为="+getModel().getCreateTime());
				getManager().save(getModel());
			}
			else{
				YyyAward award=getManager().get(getModel().getId());
				if (attch != null) {
					String filePath = UpLoadUtil.doUpload(attch,
							attchFileName, attchFolder, getServletContext());
				}
				attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
				getManager().update(award);
			}
			return SUCCESS;
		} catch (Exception e) {
			addActionError(e.getMessage());
			return INPUT;
		}
	}
	public File getAttch() {
		return attch;
	}
	public void setAttch(File attch) {
		this.attch = attch;
	}
	public String getAttchFolder() {
		return attchFolder;
	}
	public void setAttchFolder(String attchFolder) {
		this.attchFolder = attchFolder;
	}
	public String getAttchFileName() {
		return attchFileName;
	}
	public void setAttchFileName(String attchFileName) {
		this.attchFileName = attchFileName;
	}
	
}
