package com.systop.amol.app.push.webapp;

import com.systop.amol.app.push.model.PushMessage;
import com.systop.amol.app.push.service.AdvPositionManager;
import com.systop.amol.app.push.service.PushMessageManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;
import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 首页的图片展示
 * </p>
 * 
 * @version 1.0
 * @author 王会璞
 * 
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IndexImageAction extends
		DefaultCrudAction<PushMessage,PushMessageManager> {

	private static final long serialVersionUID = 4823635298400166845L;
	
	private File attch;

	private String attchFileName;

	private String attchFolder = "/uploadFiles/fileAttch/";
	@Autowired
	private AdvPositionManager advPositionManager;
	/**
	 * 首页显示图片信息管理
	 * @return
	 */
	public String indexLbt(){
		page = PageUtil.getPage(getPageNo(), 15);
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer hql = new StringBuffer("from PushMessage o where o.type = 1 ");
		List args = new ArrayList();
		System.out.print("------------------"+getModel().getTitle());
		if (StringUtils.isNotBlank(getModel().getTitle())) {
			hql.append(" and o.title like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getTitle()));
		}
		String userId = getRequest().getParameter("userId");
		if(StringUtils.isNotBlank(userId)){
			hql.append(" and o.picOfUser.id=? ");
			args.add(Integer.parseInt(userId));
		}
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return "indexLbt";
	}
	/**
	 * 首页显示图片信息管理
	 * @return
	 */
	public String index(){
		page = PageUtil.getPage(getPageNo(), 15);
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer hql = new StringBuffer("from PushMessage o where o.type = 2 ");
		List args = new ArrayList();
		System.out.print("------------------"+getModel().getTitle());
		if (StringUtils.isNotBlank(getModel().getTitle())) {
				hql.append(" and o.title like ? ");
			  args.add(MatchMode.ANYWHERE.toMatchString(getModel().getTitle()));
		}
		String userId = getRequest().getParameter("userId");
		if(StringUtils.isNotBlank(userId)){
			hql.append(" and o.picOfUser.id=? ");
			args.add(Integer.parseInt(userId));
		}
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}
	public String indexAdv(){
		page = PageUtil.getPage(getPageNo(), 15);
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer hql = new StringBuffer("from PushMessage o where o.type = 3 or o.type=0 ");
		List args = new ArrayList();
		System.out.print("------------------"+getModel().getTitle());
		if (StringUtils.isNotBlank(getModel().getTitle())) {
			hql.append(" and o.title like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getTitle()));
		}
		String userId = getRequest().getParameter("userId");
		if(StringUtils.isNotBlank(userId)){
			hql.append(" and o.picOfUser.id=? ");
			args.add(Integer.parseInt(userId));
		}
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return "indexAdv";
	}
	public String removeAdv() {
		String result = INPUT;
		try {
			String pushMessageId = getRequest().getParameter("pushMessageId");
			if (StringUtils.isNotBlank(pushMessageId)) {
				getManager().remove(getManager().get(Integer.valueOf(pushMessageId)));
			}
			result = "successAdv";
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String remove() {
		String result = INPUT;
		try {
			String pushMessageId = getRequest().getParameter("pushMessageId");
			if (StringUtils.isNotBlank(pushMessageId)) {
				getManager().remove(getManager().get(Integer.valueOf(pushMessageId)));
			}
			result = SUCCESS;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public String view() {
		String pushMessageId = getRequest().getParameter("pushMessageId");
		if (StringUtils.isNotBlank(pushMessageId)) {
			getRequest().setAttribute("pushMessage",
					getManager().get(new Integer(pushMessageId)));
		}
		return "view";
	}
	@Override
	public String edit() {
		getRequest().setAttribute("userId",getRequest().getParameter("userId"));
		return super.edit();
	}
	public Map getPositionMap(){
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			return advPositionManager.getUnitsMap(user.getId());
		}
		return null;
	}
	@Override
	public String editAdv() {
		getPositionMap();
		return super.editAdv();
	}
	
	public String save() {
		attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
		if (attch != null) {
			String filePath = UpLoadUtil.doUpload(attch,
					attchFileName, attchFolder, getServletContext());
			getModel().setTitle(attchFileName);
			getModel().setImageURL(filePath);
			getModel().setType(2);
			System.out.println("----------------------"+getModel().getPicOfUser().getId());
			getModel().setContent(getRequest().getParameter("content"));
			String businessId = getRequest().getParameter("businessId");
			if(StringUtils.isNotBlank(businessId)){
				getModel().setBusinessId(Integer.valueOf(businessId));
			}
			try {
		getManager().save(getModel());
		} catch (Exception e) {
		e.printStackTrace();
		}
		}
		return "success";
	}
	public String saveAdv() {
		attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
		System.out.println("广告访问url是"+getRequest().getParameter("advUrl"));
		getModel().setAdvUrl(getRequest().getParameter("advUrl"));
		getModel().setContent(getRequest().getParameter("content"));
		String businessId = getRequest().getParameter("businessId");
		System.out.println(getModel().getStartTime()+"---------------------"+getModel().getEndTime());
		if(StringUtils.isNotBlank(businessId)){
			getModel().setBusinessId(Integer.valueOf(businessId));
		}
		if (attch != null) {
			String filePath = UpLoadUtil.doUpload(attch,
					attchFileName, attchFolder, getServletContext());
			getModel().setTitle(attchFileName);
			getModel().setImageURL(filePath);
			try {
				getManager().save(getModel());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "successAdv";
	}
	public File getAttch() {
		return attch;
	}

	public void setAttch(File attch) {
		this.attch = attch;
	}

	public String getAttchFileName() {
		return attchFileName;
	}

	public void setAttchFileName(String attchFileName) {
		this.attchFileName = attchFileName;
	}

}