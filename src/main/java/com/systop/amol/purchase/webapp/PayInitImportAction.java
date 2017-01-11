package com.systop.amol.purchase.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.supplier.model.Supplier;
import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.purchase.model.PayInit;
import com.systop.amol.purchase.service.PayInitManager;
import com.systop.amol.purchase.service.PayInitXlsImportHelperFactory;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.util.DateUtil;
import com.systop.core.util.XlsImportHelper;
import com.systop.core.webapp.struts2.action.BaseAction;
import com.systop.core.webapp.struts2.upload.UploadedFileHandler;


/**
 * 通过Excel导入期初应付信息
 * @author ShangHua
 */
@SuppressWarnings( {"serial", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayInitImportAction extends BaseAction {
	
	@Autowired
	private SupplierManager supplierManager;
	
	@Autowired
	private PayInitManager payInitManager;
	
	/**
	 * 处理文件上传的接口
	 */
	protected UploadedFileHandler uploadedFileHandler;
  
	/**
	 * 存放导入过程中错误信息
	 */
	private List<String> errorInfo;

	/**
	 * 导入数据文件
	 */
	private File data;

	/**
	 * 导入数据文件名称
	 */
	private String dataFileName;

	/**
	 * 备份文件下载
	 */
	private String downFileName = null;

	/**
	 * 下载文件流
	 */
	private InputStream inputStream = null;

	/**
	 * 从Excel中导入监管员信息到数据库
	 */
	public String importData() {
		errorInfo = new ArrayList<String>();
		if (getLoginUser() == null) {
			errorInfo.add("请您重新登录系统!");
			return INPUT;
		}
		// 判断上传文件是否存在
		if (StringUtils.isBlank(dataFileName)) {
			errorInfo.add("请选择要导入的文件!");
			return INPUT;
		}
		if (StringUtils.isNotBlank(dataFileName)) {
			// 判断后缀是否是Xls文件
			if (!XlsImportHelper.isAllowedXls(dataFileName)) {
				errorInfo.add("只能上传以”.xls“为后缀的文件!");
				return INPUT;
			}
		}
		XlsImportHelper xih = PayInitXlsImportHelperFactory.create();
		// 读取数据存放在此list中
		List<String> list = new ArrayList<String>();
		InputStream is = null;
		Workbook rwb = null;
		Sheet rs = null;
		try {
			List<PayInit> paylist = new ArrayList<PayInit>();
			 is = new FileInputStream(data);
			 rwb = Workbook.getWorkbook(is);
			// 获取第一张Sheet表
			 rs = rwb.getSheet(0);
			for (int i = 1; i < rs.getRows(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				// 得到导入数据map
				xih.importCommonProperties(map, rs, i);
				String name = (String) map.get("name"); // 年月
				String amount = (String) (map.get("amount")); // 台网代码    
				int j = i + 1 ;
				// 年月为空不进行导入
				if (StringUtils.isBlank(name)) {
					list.add("Excel表中第【" + j + "】行供应商信息为空，不做导入处理!");
					this.addActionMessage("Excel表中第【" + j + "】行供应商信息为空，不做导入处理!");
					continue;
				}
				if (StringUtils.isBlank(amount)) {
					list.add("Excel表中第【" + j + "】行应付金额为空，不做导入处理!");
					this.addActionMessage("Excel表中第【" + j + "】行应付金额为空，不做导入处理!");
					continue;
				}
				// 获得供应商对象信息
				Supplier supplier = supplierManager.getSupplier(name,getLoginUser().getId());
				if (supplier != null) {
					//
					PayInit od = new PayInit();
					od.setPdate(DateUtil.getCurrentDate());
					od.setAmount(Double.parseDouble(amount));
					od.setPayamount(0d);
					od.setPdate(DateUtil.getCurrentDate());
		            od.setUser(getLoginUser());
					od.setSupplier(supplier);
					paylist.add(od);
					
				} else {
					list.add("Excel表中第【" + j + "】行【" + name + "】该供应商系统中不存在，不允许导入!");
					this.addActionMessage("Excel表中第【" + j + "】行【" + name + "】该供应商系统中不存在，不允许导入!");
					continue;
				}
			}
				 payInitManager.save(getLoginUser().getId(),paylist);
				 rs=null;
				 rwb=null;
				 is.close();
		            
		} catch (Exception e) {
			errorInfo.add(e.getMessage());
			this.addActionMessage(e.getMessage());
			return INPUT;
		}finally {
			if (rwb != null) {
				rwb.close();
			}
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (list.size() > 0) {
			errorInfo.addAll(list);
		    
		}else{
		this.addActionMessage("导入成功！");
		}
		//List<PayInit> payList =payInitManager.query("from PayInit where user.id=?", getLoginUser().getId());
		//if(payList.size()>0){
	   // this.getRequest().setAttribute("list", payList);
	
		//}
		return SUCCESS;
	}

	/**
	 * 将字符串转换为Float型数据格式
	 * @param str
	 * @return
	 */
	public Float parseFloat(String str) {
	  Float param = null;
	   if (StringUtils.isBlank(str)) {
	      return null;
	    }
	   try {
	     if (StringUtils.isNotBlank(str)) {
	       param = Float.valueOf(str);
	     }
	   } catch (Exception e) {
	      return null;
	    }
	  return param;
	}
		
  /**
   * @return 用户登录信息
   */
  private User getLoginUser() {
		User user = UserUtil.getPrincipal(getRequest());
    return user;
  }

  
	/**
	 * @return
	 */	
	protected Map getTableTypes() {
		return PayInitXlsImportHelperFactory.properties;
	}
	
	public UploadedFileHandler getUploadedFileHandler() {
		return uploadedFileHandler;
	}

	/**
	 * 设置{@link UploadedFileHandler}对象，用于处理单个上传文件.
	 * 
	 * @param uploadedFileHandler
	 */
	public void setUploadedFileHandler(UploadedFileHandler uploadedFileHandler) {
		this.uploadedFileHandler = uploadedFileHandler;
	}

	public File getData() {
		return data;
	}

	public void setData(File data) {
		this.data = data;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	public String getDownFileName() {
		return downFileName;
	}

	public void setDownFileName(String downFileName) {
		this.downFileName = downFileName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public List<String> getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(List<String> errorInfo) {
		this.errorInfo = errorInfo;
	}

}
