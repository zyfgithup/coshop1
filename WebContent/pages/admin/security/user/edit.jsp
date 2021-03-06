<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.systop.common.modules.region.RegionConstants" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/validator.jsp"%>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<style type="text/css">
.item{
	width:700px;
	font-size:12px;
	line-height:25px;
	margin:5px;
	padding:2px 5px;
	border-bottom:1px dashed #99BBE8;
}
.warn{
	color: red;
}
</style>
<title>编辑用户</title>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">用户管理</div>
<s:form action="user/save" id="save" validate="true" method="post">
<fieldset style="width:700px; margin:10px 25px;">
	<legend>用户信息编辑</legend>
	<s:hidden name="model.id" id="uId" />
	<s:hidden name="selfEdit" />
	<div>
		<%@ include file="/common/messages.jsp"%>
	</div>
	<div class="item">
	  <table width="700" border="0" cellpadding="0" cellspacing="0">
	    <tr>
	      <td width="230">
		    &nbsp;<b>用户类别</b>：
		    <s:if test="#attr.model.id != null">
		      <s:select name="model.type" list='#{"system":"系统用户","agent":"区域管理员"}' cssStyle="width:132px;" disabled="true"  />
		    </s:if>
		    <s:else>
		   	  <s:select name="model.type" list='#{"system":"系统用户","agent":"区域管理员"}' cssStyle="width:132px;"/>
		    <font color="red">*</font>
		    </s:else>
	      </td>
	      <td width="310">
		    <div id="region">
		      <table width="310" cellpadding="0" cellspacing="0">
		       <tr>
		        <td width="90" align="right">
		          &nbsp;<b>所属地区</b>：
		        </td>
		        <td width="170">
				  <div id='comboxWithTree'></div>				  
		        </td>
		        <td><font color="red">*</font></td>
		        <td width="50">
		        	<s:hidden id="region_id" name="model.region.id" cssClass="required "  cssStyle="100px"/>
		        </td>		        
		       </tr>
		      </table>
	         </div>
	      </td>
	    </tr>
	  </table>
	</div>
	<s:if test="#attr.model.id != null">
		<div class="item">
			&nbsp;<b>登&nbsp;&nbsp;录&nbsp;&nbsp;名</b>：
		    <s:textfield name="model.loginId" id="loginId" size="20" readOnly="true" />
			<font color="red">*</font><span id="loginIdMsg"></span>
			&nbsp;<font color="red">【注：用户名不可以修改】</font>
		</div>
	</s:if>
	<s:else>
		<div class="item">
			&nbsp;<b>登&nbsp;&nbsp;录&nbsp;&nbsp;名</b>：
		    <s:textfield name="model.loginId" id="loginId" size="20" maxlength="14" cssClass="required loginId loginIdUnique"/>
			<font color="red">*</font><span id="loginIdMsg"></span><br>
			&nbsp;系统登录名长度不超过14个字符(数字、字母、汉字、下划线和横线)
		</div>
	</s:else>
	<div class="item">
		&nbsp;<b>密　　码</b>：
		<s:password name="model.password" id="pwd" size="22" maxlength="14" cssClass="required pwd"/>
		<font color="red">*</font>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<b>确认密码</b>：
		<s:password name="model.confirmPwd" id="repwd" size="20" maxlength="14" cssClass="required repwd"/>
		<font color="red">*</font><br>
		&nbsp;密码长度6～14位，字母区分大小写&nbsp;<span id="pwdmsg" class="warn"></span>
	</div>	
	
	<div class="item">
		&nbsp;<b>公司名称</b>：
		<s:textfield name="model.name" size="20" maxlength="20" cssClass="required"/>&nbsp;<font color="red">*</font>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<b>　性　别</b>：
		<s:radio list="sexMap" name="model.sex" cssStyle="border:0px;" />
	</div>

	<div class="item">
		&nbsp;<b>身份证号</b>：
	  	<s:textfield id="idCard" name="model.idCard" cssClass="idCard" size="20" maxlength="18"></s:textfield>
	  	&nbsp;请输入公司法人身份证号，并确保身份证号正确<br>
	</div>

	<div class="item">
		&nbsp;<b>手　　机</b>：
		<s:textfield name="model.mobile" id="mobile" cssClass="mobileCheck" size="20" maxlength="18"/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;<b>固定电话</b>：
		<s:textfield name="model.hTel" size="20" maxLength="18"/>
	</div>
	
	<div class="item">
		&nbsp;<b>开&nbsp;&nbsp;户&nbsp;&nbsp;行</b>：
		<s:textfield name="model.yhmc" id="yhmc" size="20" maxlength="30"/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;<b>银行卡号</b>：
		<s:textfield name="model.yhkh" size="30" maxLength="30"/>
	</div>
	<div class="item">
		&nbsp;<b>邮　　编</b>：
		<s:textfield name="model.zip" size="6" maxlength="6" /><br>
		&nbsp;<b>通讯地址</b>：
		<s:textfield name="model.address" id="address" size="50" cssStyle="width:400px;"/>
	</div>
</fieldset>
<table width="700" style="margin-bottom: 10px;">
  <tr>
    <td align="center" class="font_white">
      <s:submit value="保存" cssClass="button" onclick="return yanzheng()" />&nbsp;&nbsp;
      <s:reset value="清空" cssClass="button"/>&nbsp;&nbsp; 
      <input type="button" value="返回" onclick="history.go(-1)" class="button"/>
    </td>
  </tr>
</table>
</s:form>

</div>
<script type="text/javascript">

var map;
var addr;
var data=new Object();
var address;
	$(function(){
		var id=$.trim($("#uId").val());
		if(isEmpty(id)){
			addr="石家庄";
		}
		else{
			addr="${model.region.name}";
			data.locX="${model.locX}";
			data.locY="${model.locY}";
			address="${model.address}";
		}
		showMap(addr);
		if(!isEmpty(id)){
		map.addOverlay(getMarker(data,"${ctx}/images/amol/base/dqsz.gif",address));
		}
});
	function isEmpty(_value){
	    return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
	}
	//判断是否为空，为空返回 true 不为空返回false 
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




var uid = $("#uId").val();
if (uid != null && uid.length > 0){
	defPass = "*********";
    $('#pwd').val(defPass);
    $('#repwd').val(defPass);
}

Ext.onReady(function() {
	var rtree = new RegionTree({
		el : 'comboxWithTree',
		target : 'region_id',
		url : '${ctx}/admin/region/regionTree.do?regionId=null',
		defValue : {id:'${model.region.id}', text: '${model.region.name}'}
	});
	rtree.init();	
	
});
function yanzheng(){
	var exist = false;
	var regionName="";
	if(isEmpty($("#uId").val())){
	$.ajax({
		url: '${ctx}/security/user/checkRegionUser.do',
	 	type: 'post',
	 	async : false,
	 	dataType: 'json',
	 	data: {'regionId' : $("#region_id").val()},
	 	success: function(rst, textStatus){
	 		exist = rst.exist;
	 		if(exist){
	 			regionName=rst.regionName;
	 			alert("["+regionName+"]区域负责人已经存在");
	 		}
	 	}
	});
	}
	return !exist; 
}
$(function() {
    //验证用户名
    $.validator.addMethod("loginId", function(value, element) { 
    	var reg = new RegExp("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9_\.\-])*$"); 
	    return reg.test(value); //首先判断非法字符
    }, "包含非法字符");
    
	$.validator.addMethod("loginIdUnique", function(value, element) {
		var exist = false;
		$.ajax({
			url: '${ctx}/security/user/checkName.do',
		 	type: 'post',
		 	async : false,
		 	dataType: 'json',
		 	data: {'model.loginId' : value, 'model.id' : uid},
		 	success: function(rst, textStatus){
		 		exist = rst.exist;
		 	}
		});
		return !exist; 
	},"用户名已存在");
	$.validator.addMethod("pwd", function(value, element) {
		if (value.length < 6){
			$("#pwdmsg").html("密码长度少于6位");
			return false;
  		} else {
  			$("#pwdmsg").html("");
  		}
		return true;
    },"");
	$.validator.addMethod("repwd", function(value, element) {
		if (value != $('#pwd').val()){
			$("#pwdmsg").html("两次输入的密码不一致");
			return false;
		} 
		$("#pwdmsg").html("");
		return true;
    },"");

	
	$.validator.addMethod("idCard", function(value, element) {
		if (value != null && value.length > 0){
			if (value.length != 15 && value.length != 18){
	  			return false;
			}
  		}
		return true;
    },"长度错误");
});

/**
 * 添加jquery验证
 */
$(document).ready(function() {
	$("#save").validate();
});

</script>
</body>
</html>