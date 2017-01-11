package com.systop.common.modules.fileattch.webapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.common.modules.fileattch.FileConstants;
import com.systop.common.modules.fileattch.model.FileAttch;
import com.systop.common.modules.fileattch.service.FileAttchManager;
import com.systop.core.util.DateUtil;
import com.systop.core.webapp.struts2.action.JsonCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;

@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FileAttchAction extends JsonCrudAction<FileAttch, FileAttchManager> {

	private File attch;

	private String attchFileName;

	private String attchFolder = "/uploadFiles/fileAttch/";
	
	//id1,id2,id3 组成的查询字符串findFiles()
	private String fileIds;
	
	//文件上传成功后异步返回ID
	private Integer handleFileId = null;
	
	//异步操作返回结果
	private Map<String, Object> optResult = null;

	public String upLoadFile() {
		attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
		handleFileId = 0;
		if (attch != null) {
			FileAttch file = new FileAttch();
			String filePath = UpLoadUtil.doUpload(attch,
					attchFileName, attchFolder, getServletContext());
			file.setName(attchFileName);
			String ext = attchFileName.substring(attchFileName.lastIndexOf("."));
			file.setExt(ext);
			file.setPath(filePath);
			//文件大小负值
			file.setTotalBytes(attch.length());
			file.setCreatetime(new Date());
			getManager().save(file);
			handleFileId = file.getId();
		}
		return "upload_success";
	}
	
	/**
	 * 异步删除删除附件
	 */
	public String ajaxRemove(){
		optResult = new HashMap<String, Object>();
		if (SUCCESS.equals(remove())){
			optResult.put("result", "success");
			optResult.put("id", getModel().getId());
		}else{
			optResult.put("result", "error");
			optResult.put("id", getModel().getId());
		}
		return "ajax_delete";
	}
	
	/**
	 * 删除附件
	 */
	public String remove(){
		if (getModel()!= null && getModel().getId() != null){
			String path = getModel().getPath();
			path = getServletContext().getRealPath(path);
			File f = new File(path);
			if (f.exists()){
				if(!f.delete()){
					//如果文件删除失败需要记录吗？应该需要吧，得先记录下，以后再删
					getModel().setType(FileConstants.DEL_FAIL);
					getManager().save(getModel());
				}
			}
			getManager().remove(getModel());
		}else{
			addActionError("文件被其他模块引用,删除失败!");
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 根据fileIds异步获得文件列表
	 * @return
	 */
	public String findFiles(){
		try{
			if (fileIds != null){
				String[] ids = fileIds.split(",");
				List<FileAttch> files = new ArrayList<FileAttch>();
				optResult = new HashMap<String, Object>();
				optResult.put("result", "success");
				for(String id : ids){
					if (StringUtils.isNumeric(id)){
						FileAttch file = getManager().get(Integer.valueOf(id));
						if (file != null){
							files.add(file);
						}
					}
				}
				optResult.put("files", files);
			}
		}catch (Exception e){
			optResult.put("result", "error");
			optResult.put("error", e.getClass().toString());
		}
		return "find_files";
	}

	public File getAttch() {
		return attch;
	}

	public void setAttch(File attch) {
		this.attch = attch;
	}

	public String getAttchFileName() {
		return attchFileName;
	}

	public void setAttchFileName(String attchFileName) {
		this.attchFileName = attchFileName;
	}

	public Integer getHandleFileId() {
		return handleFileId;
	}

	public Map<String, Object> getOptResult() {
		return optResult;
	}

	public void setFileIds(String fileIds) {
		this.fileIds = fileIds;
	}
	
}
