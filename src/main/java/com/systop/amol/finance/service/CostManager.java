package com.systop.amol.finance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.finance.model.Cost;
import com.systop.amol.finance.model.CostDetail;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 费用管理Manager
 * @author lee
 */
@Service
public class CostManager extends BaseGenericsManager<Cost> {

	@Autowired
	private CostDetailManager costDetailManager;

	/**
	 * 费用主页面中取得详单
	 */
	public List<CostDetail> getCostDetails(Cost cost) {
		List<CostDetail> cd = costDetailManager.getDetails(cost.getId());
		return cd;
	}
	
	@Transactional
	public void save(Cost cost, List<CostDetail> list) {

		try {
			//修改时
			if (cost.getId() != null ) {
				// 删除明细
				getDao().getHibernateTemplate().clear();
				List<CostDetail> detailList = costDetailManager.query(
						"from CostDetail cd where cd.cost.id = ?", cost.getId());
				for (CostDetail costDetail : detailList) {
					costDetailManager.remove(costDetail);
				}
			}
			// 保存费用主表信息
			super.save(cost);
			// 保存费用明细信息
			for (CostDetail cd : list) {
				costDetailManager.save(cd);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("保存失败");
		}
	}

}

