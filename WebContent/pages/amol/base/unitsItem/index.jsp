<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>商品单位换算管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function remove(id, productId){
   if (confirm("确认要删除单位吗？")) {
       window.location.href="${ctx}/base/units/item/remove.do?model.id=" + id+ "&productId=" + productId;
   }
}

</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">商品单位换算管理</div>
<div class="x-toolbar">
     <table width="100%">
       <tr>  
             <td><s:hidden name="productId" /></td>
             <td style=" padding-left:5px; padding-top:5px;" align="right">   
               <a href="${ctx}/base/units/item/edit.do?productId=${productId}"><img src="${ctx}/images/icons/add.gif" />新建单位换算</a>
               <a href="${ctx}/base/product/index.do"><img src="${ctx}/images/grid/filterArrow.gif" />返回商品列表</a>
             
             </td>
       </tr>
     </table>
   </div>
   <div><%@ include file="/common/messages.jsp"%></div>
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do" 
		useAjax="false"
		doPreload="false" 
		pageSizeList="15,50,100" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
        height="380px" 
        minHeight="380"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="100" property="units.name" title="单位" style="text-align:center"/>
		<ec:column width="100" property="count" title="换算数量" style="text-align:center">
		${item.count }${item.products.units.name }
		</ec:column>
		<ec:column width="100" property="conversion" title="折合包装" style="text-align:center">
		<s:if test="#attr.item.conversion == 1">
				否
			</s:if>
			<s:else>
				是
			</s:else>
		</ec:column>
		<ec:column width="75" property="outprice" title="参考出库价" style="text-align:right"/>
		<ec:column width="60" property="inprice" title="参考进价" style="text-align:right"/>		
		<ec:column width="100" property="_0" title="操作" style="text-align:center" sortable="false">
		    	<c:choose>
				    <c:when test="${item.count != 1 }">
						<a href="edit.do?model.id=${item.id}&productId=${item.products.id}" title="修改商品单位换算">编辑</a> | 
						<a href="#" onclick="remove(${item.id}, ${item.products.id})">删除</a>	
				    </c:when>		    
				    <c:otherwise>
	                    <font color="silver" >编辑</font> |
			            <font color="silver" >删除</font> 	
				    </c:otherwise>
			    </c:choose>	

		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>