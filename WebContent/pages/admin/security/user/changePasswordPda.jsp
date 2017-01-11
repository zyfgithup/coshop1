<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.systop.common.modules.security.user.model.User"%>
<%@page import="org.acegisecurity.providers.UsernamePasswordAuthenticationToken"%>
<html>
<head>
<%@include file="/common/meta.jsp"%>
</head>
<body>  
<table width="100%">
 <tr>
   <td >欢迎您：<stc:username></stc:username>登录农资PDA系统！</td>
   <td align="right">		
   <a href="${ctx}/security/user/changePasswordPda.do"  style="color: #336699">个人信息</a>   
   &nbsp;|&nbsp; 	
   <a href="${ctx}/j_acegi_logout" target="_self" style="color: #336699">注销&nbsp;  </a>
</td>
 </tr>
 <tr>
   <td height="25" style="border-bottom: 1px dashed #AAC0DD;padding-left: 2px;" colspan="2">
     <a href="${ctx}/sales/edit.do" target=main class="button">出货</a>
     <a href="#" onclick="alert(22)" class="button">出货冲红</a>
     <a href="#" onclick="alert(22)" class="button">退货</a>
     <a href="#" onclick="alert(22)" class="button">退货冲红</a>
     <a href="#" onclick="alert(33)" class="button">查询</a>
   </td>
 </tr>
</table>
<div style="overflow: visible;">
  <div style="overflow: visible;">

    <table align="center" width="97%">
	  <tr>
		<td style="padding-left: 7px;"><%@ include file="/common/messages.jsp"%></td>
	  </tr>
    </table>
          
    <table width="60%" align="left">
	  <tr>
		<td align="left" style="padding-left: 10px;">
		<s:form action="user/changePasswordPda" theme="simple" validate="true" method="POST" id="change">
		  <input type="hidden" name="model.id" 
		    value='<%=((User) ((UsernamePasswordAuthenticationToken) request.getUserPrincipal())
            .getPrincipal()).getId()%>'>
		  <fieldset style="width: 100%;" >
		    <legend>修改密码</legend>
			<table>
			  <tr>
				<td class="simple" width="40%" >旧密码：</td>
				<td width="60%" class="simple" style="text-align: left;">
				  <s:password cssClass="required" name="oldPassword" id="oldPassword" theme="simple" size="18" />
				</td>
			  </tr>
			  <tr>
				<td class="simple">新密码：</td>
				<td class="simple" style="text-align: left;">
				  <s:password name="model.password" id="pwd" theme="simple" size="18" />
				</td>
			  </tr>
			  <tr>
				<td class="simple">重复密码：</td>
				<td class="simple" style="text-align: left;">
				  <s:password name="model.confirmPwd" id="repwd" theme="simple" size="18"/>
				</td>
			  </tr>
			</table>
		  </fieldset>
		  <table width="280">
		    <tr>
			  <td colspan="2" align="center" class="font_white">
			    <s:submit value="保存" onclick="return save()" cssClass="button"></s:submit>&nbsp;&nbsp;&nbsp;&nbsp;
			    <a href="${ctx}/pages/pdaCenter.jsp" class="button"  style="color: #000000">&nbsp;返 回&nbsp;</a>
			  </td>
			</tr>
		  </table>
		</s:form>
		</td>
	  </tr>
    </table>
  </div>
</div>
<script type="text/javascript">

function save() {
	var oldPassword = document.getElementById('oldPassword').value;
    var pwd1 = document.getElementById('pwd').value;
    var pwd2 = document.getElementById('repwd').value;
    if (oldPassword == null || oldPassword == '') {
    	alert("请您输入旧密码！");
    	return false;
    }
    if (pwd1 == null || pwd1 == '') {
    	alert("请您输入新密码！");
    	return false;
    }
    if (pwd2 == null || pwd2 == '') {
    	alert("请您重复输入新密码！");
    	return false;
    }
	if(pwd1 != null & pwd2 != null & pwd1 != '' & pwd2 != '') {
		if(pwd1 != pwd2) {
			alert("您两次输入的密码不一致！");
			pwd1 = '';
			pwd2 = '';
			return false;
		}
	}
}

</script>
<script type="text/javascript">

</script>
</body>
</html>