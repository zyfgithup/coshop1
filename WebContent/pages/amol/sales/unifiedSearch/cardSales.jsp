<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>销售查询【网银】</title>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">销售查询【网银】</div>
	<div style="float: right;">
	</div>
</div>

<div class="x-toolbar">
   <form action="${ctx }/salesEnquiries/cardIndex.do" method="post">
          	&nbsp;&nbsp;单子类型:<s:select list='listTypeMap' name="listType"></s:select>  
	                      单号:<s:textfield name="model.salesNo" size="17" id="salesNo"/> 
	                      客户:<s:textfield name="model.customer.name" size="10" id="name"/>
        	开始日期:<input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
       		结束日期:<input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>  
       		<input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button" style="width:36px;"/>
   </form>
</div> 
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="${ctx }/salesEnquiries/cardIndex.do"
		useAjax="false"
		doPreload="false" 
		xlsFileName="出库单.xls" 
		pageSizeList="10,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="10"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="350px"
		minHeight="350"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
	<ec:row>
	    <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="70" property="customer.name" title="客户" style="text-align:center" ellipsis="true"/>
		<ec:column width="105" property="salesNo" title="销售单号" style="text-align:center"/>
		<ec:column width="60" property="payment.name" title="收款方式" style="text-align:center"/>
		<ec:column width="70" property="createTime" title="日期" cell="date" style="text-align:center"/>
		<ec:column width="60" property="samount" title="应收金额" style="text-align:right" format="###,##0.00" cell="number"></ec:column>
		<ec:column width="73" property="cardamount" title="代币卡消费" style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="60" property="spayamount" title="实收金额" style="text-align:right" format="###,##0.00" cell="number"></ec:column>
		<ec:column width="60" property="rttao" title="退款金额" style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="60" property="w" title="未收金额" style="text-align:right" format="###,##0.00" cell="number">
		    ${item.samount - item.rttao - item.spayamount }
		</ec:column>
		<ec:column width="70" property="redRed" title="状态" style="text-align:center">
          <s:if test="#attr.item.redRed == 1">正常 </s:if>
		  <s:if test="#attr.item.redRed == 2">已冲红 </s:if>
		  <s:if test="#attr.item.redRed == 3">冲红单 </s:if>
		</ec:column>
		<ec:column width="60" property="ckzt" title="退货状态" style="text-align:center" mappingItem="returnStatusMapS"/>
		<ec:column width="80" property="user.name" title="操作人" style="text-align:center" ellipsis="true"></ec:column>
		<ec:column width="60" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			<a href="${ctx }/sales/view.do?model.id=${item.id}" title="查看销售出库信息">详情</a>
		</ec:column>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="5" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px;">金额单位为“元”，合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${samount }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${cardamount }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${spayamount }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${rttao }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${cardamount - spayamount - rttao }" pattern="#,##0.00"/></td>
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