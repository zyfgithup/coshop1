package com.systop.amol.user.agent.phone;

import com.systop.amol.user.agent.model.RzFile;
import com.systop.amol.user.agent.model.TouBaoRen;
import com.systop.amol.user.agent.service.RzFileManager;
import com.systop.amol.user.agent.service.TouBaoRenManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.*;

/**
 * Created by Administrator on 2016/9/17.
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneTbrAction extends DefaultCrudAction<TouBaoRen,TouBaoRenManager> {

    @Autowired
    UserManager userManager;
    @Autowired
    RzFileManager rzFileManager;
    private Map<String,Object> result = new HashMap<String, Object>();
    private  File[] zszFile;
    private  String[] zszFileName;
    private  File[] zzFile;
    private  String[] zzFileName;
    private  File[] qtFile;
    private  String[] qtFileName;
    private  List<TouBaoRen> touBaoRens;
    private String attchFolder = "/uploadFiles/fileAttch/";
    private TouBaoRen touBaoRen;
    public String saveBtr(){
       try{
           String userId = getRequest().getParameter("userId");
           User user = null;
           if(StringUtils.isNotBlank(userId)){
               user = userManager.get(Integer.parseInt(userId));
               getModel().setUser(user);
           }
           getModel().setCreateTime(new Date());
           String tbName = getRequest().getParameter("tbName");
           String idCard = getRequest().getParameter("idCard");
           getModel().setTbName(tbName);
           getModel().setIdCard(idCard);
           getManager().save(getModel());
           List<RzFile> rzFiles = new ArrayList<RzFile>();
           if(null!=zszFile && zszFile.length>0){
               RzFile rz = null;
               for (int i=0;i<zszFile.length;i++){
                   rz = new RzFile();
                   String filePath = uploadFile(zszFile[i],zszFileName[i]);
                   rz.setImageUrl(filePath);
                   rz.setCreateTime(new Date());
                   rz.setFileType("3");
                   rz.setTouBaoRen(getModel());
                   rzFiles.add(rz);
               }
           }
           if(null!=zzFile && zzFile.length>0){
               RzFile rz = null;
               for (int i=0;i<zzFile.length;i++){
                   rz = new RzFile();
                   String filePath = uploadFile(zzFile[i],zzFileName[i]);
                   rz.setImageUrl(filePath);
                   rz.setCreateTime(new Date());
                   rz.setFileType("7");
                   rz.setTouBaoRen(getModel());
                   rzFiles.add(rz);
               }
           }
           if(null!=qtFile && qtFile.length>0){
               RzFile rz = null;
               for (int i=0;i<qtFile.length;i++){
                   rz = new RzFile();
                   String filePath = uploadFile(qtFile[i],qtFileName[i]);
                   rz.setImageUrl(filePath);
                   rz.setCreateTime(new Date());
                   rz.setFileType("8");
                   rz.setTouBaoRen(getModel());
                   rzFiles.add(rz);
               }
           }
           getManager().save(getModel(),rzFiles);
           result.put("msg","success");
       }catch (Exception e){
           result.put("msg","error");
       }
        return "result";
    }
    public String getTbrDetail(){
        String tbrId = getRequest().getParameter("tbrId");
        if(StringUtils.isNotBlank(tbrId)) {
            touBaoRen = getManager().get(Integer.parseInt(tbrId));
        }
        List<RzFile> rzFiles = null;
        if(null!=touBaoRen){
            rzFiles = new ArrayList<RzFile>();
            rzFiles = rzFileManager.getFilesByTbId(touBaoRen.getId());
            touBaoRen.setRzFiles(rzFiles);
            touBaoRen.setCreateTimeStr(String.valueOf(touBaoRen.getCreateTime()).replace("T"," "));
        }
        return "touBaoRen";

    }
    public String getMyTbr(){
        String userId = getRequest().getParameter("userId");
        if(StringUtils.isNotBlank(userId)) {
            touBaoRens = getManager().getList(Integer.parseInt(userId));
        }
        if(null!=touBaoRens && touBaoRens.size()>0){
            List<RzFile> rzFiles = null;
            for (TouBaoRen touBaoRen :touBaoRens){
                rzFiles = new ArrayList<RzFile>();
                rzFiles = rzFileManager.getFilesByTbId(touBaoRen.getId());
                touBaoRen.setRzFiles(rzFiles);
                touBaoRen.setCreateTimeStr(String.valueOf(touBaoRen.getCreateTime()).replace("T"," "));
            }
        }
        return "touBaoRens";

    }
    public String uploadFile(File file,String fileName){
        return  UpLoadUtil.doUpload(file,fileName, attchFolder,
                getServletContext());
    }
    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public File[] getZszFile() {
        return zszFile;
    }

    public void setZszFile(File[] zszFile) {
        this.zszFile = zszFile;
    }

    public String[] getZszFileName() {
        return zszFileName;
    }

    public void setZszFileName(String[] zszFileName) {
        this.zszFileName = zszFileName;
    }

    public File[] getZzFile() {
        return zzFile;
    }

    public void setZzFile(File[] zzFile) {
        this.zzFile = zzFile;
    }

    public String[] getZzFileName() {
        return zzFileName;
    }

    public void setZzFileName(String[] zzFileName) {
        this.zzFileName = zzFileName;
    }

    public File[] getQtFile() {
        return qtFile;
    }

    public void setQtFile(File[] qtFile) {
        this.qtFile = qtFile;
    }

    public String[] getQtFileName() {
        return qtFileName;
    }

    public void setQtFileName(String[] qtFileName) {
        this.qtFileName = qtFileName;
    }

    public String getAttchFolder() {
        return attchFolder;
    }

    public void setAttchFolder(String attchFolder) {
        this.attchFolder = attchFolder;
    }

    public List<TouBaoRen> getTouBaoRens() {
        return touBaoRens;
    }

    public void setTouBaoRens(List<TouBaoRen> touBaoRens) {
        this.touBaoRens = touBaoRens;
    }

    public TouBaoRen getTouBaoRen() {
        return touBaoRen;
    }

    public void setTouBaoRen(TouBaoRen touBaoRen) {
        this.touBaoRen = touBaoRen;
    }
}
