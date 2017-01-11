package com.systop.amol.user.agent.phone;

import com.systop.amol.user.agent.model.YhShenqing;
import com.systop.amol.user.agent.model.ZhaoPin;
import com.systop.amol.user.agent.service.RzFileManager;
import com.systop.amol.user.agent.service.YhShenqingManager;
import com.systop.amol.user.agent.service.ZhaoPinManager;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/23.
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneZhaoPinAction extends DefaultCrudAction<ZhaoPin, ZhaoPinManager> {


    private File[] files;
    private String[] filesFileName;
    private String attchFolder = "/uploadFiles/fileAttch/";
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private YhShenqingManager yhShenqingManager;
    @Autowired
    private RzFileManager rzFileManager;
    @Autowired
    private UserManager userManager;
    private Map<String,Object> map = new HashedMap();
    private String pageNumber = "1";
    private ZhaoPin zp;
    /**
     * 每页显示条数
     */
    private String pageCount = "10";
    private List<ZhaoPin> clList;
    public String updateHitTimes(){
        String zpId = getRequest().getParameter("zpId");
        ZhaoPin zp = null;
        if(StringUtils.isNotBlank(zpId)){
            zp = getManager().get(Integer.parseInt(zpId));
            if(null!=zp.getHitTimes()){
                zp.setHitTimes(zp.getHitTimes()+1);
            }else{
                zp.setHitTimes(1);
            }
            getManager().update(zp);
            map.put("msg",SUCCESS);
        }
        return "map";
    }
    public String getMyZhaoPin(){
        String userId = getRequest().getParameter("userId");
        page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("from ZhaoPin u where 1=1 and u.visible='1' ");
        if(StringUtils.isNotBlank(userId)){
            hql.append(" and  u.user.id=? ");
            args.add(Integer.parseInt(userId));
        }
        String comName = getRequest().getParameter("name");
        if(StringUtils.isNotBlank(comName)){
            hql.append(" and  (u.comName like ?  or u.zwName like ?) ");
            args.add(MatchMode.ANYWHERE.toMatchString(comName));
            args.add(MatchMode.ANYWHERE.toMatchString(comName));
        }
        hql.append(" order by u.createTime desc");
        page = getManager().pageQuery(page, hql.toString(), args.toArray());
        clList = page.getData();
        return "clList";
    }
    public String remove(){
        String zpId = getRequest().getParameter("zpId");
        ZhaoPin zp = getManager().get(Integer.parseInt(zpId));
        List<YhShenqing> sqList = yhShenqingManager.getList("zp",zp.getId());
        if(null!= sqList && sqList.size()>0){
            map.put("msg","有用户投递简历，不能删除");
        }else {
            getManager().remove(zp);
            map.put("msg", SUCCESS);
        }
        return "map";
    }
    public String getDetail(){
        String zpId = getRequest().getParameter("zpId");
        String type = getRequest().getParameter("type");
         zp = getManager().get(Integer.parseInt(zpId));
        if(null!=type && type.equals("mine")){
            System.out.println("我发布的不需要+1");
        }else {
            if (null != zp.getHitTimes()) {
                zp.setHitTimes(zp.getHitTimes() + 1);
            } else {
                zp.setHitTimes(1);
            }
        }
        getManager().update(zp);
        return "zp";
    }
    public String save() {
        ZhaoPin zp = null;
        String zpId = getRequest().getParameter("zpId");
        if (StringUtils.isNotBlank(zpId)) {
            zp = getManager().get(Integer.parseInt(zpId));
        } else {
            zp = new ZhaoPin();
            zp.setCreateTime(new Date());
        }
        String comName = getRequest().getParameter("comName");
        zp.setComName(comName);
        String zwName = getRequest().getParameter("zwName");
        zp.setZwName(zwName);
        String zpNums = getRequest().getParameter("zpNums");
        if(StringUtils.isNotBlank(zpNums)){
            zp.setZpNums(zpNums);
        }
        String daiyu = getRequest().getParameter("daiyu");
        zp.setDaiyu(daiyu);
        String relatePhone = getRequest().getParameter("relatePhone");
        zp.setRelatePhone(relatePhone);
        String email = getRequest().getParameter("email");
        zp.setEmail(email);
        String comAddress = getRequest().getParameter("comAddress");
        zp.setComAddress(comAddress);
        String desn = getRequest().getParameter("desn");
        zp.setDesn(desn);
        String yaoqiu = getRequest().getParameter("yaoqiu");
        zp.setYpyaoqiu(yaoqiu);
        String endDate = getRequest().getParameter("endDate");
        zp.setStopDate(endDate);
        String userId = getRequest().getParameter("userId");
        if(StringUtils.isNotBlank(userId)){
            zp.setUser(userManager.get(Integer.parseInt(userId)));
        }
        if(StringUtils.isNotBlank(zpId)){
            getManager().update(zp);
        }else{
            zp.setVisible("0");
            getManager().save(zp);
        }
        map.put("msg",SUCCESS);
        return "map";
    }

    public File[] getFiles() {
        return files;
    }

    public void setFiles(File[] files) {
        this.files = files;
    }

    public String[] getFilesFileName() {
        return filesFileName;
    }

    public void setFilesFileName(String[] filesFileName) {
        this.filesFileName = filesFileName;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public List<ZhaoPin> getClList() {
        return clList;
    }

    public void setClList(List<ZhaoPin> clList) {
        this.clList = clList;
    }

    public ZhaoPin getZp() {
        return zp;
    }

    public void setZp(ZhaoPin zp) {
        this.zp = zp;
    }
}
