<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>代币卡管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/autocomplete.jsp"%>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">代币卡管理</div>
<div class="x-toolbar">
<table width="99%">
	<tr>
		<td>
			<s:form id="search" action="index.do" method="post">
			           卡号：
			    <s:textfield name="model.cardNo" id="cardNo" style="width: 200px" value="%{cardNo}" maxlength="22"/>
				
				卡状态：
	            <s:select id="cardState" list="cardStates" name="model.cardState" headerKey="" 
	                 headerValue="全部" cssStyle="width:120px;" />			
				<s:submit value="查询" cssClass="button"></s:submit>
				
			</s:form>
		</td>
		<td align="right">
		  <table>
			<tr>			
				<td valign="middle">
					<!--  <a href="card.do" >循环添加代币卡</a>-->
					<a href="edit.do"><img src="${ctx}/images/icons/add.gif"/>添加代币卡</a>
				</td>
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
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
		<ec:column width="50" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="180" property="cardNo" style="text-align:center" title="卡号"/>
		<ec:column width="80" property="createDate" title="建卡时间" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="80" property="creator.name" title="建卡者" style="text-align:center" />
		<ec:column width="200" property="descn" style="text-align:center" title="描述" ellipsis="true"/>
		<ec:column width="80" property="cardState" mappingItem="cardStates" title="卡状态"  style="text-align:center" />
		<ec:column width="100" property="_0" title="操作" style="text-align:center" sortable="false">    
		    <c:choose>
				    <c:when test="${item.cardState != 0 }">
				    	<font color="silver" >编辑</font> |
			            <font color="silver" >删除</font> 
				    </c:when>		    
				    <c:otherwise>
	                    <a href="${ctx}/card/edit.do?model.id=${item.id}" title="修改卡信息">编辑</a> | 
		    			<a href="#" onclick="remove(${item.id})" title="删除记录">删除</a>
				    </c:otherwise>
			    </c:choose>  	
		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
<script type="text/javascript">
$().ready(function() {
	$.ajax({
		url: '${ctx}/card/getCards.do',
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

function remove(id) {
	if (confirm("确认要删除所选择的卡记录吗？删除后不能恢复！")){
		window.location.href = "${ctx}/card/remove.do?model.id=" + id;
	}
}

function refresh() {
  ECSideUtil.reload('ec');
}
</script>
</body>
</html>