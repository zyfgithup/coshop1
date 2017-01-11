package com.systop.amol.sales.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 付款方式
 * 退款方式
 *
 */
public enum Payment {

	CASH{
		@Override
		public String getName() {
			return "现金";
		}
	},CASHADVANCE{
		@Override
		public String getName() {
			return "预收款";
		}
	},CARD{
		@Override
		public String getName() {
			return "代币卡";
		}
	},CARDADVANCE{
		@Override
		public String getName() {
			return "预划卡";
		}
	},WXPAY{
		@Override
		public String getName() {
			return "微信";
		}
	},
	jfSale{
		@Override
		public String getName() {
			return "积分";
		}
	},
	YEZF{
		@Override
		public String getName() {
			return "余额支付";
		}
	},
	ALIPAY{
		@Override
		public String getName() {
			return "支付宝";
		}
	},NO_PAY{
		@Override
		public String getName() {
			return "";
		}
	};
	
	public abstract String getName();

	/**
	 * 收款方式Map  query
	 */
	public static final Map<String, String> PAYMENT_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		PAYMENT_MAP.put("CASH", CASH.getName());
		PAYMENT_MAP.put("CASHADVANCE", CASHADVANCE.getName());
		PAYMENT_MAP.put("CARD", CARD.getName());
		PAYMENT_MAP.put("CARDADVANCE", CARDADVANCE.getName());
	}
	
	/**
	 * 收款方式Map   Cash
 	 */
	public static final Map<String, String> PAYMENT_CASH_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		PAYMENT_CASH_MAP.put("CASHADVANCE", CASHADVANCE.getName());
		PAYMENT_CASH_MAP.put("CASH", CASH.getName());
	}
	
	/**
	 * 收款方式Map   Card
 	 */
	public static final Map<String, String> PAYMENT_CARD_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		PAYMENT_CARD_MAP.put("CARD", CARD.getName());
		PAYMENT_CARD_MAP.put("CARDADVANCE", CARDADVANCE.getName());
	}
	
	/**
	 * 退款方式Map
	 */
	public static final Map<String, String> REFUND_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		REFUND_MAP.put("CASH", CASH.getName());
		REFUND_MAP.put("CARD", CARD.getName());
	}
}
