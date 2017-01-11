package com.systop.amol.base.product;

/**
 * 商品静态类
 * @author wanghuipu
 *
 */
public class ProductConstants {

	/** 平台商品 */
	public final static int PLATFORM = 0;
	
	/** 自营商品 */
	public final static int MERCHANT = 1;
	/**普通商品*/
	public final static String NORMAL = "0";
	/**特价商品*/
	public final static String SPECIAL = "1";
	/**
	 * 积分商品
	 */
	public final static int JI_FEN=3;
	
	/** 全部（包括商品和团购商品） */
	public final static int ALL = -1;
	
	/** 平台添加的商品模板，供分销商选择，规范分销商添加的商品数据及图片展示 */
	public final static int PRODUCT_TEMPLATE = 2;
	
	/** 普通商品 */
	public static boolean ORDINARY = false;

	/** 团购商品 */
	public static boolean GROUP_PURCHASE = true;
	/** 销量升序 */
	public static String SALES_VOLUME_ASC = "0";
	/*** 销量降序 */
	public static String SALES_VOLUME_DES = "1";
	/** 价格升序 */
	public static String PRICE_ASC = "0";
	/*** 价格降序 */
	public static String PRICE_DES = "1";

	
}
