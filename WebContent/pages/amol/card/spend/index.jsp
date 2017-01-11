<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<%@include file="/common/taglibs.jsp"%>
<title>消费明细查询</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/autocomplete.jsp"%>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">消费明细查询</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
<table width="99%">
	<tr>
		<td>
		<form id="search" action="index.do" method="post">
		           身份证号：      
		    <s:textfield style="width: 130px" maxlength="18" name="idCard" id="idCard" />
		           卡号：
			<s:textfield style="width: 180px" maxlength="22" name="model.cardGrant.card.cardNo" id="cardNo" onkeyup="this.value=this.value.replace(/[^\d+(\d+)?$]/g,'')" />
			<s:hidden name="jxs"/>
			开始日期：<input id="startDate" name="startDate" size="12"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
	                     结束日期：<input id="endDate" name="endDate" size="12" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
			<s:submit value="查询" cssClass="button"></s:submit>
			<input type="button" class="button" value="返回" onclick="history.go(-1);"/>
		</form>
		</td>
	</tr>
</table>
</div>
<div class="x-panel-body">
<ec:table items="items" 
        var="item" 
        retrieveRowsCallback="limit" 
        sortRowsCallback="limit" 
		action="index.do" 
		xlsFileName="消费记录表.xls" 
		useAjax="false"
		doPreload="false" 
		pageSizeList="15,17,20," 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="350px"
		minHeight="350"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status">
	<ec:row>
		<ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="180" property="cardGrant.card.cardNo" style="text-align:center" title="卡号"/>
		<ec:column width="80" property="cardGrant.customer.name" title="持卡人" style="text-align:center" ellipsis="true"/>
		<ec:column width="115" property="cardGrant.customer.idCard" title="身份证号" style="text-align:center" ellipsis="true"/>
		<ec:column width="80" property="spendDate" title="消费日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="75" property="spendMoney" style="text-align:right" title="消费金额">
			<fmt:formatNumber value="${item.spendMoney}" pattern="#,##0.00"/>
		</ec:column>
		<ec:column width="50" property="cardGrant.card.cardState" style="text-align:right" title="卡状态" mappingItem="cardStateMap"/>
		<ec:column width="75" property="cardGrant.balance" style="text-align:right" title="最新余额" format="#,##0.00" cell="number" />
		<ec:column width="50" property="spendMoney" style="text-align:center" title="方式">
			<c:choose>
			    <c:when test="${item.spendMoney > 0}">
			    	<font color="red">消费</font>
			    </c:when>		    
			    <c:otherwise>
                    <font color="blue">退款</font> 	
			    </c:otherwise>
		    </c:choose>	
		</ec:column>
		<ec:column width="50" property="_0" title="操作" style="text-align:center" viewsAllowed="html" sortable="false">
			<a href="view.do?model.id=${item.id}" title="查看">查看</a>
		</ec:column>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="5" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">
					    消费合计（全部）	
				</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
					<fmt:formatNumber value="${totalSpendMoney}" pattern="#,##0.00"/>
				</td>
				<td style="border: 0px"></td>
				<td style="border: 0px"></td>
				<td style="border: 0px"></td>
				<td style="border: 0px"></td>
			</tr>
		</ec:extendrow>
	</s:if>
</ec:table>
</div>
</div>
<script type="text/javascript">
$().ready(function() {
	$.ajax({
		url: '${ctx}/card/grant/getCards.do',
		type: 'post',
		dataType: 'json',
		success: function(rows, textStatus){
			 $("#cardNo").autocomplete(rows, {
				  matchContains: true,
				  minChars: 0
		   });
		}
	});
});
  function refresh() {
    ECSideUtil.reload('ec');
  }
</script>
</body>
</html>