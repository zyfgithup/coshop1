package com.systop.amol.base.jfproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.base.jfproduct.model.JfPrinceple;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.product.service.ProductsManager;
import com.systop.core.service.BaseGenericsManager;

@Service
public class JfPrincepleManager extends BaseGenericsManager<JfPrinceple>{
	
	@Autowired
	private ProductsManager productsManager;
	 @Transactional
	  public void save(JfPrinceple txMoney) {
	    super.save(txMoney);
	  }
		/**
		 * 通过regioncode查询村级分销点
		 * 一个村只有一个分销点
		 * @param orderNo
		 */
		public JfPrinceple findPrinceple(int userId) {
		return this.findObject("from JfPrinceple o where o.user.id = ?", userId);
		}
		@Transactional
		public void plSaveJfproducts(List<Products> proList){
			for (Products products : proList) {
				productsManager.save(products);
			}
		}

}
