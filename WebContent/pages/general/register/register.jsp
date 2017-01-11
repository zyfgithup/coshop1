<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<link href="${ctx}/styles/style.css" type='text/css' rel='stylesheet'>
<style type="text/css">
.red{
	color: red;
	padding-left: 5px;
}
.bottom{
	border-bottom:1px dashed #3D91B5;
}
.regbutton{
	padding: 2px 5px 2px 5px;
	border: 1px solid #3D91B5;
	background-image: url("${ctx}/images/grid/footerBg.gif");
}
.warn{
	color: red;
}
</style>
<title>用户注册</title>
</head>
<body>
<form id="save" action="${ctx}">
  <table width="600" align="center" cellpadding="2" cellspacing="4" >
  	<tr>
  	  <td style="border-bottom:1px solid #3D91B5;">
  		<span style="color:#3D91B5;">
  		  <b>分销 用户注册</b>
  		</span>
  	  </td>
  	</tr>
  	<tr>
  	 <td class="bottom">
  	    &nbsp;<b>登&nbsp;&nbsp;录&nbsp;&nbsp;名：</b>
  	    <s:textfield id="loginId" name="model.loginId" cssClass="required loginId" size="20"  maxlength="14"/><span class="red">*</span>
  	    &nbsp;&nbsp;&nbsp;&nbsp;<b>所属地区：</b>
	    <span id='comboxWithTree' style="width: 100px;"></span>
	    <s:hidden id="regionId" name="model.region.id" cssClass="required"/>
	    <span class="red">*</span>
  	    <span id="loginIdMsg" class="warn">&nbsp;</span><br>
  	    &nbsp;系统登录名长度不超过14个字符(数字、字母、汉字、下划线和横线)
  	  </td>	  
  	</tr>
  	<tr>
  	 <td class="bottom">
  	    &nbsp;<b>密　　码：</b>
  	    <s:password id="password" name="model.password" cssClass="required password" cssStyle="width:130px" maxlength="14"/><span class="red">*</span>
  	    &nbsp;&nbsp;&nbsp;
  	    <b>确认密码：</b>
  	    <s:password id="confirmPwd" name="model.confirmPwd" cssClass="required confirmPwd" cssStyle="width:130px" maxlength="14"/><span class="red">*</span>
  	    <br>
  	    <div id="passMsg" class="warn" style="margin-left:5px;"></div>
  	    &nbsp;密码长度6～14位，字母区分大小写。密码过于简单的危害
  	  </td>
  	</tr>
  	<tr>
  	 <td>
  	 	&nbsp;<b>公司名称：</b>
  	    <s:textfield id="name" name="model.name" cssClass="required" size="20" maxlength="20"></s:textfield><span class="red">*</span><br>
  	  </td>
  	</tr>
  	<tr>
  	 <td class="bottom">
  	 	&nbsp;<b>身份证号：</b>
  	    <s:textfield id="idCard" name="model.idCard" cssClass="required idCard" size="20" maxlength="18"></s:textfield><span class="red">*</span>&nbsp;
  	    <span id="idCardMsg" class="warn" style="margin-left:5px;"></span><br>
  	    &nbsp;公司法人身份证号，确保身份证号正确，是申请业务的重要依据
  	  </td>
  	</tr>
  	<tr>
  	 <td class="bottom">
  	 	&nbsp;<b>手　　机：</b>
  	    <s:textfield id="mobile" name="model.mobile" size="20" maxlength="15"></s:textfield>
  	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	    <b>固　　话：</b>
  	    <s:textfield name="model.hTel" size="20" maxlength="15"></s:textfield><br>
  	    &nbsp;填写常用的联系方式
  	  </td>
  	</tr>
  	<tr>
  	 <td>
  	 	<b>服务条款：</b><br>
  	 	<iframe src="${ctx}/register/regMemo/view.do" width="500" height="150" scrolling="auto" style="border: 1px solid #C0C0C0;" frameborder="0"></iframe>
  	 	<div style="padding: 5px 0px 5px 0px;">
  	 		点击下面的“<span style="color: green">我接受</span>”，即表示您同意接受上面的
  	 		<a href="${ctx}/register/regMemo/view.do" target="_blank">服务条款</a>；
  	 		<a href="${ctx}/register/regMemo/view.do" target="_blank">可打印版本</a>
  	 	</div>
  	 	<input type="button" onclick="sunmitData()" class="regbutton" value="我接受, 并注册用户">
  	 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	 	<input type="button" onclick="javascript:window.close()" class="regbutton" value="不接受, 关闭">
  	  </td>
  	</tr>
  </table>
 </form>
 
 <script type="text/javascript">
 Ext.onReady(function() {
		var pstree = new RegionTree({
			el : 'comboxWithTree',
			target : 'regionId',
			//emptyText : '选择地区',
			comboxWidth : 200,
	    	treeWidth : 195,
			url : '${ctx}/admin/region/regionTree.do',
			defValue : {id:'${model.region.id}',text:'${model.region.name}'}
		});
		pstree.init();	
		
	});
 
   function sunmitData (){
	   if (validator.form()){
		   $.ajax({
				 url: '${ctx}/register/save.do',
				 type: 'post',
				 async : false,
				 dataType: 'json',
				 data: {
					 'model.region.id': $('#regionId').val(),
					 'model.loginId'   : $('#loginId').val(), 
					 'model.password'  : $('#password').val(),
					 'model.name'      : $('#name').val(),
					 'model.idCard'    : $('#idCard').val(),
					 'model.mobile'    : $('#mobile').val(),
					 'model.hTel'      : $('#hTel').val()
			     },
				 success: function(data, textStatus){
					 if(data.success){
						 alert("用户注册成功,现在登录");
						 window.returnValue = data.loginId;
						 window.close();
					 }else{
						 alert(data.msg);
					 }
				 }
	      	   });
	   }
   }
   var validator = null;
   
   $(function(){
	   $.validator.addMethod("company", function(value, element){
		   return (value != '-1');
	   },"请选择厂商信息");
	   
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
				 data: {"model.loginId" : value},
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
	   
	   $.validator.addMethod("password", function(value, element) {
	  		if(value.length < 6) {
	  			$('#passMsg').html('密码长度不得小于6位');
	  			return false;
	  		}else if (value.length > 14){
	  			$('#passMsg').html('密码长度不得大于14位');
	  			return false;
	  		}else{
	  			$('#passMsg').html('');
		  		return true;
	  		}
	    },"");
	   $.validator.addMethod("confirmPwd", function(value, element) {
	  		if (value != $('#password').val()){
	  			$('#passMsg').html('两次输入的密码不一致');
	  			return false;
	  		}else{
	  			$('#passMsg').html('');
	  			return true;
	  		}
	    },"");
	   $.validator.addMethod("idCard", function(value, element) {
	  		if (value.length != 15 && value.length != 18){
	  			$('#idCardMsg').html('身份证位数错误' + value.length );
	  			return false;
	  		}else{
	  			$('#idCardMsg').html('');
	  			var result;
		      	$.ajax({
		      		url: '${ctx}/security/user/checkIdCard.do',
					type: 'post',
					async : false,
					dataType: 'json',
					data: {"model.idCard" : value},
					success: function(rst, textStatus){
						if (rst.exist) {
							$('#idCardMsg').html('<b>'+value+'</b>已存在!');
		       	    	}else {
		       	    		$('#idCardMsg').html('');
		       	    	}
					}
		      	 });
		      	return result != "exist";
	  		}
	    },"");
	   //为form定义验证
	   validator = $("#save").validate();
   });
 </script>
</body>
</html>