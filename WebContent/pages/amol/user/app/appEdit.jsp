<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>

<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>

<style type="text/css">
 .input{
 	width: 150px;
 }
 
 .warn{
	color: red;
}
</style>


<title>编辑app用户</title>
</head>
<body>
<div class="x-panel" style="width: 100%">
  <div class="x-panel-header">app用户信息</div>
  <div>
    <%@ include file="/common/messages.jsp"%>
  </div>
  <div align="center" style="width:100%">
	<s:form action="save.do" id="save" validate="true" method="post">
	  <s:hidden name="model.id" id="uId" />
	  <s:hidden name="selfEdit" />
	  <fieldset style="width: 75%; padding:10px 10px 10px 10px;">
	    <legend>编辑app用户信息</legend>
	    <table width="100%" align="center" >
		  <tr>
			<td align="right">所属地区：</td>
			<td class="simple" align="left">
		        <span id='comboxWithTree' style="width: 300px;"></span>	            
			    <s:hidden id="regionId" name="model.region.id" cssClass="required" />
			    <span id="regionDescn"></span>
			    <font color="red">*</font>
			</td>
		  </tr>
		   <tr>
		    <td align="right">姓&nbsp;&nbsp;名：</td>
			<td class="simple" align="left" colspan="3">
		  	  <s:textfield id="name" name="model.name" readonly="true"  maxlength="14" cssStyle="width:197px;"/>
		  	  <span id="loginIdMsg" class="warn"></span>
		  	</td>
		  </tr>
		  <tr>
		    <td align="right">用&nbsp;户&nbsp;名：</td>
			<td class="simple" align="left" colspan="3">
		  	  <s:textfield id="loginId" name="model.loginId" readonly="true"  cssClass="required loginId input" maxlength="14" cssStyle="width:197px;"/>
		  	  <span id="loginIdMsg" class="warn"></span>
		  	  <font color="red">*</font>
		  	</td>
		  </tr>
		  <!-- tr>
			<td align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
			<td class="simple" align="left">
			  <s:textfield name="model.zip" size="6" maxlength="6" cssStyle="width:197px;"/>
			</td>
		  </tr> -->
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
$(document).ready(function() {
	$("#save").validate();
});

var uid = $("#uId").val();
if (uid != null && uid.length > 0){
	defPass = "*********";
    $('#pwd').val(defPass);
    $('#repwd').val(defPass);
}

Ext.onReady(function() {
	var region = '${loginUser.region }';
	var paramet;
	if(null != region && "" != region){
		paramet = '${loginUser.region.id }';
	}else{
		paramet = '<%=RegionConstants.HBS_ID %>';
	}
	var pstree = new RegionTree({
		el : 'comboxWithTree',
		target : 'regionId',
		//emptyText : '选择地区',
		comboxWidth : 260,
    	treeWidth : 255,
		url : '${ctx}/admin/region/regionTree.do?regionId='+paramet,
		defValue : {id:'${model.region.id}',text:'${model.region.name}'}
	});
	pstree.init();	
	
});
	
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