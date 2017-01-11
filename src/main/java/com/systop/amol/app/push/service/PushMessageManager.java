package com.systop.amol.app.push.service;

import com.systop.amol.app.push.model.PushMessage;
import com.systop.core.service.BaseGenericsManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 推送Service
 * 
 * @author 王会璞
 */
@Service
public class PushMessageManager extends BaseGenericsManager<PushMessage> {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	public List<PushMessage> getAdvertise(){
		return this.query("from PushMessage u where u.type=3 and u.startTime<now() and u.endTime>now() ");
	}
	public List<PushMessage> getByMerid(String merId){
		return this.query("from PushMessage u where u.picOfUser.id =  "+Integer.parseInt(merId));
	}
	public void deleteUpdateFile(String merId,String noUpdateIds){
		String param ="";
		String sql = "";
		if(StringUtils.isNotBlank(noUpdateIds)){
			String[] idArrays = noUpdateIds.split(",");
			for (String id:idArrays){
				param = param + Integer.parseInt(id)+",";
			}
			if(param.indexOf(",")!=-1){
				param = param.substring(0,param.lastIndexOf(","));
				param = "("+param+")";
			}
			sql = " delete from push_message where user_id = "+Integer.parseInt(merId)+" and id not in  "+param;
		}else{
			sql = " delete from push_message where user_id = "+Integer.parseInt(merId) ;
		}
		System.out.println("------------------------------sql="+sql);
		jdbcTemplate.execute(sql);
	}
//	/**
//	 * 推送信息
//	 * @param pushMessage
//	 */
//	public void push(PushMessage pushMessage){
//		/*
//		 * @brief	推送广播通知(IOS APNS)
//		 * 			message_type = 1 (默认为0)
//		 */
//		
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
////		String message = pushMessage.getContent()+"_"+DateUtil.convertDateToString(pushMessage.getStartTime(),"yyyy-MM-dd")+"_"+DateUtil.convertDateToString(pushMessage.getEndTime(),"yyyy-MM-dd")+"_"+pushMessage.getImageURL();
//		String message = pushMessage.getContent();
//		try {
//			
//			// 4. 创建请求类对象
//			PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
//			request.setDeviceType(3);	// device_type => 1: web 2: pc 3:android 4:ios 5:wp	
//			request.setMessageType(1);
//			//+"_"+pushMessage.getBusinessId()+
//			
//			request.setMessage("{\"title\":\""+pushMessage.getTitle()+"\",\"description\":\""+message+"\",\"aps\":{\"alert\":\"Hello Baidu Channel\"}}");
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
//		//苹果推送
//		try {
//			IosPush.pushs(AccessConnection.PushKeyValue(),message);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
////		pushIOS(pushMessage);
//	}
//	
//	/**
//	 * 苹果推送
//	 * @param pushMessage
//	 */
//	public void pushIOS(PushMessage pushMessage){
//		/*
//		 * @brief	推送广播通知(IOS APNS)
//		 * 			message_type = 1 (默认为0)
//		 */
//		
//		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
//		
//		// 2. 创建BaiduChannelClient对象实例
////		BaiduChannelClient channelClient = new BaiduChannelClient(pair);
//		//		倘若要使用开发版IOS推送，则采用如下配置
//				String url = "https://channel.iospush.api.duapp.com";
//				BaiduChannelClient channelClient = new BaiduChannelClient(pair, url);
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
//			request.setDeviceType(4);	// device_type => 1: web 2: pc 3:android 4:ios 5:wp	
//			request.setMessageType(1);
//			request.setMessage("{\"title\":\""+pushMessage.getTitle()+"\",\"description\":\""+pushMessage.getContent()+"_"+DateUtil.convertDateToString(pushMessage.getStartTime(),"yyyy-MM-dd")+"_"+DateUtil.convertDateToString(pushMessage.getEndTime(),"yyyy-MM-dd")+"\",\"aps\":{\"alert\":\"Hello Baidu Channel\"}}");
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
}