package com.systop.amol.base.yyy.service;

import com.systop.amol.base.yyy.model.YyyEvent;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class YyyManager extends BaseGenericsManager<YyyEvent>{
	public YyyEvent getObjectById(int id) {
		return this.findObject("from YyyEvent u where u.id=?", id);
	}
	public List<YyyEvent> getYyyListByProSortId(int id) {

		return this.getDao().query("from YyyEvent u where u.xbzy.id=?",id);
	}
	public List<YyyEvent> getListByPhone(String phone){
		return this.getDao().query("from YyyEvent u where u.stuPhone=?",phone);
	}
   	@Transactional
	public void plSaveStuInfos(List<YyyEvent> list){
		List<YyyEvent> yList = null;
		if(null!=list&&list.size()>0){
			for (YyyEvent ye : list ){
				yList = this.getListByPhone(ye.getStuPhone());
				if(null == yList || yList.size() == 0) {
					this.save(ye);
				}
			}
		}
	}
}
