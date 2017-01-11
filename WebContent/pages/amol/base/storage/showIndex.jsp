<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>

<title></title>
<script type="text/javascript">
	//
	function aa() {
		this.id;
		this.name;
	}
	function closeWindow(id, name) {
		var a = new aa();
		a.id = id;
		a.name = name;

		window.returnValue = a;
		window.close();
	}
</script>
</head>
<body>

<s:form action="remove" theme="simple" id="removeForm"></s:form>
<s:form action="unsealEmp" theme="simple" id="unsealForm"></s:form>
<div class="x-panel">
<div class="x-panel-header">选择仓库</div>
<div class="x-toolbar">
<table width="99%">
	<tr>
		<td><s:form action="index" theme="simple">
			<s:hidden name="model.status" value="1" />
        	仓库名称：<s:textfield name="model.name" size="30" />&nbsp;&nbsp;
        	<s:submit value="查询" cssClass="button"></s:submit>
		</s:form></td>
	</tr>
</table>
</div>
<div class="x-panel-body">
<div align="center"><ec:table items="items" var="item"
	retrieveRowsCallback="limit" sortRowsCallback="limit"
	action="showIndex.do" useAjax="true" doPreload="false"
	maxRowsExported="10000000" pageSizeList="20,50,100" editable="false"
	sortable="true" rowsDisplayed="20" generateScript="true"
	resizeColWidth="true" classic="false" width="100%" height="400px"
	minHeight="400"
	toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row ondblclick="closeWindow('${item.id}','${item.name}')">
		<ec:column width="30" property="_0" title="No."
			value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center" />
		<ec:column width="170" property="name" title="仓库名称" ellipsis="true" />
		<ec:column width="120" property="createTime" style="text-align:center"
			cell="date" format="yyyy-MM-dd HH:mm" sortable="false" title="创建时间" />
		<ec:column width="180" property="address" title="地址" ellipsis="true" />
		<ec:column width="160" property="descn" title="备注" ellipsis="true" />
	</ec:row>
</ec:table></div>
</div>
</div>
</body>
</html>