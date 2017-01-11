<%@page import="com.systop.common.modules.region.RegionConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>学员信息管理</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
	<script type="text/javascript" src="${ctx}/pages/amol/base/prosort/mersortCombox.js"></script>
	<script type="text/javascript" src="${ctx}/pages/amol/base/prosort/bankSortCombox.js"></script>
<script type="text/javascript" src="${ctx}/pages/amol/base/product/prosortCombox.js"></script>
	<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>

<script type="text/javascript">
function updateImageUI(){
	$("#imageId").toggle();
	//var imageIdObj = document.getElementById("imageId");
	//imageIdObj.innerHTML = "<input id='fileId' type='file' name='attch' />";
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
Ext.onReady(function() {
	var merstree = new ProsortTree({
		el : 'comboxmerWithTree',
		target : 'ssxx',
		//emptyText : '选择商家类型',
		url : '${ctx}/base/prosort/prosortTree.do?status=1&type=2',
		defValue : {
			id : '${model.school.id}',
			text : '${model.school.name}'
		}

	});
	merstree.init();
});
Ext.onReady(function() {
	var merstree = new BanksortTree({
		el : 'comboxxbzyWithTree',
		target : 'xbzy',
		//emptyText : '选择商家类型',
		url : '${ctx}/base/prosort/prosortTree.do?status=1&type=1',
		defValue : {
			id : '${model.xbzy.id}',
			text : '${model.xbzy.name}'
		}

	});
	merstree.init();
});
</script>

</head>
<body>
<div class="x-panel">
<div class="x-panel-header">学员信息管理</div>
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
<s:form action="save" validate="true" method="POST" theme="simple" enctype ="multipart/form-data" >
	<table width="78%" height="50%" align="center">
		<tr>
			<td align="center"><s:hidden name="model.id" />
			<fieldset style="margin: 10px;">
			<table width="100%" height="50%" align="center">
				<tr>
					<td align="right">学员照片：</td>
					<td align="left" >
						<c:if test="${model.imageURL == null || model.imageURL == ''}">
							<input id="fileId" type="file" name="attch" />
						</c:if>
						<c:if test="${model.imageURL != null && model.imageURL != ''}">
							<a href="${ctx }/${model.imageURL }"><img alt="${model.stuName }" src="${ctx }/${model.imageURL }" width="137" height="75"/></a>
							&nbsp;&nbsp;<input type="button" value="修改图片" class="button" onclick="updateImageUI();"/>
						</c:if>
						<input type="hidden" name="model.imageURL" value="${model.imageURL }"/>
						<div id="imageId" style="display:none"><input id="fileId" type="file" name="attch" /></div>
						<font color="red">【建议图片大小180*180或此大小的倍数】*</font>
					</td>
				</tr>
				<tr>
					<td align="right">学员姓名：</td>
					<td align="left"><s:textfield id="code"
						name="model.stuName" cssClass="required"  cssStyle="width:145px;" />
						<font color="red">&nbsp;*</font>
					</td>
				</tr>
				<tr>
					<td align="right">性别：</td>
					<td align="left"> <s:select name="model.stuSex" list='#{"0":"女","1":"男"}' cssStyle="width:132px;" disabled="false"  /><font color="red">&nbsp;*</font>
					</td>
				</tr>
				<td align="right">籍贯：</td>
				<td class="simple" align="left">
					<span id='comboxregionWithTree' style="width: 300px;"></span>
					<s:hidden id="regionId" name="model.region.id" cssClass="regionIdCheck" />
					<span id="regionIdTip"><font color="red">*</font></span>

				</td>
				<tr>
					<td align="right">所属学校：</td>
					<td align="left" colspan="3">
						<span id='comboxmerWithTree' style="width: 145px;"></span>
						<s:hidden id="ssxx" name="model.school.id" cssClass="prosortCheck" />
						<span id="ssxxTip"><font color="red">*</font></span>
					</td>
				</tr>
				<tr>
					<td align="right">系、部、专业：</td>
					<td align="left" colspan="3">
						<span id='comboxxbzyWithTree' style="width: 145px;"></span>
						<s:hidden id="xbzy" name="model.xbzy.id" cssClass="prosortCheck"/>
						<span id="xbzyTip"><font color="red">*</font></span>
					</td>
				</tr>
				<tr>
				<td align="right">宿舍号：</td>
				<td align="left"><s:textfield id="domNum"
											  name="model.domNum" cssClass="required"  cssStyle="width:145px;" />

				</td>
			</tr>
				<tr>
					<td align="right">QQ：</td>
					<td align="left"><s:textfield id="stuQq"
												  name="model.stuQq" cssClass="required"  cssStyle="width:145px;" />
						<span id="qqTip"><font color="red">&nbsp;*</font></span>

					</td>
				</tr>
				<tr>
					<td align="right">微信：</td>
					<td align="left"><s:textfield id="wxNo"
												  name="model.wxNo" cssClass="required"  cssStyle="width:145px;" />
						<span id="wxTip"><font color="red">&nbsp;*</font></span>
					</td>
				</tr>
				<tr>
					<td align="right">手机号：</td>
					<td align="left"><s:textfield id="stuPhone"
												  name="model.stuPhone" cssClass="required"  cssStyle="width:145px;" />
						<span id="stuPhoneTip"><font color="red">&nbsp;*</font></span>

					</td>
				</tr>
				<tr>
					<td align="right">班主任：</td>
					<td align="left"><s:textfield id="bzrName"
												  name="model.bzrName" cssClass="required"  cssStyle="width:145px;" />

					</td>
				</tr>
				<tr>
					<td align="right">联系电话：</td>
					<td align="left"><s:textfield id="relatePhone"
												  name="model.relatePhone" cssClass="required"  cssStyle="width:145px;" />

					</td>
				</tr>
				<tr>
					<td align="right">担任职务：</td>
					<td align="left"><s:textfield id="trzw"
												  name="model.trzw" cssClass="required"  cssStyle="width:145px;" />

					</td>
				</tr>
			</table>
			</fieldset>
			<table width="100%" style="margin-bottom: 10px;">
				<tr>
					<td colspan="2" align="center" class="font_white">
						<s:submit value="保存" cssClass="button" onclick="return yanzheng()"></s:submit>&nbsp;&nbsp;
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
	function yanzheng(){
		var flag = true;
		if(isEmpty($.trim($("#regionId").val()))){
			$("#regionIdTip").html("<font style='color: red;'>籍贯不能为空</font>");
			flag = false;
		}else{
			$("#regionIdTip").html("<font style='color: red;'>*</font>");
		}
		if(isEmpty($.trim($("#ssxx").val()))){
			$("#ssxxTip").html("<font style='color: red;'>所属学校不能为空</font>");
			flag = false;
		}
		else{
			$("#ssxxTip").html("<font style='color: red;'>*</font>");
		}
		if(isEmpty($.trim($("#xbzy").val()))){
			$("#xbzyTip").html("<font style='color: red;'>系部专业不能为空</font>");
			flag = false;
		}else{
			$("#xbzyTip").html("<font style='color: red;'>*</font>");
		}
		if(isEmpty($.trim($("#wxNo").val()))){
			$("#wxTip").html("<font style='color: red;'>学员微信号不能为空</font>");
			flag = false;
		}else{
			$("#wxTip").html("<font style='color: red;'>*</font>");
		}
		if(isEmpty($.trim($("#stuQq").val()))){
			$("#qqTip").html("<font style='color: red;'>学员QQ号不能为空</font>");
			flag = false;
		}else{
			$("#qqTip").html("<font style='color: red;'>*</font>");
		}
		if(isEmpty($.trim($("#stuPhone").val()))){
			$("#stuPhoneTip").html("<font style='color: red;'>学员QQ号不能为空</font>");
			flag = false;
		}else{
			$("#stuPhoneTip").html("<font style='color: red;'>*</font>");
		}
		return flag;
	}
	function isEmpty(_value){
		return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
	}
function RegCheck(id){
	if($("#"+id).val().indexOf(".")==-1){
		//$("#"+id).val("");
	}
	else if($("#"+id).val().split(".")[1].length!=2||$("#"+id).val().split(".")[0].length==0){
		$("#"+id).val("");
	}
	else{}
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
</script>
</body>
</html>