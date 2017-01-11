<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>库存期初管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>

</head>
<body>
<div class="x-panel">
<div class="x-panel-header">库存期初管理</div>
<div class="x-toolbar">
     <table width="100%">
     	<s:form action="index" theme="simple">
       <tr>
        <td>
        	商品编码：<s:textfield name="model.products.code" size="15"/>&nbsp;&nbsp; 
        	商品名称：<s:textfield name="model.products.name" size="15"/>&nbsp;&nbsp;
        	仓库名称：<s:select list="storageMap" name="model.storage.id" headerKey="" id="storageId" 
                     headerValue="全部" cssStyle="width:105px;"/>	
			供应商：<s:textfield name="model.products.supplier.name" size="18" />
        </td>
       </tr>
       <tr>
       		<td>
       		开始日期：<input id="startDate" name="startDate" size="15"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
      		结束日期：<input id="endDate" name="endDate" size="15" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;  	                      
        	&nbsp;&nbsp;&nbsp;<s:submit value="查询" cssClass="button"></s:submit>
        	</td>
       </tr>
       </s:form>
     </table>
   </div>
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
			action="index.do"
		useAjax="true" doPreload="false"
		xlsFileName="期初库存.xls" 
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="true"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="400px"	
		minHeight="400"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status">
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="80" property="products.code" title="商品编码" ellipsis="true"/>
		<ec:column width="100" property="products.name" title="商品名称" ellipsis="true"/>
		<ec:column width="80" property="products.stardard" title="商品规格" ellipsis="true"/>
		<ec:column width="100" property="products.supplier.name" title="供应商" ellipsis="true"/>
		<ec:column width="80" property="storage.name" title="仓库" ellipsis="true"/>
		<ec:column width="50" property="products.units.name" title="单位" ellipsis="true"/>
		<ec:column width="60" property="count" title="数量" ellipsis="true" style="text-align:right"/>
		<ec:column width="60" property="unitPack" title="包装单位" ellipsis="true" style="text-align:right"/>
		<ec:column width="80" property="createTime" title="创建时间" cell="date" style="text-align:center" ellipsis="true"/>
		<ec:column width="100" property="user.name" title="创建人" ellipsis="true" />	
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="7" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">${total}</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>			
			</tr>
		</ec:extendrow>
	</s:if>
</ec:table>
</div>
</div>
</body>
</html>