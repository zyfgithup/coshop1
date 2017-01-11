<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>供应商管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>

</head>
<body>
<div class="x-panel">
<div class="x-panel-header">供应商管理</div>

<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="initindex.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="供应商应付.xls"
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
	 
		<ec:column width="100" property="name" title="供应商名称" ellipsis="true"/>
		<ec:column width="110" property="_1" title="应付账款（元）" ellipsis="true">0
		</ec:column>
		
	</ec:row>
</ec:table>
</div>
</div>


</body>
</html>