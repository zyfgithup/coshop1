<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>

<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>

<title></title>
<script type="text/javascript">
function xdcx(){
	var form1=document.getElementById("form1");
	var sno=document.getElementById("sno");
	var sid=document.getElementById("sid");
	form1.action="${ctx}/purchase/pay/index.do?model.no="+sno.value+"&model.supplier.name="+sid.value;
	form1.submit();
}

</script>
</head>
<body>


<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">应付款详单查询</div>
	<div style="float: right;">
	<a href="#" onclick="xdcx()"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>&nbsp;返回&nbsp;&nbsp;</a>
	</div>
</div> 
    <div class="x-toolbar">
      <table width="99%">
        <tr>
          <s:form action="index" theme="simple" id="form1">
          <td> 
            <td>供应商：</td>
             <td><s:textfield name="model.pay.supplier.name" id="sid" cssStyle="width:120px" /></td>
        	<td>单号：</td>
        	<td><s:textfield name="model.pay.no" size="15" id="sno"/></td>
        	<td>开始日期：<input id="startDate" name="startDate" size="10"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
        	结束日期：<input id="endDate" name="endDate" size="10" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
        	入库单号：<s:textfield name="model.purchase.sno" size="15"/>&nbsp;&nbsp;
        	<s:submit value="查询" cssClass="button"></s:submit>
           </td>
         </s:form>
        </tr>
      </table>
    </div>   
    <div class="x-panel-body">
    <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do"
		useAjax="true" 
		xlsFileName="应付款详单.xls" 
		doPreload="false"
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="400px"	
		minHeight="400"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"    
	>
	<ec:row>
		<ec:column width="40" property="_0" title="No." value="${GLOBALROWCOUNT}" sortable="false" />
		<ec:column width="140" property="pay.no" title="单号" style="text-align:center"/>
		<ec:column width="100" property="pay.outDate" title="日期" style="text-align:center" cell="date"/>
		<ec:column width="170" property="pay.supplier.name" title="供应商" ellipsis="true"/>
		<ec:column width="140" property="purchase.sno" title="入库单号" />
		<ec:column width="100" property="purchase.samount" title="应付金额" style="text-align:right" format="###,##0.00"  cell="number"/>
		<ec:column width="100" property="amount" title="本次付款" style="text-align:right" format="###,##0.00"  cell="number"/>
	</ec:row>
	
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="5" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
				<fmt:formatNumber value="${countTotal}" pattern="#,##0.00"/>
			</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
								<fmt:formatNumber value="${amoutTotal}" pattern="#,##0.00"/>
		</td>
				
			</tr>
		</ec:extendrow>
	</s:if>
	
	</ec:table>
	</div>
	</div>
</div>


<div id="win" class="x-hidden">
    <div class="x-window-header">角色列表</div>
    <div id="role_grid"></div>
</div>
<script type="text/javascript" src="${ctx}/pages/admin/security/user/user.js">
</script>
<div id='load-mask'></div>
</body>
</html>