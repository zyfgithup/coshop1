<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>

<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>

<title></title>
<script type="text/javascript">
//供应商对象
function emp(){
this.id;
this.name;
}
function closeWindow(id,name){
var a=new emp();
a.id=id;
a.name=name;
if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
window.returnValue = a;
	window.close();
}else{
	 window.parent.close();
	window.parent.opener.returnAction(a);
	}
}

</script>
</head>
<body>

<div class="x-panel">
    <div class="x-toolbar">
      <table width="99%">
        <tr>
          <td> 
        <s:form action="showIndex" theme="simple">
            <s:hidden name="model.empDept.id"/>
               	员工姓名：<s:textfield name="model.name" size="15"/>&nbsp;&nbsp;
        	<s:submit value="查询" cssClass="button"></s:submit>
         </s:form>
        </td>
         <td align="right">
           
          </td>
        </tr>
      </table>
    </div>   
    <div class="x-panel-body">
    <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="showIndex.do"
		useAjax="true" doPreload="false"
		maxRowsExported="10000000" 
		pageSizeList="20,50,100" 
		editable="false" 
		sortable="true"	
		rowsDisplayed="20"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="410px"	
		minHeight="410"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status"   
	>
	<ec:row  ondblclick="closeWindow('${item.id}','${item.name}')">
	   
		<ec:column width="40" property="_0" title="序号" value="${GLOBALROWCOUNT}" sortable="false" />
		<ec:column width="120" property="dept.name" title="部门" />
		<ec:column width="80" property="code" title="编号" />
		<ec:column width="80" property="name" title="姓名" />
	    <ec:column width="40" property="sex" title="性别" mappingItem="sexMap" style="text-align:center"/>
		<ec:column width="130" property="mobile" title="联系电话" />	  
	</ec:row>
	</ec:table>
	</div>
	</div>
</div>
</body>
</html>