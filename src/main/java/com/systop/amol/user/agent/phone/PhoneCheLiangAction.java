package com.systop.amol.user.agent.phone;

import com.systop.amol.user.agent.model.CheLiang;
import com.systop.amol.user.agent.model.RzFile;
import com.systop.amol.user.agent.model.YhShenqing;
import com.systop.amol.user.agent.service.CheLiangManager;
import com.systop.amol.user.agent.service.RzFileManager;
import com.systop.amol.user.agent.service.YhShenqingManager;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;
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
public class PhoneCheLiangAction extends DefaultCrudAction<CheLiang, CheLiangManager> {

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
    private CheLiang cl;
    /**
     * 每页显示条数
     */
    private String pageCount = "10";
    private List<CheLiang> clList;

    public String updateHitTimes(){
        String clId = getRequest().getParameter("clId");
        CheLiang cl = null;
        if(StringUtils.isNotBlank(clId)){
            cl = getManager().get(Integer.parseInt(clId));
            if(null!=cl.getHitTimes()){
                cl.setHitTimes(cl.getHitTimes()+1);
            }else{
                cl.setHitTimes(1);
            }
            getManager().update(cl);
            map.put("msg",SUCCESS);
        }
        return "map";
    }
    public String getMyCheLiang(){
        String userId = getRequest().getParameter("userId");
        page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("from CheLiang u where 1=1 and u.visible='1' ");
        if(StringUtils.isNotBlank(userId)){
            hql.append(" and  u.user.id=? ");
            args.add(Integer.parseInt(userId));
        }
        String clName = getRequest().getParameter("clName");
        if(StringUtils.isNotBlank(clName)){
            hql.append(" and  u.clName like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(clName));
        }
        //priceQj价格区间
        String priceQj = getRequest().getParameter("priceQj");
        if(StringUtils.isNotBlank(priceQj)){
            String[] prices = priceQj.split("-");
            hql.append(" and  u.clPrice between ? and ? ");
            args.add(Double.valueOf(prices[0]));
            args.add(Double.valueOf(prices[1]));
        }
        //使用年限区间
        String usedYears = getRequest().getParameter("usedYears");
        if(StringUtils.isNotBlank(usedYears)){
            String[] usedYearss = usedYears.split("-");
            hql.append(" and  u.usedYears between ? and ? ");
            args.add(Double.valueOf(usedYearss[0]));
            args.add(Double.valueOf(usedYearss[1]));
        }
        //品牌搜索
        String grand = getRequest().getParameter("grand");
        if(StringUtils.isNotBlank(grand)){
            hql.append(" and  u.grand like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(grand));
        }
        //品牌搜索
        String clXh = getRequest().getParameter("clXh");
        if(StringUtils.isNotBlank(clXh)){
            hql.append(" and  u.clXh like ? ");
            args.add(MatchMode.ANYWHERE.toMatchString(clXh));
        }
        hql.append(" order by u.createTime desc");
        page = getManager().pageQuery(page, hql.toString(), args.toArray());
        clList = page.getData();
        for(CheLiang cl : clList){
            List<RzFile> clList = rzFileManager.getCheliangFiles(cl.getId());
            if(null!=clList && clList.size()>0){
                cl.setImageUrl(clList.get(0).getImageUrl());
                if(null!=cl.getRegion()){
                    cl.setRegionName(cl.getRegion().getName());
                    if(null!=cl.getRegion().getParent()){
                        cl.setRegionName(cl.getRegion().getParent().getName()+" "+cl.getRegion().getName());
                    }
                }
            }
        }
        return "clList";
    }
    public String remove(){
        String clId = getRequest().getParameter("clId");
        CheLiang cl = getManager().get(Integer.parseInt(clId));
        List<YhShenqing> sqList = yhShenqingManager.getList("cl",cl.getId());
        if(null!= sqList && sqList.size()>0){
            map.put("msg","有用户申请看车，不能删除");
        }else {
            getManager().remove(cl);
            map.put("msg", SUCCESS);
        }
        return "map";
    }
    public String getDetail(){
        String clId = getRequest().getParameter("clId");
        String type = getRequest().getParameter("type");
        cl = getManager().get(Integer.parseInt(clId));
        if(null!=type && type.equals("mine")){
            System.out.println("我发布的不需要+1");
        }else{
            if (null != cl.getHitTimes()) {
                cl.setHitTimes(cl.getHitTimes() + 1);
            } else {
                cl.setHitTimes(1);
            }
        }
        getManager().update(cl);
        if(null!=cl.getRegion()){
            cl.setRegionName(cl.getRegion().getName());
            if(null!=cl.getRegion().getParent()){
                cl.setRegionName(cl.getRegion().getParent().getName()+" "+cl.getRegion().getName());
            }
        }
        List<RzFile> clList = rzFileManager.getCheliangFiles(Integer.parseInt(clId));
        cl.setRzList(clList);
        return "cl";
    }
    public String save() {
                    CheLiang cl = null;
                    String clId = getRequest().getParameter("clId");
                    if (StringUtils.isNotBlank(clId)) {
                        cl = getManager().get(Integer.parseInt(clId));
                    } else {
                        cl = new CheLiang();
                        cl.setCreateTime(new Date());
                    }
                    String clName = getRequest().getParameter("clName");
                    cl.setClName(clName);
                    String clXh = getRequest().getParameter("clXh");
                    cl.setClXh(clXh);
                     String grand = getRequest().getParameter("grand");
                    cl.setGrand(grand);
                    String clPrice = getRequest().getParameter("clPrice");
                    if (StringUtils.isNotBlank(clPrice)) {
                        cl.setClPrice(Double.valueOf(clPrice));
                    }
                    String milinary = getRequest().getParameter("milinary");
                    cl.setMilinary(milinary);
                    String usedYears = getRequest().getParameter("usedYears");
                    cl.setUsedYears(Double.valueOf(usedYears));
                    String relatePhone = getRequest().getParameter("relatePhone");
                    cl.setRelatePhone(relatePhone);
                    String shangpaiDate = getRequest().getParameter("shangpaiDate");
                    cl.setShangpaiDate(shangpaiDate);
                    String guohucishu = getRequest().getParameter("guohucishu");
                    cl.setGuohucishu(guohucishu);
                    String regionId = getRequest().getParameter("regionId");
                    if(StringUtils.isNotBlank(regionId)){
                        cl.setRegion(regionManager.get(Integer.parseInt(regionId)));
                    }
                    String bxStopDate = getRequest().getParameter("bxStopDate");
                    cl.setBxStopDate(bxStopDate);
                    String njStopDate = getRequest().getParameter("njStopDate");
                    cl.setNjStopDate(njStopDate);
                    String publisher = getRequest().getParameter("publisher");
                    cl.setPublisher(publisher);
                    String userId = getRequest().getParameter("userId");
                    if(StringUtils.isNotBlank(userId)){
                        cl.setUser(userManager.get(Integer.parseInt(userId)));
                    }
                    String noUpdateIds = getRequest().getParameter("noUpdateIds");
                    if (StringUtils.isNotBlank(clId)) {
                        getManager().deleteUpdateFile(clId,noUpdateIds);
                    }
                    if(StringUtils.isNotBlank(clId)){
                        getManager().update(cl);
                    }else{
                        cl.setVisible("0");
                        getManager().save(cl);
                    }
                for (int i=0;i<files.length;i++){
                    RzFile rzFile = new RzFile();
                    attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
                    if (files[i] != null) {
                        rzFile.setCreateTime(new Date());
                        String filePath = UpLoadUtil.doUpload(files[i], filesFileName[i], attchFolder,
                                getServletContext());
                        rzFile.setImageUrl(filePath);
                        rzFile.setCheLiang(cl);
                        rzFileManager.save(rzFile);
                     }
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

    public List<CheLiang> getClList() {
        return clList;
    }

    public void setClList(List<CheLiang> clList) {
        this.clList = clList;
    }

    public CheLiang getCl() {
        return cl;
    }

    public void setCl(CheLiang cl) {
        this.cl = cl;
    }
}
