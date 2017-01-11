package com.thirdParty.yunTongXun;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import java.util.HashMap;
import java.util.Set;

/**
 * 发送手机短信验证码
 *
 */
public class SDKTestSendTemplateSMS {
	public static void sendSmss(String phone,String template,String param,String smgFreeSignName){
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23449502","b2bbe24eab5e3d8803d14a17ece23d6e");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName(smgFreeSignName);
		req.setSmsParamString(param);
		req.setRecNum(phone);
		req.setSmsTemplateCode(template);
		AlibabaAliqinFcSmsNumSendResponse rsp = null;
		try {
			rsp = client.execute(req);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		//System.out.println(rsp.getBody());
	}
	/**
	 * 云通讯短信发送验证码方法，依赖包为CCP_REST_SMS_SDK_JAVA_v2.6.3r.jar
	 * @param phone 手机号字符串，多个手机号用逗号隔开
	 * @param smsTemplateId 短信模板id
	 * @param str String数组 new String[]{"6532","5"});验证码：6532；5分钟内输入有效 
	 */
	public static void smsSend(String phone, String smsTemplateId, String[] str){
		HashMap<String, Object> result = null;

		//初始化SDK
		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
		//******************************注释*********************************************
		//*初始化服务器地址和端口                                                       *
		//*沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
		//*生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883");       *
		//*******************************************************************************
		restAPI.init("sandboxapp.cloopen.com", "8883");
		
		//******************************注释*********************************************
		//*初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN     *
		//*ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
		//*参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。                   *
		//*******************************************************************************
		restAPI.setAccount("aaf98f8952f7367a01532fb6b8136362", "b6c15284ee914c4c86041e26e4e0fd0f");
		
		
		//******************************注释*********************************************
		//*初始化应用ID                                                                 *
		//*测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID     *
		//*应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
		//*******************************************************************************
		restAPI.setAppId("aaf98f895350b6880153547c930c069e");
		
		
		//******************************注释****************************************************************
		//*调用发送模板短信的接口发送短信                                                                  *
		//*参数顺序说明：                                                                                  *
		//*第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号                          *
		//*第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。    *
		//*系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
		//*第三个参数是要替换的内容数组。																														       *
		//**************************************************************************************************
		
		//**************************************举例说明***********************************************************************
		//*假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为           *
		//*result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});																		  *
		//*则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入     *
		//*********************************************************************************************************************
		result = restAPI.sendTemplateSMS(phone, smsTemplateId, str);
		
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
				//SDKTestGetSubAccounts result={statusCode=000000, data={templateSMS={smsMessageSid=201505050009511815906, dateCreated=20150505000951}}}
				//templateSMS = {smsMessageSid=201505050009511815906, dateCreated=20150505000951}
			}
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
		}
	}
}
