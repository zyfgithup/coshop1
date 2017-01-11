package com.systop.amol.user.agent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.systop.amol.user.agent.model.Fxgz;
import com.systop.core.service.BaseGenericsManager;
@Service
public class FxgzManager extends BaseGenericsManager<Fxgz>{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Fxgz getFxgzByMerId(Integer merId,Double spayMoney) {
		return this.findObject("from Fxgz o where o.user.id=? and ? between o.startNumber and o.endNumber",merId,spayMoney);
	}
	public Integer getMaxFxgzByMerId(Integer merId){
		String  sql= "select max(end_number) as endNumber from merchant_fxgz where user_id="+merId;
		return jdbcTemplate.queryForInt(sql);
	}
	public Fxgz getMaxFxgzByEndNums(Double nums,Integer merId) {
		return this.findObject("from Fxgz o where o.user.id=?  and  o.endNumber=?",merId,nums);
		
	}
	public Fxgz getByType(String type){
		return this.findObject("from Fxgz where type="+type);
	}


}
