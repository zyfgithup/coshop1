<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>即时库存管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>

</head>
<body>
<div class="x-panel">
	<div class="x-panel-header">即时库存管理</div>
		<div class="x-toolbar">
		<table width="100%">
			<s:form action="index" theme="simple">
			<tr>
				<td>
		        	经销商级别：<s:select name="conditionSuperiorSign"
						list='#{"2":"分销商"}' headerKey="1"
						headerValue="经销商 " cssStyle="width:120px;" />
		        	&nbsp;供&nbsp;应&nbsp;商：<s:textfield name="model.products.supplier.name" size="18" />       
		        	仓库名称：<s:select list="storageMap" name="model.storage.id"
						headerKey="" id="storageId" headerValue="全部" cssStyle="width:80px;" />
				</td>
				<td rowspan="2" valign="middle"">
					<s:submit value="查询" cssClass="button"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					商&nbsp;品&nbsp;&nbsp;名&nbsp;称：<s:textfield name="model.products.name" size="18" />
		        	商品编码：<s:textfield name="model.products.code" size="18" />&nbsp;&nbsp;
		        	
				</td>
			</tr>
			</s:form>
		</table>
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
		height="400px"	
		minHeight="400"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
	    <ec:column width="100" property="products.code" title="商品编码" ellipsis="true"/>
		<ec:column width="120" property="products.name" title="商品名称" ellipsis="true"/>
		<ec:column width="100" property="products.stardard" title="商品规格" ellipsis="true"/>
		<ec:column width="140" property="products.supplier.name" title="供应商" ellipsis="true"/>
		<ec:column width="120" property="storage.name" title="仓库" ellipsis="true"/>
		<ec:column width="50" property="products.units.name" title="单位"/>
		<ec:column width="100" property="count" title="数量" style="text-align:right" ellipsis="true"/>
		<ec:column width="100" property="unitPack" title="包装数量" style="text-align:right" ellipsis="true"/>
		<ec:column width="140" property="user.name" title="创建人" style="text-align:center" ellipsis="true"/>	
	</ec:row>
	
</ec:table>
</div>
</div>
</body>
</html>