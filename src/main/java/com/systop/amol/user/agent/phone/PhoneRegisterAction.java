package com.systop.amol.user.agent.phone;

import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.amol.user.agent.model.Partners;
import com.systop.amol.user.agent.service.PartnersManager;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.util.qrcode.QRcode;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * app用户注册
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneRegisterAction extends DefaultCrudAction<User, UserManager> {

	private static final long serialVersionUID = 671986626037342586L;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	UserManager userManager;
	@Autowired
	PartnersManager partnersManager;
	
	private String channelId;
	private String userIdBaiDuPush;
	
	private User user = new User();
	@Autowired
	private RegionManager regionManager;
	@Autowired
	private ProductSortManager productSortManager;
	
	private String msg;
	public String h5Register() {
		getResponse().setContentType("text/html;charset=UTF-8");
		String username = getRequest().getParameter("username");
		String password = getRequest().getParameter("password");
		String id = getRequest().getParameter("id");
		if(StringUtils.isNotBlank(id)){
			user.setTjmUser(getManager().get(Integer.parseInt(id)));
			user.setSuperior(getManager().get(Integer.parseInt(id)));
		}
		if (username != null && !"".equals(username.trim()) && password != null
				&& !"".equals(password.trim())) {
			user.setLoginId(username);
			user.setPassword(password);
			user.setNickName(username);
			try {
		user.setVisible("1");
		user.setCreateTime(new Date());
		int prosortId = 35454977;
		ProductSort ps = productSortManager.get(prosortId);
		user.setVipType(ps);
		user.setType("vip");
		getManager().save(user);
		//savePartners(Integer.parseInt(id),user.getId());
		user = getManager().getUser(username, passwordEncoder.encodePassword(password.trim(), null));
		String absolutPath = getRequest().getSession().getServletContext().getRealPath("/uploadFiles/ewmPic/"+user.getId()+".png");
		String path = getRequest().getContextPath();
		String basePath = getRequest().getScheme() + "://"
				+ getRequest().getServerName() + ":" + getRequest().getServerPort()
				+ path + "/";
		String content = basePath+"pages/amol/reglogin/regist.jsp?yqm="+user.getId();
		QRcode handler = new QRcode();
		handler.encoderQRCode(content,absolutPath);
		getModel().setEwmImageURl("/uploadFiles/ewmPic/"+user.getId()+".png");
		getManager().update(user);
		System.out.println("解析的内容为"+content);
		System.out.println("========encoder success");
		String decoderContent = handler.decoderQRCode(absolutPath);
		System.out.println("解析结果如下：");
		System.out.println(decoderContent);
		System.out.println("========decoder success!!!");
		} catch (NumberFormatException e) {
		e.printStackTrace();
		} catch (DataAccessException e) {
		e.printStackTrace();
		}
//			user.setChannelId(channelId);
//			user.setUserIdBaiDuPush(userIdBaiDuPush);
//			getManager().update(user);
		}
		
		return SUCCESS;
	}
	public String savePartners(Integer fxUserId,Integer registerId){
		   //分享人id
		   User appUser=userManager.get(fxUserId);
		   //注册人id
		   User regUser=userManager.get(registerId);
		   Partners partners=new Partners();
		   partners.setAppUser(regUser);
		   partners.setPartnerUser(appUser);
		   partners.setInDirectOrDirect("1");
		   partners.setRelativeTime(new Date());
		   partnersManager.save(partners);
		   //根据分享人id查询他的直接合伙人
		   User jjUser=partnersManager.getPartnersByPartnersId(fxUserId,"1");
		   if(null!=jjUser){
			   Partners partners2=new Partners();
			   partners2.setInDirectOrDirect("0");
			   partners2.setAppUser(regUser);
			   partners2.setPartnerUser(jjUser);
			   partners2.setRelativeTime(new Date());
			   partnersManager.save(partners2);
		   }
		   return SUCCESS;
		}
	/**
	 * app用户注册方法
	 * 
	 * 请求参数：username账号，password密码，regionId地区id
	 * 
	 * 成功返回user对象的json字符串，失败返回null
	 * 
	 * @return
	 */
	public String register() {
		getResponse().setContentType("text/html;charset=UTF-8");
		String username = getRequest().getParameter("username");
		String password = getRequest().getParameter("password");
		String id = getRequest().getParameter("id");
		if(StringUtils.isNotBlank(id)){
			user.setTjmUser(getManager().get(Integer.parseInt(id)));
			user.setSuperior(getManager().get(Integer.parseInt(id)));
		}
		System.out.println("00000000000000000000");
		if (username != null && !"".equals(username.trim()) && password != null
				&& !"".equals(password.trim())) {
			user.setLoginId(username);
			user.setPassword(password);
			user.setNickName(username);
			user.setPhone(username);
			try {
		user.setVisible("1");
		int prosortId = 35454977;
		ProductSort ps = productSortManager.get(prosortId);
		user.setVipType(ps);
		user.setType("vip");
		user.setCreateTime(new Date());
		getManager().save(user);
		user = getManager().getUser(username, passwordEncoder.encodePassword(password.trim(), null));
		String absolutPath = getRequest().getSession().getServletContext().getRealPath("/uploadFiles/ewmPic/"+user.getId()+".png");
		String path = getRequest().getContextPath();
		String basePath = getRequest().getScheme() + "://"
				+ getRequest().getServerName() + ":" + getRequest().getServerPort()
				+ path + "/";
		String content = basePath+"pages/amol/reglogin/regist.jsp?yqm="+user.getId();
		QRcode handler = new QRcode();
		handler.encoderQRCode(content,absolutPath);
		user.setEwmImageURl("/uploadFiles/ewmPic/"+user.getId()+".png");
		getManager().update(user);
		System.out.println("解析的内容为"+content);
		System.out.println("========encoder success");
		String decoderContent = handler.decoderQRCode(absolutPath);
		System.out.println("解析结果如下：");
		System.out.println(decoderContent);
		System.out.println("========decoder success!!!");
			} catch (NumberFormatException e) {
		e.printStackTrace();
		} catch (DataAccessException e) {
		e.printStackTrace();
		}
//			user.setChannelId(channelId);
//			user.setUserIdBaiDuPush(userIdBaiDuPush);
//			getManager().update(user);
		}
		
		return SUCCESS;
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
	
}
