<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>供应商管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>

<script type="text/javascript">
//供应商对象
function suppliers(){
	this.id;
	this.name;
	this.address;
	this.phone;
}

function closeWindow(id,name,address,phone){
	var supplier = new suppliers();
	supplier.id = id;
	supplier.name = name;
	supplier.address = address;
	supplier.phone = phone;
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		window.returnValue = supplier;
		window.close();
	 }else{
		window.parent.close();
		window.parent.opener.returnAction(supplier);
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
        <s:form action="showIndex" theme="simple">
            <s:hidden name="model.region.id"/>
        	供应商名称：<s:textfield name="model.name" size="15"/>&nbsp;&nbsp;
        	 联系电话：<s:textfield name="model.phone" size="15" id="phone"/>&nbsp;&nbsp;
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
	<ec:row ondblclick="closeWindow('${item.id}','${item.name}','${item.address }','${item.phone }')">
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="100" property="name" title="名称" ellipsis="true"/>	
		<ec:column width="80" property="region.name" title="地区" ellipsis="true"/>
		<ec:column width="120" property="address" title="地址" ellipsis="true"/>
		<ec:column width="110" property="phone" title="联系电话" ellipsis="true"/>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>