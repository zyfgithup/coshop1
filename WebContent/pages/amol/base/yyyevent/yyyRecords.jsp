<%@page import="com.systop.common.modules.region.RegionConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>摇一摇记录管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox1.js"> </script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
function lingQu(id){
    if (confirm("确认要领取该奖品吗?")){
	  window.location.href="lingQu.do?model.id=" + id;
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
		defValue : {id:'${regionId}',text:'${regionName}'}
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
<div class="x-panel-header">摇一摇记录管理</div>
<div class="x-toolbar">
<table width="100%">
	<tr>
		<s:form action="index" theme="simple">
			<td>摇一摇名称</td>
			<td><s:textfield name="eventName" size="10" /></td>
			<td>用户</td>
			<td><s:textfield name="loginId" size="10" /></td>
			<td>选择</td>
			<td><select name="isZj">
			<option value="">全部</option>
			<option value="0">未中奖</option>
			<option value="1">中奖</option>
			</select></td>
			<td>地区：</td><td><span id='comboxWithTree'></span><s:hidden id="regionId" name="regionId"/></td>
			<td><s:submit value="查询" cssClass="button"></s:submit></td>
		</s:form>
	</tr>
</table>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-panel-body">

<!-- 悬浮图片 -->
<div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>

<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do"
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
		<ec:column width="30" property="_s" title="No."
			value='${GLOBALROWCOUNT}' sortable="false" style="text-align:center" />
		<ec:column width="150" property="user.loginId" title="app用户" ellipsis="true"/>
		<ec:column width="150" property="yyyAward.luckyNum" title="幸运数字" ellipsis="true"/>
		<ec:column width="150" property="yyyEvent.eventName" title="摇一摇名称" ellipsis="true"/>
		<ec:column width="200" property="_regionName" title="所属地区"  ellipsis="true">
		 	${item.yyyEvent.region.parent.parent.name }&nbsp;&nbsp;${item.yyyEvent.region.parent.name }&nbsp;&nbsp;${item.yyyEvent.region.name }
		 </ec:column>
		<ec:column width="120" property="yyyAward.proName" title="奖品名称" ellipsis="true"/>
		<ec:column width="80" property="_event" title="中奖情况" ellipsis="true">
		<c:if test="${item.isZj=='0' }">
		未中奖
		</c:if>
		<c:if test="${item.isZj=='1' }">
		已中奖
		</c:if>
		</ec:column>
		<ec:column width="80" property="_event" title="领取情况" ellipsis="true">
		<c:if test="${item.flag=='0' }">
		未领取
		</c:if>
		<c:if test="${item.flag=='1' }">
		已领取
		</c:if>
		</ec:column>
		 <ec:column width="100" property="_0" title="操作"
			style="text-align:center" sortable="false">
			<c:if test="${item.flag=='0' }">
			<a href="javascript:lingQu('${item.id}')" title="确认领取"><font color="blue">确认领取</font></a>
			</c:if>
		</ec:column> 
	</ec:row>
</ec:table></div>
</div>
</body>
</html>