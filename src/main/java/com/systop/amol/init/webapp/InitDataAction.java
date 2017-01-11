package com.systop.amol.init.webapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.init.model.InitData;
import com.systop.amol.init.service.InitDataManager;
import com.systop.amol.purchase.service.PayInitManager;
import com.systop.amol.sales.service.ReceiveInitManager;
import com.systop.amol.stock.service.StockInitManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.Constants;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.JsonCrudAction;

/**
 * 系统管理员跟据选择的经销商清除其下的所有相关的农资数据
 * 
 * @author ShangHua
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InitDataAction extends JsonCrudAction<InitData, InitDataManager> {

	/**
	 * 计量单位管理
	 */
	@Autowired
	private UserManager userManager;
	@Autowired
	private PayInitManager payInitManager;
	@Autowired
	private StockInitManager stockInitManager;
	
	@Resource
	private ReceiveInitManager receiveInitManager;
	
	//JSON数据
	private Map<String, String> jsonResult;
	
	//经销商ID
	private Integer agentId;
	
	//经销商名称
	private String agentName;

	/**
	 * 查询经销商以及其下所属二级的经销商的数据库各个表中的数据记录数
	 */
	@Override
	public String index() {
		List tablesList = new ArrayList();
		//System.out.println("经销商的ID:" + agentId);
		if (agentId != null) {
			Page page = PageUtil.getPage(getPageNo(), getPageSize());
			
			//客户表
			InitData customer = new InitData();
			customer.setTableName("客户表");
			customer.setCounts(getManager().getOneLevelCountsByCreator("Customer", "owner.id",agentId));
			tablesList.add(customer);
			
			//经销商表
			InitData agent = new InitData();
			agent.setTableName("分销商表");
			agent.setCounts(getManager().getCountsByCreator("User", "superior.id",agentId,AmolUserConstants.AGENT));
			tablesList.add(agent);
			
			//供应商表
			InitData supplier = new InitData();
			supplier.setTableName("供应商表");
			supplier.setCounts(getManager().getOneLevelCounts("Supplier", "",agentId));
			tablesList.add(supplier);
			
			// 商品类型
			InitData productSort = new InitData();
			productSort.setTableName("商品类型表");
			productSort.setCounts(getManager().getOneLevelCountsByCreator("ProductSort", "creator.id",agentId));
			tablesList.add(productSort);
			
			//计量单位换算
			InitData unititem = new InitData();
			unititem.setTableName("商品计量单位换算");
			unititem.setCounts(getManager().getOneLevelCounts("UnitsItem","units.", agentId));
			tablesList.add(unititem);
			//计量单位
			InitData units = new InitData();
			units.setTableName("商品计量单位");
			units.setCounts(getManager().getOneLevelCounts("Units","", agentId));
			tablesList.add(units);
			// 商品
			InitData product = new InitData();
			product.setTableName("商品资料表");
			product.setCounts(getManager().getOneLevelCounts("Products","", agentId));
			tablesList.add(product);
			
			// 仓库
			InitData storage = new InitData();
			storage.setTableName("仓库表");
			storage.setCounts(getManager().getOneLevelCountsByCreator("Storage", "creator.id",agentId));
			tablesList.add(storage);
			
			//部门员工表
			InitData employee = new InitData();
			employee.setTableName("员工表");
			employee.setCounts(getManager().getCountsByCreator("User","superior.id", agentId,AmolUserConstants.EMPLOYEE));
			tablesList.add(employee);
			
			//部门类别表
			InitData EmpDept = new InitData();
			EmpDept.setTableName("员工部门表");
			EmpDept.setCounts(getManager().getOneLevelCounts("Dept","", agentId));
			tablesList.add(EmpDept);
			
			//采购数据删除开始
			//订单详单
			InitData purchaseDetail = new InitData();
			purchaseDetail.setTableName("采购详单数据");
			purchaseDetail.setCounts(getManager().getTwoLevelCounts("PurchaseDetail", "purchase.",agentId));
			tablesList.add(purchaseDetail);
			
			//订单
			InitData purchase = new InitData();
			purchase.setTableName("采购数据");
			purchase.setCounts(getManager().getTwoLevelCounts("Purchase", "",agentId));
			tablesList.add(purchase);
			
			//应付详单
			InitData payDetail = new InitData();
			payDetail.setTableName("应付详单");
			payDetail.setCounts(getManager().getTwoLevelCounts("PayDetail", "pay.",agentId));
			tablesList.add(payDetail);
			
			//应付单
			InitData pay = new InitData();
			pay.setTableName("应付账款");
			pay.setCounts(getManager().getTwoLevelCounts("Pay", "",agentId));
			tablesList.add(pay);
				
			//及时应付
			InitData payable = new InitData();
			payable.setTableName("及时应付");
			payable.setCounts(getManager().getOneLevelCounts("PayAble", "",agentId));
			tablesList.add(payable);			
			//采购数据结束
			
			//商品条码
			InitData barcode = new InitData();
			barcode.setTableName("商品条码");
			barcode.setCounts(getManager().getTwoLevelCounts("Barcode", "salesDetail.sales.",agentId));
			tablesList.add(barcode);
			
			//销售明细（订单  出库单  退货单）
			InitData salesDetail = new InitData();
			salesDetail.setTableName("销售明细数据");
			salesDetail.setCounts(getManager().getTwoLevelCounts("SalesDetail", "sales.",agentId));
			tablesList.add(salesDetail);
			
			//回款单详情
			InitData receiveDetail = new InitData();
			receiveDetail.setTableName("回款单明细");
			receiveDetail.setCounts(getManager().getOneLevelCounts("ReceiveDetail", "receive.",agentId));
			tablesList.add(receiveDetail);
			
			//回款单
			InitData receive = new InitData();
			receive.setTableName("回款单");
			receive.setCounts(getManager().getOneLevelCounts("Receive", "",agentId));
			tablesList.add(receive);
			
			//销售订单    销售出库单     销售退货单
			InitData sales = new InitData();
			sales.setTableName("销售数据");
			sales.setCounts(getManager().getTwoLevelCounts("Sales", "",agentId));
			tablesList.add(sales);
		
			//库存盘点
			InitData stockCheck = new InitData();
			stockCheck.setTableName("库存盘点");
			stockCheck.setCounts(getManager().getTwoLevelCounts("StockCheck","", agentId));
			tablesList.add(stockCheck);
			
			//库存盘点详单
			InitData stockCheckDetail = new InitData();
			stockCheckDetail.setTableName("库存盘点详单");
			stockCheckDetail.setCounts(getManager().getTwoLevelCounts("StockCheckDetail","", agentId));
			tablesList.add(stockCheckDetail);
			
			//库存盘点损益单
			InitData StockCheckLP = new InitData();
			StockCheckLP.setTableName("库存盘点损益单");
			StockCheckLP.setCounts(getManager().getTwoLevelCounts("StockCheckLP","", agentId));
			tablesList.add(StockCheckLP);
			
			//库存调拨
			InitData stockTrac = new InitData();
			stockTrac.setTableName("库存调拨");
			stockTrac.setCounts(getManager().getTwoLevelCounts("StockTrac","", agentId));
			tablesList.add(stockTrac);
			
			//库存调拨详单
			InitData stockTracDetail = new InitData();
			stockTracDetail.setTableName("库存调拨详单");
			stockTracDetail.setCounts(getManager().getTwoLevelCounts("StockTracDetail","", agentId));
			tablesList.add(stockTracDetail);
			
			//及时库存
			InitData stock = new InitData();
			stock.setTableName("即时库存");
			stock.setCounts(getManager().getOneLevelCounts("Stock","", agentId));
			tablesList.add(stock);		
			
			//资金类型表
			InitData FundsSort = new InitData();
			FundsSort.setTableName("资金类别表");
			FundsSort.setCounts(getManager().getTwoLevelCounts("FundsSort","", agentId));
			tablesList.add(FundsSort);

			//收支类别表
			InitData CostSort = new InitData();
			CostSort.setTableName("收支类别表");
			CostSort.setCounts(getManager().getTwoLevelCounts("CostSort","", agentId));
			tablesList.add(CostSort);
			
			//费用表
			InitData Cost = new InitData();
			Cost.setTableName("费用表");
			Cost.setCounts(getManager().getTwoLevelCounts("Cost","", agentId));
			tablesList.add(Cost);
			
			//费用明细表
			InitData CostDetail = new InitData();
			CostDetail.setTableName("费用明细表");
			CostDetail.setCounts(getManager().getTwoLevelCounts("CostDetail", "cost.",agentId));
			tablesList.add(CostDetail);
					
			//初始化数据
			//期初应付
			InitData pinit = new InitData();
			pinit.setTableName("期初应付");
			pinit.setCounts(getManager().getOneLevelCounts("PayInit", "",agentId));
			tablesList.add(pinit);
			
			//期初应收单
			InitData receiveInit = new InitData();
			receiveInit.setTableName("期初应收");
			receiveInit.setCounts(getManager().getOneLevelCounts("ReceiveInit", "",agentId));
			tablesList.add(receiveInit);
			
			//库存初期
			InitData stockInit = new InitData();
			stockInit.setTableName("期初库存");
			stockInit.setCounts(getManager().getOneLevelCounts("StockInit","", agentId));
			tablesList.add(stockInit);  	
			
			//封装数据
			page.setData(tablesList);
			restorePageData(page);
		}

		return INDEX;
	}

	/**
	 * 根据选择的经销商初始化数据【范围：此经销商以及其下属的分销商的所有涉及到的业务表的数据】
	 * @return
	 */
	@Transactional
	public String initData() {
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		String agentId = getRequest().getParameter("agentId");
		if (StringUtils.isNotBlank(agentId)) {
				boolean isDelete = getManager().initData(Integer.valueOf(agentId));
				if (isDelete ) {
					User user = userManager.get(Integer.valueOf(agentId));
					user.setBeginningInit(Constants.NO);
					List<User> users = userManager.query("from User u where u.superior.id = ? and u.type = ?", new Object[]{user.getId(),AmolUserConstants.EMPLOYEE});
					for (User employee : users) {
						employee.setBeginningInit(Constants.NO);
						userManager.save(employee,false);
					}
					userManager.save(user, false);
					jsonResult.put("result", "success");
				}else {
					jsonResult.put("result", "error");
				}
		}
		return "jsonResult";
	}

  
	/**
	 * 打开期初设置页面
	 */
	public String edit() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			if (StringUtils.isNotBlank(user.getBeginningInit())) {
				getRequest().setAttribute("beginingInit", user.getBeginningInit());	
			}
      if (StringUtils.isNotBlank(user.getBeginningInit()) && user.getBeginningInit().equals(Constants.YES)) {
  			this.addActionMessage("您已完成期初数据的设置！");
  			return INPUT;
      }
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}
		return super.edit();
	}
	
	/**
	 * 当期初应收、期初应付以及期初库存全部完成数据录入后，可将此数据冻结!
	 * @return
	 */
	@Transactional
	public String initSet() {
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		try {
			User user = UserUtil.getPrincipal(getRequest());
			user.setBeginningInit(Constants.YES);
			List<User> users = userManager.query("from User u where u.superior.id = ? and u.type = ?", new Object[]{user.getId(),AmolUserConstants.EMPLOYEE});
			for (User employee : users) {
				employee.setBeginningInit(Constants.YES);
				userManager.save(employee,false);
			}
			List<User> agents = userManager.query("from User u where u.superior.id = ? and u.type = ?", new Object[]{user.getId(),AmolUserConstants.AGENT});
			for (User agent : agents) {
				agent.setBeginningInit(Constants.YES);
				userManager.save(agent,false);
			}
			userManager.save(user, false);
			//期初应收，修改状态为已完成
			receiveInitManager.updateAllReceiveInit(user);
			//修改期初应付，状态改为1；
			payInitManager.initstock(user);
			//期初库存，修改状态为已完成
			stockInitManager.updateAllInitStockFinishedByUser(user);
		} catch (Exception e) {
			jsonResult.put("result", "error");
		}
		jsonResult.put("result", "success");
		return "jsonResult";
	}
	
	/**
	 * 解锁期初应收、期初应付以及期初库存的数据!
	 * @return
	 */
	@Transactional
	public String initUnlock() {
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		try {
			User user = UserUtil.getPrincipal(getRequest());
			user.setBeginningInit(Constants.NO);
			List<User> users = userManager.query("from User u where u.superior.id = ? and u.type = ?", new Object[]{user.getId(),AmolUserConstants.EMPLOYEE});
			for (User employee : users) {
				employee.setBeginningInit(Constants.NO);
				userManager.save(employee,false);
			}
			List<User> agents = userManager.query("from User u where u.superior.id = ? and u.type = ?", new Object[]{user.getId(),AmolUserConstants.AGENT});
			for (User agent : agents) {
				agent.setBeginningInit(Constants.NO);
				userManager.save(agent,false);
			}
			userManager.save(user, false);
		} catch (Exception e) {
			jsonResult.put("result", "error");
		}
		jsonResult.put("result", "success");
		return "jsonResult";
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(Map<String, String> jsonResult) {
		this.jsonResult = jsonResult;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	
}
