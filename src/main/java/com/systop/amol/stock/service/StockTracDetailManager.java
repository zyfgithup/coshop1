package com.systop.amol.stock.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.storage.model.Storage;
import com.systop.amol.base.units.model.Units;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.model.StockTrac;
import com.systop.amol.stock.model.StockTracDetail;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 库存【调拨】明细表管理
 * 
 * @author songbaojie
 * 
 */
@Service
//@SuppressWarnings({ "unchecked", "rawtypes" })
public class StockTracDetailManager extends
		BaseGenericsManager<StockTracDetail> {

	@Autowired
	private StockManager stockManager;

	//
	// /**
	// * 得到用户的【入货仓库】,默认为分销商（如果sign="1"则为顶级经销商的仓库）
	// *
	// * @param user
	// * @param sign
	// * @return
	// */
	// public Map getInStorage(User user, String sign) {
	// if (user == null || user.getId() == null || sign == null) {
	// return null;
	// }
	// List<Storage> list = new ArrayList<Storage>();
	// Map map = new HashMap();
	// if (sign != null && sign.equals("1")) {
	// list = getDao().query("from Storage s where s.creator.id = ? )",
	// user.getId());
	// } else {
	// list = getDao()
	// .query("from Storage s where s.creator in (from User u where u.superior.id = ? )",
	// user.getId());
	// }
	// for (Storage s : list) {
	// map.put(s.getId(), s.getName());
	// }
	// return map;
	// }

	/**
	 * 保存【调拨】(多条数据)
	 * 
	 * @param stockTrac
	 *            调拨详单对象
	 * @param pIds
	 *            商品
	 * @param unitids
	 *            商品单位
	 * @param counts
	 *            调拨基础数量
	 * @param ncounts
	 *            调拨单位数量
	 * @param remarks
	 *            备注
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveStockTracDetail(StockTracDetail stockTracDetail,
			String[] pIds, String[] unitids, String[] counts, String[] ncounts,
			String[] remarks) {
		try {
			StockTrac stockTrac = stockTracDetail.getStockTrac();
			stockTrac.setUser(stockTracDetail.getUser());
			stockTrac.setCreateTime(new Date());
			// 保存库存调拨详单
			getDao().save(stockTrac);

			// 加载出/入库
			Storage outStorage = getDao().get(Storage.class,
					stockTrac.getOutStorage().getId());
			Storage inStorage = getDao().get(Storage.class,
					stockTrac.getInStorage().getId());

			for (int i = 0; i < pIds.length; i++) {
				if (pIds[i] == null || pIds[i].equals("")) {
					continue;
				}
				// 加载商品
				Products product = getDao().get(Products.class,
						new Integer(pIds[i]));
				// 分解单位信息
				String[] us = unitids[i].split(",");
				// 加载单位
				Units unit = getDao().get(Units.class, new Integer(us[0]));

				// 保存库存调拨详单
				StockTracDetail std = new StockTracDetail();
				std.setStockTrac(stockTrac);
				std.setCount(new Integer(counts[i]));
				std.setNcount(new Float(ncounts[i]));
				std.setUnits(unit);
				std.setProduct(product);
				std.setUser(stockTracDetail.getUser());
				std.setSign(stockTracDetail.getSign());
				std.setCreateTime(new Date());
				std.setRemark(remarks[i]);
				getDao().save(std); // 保存库存调拨详单

				// 保存出货库存
				Stock outStock = new Stock();
				outStock.setProducts(product);
				outStock.setCount(new Integer(counts[i]));
				outStock.setStorage(outStorage);
				// 判断是否为职员操作
				if (outStorage.getCreator().getSuperior() != null) {
					outStock.setUser(outStorage.getCreator().getSuperior());
				} else {
					outStock.setUser(outStorage.getCreator());
				}
				stockManager.delIn(outStock);

				// 保存入货库存
				Stock inStock = new Stock();
				inStock.setProducts(product);
				inStock.setCount(new Integer(counts[i]));
				inStock.setStorage(inStorage);
				// 判断是否为职员操作
				if (outStorage.getCreator().getSuperior() != null) {
					inStock.setUser(inStorage.getCreator().getSuperior());
				} else {
					inStock.setUser(inStorage.getCreator());
				}
				stockManager.saveIn(inStock);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	// /**
	// * 根据用户和库存调拨标示(1:调拨2:回调)返回库存调拨明细，如果为NULL则显示该用户全部信息
	// *
	// * @param user
	// * @param sign
	// * @return
	// */
	// public List<StockTracDetail> getStockTracDetailBySign(User user, String
	// sign) {
	// if (user == null || user.getId() == null) {
	// return null;
	// }
	// List<StockTracDetail> list = getDao().query(
	// "from StockTracDetail s where s.user.id = ? and s.sign = ?",
	// user.getId(), sign);
	// return list;
	// }
	//
	// /**
	// * 得到用户库存【回调】详单信息
	// *
	// * @param user
	// * @return
	// */
	// public List<StockTracDetail> getStockTracDetailCallback(User user) {
	// return getStockTracDetailBySign(user, StockConstants.STOCK_CALLBACK);
	// }
	//
	// /**
	// * 得到用户库存【调拨】详单信息
	// *
	// * @param user
	// * @return
	// */
	// public List<StockTracDetail> getStockTracDetailTrac(User user) {
	// return getStockTracDetailBySign(user, StockConstants.STOCK_TRAC);
	// }

}
