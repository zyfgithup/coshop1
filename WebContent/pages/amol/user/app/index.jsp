<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>APP用户管理</title>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>

<!-- 地区 -->
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox1.js"> </script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
Ext.onReady(function() {
	var pstree = new RegionTree({
		el : 'comboxWithTreeTest',
		target : 'regionId',
		//emptyText : '选择地区',
		comboxWidth : 200,
    	treeWidth : 195,
		url : '${ctx}/admin/region/regionTree.do?regionId=<%=RegionConstants.HBS_ID %>',
		defValue : {id:'${regionId }',text:'${regionNameAppUser }'}
	});
	pstree.init();	
	
});
</script>

</head>
<body>
  <div class="x-panel">
    <div class="x-panel-header">APP用户管理</div>
    <div class="x-toolbar">
	<s:form action="index" theme="simple">
	   <table width="100%">
        <tr>
        	<td>
        		<table>
			        <s:form action="index" theme="simple">
			        	<td>
			        		用户名：
			        	</td>
			        	<td align="left">
			        		<s:textfield id="name"  name="model.name" size="35" cssStyle="height:19px;"/>
			        	</td>
			        	<td>
			        		地区：
			        	</td>
			        	<td>
			        		<span id='comboxWithTreeTest'></span>
						</td>
						<td>
							<s:hidden id="regionId" name="regionId"/>
			        	</td>
			        	<td>
			        		<s:submit value="查询" cssClass="button"></s:submit>
			        	</td>
			         </s:form>
		         </table>
		     </td>
        </tr>
      </table>
	</s:form>
   </div>   
   <div class="x-panel-body">
     <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
	   action="index.do" 
	   useAjax="false"
	   doPreload="false" 
	   pageSizeList="15,20,40" 
	   editable="false"
	   sortable="false" 
	   rowsDisplayed="15"
	   generateScript="true" 
	   resizeColWidth="true" 
	   classic="true" 
	   width="100%"
	   height="398px"
	   minHeight="398"
	   toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	   <ec:row>     
	     <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		 <ec:column width="190" property="region.name" title="所属地区"  ellipsis="true">
		 	${item.region.parent.parent.name }&nbsp;&nbsp;${item.region.parent.name }&nbsp;&nbsp;${item.region.name }
		 </ec:column>
		 <ec:column width="100" property="loginId" title="用户名"  ellipsis="true"/>
		 <ec:column width="100" property="name" title="姓名"  ellipsis="true"/>
		 <ec:column width="200" property="idCard" title="身份证"  ellipsis="true"/>
		 <ec:column width="80" property="integral" title="积分"  ellipsis="true"/>
		 <ec:column width="400" property="_0" title="操作" style="text-align:center" sortable="false">
		 	<a href="appEdit.do?model.id=${item.id}">编辑</a> | 
			<a href="restPass.do?model.id=${item.id}" >重置密码</a> | 
			<a href="edit.do?appId=${item.id}" >积分兑换</a> | 
			<a href="showReceiveAddress.do?model.id=${item.id}" >收货地址</a> | 
			<a href="partnerLink.do?appId=${item.id}" >合伙人</a> | 
			<a href="turnUnderUsers.do?model.id=${item.id}" >合伙人转移</a>
		 </ec:column>
	   </ec:row>
      </ec:table>
    </div>
  </div>
</body>
</html>