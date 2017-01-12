package com.systop.amol.sales.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.systop.amol.user.agent.model.FenYongMiddle;
import com.systop.amol.user.agent.model.Fxgz;
import com.systop.amol.user.agent.model.ZhuanZhangRecord;
import com.systop.amol.user.agent.model.ZjLzRecord;
import com.systop.amol.user.agent.service.FenYongMiddleManager;
import com.systop.amol.user.agent.service.FxgzManager;
import com.systop.amol.user.agent.service.ZhuanZhangRecordManager;
import com.systop.amol.user.agent.service.ZjLzRecordManager;
import org.hibernate.criterion.MatchMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.card.model.CardGrant;
import com.systop.amol.card.model.CardSpend;
import com.systop.amol.card.service.CardGrantManager;
import com.systop.amol.card.service.CardSpendManager;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.Barcode;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.model.SalesDetail;
import com.systop.amol.sales.utils.Payment;
import com.systop.amol.sales.utils.SingleNumber;
import com.systop.amol.stock.model.Stock;
import com.systop.amol.stock.service.StockManager;
import com.systop.amol.util.DoubleFormatUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 出库Service
 * 
 * @author 王会璞
 */
@SuppressWarnings({ "unchecked", "deprecation" })
@Service
public class SalesManager extends BaseGenericsManager<Sales> {

	/**
	 * 仓库库存Manager
	 */
	@Resource
	private FenYongMiddleManager fenYongMiddleManager;
	@Resource
	private StockManager stockManager;
	@Resource
	private ZhuanZhangRecordManager zhuanZhangRecordManager;

	/**
	 * 代币卡消费记录Manager
	 */
	@Resource
	private CardSpendManager cardSpendManager;

	/**
	 * 出库单详单明细Manager
	 */
	@Resource
	private SalesDetailManager salesDetailManager;
	
	@Resource
	private UserManager userManager;

	/**
	 * 商品条形码Manager
	 */
	@Resource
	private BarcodeManager barcodeManager;

	/** 销售订单Manager */
	@Resource
	private SalesOrderManager salesOrderManager;

	@Resource
	private CardGrantManager cardGrantManager;

	private JdbcTemplate jdbcTemplate;
	@Resource
	private FxgzManager fxgzManager;
	@Resource
	private ZjLzRecordManager zjLzRecordManager;
	
	/**
	 * 根据userid查询出来该用户当天返现金额集合
	 */
	public Double getFxMoneyOwn(Integer userId) {
		Double fxMoney=0.0;
		List<Sales> sList = this
				.query(" from Sales s where s.user.id="+userId+" and  s.status=0 and  date(s.createTime)=date(curdate())");
		if(null!=sList&&sList.size()>0){
			for (Sales sale : sList) {
				//fxMoney+=sale.getFxMoney();
			}
		}
		return fxMoney;
	}
	//充值返现规则操作
	public void ctrlCzFx(Sales sales,String userId,Double cxMoney,String type){
		ZhuanZhangRecord zr = new ZhuanZhangRecord();
		zr.setCreateTime(new Date());
		Fxgz fxgz = fxgzManager.getByType(type);
		User user = null;
		Double fxMoney = 0.0;
		if(null!=userId){
			//如果是充值就是返现的userId 如果是分用就是收到分拥的userId
			user = userManager.get(Integer.parseInt(userId));
		}
		if(null!=fxgz){
			//0是充值
			if(type.equals("0")) {
				fxMoney = cxMoney * Double.valueOf(fxgz.getFxNum()) / 100;
				if(null!=user.getFxMoney()){
					user.setFxMoney(user.getFxMoney()+fxMoney);
				}else{
					user.setFxMoney(fxMoney);
				}
			}
			//1是分拥
			if(type.equals("1")) {
				User zfxUser = sales.getUser();
				fxMoney = cxMoney * Double.valueOf(fxgz.getFxNum())/100;
				if(null!=zfxUser.getFyMoney()){
					zfxUser.setFyMoney(zfxUser.getFyMoney() + fxMoney);
				}else{
					zfxUser.setFyMoney(fxMoney);
				}
				/*if(null!=user.getIncomeAll()){
					user.setIncomeAll(user.getIncomeAll()+fxMoney);
				}else{
					user.setIncomeAll(fxMoney);
				}*/
				if(null!=user.getFxMoney()){
					user.setFxMoney(user.getFxMoney()+fxMoney);
				}else{
					user.setFxMoney(fxMoney);
				}
				if(null!=user.getTotalXf()){
					user.setTotalXf(user.getTotalXf()+cxMoney);
				}else{
					user.setTotalXf(cxMoney);
				}
				User xfzUser = sales.getUser();
				if(null!=xfzUser.getTotalXf()){
					xfzUser.setTotalXf(xfzUser.getTotalXf()+cxMoney);
				}else{
					xfzUser.setTotalXf(cxMoney);
				}
			}
			if(null!=user.getAllMoney()){
				user.setAllMoney(user.getAllMoney()+fxMoney);
			}else{
				user.setAllMoney(fxMoney);
			}
			zr.setUser(user);
			//交易的钱
			zr.setJyJe(sales.getSpayamount());
			//分拥或者返现的钱
			zr.setZzJe(fxMoney);
			zr.setType(type);
			zr.setSales(sales);
			zhuanZhangRecordManager.save(zr);
			userManager.update(user);
		}
		/*if(type!=null&&type.equals("1")){
			FenYongMiddle fym = new FenYongMiddle();
			fym.setFyBl(Double.valueOf(fxgz.getFxNum())/100);
			fym.setSales(sales);
			fym.setCreateTime(new Date());
			fym.setFyMoney(fxMoney);
			fym.setUser(sales.getUser());
			fym.setFyUser(user);
			fym.setXiaofeiMoney(sales.getSamount());
			fenYongMiddleManager.save(fym);
		}*/
	}

	public static void main(String[] args){
		double fxMoney = 0.02 * Double.valueOf(50) / 100;
		System.out.println(fxMoney);
	}
	/**
	 * 删除出库单
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
	 * 生成出库单号
	 * 
	 * @return
	 */
	public String getSalesNumber(String billtype, Integer userId) {
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String billNo = SalesConstants.SALES_BILL + date;

		StringBuffer sql = new StringBuffer("select max(s.salesNo) from Sales s where s.status =? and s.salesNo like ? and (s.user.id=? or s.user.superior.id=?)");
		User user = userManager.get(userId);
		if(user.getSuperior() != null){
			userId = user.getSuperior().getId();
		}
		
		String sno = (String) getDao()
		.findObject(
				sql.toString(),
				new Object[] { billtype, MatchMode.START.toMatchString(billNo),
						userId, userId});
		
		return SingleNumber.getSingleNumber().getNumber(billNo, sno);
	}

	/**
	 * 如果客户使用代币卡消费，向”发卡管理“栏目中的”消费管理“插入一条代币卡消费记录，同时将代币卡消费的钱从代币卡余额中减去 调用保存销售表 商品明细信息
	 * 保存商品条形码 减库存 的方法
	 * 
	 * @param sales
	 *          销售单实体
	 * @param salesDetails
	 *          商品明细对象List
	 * @param objectList
	 *          条形码
	 */
	@Transactional
	public void saveStorehouse(Sales sales, List<SalesDetail> salesDetails,
			List<Object> objectList) throws ApplicationException {

		// 如果是代币卡消费，出库的时候要将代币卡的消费金额从代币卡中减去
		if (DoubleFormatUtil.format(sales.getCardamount()) != 0.0d) {
			CardGrant cardGrant = sales.getCardGrant();
			if (cardGrant != null) {
				// 卡的余额要大于要从卡中的消费才可允许出库
				if ((DoubleFormatUtil.format(cardGrant.getBalance()) - DoubleFormatUtil
						.format(sales.getCardamount())) >= 0) {

					// 减少代币卡中的剩余金额
					cardGrant.setBalance(cardGrant.getBalance().doubleValue()
							- sales.getCardamount().doubleValue());
					// 增加消费总额
					cardGrant.setSpend(cardGrant.getSpend().doubleValue()
							+ sales.getCardamount().doubleValue());

					// 保存代币卡消费记录
					CardSpend cardSpend = new CardSpend();
					cardSpend.setSpendDate(new Date());
					cardSpend.setSpendMoney(sales.getCardamount());
					cardSpend.setCardGrant(cardGrant);
					User user = userManager.get(sales.getUser().getId());
					cardSpend.setUser(user);
					cardSpendManager.save(cardSpend);

					// 保存
					this.save(sales, salesDetails, objectList);
				} else{
					 throw new
					 ApplicationException("您卡中的余额不足，须付："+sales.getCardamount()+"元，您卡中的余额为："+cardGrant.getBalance()+"元。");
				}
			}
		} else {
			// 保存
			this.save(sales, salesDetails, objectList);
		}
	}
/**
 * 计算app用户15天内从间/直接合伙人共获得的收益
 */
	public double getIncomeInFifteenDays(int userId){
		double allSy=0.0;
		//间接受益
		List<Double> jjList=jdbcTemplate.queryForList("select SUM(jjMoney) from sales where date_sub(curdate(), INTERVAL 15 DAY) <= date(create_time) and jj_user_id=?",new Object[]{userId});
		List<Double> zjList=jdbcTemplate.queryForList("select SUM(zjMoney) from sales where date_sub(curdate(), INTERVAL 15 DAY) <= date(create_time) and zj_user_id=?",new Object[]{userId});
		if(null!=jjList&&jjList.size()>0){
			allSy+=jjList.get(0);
		}
		if(null!=zjList&&zjList.size()>0){
			allSy+=zjList.get(0);
		}
		return allSy;
	}
	/**
	 * 保存销售表 商品明细信息 保存商品条形码 减库存
	 * 
	 * @param sales
	 *          出库单
	 * @param counts
	 *          商品数量集合
	 * @param pids
	 *          商品id集合
	 * @param salesDetails
	 *          商品明细list
	 * @param objectList
	 *          条形码List<List>
	 * @param orderId
	 */
	@Transactional
	private void save(Sales sales, List<SalesDetail> salesDetails,
			List<Object> objectList) throws ApplicationException {
		if (sales.getStorage() != null) {
			if (sales.getStorage().getId() != null
					&& !(sales.getPayment().equals(Payment.CARDADVANCE))) {
				// 减库存
				int sumCount = stockManager.inventoryReduction(salesDetails, sales);
				// 修改销售订单以及销售订单商品详情信息的信息记录，使其能够知道自己的出库情况
				if (sales.getSingle() != null && sales.getSingle().getId() != null) {
//					salesOrderManager.updateOrder(sales, sumCount, salesDetails);
				}
			}
		} else {
			// 修改销售订单以及销售订单商品详情信息的信息记录，使其能够知道自己的出库情况
			if (sales.getSingle() != null && sales.getSingle().getId() != null) {
				salesOrderManager.updateOrder(sales, salesDetails);
			}
		}

		// 保存出库单
		save(sales);

		// 商品明细信息，如果是修改就先删除旧的，再保存新的
		if (sales.getId() != null && sales.getId() > 0) {
			del(sales.getId());
		}
		// 保存出库详单
		for (SalesDetail sd : salesDetails) {
			salesDetailManager.save(sd);
		}

		// 保存商品条形码
		for (Object object : objectList) {
			List<Barcode> barcodes = (List<Barcode>) object;
			for (Barcode barcode : barcodes) {
				barcodeManager.save(barcode);
			}
		}
	}
	
	/**
	 * 通过sales_no查询订单信息
	 * @param orderNo
	 */
	public Sales findSales(String orderNo) {
	return this.findObject("from Sales o where o.status='0' and o.salesNo= ?", orderNo);
	}



	/**
	 * 未平账目 cash
	 * 
	 * @param customerId
	 *          客户（农户）id
	 * @return 给定的用户下的所有未平的出库单
	 */
	public List<Sales> getWpzm(Integer customerId) {
		List<Sales> salesList = this
				.query(
						"from Sales s where s.customer.id = ? and s.status = ? and s.redRed = ? and s.payment in (?, ?) and s.ckzt != ? and s.samount <> s.spayamount order by s.createTime asc, s.salesNo asc",
						customerId, SalesConstants.SALES, SalesConstants.NORMAL,
						Payment.CASH, Payment.CASHADVANCE, SalesConstants.FULL_RETURN);
		return salesList;
	}

	/**
	 * 未平账目 card
	 * 
	 * @param customerId
	 *          客户（农户）id
	 * @return 给定的用户下的所有未平的出库单
	 */
	public List<Sales> getCardWpzm(Integer customerId) {
		List<Sales> salesList = this
				.query(
						"from Sales s where s.customer.id = ? and s.status = ? and s.redRed = ? and s.payment in (?, ?) and s.ckzt != ? and (samount  - rttao - spayamount) > 0 order by s.createTime asc, s.salesNo asc",
						customerId, SalesConstants.SALES, SalesConstants.NORMAL,
						Payment.CARD, Payment.CARDADVANCE, SalesConstants.FULL_RETURN);
		return salesList;
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
		if (sales.getSingle() != null) {
			cardList = cardGrantManager.getCard(sales.getCardGrant().getCard().getCardNo(), sales.getUser().getId().toString(),
					sales.getSingle().getCustomer().getId().toString());
		} else {
			cardList = cardGrantManager.getCard(sales.getCardGrant().getCard().getCardNo(), sales.getUser().getId().toString(),
					null);
		}
		if(cardList != null){
			Map item = (Map) cardList.get(0);
			StringBuffer sb = new StringBuffer();
			if(!((Boolean)item.get("available"))){
				throw new ApplicationException(sb.append("代币卡状态不正常或持卡客户被禁用！").toString());
			}
			if(!((Boolean)item.get("jxs"))){
				throw new ApplicationException(sb.append("该消费代币卡不属于此销售商品的经销商！").toString());
			}
			if(sales.getSingle() != null){
				if(!((Boolean)item.get("orderCustomer"))){
					throw new ApplicationException(sb.append("该刷卡人不是下销售订单的客户！").toString());
				}
			}
		}
	}
	public SalesOrderManager getSalesOrderManager() {
		return salesOrderManager;
	}
	public void setSalesOrderManager(SalesOrderManager salesOrderManager) {
		this.salesOrderManager = salesOrderManager;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}