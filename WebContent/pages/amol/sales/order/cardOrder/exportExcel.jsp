<%@ page language="java" %>
<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>    
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail,com.systop.amol.base.units.model.UnitsItem,java.util.Set,java.util.Iterator"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	response.setHeader("Content-disposition","attachment; filename=CardSalesOrder.xls"); 
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
<div class="x-panel-header"><font size="3">销售订单【代币卡】</font></div>
</div>

<div style="width:98%;" align="left">

<table class="cost" width="100%" border="1"
	style="border-collapse:collapse;border:solid 1px #000">
		<tr>
			<td align="right" width="175">
				编 &nbsp;&nbsp;&nbsp;&nbsp;号
			</td>
			<td class="simple" align="left" width="175" colspan="3" style="vnd.ms-excel.numberformat:@">
                  ${model.salesNo}
			</td>
			<td align="right" align="right" width="100" >
				日 &nbsp;&nbsp;&nbsp;&nbsp;期
			</td>
			<td class="simple" align="left" width="150" colspan="2" style="vnd.ms-excel.numberformat:yyyy/mm/dd">
			   ${model.createTime }
			</td>
		</tr>
				
		<tr>
			<td align="right" align="right" >
				客&nbsp;&nbsp;&nbsp;&nbsp;户
			</td>
			<td class="simple" align="left" colspan="3">
			${ model.customer.name }
			</td>
			<td align="right" align="right" >
				销&nbsp;售&nbsp;员：
			</td>
			<td class="simple" align="left" colspan="2" >
			${ model.employee.name }
			</td>
		</tr>
		<tr> 
			<td align="right" >
				应&nbsp;&nbsp;&nbsp;&nbsp;收
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
       <td width="100" align="center">商品编码</td>
       <td width="100" align="center">商 品</td>
       <td width="70" align="center">规格</td>
       <td width="70" align="center">单位</td>
       <td width="70" align="center">单价</td>
       <td width="70" align="center">数量</td>
       <!--td width="100" align="center">基本单位数量</td-->
       <td width="100" align="center">出库数量</td>
       <td width="70" align="center">金额</td>
       <td width="175">备注</td>
    </tr>
   <s:if test="purchaseDetail.size==0">
   </s:if>
   <%
   List<SalesDetail> sd=(List<SalesDetail>)request.getAttribute("sd");
   if(sd!=null){
   for(SalesDetail s:sd){
   %>
   <tr height="18">
   	  <td align="center"><%=s.getProducts().getCode()%>  </td>
      <td align="center"><%=s.getProducts().getName()%>  </td>
      <td align="center"><%=s.getProducts().getStardard()%></td> 
      <td align="center"><%=s.getUnits().getName()%></td>
      <td align="right"><%=s.getOutPrice()%></td>
      <td align="right"><%=s.getNcount()%></td>
      <!--td align="right"><%=s.getCount()%></td-->
      <% 
      	      int u = 0;//单位对应的基本单位数量
      	      Set<UnitsItem> unitsItemSet = s.getUnits().getUnitsItem();
    	      Iterator<UnitsItem> unitsItemIs = unitsItemSet.iterator();
    	      while(unitsItemIs.hasNext()){
    	    	UnitsItem unitsItem = unitsItemIs.next();
    	    	if(unitsItem.getProducts().getId().intValue() == s.getProducts().getId().intValue()){
    	    		u = unitsItem.getCount();
    	    		break;
    	    	}
    	      }
      %>
      <td align="right"><%=((float)s.getHanod())/u %></td>
      <td align="right"><%=s.getAmount()%></td>
      <td><%=s.getRemark()%>&nbsp;</td>
    </tr>
    <%}} %>
    </tbody>
</table>  

</div>

</body>

</html>
