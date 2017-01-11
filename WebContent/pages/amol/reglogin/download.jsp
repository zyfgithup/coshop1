<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
	<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta name="viewport" id="viewport"
			content="width=device-width,initial-scale=1,user-scalable=0" />
		<title>沾沾乐APP下载</title>
		<base href="<%=basePath%>" />
		<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />

		<link rel="stylesheet" href="<%=basePath%>/css/v4/global.css"
			type="text/css" />
		<link rel="stylesheet" href="<%=basePath%>/css/v4/module.css"
			type="text/css" />
		<link rel="stylesheet" href="<%=basePath%>/css/registered.css"
			type="text/css" />
			<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
		<script type="text/javascript" src="${ctx}/script/jquery-1.8.2.js">
		</script>
		<style type="text/css">
		body,div,p,ul,li,ol,span,table,tr,td,dd,dt,dl,img{ margin:0 auto; padding:0;  list-style:none; list-style-type:none; color:#888888; font-size:100%;}
a{ cursor:pointer; text-decoration:none;}

img,form,fieldset,input{ border:none; display:inline;}
input,button,select,textarea{outline:none;}
textarea{resize:none;}


.login_box{ width:80%; margin-top:110px;}
.login_box li span{ font-size:12px; color:#ff7373;}
.logo img{ width:50% !important; display:block;}
.textbg{ border-radius:10px; border:2px solid #30ad73; height:40px; margin-top:20px;}
.textbg2{ border-radius:10px;  border:2px solid #30ad73; height:40px; margin-top:20px; width:40%; float:left;}
.textbg span{ width:40px; height:40px; border-right:1px solid #86d2c6; display:block; float:left; margin-right:10px;}
.textbg span img{ width:28px; height:28px; margin:5px; }
.text01{background-color:transparent; font-size:1.2em; height:40px; width:75%;  color:#30ad73;  }
.text02{background-color:transparent; font-size:1.2em; height:40px; width:75%; margin-left:3px; color:#30ad73;  }
.yzm{ float:right; display:block; font-size:1.2em; font-size:20px; color:#633; border-radius:70px;  padding:5px 20px; background:#FC0; }



/*按钮*/
.anniu{ margin-top:30px;}
.anniu a{ padding:3px 0; border-radius:10px; margin-top:10px;  font-size:1.5em;  display:block; text-align:center; line-height:50px;  
	-webkit-box-shadow: inset 0 1px 0 0 #007c72;
    box-shadow: inset 0 -2px 0 0 #007c72;
    cursor: pointer;}

.btngreen{background:#30ad73;  color:#fff;}
.btngreen:hover{background:#007c72; width:100%; color:#fff;}

.wjmm{ display:block; line-height:50px; color:#fff; }
		
body {
	-webkit-user-select: none;
	-webkit-text-size-adjust: none;
	background: url('${ctx }/images/bgg.png') repeat-x #2d877b;
}
*{margin:0; padding:0;} 
 	a{text-decoration: none;} 
 	img{max-width: 100%; height: auto;} 
 	.weixin-tip{display: none; position:fixed; left:0; top:0; bottom:0; background: rgba(0,0,0,0.8); filter:alpha(opacity=80);  height: 100%; width: 100%; z-index: 100;} 
	.weixin-tip p{text-align: center; margin-top: 10%; padding:0 5%;}

</style>

	</head>
	<br/><br/><br/>
	<body class="p4 w-ie6" scoll="no">
		<div class="weixin-tip"> 
 		<p> 
			<img src="${ctx }/images/weixin.png" alt="微信打开"/> 
 		</p> 
	</div> 
	<div class="logo">
					<img src="${ctx }/images/logo3.png"></img>
				</div>
	<br/><br/><br/><br/><br/><br/>
	<div class="anniu">
							<a id="login-submit" class="btngreen" onclick="sub();">立即下载</a>
						</div>
		<script type="text/javascript">
		
		var flag=false;
		var sUserAgent="";
		var bIsIphoneOs="";
		var bIsAndroid="";
$(function() {
	 sUserAgent = navigator.userAgent.toLowerCase(); 
     bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os"; 
     bIsAndroid = sUserAgent.match(/android/i) == "android"; 
     window.onload=function(){ 
	        var winHeight = $(window).height(); 
			function is_weixin() { 
			    var ua = navigator.userAgent.toLowerCase(); 
			    if (ua.match(/MicroMessenger/i) == "micromessenger") { 
			        return true; 
			    } else { 
			        return false; 
			    } 
			} 
			var isWeixin = is_weixin(); 
			if(isWeixin){ 
				$(".weixin-tip").css("height",winHeight); 
	            $(".weixin-tip").show(); 
			} 
      }

});
function sub() {
	if(bIsIphoneOs){
		location.href="https://itunes.apple.com/cn/app/apple-store/id1147013126";
	}
	if(bIsAndroid){
		location.href="${ctx}/56zhipei.apk";
	}
}
</script>
	</body>
</html>
