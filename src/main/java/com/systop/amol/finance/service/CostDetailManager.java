package com.systop.amol.finance.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.systop.amol.finance.model.CostDetail;
import com.systop.core.service.BaseGenericsManager;

/**
 * 资金收入详单管理Sercice
 * @author lee
 */
@Service
public class CostDetailManager extends BaseGenericsManager<CostDetail> {

	/**
	 * 获取主表对应的所有详细信息
	 */
	public List<CostDetail> getDetails(Integer costId) {
		List<CostDetail> detailList = query(
				"from CostDetail cd where cd.cost.id = ?", costId);
		return detailList;
	}
}
