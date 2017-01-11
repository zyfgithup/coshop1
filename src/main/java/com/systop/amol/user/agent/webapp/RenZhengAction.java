package com.systop.amol.user.agent.webapp;

import com.systop.amol.user.agent.model.RenZheng;
import com.systop.amol.user.agent.model.RzFile;
import com.systop.amol.user.agent.service.RenZhengManager;
import com.systop.amol.user.agent.service.RzFileManager;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
@SuppressWarnings({ "serial", "unused", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RenZhengAction extends DefaultCrudAction<RenZheng,RenZhengManager> {

    @Autowired
    private UserManager userManager;
    @Autowired
    private RzFileManager rzFileManager;
    @Transactional
    public String remove(){
        List<RzFile> rzList = rzFileManager.getRenzhengFiles(getModel().getId());
        for(RzFile rf : rzList){
            rzFileManager.remove(rf);
        }
        RenZheng rz = getManager().get(getModel().getId());
        if(null!=rz){
            getManager().remove(rz);
        }
        return SUCCESS;
    }
    public String index() {
        User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());// 得到当前登录用户
        Assert.assertNotNull("当前登录用户为空", user);
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("from RenZheng u where 1=1  ");
		if ((null!=getModel().getUser())&&StringUtils.isNotBlank(getModel().getUser().getName())) {
			hql.append(" and u.user.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getUser().getName()));
		}
        String regoinId = getRequest().getParameter("regionId");
        System.out.println("-----------------您传过来的地区id是"+regoinId);
        if(StringUtils.isNotBlank(regoinId)){
            hql.append(" and u.user.region.id=? ");
            args.add(Integer.parseInt(regoinId));
        }
        if (StringUtils.isNotBlank(getModel().getRzType())) {
            hql.append(" and u.rzType=? ");
            args.add(getModel().getRzType());
        }
        if (StringUtils.isNotBlank(getModel().getIftg())) {
            hql.append(" and u.iftg=? ");
            args.add(getModel().getIftg());
        }
        hql.append(" order by u.createTime desc");
        System.out.println("hql = "+hql);
        Page page = PageUtil.getPage(getPageNo(), getPageSize());
        page = getManager().pageQuery(page, hql.toString(), args.toArray());
        restorePageData(page);
        return INDEX;
    }
    public String lookForFile(){
        RenZheng rz = getManager().get(getModel().getId());
        List<RzFile> rzFiles = rzFileManager.getRenzhengFiles(rz.getId());
        List<RzFile> list1 = new ArrayList<RzFile>();
        List<RzFile> list2 = new ArrayList<RzFile>();
        List<RzFile> list3 = new ArrayList<RzFile>();
        List<RzFile> list4 = new ArrayList<RzFile>();
        List<RzFile> list5 = new ArrayList<RzFile>();
        List<RzFile> list6 = new ArrayList<RzFile>();
        for (RzFile tempRz : rzFiles){
            if(tempRz.getFileType().equals("1")){
                list1.add(tempRz);
            }
            if(tempRz.getFileType().equals("2")){
                list2.add(tempRz);
            }
            if(tempRz.getFileType().equals("3")){
                list3.add(tempRz);
            }
            if(tempRz.getFileType().equals("4")){
                list4.add(tempRz);
            }
            if(tempRz.getFileType().equals("5")){
                list5.add(tempRz);
            }
            if(tempRz.getFileType().equals("6")){
                list6.add(tempRz);
            }
        }
        getRequest().setAttribute("one",list1);
        getRequest().setAttribute("two",list2);
        getRequest().setAttribute("three",list3);
        getRequest().setAttribute("four",list4);
        getRequest().setAttribute("five",list5);
        getRequest().setAttribute("six",list6);
        getRequest().setAttribute("rz",rz);
        return "rzPage";
    }
    public String renzheng(){
        String rzIdea = getRequest().getParameter("idea");
        String type = getRequest().getParameter("type");
        String rzId = getRequest().getParameter("rzId");
        System.out.println(rzIdea+"----------------"+rzId+"---------------"+type);
        RenZheng rz = null;
        if(StringUtils.isNotBlank(rzId)){
             rz = getManager().get(Integer.parseInt(rzId));
        }
        String iftg = "";
        if(null!=type && type.equals("success")){
           iftg = "1";
        }else{
            iftg = "-1";
        }
        if(null!=rz){
            rz.setIftg(iftg);
            rz.setShIdea(rzIdea);
            getManager().update(rz);
        }
        return SUCCESS;

    }
}
