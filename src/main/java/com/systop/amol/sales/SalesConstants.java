package com.systop.amol.sales;

import com.systop.amol.sales.utils.Payment;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 销售管理常量类
 * 
 * @author 王会璞
 * 
 */
public final class SalesConstants {

	/**
	 * 状态常量，0订单
	 */
	public static final String ORDERS = "0";
	
	/**
	 * 状态常量，1出库单
	 */
	public static final String SALES = "1";
	
	/**
	 * 状态常量，2退货单
	 */
	public static final String RETURNS = "2";
	
	/**
	 * 状态常量，3回款单
	 */
	public static final String REIMBURSEMENT = "3";
	
	/**
	 * 状态常量，应收款单（应收和实收不相等的出库单）
	 */
	public static final String DOCORS = "4";
	
	/**
	 * 状态常量，期初单
	 */
	public static final String BEGINNING = "5";
	
	/** 请选择 */
	public static final String PLEASESELECT = "-1";
	
	/**
	 * 出库状态，0货物未出库
	 */
	public static final String SALES_NO = "0";

	/**
	 * 出库状态，1货物部分出库
	 */
	public static final String SALES_PART = "1";
	
	/**
	 * 出库状态，2货物完成出库
	 */
	public static final String SALES_COMPLETE = "2";
	/**
	 * 分销常量，1订单分销
	 */
	public static final int fxNormal = 1;
	
	/**
	 * 分销常量，2退单分销
	 */
	public static final int fxReturn= 2;

	/**
	 * 退货状态，0未退货
	 */
	public static final String NOT_RETURN = "0";

	/**
	 * 退货状态，1部分退货
	 */
	public static final String POTR = "1";
	
	/**
	 * 退货状态，2完全退货 
	 */
	public static final String FULL_RETURN = "2";
	
	/** 退款状态，退款中 */
	public static final String REFUND = "0";
	/** 退款状态，退款成功 */
	public static final String REFUND_SUCCESS = "1";
	/** 退款状态，退款失败 */
	public static final String REFUND_FAILURE = "2";
	
	/**
	 * 生成销售订单号
	 */
	public static final String ORDERS_BILL = "XD-";
	
	/**
	 * 生成销售出库单号
	 */
	public static final String SALES_BILL = "XS-";
	
	/**
	 * 生成销售退货单号
	 */
	public static final String RETURNS_BILL = "XT-";
	
	/**
	 * 生成销售回款单号
	 */
	public static final String REIMBURSEMENT_BILL = "HK-";
	
	/**
	 * 全部
	 */
	public static final Integer ALL = 0;
	
	/**
	 * 正常
	 */
	public static final Integer NORMAL = 1;
	
	/**
	 * 已冲红
	 */
	public static final Integer RED = 2;
	
	/**
	 * 冲红单
	 */
	public static final Integer REDBILL = 3;

	/**
	 * 没有回款单为出库单回款  0
	 */
	public static final String NOT_RE = "0";
	
	/**
	 * 有回款单为出库单回款     1
	 */
	public static final String YES_RE = "1";

	/**
	 * 未收款标识
	 */
	public static final String NOT_RECEIVED = "0";
	
	/**
	 * （未收 已收）全部    款标识
	 */
	public static final String ALL_ECEIVED = "1";
	
	/** 期初应收锁定标识 **/
	public static final Integer INIT_LOCKING = 0;
	/** 期初应收正常标识 **/
	public static final Integer INIT_NORMAL = 1;
	
	
	/**
	 * 收款情况Map
	 */
	public static final Map<String, String> RECEIVE_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		RECEIVE_MAP.put(NOT_RECEIVED, "未收");
		RECEIVE_MAP.put(ALL_ECEIVED, "全部");
	}
	
	/**
	 * 状态常量Map
	 */
	public static final Map<String, String> BILL_STATUS_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		BILL_STATUS_MAP.put(ORDERS, "订单");
		BILL_STATUS_MAP.put(SALES, "出库单");
		BILL_STATUS_MAP.put(RETURNS, "退货单");
		BILL_STATUS_MAP.put(REIMBURSEMENT, "回款单");
	}
	
	
	/**
	 * 出库常量Map
	 */
	public static final Map<String, String> SALES_STATUS_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		SALES_STATUS_MAP.put(SALES_NO, "未出库");
		SALES_STATUS_MAP.put(SALES_PART, "部分出库");
		SALES_STATUS_MAP.put(SALES_COMPLETE, "完成出库");
	}
	
	/**
	 * 出库常量Map S
	 */
	public static final Map<String, String> SALES_STATUS_MAP_S = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		SALES_STATUS_MAP_S.put(SALES_NO, "<font color='red'>未出库</font>");
		SALES_STATUS_MAP_S.put(SALES_PART, "<font color='blue'>部分出库</font>");
		SALES_STATUS_MAP_S.put(SALES_COMPLETE, "<font color='green'>完成出库</font>");
	}
	
	/**
	 * 退货常量Map
	 */
	public static final Map<String, String> RETURN_STATUS_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		RETURN_STATUS_MAP.put(NOT_RETURN, "未退货");
		RETURN_STATUS_MAP.put(POTR, "部分退货");
		RETURN_STATUS_MAP.put(FULL_RETURN, "完全退货");
	}
	
	/**
	 * 退货常量Map S
	 */
	public static final Map<String, String> RETURN_STATUS_MAP_S = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		RETURN_STATUS_MAP_S.put(NOT_RETURN, "未退货");
		RETURN_STATUS_MAP_S.put(POTR, "<font color='blue'>部分退货</font>");
		RETURN_STATUS_MAP_S.put(FULL_RETURN, "<font color='red'>完全退货</font>");
	}

	/** 冲红 */
	public static final Map<Integer, String> STATUS_MAP = Collections
			.synchronizedMap(new LinkedHashMap<Integer, String>());
	static {
		
		STATUS_MAP.put(ALL, "全部");
		STATUS_MAP.put(NORMAL, "正常");
		STATUS_MAP.put(RED, "已冲红");
		STATUS_MAP.put(REDBILL, "冲红单");
	}
	
	/** 单子类型 */
	public static final Map<String, String> LIST_TYPE_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		
		//LIST_TYPE_MAP.put(PLEASESELECT, "请选择");
		LIST_TYPE_MAP.put(ORDERS, "订单");
//		LIST_TYPE_MAP.put(SALES, "出库单");
		LIST_TYPE_MAP.put(RETURNS, "退货单");
			
	}
	
	/** 出库单  订单 */
	public static final Map<String, String> SALES_ORDER_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		SALES_ORDER_MAP.put(SALES, "出库单");
		SALES_ORDER_MAP.put(ORDERS, "订单");
	}
	
	/** 退货单  出库单 */
	public static final Map<String, String> RETURNS_SALES_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		RETURNS_SALES_MAP.put(RETURNS, "退货单");
		RETURNS_SALES_MAP.put(SALES, "出库单");
	}
	/** 回款单  出库单*/
	public static final Map<String, String> RECEIVE_SALES_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		RECEIVE_SALES_MAP.put(REIMBURSEMENT, "回款单");
		RECEIVE_SALES_MAP.put(SALES, "出库单");
	}
	/** 支付方式 */
	public static final Map<String, String> PAYMENT_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		PAYMENT_MAP.put("", "全部");
		PAYMENT_MAP.put(Payment.CASH.toString(), Payment.CASH.getName());
		PAYMENT_MAP.put(Payment.ALIPAY.toString(), Payment.ALIPAY.getName());
		PAYMENT_MAP.put(Payment.WXPAY.toString(), Payment.WXPAY.getName());
		PAYMENT_MAP.put(Payment.jfSale.toString(), Payment.jfSale.getName());
	}
	/** 回款单  出库单  期初应收单*/
	public static final Map<String, String> RECEIVE_SALES_RECEIVEInit_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());

	static {
		RECEIVE_SALES_RECEIVEInit_MAP.put(REIMBURSEMENT, "回款单");
		RECEIVE_SALES_RECEIVEInit_MAP.put(SALES, "出库单");
		RECEIVE_SALES_RECEIVEInit_MAP.put(BEGINNING, "期初应收单");
	}
}
