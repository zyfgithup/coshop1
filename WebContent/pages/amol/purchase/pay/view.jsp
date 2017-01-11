<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="org.apache.naming.java.javaURLContextFactory,java.text.DecimalFormat"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.purchase.model.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>

<title>采购付款单</title> 

</head>
<body>


<div class="x-panel" style="width: 100%">
<div class="x-panel-header">采购付款信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
	<s:hidden name="model.id"  /> 
	<fieldset style="width: 94%; padding:10px 10px 10px 10px;">
	<legend>采购付款单</legend>
	<table width="100%" align="left" >
	    <tr>
			<td align="right" >
				编 &nbsp;&nbsp;&nbsp;号：</td>
			<td  class="simple" align="left">
			${ model.no }
		  </td>
			<td  align="right" >
			 日&nbsp;&nbsp;&nbsp;&nbsp;期：</td>
			<td  class="simple" align="left">
			<fmt:formatDate value="${ model.outDate}" pattern="yyyy-MM-dd"/>
		  </td>
		</tr>

		<tr>
			<td align="right" >
				供&nbsp;应&nbsp;商：
			</td>
			<td class="simple" align="left">
			${ model.supplier.name }
			</td>
			<td align="right" >
				  业&nbsp;务&nbsp;员：
			</td>
			<td class="simple" align="left">
			   ${ model.employee.name }
			</td>
		</tr> 

		<tr>
			<td align="right" >
				付款金额：
			</td> 
			<td class="simple" align="left">
			${ model.amount }
			</td>
			<td align="right" >
				关联金额：
			</td>
			<td class="simple"align="left">
				${ model.linkAmount }
				</td>
		</tr>
		<tr>
			<td align="right" >
				操&nbsp;作&nbsp;员：
			</td>
			<td class="simple" align="left">
				${model.user.name}
			</td> 
			<td align="right" >
				备&nbsp;&nbsp;&nbsp;&nbsp;注：
			</td>
			<td colspan="3" class="simple" align="left">
				${ model.remark }
           		
			</td> 
		</tr>
		</table>
	</fieldset>

	<center>	

<table border="1" align="center" width="94%" style="border-collapse:collapse;border:solid 1px #CCC"> 
	
  <tbody id="tbody">
  <tr bgcolor="#F3F4F3"  align="center" height="23">
    <td width="100px">退货/入库单号</td>
    <td width="100px">入库对应订单号</td>
    <td width="100px">日期</td>
    <td width="100px">应付金额</td>
    <td width="100px">已付金额</td>
    <td width="100px">未付金额</td>
    <td width="100px">本次付款</td>
    </tr>
   <% List<PayDetail>   list=(List<PayDetail>)request.getAttribute("list"); 
     
     if(list!=null && list.size()>0){
    	 
     for(PayDetail od:list){
     %>
      <tr height="18">
      <td align="center"><%=od.getPurchase().getSno() %></td>
      <td align="center">&nbsp;<%=od.getPurchase().getOrderno()==null?"":od.getPurchase().getOrderno() %></td>
      <td align="center"><%=new java.text.SimpleDateFormat("yyyy-MM-dd").format(od.getPurchase().getSdate())%></td>
      <%if(od.getPurchase().getBillType()==2){ %>
       <td align="right"><%=-od.getPurchase().getSamount() %></td>
      <td align="right"><%=-od.getPayAmount()  %></td>
      <td align="right"><%=new DecimalFormat("#0.00").format(-(od.getPurchase().getSamount()-od.getPayAmount())) %> </td>
      <td align="right"><%=od.getAmount()%> </td>
      <%}else{ %>
      <td align="right"><%=od.getPurchase().getSamount() %></td>
      <td align="right"><%=od.getPayAmount()%></td>
      <td align="right"><%=new DecimalFormat("#0.00").format(od.getPurchase().getSamount()-od.getPayAmount()) %> </td>
      <td align="right"><%=od.getAmount()%> </td>
      <%} %>
      </tr>
     <%} }%>
  
    </tbody>
</table>

</center>
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
			<input type="button" class="button" value="导出" onclick="javascript:exportExcel(${model.id})">
				<s:reset value="返回" cssClass="button" onclick="javascript:history. back();"/>
			</td>
		</tr>
	</table>

</div>
</div>
<script type="text/javascript">

	/** ready */
	$(document).ready(function() {
		$("#save").validate();
	});
	function exportExcel(id){
		 window.location = '${ctx}/purchase/pay/exportExcel.do?model.id='+id;
	}
</script>
</body>
</html>