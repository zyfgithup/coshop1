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

<title>库存调拨</title>
<script type="text/javascript" src="${ctx}/pages/amol/stock/trac/editjs.js"></script>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header" >
	<div style="float: left;">库存调拨管理</div>
	<div style="float: right;font-weight:normal;">
	<a href="${ctx}/stock/trac/index.do" title="返回库存调拨列表">
	<img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>返回库存调拨列表
	</a>	
	</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save" id="save" validate="true" method="POST">
	<s:hidden name="model.id"  /> 
	<s:hidden name="model.sign"  />
	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>库存调拨</legend>	
	<table width="100%" align="center" >
		<tr>
			<td align="right">
				单据编号：
			</td>
			<td width="200" class="simple" align="left">
				<s:textfield id="checkNo" name="model.stockTrac.checkNo" size="16" cssStyle="border:0;border-bottom:1 solid black;background:transparent;" readonly="true" />
		    </td>
		</tr>
		<tr>
			<td align="right">
				出货仓库：
			</td>
			<td width="200" class="simple" align="left">
				<s:select id="outStorage" onchange="removeAllRow()" cssStyle="width:110px" list="outStorageMap" name="model.stockTrac.outStorage.id" headerKey="" headerValue="选择出货库" />
           		<span id="outStorageCheck"></span><font color="red">&nbsp;*</font>
		    </td>
		</tr>
		<tr>
			<td align="right">入货仓库：</td>
			<td width="80%" class="simple" align="left">
				<s:select cssStyle="width:110px" onchange="removeAllRow()" id="inStorage" list="inStorageMap" name="model.stockTrac.inStorage.id" headerKey=""	headerValue="选择入货库" />
				<span id="inStorageCheck"></span><font color="red">&nbsp;*</font>
			</td>
		</tr>
		<tr> 
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
				<td colspan="3" class="simple" align="left">
				<s:textfield name="model.stockTrac.remark" cssStyle="width:558px;"/>
			</td>
		</tr>
	</table>
	</fieldset>
	<table align="center" width="95%"> 
	<tr><td><div align="left"><input name="button" class="button" type="button" value="增加商品" onclick="addRow()"></div></td></tr>
	</table>
	<table width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9"> 
		<tbody id="tbody">
	    <tr>
	       <td height="23" align="center" bgcolor="#F3F4F3" style="width:180px;" colspan="2">商品</td>
	       <td align="center" bgcolor="#F3F4F3" style="width:100px;">编号</td>
	       <td align="center" bgcolor="#F3F4F3" style="width:100px;">规格</td>
	       <td align="center" bgcolor="#F3F4F3" style="width:80px;">库存数量</td>
	       <td align="center" bgcolor="#F3F4F3" style="width:80px;">调拨数量</td>
	       <td align="center" bgcolor="#F3F4F3" style="width:60px;">单位</td>
	       <td align="center" bgcolor="#F3F4F3" style="width:140px;">备注</td>
	       <td align="center" bgcolor="#F3F4F3" style="width:80px;">操作</td>
	    </tr>
	    <tr height="23" align="center" style="display:none" bgcolor="#FFFFFF"> 
	      <td align="center" bgcolor="#FFFFFF">
		      <input name="pname" type="text" size="15"  readonly="readonly" style="color:#808080;border: 0px;"/>
		      <input type="hidden" name="pid" />
	      </td>
	      <td align="center" bgcolor="#FFFFFF"><input name="button1"  type="button" onclick="edit(this.parentNode.parentNode)" value="选择" class="button"/></td>
	      <td align="center" bgcolor="#FFFFFF"><input name="code" type="text" size="15" readonly="readonly" style="color:#808080;border: 0px;"/></td>
	      <td align="center" bgcolor="#FFFFFF"><input name="stardard" type="text" size="15" readonly="readonly" style="color:#808080; border: 0px;"/></td>
	      <td align="center" bgcolor="#FFFFFF">
	      	<input name="countAll" type="text" size="12" value="0"  readonly="readonly" style="color:#808080; border: 0px;"/>
	      	<input type="hidden" name="basicCount" size="9" readonly="readonly"	align="right" style="color: #808080;" />
	      </td>
	      <td align="center" bgcolor="#FFFFFF">
	      	<input name="outCount" type="text" size="12" onfocus="beforeyzzs(this)"  
	      	onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'');yzzs(this)"
			onblur="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'');yzsz(this)" align="right" />
			<input type="hidden" name="counts" size="9" readonly="readonly"	align="right" style="color: #808080;" />
		  </td>
		  <td align="center" bgcolor="#FFFFFF">
	      	<input name="unit" type="text" size="6" readonly="readonly" style="color:#808080; border: 0px;" onfocus="showUnits(this)"/>
	      	<input type="hidden" name="unitid" />
	      </td> 
	      <td align="center" bgcolor="#FFFFFF"><input name="sremark" type="text" size="18"/></td>
	      <td align="center" bgcolor="#FFFFFF"><input type="button" value="删除" class="button" onclick="removeRow(this.parentNode.parentNode)"></td>
	    </tr>
		</tbody>
	</table>
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
				<input type="button" value="保存" Class="button" onclick="return saveyz();"/>&nbsp;&nbsp; 
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