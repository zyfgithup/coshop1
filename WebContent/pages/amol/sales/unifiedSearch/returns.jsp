<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>销售查询【现金】</title>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">销售查询【现金】</div>
<div class="x-toolbar">
	<form action="${ctx }/salesEnquiries/index.do" method="post">
          	&nbsp;&nbsp;单子类型:<s:select list='listTypeMap' name="listType"></s:select>  
	                      单号:<s:textfield name="model.salesNo" size="17" id="salesNo"/> 
	                      用户:<s:textfield name="model.customer.name" size="10" id="name"/>
        	开始日期:<input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
       		结束日期:<input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
       		<input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button" style="width:36px;"/>
    </form>
</div> 
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="${ctx }/salesEnquiries/index.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="退货单.xls" 
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
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="100" property="user.loginId" title="用户" style="text-align:center" ellipsis="true"/>
		<ec:column width="100" property="user.name" title="姓名" style="text-align:center" ellipsis="true"/>
		<ec:column width="200" property="_regionName" title="所属地区"  ellipsis="true">
		 	${item.user.region.parent.parent.name }&nbsp;&nbsp;${item.user.region.parent.name }&nbsp;&nbsp;${item.user.region.name }
		 </ec:column>
		<ec:column width="140" property="salesNo" title="退货单号" style="text-align:center"/>
		<ec:column width="120" property="createTime" title="退单日期" style="text-align:center" cell="date"/>
		<ec:column width="80" property="rttao" title="退款金额" style="text-align:right" format="###,##0.00" cell="number"/><!-- 退货货款金额 -->
		<ec:column width="150" property="remark" title="备注" ellipsis="true"/>
		<ec:column width="70" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			<a href="${ctx }/salesReturns/view.do?model.id=${item.id}" title="查看销售退货信息">详情</a>
		</ec:column>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="4" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px;">金额单位为“元”，退款合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${rttao }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${realReturnMoney }" pattern="#,##0.00"/></td>
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