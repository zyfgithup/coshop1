<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>

<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>

<title></title>

</head>
<body>
<div class="x-panel">
  <div class="x-panel-header">采购汇总</div>
    <div class="x-toolbar">
      <table width="99%">
        <tr>
          <td> 
        <s:form action="index" theme="simple">
        	供   应  商：<s:textfield name="supplier" size="15" />&nbsp;&nbsp;
        	商品名称：<s:textfield name="productName" size="10"/>&nbsp;&nbsp;
        	商品规格：<s:textfield name="productStardard" size="10"/><br>
        	开始日期：<input id="startDate" name="startDate" size="15"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
        	结束日期：<input id="endDate" name="endDate" size="15" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp; 	
        	<s:submit value="查询" cssClass="button"></s:submit>
         </s:form>
        </td>
        </tr>
      </table>
    </div>   
    <div class="x-panel-body">
    <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="process" sortRowsCallback="process" 
		action="index.do"
		useAjax="true" doPreload="false"
		xlsFileName="采购汇总.xls"
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="true"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="345px"	
		minHeight="345"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"   
	>
	<ec:row>
	   
		<ec:column width="40" property="_0" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
	    <ec:column width="100" property="product.code" title="商品编码" ellipsis="true"/>
	    <ec:column width="100" property="product.name" title="商品名称" ellipsis="true"/>
		<ec:column width="60" property="product.stardard" title="规格" ellipsis="true"/>
		<ec:column width="60" property="product.units.name" title="单位" />
		<ec:column width="80" property="binitCount" title="期初数量" style="text-align:right"/>
		<ec:column width="80" property="initAmount" title="期初金额"   style="text-align:right" format="###,##0.00"  cell="number"/>
		<ec:column width="80" property="binCount" title="入库数量" style="text-align:right">
		      <s:property value="@com.systop.amol.stock@getUnitPack(this.,nickName,'UTF-8')"/>
		</ec:column>
		<ec:column width="80" property="inAmount" title="入库金额"   style="text-align:right" format="###,##0.00"  cell="number"/>
		<ec:column width="80" property="breturnCount" title="退货数量" style="text-align:right"/>
		<ec:column width="80" property="returnAmount" title="退货金额"   style="text-align:right" format="###,##0.00"  cell="number"/>
        <ec:column width="80" property="bhzcount" title="汇总数量" style="text-align:right" />
		<ec:column width="80"  property="_2" title="汇总金额"   style="text-align:right" format="###,##0.00"  cell="number">${item.initAmount+item.inAmount-item.returnAmount}</ec:column>
	
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="6" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>
		
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
					<fmt:formatNumber value="${initaTotal}" pattern="#,##0.00"/></td>
				<td ></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
				<fmt:formatNumber value="${inaTotal}" pattern="#,##0.00"/></td>
				<td ></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
				<fmt:formatNumber value="${raTotal}" pattern="#,##0.00"/></td>
				<td ></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
				<fmt:formatNumber value="${initaTotal+inaTotal-raTotal}" pattern="#,##0.00"/></td>
				
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
<script type="text/javascript">

//$().ready(function() {
	//$.ajax({
		//url: '${ctx}/purchase/purchasetotal/getAutoSupplier.do',
		//type: 'post',
		//dataType: 'json',
		//success: function(rows, textStatus){
			// $("#sname").autocomplete(rows, {
				//  matchContains: true,
				 // minChars: 0
		   //});
	//	}
	//});
// });
</script>
<div id='load-mask'></div>
</body>
</html>