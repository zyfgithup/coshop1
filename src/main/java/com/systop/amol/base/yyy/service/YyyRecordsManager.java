package com.systop.amol.base.yyy.service;

import org.springframework.stereotype.Service;

import com.systop.amol.base.yyy.model.YyyEvent;
import com.systop.amol.base.yyy.model.YyyRecords;
import com.systop.core.service.BaseGenericsManager;
@Service
public class YyyRecordsManager extends BaseGenericsManager<YyyRecords>{

	
	
	public YyyRecords getObjectById(int eventId,int userId) {
		return this.findObject("from YyyRecords u where u.yyyEvent.id=? and u.user.id=? and u.isZj='1' ",new Object[]{eventId,userId});
	}
}
