package com.systop.amol.purchase.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.purchase.model.Pay;
import com.systop.amol.purchase.model.PayAble;
import com.systop.amol.purchase.model.PayDetail;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 付款表
 * 
 * @author WangHaiYan
 * 
 */
@Service
public class PayManager extends BaseGenericsManager<Pay> {

	
	/**
	 * 临时访问数据库的对象，不从缓存中取
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	//付款单详单
	@Autowired
	private PayDetailManager payDetailManager;

	//应付管理
	@Autowired
	private PayAbleManager payAbleManager;

	@Transactional
	public void save(Pay pay, List<PayDetail> paylist) {

		//try {
			// 如果是修改就先删除旧的，再保存新的
		//	if (pay.getId() != null && pay.getId() > 0) {
			//	del(pay,false);
		//	}
			//
			// 保存应付信息
			this.save(pay);
			// 修改及时应付，及时应付减少
			PayAble payAble = new PayAble();
			payAble.setAmount(pay.getAmount().doubleValue());
			payAble.setSupplier(pay.getSupplier());
			payAble.setUser(pay.getUser());
			payAbleManager.delIn(payAble);
			// 保存明细
			payDetailManager.save(pay.getSupplier().getId(),pay.getUser(),paylist);
	//	} catch (Exception e) {
		//	e.printStackTrace();
		//	throw new ApplicationException("保存失败"+e.getMessage());
		//}
	}

	/**
	 * 返回数据库的入库单信息，修改及时应付用
	 */
	private Float getOldPay(Integer Id) {

		return (Float) jdbcTemplate.query(
				"select s.amount from pays s where s.id=?",
				new Object[] { Id }, new ResultSetExtractor() {
					public Object extractData(ResultSet rs) throws SQLException {
						rs.next();
						return rs.getFloat(1);
					}
				});
	}

	public void del(Pay pay,boolean isInvalid) {
		// 删除明细
		payDetailManager.del(pay.getId());
		Float jiu = this.getOldPay(pay.getId());
		// 修改应收增加
		PayAble payAble = new PayAble();
		payAble.setAmount(jiu.doubleValue());
		payAble.setSupplier(pay.getSupplier());
		payAble.setUser(pay.getUser());
		payAbleManager.saveIn(payAble);
		
	}
	

	/*
	 *  删除入库单，同事删除详单，修改及时库存，和应付(non-Javadoc)
	 * @see com.systop.core.service.BaseGenericsManager#remove(java.lang.Object)
	 */
	@Transactional
	public void remove(Pay pay) {
		try {
			// 删除入库单明细，并且修改库存，应付
			del(pay,true);
			// 删除入库单
			pay.setStatus(1);//作废
			super.save(pay);
			//super.remove(pay);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ApplicationException("删除失败");
		}
	}
}
