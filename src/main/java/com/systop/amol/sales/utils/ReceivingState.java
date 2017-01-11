package com.systop.amol.sales.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用户（App用户）收货状态
 *
 */
public enum ReceivingState {

	GOODS_NOT_RECEIVED{
		@Override
		public String getName() {
			return "未收货";
		}
	},RECEIVED_GOODS{
		@Override
		public String getName() {
			return "已收货";
		}
	},;
	
	public abstract String getName();

	/**
	 * 收款方式Map  query
	 */
	public static final Map<String, String> DELIVERY_STATUS_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		DELIVERY_STATUS_MAP.put("GOODS_NOT_RECEIVED", GOODS_NOT_RECEIVED.getName());
		DELIVERY_STATUS_MAP.put("RECEIVED_GOODS", RECEIVED_GOODS.getName());
	}
}
