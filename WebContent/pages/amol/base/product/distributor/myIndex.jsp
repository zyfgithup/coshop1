<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>商品管理【分销商】</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
function remove(id){
	if (confirm("确认要删除商品吗？")){
		window.location.href="${ctx}/base/product/distributor/cunRemove.do?model.id=" + id;
	}
}
function showPic(url,event){
	$("#layer").html("<img src='"+url+"' with='300' height='300'>");
	$("#layer").show();
}
function hiddenPic(){
	$("#layer").html("");
	$("#layer").hide();
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">商品管理【分销商】</div>
<div class="x-toolbar">
	   <table width="100%">
        <tr>
        	<td>
        		<table>
			        <s:form action="myIndex" theme="simple">
			        	<td>
			        		商品类型：
			        	</td>
			        	<td align="left">
			        		<div id='comboxWithTree'></div>
							<s:hidden id="prosortId" name="model.prosort.id" cssStyle="display: inline;" />
			        	</td>
			        	<td>
			        		商品名称：
			        	</td>
			        	<td align="left">
			        		<s:textfield name="model.name" size="20" />
			        	</td>
			        	<td>
			        		<s:submit value="查询" cssClass="button"></s:submit>
			        	</td>
			         </s:form>
		         </table>
		     </td>
	         <td style="padding-right:10px;" align="right">
	           <table>  
	          	  <tr>	         
			      <td style="padding-left: 5px; padding-top: 5px; width: 50%"
			align="right">
			<a href="importEdit.do"><img
			src="${ctx}/images/icons/add.gif" />&nbsp;Excel导入&nbsp;<a href="javascript:downLoadMb()">&nbsp;模版下载&nbsp;</a>
			
			<span id="plCtr" style="display: none"><a href="javascript:partCtr()"><img
			src="${ctx}/images/icons/add.gif" />&nbsp;批量操作&nbsp;</a></span><a href="xjEdit.do"><img
			src="${ctx}/images/icons/add.gif" />&nbsp;新建商品&nbsp;</a></td>
	         	  </tr>
	            </table>
	          </td>
        </tr>
      </table>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-panel-body">

<!-- 悬浮图片 -->
<div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>
<ec:table items="items" var="item"
	retrieveRowsCallback="limit" sortRowsCallback="limit" action="myIndex.do"
	useAjax="true" doPreload="false" maxRowsExported="10000000"
	pageSizeList="15,20,50,100" editable="false" sortable="false"
	rowsDisplayed="15" generateScript="true" resizeColWidth="true"
	classic="false" width="100%" height="380px" minHeight="380"
	toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
		<ec:column width="30" property="_s" title="No."
			value='${GLOBALROWCOUNT}' sortable="false" style="text-align:center" />
		<ec:column width="50" property="_0" title="选择" sortable="false" style="text-align:center">
	       <input type="checkbox" name="selectedItems" id="${item.id}" onclick="updateArrays(this.id)"  value="${item.id}" class="checkbox"/>
	    </ec:column>
		<ec:column width="100" property="_title" title="图片" style="text-align:center">
			 <a href="#"><img alt="${item.name }" src="${ctx }/${item.imageURL }" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" width="26" height="26"/></a>
		</ec:column>
		<ec:column width="120" property="prosortName" title="商品类别" ellipsis="true">
			<s:if test="#attr.item.prosort.status == 0">
				<font color="red">${item.prosort.name}</font>
			</s:if>
			<s:else>
				${item.prosort.name}
			</s:else>
		</ec:column>	
		<!-- ec:column width="80" property="code" title="商品编码" ellipsis="true"/>	 -->
		<ec:column width="100" property="name" title="商品名称" ellipsis="true"/>
		<!-- ec:column width="150" property="subtitle" title="副标题" ellipsis="true"/> -->
		<ec:column width="80" property="stardard" title="商品规格" ellipsis="true"/>
		<ec:column width="60" property="units.name" title="基本单位" style="text-align:center"/>
		<ec:column width="80" property="barcode" title="商品条码" ellipsis="true"/>
		<ec:column width="60" property="originalPrice" title="市场价" style="text-align:right"/>
		<ec:column width="60" property="presentPrice" title="售价" style="text-align:right"/>
		<ec:column width="60" property="maxCount" title="剩余数量" style="text-align:right"/>
		<ec:column width="60" property="_presentPrice" title="上架状态" style="text-align:right">
		<c:if test="${item.upDownGoodshelf=='0' }">
		已下架
		</c:if>
		<c:if test="${item.upDownGoodshelf=='1' }">
		上架	
		</c:if>
		</ec:column>
		<ec:column width="180" property="_2" title="操作"
			style="text-align:center" sortable="false">
			<s:if test="#attr.item.upDownGoodshelf == 0">
		    	<a href="upOrDownShelf2.do?model.id=${item.id}&model.upDownGoodshelf=1">上架 |</a> 	    	
			</s:if> 
			<s:else>
				<a href="upOrDownShelf2.do?model.id=${item.id}&model.upDownGoodshelf=0">下架 |</a> 
			</s:else>
			<s:if test="#attr.item.belonging == 0">
				<font color="#999999" title="修改商品">编辑</font> | 
				<font color="#999999" title="删除">删除</font>
			</s:if>
			<s:if test="#attr.item.belonging == 1">
				<a href="xjEdit.do?model.id=${item.id}" title="修改商品">编辑</a> | 
				<a href="javascript:remove('${item.id}')">删除</a>
			</s:if>
		</ec:column>
	</ec:row>
</ec:table></div>
</div>
<script type="text/javascript" src="${ctx}/pages/amol/base/product/prosortCombox.js"></script>
<script type="text/javascript">
var arr = new Array();
var strs="";
Ext.onReady(function() {
	var pstree = new ProsortTree({
		el : 'comboxWithTree',
		target : 'prosortId',
		//emptyText : '选择商品类型',
		url : '${ctx}/base/prosort/prosortTree.do?status=1&type=1',
		defValue : 
			{
				id:'${model.prosort.id}',
				text:'${model.prosort.name}'
			}
	});
	pstree.init();		
});
function updateArrays(id){
	strs="";
	if($("#"+id).is(":checked")){
		arr.push($("#"+id).val());
	}else{
		arr.remove($("#"+id).val());
	}
	for(var i=0;i<arr.length;i++){
		strs+=arr[i]+",";
	}
	if(!isEmpty($.trim(strs))){
		$("#plCtr").show();
	}else{
   		$("#plCtr").hide();
	}
	strs=strs.substring(0,strs.length-1);
}
function isEmpty(_value){
    return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
}
function partCtr(){
	if (confirm("确认要操作这些商品吗？")){
		window.location.href="${ctx}/base/product/distributor/plUpdownjia.do?type=XJSuc&strs=" + strs;
	}
}
</script>
</body>
</html>