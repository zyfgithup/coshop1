<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>商品单位换算管理</title>
<%@include file="/common/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@ include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">
<div style="float: left;">商品单位换算管理</div>
<div style="float: right;">
	<a href="#" onclick="javascript:history. back();"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;" />&nbsp;返回&nbsp;</a>
	</div>
</div>

<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save.do" id="save" method="post">
	<s:hidden name="model.id" />
	<fieldset style="width: 50%; padding:10px 10px 10px 10px;">
	<legend>商品单位换算编辑</legend>
	<table width="100%" align="left">
	    <tr><td><s:hidden id="productId" name="productId"/></td></tr>
		<tr>
			<td align="right" width="100">商品单位：</td>
			<td>
			   <c:if test="${isedit==1}">
			  <s:select id="unitsMap" list="unitsMap"  name="model.units.id" headerKey="" 
                   disabled="true"  headerValue="请选择" cssStyle="width:120px;" cssClass="required"/><font color="red">*</font>
		      </c:if>
		      <c:if test="${isedit==0}">
		      <s:select id="unitsMap" list="unitsMap"  name="model.units.id" headerKey="" 
                  headerValue="请选择" cssStyle="width:120px;" cssClass="required"/><font color="red">*</font>
		     </c:if>
		   </td>
		</tr>
        <tr>
           <td align="right">换算关系：</td>
           <td align="left" colspan="3">
               <c:if test="${isedit==1}">
           		<s:textfield id="count" name="model.count"   
		      readonly="true"  cssStyle="width:120px;" cssClass="required countValidator" onblur="javascript:return calPrice()" onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')" />
           		</c:if>
		      <c:if test="${isedit==0}">
           		<s:textfield id="count" name="model.count"   
		     cssStyle="width:120px;" cssClass="required countValidator" onblur="javascript:return calPrice()" onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')" />
           		</c:if>
           		
           		<font color="red">${model.products.units.name }*</font>
           		<span id="counts">&nbsp;</span>
			</td>
        </tr>	
        <tr>
           <td align="right">折合包装：</td>
           <td align="left" colspan="3">
           		<s:select list='#{"0":"是","1":"否"}' name="model.conversion" cssStyle="width:120px;"></s:select>
			</td>
        </tr>		
		<tr>
			<td align="right">参考进价：</td>
			<td align="left" ><s:textfield id="inprice"
				name="model.inprice" cssClass="required" size="18"
				onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')" onblur="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')"/>
				<font color="red">*</font></td>
		</tr>
		<tr>
			<td align="right">参考出库价：</td>
			<td align="left" ><s:textfield id="outprice"
				name="model.outprice" cssClass="required" size="18"
				onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')" onblur="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')" />
				<font color="red">*</font></td>
		</tr>

	</table>
	</fieldset>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			  <s:submit value="保存" cssClass="button" /> 
			  &nbsp;&nbsp;
			  <s:reset value="重置" cssClass="button" />
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>

<script type="text/javascript">
$(document).ready(function() {
	$("#save").validate();
});
$(function() {
	$.validator.addMethod("countValidator", function(value, element) {      
		var res;
        var value;		
		if (value == 1 || value == 0){
			res = "err";
	    	$("#counts").html("<font color='red'>请输入大于1的数字</font>");
		}else{
	    	$("#counts").html("");
	    }		
        return res != "err";
    },"");
});
function calPrice() {
	var productId = document.getElementById('productId').value;	
	var count = document.getElementById('count').value;	
	
    if (productId == null || productId == '') {
        alert("该商品信息为空，请您重新进行单位换算！");
        return false;
     }
    
    if (count == null || count == '' || count == '0') {
    	alert("请您输入换算关系！");
        return false;
     }
    
    if (count == '1') {
    	alert("已经存在基本单位信息，请您重新输入换算关系！");
        return false;
     }
    
	Ext.Ajax.request({
		url : '/base/units/item/calPrice.do',
		params : {
			'productId' : productId,
			'count' : count
		},
		method : 'POST',
		success : function(response) {
		    var jsonResult = Ext.util.JSON.decode(response.responseText);
		        if (jsonResult.result == 'error') {
		         alert("请您重新登录系统！");	       
			     return false;
		        }
			    document.getElementById('inprice').value = jsonResult.inprice;
			    document.getElementById('outprice').value = jsonResult.outprice;			    
		    }
	});	
}
</script>
</body>
</html>