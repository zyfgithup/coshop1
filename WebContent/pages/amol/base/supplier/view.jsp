<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>供应商</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">供应商信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
	<s:hidden name="model.id" />
	<fieldset style="width: 65%; padding:10px 10px 10px 10px;">
	<legend>供应商信息</legend>
	<table width="100%" align="left">
		<tr>
			<td align="right" width="100">名&nbsp;&nbsp;&nbsp;&nbsp;称：</td>
			<td align="left" colspan="3">
				${model.name}
			</td>
		</tr>
		<tr>
			<td align="right" width="100">地&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
			<td align="left" colspan="3">
			    ${model.region.name}
			</td>
		</tr>
		<tr>
			<td align="right">联系电话：</td>
			<td align="left" colspan="3">
				${model.phone}
			</td>
		</tr>
		<tr>
			<td align="right">添加日期：</td>
			<td align="left" colspan="3">
				${createTime}
			</td>
		</tr>
		<tr>
			<td align="right">传&nbsp;&nbsp;&nbsp;&nbsp;真：</td>
			<td align="left" colspan="3">
				${model.fax}
			</td>
		</tr>
		<tr>
			<td align="right">电子邮件：</td>
			<td align="left" colspan="3">
				${model.email}
			</td>
		</tr>
		<tr>
			<td align="right">企业法人：</td>
			<td align="left" colspan="3">
				${model.gysfr}
			</td>
		</tr>
		<tr>
			<td align="right">开&nbsp;户&nbsp;行：</td>
			<td align="left" colspan="3">
				${model.khh}
			</td>
		</tr>
		<tr>
			<td align="right">账&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td align="left" colspan="3">
				${model.zh}
			</td>
		</tr>
        <tr>
           <td align="right">
           	地&nbsp;&nbsp;&nbsp;&nbsp;址：
           </td>
           <td align="left" colspan="3">
           	    ${model.address}
			</td>
        </tr>
        <tr>
           <td align="right">
           	备&nbsp;&nbsp;&nbsp;&nbsp;注：
           </td>
           <td align="left" colspan="3">
           		${model.remark}
			</td>
        </tr>		
	</table>
	</fieldset>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			  <input type="button" value="返回" onclick="history.go(-1)" class="button"/> 
			</td>
		</tr>
	</table>
</div>
</div>
</body>
</html>