package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.YhShenqing;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */
@Service
public class YhShenqingManager extends BaseGenericsManager<YhShenqing>{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<YhShenqing> getList(String type,Integer id){

        String hql = "from YhShenqing where 1=1 ";
        if(type.equals("zp")){
            hql += " and zhaoPin.id=?  ";
        }
        if(type.equals("cl")){
            hql += " and cheLiang.id=?  ";
        }
        return this.query(hql,id);
    }
    public List<YhShenqing> getListByUserId(String type,Integer id){

        String hql = "from YhShenqing where user.id=? ";
        if(type.equals("zp")){
            hql += " and zhaoPin.id is not null  ";
        }
        if(type.equals("cl")){
            hql += " and cheLiang.id is not null  ";
        }
        return this.query(hql,id);
    }
}
