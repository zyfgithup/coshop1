<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail,com.systop.amol.base.units.model.UnitsItem,java.util.Set,java.util.Iterator"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<script type="text/javascript">
function showPic(url,event){
	$("#layer").html("<img src='"+url+"' with='300' height='300'>");
	$("#layer").show();
}
function hiddenPic(){
	$("#layer").html("");
	$("#layer").hide();
}
</script>
<title>销售订单详情</title>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">销售订单详情</div>
<s:form action="exportExcel.do" id="orderView" validate="true" method="POST">
<div align="center" style="width: 100%">

	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>详情</legend>
    <table width="100%">
		<tr>
			<td align="right" width="15%" >销售单号：</td>
			<td class="simple" width="35%" >
				${model.salesNo }
				<s:hidden name="model.id" />
			</td>
			 <td align="right" width="15%" >用&nbsp;户&nbsp;名：</td>
			<td class="simple" width="35%">
				${model.user.loginId }
			</td>
		</tr>
		<tr>
			 <td align="right"  >姓&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
			<td class="simple" width="35%">
				${model.user.name }
			</td>
			<td align="right">积&nbsp;&nbsp;&nbsp;&nbsp;分：</td>
			<td class="simple">
				<fmt:formatNumber value="${model.samount }" pattern="#,##0"/>
			</td>
		</tr>
        <tr>
        <td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
			<td class="simple">
				${status }</td>
        	<td align="right">支付方式：</td>
			<td align="left" colspan="3">
				积分兑换
			</td>
		</tr>	
	</table>
	</fieldset>

</div>
<div class="x-panel-body">
<div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9"> 
		
  <tbody id="tbody">
    <tr bgcolor="#F3F4F3" align="center" height="23">
       <td width="100" align="center">图片</td>
       <td width="100" align="center">商 品</td>
       <td width="70" align="center">积分</td>
       <td width="70" align="center">数量</td>
       <!-- td width="100" align="center">基本单位数量</td> -->
       <td width="70" align="center">总积分数</td>
    </tr>
   <s:if test="purchaseDetail.size==0">
   </s:if>
   <%
   List<SalesDetail> sd=(List<SalesDetail>)request.getAttribute("sd");
   if(sd!=null){
   for(SalesDetail s:sd){
   %>
   <tr height="23" bgcolor="#FFFFFF"> 
      <td align="center"><img style="width: 40px;height: 40px" alt="<%=s.getProducts().getName()%>" src="${ctx }/<%=s.getProducts().getImageURL()%>" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" /></td>
      <td align="center"><%=s.getProducts().getName()%>  </td>
      <td align="right"><fmt:formatNumber value="<%=s.getProducts().getIntegral()%>" pattern="#,##0"/></td>
      <td align="right"><%=s.getTnorootl()%></td>
      <!-- td align="right"><%=s.getCount()%></td> -->
      <td align="right"><fmt:formatNumber value="<%=s.getAmount()%>" pattern="#,##0"/></td>
    </tr>
    <%}} %>
    </tbody>
</table>

	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			  <input type="submit" class="button" value="导出">
			  <input type="button" value="返回" onclick="history.go(-1)" class="button"/> 
			</td>
		</tr>
	</table>
</div>
</s:form>
</div>
</body>
</html>