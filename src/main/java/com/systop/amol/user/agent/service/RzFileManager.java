package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.RzFile;
import com.systop.core.service.BaseGenericsManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
@Service
public class RzFileManager extends BaseGenericsManager<RzFile> {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<RzFile> getRenzhengFiles(Integer rzId){
        return this.query("from RzFile u where u.renZheng.id =?",new Object[]{rzId});
    }
    public List<RzFile> getCheliangFiles(Integer clId){
        return this.query("from RzFile u where u.cheLiang.id =?",new Object[]{clId});
    }
    public List<RzFile> getFilesByTbId(Integer tbid){
        return this.query("from RzFile u where u.touBaoRen.id =?",new Object[]{tbid});
    }
    public void deleteRzFile(String rzId,String noUpdateIds){
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
                sql = " delete from rz_file where rz_id = "+Integer.parseInt(rzId)+" and id not in  "+param;
            }else{
                sql = " delete from rz_file where rz_id = "+Integer.parseInt(rzId) ;
            }
            System.out.println("------------------------------sql="+sql);
            jdbcTemplate.execute(sql);
    }
}
