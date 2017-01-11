<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>应付账款管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
 
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">应付账款管理</div>
<div class="x-toolbar">
     <table width="100%">
       <tr>
       <td> 
        <s:form action="index" theme="simple">
        	供应商名称：<s:textfield name="model.supplier.name" size="32"/>&nbsp;&nbsp;
        	<s:submit value="查询" cssClass="button"></s:submit>
         </s:form>
        </td>
             
       </tr>
     </table>
   </div>
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
			action="index.do"
		useAjax="true" doPreload="false"
			xlsFileName="应付汇总.xls"
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
	toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="200" property="supplier.region.name" title="地区"/>
		<ec:column width="200" property="supplier.name" title="供应商名称"/>
		<ec:column width="100" property="amount" title="应付金额" style="text-align:right"  format="###,##0.00"  cell="number"/>
			
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="3" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
				<fmt:formatNumber value="${Total}" pattern="#,##0.00"/></td>
				<td ></td>
				<td ></td>
				<td ></td>
			</tr>
		</ec:extendrow>
	</s:if>
</ec:table>
</div>
</div>
</body>
</html>