package com.systop.amol.sales.service;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.ReceiveDetail;
import com.systop.amol.sales.model.ReceiveInit;
import com.systop.amol.user.AmolUserConstants;
import com.systop.amol.util.DoubleFormatUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 应收初始化Service
 * 
 * @author 王会璞
 */
@Service
public class ReceiveInitManager extends BaseGenericsManager<ReceiveInit> {

	@Resource
	private ReceiveDetailManager receiveDetailManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	@Version
	public void save(List<ReceiveInit> riList) {

		try {

			for (ReceiveInit receiveInit : riList) {
				if (receiveInit.getId() != null && receiveInit.getId().intValue() > 0) {
					this.update(receiveInit);
				} else {
					this.save(receiveInit);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("保存失败");
		}
	}

	public List<ReceiveInit> getWpzmReceive(Integer customerId) {
		List<ReceiveInit> riList = this
				.query(
						"from ReceiveInit ri where  ri.amount != ri.amountReceived and ri.customer.id = ?",
						customerId);
		return riList;
	}

	/**
	 * 回款单明细要冲红，将回给期初应收的钱从期初应收中减去
	 * 
	 * @param receiveDetail
	 */
	@Transactional
	public void updateR(ReceiveDetail receiveDetail) {
		ReceiveInit receiveInit = receiveDetail.getReceiveInit();
		receiveInit.setAmountReceived(DoubleFormatUtil.format(receiveInit
				.getAmountReceived())
				- DoubleFormatUtil.format(receiveDetail.getTheCollection()));
		update(receiveInit);
	}

	/**
	 * 根据省份证号得到客户的期初应收记录
	 * 
	 * @param idCard
	 * @return
	 */
	public ReceiveInit getReceiveInit(String idCard) {
		return (ReceiveInit) getDao().findObject(
				"from ReceiveInit ri where ri.customer.idCard = ? and ri.status = ?", idCard, SalesConstants.INIT_NORMAL);
	}

	/**
	 * 删除期初应收
	 * 
	 * @param receiveInitId
	 */
	@Transactional
	public void delete(Integer receiveInitId) {
		ReceiveInit receiveInit = this.get(receiveInitId);
		if(receiveInit.getStatus().intValue() == SalesConstants.INIT_LOCKING){
			throw new ApplicationException("该期初应收，不能删除！！");
		}
		if (receiveDetailManager.getReceiveDetail(receiveInit.getId())) {
			throw new ApplicationException("该期初应收已被回款单回款，不能删除！！");
		} else {
			this.remove(receiveInit);
		}
	}
	/**
	 * 期初应收，修改状态为已完成
	 * 
	 * @param user
	 * @return
	 */
	@Transactional
	public boolean updateAllReceiveInit(User user) {
		try {
			StringBuffer sql = new StringBuffer(
					"update receive_init set status = ? where user_id = ? ");
			Object[] args = null;
			if(user.getSuperior() == null && user.getType().equals(AmolUserConstants.AGENT)){
				args = new Object[] { SalesConstants.INIT_LOCKING, user.getId() };
			}
			if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.EMPLOYEE)){
				args = new Object[] { SalesConstants.INIT_LOCKING, user.getSuperior().getId() };
			}
			if(args == null){
				return false;
			}else{
				jdbcTemplate.update(sql.toString(), args);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}