package com.systop.amol.sales.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.MatchMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.Receive;
import com.systop.amol.sales.model.ReceiveDetail;
import com.systop.amol.sales.model.ReceiveInit;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.utils.SingleNumber;
import com.systop.amol.util.DoubleFormatUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.service.BaseGenericsManager;

/**
 * 回款款单Service
 * 
 * @author 王会璞
 */
@Service
public class ReceiveManager extends BaseGenericsManager<Receive> {

	/** 出库单Manager */
	@Resource
	private SalesManager salesManager;
	
	@Resource
	private ReceiveInitManager receiveInitManager;

	/** 回款款单明细Manager */
	@Resource
	private ReceiveDetailManager receiveDetailManager;
	
	@Resource
	private UserManager userManager;

	/**
	 * 生成回款单单号
	 * 
	 * @return
	 */
	public String getReceiveNumber(String billtype, Integer userId) {
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String billNo = SalesConstants.REIMBURSEMENT_BILL + date;

		String hql = "select max(r.receiveNumber) from Receive r where r.receiveNumber like ? and (r.user.id=? or r.user.superior.id=?)";
		User user = userManager.get(userId);
		if(user.getSuperior() != null){
			userId = user.getSuperior().getId();
		}
		String sno = (String) getDao().findObject(hql,
				new Object[] { MatchMode.START.toMatchString(billNo), userId, userId });

		return SingleNumber.getSingleNumber().getNumber(billNo, sno);
	}

	/**
	 * 回款款单冲红
	 * 
	 * @author 王会璞
	 * @param id
	 *          回款单id
	 * @param userId
	 *          当前登录用户id
	 * @return receive 回款单
	 */
	@Transactional
	public Receive redRed(Integer id, Integer userId) {

		// 要冲红的回款单
		Receive receive = get(id);
		// 将回款单设为已冲红标识
		receive.setRedRed(SalesConstants.RED);
		update(receive);// 更新回款单

		// 克隆回款单 生成冲红单
		Receive r = (Receive) receive.clone();
		r.setId(null);
		r.setCreateTime(new Date());
		r.setSpayamount(-(DoubleFormatUtil.format(receive.getSpayamount())));//实收金额
		r.setRedRed(SalesConstants.REDBILL);
		save(r);

		List<ReceiveDetail> rdList = receiveDetailManager
				.getReceiveDetailList(receive.getId());
		for (ReceiveDetail rd : rdList) {
			
			//将回款单下的明细信息中的记录冲红
			
			rd.setRedRed(SalesConstants.RED);//将回款单明细设为已冲红标识
			receiveDetailManager.update(rd);
			
			ReceiveDetail redbrd = (ReceiveDetail)rd.clone();//根据回款单明细生成的冲红单
			redbrd.setId(null);
			redbrd.setCreateTime(new Date());
			redbrd.setSamount(-(DoubleFormatUtil.format(redbrd.getSamount())));//应收金额
			redbrd.setSpayamount(-(DoubleFormatUtil.format(redbrd.getSpayamount())));// 实收金额  
			redbrd.setRttao(-(DoubleFormatUtil.format(redbrd.getRttao())));//退款总金额
			redbrd.setTheCollection(-(DoubleFormatUtil.format(redbrd.getTheCollection())));//本次收款金额
			redbrd.setTtno(-redbrd.getTtno());//出库单剩余总数量
			redbrd.setCount(-redbrd.getCount());//出库单中的商品总数量
			redbrd.setRedRed(SalesConstants.REDBILL);
			redbrd.setReceive(r);
			receiveDetailManager.save(redbrd);
			
			
			if(rd.getSales() != null){//由应收款单生成的回款单明细
				//如果没有与出库单关联的回款单明细，那么将出库单中表示：“是否有回款单为出库单回款”置为否
				receiveDetailManager.updateSalesR(rd);
			}else if (rd.getReceiveInit() != null){//由期初应收款单产生的回款单明细
				receiveInitManager.updateR(rd);
			}
		}
		
		return receive;
	}

	/**
	 * 保存回款单
	 * 
	 * @param sales
	 *          回款款单
	 * @param slist
	 *          回款款单商品明细
	 */
	@Transactional
	public void save(Receive receive, List<ReceiveDetail> sdlist) {

		for (ReceiveDetail sd : sdlist) {

			//如果该回款明细是由应收款单（应收不实收不相等的出库单）产生的
			if(SalesConstants.DOCORS.equals(sd.getHistory())){
				Sales salesSQL = salesManager.get(sd.getId());
				salesSQL.setSpayamount(DoubleFormatUtil.format(salesSQL.getSpayamount())
						+ DoubleFormatUtil.format(sd.getSpayamount()));

				if (sd.getReceive() != null) {
					sd.setId(null);
					sd.setSales(salesSQL);
					sd.setUser(salesSQL.getUser());
					sd.setEmployee(salesSQL.getEmployee());
					sd.setCreateTime(new Date());
					sd.setCustomer(salesSQL.getCustomer());
					sd.setSamount(salesSQL.getSamount());
					sd.setSpayamount(salesSQL.getSpayamount());
					sd.setRttao(salesSQL.getRttao());
//					sd.setTtno(salesSQL.getTtno());
//					sd.setCount(salesSQL.getCount());
					sd.setPayment(salesSQL.getPayment());
					sd.setRealReturnMoney(salesSQL.getRealReturnMoney());
					receiveDetailManager.save(sd);// 保存回款单明细
				}

				salesSQL.setReceiveSalesReturn(SalesConstants.YES_RE);//该出库单有回款单为其回款
				salesManager.update(salesSQL);// 更新出库单中的实收款金额     代币卡消费金额     现金消费金额     有回款单为其回款
			}else if (SalesConstants.BEGINNING.equals(sd.getHistory())){//如果该回款单明细是由期初应收单产生的
				
				ReceiveInit receiveInitSQL = receiveInitManager.get(sd.getId());
				receiveInitSQL.setAmountReceived(DoubleFormatUtil.format(receiveInitSQL.getAmountReceived())
						+ DoubleFormatUtil.format(sd.getSpayamount()));

				if (sd.getReceive() != null) {
					sd.setId(null);
					sd.setSales(null);
					sd.setReceiveInit(receiveInitSQL);
					sd.setUser(receiveInitSQL.getUser());
					sd.setEmployee(null);
					sd.setCreateTime(new Date());
					sd.setCustomer(receiveInitSQL.getCustomer());
					sd.setSamount(receiveInitSQL.getAmount());
					sd.setSpayamount(receiveInitSQL.getAmountReceived());
					sd.setRttao(0.0d);
					sd.setTtno(0);
					sd.setCount(0);
					sd.setPayment(null);
					receiveDetailManager.save(sd);// 保存回款单明细
				}

				receiveInitManager.update(receiveInitSQL);// 更新期初应收单中的实收款金额 
			}
		}

		save(receive);// 保存回款单
	}
}