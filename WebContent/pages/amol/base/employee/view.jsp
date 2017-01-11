<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<title>查看员工</title>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">员工信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
	<fieldset style="width: 45%; padding:10px 10px 10px 10px;">
	<legend>员工信息</legend>
	<table width="100%" align="center" >
		<tr>
			<td align="right" width="200">所属部门：</td>
			<td class="simple" align="left">
				${model.dept.name }
			</td>
		</tr>	
			
		<tr>
			<td align="right">编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td class="simple" align="left">
				${model.code}
			</td>
		</tr>
			
		<tr>
			<td align="right">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
			<td class="simple" align="left">
		       ${model.name }			 
			</td>
		</tr>
		
		<tr>
			<td align="right">登&nbsp;&nbsp;录&nbsp;&nbsp;名：</td>
			<td class="simple" align="left">
		       ${model.loginId }			 
			</td>
		</tr>
		<tr>
			<td align="right" >性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
			<td class="simple" align="left">
					<c:if test="${model.sex=='F' }">女</c:if>
					<c:if test="${model.sex=='M' }">男</c:if>
			</td>
		</tr>
		
		<tr>
	  		<td align="right">雇佣日期：</td>
	  		<td class="simple" align="left">
	     		${joinTime }
	     	</td>
		</tr>
		
		<tr>
	  		<td align="right">出生日期：</td>	
	  		<td class="simple" align="left">
	  			${birthday }
			</td>
		</tr>
	  	
		<tr>
			<td align="right">身份证号：</td>
			<td class="simple" align="left">
				${model.idCard}
			</td>
		</tr>
		<tr>
			<td align="right">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</td>
			<td class="simple" align="left">
				${model.mobile}
			</td>
		</tr>
		<tr>
			<td align="right">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
			<td class="simple" align="left">
				${model.address}
			</td>
		</tr>
	</table>
	</fieldset>
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
		<s:reset
				value="返回" cssClass="button" onclick="javascript:history. back();" />
			</td>
		</tr>
	</table>
</div>
</div>

</body>
</html>