<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>消费金额统计</title>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function red(id){
	if (confirm("确认要冲红吗？冲红后不能恢复！")) {
		window.location.href="${ctx}/salesOrder/redRed.do?model.id=" + id;
     }
}

function removeAo(){
	alert("此订单已经生成出库单 ，不能冲红！");	
}

</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">消费金额统计</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
    <form action="${ctx}/salesOrder/returnMoney.do" method="post">
			&nbsp;&nbsp;订单：<s:textfield name="model.salesNo" size="17" id="salesNo"/> 
	                      用户：<s:textfield name="name" size="10" id="name"/>
	                      <input type="hidden" name="payment" value="CASH">
        	开始日期：<input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
        	结束日期：<input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>  	                      
			<s:submit value="查询" cssClass="button" cssStyle="width:36px;"/>
   </form>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="returnMoney.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="消费返现.xls" 
		pageSizeList="15,50,100" 
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
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="100" property="user.loginId" title="用户" ellipsis="true"></ec:column>
		<ec:column width="100" property="user.name" title="姓名" ellipsis="true"></ec:column>
		<ec:column width="200" property="_regionName" title="所属地区"  ellipsis="true">
		 	${item.user.region.parent.parent.name }&nbsp;&nbsp;${item.user.region.parent.name }&nbsp;&nbsp;${item.user.region.name }
		 </ec:column>
		<ec:column width="115" property="salesNo" title="订单号" style="text-align:center"/>
		<ec:column width="80" property="createTime" title="下单日期" style="text-align:center" cell="date"/>
		<ec:column width="115" property="count" title="商品总数量" style="text-align:center"/>
		<ec:column width="115" property="ttno" title="剩余数量" style="text-align:center"/>
		<ec:column width="90" property="payment.name" title="支付方式" style="text-align:center" />
		<ec:column width="90" property="_payState" title="付款状态" style="text-align:center">
			<c:if test="${empty item.payState }"><font color='red'>未付款</font></c:if>
			<c:if test="${item.payState }"><font color='green'>已支付</font></c:if>
		</ec:column>
		<ec:column width="90" property="samount" title="应收金额" style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="90" property="spayamount" title="实收金额" style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="100" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			<a href="view.do?model.id=${item.id}">详情</a>
		</ec:column>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="10" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px;">金额单位为“元”，应收合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${payTotal}" pattern="#,##0.00"/></td>
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