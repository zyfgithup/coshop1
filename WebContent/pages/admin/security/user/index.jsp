<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>
<title>用户管里</title>
<style type="text/css">
.button {
	height: 20px;
	padding: 2px 0px 2px 0px;
	background-color: #D2E0F1;
	border: 1px solid #99BBE8;
	background-image: url("../images/grid/footerBg.gif");
}
</style>
</head>
<body>
<script type="text/javascript">
/**
 * 提交表单
 */
function submitForm(){
	$("#queryForm").submit();
}

function setUserType(type,fxsjb){
	$("#userStatus").val("1");
	$("#userType").val(type);
	$("#fxsjb").val(fxsjb);
	submitForm();
}

function unVerifyUser(){
	$("#userStatus").val("2");
	$("#userType").val("agent");
	submitForm();
}

function unsealUser(formid, msg){
	var checked = false;
	$('input').each(function(i, item) {
		if (item.checked && item.id == 'selectedItems') {
			checked = true;
		}
	});
	if (!checked) {
		alert('请至少选择一个用户');
		return;
	}

	if (confirm(msg)) {
		$('#ec').attr("action", $('#' + formid).attr("action"));		
		//ec.from里面已经包含model.type项,不相信查看源代码
		$('#ec').submit();
	} 
}
</script>
<s:form action="user/remove" id="removeForm"></s:form>
<s:form action="user/unsealUser" id="unsealForm"></s:form>
<div class="x-panel">
  <div class="x-panel-header">用户管理</div>
    <div class="x-toolbar">
      <table width="100%">
        <tr>
          <td height="25" style="border-bottom: 1px dashed #AAC0DD;">
            <input type="button" id="systemButton" class="button" value="全部用户" onclick="setUserType('','')" />&nbsp;&nbsp;
          	<input type="button" id="systemButton" class="button" value="系统用户" onclick="setUserType('system','')" />&nbsp;&nbsp;
	        <!-- input type="button" id="bankButton" class="button" value="银行用户" onclick="setUserType('bank')" />&nbsp;&nbsp;
	        <input type="button" id="companyButton" class="button" value="农资生产商" onclick="setUserType('company')" />&nbsp;&nbsp; -->
	        <input type="button" id="agentButton" class="button" value="区域管理员" onclick="setUserType('agent','agent_level_county')" />&nbsp;&nbsp;
	        <!-- input type="button" id="agentButton" class="button" value="级分销商［村］" onclick="setUserType('agent_level_two')" />&nbsp;&nbsp;
        	<input type="button" id="unVerifyButton" class="button" value="未审核用户" onclick="unVerifyUser()" /> -->
          </td>
           <td style="border-bottom: 1px dashed #AAC0DD;">
             <table align="right" style="padding-right:10px;">  
          	  <tr>
		         <td>
		           <a href="${ctx}/security/user/editNew.do?model.type=${model.type}">
		           <img src="${ctx}/images/icons/add.gif"/> 新建</a></td>
		         <td>
		           <span class="ytb-sep"></span></td>
		         <td>
		           <a href="#" onclick="onRemove({noneSelectedMsg:'请至少选择一个用户.',deleteFormId:'unsealForm', confirmMsg:'确认要启用这些用户吗？'})">
		           <img src="${ctx}/images/exticons/recommend.gif"/> 启用</a></td>
		         <td>
		           <a href="#" onclick="unsealUser('removeForm','确认要禁用所选用户吗?')">
		           <img src="${ctx}/images/icons/delete.gif"/> 禁用</a></td>
		         <td>
		           <span class="ytb-sep"></span></td>
		         <td>
		           <a href="${ctx}/security/user/index.do">
		           <img src="${ctx}/images/exticons/refresh.gif"/> 刷新</a></td>
         	   </tr>
             </table>
           </td>
        </tr>
        <tr>
          <td colspan="2"> 
	        <s:form id="queryForm" action="user/index" theme="simple">
	        	&nbsp;登录名：<s:textfield id="userLoginId" name="model.loginId" size="15"/>&nbsp;&nbsp;
	        	公司名称：<s:textfield id="userName" name="model.name" size="15"/>&nbsp;&nbsp;
	        	状态：<s:select id="userStatus" name="model.status" list='#{"1":"可用","0":"禁用","2":"未审核"}' onchange="submitForm()"/>
	        	<s:hidden id="userType" name="model.type"/>
	        	<s:hidden id="fxsjb" name="model.fxsjb"/>
	        	<s:submit value="查  询" cssClass="button"></s:submit>
	         </s:form>
	        </td>
        </tr>
      </table>
    </div>   
    <div class="x-panel-body">
    <div>
	<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do"
		useAjax="true" doPreload="false"
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="false"	
		classic="false"	
		width="100%" 	
		height="370px"	
		minHeight="370"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status"   
	>
	<ec:row>
	    <ec:column width="40" property="_s" title="选择" sortable="false" style="text-align:center">
	    	<input type="checkbox" name="selectedItems" id="selectedItems" value="${item.id}" class="checkbox"/>
	    </ec:column>
		<ec:column width="130" property="loginId" title="登录名" ellipsis="true"/>
		<ec:column width="180" property="name" title="公司名称" ellipsis="true"/>
		<ec:column width="120" property="mobile" title="手机" />
		<ec:column width="90" property="createTime" title="创建日期" cell="date" format="yyyy-MM-dd" style="text-align:center"/>
		<ec:column width="80" property="type" title="用户类型" cell="com.systop.common.modules.security.user.webapp.cell.UserTypeCell"/>
		<ec:column width="40" property="_status" title="状态" style="text-align:center;">
		  <s:if test="#attr.item.status == 1">
		  	<img src="${ctx}/images/icons/accept.gif" title="可用">
		  </s:if>
		  <s:elseif test="#attr.item.status == 0">
		    <img src="${ctx}/images/grid/clear.gif" title="禁用">
		  </s:elseif>
		  <s:else>
		    <img src="${ctx}/images/icons/help_16.gif" title="未审核">
		  </s:else>
		</ec:column>
		<ec:column width="40" property="_1" title="角色" style="text-align:center" sortable='false' tipTitle="分配角色">
		  <a href="#" onclick="javascript:assignRoles('${item.id}', 1)">
		  <img src="${ctx}/images/icons/flowpic.gif"></a>
		</ec:column>
		<ec:column width="40" property="_2" title="操作" style="text-align:center"  sortable="false">
		   <a href="edit.do?model.id=${item.id}" title="编辑">
		     <img src="${ctx}/images/icons/modify.gif"></a>
		</ec:column>
	</ec:row>
	<ec:extendbar>
	  <div style="display: none;">
	  </div>
	</ec:extendbar>
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