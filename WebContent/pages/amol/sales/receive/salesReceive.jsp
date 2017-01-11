<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.Sales,com.systop.amol.sales.utils.Payment,com.systop.amol.sales.model.ReceiveInit,java.text.DecimalFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<title>回款单信息</title>
<script type="text/javascript" >

/**
 * 得到员工
 */
function emp(){
	 var cus=window.showModalDialog("${ctx}/pages/amol/base/employee/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
      if(cus!=null){
	   	   var tab = document.getElementById("empname") ;
	      tab.value=cus.name;
	      var tab1 = document.getElementById("empid") ;
	      tab1.value=cus.id;  
		   }
}

/**
 * 选择客户
 */
 function selectkh(){
 	var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/base/customer/selector.jsp",null,"dialogWidth=55","dialogHeight=300px");
 	if(cus!=null){
    	 window.location.href="outKToReSiUI.do?customerId=" + cus.id;
 	}
 }

/**
 * 付款金额数字验证
 */
function fkyz(fk){
	if(isNaN(fk.value)){
		alert("付款金额请输入数字！");
		fk.value="0";
    }
}

/**
 * 保存前的验证
 */
function saveyz(){
    var npa=document.getElementById("linkAmount");
    if(npa.value == "" || npa.value-0 == 0){
    	alert("关联金额不能为空，请选择入库单！");
    	return false;
    }
    var linkamount=document.getElementById("linkAmount");
    var amount=document.getElementById("cashConsumption");
    if(linkamount.value-amount.value!=0){
    	alert("收款金额必须等于关联金额！");
    	return false;
    }
    
    if(confirm("您确定要生成回款单吗？")){
    }else{
    	return false;
    }
}

//验证现金为数字
function yzszss(price,yfje){
     if(price.value=="-"){
    	 return;
     }
	if(isNaN(price.value)){
		alert("本次付款请输入数字！");
		 price.value="0";
		 var tab = document.getElementsByName("amount1") ;  	
	     var amount=0;
	     for(var i=0;i<tab.length;i++){
	    	amount+=Number(tab[i].value);
	     }
		 var cardMoney = document.getElementsByName("cardMoney") ;  	
	     for(var i=0;i<cardMoney.length;i++){
	    	amount+=Number(cardMoney[i].value);
	     }
	     var samount=document.getElementById("linkAmount");
	     samount.value=Number(amount);
		 return;
	}


	
	if(Number(price.value)-0!=0 && Number(price.value)-Number(yfje)>0){
		alert("本次付款不能超过未付金额!");
		price.value="0";    
		
		//return;
	}
	 var tab = document.getElementsByName("amount1") ;  	
     var amount=0;
     for(var i=0;i<tab.length;i++){
    	amount+=Number(tab[i].value);
    	 }
     var samount=document.getElementById("linkAmount");
      samount.value=Number(amount);
}
/**
 * 自动关联没有付款的入库单。
 */
function link(){
	
	//关联金额
	var tmpamount=0;
	
	//未收金额
	var nopay = document.getElementsByName("nopay");
	//每笔单子现金收款
	var amount1 = document.getElementsByName("amount1");
	//现金消费额
	var cashConsumption=document.getElementById("cashConsumption").value;

	if(cashConsumption==""){
		cashConsumption = 0;
	}
	for(var i=0;i<nopay.length;i++){
		amount1[i].value="0";
	} 
    for(var i=0;i<nopay.length;i++){
    	if(cashConsumption>0 && cashConsumption-nopay[i].value > 0){
    		amount1[i].value = nopay[i].value;
    		cashConsumption = cashConsumption-nopay[i].value;
    		cashConsumption = Math.round(cashConsumption*100)/100;
    	 }else{
    		 amount1[i].value=Math.round(cashConsumption*100)/100;
    		 break;
    	 }    	
    }
	for(var i=0;i<amount1.length;i++){
    	tmpamount+=Number(amount1[i].value);
    }
	var samount=document.getElementById("linkAmount");
    samount.value=Number(tmpamount);
}
</script>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">回款单信息【现金】</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save" id="save" validate="true" method="POST">
	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>销售回款</legend>
	<table width="100%" align="left" >
	    <tr>
			<td  align="right" >
				单&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td  class="simple" align="left">
				<input type="hidden" name="model.payment" value="<%= Payment.CASH %>"/>
				<s:textfield  name="model.receiveNumber" maxLength="20" size="25" cssClass="required" 
					readonly="true" cssStyle="color:#808080;" />
           		<!-- font color="red">*</font-->
		  </td>
			<td  align="right" >
			           日&nbsp;&nbsp;&nbsp;&nbsp;期：
			</td>
			<td  class="simple" align="left">
			<s:textfield  name="model.createTime" maxLength="20" size="25" cssClass="required" 
				onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"/>
           		<font color="red">*</font>
		  </td>
		</tr>
		
		
		<tr>
			<td align="right" >
				客&nbsp;&nbsp;&nbsp;&nbsp;户：
			</td>
			<td class="simple" align="left">
				<s:textfield  name="model.customer.name" id="sname" cssClass="required" 
				maxLength="20" size="25"  readonly="true" cssStyle="color:#808080;" />
           		<font color="red">*</font>
           		<input type="button" class="button" value="选择" onclick="selectkh();"/>
           		<s:hidden name="model.customer.id" id="sid" />
           		<s:hidden name="model.customer.type" id="customerTypeId" />
           		<s:hidden name="model.customer.idCard" id="idCard" />
			</td>
			<td align="right" >
				业&nbsp;务&nbsp;员：
			</td>
			<td class="simple" align="left">
				<s:textfield  name="model.employee.name" id="empname" maxLength="20" size="25"
				 readonly="true" cssStyle="color:#808080;" />
				<input name="button2"  type="button"   onclick="emp()" value="选择" class="button"/>
           	    <s:hidden name="model.employee.id" id="empid" />
			</td>
		</tr> 
		<tr>
			<td id="xxxfSTdId" align="right" >
				收款金额：
			</td>
			<td class="simple" align="left">
				<s:textfield id="cashConsumption" name="model.spayamount" onkeyup="fkyz(this)" 
				   onafterpaste="this.value=this.value.replace(/\D/g,'')" cssStyle="color:red;text-align: right" maxLength="20" size="25" cssClass="required"/>
				元
				<font color="red">*</font>
				<input name="button2" type="button" onclick="link()" value="自动收款" class="button"/>
			</td>
			<td align="right" >
				关联金额：
			</td>
			<td class="simple"align="left">
				<s:textfield id="linkAmount" maxLength="20" size="25" cssStyle="color:#808080;text-align: right" readonly="true"/>
           		元
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
				未收合计金额：
			</td>
			<td class="simple" align="left" id="nopayamout" >
				
           	</td>
		</tr>
		<tr> 
		<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td colspan="3" class="simple" align="left">
			<s:textfield name="model.remark" cssStyle="width:586px;" /></td>
			
		</tr>
		</table>
	</fieldset>

	<center>	
	<table align="center" width="95%"> 
	<tr><td><div align="left"></div></td></tr>
	</table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9">
	
  <tbody id="tbody">
  <tr bgcolor="#F3F4F3"  align="center" height="23">
    <td width="130px">销售单号</td>
    <td width="110px">日期</td>
    <td width="80px">应收金额</td>
    <td width="80px">已收金额</td>
    <td width="80px">退款金额</td>
    <td width="80px">实退金额</td>
    <td width="80px">未收金额</td>
    <td width="80px" style="text-align: center;">本次收款</td>
  </tr>
  <!-- -----------------------------------期初应收start-------------------------------------- -->
  <% 
     List<ReceiveInit> receiveInitList = (List<ReceiveInit>)request.getAttribute("receiveInitList"); 
     if(receiveInitList != null && receiveInitList.size()>0){
     for(ReceiveInit receiveInit : receiveInitList){
  %>
      <tr bgcolor="#FFFFFF">
      	  <td style="text-align: center;">
      	  	   期初
             <input type="hidden" name="ckdId" value="<%=receiveInit.getId()%>"/>
             <input type="hidden" name="salesNo" value=""/>
          </td>
          <td style="text-align: center;">
             <%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(receiveInit.getCreateTime()) %>
          </td>
	      <td style="text-align: right">
	         <%=new DecimalFormat("#0.00").format(receiveInit.getAmount()) %>
	      </td>
	      <td style="text-align: right">
	      	 <%=new DecimalFormat("#0.00").format(receiveInit.getAmountReceived()) %>
	      </td>
	      <td style="text-align: right">
	      	 0.00
	      </td>
	      <td style="text-align: right">
	      	 0.00
	      </td>
	      <td style="text-align: right">
	         <%=new DecimalFormat("#0.00").format(receiveInit.getAmount() - receiveInit.getAmountReceived())%>
	         <input type="hidden" name="nopay" style="text-align: right" value="<%=receiveInit.getAmount() - receiveInit.getAmountReceived() %>"/> 
	      </td>
	      <td style="text-align: center;">
	      	<input type="text" style="text-align: right" name="amount1" id="amount1" size="5" onkeyup ="yzszss(this,<%=receiveInit.getAmount() - receiveInit.getAmountReceived() %>)"  maxlength="9"/>
	      </td>
      </tr>
  <%}}%>
  <!-- -----------------------------------期初应收end-------------------------------------- -->
  <!-- -----------------------------------应收start-------------------------------------- -->
  <% 
     List<Sales> sd = (List<Sales>)request.getAttribute("sd"); 
     if(sd!=null && sd.size()>0){
     for(Sales s : sd){
  %>
      <tr bgcolor="#FFFFFF">
      	  <td style="text-align: center;">
             <%=s.getSalesNo()%>
             <input type="hidden" name="ckdId" value="<%=s.getId()%>"/>
             <input type="hidden" name="salesNo" value="<%=s.getSalesNo()%>"/>
          </td>
          <td style="text-align: center;">
             <%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(s.getCreateTime()) %>
          </td>
	      <td style="text-align: right">
	         <%=new DecimalFormat("#0.00").format(s.getSamount()) %>
	      </td>
	      <td style="text-align: right">
	      	 <%=new DecimalFormat("#0.00").format(s.getSpayamount()) %>
	      </td>
	      <td style="text-align: right">
	      	 <%=new DecimalFormat("#0.00").format(s.getRttao()) %>
	      </td>
	      <td style="text-align: right">
	      	 <%=new DecimalFormat("#0.00").format(s.getRealReturnMoney()) %>
	      </td>
	      <td style="text-align: right">
	         <%=new DecimalFormat("#0.00").format((s.getSamount() - s.getRttao() - (s.getSpayamount() - s.getRealReturnMoney()))) %>
	         <input type="hidden" name="nopay" style="text-align: right" value="<%=(s.getSamount() - s.getRttao() - (s.getSpayamount() - s.getRealReturnMoney())) %>"/> 
	      </td>
	      <td style="text-align: center;">
	      	<input type="text" style="text-align: right" name="amount1" id="amount1" size="5" onkeyup ="yzszss(this,<%=(s.getSamount() - s.getRttao() - (s.getSpayamount() - s.getRealReturnMoney())) %>)"  maxlength="9"/>
	      </td>
      </tr>
  <%}}%>
  <!-- -----------------------------------应收end-------------------------------------- -->
    </tbody>
</table>
	</center>	
	
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
			<s:submit value="保存" cssClass="button" onclick="return saveyz();" />&nbsp;&nbsp; 
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
     npa.innerHTML=Number(tmpamount)+"&nbsp;元";
	}
	noPayCount();
	
</script>



</body>
</html>