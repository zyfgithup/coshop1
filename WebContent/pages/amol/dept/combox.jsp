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
			选择部门:</td>
		<td>
			<div id='comboxWithTree' style=""></div></td>
	</tr>
	<tr>
		<td>部门ID:</td>
		<td>
			<input type="text" id="model.dept.id"></td>
	</tr>
</table>
<script type="text/javascript"
	src="${ctx}/pages/amol/dept/deptCombox.js"></script>
<script type="text/javascript">
Ext.onReady(function() {
	var pstree = new DeptTree({
		el : 'comboxWithTree',
		target : 'model.dept.id',
		url : '${ctx}/dept/deptTree.do',
		//设置默认显示数据
			defValue : {
			id : '${model.dept.id}',
			text : '${model.dept.name}'
		 }
	});
	pstree.init();

});
</script>
</body>
</html>