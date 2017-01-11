<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>APP用户地址查看</title>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<!-- 地区 -->
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<script type="text/javascript" src="${ctx}/pages/amol/base/prosort/mersortCombox.js"></script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
</head>
<body>
  <div class="x-panel">
    <div class="x-panel-header">APP用户地址</div>
   <!--  <input type="button" value="返回" onclick="history.go(-1)" class="button"/> -->
   <a st href="${ctx}/appuser/manager/index.do"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>&nbsp;返回&nbsp;&nbsp;</a>
    <div class="x-toolbar">
   </div>   
   <div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>
   <div class="x-panel-body">
     <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
	    action="showReceiveAddress.do" 
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
		 <ec:column width="125" property="user.name" title="APP用户姓名" ellipsis="true"/>
		 <ec:column width="125" property="receiveName" title="收货人姓名" ellipsis="true"/>
		 <ec:column width="105" property="address" title="收货人地址" ellipsis="true"/>
		 <ec:column width="80" property="receivePhone" title="手机"/>
		 <ec:column width="150" property="postCode" title="邮编" ellipsis="true"/>
		 <ec:column width="100" property="_ifMrAddr" title="默认地址" ellipsis="true">
		 <c:if test="${item.ifMrAddr=='1' }">
		 是
		 </c:if>
		 <c:if test="${item.ifMrAddr=='0' }">
		 否
		 </c:if>
		 </ec:column>
	   </ec:row>
      </ec:table>
    </div>
  </div>
</body>
</html>