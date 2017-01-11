<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>

<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
	<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>

<title>编辑员工</title>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">员工信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save" id="save" validate="true" method="POST">
	<s:hidden name="model.id" id="uId" />
	<s:hidden name="model.version" />
	<s:hidden name="model.status" />
	<s:hidden name="selfEdit" />
	<fieldset style="width: 60%; padding:10px 10px 10px 10px;">
	<legend>员工信息</legend>
	<table width="100%" align="center">
		<tr>
			<td align="right" width="300px">角色：</td>
			<td>
				<s:select id="rList" list="rList"
						  name="model.role.id" headerKey="" headerValue="请选择"
						  cssStyle="width:145 px;color:#808080;border: 1px solid #808080;"
						  cssClass="required" /></td>
		<tr>
			<td align="right">地区：</td>
			<td class="simple" align="left">
				<span id='comboxregionWithTree' style="width: 300px;"></span>
				<s:hidden id="regionId" name="model.region.id" cssClass="required" />
				<span id="regionDescn"></span>
				<font color="red">*</font>
			</td>
		</tr>
			<td class="simple" align="left" width="300">
				<span id="deptTip"></span>
			    <font color="red">*</font>	  
			</td>
			<td class="simple" align="left">
			&nbsp;  
			</td>
		</tr>	
			
		<tr>
			<td align="right">编&nbsp;&nbsp;号：</td>
			<td class="simple" align="left" colspan="3">
				<s:textfield  id="one" name="model.code" cssClass="required" cssStyle="width:140px;"></s:textfield >
				<font color="red">*</font>
			</td>
			<td class="simple" align="left">  
			</td>
		</tr>
			
		<tr>
			<td align="right">姓&nbsp;&nbsp;名：</td>
			<td class="simple" align="left" colspan="3">
				<s:textfield  id="two" name="model.name" cssClass="required" cssStyle="width:140px;"></s:textfield >
				<font color="red">*</font>	
			</td>
			<td class="simple" align="left">
			     
			</td>
		</tr>
		<tr>
		    <td align="right">用&nbsp;户&nbsp;名：</td>
			<td class="simple" align="left" colspan="3">
		  	  <s:textfield id="loginId" name="model.loginId" cssClass="required loginId" maxlength="14" cssStyle="width:140px;"/>
		  	  <span id="loginIdMsg" class="warn"></span>
		  	  <font color="red">*</font>
		  	</td>
		  </tr>
		  <s:if test="model.id == null">
		  <tr>
		    <td align="right">密&nbsp;&nbsp;码：</td>
			<td class="simple" align="left" colspan="3">
		  	  <s:password id="pwd" name="model.password" cssClass="required pwd input" maxlength="14" cssStyle="width:140px;"/>
		  	  <font color="red">*</font>
		  	</td>
		  </tr>
		  <tr>
			<td align="right" >确认密码：</td>
			<td class="simple" align="left" colspan="3">
		  	  <s:password id="repwd" name="model.confirmPwd" cssClass="required repwd input" maxlength="14" cssStyle="width:140px;"/>
		  	  <font color="red">*</font>
		  	  <div id="passMsg" class="warn" style="margin-left:5px;"></div>
		  	</td>
		  </tr>
		  <script>
			  $(function() {
				  $.validator.addMethod("pwd", function(value, element) {
						return (value.length > 5);
				    },"密码长度少于6位");
					
					$.validator.addMethod("repwd", function(value, element) {
						return (value == $('#pwd').val());
				    },"两次输入的密码不一致");
			  });
		  </script>
		  </s:if>
		<tr>
			<td align="right" >性&nbsp;&nbsp;别：</td>
			<td class="simple" align="left" colspan="3">
				<s:radio list="sexMap" name="model.sex" cssStyle="border:0px;" />
			</td>
		</tr>
		
		<tr>
			<td align="right">身份证号：</td>
			<td class="simple" align="left" colspan="3">
				<s:textfield name="model.idCard" maxlength="18" cssClass="required idCardValidator" cssStyle="width:140px;"/>
				<span id="idCardDescn"></span>
				<font color="red">*</font>
			</td>
		</tr>
		
		<!-- 
		<tr>
	  		<td align="right">雇佣日期：</td>
	  		<td class="simple" align="left"><s:textfield name="model.createTime" 
			    onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate" readonly="true" cssStyle="width:140px;"/>
			</td>
			<td class="simple" align="left">
			    &nbsp;	  
			</td>
		</tr>
		 -->
		<tr>
	  		<td align="right">出生日期：</td>
	  		<td class="simple" align="left" colspan="3"><s:textfield name="model.birthday" 
			    onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate" readonly="true" cssStyle="width:140px;"/>
			</td>
		</tr>
		<!--  
		<tr>
			<td align="right" ><b>学　　历：</b></td>
		   	<td class="simple" align="left">&nbsp;
		   	<s:select list='#{"初中":"初中","高中":"高中","大学":"大学","大专":"大专"}' name="model.degree">
		   	</s:select>
		   	</td>
	  	  </tr>
	  	-->  
	  	
		
		<tr>
			<td align="right">电&nbsp;&nbsp;话：</td>
			<td class="simple" align="left" colspan="3">
				<s:textfield name="model.mobile" cssStyle="width:140px;" maxlength="15" cssClass="phoneValidator"/>
				<span id="phoneDescn"></span>
			</td>
		</tr>
		<tr>
			<td align="right">地&nbsp;&nbsp;址：</td>
			<td class="simple" align="left" colspan="3">
				<s:textfield name="model.address" size="50" />
				<span>&nbsp;</span>
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
	$("#resetAll").click(function(){
		$("#comboxWithTree").html("");
		$("input").empty();
	});
 	/** ready */
	$(document).ready(function() {
		$("#save").validate();
	});
</script>
<script type="text/javascript" src="${ctx}/pages/amol/dept/deptCombox.js"></script>
<script type="text/javascript">
	Ext.onReady(function() {
		var region = '${loginUser.region }';
		var paramet;
		if(null != region && "" != region){
			paramet = '${loginUser.region.id }';
		}else{
			paramet = null;
		}
		var pstree = new RegionTree({
			el : 'comboxregionWithTree',
			target : 'regionId',
			//emptyText : '选择地区',
			comboxWidth : 260,
			treeWidth : 255,
			url : '${ctx}/admin/region/regionTree.do?regionId='+paramet,
			defValue : {id:'${model.region.id}',text:'${model.region.name}'}
		});
		pstree.init();

	});
</script>
<script type="text/javascript">
//部门验证
$.validator.addMethod("deptCheck", function(value, element) {
    var res;
    var deptId = document.getElementById('deptId').value;
		if(deptId == null || deptId == '') {
		res = "err";
		document.getElementById('deptTip').innerHTML = '&nbsp;<font color="red">'+'未选择所属部门'+'</font>';
		}else{
			res = "od";
		document.getElementById('deptTip').innerHTML = '';
		}
    return res != "err";
},"");

//用户名验证
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

//身份证号验证
$(function() {
	$.validator.addMethod("idCardValidator", function(value, element) {
        
		var res;
        var len = value.length;
		if(len>0){
		if (len == 15){
	        re = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{3})$/);
		    $("#idCardDescn").html("");
		}else if (len == 18){
	        re = new RegExp(/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})(\d)$/);   
		    $("#idCardDescn").html("");
		}else{
	    	res = "err";
	    	$("#idCardDescn").html("<font color='red'>请输入15或18位身份证号,您输入的是 "+len+"</font>");
	    }
		}
        return res != "err";
    },"");
});

//电话验证
$(function() {
	$.validator.addMethod("phoneValidator", function(value, element) {
        var res;
        if(value.length>0){
		if (value.length < 7){
			res = "err";
			$("#phoneDescn").html("<font color='red'>长度应该大于7</font>");
		}else if (value.length > 20){
			res = "err";
			$("#phoneDescn").html("<font color='red'>长度应该小于20</font>");
		}else{
			$("#phoneDescn").html("");
		}
        }
        return res != "err";
    },"");
});
</script>

</body>
</html>