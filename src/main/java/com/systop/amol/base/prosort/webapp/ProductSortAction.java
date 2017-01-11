package com.systop.amol.base.prosort.webapp;

import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.amol.base.yyy.service.YyyManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.io.File;
import java.util.*;

/**
 * 商品类型管理Action
 * 
 * @author songbaojie
 */
@SuppressWarnings({ "serial"})
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductSortAction extends
		DefaultCrudAction<ProductSort, ProductSortManager> {
	// 异步查询返回值
	private ProductSort productSort;

	private Map<String, Object> result;

	private String showPos;
	@Autowired
	UserManager userManager;
	@Autowired
	YyyManager yyyManager;
	// AJAX调用返回变量(JSON)
	private List<Map<String, Object>> productSorts;
	private List<ProductSort> list;
	private String type;
	//商品添加获取添加商品类别，1为积分商品添加页面；
	private String jfType;
	private File attch;
	private String attchFileName;
	private Integer tempId;
	private String attchFolder = "/uploadFiles/fileAttch/";
	public String merEdit() {
		String result=null;
		if(getModel().getType().equals("2")||getModel().getType().equals("4")){
			result="merInput";
		}else{
			result="bankEdit";
		}
    return result;
  }

	public String bankEdit() {
	    return "bankEdit";
	  }
  public String mersave() {
	  String result=null;
		try {
			User u = UserUtil.getPrincipal(getRequest());
			String type = getRequest().getParameter("type");
			getModel().setType(type);
			if(null!=getModel().getType()){
				System.out.println("------------------getModel().getType(="+getModel().getType());
				if("2".equals(getModel().getType())||"4".equals(getModel().getType())){
					result="mersave";
				}else{
					result="banksave";
				}
			}
			if(getModel().getStatus().equals("0")){
				getModel().setType("4");
			}
			if (u != null) {
				getModel().setCreator(u);
			} else {
				throw new ApplicationException("该用户未正确登录,请重新登录后再次录入！");
			}
			if (getModel().getId() == null) {// 添加,需要赋值上级类型
				if (getModel().getParentProsort()!=null 
						&& getModel().getParentProsort().getId() != null) {
					ProductSort parent = getManager().get(getModel().getParentProsort().getId());
					getModel().setParentProsort(parent);
				}else{
					getModel().setParentProsort(null);
				}
			}else{
				getModel().setCreateTime(new Date());// 创建日期
			}
			attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
			if (attch != null) {
				String filePath = UpLoadUtil.doUpload(attch,
						attchFileName, attchFolder, getServletContext());
				getModel().setImageURL(filePath);
			}
			// 从model中得到对象
			if (StringUtils.isNotBlank(getModel().getName()) && getManager().isProductSortByName(getModel())) {// 判断商品名称是否重复录入
				this.addActionError("类型名称重复,请重新录入");
				return "merInput";
			} else {
				getModel().setCreateTime(new Date());
				super.save();
				return result;
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			this.addActionError("添加失败！请与管理员联系！");
			return result;
		}
	}
	public String KindsSave(){
		User u = UserUtil.getPrincipal(getRequest());
		String type = getRequest().getParameter("type");
		String pId = getRequest().getParameter("pId");
		String price = getRequest().getParameter("price");
		ProductSort ps = new ProductSort();
		ps.setCreateTime(new Date());
		ps.setType(type);
		ps.setPrice(Double.valueOf(price));
		ps.setParentProsort(getManager().get(Integer.parseInt(pId)));
		if (u != null) {
			ps.setCreator(u);
		} else {
			throw new ApplicationException("该用户未正确登录,请重新登录后再次录入！");
		}
		String proName = getRequest().getParameter("proName");
		ps.setName(proName);
		getManager().save(ps);
		return "mersave";
	}
	public String fillKinds(){
		ProductSort ps = getManager().get(getModel().getId());
		getRequest().setAttribute("ps",ps);
		return "fillKinds";
	}
	/**
	 * 保存数据，异步调用
	 */
	public String save() {
		try {
			User u = UserUtil.getPrincipal(getRequest());
			if (u != null) {
				getModel().setCreator(u);
			} else {
				throw new ApplicationException("该用户未正确登录,请重新登录后再次录入！");
			}
			if (getModel().getId() == null) {// 添加,需要赋值上级类型
				if (getModel().getParentProsort()!=null 
						&& getModel().getParentProsort().getId() != null) {
					ProductSort parent = getManager().get(getModel().getParentProsort().getId());
					getModel().setParentProsort(parent);
				}else{
					getModel().setParentProsort(null);
				}
			}else{
				getModel().setCreateTime(new Date());// 创建日期
			}
			/*if(!"".equals(showPos)){
				if(showPos.equals("1")){
					getModel().setShowJfsc("show");
				}
				if(showPos.equals("0")){
					getModel().setShowTjg("show");
				}
			}*/
			// 从model中得到对象
			if (StringUtils.isNotBlank(getModel().getName()) && getManager().isProductSortByName(getModel())) {// 判断商品名称是否重复录入
				this.addActionError("系、部、专业重复,请重新录入");
				return INPUT;
			} else {
				getModel().setCreateTime(new Date());
				super.save();
				return INDEX;
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			this.addActionError("添加失败！请与管理员联系！");
			return INPUT;
		}
	}
	/**
	 * 保存数据，异步调用
	 */
	public String asynchronousSave() {
		result = new HashMap<String, Object>();
		try {
			User u = UserUtil.getPrincipal(getRequest());
			if (u != null) {
				getModel().setCreator(u);
			} else {
				throw new ApplicationException("该用户未正确登录,请重新登录后再次录入！");
			}
			if (getModel().getId() == null) {// 添加,需要赋值上级类型
				String parentId = getRequest().getParameter("parentId");
				if (StringUtils.isNumeric(parentId)) {
					ProductSort parent = getManager().get(
							Integer.valueOf(parentId));
					getModel().setParentProsort(parent);
				}
				getModel().setCreateTime(new Date());// 创建日期
			}
			productSort = getModel();// 从model中得到对象
			/*getModel().setShowTjg(getRequest().getParameter("showTjgSelect"));
			getModel().setShowJfsc( getRequest().getParameter("showJfscSelect"));*/
			productSort.setType(type);
			if (getManager().isProductSortByName(productSort)) {// 判断商品名称是否重复录入
				result.put("info", "商品类型名称重复请重新录入");
			} else {
				getManager().save(productSort);
			}
			result.put("success", true);
			result.put("id", productSort.getId());
			result.put("text", productSort.getName());
			result.put("descn", productSort.getDescn());
			result.put("status", productSort.getStatus());
		/*	result.put("showJfsc", getRequest().getParameter("showJfscSelect"));
			result.put("showTjg", getRequest().getParameter("showTjgSelect"));*/
		} catch (ApplicationException e) {
			result.put("failure", true);
			result.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return "complete";
	}
	public String removeKinds(){
		getManager().remove(getManager().get(getModel().getId()));
		tempId = getModel().getParentProsort().getId();
		return "mersave";
	}
	/**
	 * 删除数据，异步调用
	 */
	public String removeMerSort() {
		result = new HashMap<String,Object>();
		User u = UserUtil.getPrincipal(getRequest());
		if (u != null) {
			if (getModel() != null && getModel().getId() != null) {
				getManager().remove(getModel());
			}
		}
		return "mersave";
	}

	/**
	 * 删除数据，异步调用
	 */
	@Override
	public String remove() {
		result = new HashMap<String,Object>();
		User u = UserUtil.getPrincipal(getRequest());
		if (u != null) {
			if (getModel() != null && getModel().getId() != null) {
				// 或得此类型下的所有商品
				List list = yyyManager.getYyyListByProSortId(
						getModel().getId());
				// 判断此类型下是否有商品存在如果有不可删除
				if (list != null && list.size() > 0) {
					result.put("success", false);
					result.put("msg", "该类型下已经存在关联学员,不可删除!");
				} else {
					getManager().remove(getModel());
					result.put("success", true);
					result.put("msg", "删除成功!");
				}
			} else {
				result.put("success", false);
				result.put("msg", "删除的对象已不存在");
			}
		}
		return "complete";
	}
	public String getByParentId(){
		list = new ArrayList<ProductSort>();
		Integer parentId = Integer.parseInt(getRequest().getParameter("parentId"));
		list = getManager().getProsortsByProsortId(parentId);
		return "newTree";
	}
	/**
	 * 构建商品类型
	 * @return
	 */
	public String prosortTree() {
		List<ProductSort> tops = null;
		String status = getRequest().getParameter("status");// (String)getRequest().getAttribute("status");

		User u = UserUtil.getPrincipal(getRequest());
		Assert.notNull(u, "用户不允许为空");
		
		// 如果STATUS为1则只得到可用状态商品类型
		tops = getManager().getProductSortsByParnetId(null, status, u,type);
		System.out.println("---------------"+type);
	
		productSorts = new ArrayList<Map<String, Object>>();
		for (ProductSort prosort : tops) {
			Map<String, Object> top = toMap(prosort);
			top = buildTreeByParent(top, true, status, u);
			productSorts.add(top);
		}
		return "tree";
	}

	/**
	 * 根据父ID构建JSON数据
	 * 
	 * @param parent
	 * @param nested
	 *            是否嵌套
	 * @param status
	 *            状态类型：1-可用,0-禁用
	 * @param u
	 *            当前登录用户
	 * @return
	 */
	private Map<String, Object> buildTreeByParent(Map<String, Object> parent,
			boolean nested, String status, User u) {
		if (parent == null || parent.get("id") == null) {
			return null;
		}
		List<ProductSort> subs = getManager().getProductSortsByParnetId(
				(Integer) parent.get("id"), status, u,type);
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		for (ProductSort sub : subs) {
			Map<String, Object> child = toMap(sub);
			if (nested) {
				child = buildTreeByParent(child, nested, status, u);
			}
			children.add(child);
		}
		if (!children.isEmpty()) {
			parent.put("children", children);
			parent.put("leaf", false);
		} else {
			parent.put("leaf", true);
		}
		return parent;
	}
	public String findKinds() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from ProductSort c where c.parentProsort.id=?  ");
		List args = new ArrayList();
		System.out.println("------------------"+tempId+"=-------"+getModel().getId());
		if(null!=tempId&&0!=tempId){
			args.add(tempId);
		}else{
			args.add(getModel().getId());
		}
		String name = getRequest().getParameter("name");
		if(StringUtils.isNotBlank(name)){
			sql.append(" and c.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(name));
		}
		sql.append(" order by c.rowNum");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return "kindsIndex";
	}
	public String merTableIndex() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from ProductSort c where (c.type='2' or c.type='4') and c.parentProsort.id is null and c.name!='加油车' and c.name!='加油站'  ");
		List args = new ArrayList();
		if (getModel().getName() != null
				&& !getModel().getName().trim().equals("")) {
			sql.append(" and c.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		sql.append(" order by c.rowNum");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		List<ProductSort> pList = page.getData();
		List tempList = null;
		for (ProductSort ps : pList){
			tempList = new ArrayList();
			tempList = getManager().getUsersByProSort(ps.getId()+"");
			if(null != tempList && tempList.size()>0){
				ps.setIsYbl("1");
			}
		}
		restorePageData(page);
		return "merTableIndex";
	}
	public String banksTableIndex() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from ProductSort c where c.type='3' ");
		List args = new ArrayList();
		if (getModel().getName() != null
				&& !getModel().getName().trim().equals("")) {
			sql.append(" and c.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		sql.append(" order by c.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return "banksTableIndex";
	}
	/**
	 * 将商品类型中的部分内容存储到MAP中
	 * 
	 * @param p
	 * @return
	 */
	private Map<String, Object> toMap(ProductSort p) {
		Map<String, Object> map = null;
		if (p != null) {
			map = new HashMap<String, Object>();
			map.put("id", p.getId());
			map.put("text", p.getName());
			map.put("showJfsc", p.getShowJfsc());//积分app客户端显示标示
			map.put("showTjg", p.getShowTjg());//特价购app客户端显示标示
			map.put("descn", p.getDescn());
			map.put("status", p.getStatus());
			if (p.getStatus() != null && p.getStatus().equals("0")) {
				map.put("cls", "isStatusNo");
			}
			// map.put("iconCls", "x-tree-node-icon");
		}
		return map;
	}

	public ProductSort getProductSort() {
		return productSort;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public List<Map<String, Object>> getProductSorts() {
		return productSorts;
	}

	public void setType(String type) {
		this.type = type;
	}
	public UserManager getUserManager() {
		return userManager;
	}
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
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
	public String getAttchFolder() {
		return attchFolder;
	}
	public void setAttchFolder(String attchFolder) {
		this.attchFolder = attchFolder;
	}
	public String getType() {
		return type;
	}
	public void setProductSort(ProductSort productSort) {
		this.productSort = productSort;
	}
	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
	public void setProductSorts(List<Map<String, Object>> productSorts) {
		this.productSorts = productSorts;
	}
	public String getJfType() {
		return jfType;
	}
	public void setJfType(String jfType) {
		this.jfType = jfType;
	}

	public String getShowPos() {
		return showPos;
	}

	public void setShowPos(String showPos) {
		this.showPos = showPos;
	}

	public Integer getTempId() {
		return tempId;
	}

	public void setTempId(Integer tempId) {
		this.tempId = tempId;
	}

	public List<ProductSort> getList() {
		return list;
	}

	public void setList(List<ProductSort> list) {
		this.list = list;
	}
}