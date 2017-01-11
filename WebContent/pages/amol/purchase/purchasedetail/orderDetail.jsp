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
	form1.action="${ctx}/purchase/order/index.do?model.sno="+sno.value+"&model.supplier.name="+sid.value;
	form1.submit();
}

</script>
</head>
<body>

<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">订单详单查询</div>
	<div style="float: right;">
	<a href="#" onclick="xdcx()"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>&nbsp;返回&nbsp;&nbsp;</a>
	</div>
</div>  
<div class="x-toolbar">
    <table width="99%">
        <tr>
          <td> 
          <s:form action="index" theme="simple" id="form1">
        	<table>
            <tr>
            <td>供应商</td>
               <td><s:textfield name="model.purchase.supplier.name" id="sid" cssStyle="width:90px" /></td>
        	<td>商品编码</td>
        	<td><s:textfield name="model.products.code" size="8"/></td>
        	<td>商品名称</td>
        	<td><s:textfield name="model.products.name" size="9"/></td>
            <td>单号</td>
            <td><s:textfield name="model.purchase.sno" size="15" id="sno"/></td>
        	<td>开始日期</td>
        	<td><input id="startDate" name="startDate" size="10"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/></td>
        	<td>结束日期</td>
        	<td><input id="endDate" name="endDate" size="10" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/></td>
        	<td>
        	<s:submit value="查询" cssClass="button"></s:submit>
            </td>
           </tr>
           </table>
         </s:form>
        </td> 
         <td align="right">
           <table>  
          	  <tr>
         	  </tr>
            </table>
          </td>
        </tr>
      </table>
    </div>   
    <div class="x-panel-body">
    <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="process" sortRowsCallback="process" 
		action="index.do"
		useAjax="true" doPreload="false"
		xlsFileName="订单详单.xls" 
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="380px"	
		minHeight="380"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"      
	>
	
	<ec:row>
		<ec:column width="30" property="_0" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="100" property="purchase.sno" title="单号" style="text-align:center"/>
		<ec:column width="80" property="purchase.sdate" title="日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="100" property="purchase.supplier.name" title="供应商" >
		<s:if test="#attr.item.purchase == null">
				小计
			</s:if>
			<s:else>
				${item.purchase.supplier.name}
			</s:else>
		</ec:column>
		<ec:column width="60" property="products.code" title="商品编码" />
		<ec:column width="80" property="products.name" title="商品名称" />
		<ec:column width="60" property="products.stardard" title="规格" ellipsis="true"/>
		<ec:column width="60" property="units.name" title="包装单位" />
		<ec:column width="50" property="ncount" title="数量" />
		<ec:column width="60" property="bagCount" title="包装数量" />
		<ec:column width="60" property="linkCount" title="关联数量" />
		<ec:column width="50" property="price" title="单价" style="text-align:right" format="#####0.00" cell="number"/>
		<ec:column width="80" property="amount" title="金额" style="text-align:right" format="###,##0.00"  cell="number"/>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="11" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>

				<td style="border: 0px" ></td>
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