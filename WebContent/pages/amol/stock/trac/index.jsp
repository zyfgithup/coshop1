<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>库存调拨管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function remove(id){
	if (confirm("确认要删除该库存调拨信息吗?")){
		window.location.href="remove.do?model.id=" + id;
	}
}
function submitForm(){
	$("#queryForm").submit();
}

function setSign(type){
	$("#tracSign").val(type);
	submitForm();
}
</script>
</head>
<body>
<div class="x-panel">
	<div class="x-panel-header" >
	<div style="float: left;">库存调拨管理</div>
	<s:if test="model.user.beginningInit== 1">
	<div style="float: right;font-weight:normal;">
		<a href="${ctx}/stock/trac/detail/edit.do" title="创建库存调拨">
		<img src="${ctx}/images/icons/add.gif" />创建库存调拨</a>	
	</div>
	</s:if>
	</div>
	<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
	<form id="queryForm" action="${ctx}/stock/trac/index.do" method="post">
	<table width="100%">
		<tr>
			<td>
				调拨单号：<s:textfield name="model.checkNo" size="18" id="checkNo"/> 
				&nbsp;出货仓库：<s:select list="outStorageMap" name="model.outStorage.id" headerKey="" headerValue="全部" />
				入货仓库：<s:select list="inStorageMap" name="model.inStorage.id" headerKey="" headerValue="全部"/>
			</td>
			<td rowspan="2" valign="middle">
				<input type="submit" value="&#26597;&nbsp;&#35810;" class="button" />
			</td>
		</tr>
		<tr>
			<td colspan="2" >
				开始日期：<input id="startDate" name="startDate" size="18" value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate" readonly="readonly" />&nbsp; 
				结束日期：&nbsp;<input id="endDate" name="endDate" size="16" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate" readonly="readonly" />&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
		</tr>
	</table>
	</form>
</div>
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
			action="index.do"
		useAjax="true" doPreload="false"
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="370px"	
		minHeight="370"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
	   <ec:column width="50" property="_No" title="No."
			value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center" />
		<ec:column width="130" property="checkNo" title="调拨单号" style="text-align:center"/>
		<ec:column width="120" property="outStorage.name" title="出货库" ellipsis="true" />
		<ec:column width="120" property="inStorage.name" title="入货库" ellipsis="true"/>
		<ec:column width="80" property="createTime" title="创建时间" cell="date" style="text-align:center"/>
		<ec:column width="200" property="user.name" title="创建人" style="text-align:center" ellipsis="true"/>
		<ec:column width="100" property="_0" title="操作" style="text-align:center" sortable="false">
			<a href="${ctx}/stock/trac/view.do?model.id=${item.id}" title="查看">详情</a> 
		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>