package com.systop.amol.stock.webapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.storage.service.StorageManager;
import com.systop.amol.stock.StockConstants;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.model.StockCheck;
import com.systop.amol.stock.model.StockCheckDetail;
import com.systop.amol.stock.service.StockCheckDetailManager;
import com.systop.amol.stock.service.StockCheckLPManager;
import com.systop.amol.stock.service.StockCheckManager;
import com.systop.amol.stock.service.StockManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 库存盘点详单action
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StockCheckDetailAction extends
		DefaultCrudAction<StockCheckDetail, StockCheckDetailManager> {

	// 仓库管理Manager
	@Autowired
	private StockCheckManager stockCheckManager;

	// 仓库管理Manager
	@Autowired
	private StorageManager storageManager;

	// 商品名称
	private String productName;

	// 商品编码
	private String productCode;	
	
	// 商品类型
	private ProductSort productSort;

	// 异步请求，返回值
	private Map<String, Object> result;

	@Autowired
	private StockCheckLPManager stockCheckLPManager;

	@Autowired
	private StockManager stockManager;

	/**
	 * 查询库存盘点明细信息
	 */
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前登录用户为空");
		// 得到当前盘点单
		if (getModel().getStockCheck() != null
				&& getModel().getStockCheck().getId() != null) {
			StockCheck stockCheck = getManager().getDao().get(StockCheck.class,
					getModel().getStockCheck().getId());
			getModel().setStockCheck(stockCheck);
		}

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer(
				"from StockCheckDetail s where s.user.id = ? ");
		List args = new ArrayList();
		args.add(user.getId());
		if (getModel().getStockCheck() != null
				&& getModel().getStockCheck().getId() != null) {
			sql.append(" and s.stockCheck.id = ? ");
			args.add(getModel().getStockCheck().getId());
		}

		if (getModel().getStockCheck() != null
				&& getModel().getStockCheck().getStorage() != null
				&& getModel().getStockCheck().getStorage().getId() != null) { // 选定仓库ID
			sql.append(" and s.stock.storage.id = ? ");
			args.add(getModel().getStockCheck().getStorage().getId());
		}

		if(StringUtils.isNotBlank(productName)){
			String[] names = productName.trim().split(",");
			//拼接查询字符串【商品名称OR商品名称】
			sql.append(" and ( ");
			for(int i = 0; i < names.length; i++){
				if(i == names.length - 1){
					sql.append("s.stock.products.name like '%").append(names[i]).append("%'");
					continue;
				}
				sql.append("s.stock.products.name like '%").append(names[i]).append("%' or ");	
			}
			sql.append(" )");
		}
		
		//商品编码
//		if (StringUtils.isNotBlank(productCode)) {
//			String[] codes = productCode.trim().split(",");
//			//拼接查询字符串【商品编码OR商品编码】
//			sql.append(" and ( ");
//			for(int i = 0; i < codes.length; i++){
//				if(i == codes.length - 1){
//					sql.append("s.stock.products.code = ").append(codes[i]);
//					continue;
//				}
//				sql.append("s.stock.products.code = ").append(codes[i]).append(" or ");	
//			}
//			sql.append(" )");
//		}

		// 产品类型
		if (productSort != null && productSort.getId() != null
				&& productSort.getId() != 0) {
			sql.append(" and s.stock.products.prosort.id = ? ");
			args.add(productSort.getId());
			// 查询商品类型，获取类型名称
			productSort = getManager().getDao().get(productSort.getClass(),
					productSort.getId());
		}
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);

		return INDEX;
	}

	/**
	 * 新建或者编辑库存盘点明细信息
	 */
	@Override
	public String edit() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前登录用户为空");
		Assert.notNull(getModel());

		if (getModel().getStockCheck() != null
				&& getModel().getStockCheck().getId() != null) {
			// 查看盘点明细
			index();
		} else {

			StockCheck stockCheck = new StockCheck();
			// 设置库存盘点单流水号
			String order = StockConstants.CHECKORDER_PD + "-"
					+ stockCheckManager.getOrder(new Date(), user);
			stockCheck.setCheckNo(order);
			// 设置产生单据日期
			stockCheck.setCreateTime(DateUtil.getCurrentDate());
			// 设置当前用户为录入员(操作员)
			stockCheck.setUser(user);
			getModel().setUser(user);
			getModel().setStockCheck(stockCheck);
		}

		return INPUT;
	}

	@Override
	public String view() {

		return this.index();
	}

	/**
	 * 异步删除一条盘点详单记录
	 * @return
	 */
	public String removeStockCheckDetail(){
		result = new HashMap<String, Object>();
		super.remove();	
		result.put("success", true);
		return "jsonSuccess";
	}
	
	/**
	 * 异步调用,生成库存盘点数据
	 * 
	 * @return
	 */
	public String generateStockCheckDetail() {
		result = new HashMap<String, Object>();
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前登录用户为空");
		Assert.notNull(getModel());
		getModel().setUser(user);
		List args = new ArrayList();
		StringBuffer hql = new StringBuffer(
				"from Stock s where s.storage.creator.id = ?");
		// 根据当前登录用户查询仓库
		// 判断是否为职员
		if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			args.add(user.getSuperior().getId());	
		}else{
			args.add(user.getId());
		}
		
		
		//商品编码
		if (StringUtils.isNotBlank(productCode)) {
			String[] codes = productCode.trim().split(",");
			//拼接查询字符串【商品编码OR商品编码】
			hql.append(" and ( ");
			for(int i = 0; i < codes.length; i++){
				if(i == codes.length - 1){
					hql.append("s.products.code = '").append(codes[i]).append("'");
					continue;
				}
				hql.append("s.products.code = '").append(codes[i]).append("' or ");	
			}
			
			hql.append(" )");
		}
		
		if(productSort != null && productSort.getId() != null && productSort.getId() != 0){
			hql.append(" and s.products.prosort.id = ? ");
			args.add(productSort.getId());
		}
		
		if (getModel().getStockCheck() != null
				&& getModel().getStockCheck().getStorage() != null
				&& getModel().getStockCheck().getStorage().getId() != null) { // 选定仓库ID
			hql.append(" and s.storage.id = ? ");
			args.add(getModel().getStockCheck().getStorage().getId());
		} else { // 否则将不得到数据
			hql.append(" and s.storage.id = 0 ");
		}
		List<Stock> stocks = getManager().getDao().query(hql.toString(),
				args.toArray());
		if(stocks != null && stocks.size() > 0){
			// 生成库存盘点数据
			getManager().generateStockCheckDetail(getModel(), stocks);
			// 返回消息
			result.put("success", true);
			result.put("stockCheckId", getModel().getStockCheck().getId());
		}else{
			result.put("success", false);
			result.put("msg", "没有要盘点的商品");
		}
		return "jsonSuccess";
	}

	/**
	 * 	 异步调用,追加库存盘点详单数据
	 * 
	 * @return
	 */
	public String appGenerateSCD(){
		result = new HashMap<String, Object>();
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前登录用户为空");
		Assert.notNull(getModel());
		getModel().setUser(user);
		List args = new ArrayList();
		StringBuffer hql = new StringBuffer(
				"from Stock s where s.storage.creator.id = ?");
		// 根据当前登录用户查询仓库
		// 判断是否为职员
		if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			args.add(user.getSuperior().getId());	
		}else{
			args.add(user.getId());
		}

		//商品编码
		if (StringUtils.isNotBlank(productCode)) {
			String[] codes = productCode.trim().split(",");
			//拼接查询字符串【商品编码OR商品编码】
			hql.append(" and ( ");
			for(int i = 0; i < codes.length; i++){
				if(i == codes.length - 1){
					hql.append("s.products.code = '").append(codes[i]).append("'");
					continue;
				}
				hql.append("s.products.code = '").append(codes[i]).append("' or ");	
			}
			hql.append(" )");
		}
		
		if(productSort != null && productSort.getId() != null && productSort.getId() != 0){
			hql.append(" and s.products.prosort.id = ? ");
			args.add(productSort.getId());
		}
		
		if (getModel().getStockCheck() != null
				&& getModel().getStockCheck().getStorage() != null
				&& getModel().getStockCheck().getStorage().getId() != null) { // 选定仓库ID
			hql.append(" and s.storage.id = ? ");
			args.add(getModel().getStockCheck().getStorage().getId());
		} else { // 否则将不得到数据
			hql.append(" and s.storage.id = 0 ");
		}
		
		List<Stock> stocks = getManager().getDao().query(hql.toString(),
				args.toArray());
		if(stocks != null && stocks.size() > 0){
			if(getModel().getStockCheck() == null || getModel().getStockCheck().getId() == null){
				result.put("success", false);
				result.put("msg", "该盘点单不存在");	
			}
			// 生成库存盘点数据
			getManager().appGenerateSCD(getModel(), stocks);
			// 返回消息
			result.put("success", true);
			result.put("stockCheckId", getModel().getStockCheck().getId());
		}else{
			result.put("success", false);
			result.put("msg", "没有要盘点的商品");
		}
		return "jsonSuccess";
	}
	
	/**
	 * 异步调用，修改库存盘点明细
	 * 
	 * @return
	 */
	public String updateStockCheckDetail() {
		result = new HashMap<String, Object>();
		this.update();
		result.put("success", true);
		result.put("id", getModel().getId());
		// result.put("stockCheckId", getModel().getStockCheck().getId());
		return "jsonSuccess";
	}

	/**
	 * 异步调用，库存清算前返回消息
	 * 
	 * @return
	 */
	public String getStockLiquidateInfo() {
		result = new HashMap<String, Object>();

		List<StockCheckDetail> listStockIsNull = new ArrayList<StockCheckDetail>();
		List<StockCheckDetail> listStockCheckLoss = new ArrayList<StockCheckDetail>();
		List<StockCheckDetail> listStockCheckProfit = new ArrayList<StockCheckDetail>();
		if (getModel() != null && getModel().getStockCheck() != null
				&& getModel().getStockCheck().getId() != null) {
			Integer scID = getModel().getStockCheck().getId();
			// 得到没有盘点信息
			listStockIsNull = getManager().getStockCheckIsNull(scID);
			// 得到盘点亏损信息
			listStockCheckLoss = getManager().getStockCheckLoss(scID);
			// 得到盘点盈利信息
			listStockCheckProfit = getManager().getStockCheckProfit(scID);
		}
		if (listStockIsNull.size() > 0) {
			List<Map> list = new ArrayList<Map>();
			for (StockCheckDetail s : listStockIsNull) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", s.getId());
				map.put("name", s.getStock().getProducts().getName());
				list.add(map);
			}
			result.put("stockIsNull", list);
		}
		if (listStockCheckLoss.size() > 0) {
			List<Map> list = new ArrayList<Map>();
			for (StockCheckDetail s : listStockCheckLoss) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", s.getId());
				map.put("name", s.getStock().getProducts().getName());
				list.add(map);
			}
			result.put("stockCheckDeficit", list);
		}
		if (listStockCheckProfit.size() > 0) {
			List<Map> list = new ArrayList<Map>();
			for (StockCheckDetail s : listStockCheckProfit) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", s.getId());
				map.put("name", s.getStock().getProducts().getName());
				list.add(map);
			}
			result.put("stockCheckProfit", list);
		}
		return "jsonStockCheckInfoSuccess";
	}

	/**
	 * 异步调用，库存清算
	 * 
	 * @return
	 */
	public String stockLiquidate() {
		// 异步返回消息
		result = new HashMap<String, Object>();
		List<StockCheckDetail> listStockIsNull = new ArrayList<StockCheckDetail>();
		List<StockCheckDetail> listStockCheckLoss = new ArrayList<StockCheckDetail>();
		List<StockCheckDetail> listStockCheckProfit = new ArrayList<StockCheckDetail>();
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前登录用户为空");
		Assert.notNull(getModel());
		Integer scID = 0;
		if (getModel() != null && getModel().getStockCheck() != null
				&& getModel().getStockCheck().getId() != null) {
			// model中得到库存盘点详单编号
			scID = getModel().getStockCheck().getId();
		} else {
			result.put("success", false);
			result.put("msg", "未找到该单据,请与管理员联系!");
			return "jsonSuccess";
		}
		getModel().setUser(user);

		// 得到没有盘点信息
		listStockIsNull = getManager().getStockCheckIsNull(scID);
		// 修改库存盘点明细中没有盘点到的记录
		getManager().updateStockIsNull(listStockIsNull);

		// 得到盘点亏损信息
		listStockCheckLoss = getManager().getStockCheckLoss(scID);
		// 得到盘点盈利信息
		listStockCheckProfit = getManager().getStockCheckProfit(scID);

		// 生成报亏(损)单号
		String lossNo = StockConstants.LPORDER_PK + "-"
				+ stockCheckLPManager.getOrderByLossAndProfit(new Date(), user);
		// 生成报亏(损)单
		stockCheckLPManager.saveStockCheckLossAndProfit(listStockCheckLoss,
				lossNo, "0");

		// 生成报(盘)盈单号
		String profitNo = StockConstants.LPORDER_PY + "-"
				+ stockCheckLPManager.getOrderByLossAndProfit(new Date(), user);

		// 生成报盈单
		stockCheckLPManager.saveStockCheckLossAndProfit(listStockCheckProfit,
				profitNo, "1");

		// 实现平仓
		stockManager.stockUnwinding(listStockCheckLoss); // 亏损平仓
		stockManager.stockUnwinding(listStockCheckProfit); // 盈利平仓

		// 改变库存盘点状态:清算状态
		StockCheck stockCheck = getManager().getDao().get(StockCheck.class,
				getModel().getStockCheck().getId());
		stockCheckManager.stockCheckLiquidateStatus(stockCheck);

		// 如果有亏损单，就返回亏损单编号
		if (listStockCheckLoss.size() > 0) {
			result.put("lossNo", lossNo);
		}
		// 如果有盈利单，就返回盈利单编号
		if (listStockCheckProfit.size() > 0) {
			result.put("profitNo", profitNo);
		}
		result.put("success", true);
		return "jsonSuccess";
	}

	/**
	 * 仓库的下拉列表
	 */
	public Map getStorageMap() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			// 得到所有状态为可用的仓库
			return storageManager.getStorageMapRun(user);
		}
		return null;
	}

	public ProductSort getProductSort() {
		return productSort;
	}

	public void setProductSort(ProductSort productSort) {
		this.productSort = productSort;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}
