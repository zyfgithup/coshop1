<%@page import="com.systop.amol.stock.StockConstants"%>
<%@ page language="java" %>
<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>    
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List"%>
<%@page import="com.systop.amol.stock.model.StockTrac"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	StockTrac st = (StockTrac)request.getAttribute("model");
	response.setHeader("Content-disposition","attachment; filename="+st.getCheckNo()+".xls"); 
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
<div class="x-panel-header" align="left"><font size="3">【库存调拨单】</font></div>
</div>
<div style="width:98%;" align="left">
<table class="cost" width="100%" border="1"
	style="border-collapse:collapse;border:solid 1px #000">
		<tr>
			<td width="66" align="right">编&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td class="simple" align="left" width="175" colspan="3" style="vnd.ms-excel.numberformat:@">
			${model.checkNo }</td>
			<td width="96" align="right">日期：</td>
			<td class="simple" align="left" width="150" colspan="2" style="vnd.ms-excel.numberformat:yyyy/mm/dd">
			${model.createTime }</td>
		</tr>
		<tr>
			<td align="right">出货仓库：</td>
			<td align="left" colspan="3">${model.outStorage.name}</td>
		</tr>
		<tr>
			<td align="right">入货仓库：</td>
			<td class="simple" colspan="3">${model.inStorage.name}</td>
		</tr>
		<tr>
			<td align="right">操&nbsp;作&nbsp;员：</td>
			<td colspan="3" class="simple" align="left">${model.user.name}</td>
		</tr>
		<tr> 
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td colspan="6" class="simple" align="left">
				${model.remark}
			</td>
		</tr>
	</table>
	<table><tr></tr></table>
	<table class="cost" width="100%" border="1"
	style="border-collapse:collapse;border:solid 1px #000">
		<tbody id="tbody">
			<tr bgcolor="#E6F4F7" align="center">
				<td style="width: 200px;" colspan="2">商 品</td>
				<td style="width: 100px;" >编号</td>
				<td style="width: 100px;" >规格</td>
				<td style="width: 80px;" >调拨数量(基本单位)</td>
				<td style="width: 120px;" >包装单位</td>
				<td class="width: 80px;" colspan="3">备 注</td>
			</tr>
			<s:iterator value="model.stockTracDetails" var="std">
			<s:set name="tcount" value="#attr.std.count" />
			<s:set name="pid" value="#attr.std.product.id" />
			<tr>
				<td colspan="2"><s:property value="#attr.std.product.name"/></td>
				<td ><s:property value="#attr.std.product.code"/></td>
				<td ><s:property value="#attr.std.product.stardard"/></td>
				<td ><s:property value="#attr.std.count"/>(<s:property value="#attr.std.product.units.name"/>)</td>
				<td><%=StockConstants.getUnitPack(pageContext.getServletContext(),(Integer)pageContext.getAttribute("pid"),(Integer)pageContext.getAttribute("tcount"))%></td>
				<td colspan="3"><s:property value="#attr.std.remark"/></td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
</div>
</body>
</html>
