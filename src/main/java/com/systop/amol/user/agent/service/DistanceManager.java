
package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.DistanceSet;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/11.
 */
@Service
public class DistanceManager extends BaseGenericsManager<DistanceSet> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Double getDistance(){
        String sql = "  select distance from distance_set ";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        if(null!=list && list.size()>0){
        return     Double.valueOf(String.valueOf(list.get(0).get("distance")));
        }
        return 0.0;
    }
}
