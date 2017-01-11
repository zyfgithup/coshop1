<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.systop.common.modules.security.user.model.User"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
Ext.onReady(function() {
	var pstree = new RegionTree({
		el : 'comboxWithTree',
		target : 'regionId',
		//emptyText : '选择地区',
		comboxWidth : 200,
    	treeWidth : 195,
		url : '${ctx}/admin/region/regionTree.do',
		defValue : {id:'${model.region.id}',text:'${model.region.name}'}
	});
	pstree.init();	
	
});
</script>
<style type="text/css">
<!--
.Message {
	color: #FF0000;
	font-style: italic;
}
#mytable {
	border: 1px solid #A6C9E2;
	width: 80%;
	border-collapse: collapse;
}

#mytable td {
	border: 1px solid #A6C9E2;
	height: 30px;
}
-->
</style>
<title>编辑用户</title>
</head>
<body>

<%User user = (User)request.getAttribute("user");%>
<div class="x-panel-body">
<table width="80%" align="center" style="margin: 5px">
	<tr>
		<td height="20px"></td>
	</tr>
</table>
<s:form action="user/editInfo" theme="simple" id="save" validate="true"
	method="POST">
	<s:hidden name="model.id" id="uId" />
	<s:hidden name="model.version" />
	<s:hidden name="model.status" />
	<table align="center" style="width:80%;margin-bottom:2px;">
		<tr>
	   		<td style="border:none;">
	   			<%@ include file="/common/messages.jsp"%>
	   		</td>
		</tr>
	</table>
	<table id="mytable" align="center">
		<tr>
			<td class="simple" align="right">登&nbsp;录&nbsp;名：</td>
			<td class="simple">
				<s:textfield name="model.loginId" cssStyle="width:197px;" readOnly="true" />
				<font color="red">*</font>
			</td>
			<td colspan="2">登录名由注册系统分配，不允许修改</td>
		</tr>
		<tr>
			<td class="simple" align="right">身份证号：</td>
			<td class="simple">
				<s:textfield name="model.idCard" cssStyle="width:197px;" readOnly="true" />
				<font color="red">*</font>
			</td>
			<td colspan="2">身份证号为注册系统依据，不允许修改</td>
		</tr>
		<tr>
			<td class="simple" align="right">名&nbsp;&nbsp;&nbsp;&nbsp;称：</td>
			<td class="simple">
				<s:textfield name="model.name" cssStyle="width:197px;" cssClass="required" theme="simple" size="25" />
				<font color="red">*</font>
			</td>
			<td colspan="2">请输入真实的名称</td>
		</tr>
		<tr>
			<td class="simple" align="right" width="100">性&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
			<td class="simple">
				<s:radio list="sexMap" name="model.sex" cssStyle="border:0px;" /> 
			</td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td align="right" class="simple">所属地区：</td>
			<td class="simple">
				<table>
					<tr>
						<td style="border:0px;">
							<%-- <span id='comboxWithTree' style='width: 100px;'></span>	            
				    		<s:hidden id="regionId" name="model.region.id" cssClass="required" /> --%>
				    		${model.region.name }
						</td>
						<td style="border:0px">
							<font color="red">&nbsp;*</font>
						</td>
					</tr>
				</table>			    
			</td>
			<td colspan="2">请输入您所在的地区。</td>
		</tr>
		<tr>
			<td class="simple" align="right">联系电话：</td>
			<td class="simple">
				<s:textfield name="model.mobile" cssStyle="width:197px;" maxlength="15" theme="simple" size="25" /> 
			</td>
			<td colspan="2">请输入真实的电话，以便我们与您联系。</td>
		</tr>
			<tr>
			<td class="simple" align="right">开户行：</td>
			<td class="simple">
			${model.yhmc }
			</td>
			<td colspan="2">请输入您的开户行。</td>
		</tr>
			<tr>
			<td class="simple" align="right">银行卡号：</td>
			<td class="simple">
			${model.yhkh }
			</td>
			<td colspan="2">请输入您的银行卡号。</td>
		</tr>
		<tr>
			<td class="simple" align="right">固定电话：</td>
			<td class="simple">
				<s:textfield name="model.phone" cssStyle="width:197px;" maxlength="15" theme="simple" size="25" /> 
			</td>
			<td colspan="2">请输入区号和真实的电话，以便我们与您联系。</td>
		</tr>
		<tr>
			<td class="simple" align="right">电子信箱：</td>
			<td class="simple">
				<s:textfield name="model.email" cssStyle="width:197px;" id="email" theme="simple" size="25" cssClass="regEmail"/> 
			</td>
			<td align="left" colspan="2" id="usemail">
				请输入您常用的电子邮箱
				<!--
				<c:if test="${model.loginId == 'admin'}">
					,<font color="red">修改邮箱后请同时修改</font><a href="${ctx}/admin/mail/view.do" target="main" title="SMTP配置"><font color="blue">SMTP配置</font></a><font color="red">中的用户名及密码。</font>
				</c:if>
				  -->
			</td>
		</tr>
		<tr>
			<td class="simple" align="right">通信地址：</td>
			<td class="simple">
				<s:textfield name="model.address" cssStyle="width:197px;" theme="simple" size="25" /> 
			</td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="simple" align="right" width="100">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
			<td class="simple">
				<s:textfield name="model.zip" cssStyle="width:197px;" theme="simple" size="25" /> 
			</td>
			<td colspan="2"></td>
		</tr>
	</table>
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td colspan="2" align="center" class="font_white">
				<s:submit value="保存" cssClass="button"></s:submit>&nbsp;&nbsp; 
				<input type="button" value="返回" onclick="history.go(-1)" class="button"/>&nbsp;&nbsp; 
				<s:reset value="清空" cssClass="button" />
			</td>
		</tr>
	</table>
</s:form>
</div>
<script type="text/javascript">
$(function() {
  	  //验证用户邮箱
	$.validator.addMethod("regEmail", function(value, element) {
        var res;
        var emailStr = document.getElementById('email').value;
        var userId = document.getElementById('uId').value;
		var r = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
		if (emailStr != '' && !r.test(emailStr)) {
			document.getElementById('usemail').innerHTML = '<font color="red">'+'请输入正确的邮箱地址！'+'</font>';
		}else{
			document.getElementById('usemail').innerHTML = '请输入您常用的电子邮箱';
       		$.ajax({
				url: '${ctx}/regist/checkEmail.do',
				type: 'post',
				async : false,
				dataType: 'json',
				data: {uName : emailStr, uId : userId},
				success: function(rst, textStatus){
					res = rst.result;
					if (rst.result == "exist") {
       	   		  		document.getElementById('usemail').innerHTML = '<font color="red">'+'您输入的Email已存在，请重新输入。'+'</font>';
       	   			}
				}
			});
			return res != "exist";
		}
  	  },"");
}); 
</script>
<script type="text/javascript">
	$(document).ready(function() {
	$("#save").validate();
});
</script>
</body>
</html>