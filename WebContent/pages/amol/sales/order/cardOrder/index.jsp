<%@page import="com.systop.common.modules.region.RegionConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<html>
<head>
<title>销售订单管理</title>
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
function red(id){
	if (confirm("确认要冲红吗？冲红后不能恢复！")) {
		window.location.href="${ctx}/salesOrder/redRed.do?model.id=" + id;
     }
}

function removeAo(){
	alert("此订单已经生成出库单 ，不能冲红！");
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">销售订单【网银】</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
   <form action="${ctx}/salesOrder/cardSalesOrder/index.do" method="post">
   <s:hidden name="pment" value="WXPAY"/>
    <s:hidden name="payment" value="ALIPAY"/>
          	<!-- 类型：<s:select list='statusMap' name="model.redRed" cssStyle="width:48px;"></s:select> 
	                     出库状态：<s:select id="ckzt" list="%{salesStatusMap}" name="model.ckzt" headerKey="" 
                      headerValue="全部" cssStyle="width:55px;" />           --> 
	        &nbsp;&nbsp;订单：<s:textfield name="model.salesNo" size="17" id="salesNo"/> 
	                      用户：<s:textfield name="nameName" size="10" id="name"/>
        	开始日期：<input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
        	结束日期：<input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>  	                      
			        		地区：
			        		<span id='comboxWithTree'></span>	
							<s:hidden id="regionId" name="regionId"/>
	        <s:submit value="查询" cssClass="button" cssStyle="width:36px;"/>
   </form>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="${ctx}/salesOrder/cardSalesOrder/index.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="销售订单（网银）.xls" 
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
		<ec:column width="80" property="user.name" title="用户" ellipsis="true"></ec:column>
		<ec:column width="115" property="salesNo" title="订单号" style="text-align:center"/>
		<ec:column width="80" property="createTime" title="下单时间" style="text-align:center" cell="date"/>
		<ec:column width="90" property="samount" title="金额" style="text-align:right" format="###,##0.00" cell="number"/>
        <ec:column width="150" property="remark" title="备注" ellipsis="true"/>		
		<ec:column width="100" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			<a href="view.do?model.id=${item.id}">详情</a> | 
			<c:if test="${item.redRed == 1}">
			   <c:if test="${item.ckzt eq '0'}">
				  <a href="#" onclick="red(${item.id})" title="冲红">冲红</a><!--  |
				  a href="${ctx}/sales/orderToEditUI.do?orderId=${item.id}" title="出库">生成出库单</a> -->			 
	           </c:if>
	 		   <c:if test="${item.ckzt eq '1'}">
				  <a href="#" onclick="removeAo()" title="冲红"><font color="#999999">冲红</font></a><!--  |
				  a href="${ctx}/sales/orderToEditUI.do?orderId=${item.id}" title="出库">生成出库单</a> -->			 
	           </c:if>  
	 		   <c:if test="${item.ckzt eq '2'}">
				  <a href="#" onclick="removeAo()" title="冲红"><font color="#999999">冲红</font></a>
				  <!-- font color="#999999" >生成出库单</font> -->			 
	           </c:if>  
            </c:if> 
             
            <c:if test="${item.redRed > 1}">  
		       <a href="#" onclick="removeAo()" title="冲红"><font color="#999999">冲红</font></a>
		       <!--  |<font color="#999999">生成出库单</font>-->
		    </c:if>                     
		</ec:column>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="4" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px;">金额单位为“元”，应收合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><%@include file="/common/taglibs.jsp"%>
<fmt:formatNumber value="${payTotal}" pattern="#,##0.00"/></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</ec:extendrow>
	</s:if>
</ec:table>
</div>
</div>
</body>
</html>