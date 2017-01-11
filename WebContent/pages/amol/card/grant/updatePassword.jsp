<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>代币卡管理</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">代币卡管理</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="updatePassword.do" id="change" method="post">
	<s:hidden name="model.id" />
	<fieldset style="width: 80%; padding:10px 10px 10px 10px;">
	<legend>修改密码</legend>
	<table width="100%" align="center">
	
		<tr>
			<td align="right" width="180">代&nbsp;币&nbsp;卡&nbsp;号&nbsp;：</td>
			<td align="left" colspan="3">
				<s:textfield id="" name="model.card.cardNo" cssStyle="width:250px;" readonly="true" />
			</td>			
		</tr>
		
		<tr>
			<td align="right" width="180">身&nbsp;份&nbsp;证&nbsp;号&nbsp;：</td>
			<td align="left" colspan="3">
				<s:textfield id="" name="model.customer.idCard" cssStyle="width:250px;" readonly="true" />
			</td>			
		</tr>
		
		<tr>
			<td align="right" width="180">持&nbsp;&nbsp;卡&nbsp;&nbsp;人&nbsp;&nbsp;：</td>
			<td align="left" colspan="3">
				<s:textfield name="model.customer.name" id="userNames" cssStyle="width:250px" disabled="true"/>
			</td>
		</tr>
		
		<tr>
			<td align="right" width="180">原&nbsp;始&nbsp;密&nbsp;码&nbsp;：</td>
			<td align="left" colspan="3">
				<s:password name="oldPassword" maxlength="6" id="pwd" cssStyle="width:250px;" cssClass="required"/>&nbsp;<font color="red">*</font>
			</td>
		</tr>
		
		<tr>
			<td align="right" width="180">
				新&nbsp;&nbsp;密&nbsp;&nbsp;码&nbsp;&nbsp;：
			</td>
			<td align="left" colspan="3">
				<s:password id="newpwd" name="model.password" maxlength="6" cssStyle="width:250px;" cssClass="required passwordValidator"/>&nbsp;<font color="red">*</font>
				<span id="respwd">&nbsp;请输入六位密码</span>
			</td>
		</tr>
		
		<tr>
			<td align="right" width="180">
				确&nbsp;认&nbsp;密&nbsp;码&nbsp;：
			</td>
			<td align="left" colspan="3">
				<s:password id="doubleRepwd" name="doubleRepwd" maxlength="6" cssStyle="width:250px;" cssClass="passwordCheck required"/>
				<font color="red">*</font>
				<span id="pswd">&nbsp;请输入新密码</span>
			</td>
		</tr>
		
	</table>
	</fieldset>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
				<s:submit value="保存" cssClass="button" /> 
				&nbsp;&nbsp;
				<input type="button" value="返回" onclick="history.go(-1)" class="button"/>&nbsp;&nbsp;
				<s:reset value="重置" cssClass="button" />
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
<script type="text/javascript">
//页面加载，密码框获取焦点
$(document).ready(function(){
	$("#pwd").focus();
}); 
//密码六位的验证
$(function() {
	$.validator.addMethod("passwordValidator", function(value, element) {      
		var res;
        var len = value.length;		
		if (len == 6){
	        res = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{3})$/);
		    $("#respwd").html("");
		}else{
	    	res = "err";
	    	$("#respwd").html("<font color='red'>请输入6位密码</font>");
	    }		
        return res != "err";
    },"");
});
//验证密码一致性
$(function() {
	$.validator.addMethod("passwordCheck", function(value, element) {
        var res;
        var pwd1 = document.getElementById('newpwd').value;
  		var pwd2 = document.getElementById('doubleRepwd').value;
  		if(pwd1 != null & pwd2 != null & pwd1 != '' & pwd2 != '') {
  			if(pwd1 != pwd2) {
  				res = "err";
  				document.getElementById('pswd').innerHTML = '<font color="red">'+'两次密码不一致。'+'</font>';
  			}else{
  				res = "ok";
  				document.getElementById('pswd').innerHTML = '&nbsp;'; 				
  			}
  		}
        return res != "err";
    },"");
})

$(document).ready(function() {
	$("#change").validate();
});
</script>
</body>
</html>