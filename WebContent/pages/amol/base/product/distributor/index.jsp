<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>商品管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
function remove(id){
	if (confirm("确认要删除商品吗？")){
		window.location.href="${ctx}/base/product/distributor/remove.do?model.id=" + id;
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

<!-- 地区 -->
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">商品管理</div>
<div class="x-toolbar">
	   <table width="100%">
        <tr>
        	<td>
        		<table>
			        <s:form action="index" theme="simple">
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
			        	<c:if test="${loginUser.type=='admin' }">
			        	<td>
			        		地区：
			        	</td>
			        	<td>
			        		<span id='comboxWithRegionTree'></span>	
						</td>
						<td>
							<s:hidden id="regionId" name="regionId"/>
			        	</td></c:if>
			        	<td>
			        		<s:submit value="查询" cssClass="button"></s:submit>
			        	</td>
			         </s:form>
		         </table>
		         <span id="plCtr" style="display: none"><a href="javascript:partCtr()"><img
			src="${ctx}/images/icons/add.gif" />&nbsp;批量删除&nbsp;</a></span>  <a href="${ctx}/base/product/distributor/edit.do"><img
					src="${ctx}/images/icons/add.gif" />&nbsp;新建商品&nbsp;</a></td>
		     </td>
        </tr>
      </table>
	</tr>
</table>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-panel-body">
<!-- 悬浮图片 -->
<div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>
<ec:table items="items" var="item"
	retrieveRowsCallback="limit" sortRowsCallback="limit" action="index.do"
	useAjax="true" doPreload="false" maxRowsExported="10000000"
	pageSizeList="15,20,50,100" editable="false" sortable="false"
	rowsDisplayed="15" generateScript="true" resizeColWidth="true"
	classic="false" width="100%" height="380px" minHeight="380"
	toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
		<ec:column width="30" property="_s" title="No."
			value='${GLOBALROWCOUNT}' sortable="false" style="text-align:center" />
			<ec:column width="50" property="_0" title="选择" sortable="false" style="text-align:center">
	       <input type="checkbox" name="selectedItems" id="${item.id}" onclick="updateArrays(this.id)"  value="${item.id}" class="checkbox"/></ec:column>
		<ec:column width="100" property="_title" title="图片" style="text-align:center">
			 <a href="#"><img alt="${item.name }" src="${ctx }/${item.imageURL }" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" width="26" height="26"/></a>
		</ec:column>
		<ec:column width="200" property="region.name" title="所属地区"  ellipsis="true">
		 	${item.user.region.parent.parent.parent.name }&nbsp;&nbsp;${item.user.region.parent.parent.name }&nbsp;&nbsp;${item.user.region.parent.name }&nbsp;&nbsp;${item.user.region.name }
		 </ec:column>
		<ec:column width="60" property="prosortName" title="商品类别" ellipsis="true">
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
		<ec:column width="60" property="stardard" title="商品规格" ellipsis="true"/>
		<ec:column width="80" property="user.name" title="所属商家" ellipsis="true"/>
		<ec:column width="60" property="units.name" title="基本单位" style="text-align:center"/>
		<ec:column width="60" property="originalPrice" title="市场价" style="text-align:right"/>
		<ec:column width="60" property="presentPrice" title="售价" style="text-align:right"/>
		<ec:column width="60" property="_presentPrice" title="上架状态" style="text-align:right">
			<c:if test="${item.upDownGoodshelf=='0' }">
				已下架
			</c:if>
			<c:if test="${item.upDownGoodshelf=='1' }">
				上架
			</c:if>
		</ec:column>
		<ec:column width="200" property="_1" title="操作"
			style="text-align:center" sortable="false">
			<s:if test="#attr.item.upDownGoodshelf == 0">
				<a href="upOrDownShelf1.do?model.id=${item.id}&model.upDownGoodshelf=1">上架</a>
			</s:if>
			<s:else>
				<a href="upOrDownShelf1.do?model.id=${item.id}&model.upDownGoodshelf=0">下架</a>
			</s:else>
				<a href="edit.do?model.id=${item.id}" title="修改商品">编辑</a> |
				<a href="javascript:remove('${item.id}')">删除</a>
		</ec:column>
	</ec:row>
</ec:table>
</div>
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
Ext.onReady(function() {
	var pstree = new RegionTree({
		el : 'comboxWithRegionTree',
		target : 'regionId',
		//emptyText : '选择地区',
		comboxWidth : 200,
    	treeWidth : 195,
		url : '${ctx}/admin/region/regionTree.do?regionId=null',
		defValue : {id:'${regionId}',text:'${regionName}'}
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
		window.location.href="${ctx}/base/product/distributor/plRemove.do?type=success&strs=" + strs;
	}
}
/*function partCtr(){
	if (confirm("确认要操作这些商品吗？")){
		window.location.href="${ctx}/base/product/distributor/plUpdownjia.do?type=success&strs=" + strs;
	}
}*/
</script>
</body>
</html>