<%@page import="com.systop.amol.stock.StockConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.systop.amol.stock.model.StockCheckDetail"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<style type="text/css">
<!--
.Message {
	color: #FF0000;
	font-style: italic;
}

#mytable {
	border: 1px solid #A6C9E2;
	border-collapse: collapse;
}

#mytable td {
	border: 1px solid #A6C9E2;
	height: 26px;
}
.tdSty{
	border: 1px solid #A6C9E2;
	height: 26px;
}
-->
</style>
<title>库存状态详情</title>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">
<table width="100%">
	<tr>
		<td>库存状态详情信息</td>
	</tr>
</table>
</div>
<div align="center" style="width: 100%"><s:hidden name="model.id" />
<fieldset style="width: 95%; padding: 10px 10px 10px 10px;">
<legend>库存状态详情信息</legend> 
<center>
	<table cellpadding="0" cellspacing="0" style="border:0px solid A6C9E2" align="center" width="95%">
		<tbody id="tbody">
			<tr height="25" style="border:1px solid red"  bgcolor="#E6F4F7" align="center">
				<td class="tdSty">经销商</td>
				<td class="tdSty">商品</td>
				<td class="tdSty">编号</td>
				<td class="tdSty">规格</td>
				<td class="tdSty">基本单位 </td>
				<td class="tdSty">数量</td>
				<td class="tdSty">包装单位</td>
			</tr>
			<tr >
				<td colspan="6" height="25">经销商</td>
			</tr>
			<s:iterator value="#request.distributor1" var="stock">
			<tr height="25">
				<td  class="tdSty" align="center"><s:property value="#attr.stock.user.name"/>&nbsp;</td>
				<td  class="tdSty" align="center"><s:property value="#attr.stock.product.name"/>&nbsp;</td>
				<td  class="tdSty" align="center"><s:property value="#attr.stock.product.code"/>&nbsp;</td>
				<td  class="tdSty" align="center"><s:property value="#attr.stock.product.stardard"/>&nbsp;</td>
				<td  class="tdSty" align="center"><s:property value="#attr.stock.product.units.name"/>&nbsp;</td>
				<td  class="tdSty" align="right"><s:property value="#attr.stock.count"/>&nbsp;</td>
				<td  class="tdSty" align="center">
					<s:set name="tcount" value="#attr.stock.count" />
					<s:set name="pid" value="#attr.stock.product.id" />
					<%=StockConstants.getUnitPack(pageContext.getServletContext(),(Integer)pageContext.getAttribute("pid"),((Long)pageContext.getAttribute("tcount")).intValue())%>&nbsp;
				</td>
			</tr>
			</s:iterator>
			<tr>
				<td colspan="6" height="25">分销商</td>
			</tr>
			<s:if test="#attr.request.distributor2.size > 0">
			<s:iterator value="#request.distributor2" var="stock">
			<tr height="25">
				<td  class="tdSty" align="center"><s:property value="#attr.stock.user.name"/>&nbsp;</td>
				<td  class="tdSty" align="center"><s:property value="#attr.stock.product.name"/>&nbsp;</td>
				<td  class="tdSty" align="center"><s:property value="#attr.stock.product.code"/>&nbsp;</td>
				<td  class="tdSty" align="center"><s:property value="#attr.stock.product.stardard"/>&nbsp;</td>
				<td  class="tdSty" align="center"><s:property value="#attr.stock.product.units.name"/>&nbsp;</td>
				<td  class="tdSty" align="right"><s:property value="#attr.stock.count"/>&nbsp;</td>
				<td  class="tdSty" align="center">
					<s:set name="tcount" value="#attr.stock.count" />
					<s:set name="pid" value="#attr.stock.product.id" />
					<%=StockConstants.getUnitPack(pageContext.getServletContext(),(Integer)pageContext.getAttribute("pid"),((Long)pageContext.getAttribute("tcount")).intValue())%>&nbsp;
				</td>
			</tr>
			</s:iterator>
			</s:if>
			<s:else>
				<tr height="25">
				<td colspan="7" class="tdSty" align="center">没有分销商库存信息</td>
				</tr>
			</s:else>
			<tr><td colspan="6">&nbsp;</td></tr>
			<tr style="background-color: #ffe3ee" height="25">
				<td class="tdSty" colspan="5" align="center" id="No1"
					style="font-weight: bold; ">合计(全部)</td>
				<td class="tdSty" style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">${countTotal}&nbsp;</td>
				<td class="tdSty" >&nbsp;</td>
			</tr>
		</tbody>
	</table>
</center>
</fieldset>
<table width="95%" style="margin-bottom: 10px; margin-top: 20px" align="center">
	<tr>
		<td align="center">
<!--			<input type="button" class="button" value="导出" onclick="javascript:exportExcel('${model.id}')">-->
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