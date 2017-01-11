<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%request.setAttribute("ctx", request.getContextPath()); %>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>供应商信息</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox1.js"></script>
<script type="text/javascript">
var pstree = new RegionTree({
	el : 'comboxWithTree',
	target : 'regionId',
	//emptyText : '选择地区',
	comboxWidth : 200,
	treeWidth : 195,
	url : '${ctx}/admin/region/regionTree.do',
	defValue : {id:'${model.region.id}',text:'${model.region.name}'}
});
Ext.onReady(function() {
	pstree.init();	
	
});
/**
 * 选择供应商
 */
function supper(){
	var regionId=$('#regionId').val();
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog("${ctx}/pages/admin/security/user/selector.jsp?regionId="+regionId,null,"dialogWidth=800px;resizable: Yes;");
		if(cus!=null){
		   var tab = document.getElementById("sname") ;
		   tab.value=cus.name;
		   var tab1 = document.getElementById("sid") ;
		   tab1.value=cus.id; 
		   var tab2 = document.getElementById("regionId") ;
		   tab2.value=cus.regionid;
		   var tab3 = document.getElementById("phone") ;
		   tab3.value=cus.phone;
		   var tab4 = document.getElementById("mobile") ;
		   tab4.value=cus.mobile;
		   var tab5 = document.getElementById("address") ;
		   tab5.value=cus.address;
		   pstree.comboxTree.setValue(cus.regionname);	
		   //document.getElementById("sname").readOnly="true";
		 }
 	}else{
		window.open("${ctx}/pages/admin/security/user/selector.jsp?regionId="+regionId,"","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	    this.returnAction=function(cus){
	    	if(cus!=null){
	    	   var tab = document.getElementById("sname") ;
	  		   tab.value=cus.name;
	  		   var tab1 = document.getElementById("sid") ;
	  		   tab1.value=cus.id; 
	  		   var tab2 = document.getElementById("regionId") ;
	  		   tab2.value=cus.regionid;
	  		   var tab3 = document.getElementById("phone") ;
	  		   tab3.value=cus.phone;
	  		   var tab4 = document.getElementById("mobile") ;
	  		   tab4.value=cus.mobile;
	  		   var tab5 = document.getElementById("address") ;
	  		   tab5.value=cus.address;
	  		   pstree.comboxTree.setValue(cus.regionname);	
	    	   //document.getElementById("sname").readOnly="true";
	    	 }
		};
 	}
}
</script>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">供应商信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save.do" id="save" validate="true" method="post">
	<s:hidden name="model.id" />
	<fieldset style="width: 85%; padding:10px 10px 10px 10px;">
	<legend>供应商信息</legend>
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
			<td align="right" width="100">名&nbsp;&nbsp;&nbsp;&nbsp;称：</td>
			<td align="left" colspan="3">
				<s:textfield id="sname" name="model.name" maxLength="50" cssStyle="width:197px;" cssClass="required, nameValidator"/>
				<span id="nameDescn"></span>
				<font color="red">*</font>
				<!-- input type="button" id="btn1" class="button" value="选择供应商" onclick="supper()"/> -->
				<s:hidden id="sid" name="model.sid" />
				<font color="red">【注：请输入供应商全称！】</font>
			</td>
		</tr>
		<tr>
			<td align="right">联系电话：</td>
			<td align="left" colspan="3">
				<s:textfield id="phone" name="model.phone" maxLength="15" cssStyle="width:197px;" cssClass="required, phoneValidator"/>
				<span id="phoneDescn"></span>
				<font color="red">*</font>
			</td>
		</tr>
		<tr>
			<td align="right">手&nbsp;&nbsp;&nbsp;&nbsp;机：</td>
			<td align="left" colspan="3">
			  <s:textfield id="mobile" name="model.mobile" size="20" maxlength="15" cssStyle="width:197px;"/>
			</td>
		</tr>
		<tr>
			<td align="right">传&nbsp;&nbsp;&nbsp;&nbsp;真：</td>
			<td align="left" colspan="3">
				<s:textfield id="" name="model.fax" maxLength="20" cssStyle="width:197px;"/>
			</td>
		</tr>
		<tr>
			<td align="right">电子邮件：</td>
			<td align="left" colspan="3">
				<s:textfield id="emailId" name="model.email" maxLength="50" cssStyle="width:197px;" cssClass="emailValidator"/>
				<span id="emailx"></span>
			</td>
		</tr>
		<tr>
			<td align="right">企业法人：</td>
			<td align="left" colspan="3">
				<s:textfield id="" name="model.gysfr" maxLength="20" cssStyle="width:197px;"/>
			</td>
		</tr>
		<tr>
			<td align="right">开&nbsp;户&nbsp;行：</td>
			<td align="left" colspan="3">
				<s:textfield id="" name="model.khh" maxLength="50" cssStyle="width:197px;"/>
			</td>
		</tr>
		<tr>
			<td align="right">账&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td align="left" colspan="3">
				<s:textfield id="" name="model.zh" cssStyle="width:197px;"/>
			</td>
		</tr>
        <tr>
           <td align="right">
           	地&nbsp;&nbsp;&nbsp;&nbsp;址：
           </td>
           <td align="left" colspan="3">
           		<s:textarea id="address" name="model.address" cols="55" rows="4" ></s:textarea>
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
				<input class="button" id="save" type="button" value="保存" onclick="return saveValidate();"/>			  
			  &nbsp;
			  <s:reset value="重置" cssClass="button" />
			  &nbsp;
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
			if(regionId.length == 0) {
			res = "err";
			document.getElementById('regionDescn').innerHTML = '<font color="red">'+'请您选择地区'+'</font>';
			}else{
				res = "od";
			document.getElementById('regionDescn').innerHTML = '';
			}
	    return res != "err";
	},"");
	$.validator.addMethod("nameValidator", function(value, element) {
        var res;
		if (value.length < 2){
			res = "err";
			$("#nameDescn").html("<font color='red'>长度应该大于2</font>");
		}else{
			$("#nameDescn").html("");
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
        	$("#emailx").html("");
        	return true;
        }else{
        		var emailRegExp = new RegExp("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
        		if (!emailRegExp.test(value)||value.indexOf('.')==-1){
        			res = "err";
        			$("#emailx").html("<font color='red'>该项如果要填，请填入正确的邮箱。</font>");
        			return false;
        		}else{
        			$("#emailx").html("");
        		}
        }
        return res != "err";
    },"");
});

$(function() {
	$.validator.addMethod("phoneValidator", function(value, element) {
        var res;
		if (value.length < 7){
			res = "err";
			$("#phoneDescn").html("<font color='red'>长度应该大于7</font>");
		}else if (value.length > 20){
			res = "err";
			$("#phoneDescn").html("<font color='red'>长度应该小于 15</font>");
		}else{
			$("#phoneDescn").html("");
		}
        return res != "err";
    },"");
});

//保存前验证
function saveValidate(){
	var regionId = document.getElementById('regionId').value;
	var sid = document.getElementById('sid').value;
	var sname = document.getElementById('sname').value;
	var phone = document.getElementById('phone').value;
	var regionDescn = document.getElementById('regionDescn').outerText.length;
	var nameDescn = document.getElementById('nameDescn').outerText.length;
	var phoneDescn = document.getElementById('phoneDescn').outerText.length;
	var emailx = document.getElementById('emailx').outerText.length;
	if(regionId.length == 0) {
		alert("请您选择地区！");
		return false;
	}
	if(sname.length == 0) {
		alert("请选择或填写供应商名称！");
		return false;
	}
	if(phone.length == 0) {
		alert("请填写联系方式！");
		return false;
	}
	if(regionDescn != 0 || nameDescn != 0 || phoneDescn != 0 || emailx != 0){
		return false;
	}
	if(sid.length > 0){
		Ext.Ajax.request({
			url : '${ctx}/base/supplier/selectSupplier.do',
			params : {
				'sname' : sname,
				'sid' : sid
			},
			method : 'POST',
			success : function(response) {
				var jsonResult = Ext.util.JSON.decode(response.responseText);
		        if (jsonResult.result == 'error') {
		        	alert("请您重新登录系统！");       
			    	return false;
		        }
		        if (jsonResult.result == 'noExist') {
			        alert("您输入的供应商和当前选择的不一致，请您重新输入！");
					document.getElementById('sname').value = "";
					document.getElementById('sid').value = "";
					return false;
				} 
		 	    var frm = document.getElementById('save');
		        frm.action = 'save.do';
		        frm.submit(); 
			}
		});	  
	}else{
		Ext.Ajax.request({
			url : '${ctx}/base/supplier/selectSupplier.do',
			params : {
				'sname' : sname
			},
			method : 'POST',
			success : function(response) {
				var jsonResult = Ext.util.JSON.decode(response.responseText);
		        if (jsonResult.result == 'error') {
		        	alert("请您重新登录系统！");       
			    	return false;
		        }
		        if (jsonResult.result == 'exist') {
		        	 alert("您输入的供应商名字系统中已存在，请选择此供应商！");	   
				     return false;
			    }
		        document.getElementById('sname').value = sname;
		 	    var frm = document.getElementById('save');
		        frm.action = 'save.do';
		        frm.submit();    
			}
		});
		
	}
}

</script>
</body>
</html>