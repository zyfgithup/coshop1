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
<div class="x-panel-header"><font size="3">【采购订单】</font></div>
</div>

<div style="width:98%;" align="left">

<table class="cost" width="100%" border="1"
	style="border-collapse:collapse;border:solid 1px #000">
		<tr>
			<td align="right" width="175">
				编 &nbsp;&nbsp;&nbsp;&nbsp;号
			</td>
			<td class="simple" align="left" width="250" colspan="3" style="vnd.ms-excel.numberformat:@">
                  ${model.sno}
			</td>
			<td align="right" align="right" width="100" >
				日 &nbsp;&nbsp;&nbsp;&nbsp;期
			</td>
			<td class="simple" align="left" width="150" colspan="2" style="vnd.ms-excel.numberformat:yyyy/mm/dd">
			   ${model.sdate }
			</td>
		</tr>
				
		<tr>
			<td align="right" align="right" >
				供&nbsp;应&nbsp; 商
			</td>
			<td class="simple" align="left" colspan="3">
			${ model.supplier.name }
			</td>
			<td align="right" align="right" >
				采&nbsp;购&nbsp; 员
			</td>
			<td class="simple" align="left" colspan="2" >
			${ model.employee.name }
			</td>
		</tr>
		<tr> 
			<td align="right" >
				应 &nbsp;&nbsp;&nbsp;&nbsp;付
			</td>  
			<td class="simple" align="left" colspan="3" style="vnd.ms-excel.numberformat:￥#,##0.00">
                ${model.samount} 
			</td>
			<td align="right" >
				操&nbsp;作&nbsp; 员
			</td>
			<td class="simple" align="left" colspan="2">
				${model.user.name}
			</td> 
		</tr>
		<tr> 
		<td align="right">备 &nbsp;&nbsp;&nbsp;&nbsp;注</td>
			<td colspan="3" class="simple" align="left" colspan="6">
			${model.remark}
			</td>	
		</tr>
	   </table>
	   
	<table class="cost" width="100%" border="1"
	style="border-collapse:collapse;border:solid 1px #000">	   
	     <tbody id="tbody">
    <tr bgcolor="#E6F4F7" align="center" height="18">
       <td width="175">商 品</td>
       <td >规格</td>
       <td >单位</td>
       <td >单价</td>
       <td >数量</td>
        <td >金额</td>
       <td >备注</td>
    </tr>
   <s:if test="purchaseDetail.size==0">
   </s:if>
   <%
   List<PurchaseDetail> sd=(List<PurchaseDetail>)request.getAttribute("purchaseDetail");
   if(sd!=null){
   for(PurchaseDetail s:sd){
   %>
   <tr > 
      <td><%=s.getProducts().getName()%>  </td>
      <td ><%=s.getProducts().getStardard()%></td> 
      <td ><%=s.getUnits().getName()%></td>
      <td><%=s.getPrice()%></td>
      <td><%=s.getNcount()%></td>
      <td><fmt:formatNumber value="<%=s.getAmount()%>" type="number"/></td>
      <td><%=s.getRemark()%>&nbsp;</td>
    </tr>
    <%}} %>
    </tbody>
</table>  

</div>

</body>

</html>
