package com.systop.amol.base.product.service;

import com.systop.amol.base.product.ProductConstants;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.units.model.UnitsItem;
import com.systop.amol.base.units.service.UnitsItemManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 计量单位换算管理Manager
 * 
 * @author ShangHaiYan
 * 
 */
@Service
@SuppressWarnings( { "unchecked" })
public class ProductsManager extends BaseGenericsManager<Products> {
	/**
	 * 计量单位管理
	 */
	@Autowired
	private UnitsItemManager unitsItemManager;
	@Transactional
	public void remove(Products products) {
		try {
			// 删除该商品所有的单位换算信息
			if (CollectionUtils.isNotEmpty(products.getUnitsItem())) {
				for (UnitsItem unitsItem : products.getUnitsItem()) {
					unitsItemManager.remove(unitsItem);
				}
			}
			super.remove(products);
		} catch (Exception ex) {
			throw new ApplicationException("删除失败，该商品已经使用，不能删除！");
		}
	}
	/**
	 * 商品上下架业务逻辑
	 */
	public void upOrDownShelf(Integer productId,String type){
		Products products=this.get(productId);
		products.setUpDownGoodshelf(type);
		products.setMaxCount(0);
		this.update(products);
	}
	public List<Products> getByType(String type,String proSorId,String userId){
		if(StringUtils.isNotBlank(proSorId)&& StringUtils.isNotBlank(userId)) {
			return this.getDao().query("from Products p where p.visible='1' and p.upDownGoodshelf = ? and p.prosort.id=? and p.user.id=? order by p.createTime desc", type, Integer.parseInt(proSorId), Integer.parseInt(userId));
		}else{
			return null;
		}
		}
	@Transactional
	public void save(Products products) {
//		if (getDao().exists(products, "code", "user.id")) {
//			throw new ApplicationException("商品编码为【" + products.getCode()
//					+ "】的商品已存在！");
//		}
//		if (products.getBarcode()!=null && !products.getBarcode().equals("") &&  getDao().exists(products, "barcode", "user.id")) {
//			throw new ApplicationException("商品条码为【"+products.getBarcode() +"】的商品已存在！");
//		}
		super.save(products);
	}
	
	/**
	 * 保存商品方法
	 * @param products 修改后的商品或新添加的商品
	 * @param productsClone 历史记录商品信息
	 */
	@Transactional
	public void save(Products products,Products productsClone) {
		if(null != productsClone){
//			getDao().clear();
			super.save(productsClone);
		}
		super.save(products);
//		if (getDao().exists(products, "code", "user.id")) {
//			throw new ApplicationException("商品编码为【" + products.getCode()
//					+ "】的商品已存在！");
//		}
//		if (products.getBarcode()!=null && !products.getBarcode().equals("") &&  getDao().exists(products, "barcode", "user.id")) {
//			throw new ApplicationException("商品条码为【"+products.getBarcode() +"】的商品已存在！");
//		}
//		super.save(products);
	}
	
	@Transactional
	public void saveProductTemplate(Products products) {
//		if (products.getBarcode()!=null && !products.getBarcode().equals("") &&  getDao().exists(products, "barcode", "user.id")) {
//			throw new ApplicationException("商品条码为【"+products.getBarcode() +"】的商品已存在！");
//		}
		super.save(products);
	}

	/**
	 * 保存商品信息时将商品基本单位信息存储到商品换算单位表中,以便换算时使用！
	 * 
	 * @param productId
	 */
	@Transactional
	public void unitsItemSave(Integer productId) {
		Products product = this.get(productId);

		if (product.getUnitsItem().size() > 0) {
			for (UnitsItem unitsItem : product.getUnitsItem()) {
				
				if (unitsItem.getCount() == 1) {
					unitsItem.setUnits(product.getUnits());
					unitsItem.setInprice(product.getInprice());
					unitsItem.setOutprice(product.getOutprice());
					getDao().save(unitsItem);
				} else {
					unitsItem.setInprice(product.getInprice()
							* unitsItem.getCount());
					unitsItem.setOutprice(product.getOutprice()
							* unitsItem.getCount());
					getDao().save(unitsItem);
				}
			}
		} else {
			UnitsItem unitsItem = new UnitsItem();
			unitsItem.setConversion(0);
			unitsItem.setProducts(product);
			unitsItem.setUser(product.getUser());
			unitsItem.setCount(1);
			unitsItem.setUnits(product.getUnits());
			unitsItem.setInprice(product.getInprice());
			unitsItem.setOutprice(product.getOutprice());
			getDao().save(unitsItem);
		}
	}

	/**
	 * @author 王会璞
	 *         <p>
	 *         根据商品编码得到商品
	 *         </p>
	 * @param code
	 *            商品编码
	 * @return 商品对象
	 */
	public Products getProducts(String code) {
		if (StringUtils.isNotBlank(code)) {
			return this.findObject("from Products p where p.code = ?", code);
		}
		return null;
	}
	public Products getProductsById(int id) {
			return this.findObject("from Products p where p.id = ?", id);
	}
	/**
	 * 根据商品名称,生产厂商和当前用户得到商品信息
	 * 
	 * @author songbaojie
	 * @param name
	 * @param supplierName
	 * @param user
	 * @return
	 */
	public Products getProducts(String code , String name,String supplierName, User user) {
		if (StringUtils.isNotBlank(name) && user != null) {
			List<Products> list = this.getDao().query("from Products p where p.code = ? and p.name = ? and p.supplier.name = ? and p.user = ?"
					,code ,name, supplierName, user);
			if(list != null && list.size() > 0){
				return list.get(0);	
			}
		}
		return null;
	}

	/**
	 * 平台商品集合
	 * @return
	 */
	public List<Products> platformProductList() {
		
	return this.getDao().query("from Products p where p.belonging = ? and p.productType = ? ", ProductConstants.PLATFORM, false);
	}
}
