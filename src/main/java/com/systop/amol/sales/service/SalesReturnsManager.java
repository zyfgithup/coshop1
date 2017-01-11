package com.systop.amol.sales.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.MatchMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.card.model.CardGrant;
import com.systop.amol.card.model.CardSpend;
import com.systop.amol.card.service.CardSpendManager;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.Barcode;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.model.SalesDetail;
import com.systop.amol.sales.utils.Payment;
import com.systop.amol.sales.utils.SingleNumber;
import com.systop.amol.stock.service.StockManager;
import com.systop.amol.util.DoubleFormatUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 销售退货Service
 * 
 * @author 王会璞
 */
@SuppressWarnings({"unchecked","deprecation"})
@Service
public class SalesReturnsManager extends BaseGenericsManager<Sales> {

	/**
	 * 仓库库存Manager
	 */
	@Resource
	private StockManager stockManager;

	/**
	 * 代币卡消费记录Manager
	 */
	@Resource
	private CardSpendManager cardSpendManager;
	
	@Resource
	private UserManager userManager;

	/**
	 * 销售详单明细Manager
	 */
	@Resource
	private SalesDetailManager salesDetailManager;

	/**
	 * 商品条形码Manager
	 */
	@Resource
	private BarcodeManager barcodeManager;

	/** 出库单Manager */
	@Resource
	private SalesManager salesManager;

	/**
	 * 生成退货单号
	 * 
	 * @return
	 */
	public String getReturnSales(String billtype, Integer userId) {
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String billNo = SalesConstants.RETURNS_BILL + date;

		String hql = "select max(s.salesNo) from Sales s where s.status =? and s.salesNo like ? and (s.user.id=? or s.user.superior.id=?)";
		User user = userManager.get(userId);
		if(user.getSuperior() != null){
			userId = user.getSuperior().getId();
		}
		String sno = (String) getDao()
				.findObject(
						hql,
						new Object[] { billtype, MatchMode.START.toMatchString(billNo),
								userId , userId});

		return SingleNumber.getSingleNumber().getNumber(billNo, sno);
	}


	/**
	 * 删除销售单
	 * 
	 * @param orderid
	 */
	@Transactional
	public void del(int salesId) {
		List<SalesDetail> detailList = salesDetailManager.query(
				"from SalesDetail s where s.sales.id = ?", salesId);
		for (SalesDetail sd : detailList) {
			salesDetailManager.remove(sd);
		}
	}

	/**
	 * 使退货单冲红
	 * 
	 * @param id
	 * @param userId
	 * @return Sales 退货单
	 */
	@Transactional
	public Sales redRed(Integer id, Integer userId) {

		Sales sales = get(id);
		sales.setRedRed(SalesConstants.RED);
		update(sales);

		// 保存冲红单
		Sales redRed = (Sales) sales.clone();
		redRed.setId(null);
		redRed.setRedRed(SalesConstants.REDBILL);
		redRed.setSalesNo(getReturnSales(SalesConstants.RETURNS, userId));
		redRed.setRemark("冲红退货单号：" + sales.getSalesNo());
		redRed.setSamount(-(DoubleFormatUtil.format(sales.getSamount())));// 应收金额
		redRed.setSpayamount(-(DoubleFormatUtil.format(sales.getSpayamount())));// 实收金额
		redRed.setCashConsumption(-(DoubleFormatUtil.format(sales.getCashConsumption())));// 现金
		redRed.setCardamount(-(DoubleFormatUtil.format(sales.getCardamount())));// 代币卡
		redRed.setRttao(-(DoubleFormatUtil.format(sales.getRttao())));// 退款金额
		if(sales.getTtno() != null){
			redRed.setTtno(-sales.getTtno());//订单中的商品剩余总数量 ，出库单中的商品真实出库总数量
		}
		redRed.setCount(-sales.getCount());//订单中的商品总数量 ，出库单中的商品原始出库总数量，退货单中的退货总数量
		save(redRed);
		
	  //保存冲红单的商品明细
		List<SalesDetail> sdList = salesDetailManager.getDetails(id);
		for (SalesDetail sd : sdList) {
			SalesDetail sdRedBill = (SalesDetail)sd.clone();
			sdRedBill.setId(null);
			sdRedBill.setCount(-sd.getCount());
			sdRedBill.setNcount(-sd.getNcount());
			sdRedBill.setAmount(-(DoubleFormatUtil.format(sd.getAmount())));
			sdRedBill.setSales(redRed);
			salesDetailManager.save(sdRedBill);
		}

		List<SalesDetail> sdlist = salesDetailManager.getDetails(id);
		// 退货单冲红后将库存减去
		if(sales.getStorage() != null){
			stockManager.inventoryReduction(sdlist, sales);
		}
		
		for (SalesDetail sd : sdlist) {
			
		  // 退货冲红后将条形码加上
//			List<Barcode> barcodeList = barcodeManager.getBarcodeList(sd.getId());
//			for(Barcode barcode : barcodeList){
//				barcodeManager.save(barcode);
//			}
			
			// 将出库单商品个数减少，减少数量等于退货单的商品数量
			if (sales.getSingle() != null && sales.getSingle().getId().intValue() > 0) {
				SalesDetail redDetail = salesDetailManager.get(sd.getOORSalesDetailId()
						.getId());
				redDetail.setTnorootl(redDetail.getTnorootl() + sd.getCount());
				redDetail.setHanod(redDetail.getHanod() - sd.getCount());
				redDetail.setRttao(redDetail.getRttao().doubleValue() - sd.getAmount().doubleValue());
				salesDetailManager.update(redDetail);// 更新出库单商品明细
			}
		}

		// 如果是代币卡消费，冲红的时候要将代币卡的消费金额从代币卡中减去
		if (DoubleFormatUtil.format(sales.getCardamount()) != 0.0d) {
			CardGrant cardGrant = sales.getCardGrant();
				if (cardGrant != null) {
					// 卡的余额要大于要从卡中的消费才可允许出库
					if ((DoubleFormatUtil.format(cardGrant.getBalance()) - DoubleFormatUtil.format(sales.getCardamount())) >= 0) {

						// 减少代币卡中的剩余金额
						cardGrant
								.setBalance(DoubleFormatUtil.format(cardGrant.getBalance()) - DoubleFormatUtil.format(sales.getCardamount()));
					  // 增加消费总额
						cardGrant.setSpend(DoubleFormatUtil.format(cardGrant.getSpend()) + DoubleFormatUtil.format(sales.getCardamount()));

						// 保存代币卡消费记录
						CardSpend cardSpend = new CardSpend();
						cardSpend.setSpendDate(new Date());
						cardSpend.setSpendMoney(sales.getCardamount());
						cardSpend.setCardGrant(cardGrant);
						User user = userManager.get(userId);
						cardSpend.setUser(user);
						cardSpendManager.save(cardSpend);

					} else {
						throw new ApplicationException("您卡中的余额不足，须付："
								+ DoubleFormatUtil.format(sales.getCardamount()) + "元，您卡中的余额为：" + DoubleFormatUtil.format(cardGrant.getBalance())
								+ "元。");
					}
				}
		}
		
	  // 修改出库单信息
		if(sales.getSingle() != null && sales.getSingle().getId().intValue() > 0){
			Sales salesCk = salesManager.get(sales.getSingle().getId());
//			salesCk.setTtno(salesCk.getTtno().intValue() + sales.getCount().intValue());
			salesCk.setRttao(DoubleFormatUtil.format(salesCk.getRttao()) - DoubleFormatUtil.format(sales.getRttao()));
			if(salesCk.getTtno().equals(salesCk.getCount())){
				salesCk.setCkzt(SalesConstants.NOT_RETURN);
			}
			salesManager.update(salesCk);
		}
		
		return sales;
	}

	// @Transactional
	// public void remove(Sales sales) {
	// try {
	// // 删除销售单明细
	// del(sales.getId());
	// // 删除销售单
	// super.remove(sales);
	// } catch (Exception e) {
	// throw new ApplicationException("删除失败");
	// }
	// }
}