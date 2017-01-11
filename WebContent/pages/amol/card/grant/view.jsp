<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>代币卡信息</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">代币卡信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
	<s:hidden name="model.id" />
	<fieldset style="width: 65%; padding:10px 10px 10px 10px;">
	<legend>代币卡信息</legend>
	<table width="100%" align="left">
		<tr>
			<td align="right" width="250">经&nbsp;&nbsp;&nbsp;销&nbsp;&nbsp;&nbsp;商：</td>
			<td align="left" colspan="3">
			    ${model.customer.owner.name}
			</td>
		</tr>
		<tr>
			<td align="right" width="250">身&nbsp;份&nbsp;证&nbsp;号&nbsp;：</td>
			<td align="left" colspan="3">
			    ${model.customer.idCard}
			</td>
		</tr>
		<tr>
			<td align="right">持&nbsp;&nbsp;&nbsp;卡&nbsp;&nbsp;&nbsp;人：</td>
			<td align="left" colspan="3">
				${model.customer.name}
			</td>
		</tr>
		<tr>
			<td align="right">发&nbsp;&nbsp;&nbsp;卡&nbsp;&nbsp;&nbsp;人：</td>
			<td align="left" colspan="3">
				${model.creator.name}
			</td>
		</tr>
		<tr>
			<td align="right">代&nbsp;币&nbsp;卡&nbsp;号&nbsp;：</td>
			<td align="left" colspan="3">
				${model.card.cardNo}
			</td>
		</tr>
		<tr>
			<td align="right">存&nbsp;款&nbsp;单&nbsp;号&nbsp;：</td>
			<td align="left" colspan="3">
				${model.depositReceipt}
			</td>
		</tr>
		<tr>
			<td align="right">开&nbsp;卡&nbsp;日&nbsp;期&nbsp;：</td>
			<td align="left" colspan="3">
				${createDate}
			</td>
		</tr>
		<tr>
			<td align="right">失&nbsp;效&nbsp;日&nbsp;期&nbsp;：</td>
			<td align="left" colspan="3">
				${endDate}
			</td>
		</tr>
		<tr>
			<td align="right" width="250" >信&nbsp;用&nbsp;额&nbsp;度&nbsp;：</td>
			<td class="simple">
			    ${model.credit}
			</td>
		</tr>
        <tr>
			<td align="right">消&nbsp;费&nbsp;额(元)：</td>
			<td align="left" colspan="3">
				${model.spend}
			</td>
		</tr>
		<tr>
			<td align="right">充值总额(元)：</td>
			<td align="left" colspan="3">
				${model.upMoney}
			</td>
		</tr>
		<tr>
			<td align="right">余&nbsp;&nbsp;&nbsp;&nbsp;额(元)：</td>
			<td align="left" colspan="3">
				${model.balance}
			</td>
		</tr>	
		<tr>
			<td align="right">描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述：</td>
			<td align="left" colspan="3">
				${model.descn}
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