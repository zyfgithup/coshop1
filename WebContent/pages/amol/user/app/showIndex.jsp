<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>APP用户管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>

<script type="text/javascript">
//商品对象
function appusers(){
	this.id;
	this.name;
	this.loginId;
}
function closeWindow(id,loginId,name){
	var appuser = new appusers();
	appuser.id = id;
	appuser.name = name;
	appuser.loginId = loginId;
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		window.returnValue = appuser;
		window.close();
 	}else{
	    window.parent.close();
		window.parent.opener.returnAction(appuser);
	}
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-toolbar">
     <table width="100%">
       <tr>
       <td> 
        <s:form action="getCheckUsers" theme="simple">
            <input type="hidden" name="userId" value="${userId }"/>
        	用户登录名：<s:textfield name="regionId" size="15"/>&nbsp;&nbsp;
        	<s:submit value="查询" cssClass="button"></s:submit>
         </s:form>
        </td>
       </tr>
     </table>
   </div>
<div class="x-panel-body">
<ec:table items="items" var="item" 
        retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="showIndex.do"
		useAjax="true" doPreload="false"
		maxRowsExported="10000000" 
		pageSizeList="10,20,50,100" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="10"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="407px"	
		minHeight="407"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row ondblclick="closeWindow('${item.id}','${item.loginId}','${item.name}')">
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="100" property="loginId" title="用户登录名"/>
		<ec:column width="100" property="name" title="用户姓名"/>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>