package com.systop.amol.sales.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.sales.model.Barcode;
import com.systop.amol.sales.service.BarcodeManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 条形码管理Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BarcodeAction extends DefaultCrudAction<Barcode, BarcodeManager> {

	/** 供应商manager */
	@Resource
	private SupplierManager supplierManager;
	
	/** 开始时间 */
	private String startDate;

	/** 结束时间 */
	private String endDate;
	
	/** 供应商名称id */
	private Integer suppliersId;
	
	/** 供应商名称 **/
	private String supplierName;

	/**
	 * 货品流向信息     生产厂家查询
	 */
	@Override
	public String index() {
		try {
			if (StringUtils.isNotBlank(getModel().getBarcode()) || StringUtils.isNotBlank(startDate)
					|| StringUtils.isNotBlank(endDate)) {
				Page page = PageUtil.getPage(getPageNo(), getPageSize());
				StringBuffer sql = new StringBuffer("from Barcode b where 1 = 1 ");
				List args = new ArrayList();

				User user = UserUtil.getPrincipal(getRequest());
				if (user != null) {
					sql.append(" and b.salesDetail.products.supplier.name = ? ");
					args.add(user.getName());
				}

				if (StringUtils.isNotBlank(getModel().getBarcode())) {
					// sql.append(" and b.barcode like ?");
					// args.add(MatchMode.ANYWHERE.toMatchString(getModel().getBarcode()));
					sql.append(" and b.barcode = ?");
					args.add(getModel().getBarcode());
				}

				if (StringUtils.isNotBlank(getModel().getBarcode())) {
					sql.append(" and b.salesDetail.products.name like ?");
					args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesDetail()
							.getProducts().getName()));
				}

				try {
					if (StringUtils.isNotBlank(startDate)) {
						sql.append(" and b.salesDetail.sales.createTime >= ?");
						args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
								"yyyy-MM-dd", startDate)));
					}
					if (StringUtils.isNotBlank(endDate)) {
						sql.append(" and b.salesDetail.sales.createTime <= ?");
						args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
								"yyyy-MM-dd", endDate)));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}

				// group by b.barcode,b.salesDetail.sales.user.id
				sql.append(" order by b.id desc");
				page = getManager().pageQuery(page, sql.toString(), args.toArray());
				if(StringUtils.isNotBlank(getModel().getBarcode())){
					List list = page.getData();
					if (list.size() > 1) {
						for (int i = 0; i < list.size(); i++) {
							Barcode barcode = (Barcode) list.get(i);
							if (barcode.getSalesDetail().getSales().getUser().getSuperior() == null) {
								list.remove(i);
							}
						}
					}
				}
				restorePageData(page);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return INDEX;
	}
	
	/**
	 * 货品流向信息      经销商查询
	 */
	public String indexADealer() {
		try {
			if (StringUtils.isNotBlank(getModel().getBarcode()) || StringUtils.isNotBlank(startDate)
					|| StringUtils.isNotBlank(endDate)) {
				Page page = PageUtil.getPage(getPageNo(), getPageSize());
				StringBuffer sql = new StringBuffer("from Barcode b where 1 = 1 ");
				List args = new ArrayList();

				User user = UserUtil.getPrincipal(getRequest());
				// if (user != null) {
				// sql.append(" and b.salesDetail.products.supplier.name = ? ");
				// args.add(user.getName());
				// }
				
				if (user != null && user.getSuperior() == null) {
					sql.append(" and b.salesDetail.sales.user.id = ?");
					args.add(user.getId());
				}
				
				if (StringUtils.isNotBlank(getModel().getBarcode())) {
					// sql.append(" and b.barcode like ?");
					// args.add(MatchMode.ANYWHERE.toMatchString(getModel().getBarcode()));
					sql.append(" and b.barcode = ?");
					args.add(getModel().getBarcode());
				}

				if (StringUtils.isNotBlank(getModel().getBarcode())) {
					sql.append(" and b.salesDetail.products.name like ?");
					args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesDetail()
							.getProducts().getName()));
				}
				
				/*if(suppliersId != null && suppliersId.intValue() > 0){
					sql.append(" and b.salesDetail.products.supplier.id = ?");
					args.add(suppliersId);
				}*/
				
				if(StringUtils.isNotBlank(supplierName)){
					sql.append(" and b.salesDetail.products.supplier.name like ?");
					args.add(MatchMode.ANYWHERE.toMatchString(supplierName));
				}

				try {
					if (StringUtils.isNotBlank(startDate)) {
						sql.append(" and b.salesDetail.sales.createTime >= ?");
						args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
								"yyyy-MM-dd", startDate)));
					}
					if (StringUtils.isNotBlank(endDate)) {
						sql.append(" and b.salesDetail.sales.createTime <= ?");
						args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
								"yyyy-MM-dd", endDate)));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}

				// group by b.barcode,b.salesDetail.sales.user.id
				sql.append(" order by b.id desc");
				page = getManager().pageQuery(page, sql.toString(), args.toArray());
				if(StringUtils.isNotBlank(getModel().getBarcode())){
					List list = page.getData();
					if (list.size() > 1) {
						for (int i = 0; i < list.size(); i++) {
							Barcode barcode = (Barcode) list.get(i);
							if (barcode.getSalesDetail().getSales().getUser().getSuperior() == null) {
								list.remove(i);
							}
						}
					}
				}
				restorePageData(page);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "indexADealer";
	}

	/**
	 * 供应商的下拉列表
	 */
	public Map getSupplierMap() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			// 得到所有状态为可用的供应商
			return supplierManager.getSupplierMapByUser(user);
		}
		return null;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getSuppliersId() {
		return suppliersId;
	}

	public void setSuppliersId(Integer suppliersId) {
		this.suppliersId = suppliersId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
}