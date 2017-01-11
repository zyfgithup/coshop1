<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>快递公司管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function remove(id){
	if (confirm("确认要删除该快递公司吗？")){
		window.location.href = "${ctx}/base/expressCompany/remove.do?model.id=" + id;
	}
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">快递公司管理</div>
<div class="x-toolbar">
     <table width="100%">
       <tr>
             <td style=" padding-left:5px; padding-top:5px;" align="right">   
               <a href="${ctx}/base/expressCompany/edit.do"><img src="${ctx}/images/icons/add.gif" />新建快递公司</a>&nbsp;
             </td>
       </tr>
     </table>
   </div>
   <div><%@ include file="/common/messages.jsp"%></div>
<div class="x-panel-body">
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
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="200" property="name" title="名称"/>
		<ec:column width="200" property="user.name" title="创建人"/>		
		<ec:column width="100" property="_0" title="操作" style="text-align:center" sortable="false">
			<a href="edit.do?model.id=${item.id}" title="修改商品单位">编辑</a> | 
			  <a href="javascript:remove(${item.id});">
			               删除
			  </a>
		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>