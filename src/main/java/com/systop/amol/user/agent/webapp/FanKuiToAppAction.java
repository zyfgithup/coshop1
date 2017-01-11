package com.systop.amol.user.agent.webapp;

import com.systop.amol.user.agent.model.FanKuiToApp;
import com.systop.amol.user.agent.service.FanKuiToAppManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import junit.framework.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/15.
 */
@SuppressWarnings({ "serial", "unused", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FanKuiToAppAction extends DefaultCrudAction<FanKuiToApp,FanKuiToAppManager> {
    @Autowired
    private UserManager userManager;

    public String index() {
        User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());// 得到当前登录用户
        Assert.assertNotNull("当前登录用户为空", user);
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("from FanKuiToApp u where 1=1  ");
      /*  if(StringUtils.isNotBlank(regionId)){
            Region region = regionManager.get(Integer.parseInt(regionId));
            hql.append(" and u.region.code like ? ");
            args.add(MatchMode.START.toMatchString(region.getCode()));
        }*/
      /*  if(null!=getModel().getUser()&&null!=getModel().getUser().getLoginId()){
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
        }*/
        hql.append(" order by u.createTime desc");
        System.out.println("hql = "+hql);
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        page = getManager().pageQuery(page, hql.toString(), args.toArray());
        restorePageData(page);
        return INDEX;
    }
    public String remove(){
        FanKuiToApp fk = getManager().get(getModel().getId());
        getManager().remove(fk);
        return SUCCESS;
    }
}
