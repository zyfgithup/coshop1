package com.systop.amol.sales.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.MatchMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.product.service.ProductsManager;
import com.systop.amol.card.service.CardGrantManager;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.model.SalesDetail;
import com.systop.amol.sales.utils.Payment;
import com.systop.amol.sales.utils.SingleNumber;
import com.systop.amol.user.agent.model.Fxgz;
import com.systop.amol.util.DoubleFormatUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 销售订单Service
 * 
 * @author 王会璞
 */
@Service
public class SalesOrderManager extends BaseGenericsManager<Sales> {

	/**
	 * 销售订单详单明细Manager
	 */
	@Resource
	private SalesDetailManager salesDetailManager;

	/** 代币卡Manager */
	@Resource
	private CardGrantManager cardGrantManager;

	@Resource
	private UserManager userManager;

	@Resource
	private ProductsManager productsManager;
	
	@Resource
	private SalesOrderManager salesOrderManager;
	
	

	/**
	 * 
	 * @param order 订单
	 * @param user  购物的app用户
	 * @param fxgz  分销规则
	 * @param type  (1订单增加，2退单消减)
	 */
	@Transactional
	public void updateOrderOrUserOrFx(Sales order, User user,Fxgz fxgz, int type) {
		double flMoney=0.0;//直接合伙人返现金额
		double integal=0;//简介合伙人返现金额
		Integer dnIntegal=0;//用户返现积分
		if(null!=fxgz){
			flMoney=fxgz.getFlMoney();
			integal=fxgz.getIntegal();
			dnIntegal=fxgz.getHdIntegal();
		}
		changeFxByorder(user, order, flMoney, integal, dnIntegal, type);//分销
		
	}
	@Transactional
	public void updateOrderAddDetail(Sales sales,List<SalesDetail> sdList){
		this.update(sales);
		for (SalesDetail sd : sdList){
			salesDetailManager.save(sd);
		}
	}
	/**
	 * 修改用户分销信息
	 * @param user 购物的app用户
	 * @param order 订单
	 * @param flMoney 直接合伙人返现金额
	 * @param integal 间接合伙人返现金额
	 * @param dnIntegal 用户返现积分
	 * @param type type(1订单增加，2退单消减)
	 */
	@Transactional
	public void changeFxByorder(User user, Sales order,double flMoney, double integal,Integer dnIntegal,Integer type) {
		
		User zjuser=user.getSuperior();//消费app用户的上级合伙人
		User meuser=order.getMerUser();//商户
		System.out.println("分销==============================修改用户分销信息");
		if(type==1){
			if(null!=user.getIntegral()){
				user.setIntegral(user.getIntegral()+dnIntegal);
			}else{
				user.setIntegral(dnIntegal);
			}
			if(null!=order.getJfNumber()){
				order.setJfNumber(order.getJfNumber()+dnIntegal);
			}else{
				order.setJfNumber(dnIntegal);
			}
			if(zjuser!=null){
				if(null!=user.getZjMoney()){
					user.setZjMoney(user.getZjMoney()+flMoney);
				}else{
					user.setZjMoney(flMoney);
				}
				/**直接合伙人修改总金额*/
				if(null!=zjuser.getAllMoney()){
					zjuser.setAllMoney(zjuser.getAllMoney()+flMoney);
				}else{
					zjuser.setAllMoney(flMoney);
				}
				/**直接合伙人修改总收入*/
				if(null!=zjuser.getIncomeAll()){
					zjuser.setIncomeAll(zjuser.getIncomeAll()+flMoney);
				}else{
					zjuser.setIncomeAll(flMoney);
				}
				User jjuser=zjuser.getSuperior();
				if(jjuser!=null){
					if(null!=user.getJjMoney()){
						user.setJjMoney(user.getJjMoney()+integal);
					}else{
						user.setJjMoney(integal);
					}
					/**间接合伙人修改总金额*/
					if(null!=jjuser.getAllMoney()){
						jjuser.setAllMoney(jjuser.getAllMoney()+integal);
					}else{
						jjuser.setAllMoney(integal);
					}
					/**间接合伙人修改总收入*/
					if(null!=jjuser.getIncomeAll()){
						jjuser.setIncomeAll(jjuser.getIncomeAll()+integal);
					}else{
						jjuser.setIncomeAll(integal);
					}
					userManager.update(jjuser);
					if(null!=order.getJjMoney()){
						order.setJjMoney(order.getJjMoney()+integal);
					}else{
						order.setJjMoney(integal);
					}
					order.setJjUser(jjuser);
				}
				userManager.update(zjuser);
				if(null!=order.getZjMoney()){
					order.setZjMoney(order.getZjMoney()+flMoney);
				}else{
					order.setZjMoney(flMoney);
				}
				order.setZjUser(zjuser);
			}
			//订单中直接合伙人和间接合伙人的收入
			if(null != order.getZjMoney()){//订单金额需要减去返给直接合伙人的钱
				order.setSjMoney(order.getSamount().doubleValue() - order.getZjMoney().doubleValue());
				if(null != order.getJjMoney()){//订单金额需要减去翻个间接合伙人的钱
					order.setSjMoney(order.getSjMoney().doubleValue() - order.getJjMoney().doubleValue());
				}
			}else{
				order.setSjMoney(order.getSamount());
			}
			System.out.println("购买后更新商户余额和收入---------"+meuser.getName());
			System.out.println(meuser.getAllMoney()+"------余额，收入---------"+meuser.getIncomeAll());
			//商家收入
			if(meuser.getAllMoney()!=null && null != order.getSjMoney()){//增加商家的余额
				System.out.println("111111111111111111111111");
				meuser.setAllMoney(meuser.getAllMoney().doubleValue() + order.getSjMoney().doubleValue());
			}else{
				System.out.println("22222222222222222222222");
				meuser.setAllMoney(order.getSjMoney());
			}
			if(meuser.getIncomeAll()!=null && null != order.getSjMoney()){//增加商家的收入
				System.out.println("3333333333333333333333");
				meuser.setIncomeAll(meuser.getIncomeAll().doubleValue() + order.getSjMoney().doubleValue());
			}else{
				System.out.println("444444444444444444");
				meuser.setIncomeAll(order.getSjMoney());
			}
			System.out.println("商户余额---------"+meuser.getAllMoney()+"; 商户收入=========="+meuser.getIncomeAll());
			userManager.update(meuser);//更新商家余额和收入
			System.out.println("商户余额---------"+meuser.getAllMoney()+"; 商户0000收入=========="+meuser.getIncomeAll());
		}
		if(type==2){
			System.out.println("退款--------------------------------->退单减直接、间接合伙人收入、减购物用户积分、见商家余额、总收入");
			user.setIntegral(user.getIntegral()-dnIntegal);//减购物用户积分
			order.setJfNumber(order.getJfNumber()-dnIntegal);//减订单中app用户的通过购物得到的积分
			if(zjuser!=null){
				/** 减去给直接合伙人返的钱 */
				user.setZjMoney(user.getZjMoney()-flMoney);
				/**直接合伙人修改总金额*/
				zjuser.setAllMoney(zjuser.getAllMoney()-flMoney);
				/**直接合伙人修改总收入*/
				zjuser.setIncomeAll(zjuser.getIncomeAll()-flMoney);
				
				//间接合伙人
				User jjuser=zjuser.getSuperior();
				if(jjuser!=null){
					/** 减去给间接合伙人返的钱 */
					user.setJjMoney(user.getJjMoney()-integal);
					/**间接合伙人修改总金额*/
					jjuser.setAllMoney(jjuser.getAllMoney()-integal);
					/**间接合伙人修改总收入*/
					jjuser.setIncomeAll(jjuser.getIncomeAll()-integal);
					userManager.update(jjuser);
					order.setJjMoney(order.getJjMoney()-integal);
					order.setJjUser(jjuser);
				}
				userManager.update(zjuser);
				order.setZjMoney(order.getZjMoney()-flMoney);
				order.setZjUser(zjuser);
			}
			System.out.println("退货后更新商户余额和收入---------"+meuser.getName());
			if(null != meuser.getAllMoney() && null != order.getSjMoney()){
				meuser.setAllMoney(meuser.getAllMoney().doubleValue() - order.getSjMoney().doubleValue());
			}
			if(null != meuser.getIncomeAll() && null != order.getSjMoney()){
				meuser.setIncomeAll(meuser.getIncomeAll().doubleValue() - order.getSjMoney().doubleValue());
			}
			
			userManager.update(meuser);
		}
		System.out.println("dddddddooooooo");
		userManager.update(user);
		salesOrderManager.update(order);
		System.out.println("更新app用户，更新订单-----==============订单id = "+order.getId());
	}

	/**
	 * 删除销售单
	 * 
	 * @param orderid
	 */
	@Transactional
	public void del(int salesId) {
	List<SalesDetail> detailList = salesDetailManager
		.query("from SalesDetail s where s.sales.id = ?", salesId);
	for (SalesDetail sd : detailList) {
		salesDetailManager.remove(sd);
	}
	}
	/**
	 * 保存销售订单(现金)
	 * 
	 * @return
	 */
	@Transactional
	public void save(Sales sales, List<SalesDetail> list) {
	try {
		// 如果是修改就先删除旧的，再保存新的
		if (sales.getId() != null && sales.getId() > 0) {
		del(sales.getId());
		} else {
		// 更新app用户积分
		if(null==sales.getSalesType()||!sales.getSalesType().equals("jfSale")){
			//updateUserIntegral(sales);
			//updateValligeIntegral(sales);
		}
		}
		super.save(sales);
		// 保存销售详单,更新销售数量
		Products products;
		for (SalesDetail sd : list) {
		products = sd.getProducts();
		if (null == products.getSalesVolume()) {
			products.setSalesVolume(sd.getCount());
		} else {
			products.setSalesVolume(products.getSalesVolume().intValue()
				+ sd.getCount().intValue());
		}
		productsManager.update(products);// 更新商品销售量
		/*if(null!=products.getMaxCount()){
			if(products.getMaxCount()-sd.getCount()==0||products.getMaxCount()-sd.getCount()<0){
				productsManager.upOrDownShelf(products.getId(),"0");
			}
		}*/
		salesDetailManager.save(sd);// 保存订单详情
		}
	} catch (Exception e) {
		throw new ApplicationException("保存失败");
	}
	}
	
	/**
	 * 保存销售订单(支付宝)
	 * 
	 * @return
	 */
	@Transactional
	public void saveAlipay(Sales sales, List<SalesDetail> list) {
	try {
		// 如果是修改就先删除旧的，再保存新的
		if (sales.getId() != null && sales.getId() > 0) {
		del(sales.getId());
		} 
		super.save(sales);
		// 保存销售详单,更新销售数量
		Products products;
		for (SalesDetail sd : list) {
		products = sd.getProducts();
		if (null == products.getSalesVolume()) {
			products.setSalesVolume(sd.getCount());
		} else {
			products.setSalesVolume(products.getSalesVolume().intValue()
				+ sd.getCount().intValue());
		}
		if(null!=products.getMaxCount()){
			products.setMaxCount(products.getMaxCount()-sd.getCount().intValue());
		}
		productsManager.update(products);// 更新商品销售量
		if(null!=products.getSalesVolume()&&null!=products.getMaxCount()){
			if(products.getMaxCount()==0||products.getMaxCount()<0){
				productsManager.upOrDownShelf(products.getId(),"0");
			}
		}
		salesDetailManager.save(sd);// 保存订单详情
		}

	} catch (Exception e) {
		throw new ApplicationException("保存失败");
	}
	}
	
	
	
	public void minusJf(){
		
	}
	/**
	 * 更新某个村的总积分数量
	 * 
	 * @param sales
	 */
	public void updateValligeIntegral(Sales sales) {
	User user = sales.getUser();
	//获得村级分销用户对象
	User cjFxUser=userManager.findUser(user.getRegion().getCode());
	if(null!=cjFxUser){
	String spayamountStr = sales.getSpayamount().toString();
	spayamountStr = spayamountStr.substring(0, spayamountStr.indexOf("."));
	if(null==cjFxUser.getIntegral()){
		cjFxUser.setIntegral(Integer.valueOf(spayamountStr));
	}else{
		cjFxUser.setIntegral(cjFxUser.getIntegral()+Integer.valueOf(spayamountStr));
	}
	userManager.update(cjFxUser);}
	}
	/**
	 * 更新用户积分
	 * 
	 * @param sales
	 */
	public void updateUserIntegral(Sales sales) {
	User user = sales.getUser();
	sales.setSpayamount(sales.getSamount());
	String spayamountStr = sales.getSpayamount().toString();
	spayamountStr = spayamountStr.substring(0, spayamountStr.indexOf("."));
	//if (Payment.CASH.equals(sales.getPayment())) {
		if (null == user.getIntegral()) {
		user.setIntegral(Integer.valueOf(spayamountStr));
		} else {
		user.setIntegral(user.getIntegral().intValue()
			+ Integer.valueOf(spayamountStr).intValue());
		}
		userManager.update(user);
	//}
	}

	@Transactional
	public void remove(Sales sales) {
	try {
		// 删除销售单明细
		del(sales.getId());
		// 删除销售单
		super.remove(sales);
	} catch (Exception e) {
		throw new ApplicationException("删除失败");
	}
	}

	/**
	 * 生成单号
	 * 
	 * @param billtype
	 *          订单类型
	 * @param userId
	 * @return
	 */
	public String getOrderNumber(String billtype, Integer userId) {
	String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
	String billNo = SalesConstants.ORDERS_BILL + date;
	// String hql = "select max(s.salesNo) from Sales s where s.status =? and
	// s.salesNo like ? and (s.user.id=? or s.user.superior.id=?)";
	String hql = "select max(s.salesNo) from Sales s where s.status =? and s.salesNo like ?";

	// User user = userManager.get(userId);
	// if(user.getSuperior() != null){
	// userId = user.getSuperior().getId();
	// }
	String sno = (String) getDao().findObject(hql, new Object[] { billtype,
		MatchMode.START.toMatchString(billNo)/*
												 * , userId , userId
												 */ });

	return SingleNumber.getSingleNumber().getNumber(billNo, sno);
	}

	/**
	 * @author 王会璞 订单冲红
	 * @param id
	 *          订单id
	 * @param userId
	 *          用户id
	 */
	@Transactional
	public void redRed(Integer id, Integer userId) {
	Sales sales = get(id);
	if (sales.getCount().equals(sales.getTtno())) {
		// 将订单设为冲红标识
		sales.setRedRed(SalesConstants.RED);
		update(sales);

		// 保存冲红单
		Sales redRed = (Sales) sales.clone();
		redRed.setId(null);
		redRed.setRedRed(SalesConstants.REDBILL);
		redRed.setSalesNo(getOrderNumber(SalesConstants.ORDERS, userId));
		redRed.setRemark("冲红订单号：" + sales.getSalesNo());
		redRed.setSamount(-(DoubleFormatUtil.format(sales.getSamount())));// 应收金额
		redRed.setSpayamount(-(DoubleFormatUtil.format(sales.getSpayamount())));// 实收金额
		redRed.setCashConsumption(
			-(DoubleFormatUtil.format(sales.getCashConsumption())));// 现金
		redRed.setCardamount(-(DoubleFormatUtil.format(sales.getCardamount())));// 代币卡
		redRed.setRttao(-(DoubleFormatUtil.format(sales.getRttao())));// 退款金额
		redRed.setTtno(-sales.getTtno());// 订单中的商品剩余总数量 ，出库单中的商品真实出库总数量
		redRed.setCount(-sales.getCount());// 订单中的商品总数量
											// ，出库单中的商品原始出库总数量，退货单中的退货总数量
		save(redRed);

		// 保存冲红单的商品明细
		List<SalesDetail> sdList = salesDetailManager.getDetails(id);
		for (SalesDetail sd : sdList) {
		SalesDetail sdRedBill = (SalesDetail) sd.clone();
		sdRedBill.setId(null);
		sdRedBill.setCount(-sd.getCount());
		sdRedBill.setNcount(-sd.getNcount());
		sdRedBill.setAmount(-(DoubleFormatUtil.format(sd.getAmount())));
		sdRedBill.setSales(redRed);
		salesDetailManager.save(sdRedBill);
		}
	}
	}

	/**
	 * 修改销售订单以及销售订单商品详情信息的信息记录，使其能够知道自己的出库情况 有仓库的情况下出库
	 * 
	 * @param sales
	 *          出库单
	 * @param sumCount
	 *          出库商品总数量
	 * @param salesDetails
	 *          出库商品明细
	 */
//	@Transactional
//	public void updateOrder(Sales sales, int sumCount,
//		List<SalesDetail> salesDetails) {
//
//	// 销售订单
//	Sales order = get(sales.getSingle().getId());
//	// 本次出库后该笔订单商品的剩余数量
////	int sy = order.getTtno() - sumCount;
//	if (sy >= 0) {
//		if (sy != 0 && sy < order.getCount()) {
//		order.setCkzt(SalesConstants.SALES_PART);// 部分出库
//		}
//		if (sy == 0) {
//		order.setCkzt(SalesConstants.SALES_COMPLETE);// 完全出库
//		}
//		order.setTtno(sy);
//
//		// 更新销售订单
//		update(order);
//
//		// 更新订单明细
//		for (SalesDetail detail : salesDetails) {
//		// 每种商品的出库数量
//		int th = detail.getCount();
//		// 每种商品的出库数量（单位数量）
//		// float nth = detail.getNcount();
//		// 订单商品明细
//		SalesDetail orderSDSQL = salesDetailManager
//			.get(detail.getOORSalesDetailId().getId());
//
//		if (orderSDSQL.getTnorootl() - th >= 0) {
//
//			orderSDSQL.setTnorootl(orderSDSQL.getTnorootl() - th);// 商品的剩余出库数量（订单商品明细）
//			orderSDSQL.setHanod(orderSDSQL.getHanod() + th);// 商品的已出库数量（订单商品明细）
//			// orderSDSQL.setNtnorootl(orderSDSQL.getNtnorootl() - nth);//
//			// 商品的剩余出库数量（订单商品明细）（单位数量）
//			// orderSDSQL.setNhanod(orderSDSQL.getNhanod() + nth);//
//			// 商品的已出库数量（订单商品明细）（单位数量）
//			// detail.setNtnorootl(orderSDSQL.getNhanod());//
//			// 商品的剩余退货数量（出库单商品明细）（单位数量）
//			salesDetailManager.update(orderSDSQL);
//		} else {
//			throw new ApplicationException(
//				"本次出库商品【" + orderSDSQL.getProducts().getName() + "】的数量为"
//					+ th /*
//							 * + detail.getUnits ( ).getName()
//							 */ + "，订单：" + order.getSalesNo() + "  中此商品的可出库数量为"
//					+ orderSDSQL
//						.getTnorootl() /*
//										 * + detail . getUnits ( ) . getName ( )
//										 */
//				+ "，所以不能出库。");
//		}
//		}
//	} else {
//		throw new ApplicationException(
//			"订单：" + order.getSalesNo() + "  本次出库商品总数量为" + sumCount
//				+ "，超出了该订单中商品可出库数量" + order.getTtno() + "，所以不能出库。");
//	}
//	}

	/**
	 * 修改销售订单以及销售订单商品详情信息的信息记录，使其能够知道自己的出库情况 没有仓库的情况下出库
	 * 
	 * @param sales
	 * @param salesDetails
	 */
	public void updateOrder(Sales sales, List<SalesDetail> salesDetails) {
	// sumCount商品数量之和
	int sumCount = 0;
	for (SalesDetail salesDetail : salesDetails) {
		int sdCount = salesDetail.getCount();
		sumCount += sdCount;
	}
//	updateOrder(sales, sumCount, salesDetails);
	}

	/**
	 * <p>
	 * 验证买商品的客户是否属于销售商品的经销商 如果该销售单是由订单产生的验证下订单的客户与刷卡消费客户是否是同一个客户
	 * </P>
	 * 
	 * @param sales
	 *          销售对象
	 */
	@SuppressWarnings("rawtypes")
	public void validate(Sales sales) {
	List cardList = null;
	cardList = cardGrantManager.getCard(
		sales.getCardGrant().getCard().getCardNo(),
		sales.getUser().getId().toString(), null);
	if (cardList != null) {
		Map item = (Map) cardList.get(0);
		StringBuffer sb = new StringBuffer();
		if (!((Boolean) item.get("available"))) {
		throw new ApplicationException(
			sb.append("代币卡状态不正常或持卡客户被禁用！").toString());
		}
		if (!((Boolean) item.get("jxs"))) {
		throw new ApplicationException(
			sb.append("该消费代币卡不属于此销售商品的经销商！").toString());
		}
	}
	}

	/**
	 * 通过订单号查询订单
	 * 
	 * @param orderNo
	 */
	public Sales findOrder(String orderNo) {
	return this.findObject("from Sales o where o.salesNo = ?", orderNo);
	}

	/**
	 * 退货业务逻辑
	 * 
	 * @param sales
	 * @param returnOrder
	 * @param sdlist
	 */
	@Transactional
	public void save(Sales sales, Sales returnOrder, List<SalesDetail> sdlist) {
	System.out.println("111111111111");
//	// 真实出库总数量
//	sales.setTtno(
//		sales.getTtno().intValue() - returnOrder.getCount().intValue());
//	// 订单中应收金额
//	sales.setSamount(sales.getSamount().doubleValue() - returnOrder.getRttao());
	// 订单中实收金额
//	sales.setSpayamount(
//		sales.getSamount().doubleValue() - returnOrder.getRttao());
//	this.update(sales);
	System.out.println("2222222222222");
	// 保存退货单
	this.save(returnOrder);
	System.out.println("3333333333333333");
	List<SalesDetail> sdListDB = salesDetailManager.getDetails(sales.getId());
	// 保存退货单商品明细
	int productIdDB;
	try {
		for (SalesDetail sdDB : sdListDB) {
		productIdDB = sdDB.getProducts().getId().intValue();
		for (SalesDetail sd : sdlist) {
			System.out.println(
				productIdDB + " === " + sd.getProducts().getId().intValue());
			if (productIdDB == sd.getProducts().getId().intValue()) {
			System.out.println("ddddddddlllllll");
			// 剩余出库数量
//			sdDB.setTnorootl(
//				sdDB.getTnorootl().intValue() - sd.getHanod().intValue());
//			// 剩余出库商品金额
//			sdDB.setAmount(
//				sdDB.getAmount().doubleValue() - sd.getAmount().doubleValue());
			// 剩余出库商品
//			sdDB.setCount(
//				sdDB.getCount().intValue() - sd.getHanod().intValue());
//			salesDetailManager.update(sdDB);// 更新商品数量
//			sd.setOORSalesDetailId(sdDB);
			sd.setVillageDistributionCommission(sdDB.getVillageDistributionCommission());
			sd.setDistributionCommission(sdDB.getDistributionCommission());
			sd.setUnits(sdDB.getProducts().getUnits());
			sd.setRttao(sdDB.getRttao());
			sd.setProducts(sdDB.getProducts());
			salesDetailManager.save(sd);
			System.out.println("6666666666666666");
			}
		}
		System.out.println("7777777777777");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	}

}