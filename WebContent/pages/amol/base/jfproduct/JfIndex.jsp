<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>文章管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
function remove(id){
	if (confirm("确认要删除该文章吗？")){
		window.location.href="${ctx}/base/jfpriceple/remove.do?pId=" + id;
	}
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">文章管理</div>
<div class="x-toolbar">
<table width="100%">
	<tr>
		<td style="padding-left: 5px; padding-top: 5px; width: 50%"
			align="right"><a href="${ctx}/base/jfpriceple/edit.do"><img
			src="${ctx}/images/icons/add.gif" />&nbsp;新建文章&nbsp;</a></td>
	</tr>
</table>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-panel-body"><ec:table items="items" var="item"
	retrieveRowsCallback="limit" sortRowsCallback="limit" action="index.do"
	useAjax="true" doPreload="false" maxRowsExported="10000000"
	pageSizeList="15,20,50,100" editable="false" sortable="false"
	rowsDisplayed="15" generateScript="true" resizeColWidth="true"
	classic="false" width="100%" height="380px" minHeight="380"
	toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
		<ec:column width="30" property="_s" title="No."
			value='${GLOBALROWCOUNT}' sortable="false" style="text-align:center" />
		<ec:column width="100" property="title" title="文章标题" ellipsis="true"/>
		<ec:column width="100" property="type_" title="文章类型" ellipsis="true">
			<c:if test="${item.wzType=='1'}">
				支付帮助
			</c:if>
			<c:if test="${item.wzType=='2'}">
				关于我们
			</c:if>
			<c:if test="${item.wzType=='3'}">
				用户指南
			</c:if>
			<c:if test="${item.wzType=='4'}">
				联系我们
			</c:if>
			<c:if test="${item.wzType=='5'}">
				法律条款
			</c:if>
			<c:if test="${item.wzType=='6'}">
				用户协议
			</c:if>
		</ec:column>
		<ec:column width="100" property="content" title="文章内容" ellipsis="true"/>
		<ec:column width="100" property="_0" title="操作"
			style="text-align:center" sortable="false">
			<a href="edit.do?model.id=${item.id}" title="修改文章">编辑</a> |
			<a href="javascript:remove('${item.id}')" title="删除文章">删除</a>
		</ec:column>
	</ec:row>
</ec:table></div>
</div>
<script type="text/javascript" src="${ctx}/pages/amol/base/product/prosortCombox.js"></script>
</body>
</html>