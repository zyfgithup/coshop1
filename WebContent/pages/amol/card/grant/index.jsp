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
<div class="x-panel-header">发卡管理</div>
<div class="x-toolbar">
<table width="99%" >
	<tr>
		<td>
		<form id="search" action="index.do" method="post">
		           卡号：
			<s:textfield name="model.card.cardNo" id="cardNo" style="width: 170px" value="%{card.cardNo}" maxlength="22" onkeyup="this.value=this.value.replace(/[^\d+(\d+)?$]/g,'')" />
			身份证号：
			<s:textfield name="model.customer.idCard" maxlength="18" style="width: 140px" />
		          卡状态：
			<s:select id="cardStateMap" list="cardStateMap"  headerKey="" 
                 headerValue="全部" cssStyle="width:80px;" name="model.card.cardState" />
			<s:submit value="查询" cssClass="button"></s:submit>
		</form>
		</td>
		<td align="right">
		  <table>
			<tr>
				<td valign="middle"><a href="edit.do"><img src="${ctx}/images/icons/add.gif"/>发卡</a></td>
				<td><span class="ytb-sep"></span></td>
				<td valign="middle"><a href="${ctx}/card/replace/edit.do"><img src="${ctx}/images/icons/add.gif"/>补卡</a></td>
				<td><span class="ytb-sep"></span></td>	
				<td valign="middle"><a href="${ctx}/card/rest/edit.do"><img src="${ctx}/images/icons/add.gif"/>重设密码</a></td>			
			</tr>
		  </table>
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
		<ec:column width="100" property="customer.owner.name" title="经销商" style="text-align:center" ellipsis="true"/>
		<ec:column width="80" property="customer.name" title="持卡人" style="text-align:center" ellipsis="true"/>
		<ec:column width="120" property="customer.idCard" title="身份证号" style="text-align:center"/>
		<ec:column width="90" property="depositReceipt" title="存款单号" style="text-align:center" ellipsis="true"/>
		<ec:column width="80" property="creator.name" title="发卡人" style="text-align:center" ellipsis="true"/>
		<ec:column width="80" property="createDate" title="开卡日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="80" property="endDate" title="失效日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="80" property="upMoney" title="充值额" style="text-align:right" format="#,##0.00" cell="number" />
		<ec:column width="80" property="spend" title="消费额" style="text-align:right" format="#,##0.00" cell="number" />
		<ec:column width="80" property="balance" title="余额" style="text-align:right" format="#,##0.00" cell="number" />
		<ec:column width="80" property="credit" title="信用额度" style="text-align:right" format="#,##0.00" cell="number" />
		<ec:column width="80" property="card.cardState" title="卡状态" style="text-align:center" mappingItem="cardStateMap"/>
		<ec:column width="280" property="_0" title="操作" style="text-align:center" sortable="false">			
			<c:choose>
				<c:when test="${item.card.cardState == 1 }">
				    <font color="silver" >正常</font> |
			        <a href="#" onclick="updateCard(${item.id},3)" title="挂失">挂失</a> |
				    <a href="#" onclick="updateCard(${item.id},4)" title="冻结">冻结</a> | 
				    <a href="#" onclick="updateCard(${item.id},5)" title="注销">注销</a> | 
				    <a href="view.do?model.id=${item.id}" title="查看">查看</a> |
					<a href="${ctx}/card/grant/updatePass.do?model.id=${item.id}" title="修改代币卡密码">修改密码</a>  
				</c:when>
				<c:when test="${item.card.cardState == 2 }">
				    <font color="silver" >正常</font> |
			        <font color="silver" >挂失</font> |
				    <a href="#" onclick="updateCard(${item.id},4)" title="冻结">冻结</a> | 
				    <a href="#" onclick="updateCard(${item.id},5)" title="注销">注销</a> | 
				    <a href="view.do?model.id=${item.id}" title="查看">查看</a> |
					<font color="silver" >修改密码</font>
				</c:when>
				<c:when test="${item.card.cardState == 3 }">
				    <a href="#" onclick="updateCard(${item.id},1)" title="正常">正常</a> |
			        <font color="silver" >挂失</font> |
					<font color="silver" >冻结</font> |
					<font color="silver" >注销</font> |
					<a href="view.do?model.id=${item.id}" title="查看">查看</a> |
					<font color="silver" >修改密码</font>
					<!--<a href="${ctx}/card/grant/edit.do?model.id=${item.id}" title="修改卡信息">编辑</a> |  -->
					<!--<a href="${ctx}/card/grant/updatePass.do?model.id=${item.id}" title="修改代币卡密码">修改密码</a>   --> 
				</c:when>	
				<c:when test="${item.card.cardState == 4 }">
				    <a href="#" onclick="updateCard(${item.id},1)" title="正常">正常</a> |
			        <font color="silver" >挂失</font> |
					<font color="silver" >冻结</font> |
					<font color="silver" >注销</font> |
					<a href="view.do?model.id=${item.id}" title="查看">查看</a> |
					<font color="silver" >修改密码</font>
					<!--  <a href="${ctx}/card/grant/edit.do?model.id=${item.id}" title="修改卡信息">编辑</a> |-->
					<!--  <a href="${ctx}/card/grant/updatePass.do?model.id=${item.id}" title="修改代币卡密码">修改密码</a> --> 
				</c:when>
				<c:when test="${item.card.cardState == 5 }">
				    <font color="silver" >正常</font> |
			        <font color="silver" >挂失</font> |
					<font color="silver" >冻结</font> |
					<font color="silver" >注销</font> |
					<a href="view.do?model.id=${item.id}" title="查看">查看</a> |
					<!--<a href="${ctx}/card/grant/edit.do?model.id=${item.id}" title="修改卡信息">编辑</a> |  -->
					<font color="silver" >修改密码</font> 
				</c:when>	    
				<c:otherwise>
	                <a href="#" onclick="updateCard(${item.id},1)" title="正常">正常</a> |
					<a href="#" onclick="updateCard(${item.id},3)" title="挂失">挂失</a> |
					<a href="#" onclick="updateCard(${item.id},4)" title="冻结">冻结</a> | 
					<a href="view.do?model.id=${item.id}" title="查看">查看</a> |
					<a href="${ctx}/card/grant/updatePass.do?model.id=${item.id}" title="修改代币卡密码">修改密码</a> 
				</c:otherwise> 
			</c:choose>
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