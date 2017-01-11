<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
<title>数据库管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx}/pages/amol/database/all/index.js"></script>
<script type="text/javascript">
/**
 * 删除记录
 */
function removeBackup(id){
    if(confirm('确认要删除这条备份记录吗？')){
        window.location.href="${ctx}/databackup/all/remove.do?model.id=" + id;
    }	
}
</script>
</head>
<body>
<div class="x-panel">
	<div class="x-panel-header">数据库备份管理</div>
	<div><%@ include file="/common/messages.jsp"%></div>
		<div class="x-toolbar">
		<table width="100%">
			<s:form action="index" theme="simple">
			<s:hidden name="model.sign" value="1"/>
			<tr>
				<td valign="middle"">
					开始日期：&nbsp;&nbsp;<input id="startDate" name="startDate" size="12"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
      				结束日期：<input id="endDate" name="endDate" size="12" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
					<s:submit value="查询" cssClass="button"/>
				</td>
			</tr>
			</s:form>
			<tr>
				<td>
					备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：
					<input type="text" id="remark" name="model.remark" size="44">
					&nbsp;&nbsp;<input type="button" onclick="dataBackup();" value="备份" class="button"/>
				</td>
			</tr>
		</table>
	</div>
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
			action="index.do"
		useAjax="true" doPreload="false"
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="400px"	
		minHeight="400"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="120" property="createTime" title="创建时间" cell="date" format="yyyy-MM-dd hh:mm:ss" style="text-align:center" ellipsis="true"/>
		<ec:column width="160" property="user.name" title="创建人" style="text-align:center" ellipsis="true"/>
		<ec:column width="250" property="remark" title="备注" style="text-align:center" ellipsis="true"/>
		<ec:column width="140" property="_0" title="操作" style="text-align:center" sortable="false">
			<a href="#" onclick="removeBackup('${item.id}')" title="删除此次备份记录">删除</a>|
			<a href="#" onclick="downloadFile('${item.id}')" title="备份文件下载">下载</a>	
		</ec:column>	
	</ec:row>
	
</ec:table>
</div>
</div>
</body>
</html>