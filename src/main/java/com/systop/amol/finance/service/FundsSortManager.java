package com.systop.amol.finance.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.systop.amol.finance.model.FundsSort;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.service.BaseGenericsManager;

/**
 * 资金去向管理Sercice
 * @author lee
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class FundsSortManager extends BaseGenericsManager<FundsSort> {
	
	public Map getFundsSortMap(User user) {
		List<FundsSort> fundsSortList = null;
		Map FundsSortMap = new LinkedHashMap();
		if(user != null){
			if(user.getSuperior() != null){
				fundsSortList = query("from FundsSort fs where fs.user.id = ?",user.getSuperior().getId());
			}else{
				fundsSortList = query("from FundsSort fs where fs.user.id = ?",user.getId());
			}
			
			for (FundsSort fundsSort : fundsSortList) {
				FundsSortMap.put(fundsSort.getId(), fundsSort.getName());
			}
		}	
		return FundsSortMap;
	}
}
