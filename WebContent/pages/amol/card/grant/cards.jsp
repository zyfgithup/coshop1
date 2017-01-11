<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<%@include file="/common/taglibs.jsp"%>
<title>发卡管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/autocomplete.jsp"%>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">客户代币卡	
<div class="x-toolbar">
<table width="99%">
	<tr>
		<td>
			<s:form id="search" action="search.do" method="post">
			           卡号：
			   <s:textfield name="model.card.cardNo" id="cardNo" style="width: 200px" value="%{card.cardNo}" maxlength="22" onkeyup="this.value=this.value.replace(/[^\d+(\d+)?$]/g,'')" />
				<s:submit value="查询" cssClass="button"></s:submit>				
			</s:form>
		</td>
		<td align="right">
		  <table>
			<tr>			
				<td valign="middle">
					<a href="${ctx}/base/customer/index.do"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>&nbsp;返回&nbsp;&nbsp;</a>
				</td>
		  </table>
		</td>
	</tr>
</table>
</div>
</div>
<div class="x-panel-body">
<ec:table 
		items="items" 
		var="item" 
		retrieveRowsCallback="limit" 
		sortRowsCallback="limit" 
		action="index.do" 
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
		<ec:column width="180" property="card.cardNo" title="卡号" style="text-align:center"/>
		<ec:column width="80" property="createDate" title="开卡日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="80" property="endDate" title="失效日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="80" property="upMoney" title="充值额" style="text-align:right" format="#,##0.00" cell="number" />
		<ec:column width="80" property="spend" title="消费额" style="text-align:right" format="#,##0.00" cell="number" />
		<ec:column width="80" property="balance" title="余额" style="text-align:right" format="#,##0.00" cell="number" />
		<ec:column width="80" property="credit" title="信用额度" style="text-align:right" format="#,##0.00" cell="number" />
		<ec:column width="80" property="card.cardState" title="卡状态" style="text-align:center" mappingItem="cardStateMap"/>
	</ec:row>
</ec:table>
</div>
</div>
<script type="text/javascript">
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