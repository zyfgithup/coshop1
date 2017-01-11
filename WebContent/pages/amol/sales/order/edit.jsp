<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<title>销售订单</title>

</head>
<body>

<div class="x-panel" style="width: 100%">
<div class="x-panel-header">销售订单信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save" id="save" validate="true" method="POST">
	<s:hidden name="model.id"  /> 
	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>销售订单</legend>
	<table width="100%" align="left" >
		<tr>
			<td align="right" >
				用&nbsp;&nbsp;户：
			</td>
			<td width="200" class="simple" align="left">
				<s:textfield  name="model.user.loginId" maxLength="20"
							  cssStyle="width:155px;color:#808080;" cssClass="required" disabled="true" readonly="true"/>
				<!--  font color="red">*</font>-->
			</td>
			<td  align="right" >
				商&nbsp;&nbsp;家：</td>
			<td width="213" class="simple" align="left">
				<s:textfield  name="model.merUser.loginId" disabled="true" maxLength="20" cssStyle="width:155px;" cssClass="required"
							   readonly="true"/>
				<font color="red">*</font>
			</td>
		</tr>
		<tr>
			<td align="right" >
				编 &nbsp;&nbsp;号：
			</td>
			<td width="200" class="simple" align="left">
			    <s:textfield  name="model.salesNo" maxLength="20" 
					cssStyle="width:155px;color:#808080;" cssClass="required" readonly="true"/>
           		<!--  font color="red">*</font>-->
			</td>
			<td  align="right" >
			         日&nbsp;&nbsp;期：</td>
			<td width="213" class="simple" align="left">
				<s:textfield  name="model.createTime" maxLength="20" cssStyle="width:155px;" cssClass="required" 
				onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" readonly="true"/>
           		<font color="red">*</font>
			</td>
		</tr>
		<tr>
			<td align="right">价&nbsp;&nbsp;格：</td>
			<td align="left" colspan="1">
				<s:textfield  name="model.samount" id="samount" maxLength="20" size="25"
				  cssStyle="width:155px;color:#808080;" cssClass="required"/>
				元
				<font color="red">*</font>
			</td>
			<td align="right">数&nbsp;&nbsp;量：</td>
			<td align="left" colspan="1">
				<s:textfield  name="model.count" id="count" maxLength="20" size="25"
							   cssStyle="width:155px;color:#808080;" cssClass="required"/>

				<font color="red">*</font>
			</td>

		</tr>
		<tr> 
		<td align="right">备&nbsp;&nbsp;注：</td>
			<td colspan="3" class="simple" align="left">
			<s:textfield name="model.remark" cssStyle="width:562px;" /></td>
			
		</tr>
	  </table>
	</fieldset>

	<table align="center" width="95%"> 
	<%--<tr><td><div align="left"><input name="button" type="button" value="增加商品" class="button"  onclick="addRow()"></div></td></tr>--%>
	</table>
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
				<s:submit value="保存" cssClass="button"/>&nbsp;&nbsp;
				<s:reset value="返回" cssClass="button" onclick="javascript:history. back();"/>
			</td>
		</tr>
	</table>
</s:form>
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