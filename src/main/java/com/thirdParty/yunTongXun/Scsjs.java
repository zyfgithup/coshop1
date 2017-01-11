package com.thirdParty.yunTongXun;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 生成随机数
 * @author wanghuipu
 *
 */
public class Scsjs {
	/**
	 * 生成随机数方法
	 * 
	 * @return
	 */
	public static final String scsjs() {

		// String[] beforeShuffle = new String[] { "0", "1", "2", "3", "4", "5",
		// "6",
		// "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
		// "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
		// "Z" };
		String[] beforeShuffle = new String[] { "0", "1", "2", "3", "4", "5", "6",
				"7", "8", "9" };
		List list = Arrays.asList(beforeShuffle);
		Collections.shuffle(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
		}
		String afterShuffle = sb.toString();
		String result = afterShuffle.substring(5, 9);
		return result;
	}

}
