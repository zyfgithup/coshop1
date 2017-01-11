package com.systop.common.modules.region.webapp;

import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionCodeManager;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;
import com.systop.core.webapp.struts2.action.JsonCrudAction;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"serial", "unchecked", "rawtypes"})
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegionAction extends JsonCrudAction<Region, RegionManager> {
	
	//异步查询返回值
	private Region region;
	
	private Map<String, Object> result;
	
	
	@Autowired
	private UserManager userManager;
	//AJAX调用返回变量(JSON)
	private List<Map<String, Object>> regions;
	
	public static final List REGIONS = new ArrayList();
	
	@Autowired
	private RegionCodeManager regionCodeManager;

	/**
	 * 重置地区编号
	 * @return
	 */
	public String resetAllCode(){
		regionCodeManager.updateAllSerialNo();
		return null;
	}
	
	/**
	 * 保存数据，异步调用
	 */
	public String save(){
		result = new HashMap<String, Object>();
		try{
			
			//判断要添加的地区同级目录下是否有重名
			boolean isRegion = false;
			
//			if (!getManager().getDao().exists(getModel(), "name")){
					if (getModel().getId() == null){//添加,需要赋值上级地区
						String parentId = getRequest().getParameter("parentId");
						if (StringUtils.isNumeric(parentId)){
							Region parent = getManager().get(Integer.valueOf(parentId));
							
							List<Region> regionList = getManager().query("from Region o where o.parent.id = ? and o.name = ?", Integer.valueOf(parentId), getModel().getName().trim());
							System.out.println(regionList+" = "+regionList.size());
							if(null != regionList && regionList.size() >= 1){
								isRegion = true;
							}
							
							getModel().setParent(parent);
							getModel().setCode(regionCodeManager.getCode(getModel()));
						}
					} 
					region = getModel();
					if(!isRegion){
						getManager().save(region);
					  result.put("success", true);
					  result.put("id", region.getId());
					  result.put("text", region.getName());
					}else{
						result.put("failure", true);
					  result.put("msg", getModel().getName() + "名称存在重复");
					}
					
//			}else{
//				result.put("failure", true);
//				result.put("msg", getModel().getName() + "名称存在重复");
//			}
		} catch(ApplicationException e){
			result.put("failure", true);
			result.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 删除一个地区,同样是异步请求。
	 */
	public String remove(){
		result = new HashMap<String, Object>();
		if (getModel() != null && getModel().getId() != null){
			getManager().remove(getModel());
			result.put("success", true);
		}else{
			result.put("failure", true);
			result.put("msg", "删除的对象已不存在");
		}
		return "remove";
	}
	/**
	 * 构建地区
	 * @return
	 */
	public String regionTree(){
/*		List<Region> tops = getManager().getRegionsByParnetId(null);
		regions =  new ArrayList<Map<String, Object>>();
		for(Region region : tops){
			Map<String, Object> top = toMap(region);
			top = buildTreeByParent(top, true);
			regions.add(top);
		}*/
		
//		if (REGIONS.size() == 0) {
			User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
			String regionIdStr = getRequest().getParameter("regionId");
			Integer regionId = null;
			if(StringUtils.isNotBlank(regionIdStr)&&!"null".equals(regionIdStr)){
				regionId = Integer.valueOf(regionIdStr);
			}
			if(null!=user.getRegion()){
				regionId=user.getRegion().getId();
			}
			List<Region> tops = getManager().getRegionsByParnetId(regionId);
			regions =  new ArrayList<Map<String, Object>>();
			for(Region region : tops){
				Map<String, Object> top = toMap(region);
				top = buildTreeByParent(top, true);
				regions.add(top);
			}
			REGIONS.addAll(regions);
//		}else {
//			regions = REGIONS;
//		}
		return "tree";
	}
	
	/**
	 * 根据父ID构建JSON数据
	 * @param parent 
	 * @param nested 是否嵌套
	 * @return
	 */
	private Map<String, Object> buildTreeByParent(Map<String, Object> parent, boolean nested){
		if (parent == null || parent.get("id") == null) {
			return null;
		}
		List<Region> subs = getManager().getRegionsByParnetId((Integer) parent.get("id"));
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		for(Region sub : subs){
			Map<String, Object> child = toMap(sub);
			if (nested){
				child = buildTreeByParent(child, nested);
			}
			children.add(child);
		}
		if (!children.isEmpty()){
			parent.put("children", children);
			parent.put("leaf", false);
		}else{
			parent.put("leaf", true);
		}
		return parent;
	}
	
	private Map<String, Object> toMap(Region r){
		Map<String, Object> map = null;
		if (r != null){
			map = new HashMap<String, Object>();
			map.put("id", r.getId());
			map.put("text", r.getName());
			map.put("ckPrice", r.getCkPrice());
			map.put("ckName", r.getCkYname());
		}
		return map;
	}

	public List<Map<String, Object>> getRegions() {
		return regions;
	}

	public Region getRegion() {
		return region;
	}

	public Map<String, Object> getResult() {
		return result;
	}
}
