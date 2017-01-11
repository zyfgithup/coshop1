<%@page import="com.systop.common.modules.security.user.model.User"%>
<%@page import="com.systop.common.modules.region.RegionConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%> 
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>编辑积分商品</title>
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

<script>
        var UEDITOR_HOME_URL = "/${ctx}/ueditor/utf8-jsp/";//从项目的根目录开始
</script>


</head>
<body>
<div class="x-panel">
<div class="x-panel-header">编辑积分商品</div>
<div class="x-toolbar">
<table width="100%">
	<tr>
		<td align="right">
		<table>
			<tr>
				<td><a href="${ctx}/base/jfproduct/index.do"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>&nbsp;返回&nbsp;&nbsp;</a></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-panel-body">
<s:form action="save" validate="true" method="POST" enctype ="multipart/form-data" theme="simple">
	<table width="78%" height="50%" align="center">
		<tr>
			<td align="center"><s:hidden name="model.id" />
			<fieldset style="margin: 10px;">
			<table width="100%" height="50%" align="center">
			<!-- 1商品名称 图片 描述 积分 1分类 1商品规格 1基本单位 -->
				<tr>
					<td align="right">商品类别：</td>
					<td align="left" colspan="3">
				      <span id='comboxWithTree' style="width: 145px;"></span>
				      <s:hidden id="prosortId" name="model.prosort.id" cssClass="prosortCheck"/>
			        <span id="proTip"></span><font color="red">*</font>
					</td>
				</tr>		
				<tr>
					<td align="right">商品名称：</td>
					<td align="left" ><s:textfield id="name" cssStyle="width:300px;"
						name="model.name" cssClass="required" /><font color="red">&nbsp;*</font>
					</td>
				</tr>
				<tr>
					<td align="right">商品积分：</td>
					<td align="left" ><s:textfield id="integral" cssStyle="width:300px;"
						name="model.integral" cssClass="required"  onkeyup="this.value=this.value.replace(/[^\d]/g,'')"/><font color="red">【注：该商品的积分为整数】*</font>
					</td>
				</tr>
				<tr>
					<td align="right">商品规格：</td>
					<td align="left" ><s:textfield id="stardard" cssStyle="width:145px;"
						name="model.stardard" />
					</td>
				</tr>
				
				<tr>
					<td align="right">基本单位：</td>
					<td align="left" >
					      <s:if test="model.unitsItem.size > 1 ">
					       <span onmousemove="this.setCapture();" onmouseout="this.releaseCapture();" > 
			                 <s:select id="unitsMap" list="unitsMap"
							  name="model.units.id" headerKey="" headerValue="请选择"
							  cssStyle="width:145 px;color:#808080;border: 1px solid #808080;"
							  cssClass="required" />
							  <font color="red">【注：已存在该商品的单位换算关系，请先删除换算关系后再进行修改】*</font>							  
						   </span> 
			              </s:if>
			              <s:else>   
			                 <s:select id="unitsMap" list="unitsMap"
							  name="model.units.id" headerKey="" headerValue="请选择"
							  cssStyle="width:145px;border: 1px solid #808080;"
							  cssClass="required" />
							  <font color="red">【注：请选择该商品的最小包装单位】*</font>
			              </s:else>       
					 </td>
				</tr>
				<tr>
					<td align="right">商品图片：</td>
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
						<font color="red">【建议图片大小400*300或此大小的倍数】*</font>
					</td>
				</tr>
				
				<tr>
					<td align="right">描&nbsp;&nbsp;述：</td>		
					<td align="left" colspan="1">
					
						<script id="container" name="model.remark" type="text/plain">${model.remark }</script>
		
					  	<!-- 配置文件 -->
					  	<script type="text/javascript" src="${ctx}/ueditor/utf8-jsp/ueditor.config.js"></script>
					  	<!-- 编辑器源码文件 -->
					  	<script type="text/javascript" src="${ctx}/ueditor/utf8-jsp/ueditor.all.js"></script>
					  	<!-- 实例化编辑器 -->
					  	<script type="text/javascript">
						  	var ue = UE.getEditor('container');
					  	</script>
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
function RegCheck(id){
	if($("#"+id).val().indexOf(".")==-1){
		$("#"+id).val("");
	}
	else if($("#"+id).val().split(".")[1].length!=2){
		$("#"+id).val("");
	}
	else{}
}
	Ext.onReady(function() {
		var pstree = new ProsortTree({
			el : 'comboxWithTree',
			target : 'prosortId',
			//emptyText : '选择商品类型',
			url : '${ctx}/base/prosort/prosortTree.do?jfType=1&status=1&type=1',
			defValue : {
				id : '${model.prosort.id}',
				text : '${model.prosort.name}'
			}
		});
		pstree.init();

	});
</script>
<script type="text/javascript">
function yanzheng(){
	var exist = false;
	var proSortName="";
	$.ajax({
		url: '${ctx}/security/user/checkProductsJb.do',
	 	type: 'post',
	 	async : false,
	 	dataType: 'json',
	 	data: {'prosortId' : $("#prosortId").val()},
	 	success: function(rst, textStatus){
	 		exist = rst.exist;
	 		if(exist){
	 			proSortName=rst.productSort;
	 			alert("["+proSortName+"]不是第二级商品类别，请选择二级分类");
	 		}
	 	}
	});
	return !exist; 
}
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
</script>
</body>
</html>