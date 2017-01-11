<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>商品管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>

<script type="text/javascript">
//商品对象
function products(){
	this.id;
	this.name;
	this.code;
	this.stardard;
	this.unitname;
	this.unitid;
	this.inprice;
	this.outprice;
	this.userId;
	this.supplier;
	this.supplierId;
}
function closeWindow(id,name,code,stardard,unitname,unitid,inprice,outprice,userId,supplier,supplierId){
	var product = new products();
	product.id = id;
	product.name = name;
	product.code = code;
	product.stardard = stardard;
	product.unitname = unitname;
	product.unitid = unitid;
	product.inprice = inprice;
	product.userId = userId;
	product.outprice = outprice;
	product.supplier = supplier;
	product.supplierId = supplierId; 
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		window.returnValue = product;
		window.close();
 	}else{
	    window.parent.close();
		window.parent.opener.returnAction(product);
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
            <s:hidden name="model.prosort.id"/>
        	商品名称：<s:textfield name="model.name" size="15"/>&nbsp;&nbsp;
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
	<ec:row ondblclick="closeWindow('${item.id}','${item.name}','${item.code}','${item.stardard}','${item.units.name }','${item.units.id }','${item.inprice }','${item.outprice }','${item.user.id }','${item.supplier.name}','${item.supplier.id}')">
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="100" property="prosort.name" title="商品类别"/>
		<ec:column width="100" property="supplier.name" title="生产商"/>
		<ec:column width="83" property="code" title="商品编码"/>
		<ec:column width="100" property="name" title="商品名称"/>
		<ec:column width="70" property="stardard" title="规格"/>
		<ec:column width="60" property="units.name" title="基本单位"/>
		<ec:column width="65" property="outprice" title="参考出价"/>
		<ec:column width="65" property="inprice" title="参考进价"/>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>