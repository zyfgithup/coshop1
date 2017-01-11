<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>货品流向查询</title>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">货品流向查询</div>
</div>

<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form action="${ctx}/barcode/indexADealer.do" method="post">
          	          供应商：
          	    <!-- s:select list="supplierMap" name="suppliersId" headerKey="" id="supplierId" 
                     headerValue="请选择供应商" cssClass="supplierCheck" cssStyle="width:100px;color:#808080;border: 1px solid #808080;"/>
		         -->
		        <s:textfield name="supplierName" size="13" id="supplierId"/>          
		                     条形码：<s:textfield name="model.barcode" size="20" id="salesNo"/>
		                     商品：<s:textfield name="model.salesDetail.products.name" size="10" id="salesNo"/> 
		       	开始日期：<input id="startDate" name="startDate" size="12"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
		       	结束日期：<input id="endDate" name="endDate" size="12" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;  	                      
		        <input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button"/>
           </form>
         </td>
       </tr>
     </table>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="indexADealer.do" 
		useAjax="false"
		doPreload="false" 
		pageSizeList="15,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="400px"
		minHeight="400"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
	    <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="150" property="salesDetail.products.name" title="商品" style="text-align:center" ellipsis="true"/>
		<ec:column width="150" property="barcode" title="条形码" style="text-align:center" ellipsis="true"/>		
        <ec:column width="80" property="salesDetail.sales.createTime" title="日期" cell="date" style="text-align:center"/>		
		<ec:column width="140" property="_0" title="经销商" style="text-align:center" ellipsis="true">
			<c:if test="${item.salesDetail.sales.user.superior.id == null }">
				<font color='red'>${item.salesDetail.sales.user.name }</font>
			</c:if>
			<c:if test="${item.salesDetail.sales.user.superior.id != null }">
				<font color='red'>${item.salesDetail.sales.user.superior.name }</font>
			</c:if>
		</ec:column>
		<ec:column width="140" property="_0" title="分销商" style="text-align:center" ellipsis="true">
			<c:if test="${item.salesDetail.sales.user.superior.id != null }">
				<font color='red'>${item.salesDetail.sales.user.name }</font>
			</c:if>
		</ec:column>
		<ec:column width="100" property="salesDetail.sales.customer.name" title="客户" style="text-align:center" ellipsis="true"/>
	</ec:row>
	
</ec:table>
</div>
</div>
</body>
</html>