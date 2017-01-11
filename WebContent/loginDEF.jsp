<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include  file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户登录 -- 物流信息管理系统</title>
<link href="${ctx}/styles/style.css" type='text/css' rel='stylesheet'>
<link href="${ctx}/styles/login/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/iepngfix_tilebg.js"></script>
<script src="${ctx}/scripts/swfobject_modified.js" type="text/javascript"></script>
<style type="text/css">
	img, div, table{ behavior: url(styles/login/iepngfix.htc); }
</style>
<script src="${ctx}/scripts/login/iepngfix_tilebg.js" type="text/javascript" ></script>

<style type="text/css">
body {
	margin: 0px;
	font-size: 12px;
	color: #12588E;
	background-image: url(${ctx}/images/amol/000.jpg);
}

.inputborder {
	border: 1px solid #88ABDC;
	height: 20px;
	line-height:18px;
	color:#C2060F
}
.STYLE3 {
	font-family:serif;
	color: #11588E;
	font-weight: bold;
}
.username{
	height:20px;
	width:85px;
	line-height:16px;
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
	width:85px;
	line-height:16px;
	padding-left:21px;
    background-image:url(${ctx}/images/icons/lock_go.gif);
	background-position:2px 1px;
	background-repeat:no-repeat;
	border: 1px solid #88ABDC;
	color:#11588E;
}
.menu {
	font-size: 14px;
	font-weight: bold;
	color:#286e94;
}
</style>
</head>
<body>
<p>&nbsp;</p>
<p>&nbsp;</p>
<table width="600" height="500" border="0" align="center" cellpadding="0" cellspacing="0" style="background-image:url(${ctx}/images/amol/login_box4.png)" >
  <tr>
    <td valign="top" ><table width="600" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="65">&nbsp;</td>
      </tr>
      <tr>
        <td height="205" align="center"><object id="FlashID" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="564" height="200">
          <param name="movie" value="${ctx}/images/amol/arrow.swf" />
          <param name="quality" value="high" />
          <param name="wmode" value="transparent" />
          <param name="swfversion" value="6.0.65.0" />
          <!-- 此 param 标签提示使用 Flash Player 6.0 r65 和更高版本的用户下载最新版本的 Flash Player。如果您不想让用户看到该提示，请将其删除。 -->
          <param name="expressinstall" value="${ctx}/scripts/expressInstall.swf" />
          <!-- 下一个对象标签用于非 IE 浏览器。所以使用 IECC 将其从 IE 隐藏。 -->
          <!--[if !IE]>-->
          <object type="application/x-shockwave-flash" data="${ctx}/images/13.swf" width="564" height="200">
            <!--<![endif]-->
            <param name="quality" value="high" />
            <param name="wmode" value="transparent" />
            <param name="swfversion" value="6.0.65.0" />
            <param name="expressinstall" value="${ctx}/scripts/expressInstall.swf" />
            <!-- 浏览器将以下替代内容显示给使用 Flash Player 6.0 和更低版本的用户。 -->
            <div>
              <h4>此页面上的内容需要较新版本的 Adobe Flash Player。</h4>
              <p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="获取 Adobe Flash Player" width="112" height="33" /></a></p>
            </div>
            <!--[if !IE]>-->
          </object>
          <!--<![endif]-->
        </object></td>
      </tr>
      <tr>
        <td height="21">&nbsp;</td>
      </tr>
      <tr>
        <td height="50">
       
       <form id="loginForm" action='${ctx}/j_security_check' method="post" style="margin: 0;padding: 0">
	  <table width="600" height="33" border="0" cellpadding="0" cellspacing="0" >
      <tr>
        <td width="22">&nbsp;</td>
        <td width="60">用户名：</td>
        <td width="92"><input name="j_username" id="j_username" type="text" class="username" size="9" /></td>
        <td width="40">密码：</td>
        <td width="92"><input name="j_password" id="j_password" class="password" type="password" size="9"/></td>
        <td width="60">验证码：</td>

        <td width="174" align="left" valign="top">
			<input type="text" name="j_captcha_response" style="width: 45px;margin-bottom: 3px;padding: 0;border: solid 1px #88ABDC;">
			<span style="text-align:left;">
				<iframe id="captchaFrame" name="cf" src="<c:url value='/captcha.jpg' />" frameborder="0" scrolling="no" style="width:80px;height:25px;" marginheight="0" marginwidth="0"></iframe>
				<a href="#" style="color:#425E8D;" onclick="frames['cf'].location='<c:url value="/captcha.jpg" />';">
				[刷]</a>
			</span> 
        </td>
        <td width="99">
       <input type="image" src="images/amol/btn_login.gif"/>
        </td>
      </tr>
    </table>
    </form>
        </td>
      </tr>
    </table>
    <table width="600" height="25" border="0" cellpadding="0" cellspacing="0" style="margin-top: 5px;">
    <tr>
    <td>
      <table width="500"  border="0" cellpadding="0" cellspacing="0" >
      <tr>
        <td width="500">
			<c:if test="${param.login_error == 'code_error'}">
			   <div style="border:0px solid #a9a9a9; width: 350;  padding-left: 20px;" >
			       <img border="0" src="<c:url value='/images/icons/warning.gif'/>"/>&nbsp;<font color="#000000">验证码输入错误！</font>
			   </div>
			</c:if> 
			<c:if test="${param.login_error == 'user_psw_error' || param.login_error == '1'}">
			   <div style="border:0px solid #000000; width: 350;  padding-left: 20px;" >
			       <img border="0" src="<c:url value='/images/icons/warning.gif'/>" />&nbsp;<font color="#000000">用户名或密码错误，请重试！</font>
			   </div>
			</c:if> 
			<c:if test="${param.login_error == 'too_many_user_error'}">
			    <div style="border:0px solid #a9a9a9; width: 350;  padding-left: 20px;">
			       <img border="0" src="<c:url value='/images/icons/warning.gif'/>"/>&nbsp;<font color="#000000">该用户已经在其他地方登录了，请稍候。</font>
			   </div>
			</c:if>
			&nbsp; 
        </td>
      </tr>
    </table>
    </td>
    <td valign="top">
         <table width="100" border="0" cellpadding="0" cellspacing="0">
        <%-- <tr>
          <td width="100"><img src="${ctx}/images/icons/user_go.gif"><a href="#" onclick="openReg()">用户注册</a></td>
        </tr> --%>
     </table>
    </td>
    </tr>
    </table>
    </td>
  </tr>
</table>
<script type="text/javascript">
swfobject.registerObject("FlashID");
function openReg(){
	var loginId = window.showModalDialog('${ctx}/register/edit.do', 'null', 'dialogWidth:600px;dialogHeight:500px;scroll:no');
	if (loginId != null){
		document.getElementById("j_username").value = loginId;
		document.getElementById("j_password").value = '';
	}
}
</script>
</body>
</html>
