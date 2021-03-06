<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>商品信息管理【分销商】</title>
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


/**
* 模板商品
*/
function supper(){
	var regionId=$('#regionId').val();
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog("${ctx}/pages/amol/base/product/product_template/selector.jsp?regionId="+regionId,null,"dialogWidth=800px;resizable: Yes;");
		if(cus!=null){
			window.location.href="${ctx}/base/product/distributor/templateProductUI.do?model.id="+cus.id;
		 }
	}else{
		window.open("${ctx}/pages/amol/base/product/product_template/selector.jsp?regionId="+regionId,"","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	    this.returnAction=function(cus){
	    	if(cus!=null){
		  	   window.location.href="${ctx}/base/product/distributor/edit.do?model.id="+cus.id;
	    	 }
		};
	}
}

</script>

</head>
<body>
<div class="x-panel">
<div class="x-panel-header">编辑商品信息【分销商】</div>
<div class="x-toolbar">
<table width="100%">
	<tr>
		<td align="right">
		<table>
			<tr>
				<td><a href="${ctx}/base/product/distributor/myIndex.do"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>&nbsp;返回&nbsp;&nbsp;</a></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-panel-body">
<s:form action="cjzybc" validate="true" method="POST" enctype ="multipart/form-data" theme="simple">
	<table width="78%" height="50%" align="center">
		<tr>
			<td align="center"><s:hidden name="model.id" />
			<fieldset style="margin: 10px;"><legend>编辑商品基本信息【模板】</legend>
			<table width="100%" height="50%" align="center">
				<tr>
					<td align="right">商品模板：</td>
					<td align="left" >
						<s:hidden id="sid" name="productId" /> 
						<s:textfield id="sname" maxLength="50" cssStyle="width:145px;" cssClass="required, nameValidator"/>
						<input type="button" id="btn1" class="button" value="点我啊" onclick="supper()"/>
						<font color="red">【酷炫噢，点击看下，是不是有你想要添加的商品】</font>
					</td>
				</tr>				
				<tr>
					<td align="right">商品类别：</td>
					<td align="left" colspan="3">
						<span id='comboxWithTree' style="width: 145px;"></span>
					      <s:hidden id="prosortId" name="model.prosort.id" cssClass="prosortCheck"/>
				        <span id="proTip"></span><font color="red">*</font>
					</td>
				</tr>		
				<tr>
					<td align="right">商品编码：</td>
					<td align="left"><s:textfield id="code"
						name="model.code" cssStyle="width:145px;"/>
					</td>
				</tr>
				<tr>
					<td align="right">商品名称：</td>
					<td align="left" ><s:textfield id="name" cssStyle="width:300px;"
						name="model.name" cssClass="required"/><font color="red">&nbsp;*</font>
					</td>
				</tr>
				
				<tr>
					<td align="right">副&nbsp;标&nbsp;题：</td>
					<td align="left" ><s:textfield id="subtitle" cssStyle="width:300px;"
						name="model.subtitle" />
					</td>
				</tr>
				
				<tr>
					<td align="right">商品规格：</td>
					<td align="left" ><s:textfield id="stardard" cssStyle="width:145px;"
						name="model.stardard"/>
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
							  cssClass="required"/>
							  <font color="red">*</font>							  
						   </span> 
			              </s:if>
			              <s:else>   
			                 <s:select id="unitsMap" list="unitsMap"
							  name="model.units.id" headerKey="" headerValue="请选择"
							  cssStyle="width:145px;border: 1px solid #808080;"
							  cssClass="required"/>
							  <font color="red">*</font>
			              </s:else>       
					 </td>
				</tr>
				<tr>
					<td align="right">商品条码：</td>
					<td align="left" >
						<s:textfield id="barcode"
							name="model.barcode" cssStyle="width:145px;"/>
				</tr>
				<tr>
					<td align="right">市&nbsp;&nbsp;场&nbsp;&nbsp;价：</td>
					<td align="left" >
						<s:textfield id="originalPrice"
							name="model.originalPrice" cssStyle="width:145px;text-align:right;" onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')" onblur="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')"/>
						<font color="red">元/基本单位*</font>
					</td>
				</tr>
				
				<tr>
					<td align="right">售&nbsp;&nbsp;&nbsp;&nbsp;价：</td>
					<td align="left" >
						<s:textfield id="presentPrice"
							name="model.presentPrice" cssStyle="width:145px;text-align:right;" onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')" onblur="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')"/>
						<font color="red">元/基本单位*</font>
					</td>
				</tr>
				
				<tr>
					<td align="right">商品图片：</td>
					<td align="left" >
							<a href="${ctx }/${model.imageURL }"><img alt="${model.name }" src="${ctx }/${model.imageURL }" width="105" height="105"/></a>
					</td>
				</tr>
				
				<tr>
					<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>		
					<td align="left" colspan="1">
						<textarea id="remark" name="model.remark" cols="50" rows="4" >${model.remark}</textarea>
					</td>
				</tr>				
			</table>
			</fieldset>
			<table width="100%" style="margin-bottom: 10px;">
				<tr>
					<td colspan="2" align="center" class="font_white">
						<s:submit value="保存" cssClass="button"></s:submit>&nbsp;&nbsp;
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
	Ext.onReady(function() {
		var pstree = new ProsortTree({
			el : 'comboxWithTree',
			target : 'prosortId',
			//emptyText : '选择商品类型',
			url : '${ctx}/base/prosort/prosortTree.do?status=1&type=1',
			defValue : {
				id : '${model.prosort.id}',
				text : '${model.prosort.name}'
			}
		});
		pstree.init();

	});
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