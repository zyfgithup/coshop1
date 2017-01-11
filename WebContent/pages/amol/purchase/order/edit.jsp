<%@ page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.purchase.model.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>

<title>采购订单</title> 
<script type="text/javascript" src="${ctx}/pages/amol/purchase/order/editjs.js"></script>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">采购订单信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save" id="save" validate="true" method="POST">
	<s:hidden name="model.id"  /> 
	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>采购订单</legend>
	<table width="100%" align="left" >
		<tr>
			<td align="right" >
				编 &nbsp;&nbsp;&nbsp;号：
			<td width="400" class="simple" align="left">
					<s:textfield  name="model.sno" maxLength="20" 
					cssStyle="width:155px;color:#808080;" cssClass="required" readonly="true"/>
           		<font color="red">&nbsp;*</font>
		  </td>
			<td align="right" >
				 日&nbsp;&nbsp;&nbsp;&nbsp;期：</td>
			<td width="213" class="simple" align="left">
			<s:textfield  name="model.sdate" maxLength="20" cssStyle="width:155px;"  cssClass="required" id="sdate"
				onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate" readonly="true"/>
           		<font color="red">&nbsp;*</font>
		  </td>
		</tr>
		
		<tr>
			<td align="right" >
				供&nbsp;应&nbsp;商：
			</td>
			<td class="simple" align="left">
			<s:hidden name="model.supplier.id" id="sid" />
				<s:textfield  name="model.supplier.name" id="supname" maxLength="20" size="25" 
				readonly="true" cssStyle="width:155px;color:#808080;"/>
				<input name="button2"  type="button"   onclick="supplier()" value="选择" class="button"/>
				<font color="red">&nbsp;*</font>     		
			</td>
			<td align="right" >
				采&nbsp;购&nbsp;员：
			</td>
			<td class="simple" align="left">
				<s:hidden name="model.employee.id" id="empid" />
				<s:textfield  name="model.employee.name" id="empname" maxLength="20" size="25" 
				readonly="true" cssStyle="width:155px;color:#808080;"/>
				<input name="button2"  type="button"   onclick="emp()" value="选择" class="button"/>
           	
			</td>
		</tr>
		<tr> 
			<td align="right" >
				应&nbsp;&nbsp;&nbsp;&nbsp;付：
			</td>  
			<td class="simple" align="left">
				<s:textfield  name="model.samount"  id="samount" maxLength="20" size="25"
				  readonly="true" cssStyle="width:155px;color:#808080;text-align: right" cssClass="required"/>
           		<font color="red">&nbsp;*</font>
			</td>
			<td align="right" >
				操&nbsp;作&nbsp;员：
			</td>
			<td class="simple" align="left">
				<s:textfield  name="model.user.name" disabled="true" maxLength="20" cssStyle="width:155px;" />
			</td> 
		</tr>
		<tr> 
		<td align="right" >
				 要货时间：</td>
			<td width="213" class="simple" align="left">
			<s:textfield  name="model.getDate" maxLength="20" cssStyle="width:155px;"   
				onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate" readonly="true"/>
           		
		  </td>
		<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td  class="simple" align="left"><s:textfield
				name="model.remark" cssStyle="width:155px;"/></td>
			
		</tr>
				</table>
	</fieldset>

	<table align="center" width="95%"> 
	<tr><td><div align="left"><input name="button" class="button" type="button" value="增加商品" onclick="addRow()"></div></td></tr>
	</table>

	<table  width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9">
  <tbody id="tbody">
    <tr bgcolor="#F3F4F3" align="center" height="23">
       <td style="width:80px;">商品编码</td><td></td>
       <td style="width:80px;">商品名称</td>
       <td style="width:80px;">规格</td>
       <td style="width:80px;">包装单位</td>
       <td style="width:60px;">单价</td>
       <td style="width:60px;">数量</td>
       <td style="width:80px;">金额</td>
       <td style="width:100px;">备注</td>
       <td style="width:60px;">删除</td>
    </tr>
    <tr style="display:none" height="23" bgcolor="#FFFFFF"> 
      <td><input name="pcode" type="text" size="10"  readonly="readonly" style="color:#808080;"/></td>
      <td><input name="button1"  type="button" onclick="edit(this.parentNode.parentNode)" value="选择" class="button"/></td>
      <td><input name="pname" type="text" size="10"  readonly="readonly" style="color:#808080;"/>
      <input type="hidden" name="pid" /></td>
      <td><input name="stardard" type="text" size="9" readonly="readonly" style="color:#808080;"/></td> 
      <td><input name="unitname" type="text" size="4"  /><input type="hidden" name="unitid"/></td>
      <td><input name="inprice" type="text" size="8" value="0" onkeyup="yzsz(this)" /></td>
      <td><input name="ncount" type="text" size="10" value="0" onkeyup="yzzs(this)" onfocus="beforeyzzs(this)"/>
      <input name="count" type="hidden" size="10" value="0"  readonly="readonly" style="color:#808080;"/></td>
      <td><input name="money" align="right" type="text" size="10" value="0" readonly="readonly" style="color:#808080;text-align: right"/></td>
      <td><input name="sremark" type="text" size="12" /></td>
      <td><input type="button" value="删除" class="button" onclick="removeRow(this.parentNode.parentNode)"></td>
    </tr>
   <s:if test="purchaseDetail.size==0">
   </s:if>
   <%
   List<PurchaseDetail> sd=(List<PurchaseDetail>)request.getAttribute("purchaseDetail");
   if(sd!=null){
   for(PurchaseDetail s:sd){
   %>
   <tr height="23" bgcolor="#FFFFFF"> 
   
     <td ><input name="pcode" type="text" size="10" value="<%=s.getProducts().getCode()%>" readonly="readonly" style="color:#808080;"/>
	  </td><td><input name="button1"  type="button"   onclick="edit(this.parentNode.parentNode)" value="选择" class="button"/> </td>
	  <td ><input name="pname" type="text" size="10" value="<%=s.getProducts().getName()%>" readonly="readonly" style="color:#808080;"/>
     <input type="hidden" name="pid" value="<%=s.getProducts().getId()%>"/>
	  </td>
      <td ><input name="stardard" value="<%=s.getProducts().getStardard()%>" type="text" size="9" readonly="readonly" style="color:#808080;"/></td> 
      <td ><input name="unitname" value="<%=s.getUnits().getName()%>" type="text" size="4" /><input type="hidden"  value="<%=s.getUnits().getId()%>" name="unitid"/></td>
      <td><input name="inprice" value="<%=s.getPrice()%>" type="text" size="8" value="0" onkeyup="yzsz(this)"/></td>
       <td><input name="ncount" value="<%=s.getNcount()%>" type="text" size="10" value="0" onkeyup="yzzs(this)" onfocus="beforeyzzs(this)" />
     <input name="count" type="hidden" value="<%=s.getCount()%>"  size="10" value="0"  readonly="readonly" style="color:#808080;"/></td>
      <td><input name="money"  value="<%=s.getAmount()%>" type="text" size="10" value="0" readonly="readonly" style="color:#808080;text-align: right"/></td>
      <td><input name="sremark" value="<%=s.getRemark()%>" type="text" size="12" /></td>
      <td><input type="button" class="button" value="删除" class="button" onclick="removeRow(this.parentNode.parentNode)"></td>
    </tr>
    <%}} %>
    </tbody>
</table>
		
		
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
				<s:submit value="保存" cssClass="button" onclick="return saveyz();"/>&nbsp;&nbsp; 
				<s:reset value="返回" cssClass="button" onclick="javascript:history. back();"/>
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
<script type="text/javascript">
	/** ready */
	$(document).ready(function() {
		$("#save").validate();
	});
</script>


</body>
</html>