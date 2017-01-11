<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.systop.amol.stock.model.StockCheckDetail"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<link type="text/css"	rel="stylesheet" href="${ctx}/scripts/jquery/validate/css/screen.css">
<title>
		<s:if test="model.sign == 0">
			库存盘点【报损单】信息
		</s:if>
		<s:else>
			库存盘点【报盈单】信息
		</s:else>
</title>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">
<table width="100%">
	<tr>
		<td>
		<s:if test="model.sign == 0">
			库存盘点【报损单】信息
		</s:if>
		<s:else>
			库存盘点【报盈单】信息
		</s:else>
		</td>
	</tr>
</table>
</div>
<div align="center" style="width: 100%"><s:hidden name="model.id" />
<fieldset style="width: 95%; padding: 10px 10px 10px 10px;">
<legend>
	<s:if test="model.sign == 0">
		库存盘点报损单
	</s:if>
	<s:else>
		库存盘点报盈单
	</s:else>
</legend>
	<table width="95%" align="left" border="0">
		<tr>
			<td align="right">【库存盘点单】编号：</td>
			<td class="simple" align="left">${model.stockCheckDetail.stockCheck.checkNo}</td>
			<td align="right">生成日期：</td>
			<td class="simple" align="left">
				<fmt:formatDate	value="${model.stockCheckDetail.stockCheck.createTime}" pattern="yyyy-MM-dd" />
			</td>
		</tr>
		<tr>
			<td align="right">
				<s:if test="model.sign == 0">
					【报损单】编号：
				</s:if>
				<s:else>
					【报盈单】编号：
				</s:else>
			</td>
			<td class="simple" align="left">${model.checkNo}</td>
			<td align="right">日&nbsp;&nbsp;&nbsp;&nbsp;期：</td>
			<td class="simple" align="left">
				<fmt:formatDate	value="${model.createTime}" pattern="yyyy-MM-dd" />
			</td>
		</tr>
		<tr>
			<td align="right">操&nbsp;作&nbsp;员：</td>
			<td align="left">${model.user.name}</td>
			<td align="right">仓&nbsp;&nbsp;&nbsp;&nbsp;库：</td>
			<td class="simple" align="left">${model.stockCheckDetail.stockCheck.storage.name}</td>
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
				<td align="center" bgcolor="#F3F4F3">实际数量</td>
				<td align="center" bgcolor="#F3F4F3">库存数量</td>
				<td align="center" bgcolor="#F3F4F3">清查数量</td>
			</tr>
			<s:iterator value="#request.stockCheckLPs" var="lp">
			<tr height="25">
				<td  align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.lp.stockCheckDetail.stock.products.code"/>&nbsp;</td>
				<td  align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.lp.stockCheckDetail.stock.products.name"/>&nbsp;</td>
				<td  align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.lp.stockCheckDetail.stock.products.stardard"/>&nbsp;</td>
				<td  align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.lp.stockCheckDetail.count"/>&nbsp;</td>
				<td  align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.lp.stockCheckDetail.stockCount"/>&nbsp;</td>
				<td  align="center" bgcolor="#FFFFFF" align="center"><s:property value="#attr.lp.stockCheckDetail.checkCount"/>&nbsp;</td>
			</tr>
			</s:iterator>
			<s:if test="#attr.stockCheckLPs.size == 0">
				<s:if test="model.sign == 0">
					<font color="red">没有生成【报损单】</font>
				</s:if>
				<s:else>
					<font color="red">没有生成【报盈单】</font>
				</s:else>
			</s:if>
		</tbody>
	</table>
</center>
<table width="100%" style="margin-bottom: 10px; margin-top: 20px" align="center">
	<tr>
		<td align="center">
            <!--<input type="button" class="button" value="导出" onclick="javascript:exportExcel('${model.id}')">-->
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