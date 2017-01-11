<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>消费金额统计</title>

<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
Ext.onReady(function() {
	var pstree = new RegionTree({
		el : 'comboxWithTree',
		target : 'regionId',
		//emptyText : '选择地区',
		comboxWidth : 200,
    	treeWidth : 195,
		url : '${ctx}/admin/region/regionTree.do?regionId=<%=RegionConstants.HBS_ID %>',
		defValue : {id:'${regionId}',text:'${regionName}'}
	});
	pstree.init();	
	
});
</script>

</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">消费金额统计</div>
	<div style="float: right;">
	</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
	<form action="${ctx}/salesOrder/moneySum.do" method="post">
      <table>
		  <td>
		  		地区：
		  </td>
		  <td align="left">
			    <span id='comboxWithTree'></span>	
			    <s:hidden id="regionId" name="regionId"/>
		  <td>
       	<td colspan="5">
       	
       		开始日期：<input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
		       	结束日期：<input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;  	                      
		        <input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button"/>
       	
       	</td>
       </tr>
     </table>
    </form>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="moneySum.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="消费记录汇总.xls" 
		pageSizeList="15,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="false"	
		width="100%" 	
		height="380px"	
		minHeight="380"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
	<ec:row>
	    <ec:column width="50" property="_s" title="No." value="${GLOBALROWCOUNT}" viewsAllowed="html"  sortable="false" style="text-align:center"/>
		<ec:column width="100" property="loginId" title="用户名" style="text-align:center" ellipsis="true"/>
		<ec:column width="100" property="name" title="姓名" style="text-align:center" ellipsis="true"/>
		<ec:column width="80" property="sName" title="市/县"  ellipsis="true"/>
		<ec:column width="80" property="xName" title="镇"  ellipsis="true"/>
		<ec:column width="80" property="cName" title="村"  ellipsis="true"/>
		<ec:column width="150" property="cdName" title="村点名称" style="text-align:right" cell="number" ellipsis="true"/>
		<ec:column width="150" property="sumMoney" title="消费金额总计" style="text-align:right" cell="number" ellipsis="true"/>
		<ec:column width="165"  property="fxMoney" title="返现金额" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true"/>
	</ec:row>
</ec:table>
</div>
</div>

</body>
</html>