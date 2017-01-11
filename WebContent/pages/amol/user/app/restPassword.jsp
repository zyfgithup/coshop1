<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<style type="text/css">
 .input{
 	width: 150px;
 }
 
 .warn{
	color: red;
}
</style>
<title>重置密码</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">重置密码</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="restPassword.do" id="save" method="post">
	<s:hidden name="model.id" />
	<fieldset style="width: 60%; padding:10px 10px 10px 10px;">
	<legend>重置密码</legend>
	<table width="100%" align="center">
		<tr>
			<td align="right" width="150">名&nbsp;&nbsp;&nbsp;&nbsp;称：</td>
			<td class="simple" align="left">
			  <s:textfield name="model.name" cssClass="input" disabled="true" maxlength="40"></s:textfield >
			</td>
		</tr>
		<!-- tr>
			<td align="right">身份证号：</td>
			<td class="simple" align="left">
			  <s:textfield name="model.idCard" cssClass="required input" disabled="true" maxlength="18"/>
			  <span id="idCardMsg" class="warn"></span>
			  <font color="red">*</font>
			</td>
		</tr> -->
		<tr>
		    <td align="right">用&nbsp;户&nbsp;名：</td>
			<td class="simple" align="left" colspan="3">
		  	  <s:textfield id="loginId" name="model.loginId" cssClass="required input" disabled="true" maxlength="14"/>
		  	  <span id="loginIdMsg" class="warn"></span>
		  	  <font color="red">*</font>
		 	</td>
		</tr>
		 
		<tr>
			<td align="right">
				密&nbsp;&nbsp;&nbsp;&nbsp;码：
			</td>
			<td class="simple" align="left" colspan="3">
				<s:password id="repwd" name="model.password" cssStyle="width:150px;" cssClass="required passwordValidator input"/>
				<font color="red">*</font>
			</td>
		</tr>
		
		<tr>
			<td align="right">
				确认密码：
			</td>
			<td class="simple" align="left" colspan="3">
				<s:password id="doubleRepwd" name="model.confirmPwd" cssStyle="width:150px;" cssClass="required passwordCheck input"/>
				<font color="red">*</font>
			</td>
		</tr>
	</table>
	</fieldset>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
              <s:submit value="保存" cssClass="button"/>&nbsp;&nbsp;
			  <s:reset value="重置" cssClass="button" />&nbsp;&nbsp;
			  <input type="button" value="返回" onclick="history.go(-1)" class="button"/>
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
<script type="text/javascript">
$(function() {
	  $.validator.addMethod("passwordValidator", function(value, element) {
			return (value.length > 5);
	    },"密码长度少于6位");
		
		$.validator.addMethod("passwordCheck", function(value, element) {
			return (value == $('#repwd').val());
	    },"两次输入的密码不一致");
});

$(document).ready(function() {
	$("#save").validate();
});
</script>
</body>
</html>