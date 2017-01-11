package com.systop.amol.base.yyy.service;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.systop.amol.base.yyy.model.YyyAward;
import com.systop.core.service.BaseGenericsManager;
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class YyyAwardManager extends BaseGenericsManager<YyyAward>{
	
	private JdbcTemplate jdbcTemplate;
	
	public int getCntOfAward(Integer id){
		String sql="select count(*) from yyy_award where yyy_event_id=?";
		return jdbcTemplate.queryForInt(sql, new Object[]{id});
	}
	public List<Map<String,Object>> getAwardNames(Integer id){
		String sql="select id,jx_name as jxName from yyy_award where yyy_event_id=? ";
		return jdbcTemplate.queryForList(sql, new Object[]{id});
	}
	public YyyAward getYyyAwardByYyyeventId(Integer awardId) {
		return this.findObject("from YyyAward u where u.id=?", awardId);
	}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

}
