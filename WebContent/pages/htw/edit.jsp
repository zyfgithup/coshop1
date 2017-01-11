<%@page import="com.systop.common.modules.region.RegionConstants"%>
<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,java.util.Set,java.util.Iterator"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/validator.jsp"%>
<title>添加商铺轮播图</title>
<script type="text/javascript">
/**
 * 地区
 */
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
/**
 * 选择商家
 */
function selectBusiness(){
	var result = window.showModalDialog("${ctx}/pages/business/selector.jsp",null,"dialogWidth=1100px;resizable: Yes;");
	if(result != null){
		var pushMessageObj = document.getElementById("businessNameID");
		var businessIdObj = document.getElementById("businessId");
		//var contentIdObj = document.getElementById("contentId");
		pushMessageObj.value = result.name;
		businessIdObj.value = result.id;
		//contentIdObj.value = "";
	}
}
/**
 * 保存前验证
 */
function saveValidate(){
	var titleValue = document.getElementById("titleId").value;
	var contentValue = document.getElementById("contentId").value;
	var fileIdValue = document.getElementById("fileId").value;
	var businessIdValue = document.getElementById("businessId").value;
	
	if("" == titleValue){
		alert("标题名称不能为空！");
		return false;
	}
	if(titleValue.length > 20){
		alert("标题名称不能超过20个字符！");
		return false;
	}
	
	if("" == contentValue){
		alert("添加商铺轮播图不能为空！");
		return false;
	}
	if(contentValue.length > 900){
		alert("推送内容不能超过900个字符！");
		return false;
	}
	if("" != fileIdValue && 0 != fileIdValue.length && "" != businessIdValue && 0 != businessIdValue.length && 0 != businessIdValue){
		alert("图片和商家是互斥的，请选一项填写！");
		return false;
	}
	
	if("" != fileIdValue){
		var arry = fileIdValue.split('.');
		var imagel = arry.length-1;
		if(arry[imagel] != "jpg" && arry[imagel] != "png" && arry[imagel] != "bmp" && arry[imagel] != "jpeg" && arry[imagel] != "gif"
			&& arry[imagel] != "JPG" && arry[imagel] != "PNG" && arry[imagel] != "BMP" && arry[imagel] != "JPEG" && arry[imagel] != "GIF"){
			alert("请上传jpg、png、bmp、jpeg、gif格式的图片");
			return false;
		}
	}
	
	return true;
}

function updateImageUI(){
	$("#imageId").toggle();
	//var imageIdObj = document.getElementById("imageId");
	//imageIdObj.innerHTML = "<input id='fileId' type='file' name='attch' />";
}
</script>
</head>
<body>

<div class="x-panel" style="width: 100%">
<div class="x-panel-header">添加商铺轮播图</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save"  id="save" validate="true" method="POST" enctype ="multipart/form-data">
	<br/>
	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>推送</legend>
	<table width="90%">
		<tr>
			<td align="right">
				 标&nbsp;&nbsp;&nbsp;&nbsp;题：
			</td>
			<td class="simple" align="left">
			    <s:textfield id="titleId" name="model.title" maxLength="20" size="25" cssClass="required" />
			    <input type="hidden" name="pushMessageId" value="${model.id }"/>
			    <s:hidden name="model.status"></s:hidden>
           		<font color="red">*</font>
			</td>
			<td align="right">
				 图&nbsp;&nbsp;&nbsp;&nbsp;片：
			</td>
			<td class="simple" align="left">
			    <c:if test="${model.imageURL == null || model.imageURL == ''}">
					<input id="fileId" type="file" name="attch" />
				</c:if>
				<c:if test="${model.imageURL != null && model.imageURL != ''}">
					<a href="${ctx }/${model.imageURL }"><img alt="${model.title }" src="${ctx }/${model.imageURL }" width="137" height="75"/></a>
					&nbsp;&nbsp;<input type="button" value="修改图片" class="button" onclick="updateImageUI();"/>
				</c:if>
				<input type="hidden" name="model.imageURL" value="${model.imageURL }"/>
				<div id="imageId" style="display:none"><input id="fileId" type="file" name="attch" /></div>
           		<font color="red">*</font>
			</td>
		</tr>
		<!-- tr>
           	<td align="right">结束日期：</td>
           	<td>
           		<input id="endDate" name="endDate" size="20" value='${model.endTime}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
           		【有效时间】
           	</td>
           	<td align="right">
				 商&nbsp;&nbsp;&nbsp;&nbsp;家：
			</td>
			<td class="simple" align="left">
			    <input id="businessNameID" onclick="selectBusiness();" value="${business.name }" size="50"/>
			    <input type="hidden" id="businessId" name="businessId" value="${model.businessId }"/>
			</td>
        </tr> -->
		<tr>
			<td  align="right">
			           内&nbsp;&nbsp;&nbsp;&nbsp;容：</td>
			<td class="simple" align="left">
				<s:textarea id="contentId" name="model.content" rows="3" cols="50" cssClass="required"></s:textarea>
           		<font color="red">*</font>
			</td>
			<td align="right">所属地区：</td>
			<td class="simple" align="left">
		        <span id='comboxWithTree' style="width: 300px;"></span>	            
			    <s:hidden id="regionId" name="model.region.id" cssClass="required" />
			    <span id="regionDescn"></span>
			    <font color="red">*</font>
			</td>
		</tr>
	  </table>
	</fieldset>
		
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
				<s:submit value="保存" cssClass="button" onclick="return saveValidate();"/>&nbsp;&nbsp; 
				<s:reset value="返回" cssClass="button" onclick="javascript:history. back();"/>
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
<script type="text/javascript">
/** ready */
$(document).ready(function() {
	$("#save").validate();
});
</script>
</body>
</html>