package com.systop.amol.app.update.webapp;

import java.util.ArrayList;
import java.util.List;

import com.systop.amol.app.push.MessageConstants;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.app.update.model.UpdateApp;
import com.systop.amol.app.update.service.UpdateAppManager;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * <p>
 * 	更新软件Action
 * </p>
 * @see com.yhkj.app.update.model.UpdateApp
 * @version 1.0
 * @author 王会璞
 * 
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UpdateAppAction extends
		DefaultCrudAction<UpdateApp, UpdateAppManager> {

	/** 版本号code **/
	private String versionCode;
	
	/** 版本号名 称 */
  private String versionName;
	
	private static final long serialVersionUID = -7281645677530053032L;

	/** 更新信息 */
	private List<UpdateApp> updateAppList = new ArrayList<UpdateApp>();
	
	/**
	 * <p>
	 * 	更新app，versionCode,versionName需传入，如何不是最新版本会给客户端返回下载信息UpdateApp，
	 *  返回null代表客户端目前是最新版本或是客户端未传入versionCode,versionName
	 * </p>
	 * @return 
	 */
	public String update() {
		UpdateApp updateApp = getManager().get(Integer.valueOf(1));
		String path = getRequest().getContextPath();
		String basePath = getRequest().getScheme()+"://"+getRequest().getServerName()+":"+getRequest().getServerPort()+path+"/";
		updateApp.setAppURL(basePath+updateApp.getAppURL());
		updateAppList.add(updateApp);
//		System.out.println(updateApp.getVersionCode());
//		System.out.println("versionCode = "+versionCode+", versionName = "+versionName);
//		if(null != versionCode && null != versionName){
//			System.out.println("ddddddddddddd");
//			if(updateApp.getVersionCode().equals(versionCode) && updateApp.getVersionName().equals(versionName)){
//				System.out.println("yyyyyyyyyy");
//				updateApp = null;
//			}
//		}else{
//			System.out.println("dddddddddddddd");
////			updateApp = updateApp;
//		}
		return "jsonRst";
	}

	public static void main(String[] args){
		/*String path = getrequest.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";*/
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public List<UpdateApp> getUpdateAppList() {
		return updateAppList;
	}

	public void setUpdateAppList(List<UpdateApp> updateAppList) {
		this.updateAppList = updateAppList;
	}

}