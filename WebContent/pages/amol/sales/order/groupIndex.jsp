<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>团购消费统计</title>

<script type="text/javascript">

//提取经销商
function showAgent() {
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog("${ctx }/pages/amol/user/agent/selectorAgent.jsp",null,"dialogWidth=800px;resizable: Yes;");
	    if(cus!=null){
	    	var tab = document.getElementById("agent") ;
			tab.value=cus.name;
			var tab1 = document.getElementById("agentId") ;
			tab1.value=cus.id;
		}
 	}else{
		window.open("${ctx }/pages/amol/user/agent/selectorAgent.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	    this.returnAction=function(cus){
	    	if(cus!=null){
	    		var tab = document.getElementById("agent") ;
	    		tab.value=cus.name;
	    		var tab1 = document.getElementById("agentId") ;
	    		tab1.value=cus.id;
	    	}
		};
 	}
}
</script>
<!-- 地区 -->
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
	<div style="float: left;">团购消费统计</div>
	<div style="float: right;">
	</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
	<form action="${ctx}/salesOrder/groupSum.do" method="post">
      <table>
        <tr>
		  <td>
		  		地区：
		  </td>
		  <td align="left">
			    <span id='comboxWithTree'></span>	
			    <s:hidden id="regionId" name="regionId"/>
		  <td>
		  <td>         
		        商品名称：<s:textfield name="productName" size="10" id="name"/>
         </td>
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
<ec:table items="items" var="item" retrieveRowsCallback="process" sortRowsCallback="process" 
		action="${ctx}/salesOrder/moneySum.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="团购汇总.xls" 
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
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status" >
	<ec:row>
	    <ec:column width="110" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<%--  <ec:column width="150" property="goodsName" title="商 品" style="text-align:center" ellipsis="true"/>--%>
		<ec:column width="150" property="groupNumSum" title="销售数量" style="text-align:right" cell="number" ellipsis="true"/>
			<ec:column width="110" property="groupMoneySum" title="销售额" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true"/>
		<ec:column width="120" property="xjFxName" title="县级分销" style="text-align:right"   ellipsis="true"/>
		<ec:column width="120" property="xjFxMoney" title="县级分成【元】" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true"/>
		<ec:column width="120" property="adminFxMoney" title="平台分成【元】" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true"/>
		</ec:row>
</ec:table>
</div>
</div>

</body>
</html>