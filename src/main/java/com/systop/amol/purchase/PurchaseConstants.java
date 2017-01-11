package com.systop.amol.purchase;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 采购管理及相关模块的常量类
 * 
 * @author Sam Lee
 * 
 */
public final class PurchaseConstants {

	/**
	 * 付款类型状态常量Map
	 */
	public static final Map<Integer, String> PAY_TYPE = Collections
			.synchronizedMap(new LinkedHashMap<Integer, String>());
	static {
		PAY_TYPE.put(0, "预付款");
		PAY_TYPE.put(1, "全款");
		PAY_TYPE.put(2, "代币卡");
		PAY_TYPE.put(3, "预划卡");

	}

	// 采购订单的前缀
	public static final String ORDERPREFIX = "JD-";
	// 入库单前缀
	public static final String PURCHASEPREFIX = "JH-";
	// 退货单前缀
	public static final String RETURNPREFIX = "JT-";
	// 应付款单前缀
	public static final String PAYPREFIX = "YF-";
	// 订单的单据类型为0
	public static final Integer ORDERTYPE = 0;
	public static final String ORDER = "采购订单";

	// 入库的单据类型
	public static final Integer PURCHASETYPE = 1;
	public static final String PURCHASE = "采购入库单";
	// 退货的单据类型
	public static final Integer RETURNTYPE = 2;
	public static final String RETURNS = "采购退货单";
	// 应付款单的单据类型
	public static final Integer PAYTYPE = 3;
	public static final String PAY = "应付款单";

	// 采购查询页面的map
	public static final Map<String, String> SELECT_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		SELECT_MAP.put("", "请选择");
		SELECT_MAP.put(ORDERTYPE.toString(), ORDER);
		SELECT_MAP.put(PURCHASETYPE.toString(), PURCHASE);
		SELECT_MAP.put(RETURNTYPE.toString(), RETURNS);
	}
	// 厂商采购查询页面的map
	public static final Map<String, String> PSELECT_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		PSELECT_MAP.put("", "请选择");
		PSELECT_MAP.put(ORDERTYPE.toString(), ORDER);
		PSELECT_MAP.put(RETURNTYPE.toString(), RETURNS);
	}
	public static final Integer ORDER_NOTOVER_INT = 0;
	public static final Integer ORDER_OVER_INT = 1;
	public static final Integer ORDER_PARTOVER_INT = 3;
	public static final String ORDER_OVER = "完成";
	public static final String ORDER_PARTOVER = "部分完成";
	public static final String ORDER_NOTOVER = "未完成";

	// 采购订单查询页面的map
	public static final Map<Integer, String> ORDER_MAP = Collections
			.synchronizedMap(new LinkedHashMap<Integer, String>());
	static {
		ORDER_MAP.put(2, "全部");
		ORDER_MAP.put(ORDER_NOTOVER_INT, ORDER_NOTOVER);
		ORDER_MAP.put(ORDER_PARTOVER_INT, ORDER_PARTOVER);
		ORDER_MAP.put(ORDER_OVER_INT, ORDER_OVER);
	}
	// 查询是正常还是作废
	public static final Map<Integer, String> STATUS_MAP = Collections
			.synchronizedMap(new LinkedHashMap<Integer, String>());
	static {
		
		STATUS_MAP.put(9, "全部");
		STATUS_MAP.put(0, "正常");
		STATUS_MAP.put(1, "已冲红");
		STATUS_MAP.put(2, "冲红单");
	}
	//期初应付初始化完成
	public static final Integer PAYINIT_FINISH=1;
}
