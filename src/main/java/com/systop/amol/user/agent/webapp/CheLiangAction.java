package com.systop.amol.user.agent.webapp;

import com.systop.amol.user.agent.model.CheLiang;
import com.systop.amol.user.agent.model.RzFile;
import com.systop.amol.user.agent.service.CheLiangManager;
import com.systop.amol.user.agent.service.RzFileManager;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import junit.framework.Assert;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
@SuppressWarnings({ "serial", "unused", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CheLiangAction extends DefaultCrudAction<CheLiang,CheLiangManager> {
    @Autowired
    private UserManager userManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private RzFileManager rzFileManager;
    private String regionId;
    private CheLiang cheLiang;
    private List<RzFile> rzList = new ArrayList<RzFile>();
    public String index() {
        User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());// 得到当前登录用户
        Assert.assertNotNull("当前登录用户为空", user);
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("from CheLiang u where 1=1  ");
        if(StringUtils.isNotBlank(regionId)){
            Region region = regionManager.get(Integer.parseInt(regionId));
            hql.append(" and u.region.code like ? ");
            args.add(MatchMode.START.toMatchString(region.getCode()));
        }
        if(null!=getModel().getUser()&&null!=getModel().getUser().getLoginId()){
                hql.append(" and u.user.loginId like ? ");
                args.add(MatchMode.ANYWHERE.toMatchString(getModel().getUser().getLoginId()));
        }
        if(StringUtils.isNotBlank(getModel().getVisible())){
            if("0".equals(getModel().getVisible())){
                hql.append(" and (u.visible = '0' or u.visible is null) ");
            }else {
                hql.append(" and u.visible = ? ");
                args.add(getModel().getVisible());
            }
        }
        hql.append(" order by u.createTime desc");
        System.out.println("hql = "+hql);
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        page = getManager().pageQuery(page, hql.toString(), args.toArray());
        restorePageData(page);
        return INDEX;
    }
    public String lookFiles(){
        rzList = rzFileManager.getCheliangFiles(getModel().getId());
        cheLiang = getManager().get(getModel().getId());
        return "lookFiles";
    }
    public String renzheng(){
        String type = getRequest().getParameter("type");
        String idea = getRequest().getParameter("idea");
        String clId = getRequest().getParameter("clId");
        if(StringUtils.isNotBlank(clId)){
            CheLiang cl = getManager().get(Integer.parseInt(clId));
            if(null!=type && type.equals("error")){
                cl.setVisible("-1");
            }
            if(null!=type && type.equals("success")){
                cl.setVisible("1");
            }
            cl.setIdea(idea);
            getManager().update(cl);
        }
        return "success";

    }
    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public List<RzFile> getRzList() {
        return rzList;
    }

    public void setRzList(List<RzFile> rzList) {
        this.rzList = rzList;
    }

    public CheLiang getCheLiang() {
        return cheLiang;
    }

    public void setCheLiang(CheLiang cheLiang) {
        this.cheLiang = cheLiang;
    }
}
