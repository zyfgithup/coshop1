package com.systop.amol.sales.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.Receive;
import com.systop.amol.sales.model.ReceiveDetail;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.util.DoubleFormatUtil;
import com.systop.core.service.BaseGenericsManager;

/**
 * 回款单明细Service
 * 
 * @author 王会璞
 */
@Service
public class ReceiveDetailManager extends BaseGenericsManager<ReceiveDetail> {

	/** 回款单Manager */
	@Resource
	private ReceiveManager receiveManager;
	
	/** 出库单Manager */
	@Resource
  private SalesManager salesManager;
	
	/** 期初应收Manager */
	@Resource
	private ReceiveInitManager receiveInitManager;
	
	/**
	 * 保存回款单明细
	 * 
	 * @param sales
	 *          应收款单明细
	 */
	@Transactional
	public void save(List<ReceiveDetail> rdList) {
		for (ReceiveDetail rd : rdList) {
			save(rd);
		}
	}
	
	/**
	 * 回款单明细
	 * @param singleId  回收款单  id
	 * @return
	 */
	public List<ReceiveDetail> getReceiveDetailList(Integer singleId) {
		List<ReceiveDetail> sdList = this.query(
				"from ReceiveDetail rd where rd.receive.id = ? order by rd.createTime desc, rd.id desc",
				singleId);
		return sdList;
	}

	/**
	 * 冲红回款单详情中的一条记录
	 * @param receiveDetailId
	 * @param userId
	 */
	@Transactional
	public ReceiveDetail redRed(Integer receiveDetailId, Integer userId) {
		
		//冲红
		ReceiveDetail receiveDetail = get(receiveDetailId);
		try {
			receiveDetail.setRedRed(SalesConstants.RED);
			update(receiveDetail);
			
			//冲红单记录
			ReceiveDetail redBill = (ReceiveDetail)receiveDetail.clone();
			redBill.setId(null);
			redBill.setSamount(-(com.systop.amol.util.DoubleFormatUtil.format(redBill.getSamount())));//应收金额
			redBill.setSpayamount(-(DoubleFormatUtil.format(redBill.getSpayamount())));// 实收金额  
			redBill.setRttao(-(DoubleFormatUtil.format(redBill.getRttao())));//退款总金额
			redBill.setTheCollection(-(DoubleFormatUtil.format(redBill.getTheCollection())));//本次收款金额
			redBill.setTtno(-redBill.getTtno());//出库单剩余总数量
			redBill.setCount(-redBill.getCount());//出库单中的商品总数量
			redBill.setRedRed(SalesConstants.REDBILL);
			save(redBill);

			//更新回款单
			Receive receive = receiveDetail.getReceive();
			receive.setSpayamount(DoubleFormatUtil.format(receive.getSpayamount()) - DoubleFormatUtil.format(receiveDetail.getTheCollection()));
			receiveManager.update(receive);
			
			if(receiveDetail.getSales() != null){//由应收款单生成的回款单明细
				//如果没有与出库单关联的回款单明细，那么将出库单中表示：“是否有回款单为出库单回款”置为否
				updateSalesR(receiveDetail);
			}else if (receiveDetail.getReceiveInit() != null){//由期初应收款单产生的回款单明细
				System.out.println("receiveInitManager："+receiveInitManager);
				System.out.println("receiveDetail："+receiveDetail);
				receiveInitManager.updateR(receiveDetail);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return receiveDetail;
	}
	
	/**
	 * 如果没有正常（未冲红）与出库单关联的回款单明细，那么将出库单中表示：“是否有回款单为出库单回款”置为否
	 * 将出库单中的金额减少，因为回款单要冲红
	 * @param receiveDetail  回款单明细记录
	 */
	@Transactional
	public void updateSalesR(ReceiveDetail receiveDetail){
		List<ReceiveDetail> sdList = this.query(
				"from ReceiveDetail rd where rd.redRed = ? and rd.sales.id = ?",SalesConstants.NORMAL,receiveDetail.getSales().getId());
		if(sdList.size() == 0){
			Sales salse = receiveDetail.getSales();
			salse.setReceiveSalesReturn(SalesConstants.NOT_RE);
			salesManager.update(salse);
		}
		
	  //将出库单中的金额减少，因为回款单要冲红
		Sales sales = salesManager.get(receiveDetail.getSales().getId());
		sales.setSpayamount(DoubleFormatUtil.format(sales.getSpayamount())
				- DoubleFormatUtil.format(receiveDetail.getTheCollection()));
		salesManager.update(sales);
	}
	
	/**
	 * 查询回款单中与出库单有关的回款单明细记录
	 * @param salesNo  出库单号
	 * @return
	 */
	public List<ReceiveDetail> querySalesNORD(String salesNo){
		List<ReceiveDetail> sdList = this.query(
				"from ReceiveDetail rd where rd.sales.salesNo = ? and rd.sales.status = ? ",salesNo,SalesConstants.SALES);
		return sdList;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	public boolean getReceiveDetail(Integer receiveInitId) {
		List<ReceiveDetail> details = this.query("from ReceiveDetail rd where rd.receiveInit.id=?", receiveInitId);
		if(details != null && details.size() > 0){
			return true;
		}else if(details != null && details.size() == 0){
			return false;
		}
		return true;
	}

}