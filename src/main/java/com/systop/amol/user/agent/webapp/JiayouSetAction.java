package com.systop.amol.user.agent.webapp;

import com.systop.amol.user.agent.model.JiayouSet;
import com.systop.amol.user.agent.service.JiayouSetManager;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import junit.framework.Assert;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/5.
 */
@SuppressWarnings({ "serial", "unused", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JiayouSetAction extends DefaultCrudAction<JiayouSet,JiayouSetManager> {
    @Autowired
    private UserManager userManager;
    @Autowired
    private RegionManager regionManager;
    private Integer regionId;
    private List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
    public String index() {
        User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());// 得到当前登录用户
        Assert.assertNotNull("当前登录用户为空", user);
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("from JiayouSet u where 1=1  ");
        if(null == regionId || 0 == regionId){
            if(null != user.getRegion()){
                hql.append(" and u.region.code like ?");
                args.add(MatchMode.START.toMatchString(user.getRegion().getCode()));
            }
        }else{
            Region region = regionManager.get(Integer.valueOf(regionId));
            hql.append(" and u.region.code like ?");
            args.add(MatchMode.START.toMatchString(region.getCode()));
        }
        hql.append(" order by u.createTime desc");
        System.out.println("hql = "+hql);
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        page = getManager().pageQuery(page, hql.toString(), args.toArray());
        restorePageData(page);
        return INDEX;
    }
    public String getByRegionId(){
        list = getManager().getByRegionId(regionId);
        return "list";
    }
    public String save(){
        Region region = regionManager.get(getModel().getRegion().getId());
        getModel().setRegionName(region.getName());
        System.out.println("--------------------------getModel().getId()="+getModel().getId());
        if(getModel().getId()!=null) {
            getManager().getDao().clear();
            getManager().update(getModel());
        }else{
            getModel().setCreateTime(new Date());
            getManager().save(getModel());
        }
        return SUCCESS;
    }
    public String remove(){
        JiayouSet jiayouSet = getManager().get(getModel().getId());
        getManager().remove(jiayouSet);
        return SUCCESS;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }
}



