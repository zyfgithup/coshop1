package com.systop.amol.pda.sales.webapp;

import com.systop.amol.base.product.service.ProductsManager;
import com.systop.amol.base.storage.service.StorageManager;
import com.systop.amol.card.model.CardGrant;
import com.systop.amol.card.service.CardGrantManager;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.Barcode;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.model.SalesDetail;
import com.systop.amol.sales.service.SalesDetailManager;
import com.systop.amol.sales.service.SalesManager;
import com.systop.amol.sales.utils.Payment;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.service.StockManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 出库单管理Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PdaSalesAction extends DefaultCrudAction<Sales, SalesManager> {

	/** 仓库Manager */
	@Resource
	private StorageManager storageManager;
	/** 出库单详情明细Manager */
	@Resource
	private SalesDetailManager salesDetailManager;
	/** 商品Manager */
	@Resource
	private ProductsManager productsManager;
	/** 实际生效代币卡[与客户管理]Manager */
	@Resource
	private CardGrantManager cardGrantManager;

	/** 及时库存Manager */
	@Resource
	private StockManager stockManager;

	/** 代币卡号 */
	private String cardNo;

	/** 代币卡密码 */
	private String cardPassword;

	/** 开始时间 */
	private String startDate;

	/** 结束时间 */
	private String endDate;

	public String index() {

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Sales s where s.status = 1 ");
		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		if (user.getId() != null && user.getId() > 0) {
			sql.append(" and s.user.id = ? ");
			args.add(user.getId());
		}

		if (StringUtils.isNotBlank(getModel().getCkzt())) {
			sql.append(" and s.ckzt = ? ");
			args.add(getModel().getCkzt());
		}

		if (StringUtils.isNotBlank(getModel().getSalesNo())) {
			sql.append(" and s.salesNo like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
		}

		if (null != getModel().getCustomer()) {
			if (StringUtils.isNotBlank(getModel().getCustomer().getName())) {
				sql.append(" and s.customer.name like ?");
				args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCustomer()
						.getName()));
			}
			if (StringUtils.isNotBlank(getModel().getCustomer().getPhone())) {
				sql.append(" and s.customer.phone = ?");
				args.add(getModel().getCustomer().getPhone());
			}
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and s.createTime >= ?");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and s.createTime <= ?");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (getModel() != null && getModel().getRedRed() != null
				&& getModel().getRedRed() != SalesConstants.ALL) {
			sql.append(" and s.redRed = ?");
			args.add(getModel().getRedRed());
		}

		sql.append(" order by s.createTime desc, s.salesNo desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);

		return "index";
	}

	/**
	 * 新建或者编辑出库单信息Card
	 */
	public String editCard() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		// 生成出库单号
		getModel().setSalesNo(
				getManager().getSalesNumber(SalesConstants.SALES, user.getId()));
		// 时间
		getModel().setCreateTime(DateUtil.getCurrentDate());
		// 添加商品标识为true，可添加商品，显示添加商品按钮
		getRequest().setAttribute("isAddProduct", true);
		super.edit();
		return "editCard";
	}

	/**
	 * 保存出库单
	 */
	@Override
	public String save() {

		List<SalesDetail> sdlist = null;

		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		try {

			// 验证代币卡是否合法
			CardGrant cardGrant = cardGrantManager.getCardGrant(cardNo, cardPassword);
			if (cardGrant != null) {

				getModel().setUser(user);
				Stock stock = stockManager.findStock(user.getId());
				if (stock != null) {
					getModel().setStorage(stock.getStorage());
				}
				getModel().setStatus(SalesConstants.SALES);
				getModel().setCkzt(SalesConstants.NOT_RETURN);
				getModel().setPayment(Payment.CARD);
				getModel().setCustomer(cardGrant.getCustomer());

				// 商品总数量
				double count = 0;
				// 出库单商品
				String[] productCodes = this.getRequest().getParameterValues(
						"productCode");// 商品编码
				String[] counts = this.getRequest().getParameterValues("counts");// 商品数量
				String[] moneys = this.getRequest().getParameterValues("money");// 商品价格
				String[] codes = this.getRequest().getParameterValues("codes");// 商品条形码

				// 出库单详情明细
				sdlist = new ArrayList<SalesDetail>();
				// 商品条形码集合
				List<Object> objectList = new ArrayList<Object>();

				// y商品编码数量
				int y = 0;
				if (productCodes == null) {
					addActionError("<font color='red'>请扫描商品编码</font>");
				} else {
					y = productCodes.length;
				}
				for (int i = 0; i < y; i++) {
					if (StringUtils.isNotBlank(productCodes[i])) {
						SalesDetail sd = new SalesDetail();
						sd.setAmount(Double.parseDouble(moneys[i]));
						sd.setCount(Integer.parseInt(counts[i]));
						count += sd.getCount().intValue();// 累加所有出库商品

						sd.setProducts(productsManager.getProducts(productCodes[i]));// 商品
						sd.setSales(getModel());// 使出库单商品与出库单关联

						// 商品条形码集合
						List<Barcode> barcodes = new ArrayList<Barcode>();
						String code = codes[i];
						String[] stra = code.split(",");
						if (codes != null && stra.length > 0) {
							for (String str : stra) {
								Barcode barcode = new Barcode();
								barcode.setBarcode(str);
								barcode.setSalesDetail(sd);
								// 将条形码记录添加到一个集合中
								barcodes.add(barcode);
							}
						}

						// 将商品条码保存到一个集合中
						objectList.add(barcodes);

						// 将商品记录与添加到一个集合中
						sdlist.add(sd);
					}
				}

				// 将累加的出库商品总数放到出库单中
				getModel().setCount(count);
				// 初始化出库商品对象中的出库真实数量字段
				getModel().setTtno(getModel().getCount());

				// 保存出库单
				getManager().saveStorehouse(getModel(), sdlist, objectList);

				return SUCCESS;
			} else {
				addActionError("<font color='red'>密码输入错误！！</font>");
			}
		} catch (ApplicationException e) {
			saveException(sdlist);
			addActionError(e.getMessage());
			return INPUT;
		} catch (NumberFormatException e) {
			saveException(sdlist);
			addActionError("商品的最小单位数量必须是整数！");
			return INPUT;
		}

		return INPUT;
	}

	/**
	 * 保存出库单时报的异常
	 */
	private void saveException(List<SalesDetail> sdlist) {
		this.getRequest().setAttribute("sd", sdlist);
	}

	/**
	 * 把销售明细传到页面
	 */
	public List<SalesDetail> getSalesDetail(Integer id) {
		List<SalesDetail> sd = salesDetailManager.getDetails(id);
		this.getRequest().setAttribute("sd", sd);
		return sd;
	}


	/**
	 * 仓库的下拉列表
	 */
	public Map getStorageMap() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			Map map = storageManager.getStorageMap(user);
			if (map.size() > 0) {
				return map;
			}
			return null;
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardPassword() {
		return cardPassword;
	}

	public void setCardPassword(String cardPassword) {
		this.cardPassword = cardPassword;
	}

	/**
	 * 冲红map
	 * 
	 * @return
	 */
	public Map<Integer, String> getStatusMap() {
		return SalesConstants.STATUS_MAP;
	}

	/**
	 * 退货状态Map
	 * 
	 * @author 王会璞
	 */
	public Map<String, String> getReturnStatusMap() {
		return SalesConstants.RETURN_STATUS_MAP;
	}
}