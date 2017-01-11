<%@ page language="java" %>
<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>    
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.ReceiveDetail,com.systop.amol.util.DoubleFormatUtil"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	response.setHeader("Content-disposition","attachment; filename=ReceiveCash.xls"); 
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
<div class="x-panel-header"><font size="3">销售回款单【现金】</font></div>
</div>

<div style="width:98%;" align="left">

<table class="cost" width="100%" border="1"
	style="border-collapse:collapse;border:solid 1px #000">
		 <tr>
			<td  align="right" >
				单&nbsp;&nbsp;&nbsp;&nbsp;号：			</td>
			<td  class="simple" align="left">
			${ model.receiveNumber }
		  </td>
			<td  align="right" >
				日&nbsp;&nbsp;&nbsp;&nbsp;期：
			</td>
			<td  class="simple" align="left">
			<fmt:formatDate value="${ model.createTime}" pattern="yyyy-MM-dd"/>
		  </td>
		</tr>
		
		<tr>
			<td align="right" >
				客&nbsp;&nbsp;&nbsp;&nbsp;户：
			</td>
			<td class="simple" align="left">
			${ model.customer.name }
			</td>
			<td align="right" >
				业&nbsp;务&nbsp;员：
			</td>
			<td class="simple" align="left">
			${model.employee.name }
			</td>
		</tr> 
		<tr>
			<td align="right" >
				收款金额：
			</td> 
			<td class="simple" align="left">
				${model.spayamount }
			</td>
			<td align="right" >
				操&nbsp;作&nbsp;员：
			</td>
			<td class="simple" align="left">
				${model.user.name}
			</td> 
		</tr>
		<tr>
			<td align="right">
				备&nbsp;&nbsp;&nbsp;&nbsp;注：
			</td>
			<td class="simple" align="left" colspan="3">&nbsp;
				${ model.remark }
           		
			</td> 
		</tr>	
	   </table>
	   
	<table class="cost" width="100%" border="1"
	style="border-collapse:collapse;border:solid 1px #000">	   
	     <tbody id="tbody">
    <tr bgcolor="#E6F4F7" align="center" height="18">
        <td width="120px">应收单号</td>
	    <td width="120px">日期</td>
	    <td width="65px">应收金额</td>
	    <td width="70px">已收金额</td>
	    <td width="65px">退款金额</td>
	    <td width="65px">实退金额</td>
	    <td width="65px">未收金额</td>
    </tr>
   <%
   List<ReceiveDetail> sd=(List<ReceiveDetail>)request.getAttribute("sd");
   if(sd!=null){
   for(ReceiveDetail s:sd){
   %>
   <tr> 
          <td>
             <c:if test="<%=s.getSales() != null%>">
      	  	 	<%=s.getSales().getSalesNo()%>
      	  	 </c:if>
      	  	 <c:if test="<%=s.getReceiveInit() != null%>">
      	  	 	期初
      	  	 </c:if>
          </td>
          <td style="text-align: center;">
             <%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(s.getCreateTime()) %>
          </td>
	      <td style="text-align: right">
	         <%=s.getSamount() %>
	      </td>
	      <td style="text-align: right">
	      	 <%=s.getSpayamount() %>
	      </td>
	      <td style="text-align: right">
	      	 <%=s.getRttao() %>
	      </td>
	      <td style="text-align: right">
	      	 <fmt:formatNumber value="<%=DoubleFormatUtil.format(s.getRealReturnMoney()) %>" pattern="#,##0.00"/>
	      </td>
	      <td style="text-align: right">
	         <%=s.getSamount() - s.getRttao() - (s.getSpayamount()  - DoubleFormatUtil.format(s.getRealReturnMoney())) %>
	      </td>
    </tr>
    <%}} %>
    </tbody>
</table>  

</div>

</body>

</html>
