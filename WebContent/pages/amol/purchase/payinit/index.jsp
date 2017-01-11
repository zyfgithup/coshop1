<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>期初应付管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">期初应付管理</div>
</div>  
<div class="x-toolbar">
     <table width="100%">
       <tr>
       <s:form action="index" theme="simple" id="form1">
         <td>供应商名称:
         <s:textfield name="model.supplier.name" id="sid" cssStyle="width:150px" />
          <s:submit value="查询" cssClass="button" cssStyle="width:40px;"></s:submit></td>
        </s:form>   
       </tr>
     </table>
</div>
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do"
		useAjax="true" doPreload="false"
		xlsFileName="应付初始化单.xls"
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
	toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  
		>
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="80" property="pdate" title="日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="170" property="supplier.name" title="供应商名称" ellipsis="true"/>
		<ec:column width="170" property="supplier.address" title="供应商地址" ellipsis="true"/>
		<ec:column width="170" property="supplier.phone" title="供应商电话"/>
		<ec:column width="100" property="amount" title="金额"  style="text-align:right" format="###,##0.00" cell="number"/>			
	</ec:row>
	
	
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="5" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">${amountTotal}</td>
				
			</tr>
		</ec:extendrow>
	</s:if>
</ec:table>
</div>
</div>
</body>
</html>