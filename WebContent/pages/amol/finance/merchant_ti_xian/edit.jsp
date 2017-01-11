<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<title>申请提现</title>
</head>
<body>
<div class="x-panel" style="width:100%">
<div class="x-panel-header">申请提现</div>
<div><%@ include file="/common/messages.jsp"%></div>
<s:form action="tiXian" id="save" validate="true" method="POST" theme="simple">
<div align="center" style="width: 100%">
	<fieldset style="width: 80%; padding:10px 10px 10px 10px;">
	<legend>申请提现</legend>
	
	<table width="100%" align="center" >
		
		<tr>
			<td align="right" >提现金额：</td>
			<td class="simple" align="left">
				<s:textfield id="tiXianMoney"
						name="tiXianMoney"  cssClass="required" cssStyle="width:145px;text-align:right;"
						onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')" onblur="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')"/>
						<font color="red">元*&nbsp;&nbsp;&nbsp;最大提现金额：${merchant.maxTiXianMoney }</font>
			</td>
			<td align="right">
	          	余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额：
	        </td>
	        <td class="simple" align="left">
	        	<input type="text"
						value="${merchant.allMoney }" class="required" style="width:145px;text-align:right;" readonly="readonly"/>
				元
	        </td>
		</tr>
		<tr>
			<td align="right" >收&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;入：</td>
			<td class="simple" align="left">
				<input type="text"
						value="${merchant.incomeAll }" class="required" style="width:145px;text-align:right;" readonly="readonly"/>
				元
			</td>
			<td align="right" >开&nbsp;&nbsp;户&nbsp;&nbsp;行：</td>
			<td class="simple" align="left">
				${merchant.bankSort.name }
			</td>
		</tr>
		<tr>
			<td align="right" >银行账号：</td>
			<td class="simple" align="left">
				${merchant.yhkh }
			</td>
		</tr>
	</table>
	
	</fieldset>
	
	<br/>
	注：从当前算起，向前推15天内的收入<c:if test="${!empty merchant.dongJieTiXianMoney || 0 == merchant.dongJieTiXianMoney }">【${merchant.dongJieTiXianMoney } 元】</c:if>是不可以提现
	<br/>
	<br/>
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td colspan="2" align="center" class="font_white">
						<s:submit value="保存" cssClass="button"></s:submit>&nbsp;&nbsp;
						<s:reset value="返回" cssClass="button" onclick="javascript:history.back();"/>
			</td>
		</tr>
	</table>
	
</div>

</s:form>

</div>
<script type="text/javascript">
$(document).ready(function() {
	$("#save").validate();
});
</script>
</body>
</html>