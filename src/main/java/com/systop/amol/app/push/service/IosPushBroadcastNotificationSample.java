//package com.systop.amol.app.push.service;
//
//import com.baidu.yun.channel.auth.ChannelKeyPair;
//import com.baidu.yun.channel.client.BaiduChannelClient;
//import com.baidu.yun.channel.exception.ChannelClientException;
//import com.baidu.yun.channel.exception.ChannelServerException;
//import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
//import com.baidu.yun.channel.model.PushBroadcastMessageResponse;
//import com.baidu.yun.core.log.YunLogEvent;
//import com.baidu.yun.core.log.YunLogHandler;
//
//public class IosPushBroadcastNotificationSample {
//
//	public static void main(String[] args) {
//		
//		/*
//		 * @brief	推送广播通知(IOS APNS)
//		 * 			message_type = 1 (默认为0)
//		 */
//		
//		// 1. 设置developer平台的ApiKey/SecretKey
//		String apiKey = "aMSpABnRwACUBB2GpHbGlFGe";
//		String secretKey = "G4r1v1iHWNhWMDxYWu1adHTnrVQ7ZkGn";
//		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
//		
//		// 2. 创建BaiduChannelClient对象实例
//		BaiduChannelClient channelClient = new BaiduChannelClient(pair);
//		//		倘若要使用开发版IOS推送，则采用如下配置
//		//		String url = "https://channel.iospush.api.duapp.com";
//		//		BaiduChannelClient channelClient = new BaiduChannelClient(pair, url);
//		
//		// 3. 若要了解交互细节，请注册YunLogHandler类
//		channelClient.setChannelLogHandler(new YunLogHandler() {
//			@Override
//			public void onHandle(YunLogEvent event) {
//				System.out.println(event.getMessage());
//			}
//		});
//		
//		try {
//			
//			// 4. 创建请求类对象
//			PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
//			request.setDeviceType(3);	// device_type => 1: web 2: pc 3:android 4:ios 5:wp	
//			request.setMessageType(1);
//			request.setMessage("{\"title\":\"慧游天下\",\"description\":\"慧游天下，青岛旅游景区，风景秀美，海边清风微拂过，站在海边看着\",\"aps\":{\"alert\":\"Hello Baidu Channel\"}}");
// 			
//			// 5. 调用pushMessage接口
//			PushBroadcastMessageResponse response = channelClient.pushBroadcastMessage(request);
//				
//			// 6. 认证推送成功
//			System.out.println("push amount : " + response.getSuccessAmount()); 
//			
//		} catch (ChannelClientException e) {
//			// 处理客户端错误异常
//			e.printStackTrace();
//		} catch (ChannelServerException e) {
//			// 处理服务端错误异常
//			System.out.println(
//					String.format("request_id: %d, error_code: %d, error_message: %s" , 
//						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()
//						)
//					);
//		}
//		
//	}
//	
//}
