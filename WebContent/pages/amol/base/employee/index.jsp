<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>

<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<title>员工管理</title>
<%--<script type="text/javascript" src="${ctx}/pages/amol/dept/deptCombox.js"></script>--%>
	<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
</head>
<body>
<script type="text/javascript">

	function toMenu(){
		window.location.href="${ctx}/base/employee/index.do";
	}
	Ext.onReady(function() {
		var pstree = new RegionTree({
			el : 'comboxWithTree',
			target : 'regionId',
			//emptyText : '选择地区',
			comboxWidth : 200,
			treeWidth : 195,
			url : '${ctx}/admin/region/regionTree.do?regionId=null',
			defValue : {id:'${regionId }',text:'${regionNameCun }'}
		});
		pstree.init();

	});
</script>
<div class="x-panel">
  <div class="x-panel-header">员工管理</div>
    <div class="x-toolbar">
      <table width="100%">
        <tr>
        	<td>
        		<table>
			        <s:form action="index" theme="simple">
			        	<td>
			        		状&nbsp;&nbsp;态：<s:select name="model.status" list='#{"1":"可用","0":"禁用"}' headerKey="" headerValue="全部" cssStyle="width:100px;"/>
			        	</td>
			        	<td align="left">
			        		编号：<s:textfield name="model.code" size="10"/>&nbsp;&nbsp;
			        	</td>
			        	<td>
			        		员工姓名：<s:textfield name="model.name" size="15"/>&nbsp;&nbsp;
			        	</td>
						<td>
							地区：
						</td>
						<td>
							<span id='comboxWithTree'></span>
						</td>
						<td>
							<s:hidden id="regionId" name="regionId"/>
						</td>

			        	<td>
			        		<s:submit value="查询" cssClass="button"></s:submit>
							<s:reset value="重置" cssClass="button" onclick="toMenu()"></s:reset>
			        	</td>
			         </s:form>
		         </table>
		     </td>
	         <td style="padding-right:10px;" align="right">
	           <table>  
	          	  <tr>	         
			         <td><a href="${ctx}/base/employee/edit.do"><img src="${ctx}/images/icons/add.gif"/> 新建员工</a></td>
	         	  </tr>
	            </table>
	          </td>
        </tr>
      </table>
    </div>   
    <div class="x-panel-body">
    <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do"
		useAjax="true"
			  doPreload="false"
			  xlsFileName="用户管理.xls"
		maxRowsExported="10000000" 
		pageSizeList="15,20,50,100"
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
	   <ec:column width="35" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="50" property="code" title="编号" />
		<ec:column width="80" property="name" title="姓名" />
		<ec:column width="80" property="_regionName" title="所在区域" >
			${item.region.parent.parent.parent.name }&nbsp;&nbsp;${item.region.parent.parent.name }&nbsp;&nbsp;${item.region.parent.name }&nbsp;&nbsp;${item.region.name }
		</ec:column>
		<ec:column width="80" property="role.name" title="角色名" />
		<ec:column width="70" property="loginId" title="用户名" style="text-align:center;" />
	    <ec:column width="40" property="sex" title="性别" mappingItem="sexMap" style="text-align:center"/>
		<ec:column width="100" property="mobile" title="联系电话" />	  
		<ec:column width="110" property="address" title="地址" ellipsis="true"/>	  		  
		<ec:column width="50" property="status" title="状态" style="text-align:center">
		  <s:if test="#attr.item.status == 0">
		  	<img src="${ctx}/images/grid/clear.gif" title="禁用">
		  </s:if>
		  <s:else>
		    <img src="${ctx}/images/icons/accept.gif" title="可用">
		  </s:else>
		</ec:column>
		
		<%--<ec:column width="40" property="_1" title="角色" style="text-align:center" sortable='false' tipTitle="分配角色">
		  
		     <a href="#" onclick="javascript:assignRoles('${item.id}', 3)"><img src="${ctx}/images/icons/flowpic.gif"></a>
		  
		</ec:column>	--%>
			
		<ec:column width="210" property="_2" title="操作" style="text-align:center"  sortable="false">
			<a href="removeUser.do?model.id=${item.id}">删除</a> |
		   <s:if test="#attr.item.status == 0">
		   	<a href="view.do?model.id=${item.id}">查看</a> |
		  	<font color="silver">编辑</font>|
		  	<a href="unsealUser.do?model.id=${item.id}">启用</a>	|
		  	<font color="silver">重置密码</font>
		   </s:if>
		   <s:else>
		   	<a href="view.do?model.id=${item.id}">查看</a> | 
		   	<a href="edit.do?model.id=${item.id}">编辑</a>|
		   	<a href="remove.do?model.id=${item.id}">禁用</a> |
		   	<a href="restPass.do?model.id=${item.id}" >重置密码</a>
		   </s:else>
		</ec:column>
	</ec:row>
	</ec:table>
	</div>
	</div>
</div>


<div id="win" class="x-hidden">
    <div class="x-window-header">角色列表</div>
    <div id="role_grid"></div>
</div>
<script type="text/javascript" src="${ctx}/pages/admin/security/user/user.js">
</script>
<div id='load-mask'></div>
</body>
</html>