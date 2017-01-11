package com.systop.amol.util;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


/**
 * <p>
 * </p>
 * @author 王会璞
 *
 */
public class NetTool {
	/**
	 * 以java方式发送post数据                           效率高
	 * @param urlpath
	 * @param params  请求参数，key为请求参数名称，value为参数值
	 * @return
	 * @throws Exception
	 */
	public static String sendPostRequest(String urlpath,Map<String,String> params,String encoding) throws Exception{
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(),encoding)).append('&');
		}
		sb.deleteCharAt(sb.length() - 1);
		byte[] date = sb.toString().getBytes();
		URL url = new URL(urlpath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(6 * 1000);// 如果超过android组件的阻塞时间（10秒），就会被系统回收
		conn.setDoOutput(true);//发送POST请求必须设置允许输出
		conn.setUseCaches(false);//不使用Cache
		conn.setRequestMethod("POST");	        
		conn.setRequestProperty("Connection", "Keep-Alive");//维持长连接
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Length", String.valueOf(date.length));
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		
		DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
		dataOutputStream.write(date);
		dataOutputStream.flush();
		dataOutputStream.close();
		int responseCode = conn.getResponseCode();
		if (200 == conn.getResponseCode() || 405 == conn.getResponseCode()) {
			String result = getTextContent(conn.getInputStream(),"UTF-8");
			return result;
		}
		return null;
	}
	
	/**
	 * 获取URL路径指定的的内容
	 * @param urlpath
	 * @return
	 * @throws Exception
	 */
	public static String getTextContent(InputStream inStream, String encoding) throws Exception  {
		byte [] data = readStream(inStream);
		return new String(data, encoding);

	}
	
	/**
	 * 获取URL路径指定的的内容
	 * @param urlpath
	 * @return
	 * @throws Exception
	 */
	public static String getGetContent(String urlpath,String encoding) throws Exception {
		URL url = new URL(urlpath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(6 * 1000);
		// 如果超过android组件的阻塞时间（10秒），就会被系统回收
		if (200 == conn.getResponseCode()) {
			InputStream inStream = conn.getInputStream();
			byte [] data = readStream(inStream);
			return new String(data, encoding);
		}
		return null;
	}
	
	/**
	 * 获取URL路径指定的输入流
	 * @param urlpath
	 * @return
	 * @throws Exception
	 */
	public static InputStream getContent(String urlpath,String encoding) throws Exception {
		URL url = new URL(urlpath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(6 * 1000);
		// 如果超过android组件的阻塞时间（10秒），就会被系统回收
		if (200 == conn.getResponseCode()) {
			InputStream inStream = conn.getInputStream();
			return inStream;
		}
		return null;
	}
	
	/**
	 * 获取输入流数据
	 * @param inStream  输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while (-1 != (len = inStream.read(buffer))) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}
}
