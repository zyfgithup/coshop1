//package com.systop.amol.app.push.service;
//
//import java.util.List;
//
//import javapns.Push;
//import javapns.notification.PushedNotification;
//import javapns.notification.ResponsePacket;
//
//public class IosPush {
//
////	public static void main(String[] args) {
////		List<String> list = new ArrayList<String>();
////		list.add("48ea3e9b9d73df584e3a9a618a8853f6b79c018d35caa906d4191e986948bd08");
////
////		try {
////			pushs(list);
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////	}
//
//	public static void pushs(List<String> list, String message) throws Exception {
//		try {
////			List<Device> inactiveDevices = Push.feedback("E:\\aps_production.p12",
////					"111111", true);
////			/* remove inactive devices from your own list of devices */
////			for (Device d : inactiveDevices) {
////				for (int i = 0; i < list.size(); i++) {
////					System.out.println("移除的  " + d.getToken());
////
////					if (d.getToken().equals(list.get(i))) {
////						list.remove(i);
////					}
////				}
////			}
//			List<PushedNotification> notifications = Push.alert(message,
//					"E:\\hytxldx.p12", "111111", true, list);
//			//F:\\hytxPush.p12   E:\\aps_production.p12
//			// Push.sound("1", "/Users/han/Desktop/push/aps_developement.p12",
//			// "123456", false, list);
//			// String payload =
//			// "{\"aps\":{\"alert\":\"Yougot a new message!\",\"badge\":5,\"sound\":\"beep.wav\"},\"acme1\":\"bar\",\"acme2\":42}"
//			// Push.payload(payload, "/Users/han/Desktop/push/aps_developement.p12",
//			// "123456", false, list)
//
//			for (PushedNotification notification : notifications) {
//				if (notification.isSuccessful()) {
//					/* Apple accepted the notification and should deliver it */
////					System.out.println("Push notification sent successfully to: "
////							+ notification.getDevice().getToken());
//
//				} else {
//					String invalidToken = notification.getDevice().getToken();
////					System.out.println("token: " + invalidToken);
//					/* Add code here to remove invalidToken from your database */
//
//					/* Find out more about what the problem was 找到更多关于的问题是什么 */
//					Exception theProblem = notification.getException();
//					theProblem.printStackTrace();
//
//					/*
//					 * If the problem was an error-response packet returned by Apple, get
//					 * it如果问题是一个错误响应数据包返回苹果,得到它
//					 */
//					ResponsePacket theErrorResponse = notification.getResponse();
//					if (theErrorResponse != null) {
////						System.out.println(theErrorResponse.getMessage());
//					}
//				}
//			}
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//}
