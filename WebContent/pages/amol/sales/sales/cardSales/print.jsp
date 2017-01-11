<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.systop.amol.sales.model.Sales,java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail"%>
<html>
<head>
<style type="text/css" media=print>
.noprint{display : none }
</style>

<script language="JavaScript">

function doPrint(){ 
	window.print(); 
}

</script>
</head>
<body onload="doPrint();">

<div id="div1" class="noprint" style="padding-top: 5px;">
   <input id="colseId" type="button" name="Close" value="返  回"
    onclick="history.go(-1);" class="button">
   <input id="dyId" type="button" name="Print" value="打印本页"
    onclick="doPrint();" class="button">
</div>
<p style="font-family:Arial, Helvetica, sans-serif">
出库单号：${model.salesNo }<br/>
　消费者：${model.customer.name }<br/>
　时　间：<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(((Sales)request.getAttribute("model")).getCreateTime())%><br/>
收款方式：${model.payment.name }<br/>
消费卡号：<br/>
<font size="2">${model.cardGrant.card.cardNo }</font><br/>
刷卡金额：${model.cardamount }<br/>
余额：${model.cardGrant.balance }<br/>
____________________________
<br/>
   <%
	   List<SalesDetail> sd=(List<SalesDetail>)request.getAttribute("sd");
	   if(sd!=null){
	   for(SalesDetail s:sd){
   %>
  <br/>
　编　码：<%=s.getProducts().getCode()%><br/>
商品名称：<%=s.getProducts().getName()%><br/>
　数　量：<%=s.getNcount()%><br/>
　单　位：<%=s.getUnits().getName()%><br/>
　单　价：<%=s.getOutPrice()%><br/>
　金　额：<%=s.getAmount()%><br/>
<br/>
   ------------------------------------------
   <%}} %>
<br/><br/>
客户签字：
</p>
<script type="text/javascript">
//禁止页面刷新的js
document.onkeydown = function()
{
         if(event.keyCode==116) {
         event.keyCode=0;
         event.returnValue = false;
         }
};
document.oncontextmenu = function() {event.returnValue = false;};

</script>
</body>
</html>