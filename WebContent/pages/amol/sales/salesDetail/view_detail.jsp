<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%request.setAttribute("ctx", request.getContextPath()); %>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>销售商品信息</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">销售商品信息</div>
<div align="center" style="width: 100%">
	<fieldset style="width: 75%; padding:10px 10px 10px 10px;">
	<legend>销售商品信息</legend>
	<table width="100%" align="left">
		<tr>
			<td align="right" width="100">商品名称：</td>
			<td align="left" colspan="3">
				${model.products.name }
			</td>
		</tr>
        <tr>
           <td align="right">
           	单&nbsp;&nbsp;&nbsp;&nbsp;价：
           </td>
           <td align="left" colspan="3">
           		<fmt:formatNumber value="${model.products.presentPrice }" pattern="#,##0.00"/><font color="red">&nbsp;元</font>
			</td>
        </tr>
        <!--  tr>
           <td align="right">
           	数&nbsp;&nbsp;&nbsp;&nbsp;量：
           </td>
           <td align="left" colspan="3">
           		${model.count }
			</td>
        </tr>-->
        <tr>
           <td align="right">
           	单&nbsp;&nbsp;&nbsp;&nbsp;位：
           </td>
           <td align="left" colspan="3">
           		${model.units.name }
			</td>
        </tr>
        <tr>
           <td align="right">
           	数&nbsp;&nbsp;&nbsp;&nbsp;量：
           </td>
           <td align="left" colspan="3">
           		${model.count }&nbsp;${model.units.name }
			</td>
        </tr>
        <tr>
           <td align="right">
           	退货数量：
           </td>
           <td align="left" colspan="3">
           		${model.hanod }&nbsp;${model.units.name }
			</td>
        </tr>
        <tr>
           <td align="right">
           	总&nbsp;金&nbsp;额：
           </td>
           <td align="left" colspan="3">
           		<fmt:formatNumber value="${model.sales.rttao }" pattern="#,##0.00"/><font color="red">&nbsp;元</font>
			</td>
        </tr>
        <tr>
           <td align="right">
           	备&nbsp;&nbsp;&nbsp;&nbsp;注：
           </td>
           <td align="left" colspan="3">
           		${model.remark }
			</td>
        </tr>
	</table>
	</fieldset>
	<fieldset style="width: 75%; padding:10px 10px 10px 10px;">
		<legend>条形码</legend>
		<s:iterator id="barcode" value="#request.barcodeList">
              	<s:property value="#barcode.barcode"/><br/>
        </s:iterator>
	</fieldset>
	<br/>
	<br/>
	<br/>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			  <input type="button" value="返回" onclick="history.go(-1);" class="button"/> 
			  <input type="button" value="关闭" onclick="window.close();" class="button"/> 
			</td>
		</tr>
	</table>
</div>
</div>
</body>
</html>