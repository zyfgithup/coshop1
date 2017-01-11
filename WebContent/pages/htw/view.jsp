<%@page import="com.systop.common.modules.region.RegionConstants"%>
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
<title>推送详情</title>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<script type="text/javascript">
</script>
</head>
<body>

<div class="x-panel" style="width: 100%">
<div class="x-panel-header">推送详情</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
	<br/>
	<s:hidden id="id" name="model.id"  /> 
	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>推送详情</legend>
	<table width="100%">
		<tr>
			<td align="right">
				 标&nbsp;&nbsp;&nbsp;&nbsp;题：
			</td>
			<td class="simple" align="left">
				<input size="50" value="${model.title }" readonly="readonly"/>
			</td>
			
			<td align="right">
				 创建时间：
			</td>
			<td class="simple" align="left">
			    <input value="<fmt:formatDate value="${model.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly="readonly"/>
			</td>
		</tr>
		<!-- tr>
           	<td align="right">结束日期：</td>
           	<td>
           		<input id="endDate" name="endDate" size="20" value='${pushMessage.endTime}' readonly="readonly"/>
           		【有效时间】
           	</td>
           	<td align="right">
				 商&nbsp;&nbsp;&nbsp;&nbsp;家：
			</td>
			<td class="simple" align="left">
			    <input id="businessNameID" value="${business.name }" size="50" readonly="readonly"/>
			</td>
        </tr> -->
        <tr>
			<td  align="right">
			           图&nbsp;&nbsp;&nbsp;&nbsp;片：</td>
			<td class="simple" align="left">
				<c:if test="${model.imageURL == null || model.imageURL == ''}">
					未上传图片
				</c:if>
				<c:if test="${model.imageURL != null && model.imageURL != ''}">
					<a href="${ctx }/${model.imageURL }"><img alt="${model.title }" src="${ctx }/${model.imageURL }" width="137" height="75"/></a>
				</c:if>
			</td>
		</tr>
		<tr>
			<td  align="right">
			           内容：</td>
			<td class="simple" align="left">
				<textarea rows="3" cols="50" readonly="readonly">${model.content }</textarea>
			</td>
				<td align="right">所属地区：</td>
			<td class="simple" align="left">
			${model.region.name }
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