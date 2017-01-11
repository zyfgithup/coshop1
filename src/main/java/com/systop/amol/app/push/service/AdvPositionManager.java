package com.systop.amol.app.push.service;

import com.systop.amol.app.push.model.AdvPosition;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/27.
 */
@Service
public class AdvPositionManager  extends BaseGenericsManager<AdvPosition> {


    public List<AdvPosition> getAll(){

       return  getDao().query(" from AdvPosition ");
    }
    public Map getUnitsMap(Integer userId) {
        List<AdvPosition> unitList = query("from AdvPosition u ");//query("from Units u where u.user.id = ?", userId);
        Map unitMap = new LinkedHashMap();
        for (AdvPosition nt : unitList) {
            unitMap.put(nt.getId(), nt.getName());
        }
        return unitMap;
    }
}
