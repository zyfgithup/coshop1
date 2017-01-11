package com.systop.amol.stock.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.base.units.model.UnitsItem;
import com.systop.amol.base.units.service.UnitsItemManager;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.model.SalesDetail;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.model.StockCheckDetail;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 即时库存管理
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings({ "unchecked"})
@Service
public class StockManager extends BaseGenericsManager<Stock> {

	// 单位换算管理Manager
	@Autowired
	private UnitsItemManager unitsItemManager;
	
	@Autowired
	private UserManager userManager;
	
	/*
	 * 入库保存的时候修改库存是库存=库存+本次入库库存
	 */
	@Transactional
	public void saveIn(Stock stock) {
		String hsql = "from Stock c where "
				+ "  c.products.id=" + stock.getProducts().getId()
				+ " and c.storage.id=" + stock.getStorage().getId();
		// System.out.println(hsql);
		List<Stock> slist = null;
		slist = this.query(hsql);
		// 修改
		if (slist.size() > 0) {
			Stock oldStock = slist.get(0);
			oldStock.setCount(stock.getCount() + oldStock.getCount());
			this.save(oldStock);
		} else {
			this.save(stock);
		}
	}

	/**
	 * 保存或修改库存信息(追加库存数量)
	 * @author songbaojie
	 * 
	 * @param stock
	 */
	@Transactional
	public void saveOrUpdateStock(Stock stock) {
		String hsql = "from Stock c where  c.products.id= ? and c.storage.id= ? ";
		// System.out.println(hsql);
		List<Stock> slist = null;
		slist = this.query(hsql,stock.getProducts().getId(),stock.getStorage().getId());
		// 修改
		if (slist.size() > 0) {
			Stock oldStock = slist.get(0);
			//追加库存
			int count = oldStock.getCount() + stock.getCount();
			//如果追加的数量小于0则按0存储
			if(count < 0){
				count = 0;
			}
			oldStock.setCount(count);
			this.save(oldStock);
		} else {
			this.save(stock);
		}
	}

	/*
	 * 入库单删除的时候修改库存=库存-本次库存
	 */
	@Transactional
	public void delIn(Stock stock) {

		String hsql = "from Stock c where " 
				+ "  c.products.id=" + stock.getProducts().getId()
				+ " and c.storage.id=" + stock.getStorage().getId();
		List<Stock> slist = this.query(hsql); // 修改
		if (slist.size() > 0) {
			Stock oldStock = slist.get(0);
			// if (oldStock.getCount() - stock.getCount() < 0) {
			// oldStock.setCount(0);
			// } else {
			oldStock.setCount(oldStock.getCount() - stock.getCount());
			// }
			this.save(oldStock);
		} else {
			// this.save(stores);
		}
	}

	/**
	 * @author 王会璞
	 * @param storageid
	 *            仓库id
	 * @param productId
	 *            商品id
	 * @param userId
	 *            用户id
	 * @return
	 */
	public Stock findStock(Integer stockId, Integer productId, Integer userId) {
		
		User user = userManager.get(userId);
		if(user.getSuperior() != null && AmolUserConstants.EMPLOYEE.equals(user.getType())){
			userId = user.getSuperior().getId();
		}
			
		return this
		.findObject("from Stock s where s.storage.id=? and s.products.id = ? and s.user.id = ? ",new Object[] { stockId, productId, userId });
	}
	public Stock findStockPurchase(Integer stockId, Integer productId) {
		Stock stock = this
				.findObject(
						"from Stock s where s.storage.id=? and s.products.id = ? ",
						new Object[] { stockId, productId });
		return stock;
	}
	/**
	 * @author 王会璞
	 * @param userId
	 *            用户id
	 * @return
	 */
	public Stock findStock(Integer userId) {
		
		User user = userManager.get(userId);
		if(user.getSuperior() != null && AmolUserConstants.EMPLOYEE.equals(user.getType())){
			userId = user.getSuperior().getId();
		}
		
		return this.findObject("from Stock s where s.user.id = ? ",new Object[] { userId });
	}

	/**
	 * 销售管理出库时减少该库下该商品的库存量
	 * 
	 * @author 王会璞
	 * @param salesDetails
	 * @param id
	 * @throws ApplicationException
	 */
	@Deprecated
	@Transactional
	public int inventoryReduction(List<SalesDetail> salesDetails, Sales sales)
			throws ApplicationException {

		int sumCount = 0;
		for (SalesDetail salesDetail : salesDetails) {
			Stock stock = this.findStock(sales.getStorage().getId(),
					salesDetail.getProducts().getId(), sales.getUser().getId());
			if (stock != null) {
				int sdCount = salesDetail.getCount();
				if (stock.getCount().intValue() - sdCount >= 0) {
					stock.setCount(stock.getCount() - sdCount);
					sumCount += sdCount;
					update(stock);// 更新库存
				} else if (stock.getCount() == 0) {
					throw new ApplicationException("商品【"
							+ stock.getProducts().getName() + "】库存已为0，不能销售出库。");
				} else {
					throw new ApplicationException("商品【"
							+ stock.getProducts().getName() + "】库存为"
							+ stock.getCount() + "，本次该商品出库个数为" + sdCount
							+ "，所以不能销售出库。");
				}
			} else {
				throw new ApplicationException("商品【"
						+ salesDetail.getProducts().getName() + "】仓库中不存在。");
			}
		}
		return sumCount;
	}

	/**
	 * 仓库平仓操作（批量修改库存中的实际库存）
	 * 
	 * @param list
	 *            及时仓存列表
	 * @author songbaojie
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void stockUnwindingByStockList(List<Stock> list) {
		for (Stock s : list) {
			getDao().update(s);
		}
	}

	/**
	 * 仓库平仓操作（批量修改库存中的实际库存）
	 * 
	 * @param list
	 *            仓库盘点详单列表（必须包含即时库存）
	 * @author songbaojie
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void stockUnwinding(List<StockCheckDetail> list) {
		for (StockCheckDetail s : list) {
			Stock stock = s.getStock();
			stock.setCount(s.getCount());
			getDao().update(stock);
		}
	}

	/**
	 * @author 王会璞 退货加库存
	 * @param salesDetails
	 * @param sales
	 */
	@Transactional
	public int canadianStocks(List<SalesDetail> salesDetails, Sales sales) {
		int sumCount = 0;
		for (SalesDetail salesDetail : salesDetails) {
			Stock stock = this.findStock(sales.getStorage().getId(),
					salesDetail.getProducts().getId(), sales.getUser().getId());
			int sdCount = salesDetail.getCount();
			stock.setCount(stock.getCount() + sdCount);
			sumCount += sdCount;
			update(stock);
		}
		return sumCount;
	}

	/**
	 * 根据仓库ID获得即时库存信息
	 * 
	 * @param storageId
	 * @author songbaojie
	 * @return
	 */
	public List<Stock> listStocksByStorageId(Integer storageId) {
		return getDao().query("from Stock s where s.storage.id = ?", storageId);
	}
	
	/**
	 * 根据经销商ID得到所有下级经销商库存和
	 * @param uid
	 * @return
	 */
	public Long getChildSuperiorsCount(Integer uid,Integer pid){
		Long count = 0l;
		List<Object> list = getDao().query("select sum(s.count) from Stock s where s.user.superior.id = ? and s.products.id = ?", uid,pid);
		for (Object objs : list) {
			if(objs != null ){
				count = (Long)objs;				
			}
		}
		return count;
	}
	
	/**
	 * 当前用户即时库存商品进行的计量单位
	 * 如果是分销商，就调用经销商的商品数据
	 * @param userid 当前用户的id
	 * @param userid1 如果当前是分销商，那么用经销商的id，如果是经销商就跟第一个参数一样
	 * @param productid
	 * @return
	 */
	public List<Stock> getUnitsItems(Integer userid,Integer userid1,List<Stock> stocks){
		StringBuffer str = new StringBuffer();
		List<Stock> list = new ArrayList<Stock>();
		try {
			for(Stock stock : stocks){
				List<UnitsItem> items = this.unitsItemManager.getUnitsItemOrderDesc(
						userid, userid1, stock.getProducts().getId());
				int sc = stock.getCount();
				int i = 0;
				for(UnitsItem ui : items){
					i++;
					int count = ui.getCount();
					// 判断是否需要折合
					if(ui.getConversion() != null && ui.getConversion() != 1){
						int scc = sc/count;
						sc = sc - scc*count;
						// 如果折合到最后不为0，就录入信息
						if(scc != 0 || (sc != 0 && i == items.size())){
							str.append(scc).append(ui.getUnits().getName());	
						}						
					}
				}
				stock.setUnitPack(str.toString());
				//清空StringBuffer
				str.delete(0, str.length());
				list.add(stock);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}