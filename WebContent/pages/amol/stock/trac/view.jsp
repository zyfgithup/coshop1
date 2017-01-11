<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.systop.amol.stock.StockConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<link type="text/css"	rel="stylesheet" href="${ctx}/scripts/jquery/validate/css/screen.css">
<title>库存调拨单</title>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">
<table width="100%">
	<tr>
		<td>库存调拨信息</td>
	</tr>
</table>
</div>
<div align="center" style="width: 100%"><s:hidden name="model.id" />
<fieldset style="width: 95%; padding: 10px 10px 10px 10px;">
<legend>库存调拨单</legend> 
	<table width="100%" align="left" border="0">
		<tr>
			<td align="right">编 &nbsp;&nbsp;&nbsp;号：</td>
			<td class="simple" align="left">${model.checkNo}</td>
			<td align="right">日&nbsp;&nbsp;&nbsp;&nbsp;期：</td>
			<td class="simple" align="left">
				<fmt:formatDate	value="${model.createTime}" pattern="yyyy-MM-dd" />
			</td>
		</tr>
		<tr>
			<td align="right">出货仓库：</td>
			<td align="left">${model.outStorage.name}</td>
			<td align="right">入货仓库：</td>
			<td class="simple" align="left">${model.inStorage.name}</td>
		</tr>
		<tr>
			<td align="right">操&nbsp;作&nbsp;员：</td>
			<td colspan="3" class="simple" align="left">${model.user.name}</td>
		</tr>
		<tr> 
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td colspan="3" class="simple" align="left">
				${model.remark}
			</td>
		</tr>
	</table>
</fieldset>
<center>
	<table width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9">
		<tbody id="tbody">
			<tr height="25" style="border:1px solid red"  bgcolor="#E6F4F7" align="center">
				<td height="23" align="center" bgcolor="#F3F4F3">商品编码</td>
				<td align="center" bgcolor="#F3F4F3">商品名称</td>
				<td align="center" bgcolor="#F3F4F3">商品规格</td>
				<td align="center" bgcolor="#F3F4F3">调拨数量(基本单位)</td>
				<td align="center" bgcolor="#F3F4F3">包装单位</td>
				<td align="center" bgcolor="#F3F4F3">备注</td>
			</tr>
			<s:iterator value="model.stockTracDetails" var="std">
			<s:set name="tcount" value="#attr.std.count" />
			<s:set name="pid" value="#attr.std.product.id" />
			<tr align="center" bgcolor="#FFFFFF">
				<td  height="23" align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.std.product.code"/>&nbsp;</td>
				<td  align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.std.product.name"/>&nbsp;</td>
				<td  align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.std.product.stardard"/>&nbsp;</td>
				<td  align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.std.count"/>(<s:property value="#attr.std.product.units.name"/>)&nbsp;</td>
				<td  align="center" bgcolor="#FFFFFF" align="center"><%=StockConstants.getUnitPack(pageContext.getServletContext(),(Integer)pageContext.getAttribute("pid"),(Integer)pageContext.getAttribute("tcount"))%>&nbsp;</td>
				<td  align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.std.remark"/>&nbsp;</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
</center>
<table width="95%" style="margin-bottom: 10px; margin-top: 20px" align="center">
	<tr>
		<td align="center">
			<input type="button" class="button" value="导出" onclick="javascript:exportExcel('${model.id}')">
			<s:reset value="返回" cssClass="button" onclick="javascript:history.back();" />
		</td>
	</tr>
</table>

</div>
</div>
<script type="text/javascript">
	function exportExcel(id) {
		window.location = '${ctx}/stock/trac/exportExcel.do?model.id=' + id;
	}
</script>

</body>
</html>