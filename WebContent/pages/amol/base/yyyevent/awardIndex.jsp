<%@page import="com.systop.common.modules.region.RegionConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>跟踪记录管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox1.js"> </script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
function remove(id,isUsed){
	if (confirm("确认要删除该跟踪记录吗？")){
		window.location.href="${ctx}/base/yyy/remove.do?model.isUsed='"+isUsed+"'&model.id=" + id;
	}
}
function ssYyy(id,isUsed){
	var msg="";
	if(isUsed=="1"){
		msg="开始";
	}else{
		msg="结束";
	}
	if (confirm("确认要"+msg+"该摇一摇活动吗？")){
		window.location.href="${ctx}/base/yyy/ssYyy.do?model.isUsed="+isUsed+"&model.id=" + id;
	}
}
Ext.onReady(function() {
	var pstree = new RegionTree({
		el : 'comboxWithTree',
		target : 'regionId',
		//emptyText : '选择地区',
		comboxWidth : 200,
    	treeWidth : 195,
		url : '${ctx}/admin/region/regionTree.do?regionId=<%=RegionConstants.HBS_ID %>',
		defValue : {id:'${regionId}',text:'${regionNameCun}'}
	});
	pstree.init();	
	
});
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
<div class="x-panel-header">跟踪记录管理</div>
<div class="x-toolbar">
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-panel-body">
<table width="100%">
	<tr>
		<td align="right">
		<table>
			<tr>
				<td><a href="${ctx}/base/yyy/index.do"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>&nbsp;返回&nbsp;&nbsp;</a></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<!-- 悬浮图片 -->
<div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>

<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="awardIndex.do"
		useAjax="true" doPreload="false"
		maxRowsExported="10000000" 
		pageSizeList="15,20,50,100" 
		editable="false" 
		sortable="true"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="380px"	
		minHeight="380"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
		<ec:column width="150" property="yyyEvent.createUser.loginId" title="区域管理员" ellipsis="true"/>
		<ec:column width="150" property="showTime" title="跟踪时间" ellipsis="true"/>
		<ec:column width="200" property="gzContent" title="跟踪内容" ellipsis="true"/>
	</ec:row>
</ec:table></div>
</div>
</body>
</html>