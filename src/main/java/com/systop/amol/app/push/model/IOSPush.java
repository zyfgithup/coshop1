package com.systop.amol.app.push.model;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.KeyStore;
import java.util.regex.Pattern;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class IOSPush {
	
	public static void main(String[] args) {System.out.println();

		String keyPath = "D:/devPush.p12";
		String ksType = "PKCS12";
		String ksPassword = "123456";
		String ksAlgorithm = "SunX509";
		String deviceToken ="f2b2a052 13071e21 2386beca ddd7e3a3 0ce4ac6b d7fcfff1 518de64d cd28ef09";
		String serverHost = "gateway.sandbox.push.apple.com";
		int serverPort = 2195;

		try {
			InputStream certInput = new FileInputStream(keyPath);
			KeyStore keyStore = KeyStore.getInstance(ksType);
			keyStore.load(certInput, ksPassword.toCharArray());

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(ksAlgorithm);
			kmf.init(keyStore, ksPassword.toCharArray());

			SSLContext sslContext = SSLContext.getInstance("TLS");
			
			sslContext.init(kmf.getKeyManagers(), null, null);
			
			SSLSocketFactory socketFactory = sslContext.getSocketFactory();

			Socket socket = socketFactory.createSocket(serverHost, serverPort);

			StringBuilder content = new StringBuilder();

			String text = "this is a test.";

			content.append("{\"aps\":");
			content.append("{\"alert\":\"").append(text)
				.append("\",\"badge\":1,\"sound\":\"").append("ping1")
				.append("\"}");

			content.append(",\"cpn\":{\"t0\":").append(System.currentTimeMillis())
				.append("}");
			content.append("}");

			byte[] msgByte = makebyte((byte) 1, deviceToken, content.toString(),
				10000001);

			System.out.println(msgByte);

			socket.getOutputStream().write(msgByte);
			socket.getOutputStream().flush();

			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 组装apns规定的字节数组 使用增强型
	 * @param command
	 * @param deviceToken
	 * @param payload
	 * @return
	 * @throws IOException
	 */
	public void iosPush(String keyValue,String myCont){
		String keyPath = "D:/devPush.p12";
		String ksType = "PKCS12";
		String ksPassword = "123456";
		String ksAlgorithm = "SunX509";
		String deviceToken =keyValue ;
		String serverHost = "gateway.sandbox.push.apple.com";
		int serverPort = 2195;
System.out.println("deviceToken = "+deviceToken);
System.out.println("myCont = "+myCont);
		try {
			InputStream certInput = new FileInputStream(keyPath);
			KeyStore keyStore = KeyStore.getInstance(ksType);
			keyStore.load(certInput, ksPassword.toCharArray());

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(ksAlgorithm);
			kmf.init(keyStore, ksPassword.toCharArray());

			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), null, null);

			SSLSocketFactory socketFactory = sslContext.getSocketFactory();

			Socket socket = socketFactory.createSocket(serverHost, serverPort);

			StringBuilder content = new StringBuilder();

			String text = myCont;

			content.append("{\"aps\":");
			content.append("{\"alert\":\"").append(text)
				.append("\",\"badge\":1,\"sound\":\"").append("ping1")
				.append("\"}");

			content.append(",\"cpn\":{\"t0\":").append(System.currentTimeMillis())
				.append("}");
			content.append("}");

			byte[] msgByte = makebyte((byte) 1, deviceToken, content.toString(),
				10000001);

			System.out.println(msgByte);

			socket.getOutputStream().write(msgByte);
			socket.getOutputStream().flush();

			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static byte[] makebyte(byte command, String deviceToken,
		String payload, int identifer) {

	byte[] deviceTokenb = decodeHex(deviceToken);
	byte[] payloadBytes = null;
	ByteArrayOutputStream boas = new ByteArrayOutputStream();
	DataOutputStream dos = new DataOutputStream(boas);

	try {
		payloadBytes = payload.getBytes("UTF-8");
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
		return null;
	}

	try {
		dos.writeByte(command);
		dos.writeInt(identifer);// identifer
		dos.writeInt(Integer.MAX_VALUE);
		dos.writeShort(deviceTokenb.length);
		dos.write(deviceTokenb);
		dos.writeShort(payloadBytes.length);
		dos.write(payloadBytes);
		return boas.toByteArray();
	} catch (IOException e) {
		e.printStackTrace();
		return null;
	}
	}

	private static final Pattern pattern = Pattern.compile("[ -]");

	private static byte[] decodeHex(String deviceToken) {
	String hex = pattern.matcher(deviceToken).replaceAll("");

	byte[] bts = new byte[hex.length() / 2];
	for (int i = 0; i < bts.length; i++) {
		bts[i] = (byte) (charval(hex.charAt(2 * i)) * 16
			+ charval(hex.charAt(2 * i + 1)));
	}
	return bts;
	}

	private static int charval(char a) {
	if ('0' <= a && a <= '9')
		return (a - '0');
	else if ('a' <= a && a <= 'f')
		return (a - 'a') + 10;
	else if ('A' <= a && a <= 'F')
		return (a - 'A') + 10;
	else {
		throw new RuntimeException("Invalid hex character: " + a);
	}

	}
}
