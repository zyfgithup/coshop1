package com.systop.amol.base.yyy.webapp;

import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.amol.base.yyy.model.YyyEvent;
import com.systop.amol.base.yyy.service.StuImportXlsImportHelperFactory;
import com.systop.amol.base.yyy.service.YyyAwardManager;
import com.systop.amol.base.yyy.service.YyyManager;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.util.XlsImportHelper;
import com.systop.core.webapp.struts2.action.JsonCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class YyyEventAction extends
JsonCrudAction<YyyEvent,YyyManager>{
	@Autowired
	private YyyManager yyyManager;
	private String dataFileName;
	private String errorInfo;

	@Autowired
	private ProductSortManager productSortManager;
	/**
	 * 导入数据文件
	 */
	private File data;
	@Autowired
	private UserManager userManager;
	@Autowired
	private YyyAwardManager yyyAwardManager;
	private Integer regionId;
	private String regionNameCun;
	@Autowired
	private RegionManager regionManager;
	private File attch;
	private String attchFolder = "/uploadFiles/fileAttch/";
	private String attchFileName;
	/** 地区名称 */
	private String regionName;
	public String index() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from YyyEvent c where 1=1 ");
		List args = new ArrayList();
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		if(null!=user&&!user.getType().equals("admin")) {
			sql.append(" and c.createUser.id = ? ");
			args.add(user.getId());
		}
		if(null!=getModel().getCreateUser()) {
			if (StringUtils.isNotBlank(getModel().getCreateUser().getLoginId())) {
				sql.append(" and c.createUser.loginId = ? ");
				args.add(getModel().getCreateUser().getLoginId());
			}
		}
		if(StringUtils.isNotBlank(getModel().getStuName())){
			sql.append(" and c.stuName like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getStuName()));
		}
		if(null!=user.getRegion()){
			Region region = regionManager.get(Integer.valueOf(user.getRegion().getId()));
    	sql.append(" and c.region.code like ?");
    	args.add(MatchMode.START.toMatchString(region.getCode()));
    	regionName = region.getName();
		}
		 if(null == regionId || 0 == regionId){
	    	if(null != user.getRegion()){
	    		sql.append(" and c.region.code like ?");
	    	  args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
	    	  regionNameCun = user.getRegion().getName();
	    	}
	    }else{
	    	Region region = regionManager.get(Integer.valueOf(regionId));
	    	sql.append(" and c.region.code like ?");
	    	args.add(MatchMode.START.toMatchString(region.getCode()));
	    	regionNameCun = region.getName();
	    	
	    }
	  	/*	if (getModel().getCode() != null
				&& !getModel().getCode().trim().equals("")) {
			sql.append(" and c.code like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCode()));
		}
		if (getModel().getSupplier() != null
				&& getModel().getSupplier().getName() != null 
				&&  !getModel().getSupplier().getName().equals("")) {
			sql.append(" and c.supplier.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSupplier().getName()));
		}
*/
		sql.append(" order by c.createTime desc");
		getRequest().setAttribute("logonUser", user);
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}
	public String importEdit(){

		return "importEdit";
	}

	/**
	 * 从Excel中导入学员信息到数据库
	 */
	public String importData() {
		User user = UserUtil.getPrincipal(getRequest());

		// 判断上传文件是否存在
		if (StringUtils.isBlank(dataFileName)) {
			errorInfo="请选择要导入的文件!";
			return "importEdit";
		}
		if (StringUtils.isNotBlank(dataFileName)) {
			// 判断后缀是否是Xls文件
			if (!XlsImportHelper.isAllowedXls(dataFileName)) {
				errorInfo="只能上传以”.xls“为后缀的文件!";
				return "importEdit";
			}
		}
		XlsImportHelper xih = StuImportXlsImportHelperFactory.create();
		System.out.println("xih  --------------->>>  "+xih);
		// 读取数据存放在此list中
		List<String> list = new ArrayList<String>();
		InputStream is = null;
		Workbook rwb = null;
		Sheet rs = null;
		try {
			List<YyyEvent> yyyList = new ArrayList<YyyEvent>();
			is = new FileInputStream(data);
			System.out.println("is  --------------->>>  "+is);
			rwb = Workbook.getWorkbook(is);
			System.out.println("rwb  --------------->>>  "+rwb);
			// 获取第一张Sheet表
			rs = rwb.getSheet(0);
			System.out.println("rs rows  --------------->>>  "+rs.getRows());

			//col排  row列
			YyyEvent yyyEvent = null;
			for (int i = 1; i < rs.getRows(); i++) {
				attchFolder="/uploadFiles/fileAttch/";
				yyyEvent = new YyyEvent();
				Map<String, String> map = new HashMap<String, String>();
				// 得到导入数据map
				xih.importCommonProperties(map, rs, i);
				/**
				 * properties.put("stuName", new StringConverter(0)); //学员姓名
				 properties.put("stuSex", new StringConverter(1)); //学员性别
				 properties.put("school", new StringConverter(2)); //学校
				 properties.put("xbzy", new StringConverter(3)); //系、部、专业
				 properties.put("domNum", new StringConverter(4)); //宿舍号
				 properties.put("stuQq", new StringConverter(5)); //QQ
				 properties.put("wx", new StringConverter(6)); //微信
				 properties.put("phone", new StringConverter(7)); //手机号
				 properties.put("jg", new StringConverter(8)); //籍贯
				 properties.put("bzr", new StringConverter(9)); //班主任
				 properties.put("relatePhone", new StringConverter(10)); //电话
				 properties.put("drzw", new StringConverter(11)); //担任职务
				 properties.put("desn", new StringConverter(12)); //担任职务上
				 */
				String stuName=map.get("stuName");
				yyyEvent.setStuName(stuName);
				yyyEvent.setCreateUser(user);
				String stuSex=map.get("stuSex");
				yyyEvent.setStuSex(stuSex);
				String school = map.get("school");
				ProductSort productSort = productSortManager.get(Integer.parseInt(school));
				yyyEvent.setSchool(productSort);
				String xbzy=map.get("xbzy");
				ProductSort xbzyPro = productSortManager.get(Integer.parseInt(xbzy));
				yyyEvent.setXbzy(xbzyPro);
				String domNum=map.get("domNum");
				yyyEvent.setDomNum(domNum);
				String stuQq=map.get("stuQq");
				yyyEvent.setStuQq(stuQq);
				String wx=map.get("wx");
				yyyEvent.setWxNo(wx);
				String phone=map.get("phone");
				yyyEvent.setStuPhone(phone);
				String jg=map.get("jg");
				Region region = regionManager.get(Integer.parseInt(jg));
				yyyEvent.setRegion(region);
				String bzr=map.get("bzr");
				yyyEvent.setBzrName(bzr);
				String relatePhone=map.get("relatePhone");
				yyyEvent.setRelatePhone(relatePhone);
				String drzw=map.get("drzw");
				yyyEvent.setTrzw(drzw);
				yyyEvent.setCreateTime(new Date());
				System.out.println("--------------------------------------------"+i);
				int j = i + 1 ;
				// 年月为空不进行导入
				if (StringUtils.isBlank(stuName)) {
					this.addActionMessage("Excel表中第【" + j + "】行学员姓名为空，不做导入处理!");
					continue;
				}
				if (StringUtils.isBlank(stuSex)) {
					this.addActionMessage("Excel表中第【" + j + "】学员性别为空，不做导入处理!");
					continue;
				}
				if (StringUtils.isBlank(phone)) {
					this.addActionMessage("Excel表中第【" + j + "】学员电话为空，不做导入处理!");
					continue;
				}
				List<YyyEvent> plist = getManager().getListByPhone(phone);
				if(null != plist && plist.size()>0){
					this.addActionMessage("Excel表中第【" + j + "】学员电话已经存在，不能重复录入!");
					continue;
				}
				yyyList.add(yyyEvent);
			}
			System.out.println("---------------------"+yyyList.size());
			getManager().plSaveStuInfos(yyyList);
			rs=null;
			rwb=null;
			is.close();
		} catch (Exception e) {
			this.addActionMessage(e.getMessage());
			return "importEdit";
		}finally {
			if (rwb != null) {
				rwb.close();
			}
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
		}
		if (list.size() > 0) {
			//errorInfo.addAll(list);

		}else{
			this.addActionMessage("导入成功！");
		}
		//List<PayInit> payList =payInitManager.query("from PayInit where user.id=?", getLoginUser().getId());
		//if(payList.size()>0){
		// this.getRequest().setAttribute("list", payList);

		//}
		return SUCCESS;
	}
	@Override
	public String save() {
		try {
			if(null==getModel().getId()){
				User user = UserUtil.getPrincipal(getRequest());
				/*if(null!=user.getType()&&user.getType().equals("agent")){
					getModel().setRegion(user.getRegion());
				}*/
				User uu = userManager.get(user.getId());
				if (attch != null) {
					String filePath = UpLoadUtil.doUpload(attch,
							attchFileName, attchFolder, getServletContext());
					getModel().setImageURL(filePath);
				}
				attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
				getModel().setCreateTime(new Date());
				getModel().setCreateUser(uu);
				getManager().save(getModel());
			}
			else{
				getManager().getDao().getHibernateTemplate().clear();
				if (attch != null) {
					String filePath = UpLoadUtil.doUpload(attch,
							attchFileName, attchFolder, getServletContext());
					getModel().setImageURL(filePath);
				}
				attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
				getManager().update(getModel());
			}
			return SUCCESS;
		} catch (Exception e) {
			addActionError(e.getMessage());
			return INPUT;
		}
	}
	public String ssYyy(){
		if(null!=getModel().getId()){
			YyyEvent event=getManager().get(getModel().getId());
			getManager().update(event);
		}
		return SUCCESS;
	}
	public String remove(){
		if(null!=getModel().getId()){
			getManager().remove(getModel());
		}
		return SUCCESS;
	}
	public String edit(){
		return super.edit();
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
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
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public String getRegionNameCun() {
		return regionNameCun;
	}
	public void setRegionNameCun(String regionNameCun) {
		this.regionNameCun = regionNameCun;
	}

	public YyyManager getYyyManager() {
		return yyyManager;
	}

	public void setYyyManager(YyyManager yyyManager) {
		this.yyyManager = yyyManager;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public File getData() {
		return data;
	}

	public void setData(File data) {
		this.data = data;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public YyyAwardManager getYyyAwardManager() {
		return yyyAwardManager;
	}

	public void setYyyAwardManager(YyyAwardManager yyyAwardManager) {
		this.yyyAwardManager = yyyAwardManager;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public RegionManager getRegionManager() {
		return regionManager;
	}

	public void setRegionManager(RegionManager regionManager) {
		this.regionManager = regionManager;
	}
}
