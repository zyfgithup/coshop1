<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>代币卡充值信息</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">代币卡充值信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
	<s:hidden name="model.id" />
	<fieldset style="width: 65%; padding:10px 10px 10px 10px;">
	<legend>代币卡充值信息</legend>
	<table width="100%" align="left">
		<tr>
			<td align="right" width="250">代&nbsp;币&nbsp;卡&nbsp;号&nbsp;：</td>
			<td align="left" colspan="3">
			    ${model.cardGrant.card.cardNo}
			</td>
		</tr>
		<tr>
			<td align="right" width="250">身&nbsp;份&nbsp;证&nbsp;号&nbsp;：</td>
			<td align="left" colspan="3">
			    ${model.cardGrant.customer.idCard}
			</td>
		</tr>
		<tr>
			<td align="right">持&nbsp;&nbsp;卡&nbsp;&nbsp;人&nbsp;&nbsp;：</td>
			<td align="left" colspan="3">
				${model.cardGrant.customer.name}
			</td>
		</tr>
		<tr>
			<td align="right">充&nbsp;值&nbsp;日&nbsp;期&nbsp;：</td>
			<td align="left" colspan="3">
				<fmt:formatDate value="${model.recharge}" pattern="yyyy-MM-dd"/>				
			</td>
		</tr>
		<tr>
			<td align="right">经&nbsp;&nbsp;手&nbsp;&nbsp;人&nbsp;&nbsp;：</td>
			<td align="left" colspan="3">
				${model.user.name}
			</td>
		</tr>
		<tr>
			<td align="right" width="250" >充值金额(元)：</td>
			<td class="simple">
			    ${model.upMoney}
			</td>
		</tr>
		<tr>
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td align="left" colspan="3">
				${model.remark}
			</td>
		</tr>	
	</table>
	</fieldset>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			  <input type="button" value="返回" onclick="history.go(-1)" class="button"/> 
			</td>
		</tr>
	</table>
</div>
</div>
</body>
</html>