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
			选择员工部门:</td>
		<td>
			<div id='comboxWithTree' style=""></div></td>
	</tr>
	<tr>
		<td>员工部门ID:</td>
		<td>
			<input type="text" id="model.empDept.id"></td>
	</tr>
</table>
<script type="text/javascript"
	src="${ctx}/pages/amol/base/dept/empDeptCombox.js"></script>
<script type="text/javascript">
Ext.onReady(function() {
	var pstree = new EmpDeptTree({
		el : 'comboxWithTree',
		target : 'model.empDept.id',
		url : '${ctx}/base/empdept/empDeptTree.do',
		//设置默认显示数据
			defValue : {
			id : '${model.empDept.id}',
			text : '${model.empDept.name}'
		 }
	});
	pstree.init();

});
</script>
</body>
</html>