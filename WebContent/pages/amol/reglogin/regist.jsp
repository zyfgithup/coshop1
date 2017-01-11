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
		<title>注册</title>
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


.login_box{ width:80%; margin-top:80px;}
.login_box li span{ font-size:12px; color:#ff7373;}
.logo img{ width:80% !important; display:block; margin-top:20px;}
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

    box-shadow: inset 0 -2px 0 0 #007c72;
    cursor: pointer;}

.btngreen{background:#30ad73;  color:#fff;}
.btngreen:hover{background:#007c72;  color:#fff;}

.wjmm{ display:block; line-height:50px; color:#fff; }
		
*{margin:0; padding:0;} 
 	a{text-decoration: none;} 
 	img{max-width: 100%; height: auto;} 
 	.weixin-tip{display: none; position:fixed; left:0; top:0; bottom:0; background: rgba(0,0,0,0.8); filter:alpha(opacity=80);  height: 100%; width: 100%; z-index: 100;} 
	.weixin-tip p{text-align: center; margin-top: 10%; padding:0 5%;}
</style>
	</head>
	<body class="p4 w-ie6" scoll="no">
		<div class="weixin-tip"> 
 		<p> 
			<img src="${ctx}/images/weixin.png" alt="微信打开"/> 
 		</p> 
	</div> 
		<input type="hidden" id="docId" value="${param.yqm}"></input>
		<form name="regform" method="post" action="#" id="regform"
			style="height: 900px;">
			<div class="login_box">
				<div class="logo">
					<img src="${ctx}/images/logo3.png" width="300px" height="250px"></img>
				</div>
				<ul>
					<li>
						<div class="textbg">
							<span><img src="${ctx }/images/phone.png"></img>
							</span>
							<input type="text" name="phone" id="phone" class="text01" 
								value="手机号" onblur="jiancha();" />
						</div>
					</li>
					<li>
						<span id="psp" ></span>
					</li>
					<li>
						<div class="textbg">
							<span><img src="${ctx }/images/password.png"></img>
							</span>
							<input type="password" class="text01" id="password"
								name="password" value="" />
						</div>
					</li>
					<li>
						<span id="msp" ></span>
					</li>
					
					<li>	
						<div >
						<a  class="yzm" id="btn" onclick="sendSms()" style="margin-top:25px;" 
							value="获取验证码">获取验证码</a></div>
	
						<div class="textbg2">
							<input style="" type="text" id="yzm" class="text02" value="验证码" />
						</div>
					</li>
					<div style="clear:both;"></div>
					<li>
						<div class="anniu">
							<a id="login-submit" class="btngreen" onclick="sub();">立即注册</a>
						</div>
					</li>
					<li><a id="logon" style="display: block; float:left; margin-top:10px;" href="${ctx }/pages/amol/reglogin/page_logon.jsp?id=${param.id}">已注册？【登录】</a><a id="iApp" style="display: none; float:right; margin-top:10px;">沾沾乐下载</a></li>
				</ul>
				</div>
		</form>
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
var url = ""
	$("#phone").focus(function() {
		$("#phone").val("");
	})
	$("#password").focus(function() {
		$("#password").val("");
	})
	$("#yzm").focus(function() {
		$("#yzm").val("");
	})
	$("#password").blur(function() {
		var str=   $("#password").val().replace(/\s+/g,""); 
		if ($("#password").val() == "" || $("#password").val() == "密码") {
			$("#msp").html("密码不能为空！！！");
			return false;
			$("#password").val("")
		} else if(str.length!=6) {
			$("#msp").html("密码长度必须是六位！");
			return false;
		}
		else{
			$("#msp").html("");
		}
	});
function jiancha() {
	if ($("#phone").val() == "") {
		$("#psp").html("手机号不能为空！！！");
		$("#phone").val("手机号");
		return false;
	} else if (!(/^1[3|5|8|7][0-9]\d{4,8}$/.test($("#phone").val()))) {
		$("#psp").html("不是完整的11位手机号或者正确的手机号前七位");
		// alert("不是完整的11位手机号或者正确的手机号前七位"); 
		$("#phone").focus();
		return false;
	} else {
		$.ajax({
			 url: '${ctx}/appuser/manager/getUserByPhone.do',
			 type: 'post',
			 async : false,
			 dataType: 'json',
			 data: {"phone" : $("#phone").val()},
			 success: function(rst, textStatus){
				 exist = rst.exist;
				if (exist) {
					$('#psp').html('<b>用户名'+$("#phone").val()+'</b>已存在!');
      	    	}else {
      	    		$('#psp').html('');
      	    	}
			 }
     	   });
	}
}

var wait = 60;
function sendSms() {
	if ($("#phone").val() == "" || $("#phone").val() == "手机号") {
		alert("请输入手机号");
		$("#phone").focus();
		return false;
	} 
	 else if (!(/^1[3|5|8|7][0-9]\d{4,8}$/.test($("#phone").val()))) {
		$("#psp").html("不是完整的11位手机号或者正确的手机号前七位");
		// alert("不是完整的11位手机号或者正确的手机号前七位"); 
		$("#phone").focus();
		return false;
	}else {
		$.ajax({
			 url: '${ctx}/appuser/manager/getUserByPhone.do',
			 type: 'post',
			 async : false,
			 dataType: 'json',
			 data: {"phone" : $("#phone").val()},
			 success: function(rst, textStatus){
				 exist = rst.exist;
				if (exist) {
					$('#psp').html('<b>用户名'+$("#phone").val()+'</b>已存在!');
					flag=true;
					return;
     	    	}else {
     	    		$('#psp').html('');
     	    		flag=false;
     	    	}
			 }
    	   });
		
	if(!flag)
		{$.ajax({
			 url: '${ctx}/appuser/user/gainValidateCode.do',
			 type: 'post',
			 async : false,
			 dataType: 'json',
			 data: {"phone" : $("#phone").val()},
			 success: function(rst, textStatus){
				 result = rst.result;
				 if(result){
					 time();
				 }
			 }
    	   });
		}
	}
}
function time() {
	if (wait == 0) {
		$("#btn").removeAttr("disabled");
		$("#btn").html("获取验证码");
		wait = 60;
	} else {
		$("#btn").attr("disabled", true);
		$("#btn").html("重新发送(" + wait + ")");
		wait--;
		setTimeout(function() {
			time()
		}, 1000)
	}
}

function sub() {
	if(flag)
		{
		$("#psp").html("手机号已经注册！！！");
		$("#phone").focus();
		return false;
		}
	if ($("#yzm").val() == "" || $("#yzm").val() == "验证码"
			|| $("#phone").val() == "") {
		alert("不符合规则！！！");
		return false;
	}else if($("#password").val().replace(/\s+/g,"").length!=6)
		{
		alert("密码的长度必须是六位！");
		return false;
		}
	else {
		$.ajax({
			 url: '${ctx}/appuser/user/validateCode.do',
			 type: 'post',
			 async : false,
			 dataType: 'json',
			 data: {"phone" : $("#phone").val(),'validateCode':$("#yzm").val()},
			 success: function(rst, textStatus){
				 result = rst.result;
				 if(result){
					 register();
					 
				 }else{
					 alert("验证码校验失败");
					 $("#yzm").focus();
				 }
			 }
   	   });
	}
}
function register(){
	$.ajax({
		 url: '${ctx}/appuser/register/h5Register.do',
		 type: 'post',
		 async : false,
		 dataType: 'json',
		 data: {"username" : $("#phone").val(),'password':$("#password").val(),'id':$("#docId").val()},
		 success: function(rst, textStatus){
			 if(rst=="input"){
					alert("用户名或密码错误");
				}   
				else{
					alert("注册成功");
				    if(bIsIphoneOs){
						location.href="https://itunes.apple.com/cn/app/apple-store/id1147013126";
					}else{
					$("#login-submit").attr("disabled",true);
					 $("#iApp").show();
					 $("#iApp").attr('href',"${ctx}/56zhipei.apk");
					 location.href = "${ctx}/56zhipei.apk";
				}
		 }
		 }
	   });
}
		</script>
	</body>
</html>
