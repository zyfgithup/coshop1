<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>商品单位管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function remove(id){
	if (confirm("确认要删除商品单位吗？")){
		window.location.href = "${ctx}/base/units/remove.do?model.id=" + id;
	}
}
function removeAo(){
	alert("此商品单位已经使用 ，不能删除！");
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">商品单位管理</div>
<div class="x-toolbar">
     <table width="100%">
       <tr>
             <td style=" padding-left:5px; padding-top:5px;" align="right">   
               <a href="${ctx}/base/units/edit.do"><img src="${ctx}/images/icons/add.gif" />新建单位</a>&nbsp;
             </td>
       </tr>
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
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="200" property="name" title="名称"/>
		<ec:column width="200" property="user.name" title="创建人"/>		
		<ec:column width="100" property="_0" title="操作" style="text-align:center" sortable="false">
			<a href="edit.do?model.id=${item.id}" title="修改商品单位">编辑</a> | 
			<c:if test="${item.unitsItem != '[]'}">
			  <a href="javascript:removeAo();">
			     <font color="#999999">删除</font>
			  </a>
			</c:if>
			<c:if test="${item.unitsItem == '[]'}">
			  <a href="javascript:remove(${item.id});">
			               删除
			  </a>
			</c:if>
				
		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>