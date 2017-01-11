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
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox1.js"> </script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=86a779fd4d8626be0c0ac346b2f68dc0"></script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<style type="text/css">
 .input{
 	width: 150px;
 }
 .warn{
	color: red;
}
</style>
<title>编辑商家</title>
</head>
<body>
<div class="x-panel" style="width: 100%">
  <div class="x-panel-header">商家信息</div>
  <div>
    <%@ include file="/common/messages.jsp"%>
  </div>
  <div align="center" style="width: 100%">
	<s:form action="cunSave.do" onsubmit="return checkApp()" id="save" validate="true" method="post" enctype ="multipart/form-data">
	  <s:hidden name="model.id" id="uId" />
	  <s:hidden name="selfEdit" />
	  <fieldset style="width: 75%; padding:10px 10px 10px 10px;">
	    <legend>编辑商家信息</legend>
	    <table width="100%" align="center" >
		  <tr>
			  <c:if test="${loginUser.type=='admin'}">
			  <td align="right">所属省市：</td>
			  <td class="simple" align="left">
				  <span id='comboxregionWithTree' style="width: 300px;"></span>
				  <s:hidden id="regionId" name="model.region.id" cssClass="required" />
				  <span id="regionDescn"></span>
				  <font color="red">*</font>
			  </td>
			  </c:if>
		  </tr>
	       <tr>
			<td align="right">商店名称：</td>
			<td class="simple" align="left">
			  <s:textfield name="model.name" cssClass="required" maxlength="40" cssStyle="width:197px;"></s:textfield >
	          <font color="red">*</font>
			</td>
			<td rowspan="18"><div id="Map" class="w w-ie6" style="width:350px; height:400px;"></div></td>
		  </tr>
		  <tr>
			<td align="right" id="reg">注册用户：</td>
			<td class="simple" align="left" id="appId">
				<select id="shopUser" name="model.shopOfUser.id" cssClass="required">
				</select>
			</td>
				  <td align="right" id="reg1">注册用户：</td>
				  <td class="simple" align="left" id="regap1">
					  <input type="hidden" name="model.shopOfUser.id" id="inputShowAPP" value="${model.shopOfUser.id}"/>${model.shopOfUser.loginId}
				  </td>
		  </tr>
			<tr id="proSortIdtr">
				<td align="right" id="merSort">店铺类型：</td>
				<td id="merSortSpan" class="simple" align="left">
					<select name="productSortId" id="proSortId">
					</select>
				</td>
			</tr>
		 	 <tr>
				<td align="right" id="merKinds">商家类别：</td>
				 <input type="hidden" id="merSortNameStr" name="model.merSortNameStr">
				 <input type="hidden" id="merSortStr" name="model.merSortStr">
					<td id="merkindsSpan" class="simple" align="left"></td>
				</tr>
		  <!-- tr>
			<td align="right">身份证号：</td>
			<td class="simple" align="left">
			  <s:textfield name="model.idCard" cssClass="required idCard input" maxlength="18" cssStyle="width:197px;"/>
			  <span id="idCardMsg" class="warn"></span>
			  <font color="red">*</font>
			</td>
		  </tr> -->
		<%--  <tr>
		    <td align="right">登录名称：</td>
			<td class="simple" align="left" colspan="3">
		  	  <s:textfield id="loginId" name="model.loginId" cssClass="required loginId input" maxlength="14" cssStyle="width:197px;"/>
		  	  <span id="loginIdMsg" class="warn"></span>
		  	  <font color="red">*</font>
		  	</td>
		  </tr>--%>
		  <s:if test="model.id == null">
		  <tr>
		    <td align="right">密&nbsp;&nbsp;码：</td>
			<td class="simple" align="left" colspan="3">
		  	  <s:password id="pwd" name="model.password" cssClass="required pwd input" maxlength="14" cssStyle="width:197px;"/>
		  	  <font color="red">*</font>
		  	</td>
		  </tr>
		  <tr>
			<td align="right" >确认密码：</td>
			<td class="simple" align="left" colspan="3">
		  	  <s:password id="repwd" name="model.confirmPwd" cssClass="required repwd input" maxlength="14" cssStyle="width:197px;"/>
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
			<td align="right">手&nbsp;&nbsp;机：</td>
			<td class="simple" align="left">
			  <s:textfield name="model.mobile" cssClass="input" size="20" maxlength="15" cssStyle="width:197px;"/>
			</td>
		  </tr>
		  <tr>
			<td align="right">电&nbsp;&nbsp;话：</td>
			<td class="simple" align="left">
			  <s:textfield name="model.phone"  cssClass="input" maxlength="15" cssStyle="width:197px;"/>
			</td>
		  </tr>
		  	<tr>
					<td align="right">商家图片：</td>
					<td align="left" >
						<c:if test="${model.imageURL == null || model.imageURL == ''}">
							<input id="fileId" type="file" name="attch" />
						</c:if>
						<c:if test="${model.imageURL != null && model.imageURL != ''}">
							<a href="${ctx }/${model.imageURL }"><img alt="${model.name }" src="${ctx }/${model.imageURL }" width="137" height="75"/></a>
							&nbsp;&nbsp;<input type="button" value="修改图片" class="button" onclick="updateImageUI();"/>
						</c:if>
						<input type="hidden" name="model.imageURL" value="${model.imageURL }"/>
						<div id="imageId" style="display:none"><input id="fileId" type="file" name="attch" /></div>
						<font color="red">【建议图片大小180*180或此大小的倍数】*</font>
					</td>
				</tr>
		   <tr>
			<td align="right">审&nbsp;&nbsp;核：</td>
			<td class="simple" align="left">
			<c:if test="${model.ifRecommend=='1' }">
			通过：<input type="radio" value="1" name="model.ifRecommend" checked="checked"/>&nbsp;&nbsp;&nbsp;&nbsp; 不通过：<input type="radio" value="0" name="model.ifRecommend" />
		  	</c:if>
		  	<c:if test="${model.ifRecommend!='1'}">
				通过：<input type="radio" value="1" name="model.ifRecommend" />&nbsp;&nbsp;&nbsp;&nbsp; 不通过：<input type="radio" value="0" name="model.ifRecommend" checked="checked"/>
		  	</c:if>
		  </tr>
			<tr>
				<td align="right">审核意见：</td>
				<td class="simple" align="left">
					<s:textfield name="model.shIea"  cssClass="required input"  id="shIea" maxlength="15" cssStyle="width:197px;"/>
					<font color="red">*</font>
				</td>
			</tr>
		   <tr>
			<td align="right">经&nbsp;&nbsp;度：</td>
			<td class="simple" align="left">
			  <s:textfield name="model.locX"  cssClass="required input"  id="locX" maxlength="15" cssStyle="width:197px;"/>
			  <font color="red">*</font>
			</td>
		  </tr>
		   <tr>
			<td align="right">纬&nbsp;&nbsp;度：</td>
			<td class="simple" align="left">
			  <s:textfield name="model.locY" cssClass="required input" id="locY"  maxlength="15" cssStyle="width:197px;"/>
			  <font color="red">*</font>
			</td>
		  </tr>
		  <!-- tr>
			<td align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
			<td class="simple" align="left">
			  <s:textfield name="model.zip" size="6" maxlength="6" cssStyle="width:197px;"/>
			</td>
		  </tr> -->
		  <tr>
			<td align="right">地&nbsp;&nbsp;址：</td>
			<td class="simple" align="left">
			  <s:textfield name="model.address" id="address" size="50" cssStyle="width:400px;"/>
			</td>
		  </tr>
		 <%-- <tr>
			<td align="right">开&nbsp;户&nbsp行：</td>
					<td align="left" colspan="3">
				      <span id='comboxmerBankWithTree' style="width: 145px;"></span>
				      <s:hidden id="bankSortId" name="model.bankSort.id" cssClass="bankSortCheck"/>
			        <span id="proTip"></span><font color="red">*</font>
					</td>
		  </tr>
		  <tr>
			<td align="right">银行卡号：</td>
			<td class="simple" align="left">
			  <s:textfield name="model.yhkh" size="50" cssStyle="width:400px;"/>
			</td>
		  </tr>
		  <tr>
			<td align="right">商家描述：</td>
			<td class="simple" align="left">
			<s:textarea id="model.descn" name="model.descn"></s:textarea>
			</td>
		  </tr>--%>
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
/*function preFckEditor(){
	var fckEditor = new FCKeditor( 'model.descn' ) ;
    fckEditor.BasePath = "${ctx}/scripts/fckeditor/";
    fckEditor.ToolbarSet = 'Default';
    fckEditor.Height = 400;
    fckEditor.ReplaceTextarea();
}*/
var array = new Array();
var nameArray = new Array();
var map;
var addr;
var data=new Object();
var address;
	$(function(){
//		initMerSort();
		getAppUserNotsq1();
		if("${type}"!="edit"){
		getAppUserNotsq();
		}else{
			$("#proSortId").val("${obj.productSort.id}");
			$("#appId").hide();
			$("#reg").hide();
		}
		var id=$.trim($("#uId").val());
		if(isEmpty(id)&&"${loginUser.type}"=="admin"){
			addr="北京";
		}else if(isEmpty(id)&&"${loginUser.type}"=="agent"){
			addr="${loginUser.region.name}";
		}
		else{
			addr="${model.region.name}";
			data.locX="${model.locX}";
			data.locY="${model.locY}";
			address="${model.address}";
		}
		showMap(addr);
		initssss("${obj.region.id}");
		if(!isEmpty(id)){
		map.addOverlay(getMarker(data,"${ctx}/images/amol/base/dqsz.gif",address));
		}
		if(!isEmpty("${model.merSortStr}"&&!isEmpty("${model.merSortNameStr}"))) {
			initArrayAndSort();
		}
});
function initMerSort(parentId){
	$.ajax({
		url:'${ctx}/base/prosort/getByParentId.do?parentId='+parentId,
		type:'post',
		dataType:'json',
		async : false, //默认为true 异步
		error:function(){
			alert('error');
		},
		success:function(data){
			array = new Array();
			nameArray = new Array();
			$("#merkindsSpan").html("");
			if(data!=null)
			{
				for(var i = 0;i<data.length;i++){
					$("#merkindsSpan").append(data[i].name+"<input type='checkbox' value='"+data[i].name+"' onclick='bqStr(this.id)' id="+data[i].id+">");
				}
			}
		}
	});
}
    function initArrayAndSort(){
		var merSortStr = "";
		var merSortName = "";
		array = "${model.merSortStr}".split(",");
		nameArray = "${model.merSortNameStr}".split(",");
		for(var i = 0; i<nameArray.length;i++){
			merSortName = merSortName + nameArray[i]+",";
		}
		$("#merSortNameStr").val(merSortName.substring(0,merSortName.length-1));
		for(var i = 0; i<array.length;i++){
			merSortStr = merSortStr + array[i]+",";
		}
		$("#merSortStr").val(merSortStr.substring(0,merSortStr.length-1));
		for(var i=0;i<array.length;i++){
			$("#"+array[i]).attr("checked",true);
		}
	}
	function isEmpty(_value){
	    return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
	}
	//判断是否为空，为空返回 true 不为空返回false
	function showMap(addr){
		map = new BMap.Map("Map");
		if(isEmpty(addr)){
		map.centerAndZoom(new BMap.Point(data.locX, data.locY), 11);}
		else{
			map.centerAndZoom(addr, 11);
		}
		map.enableScrollWheelZoom();
		map.addEventListener("click", showInfo);
	}
		function getMarker(data, img, message){
			var point = new BMap.Point(data.locX, data.locY);
			var icon = new BMap.Icon(img, new BMap.Size(32, 32));
			var label = new BMap.Label("商家地点", { offset: new BMap.Size(30, -15) });
			var marker = new BMap.Marker(point, { icon: icon });
			var opts = new InfoWindow("商家地点");
			var infoWindow = new BMap.InfoWindow(message, opts);  // 创建信息窗口对象
			marker.setLabel(label);
			marker.addEventListener("click", function () {
	               map.openInfoWindow(infoWindow, point); //开启信息窗口
	      	});
	        return marker;
		}
		function InfoWindow(title) {
	        this.width = 220;
	        this.height = 90;
	        this.title = title;
	        this.enableMessage = false; //设置允许信息窗发送短息
	    }
	function showInfo(e){
		var x = e.point.lng;
		var y = e.point.lat;
		map.clearOverlays();
		var point=new BMap.Point(x, y);
		var marker=new BMap.Marker(point);
		map.addOverlay(marker);
		var gc = new BMap.Geocoder();
		gc.getLocation(point,function(rs){
			var infoWindowTitle = '<div style="font-weight:bold;color:#CE5521;font-size:14px">位置信息</div>';
			var infoWindowHtml = [];
			infoWindowHtml.push("<div style='font-size:13px;font-family:\"Arial\",\"Tahoma\",\"微软雅黑\",\"雅黑\";'>地址：" + rs.address + "</br>");
			infoWindowHtml.push("坐标：[" + x + "," + y +"]</br></br>");
			infoWindowHtml.push("<input type='button' value='使用此位置' class='button' style='width:100%;' onclick='retLocation(\"" +rs.address +"\"," +x+","+y +")' /></div>");
			var infoWindow = new BMap.InfoWindow(infoWindowHtml.join(""), { title: infoWindowTitle});
			marker.openInfoWindow(infoWindow);
		});
	}

	function retLocation(address,locX,locY){
		$("#address").val(address);
		$("#locX").val(locX);
		$("#locY").val(locY);
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
/*
*/
//查找注册的未申请开店的app用户
function getAppUserNotsq(){
	$.ajax({
		url:'${ctx}/appuser/manager/getAppList.do',
		type:'post',
		dataType:'json',
		async : false, //默认为true 异步
		error:function(){
			alert('error');
		},
		success:function(data){
			//shopUser
			$("#shopUser").append("<option value=''>--请选择--</option>");
			for(var i=0;i<data.length;i++){
				$("#shopUser").append("<option value='"+data[i].id+"'>"+data[i].loginId+"</option>");
			}
			$("#inputShowAPP").attr("disabled","disabled");
			$("#regap1").hide();
			$("#reg1").hide();
		}
	});
}
function getAppUserNotsq1(){
	$.ajax({
		url:'${ctx}/base/prosort/prosortTree.do?status=1&type=2',
		type:'post',
		dataType:'json',
		async : false, //默认为true 异步
		error:function(){
			alert('error');
		},
		success:function(data){
			//shopUser
			for(var i=0;i<data.length;i++){
				$("#proSortId").append("<option value='"+data[i].id+"'>"+data[i].text+"</option>");
			}
		}
	});
}
function initssss(regionId){
	$.ajax({
		url:'${ctx}/jiayouset/getByRegionId.do?regionId='+regionId,
		type:'post',
		dataType:'json',
		async : false, //默认为true 异步
		error:function(){
			alert('error');
		},
		success:function(data){
			 array = new Array();
			 nameArray = new Array();
			$("#merkindsSpan").html("");
			if(data!=null)
			{
				for(var i = 0;i<data.length;i++){
					$("#merkindsSpan").append(data[i].name+"<input type='checkbox' value='"+data[i].name+"' onclick='bqStr(this.id)' id="+data[i].id+">");
				}
			}
		}
	});
}
function bqStr(id){
	var merSortStr = "";
	var merSortName = "";
	if($("#"+id).is(":checked")){
		array.push(id);
		nameArray.push($("#"+id).val());
	}else{
		array.remove(id);
		nameArray.remove($("#"+id).val());
	}
	for(var i = 0; i<nameArray.length;i++){
		merSortName = merSortName + nameArray[i]+",";
	}
	$("#merSortNameStr").val(merSortName.substring(0,merSortName.length-1));
	for(var i = 0; i<array.length;i++){
		merSortStr = merSortStr + array[i]+",";
	}
	$("#merSortStr").val(merSortStr.substring(0,merSortStr.length-1));
}
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
/*if("${type}"!="edit"){
function checkApp(){
	if(isEmpty($("#shopUser").val())){
		alert("请选择申请开店的app用户");
		$("#shopUser").focus();
		return false;
	}
	if(isEmpty($("#merSortStr").val())){
		alert("请选择商铺种类");
		return false;
	}

}
}*/
$(function() {
	/*$.validator.addMethod("loginId", function(value, element){
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
	   },"");*/

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