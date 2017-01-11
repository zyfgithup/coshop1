package com.systop.amol.sales.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 商家是否同意退货的状态
 *
 */
public enum MerchantIsRutrn {

	MERCHANT_YES_TUI_HUO{
		@Override
		public String getName() {
			return "商家同意退货";
		}
	},MERCHANT_NO_TUI_HUO{
		@Override
		public String getName() {
			return "商家不同意退货";
		}
	},;
	
	public abstract String getName();

	/**
	 * 收款方式Map  query
	 */
	public static final Map<String, String> DELIVERY_STATUS_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		DELIVERY_STATUS_MAP.put("MERCHANT_YES_TUI_HUO", MERCHANT_YES_TUI_HUO.getName());
		DELIVERY_STATUS_MAP.put("MERCHANT_NO_TUI_HUO", MERCHANT_NO_TUI_HUO.getName());
	}
}
