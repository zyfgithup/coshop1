package com.systop.common.modules.region.service;

import com.systop.common.modules.region.model.Region;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionManager extends BaseGenericsManager<Region> {

	public List<Region> getByParentId(Integer parentId) {
		List<Region> regions = null;
		String hql = null;
		if (parentId == null) {
			hql = "from Region r where r.parent.id is null order by sortId";
			regions = query(hql);
		} else {
			hql = "from Region r where r.parent.id = ? order by sortId";
			regions = query(hql, parentId);
		}
		return regions;
	}
	/**
	 * 根据父ID获得地区，如果父ID为Null则获得顶级地区
	 * @param parentId
	 * @return
	 */
	public List<Region> getRegionsByParnetId(Integer parentId) {
		List<Region> regions = null;
		String hql = null;
		if (parentId == null) {
			hql = "from Region r where r.parent is null order by sortId";
			regions = query(hql);
		} else {
			hql = "from Region r where r.parent.id = ? order by sortId";
			regions = query(hql, parentId);
		}
		return regions;
	}
	public Region getObjectById(int id){
		return this.findObject("from Region r where r.id=?", id);
	}
}
