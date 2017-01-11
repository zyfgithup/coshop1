package com.systop.amol.purchase.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.purchase.model.PurchaseDetail;
import com.systop.amol.purchase.model.PurchaseTotal;
import com.systop.amol.purchase.service.PurchaseDetailManager;
import com.systop.amol.purchase.service.PurchaseTotalManager;
import com.systop.amol.stock.StockConstants;
import com.systop.amol.stock.model.StockInit;
import com.systop.amol.stock.service.StockInitManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 采购汇总管理Manager
 * 
 * @author WangHaiYan
 * 
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PurchaseTotalAction extends
		DefaultCrudAction<PurchaseTotal, PurchaseTotalManager> {
	@Autowired
	private PurchaseDetailManager purchaseDetailManager;
	@Autowired
	private StockInitManager stockInitManager;
	//@Autowired
	//private SupplierManager supplierManager;
	//供应商
	private String supplier;

	// 开始日期
	private String startDate;
	// 结束日期
	private String endDate;
	// 商品名称
	private String productName;
   //商品规格
	private String productStardard;
	public String getProductStardard() {
		return productStardard;
	}

	public void setProductStardard(String productStardard) {
		this.productStardard = productStardard;
	}

	// 商品类型
	private String prosort;
	
	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getProsort() {
		return prosort;
	}

	public void setProsort(String prosort) {
		this.prosort = prosort;
	}

	public PurchaseDetailManager getPurchaseDetailManager() {
		return purchaseDetailManager;
	}

	public void setPurchaseDetailManager(
			PurchaseDetailManager purchaseDetailManager) {
		this.purchaseDetailManager = purchaseDetailManager;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String index() {
		try {
			User user = UserUtil.getPrincipal(getRequest());
			if (user != null) {

			} else {
				this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
				return INDEX;
			}

			// 进货
			List args = new ArrayList();
			List rargs = new ArrayList();
			List initargs = new ArrayList();
			StringBuffer hql=null;
			StringBuffer rhql=null;
			StringBuffer inithql=null;
			if(user.getType().equals("employee")){//职员只汇总自己的，一级经销商汇总全部的
			 hql = new StringBuffer(
					"from PurchaseDetail pd where pd.purchase.status=0 "
							+ "and pd.purchase.billType=1 and pd.purchase.user.id=? ");
			 rhql = new StringBuffer(
					"from PurchaseDetail pd where pd.purchase.status=0 "
							+ "and pd.purchase.billType=2 and pd.purchase.user.id=? ");
			 inithql=new StringBuffer("from StockInit pd where pd.user.id=?");
     
			args.add(user.getId());
			rargs.add(user.getId());
			initargs.add(user.getSuperior().getId());
			}else{
				hql = new StringBuffer(
						"from PurchaseDetail pd where pd.purchase.status=0 "
								+ "and pd.purchase.billType=1 and (pd.purchase.user.id=? or pd.purchase.user.superior.id=?)  ");
				 rhql = new StringBuffer(
						"from PurchaseDetail pd where pd.purchase.status=0 "
								+ "and pd.purchase.billType=2 and (pd.purchase.user.id=? or pd.purchase.user.superior.id=?) ");
				 inithql=new StringBuffer("from StockInit pd where pd.user.id=?");
	     
				 args.add(user.getId());
				args.add(user.getId());
				rargs.add(user.getId());
				rargs.add(user.getId());
				initargs.add(user.getId());
			}
			if (productName != null && !productName.equals("")) {
				hql.append(" and pd.products.name like ?");
				rhql.append(" and pd.products.name like ?");
				inithql.append(" and pd.products.name like ?");
				args.add("%" + productName + "%");
				rargs.add("%" + productName + "%");
				initargs.add("%" + productName + "%");
			}
			if (productStardard != null && !productStardard.equals("")) {
				hql.append(" and pd.products.stardard like ?");
				rhql.append(" and pd.products.stardard like ?");
				inithql.append(" and pd.products.stardard like ?");
				args.add("%" + productStardard + "%");
				rargs.add("%" + productStardard + "%");
				initargs.add("%" + productStardard + "%");
			}
			if (supplier != null && !supplier.equals("")) {
				hql.append(" and pd.purchase.supplier.name like ?");
				rhql.append(" and pd.purchase.supplier.name like ?");
				inithql.append(" and pd.products.supplier.name like ?");
				args.add("%" + supplier + "%");
				rargs.add("%" + supplier + "%");
				initargs.add("%" + supplier + "%");
			}
			if (prosort != null && !prosort.equals("") && !prosort.equals("0")) {
				ProductSort productSort = getManager().getDao().get(
						ProductSort.class, Integer.parseInt(prosort));
				if (productSort != null) {
					hql.append("and pd.products.prosort.serialNo like ? ");
					rhql.append("and pd.products.prosort.serialNo like ? ");
					inithql.append("and pd.products.prosort.serialNo like ? ");
					args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
					rargs.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
					initargs.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
				}
			}
			try {
				if (StringUtils.isNotBlank(startDate)) {
					hql.append(" and pd.purchase.sdate >= ?");
					rhql.append(" and pd.purchase.sdate >= ?");
					inithql.append(" and pd.createTime >= ?");
					args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
							"yyyy-MM-dd", startDate)));
					rargs.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
							"yyyy-MM-dd", startDate)));
					initargs.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
							"yyyy-MM-dd", startDate)));
				}
				if (StringUtils.isNotBlank(endDate)) {
					hql.append(" and pd.purchase.sdate <= ?");
					rhql.append(" and pd.purchase.sdate <= ?");
					inithql.append(" and pd.createTime <= ?");
					args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
							"yyyy-MM-dd", endDate)));
					rargs.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
							"yyyy-MM-dd", endDate)));
					initargs.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
							"yyyy-MM-dd", endDate)));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//期初
			List<StockInit> silist = stockInitManager.query(inithql.toString(),
					initargs.toArray());
			Map<Products, PurchaseTotal> ptmap = new HashMap<Products, PurchaseTotal>();
			for (StockInit si : silist) {
				PurchaseTotal pt = null;
				if (ptmap.containsKey(si.getProducts())) {
					pt = ptmap.get(si.getProducts());
					pt.setInitCount(si.getCount()+pt.getInitCount());
					pt.setInitAmount(pt.getInitAmount()+si.getAmount());
				} else {
					pt = new PurchaseTotal();
					pt.setProduct(si.getProducts());
					pt.setInitCount(si.getCount());
					pt.setInitAmount(si.getAmount().doubleValue());
					pt.setInCount(0);
					pt.setInAmount(0d);
					pt.setReturnCount(0);
					pt.setReturnAmount(0d);
				}
				ptmap.put(si.getProducts(), pt);
			}
			//入库
			List<PurchaseDetail> pdlist = purchaseDetailManager.query(hql.toString(),
					args.toArray());
			
			for (PurchaseDetail pd : pdlist) {
				PurchaseTotal pt = null;
				if (ptmap.containsKey(pd.getProducts())) {
					pt = ptmap.get(pd.getProducts());
					pt.setInCount(pt.getInCount() + pd.getCount());
					pt.setInAmount(pt.getInAmount() + pd.getAmount());
				} else {
					pt = new PurchaseTotal();
					pt.setInitCount(0);
					pt.setInitAmount(0d);
					pt.setProduct(pd.getProducts());
					pt.setInCount(pd.getCount());
					pt.setInAmount(pd.getAmount());
					pt.setReturnCount(0);
					pt.setReturnAmount(0d);
				}
				ptmap.put(pd.getProducts(), pt);
			}
			// 退货

			pdlist = purchaseDetailManager.query(rhql.toString(), rargs.toArray());
			for (PurchaseDetail pd : pdlist) {
				PurchaseTotal pt = null;
				if (ptmap.containsKey(pd.getProducts())) {
					pt = ptmap.get(pd.getProducts());
					pt.setReturnCount(pt.getReturnCount() + pd.getCount());
					pt.setReturnAmount(pt.getReturnAmount() + pd.getAmount());
				} else {
					pt = new PurchaseTotal();
					pt.setProduct(pd.getProducts());
					pt.setReturnCount(pd.getCount());
					pt.setReturnAmount(pd.getAmount());
				}

				ptmap.put(pd.getProducts(), pt);
			}
			
			
			List<PurchaseTotal> ptlist = new ArrayList<PurchaseTotal>();
			java.util.Iterator it = ptmap.entrySet().iterator();
			while (it.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				PurchaseTotal pt = (PurchaseTotal) entry.getValue();
				
				pt.setBhzcount(StockConstants.getUnitPack(getServletContext(), pt.getProduct().getId(),pt.getInCount()+pt.getInitCount()-pt.getReturnCount()));
				pt.setBinCount(StockConstants.getUnitPack(getServletContext(), pt.getProduct().getId(),pt.getInCount()));
				pt.setBinitCount(StockConstants.getUnitPack(getServletContext(), pt.getProduct().getId(),pt.getInitCount()));
				pt.setBreturnCount(StockConstants.getUnitPack(getServletContext(), pt.getProduct().getId(),pt.getReturnCount()));
				ptlist.add(pt);
			}
			Page page = PageUtil.getPage(getPageNo(), getPageSize());
			page.setData(ptlist);
			restorePageData(page);

			List<PurchaseTotal> list = new ArrayList<PurchaseTotal>();
			list = page.getData();
			Integer initcTotal = 0;
			Double initaTotal = 0.00d;
			Integer incTotal = 0;
			Double inaTotal = 0.00d;
			Integer rcTotal = 0;
			Double raTotal = 0.00d;
			for (PurchaseTotal p : list) {
				if(p.getInCount() == null){
					p.setInCount(0);
				}
				if(p.getInAmount() == null){
					p.setInAmount(0d);
				}
				incTotal += p.getInCount();
				inaTotal += p.getInAmount();
				initcTotal += p.getInitCount();
				initaTotal += p.getInitAmount();
				rcTotal += p.getReturnCount();
				raTotal += p.getReturnAmount();

			}
			getRequest().setAttribute("initcTotal", String.valueOf(initcTotal));
			getRequest().setAttribute("initaTotal", String.valueOf(initaTotal));
			getRequest().setAttribute("incTotal", String.valueOf(incTotal));
			getRequest().setAttribute("inaTotal", String.valueOf(inaTotal));
			getRequest().setAttribute("rcTotal", String.valueOf(rcTotal));
			getRequest().setAttribute("raTotal", String.valueOf(raTotal));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return INDEX;
	}
	/**JSON数据*/
	/*private List cardsRst;
	public List getCardsRst() {
		return cardsRst;
	}

	public void setCardsRst(List cardsRst) {
		this.cardsRst = cardsRst;
	}*/

	/**
	 * 自动匹配卡号信息
	 * @return
	 */
	/*public String getAutoSupplier() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {

		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INDEX;
		}
		cardsRst = Collections.EMPTY_LIST;
		if (CollectionUtils.isEmpty(cardsRst)) {
			cardsRst = supplierManager.getSupplier(user);
		}
		return "jsonRst";
	}*/
}
