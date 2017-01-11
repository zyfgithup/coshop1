<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>商家管理</title>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function refresh() {
	  ECSideUtil.reload('ec');
	}
function remove(id){
    if (confirm("确认要删除该商家吗?")){
	  window.location.href="remove.do?model.id=" + id;
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
<script type="text/javascript" src="${ctx}/pages/amol/base/prosort/mersortCombox.js"></script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
</head>
<body>
  <div class="x-panel">
    <div class="x-panel-header">商家评价</div>
   <!--  <input type="button" value="返回" onclick="history.go(-1)" class="button"/> -->
   <a st href="${ctx}/user/agent/indexCun.do"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>&nbsp;返回&nbsp;&nbsp;</a>
    <div class="x-toolbar">
   </div>   
   <div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>
   <div class="x-panel-body">
     <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
	    action="getValidation.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="商家评价.xls" 
		pageSizeList="15,50,100" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true"
		classic="true" 
		width="100%"
		height="350px"
		minHeight="350"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status">
	   <ec:row>
	     <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		 <ec:column width="125" property="appUser.name" title="评价人" ellipsis="true"/>
		 <ec:column width="125" property="merchantUser.name" title="商店名称" ellipsis="true"/>
		 <ec:column width="105" property="merchantUser.productSort.name" title="商家类型" ellipsis="true"/>
		 <ec:column width="80" property="merchantUser.mobile" title="手机"/>
		 <ec:column width="150" property="merchantUser.address" title="地址" ellipsis="true"/>
		 <ec:column width="150" property="_score" title="评分【星】" ellipsis="true">
		  <a href="#"><img alt="${item.score }" src="${ctx }/images/amol/pingfen/${item.score }.png" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" width="26" height="26"/></a>
		 </ec:column>
		 <ec:column width="300" property="content" title="评价内容" ellipsis="true"/>
	   </ec:row>
      </ec:table>
    </div>
  </div>
</body>
</html>