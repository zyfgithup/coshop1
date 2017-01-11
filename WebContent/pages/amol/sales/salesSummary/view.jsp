<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>明细</title>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">明细</div>
	<div style="float: right;">
	</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form action="${ctx}/salesSummary/view.do" method="post">
          	    <s:hidden name="productId"/>
          	    <s:hidden name="distributorId"/>
          	          身份证号：<s:textfield name="idCard" size="20" maxLength="18" id="idCard"/>
		       	开始日期：<input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
		       	结束日期：<input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;  	                      
		        <input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button"/>
		        <input type="button" value="返回" onclick="history.go(-1);" class="button"/>
           </form>
         </td>
       </tr>
     </table>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="process" sortRowsCallback="process" 
		action="index.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="销售汇总.xls" 
		pageSizeList="15,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="false"	
		width="100%" 	
		height="380px"	
		minHeight="380"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
	<ec:row>
	    <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="70" property="products.code" title="编 号" style="text-align:center" ellipsis="true"/>
		<ec:column width="70" property="products.name" title="商 品" style="text-align:center" ellipsis="true"/>
		<ec:column width="70" property="products.stardard" title="规格" style="text-align:center" ellipsis="true"/>
		<ec:column width="70" property="sales.customer.name" title="农户" ellipsis="true"/>
		<ec:column width="115" property="sales.customer.idCard" title="身份证号" ellipsis="true"/>
		<ec:column width="200" property="sales.cardGrant.card.cardNo" title="卡号" ellipsis="true"/>
		<ec:column width="60" property="ncount" title="数量" style="text-align:right"/>
		<ec:column width="60" property="units.name" title="单位" style="text-align:center"/>
		<ec:column width="60" property="outPrice" title="价格" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true"/>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>