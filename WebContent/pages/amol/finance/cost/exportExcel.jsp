<%@ page language="java" %>
<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>    
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.List,com.systop.amol.finance.model.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:choose>
	<c:when test="${status == 1 }">
		<%
			Cost c = (Cost)request.getAttribute("model");
			response.setHeader("Content-disposition","attachment; filename=in"+c.getReceipts()+".xls"); 
		%>         
	</c:when>		    
	<c:otherwise>
		<%
			Cost c = (Cost)request.getAttribute("model");
			response.setHeader("Content-disposition","attachment; filename=out"+c.getReceipts()+".xls"); 
		%>
	</c:otherwise>
</c:choose>

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
<div align="center" style="width: 100%">
	<table width="100%" align="left" border="1">
		<tr>
			<td align="right" width="150">单 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td class="simple" align="left" colspan="3" style="mso-number-format:\@;">&nbsp; 
				${ model.receipts }
			</td>
			<td align="right" class="simple">单&nbsp;据&nbsp;顺&nbsp;序&nbsp;号：</td>
			<td class="simple" width="350" colspan="2" style="mso-number-format:\@;">&nbsp;
				${ model.serialNumber } 
			</td>
		</tr>

		<tr>
			<td align="right" class="simple" width="150">
				<c:choose>
					<c:when test="${status == 1 }">
						收&nbsp;&nbsp;入&nbsp;&nbsp;日&nbsp;&nbsp;期：         
					</c:when>		    
					<c:otherwise>
						支&nbsp;&nbsp;出&nbsp;&nbsp;日&nbsp;&nbsp;期：            
					</c:otherwise>
  				</c:choose>
			</td>
			<td width="250" class="simple"  colspan="3" align="left">&nbsp; 
				<fmt:formatDate value="${ model.createDate }" pattern="yyyy-MM-dd"/>				
			</td>	
			<td align="right" class="simple">操&nbsp;&nbsp;&nbsp;&nbsp;作&nbsp;&nbsp;&nbsp;&nbsp;员：</td>
			<td class="simple" width="450"  colspan="2" class="simple" align="left">&nbsp; 
				${ model.user.name }
			</td>					
		</tr>

		<tr>
			<td align="right" class="simple" >资&nbsp;&nbsp;金&nbsp;&nbsp;类&nbsp;&nbsp;型：</td>
			<td class="simple" align="left" colspan="3">&nbsp; 
				${ model.fundsSort.name }
			</td>
			<td align="right" class="simple">支&nbsp;&nbsp;&nbsp;&nbsp;票&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td class="simple" align="left"  colspan="2">&nbsp; 
				${ model.checkNo }
			</td>
		</tr>
		
		<tr>
			<td align="right" class="simple">对&nbsp;&nbsp;方&nbsp;&nbsp;单&nbsp;&nbsp;位：</td>
			<td class="simple" width="350" colspan="3" align="left">&nbsp;
				${ model.source }
			</td>
			<td align="right">总&nbsp;&nbsp;&nbsp;&nbsp;金&nbsp;&nbsp;&nbsp;&nbsp;额：</td>
			<td class="simple" align="left" colspan="2">&nbsp;
				${ model.totalMoney } 
			</td>
		</tr>
		
		<tr>
				
			<td align="right" class="simple">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td class="simple" align="left" colspan="6">&nbsp;
				${ model.remark } 
			</td>	
		</tr>
		
	</table>
		<table width="98%" border="1" id="costSort">
		<tbody id="tbody">
			<tr bgcolor="#E6F4F7"  align="center" height="19">
				<td>类型</td>
				<td colspan="3">摘要</td>
				<td colspan="3">金额</td>
			</tr>
			
			  <%
			   List<CostDetail> sd=(List<CostDetail>)request.getAttribute("costDetail");
			   if(sd != null){
			   for(CostDetail s:sd){
			   %>
				
			   <tr> 
			     <td>
			        <%=s.getCostSort().getName()%>
				 </td>
			     <td colspan="3">
			     	<%=s.getNote()%>
			     </td> 
			     <td colspan="3">
			     	<%=s.getMoney()%>
			     </td>
			   </tr>
				    
			   <%}} %>
			   </tbody>
		</table>
</div>
</div>
</body>
</html>
