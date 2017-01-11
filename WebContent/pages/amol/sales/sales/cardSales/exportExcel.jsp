<%@ page language="java" %>
<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>    
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	response.setHeader("Content-disposition","attachment; filename=cardSales.xls"); 
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
<div class="x-panel-header"><font size="3">销售出库单【代币卡】</font></div>
</div>

<div style="width:98%;" align="left">

<table class="cost" width="100%" border="1"
	style="border-collapse:collapse;border:solid 1px #000">
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
			<td align="right">收款方式：</td>
			<td align="left">
				${model.payment.name }
			</td>
			<td align="right">业务员&nbsp;&nbsp;：</td>
			<td align="left">
				${model.employee.name }
			</td>
		</tr>
        <tr>
			<td align="right">应付金额：</td>
			<td align="left">
				${model.samount }
			</td>
			<td align="right">实收金额：</td>
			<td align="left">
				${model.spayamount }
			</td>
		</tr>
		<tr>
			<td align="right">仓&nbsp;&nbsp;&nbsp;&nbsp;库：</td>
			<td align="left" colspan="3">
				${model.storage.name }
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
	   
	<table class="cost" width="100%" border="1"
	style="border-collapse:collapse;border:solid 1px #000">	   
  <tbody id="tbody">
    <tr bgcolor="#E6F4F7" align="center" height="18">
       <td width="100" align="center">商 品</td>
       <td width="70" align="center">规格</td>
       <td width="70" align="center">单位</td>
       <td width="70" align="center">单价</td>
       <td width="70" align="center">出库数量</td>
       <td width="70" align="center">退货数量</td>
       <!--td width="100" align="center">基本单位数量</td-->
       <td width="70" align="center">金额</td>
       <td width="150">备注</td>
    </tr>
   <%
   List<SalesDetail> sd=(List<SalesDetail>)request.getAttribute("sd");
   if(sd!=null){
   for(SalesDetail s:sd){
   %>
   <tr height="18"> 
      <td align="center"><%=s.getProducts().getName()%>  </td>
      <td align="center"><%=s.getProducts().getStardard()%></td> 
      <td align="center"><%=s.getUnits().getName()%></td>
      <td align="right"><%=s.getOutPrice()%></td>
      <td align="right"><%=s.getNcount()%></td>
      <td align="right"><%=s.getHanod() %></td>
      <!--td align="right"><%=s.getCount()%></td-->
      <td align="right"><%=s.getAmount()%></td>
      <td><%=s.getRemark()%>&nbsp;</td>
    </tr>
    <%}} %>
    </tbody>
</table>  

</div>

</body>

</html>
