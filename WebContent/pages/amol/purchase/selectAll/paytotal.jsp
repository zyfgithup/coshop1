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
  <div class="x-panel-header">应付明细查询</div>
    <div class="x-toolbar">
      <table width="99%">
        <tr>
          <s:form action="index" theme="simple" id="form1">        
          <td> 
                                供应商：  <s:textfield name="supplier" id="sid" cssStyle="width:100px" />
                               状态：<s:select list='#{"0":"全部","1":"未付款" }'   name="status" cssStyle="width:60px" ></s:select>
                               单据编号：<s:textfield name="billNo" size="15"></s:textfield>                    
        	开始日期：<input id="startDate" name="startDate" size="12"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
        	结束日期：<input id="endDate" name="endDate" size="12" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>	
        	<s:submit value="查询" cssClass="button"></s:submit>
          </td>
          </s:form>
        </tr>
      </table>
    </div>   
    <div class="x-panel-body">
    <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="process" sortRowsCallback="process" 
		action="index.do"
		useAjax="true" doPreload="false"
		xlsFileName="应付明细.xls"
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
	   
		<ec:column width="40" property="_0" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
	    <ec:column width="120" property="supplier" title="供应商" style="text-align:center"/>
	    <ec:column width="50" property="billType" title="类型" style="text-align:center"/>
	    <ec:column width="100" property="billNo" title="入库/退货编号" style="text-align:center"/>
		<ec:column width="90" property="date" title="日期" cell="date" style="text-align:center"/>
		<ec:column width="90" property="pay" title="应付" style="text-align:right" format="###,##0.00"  cell="number"/>
		<ec:column width="90" property="bcpay" title="本次付款" style="text-align:right" format="###,##0.00"  cell="number"/>
		<ec:column width="90" property="nowpay" title="付款合计" style="text-align:right" format="###,##0.00"  cell="number"/>
		<ec:column width="90" property="_2" title="未付款金额" format="###,##0.00"  style="text-align:right" >
		<c:if test="${item.billType != '应付'}">
			<fmt:formatNumber value="${item.pay-item.nowpay}" pattern="#,##0.00"/>
		</c:if>
		</ec:column>
		<ec:column width="80" property="_2" title="操作" style="text-align:center"  sortable="false" viewsDenied="xls">
           <c:if test="${item.billType eq '入库'}">
		   <a href="${ctx}/purchase/view.do?model.id=${item.id}">查看</a>
		   </c:if>	   
		    <c:if test="${item.billType eq '付款'}">
		   <a href="${ctx}/purchase/pay/view.do?model.id=${item.id}">查看</a>
		   </c:if>
		     <c:if test="${item.billType eq '退货'}">
		   <a href="${ctx}/purchase/returns/view.do?model.id=${item.id}">查看</a>
		   </c:if>
		   <c:if test="${item.billType eq '期初'}">
		   <a href="#"><font color="#999999">查看</font></a>
		   </c:if>
		   
		</ec:column>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="5" align="center" id="No1"    
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
					<fmt:formatNumber value="${payTotal}" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
					<fmt:formatNumber value="${bcTotal}" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
					<fmt:formatNumber value="${nowTotal}" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
					<fmt:formatNumber value="${payTotal-nowTotal}" pattern="#,##0.00"/></td>
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