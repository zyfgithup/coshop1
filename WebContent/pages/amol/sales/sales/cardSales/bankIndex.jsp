<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>应收查询</title>
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

function query(customerId){
	var ownerIdValue = document.getElementById("id").value;
	window.location.href="${ctx}/card/grant/viewBank.do?customerId=" + customerId + "&ownerId=" + ownerIdValue;
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">应收查询</div>
</div>

<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form action="${ctx}/sales/cardSales/queryBankIndex.do" method="post">
		        <input type="button" id="btn1" class="button" value="选择经销商" onclick="showAgent()"/>
		        <s:textfield name="name" maxlength="18" id="name" class="required" style="width:130px" readOnly="true"/>				
			    <s:hidden name="id" id="id"/>            
		                     身份证号：<s:textfield name="customerIdCard" size="20" maxLength="18"/>        
		       	开始日期：<input id="startDate" name="startDate" size="12"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
		       	结束日期：<input id="endDate" name="endDate" size="12" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>                      
		        <input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button"/>
           </form>
         </td>
       </tr>
     </table>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="${ctx}/sales/cardSales/queryBankIndex.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="应收款单.xls" 
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
		<ec:column width="130" property="_0" title="身份证号" style="text-align:center" ellipsis="true">
			${item.customer.idCard }
		</ec:column>
		<ec:column width="80" property="customer.name" title="客户" style="text-align:center" ellipsis="true"/>
		<ec:column width="80" property="samount" title="应收金额" style="text-align:right" format="###,##0.00" cell="number"></ec:column>
		<ec:column width="80" property="cardamount" title="刷卡金额" style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="80" property="spayamount" title="实收金额" style="text-align:right" format="###,##0.00" cell="number"></ec:column>
		<ec:column width="80" property="rttao" title="退款金额" style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="80" property="w" title="未收金额" style="text-align:right" format="###,##0.00" cell="number">
		    ${item.samount - item.rttao - item.spayamount }
		</ec:column>
		<ec:column width="80" property="cz" title="操作" style="text-align:center" >
		    <a href="javascript:query(${item.customer.id });">详情</a>
		</ec:column>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="3" align="right" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px;">合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;">${samount }</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;">${cardamount }</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;">${spayamount }</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;">${rttao }</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;">${cardamount - spayamount - rttao }</td>
				<td>&nbsp;</td>
			</tr>
		</ec:extendrow>
	</s:if>
</ec:table>
</div>
</div>
</body>
</html>