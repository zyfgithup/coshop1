<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.purchase.model.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<title>采购退货单</title>

</head> 
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">采购退货信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">


	<s:hidden name="model.id" />
	<fieldset style="width: 94%; padding: 10px 10px 10px 10px;">
	<legend>采购退货单</legend>
	<table width="100%" align="left">
		<tr>
			<td align="right">编 &nbsp;&nbsp;&nbsp;号：</td>
			<td width="200" class="simple" align="left">
			${model.sno}</td>
			<td align="right">日&nbsp;&nbsp;&nbsp;&nbsp;期：</td>
			<td align="left">
			<fmt:formatDate value="${ model.sdate}" pattern="yyyy-MM-dd"/>
			</td>
		</tr>


		<tr>
			<td align="right">供&nbsp;应&nbsp;商：</td>
			<td class="simple" align="left">
			${ model.supplier.name }
			</td>
				<s:if test="null!=model.storage.name&&!model.storage.name.isEmpty()">
			<td align="right">仓&nbsp;&nbsp;&nbsp;&nbsp;库：</td>
			<td class="simple" align="left">
			${model.storage.name}</td>
             </s:if>
		</tr>
		<tr>
			<td align="right">采&nbsp;购&nbsp;员：</td>
			<td class="simple" align="left">${model.employee.name}</td>
			<td align="right">应&nbsp;&nbsp;&nbsp;&nbsp;退：</td>
			<td class="simple" align="left">${model.samount }
			</td>

		</tr>
		<tr>
			<td align="right">
	                     操&nbsp;作&nbsp;员：
			</td>
			<td class="simple" align="left">
				${ model.user.name}
			</td> 
			<td align="right">实&nbsp;&nbsp;&nbsp;&nbsp;退：</td>
			<td class="simple" align="left">${model.spayamount}</td>
		</tr>
		<tr>
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td class="simple" align="left" colspan="3">${model.remark }</td>
		</tr>
	</table>
	</fieldset>
	
	<table border="1" align="center" width="94%" style="border-collapse:collapse;border:solid 1px #CCC">
		<tbody id="tbody">
			<tr bgcolor="#F3F4F3" align="center" height="23">
				<td style="width:100px;" >商品编号</td>
       <td style="width:100px;" >商品名称</td>
				<td style="width: 100px;">规格</td>
				<td style="width: 80px;">包装单位</td>
				<td style="width: 100px;">单价</td>
				<td style="width: 100px;">数量</td>
				<td style="width: 80px;">金额</td>
				<td style="width: 100px;">备注</td>
			
			</tr>
			
			<s:if test="purchaseDetail.size==0">

			</s:if>
			<%
   List<PurchaseDetail> sd=(List<PurchaseDetail>)request.getAttribute("purchaseDetail");
			if(sd != null){
   for(PurchaseDetail s:sd){
    %>

			<tr height="18">
			<td align="center"><%=s.getProducts().getCode()%>  </td>
				<td align="center"><%=s.getProducts().getName()%></td>
				<td align="center"><%=s.getProducts().getStardard()%></td>
				<td align="center"><%=s.getUnits().getName()%></td>
				<td align="center"><%=s.getPrice()%></td>
				<td align="center"><%=s.getNcount()%></td>
				<td align="right"><%=s.getAmount()%></td>
				<td align="center"><%=s.getRemark()==null?"":s.getRemark()%></td>
				
			</tr>


			<%}} %>
		</tbody>
	</table>

	<table width="100%" style="margin-bottom: 10px;">
		<tr>
		 <td align="center">
			<input type="button" class="button" value="导出" onclick="javascript:exportExcel(${model.id})">
			<s:reset
				value="返回" cssClass="button" onclick="javascript:history. back();" />
		 </td>
		</tr>
	</table>
</div>
</div>



</body>
<script type="text/javascript">

function exportExcel(id){
	 window.location = '${ctx}/purchase/returns/exportExcel.do?model.id='+id;
}
</script>
</html>