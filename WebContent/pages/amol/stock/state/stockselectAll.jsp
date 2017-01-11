<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>

<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>
<title>库存状态查询</title>
<script type="text/javascript">
//提取经销商
function showAgent() {
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog("${ctx}/pages/amol/user/agent/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
	    if(cus!=null){
	    	var name = document.getElementById("name") ;
	    	name.value=cus.name;
			var id = document.getElementById("id") ;
			id.value=cus.id;
		}
 	}else{
		window.open("${ctx}/pages/amol/user/agent/selector.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	    this.returnAction=function(cus){
	    	if(cus!=null){
	    		var name = document.getElementById("name") ;
	    		name.value=cus.name;
	    		var id = document.getElementById("id") ;
	    		id.value=cus.id;
	    	}
		};
 	}
}
</script>
</head>
<body>
<div class="x-panel">
  <div class="x-panel-header">库存状态查询</div>
  <div><%@ include file="/common/messages.jsp"%></div>
  <div class="x-toolbar">
	<table width="100%">
		<tr>
			<s:form action="stockIndex" theme="simple">
				<td >
					<input type="button" id="btn1" class="button" value="选择经销商" onclick="showAgent()"/>
					经销商名称：<s:textfield name="conditionName" maxlength="18" id="name" class="required" style="width:150px" readOnly="true"/>				
			    	<s:hidden name="conditionId" id="id"/>
			    	商品名称：<s:textfield name="conditionProductName" size="15" />
			    	商品编码：<s:textfield name="conditionProductCode" size="15" />
					<input type="submit" value="查询" class="button">					
				</td>
			</s:form>
		</tr>
	</table>
	</div>
    <div class="x-panel-body">
    <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="stockIndex.do"
		useAjax="true" doPreload="false"
		xlsFileName="库存查询.xls"
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="false"	
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
		<ec:column width="40" title="序号" property="_No" value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="140" property="user.name" title="经销商" ellipsis="true"/>
		<ec:column width="80" property="product.code" title="商品编码" ellipsis="true"/>
		<ec:column width="100" property="product.name" title="商品名称" ellipsis="true"/>
		<ec:column width="80" property="product.stardard" title="商品规格" ellipsis="true"/>	
		<ec:column width="100" property="product.units.name" title="基本单位" style="text-align:center" />	
		<ec:column width="100" cell="number" property="count" title="商品数量"/>
		<ec:column width="100" property="unitPack" title="包装单位" ellipsis="true"/>
		<ec:column width="80" property="_0" title="操作" style="text-align:center" viewsAllowed="html">
			<a href="${ctx}/stock/stockIndexView.do?model.user.id=${item.user.id}&model.products.id=${item.product.id}" title="查看">详情</a> 
		</ec:column>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="6" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: left">${countTotal}</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</ec:extendrow>
	</s:if>
	
	</ec:table>
	</div>
	</div>
</div>
</body>
</html>