package com.systop.amol.base.units.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.base.units.model.Units;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 计量单位管理
 * @author WangHaiYan
 * 
 */
@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class UnitsManager extends BaseGenericsManager<Units> {

  @Transactional
  public void save(Units units) {
    if (getDao().exists(units,  "name", "user.id")) {
      if ( StringUtils.isNotEmpty(units.getName())) {
        throw new ApplicationException("名称为【" + units.getName() + "】的计量单位已存在。");
      }
    }
   
    super.save(units);
  }
  
  /**
   * 获取当前登录用户下的所有计量单位信息
   * @return
   */
	public Map getUnitsMap(Integer userId) {
		List<Units> unitList = query("from Units u ");//query("from Units u where u.user.id = ?", userId);
		Map unitMap = new LinkedHashMap();
		for (Units nt : unitList) {
			unitMap.put(nt.getId(), nt.getName());
		}
		return unitMap;
	}
}
