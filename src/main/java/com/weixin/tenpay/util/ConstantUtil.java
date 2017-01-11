package com.weixin.tenpay.util;

public class ConstantUtil {
	/**
	 * �̼ҿ��Կ��Ƕ�ȡ�����ļ�
	 */
	
	//��ʼ��
	public static String APP_ID = "wxb8f14b099a9201d0";//΢�ſ���ƽ̨Ӧ��id
	public static String PARTNER = "1382445602";//�Ƹ�ͨ�̻�
	public static String APP_SECRET = "db426a9829e4b49a0dcac7b4162da6b6";//Ӧ�ö�Ӧ��ƾ֤
	//Ӧ�ö�Ӧ����Կ
	public static String APP_KEY = "0c1520dc51ad86576463ae4078574165";
	public static String PARTNER_KEY = "qiantaiwuliu123qiantaiwuliu12300";//�̻��Ŷ�Ӧ����Կ
	public static String TOKENURL = "https://api.weixin.qq.com/cgi-bin/token";//��ȡaccess_token��Ӧ��url
	public static String GRANT_TYPE = "client_credential";//�����̶�ֵ 
	public static String EXPIRE_ERRCODE = "42001";//access_tokenʧЧ�����󷵻ص�errcode
	public static String FAIL_ERRCODE = "40001";//�ظ���ȡ������һ�λ�ȡ��access_tokenʧЧ,���ش�����
	public static String GATEURL = "https://api.weixin.qq.com/pay/genprepay?access_token=";//��ȡԤ֧��id�Ľӿ�url
	public static String ACCESS_TOKEN = "access_token";//access_token����ֵ
	public static String ERRORCODE = "errcode";//�����ж�access_token�Ƿ�ʧЧ��ֵ
	public static String SIGN_METHOD = "sha1";//ǩ���㷨���
	public static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//ǩ���㷨��// �ֵ
	//package����ֵ
	public static String packageValue = "bank_type=WX&body=%B2%E2%CA%D4&fee_type=1&input_charset=GBK&notify_url=http%3A%2F%2F127.0.0.1%3A8180%2Ftenpay_api_b2c%2FpayNotifyUrl.jsp&out_trade_no=2051571832&partner=1900000109&sign=10DA99BCB3F63EF23E4981B331B0A3EF&spbill_create_ip=127.0.0.1&time_expire=20131222091010&total_fee=1";
	public static String traceid = "testtraceid001";//�����û�id
	public static String notify_url = "http://www.56zhipei.com:8090/coshop/orderPhoneAlipay/wxNotify.do";
}
