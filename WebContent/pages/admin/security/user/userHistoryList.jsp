<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>

<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/meta.jsp" %>
<title></title>
</head>
<body>
<div class="x-panel">
  <div class="x-panel-header">用户登录历史记录</div>
     <div class="x-toolbar">
      <table width="99%">
        <tr>
          <td> 
        <s:form action="/userHistory/userHistoryList.do" theme="simple">
      <table border="0"><tr>  	
	      <td>用户名：<s:textfield name="loginUsername" size="15"/></td>
	      <!--
	      <td>部门:</td>
	         <td width="20"><div id="comboxWithTree" class="required"></div>
				<s:hidden name="deptId" id="deptId"></s:hidden></td>
	      -->
	         <td>&nbsp; 时间：
      <input type="text" id="startDate" name="startDate" value='${startDate}'
          onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate"  readonly="readonly"/>      
      <!-- 选择查询结束时间 --> 
     至
        <input type="text" id="endDate" name="endDate" value='${endDate}'
          onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate"  readonly="readonly"/> </td>     
	       <td>&nbsp;&nbsp;<s:submit value="查询" cssClass="button"></s:submit></td> 
         </tr></table>
         </s:form>
        </td>
        
        </tr>
      </table>
    </div>     
    <div class="x-panel-body"> 
	<ec:table items="items" 
	var="item" 
	retrieveRowsCallback="limit"
	sortRowsCallback="limit" 
	action="userHistoryList.do" 
	useAjax="false"
	doPreload="false" 
	pageSizeList="15,50,100,200" 
	editable="false"
	sortable="false" 
	rowsDisplayed="15" 
	generateScript="true"
	resizeColWidth="false" 
	classic="false" 
	width="100%" 
	height="400px"
	minHeight="400"
	toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>    
		<ec:column width="40" property="_0" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="140" property="user.loginId" title="登录名" />
		<ec:column width="140" property="user.name" title="用户名称" />
		<ec:column width="140" property="user.type" title="用户类型" cell="com.systop.common.modules.security.user.webapp.cell.UserTypeCell"/>
		<ec:column width="160" property="loginTime" title="登录时间" cell="date" format="yyyy-MM-dd HH:mm" />
		<ec:column width="200" property="loginIp" title="登录地址" />
		
	</ec:row>
	</ec:table>
	</div>
</div>

</body>
</html>