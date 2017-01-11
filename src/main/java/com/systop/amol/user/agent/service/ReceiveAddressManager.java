package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.ReceiveAddress;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReceiveAddressManager extends BaseGenericsManager<ReceiveAddress>{
	
	public ReceiveAddress getObjectById(int id) {
		return this.findObject("from ReceiveAddress u where u.id=?", id);
	}
	public ReceiveAddress getRsByUserId(int userId) {
		return this.findObject("from ReceiveAddress rs where rs.ifMrAddr=1 and rs.visible='1' and rs.user.id=?",userId);
	}
	public ReceiveAddress getRsByUAId(int userId,int addId) {
		return this.findObject("from ReceiveAddress rs where rs.user.id=? and rs.ifMrAddr=1 and rs.visible='1' and rs.id!=?",userId,addId);
	}
	public List<ReceiveAddress> getRsListByUserId(int userId) {
		List<ReceiveAddress> list=getDao().query("from ReceiveAddress rs where rs.user.id=? and rs.visible='1'",userId);
		return list;
	}
	public ReceiveAddress getRsByUserIdMerId(int userId,int merId) {
		ReceiveAddress list=(ReceiveAddress)getDao().findObject("from ReceiveAddress rs where rs.user.id=? and rs.merUser.id=? and rs.visible='1'",userId,merId);
		return list;
	}
	public List<ReceiveAddress> getKpInfoByType(int merId,String type) {
		List<ReceiveAddress> list=getDao().query("from ReceiveAddress rs where rs.merUser.id=? and rs.visible='1'",new Object[]{merId,type});
		return list;
	}
}
