<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>出库单回款明细</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function remove(id){
	if(confirm("确认要冲红该记录吗？")){
		window.location.href="${ctx}/receiveDetail/redRed.do?model.id=" + id;
	}else{
	}
}

function removeAo(){
    alert("此记录已经被冲红 ，不能再冲红！");
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">出库单回款明细【代币卡】</div>
</div>
<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form action="${ctx }/receiveDetail/cardIndex.do" method="post">
          		状态：<s:select list='statusMap' name="model.redRed"></s:select>
          		出库单号：     
		        <s:textfield name="model.sales.salesNo" size="15" id="salesNo"/> 
		                     客户：<s:textfield name="model.customer.name" size="8" id="name"/>
		       	开始日期：<input id="startDate" name="startDate" size="12"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
		       	结束日期：<input id="endDate" name="endDate" size="12" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;  	                      
		        <input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button"/>
           </form>
         </td>
       </tr>
     </table>
   </div>
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="${ctx }/receiveDetail/cardIndex.do"
		useAjax="true" doPreload="false"
		xlsFileName="出库单回款明细（现金）.xls"
		maxRowsExported="10000000" 
		pageSizeList="20,50,100" 
		editable="false" 
		sortable="true"	
		rowsDisplayed="20"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="400px"	
		minHeight="400"
	toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  
		>
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="105" property="receive.receiveNumber" title="回款单号" style="text-align:center"/>
		<ec:column width="105" property="sales.salesNo" title="出库单号" style="text-align:center"/>
		<ec:column width="70" property="customer.name" title="客户名称" />
		<ec:column width="70" property="createTime" title="日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="60" property="samount" title="应收金额" style="text-align:right" format="#####0.00" cell="number"/>
		<ec:column width="60" property="spayamount" title="已收金额" style="text-align:right" format="#####0.00" cell="number"/>
		<ec:column width="60" property="rttao" title="退款金额" style="text-align:right" format="#####0.00" cell="number"/>
		<ec:column width="60" property="null" title="未收金额" style="text-align:right" format="#####0.00" cell="number">
			${item.samount - item.rttao - item.spayamount }
		</ec:column>
		<ec:column width="60" property="theCollection" title="本次收款" style="text-align:right" format="#####0.00" cell="number"/>
		<ec:column width="65" property="redRed" title="状态" style="text-align:center">
		         <s:if test="#attr.item.redRed == 1">正常 </s:if>
		         <s:if test="#attr.item.redRed == 2">已冲红 </s:if>
		         <s:if test="#attr.item.redRed == 3">冲红单 </s:if>
		</ec:column>
		<ec:column width="60" property="_0" title="操作" style="text-align:center" sortable="false" viewsDenied="xls">
			<c:if test="${item.redRed == 1 }">
			    <a href="#" onclick="remove(${item.id})" title="冲红">冲红</a>  	
			</c:if>
		    <c:if test="${item.redRed > 1}">  
		       <a href="#" onclick="removeAo()" title="冲红"><font color="#999999">冲红</font></a>
		    </c:if> 
		</ec:column>
			
	</ec:row>
	
	
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="5" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">${amountTotal}</td>
				<td ></td>
				<td ></td>
				<td ></td>
				<td ></td>
				<td ></td>
				<td ></td>
				<td ></td>
			</tr>
		</ec:extendrow>
	</s:if>
</ec:table>
</div>
</div>
</body>
</html>