package com.systop.amol.sales.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 单态设计模式
 * 生成单号
 * 
 * @author 王会璞
 * 
 */
public final class SingleNumber {

	private static SingleNumber singleNumber = null;

	private SingleNumber() {
	}

	public static SingleNumber getSingleNumber() {
		if (singleNumber == null) {
			synchronized (SingleNumber.class) {
				if (singleNumber == null) {
					return new SingleNumber();
				}
			}
		}
		return singleNumber;
	}

	/**
	 * 生成单号，订单号的组成：两位年份两位月份两位日期+（流水号，不够8位前面补零），如：2011062300000001
	 * @param billNo 单子类型
	 * @param sno 单子号
	 * @return
	 */
	/*
	 * @SuppressWarnings("rawtypes") public String getOrder(Date date) {
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	 * StringBuilder sb = new StringBuilder(dateFormat.format(date)); dateFormat =
	 * new SimpleDateFormat("yyyy-MM-dd"); try { Date now =
	 * dateFormat.parse(dateFormat.format(date)); List list =
	 * query("select count(s) from Sales s where s.createTime>= ? ", now); long
	 * count = (Long) list.get(0); sb.append(fillZero(4, String.valueOf(count +
	 * 1))); } catch (ParseException e) { throw new RuntimeException("生成订单号失败"); }
	 * return sb.toString(); }
	 */
	public String getNumber(String billNo,String sno) {
		if (StringUtils.isBlank(sno)) {
			sno = billNo + "0001";
		} else {
			Long no = Long.parseLong(sno.substring(11, sno.length())) + 1;
			sno = billNo + fillZero(4, no.toString());
		}
		return sno;
	}

	/**
	 * 补零
	 * 
	 * @param length
	 *          补零后的长度
	 * @param source
	 *          需要补零的字符串
	 * @return
	 */
	private String fillZero(int length, String source) {
		StringBuilder result = new StringBuilder(source);
		for (int i = result.length(); i < length; i++) {
			result.insert(0, '0');
		}
		return result.toString();
	}
}
