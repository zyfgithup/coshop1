<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>分销商即时库存管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>

</head>
<body>
<div class="x-panel">
	<div class="x-panel-header">即时库存查询</div>
		<div class="x-toolbar">
		<table width="100%">
			<s:form action="/stock/salesStockIndex.do" theme="simple">
			<tr>
				<td>
					商品编码：<s:textfield name="model.products.code" size="18" />&nbsp;&nbsp;
		        	商品名称：<s:textfield name="model.products.name" size="18" />&nbsp;&nbsp;
		        	供应商：<s:textfield name="products.supplier.name" size="18" />&nbsp;&nbsp;
		        	<s:submit value="查询" cssClass="button"/>
				</td>
			</tr>
			</s:form>
		</table>
	</div>
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
			action="${ctx}/stock/salesStockIndex.do"
		useAjax="true" doPreload="false"
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="true"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="400px"	
		minHeight="400"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
	    <ec:column width="100" property="products.code" title="商品编码" ellipsis="true"/>
		<ec:column width="120" property="products.name" title="商品名称" ellipsis="true"/>
		<ec:column width="100" property="products.stardard" title="商品规格" ellipsis="true"/>
		<ec:column width="140" property="products.supplier.name" title="供应商" ellipsis="true"/>
		<ec:column width="50" property="products.units.name" title="单位"/>
		<ec:column width="100" property="count" title="数量" style="text-align:right" ellipsis="true"/>
		<ec:column width="100" property="unitPack" title="包装数量" style="text-align:right" ellipsis="true"/>
	</ec:row>
	
</ec:table>
</div>
</div>
</body>
</html>