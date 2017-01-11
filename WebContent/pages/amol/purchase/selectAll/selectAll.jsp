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
	var sid=document.getElementById("sid");
	form1.action="${ctx}/purchase/selectDetail/indexDetail.do?model.purchase.supplier.name="+sid.value;
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
        <s:form action="index" theme="simple" id="form1">
                                查询类型：<s:select list='selectMap' name="findType"></s:select>
                                 供应商：<s:textfield name="model.supplier.name" id="sid" cssStyle="width:150px" />
        	开始日期：<input id="startDate" name="startDate" size="12"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
        	结束日期：<input id="endDate" name="endDate" size="12" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp; 	
        	<s:submit value="查询" cssClass="button"></s:submit>
        	<s:submit value="详单" cssClass="button" onclick="xdcx()"></s:submit>
         </s:form>
        </td>
        </tr>
      </table>
    </div>   
    <div class="x-panel-body">
    <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do"
		useAjax="true" doPreload="false"
		xlsFileName="采购查询.xls"
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
		<ec:column width="110" property="sno" title="单号" style="text-align:center"/>
		<ec:column width="150" property="supplier.name" title="供应商" ellipsis="true"/>	
		<ec:column width="90" property="sdate" title="日期" style="text-align:center" cell="date"/>
		<ec:column width="80" property="samount" title="应付" style="text-align:right" format="###,##0.00"  cell="number"/>
		  <s:if test="#attr.findType != 0">
		<ec:column width="80" property="spayTotal" title="实付" style="text-align:right" format="###,##0.00"  cell="number"/>
		</s:if>
		<ec:column width="100" property="remark" title="备注" ellipsis="true"/>
		<ec:column width="110" property="user.name" title="操作人" />				
		<ec:column width="80" property="_2" title="操作" style="text-align:center"  sortable="false" viewsDenied="xls">
           <s:if test="#attr.findType eq 0">
		   <a href="${ctx}/purchase/order/view.do?model.id=${item.id}">查看</a>
		   </s:if>	   
		    <s:if test="#attr.findType eq 1">
		   <a href="${ctx}/purchase/view.do?model.id=${item.id}">查看</a>
		   </s:if>
		     <s:if test="#attr.findType eq 2">
		   <a href="${ctx}/purchase/returns/view.do?model.id=${item.id}">查看</a>
		   </s:if>
		</ec:column>
	</ec:row>
	
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="4" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
					<fmt:formatNumber value="${sfTotal}" pattern="#,##0.00"/></td>
				 <s:if test="#attr.findType != 0">
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
					<fmt:formatNumber value="${payTotal}" pattern="#,##0.00"/></td>
				</s:if>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
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