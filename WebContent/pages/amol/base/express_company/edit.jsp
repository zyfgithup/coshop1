<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>快递公司管理</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">快递公司管理</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save.do" id="save" method="post">
	<s:hidden name="model.id" />
	<fieldset style="width: 40%; padding:10px 10px 10px 10px;">
	<legend>快递公司编辑</legend>
	<table width="100%" align="left">
		
        <tr>
           <td align="right">
           	名称：
           </td>
           <td align="left" colspan="3">
           		<s:textfield  id="" name="model.name" cssClass="required"></s:textfield >
           		<font color="red">&nbsp;*</font>
			</td>
        </tr>		
		

	</table>
	</fieldset>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			  <s:submit value="保存" 	cssClass="button" /> 
			  &nbsp;&nbsp;
			  <s:reset value="重置" cssClass="button" /> &nbsp;&nbsp;
			  <input type="button" value="返回" onclick="history.go(-1)" class="button"/>
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>

<script type="text/javascript">
$(document).ready(function() {
	$("#save").validate();
});
</script>
</body>
</html>