package com.systop.amol.stock;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.systop.amol.base.units.model.UnitsItem;
import com.systop.amol.base.units.service.UnitsItemManager;

/**
 * 库存管理常量类
 * 
 * @author songbaojie
 * 
 */
public final class StockConstants {

	/**
	 * 状态常量，未清算(未进行录入)
	 */
	public static final String NOT_LIQUIDATE = "0";

	/**
	 * 状态常量，清算
	 */
	public static final String LIQUIDATE = "1";

	/**
	 * 状态常量，没有录入完商品,可继续录入(未冻结录入按钮)
	 */
	public static final String ENTRY_LIQUIDATE = "2";

	/**
	 * 标示常量:亏损
	 */
	public static final String LOSS = "0";

	/**
	 * 标示常量:盈利
	 */
	public static final String PROFIT = "1";

	/**
	 * 期初库存状态：0 未完成
	 */
	public static final String INIT_UNFINISHED = "0";
	
	/**
	 * 期初库存状态：1 已完成
	 */
	public static final String INIT_FINISHED = "1";
	
	/**
	 * 库存盘点单编号前缀
	 */
	public static final String CHECKORDER_PD = "PD";

	/**
	 * 库存盘点盘盈单编号前缀
	 */
	public static final String LPORDER_PY = "PY";

	/**
	 * 库存盘点盘亏(损)单编号前缀
	 */
	public static final String LPORDER_PK = "PK";

	/**
	 * 库存调拨单编号前缀
	 */
	public static final String TRAC_DB = "DB";
	/**
	 * 库存调拨（从顶级经销商仓库向分销商仓库调拨商品）标示
	 */
	public static final String STOCK_TRAC = "1";

	/**
	 * 库存回调（从分销商仓库向顶级经销商仓库回拨商品）标示
	 */
	public static final String STOCK_CALLBACK = "2";

	/**
	 * 从spring中获取一个bean.
	 */
	public static Object getBean(ServletContext servletCtx, String beanName) {
		ApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(servletCtx);
		return ctx.getBean(beanName);
	}

	/**
	 * （换算）得到包装单位 
	 * @param servletCtx 
	 * @param pid 商品ID
	 * @param count 数量
	 * @return
	 */
	public static String getUnitPack(ServletContext servletCtx, Integer pid,
			Integer count) {
		UnitsItemManager unitsItemManager = (UnitsItemManager)getBean(servletCtx, "unitsItemManager");
		List<UnitsItem> list= unitsItemManager.getUnitsItemOrderDesc(pid);
		StringBuffer str = new StringBuffer();
		int i = 0; //计数器
		for(UnitsItem ui : list){
			i++;
			// 得到换算个数
			int uc = ui.getCount();
			// 判断是否需要折合
			if(ui.getConversion() != null && ui.getConversion() != 1){
				// 折合数量
				int scc = count/uc;
				//这个后剩余
				count = count - scc*uc;
				// 如果折合到最后不为0，就录入信息
				if(scc != 0 || (count != 0 && i == list.size())){
					str.append(scc).append(ui.getUnits().getName());	
				}						
			}
		}
		return str.toString();
	}
}
