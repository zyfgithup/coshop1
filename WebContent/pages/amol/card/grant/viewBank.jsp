<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>客户代币卡</title>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">客户代币卡</div>
</div>

<div class="x-toolbar">
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="${ctx}/card/grant/viewBank.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="客户代币卡.xls" 
		pageSizeList="15,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="400px"
		minHeight="400"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
	<ec:row>
		<ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="80" property="customer.name" title="客户" style="text-align:center" ellipsis="true"/>
	    <ec:column width="130" property="customer.idCard" title="身份证号 " style="text-align:center" ellipsis="true"/>
		<ec:column width="180" property="card.cardNo" title="卡号" style="text-align:center" ellipsis="true"/>
		<ec:column width="80" property="credit" title="信用额度" style="text-align:right" format="#,##0.00" cell="number"></ec:column>
		<ec:column width="80" property="spend" title="消费额" style="text-align:right" format="#,##0.00" cell="number"/>
		<ec:column width="80" property="balance" title="余额" style="text-align:right" format="#,##0.00" cell="number"></ec:column>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>