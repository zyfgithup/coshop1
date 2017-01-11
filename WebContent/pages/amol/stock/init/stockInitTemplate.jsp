<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>期初库存模板管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>

</head>
<body>
<div class="x-panel">
<div class="x-panel-header">期初库存模板管理</div>

<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="stockInitTemplate.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="期初库存模板.xls"
		pageSizeList="15,50,100" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="400px"
		minHeight="400"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status">
	<ec:row>
		<ec:column width="100" property="code" title="商品编码" ellipsis="true"/>	
		<ec:column width="100" property="name" title="商品名称" ellipsis="true"/>
		<ec:column width="100" property="stardard" title="商品规格" ellipsis="true"/>
		<ec:column width="150" property="supplier.name" title="供应商" ellipsis="true"/>
		<ec:column width="150" property="_0" title="仓库名称" ellipsis="true"/>
		<ec:column width="70" property="_1" cell="number" title="库存数量" ellipsis="true"/>
		<ec:column width="60" property="units.name" title="包装单位" ellipsis="true"/>
		<ec:column width="70" property="inprice" title="单价" ellipsis="true"/>
		<ec:column width="70" property="_3" cell="number" title="金额" ellipsis="true"/>		
	</ec:row>
</ec:table>
</div>
</div>


</body>
</html>