<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<title>属性列表选择器</title>
</head>
<body>
<script type="text/javascript" src="regionCombox.js"> </script>
<script type="text/javascript">
Ext.onReady(function() {
	var rtree = new RegionTree({
		el : 'comboxWithTree',
		target : 'region_id',
		url : '${ctx}/admin/region/regionTree.do',
		defValue : {}
	});
	rtree.init();	
	
});
</script>
<br>
<table align="center">
	<tr>
		<td>
			选择地区:</td>
		<td>
			<div id='comboxWithTree' style=""></div></td>
	</tr>
	<tr>
		<td>地区ID:</td>
		<td>
			<input type="text" id="region_id"></td>
	</tr>
</table>

</body>
</html>