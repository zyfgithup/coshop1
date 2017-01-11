package com.systop.amol.sales.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.sales.model.Barcode;
import com.systop.core.service.BaseGenericsManager;

/**
 * 商品条形码Service
 * 
 * @author 王会璞
 */
@Service
public class BarcodeManager extends BaseGenericsManager<Barcode> {

	/**
	 * 根据商品条形码删除商品条形码记录
	 * 
	 * @param barcode
	 */
	@Transactional
	public void delete(Barcode barcode) {
		List<Barcode> barcodeSqlList = this.query("from Barcode b where b.barcode=?",new Object[] { barcode.getBarcode().trim() });
		if (barcodeSqlList != null && barcodeSqlList.size() != 0) {
			remove(barcodeSqlList.get(0));
		}
		/*if (barcodeSqlList != null && barcodeSqlList.size() != 0) {
			if(userId.intValue() == barcodeSqlList.get(0).getSalesDetail().getSales().getUser().getId()){
				remove(barcodeSqlList.get(0));
			}else{
				throw new ApplicationException("所退货品必须是本经销商销售出去的商品！商品条形码为：【"+barcodeSqlList.get(0).getBarcode()+"】不是本经销商销售的商品！");
			}
		}*/
	}

	/**
	 * @author 王会璞 
	 * <p>
	 * 保存商品条形码
	 * </p>
	 */
	@Transactional
	@Override
	public void save(Barcode barcode) {
		if(StringUtils.isNotBlank(barcode.getBarcode())){
			super.save(barcode);
		}
	}
	
	/**
	 * 得到商品记录对应的条形码信息
	 * @param salesDetailId  商品记录id
	 * @return
	 */
	public List<Barcode> getBarcodeList(Integer salesDetailId){
		return this.query("from Barcode b where b.salesDetail.id=?",
				new Object[] {salesDetailId });
	}
}
