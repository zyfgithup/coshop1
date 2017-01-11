package com.systop.amol.user.agent.phone;

import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.util.DateUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * app用户登录
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneLoginAction extends DefaultCrudAction<User, UserManager> {

	private static final long serialVersionUID = 671986626037342586L;

	// 对密码加密类
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private String channelId;
	private String userIdBaiDuPush;
	private User user;
	private String msg;
	private File t_photo;
	private String t_photoFileName;
	private Map<String,Object> result = new HashMap<String, Object>();
	private String attchFolder = "/uploadFiles/fileAttch/";
	public String qqOpenLogin(){
		String nickName = getRequest().getParameter("t_nickname");
		String accessToken = getRequest().getParameter("access_token");
		String openId = getRequest().getParameter("openid");
		getModel().setLoginId(nickName);
		getModel().setNickName(nickName);
		String openType = getRequest().getParameter("openType");
		if(StringUtils.isNotBlank(openType)){
			getModel().setOpenType("微博");
		}else{
			getModel().setOpenType("QQ");
		}
		getModel().setOpenId(openId);
		getModel().setAccessToken(accessToken);
		getModel().setCreateTime(new Date());
		getModel().setType("app_user");
		attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
		String filePath = getRequest().getParameter("filePath");
		System.out.println("----------------您传过来的头像地址是="+filePath);
		getModel().setImageURL(filePath);
		/*if (t_photo != null) {
			System.out.println("11111111111111111111111");
			filePath = UpLoadUtil.doUpload(t_photo,
					t_photoFileName, attchFolder, getServletContext());
			System.out.println("---------------------------filePath="+filePath);
			getModel().setImageURL(filePath);
		}*/
		User user1 = getManager().getUserByOpentId(openId);
		if(null==user1) {
			getManager().save(getModel());
		}else{
			user1.setAccessToken(accessToken);
			user1.setLoginId(nickName);
			user1.setNickName(nickName);
			user1.setImageURL(filePath);
			getManager().update(user1);
		}
		user = getManager().getUserByOpentId(openId);
		return "success";
	}
	public String wxOpenLogin(){
		String nickName = getRequest().getParameter("t_nickname");
		String accessToken = getRequest().getParameter("access_token");
		String openId = getRequest().getParameter("openid");
		getModel().setLoginId(nickName);
		getModel().setNickName(nickName);
		getModel().setOpenType("wx");
		getModel().setOpenId(openId);
		getModel().setAccessToken(accessToken);
		getModel().setCreateTime(new Date());
		getModel().setType("app_user");
		String filePath = getRequest().getParameter("filePath");
		System.out.println("----------------您传过来的头像地址是="+filePath);
		getModel().setImageURL(filePath);
		User user1 = getManager().getUserByOpentId(openId);
		if(null==user1) {
			getManager().save(getModel());
		}else{
			user1.setAccessToken(accessToken);
			user1.setLoginId(nickName);
			user1.setNickName(nickName);
			user1.setImageURL(filePath);
			getManager().update(user1);
		}
		user = getManager().getUserByOpentId(openId);
		return "success";
	}
	/**
	 * app用户登陆方法
	 * 请求参数：username账号，password密码
	 * 
	 * 成功返回user对象的json字符串，失败返回null
	 * @return
	 */
	public String login() {
		getResponse().setContentType("text/html;charset=UTF-8");
		String username = getRequest().getParameter("username");
		String password = getRequest().getParameter("password");
		if (username != null && !"".equals(username.trim()) && password != null
				&& !"".equals(password.trim())) {
			password = passwordEncoder.encodePassword(password.trim(), null);
			user = getManager().getUser(username, password);
			if(null!=user.getFxsjb() && user.getFxsjb().equals("jyc")){
				if(null!=user.getSuperior()){
					user.setSuperiorId(user.getSuperior().getId());
				}
			}
			System.out.println("----------------登录查到的user为="+user);
			if(null!=user) {
				List<User> uList = getManager().getMerUserByappId(user.getId());
				if (null == user) {
					msg = "input";
					return "input";
				}
				if(null != uList && uList.size()>0){
					for (User u : uList){
						//隐藏的为客户服务器
						if(null!=u.getProductSort() && (u.getProductSort().getId()==41418753 || u.getProductSort().getId()==41418754)){
							user.setMerId(u.getId());
						}
						if(null!=u.getProductSort() && u.getProductSort().getId()==41418755){
							user.setBxMerId(u.getId());
						}
						if(null!=u.getProductSort() && u.getProductSort().getId()==41418752){
							user.setWxMerId(u.getId());
						}
						/*if(null!=u.getProductSort() && u.getProductSort().getId()==35258368){
							user.setMerId(u.getId());
						}
						if(null!=u.getProductSort() && u.getProductSort().getId()==39518208){
							user.setBxMerId(u.getId());
						}
						if(null!=u.getProductSort() && u.getProductSort().getId()==39518209){
							user.setWxMerId(u.getId());
						}*/
					}
				}
			}
//			user.setChannelId(channelId);
//			user.setUserIdBaiDuPush(userIdBaiDuPush);
//			getManager().update(user);
		}return SUCCESS;
	}
	
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getUserIdBaiDuPush() {
		return userIdBaiDuPush;
	}

	public void setUserIdBaiDuPush(String userIdBaiDuPush) {
		this.userIdBaiDuPush = userIdBaiDuPush;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	public String getAttchFolder() {
		return attchFolder;
	}

	public void setAttchFolder(String attchFolder) {
		this.attchFolder = attchFolder;
	}

	public File getT_photo() {
		return t_photo;
	}

	public void setT_photo(File t_photo) {
		this.t_photo = t_photo;
	}

	public String getT_photoFileName() {
		return t_photoFileName;
	}

	public void setT_photoFileName(String t_photoFileName) {
		this.t_photoFileName = t_photoFileName;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
}
