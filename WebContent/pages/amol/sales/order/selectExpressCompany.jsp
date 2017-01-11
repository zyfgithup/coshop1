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
<title>选择快递公司</title>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">选择快递公司</div>
<div><%@ include file="/common/messages.jsp"%></div>
<s:form action="saveSelectExpressCompany.do" id="orderView" validate="true" method="POST">
<div align="center" style="width: 100%">

	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>快递公司</legend>
    <table width="100%">
    
    	<tr>
        	<td align="right">快递公司：</td>
			<td class="simple">
				
					<select name="model.expressCompany.id">
						<option value="0">--请选择--</option>
						<c:forEach items="${expressCompanyList }" var="expressCompany" varStatus="status">
							
							<option value="${expressCompany.id }" 
								<c:if test="${model.expressCompany.id == expressCompany.id }">selected="selected"</c:if>>
									${expressCompany.name }
							</option>
						</c:forEach>
					</select>
				
			</td>
        	<td align="right">快递单号：</td>
			<td align="left">
				<input type="text" name="model.courierNumber" value="${model.courierNumber }" size="35" maxlength="80"/>
			</td>
		</tr>
    
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
			<c:if test="${!(model.salesType eq 'jfSale') }">
				<td align="right">金&nbsp;&nbsp;&nbsp;&nbsp;额：</td>
				<td class="simple">
					<fmt:formatNumber value="${model.samount}" pattern="#,##0.00"/>
				</td>
			</c:if>
			<c:if test="${model.salesType eq 'jfSale' }">
				<td align="right">积&nbsp;&nbsp;&nbsp;&nbsp;分：</td>
				<td class="simple">
					<fmt:formatNumber value="${model.samount}"/>
				</td>
			</c:if>
		</tr>
		<c:if test="${payment.name =='支付宝'||payment.name =='微信'}">
		<tr>
			 <td align="right">收货姓名：</td>
			<td class="simple" width="35%">
				${model.address.receiveName }
			</td>
			<td align="right">收货手机：</td>
			<td class="simple">
				${model.address.receivePhone }
			</td>
		</tr>
		<tr>
			 <td align="right" width="15%" >收货地址：</td>
			<td class="simple" width="35%">
				${model.address.address }
			</td>
			<td align="right">收货邮编：</td>
			<td class="simple">
				${model.address.postCode }
			</td>
		</tr>
		
		</c:if>
			
	</table>
	</fieldset>

</div>
<div class="x-panel-body">
<div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			  <input type="submit" class="button" value=" 确认配送 ">
			  <input type="button" value=" 返 回 " onclick="history.go(-1)" class="button"/> 
			</td>
		</tr>
	</table>
</div>
</s:form>
</div>
</body>
</html>