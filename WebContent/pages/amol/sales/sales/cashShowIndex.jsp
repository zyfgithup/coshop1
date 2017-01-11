<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>选择出库单【现金】</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
//出库单
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
	window.showModalDialog("view.do?model.id="+id,null,"dialogWidth=800px;resizable: Yes;");
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">销售出库【现金】</div>
	<s:if test="model.user.beginningInit==1">
	<div style="float: right;">
	</div>
	</s:if>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
     <table width="99%">
        <tr>
          <s:form action="/sales/showIndex.do" method="post">
          <td> 
          <table>
           <tr>
	          <td>收款方式：</td>
	          <td><s:select list='PaymentCashMap' name="model.payment" headerKey="" headerValue="全部" cssStyle="width:99px;"></s:select></td>
	         <td>开始日期：</td>
           <td>&nbsp;<input id="startDate" name="startDate" size="13"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/></td>
           <td>&nbsp;结束日期：</td>
           <td><input id="endDate" name="endDate" size="13" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/></td>
           </tr>
           <tr>
           <td>客户名称：</td>
           <td>&nbsp;<s:textfield name="model.customer.name" id="name" cssStyle="width:98px;"/></td>   
           <td><s:select list='salesOrderMap' name="model.status"></s:select></td>
	       <td colspan="2">&nbsp;<s:textfield name="model.salesNo" id="salesNo" cssStyle="width:153px;"/> </td>  
           </tr>
           </table>
           </td>
           <td align="right">
             <table>  
          	  <tr>
		         <td>
		         <s:submit value="查询" cssClass="button"  cssStyle="width:50px;"></s:submit>   
        	     </td>
         	  </tr>
            </table>
           </td>
        </s:form>
        </tr>
      </table>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="showIndex.do" 
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
		height="336px"
		minHeight="336"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
	<ec:row ondblclick="closeWindow('${item.id}','${item.salesNo}','${item.customer.type}')">
	    <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="85" property="customer.name" title="客户" ellipsis="true"/>
		<ec:column width="105" property="salesNo" title="销售单号"/>
		<ec:column width="65" property="payment.name" title="收款方式" style="text-align:center"/>
		<ec:column width="70" property="createTime" title="日期" cell="date" style="text-align:center"/>
		<ec:column width="70" property="samount" title="应收金额" style="text-align:right" format="###,##0.00" cell="number"></ec:column>
		<ec:column width="70" property="spayamount" title="实收金额" style="text-align:right" format="###,##0.00" cell="number"></ec:column>
		<ec:column width="70" property="rttao" title="应退金额" style="text-align:right" format="###,##0.00" cell="number"/><!-- 退货货款金额 -->
		<ec:column width="70" property="realReturnMoney" title="实退金额" style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="70" property="w" title="未收金额" style="text-align:right" format="###,##0.00" cell="number">
		    	${item.samount  - item.rttao - (item.spayamount  - item.realReturnMoney) }
		</ec:column>
		<ec:column width="55" property="redRed" title="状态" style="text-align:center">
		  <s:if test="#attr.item.redRed == 1">正常 </s:if>
		  <s:if test="#attr.item.redRed == 2">已冲红 </s:if>
		  <s:if test="#attr.item.redRed == 3">冲红单 </s:if>
		</ec:column>
		<ec:column width="60" property="ckzt" title="退货状态" style="text-align:center" mappingItem="returnStatusMapS"/>
		<ec:column width="50" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			<a href="#" onclick="view(${item.id});">详情</a>
		</ec:column>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="5" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px;">金额单位为“元”，合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${samount}" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${spayamount}" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${rttao}" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${realReturnMoney}" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${samount  - rttao - (spayamount  - realReturnMoney) }" pattern="#,##0.00"/></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</ec:extendrow>
	</s:if>
</ec:table>
</div>
</div>
</body>
</html>