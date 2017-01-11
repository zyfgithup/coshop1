<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>商家/二手车类别</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
function remove(id){
	if (confirm("确认要删除该类别吗？")){
		window.location.href="${ctx}/base/prosort/removeMerSort.do?model.id=" + id;
	}
}
function showPic(url,event){
	$("#layer").html("<img src='"+url+"' with='300' height='300'>");
	$("#layer").show();
}
function hiddenPic(){
	$("#layer").html("");
	$("#layer").hide();
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">商家/二手车类别</div>
<div class="x-toolbar">
<table >
	<tr>
		<s:form action="merTableIndex" theme="simple">
			<td>类别名称</td>
			<td><s:textfield name="model.name" size="10" /></td>
			<td style="padding-left: 5px" align="left"><s:submit value="查询" cssClass="button"></s:submit>
			</td>
		</s:form>
		 <td style="padding-left: 5px; padding-top: 5px; width: 50%"
			align="right"><a href="${ctx}/base/prosort/merEdit.do?model.type=2"><img
			src="${ctx}/images/icons/add.gif" />&nbsp;新建&nbsp;</a></td>
	</tr>
</table>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-panel-body">
<!-- 悬浮图片 -->
<div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>

<ec:table items="items" var="item"
	retrieveRowsCallback="limit" sortRowsCallback="limit" action="merTableIndex.do"
	useAjax="true" doPreload="false" maxRowsExported="10000000"
	pageSizeList="15,20,50,100" editable="false" sortable="false"
	rowsDisplayed="15" generateScript="true" resizeColWidth="true"
	classic="false" width="100%" height="380px" minHeight="380"
	toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
		<ec:column width="30" property="_s" title="No."
			value='${GLOBALROWCOUNT}' sortable="false" style="text-align:center" />
		<ec:column width="100" property="_title" title="商家类别图片" style="text-align:center">
			 <a href="#"><img alt="${item.name }" src="${ctx }/${item.imageURL }" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" width="26" height="26"/></a>
		</ec:column>
		<ec:column width="120" property="name" title="类别名称" ellipsis="true"/>
		<ec:column width="120" property="_type" title="所属" ellipsis="true">
			<c:if test="${item.type=='2'}">
				店铺
			</c:if>
			<c:if test="${item.type=='4'}">
				二手车
			</c:if>
		</ec:column>
		<ec:column width="120" property="creator.name" title="创建者" ellipsis="true"/>
		<ec:column width="200" property="_0" title="操作"
				   style="text-align:center" sortable="false">
			<a href="merEdit.do?model.id=${item.id}&model.type=${item.type}" title="修改类别信息">编辑</a> |
			<c:if test="${item.childProductSorts=='[]'}">
				<a href="javascript:remove('${item.id}')">删除</a> |
			</c:if>
			<c:if test="${item.childProductSorts!='[]'}">
				<a href="#"><font color="#999999">删除</font></a> |
			</c:if>
			<a href="fillKinds.do?model.id=${item.id}">添加种类</a> |
			<a href="findKinds.do?model.id=${item.id}">查看种类</a>
		</ec:column>
	</ec:row>

</ec:table></div>
</div>
<script type="text/javascript" src="${ctx}/pages/amol/base/product/prosortCombox.js"></script>
</body>
</html>