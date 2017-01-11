package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.ZhuanZhangRecord;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class ZhuanZhangRecordManager extends BaseGenericsManager<ZhuanZhangRecord> {

    public List<ZhuanZhangRecord> getZZList(Integer salesId){
        String hql = " from ZhuanZhangRecord WHERE sales.id="+salesId;
        return this.query(hql);
    }



}
