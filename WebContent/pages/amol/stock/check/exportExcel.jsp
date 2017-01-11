<%@page import="com.systop.amol.stock.model.StockCheck"%>
<%@ page language="java" %>
<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>    
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	StockCheck sc = (StockCheck)request.getAttribute("model");
	response.setHeader("Content-disposition","attachment; filename="+sc.getCheckNo()+".xls"); 
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
<div class="x-panel-header" align="left"><font size="3">【库存盘点单】</font></div>
</div>
<div style="width:98%;" align="left">
<table class="cost" width="100%" border="1"
	style="border-collapse:collapse;border:solid 1px #000">
		<tr>
			<td width="66" align="right">编号：</td>
			<td class="simple" align="left" width="175" colspan="3" style="vnd.ms-excel.numberformat:@">
			${model.checkNo }</td>
			<td width="96" align="right">日期：</td>
			<td class="simple" align="left" width="150" colspan="2" style="vnd.ms-excel.numberformat:yyyy/mm/dd">
			${model.createTime }</td>
		</tr>
		<tr>
			<td align="right">清算人员：</td>
			<td class="simple" align="left" colspan="3">${model.employee.name} 
			</td>
			<td align="right">仓库：</td>
			<td class="simple" align="left" colspan="2">${model.storage.name}</td>
		</tr>
		<tr>
			<td align="right" >
				操作员：
			</td>
			<td class="simple" align="left" colspan="3">
				${ model.user.name}
			</td> 
		</tr>	
		<s:if test="model.status == 1">
		<tr>
			<td colspan="4" class="simple" align="left" style="color:red">注意:该单已经清算完毕!</td>
		</tr>
		</s:if>
		<s:else>
		<tr>
			<td colspan="4" class="simple" align="left" style="color:red">注意:该单未清算完毕!</td>
		</tr>
		</s:else>
	</table>
	<table><tr></tr></table>
	<table class="cost" width="100%" border="1"
	style="border-collapse:collapse;border:solid 1px #000">
		<tbody id="tbody">
			<tr bgcolor="#E6F4F7" align="center">
				<td style="width: 200px;" colspan="1">编号</td>
				<td style="width: 200px;" colspan="1">商品</td>
				<td style="width: 200px;" colspan="1">规格</td>
				<td style="width: 100px;" colspan="1">单位</td>
				<td style="width: 100px;" colspan="2">实际数量</td>
				<td style="width: 80px;" colspan="2">库存数量</td>
				<td style="width: 100px;" colspan="2">清查数量</td>
			</tr>
			<s:iterator value="model.stockCheckDetails" var="scd">
			<tr>
				<td colspan="1"><s:property value="#attr.scd.stock.products.code"/></td>
				<td colspan="1"><s:property value="#attr.scd.stock.products.name"/></td>
				<td colspan="1"><s:property value="#attr.scd.stock.products.stardard"/></td>
				<td colspan="1"><s:property value="#attr.scd.stock.products.units.name"/></td>
				<td colspan="2"><s:property value="#attr.scd.count"/></td>
				<td colspan="2"><s:property value="#attr.scd.stockCount"/></td>
				<td colspan="2"><s:property value="#attr.scd.checkCount"/></td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
</div>
</body>
</html>
