package com.systop.common.modules.fileattch.service;

import org.springframework.stereotype.Service;

import com.systop.common.modules.fileattch.model.FileAttch;
import com.systop.core.service.BaseGenericsManager;

@Service
public class FileAttchManager extends BaseGenericsManager<FileAttch> {

	/**
	 * 根据上传的附件Id更新附件表的Type
	 */
  public FileAttch getFileAttch(Integer AttachId) {
    String hql = "from FileAttch fa where fa.id = ? ";
    return (FileAttch) getDao().findObject(hql, new Object[] { AttachId });
  }
	
	
}
