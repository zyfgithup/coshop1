package com.systop.amol.card;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 卡的管理常量类
 * 
 * @author Administrator
 * 
 */
public final class CardConstants {

	/**
	 * 卡状态,0-未绑定
	 */
	public static final String CARD_NOTOCCUPY = "0";

	/**
	 * 卡状态，1-正常
	 */
	public static final String CARD_AVAILABLE = "1";

	/**
	 * 卡状态，2-过期
	 */
	public static final String CARD_FAIL = "2";

	/**
	 * 卡状态，3-挂失
	 */
	public static final String CARD_LOSS = "3";

	/**
	 * 卡状态，4-冻结
	 */
	public static final String CARD_FREEZE = "4";

	/**
	 * 卡状态，5-注销
	 */
	public static final String CARD_OTHER = "5";
	
	/**
	 * 未冲红
	 */
	public static final String RED_NO = "0";
	
	/***
	 * 已冲红
	 */
	public static final String RED_YES = "1";
	
	/**
	 * 冲红单
	 */
	public static final String RED_RED = "2";

	/**
	 * 卡状态常量Map
	 */
	public static final Map<String, String> CARD_MAP_CARD = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		CARD_MAP_CARD.put(CARD_NOTOCCUPY, "未绑定");
		CARD_MAP_CARD.put(CARD_AVAILABLE, "正常");
		CARD_MAP_CARD.put(CARD_FAIL, "过期");
		CARD_MAP_CARD.put(CARD_LOSS, "挂失");
		CARD_MAP_CARD.put(CARD_FREEZE, "冻结");
		CARD_MAP_CARD.put(CARD_OTHER, "注销");
	}
}
