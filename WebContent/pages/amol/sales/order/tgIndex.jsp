<%@page import="com.systop.common.modules.region.RegionConstants"%>
<%@ page language="java" import="com.systop.amol.sales.utils.Payment" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>充值订单管理</title>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<script type="text/javascript">
Ext.onReady(function() {
	var pstree = new RegionTree({
		el : 'comboxWithTree',
		target : 'regionId',
		//emptyText : '选择地区',
		comboxWidth : 200,
    	treeWidth : 195,
		url : '${ctx}/admin/region/regionTree.do?regionId=null',
		defValue : {id:'${regionId}',text:'${regionNameCun}'}
	});
	pstree.init();	
	
});
function red(id){
	if (confirm("确认要冲红吗？冲红后不能恢复！")) {
		window.location.href="${ctx}/salesOrder/redRed.do?model.id=" + id;
     }
}
function isEmpty(_value){
	return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
}
function partCtr(){
	if (confirm("确认要操作这些订单吗？")){
		window.location.href="plRemove.do?type=tgSuc&strs=" + strs;
	}
}
var arr = new Array();
var strs="";
function updateArrays(id){
	strs="";
	if($("#"+id).is(":checked")){
		arr.push($("#"+id).val());
	}else{
		arr.remove($("#"+id).val());
	}
	for(var i=0;i<arr.length;i++){
		strs+=arr[i]+",";
	}
	if(!isEmpty($.trim(strs))){
		$("#plCtr").show();
	}else{
		$("#plCtr").hide();
	}
	strs=strs.substring(0,strs.length-1);
}
function removeAo(){
	alert("此订单已经生成出库单 ，不能冲红！");	
}
function removetgObj(id){
	window.location.href = "removetgObj.do?model.id="+id;
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">充值订单管理</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
    <form action="${ctx}/salesOrder/tgIndex.do" method="post">
		<table>
    <tr><td>&nbsp;&nbsp;订单：<s:textfield name="model.salesNo" size="17" id="salesNo"/> </td>
	                <td>      用户：<s:textfield name="name" size="10" id="name"/>
	                      <input type="hidden" name="salesType" value="g"></td>
        	<td>开始日期：<input id="startDate" name="startDate" size="20"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate"  readonly="readonly"/></td>
        	<td>结束日期：<input id="endDate" name="endDate" size="20" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate"  readonly="readonly"/>  </td>	                      
			<td>地区：</td><td><span id='comboxWithTree'></span><s:hidden id="regionId" name="regionId"/></td>
			<td><s:submit value="查询" cssClass="button" cssStyle="width:36px;"/></td>
			</tr>
			</table>
		<span id="plCtr" style="display: none"><a href="javascript:partCtr()"><img
				src="${ctx}/images/icons/add.gif" />&nbsp;批量删除&nbsp;</a></span>
   </form>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="tgIndex.do"
		useAjax="false"
		doPreload="false" 
		xlsFileName="充值订单.xls"
		pageSizeList="15,50,100" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="350px"
		minHeight="350"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="50" property="_1" title="选择" sortable="false" style="text-align:center">
			<input type="checkbox" name="selectedItems" id="${item.id}" onclick="updateArrays(this.id)"  value="${item.id}" class="checkbox"/></ec:column>
		<ec:column width="100" property="user.loginId" title="用户" ellipsis="true"></ec:column>
		<ec:column width="100" property="user.name" title="姓名" ellipsis="true"></ec:column>
		<ec:column width="200" property="_regionName" title="所属地区"  ellipsis="true">
		 	${item.user.region.parent.parent.name }&nbsp;&nbsp;${item.user.region.parent.name }&nbsp;&nbsp;${item.user.region.name }
		 </ec:column>
		<ec:column width="150" property="salesNo" title="订单号" style="text-align:center"/>
		<ec:column width="150" property="createTime" title="下单时间" style="text-align:center" format="yyyy-MM-dd HH:mm:ss" cell="date"/>
		<ec:column width="90" property="payment.name" title="支付方式" style="text-align:center" />
		<ec:column width="90" property="_payState" title="付款状态" style="text-align:center">
			<c:if test="${empty item.payState }"><font color='red'>未支付</font></c:if>
			<c:if test="${item.payState}"><font color='green'>已支付</font></c:if>
		</ec:column>
		<ec:column width="90" property="spayamount" title="金额" style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="100" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			<a href="view.do?type=cz&model.id=${item.id}">详情</a> |
			<a href="javascript:removetgObj(${item.id})">删除</a>
			<c:if test="${item.salesType=='g'&&item.payState&&empty item.oState}">
			<a href="updategState.do?flag=pay&model.id=${item.id}">已配送</a>
			</c:if>
			<c:if test="${item.salesType=='g'&&item.oState=='1'}">
			<a href="updategState.do?flag=send&model.id=${item.id}">已完成</a>
			</c:if>
		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>