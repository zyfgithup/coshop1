<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>提现金额设置</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function remove(id){
	if (confirm("确认要删除提现设置吗？")){
		window.location.href = "${ctx}/base/txmoneyset/remove.do?txsetId=" + id;
	}
}

</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">提现金额设置</div>
<div class="x-toolbar">
     <table width="100%">
     <c:if test="${flag=='no'}">
       <tr>
             <td style=" padding-left:5px; padding-top:5px;" align="right">   
               <a href="${ctx}/base/txmoneyset/edit.do"><img src="${ctx}/images/icons/add.gif" />设置提现金额</a>&nbsp;
             </td>
       </tr>
       </c:if>
     </table>
   </div>
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
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="true" style="text-align:center"/>
		<ec:column width="200" property="money" title="提现金额" sortable="false"/>
		<ec:column width="100" property="_0" title="操作" style="text-align:center" sortable="false">
			<a href="edit.do?model.id=${item.id}" title="修改提现金额设置">编辑</a>|
			<a href="javascript:remove(${item.id});">删除</a> 
		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>