package com.systop.amol.app.push.webapp;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import com.systop.amol.app.push.model.AdvPosition;
import com.systop.amol.app.push.service.AdvPositionManager;
import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.app.push.MessageConstants;
import com.systop.amol.app.push.Status;
import com.systop.amol.app.push.model.IOSPush;
import com.systop.amol.app.push.model.PushMessage;
import com.systop.amol.app.push.service.PushMessageManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;

/**
 * <p>
 * 推送Action，供网站调用该接口推送信息，保存消息成功后调用该接口，将消息推送出去
 * </p>
 * 
 * @see com.yhkj.app.push.model.PushMessage
 * @version 1.0
 * @author 王会璞
 * 
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PushMessageAction extends
		DefaultCrudAction<PushMessage, PushMessageManager> {

	private static final long serialVersionUID = 5855587303581149082L;
	private final static  String appKey ="99e00c56584d0d266e8dc360";
	private final static  String masterSecret = "18dd15d6b96b95fdde7fdd10";
	/** 推送信息 */
	private PushMessage pushMessage;
	/** 分页，页码 */
	private Integer pageNoNumber = 1;
	/** 每页显示条数 */
	private Integer pageSizeNumber = 10;
	/** 开始时间 【有效期】 */
	private String startDate;
	private static String alert;

	/** 结束时间【有效期】 */
	private String endDate;
	/** 推送值 */
	private String keyValue;
	/** 返回苹果推送值保存状态 */
	private String keyValueStatus;
	private File attch;
	private String attchFileName;
	private String attchFolder = "/uploadFiles/fileAttch/";
	@Autowired
	private AdvPositionManager advPositionManager;
	@Resource
	UserManager userManager;
	/**
	 * <p>
	 * 需要推送信息时调用该接口
	 * </p>
	 * @see
	 * @return
	 * @throws APIRequestException 
	 * @throws APIConnectionException 
	 */
	public String push() throws Exception {
		String content= getRequest().getParameter("content");
		alert=content;
		Status status = Status.FAILURE;
		Integer pushMessageId = null;
		List args = new ArrayList();
		PushMessage pm=null;
		if(StringUtils.isNotBlank(getRequest().getParameter("pushMessageId"))){
			pushMessageId = Integer.parseInt(getRequest().getParameter("pushMessageId"));
			pm=getManager().get(pushMessageId);
		}
		IOSPush ip=new IOSPush();
		ClientConfig config = ClientConfig.getInstance();
	  	config.setPushHostName("https://api.jpush.cn");
		JPushClient push=new JPushClient(masterSecret, appKey,3);
		PushPayload payload = buildPushObject_all_all_alert();
 try {
	 if(null!=pm.getRegion()){
	 List<User> list=userManager.getUserByRegionCode(pm.getRegion().getCode());
		for (User user : list) {
			if(user.getIosToken()!=null&&!"".equals(user.getIosToken())){
				for (int i = 0; i < user.getIosToken().split(",").length; i++) {
					ip.iosPush(user.getIosToken().split("，")[i], pm.getContent());
				}
			}
			if(user.getAndroidToken()!=null&&!"".equals(user.getAndroidToken())){
				for(int i=0;i<user.getAndroidToken().split(",").length;i++){
					 PushResult result = push.sendAndroidNotificationWithRegistrationID(pm.getTitle(),pm.getContent(),null, user.getAndroidToken().split(",")[i]);
				}
			}
		}
	 }
	 else{//ios全推(需要遍历所有token值)
		 List<User> list=userManager.getAppIosUser();
		 if(list.size()>0&&list!=null){
			 for (User u : list) {
				for (int i = 0; i < u.getIosToken().split(",").length; i++) {
					ip.iosPush(u.getIosToken().split(",")[i],pm.getContent());
				}
			}
		 }
		 //安卓全推
		 PushResult result= push.sendPush(payload);
	 }
      } catch (APIConnectionException e) {
          LOG.error("Connection error. Should retry later. ", e);

      } catch (APIRequestException e) {
          LOG.error("Error response from JPush server. Should review and fix it. ", e);
          LOG.info("HTTP Status: " + e.getStatus());
          LOG.info("Error Code: " + e.getErrorCode());
          LOG.info("Error Message: " + e.getErrorMessage());
          LOG.info("Msg ID: " + e.getMsgId());
      }
		 status = Status.SUCCESS;
			pm.setStatus(status);
			pm.setType(1);
			getManager().update(pm);		//getManager().updatePushStatus(status, pushMessage.getId());
		return SUCCESS;
	}
	public static PushPayload buildPushObject_all_all_alert() {
    return PushPayload.alertAll(alert);
}
	/**
	 * 首页显示图片信息
	 * @return
	 */
	public String indexImage(){
		page = PageUtil.getPage(getPageNo(), 4);

		StringBuffer sql = new StringBuffer("");

//		if (StringUtils.isNotBlank(getModel().getTitle())) {
			sql.append(" and type = 2 ");
//		}

//		page = AccessConnection.pageQueryPushMessage(page.getPageNo(),
//				page.getPageSize(), sql.toString());
		restorePageData(page);
		return "indexImage";
	}
	/**
	 * 保存苹果推送值,传值传递keyValue
	 * @return success成功，error失败
	 */
	public String saveKeyValue(){
			try {
//				if(AccessConnection.isPushKeyValue(keyValue)){
//					AccessConnection.saveKeyValue(keyValue);
//				}
				User user=userManager.get(Integer.parseInt(this.getRequest().getParameter("id")));
				String eAndoridToken=user.getAndroidToken();
				String eIosToken=user.getIosToken();
				String androidToken=this.getRequest().getParameter("androidToken");
				String iosToken=this.getRequest().getParameter("iosToken");
				if(StringUtils.isNotBlank(androidToken)){
					if(eAndoridToken==null||"".equals(eAndoridToken)){
						user.setAndroidToken(androidToken);
					}else if(eAndoridToken.indexOf(androidToken)==-1){
						user.setAndroidToken(eAndoridToken+","+androidToken);
					}
				}
				if(StringUtils.isNotBlank(iosToken)){
					iosToken=iosToken.substring(iosToken.indexOf("<"), iosToken.indexOf(">"));
					if(eIosToken==null||"".equals(eIosToken)){
						user.setIosToken(iosToken);
					}else if(eIosToken.indexOf(iosToken)==-1){
						user.setIosToken(eIosToken+","+iosToken);
					}
				}
				userManager.update(user);
				keyValueStatus = "success";
			} catch (Exception e) {
				keyValueStatus = "error";
			}
		return "saveKeyValue";
	}
	
	@Override
	public String remove(){
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
	public String edit() {
		String pushMessageId = getRequest().getParameter("pushMessageId");
		if (StringUtils.isNotBlank(pushMessageId)) {
//			MessageBusiness messageBusiness = AccessConnection.getPushMessageBusiness(new Integer(pushMessageId));
//			setModel(messageBusiness.getPushMessage());
//			getRequest().setAttribute("business", messageBusiness.getBusiness());
			PushMessage pushMessage = getManager().get(new Integer(pushMessageId));
			setModel(pushMessage);
			System.out.println("edit = "+pushMessage);
		}
		List<AdvPosition> advList = advPositionManager.getAll();
		System.out.println("-=-=-----------------------advList.size="+advList.size());
		if(advList.size()>0){
		getRequest().setAttribute("apList",advPositionManager.getAll());
		}
		return "pushUI";
	}
	@Override
	public String save() {
		try {
			Integer pushMessageId = null;
			if(StringUtils.isNotBlank(getRequest().getParameter("pushMessageId"))){
				pushMessageId = Integer.parseInt(getRequest().getParameter("pushMessageId"));
				getModel().setId(pushMessageId);
			}
			getModel().setStartTime(DateUtil.convertStringToDate(startDate));
			getModel().setEndTime(DateUtil.convertStringToDate(endDate));
			getModel().setType(1);
			if(null == getModel().getId()){
				getModel().setStatus(Status.UNSENT);
			}
			System.out.println(getModel().getRegion()+":"+getModel().getRegion().getId());
			if(null == getModel().getRegion() || 0 == getModel().getRegion().getId() ){
				System.out.println("dddddddddddddddd");
				getModel().setRegion(null);
			}
			String businessId = getRequest().getParameter("businessId");
			if(StringUtils.isNotBlank(businessId)){
				getModel().setBusinessId(Integer.valueOf(businessId));
			}
			attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
			System.out.println("attch = "+attch);
			if (attch != null) {
				System.out.println("ssssssssssss");
				String filePath = UpLoadUtil.doUpload(attch,
						attchFileName, attchFolder, getServletContext());
				getModel().setImageURL(filePath);
				System.out.println(filePath);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		getManager().save(getModel());
		return SUCCESS;
	}
	@Override
	public String index() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer hql = new StringBuffer("from PushMessage o where o.type = "+MessageConstants.PUSH_MESSAGE);
		List args = new ArrayList();
		
		if (StringUtils.isNotBlank(getModel().getTitle())) {
				hql.append(" and o.title like ? ");
			  args.add(MatchMode.ANYWHERE.toMatchString(getModel().getTitle()));
		}
		hql.append(" order by o.pushMessageTime desc ");
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		
//		page = PageUtil.getPage(getPageNo(), 15);
//		StringBuffer hql = new StringBuffer("from PushMessage o where o.type = ");
//		List args = new ArrayList();
//
//		if (StringUtils.isNotBlank(getModel().getTitle())) {
//			hql.append(" and o.title like ? ");
//		  args.add(MatchMode.ANYWHERE.toMatchString(getModel().getTitle()));
//	}
//
//		
////		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
////		System.out.println(user.getName()+"=========="+user.getRoles().size());
//		
////		Set<Role> set = user.getRoles();
////		Iterator<Role> it = set.iterator();
////		while(it.hasNext()){
////			System.out.println(it.next().getName());
////		}
//		
//		page = getManager().pageQuery(page, hql.toString(), args.toArray());
//		restorePageData(page);
//		System.out.println(hql);
//		System.out.println(page.getData());
		
		return INDEX;
	}

	@Override
	public String view() {
		String pushMessageId = getRequest().getParameter("pushMessageId");
		if (StringUtils.isNotBlank(pushMessageId)) {
//			MessageBusiness messageBusiness = AccessConnection.getPushMessageBusiness(new Integer(pushMessageId));
//			getRequest().setAttribute("business", messageBusiness.getBusiness());
//			getRequest().setAttribute("pushMessage",messageBusiness.getPushMessage());
			PushMessage pushMessage = getManager().get(new Integer(pushMessageId));
			setModel(pushMessage);
			System.out.println("pushMessage "+pushMessage);
		}
		return "view";
	}

	/**
	 * 消息历史记录
	 * 
	 * @return
	 */
	public String pushMessageList() {

//		page = getManager().pageQuery(page, hql, args).pageQueryPushMessage(pageNoNumber, pageSizeNumber,
//				" and status = '"+Status.SUCCESS.toString()+"' and type = 1");
		restorePageData(page);
		System.out.println(page.getData());
		return "jsonList";
	}

	public PushMessage getPushMessage() {
		return pushMessage;
	}

	public void setPushMessage(PushMessage pushMessage) {
		this.pushMessage = pushMessage;
	}

	public Integer getPageNoNumber() {
		return pageNoNumber;
	}

	public void setPageNoNumber(Integer pageNoNumber) {
		this.pageNoNumber = pageNoNumber;
	}

	public Integer getPageSizeNumber() {
		return pageSizeNumber;
	}

	public void setPageSizeNumber(Integer pageSizeNumber) {
		this.pageSizeNumber = pageSizeNumber;
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

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getKeyValueStatus() {
		return keyValueStatus;
	}

	public void setKeyValueStatus(String keyValueStatus) {
		this.keyValueStatus = keyValueStatus;
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