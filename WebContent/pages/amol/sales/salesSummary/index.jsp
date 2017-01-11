<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>佣金分配</title>

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
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox1.js"> </script>
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
	<div style="float: left;">销售汇总</div>
	<div style="float: right;">
	</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
	<form action="${ctx}/salesSummary/index.do" method="post">
      <table>
        <tr>
          <td>
          	    <s:hidden name="prosortId"/>
          	    <span id="jxs">
          	    	<s:textfield name="distributorName" maxlength="18" id="agent" style="width:150px" readOnly="true"/>								    	
			    	<input type="button" id="btn1" class="button" value="选择供货商" style="width:70px;" onclick="showAgent()"/>
			    	<s:hidden name="distributorId" id="agentId"/>
			    </span>
		  </td>
		  <td>
		  		地区：
		  </td>
		  <td align="left">
			    <span id='comboxWithTree'></span>	
			    <s:hidden id="regionId" name="regionId"/>
		  <td>
		  <td>         
          	    <%-- 商品编码：<s:textfield name="productCode" size="7" id="productCode"/> --%>
		        商品名称：<s:textfield name="productName" size="10" id="name"/>
         </td>
         <td>
         		支付方式：<s:select list='paymentMap' name="payment"></s:select>
         </td>
       </tr>
       <tr>
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
		action="index.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="销售汇总.xls" 
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
	    <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="70" property="code" title="编 号" style="text-align:center" ellipsis="true"/>
		<ec:column width="70" property="productName" title="商 品" style="text-align:center" ellipsis="true"/>
		<ec:column width="70" property="stardard" title="规格" style="text-align:center" ellipsis="true"/>
		<!-- ec:column width="70" property="unitName" title="单位" cell="date" style="text-align:center" ellipsis="true"/> -->
		<ec:column width="70" property="outCount" title="销售数量" style="text-align:right" cell="number" ellipsis="true"/>
		<ec:column width="60" property="outAmount" title="销售额" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true"/>
		<ec:column width="60" property="inprice" title="成本" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true"/>
		<ec:column width="80" property="distributionCommission" title="佣金【县】" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true"/>
		<ec:column width="80" property="villageDistributionCommission" title="佣金【村】" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true"/>
		<ec:column width="60" property="returnCount" title="退货数量" style="text-align:right" cell="number" ellipsis="true"/>
		<ec:column width="60" property="returnAmount" title="退货额" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true"/>
		<!-- ec:column width="70" property="allUnitPack" title="汇总数量" ellipsis="true"/> -->
		<ec:column width="70" property="allUnitPack" title="汇总数量" style="text-align:right" cell="number" ellipsis="true">
			${item.outCount - item.returnCount }
		</ec:column>
		<ec:column width="70" property="w" title="汇总金额" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true">
		    ${item.outAmount - item.returnAmount }
		</ec:column>
		<ec:column width="70" property="w" title="汇总利润" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true">
		    ${item.outAmount - item.inprice-item.returnAmount - item.distributionCommission - item.villageDistributionCommission }
		</ec:column>
	</ec:row>
		<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="4" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px;">金额单位为“元”，合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;">${count }</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${amount }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${inprice}" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${vdc }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${dc }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;">${hanod}</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${rttao }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;">${count - hanod }</td>
				<c:if test="${vdc!=null&&dc!=null}">
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${amount - rttao }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${amount  - rttao-inprice - vdc - dc }" pattern="#,##0.00"/></td>
				</c:if>
			</tr>
		</ec:extendrow>
	</s:if>
</ec:table>
</div>
</div>

</body>
</html>