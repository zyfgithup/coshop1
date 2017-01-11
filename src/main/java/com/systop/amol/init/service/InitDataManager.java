package com.systop.amol.init.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.init.model.InitData;
import com.systop.core.service.BaseGenericsManager;

/**
 * 数据初始化Manager
 * 
 * @author
 */
@Service
@SuppressWarnings("rawtypes")
public class InitDataManager extends BaseGenericsManager<InitData> {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 根据实体名和经销商ID获取该经销商下的相关表记录数【不涉及到分销商数据的用此方法】
	 * 
	 * @param agentId
	 * @return
	 */
	public Integer getOneLevelCounts(String entityName, String name,
			Integer agentId) {
		String sql = "select count(*) from " + entityName + " where " + name
				+ "user.id = ? ";
		List list = this.query(sql, new Object[] { agentId });
		for (Object object : list) {
			if (object != null) {
				return Integer.valueOf(object.toString());
			}
		}
		return null;
	}

	/**
	 * 根据实体名和经销商ID获取该经销商下的相关表记录数【不涉及到分销商数据的用此方法】
	 * 
	 * @param agentId
	 * @author songbaojie
	 * @return
	 */
	public Integer getOneLevelCountsByCreator(String entityName, String name,
			Integer agentId) {
		String sql = "select count(*) from " + entityName + " where " + name
				+ " = ? ";
		List list = this.query(sql, new Object[] { agentId });
		for (Object object : list) {
			if (object != null) {
				return Integer.valueOf(object.toString());
			}
		}
		return null;
	}

	/**
	 * 根据实体名和经销商ID获取该经销商下的相关表记录数【不涉及到分销商数据的用此方法】
	 * 
	 * @param agentId
	 * @author songbaojie
	 * @return
	 */
	public Integer getCountsByCreator(String entityName, String name,
			Integer agentId,String type) {
		String sql = "select count(*) from " + entityName + " where " + name
				+ " = ? and type = ?";
		List list = this.query(sql, new Object[] { agentId ,type});
		for (Object object : list) {
			if (object != null) {
				return Integer.valueOf(object.toString());
			}
		}
		return null;
	}
	
	/**
	 * 根据实体名和经销商ID获取该经销商下的相关表记录数【涉及到分销商数据的用此方法】
	 * 
	 * @param agentId
	 * @return
	 */
	public Integer getTwoLevelCounts(String entityName, String name, Integer agentId) {
		String sql = "select count(*) from " + entityName + " where " + name + "user.id = ? or " + name + "user.superior.id = ?";
		List list = this.query(sql, new Object[] { agentId, agentId });
		for (Object object : list) {
			if (object != null) {
				return Integer.valueOf(object.toString());
			}
		}
		return null;
	}

	/**
	 * 删除该经销商下的所有涉及到的相关数据信息
	 * 
	 * @param agentId
	 * @throws Exception
	 */
	@Transactional
	public boolean initData(Integer agentId) {
		try {
			if (agentId != null) {
				// 删除费用明细表
				jdbcTemplate.update("delete from cost_details where cost_id in (select id from costs where user_id = '" + agentId + "' or user_id in (select user_id from users where superior_id = '" + agentId + "'))");

				// 删除费用表
				jdbcTemplate.update("delete from costs where user_id = '" + agentId  + "' or user_id in (select user_id from users where superior_id = '" + agentId + "')");

				// 删除资金类别
				jdbcTemplate.update("delete from funds_sorts where user_id = '" + agentId + "' or user_id in (select user_id from users where superior_id = '" + agentId + "')");

				// 删除收支类别
				jdbcTemplate.update("delete from cost_sorts where user_id = '" + agentId + "' or user_id in (select user_id from users where superior_id = '" + agentId + "') order by create_time desc");
				
				// 采购数据删除开始
				// 应付详单
				jdbcTemplate.execute("delete  a from  pay_details a,pays b where a.pay_id=b.id and (b.user_id = " + agentId +" or b.user_id in (select user_id from users where superior_id = " + agentId + "))");
				// 应付单
				jdbcTemplate.execute("delete  from pays where user_id = " + agentId +" or user_id in (select user_id from users where superior_id = " + agentId + ")");
				// 初始化数据
				jdbcTemplate.execute("delete  from pay_init  where user_id = " + agentId);
				// 及时应付
				jdbcTemplate.execute(" delete from pay_ables   where user_id = " + agentId);
				// 订单详单
				jdbcTemplate.execute("  delete a from purchase_details a,purchases b where a.purchase_id=b.id and b.user_id=" + agentId +" or b.user_id in (select user_id from users where superior_id = " + agentId + ")");
				// 订单
				jdbcTemplate.execute("  delete  from purchases  where  user_id =" + agentId + " or user_id in (select user_id from users where superior_id = " + agentId + ")");

				

				// 库存
				// 库存盘点损益单
				jdbcTemplate.execute("  delete from stock_check_lp where user_id = " + agentId+ " or user_id in (select user_id from users where superior_id = " + agentId + ")");
				// 库存盘点详单
				jdbcTemplate.execute("  delete from stock_check_details where user_id = " + agentId+ " or user_id in (select user_id from users where superior_id = " + agentId + ")");
				// 库存盘点
				jdbcTemplate.execute("  delete from stock_checks where user_id = " + agentId+ " or user_id in (select user_id from users where superior_id = " + agentId + ")");
				// 库存调拨详单
				jdbcTemplate.execute("  delete from stock_trac_details where user_id = " + agentId+ " or user_id in (select user_id from users where superior_id = " + agentId + ")");
				// 库存调拨
				jdbcTemplate.execute("  delete from stock_tracs where user_id = " + agentId+ " or user_id in (select user_id from users where superior_id = " + agentId + ")");
				// 库存初期
				jdbcTemplate.execute("  delete from stock_inits where user_id = " + agentId+ " or user_id in (select user_id from users where superior_id = " + agentId + ")");

				// 相关的分销商
				// 分销商及时库存 
				jdbcTemplate.execute("  delete s from stocks s ,users u where s.user_id = u.id and u.superior_id = " + agentId);
				// 及时库存
				jdbcTemplate.execute("  delete  from stocks  where  user_id = " + agentId);

				// 销售管理start
				// 条形码
				jdbcTemplate.execute("delete b from barcodes b,sales_details sd,sales s,users u where b.sales_detail_id = sd.id and sd.sales_id = s.id and s.user_id=u.id and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
			  // 退货单详情
				jdbcTemplate.execute("delete sd from sales_details sd,sales s,users u where sd.oor_salesDetailId_id is not null and sd.sales_id = s.id and s.status='2' and s.user_id=u.id and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
				jdbcTemplate.execute("delete sd from sales_details sd,sales s,users u where sd.sales_id = s.id and s.status='2' and s.user_id=u.id and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
				// 出库单详情
				jdbcTemplate.execute("delete sd from sales_details sd,sales s,users u where sd.oor_salesDetailId_id is not null and sd.sales_id = s.id and s.status='1' and s.user_id=u.id and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
				jdbcTemplate.execute("delete sd from sales_details sd,sales s,users u where sd.sales_id = s.id and s.status='1' and s.user_id=u.id and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
				// 订单详情
				jdbcTemplate.execute("delete sd from sales_details sd,sales s,users u where sd.sales_id = s.id and s.status='0' and s.user_id=u.id and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
				// 回款详情
				jdbcTemplate.execute("delete rd from receive_details rd,receives r,users u where rd.receive_id = r.id and r.user_id=u.id and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
				// 回款单
				jdbcTemplate.execute("delete r from receives r,users u where r.user_id=u.id and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
			  // 退货单
				jdbcTemplate.execute("delete s from sales s,users u where s.single_id is not null and s.status='2' and s.user_id=u.id and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
				jdbcTemplate.execute("delete s from sales s,users u where s.status='2' and s.user_id=u.id and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
				// 出库单
				jdbcTemplate.execute("delete s from sales s,users u where s.single_id is not null and s.status='1' and s.user_id=u.id and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
				jdbcTemplate.execute("delete s from sales s,users u where s.status='1' and s.user_id=u.id and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
				// 订单
				jdbcTemplate.execute("delete s from sales s,users u where s.user_id=u.id and s.status='0' and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
			  // 期初应收单
				jdbcTemplate.execute("delete ri from receive_init ri,users u where ri.user_id=u.id and (u.superior_id='" + agentId + "' or u.id='" + agentId + "')");
				// 销售管理end
				
				
				
				// 基数数据---------------------------------------------
				// 分销商仓库
		        jdbcTemplate.execute("delete s from storage s ,users u where s.creator_id = u.id and u.superior_id = " + agentId); 
		        // 仓库
		        jdbcTemplate.execute("delete  from storage where creator_id = " + agentId);
         
		        // 计量单位换算
		        jdbcTemplate.execute("delete b from units a,units_item b where a.id = b.units_id and  a.user_id = " + agentId);
				//商品
				 jdbcTemplate.execute("delete  from products  where  user_id = " + agentId);
				//商品类型
				jdbcTemplate.execute("delete  from products_sort where creator_id = " + agentId);
					       
				// 计量单位
				jdbcTemplate.execute("delete  from units  where  user_id=" + agentId);
					
				//删除供应商
				jdbcTemplate.update("delete from suppliers where user_id = '" + agentId + "'");
				
				//客户及经销商
				//更新卡记录
				jdbcTemplate.update("update cards set card_state=0 where id in (select card_id from cards_grant where customer_id in (select id from customers where owner_id = '" + agentId + "'))");
				//删除充值记录
				jdbcTemplate.update("delete from card_ups where cardGrant_id in (select id from cards_grant where customer_id in (select id from customers where owner_id = '" + agentId + "'))");
				//删除消费记录
				jdbcTemplate.update("delete from card_spends where cardGrant_id in (select id from cards_grant where customer_id in (select id from customers where owner_id = '" + agentId + "'))");
				//删除发卡记录
				jdbcTemplate.update("delete from cards_grant where customer_id in (select id from customers where owner_id = '" + agentId + "')");
				//删除客户
				jdbcTemplate.update("delete from customers where owner_id = '" + agentId + "'");
				//删除下级经销商角色
				jdbcTemplate.update("delete from user_role where user_id in (select id from users where superior_id = '" + agentId + "')");
				//删除登录记录
				jdbcTemplate.update("delete from user_login_history where user_id in (select id from users where superior_id = '" + agentId + "' or id = '" + agentId + "')");
				//删除下级经销商
				jdbcTemplate.update("delete from users where superior_id = '" + agentId + "'");
				//删除员工表信息
				jdbcTemplate.update("delete from users where superior_id = '" + agentId + "' and type = 'employee'");
				//删除员工部门表
				jdbcTemplate.update("delete from depts where user_id = '" + agentId + "'");	
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

}