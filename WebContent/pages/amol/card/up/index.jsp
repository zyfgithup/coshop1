<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<%@include file="/common/taglibs.jsp"%>
<title>充值管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/autocomplete.jsp"%>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">充值管理</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
<table width="99%">
	<tr>
		<td>
		<form id="search" action="index.do" method="post">
		           卡号：
			<s:textfield name="model.cardGrant.card.cardNo" id="cardNo" style="width: 180px" value="%{cardGrant.card.cardNo}" maxlength="22" onkeyup="this.value=this.value.replace(/[^\d+(\d+)?$]/g,'')" />&nbsp;&nbsp;
			开始日期：<input id="startDate" name="startDate" size="12"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
	                     结束日期：<input id="endDate" name="endDate" size="12" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
			<s:submit value="查询" cssClass="button"></s:submit>
		</form>
		</td>
		<td align="right">
		  <table>
			<tr>
				<td valign="middle"><a href="edit.do"><img src="${ctx}/images/icons/add.gif"/>充值</a></td>
		  </table>
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
		xlsFileName="充值记录表.xls" 
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
		height="460px"
		minHeight="390"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status">
	<ec:row>
		<ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="180" property="cardGrant.card.cardNo" style="text-align:center" title="卡号"/>
		<ec:column width="75" property="cardGrant.customer.name" title="持卡人" style="text-align:center" ellipsis="true"/>
		<ec:column width="115" property="cardGrant.customer.idCard" title="身份证号" style="text-align:center" ellipsis="true"/>
		<ec:column width="75" property="recharge" title="充值日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="75" property="user.name" title="经手人" style="text-align:center" ellipsis="true"/>
		<ec:column width="90" property="upMoney" style="text-align:right" title="充值金额" format="#,##0.00" cell="number"/>
		<ec:column width="80" property="remark" title="备注" ellipsis="true" />
		<ec:column width="80" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			<a href="view.do?model.id=${item.id}" title="查看">查看</a>|
		<c:choose>
				<c:when test="${item.red == 0 }">
		   			<a href="#" onclick="red(${item.id},${GLOBALROWCOUNT})" title="冲红"><font color="red" >冲红</font></a>	 				    	
				</c:when>
				<c:when test="${item.red == 1 }">
			        <font color="silver" >冲红</font> 	 				    	
				</c:when>		    
				<c:otherwise>
			        <font color="silver" >冲红</font>               
				</c:otherwise>
			</c:choose>	
		</ec:column>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="6" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">
					    充值合计（全部）	
				</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
					<fmt:formatNumber value="${totalUpMoney}" pattern="#,##0.00"/>
				</td>
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
  
function red(id,no) {
	if (confirm("确认要冲红No为【"+ no +"】的充值记录吗？冲红后不能恢复！")) {
	   window.location.href = "${ctx}/card/up/red.do?model.id=" + id ;
	}
}
</script>
</body>
</html>