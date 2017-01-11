<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail"%>
<html>
<head>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<title>销售出库单【代币卡】详情</title>
<script type="text/javascript">
function view(id){
	window.showModalDialog("${ctx }/salesDetail/view.do?model.id="+id,null,"dialogWidth=800px;resizable: Yes;");
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">销售出库单【代币卡】详情</div>
<s:form action="exportExcel.do" id="salesReturnsView" validate="true" method="POST">
<div align="center" style="width: 100%">
	<s:hidden name="model.id" />
	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>详情</legend>
    <table width="100%">
		<tr>
			<td align="right" width="15%" >销售单号：</td>
			<td class="simple" width="35%" >
				${model.salesNo }
			</td>
			<td align="right" width="15%" >客&nbsp;&nbsp;&nbsp;&nbsp;户：</td>
			<td class="simple" width="35%">
				${model.customer.name }
			</td>
		</tr>
		<tr>
			<td align="right">客户电话：</td>
			<td class="simple">
				${model.customer.phone }
			</td>
			<td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
			<td class="simple">
				${status }
		</tr>
		<tr>
			<td align="right">付款方式：</td>
			<td align="left">
				${model.payment.name }
			</td>
			<td align="right">代币卡号：</td>
			<td align="left">
				${model.cardGrant.card.cardNo }
			</td>
		</tr>
        <tr>
			<td align="right">应付金额：</td>
			<td align="left">
				<fmt:formatNumber value="${model.samount }" pattern="#,##0.00"/>
			</td>
			<td align="right">实收金额：</td>
			<td align="left">
				<fmt:formatNumber value="${model.spayamount }" pattern="#,##0.00"/>
			</td>
		</tr>
		<tr>
			<td align="right">仓&nbsp;&nbsp;&nbsp;&nbsp;库：</td>
			<td align="left">
				${model.storage.name }
			</td>
			<td align="right">业务员&nbsp;&nbsp;：</td>
			<td align="left">
				${model.employee.name }
			</td>
		</tr>
        <tr>
           <td align="right">
           	备&nbsp;&nbsp;&nbsp;&nbsp;注：
           </td>
           <td align="left" colspan="3">
           		${model.remark }
		   </td>
        </tr>		
	</table>
	</fieldset>

</div> 
<div class="x-panel-body">
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9">
		
    <tbody id="tbody">
    <tr bgcolor="#F3F4F3" align="center" height="23">
       <td width="100" align="center">商 品</td>
       <td width="70" align="center">规格</td>
       <td width="70" align="center">单位</td>
       <td width="70" align="center">单价</td>
       <td width="70" align="center">出库数量</td>
       <td width="70" align="center">退货数量</td>
       <!-- td width="100" align="center">基本单位数量</td> -->
       <td width="70" align="center">金额</td>
       <td width="150">备注</td>
       <td align="center">操作</td>
    </tr>
    <%
	   List<SalesDetail> sd=(List<SalesDetail>)request.getAttribute("sd");
       if(sd!=null){
  	        for(SalesDetail s:sd){
    %>
   <tr height="23" bgcolor="#FFFFFF">
      <td align="center"><%=s.getProducts().getName()%></td>
      <td align="center"><%=s.getProducts().getStardard()%></td> 
      <td align="center"><%=s.getUnits().getName()%></td>
      <td align="right"><fmt:formatNumber value="<%=s.getOutPrice()%>" pattern="#,##0.00"/></td>
      <td align="right"><%=s.getNcount()%></td>
      <td align="right"><%=s.getHanod() %></td>
      <!-- td align="right"><%=s.getCount()%></td-->
      <td align="right"><fmt:formatNumber value="<%=s.getAmount()%>" pattern="#,##0.00"/></td>
      <td><%=s.getRemark()%>&nbsp;</td>
      <td align="center"><a href="#" onclick="view(<%=s.getId()%>);">详情</a></td>
   </tr>
   <%}} %>
 </tbody>
</table>
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			  <input type="submit" class="button" value="导出">
			  <a href="${ctx }/sales/cardSales/print.do?model.id=${model.id}" class="button">&nbsp;打印&nbsp;</a>
			  <input type="button" value="返回" onclick="history.go(-1);" class="button"/> 
			</td>
		</tr>
	</table>
</div>
</s:form>
</div>
</body>
</html>