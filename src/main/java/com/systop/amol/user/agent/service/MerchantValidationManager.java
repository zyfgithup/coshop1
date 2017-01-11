package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.MerchantValidations;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class MerchantValidationManager extends BaseGenericsManager<MerchantValidations>{
	private JdbcTemplate jdbcTemplate;
	public String getAvgScores(User user){
		String sql="select avg(score) as avgScore from merchant_validation where meruser_id="+user.getId();
		Map map=jdbcTemplate.queryForMap(sql);
		return String.valueOf(map.get("avgScore"));
	}
	public MerchantValidations getByUserIdAndSalesId(Integer userId,Integer salesId){
		return this.findObject(" from MerchantValidations where appUser.id="+userId+" and sales.id="+salesId+" ");
	}
	public MerchantValidations getScoreOfVs(Integer plrId,Integer bplrId,Integer salesId){
		String hql = " from MerchantValidations where appUser.id=? and merchantUser.id=?  and sales.id=?";
		return this.findObject(hql,new Object[]{plrId,bplrId,salesId});
	}
	public List<MerchantValidations> getMvListBySalesId(Integer salesId){
		String hql = " from MerchantValidations where sales.id ="+salesId;
		return this.query(hql);
	}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}
