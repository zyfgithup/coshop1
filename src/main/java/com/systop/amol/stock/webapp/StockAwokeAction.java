package com.systop.amol.stock.webapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.systop.amol.base.storage.service.StorageManager;
import com.systop.amol.base.storage.service.StorageSecondManager;
import com.systop.amol.stock.StockConstants;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.service.StockManager;
import com.systop.amol.stock.model.StockAwokePojo;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 库存上下限提醒action
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StockAwokeAction extends DefaultCrudAction<Stock, StockManager> {
	@Autowired
	private StorageManager storageManager;
	@Autowired
	private StorageSecondManager storageSecondManager;
	
	// 仓库ID
	private Integer conditionId;
	
	/**
	 * 经销商级别（1:经销商2：分销商）
	 */
	private String conditionSuperiorSign;
	
	/**
	 * 商品名称
	 */
	private String conditionName;
	
	/**
	 * 商品编码
	 */
	private String conditionCode;
	
	/**
	 * 仓库MAP
	 */
	private Map storageMap;
	
	// 上下限标识
	private String awokeSign;

	// 异步返回值
	Map<String, Object> result;

	@Override
	public String index() {
		Page page = PageUtil.getPage(getPageNo(), getPageSize());

		User u = UserUtil.getPrincipal(getRequest());
		Assert.notNull(u, "用户不允许为空");

		// 得到检索条件
		Map<String, Object> param = getStockAwokePage(u);

		page = getManager().pageQuery(page, param.get("sql").toString(),
				((List) param.get("param")).toArray());

		// 从page中得到数据，数据中存储的是Object数组
		List<StockAwokePojo> list = page.getData();
		// 创建List集合保存POJO数据
		List spList = new ArrayList<StockAwokePojo>();
		for (Object o : list) {
			StockAwokePojo sp = new StockAwokePojo();
			Object[] objs = (Object[]) o; // o对象是Object数组
			sp.setStorageName(objs[0].toString());
			sp.setProductId(new Integer(objs[1].toString()));
			sp.setProductCode(objs[2].toString());
			sp.setProductName(objs[3].toString());
			sp.setProductStardard(objs[4].toString());
			sp.setUnitName(objs[5].toString());
			sp.setProductMaxCount(new Integer(objs[6].toString()));
			sp.setProductMinCount(new Integer(objs[7].toString()));
			sp.setFactCount(new Integer(objs[8].toString()));
			//得到包装单位
			sp.setUnitPack(StockConstants.getUnitPack(getServletContext(), sp.getProductId(), sp.getFactCount()));
			spList.add(sp);
		}
		// 从新向PAGE中保存数据
		page.setData(spList);

		restorePageData(page);
		return INDEX;
	}

	public Integer getConditionId() {
		return conditionId;
	}

	public void setConditionId(Integer conditionId) {
		this.conditionId = conditionId;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	/**
	 * 显示仓库
	 * 
	 * @return
	 */
	public Map getStorageMap() {
		return storageMap;
	}

	public void setStorageMap(Map storageMap) {
		this.storageMap = storageMap;
	}

	/**
	 * 根据用户得到仓库报警检索条件Map中的key(sql和param)
	 */
	private Map<String, Object> getStockAwokePage(User user) {
		// Page page = PageUtil.getPage(getPageNo(), getPageSize());
		if(user != null && user.getId() != null){
			user = getManager().getDao().get(User.class, user.getId());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(
				"select s.storage.name as storageName," // 得到仓库名称
						+ "s.products.id as productId," // 得到商品ID
						+ "s.products.code as productCode," // 得到商品编码
						+ "s.products.name as productName," // 得到商品名称
						+ "s.products.stardard as productStardard, " // 得到商品规格
						+ "s.products.units.name as unitName, " // 得到（商品）单位名称
						+ "s.products.maxCount as productMaxCount," // 得到上限
						+ "s.products.minCount as productMinCount," // 得到下限
						+ "s.count as fact " // 得到实际库存
						+ "from Stock s " // 查询实际库存
						+ "where 1=1 ");
		// (s.count > s.products.maxCount or s.count < s.products.minCount)
		List args = new ArrayList();

		// 查询所有分销商仓库
		if(conditionSuperiorSign != null && !conditionSuperiorSign.equals("") && conditionSuperiorSign.equals("2")){
			sql.append(" and s.count = 0 ");
			sql.append(" and s.storage.creator in (from User u where u.superior.id = ? )"); 
			// 得到所有状态为可用的分销商仓库
			// 判断是否为职员操作
			if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
				storageMap = storageSecondManager.getSecondStorageMapRun(user.getSuperior());	
			}else{
				storageMap = storageSecondManager.getSecondStorageMapRun(user);	
			}
		}else{		// 查询所有经销商仓库
			// 判断查看的是上限还是下限
			if (awokeSign == null || awokeSign.equals("")) {
				sql.append(" and (s.count >= s.products.maxCount or s.count <= s.products.minCount) ");
			} else if (awokeSign.equals("1")) {
				sql.append(" and s.count >= s.products.maxCount ");
			} else {
				sql.append(" and s.count <= s.products.minCount ");
			}
			sql.append("  and s.storage.creator.id =?");
			// 得到所有状态为可用的经销商仓库
			// 判断是否为职员操作
			if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
				storageMap = storageManager.getStorageMapRun(user.getSuperior());	
			}else{
				storageMap = storageManager.getStorageMapRun(user);	
			}
		}
		// 判断是否为职员操作
		if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
			args.add(user.getSuperior().getId());
		}else{
			args.add(user.getId());	
		}
		
		if (conditionId != null) { // 添加仓库ID条件
			// 判断如果改变经销商级别选项时忽略库
			if(storageMap.get(conditionId) != null && !storageMap.get(conditionId).equals("")){
				sql.append(" and s.storage.id = ? ");
				args.add(conditionId);	
			}
		}

		if (StringUtils.isNotBlank(conditionName)) { // 添加商品名称条件
			sql.append(" and s.products.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(conditionName));
		}
		
		if (StringUtils.isNotBlank(conditionCode)) { // 添加商品编码条件
			sql.append(" and s.products.code like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(conditionCode));
		}
		
		// page = getManager().pageQuery(page, sql.toString(), args.toArray());
		result.put("sql", sql);
		result.put("param", args);
		return result;
	}

	/**
	 * 提示是否仓库报警 异步调用
	 * 
	 * @return
	 */
	public String isStockAwoke() {
		result = new HashMap<String, Object>();
		User u = UserUtil.getPrincipal(getRequest());
		Map<String, Object> param = getStockAwokePage(u);

		List list = getManager().getDao().query(param.get("sql").toString(),
				((List) param.get("param")).toArray());
		if (list != null && list.size() > 0) {
			result.put("success", true); // 库存货物报警
			// result.put("msg", "库存!");
		} else {
			result.put("success", false);
		}
		return "jsonSuccess";
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public String getAwokeSign() {
		return awokeSign;
	}

	public void setAwokeSign(String awokeSign) {
		this.awokeSign = awokeSign;
	}

	public String getConditionSuperiorSign() {
		return conditionSuperiorSign;
	}

	public void setConditionSuperiorSign(String conditionSuperiorSign) {
		this.conditionSuperiorSign = conditionSuperiorSign;
	}

	public String getConditionCode() {
		return conditionCode;
	}

	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}
}
