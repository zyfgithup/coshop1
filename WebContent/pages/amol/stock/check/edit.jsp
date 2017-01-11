<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>库存管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<s:if test="model.user.beginningInit == 0">
	<script type="text/javascript">
		location.href = URL_PREFIX+'/stock/check/index.do';
	</script >
</s:if>
<script type="text/javascript" src="${ctx}/pages/amol/stock/check/edit.js"></script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">库存盘点管理</div>
	<div style="float: right;font-weight: normal;">
	    <a href="${ctx}/stock/check/index.do"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>&nbsp;&nbsp;返回&nbsp;</a>	
	</div>
</div> 
<div class="x-toolbar">
<table width="100%">
	<s:form action="index.do" theme="simple" namespace="/stock/check/detail/">
		<tr>
			<td>
            <s:hidden name="model.stockCheck.id" id="stockCheckId"/>
			单据编号：<s:textfield id="checkNo" name="model.stockCheck.checkNo" size="16" cssStyle="border:0;border-bottom:1 solid black;background:transparent;" readonly="true" />					 
			单据日期：<s:textfield id="checkDate" name="model.stockCheck.createTime"  cssStyle="width:72px;" cssClass="required" readonly="true"/>
			盘点人员：<s:textfield cssStyle="color:#808080;width:151px;" name="model.stockCheck.employee.name" id="empname" cssClass="empCheck" readonly="true" />
    		 <s:if test="model.stockCheck.id != null ">
    		 		<input name="button2" id="empButton" type="button" class="button" disabled="disabled" value="选择" />
					<s:hidden name="model.stockCheck.employee.id" id="empid" />
    		            仓库名称：
    		     <span onmousemove="this.setCapture();" onmouseout="this.releaseCapture();" > 
                 <s:select list="storageMap" name="model.stockCheck.storage.id" headerKey="" id="storageId" 
                     headerValue="请选择" cssStyle="width:100px;color:#808080;border: 1px solid #808080;"/>	
                 </span> 
             </s:if>
             <s:else>
			   <input name="button2" id="empButton" type="button" class="button" onclick="emp()" value="选择" />
			   <s:hidden name="model.stockCheck.employee.id" id="empid" />
    		           仓库名称：             
    		    <s:select list="storageMap" name="model.stockCheck.storage.id" id="storageId" headerKey="" 
                   headerValue="请选择" onchange="selectStorage();" cssStyle="width:100px;border: 1px solid"/>	
             </s:else>
			</td>
		</tr>
		<tr>
			<td>
				操&nbsp;&nbsp;作&nbsp;&nbsp;员：<input name="model.stockCheck.user.name" disabled="disabled" style="width:110px;" value="${model.stockCheck.user.name}"/>
				商品类别：<span id='comboxWithTree' style="width: 80px;display: inline;"></span><s:hidden id="productSortId" name="productSort.id" /><s:hidden id="productSortName" name="productSort.Name" />
				商品名称：<s:textfield name="productName" id="productName" size="8" />
						 <s:hidden name="productCode" id="productCode" />
						<input name="button2" id="proButton" onclick="selectPro()" type="button" class="button" value="选择" />
						<s:if test="model.stockCheck.status == 0 || model.stockCheck.status == null">
							<input type="button" value="生成库存盘点单" title="追加盘点信息" class="button" id="generateStockCheckButton" onclick="generateStockCheckInfo()" />
							<input type="button" value="确定" class="button" id="isGenerateStockCheckButton" onclick="updateStockCheckStatus()" />
						</s:if>
						<c:choose>
							<c:when test="${model.stockCheck.status == '2'}">
								<s:if test="model.stockCheck.id != null ">
									<input class="button" type="button" onclick="stockCheckAll()" value="清查" />
								</s:if>
							</c:when>
						</c:choose>
						<s:if test="model.stockCheck.id != null ">
							<s:submit value="查询" id="searchButtion" cssClass="button" />
						</s:if>
			</td>
		</tr>	
	</s:form>
</table>
</div>
<div class="x-panel-body">
	<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit"
		action="index.do"
		useAjax="true"
		doPreload="false"
		xlsFileName="${model.stockCheck.checkNo}.xls" 
		maxRowsExported="10000000"
		pageSizeList="15,50,100"
		editable="false"
		sortable="false"
		rowsDisplayed="15"
		generateScript="true"
		resizeColWidth="false"
		classic="false"
		width="100%"
		height="375px"
		minHeight="375"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status">
		<ec:row rowId="${item.id}">
			<ec:column width="70" property="_0" title="No."	value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center" />
			<ec:column width="160" property="stock.products.supplier.name" title="供应商" ellipsis="true" />
			<ec:column width="140" property="stock.products.code" title="商品编码" ellipsis="true" />
			<ec:column width="160" property="stock.products.name" title="商品名称" ellipsis="true" />
			<ec:column width="100" property="stock.products.stardard" title="商品规格" ellipsis="true" />
			<ec:column width="40" property="stock.products.units.name" title="单位" ellipsis="true" style="text-align:center"/>
			<ec:column width="72" property="count" title="实际库存" ellipsis="true">
				<s:if test="model.stockCheck.status == 2">
					<input type="text"
						onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')"
						onblur="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'');updateSCD(this);"
						name="modelCount" size="8" value="${item.count}" />
				</s:if>
				<s:else>
					${item.count}
				</s:else>
				<!-- 库存盘点详单ID -->
				<input type="hidden" id="stockCheckDetailId" value="${item.id}"/>
			</ec:column>
			<ec:column width="70" title="库存数量" property="stockCount" ellipsis="true" columnId="stock.count" />
			<ec:column width="70" title="清查数量" property="checkCount" ellipsis="true" columnId="checkCount" />
			
			<ec:column width="60" property="_c" title="操作" style="text-align:center" sortable="false">
				<s:if test="#attr.item.stockCheck.status == 0">
			    	<a href="#" onclick="remove('${item.id}')" title="删除">删除</a>
			    </s:if>
			    <s:else>
			    	<font color="silver" >删除</font>	
			    </s:else>
			</ec:column>
			
		</ec:row>
	</ec:table>
</div>
</div>
<script type="text/javascript"
	src="${ctx}/pages/amol/base/product/prosortCombox.js"></script>
<script type="text/javascript">
	Ext.onReady(function() {
		var pstree = new ProsortTree({
			el : 'comboxWithTree',
			target : 'productSortId',
			targetName : 'productSortName',
			//emptyText : '选择商品类型',
			url : '${ctx}/base/prosort/prosortTree.do?status=1',
			defValue : {
				id : '${productSortId}',
				text : '${productSort.name}'
			}
		});
		pstree.init();
	});
</script>
<script type="text/javascript">
	/** ready */
	$(document).ready(function() {
/*		var stockCheckId = $("#stockCheckId").val();
		var generateSCButton = $("#generateStockCheckButton").val();
		if(stockCheckId ){
			$("#generateStockCheckButton").hide();
			$("#searchButtion").show();
		}else{
			$("#generateStockCheckButton").show();
			$("#searchButtion").hide();
		}*/
		$("#editStockCheckDetail").validate();
		var emp = document.getElementById('empname').value;
		if (emp != null && emp != '') {
			document.getElementById('empButton').disabled = true;
		}
	});

	$.validator.addMethod("storageCheck", function(value, element) {
		var res = "";
		var storage = document.getElementById('storageId').value;
		if (storage == null || storage == '') {
			res = "err";
			Ext.MessageBox.show({
				title : '提示',
				minWidth : 220,
				msg : '<div style=\'width:180\';><br/>请正确选择仓库！</div>',
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
		}
		return res != "err";
	}, "");
	$.validator.addMethod("empCheck", function(value, element) {
		var res = "";
		var empName = document.getElementById('empname').value;
		if (empName == null || empName == '') {
			res = "err";
			Ext.MessageBox.show({
				title : '提示',
				minWidth : 220,
				msg : '<div style=\'width:180\';><br/>请正确选择清查人员！</div>',
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
		}
		return res != "err";
	}, "");
	
	function ecRefresh() {
	  ECSideUtil.reload('ec');
	}
</script>
</body>
</html>