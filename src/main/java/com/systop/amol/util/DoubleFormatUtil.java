package com.systop.amol.util;

/**
 * 将Double类型的对象转为double型
 * @author 王会璞
 *
 */
public class DoubleFormatUtil {

	
	/**
	 * 将Double类型的对象转为double型
	 * @param doubleValue Double对象
	 * @return
	 */
	public static double format(Double doubleValue){
		if(doubleValue == null){
			return 0.0d;
		}else{
			try {
				return doubleValue.doubleValue();
			} catch (Exception e) {
				System.out.println(DoubleFormatUtil.class.getName()+"："+e.getMessage());
				return 0.0d;
			}
		}
	}
}
