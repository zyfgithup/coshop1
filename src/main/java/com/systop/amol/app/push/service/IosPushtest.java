//package com.systop.amol.app.push.service;
//import javapns.back.PushNotificationManager;
//import javapns.back.SSLConnectionHelper;
//import javapns.data.Device;
//import javapns.data.PayLoad;
//
///**
// * 苹果推送
// * @author 王会璞
// *
// */
//public class IosPushtest {
//
//	public static void main(String[] args) {
//		push("");
//	}
//	
//	public static void push(String message) {
//			// 获取客户端deviceToken
//			// 81ba0f73 0554fbde b1cdd6da 07446b9b 183c766b 39b71111 03209bf7
//			// 8a2d7acb
//			// 01a88719 c2a835ec 6ce61dbd 3240d44a 231bd180 976bd74a 3946be63
//			// 0c874620
//			// 4a13693d acbb89a1 e91d4f9a 5061e964 0c0b8a62 1b829224 9fca4df3
//			// 60485e31
//		  //48ea3e9b9d73df584e3a9a618a8853f6b79c018d35caa906d4191e986948bd08
//		//4a13693dacbb89a1e91d4f9a5061e9640c0b8a621b8292249fca4df360485e31
//			String deviceToken = "48ea3e9b9d73df584e3a9a618a8853f6b79c018d35caa906d4191e986948bd08";
//			System.out.println("Push Start deviceToken:" + deviceToken);
//			// 定义消息模式
//			PayLoad payLoad = new PayLoad();
//			try {
//				payLoad.addAlert(message);
//				payLoad.addBadge(1);
//				payLoad.addSound("default");
//				// 注册deviceToken
//				PushNotificationManager pushManager = PushNotificationManager
//						.getInstance();
//				pushManager.addDevice("iPhone", deviceToken);
//				// 连接APNS
//				String host = "gateway.sandbox.push.apple.com";
//				int port = 2195;
//				System.out.println("star"+pushManager);
//				String certificatePath = ("F:\\hytxPush.p12");// ("/Users/han/Desktop/push/aps_developement.p12");///aps_developer_identity.p12");//aps_developement//aps_production
//				// certificatePath *.p12地址
//				String certificatePassword = "111111";
//				pushManager.initializeConnection(host, port, certificatePath,
//						certificatePassword,
//						SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
//				System.out.println("end");
//				// 发送推送
//				Device client = pushManager.getDevice("iPhone");
//				pushManager.sendNotification(client, payLoad);
//				// 停止连接APNS
//				pushManager.stopConnection();
//				// deviceToken
//				pushManager.removeDevice("iPhone");
//				System.out.println("Push End");
//			} catch (Exception e) {
//				System.out.println("Excepiton = "+e.getMessage());
//			} 
//
//	}
//}
