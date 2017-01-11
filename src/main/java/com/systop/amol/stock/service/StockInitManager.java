package com.systop.amol.stock.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.storage.model.Storage;
import com.systop.amol.base.units.model.Units;
import com.systop.amol.stock.StockConstants;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.model.StockInit;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 库存初期管理
 * 
 * @author songbaojie
 * 
 */
@Service
public class StockInitManager extends BaseGenericsManager<StockInit> {

	@Autowired
	private StockManager stockManager;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 保存或修改库存期初信息
	 * 
	 * @author songbaojie
	 * 
	 * @param StockInit
	 */
	@Transactional
	public void saveOrUpdateStockInit(StockInit stockInit) {
		// 查询期初库存是否存在
		StockInit oldStock = findStockInit(stockInit.getStorage().getId(), stockInit
				.getProducts().getId(), stockInit.getUser().getId(), stockInit.getStatus());
		// 修改
		if (oldStock != null) {
			oldStock.setCount(stockInit.getCount());
			oldStock.setNcount(stockInit.getNcount());
			oldStock.setUnits(stockInit.getUnits());
			oldStock.setStorage(stockInit.getStorage());
			oldStock.setAmount(stockInit.getAmount());
			oldStock.setPrice(stockInit.getPrice());
			oldStock.setCreateTime(stockInit.getCreateTime());
			this.save(oldStock);
		} else {
			this.save(stockInit);
		}
	}

	/**
	 * 保存期初库存信息(多条)
	 * 
	 * @param siIds 期初库存ID列表
	 * @param pIds 商品ID列表
	 * @param unitIds 单位ID列表(值[id,name,count,price])
	 * @param ncounts 单位数量列表
	 * @param counts 单位数量列表
	 * @param prices 单价列表
	 * @param amountss 金额列表
	 * @param status 状态列表 
	 * @param storage 仓库
	 * @param user 用户
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveInitStockAndStock(String[] siIds, String[] pIds, String[] unitIds,String[] ncounts,String[] counts,
			String[] prices, String[] amounts, String[] status, Storage storage, User user) {
		for (int i = 0; i < pIds.length; i++) {
			// 如果商品ID不存在,或者状态是期初数据已完成(1),将进行下次循环
			if (pIds[i] == null || pIds[i].equals("") || status[i].equals("1")) {
				continue;
			}

			Products product = getDao().get(Products.class,
					new Integer(pIds[i]));
			
			String[] us = unitIds[i].split(",");
			Units unit = getDao().get(Units.class,new Integer(us[0]));
			Stock stock = new Stock();
			// 如果期初库存数据存在 			
			if(siIds[i] != null && !siIds[i].equals("")){
				StockInit temp = this.get(new Integer(siIds[i]));
				//将上一次期初还原后在操作
				stock.setCount(new Integer(counts[i]) - temp.getCount());
			}else{
				stock.setCount(new Integer(counts[i]));				
			}
			stock.setProducts(product);
			stock.setStorage(storage);
			stock.setUser(user);
			stockManager.saveOrUpdateStock(stock);

			StockInit stockInit = new StockInit();
			stockInit.setCount(new Integer(counts[i]));
			stockInit.setPrice(parseDouble(prices[i]));
			stockInit.setAmount(parseDouble(amounts[i]));
			stockInit.setNcount(new Float(ncounts[i]));
			stockInit.setProducts(product);
			stockInit.setStorage(storage);
			stockInit.setUser(user);
			stockInit.setStatus(status[i]);
			stockInit.setCreateTime(new Date());
			stockInit.setUnits(unit);
			this.saveOrUpdateStockInit(stockInit);
		}
	}

	/**
	 * 保存期初库存信息
	 * 
	 * @param product
	 * @param count
	 * @param price
	 * @param amount
	 * @param storage
	 * @param status
	 * @param user
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveInitStockAndStock(Products product, Integer count,
			Double price, Double amount, Storage storage,String status,User user) {
		try{
			Stock stock = new Stock();
			
			//查询状态未完成的期初库存是否存在
			StockInit temp = findStockInit(storage.getId(), product.getId(), user.getId(), StockConstants.INIT_UNFINISHED);
			// 如果期初库存数据存在
			if(temp != null){
				//将上一次期初还原后在操作
				stock.setCount(new Integer(count) - temp.getCount());
			}else{
				stock.setCount(new Integer(count));
			}
			stock.setProducts(product);
			stock.setStorage(storage);
			stock.setUser(user);
			stockManager.saveOrUpdateStock(stock);
			
			
			StockInit stockInit = new StockInit();
			stockInit.setCount(new Integer(count));
			stockInit.setPrice(new Double(price));
			stockInit.setAmount(new Double(amount));
			stockInit.setUnits(product.getUnits());
			stockInit.setNcount(new Float(count));
			stockInit.setProducts(product);
			stockInit.setStorage(storage);
			stockInit.setStatus(status);
			stockInit.setUser(user);
			stockInit.setCreateTime(new Date());
			this.saveOrUpdateStockInit(stockInit);
	
		}catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	/**
	 * 根据仓库ID,商品ID，用户ID得到期初库存
	 * 
	 * @param storageId
	 *            仓库id
	 * @param productId
	 *            商品id
	 * @param userId
	 *            用户id
	 * @param status
	 *            期初库存状态
	 * @return
	 */
	public StockInit findStockInit(Integer storageId, Integer productId,
			Integer userId,String status) {
		StockInit stockInit = this
				.findObject(
						"from StockInit s where s.storage.id=? and s.products.id = ? and s.user.id = ? and s.status = ?",
						new Object[] { storageId, productId, userId, status});
		return stockInit;
	}

	/**
	 * 删除期初库存和库存中一条记录
	 * 
	 * @param id 期初库存ID
	 * @param product 商品对象
	 * @param storage 仓库对象
	 * @param user 用户
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void delInitStockAndStock(Integer id, Products product, Storage storage,
			User user) {
		if (product.getId() == null || storage.getId() == null
				|| user.getId() == null) {
			throw new ApplicationException("参数为NULL");
		}
		Stock stock = stockManager.findStock(storage.getId(), product.getId(),
				user.getId());
		StockInit stockInit = this.get(id);
		//stockManager.remove(stock);
		if(stock != null && stockInit != null){
			// 改变即时库存中的商品数量
			stock.setCount(stock.getCount() - stockInit.getCount());	
		}
		// 不做删除操作，做修改操作
		stockManager.update(stock);
		this.remove(stockInit);
	}
	
	/**
	 * 修改所有的期初库存状态为已完成状态 
	 * @param user 用户
	 * @return void
	 */
	public boolean updateAllInitStockFinishedByUser(User user){
		try{
			jdbcTemplate.update("update stock_inits set status = ? where status = ? and user_id = ?"
					,new Object[]{StockConstants.INIT_FINISHED, StockConstants.INIT_UNFINISHED, user.getId()} );	
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 将字符串转换为Float型数据格式
	 * 
	 * @param str
	 * @return
	 */
	public Double parseDouble(String str) {
		Double param = null;
		if (StringUtils.isBlank(str)) {
			return null;
		}
		try {
			if (StringUtils.isNotBlank(str)) {
				str = str.replaceAll(",", "");
				param = Double.valueOf(str);
			}
		} catch (Exception e) {
			return null;
		}
		return param;
	}
}