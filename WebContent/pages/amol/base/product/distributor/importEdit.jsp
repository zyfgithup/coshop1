<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.ReceiveInit"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/msgDiv.jsp"%>
<title>商品excel导入</title>
<script type="text/javascript" src="${ctx }/pages/amol/sales/receiveInit/editjs.js"></script>
</head>
<body>


<div class="x-panel" style="width: 100%">
<div class="x-panel-header">商品excel导入</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">

<table width="98%">
	<tr>
		<td align="left"><s:form id="importFrm" action="importData.do" theme="simple" method="POST"
			enctype="multipart/form-data">
			<fieldset style="margin: 10px;"><legend>商品Excel数据导入</legend>
			<table align="left">
				<tr>
					<td>批量导入数据：</td>
					<td><s:file name="data" size="30" cssStyle="background-color: FFFFFF;" /></td>
					<td>&nbsp;&nbsp;
					<s:submit value="数据导入" cssClass="button"></s:submit>
					</td>
					<td><span><font style="color: red">${errorInfo}</span></td>
				</tr>
			</table>
			</fieldset>
		</s:form></td>
	</tr>
</table>
<script type="text/javascript">
$("#sub").onclick(function(){
	$("#importFrm").submit();
});
</script>
</div>
</div>
</body>
</html>