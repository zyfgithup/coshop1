package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.CheLiang;
import com.systop.core.service.BaseGenericsManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/9/23.
 */
@Service
public class CheLiangManager extends BaseGenericsManager<CheLiang> {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public void deleteUpdateFile(String clId,String noUpdateIds){
        String param ="";
        String sql = "";
        if(StringUtils.isNotBlank(noUpdateIds)){
            String[] idArrays = noUpdateIds.split(",");
            for (String id:idArrays){
                param = param + Integer.parseInt(id)+",";
            }
            if(param.indexOf(",")!=-1){
                param = param.substring(0,param.lastIndexOf(","));
                param = "("+param+")";
            }
            sql = " delete from rz_file where cheliang_id = "+Integer.parseInt(clId)+" and id not in  "+param;
        }else{
            sql = " delete from rz_file where cheliang_id = "+Integer.parseInt(clId) ;
        }
        System.out.println("------------------------------sql="+sql);
        jdbcTemplate.execute(sql);
    }
}
