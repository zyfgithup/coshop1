package com.systop.common.modules.region.phoneapp;

import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.core.webapp.struts2.action.JsonCrudAction;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"serial", "unchecked", "rawtypes"})
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneRegionAction extends JsonCrudAction<Region, RegionManager> {
	
	//异步查询返回值
	private Region region;
	
	//AJAX调用返回变量(JSON)
	private List<Map<String, Object>> regions;

	private List<Region> listRegions;
	
	public static final List REGIONS = new ArrayList();

	private List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();

	private JdbcTemplate jdbcTemplate;

	/**
	 * 获得所有的省，市 县
	 */
	public String getAllCity(){
		String parentId = getRequest().getParameter("parentId");
		System.out.println("您传过来的parentId是"+parentId);
		if(null!=parentId && !"null".equals(parentId) && !"".equals(parentId)){
			listRegions = getManager().getByParentId(Integer.parseInt(parentId));
		}else{
			listRegions = getManager().getByParentId(null);
		}
		return SUCCESS;
	}

	public String getByRegionName(){
		String name = getRequest().getParameter("name");
		System.out.println("---------------------name"+name);
		String sql = "select id,name from regions where name like '%"+name+"%'";
		regions = jdbcTemplate.queryForList(sql);
		return "tree";
	}
	/**
	 * 构建地区
	 * @return
	 */
	public String regionTree(){
			List<Region> tops = getManager().getRegionsByParnetId(null);
			regions =  new ArrayList<Map<String, Object>>();
			for(Region region : tops){
				Map<String, Object> top = toMap(region);
				top = buildTreeByParent(top, true);
				regions.add(top);
			}
			REGIONS.addAll(regions);

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
		}
		return map;
	}

	public List<Map<String, Object>> getRegions() {
		return regions;
	}

	public Region getRegion() {
		return region;
	}

	public List<Region> getListRegions() {
		return listRegions;
	}

	public void setListRegions(List<Region> listRegions) {
		this.listRegions = listRegions;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
