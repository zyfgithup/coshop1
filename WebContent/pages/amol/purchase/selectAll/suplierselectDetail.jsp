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
	form1.action="${ctx}/purchase/selectAll/suplierindex.do";
	form1.submit();
}

</script>
</head>
<body>
<div class="x-panel">
  <div class="x-panel-header">采购查询</div>
    <div class="x-toolbar">
      <table width="99%">
        <tr>
          <td> 
        <s:form action="suplierindexDetail" theme="simple" id="form1">
                                类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型：<s:select list='pselectMap' name="findType"></s:select>&nbsp;
                                操 作 人：<s:textfield name="users" size="12"></s:textfield>                    
        	商品编码：<s:textfield name="model.products.code" size="12"/>&nbsp;&nbsp;
        	商品名称：<s:textfield name="model.products.name" size="12"/>&nbsp;&nbsp;
        	<br>
        	开始日期：<input id="startDate" name="startDate" size="12"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
        	结束日期：<input id="endDate" name="endDate" size="12" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp; 	
        	    <s:submit value="查询" cssClass="button" cssStyle="width:40px;"/>
        		<s:submit value="返回" cssClass="button" onclick="xdcx()" cssStyle="width:40px;"/>
         </s:form>
        </td>
        </tr>
      </table>
    </div>   
    <div class="x-panel-body">
    <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="process" sortRowsCallback="process" 
		action="suplierindexDetail.do"
		useAjax="true" doPreload="false"
		xlsFileName="采购查询.xls"
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="true"	
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
	   
		<ec:column width="40" property="_0" title="No." value="${GLOBALROWCOUNT}" sortable="false" />
		<ec:column width="100" property="purchase.supplier.name" title="供应商" ellipsis="true">
		<s:if test="#attr.item.purchase == null">
				小计
			</s:if>
			<s:else>
				${item.purchase.supplier.name}
			</s:else>
		
		</ec:column>		
		<ec:column width="80" property="purchase.user.name" title="操作人" ellipsis="true"/>
		<ec:column width="100" property="purchase.sno" title="单号" ellipsis="true"/>
		<ec:column width="80" property="purchase.sdate" title="日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="60" property="purchase.storage.name" title="仓库" ellipsis="true"/>
		<ec:column width="80" property="products.code" title="商品编码" ellipsis="true"/>
		<ec:column width="80" property="products.name" title="商品名称" ellipsis="true"/>
		<ec:column width="80" property="products.stardard" title="规格" ellipsis="true"/>
		<ec:column width="70" property="units.name" title="单位" />
		<ec:column width="80" property="ncount" title="数量" />
			<ec:column width="60" property="bagCount" title="包装数量" />
		<ec:column width="80" property="price" title="单价" />
		<ec:column width="80" property="amount" title="金额" style="text-align:right" format="###,##0.00"  cell="number"/>
        
	
	</ec:row>
	
	
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="11" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>
				
				<td style="border: 0px" ></td>
				<td style="border: 0px" ></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: left">
				<fmt:formatNumber value="${amoutTotal}" pattern="#,##0.00"/></td>
				
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