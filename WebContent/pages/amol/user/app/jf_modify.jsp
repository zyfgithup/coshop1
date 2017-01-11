<%@page import="com.systop.common.modules.region.RegionConstants"%>
<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,java.util.Set,java.util.Iterator"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/validator.jsp"%>
<title>积分兑换</title>
<script type="text/javascript">
function saveValidate(){
	var inputJf = document.getElementById("inputJf").value;
	var integral = document.getElementById("integral").value;
	if(inputJf==null||inputJf==""){
		alert("请填写兑换积分数！")
		;
		return false;
	}
    if(parseInt(inputJf)>parseInt(integral)){
    	alert("该用户积分余额不足！");
    	return false;
    }
	return true;
}
</script>
</head>
<body>
<div class="x-panel" style="width:100%">
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="updateJf"  id="save" validate="true" method="POST" enctype ="multipart/form-data">
	<br/>
	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>积分兑换</legend>
	<table width="90%">
		<tr>
			<td align="right">
				要兑换的积分数：
			</td>
			<td class="simple" align="left">
			    <s:textfield id="inputJf" name="inputJf" cssClass="required" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" />
			    <input type="hidden" id="integral" name="model.integral" value="${model.integral}"/>
			    <input type="hidden" id="id" name="model.id" value="${model.id}"/>
           		<font id="jfsp" color="red">*</font>
			</td>
		</tr>
	  </table>
	</fieldset>
		
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
				<s:submit value="保存" cssClass="button" onclick="return saveValidate();"/>&nbsp;&nbsp; 
				<s:reset value="返回" cssClass="button" onclick="javascript:history. back();"/>
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
</body>
</html>