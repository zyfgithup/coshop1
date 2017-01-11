package com.systop.amol.sales.webapp;

import com.systop.amol.base.product.ProductConstants;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.SalesSummary;
import com.systop.amol.sales.service.SalesDetailManager;
import com.systop.amol.sales.service.SalesSummaryManager;
import com.systop.amol.sales.utils.Payment;
import com.systop.amol.stock.StockConstants;
import com.systop.amol.user.AmolUserConstants;
import com.systop.amol.util.DoubleFormatUtil;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售汇总管理Action
 * 
 * @author 王会璞
 * 
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SalesSummaryAction extends
		DefaultCrudAction<SalesSummary, SalesSummaryManager> {

	/**
	 * 分销商名称
	 */
	private String distributorName;

	/** 分销商id **/
	private Integer distributorId;

	/** 商品名称 */
	private String productName;

	/** 开始时间 */
	private String startDate;

	/** 结束时间 */
	private String endDate;

	private Integer prosortId;

	/** 商品编码 */
	private String productCode;
	/**
	 * 商品类型
	 */
	private String type;

	/** 身份证号 **/
	private String idCard;
	
	/** 地区id */
	private Integer regionId;
	
	/** 地区名称 */
	private String regionName;
	
	/** 支付方式 */
	private String payment;

	@Resource
	private ProductSortManager productSortManager;

	@Resource
	private UserManager userManager;

	@Resource
	private SalesDetailManager salesDetailManager;
	
	@Resource
	private RegionManager regionManager;
	
	/** 地区 */
	private Region region;

	/**
	 * 详情
	 */
	@Override
	public String view() {
		try {
			Page page = PageUtil.getPage(getPageNo(), getPageSize());
			String productIdStr = getRequest().getParameter("productId");
			if (StringUtils.isNotBlank(productIdStr)) {
				StringBuffer sql = new StringBuffer(
						"from SalesDetail sd where sd.products.id = "
								+ Integer.parseInt(productIdStr) + " and sd.sales.status = "
								+ SalesConstants.SALES + " and sd.sales.redRed = "
								+ SalesConstants.NORMAL);
				List args = new ArrayList();

				if (distributorId != null && distributorId.intValue() > 0) {
					User userDistributor = userManager.get(distributorId);
					if (userDistributor.getSuperior() != null) {
						sql.append(" and sd.sales.user.id = ? ");
						args.add(userDistributor.getId());
					}
				} /*
					 * else { sql.append(
					 * " and sd.sales.user.type = ? and sd.sales.user.superior.id != ?");
					 * args.add(AmolUserConstants.AGENT); args.add(null); }
					 */

				if (StringUtils.isNotBlank(idCard)) {
					sql.append(" and sd.sales.customer.idCard like ? ");
					args.add(MatchMode.ANYWHERE.toMatchString(idCard));
				}

				try {
					if (StringUtils.isNotBlank(startDate)) {
						sql.append(" and sd.sales.createTime >= ? ");
						args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
								"yyyy-MM-dd", startDate)));
					}
					if (StringUtils.isNotBlank(endDate)) {
						sql.append(" and sd.sales.createTime <= ? ");
						args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
								"yyyy-MM-dd", endDate)));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				sql.append(" order by sd.sales.createTime desc");
				page = salesDetailManager.pageQuery(page, sql.toString(),
						args.toArray());
				restorePageData(page);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return VIEW;
	}

	/**
	 * 分销商销售汇总（按商品类别）
	 */
	public String indexDistributor() {
		
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		// 得到检索条件
		Map<String, Object> param = this.getQueryDistributor();
		System.out.println(param.get("sql").toString());
		page = getManager().pageQuery(page, param.get("sql").toString(),
				((List) param.get("param")).toArray());
		System.out.println(page.getData());

		List<SalesSummary> rsList = new ArrayList<SalesSummary>();

		List listSQL = page.getData();
		for (Object o : listSQL) {
			Object[] objs = (Object[]) o; // o对象是Object数组
			SalesSummary salesSummary = new SalesSummary();
			salesSummary.setProductName(objs[0].toString());// 商品名称
			salesSummary.setStardard(objs[1].toString());// 商品规格
			salesSummary.setUnitName(objs[2].toString());// 单位名称
			salesSummary.setOutCount(new Integer(objs[3].toString()));// 出库数量

			// 出库数量包装单位
			Integer productId = new Integer(objs[8].toString());
			salesSummary.setOutUnitPack(StockConstants.getUnitPack(
					getServletContext(), productId, salesSummary.getOutCount()));
			salesSummary.setOutAmount(new Double(objs[4].toString()));// 出库金额

			// 退货数量
			Integer returnCount = new Integer(objs[5].toString());
			salesSummary.setReturnCount(returnCount);

			// 退货数量包装单位
			salesSummary.setReturnUnitPack(StockConstants.getUnitPack(
					getServletContext(), productId, salesSummary.getReturnCount()
							.intValue()));

			// 最终出库总数量包装单位
			salesSummary.setAllUnitPack(StockConstants.getUnitPack(
					getServletContext(), productId, salesSummary.getOutCount().intValue()
							- salesSummary.getReturnCount().intValue()));

			salesSummary.setCode(objs[7].toString());// 商品编码

			salesSummary.setProductId(new Integer(objs[8].toString()));// 商品id

			// 退货金额
			double rttao = 0.0d;
			if (objs[6] != null) {
				String rttaoStr = objs[6].toString();
				if (StringUtils.isNotBlank(rttaoStr)) {
					rttao = DoubleFormatUtil.format(new Double(rttaoStr));
				}
			}
			salesSummary.setReturnAmount(rttao);

			rsList.add(salesSummary);
		}
		page.setData(rsList);
		restorePageData(page);

		// 得到检索条件
		Map<String, Object> paramSum = this.getQuerySumDistributor();
		List list = getManager().query(paramSum.get("sql").toString(),
				((List) paramSum.get("param")).toArray());
		String count = "";
		String amount = "";
		String hanod = "";
		String rttao = "";
		if (list.size() > 0) {
			for (Object o : list) {
				Object[] oo = (Object[]) o;
				if (oo[0] != null) {
					count = oo[0].toString();
				}
				if (oo[1] != null) {
					amount = oo[1].toString();
				}
				if (oo[2] != null) {
					hanod = oo[2].toString();
				}
				if (oo[3] != null) {
					rttao = oo[3].toString();
				}
			}
		}
		getRequest().setAttribute("count", count);
		getRequest().setAttribute("amount", amount);
		getRequest().setAttribute("hanod", hanod);
		getRequest().setAttribute("rttao", rttao);
		return "indexDistributor";
	}

	private Map<String, Object> getQuerySumDistributor() {
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(
				"select sum(sd.count),sum(sd.amount),sum(sd.hanod),sum(sd.rttao) from SalesDetail sd where sd.sales.status = "
						+ SalesConstants.ORDERS
						+ " and sd.sales.redRed = "
						+ SalesConstants.NORMAL);

		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		if (distributorId != null && distributorId.intValue() > 0) {
			User userDistributor = userManager.get(distributorId);
			if (userDistributor.getSuperior() != null) {
				sql.append(" and sd.sales.user.id = ? ");
				args.add(userDistributor.getId());
			}
		} else if (user.getSuperior() != null
				&& user.getType().equals(AmolUserConstants.AGENT)) {
			sql.append(" and sd.sales.user.type = ? and sd.sales.user.id = ?");
			args.add(AmolUserConstants.AGENT);
			args.add(user.getId());
		} /*
			 * else if (user.getSuperior() == null ||
			 * user.getType().equals(AmolUserConstants.EMPLOYEE)) {
			 * sql.append(" and sd.sales.user.type = ? and sd.sales.user.superior.id = ?"
			 * ); args.add(AmolUserConstants.AGENT); args.add(user.getId()); }
			 */else {
			sql.append(" and sd.id = -1");
		}

		if (StringUtils.isNotBlank(productName)) {
			sql.append(" and sd.products.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(productName));
		}

		if (StringUtils.isNotBlank(productCode)) {
			sql.append(" and sd.products.code like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(productCode));
		}

		String prosortIdStr = getRequest().getParameter("prosortId");
		if (StringUtils.isNotBlank(prosortIdStr)) {
			sql.append(" and sd.products.prosort.serialNo like ? ");
			ProductSort productSort = productSortManager
					.get(new Integer(prosortIdStr));
			args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and sd.sales.createTime >= ? ");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and sd.sales.createTime <= ? ");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		result.put("sql", sql);
		result.put("param", args);

		return result;
	}

	/**
	 * 查询条件和查询参数
	 * 
	 * @return
	 */
	private Map<String, Object> getQueryDistributor() {
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(// sd.outPrice,sd.units.id
				"select sd.products.name,sd.products.stardard,sd.products.units.name,sum(sd.count),sum(sd.amount),sum(sd.hanod),sum(sd.rttao),sd.products.code,sd.products.id from SalesDetail sd where sd.sales.status = "
						+ SalesConstants.ORDERS
						+ " and sd.sales.redRed = "
						+ SalesConstants.NORMAL);

		List args = new ArrayList();

		User user = UserUtil.getPrincipal(getRequest());
		if (distributorId != null && distributorId.intValue() > 0) {
			User userDistributor = userManager.get(distributorId);
			if (userDistributor.getSuperior() != null) {
				sql.append(" and sd.sales.user.id = ? ");
				args.add(userDistributor.getId());
			}
		} else if (user.getSuperior() != null
				&& user.getType().equals(AmolUserConstants.AGENT)) {
			sql.append(" and sd.sales.user.type = ? and sd.sales.user.id = ?");
			args.add(AmolUserConstants.AGENT);
			args.add(user.getId());
		} /*else if (user.getSuperior() != null
				&& user.getType().equals(AmolUserConstants.EMPLOYEE)) {
			sql.append(" and sd.sales.user.type = ? and sd.sales.user.superior.id = ?");
			args.add(AmolUserConstants.AGENT);
			args.add(user.getId());gh
		} */else {
			sql.append(" and sd.id = -1");
		}

		if (StringUtils.isNotBlank(productName)) {
			sql.append(" and sd.products.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(productName));
		}

		if (StringUtils.isNotBlank(productCode)) {
			sql.append(" and sd.products.code like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(productCode));
		}

		String prosortIdStr = getRequest().getParameter("prosortId");
		if (StringUtils.isNotBlank(prosortIdStr)) {
			sql.append(" and sd.products.prosort.serialNo like ? ");
			ProductSort productSort = productSortManager
					.get(new Integer(prosortIdStr));
			args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and sd.sales.createTime >= ? ");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and sd.sales.createTime <= ? ");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		sql.append(" group by sd.products.id order by sd.id desc");
		result.put("sql", sql);
		result.put("param", args);

		return result;
	}
	/**
	 * 销售汇总（按商品类别）
	 */
	@Override
	public String index() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		//根据地区查询
		if(null != regionId && 0 != regionId){
			region = regionManager.get(regionId);
			regionName = region.getName();
		}
		// 得到检索条件
		Map<String,Object> param = this.getQuery();
		page = getManager().pageQuery(page, param.get("sql").toString(),
				((List) param.get("param")).toArray());
		List<SalesSummary> rsList = new ArrayList<SalesSummary>();
		List listSQL = page.getData();
		for (Object o : listSQL) {
			Object[] objs = (Object[]) o; // o对象是Object数组
			SalesSummary salesSummary = new SalesSummary();
			salesSummary.setProductName(objs[0].toString());// 商品名称
			salesSummary.setStardard(objs[1].toString());// 商品规格
			salesSummary.setUnitName(objs[2].toString());// 单位名称
			salesSummary.setOutCount(new Integer(objs[3].toString()));// 出库数量
			// 出库数量包装单位
//			Integer productId = new Integer(objs[8].toString());
//			salesSummary.setOutUnitPack(StockConstants.getUnitPack(
//					getServletContext(), productId, salesSummary.getOutCount()));
			salesSummary.setOutAmount(new Double(objs[4].toString()));// 出库金额

			// 退货数量
			Integer returnCount = new Integer(objs[5].toString());
			salesSummary.setReturnCount(returnCount);

			// 退货数量包装单位
//			salesSummary.setReturnUnitPack(StockConstants.getUnitPack(
//					getServletContext(), productId, salesSummary.getReturnCount()
//							.intValue()));

			// 最终出库总数量包装单位
			//salesSummary.setAllUnitPack(StockConstants.getUnitPack(
			//		getServletContext(), productId, salesSummary.getOutCount().intValue()
			//				- salesSummary.getReturnCount().intValue()));

			salesSummary.setCode(objs[7].toString());// 商品编码

			// 退货金额
			double rttao = 0.0d;
			if (objs[6] != null) {
				String rttaoStr = objs[6].toString();
				if (StringUtils.isNotBlank(rttaoStr)) {
					rttao = DoubleFormatUtil.format(new Double(rttaoStr));
				}
			}
			salesSummary.setReturnAmount(rttao);

		  // 县佣金
			if(null != objs[9]){
				Float distributionCommission = new Float(objs[9].toString());
			  salesSummary.setDistributionCommission(distributionCommission);
			}
			
			// 村佣金
			if(null != objs[10]){
				Float villageDistributionCommission = new Float(objs[10].toString());
			  salesSummary.setVillageDistributionCommission(villageDistributionCommission);
			}
			
			// 成本
			if(null != objs[11]){
				Float inprice = new Float(objs[11].toString());
			  salesSummary.setInprice(inprice);
			}
			rsList.add(salesSummary);
		}
		page.setData(rsList);
		restorePageData(page);
		// 得到检索条件
		Map<String, Object> paramSum = this.getQuerySum();
		List list = getManager().query(paramSum.get("sql").toString(),
				((List) paramSum.get("param")).toArray());
		String count = "";//销售商品数量
		String amount = "";//销售额
		String vdc = "";//县级分销商佣金
		String dc = "";//村点佣金
		String hanod = "";//退货数量
		String rttao = "";//退货总金额
		String inprice = "";//成本
		if (list.size() > 0) {
			for (Object o : list) {
				Object[] oo = (Object[]) o;
				if (oo[0] != null) {
					count = oo[0].toString();
				}
				if (oo[1] != null) {
					amount = oo[1].toString();
				}
				
				if (oo[2] != null) {
					vdc = oo[2].toString();
				}
				if (oo[3] != null) {
					dc = oo[3].toString();
				}
				
				if (oo[4] != null) {
					hanod = oo[4].toString();
				}
				if (oo[5] != null) {
					rttao = oo[5].toString();
				}
				if (oo[6] != null) {
					inprice = oo[6].toString();
				}
			}
		}
		getRequest().setAttribute("count", count);
		getRequest().setAttribute("amount", amount);
		getRequest().setAttribute("vdc", vdc);
		getRequest().setAttribute("dc", dc);
		getRequest().setAttribute("hanod", hanod);
		getRequest().setAttribute("rttao", rttao);
		getRequest().setAttribute("inprice", inprice);
		return INDEX;
	}
	@Override
	public String myindex() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		//根据地区查询
		if(null != regionId && 0 != regionId){
			region = regionManager.get(regionId);
			regionName = region.getName();
		}
		// 得到检索条件
		Integer belong = null;
		boolean type = false;
		Map<String,Object> param = this.myGetQuery();
		page = getManager().pageQuery(page, param.get("sql").toString(),
				((List) param.get("param")).toArray());
		List<SalesSummary> rsList = new ArrayList<SalesSummary>();
		List listSQL = page.getData();
		for (Object o : listSQL) {
			Object[] objs = (Object[]) o; // o对象是Object数组
			SalesSummary salesSummary = new SalesSummary();
			salesSummary.setProductName(objs[0].toString());// 商品名称
			salesSummary.setStardard(objs[1].toString());// 商品规格
			salesSummary.setUnitName(objs[2].toString());// 单位名称
			salesSummary.setOutCount(new Integer(objs[3].toString()));// 出库数量

			// 出库数量包装单位
//			Integer productId = new Integer(objs[8].toString());
//			salesSummary.setOutUnitPack(StockConstants.getUnitPack(
//					getServletContext(), productId, salesSummary.getOutCount()));
			salesSummary.setOutAmount(new Double(objs[4].toString()));// 出库金额
			

			// 退货数量
			Integer returnCount = new Integer(objs[5].toString());
			salesSummary.setReturnCount(returnCount);
			Integer ktNum=new Integer(objs[12].toString());
			salesSummary.setKtNum(ktNum);
			// 退货数量包装单位
//			salesSummary.setReturnUnitPack(StockConstants.getUnitPack(
//					getServletContext(), productId, salesSummary.getReturnCount()
//							.intValue()));

			// 最终出库总数量包装单位
			//salesSummary.setAllUnitPack(StockConstants.getUnitPack(
			//		getServletContext(), productId, salesSummary.getOutCount().intValue()
			//				- salesSummary.getReturnCount().intValue()));

			if(null!=objs[7]){
			salesSummary.setCode(objs[7].toString());// 商品编码
			}
			// 退货金额
			double rttao = 0.0d;
			if (objs[6] != null) {
				String rttaoStr = objs[6].toString();
				if (StringUtils.isNotBlank(rttaoStr)) {
					rttao = DoubleFormatUtil.format(new Double(rttaoStr));
				}
			}
			salesSummary.setReturnAmount(rttao);
		  // 县佣金
			if(null != objs[9]){
				Float distributionCommission = new Float(objs[9].toString());
			  salesSummary.setDistributionCommission(distributionCommission);
			}
			// 村佣金
			if(null != objs[10]){
				Float villageDistributionCommission = new Float(objs[10].toString());
			  salesSummary.setVillageDistributionCommission(villageDistributionCommission);
			}
			// 成本
			if(null != objs[11]){
				Float inprice = new Float(objs[11].toString());
			  salesSummary.setInprice(inprice);
			}
			if(null != objs[13]){
				belong=Integer.parseInt(String.valueOf(objs[13]));
				salesSummary.setBelong(belong);
			}
			if(null != objs[14]){
				type=Boolean.valueOf(String.valueOf(objs[14]));
				salesSummary.setType(type);
			}
			if(null != objs[15]){
				String imageUrl=String.valueOf(String.valueOf(objs[15]));
				salesSummary.setImageUrl(imageUrl);
			}
			rsList.add(salesSummary);
		}
		page.setData(rsList);
		restorePageData(page);
		// 得到检索条件
		Map<String, Object> paramSum = this.getQuerySum();
		List list = getManager().query(paramSum.get("sql").toString(),
				((List) paramSum.get("param")).toArray());
		String count = "";//销售商品数量
		String amount = "";//销售额
		String vdc = "";//县级分销商佣金
		String dc = "";//村点佣金
		String hanod = "";//退货数量
		String rttao = "";//退货总金额
		String inprice = "";//成本
		if (list.size() > 0) {
			for (Object o : list) {
				Object[] oo = (Object[]) o;
				if (oo[0] != null) {
					count = oo[0].toString();
				}
				if (oo[1] != null) {
					amount = oo[1].toString();
				}
				
				if (oo[2] != null) {
					vdc = oo[2].toString();
				}
				if (oo[3] != null) {
					dc = oo[3].toString();
				}
				
				if (oo[4] != null) {
					hanod = oo[4].toString();
				}
				if (oo[5] != null) {
					rttao = oo[5].toString();
				}
				if (oo[6] != null) {
					inprice = oo[6].toString();
				}
			}
		}
		getRequest().setAttribute("count", count);
		getRequest().setAttribute("amount", amount);
		getRequest().setAttribute("vdc", vdc);
		getRequest().setAttribute("dc", dc);
		getRequest().setAttribute("hanod", hanod);
		getRequest().setAttribute("rttao", rttao);
		getRequest().setAttribute("inprice", inprice);
		getRequest().setAttribute("belong", belong);
		getRequest().setAttribute("type", type);
		return "myindex";
	}
 
	/**
	 * 查询销售商品总数量，总金额，县级分销商利润，村点利润
	 * 退货商品总数量，总退货金额
	 * 取7个字段
	 * sum(sd.products.distributionCommission*sd.count-sd.products.distributionCommission*sd.hanod),sum(sd.products.villageDistributionCommission*sd.count-sd.products.villageDistributionCommission*sd.hanod)
			
			sum(sd.products.distributionCommission*sd.count-sd.products.distributionCommission*sd.hanod),sum(sd.products.villageDistributionCommission*sd.count-sd.products.villageDistributionCommission*sd.hanod)
	 		sum(sd.products.distributionCommission*sd.count-sd.products.distributionCommission*sd.hanod),sum(sd.products.villageDistributionCommission*sd.count-sd.products.villageDistributionCommission*sd.hanod)
	 * @return
	 */
	private Map<String, Object> getQuerySum() {
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(
				"select sum(sd.count),sum(sd.amount)+sum(rttao), sum(sd.distributionCommission*sd.count-sd.distributionCommission*sd.hanod),sum(sd.villageDistributionCommission*sd.count-sd.villageDistributionCommission*sd.hanod)   ,sum(sd.hanod),sum(sd.rttao),sum(sd.products.inprice*(sd.count-sd.hanod)) from SalesDetail sd where  "
						+ " sd.sales.status = " + SalesConstants.ORDERS
						+ " and sd.sales.redRed = "
						+ SalesConstants.NORMAL
						+ " and sd.products.productType = false"
						+ " and sd.sales.payState = true");

		List args = new ArrayList();

//		// 如果是顶级经销商，他可以查看自己以及自己下属的分销商的所有销售信息情况
//		User user = UserUtil.getPrincipal(getRequest());
//		if (user.getSuperior() == null) {
//			Integer superid = user.getId();
//			if (superid != null && superid.intValue() > 0) {
//				sql.append(" and (sd.sales.user.superior.id = ? or sd.sales.user.id = ?) ");
//				args.add(superid);
//				args.add(superid);
//			}
//		} /*else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
//			Integer superid = user.getSuperior().getId();
//			if (superid != null && superid.intValue() > 0) {
//				sql.append(" and (sd.sales.user.superior.id = ? or sd.sales.user.id = ?) ");
//				args.add(superid);
//				args.add(superid);
//			}
//		} */else/* if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT))*/{// 如何是分销商，只能查看自己的销售信息情况
//			sql.append(" and sd.sales.user.id = ? ");
//			args.add(user.getId());
//		}
		
	//根据地区查询
    if(null != regionId && 0 != regionId){
    	sql.append(" and sd.sales.user.region.code like ?");
    	args.add(MatchMode.START.toMatchString(region.getCode()));
    }
    //支付方式
    if(StringUtils.isNotBlank(payment)){
    	sql.append(" and sd.sales.payment = ? ");
			args.add(Payment.valueOf(payment));
    }

		if (null != distributorId) {
			sql.append(" and sd.products.user.id = ? ");
			args.add(distributorId);
		}
		
		if (StringUtils.isNotBlank(productName)) {
			sql.append(" and sd.products.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(productName));
		}

		if (StringUtils.isNotBlank(productCode)) {
			sql.append(" and sd.products.code like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(productCode));
		}

		String prosortIdStr = getRequest().getParameter("prosortId");
		if (StringUtils.isNotBlank(prosortIdStr) && !"0".equals(prosortIdStr)) {
			sql.append(" and sd.products.prosort.serialNo like ? ");
			ProductSort productSort = productSortManager
					.get(new Integer(prosortIdStr));
			args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
		}

		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and sd.sales.createTime >= ? ");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and sd.sales.createTime <= ? ");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		result.put("sql", sql);
		result.put("param", args);

		return result;
	}
	/**
	 * 查询条件和查询参数
	 * 取11个字段
	 * @return
	 * sum(sd.products.distributionCommission)*(sd.count-sd.hanod),sum(sd.products.villageDistributionCommission)
	 * sum(sd.products.distributionCommission)*(sd.count-sd.hanod),sum(sd.products.villageDistributionCommission)
	 */
	private Map<String,Object> getQuery() {
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		String regionCode=user.getRegion().getCode();
		Map<String, Object> result = new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer(// sd.outPrice,sd.units.id
				"select sd.products.name,sd.products.stardard,sd.products.units.name,sum(sd.count),sum(sd.amount)+sum(sd.rttao),sum(sd.hanod),sum(sd.rttao),sd.products.code,sd.products.id,sd.distributionCommission*sum(sd.tnorootl),sd.villageDistributionCommission*sum(sd.tnorootl),sd.products.inprice*sum(sd.tnorootl) from SalesDetail sd  where "
//				"select sd.products.name,sd.products.stardard,sd.products.units.name,sum(sd.count),sum(sd.amount),sum(sd.hanod),sum(sd.rttao),sd.products.code,sd.products.id from SalesDetail sd where "
				+ " sd.sales.status = " + SalesConstants.ORDERS
				+ " and sd.products.belonging = " + ProductConstants.PLATFORM
						+ " and sd.sales.redRed = "
						+ SalesConstants.NORMAL
						+ " and sd.products.productType = false and sd.sales.merUser.region.code like '%"+regionCode+"%'"
						+ " and sd.sales.payState = true ");
		if(user.getType().equals("agent")){
			sql.append(" and sd.sales.user.fxsjb='"+user.getFxsjb()+"' ");
		}
		List args = new ArrayList();

//		// 如果是顶级经销商，他可以查看自己以及自己下属的分销商的所有销售信息情况
//		User user = UserUtil.getPrincipal(getRequest());
//		if (user.getSuperior() == null) {
//			Integer superid = user.getId();
//			if (superid != null && superid.intValue() > 0) {
//				sql.append(" and (sd.sales.user.superior.id = ? or sd.sales.user.id = ?) ");
//				args.add(superid);
//				args.add(superid);
//			}
//		} /*else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
//			Integer superid = user.getSuperior().getId();
//			if (superid != null && superid.intValue() > 0) {
//				sql.append(" and (sd.sales.user.superior.id = ? or sd.sales.user.id = ?) ");
//				args.add(superid);
//				args.add(superid);
//			}
//		} */else/* if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT))*/{// 如何是分销商，只能查看自己的销售信息情况
//			sql.append(" and sd.sales.user.id = ? ");
//			args.add(user.getId());
//		}
		
		//根据地区查询
    if(null != regionId && 0 != regionId){
    	sql.append(" and sd.sales.user.region.code like ?");
    	args.add(MatchMode.START.toMatchString(region.getCode()));
    }
    //支付方式
    if(StringUtils.isNotBlank(payment)){
    	sql.append(" and sd.sales.payment = ? ");
			args.add(Payment.valueOf(payment));
    }
		
		if (null != distributorId) {
			sql.append(" and sd.products.user.id = ? ");
			args.add(distributorId);
		}

		if (StringUtils.isNotBlank(productName)) {
			sql.append(" and sd.products.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(productName));
		}

		if (StringUtils.isNotBlank(productCode)) {
			sql.append(" and sd.products.code like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(productCode));
		}

		String prosortIdStr = getRequest().getParameter("prosortId");
		if (StringUtils.isNotBlank(prosortIdStr) && !"0".equals(prosortIdStr)) {
			sql.append(" and sd.products.prosort.serialNo like ? ");
			ProductSort productSort = productSortManager
					.get(new Integer(prosortIdStr));
			args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
		}
		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and sd.sales.createTime >= ? ");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and sd.sales.createTime <= ? ");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		sql.append(" group by sd.products.id order by sd.id desc");
		result.put("sql", sql);
		result.put("param", args);
		return result;
	}
	private Map<String,Object> myGetQuery() {
		User user = userManager.get(UserUtil.getPrincipal(getRequest()).getId());
		Map<String, Object> result = new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer(// sd.outPrice,sd.units.id
				"select sd.products.name,sd.products.stardard,sd.products.units.name,sum(sd.count),sum(sd.amount),sum(sd.hanod),sum(sd.rttao),sd.products.code,sd.products.id,sum(sd.products.distributionCommission)*(sd.count-sd.hanod),sum(sd.products.villageDistributionCommission)*(sd.count-sd.hanod),sum(sd.products.inprice)*(sd.count-sd.hanod),sum(sd.tnorootl),sd.products.belonging,sd.products.productType,sd.products.imageURL from SalesDetail sd where "
//				"select sd.products.name,sd.products.stardard,sd.products.units.name,sum(sd.count),sum(sd.amount),sum(sd.hanod),sum(sd.rttao),sd.products.code,sd.products.id from SalesDetail sd where "
				+ " sd.sales.status = " + SalesConstants.ORDERS
			//	+ " and sd.products.belonging = " + ProductConstants.PLATFORM
						+ " and sd.products.productType='0' and sd.sales.redRed = "
						+ SalesConstants.NORMAL
						+ " and sd.sales.payState = true");
		if(user.getType().equals("agent")){
			sql.append(" and sd.sales.user.fxsjb='"+user.getFxsjb()+"' ");
		}
		if(null!=user.getRegion()){
		sql.append(" and sd.sales.user.region.code like '%"+user.getRegion().getCode()+"%' ");
		}
		List args = new ArrayList();
//		// 如果是顶级经销商，他可以查看自己以及自己下属的分销商的所有销售信息情况
//		User user = UserUtil.getPrincipal(getRequest());
//		if (user.getSuperior() == null) {
//			Integer superid = user.getId();
//			if (superid != null && superid.intValue() > 0) {
//				sql.append(" and (sd.sales.user.superior.id = ? or sd.sales.user.id = ?) ");
//				args.add(superid);
//				args.add(superid);
//			}
//		} /*else if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
//			Integer superid = user.getSuperior().getId();
//			if (superid != null && superid.intValue() > 0) {
//				sql.append(" and (sd.sales.user.superior.id = ? or sd.sales.user.id = ?) ");
//				args.add(superid);
//				args.add(superid);
//			}
//		} */else/* if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT))*/{// 如何是分销商，只能查看自己的销售信息情况
//			sql.append(" and sd.sales.user.id = ? ");
//			args.add(user.getId());
//		}
		//根据地区查询
    if(null != regionId && 0 != regionId){
    	sql.append(" and sd.sales.user.region.code like ?");
    	args.add(MatchMode.START.toMatchString(region.getCode()));
    }
    if(StringUtils.isNotBlank(type)){
    	if(type.equals("1")){sql.append(" and sd.products.belonging=0 ");}
    	else{
    		sql.append(" and sd.products.belonging=1 ");
    	}
    }
    //支付方式
    if(StringUtils.isNotBlank(payment)){
    	sql.append(" and sd.sales.payment = ? ");
			args.add(Payment.valueOf(payment));
    }
		
		if (null != distributorId) {
			sql.append(" and sd.products.user.id = ? ");
			args.add(distributorId);
		}

		if (StringUtils.isNotBlank(productName)) {
			sql.append(" and sd.products.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(productName));
		}

		if (StringUtils.isNotBlank(productCode)) {
			sql.append(" and sd.products.code like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(productCode));
		}

		String prosortIdStr = getRequest().getParameter("prosortId");
		if (StringUtils.isNotBlank(prosortIdStr) && !"0".equals(prosortIdStr)) {
			sql.append(" and sd.products.prosort.serialNo like ? ");
			ProductSort productSort = productSortManager
					.get(new Integer(prosortIdStr));
			args.add(MatchMode.START.toMatchString(productSort.getSerialNo()));
		}
		try {
			if (StringUtils.isNotBlank(startDate)) {
				sql.append(" and sd.sales.createTime >= ? ");
				args.add(DateUtil.firstSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", startDate)));
			}
			if (StringUtils.isNotBlank(endDate)) {
				sql.append(" and sd.sales.createTime <= ? ");
				args.add(DateUtil.lastSecondOfDate(DateUtil.convertStringToDate(
						"yyyy-MM-dd", endDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sql.append(" group by sd.products.id order by sd.id desc");
		result.put("sql", sql);
		result.put("param", args);

		return result;
	}
	/**
	 * 支付状态Map
	 * 
	 * @author 王会璞
	 */
	public Map<String, String> getPaymentMap() {
		return SalesConstants.PAYMENT_MAP;
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

	public Integer getProsortId() {
		return prosortId;
	}

	public void setProsortId(Integer prosortId) {
		this.prosortId = prosortId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public Integer getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Integer distributorId) {
		this.distributorId = distributorId;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}