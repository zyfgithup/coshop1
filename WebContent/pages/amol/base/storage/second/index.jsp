<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>二级仓库信息</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">

function remove(id){
	if (confirm("确认要删除该仓库吗?")){
		window.location.href="remove.do?model.id=" + id;
	}
}
function removeAo(){
	alert('删除失败,该仓库下已货物存储!');
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">分销商仓库管理</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
<table width="99%">
	<tr>
		<td><s:form action="index" theme="simple">
        		分销商：<s:select name="conditionId"  cssStyle="width:120px;"
				list="superiorsMap" headerKey="" headerValue="全部 " />
        	仓库名称：<s:textfield name="conditionName" size="30" />&nbsp;&nbsp;
        	<s:submit value="查询" cssClass="button"></s:submit>
		</s:form></td>
		<td align="right">
		<table>
			<tr>
				<td><a href="edit.do" title="添加分销商仓库"><img
					src="${ctx}/images/icons/add.gif" />添加分销商仓库</a></td>
				<td><span class="ytb-sep"></span></td>
				<td><a href="${ctx}/base/storage/index.do" title="返回仓库列表">
				<img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>返回仓库列表</a></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
<div class="x-panel-body"><ec:table items="items" var="item"
	retrieveRowsCallback="limit" sortRowsCallback="limit" action="index.do"
	useAjax="false" doPreload="false" pageSizeList="15,20,50"
	editable="false" sortable="false" rowsDisplayed="15"
	generateScript="true" 
	resizeColWidth="true" 
	classic="true" 
	width="100%"
	height="400px"	
	minHeight="400"
	toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
		<ec:column width="30" property="_0" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center" />
		<ec:column width="140" property="name" title="仓库名称" ellipsis="true" />
		<ec:column width="140" property="creator.name" title="分销商" ellipsis="true" />
		<ec:column width="80" property="createTime" style="text-align:center" cell="date" format="yyyy-MM-dd" sortable="false" title="创建时间"/>
		<ec:column width="170" property="address" title="地址" ellipsis="true" />
		<ec:column width="160" property="descn" title="备注" ellipsis="true" />
		<ec:column width="100" property="_1" title="操作"
			style="text-align:center" sortable="false">
			<a href="edit.do?model.id=${item.id}" title="修改仓库信息">编辑</a> | 
			<a href="#" onclick="remove(${item.id})" title="删除仓库信息">删除</a>
		</ec:column>
	</ec:row>
</ec:table></div>
</div>
</body>
</html>