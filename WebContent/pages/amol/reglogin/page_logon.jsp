<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
<meta name="viewport" id="viewport" content="width=device-width,initial-scale=1,user-scalable=0" />
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <title>登录</title>
	<base href="<%=basePath%>">

	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" href="<%=basePath%>/css/registered.css"
			type="text/css" />
	<style type="text/css">
	body,div,p,ul,li,ol,span,table,tr,td,dd,dt,dl,img{ margin:0 auto; padding:0;  list-style:none; list-style-type:none; color:#888888; font-size:100%;}
a{ cursor:pointer; text-decoration:none;}

img,form,fieldset,input{ border:none; display:inline;}
input,button,select,textarea{outline:none;}
textarea{resize:none;}


.login_box{ width:80%; margin-top:110px;}
.login_box li span{ font-size:12px; color:#ff7373;}
.logo img{ width:80% !important; display:block;margin-bottom: 30px}
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
.btngreen:hover{background:#007c72;  color:#fff;}

.wjmm{ display:block; line-height:50px; color:#fff; }
		
		body {
		-webkit-user-select: none;
		-webkit-text-size-adjust: none;
		background: url('${ctx }/images/bgg.png') repeat-x #2d877b;
		}
	</style>
	<script type="text/javascript" src="<%=basePath%>/script/jquery-1.8.2.js">
		</script>
  </head>
 <body scoll="no" >
	<form id="frmLogin" name="frmLogin"  method="post" action="xxx" style="height:900px;">
		<div class="login_box">
        	<div class="logo"><img src="${ctx }/images/logo3.png"></div>
            <ul>
        	<li>
        		<div class="textbg">
        		<input type="hidden" id="docId" value="${param.id}"></input>
        			<span><img src="${ctx }/images/phone.png"></span>
        			<input type="text" name="phone" id="phone" class="text01" value="手机号" onblur="jiancha();"  />
        		</div>
        	</li>
            <li><span id="psp"></span></li>
            <li><div class="textbg"><span><img src="${ctx }/images/password.png"></span><input type="password" id="password" name="password" class="text01" /></div></li>
        	<div style="clear:both;"></div>
            <li><div class="anniu">
                <a id="login-submit"  class="btngreen" onclick="sub();">登录</a>
            </div></li>
            <li><a href="${ctx }/pages/amol/reglogin/regist.jsp?id=${param.id}" float:left; id="regist" style=" display:block; margin-top:10px;">注册</a><a id="iApp" style="display: none; float:right; margin-top:10px;">患者端app下载(android)</a><a id="dApp" style="display: none; float:right; margin-top:10px;">患者端app下载(iphone)</a></li>
            <li></li>
			<li></li>
            </ul>
        </div>
    </form>
    <script type="text/javascript">
   	    var sUserAgent="";
		var bIsIphoneOs="";
		var bIsAndroid="";
		$(function(){
				 sUserAgent = navigator.userAgent.toLowerCase(); 
			     bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os"; 
			     bIsAndroid = sUserAgent.match(/android/i) == "android"; 
		})
    $("#phone").focus(function(){
    	$("#phone").val("");
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
	}
}
    function sub() {
    	if($("#phone").val()==""||$("#phone").val()==undefined||$("#phone").val()=="手机号")
    		{
    		alert("手机号不能为空！");
    		$("#phone").focus();
    		return false;
    		}
    	else if($("#password").val()==""||$("#password").val()==undefined)
    		{
    		alert("请输入密码！");
    		$("#password").focus();
    		return false;
    		}
    	else{
    		$.ajax({
   			 url: '${ctx}/appuser/login/login.do',
   			 type: 'post',
   			 async : false,
   			 dataType: 'json',
   			 data: {"username" : $("#phone").val(),'password':$("#password").val()},
   			 success: function(rst, textStatus){
						if(rst=="input"){
							alert("用户名或密码错误");
						}   
						else{
							alert("登录成功");
							$("#login-submit").attr("disabled",true);
							 $("#iApp").show();
							 $("#iApp").attr('href',"${ctx}/56zhipei.apk");
							 location.href = "${ctx}/56zhipei.apk";
						}
   			 }
         });
    }
}
    </script>    
</body>
</html>
