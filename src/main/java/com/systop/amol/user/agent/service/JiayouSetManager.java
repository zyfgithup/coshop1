package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.JiayouSet;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/5.
 */
@Service
public class JiayouSetManager extends BaseGenericsManager<JiayouSet> {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<Map<String,Object>> getByRegionId(Integer regoinId){
        if(null!=regoinId && 0!=regoinId){
            String sql = " select kind_name as name,id,gr_price from jiayou_set where region_id= "+regoinId;
            List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
            return list;
        }
        return null;
    }
}
