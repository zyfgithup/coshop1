package com.systop.amol.user.agent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.service.BaseGenericsManager;

/**
 * 经销商管理Manager
 * @author NiceLunch
 *
 */
@Service
public class AgentManager extends BaseGenericsManager<User> {

	@Autowired
	private UserManager userManager;
	
	/** 保存 */
	public void save(User user) {
		userManager.save(user);
	}
	public void update(User user){
		userManager.update(user);
	}
	public void remove(User user){
		userManager.remove(user);
	}
	public void unsealUser(User user){
		userManager.unsealUser(user);
	}
}
