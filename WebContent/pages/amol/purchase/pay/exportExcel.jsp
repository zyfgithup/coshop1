<%@ page language="java" %>
<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>    
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.purchase.model.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	response.setHeader("Content-disposition","attachment; filename=PurchaseOrder.xls"); 
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:o="urn:schemas-microsoft-com:office:office"
xmlns:x="urn:schemas-microsoft-com:office:excel"
xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta name=ProgId content=Excel.Sheet>
<meta name=Generator content="Microsoft Excel 11">


<script type="text/javascript"
	src="${ctx}/dwr/interface/CostCollection86Action.js"></script>
<style type="text/css">
body{font-size:16px}
.cost td{ 
	font-family:宋体;
	font-size:10pt;
	border:solid 0.5pt #000;
	height:18px
}
.cost th{
	font-family:宋体;
	font-size:11pt;
	font-weight:bold;
	border:solid 0.5pt #000;
	text-align:center
	}
.title{
	font-family:宋体;
	font-size:12pt;
	font-weight:bold;
	background:#C0C0C0;
	text-align:center
}
.date{
	font-family:宋体;
	font-size:12pt;
	font-weight:bold
}
.white{
	background:#FFFFFF;
	font-weight:bold;
	font-size:10pt;	
	text-align:right
	}	
.grey{
	background:#C0C0C0;
	font-weight:bold;
	font-size:11pt;		
	text-align:right
	}
.red{
	background:#FF6600;
	font-weight:bold;
	text-align:right
	}
.yellow{
	background:#FF6600;
	font-weight:bold;
	font-size:10pt;		
	text-align:right
	}
.left{
	text-align:left
	}
.right{
	text-align:right
	}
.v
	{mso-style-parent:style0;
	text-align:center;
	vertical-align:middle;
	layout-flow:vertical;}
.toolbar table{
	border-style:none;
	}
.toolbar td{
	border-style:none}
body,td,th {
	font-size: 16px;
}
</style>

</head>

<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header" align="left"><font size="3">【采购付款单】</font></div>
</div>

<div style="width:98%;" align="left">

<table class="cost" width="100%" border="1"
	style="border-collapse:collapse;border:solid 1px #000">
		 <tr>
			<td  align="right" >
				单据编号：			</td>
			<td class="simple" align="left" width="175" colspan="2" style="vnd.ms-excel.numberformat:@">
			${ model.no }
		  </td>
			<td  align="right" >
			日期：		</td>
			<td class="simple" align="left" width="150" colspan="2" style="vnd.ms-excel.numberformat:yyyy/mm/dd">
			${ model.outDate }
		  </td>
		</tr>
		
		
		<tr>
			<td align="right" >
				供应商：
			</td>
			<td class="simple" align="left" colspan="2">
			${ model.supplier.name }
			</td>
			<td align="right" >
				付款金额：
			</td> 
			<td class="simple" align="left" colspan="2">
			${ model.amount }
			</td>
		</tr> 
		
		<tr>
			<td align="right" >
				操作员：
			</td>
			<td class="simple" align="left" colspan="2">
				${model.user.name}
			</td> 
			<td align="right" >
				备注：
			</td>
			<td class="simple"align="left" colspan="2">&nbsp;
				${ model.remark }
           		
			</td> 
		</tr>
		</table>
	

<table class="cost" width="100%" border="1"
	style="border-collapse:collapse;border:solid 1px #000">
	
  <tbody id="tbody">
  <tr bgcolor="#E6F4F7"  align="center" >
    <td width="100px">入库单号</td>
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
     
       <tr>
       <td><%=od.getPurchase().getSno() %></td>
      <td style="text-align: center"><%=new java.text.SimpleDateFormat("yyyy-MM-dd").format(od.getPurchase().getSdate())%></td>
      <%if(od.getPurchase().getBillType()==2) {%>
      <td style="text-align: right"><%=-od.getPurchase().getSamount() %></td>
      <td style="text-align: right"><%=-(od.getPurchase().getSpayTotal()-Math.abs(od.getAmount())) %></td>
      <td style="text-align: right"><%=-(od.getPurchase().getSamount()-od.getPurchase().getSpayTotal()+Math.abs(od.getAmount()))%>
      </td>
      <td><%=od.getAmount()%> </td>
      <%}else{ %>
       <td style="text-align: right"><%=od.getPurchase().getSamount() %></td>
      <td style="text-align: right"><%=od.getPurchase().getSpayTotal()-od.getAmount() %></td>
      <td style="text-align: right"><%=od.getPurchase().getSamount()-od.getPurchase().getSpayTotal()+od.getAmount()%></td>
      <td><%=od.getAmount()%></td>
    
      <%} %>
      </tr>
     <%} }%>
  
    </tbody>
</table>

</div>

</body>

</html>
