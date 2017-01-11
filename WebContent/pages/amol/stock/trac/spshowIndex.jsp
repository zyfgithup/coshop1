<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>库存商品调拨/回调管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>

<script type="text/javascript">
//商品对象
function products(){
	this.pid; //商品ID
	this.name; //商品名称
	this.code; //商品编码
	this.stardard;//商品规格
	this.unit; //商品单位名称(基本)
	this.unitid;//商品单位ID(基本)
	this.count; //实际数量
	this.unitPack; //包装单位
}
function closeWindow(pid,name,code,stardard,unit,unitid,count,unitPack){
	if(count <= 0){
		alert('商品【' + name + '】库存量为:'+ count + '\n不能【调拨/回调】' );
		return;
	}
	var product = new products();
	product.pid = pid;
	product.name = name;
	product.code = code;
	product.stardard = stardard;
	product.unit = unit;  
	product.unitid = unitid;
	product.count = count;
	product.unitPack = unitPack;
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
        <s:form action="stockShowIndex" theme="simple">
            <s:hidden name="model.user.id"/>
            <s:hidden name="model.products.prosort.id"/>
            <s:hidden name="model.storage.id"/>
        	商品名称：<s:textfield name="model.products.name" size="15"/>&nbsp;&nbsp;
        	<s:submit value="查询" cssClass="button"></s:submit>
         </s:form>
        </td>
       </tr>
     </table>
   </div>
<div class="x-panel-body">
<ec:table items="items" var="item" 
        retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="purchaseshowIndex.do"
		useAjax="true" doPreload="false"
		maxRowsExported="10000000" 
		pageSizeList="15,20,50,100" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="407px"	
		minHeight="407"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row ondblclick="closeWindow('${item.products.id}','${item.products.name}','${item.products.code}','${item.products.stardard}','${item.products.units.name}','${item.products.units.id}','${item.count}','${item.unitPack}')">
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="100" property="products.prosort.name" title="商品类别"/>
		<ec:column width="100" property="products.supplier.name" title="生产商"/>
		<ec:column width="90" property="products.code" title="商品编码"/>
		<ec:column width="100" property="products.name" title="商品名称"/>
		<ec:column width="100" property="products.stardard" title="商品规格"/>		
		<ec:column width="100" property="storage.name" title="仓库名称" />
		<ec:column width="90" property="unitPack" title="包装单位数量" />
		<ec:column width="80" property="count" title="库存数量" />
		<ec:column width="100" property="user.name" title="仓库管理员" />
	</ec:row>
</ec:table>
<!--ondblclick="closeWindow('${item.id}','${item.products.name}','${item.products.code}','${item.products.units.name }','${item.products.units.id }','${item.products.inprice }','${item.products.outprice }','${item.user.id }')"-->
</div>
</div>
</body>
</html>