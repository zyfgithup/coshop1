<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>期初应收管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">期初应收管理</div>
</div>  

<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form action="${ctx}/receiveInit/index.do" method="post">
		                     客户：<s:textfield name="model.customer.name" size="8" id="name"/>
		       	开始日期：<input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
		       	结束日期：<input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;  	                      
		        <input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button"/>
           </form>
         </td>
       </tr>
     </table>
</div>  
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="${ctx}/receiveInit/index.do"
		useAjax="true" doPreload="false"
		xlsFileName="应收初始化数据.xls"
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
		<ec:column width="170" property="customer.name" title="客户名称" ellipsis="true"/>
		<ec:column width="100" property="amount" title="应收金额"  style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="100" property="amountReceived" title="已收金额"  style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="100" property="0_" title="未收金额"  style="text-align:right" format="###,##0.00" cell="number">
			${item.amount - item.amountReceived }
		</ec:column>			
	</ec:row>
	
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="2" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">${amountTotal }元</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">${amountReceivedSum }元</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">${amountTotal - amountReceivedSum }元</td>
			</tr>
		</ec:extendrow>
	</s:if>
</ec:table>
</div>
</div>
</body>
</html>