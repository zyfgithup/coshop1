package com.systop.amol.stock.webapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.systop.amol.stock.StockConstants;
import com.systop.amol.stock.model.StockCheck;
import com.systop.amol.stock.model.StockCheckLP;
import com.systop.amol.stock.service.StockCheckLPManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 库存盘点报损盈action
 * 
 * @author songbaojie
 * 
 */
@SuppressWarnings({ "serial" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StockCheckLPAction extends
		DefaultCrudAction<StockCheckLP, StockCheckLPManager> {

	@Override
	public String view() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前登录用户为空");
		Assert.notNull(getModel());
		// 得到当前盘点单
		if (getModel() != null
				&& getModel().getStockCheckDetail() != null
				&& getModel().getStockCheckDetail().getStockCheck() != null
				&& getModel().getStockCheckDetail().getStockCheck().getId() != null) {
			List<StockCheckLP> checkLPs = new ArrayList<StockCheckLP>();

			StockCheck stockCheck = getManager().getDao().get(StockCheck.class,
					getModel().getStockCheckDetail().getStockCheck().getId());
			getModel().getStockCheckDetail().setStockCheck(stockCheck);

			if (getModel().getSign() != null
					&& getModel().getSign().equals(StockConstants.LOSS)) {
				checkLPs = getManager().listLossStockCheckLP(
						getModel().getStockCheckDetail().getStockCheck().getId());
			} else {
				checkLPs = getManager().listProfitStockCheckLP(
						getModel().getStockCheckDetail().getStockCheck().getId());
			}
			if (checkLPs != null && checkLPs.size() > 0) {
				getModel().setCreateTime(checkLPs.get(0).getCreateTime());
				getModel().setCheckNo(checkLPs.get(0).getCheckNo());
				getModel().setUser(checkLPs.get(0).getUser());
			}
			getRequest().setAttribute("stockCheckLPs", checkLPs);
		}
		return super.view();
	}

	/**
	 * 导出到excel；
	 * 
	 * @return
	 */
	public String exportExcel() {
		this.view();
		return "exportExcel";
	}

}
