<%@page import="com.systop.amol.user.AmolUserConstants"%>
<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail,com.systop.common.modules.security.user.model.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>

<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<script type="text/javascript" src="${ctx}/pages/amol/base/prosort/mersortCombox.js"></script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<style type="text/css">
 .input{
 	width: 150px;
 }
 .warn{
	color: red;
}
</style>
<title>编辑返现规则</title>
</head>
<body >
<div class="x-panel" style="width: 100%">
  <div class="x-panel-header">返现规则</div>
  <div>
    <%@ include file="/common/messages.jsp"%>
  </div>
  <div align="center" style="width: 100%">
	<s:form action="save.do" id="save" validate="true" method="post">
	  <s:hidden name="model.id" id="uId" />
	  <fieldset style="width: 75%; padding:10px 10px 10px 10px;">
	    <legend>返现规则编辑</legend>
	    <table align="center" >
		  <tr>
			<td align="right">返现比例：</td>
			<td class="simple" align="left">
			  <s:textfield name="model.fxNum" cssClass="required " size="20" maxlength="15" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" cssStyle="width:197px;"/>
			   <span id="loginIdMsg" class="warn"></span>
		  	 	 <font color="red">%*</font>
			</td>
		  </tr>
			<tr>
				<td align="right">返现类型：</td>
				<td class="simple" align="left">
					<s:select list="#{'0':'充值返现','1':'分拥返现'}" name="model.type" cssClass="required" cssStyle="width:200px;"></s:select>
					<span id="loginIdMsg" class="warn"></span>
					<font color="red">${errorMsg}*</font>
				</td>
			</tr>
		</table>
	  </fieldset>
	  <table width="100%" style="margin-bottom: 10px;">
		<tr>
		  <td align="center" class="font_white">
			<s:submit value="保存" cssClass="button"/>&nbsp;&nbsp; 
			<input type="button" value="返回" onclick="history.go(-1)" class="button"/>
		  </td>
		</tr>
	  </table>
	</s:form>
  </div>
</div>
<script type="text/javascript">
function RegCheck(id){
	if($("#"+id).val().indexOf(".")==-1){
		//$("#"+id).val("");
	}
	else if($("#"+id).val().split(".")[1].length!=2||$("#"+id).val().split(".")[0].length==0){
		$("#"+id).val("");
	}
	else{}
}
	function isEmpty(_value){
	    return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
	}

function updateImageUI(){
	$("#imageId").toggle();
	//var imageIdObj = document.getElementById("imageId");
	//imageIdObj.innerHTML = "<input id='fileId' type='file' name='attch' />";
}
$(document).ready(function() {
	$("#save").validate();
});
var uid = $("#uId").val();
if (uid != null && uid.length > 0){
	defPass = "*********";
    $('#pwd').val(defPass);
    $('#repwd').val(defPass);
}
$(function() {
	$.validator.addMethod("loginId", function(value, element){
		   var reg = new RegExp("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9_\.\-])*$"); 
		   if (!reg.test(value)){//首先判断非法字符
			   $('#loginIdMsg').html('用户名包含非法字符!');
			   return false;
		   }else {//判断用户名是否存在
			   $('#loginIdMsg').html('');
			   var exist;
	      	   $.ajax({
				 url: '${ctx}/security/user/checkName.do',
				 type: 'post',
				 async : false,
				 dataType: 'json',
				 data: {"model.id" : $("#uId").val(),"model.loginId" : value},
				 success: function(rst, textStatus){
					 exist = rst.exist;
					if (exist) {
						$('#loginIdMsg').html('<b>'+value+'</b>已存在!');
	       	    	}else {
	       	    		$('#loginIdMsg').html('');
	       	    	}
				 }
	      	   });
	      	   return !exist;
		   }
		   return true;
	   },"");
	
	$.validator.addMethod("idCard", function(value, element) {
		if (value.length != 15 && value.length != 18){
	    	$("#idCardMsg").html('身份证位数错误' + value.length );
			return false;
		}else{
			$("#idCardMsg").html('');
		}
		return true;
    },"");
});
</script>
</body>
</html>