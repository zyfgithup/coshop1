package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.RenZheng;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
@Service
public class RenZhengManager extends BaseGenericsManager<RenZheng> {

    public List<RenZheng> getReZhengList(Integer userId){
        return this.query("from RenZheng r where r.user.id=?",new Object[]{userId});
    }
    public RenZheng getReZheng(Integer userId,String rzType){
        return this.findObject("from RenZheng where user.id=? and rzType = ?",new Object[]{userId,rzType});
    }
}
