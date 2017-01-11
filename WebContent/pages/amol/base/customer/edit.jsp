<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%request.setAttribute("ctx", request.getContextPath()); %>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>客户信息</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"></script>
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
</script>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">客户信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save.do" id="save" method="post">
	<s:hidden name="model.id" id="customerId"/>
	<fieldset style="width: 75%; padding:10px 10px 10px 10px;">
	<legend>客户信息</legend>
	<table width="100%" align="left">
		<tr>
			<td align="right">地&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
			<td align="left" colspan="3">
		        <span id='comboxWithTree' style="width: 100px;"></span>	            
			    <s:hidden id="regionId" name="model.region.id" cssClass="regionCheck" />
			    <span id="regionDescn"></span>
			    <font color="red">*</font>
			</td>
		</tr>		
		<tr>
			<td align="right" width="100">客户名称：</td>
			<td align="left" colspan="3">
				<s:textfield id="name" name="model.name" maxLength="10" cssStyle="width:197px;" cssClass="required"/>
				<span id="nameDescn"></span>
				<font color="red">*</font>
			</td>
		</tr>
		<tr>
			<td align="right">身份证号：</td>
			<td align="left" colspan="3">
				<s:textfield id="idCard" name="model.idCard" maxLength="18" cssStyle="width:197px;" cssClass="required idCardValidator, IdCard"/>
				<span id="idCardDescn"></span>
				<font color="red">*</font>
			</td>
		</tr>
		<tr>
			<td align="right" width="100">经&nbsp;销&nbsp;商：</td>
			<td align="left">
				<s:textfield name="model.agent.name" id="agent" maxlength="18" cssStyle="width:197px" readOnly="true"/>								    	
			    <input type="button" id="btn1" class="button" value="选择从属分销商" onclick="showAgent()"/>
			    <s:hidden id="agentId" name="model.agent.id"></s:hidden>
			</td>
		</tr>
		<tr>
			<td align="right">客户电话：</td>
			<td align="left" colspan="3">
				<s:textfield id="" name="model.phone" maxLength="15" cssStyle="width:197px;" cssClass="phoneValidator"/>
				<span id="phoneDescn"></span>
			</td>
		</tr>
		<tr>
			<td align="right">客户手机：</td>
			<td align="left" colspan="3">
				<s:textfield id="" name="model.mobile" size="20" maxlength="15" cssStyle="width:197px;" cssClass="mobileValidator"/>
				<span id="mobilex"></span>
			</td>
		</tr>
		<tr>
			<td align="right" width="100" >客户类别：</td>
			<td class="simple">&nbsp; <s:radio list="statusMap"
				name="model.type" cssStyle="border:0px;" /></td>
		</tr>
        <tr>
			<td align="right">传&nbsp;&nbsp;&nbsp;&nbsp;真：</td>
			<td align="left" colspan="3">
				<s:textfield id="" name="model.fax" maxLength="15" cssStyle="width:197px;"/>
			</td>
		</tr>
		<tr>
			<td align="right">电子邮件：</td>
			<td align="left" colspan="3">
				<s:textfield id="emailId" name="model.email" maxLength="50" cssStyle="width:197px;" cssClass="emailValidator"/>
				<span id="emailDescn"></span>
			</td>
		</tr>
		<tr>
			<td align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
			<td align="left" colspan="3">
				<s:textfield id="zipId" name="model.zip" maxLength="6" cssStyle="width:197px;" cssClass="zipValidator"/>
				<span id="zipx"></span>
			</td>
		</tr>
        <tr>
           <td align="right">
           	地&nbsp;&nbsp;&nbsp;&nbsp;址：
           </td>
           <td align="left" colspan="3">
           		<s:textarea id="" name="model.address"  cols="55" rows="4"></s:textarea>
			</td>
        </tr>
        <tr>
           <td align="right">
           	备&nbsp;&nbsp;&nbsp;&nbsp;注：
           </td>
           <td align="left" colspan="3">
           		<s:textarea id="" name="model.remark" cols="55" rows="4"></s:textarea>
			</td>
        </tr>		
	</table>
	</fieldset>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			  <s:submit value="保存" cssClass="button" /> 
			  &nbsp;&nbsp;
			  <s:reset value="重置" cssClass="button" />
			  &nbsp;&nbsp;
			  <input type="button" value="返回" onclick="history.go(-1)" class="button"/> 
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
<script type="text/javascript">
$(function() {
	$.validator.addMethod("regionCheck", function(value, element) {
	    var res;
	    var regionId = document.getElementById('regionId').value;
			if(regionId == null || regionId == '') {
			res = "err";
			document.getElementById('regionDescn').innerHTML = '<font color="red">'+'请您选择地区'+'</font>';
			}else{
				res = "od";
			document.getElementById('regionDescn').innerHTML = '';
			}
	    return res != "err";
	},"");
	
});

$(function() {
	
	$.validator.addMethod("IdCard", function(value, element){
		$("#idCardDescn").html("");
		var exist;
	    $.ajax({
			url: '${ctx}/base/customer/checkIdCard.do',
			type: 'post',
			async : false,
			dataType: 'json',
			data: {"model.id" : $("#customerId").val(),"model.idCard" : value},
			success: function(rst, textStatus){
			exist = rst.exist;
				if (exist) {
						$('#idCardDescn').html('<b>'+value+'</b>已存在!');
		       	}else {
		       	    	$('#idCardDescn').html('');
		       	}
			}
	   });
	    return !exist;
	},"");
	$.validator.addMethod("idCardValidator", function(value, element) {
        
		var res;
        var len = value.length;
		
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
		
        return res != "err";
    },"");
});

$(function() {
	$.validator.addMethod("phoneValidator", function(value, element) {
        var res;
        if(value.length == 0){
        	$("#phoneDescn").html("");
        	return true;
        }else{
			if (value.length < 7){
				res = "err";
				$("#phoneDescn").html("<font color='red'>长度应该大于6</font>");
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

$(function() {
	$.validator.addMethod("mobileValidator", function(value, element) {
        var res;
        if(value.length == 0){
        	$("#mobilex").html("");
        	return true;
        }else{
			if (value.length < 11){
				res = "err";
				$("#mobilex").html("<font color='red'>长度应该大于10</font>");
			}else if (value.length > 20){
				res = "err";
				$("#mobilex").html("<font color='red'>长度应该小于20</font>");
			}else{
				$("#mobilex").html("");
			}
        }
        return res != "err";
    },"");
});

$(document).ready(function() {
	$("#save").validate();
});

$(function() {
	$.validator.addMethod("emailValidator", function(value, element) {
        var res;
        if(value.length == 0){
        	$("#emailDescn").html("");
        	return true;
        }else{
        		var emailRegExp = new RegExp("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
        		if (!emailRegExp.test(value)||value.indexOf('.')==-1){
        			res = "err";
        			$("#emailDescn").html("<font color='red'>该项非必选项，如果要填，请填入正确的邮箱。</font>");
        			return false;
        		}else{
        			$("#emailDescn").html("");
        		}
        }
        return res != "err";
    },"");
});

//提取经销商
function showAgent() {
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog("${ctx}/pages/amol/user/agent/selectorAgent.jsp",null,"dialogWidth=800px;resizable: Yes;");
	    if(cus!=null){
	    	var tab = document.getElementById("agent") ;
			tab.value=cus.name;
			var tab1 = document.getElementById("agentId") ;
			tab1.value=cus.id;
		}
 	}else{
		window.open("${ctx}/pages/amol/user/agent/selectorAgent.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	    this.returnAction=function(cus){
	    	if(cus!=null){
	    		var tab = document.getElementById("agent") ;
	    		tab.value=cus.name;
	    		var tab1 = document.getElementById("agentId") ;
	    		tab1.value=cus.id;
	    	}
		};
 	}
}
</script>
</body>
</html>