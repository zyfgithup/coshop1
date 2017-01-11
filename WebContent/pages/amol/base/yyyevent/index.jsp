<%@page import="com.systop.common.modules.region.RegionConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>学员管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox1.js"> </script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
function remove(id){
	if (confirm("确认要删除该学员吗？")){
		window.location.href="${ctx}/base/yyy/remove.do?model.id=" + id;
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
function downLoadMb(){
	location.href="${ctx}/template/import/stuExcel.xls";
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">学员管理</div>
<div class="x-toolbar">
<table >
	<tr>
		<s:form action="index" theme="simple">
			<td>学员名字</td>
			<td><s:textfield name="model.stuName" size="10" /></td>
			<td>籍贯</td><td><span id='comboxWithTree'></span><s:hidden id="regionId" name="regionId"/></td>
			<c:if test="${logonUser.type=='admin' }">
				<td>区域管理员</td>
				<td><s:textfield name="model.createUser.loginId" size="10" /></td>
			</c:if>
			<td><s:submit value="查询" cssClass="button"></s:submit></td>
		</s:form>
		<td style="padding-left: 5px; padding-top: 5px; width: 50%"
			align="right"><a href="${ctx}/base/yyy/edit.do"><img
			src="${ctx}/images/icons/add.gif" />&nbsp;录入学员&nbsp;</a>

			<a href="importEdit.do"><img
					src="${ctx}/images/icons/add.gif" />&nbsp;Excel导入&nbsp;&nbsp;&nbsp;<a href="javascript:downLoadMb()">&nbsp;模版下载&nbsp;</a>
		</td>
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
		<ec:column width="80" property="createUser.loginId" title="区域管理员" ellipsis="true"/>
		<ec:column width="80" property="stuName" title="学员姓名" ellipsis="true"/>
		<ec:column width="200" property="_regionName" title="籍贯"  ellipsis="true">
			${item.region.parent.parent.name }&nbsp;&nbsp;${item.region.parent.name }&nbsp;&nbsp;${item.region.name }
		</ec:column>
		<ec:column width="80" property="_stuSex" title="学员性别" ellipsis="true">
			<c:if test="${item.stuSex=='0'}">
				女

			</c:if>
			<c:if test="${item.stuSex=='1'}">
				男

			</c:if>
		</ec:column>
		<ec:column width="80" property="school.name" title="所属学校" ellipsis="true"/>
		<ec:column width="200" property="_regionName" title="系、部、专业"  ellipsis="true">
			${item.xbzy.parentProsort.parentProsort.name }&nbsp;&nbsp;${item.xbzy.parentProsort.name }&nbsp;&nbsp;${item.xbzy.name }
		</ec:column>
		<ec:column width="60" property="domNum" title="宿舍号"  ellipsis="true"/>
		<ec:column width="60" property="stuQq" title="QQ号" ellipsis="true"/>
		<ec:column width="90" property="stuPhone" title="手机号" ellipsis="true"/>
		<ec:column width="130" property="bzrName" title="班主任/导员" ellipsis="true"/>
		<ec:column width="80" property="relatePhone" title="联系电话" ellipsis="true"/>
		<ec:column width="80" property="trzw" title="担任职务" ellipsis="true"/>
		<ec:column width="290" property="_0" title="操作"
			style="text-align:center" sortable="false">
			<c:if test="${item.childYyyAwards=='[]' }">
				<a href="javascript:remove('${item.id}')">删除</a> |
			</c:if>
			<c:if test="${item.childYyyAwards!='[]' }">
				<a href="#"><font color="#999999">删除</font></a> |
			</c:if>
			<a href="edit.do?model.id=${item.id}" title="修改商品">编辑</a> |
			<a href="${ctx }/base/yyyaward/addAward.do?eventId=${item.id}">录入跟踪记录</a> |
			<a href="${ctx }/base/yyyaward/awardIndex.do?id=${item.id}">查看跟踪记录</a>
		</ec:column>
	</ec:row>
</ec:table></div>
</div>
</body>
</html>