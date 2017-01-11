<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>销售应收管理</title>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function remove(id){
	if (confirm("确认要冲红吗？冲红后不能恢复！")) {
		window.location.href="${ctx}/sales/redRed.do?model.id=" + id;
     }
}

function removeAo(){
	alert("此出库单已经生成退货单 ，不能冲红！");	
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">销售应收【现金】</div>
	<div style="float: right;">
	</div>
	
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form action="${ctx}/sales/queryCashIndex.do" method="post">
		                     收款情况：<s:select list='receiveMap' name="receive" cssStyle="width:60px;"></s:select>             
		                     出库单：
		        <s:textfield name="model.salesNo" size="18" id="salesNo"/> 
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
<ec:table items="items" var="item" retrieveRowsCallback="process" sortRowsCallback="process" 
		action="queryCashIndex.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="出库单.xls" 
		pageSizeList="15,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="350px"
		minHeight="350"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
	<ec:row>
	    <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="70" property="customer.name" title="客户" ellipsis="true"/>
		<ec:column width="105" property="salesNo" title="销售单号" style="text-align:center"/>
		<ec:column width="60" property="payment.name" title="收款方式" style="text-align:center"/>
		<ec:column width="70" property="createTime" title="日期" cell="date" style="text-align:center"/>
		<ec:column width="65" property="samount" title="应收金额" style="text-align:right" format="###,##0.00" cell="number"></ec:column>
		<ec:column width="65" property="spayamount" title="实收金额" style="text-align:right" format="###,##0.00" cell="number"></ec:column>
		<ec:column width="65" property="rttao" title="应退金额" style="text-align:right" format="###,##0.00" cell="number"/><!-- 退货货款金额 -->
		<ec:column width="65" property="realReturnMoney" title="实退金额" style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="65" property="w" title="未收金额" style="text-align:right" format="###,##0.00" cell="number">
		    	${item.samount  - item.rttao - (item.spayamount  - item.realReturnMoney) }
		</ec:column>
		<ec:column width="50" property="redRed" title="状态" style="text-align:center">
		  <s:if test="#attr.item.redRed == 1">正常 </s:if>
		</ec:column>
		<ec:column width="70" property="user.name" title="经销商" ellipsis="true"/>
		<ec:column width="60" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			<c:if test="${item.salesNo eq '期初' }">
				<font color="#999999">详情</font>
			</c:if>
			<c:if test="${!(item.salesNo eq '期初') }">
				<a href="view.do?model.id=${item.id}">详情</a>
			</c:if>
		</ec:column>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="5" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px;">金额单位为“元”，合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${samount }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${spayamount }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${rttao }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${realReturnMoney }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${samount  - rttao - (spayamount  - realReturnMoney) }" pattern="#,##0.00"/></td>
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