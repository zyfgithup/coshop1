package com.systop.amol.sales.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 发货状态
 *
 */
public enum DeliveryStatus {

	NOT_SHIPPED{
		@Override
		public String getName() {
			return "未发货";
		}
	},ALREADY_SHIPPED{
		@Override
		public String getName() {
			return "已发货";
		}
	},;
	
	public abstract String getName();

	/**
	 * 收款方式Map  query
	 */
	public static final Map<String, String> DELIVERY_STATUS_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		DELIVERY_STATUS_MAP.put("NOT_SHIPPED", NOT_SHIPPED.getName());
		DELIVERY_STATUS_MAP.put("ALREADY_SHIPPED", ALREADY_SHIPPED.getName());
	}
}
