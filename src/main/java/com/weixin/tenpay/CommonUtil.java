package com.weixin.tenpay;

import com.systop.amol.sales.model.Sales;
import com.weixin.tenpay.util.ConstantUtil;
import com.weixin.tenpay.util.WXUtil;
import com.weixin.tenpay.util.XMLUtil;
import org.directwebremoting.util.IdGenerator;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/11/17.
 */
public class CommonUtil {


//    private static Logger log = LoggerFactory.getLogger(CommonUtil.class);
    /**
     * 发送https请求
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return 返回微信服务器响应的信息
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
           /* MyX509TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());*/
            // 从上述SSLContext对象中得到SSLSocketFactory对象
//            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
//            log.error("连接超时：{}", ce);
        } catch (Exception e) {
//            log.error("https请求异常：{}", e);
        }
        return null;
    }

    /**
     * 获取接口访问凭证
     *
     * @param appid 凭证
     * @param appsecret 密钥
     * @return
     *//*
    public static Token getToken(String appid, String appsecret) {
        Token token = null;
        String requestUrl = ConfigUtil.TOKEN_URL.replace("APPID", appid).replace("APPSECRET", appsecret);
        // 发起GET请求获取凭证
        JSONObject jsonObject = JSONObject.fromObject(httpsRequest(requestUrl, "GET", null));

        if (null != jsonObject) {
            try {
                token = new Token();
                token.setAccessToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getInt("expires_in"));
            } catch (JSONException e) {
                token = null;
                // 获取token失败
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return token;
    }
    public static String urlEncodeUTF8(String source){
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }*/
//重点：封装参数调用统一下单接口，生成prepay_id(预支付订单Id)

/**
 * 微信支付
 * @param orderId 订单编号
 * @param actualPay 实际支付金额
 * @return
 */
public static SortedMap generateOrderInfoByWeiXinPay(Sales sales, HttpServletRequest request) throws Exception{
//        String notify_url=propertiesService.WEI_XIN_NOTIFY_URL;//回调地址
        String uuid = WXUtil.getNonceStr();
        System.out.print("uuid"+uuid);
        SortedMap<String, String> signParams=new TreeMap<String, String>();
        signParams.put("appid", ConstantUtil.APP_ID);//app_id
        signParams.put("body","测试");//商品参数信息
        signParams.put("mch_id",ConstantUtil.PARTNER);//微信商户账号
        signParams.put("nonce_str",uuid);//32位不重复的编号
        if(sales.getSalesType().equals("charge")) {
            signParams.put("notify_url", "http://www.56zhipei.com:8090/coshop/orderPhoneAlipay/zxWxNotify.do");//北京时间时间戳
        }else{
            signParams.put("notify_url",  ConstantUtil.notify_url);//北京时间时间戳
        }
        signParams.put("out_trade_no",sales.getSalesNo());//订单编号
        signParams.put("spbill_create_ip",request.getRemoteAddr());//请求的实际ip地址
        System.out.println("-----------------------sales.getSamount()="+sales.getSamount()*100+"");
        String totalFee = sales.getSamount()*100+"";
        if(totalFee.indexOf(".")>-1){
            totalFee = totalFee.split("\\.")[0];
        }
        signParams.put("total_fee",totalFee);//支付金额 单位为分
        signParams.put("trade_type","APP");//付款类型为APP
        String sign=PayCommonUtil.createSign("UTF-8",signParams);//生成签名
        signParams.put("sign",sign);
        signParams.remove("key");//调用统一下单无需key（商户应用密钥）
        String requestXml=PayCommonUtil.getRequestXml(signParams);//生成Xml格式的字符串
        String result=CommonUtil.httpsRequest(ConstantUtil.UNIFIED_ORDER_URL,"POST",requestXml);//以post请求的方式调用统一下单接口

        System.out.println("-------------------result="+result);
       /* （注：ConstantUtil.UNIFIED_ORDER_URL=https://api.mch.weixin.qq.com/pay/unifiedorder;

        ）*/



//        返回的result成功结果取出prepay_id：

        Map map= XMLUtil.doXMLParse(result);
        String return_code=(String)map.get("return_code");
        String prepay_id=null;
        String returnSign=null;
        String returnNonce_str=null;
        if(return_code.contains("SUCCESS")){
        prepay_id=(String)map.get("prepay_id");//获取到prepay_id
        }
        StringBuffer weiXinVo=new StringBuffer();
        long currentTimeMillis=System.currentTimeMillis();//生成时间戳
        long second=currentTimeMillis/1000L;//（转换成秒）
        String seconds=String.valueOf(second).substring(0,10);//（截取前10位）
        SortedMap<String, String>signParam=new TreeMap<String, String>();
        signParam.put("appid",ConstantUtil.APP_ID);//app_id
        signParam.put("partnerid",ConstantUtil.PARTNER);//微信商户账号
        signParam.put("prepayid",prepay_id);//预付订单id
        signParam.put("package","Sign=WXPay");//默认sign=WXPay
        signParam.put("noncestr",uuid);//自定义不重复的长度不长于32位
        signParam.put("timestamp",seconds);//北京时间时间戳
        String signAgain=PayCommonUtil.createSign("UTF-8",signParam);//再次生成签名
        System.out.println("--------------------signAgin="+signAgain);
        signParam.put("sign",signAgain);
        weiXinVo.append("appid=").append(ConstantUtil.APP_ID).append("&partnerid=").append(ConstantUtil.PARTNER).append("&prepayid=").append(prepay_id).append("&package=Sign=WXPay").append("&noncestr=").append(uuid).append("&timestamp=").append(seconds).append("&sign=").append(signAgain);//拼接参数返回给移动端
        return signParam;
        }
}
