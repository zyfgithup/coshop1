<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>商品信息管理</title>
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
		   /*var tab = document.getElementById("sname") ;
		   tab.value=cus.name;
		   var tab2 = document.getElementById("code") ;
		   tab2.value=cus.code;
		   var tab3 = document.getElementById("name") ;
		   tab3.value=cus.name;
		   var tab4 = document.getElementById("stardard") ;
		   tab4.value=cus.stardard;
		   
		   //var tab5 = document.getElementById("unitname") ;
  		   //tab5.value=cus.unitname;
  		   //var tab6 = document.getElementById("unitId") ;
  		   //tab6.value=cus.unitid;
		   
  		   pstree.comboxTree.setValue(cus.regionname);*/
		   //document.getElementById("sname").readOnly="true";
		 }
	}else{
		window.open("${ctx}/pages/amol/base/product/product_template/selector.jsp?regionId="+regionId,"","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	    this.returnAction=function(cus){
	    	if(cus!=null){
	    	   var tab1 = document.getElementById("sid") ;
		  	   //tab1.value=cus.id;
		  	   window.location.href="${ctx}/base/product/distributor/templateProductUI.do?model.id="+cus.id;
	    		
	    	   /*var tab = document.getElementById("sname") ;
	  		   tab.value=cus.name;
	  		   var tab2 = document.getElementById("code") ;
	  		   tab2.value=cus.code;
	  		   var tab3 = document.getElementById("name") ;
	  		   tab3.value=cus.name;
	  		   var tab4 = document.getElementById("stardard") ;
	  		   tab4.value=cus.stardard;
	  		   
	  		   //var tab5 = document.getElementById("unitname") ;
	  		   //tab5.value=cus.unitname;
	  		   //var tab6 = document.getElementById("unitId") ;
	  		   //tab6.value=cus.unitid;
	  		   //alert(cus.unitname+"  "+cus.unitid);
	  		   //alert(cus.prosortName+"  "+cus.prosortId);
	  		   pstree.comboxTree.setValue(cus.prosortName);*/
	    	   //document.getElementById("sname").readOnly="true";
	    	 }
		};
	}
}


</script>

</head>
<body>
<div class="x-panel">
<div class="x-panel-header">编辑商品信息</div>
<div class="x-toolbar">
<table width="100%">
	<tr>
		<td align="right">
		<table>
			<tr>
				<td><a href="${ctx}/base/product/distributor/indexPlatformProduct.do"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>&nbsp;返回&nbsp;&nbsp;</a></td>
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
			<table width="100%" height="50%" align="center">
				<tr>
					<td align="right">所属商家：</td>
					<td align="left" colspan="3">
						<select id="userSelect" name="selectUser">
						</select>
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
						name="model.code" cssStyle="width:145px;" />
					</td>
				</tr>
				<tr>
					<td align="right">商品名称：</td>
					<td align="left" ><s:textfield id="name" cssStyle="width:300px;"
						name="model.name" cssClass="required" /><font color="red">&nbsp;*</font>
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
					<td align="right">商品条码：</td>
					<td align="left" >
						<s:textfield id="barcode"
							name="model.barcode"  cssStyle="width:145px;"/>
				</tr>
				<!-- <tr>
					<td align="right">进&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;价：</td>
					<td align="left" ><s:textfield id="inprice"
						name="model.inprice" cssClass="required" cssStyle="width:145px;text-align:right;"
						onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')" onblur="RegCheck(this.id)"/>
						<font color="red">元/基本单位*</font></td>
				</tr> -->
				
				<tr>
					<td align="right">市&nbsp;场&nbsp;价：</td>
					<td align="left" >
						<s:textfield id="originalPrice"
							name="model.originalPrice" cssStyle="width:145px;text-align:right;" onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')" onblur="RegCheck(this.id)"/>
						<font color="red">元/基本单位*</font>
					</td>
				</tr>
				<tr>
					<td align="right">售&nbsp;&nbsp;价：</td>
					<td align="left" >
						<s:textfield id="presentPrice"
							name="model.presentPrice" cssStyle="width:145px;text-align:right;" onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')" onblur="RegCheck(this.id)"/>
						<font color="red">元/基本单位*</font>
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
			</table>
			</fieldset>
			<table width="100%" style="margin-bottom: 10px;">
				<tr>
					<td colspan="2" align="center" class="font_white">
						<s:submit value="保存" cssClass="button"></s:submit>&nbsp;&nbsp;
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
	$(function(){
		initMerSelect();
	})
	function initMerSelect(){
		$.ajax({
			url:'${ctx}/user/agent/getAllMerchat.do',
			type:'post',
			dataType:'json',
			async : false, //默认为true 异步
			error:function(){
				alert('error');
			},
			success:function(data){
				$("#userSelect").html("");
				if(data!=null)
				{
					for(var i = 0;i<data.length;i++){
						$("#userSelect").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
					}
				}
			}
		});
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