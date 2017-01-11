<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,java.util.Set,java.util.Iterator"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/validator.jsp"%>
<title>添加商铺轮播图</title>
<script type="text/javascript">
/**
 * 选择商品
 */
function selectProduct(){

	var productNameObj = document.getElementById("productNameID");
	var productIdObj = document.getElementById("productId");
	  
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/base/product/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
	 	if(cus!=null){
	 		  productNameObj.value = cus.name;
	 		  productIdObj.value = cus.id;
	 	}
	}else{//火狐
		window.open(URL_PREFIX+"/pages/amol/base/product/selector.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
		this.returnAction=function(cus){
			if(cus!=null){
				productNameObj.value = cus.name;
				productIdObj.value = cus.id;
			}
		}
	}
}


/**
 * 保存前验证
 */
function saveValidate(){
	
	var fileIdValue = document.getElementById("fileId").value;
	var contentIdValue = document.getElementById("contentId").value;
	var productIdValue = document.getElementById("productId").value;
	if("" == fileIdValue || 0 == fileIdValue.length){
		alert("请上传图片！");
		return false;
	}
	if("" != fileIdValue && 0 != fileIdValue.length && "" != productIdValue && 0 != productIdValue.length && 0 != productIdValue){
		alert("图片和商家是互斥的，请选一项填写！");
		return false;
	}
	
	var arry = fileIdValue.split('.');
	var imagel = arry.length-1;
	if(arry[imagel] != "jpg" && arry[imagel] != "png" && arry[imagel] != "bmp" && arry[imagel] != "jpeg" && arry[imagel] != "gif"
		&& arry[imagel] != "JPG" && arry[imagel] != "PNG" && arry[imagel] != "BMP" && arry[imagel] != "JPEG" && arry[imagel] != "GIF"){
		alert("请上传jpg、png、bmp、jpeg、gif格式的图片");
		return false;
	}
	return true;
}
</script>
</head>
<body>

<div class="x-panel" style="width: 100%">
<div class="x-panel-header">添加</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save"  id="save" validate="true" method="POST" enctype ="multipart/form-data">
	<br/>
	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>添加</legend>
	<table width="90%">
		<tr>
			<td align="right">图片：</td>
           	<td>
				<input type="hidden" value="${userId}" name="model.picOfUser.id"/>
           		<input id="fileId" type="file" name="attch" />
           		<font color="red">【建议图片大小400*300或此大小的倍数】*</font>
           	</td>
		</tr>
		<tr>
			<td  align="right">
			           内容：</td>
			<td class="simple" align="left" colspan="1">
				<textarea rows="3" id="contentId" name="content" cols="50">${pushMessage.content }</textarea>
			</td>
		</tr>
	  </table>
	</fieldset>
		
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
				<s:submit value="保存" cssClass="button" onclick="return saveValidate();"/>&nbsp;&nbsp;
				<s:reset value="重置" cssClass="button"></s:reset> &nbsp;&nbsp;
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