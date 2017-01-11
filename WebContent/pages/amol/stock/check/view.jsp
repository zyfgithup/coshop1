<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.systop.amol.stock.model.StockCheckDetail"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<link type="text/css"	rel="stylesheet" href="${ctx}/scripts/jquery/validate/css/screen.css">
<title>库存盘点单</title>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">
<table width="100%">
	<tr>
		<td>库存盘点信息</td>
		<td align="right">
			<table>
			<tr>
				<td>
					<form action="${ctx}/stock/check/lp/view.do">
						<input type='hidden' name="model.stockCheckDetail.stockCheck.id" value="${model.id }" />
						<input type='hidden' name="model.sign" value="0"/>
						<s:submit value="报损单" cssClass="button"/>
					</form>
				</td>
				<td>
					<form action="${ctx}/stock/check/lp/view.do?model.stockCheckDetail.StockCheck.id=${model.id}&model.sign=1">
						<input type='hidden' name="model.stockCheckDetail.stockCheck.id" value="${model.id }" />
						<input type='hidden' name="model.sign" value="1"/>
						<s:submit value="报盈单" cssClass="button"/>
					</form>
				</td>
			</tr>
			</table>
		</td>
	</tr>
</table>
</div>
<div align="center" style="width: 100%"><s:hidden name="model.id" />
<fieldset style="width: 95%;padding: 10px 10px 10px 10px;">
<legend>库存盘点单</legend> 
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
			<td align="right">清算人员：</td>
			<td align="left">${model.employee.name}</td>
			<td align="right">仓&nbsp;&nbsp;&nbsp;&nbsp;库：</td>
			<td class="simple" align="left">${model.storage.name}</td>
		</tr>
		<tr>
			<td align="right">操&nbsp;作&nbsp;员：</td>
			<td colspan="3" class="simple" align="left">${model.user.name}</td>
		</tr>

		<tr>
			<td colspan="4" class="simple" align="left" style="color:red">
			    <br/>
				<s:if test="model.status == 1">
					注意:该单【已经】清算完毕!
				</s:if>
				<s:else>
					注意:该单【未】清算完毕!
				</s:else>
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
				<td align="center" bgcolor="#F3F4F3">单位</td>
				<td align="center" bgcolor="#F3F4F3">实际数量</td>
				<td align="center" bgcolor="#F3F4F3">库存数量</td>
				<td align="center" bgcolor="#F3F4F3">清查数量</td>
			</tr>
			<s:iterator value="model.stockCheckDetails" var="scd">
			<tr align="center" bgcolor="#FFFFFF">
				<td height="23" align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.scd.stock.products.code"/>&nbsp;</td>
				<td align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.scd.stock.products.name"/>&nbsp;</td>
				<td align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.scd.stock.products.stardard"/>&nbsp;</td>
				<td align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.scd.stock.products.units.name"/>&nbsp;</td>
				<td align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.scd.count"/>&nbsp;</td>
				<td align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.scd.stockCount"/>&nbsp;</td>
				<td align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.scd.checkCount"/>&nbsp;</td>
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
		window.location = '${ctx}/stock/check/exportExcel.do?model.id=' + id;
	}
</script>

</body>
</html>