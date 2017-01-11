package com.systop.amol.user.agent.webapp;

import com.systop.amol.user.agent.model.DistanceSet;
import com.systop.amol.user.agent.service.DistanceManager;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
@SuppressWarnings({ "serial", "unused", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DistanceAction extends DefaultCrudAction<DistanceSet,DistanceManager> {

    @Autowired
    private UserManager userManager;
    private String flag;
    @Transactional
    public String save() {
        User user = UserUtil.getPrincipal(getRequest());
        user=userManager.get(user.getId());
        getModel().setUser(user);
        getModel().setCreateTime(new Date());
        System.out.println("------------------getModel().getId()="+getModel().getId());
        if(null==getModel().getId()){
            getManager().save(getModel());
        }else{
            getManager().update(getModel());
        }
        return SUCCESS;
    }
    public String delete() {
        getManager().remove(getManager().get(getModel().getId()));
        return SUCCESS;
    }
    public String index() {
        User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());// 得到当前登录用户
        Assert.assertNotNull("当前登录用户为空", user);
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("from DistanceSet u where 1=1  ");
        hql.append(" order by u.createTime desc");
        System.out.println("hql = "+hql);
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        page = getManager().pageQuery(page, hql.toString(), args.toArray());
        List list = page.getData();
        if(list.size()==0){
            flag = "yes";
        }else{
            flag = "no";
        }
        restorePageData(page);
        return INDEX;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
