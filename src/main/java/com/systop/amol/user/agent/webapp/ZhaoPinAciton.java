package com.systop.amol.user.agent.webapp;

import com.systop.amol.user.agent.model.ZhaoPin;
import com.systop.amol.user.agent.service.ZhaoPinManager;
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
public class ZhaoPinAciton extends DefaultCrudAction<ZhaoPin,ZhaoPinManager> {

    @Autowired
    private UserManager userManager;
    private ZhaoPin zhaoPin;
    public String index() {
        User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());// 得到当前登录用户
        Assert.assertNotNull("当前登录用户为空", user);
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("from ZhaoPin u where 1=1  ");
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
        zhaoPin = getManager().get(getModel().getId());
        return "lookFiles";
    }
    public String renzheng(){
        String type = getRequest().getParameter("type");
        String idea = getRequest().getParameter("idea");
        String zpId = getRequest().getParameter("zpId");
        if(StringUtils.isNotBlank(zpId)){
            ZhaoPin zp = getManager().get(Integer.parseInt(zpId));
            if(null!=type && type.equals("error")){
                zp.setVisible("-1");
            }
            if(null!=type && type.equals("success")){
                zp.setVisible("1");
            }
            zp.setIdea(idea);
            getManager().update(zp);
        }
        return "success";

    }
    public String unsealUser(){
        ZhaoPin zp = getManager().get(getModel().getId());
        zp.setVisible("-1");
        getManager().update(zp);
        return SUCCESS;
    }
    public String remove(){
        ZhaoPin zp = getManager().get(getModel().getId());
        zp.setVisible("1");
        getManager().update(zp);
        return SUCCESS;
    }

    public ZhaoPin getZhaoPin() {
        return zhaoPin;
    }

    public void setZhaoPin(ZhaoPin zhaoPin) {
        this.zhaoPin = zhaoPin;
    }
}
