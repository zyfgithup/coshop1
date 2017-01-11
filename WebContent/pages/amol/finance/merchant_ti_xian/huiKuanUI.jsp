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
<title>汇款页面</title>

<script type="text/javascript">

/**
 * 确认汇款
 */
function qrhk(){
	window.location.href="${ctx }/tiXianManager/huiKuan.do?tiXianRecordId=${model.id }";
}

</script>

</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">汇款页面</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
	<fieldset style="width: 80%; padding:10px 10px 10px 10px;">
	<legend>汇款页面</legend>
	<table width="100%" align="center" >
		<tr>
			<td align="right" >商&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;家：</td>
			<td class="simple" align="left">
				<font color="red">${model.merchant.name }</font>
			</td>
			<td align="right">
	          	提现时间：
	        </td>
	        <td class="simple" align="left">
	        	<fmt:formatDate value="${model.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
	        </td>
		</tr>
		<tr>
			<td align="right" >手&nbsp;&nbsp;机&nbsp;&nbsp;号：</td>
			<td class="simple" align="left">
				${model.merchant.mobile }
			</td>
			<td align="right">
	          	电话：
	        </td>
	        <td class="simple" align="left">
	        	${model.merchant.phone }
	        </td>
		</tr>
		<tr>
			<td align="right" >提现金额：</td>
			<td class="simple" align="left">
				<font color="red">${model.tiXianMoney }元</font>
			</td>
			<td align="right">
	          	余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额：
	        </td>
	        <td class="simple" align="left">
	        	<font color="red">${model.balance }元</font>
	        </td>
		</tr>
		<tr>
			<td align="right" >收&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;入：</td>
			<td class="simple" align="left">
				${model.incomeAll }元
			</td>
			<td align="right">
	          	汇&nbsp;&nbsp;款&nbsp;&nbsp;人：
	        </td>
	        <td class="simple" align="left">
	        	<stc:username></stc:username>
	        </td>
		</tr>
		<tr>
			<td align="right">
	          	开&nbsp;&nbsp;户&nbsp;&nbsp;行：
	        </td>
	        <td class="simple" align="left">
	        	<font color="red">${model.merchant.bankSort.name }</font>
	        </td>
	        <td align="right">
	          	银行账号：
	        </td>
	        <td class="simple" align="left">
	        	<font color="red">${model.merchant.yhkh }</font>
	        </td>
		</tr>
	</table>
	
	</fieldset>
	
	
	<br/>
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
		<input type="button"
				value="确认汇款" class="button" onclick="qrhk();" />
				&nbsp;&nbsp;
			</td>
		<s:reset
				value="返回" cssClass="button" onclick="javascript:history. back();" />
			</td>
		</tr>
	</table>
</div>
</div>
</body>
</html>