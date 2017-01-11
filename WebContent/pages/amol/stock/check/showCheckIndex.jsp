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
function closeWindow(pros){
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		window.returnValue = pros;
		window.close();
 	}else{
	    window.parent.close();
		window.parent.opener.returnAction(pros);
	}
}
function submitProducts(){
	// 商品集合
	var pros = new Array();
	var checked = false;
	//遍历checkbox
	$('input').each(function(i, item) {
		if (item.checked && item.id == 'selectedItems') {
			//分割checkbox中的value值(id,code)
			var temp = item.value.split(",");
			var product = new products();
			product.id = temp[0];
			product.code = temp[1];
			product.name = temp[2];
			pros[i] = product;
			checked = true;
		}
	});
	if(!checked){
		if(confirm('未选中一件商品,是否提交?')){
			closeWindow(pros);
		}
	}else{
		if(confirm('是否提交?')){
			closeWindow(pros);
		}
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
        <s:form action="selectorProIndex" theme="simple">
            <s:hidden name="model.products.prosort.id"/>
            <s:hidden name="model.storage.id"/>
            <input type="hidden" name="proCode" value="${request.proCode}"/>
            <s:hidden name="checkNo"/>
        	商品名称：<s:textfield name="model.products.name" size="15"/>&nbsp;&nbsp;
        	商品编码：<s:textfield name="model.products.code" size="15"/>&nbsp;&nbsp;
        	<s:submit value="查询" cssClass="button"></s:submit>
        	<input type="button" value="提交" class="button" onclick="submitProducts()"/>
         </s:form>
        </td>
       </tr>
     </table>
   </div>
<div class="x-panel-body">
<ec:table items="items" var="item" 
        retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="selectorProIndex.do"
		useAjax="true" doPreload="false"
		maxRowsExported="10000000" 
		pageSizeList="10000000" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="10000000"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="407px"	
		minHeight="407">
	<ec:row>
	 	<ec:column width="50" property="_0" title="选择" sortable="false" style="text-align:center">
	       <input type="checkbox" name="selectedItems" id="selectedItems" value="${item.products.id},${item.products.code},${item.products.name}" class="checkbox"/>
	    </ec:column>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="100" property="products.prosort.name" title="商品类别"/>
		<ec:column width="100" property="products.supplier.name" title="生产商"/>
		<ec:column width="83" property="products.code" title="商品编码"/>
		<ec:column width="100" property="products.name" title="商品名称"/>
		<ec:column width="70" property="products.stardard" title="规格"/>
		<ec:column width="60" property="products.units.name" title="基本单位"/>
		<ec:column width="65" property="products.outprice" title="参考出价"/>
		<ec:column width="65" property="products.inprice" title="参考进价"/>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>