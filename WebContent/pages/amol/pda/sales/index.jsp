<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>销售出库管理</title>
<script type="text/javascript">
function remove(id){
   if (confirm("确认要冲红该出库单吗？")) {
       window.location.href="${ctx}/pda/sales/redRed.do?model.id=" + id;
   }
}

function removeAo(){
    alert("此出库单已经冲红或已生成退货单 ，不能冲红！");
}

function removeRe(){
	alert("该出库单有回款单为其回款，请先将为其回款的回款单的回款明细中为该出库单回款的记录冲红后才允许冲红该出库单！");
}
</script>
</head>
<body>
<table width="100%">
 <tr>
   <td >欢迎您：<stc:username></stc:username>登录农资PDA系统！</td>
   <td align="right">		
   <a href="${ctx}/security/user/changePasswordPda.do"  style="color: #336699">个人信息</a>   
   &nbsp;|&nbsp; 	
   <a href="${ctx}/j_acegi_logout" target="_self" style="color: #336699">注销&nbsp;  </a>
</td>
 </tr>
 <tr>
   <td height="25" style="border-bottom: 1px dashed #AAC0DD;padding-left: 2px;" colspan="2">
     <a href="${ctx}/pda/sales/editCard.do" target="_self" class="button">出货</a>
     <a href="${ctx}/pda/sales/index.do" class="button">出货冲红</a>
     <a href="#" onclick="alert(22)" class="button">退货</a>
     <a href="#" onclick="alert(22)" class="button">退货冲红</a>
     <a href="${ctx}/pda/sales/index.do" class="button">查询</a>
   </td>
 </tr>
</table>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">销售出库【代币卡】</div>
	<div style="float: right;">
	<a href="${ctx}/pda/sales/editCard.do"><img src="${ctx}/images/icons/add.gif" style="width: 12px; height: 12px;" />&nbsp;添加出库单&nbsp;</a>
	</div>
	
</div>

<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form action="${ctx}/pda/sales/index.do" method="post">
          		
          		状态：<s:select list='statusMap' name="model.redRed"></s:select>
		                     退货状态：<s:select id="ckzt" list="%{returnStatusMap}" name="model.ckzt" headerKey="" 
		                     headerValue="全部" cssStyle="width:75px;" />           
		                     出库单号：
		        <s:textfield name="model.salesNo" size="18" id="salesNo"/> 
		                     客户：<s:textfield name="model.customer.name" size="8" id="name"/>
		        <br/>
		       	开始日期：<input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
		       	结束日期：<input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;  	                      
		        <input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button"/>
           </form>
         </td>
       </tr>
     </table>
</div>   



<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="${ctx}/pda/sales/index.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="出库单.xls" 
		pageSizeList="15,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="400px"
		minHeight="400"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
	<ec:row>
	    <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="60" property="customer.name" title="客户" style="text-align:center"/>
		<ec:column width="105" property="salesNo" title="销售单号" style="text-align:center"/>
		<ec:column width="60" property="payment.name" title="付款方式" style="text-align:center"/>
		<ec:column width="70" property="createTime" title="日期" cell="date" style="text-align:center"/>
		<ec:column width="60" property="samount" title="应收金额" style="text-align:right" format="#####0.00" cell="number"></ec:column>
		<ec:column width="60" property="cardamount" title="刷卡金额" style="text-align:right" format="#####0.00" cell="number"/>
		<ec:column width="60" property="spayamount" title="实收金额" style="text-align:right" format="#####0.00" cell="number"></ec:column>
		<ec:column width="60" property="rttao" title="退款金额" style="text-align:right" format="#####0.00" cell="number"/>
		<ec:column width="60" property="w" title="未收金额" style="text-align:right" format="#####0.00" cell="number">
		    ${item.samount - item.rttao - item.spayamount }
		</ec:column>
		<ec:column width="50" property="redRed" title="状态" style="text-align:center">
		  <s:if test="#attr.item.redRed == 1">正常 </s:if>
		  <s:if test="#attr.item.redRed == 2">已冲红 </s:if>
		  <s:if test="#attr.item.redRed == 3">冲红单 </s:if>
		</ec:column>
		<ec:column width="60" property="ckzt" title="退货状态" style="text-align:center" mappingItem="returnStatusMapS"/>
		<ec:column width="105" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			<a href="view.do?model.id=${item.id}" title="查看销售出库信息">详情</a>|
			<c:if test="${item.redRed == 1}">
				<c:if test="${item.ckzt eq '0'}">
				 	<c:if test="${item.receiveSalesReturn eq '0' }">
						<a href="#" onclick="remove(${item.id})" title="冲红">冲红</a>|
					</c:if>
					<c:if test="${item.receiveSalesReturn eq '1' }">
						<a href="#" onclick="removeRe()" title="冲红">冲红</a>|
					</c:if>
				 	<c:if test="${item.payment eq 'CARDADVANCE'}">
				 		<font color="#999999">退货</font>
				 	</c:if>
				 	<c:if test="${!(item.payment eq 'CARDADVANCE')}">
				 		<a href="${ctx}/salesReturns/stTosREditUI.do?stId=${item.id}" title="退货">退货</a>
				 	</c:if>
			    </c:if>
			    <c:if test="${item.ckzt eq '1'}">
				 	<a href="#" onclick="removeAo(${item.id})"><font color="#999999">冲红</font></a>|
				 	<c:if test="${item.payment eq 'CARDADVANCE'}">
				 		<font color="#999999">退货</font>
				 	</c:if>
				 	<c:if test="${!(item.payment eq 'CARDADVANCE')}">
				 		<a href="${ctx}/salesReturns/stTosREditUI.do?stId=${item.id}" title="退货">退货</a>
				 	</c:if>
				</c:if>
				<c:if test="${item.ckzt eq '2'}">
				 	<a href="#" onclick="removeAo(${item.id})"><font color="#999999">冲红</font></a>|
				 	<font color="#999999">退货</font>
				</c:if>
			</c:if>

			<c:if test="${item.redRed > 1}">  
		       <a href="#" onclick="removeAo()" title="冲红"><font color="#999999">冲红</font></a>|
		       <font color="#999999">退货</font>
		    </c:if> 
		</ec:column>
	</ec:row>
	
</ec:table>
</div>
</div>
</body>
</html>