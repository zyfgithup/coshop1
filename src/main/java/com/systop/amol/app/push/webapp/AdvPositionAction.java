package com.systop.amol.app.push.webapp;

import com.systop.amol.app.push.model.AdvPosition;
import com.systop.amol.app.push.service.AdvPositionManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/27.
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AdvPositionAction extends
        DefaultCrudAction<AdvPosition,AdvPositionManager> {

    public String index(){
        page = PageUtil.getPage(getPageNo(), 15);
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        StringBuffer hql = new StringBuffer("from AdvPosition o where 1=1 ");
        List args = new ArrayList();
        if (StringUtils.isNotBlank(getModel().getName())) {
            hql.append(" and o.name like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
        }
       /* String userId = getRequest().getParameter("userId");
        if(StringUtils.isNotBlank(userId)){
            hql.append(" and o.picOfUser.id=? ");
            args.add(Integer.parseInt(userId));
        }*/
        page = getManager().pageQuery(page, hql.toString(), args.toArray());
        restorePageData(page);
        return INDEX;
    }
    public String save(){
        if(null==getModel().getId()) {
            getModel().setCreateTime(new Date());
            getManager().save(getModel());
        }else{
            getManager().update(getModel());
        }
        return SUCCESS;
    }
    @Override
    public String edit() {
        return super.edit();
    }
    @Override
    public String remove(){
        getManager().get(getModel().getId());
        getManager().remove(getModel());
        return SUCCESS;
    }
}
