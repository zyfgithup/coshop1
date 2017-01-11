package com.systop.amol.sales.webapp;

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

import com.systop.amol.base.customer.model.Customer;
import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.purchase.service.PayInitXlsImportHelperFactory;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.ReceiveInit;
import com.systop.amol.sales.service.ReceiveInitManager;
import com.systop.amol.sales.service.ReceiveInitXlsImportHelperFactory;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.util.XlsImportHelper;
import com.systop.core.webapp.struts2.action.BaseAction;
import com.systop.core.webapp.struts2.upload.UploadedFileHandler;

/**
 * 通过Excel导入期初应收信息
 * 
 * @author ShangHua
 */
@SuppressWarnings({ "serial", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReceiveInitImportAction extends BaseAction {

	@Autowired
	private CustomerManager customerManager;

	@Autowired
	private ReceiveInitManager receiveInitManager;

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
		XlsImportHelper xih = ReceiveInitXlsImportHelperFactory.create();
		InputStream is = null;
		Workbook rwb = null;
		Sheet rs = null;
		// 读取数据存放在此list中
		List<String> list = new ArrayList<String>();
		try {
			is = new FileInputStream(data);
			rwb = Workbook.getWorkbook(is);
			// 获取第一张Sheet表
			rs = rwb.getSheet(0);
			for (int i = 1; i < rs.getRows(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				// 得到导入数据map
				xih.importCommonProperties(map, rs, i);
				String name = (String) map.get("name"); // 客户名称
				String idCard = (String) map.get("idCard"); // 客户身份证号
				String amount = (String) (map.get("amount")); // 应收金额
				int j = i + 1;
				// 年月为空不进行导入
				if (StringUtils.isBlank(name)) {
					list.add("Excel表中第【" + j + "】行客户姓名为空，不做导入处理!");
					continue;
				}
				if (StringUtils.isBlank(idCard)) {
					list.add("Excel表中第【" + j + "】行身份证号为空，不做导入处理!");
					continue;
				}
				if (StringUtils.isBlank(amount)) {
					list.add("Excel表中第【" + j + "】应收金额为空，不做导入处理!");
					continue;
				}
				if ("0".equals(amount)) {
					list.add("Excel表中第【" + j + "】应收金额为0，不做导入处理!");
					continue;
				}

				// 获得客户对象信息
				Customer customer = customerManager.searchCustomer(UserUtil
						.getPrincipal(getRequest()).getId(), idCard);
				if (customer != null) {
					
					if(!customer.getName().equals(name.trim())){
						list.add("Excel表中第【" + j + "】行【" + name + "   身份证号：" + idCard
								+ "】该客户系统中不存在，不允许导入！");
						continue;
					}
					
					ReceiveInit receiveInit = receiveInitManager.getReceiveInit(idCard);
					if ((receiveInit != null) && (SalesConstants.INIT_NORMAL.intValue() == receiveInit.getStatus().intValue())) {
						// 删除系统运行月得分表对象信息
						receiveInit.setAmount(Double.parseDouble(amount));
						receiveInitManager.update(receiveInit);
					}else if((receiveInit == null) || (SalesConstants.INIT_LOCKING.intValue() == receiveInit.getStatus().intValue())){
						receiveInit = new ReceiveInit();
						receiveInit.setCustomer(customer);
						receiveInit.setAmount(Double.parseDouble(amount));
						receiveInit.setUser(getLoginUser());
						receiveInitManager.save(receiveInit);
					}
				} else if(customer == null){
					list.add("Excel表中第【" + j + "】行【" + name + "   身份证号：" + idCard
							+ "】该客户系统中不存在，不允许导入！");
					continue;
				}
			}
		} catch (Exception e) {
			errorInfo.add(e.getMessage());
			return INPUT;
		} finally {
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
			List<ReceiveInit> inits = receiveInitManager.get();
			getRequest().setAttribute("list", inits);
			return INPUT;
		}else{
			this.addActionMessage("导入成功！");
		}
		return SUCCESS;
	}

	/**
	 * 将字符串转换为Float型数据格式
	 * 
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
