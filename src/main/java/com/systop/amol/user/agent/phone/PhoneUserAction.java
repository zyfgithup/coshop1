package com.systop.amol.user.agent.phone;

import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.amol.user.agent.model.Partners;
import com.systop.amol.user.agent.model.RenZheng;
import com.systop.amol.user.agent.model.RzFile;
import com.systop.amol.user.agent.service.PartnersManager;
import com.systop.amol.user.agent.service.RenZhengManager;
import com.systop.amol.user.agent.service.RzFileManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;
import com.thirdParty.yunTongXun.SDKTestSendTemplateSMS;
import com.thirdParty.yunTongXun.SMSConstants;
import com.thirdParty.yunTongXun.Scsjs;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletContext;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * app用户登录
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneUserAction extends DefaultCrudAction<User,UserManager> {

	private static final long serialVersionUID = 671986626037342586L;

	// 对密码加密类
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	PartnersManager partnersManager;
	@Autowired
	RzFileManager rzFileManager;
	@Autowired
	RenZhengManager renZhengManager;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/** ajax校验微信用户手机号返回信息 */
	Map<String,Object> validatePhoneMessage = new HashMap<String, Object>();
	@Autowired
	UserManager userManager;
	/** 手机号 */
	private String phone;
	
	private User user;

	 SimpleDateFormat dtformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String pageNumber = "1";
	private String pageCount = "10";
	private File attch;
	private File[] fileList1;
	private File[] fileList2;
	private File[] fileList3;
	private File[] fileList4;
	private File[] fileList5;
	private File[] fileList6;
	private File[] fileList9;
	private String[] fileList1FileName;
	private String[] fileList2FileName;
	private String[] fileList3FileName;
	private String[] fileList4FileName;
	private String[] fileList5FileName;
	private String[] fileList6FileName;
	private String[] fileList9FileName;
	private String attchFileName;
	private String attchFolder = "/uploadFiles/fileAttch/";
	/** 积分 */
	private Map<String,Integer> result = new HashMap<String,Integer>();
	@Autowired
	ProductSortManager productSortManager;
	private String userBankSort;
	private List<Partners> list=new ArrayList<Partners>();
	private List<Map<String,Object>> newList = new ArrayList<Map<String,Object>>();
	//我的认证返回接口
	public String getMyRz(){
		String userId = getRequest().getParameter("userId");
		String sql = "select id,rz_type as rzType,if_tg as ifTg from ren_zheng where user_id="+Integer.parseInt(userId);
		newList = jdbcTemplate.queryForList(sql);
		return "newList";
	}
	public String uploadRzFile(){
		String rzId = getRequest().getParameter("rzId");
		RenZheng rz = null;
		if(StringUtils.isNotBlank(rzId)){
			rz = renZhengManager.get(Integer.parseInt(rzId));
		}else{
			rz = new RenZheng();
			String userId = getRequest().getParameter("userId");
			User user = getManager().get(Integer.parseInt(userId));
			rz.setUser(user);
		}
		String name = getRequest().getParameter("name");
		rz.setName(name);
		String phone = getRequest().getParameter("phone");
		String address = getRequest().getParameter("address");
		String bankCard = getRequest().getParameter("bankCard");
		rz.setBankCard(bankCard);
		rz.setAddress(address);
		rz.setPhone(phone);
		String rzType = getRequest().getParameter("rzType");
		String noUpdateIds = getRequest().getParameter("noUpdateIds");
		if (StringUtils.isNotBlank(rzId)) {
			rzFileManager.deleteRzFile(rzId, noUpdateIds);
		}
		rz.setRzType(rzType);
		if(StringUtils.isNotBlank(rzId)) {
			renZhengManager.update(rz);
		}else{
			rz.setIftg("0");
			rz.setCreateTime(new Date());
			renZhengManager.save(rz);
		}
		List<RzFile> list = new ArrayList<RzFile>();
		//认证类型 1 保险认证2 加油车3 企业4 实名认证5 维修厂6加油站认证
		//1 身份证2 营业执照3车辆行驶证 4 驾驶证 5 危化品运输资格证6 税务登记证证明 7组织机构代码证 8其他资料证明9加油站经营证明
		if(null!=rzType){
			RzFile rf = null;
			if(rzType.equals("6")){
				//身份证图片上传
				if(null!=fileList1) {
					for (int i = 0; i < fileList1.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList1[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList1[i],
									fileList1FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("1");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
				//营业执照图片上传
				if(null!=fileList9) {
					for (int i = 0; i < fileList9.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList9[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList9[i],
									fileList9FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("9");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
				//营业执照图片上传
				if(null!=fileList2) {
					for (int i = 0; i < fileList2.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList2[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList2[i],
									fileList2FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("2");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
			}
			if(rzType.equals("1")){
				//身份证图片上传
				if(null!=fileList1) {
					for (int i = 0; i < fileList1.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList1[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList1[i],
									fileList1FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("1");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
				//营业执照图片上传
				if(null!=fileList2) {
					for (int i = 0; i < fileList2.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList2[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList2[i],
									fileList2FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("2");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
		}
			if(rzType.equals("2")) {
				//身份证图片上传
				if (null != fileList1) {
					for (int i = 0; i < fileList1.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList1[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList1[i],
									fileList1FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("1");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
				//车辆行驶证图片上传
				if (null != fileList3) {
					for (int i = 0; i < fileList3.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList3[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList3[i],
									fileList3FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("3");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
				//驾驶证图片上传
				if (null != fileList4) {
					for (int i = 0; i < fileList4.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList4[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList4[i],
									fileList4FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("4");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
				//驾驶证图片上传
				if (null != fileList5) {
					for (int i = 0; i < fileList5.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList5[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList5[i],
									fileList5FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("5");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
			}
			if(rzType.equals("3")) {
				//身份证图片上传
				if (null != fileList1) {
					for (int i = 0; i < fileList1.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList1[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList1[i],
									fileList1FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("1");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
				//营业执照图片上传
				if (null != fileList2) {
					for (int i = 0; i < fileList2.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList2[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList2[i],
									fileList2FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("2");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
				//税务登记证照图片上传
				if (null != fileList6) {
					for (int i = 0; i < fileList6.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList6[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList6[i],
									fileList6FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("6");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
			}
			if(rzType.equals("4")) {
				//身份证照图片上传
				if (null != fileList1 && fileList1.length > 0) {
					for (int i = 0; i < fileList1.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList1[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList1[i],
									fileList1FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("1");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
			}
			if(rzType.equals("5")) {
				//身份证图片上传

				if (null != fileList1) {
					for (int i = 0; i < fileList1.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList1[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList1[i],
									fileList1FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setRenZheng(rz);
							rf.setFileType("1");
							list.add(rf);
						}
					}
				}
				//营业执照图片上传
				if (null != fileList2) {
					for (int i = 0; i < fileList2.length; i++) {
						rf = new RzFile();
						attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
						if (fileList2[i] != null) {
							String filePath = UpLoadUtil.doUpload(fileList2[i],
									fileList2FileName[i], attchFolder, getServletContext());
							rf.setImageUrl(filePath);
							rf.setFileType("2");
							rf.setRenZheng(rz);
							list.add(rf);
						}
					}
				}
			}
			for (RzFile temp : list){
				rzFileManager.save(temp);
			}
			if(StringUtils.isNotBlank(rzId)){
				validatePhoneMessage.put("msg","修改成功");
			}else {
				validatePhoneMessage.put("msg", "提交成功");
			}
		}
		return "validateCode";
	}
	public String uploadTx(){
		String userId = getRequest().getParameter("userId");
		User user = getManager().get(Integer.parseInt(userId));
		attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
		if (attch != null) {
			String filePath = UpLoadUtil.doUpload(attch,
					attchFileName, attchFolder, getServletContext());
			user.setImageURL(filePath);
		}
		getManager().update(user);
		validatePhoneMessage.put("msg","上传成功");
		return "gainValidateCode";
	}
	//关联用户保存(直接合伙人间接合伙人)
	public String appUserPartners(){
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		List<Object> args = new ArrayList<Object>();
		String userId=getRequest().getParameter("appId");
		String type=getRequest().getParameter("type");
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select id,login_id,name,createTime,");
		if(type.equals("1")){
			sbsql.append("zj_money as turnmoney from ");
			sbsql.append("(select * from  users  where superior_id="+userId+")a");
		}else{
			sbsql.append("jj_money as turnmoney from ");
			sbsql.append("(select * from users where superior_id in");
			sbsql.append("  (select  id  from  users  where superior_id="+userId+"))a");
		}
		System.out.println("partrntsql-------"+sbsql.toString());
		List<Map<String,Object>> userData=jdbcTemplate.queryForList(sbsql.toString());
		for(int i=0;i < userData.size();i++){
			Partners partners = new Partners();
			partners.setId(Integer.parseInt(userData.get(i).get("id").toString()));
			if(null!=userData.get(i).get("name")){
				partners.setPartnerName(userData.get(i).get("name").toString());
			}else{
				partners.setPartnerName(userData.get(i).get("login_id").toString());
			}
			if(null!=userData.get(i).get("turnmoney")){
				partners.setBringMoney(Double.parseDouble(userData.get(i).get("turnmoney").toString()));
			}else{
				partners.setBringMoney(0.0);
			}
			try {
				partners.setRelativeTime(dtformat.parse(userData.get(i).get("createTime").toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			partners.setBringJf(0);
			list.add(partners);
		}
		System.out.println("----------list.size()---------?"+list.size());
		return "partnerLink";
	}
	public String userIntegral(){
		User user = getManager().get(Integer.valueOf(getRequest().getParameter("userId")));
		result.put("integral", user.getIntegral());
		return "userIntegral";
	}
	/**
	 * 通过用户id得到用户信息
	 * @return
	 */
	public String getUsersInfos(){
	    user = getManager().get(Integer.valueOf(getRequest().getParameter("userId")));
	    if(null != user.getBankSort() && null != user.getBankSort().getId()){
	    	user.setBankId(Integer.valueOf(user.getBankSort().getId()));
	    }
	    
		return "user";
	}
	/**
	 * 修改个人信息
	 * 
	 * 请求参数：id用户id；name用户真实姓名；idCard用户身份证号
	 * 
	 * 返回数据：{"result",true}，成功返回true，失败返回false
	 * 
	 */
	public String update() {
	boolean result = true;
	String message = null;
	String id = getRequest().getParameter("id");
	String name = getRequest().getParameter("name");
	String idCard = getRequest().getParameter("idCard");
	String yhkh = getRequest().getParameter("yhkh");
	Integer bankId = Integer.parseInt(userBankSort);
	System.out.println("id = "+id);
	if (StringUtils.isNotBlank(id)) {
		User user = getManager().get(Integer.valueOf(id));
		if (yhkh != null && !"".equals(yhkh.trim())) {
			user.setYhkh(yhkh);
			} else {
			result = false;
			message = "亲，请填写银行卡号";
			}
		ProductSort productSort=productSortManager.get(bankId);
		user.setYhmc(productSort.getName());
		user.setBankSort(productSort);
		if (name != null && !"".equals(name.trim())) {
		user.setName(name);
		} else {
		result = false;
		message = "亲，请填写真实姓名";
		}

		if (idCard != null && !"".equals(idCard.trim())) {
		user.setIdCard(idCard);
		} else {
		result = false;
		message = "亲，请填写身份证号";
		}

		if (result) {
			getManager().update(user);
		}
	}else{
		result = false;
		message = "需传有效用户id";
	}
	validatePhoneMessage.put("result", result);
	if(StringUtils.isNotBlank(message)){
		validatePhoneMessage.put("message", message);
	}
	return "update";
	}

	/** 修改密码 */
	public String updatePassword() {
	boolean result = false;
	String phone = getRequest().getParameter("phone");
	String curpass = getRequest().getParameter("curpass");
	curpass = passwordEncoder.encodePassword(curpass.trim(), null);
	User curUser = getManager().getUser(phone, curpass);
	String password = getRequest().getParameter("password");
	User user = null;
		if(null!=curUser) {
			if (phone != null && !"".equals(phone.trim()) && password != null
					&& !"".equals(password.trim())) {
				password = passwordEncoder.encodePassword(password.trim(), null);
				user = getManager().getUserPhone(phone);
				if (null != user) {
					user.setPassword(password);
					getManager().update(user);
					result = true;
				} else {
					result = false;
				}
			}
		}else{
			result = false;
		}
	System.out.println(user);

	validatePhoneMessage.put("result", result);

	return "update_password";
	}
	/** 修改密码 */
	public String forgetPassword() {
		boolean result = false;
		String phone = getRequest().getParameter("phone");
		String password = getRequest().getParameter("password");
			if (phone != null && !"".equals(phone.trim()) && password != null
					&& !"".equals(password.trim())) {
				password = passwordEncoder.encodePassword(password.trim(), null);
				user = getManager().getUserPhone(phone);
				if (null != user) {
					user.setPassword(password);
					getManager().update(user);
					result = true;
				} else {
					result = false;
				}
			}
		validatePhoneMessage.put("result", result);

		return "update_password";
	}

	/**
	 * 校验用户手机号是否存在
	 * 
	 * 请求数据：phone，手机号
	 * 
	 * 返回数据：{"result",false}，用户存在true，不存在false
	 */
	public String validatePhone() {

	User user = getManager().findObject("from User o where o.loginId = ? and o.visible = '1' ",
		phone);
	System.out.println("user = "+user);
	boolean result = false;
	if (null != user) {
		result = true;
	}

	validatePhoneMessage.put("result", result);

	return "validate_phone";
	}

	/**
	 * 校验验证码
	 * 
	 * 请求参数：validateCode，用户收到的短信验证码；phone，手机号
	 * 
	 * 返回数据：{"result",true}，校验成功true，校验失败false
	 * 
	 * @return
	 */
	public String validateCode() {

	boolean result = false;

	String validateCode = getRequest().getParameter("validateCode");
	String phone = getRequest().getParameter("phone");

	ServletContext servletContext = getRequest().getSession()
		.getServletContext();
	Object sessionValidateCode = servletContext.getAttribute(phone);

	System.out.println("sessionValidateCode = " + sessionValidateCode + "  "
		+ validateCode.equals(sessionValidateCode.toString()));

	if (null != sessionValidateCode
		&& validateCode.equals(sessionValidateCode.toString())) {
		result = true;
	}

	validatePhoneMessage.put("result", result);

	return "validateCode";
	}
public static void main(String[] args){
	System.out.println("111111111111111---------------11111111111111");
	SDKTestSendTemplateSMS.sendSmss("15612930472", SMSConstants.VALIDATE_CODE,"{\"code\":\"1234\",\"product\":\"ddd\"}","注册验证"
	);
}
	/**
	 * 用户获取短信验证码
	 * 
	 * 请求参数：phone，手机号
	 * 
	 * 返回数据：{"result",true}，短信已发送true
	 * 
	 * @return
	 */
	public String gainValidateCode() {

	boolean result = true;
		//String validateCodeStr = "1234";
	String validateCodeStr = Scsjs.scsjs();

	System.out.println("1 phone : " + phone + ", 验证码：" + validateCodeStr);
	System.out.println("-----------------------phone"+phone);
	SDKTestSendTemplateSMS.sendSmss(phone, SMSConstants.VALIDATE_CODE,"{\"code\":'"+validateCodeStr+"',\"product\":\"56智配\"}"
	,"注册验证");
	final ServletContext servletContext = getRequest().getSession()
		.getServletContext();
	servletContext.setAttribute(phone, validateCodeStr);
	new Thread(new Runnable() {
		@Override
		public void run() {
		try {
			Thread.sleep(1000 * 60 * 60
				* Integer.valueOf(SMSConstants.EFFECTIVE_MINUTE));
			servletContext.removeAttribute(phone);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // 10秒
		}
	}).start();

	System.out.println("2 phone : " + phone + ", 验证码：" + validateCodeStr);

	validatePhoneMessage.put("result", result);

	return "gainValidateCode";
	}

	public Map<String, Object> getValidatePhoneMessage() {
	return validatePhoneMessage;
	}

	public void setValidatePhoneMessage(
		Map<String, Object> validatePhoneMessage) {
	this.validatePhoneMessage = validatePhoneMessage;
	}

	public String getPhone() {
	return phone;
	}

	public void setPhone(String phone) {
	this.phone = phone;
	}

	public Map<String, Integer> getResult() {
		return result;
	}

	public void setResult(Map<String, Integer> result) {
		this.result = result;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ProductSortManager getProductSortManager() {
		return productSortManager;
	}
	public void setProductSortManager(ProductSortManager productSortManager) {
		this.productSortManager = productSortManager;
	}
	public String getUserBankSort() {
		return userBankSort;
	}
	public void setUserBankSort(String userBankSort) {
		this.userBankSort = userBankSort;
	}
	public String getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getPageCount() {
		return pageCount;
	}
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	public List<Partners> getList() {
		return list;
	}
	public void setList(List<Partners> list) {
		this.list = list;
	}

	public String getAttchFileName() {
		return attchFileName;
	}

	public void setAttchFileName(String attchFileName) {
		this.attchFileName = attchFileName;
	}

	public File getAttch() {
		return attch;
	}

	public void setAttch(File attch) {
		this.attch = attch;
	}

	public File[] getFileList1() {
		return fileList1;
	}

	public void setFileList1(File[] fileList1) {
		this.fileList1 = fileList1;
	}

	public File[] getFileList2() {
		return fileList2;
	}

	public void setFileList2(File[] fileList2) {
		this.fileList2 = fileList2;
	}

	public File[] getFileList3() {
		return fileList3;
	}

	public void setFileList3(File[] fileList3) {
		this.fileList3 = fileList3;
	}

	public File[] getFileList4() {
		return fileList4;
	}

	public void setFileList4(File[] fileList4) {
		this.fileList4 = fileList4;
	}

	public File[] getFileList5() {
		return fileList5;
	}

	public void setFileList5(File[] fileList5) {
		this.fileList5 = fileList5;
	}

	public File[] getFileList6() {
		return fileList6;
	}

	public void setFileList6(File[] fileList6) {
		this.fileList6 = fileList6;
	}

	public String[] getFileList1FileName() {
		return fileList1FileName;
	}

	public void setFileList1FileName(String[] fileList1FileName) {
		this.fileList1FileName = fileList1FileName;
	}

	public String[] getFileList2FileName() {
		return fileList2FileName;
	}

	public void setFileList2FileName(String[] fileList2FileName) {
		this.fileList2FileName = fileList2FileName;
	}

	public String[] getFileList3FileName() {
		return fileList3FileName;
	}

	public void setFileList3FileName(String[] fileList3FileName) {
		this.fileList3FileName = fileList3FileName;
	}

	public String[] getFileList4FileName() {
		return fileList4FileName;
	}

	public void setFileList4FileName(String[] fileList4FileName) {
		this.fileList4FileName = fileList4FileName;
	}

	public String[] getFileList5FileName() {
		return fileList5FileName;
	}

	public void setFileList5FileName(String[] fileList5FileName) {
		this.fileList5FileName = fileList5FileName;
	}

	public String[] getFileList6FileName() {
		return fileList6FileName;
	}

	public void setFileList6FileName(String[] fileList6FileName) {
		this.fileList6FileName = fileList6FileName;
	}

	public List<Map<String, Object>> getNewList() {
		return newList;
	}

	public void setNewList(List<Map<String, Object>> newList) {
		this.newList = newList;
	}

	public String[] getFileList9FileName() {
		return fileList9FileName;
	}

	public void setFileList9FileName(String[] fileList9FileName) {
		this.fileList9FileName = fileList9FileName;
	}

	public File[] getFileList9() {
		return fileList9;
	}

	public void setFileList9(File[] fileList9) {
		this.fileList9 = fileList9;
	}
}
