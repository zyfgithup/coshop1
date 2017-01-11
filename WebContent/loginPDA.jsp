<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include  file="/common/taglibs.jsp"%>	
<html>
<head>
<title>用户POS机登录 -- 学员信息录入系统</title>
<link href="${ctx}/styles/style.css" type='text/css' rel='stylesheet'>
<style type="text/css">

.username{
	height:20px;
	width:150px;
	padding-left:21px;
    background-image:url(${ctx}/images/icons/user.gif);
    background-position:1px 1px;
    background-attachment:inherit;
	background-repeat:no-repeat;
	border: 1px solid #88ABDC;
	color:#11588E;
}
.password{
	height:20px;
	width:150px;
	padding-left:21px;
    background-image:url(${ctx}/images/icons/lock_go.gif);
	background-position:2px 1px;
	background-repeat:no-repeat;
	border: 1px solid #88ABDC;
	color:#11588E;
}
</style>
</head>
<body>
<fieldset>
 <legend>用户登录</legend>
 <table width="250" align="center">
 	<tr>
     	<td>
     	<c:if test="${param.login_error == 'code_error'}">
		   <div style="border:0px solid #a9a9a9; padding-left: 20px;" >
		       <img border="0" src="<c:url value='/images/icons/warning.gif'/>"/>&nbsp;<font color="#000000">验证码输入错误！</font>
		   </div>
		</c:if> 
		<c:if test="${param.login_error == 'user_psw_error' || param.login_error == '1'}">
		   <div style="border:0px solid #000000; padding-left: 20px;" >
		       <img border="0" src="<c:url value='/images/icons/warning.gif'/>" />&nbsp;<font color="#000000">用户名或密码错误，请重试！</font>
		   </div>
		</c:if> 
		<c:if test="${param.login_error == 'too_many_user_error'}">
		    <div style="border:0px solid #a9a9a9; padding-left: 20px;">
		       <img border="0" src="<c:url value='/images/icons/warning.gif'/>"/>&nbsp;<font color="#000000">该用户已经在其他地方登录了，请稍候。</font>
		   </div>
		</c:if>
     	</td>
     </tr>
  </table>
 <form id="loginForm" action='${ctx}/j_security_check' method="post" style="margin: 0;padding: 0">
   <input type="hidden" name="is_pda_login" value="yes">
   <table width="250" align="center">
   	 <tr>
        <td>
        &nbsp;用户名：<input name="j_username" type="text" class="username"/>
        </td>
     </tr>
     <tr>
        <td>
        &nbsp;密　码：<input name="j_password" type="password" class="password"/>
        </td>
     </tr>
     <tr>
        <td>
        &nbsp;验证码：<input type="text" name="j_captcha_response" style="width: 45px;margin-bottom: 3px;padding: 0;border: solid 1px #88ABDC;">
        <iframe id="captchaFrame" name="cf" src="<c:url value='/captcha.jpg' />" frameborder="0" scrolling="no" style="width:80px;height:25px;" marginheight="0" marginwidth="0"></iframe>
		<a href="#" onclick="frames['cf'].location='<c:url value="/captcha.jpg" />';">[刷]</a>
        </td>
     </tr>
     <tr>
        <td align="center">
          <input type="submit" class="button" value="登录">&nbsp;&nbsp;
          <input type="button" class="button" value="关闭" onclick="javascript:window.close();">
        </td>
     </tr>
   </table>
 </form>
</fieldset>
</body>
</html>
