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
}

function closeWindow(id,name,regionid,regionname,phone,address,mobile){
	var supplier = new suppliers();
	supplier.id = id;
	supplier.name = name;
	supplier.regionid=regionid;
	supplier.regionname=regionname;
	supplier.phone=phone;
	supplier.address=address;
	supplier.mobile=mobile;
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
        <s:form action="user/selectCompany" theme="simple">
            <s:hidden name="model.region.id"/>
        	供应商名称：<s:textfield name="model.name" size="15"/>&nbsp;&nbsp;
        	<s:submit value="查询" cssClass="button"></s:submit>
         </s:form>
        </td>
       </tr>
     </table>
   </div>
<div class="x-panel-body">
<ec:table items="items" var="item" 
        retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="selectCompany.do"
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
	<ec:row ondblclick="closeWindow('${item.id}','${item.name}','${item.region.id}','${item.region.name}','${item.phone}','${item.address}','${item.mobile}')">
	    <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="90" property="name" title="名称" ellipsis="true"/>	
		<ec:column width="110" property="idCard" title="身份证号" style="text-align:center"/>
		<ec:column width="120" property="address" title="地址" ellipsis="true"/>
		<ec:column width="70" property="region.name" title="地区" ellipsis="true"/>
		<ec:column width="100" property="phone" title="联系电话" ellipsis="true"/>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>