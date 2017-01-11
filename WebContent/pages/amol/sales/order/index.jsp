<%@page import="com.systop.common.modules.region.RegionConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>订单管理</title>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
	function removeObj(id){
		window.location.href = "remove.do?model.id="+id;
	}
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
	function isEmpty(_value){
		return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
	}
	function partCtr(){
		if (confirm("确认要操作这些订单吗？")){
			window.location.href="plRemove.do?type=success&strs=" + strs;
		}
	}
function red(id){
	if (confirm("确认要冲红吗？冲红后不能恢复！")) {
		window.location.href="${ctx}/salesOrder/redRed.do?model.id=" + id;
     }
}
function removeAo(){
	alert("此订单已经生成出库单 ，不能冲红！");	
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
	function toMenus(){
		window.location.href = "${ctx}/salesOrder/index.do";
	}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">订单管理</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
    <form action="${ctx}/salesOrder/index.do" method="post">
    <table>
    <tr><td>&nbsp;&nbsp;订单：<s:textfield name="model.salesNo" size="17" id="salesNo"/> </td>
	                <td>      用户：<s:textfield name="name" size="10" id="name"/>
	                      </td>
		<td>      手机号：<s:textfield name="mobile" size="10" id="mobile"/>
		</td>

		<td>      开票状态：<s:select list="#{'':'全部','1':'已申请','2':'未申请'}" name="model.sqkfp" cssClass="required" cssStyle="width:60px;"></s:select></td>
		<td>      订单类型：<s:select list="#{'':'全部','jy':'加油单','bx':'保险单','wx':'维修单'}" name="model.salesType" cssClass="required" cssStyle="width:60px;"></s:select></td>
	</tr>
		<tr><td>开始日期：<input id="startDate" name="startDate" size="20"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate"  readonly="readonly"/></td>
        	<td>结束日期：<input id="endDate" name="endDate" size="20" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate"  readonly="readonly"/>  </td>	                      
			<td>地区：<span id='comboxWithTree'></span><s:hidden id="regionId" name="regionId"/></td>
			<td><s:submit value="查询" cssClass="button" cssStyle="width:36px;"  id="searchBtn"/>
				<s:reset value="重置" cssClass="button" onclick="toMenus()"/>
			</td>
			</tr>
			</table>
		<span id="plCtr" style="display: none"><a href="javascript:partCtr()"><img
				src="${ctx}/images/icons/add.gif" />&nbsp;批量删除&nbsp;</a></span>
   </form>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit"
		action="index.do"
		useAjax="false"
		doPreload="false"
		xlsFileName="订单管理.xls"
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
		<ec:column width="40" property="_1" title="选择" sortable="false" style="text-align:center">
			<input type="checkbox" name="selectedItems" id="${item.id}" onclick="updateArrays(this.id)"  value="${item.id}" class="checkbox"/></ec:column>
		<ec:column width="100" property="user.loginId" title="用户" ellipsis="true"/>
		<ec:column width="100" property="user.mobile" title="手机号" ellipsis="true"/>
		<ec:column width="100" property="user.name" title="姓名" ellipsis="true"/>
		<ec:column width="200" property="_regionName" title="所属地区"  ellipsis="true">
		 	${item.merUser.region.parent.parent.name }&nbsp;&nbsp;${item.merUser.region.parent.name }&nbsp;&nbsp;${item.merUser.region.name }
		 </ec:column>
		<ec:column width="150" property="_ordertype" title="订单类型" style="text-align:center">
			
			<c:if test="${item.salesType=='jy'}">
				加油单
			</c:if>
			<c:if test="${item.salesType=='bx'}">
				保险单
			</c:if>
			<c:if test="${item.salesType=='wx'}">
				维修单
			</c:if>
		</ec:column>
		<ec:column width="150" property="salesNo" title="订单号" style="text-align:center"/>
		<ec:column width="100" property="_kpzt" title="开票状态"  ellipsis="true">
			<c:if test="${item.sqkfp=='1'}">
				申请
			</c:if>
			<c:if test="${item.sqkfp=='0'||item.sqkfp==null}">
				未申请
			</c:if>
		</ec:column>
		<ec:column width="150" property="createTime" title="下单时间" style="text-align:center" format="yyyy-MM-dd HH:mm:ss" cell="date"/>
		<ec:column width="80" property="count" title="商品总数量" style="text-align:center"/>
		<ec:column width="60" property="payment.name" title="支付方式" style="text-align:center" />
		<ec:column width="60" property="_payState" title="付款状态" style="text-align:center">
			<c:if test="${empty item.payState }"><font color='red'>未付款</font></c:if>
			<c:if test="${item.payState==false }"><font color='red'>支付失败</font></c:if>
			<c:if test="${item.payState==true }"><font color='green'>已支付</font></c:if>
		</ec:column>
		<ec:column width="60" property="samount" title="应收金额" style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="60" property="_spayamount" title="实收金额" style="text-align:right" format="###,##0.00" cell="number">
			<c:if test="${item.spayamount == null}">
				0.0
			</c:if>
			<c:if test="${item.spayamount != null}">
				${item.spayamount}
			</c:if>
		</ec:column>
		<ec:column width="200" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			<a href="javascript:toSeeDetail('${item.salesType}','${item.id}')">详情</a> |
			<a href="edit.do?model.id=${item.id}">编辑</a> |
			<c:if test="${item.sqkfp=='1'}">
				<a href="seeFp.do?model.id=${item.id}">发票信息</a> |
			</c:if>
			<a href="javascript:removeObj(${item.id})">删除</a>
		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
<script type="text/javascript">

	function toSeeDetail(type,id){
		if(type=="jy"){
			window.location.href = "view.do?model.id="+id;
		}
		if(type=='wx'){
			window.location.href = "viewWx.do?model.id="+id;
		}
		if(type=='bx'){
			window.location.href = "viewBx.do?model.id="+id;
		}
	}
</script>
</body>
</html>