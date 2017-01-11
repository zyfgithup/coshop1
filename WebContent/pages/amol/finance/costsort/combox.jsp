<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp"%>
<title>属性列表选择器</title>
</head>
<body>
<table align="center">
	<tr>
		<td>
			选择收支类别:</td>
		<td>
			<div id='comboxWithTree' style=""></div></td>
	</tr>
	<tr>
		<td>收支类别ID:</td>
		<td>
			<input type="text" id="costsort_id"></td>
	</tr>
</table>
<script type="text/javascript"
	src="${ctx}/pages/amol/finance/costsort/costSortCombox.js"></script>
<script type="text/javascript">
	Ext.onReady(function() {
		var pstree = new CostSortTree({
			el : 'comboxWithTree',
			target : 'costsort_id',
			//emptyText : '选择商品类型',
			//type=1表示收入2表示支出
			url : '${ctx}/finance/costSort/costSortTree.do?type=1'
			//设置默认显示数据
			//	defValue : {
			//	id : '${model.prosort.id}',
			//	text : '${model.prosort.name}'
			//}
		});
		pstree.init();

	});
</script>
</body>
</html>