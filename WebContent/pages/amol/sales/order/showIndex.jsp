<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>选择订单</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
//订单
function aa(){
this.id;
this.salesNo;
this.customerType;
}
function closeWindow(id,salesNo,customerType){
var a=new aa();
a.id=id;
a.salesNo=salesNo;
a.customerType=customerType;

window.returnValue = a;
window.close();
}

function view(id){
	window.showModalDialog("view.do?model.id="+id,null,"dialogWidth=700px;resizable: Yes;");
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">选择订单</div>
<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form action="${ctx}/salesOrder/showIndex.do" method="post">
	         <s:hidden name="model.redRed" value="1" />
	                       订单：<s:textfield name="model.salesNo" size="17" id="salesNo"/> 
	                      客户：<s:textfield name="model.customer.name" size="5" id="name"/>
        	开始日期：<input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
        	结束日期：<input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>  	
	        <input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button"/>
           </form>
         </td>
       </tr>
     </table>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="${ctx}/salesOrder/showIndex.do"
		useAjax="false"
		doPreload="false" 
		pageSizeList="15,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="370px"
		minHeight="370"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row ondblclick="closeWindow('${item.id}','${item.salesNo}','${item.customer.type}')">
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="80" property="customer.name" title="客户" style="text-align:center"></ec:column>
		<ec:column width="115" property="salesNo" title="订单号" style="text-align:center"/>
		<ec:column width="80" property="createTime" title="下单日期" style="text-align:center" cell="date"/>
		<ec:column width="90" property="samount" title="应收金额" style="text-align:right" format="#####0.00" cell="number"></ec:column>
        <ec:column width="80" property="ckzt" title="出库状态" style="text-align:center" mappingItem="salesStatusMapS"/>		
        <ec:column width="80" property="redRed" title="状态" style="text-align:center">
          <s:if test="#attr.item.redRed == 1">正常 </s:if>
		  <s:if test="#attr.item.redRed == 2">已冲红 </s:if>
		  <s:if test="#attr.item.redRed == 3">冲红单 </s:if>
		</ec:column>
        <ec:column width="150" property="remark" title="备注" style="text-align:center" ellipsis="true"/>		
		<ec:column width="100" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			<a href="#" onclick="view(${item.id});">详情</a>                   
		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>