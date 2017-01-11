<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>商品管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
function remove(id){
	if (confirm("确认要删除商品吗？")){
		window.location.href="${ctx}/base/product/productTemplate/remove.do?model.id=" + id;
	}
}

/**
 * 图片地址
 */
function viewImage(urlImage){
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog(urlImage,null,"dialogWidth=800px;resizable: Yes;");
	}else{
		window.open(urlImage,"","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
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
	<div class="x-panel-header">公共图片管理</div>
<div class="x-toolbar">
<table width="100%">
	<tr>
		<s:form action="index" theme="simple" method="post">
			<!-- td align="left">商品类型</td>
			<td align="left">
				<div id='comboxWithTree'></div>
				<s:hidden id="prosortId" name="model.prosort.id"
					cssStyle="display: inline;" /></td>
			<td>商品编码</td>
			<td><s:textfield name="model.code" size="10" /></td> -->
			<td>
				商品名称：<s:textfield name="model.name" size="10" /><s:submit value="查询" cssClass="button"></s:submit>
			</td>
		</s:form>
		<td style="padding-left: 5px; padding-top: 5px; width: 50%"
			align="right"><a href="${ctx}/base/product/productTemplate/edit.do"><img
			src="${ctx}/images/icons/add.gif" />&nbsp;添加&nbsp;&nbsp;</a></td>
	</tr>
</table>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-panel-body">

<!-- 悬浮图片 -->
<div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>

<ec:table items="items" var="item"
	retrieveRowsCallback="limit" sortRowsCallback="limit" action="index.do"
	useAjax="true" doPreload="false" maxRowsExported="10000000"
	pageSizeList="15,20,50,100" editable="false" sortable="false"
	rowsDisplayed="15" generateScript="true" resizeColWidth="true"
	classic="false" width="100%" height="380px" minHeight="380"
	toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
		<ec:column width="30" property="_s" title="No."
			value='${GLOBALROWCOUNT}' sortable="false" style="text-align:center" />
		<ec:column width="100" property="_title" title="图片" style="text-align:center">
			 <a href="javascript:viewImage('${ctx }'+'${item.imageURL }');"><img alt="${item.name }" src="${ctx }/${item.imageURL }" width="26" height="26"/></a>
		</ec:column>
		<ec:column width="200" property="name" title="商品名称" ellipsis="true"/>
		<ec:column width="100" property="_0" title="操作"
			style="text-align:center" sortable="false">
			<a href="edit.do?model.id=${item.id}" title="修改商品">编辑</a> | 
			<a href="javascript:remove('${item.id}')">删除</a>
		</ec:column>
	</ec:row>
</ec:table></div>
</div>
<script type="text/javascript" src="${ctx}/pages/amol/base/product/prosortCombox.js"></script>
<script type="text/javascript">
Ext.onReady(function() {
	var pstree = new ProsortTree({
		el : 'comboxWithTree',
		target : 'prosortId',
		//emptyText : '选择商品类型',
		url : '${ctx}/base/prosort/prosortTree.do?status=1',
		defValue : 
			{
				id:'${model.prosort.id}',
				text:'${model.prosort.name}'
			}
	});
	pstree.init();		
});
</script>
<!-- 
		<!-- ec:column width="150" property="subtitle" title="副标题" ellipsis="true"/>
		<ec:column width="60" property="units.name" title="基本单位" style="text-align:center"/>
		<ec:column width="120" property="prosortName" title="商品类别" ellipsis="true">
			<s:if test="#attr.item.prosort.status == 0">
				<font color="red">${item.prosort.name}</font>
			</s:if>
			<s:else>
				${item.prosort.name}
			</s:else>
		</ec:column>	
		<ec:column width="80" property="stardard" title="商品规格" ellipsis="true"/>
		<ec:column width="80" property="code" title="商品编码" ellipsis="true"/>	
		<ec:column width="80" property="barcode" title="商品条码" ellipsis="true"/> --> 
</body>
</html>