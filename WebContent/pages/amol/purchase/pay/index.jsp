<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>采购付款管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>

<script type="text/javascript">
function xdcx(){
	var form1=document.getElementById("form1");
	var sno=document.getElementById("sno");
	var sid=document.getElementById("sid");
	form1.action="${ctx}/purchase/paydetail/index.do?model.pay.no="+sno.value+"&model.pay.supplier.name="+sid.value;
	form1.submit();
}
function red(id, no){
	if (confirm("您确认要冲红该【" + no + "】采购付款单吗？")){
		window.location.href="red.do?model.id=" + id;
	}
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">采购付款管理

	</div>
 <s:if test="model.user.beginningInit==1 || model.user.superior.beginningInit==1">
	<div style="float: right;">
	<a href="${ctx}/purchase/pay/edit.do"><img src="${ctx}/images/icons/add.gif" style="width: 12px; height: 12px;" />&nbsp;添加付款单&nbsp;</a>
	</div>
   </s:if>
</div>  
	<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
     <table width="100%">
       <tr>
       <s:form action="index" theme="simple" id="form1">
       <td> 状态</td>
       <td><s:select list='statusMap' name="model.status" cssStyle="width:60px"></s:select></td>
       <td>供应商</td>
       <td><s:textfield name="model.supplier.name" id="sid" cssStyle="width:140px" /></td>
       <td>单号 <s:textfield name="model.no" size="15" id="sno"/></td>
       <td>开始日期 <input id="startDate" name="startDate" size="12"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
                             结束日期 <input id="endDate" name="endDate" size="12" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>   	
           <s:submit value="查询" cssClass="button" cssStyle="width:40px;"></s:submit>
           <s:submit value="详单" cssClass="button" onclick="xdcx()" cssStyle="width:40px;"></s:submit>
        </td>  
        </s:form>   
       </tr>
     </table>
   </div>
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do"
		useAjax="true" doPreload="false"
		xlsFileName="采购付款单.xls"
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="true"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="380px"	
		minHeight="380"
	toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  
		>
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="110" property="no" title="应付款单" style="text-align:center"/>
		<ec:column width="80" property="outDate" title="下单日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="170" property="supplier.name" title="供应商名称"/>
		<ec:column width="100" property="amount" title="金额"  style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="100" property="remark" title="备注" ellipsis="true"/>
		 <ec:column width="80" property="status" title="状态"  style="text-align:center">
		   <s:if test="#attr.item.status == 0">正常 </s:if>
		  <s:if test="#attr.item.status == 1"><font color="blue">已冲红</font></s:if>
		  <s:if test="#attr.item.status == 2"><font color="red">冲红单</font></s:if>
		</ec:column>
		<ec:column width="90" property="_0" title="操作" style="text-align:center" sortable="false" viewsDenied="xls">
			<a href="view.do?model.id=${item.id}" >查看</a> | 
			<c:if test="${item.status == 0 }">
			<!-- <a href="edit.do?model.id=${item.id}" title="修改付款表">编辑</a> | -->
			<a href="#" onclick="red(${item.id}, '${item.no}')"><font color="red">冲红</font></a>  	
			 </c:if>
		   <c:if test="${item.status >0 }">
		   <a href="#"><font color="#999999">冲红</font></a>
		   </c:if>
		</ec:column>
			
	</ec:row>
	
	
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="4" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
				<fmt:formatNumber value="${amountTotal}" pattern="#,##0.00"/></td>
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