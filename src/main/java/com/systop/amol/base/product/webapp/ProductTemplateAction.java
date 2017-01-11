package com.systop.amol.base.product.webapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.systop.amol.base.product.ProductConstants;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.product.service.ProductsManager;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.base.units.service.UnitsManager;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.service.StockManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;

/**
 * 平台添加的商品模板，供分销商选择，规范分销商添加的商品数据及图片展示
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductTemplateAction
		extends DefaultCrudAction<Products, ProductsManager> {

	/**
	 * 计量单位管理
	 */
	@Autowired
	private UnitsManager unitsManager;

	/**
	 * 及即时库存管理
	 */
	@Autowired
	private StockManager stockManager;

	/**
	 * 经销商管理
	 */
	@Autowired
	private SupplierManager supplierManager;

	/** 用户管理 */
	@Autowired
	private UserManager userManager;

	private File attch;

	private String attchFileName;

	private String attchFolder = "/uploadFiles/fileAttch/";

	/**
	 * 计量单位下拉列表
	 */
	public Map getUnitsMap() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			return unitsManager.getUnitsMap(user.getId());
		}
		return null;
	}

	/**
	 * 保存模板商品
	 */
	@Override
	public String save() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		Assert.notNull(getModel());
		getModel().setBelonging(ProductConstants.PRODUCT_TEMPLATE);
		try {

			// ProductSort productSort = null; // 创建商品类型
			//
			// if (getModel() != null && getModel().getProsort() != null
			// && getModel().getProsort().getId() != null) {
			// productSort = getManager().getDao().get(ProductSort.class,
			// getModel().getProsort().getId());
			// }
			// 判断商品类型是否存在
			// if (productSort == null) {
			// this.addActionMessage("当前商品类型不存在，请您重新选择商品类型！");
			// return INPUT;
			// }
			//
			// getModel().setProsort(productSort);
			if(null == getModel().getId()){
				Products productsDB = getManager()
						.findObject("from Products c where c.name = ?", getModel().getName());
				if (productsDB != null) {
					this.addActionMessage("名称不能重复！");
					return INPUT;
				}
			}
			attchFolder += DateUtil.getDateToString(new Date(), "yyyy-MM") + "/";
			if (attch != null) {
				String filePath = UpLoadUtil.doUpload(attch, attchFileName, attchFolder,
						getServletContext());
				getModel().setImageURL(filePath);
			}

			// getManager().getDao().getHibernateTemplate().clear();
			getManager().saveProductTemplate(getModel());
			//
			// // 判断是否为平台还是分销商自营的商品
			// if(null != user.getType() &&
			// !AmolUserConstants.EMPLOYEE.equals(user.getType())){
			// getModel().setBelonging(ProductConstants.MERCHANT);
			// }
			//
			// // 同时保存该商品的基本单位设置信息
			// getManager().unitsItemSave(getModel().getId());

			return SUCCESS;
		} catch (Exception e) {
			addActionError(e.getMessage());
			return INPUT;
		}
	}

	/**
	 * 查询
	 */
	public String index() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Products c where c.visible='1' and  c.belonging = "
				+ ProductConstants.PRODUCT_TEMPLATE);
		List args = new ArrayList();

		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());

		if (getModel().getProductType()) {
			sql.append(" and c.productType = ?");
			args.add(true);
		}

		if (user.getSuperior() != null) {
			Integer superid = user.getSuperior().getId();

			if (superid != null && superid.intValue() > 0
					&& user.getSuperior().getSuperior() == null) {
				sql.append(" and (c.user.id = ? or c.user.id = ?)");
				args.add(superid);
				args.add(user.getId());
			} else if (superid != null && superid.intValue() > 0
					&& user.getSuperior().getSuperior() != null) {
				sql.append(" and (c.user.id = ? or c.user.id = ? or c.user.id = ?)");
				args.add(user.getSuperior().getSuperior().getId());
				args.add(superid);
				args.add(user.getId());
			}

		} else {
			sql.append(" and c.user.id = ? ");
			args.add(user.getId());
		}

		if (getModel().getName() != null
				&& !getModel().getName().trim().equals("")) {
			sql.append(" and c.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (getModel().getCode() != null
				&& !getModel().getCode().trim().equals("")) {
			sql.append(" and c.code like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCode()));
		}
		if (getModel().getSupplier() != null
				&& getModel().getSupplier().getName() != null
				&& !getModel().getSupplier().getName().equals("")) {
			sql.append(" and c.supplier.name like ?");
			args.add(
					MatchMode.ANYWHERE.toMatchString(getModel().getSupplier().getName()));
		}

		// sql.append(" and c.prosort.status = ?");
		// args.add("1");

		// 判断是否是顶级商品类别，如果不是顶级商品类别则加查询条件，否则查询全部
		if (getModel() != null && getModel().getProsort() != null
				&& getModel().getProsort().getId() != null) {
			ProductSort productSort = getManager().getDao().get(ProductSort.class,
					getModel().getProsort().getId());
			if (productSort != null) {
				// sql.append("and (c.prosort.id = ? or c.prosort.parentProsort.id = ?)
				// ");
				// args.add(getModel().getProsort().getId());
				// args.add(getModel().getProsort().getId());
				sql.append("and c.prosort.serialNo like ? ");
				args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
			}

			// 为了返回查询中的条件数据需要将ProductSort存入到MODEL中
			getModel().setProsort(productSort);

		}
		sql.append(" order by c.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}

	/**
	 * 期初库存：导出模板(导出的是所有商品信息)
	 * 
	 * @author songbaojie
	 */
	public String stockInitTemplate() {
		index();
		return "stockInitTemplate";
	}

	/**
	 * 删除商品以及商品单位换算表
	 */
	@Override
	public String remove() {
		Products products = null;
		if (getModel().getId() != null) {
			products = getManager().get(getModel().getId());
			products.setVisible("0");
		}
		try {
			getManager().update(products);
		} catch (Exception e) {
			this.addActionMessage(
					"删除失败，" + "名称为【" + products.getName() + "】的商品已经使用，不能删除！");
		}
		return SUCCESS;
	}

	/**
	 * 进销存采购管理中使用的商品选择列表
	 * 
	 * @return
	 */
	public String showIndex() {
		index();
		return "showIndex";
	}

	/**
	 * 采购管理中使用的商品选择列表此供应商对应的商品
	 * 
	 * @return
	 */
	public String purchaseshowIndex() {
		try {
			Page page = PageUtil.getPage(getPageNo(), getPageSize());
			StringBuffer sql = new StringBuffer(
					"from Products c where c.prosort.status=1 ");
			List args = new ArrayList();

			// 根据当前登录用户查询商品
			User u = UserUtil.getPrincipal(getRequest());
			if (u != null) {
				sql.append(" and (user.id = ? or user.id=?)");
				args.add(u.getId());
				if (u.getSuperior() != null) {
					args.add(u.getSuperior().getId());
				} else {
					args.add(u.getId());
				}
			}

			String status = getRequest().getParameter("status");

			if (status != null && !status.equals("")) {
				sql.append(" and c.prosort.status = ?");
				args.add(status);
			}
			if (getModel() != null && getModel().getSupplier() != null
					&& getModel().getSupplier().getId() != null) {
				sql.append(" and  c.supplier.id=? ");
				args.add(getModel().getSupplier().getId());

			}

			if (getModel().getName() != null
					&& !getModel().getName().trim().equals("")) {
				sql.append(" and c.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
			}
			if (getModel().getCode() != null
					&& !getModel().getCode().trim().equals("")) {
				sql.append(" and c.code like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCode()));
			}

			// 判断是否是顶级商品类别，如果不是顶级商品类别则加查询条件，否则查询全部
			if (getModel() != null && getModel().getProsort() != null
					&& getModel().getProsort().getId() != null) {
				ProductSort productSort = getManager().getDao().get(ProductSort.class,
						getModel().getProsort().getId());
				if (productSort != null) {
					sql.append("and c.prosort.serialNo like ? ");
					args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
				}

				// 为了返回查询中的条件数据需要将ProductSort存入到MODEL中
				getModel().setProsort(productSort);

			}
			page = getManager().pageQuery(page, sql.toString(), args.toArray());
			List<Products> plist = page.getData();
			// 加入即时库存
			String storageid = this.getRequest().getParameter("storageid");
			if (storageid != null && !storageid.equals("0")
					&& !storageid.equals("")) {
				this.getRequest().setAttribute("storageid", storageid);
				for (Products product : plist) {
					Stock stock = stockManager
							.findStockPurchase(Integer.parseInt(storageid), product.getId());
					if (stock == null) {
						product.setOutprice(0f);
					} else {
						product.setOutprice(stock.getCount() * 1.0f);
					}
				}
			}
			restorePageData(page);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "purchaseshowIndex";
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

	public String getAttchFolder() {
		return attchFolder;
	}

	public void setAttchFolder(String attchFolder) {
		this.attchFolder = attchFolder;
	}
}
