<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,java.util.Set,java.util.Iterator"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/validator.jsp"%>
<title>首页图片</title>
<script type="text/javascript">
</script>
</head>
<body>

<div class="x-panel" style="width: 100%">
<div class="x-panel-header">首页图片详情</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
	<br/>
	<s:hidden id="id" name="model.id"  /> 
	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>首页图片详情</legend>
	<table width="100%">
		<tr>
			<td align="right">图片：</td>
           	<td>
           		<a href="${ctx }/${pushMessage.imageURL }"><img alt="${pushMessage.title }" src="${ctx }/${pushMessage.imageURL }" width="137" height="75"/></a>
           	</td>
			<td align="right">
				 商家：
			</td>
			<td class="simple" align="left">
			    <input size="20" value=""/>
			</td>
		</tr>
		<tr>
			<td  align="right">
			           内容：</td>
			<td class="simple" align="left" colspan="3">
				<textarea rows="3" cols="50">${pushMessage.content }</textarea>
			</td>
		</tr>
	  </table>
	</fieldset>
		
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
				<s:reset value="返回" cssClass="button" onclick="javascript:history. back();"/>
			</td>
		</tr>
	</table>
</div>
</div>
<script type="text/javascript">
/** ready */
$(document).ready(function() {
	$("#save").validate();
});
</script>
</body>
</html>