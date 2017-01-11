<%@ page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page
	import="java.util.Date,java.util.List,com.systop.amol.purchase.model.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<%@include file="/common/msgDiv.jsp"%>
<title>库存期初</title>
<script type="text/javascript"
	src="${ctx}/pages/amol/stock/init/editjs.js"></script>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">
<div style="float: left;">库存期初管理</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<fieldset style="width: 95%; padding: 10px 10px 10px 10px;">
<legend>库存期初</legend> 
<s:form id="query" namespace="/stock/init"
	theme="simple" method="POST" action="edit.do"
	enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td align="left" width="40">仓库：</td>
			<td class="simple" align="left">
			<s:select id="storageId"
				cssStyle="width:110px" list="storageMap" name="model.storage.id"
				headerKey="" headerValue="选择仓库" onchange="queryProByStorage()"/> 
			<input type="button" value="查询全部" Class="button" onclick="queryProAll()" /></td>
		</tr>
		<tr>
			<td>批量导入数据：</td>
			<td><s:file id="dataInfo" name="data" size="30"	cssStyle="background-color: FFFFFF;" />&nbsp;&nbsp;
				<a href="#"	onclick="return importData();" class="button">数据导入</a>
				<a href="#" onclick="initTemplate()"><font color="blue">【模板下载】</font></a>&nbsp;&nbsp;&nbsp;
				<font color="red">注意：导入数据中的商品均为【基本单位】</font>
			</td>
		</tr>
		<tr>
			<s:if test="errorInfo.size() > 0">
				<td align="left"><font color="red">系统提示信息：</font></td>
				<td align="left" colspan="3"><s:iterator value="errorInfo">
					<font color="red"><s:property /></font>
					<br />
				</s:iterator></td>
			</s:if>
		</tr>
	</table>
</s:form>
</fieldset>
<s:form action="save" id="save" validate="true" method="post">
	<s:hidden id="saveStorageId" name="model.storage.id" />
	<table align="center" width="95%">
		<tr>
			<td>
			<div align="left"><input name="button" class="button"
				type="button" value="增加商品" onclick="addRow()"></div>
			</td>
		</tr>
	</table>
	
	<table id="contentTbl" width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9">
		<tbody id="tbody">
			<tr>
				<td height="23" align="center" bgcolor="#F3F4F3">经销商</td>
				<td align="center" bgcolor="#F3F4F3" colspan="2">商品</td>
				<td align="center" bgcolor="#F3F4F3">编号</td>
				<td align="center" bgcolor="#F3F4F3">规格</td>
				<td align="center" bgcolor="#F3F4F3">仓库</td>
				<td align="center" bgcolor="#F3F4F3">单位</td>
				<td align="center" bgcolor="#F3F4F3">数量</td>
				<td align="center" bgcolor="#F3F4F3">单价</td>
				<td align="center" bgcolor="#F3F4F3">金额</td>
				<td align="center" bgcolor="#F3F4F3">操作</td>
			</tr>
			<tr style="display: none" height="23" align="center" bgcolor="#FFFFFF">
				<td align="center" bgcolor="#FFFFFF">
					<input name="suppliers" type="text" size="14" readonly="readonly" style="color: #808080; border: 0px;" />
				</td>
				<td align="center" bgcolor="#FFFFFF">
					<input name="pname" type="text" size="12"
					readonly="readonly" style="color: #808080; border: 0px;" /><input type="hidden"
					name="pid" /><input type="hidden" name="status" value="0" />
					<input type="hidden" name="siId" value="" />
				</td>
				<td align="center" bgcolor="#FFFFFF">
					<input name="button1" type="button"
					onclick="edit(this.parentNode.parentNode)" value="选择"
					class="button" />
				</td>
				<td align="center" bgcolor="#FFFFFF">
					<input name="code" type="text" size="8"
					readonly="readonly" style="color: #808080; border: 0px;" />
				</td>
				<td align="center" bgcolor="#FFFFFF">
					<input name="stardard" type="text" size="8"
					readonly="readonly" style="color: #808080; border: 0px;" />
				</td>
				<td align="center" bgcolor="#FFFFFF">
					<input name="storageName" type="text" size="10" readonly="readonly" style="color: #808080; border: 0px;" />
					<input type="hidden"" name="sId"/>
				</td>
				<td align="center" bgcolor="#FFFFFF">
					<input name="unit" onfocus="showUnits(this)" type="text"
					size="6" style="color: #808080;border: 0px;" /><input type="hidden"
					name="unitid" />
				</td>
				<td align="center" bgcolor="#FFFFFF">
					<input name="ncounts" size="8" onfocus="beforeyzzs(this)"
					onkeyup="yzzs(this)"
					onblur="this.value=this.value.replace(/[^(-?\d+)|(\.\d+)?$]/g,'');yzsz(this)"
					align="right" />
					<input type="hidden" name="counts" size="9" readonly="readonly"
					align="right" style="color: #808080;" />
				</td>
				<td align="center" bgcolor="#FFFFFF">
					<input name="prices" size="5"
					onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'');yzsz(this)"
					onblur="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'');yzsz(this)"
					align="right" style="text-align: right;" /></td>
				<td align="center" bgcolor="#FFFFFF">
					<input name="amounts" size="10" readonly="readonly"
					style="color: #808080;border: 0px;" style="text-align: right;" /></td>
				<td align="center" bgcolor="#FFFFFF">
					<input type="button" value="删除"
					class="button" onclick="removeRow(this.parentNode.parentNode)">
				</td>
			</tr>
			<s:iterator value="#request.stockInits" var="si">
				<tr align="center" bgcolor="#FFFFFF">
					<td height="23" align="center" bgcolor="#FFFFFF">
						<input name="suppliers" type="text"
						value="${si.products.supplier.name}" size="14" readonly="readonly"
						style="color: #808080; border: 0px"/>
						
					</td>
					<td align="center" bgcolor="#FFFFFF">
						<input name="pname" type="text"
						value="${si.products.name}" size="12" readonly="readonly"
						style="color: #808080; border: 0px" />
						<input type="hidden" name="siId" value="${si.id}" />
						<input type="hidden" name="pid" value="${si.products.id}" />
						<input type="hidden" name="status" value="${si.status}" />
					</td>
					<td align="center" bgcolor="#FFFFFF">
						<s:if test="#attr.si.status == 0">
							<input name="button1" type="button"	onclick="edit(this.parentNode.parentNode)" value="选择"	class="button" />
						</s:if>
						<s:else>
							<input name="button1" type="button"	disabled="disabled" value="选择"	class="button" />
						</s:else>
					</td>
					
					<td align="center" bgcolor="#FFFFFF">
						<input name="code" type="text" value="${si.products.code}"
						size="8" readonly="readonly" style="color: #808080;border: 0px;" />
					</td>
					<td align="center" bgcolor="#FFFFFF">
						<input name="stardard" value="${si.products.stardard}"
						size="8" readonly="readonly" style="color: #808080;border: 0px;" />
					</td>
					<td align="center" bgcolor="#FFFFFF">
						<input name="storageName" type="text" value="${si.storage.name}"
						size="8" readonly="readonly" style="color: #808080;border: 0px;" />
						<input type="hidden"" name="sId" value="${si.storage.id}"/>
					</td>
					<td align="center" bgcolor="#FFFFFF">
						<s:if test="#attr.si.status == 0">
							<input name="unit" onfocus="showUnits(this)"
							value="${si.units.name}" size="6" style="color: #808080;border: 0px;" />
							<input type="hidden" name="unitid" value="${si.units.id}" />
						</s:if>
						<s:else>
							<input name="unit" disabled="disabled"
							value="${si.units.name}" size="6" style="color: #808080;border: 0px;" />
							<input type="hidden" name="unitid" value="${si.units.id}" />
						</s:else>
					</td>
					<td align="center" bgcolor="#FFFFFF">
						<s:if test="#attr.si.status == 0">
							<input name="ncounts" size="8" value="${si.ncount}"
							onfocus="beforeyzzs(this)"
							onkeyup="this.value=this.value.replace(/[^(-?\d+)(\.\d+)?$]/g,'');yzzs(this)"
							onblur="this.value=this.value.replace(/[^(-?\d+)(\.\d+)?$]/g,'');yzsz(this)"
							align="right" title="基本数量:${si.count}"/>
							<input type="hidden" name="counts" value="${si.count}" />
						</s:if>
						<s:else>
							<input name="ncounts" size="8" value="${si.ncount}" readonly="readonly" align="right" style="color:#808080" title="基本数量:${si.count}"/>
							<input type="hidden" name="counts" value="${si.count}"/>
						</s:else>
					</td>
					<td align="center" bgcolor="#FFFFFF">
						<s:if test="#attr.si.status == 0">
						<input name="prices" size="5" value="${si.price}"
						onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')"
						onblur="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'');yzsz(this)"
						align="right" style="text-align: right;" />
						</s:if>
						<s:else>
							<input name="prices" size="5" value="${si.price}" readonly="readonly" align="right" style="text-align: right;color:#808080" />
						</s:else>
					</td>
					<td align="center" bgcolor="#FFFFFF"><input name="amounts" size="10" value="<fmt:formatNumber value='${si.amount}' pattern='0.00' type='number'/>"
						readonly="readonly" style="color: #808080;border: 0px;"
						style="text-align: right;" />
					</td>
					<td align="center" bgcolor="#FFFFFF">
						<s:if test="#attr.si.status == 0">
							<input type="button" value="删除"
							class="button" onclick="removeRow(this.parentNode.parentNode)">
						</s:if>
						<s:else>
							<input type="button" value="删除"
							class="button" disabled="disabled">
						</s:else>
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
			<input type="button" value="保存" Class="button"
				onclick="return saveyz();" /></td>
		</tr>
	</table>
</s:form></div>
</div>
<script type="text/javascript">
	/** ready */
	$(document).ready(function() {
		$("#save").validate();
	});
</script>
<script type="text/javascript">
	function importData() {
		var data = $('#dataInfo').val();
		if (!data || data == "") {
			alert('选择批量导入路径');
			return;
		}
		var importFrm = document.getElementById('query');
		if (confirm('您确定要导入期初库存数据吗？')) {
			loadDiv("正在导入期初数据，请稍等.......");
			importFrm.action = 'importData.do';
			importFrm.submit();
		}
	}
</script>
</body>
</html>