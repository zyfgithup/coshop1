package com.systop.amol.app.push.phone;

import java.util.ArrayList;
import java.util.List;

import com.systop.amol.app.push.model.AdvPosition;
import com.systop.amol.app.push.service.AdvPositionManager;
import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.app.push.MessageConstants;
import com.systop.amol.app.push.model.PushMessage;
import com.systop.amol.app.push.service.PushMessageManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

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
public class PushMessagePhoneAction extends
		DefaultCrudAction<PushMessage, PushMessageManager> {

	private static final long serialVersionUID = 8485717682421443850L;
	/** 分页，页码 */
	private Integer pageNumber = 1;
	@Autowired
	private AdvPositionManager advPositionManager;
	/** 每页显示条数 */
	private Integer pageCount = 10;
	
	/** 推送值 */
	private String keyValue;
	
	/** 返回苹果推送值保存状态 */
	private String keyValueStatus;
	
	private List<PushMessage> pushMessageList;

	private List<AdvPosition> aPosList;


	/**
	 * 保存苹果推送值,传值传递keyValue
	 * @return success成功，error失败
	 */
	public String saveKeyValue(){
		if(StringUtils.isNotBlank(keyValue)){
			try {
//				if(AccessConnection.isPushKeyValue(keyValue)){
//					AccessConnection.saveKeyValue(keyValue);
//				}
				keyValueStatus = "success";
			} catch (Exception e) {
				keyValueStatus = "error";
			}
		}else{
			keyValueStatus = "error";
		}
		return "saveKeyValue";
	}
	public String indexLbt() {

		Page page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		StringBuffer hql = new StringBuffer("from PushMessage o where o.type ='0' ");
		List args = new ArrayList();
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		pushMessageList = page.getData();

		return "jsonList";
	}

	public String getAllPoses() {
		aPosList = advPositionManager.getAll();
		return "aPosList";
	}
	@Override
	public String index() {

		Page page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		StringBuffer hql = new StringBuffer("from PushMessage o where o.type = "+MessageConstants.PUSH_MESSAGE);
		List args = new ArrayList();
		
		if (StringUtils.isNotBlank(getModel().getTitle())) {
				hql.append(" and o.title like ? ");
			  args.add(MatchMode.ANYWHERE.toMatchString(getModel().getTitle()));
		}
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		pushMessageList = page.getData();
		
		return "jsonList";
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
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

	public List<PushMessage> getPushMessageList() {
		return pushMessageList;
	}

	public void setPushMessageList(List<PushMessage> pushMessageList) {
		this.pushMessageList = pushMessageList;
	}

	public List<AdvPosition> getaPosList() {
		return aPosList;
	}

	public void setaPosList(List<AdvPosition> aPosList) {
		this.aPosList = aPosList;
	}
}