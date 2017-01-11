<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<%@include file="/common/taglibs.jsp"%>
<title>消费查询</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/autocomplete.jsp"%>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">消费查询</div>
<div class="x-toolbar">
<table width="99%" >
	<tr>
		<td>
		<form id="search" action="${ctx }/card/grant/indexSales.do" method="post">
		           卡号：
			<s:textfield name="model.card.cardNo" id="cardNo" style="width: 180px" value="%{card.cardNo}" maxlength="22" onkeyup="this.value=this.value.replace(/[^\d+(\d+)?$]/g,'')" />
			身份证号：
			<s:textfield name="model.customer.idCard" maxlength="18" style="width: 140px" />
			<s:submit value="查询" cssClass="button"></s:submit>
		</form>
		</td>
	</tr>
</table>
</div>
<div class="x-panel-body">
<ec:table 
		items="items" 
		var="item" 
		retrieveRowsCallback="limit" 
		sortRowsCallback="limit" 
		action="${ctx }/card/grant/indexSales.do" 
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
		height="395px"
		minHeight="395"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
		<ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="160" property="card.cardNo" title="卡号" style="text-align:center"/>
		<ec:column width="100" property="customer.owner.name" title="经销商" style="text-align:center" ellipsis="true"/>
		<ec:column width="80" property="customer.name" title="持卡人" style="text-align:center" ellipsis="true"/>
		<ec:column width="120" property="customer.idCard" title="身份证号" style="text-align:center"/>
		<ec:column width="80" property="createDate" title="开卡日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="80" property="endDate" title="失效日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="80" property="spend" title="消费额" style="text-align:right" format="#,##0.00" cell="number" />
		<ec:column width="80" property="balance" title="余额" style="text-align:right" format="#,##0.00" cell="number" />
		<ec:column width="60" property="card.cardState" title="卡状态" style="text-align:center" mappingItem="cardStateMap"/>
		<ec:column width="60" property="_0" title="操作" style="text-align:center" sortable="false">			
				    <a href="${ctx }/card/spend/index.do?model.cardGrant.card.cardNo=${item.card.cardNo }" title="查看">查看</a>
		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
<script type="text/javascript">
//更新代币卡状态
function updateCard(id,state) {
	if (confirm("确认要修改代币卡状态吗？")){
		window.location.href = "${ctx}/card/grant/updateCard.do?model.id=" + id+"&model.card.cardState="+state;
	}
}
function refresh() {
	ECSideUtil.reload('ec');
};

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
</script>
</body>
</html>