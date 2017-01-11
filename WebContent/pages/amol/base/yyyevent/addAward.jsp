<%@page import="com.systop.common.modules.region.RegionConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>跟踪记录添加页面</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<script type="text/javascript" src="${ctx}/pages/amol/base/product/prosortCombox.js"></script>

<script type="text/javascript">
function updateImageUI(){
	$("#imageId").toggle();
	//var imageIdObj = document.getElementById("imageId");
	//imageIdObj.innerHTML = "<input id='fileId' type='file' name='attch' />";
}
</script>

</head>
<body>
<div class="x-panel">
<div class="x-panel-header">跟踪记录管理</div>
<div class="x-toolbar">
<table width="100%">
	<tr>
		<td align="right">
		<table>
			<tr>
				<td><a href="${ctx}/base/yyy/index.do"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>&nbsp;返回&nbsp;&nbsp;</a></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-panel-body">
<s:form action="save.do" validate="true" method="POST"  enctype ="multipart/form-data" theme="simple">
	<table width="78%" height="50%" align="center">
		<tr>
			<td align="center">
			<fieldset style="margin: 10px;">
			<table width="100%" height="50%" align="center">
			<input name="eventId" type="hidden" id="eventId" value="${eventId }"/>
			<input name="id" type="hidden" id="model.id" value="${id }"/>
				<tr>
					<td align="right">跟踪日期：</td>
					<td align="left" ><s:textfield id="createTime" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"
												   name="model.createTime" cssClass="required" /><font color="red" id="createTimemsg">&nbsp;*</font>
					</td>
				</tr>
				<tr>
					<td align="right">跟踪描述：</td>
					<td align="left" colspan="1">
						<textarea id="gzContent" name="model.gzContent"  cols="50" rows="4" ></textarea>
					</td>
				</tr>	
			</table>
			</fieldset>
			<table width="100%" style="margin-bottom: 10px;">
				<tr>
					<td colspan="2" align="center" class="font_white">
						<s:submit value="保存" cssClass="button" onclick="return yanzheng()" ></s:submit>&nbsp;&nbsp;
						<s:reset value="重置" cssClass="button" />
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</s:form></div>
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
function clearDom(id){
	$("#"+id+"Msg").html("*");
}
function yanzheng(){
	var flag=true;
	var createTime=$.trim($("#createTime").val());
	if(isEmpty(createTime)){
		flag=false;
		$("#createTimemsg").html("必填");
		$("#createTime").focus();
	}else{
		$("#createTimemsg").html("*");
		flag=true;
	}
	return flag;
}
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#save").validate();
	});
	$.validator.addMethod("prosortCheck", function(value, element) {
	    var res = "";
	    var proId = document.getElementById('prosortId').value;
			if(proId == null || proId == '') {
				res = "err";
				document.getElementById('proTip').innerHTML = '<font color="red">未选择所属类型</font>';
			}else{
				document.getElementById('proTip').innerHTML = '';
			}
	    return res != "err";
	},"");
	$.validator.addMethod("supplierCheck", function(value, element) {
	    var res = "";
	    var supplierId = document.getElementById('supplierId').value;
			if(supplierId == null || supplierId == '') {
				res = "err";
				document.getElementById('supplierTip').innerHTML = '<font color="red">未选择供应商</font>';
			}else{
				document.getElementById('supplierTip').innerHTML = '';
			}
	    return res != "err";
	},"");
	$.validator.addMethod("proMaxCountCheck",function(value, element) {
		var res = "";
		var maxCount = document.getElementById('maxCount').value;
		if(maxCount == null || maxCount == '') {
			res = "err";
			document.getElementById('maxCountTip').innerHTML = '&nbsp;<font color="red">'
					+ '商品上限不允许为空！' + '</font>';
		}else{
			document.getElementById('maxCountTip').innerHTML = "";
		}
		return res != "err";
	}, "");
	$.validator.addMethod("proMinCountCheck",function(value, element) {
		var res = "";
		var maxCount = document.getElementById('maxCount').value;
		var minCount = document.getElementById('minCount').value;
		if(minCount== null || minCount == ''){
			res = "err";
			document.getElementById('minCountTip').innerHTML = '&nbsp;<font color="red">'
					+ '商品下限不允许为空！' + '</font>';
		}else{

			if(parseFloat(maxCount) < parseFloat(minCount)){
				res = "err";
				document.getElementById('minCountTip').innerHTML = '&nbsp;<font color="red">'
						+ '商品上限不允许小于商品下限！' + '</font>';
			}else{
				res = "";	
				document.getElementById('minCountTip').innerHTML = "";
			}
		}
		return res != "err";
	}, "");
	/**
	*选择供应商
	*/
	function supplier(){

		if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
	var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/base/supplier/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
	 		if(cus!=null){
	          var tab = document.getElementById("supname") ;
		      tab.value=cus.name;
		      var tab1 = document.getElementById("supplierId") ;
		      tab1.value=cus.id; 
	 				 }
		}else{//火狐
			window.open(URL_PREFIX+"/pages/amol/base/supplier/selector.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
		     this.returnAction=function(cus){
			    if(cus!=null){
			    	var tab = document.getElementById("supname") ;
		          tab.value=cus.name;
		          var tab1 = document.getElementById("supplierId") ;
		         tab1.value=cus.id; 
			    }
			    }
		  }
			    
	}
	function isEmpty(_value){
	    return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
	}
</script>
</body>
</html>