<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<title>资金类型</title>
<script type="text/javascript">
function remove(id){
   if (confirm("确认要删除资金类型吗？")) {
       window.location.href="${ctx}/finance/fundsSort/remove.do?model.id=" + id;
   }
}

function removeAo(){
	alert("此资金类型下已有费用明细信息，不能删除！");
}
</script>
</head>
<body>
<div class="x-panel">
  <div class="x-panel-header">资金类型</div>
  <div class="x-toolbar">
     <s:if test="model.user.beginningInit==1">
     <s:if test="model.user.superior == null">
     <table width="100%">
       <tr>
             <td style=" padding-left:5px; padding-top:5px;" align="right"> 
				<a href="${ctx}/finance/fundsSort/edit.do"><img src="${ctx}/images/icons/add.gif" />新建类型</a>               
             </td>
       </tr>
     </table>
     </s:if>
     </s:if>
   </div>
   <div><%@ include file="/common/messages.jsp"%></div>
   <div class="x-panel-body">
     <div style="margin-left:-3px;" align="center">
     <ec:table items="items" var="item" retrieveRowsCallback="process" sortRowsCallback="process"
	   action="index.do" 
	   useAjax="true"
	   doPreload="false"
	   pageSizeList="15,35,45" 
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
		<ec:column width="40" property="_o" title="No." value="${GLOBALROWCOUNT}" style="text-align:center" sortable="false"/>
		<ec:column width="350" property="name" title="资金类型" />
		<s:if test="model.user.superior == null">
		<ec:column width="100" property="_0" title="操作" style="text-align:center" sortable="false">
			<a href="edit.do?model.id=${item.id}">编辑</a> |
			
			<c:if test="${item.costs != '[]'}">
			  <a href="#" onclick="removeAo()">
			     <font color="#999999">删除</font>
			  </a>
			</c:if>
			<c:if test="${item.costs == '[]'}">
			  <a href="#" onclick="remove(${item.id})">
			               删除
			  </a>
			</c:if>
			
		</ec:column>
		</s:if>
 		
	  </ec:row>
    </ec:table>
    </div>
  </div>
</div>
</body>
</html>