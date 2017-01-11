<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>应收账款现金</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">应收账款【现金汇总】</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form action="${ctx}/sales/receiveAble.do" method="post">
		                     客户：<s:textfield name="model.customer.name" size="8" id="name"/>
		       	开始日期：<input id="startDate" name="startDate" size="12"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
		       	结束日期：<input id="endDate" name="endDate" size="12" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;  	                      
		        <input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button"/>
		        <input type="button" value="应收查询" onclick="window.location.href='${ctx}/sales/queryCashIndex.do'" class="button"/>
           </form>
         </td>
       </tr>
     </table>
   </div>
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="${ctx}/sales/receiveAble.do"
		useAjax="true" doPreload="false"
		xlsFileName="应收账款（现金汇总）.xls"
		maxRowsExported="10000000" 
		pageSizeList="20,50,100" 
		editable="false" 
		sortable="true"	
		rowsDisplayed="20"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="350px"	
		minHeight="350"
	toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  
		>
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="160" property="regionName" title="地区"/>
		<ec:column width="120" property="customerName" title="客户名称" ellipsis="true"/>
		<ec:column width="220" property="idCard" title="身份证"/>
		<ec:column width="180" property="mobile" title="手机号"/>
		<ec:column width="100" property="amount" title="应收金额" style="text-align:right" format="###,##0.00" cell="number"/>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="5" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px;">金额单位为“元”，应收合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${amountSum }" pattern="#,##0.00"/></td>
			</tr>
		</ec:extendrow>
	</s:if>
</ec:table>
</div>
</div>
</body>
</html>