<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="org.apache.naming.java.javaURLContextFactory,java.text.DecimalFormat"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.purchase.model.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<title>采购付款单</title> 
<script type="text/javascript" src="${ctx }/pages/amol/purchase/pay/editjs.js"></script>
</head>
<body>


<div class="x-panel" style="width: 100%">
<div class="x-panel-header">采购付款信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save" id="save" validate="true" method="POST">
	<s:hidden name="model.id"  /> 
	<fieldset style="width: 94%; padding:10px 10px 10px 10px;">
	<legend>采购付款单</legend>
	<table width="100%" align="left" >
	    <tr>
			<td  align="right" >
				编 &nbsp;&nbsp;&nbsp;号：</td>
			<td  class="simple" align="left">
					<s:textfield  name="model.no" maxLength="20" size="25" cssClass="required" 
					readonly="true" cssStyle="color:#808080;" />
           		<font color="red">&nbsp;*</font>
		  </td>
			<td  align="right" >
			          日&nbsp;&nbsp;&nbsp;&nbsp;期：</td>
			<td  class="simple" align="left">
			<s:textfield  name="model.outDate" maxLength="20" size="25" cssClass="required" id="sdate"
				onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"/>
           		<font color="red">&nbsp;*</font>
		  </td>
		</tr>
		
		
		<tr>
			<td align="right" >
				供&nbsp;应&nbsp;商：
			</td>
			<td class="simple" align="left">
			<s:hidden name="model.supplier.id" id="sid" />
				<s:textfield  name="model.supplier.name" id="supname" maxLength="20" size="25" 
				readonly="true" cssStyle="width:155px;color:#808080;"/>
				<input name="button2"  type="button"   onclick="supplier()" value="选择" class="button"/>
				<font color="red">&nbsp;*</font>    
			</td>
			<td align="right" >
				业&nbsp;务&nbsp;员：
			</td>
			<td class="simple" align="left">
				<s:hidden name="model.employee.id" id="empid" />
				<s:textfield  name="model.employee.name" id="empname" maxLength="20" size="25"
				 readonly="true" cssStyle="color:#808080;" />
				<input name="button2"  type="button"   onclick="emp()" value="选择" class="button"/>
			</td>
		</tr> 
		
		<tr>
			<td align="right" >
				付款金额：
			</td> 
			<td class="simple" align="left">
				<s:textfield  name="model.amount" id="amount" onblur="fkyz(this)" maxLength="20" size="25" cssClass="required"  
				cssStyle="text-align: right"/>
           		<input name="button2"  type="button"   onclick="link()" value="自动付款" class="button"/>
           		<font color="red">&nbsp;*</font>
			</td>
			<td align="right" >
				关联金额：
			</td>
			<td class="simple"align="left">
				<s:textfield  name="model.linkAmount" id="linkAmount" maxLength="20" size="25"   cssStyle="color:#808080;text-align: right" readonly="true"/>
           		</td> 
		</tr>
		<tr>
			
			<td align="right" >
				操&nbsp;作&nbsp;员：
			</td>
			<td class="simple" align="left">
				<s:textfield  name="model.user.name" disabled="true" maxLength="20" cssStyle="width:155px;" />
			</td> 
			<td align="right" >
				未付合计金额：
			</td>
			<td class="simple"align="left" id="nopayamout" >
				
           		</td> 
		</tr>
		<tr> 
		<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td colspan="3" class="simple" align="left"><s:textfield
				name="model.remark" cssStyle="width:580px;"/></td>
			
		</tr>
		</table>
	</fieldset>

	<center>	
	<table align="center" width="95%"> 
	<tr><td>	<div align="left">	<input name="button" type="button" class="button" value="此供应商的应付信息" onclick="addRow()"></div></td></tr>
	</table>
<table border="1" align="center" width="95%" style="border-collapse:collapse;border:solid 1px #CCC">
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
      <tr height="23" bgcolor="#FFFFFF">
      <td><input type="hidden" name="stockid" value="<%=od.getPurchase().getId() %>"/>
      <input type="hidden" name="payinitid" value="<%=od.getPurchase().getOrderid() %>"/>
      <%=od.getPurchase().getSno() %></td>
      <td>&nbsp;<%=od.getPurchase().getOrderno()==null?"":od.getPurchase().getOrderno() %></td>
      <td style="text-align: center"><%=new java.text.SimpleDateFormat("yyyy-MM-dd").format(od.getPurchase().getSdate())%></td>
      <%if(od.getPurchase().getBillType()==2) {%>
      <td style="text-align: right"><%=-od.getPurchase().getSamount() %></td>
      <td style="text-align: right"><%=-(od.getPurchase().getSpayTotal()-Math.abs(od.getAmount()))-0==0?"0":-(od.getPurchase().getSpayTotal()-Math.abs(od.getAmount())) %>
      <input type="hidden" name="payAmount" value="<%=-(od.getPurchase().getSpayTotal()-Math.abs(od.getAmount())) %>"/>
      </td>
      <td style="text-align: right"><%=new DecimalFormat("#0.00").format(-(od.getPurchase().getSamount()-od.getPurchase().getSpayTotal()+Math.abs(od.getAmount())))%>
      <input type="hidden" name="nopay" style="text-align: right" value="<%=new DecimalFormat("#0.00").format(-(od.getPurchase().getSamount()-od.getPurchase().getSpayTotal()+Math.abs(od.getAmount()))) %>"/> 
      </td>
      <td><input type="text" style="text-align: right" name="amount1" id="amount1" value="<%=od.getAmount()-0==0?"0":od.getAmount()%>"  onblur ="yzszss(this,<%=new DecimalFormat("#0.00").format(-(od.getPurchase().getSamount()-od.getPurchase().getSpayTotal()+Math.abs(od.getAmount()))) %>)" /> </td>
      <%}else{ %>
       <td style="text-align: right"><%=od.getPurchase().getSamount() %></td>
      <td style="text-align: right"><%=od.getPurchase().getSpayTotal()-od.getAmount()-0==0?"0":od.getPurchase().getSpayTotal()-od.getAmount() %>
<input type="hidden" name="payAmount" value="<%=od.getPurchase().getSpayTotal()-od.getAmount() %>"/>
      </td>
      <td style="text-align: right"><%=new DecimalFormat("#0.00").format(od.getPurchase().getSamount()-od.getPurchase().getSpayTotal()+od.getAmount())%>
      <input type="hidden" style="text-align: right" name="nopay" value="<%=new DecimalFormat("#0.00").format(od.getPurchase().getSamount()-od.getPurchase().getSpayTotal()+od.getAmount()) %>"/> 
      </td>
      <td><input type="text" name="amount1" style="text-align: right" id="amount1" value="<%=od.getAmount()-0==0?"0":od.getAmount()%>" onblur="yzszss(this,<%=new DecimalFormat("#0.00").format(od.getPurchase().getSamount()-od.getPurchase().getSpayTotal()+od.getAmount()) %>)" /></td>
      <%} %>
      </tr>
     <%} }%>
  
    </tbody>
</table> 
	</center>	
	
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
			    <s:submit value="保存" cssClass="button" onclick="return saveyz();"/>&nbsp;&nbsp;
				<s:reset value="返回" cssClass="button" onclick="javascript:history. back();"/>
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
<script type="text/javascript">
	/** ready */
	$(document).ready(function() {
		$("#save").validate();
	});
	function noPayCount(){
	var amount1 = document.getElementsByName("nopay") ;  
	var tmpamount=0;
    for(var i=0;i<amount1.length;i++){
   	tmpamount+=Number(amount1[i].value);
   	 }
    var npa=document.getElementById("nopayamout");
     npa.innerHTML=Number(tmpamount);
     var linkAmount=document.getElementById("linkAmount");
     linkAmount.value=0;
	}
	noPayCount();
</script>



</body>
</html>