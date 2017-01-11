package com.systop.amol.user.agent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.systop.amol.user.agent.model.Partners;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.service.BaseGenericsManager;
@Service
public class PartnersManager extends BaseGenericsManager<Partners>{
	@Autowired
	UserManager userManager;
	//根据用户id查询用户对象
	public Partners getPartnersById(int id,String type) {
		return this.findObject("from Partners u where u.appUser.id=? and u.inDirectOrDirect=?  ", id,type);
	}
	//根据userId查询直接/间接合伙人
	public User getPartnersByPartnersId(int id,String type){
		Partners partners=this.findObject("from Partners u where u.appUser.id=? and u.inDirectOrDirect='"+type+"'",id);
		if(null!=partners){
		return userManager.get(partners.getPartnerUser().getId());
		}else{
			return null;
		}
	}

}
