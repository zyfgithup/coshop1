package com.systop.amol.base.express.service;

import com.systop.amol.base.express.model.ExpressCompany;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 快递公司管理Manager
 */
@Service
public class ExpressCompanyManager extends BaseGenericsManager<ExpressCompany> {

	@Transactional
	public void save(ExpressCompany expressCompany) {
		this.getDao().clear();
		if (null == expressCompany.getId() && null != this.findObject("from ExpressCompany o where o.name = ? and o.user.id=?",new Object[]{expressCompany.getName(),expressCompany.getUser().getId()} )) {
			throw new ApplicationException("公司为【" + expressCompany.getName()  + "】的信息已存在！");
		}else{
			super.save(expressCompany);
		}
	}
	public ExpressCompany getRsByUserId(int userId) {
		return this.findObject("from ExpressCompany rs where  visible = '1'  and rs.user.id=?",userId);
	}
}
