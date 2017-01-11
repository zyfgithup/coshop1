<%@page import="com.systop.amol.stock.StockConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>库量上下限提醒表</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">库量上下限提醒表</div>
<div class="x-toolbar">
<table width="99%">
	<s:form action="index.do" theme="simple">
	<tr>
		<td>
			经销商级别：<s:select name="conditionSuperiorSign"	list='#{"2":"分销商"}' 
        				headerKey="1" headerValue="经销商" cssStyle="width:120px;"/>
        	仓库名称： <s:select id="storageMap" list="storageMap" name="conditionId" headerKey="" 
                 headerValue="查看全部" cssStyle="width:120px;" />&nbsp;&nbsp;
            <s:if test="conditionSuperiorSign != 2">
            	报警提醒 <s:select id="awokeSign" name="awokeSign"list="#{'1':'上限报警','0':'下限报警'}" 
						headerKey="" headerValue="所有报警提醒"/>&nbsp;&nbsp;
            </s:if>
		</td>
		<td rowspan="2" valign="middle"">
			<s:submit value="查询" cssClass="button"></s:submit>			
		</td>
	</tr>
	<tr>
		<td colspan="2">
			商&nbsp;品&nbsp;&nbsp;编&nbsp;码：<s:textfield name="conditionCode" size="18" />
			&nbsp;商品名称：&nbsp;&nbsp;<s:textfield name="conditionName" size="18" />&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
	</s:form>	
</table>
</div>
<div class="x-panel-body"><ec:table items="items" var="item"
	retrieveRowsCallback="limit" sortRowsCallback="limit" action="index.do"
	useAjax="false" doPreload="false" pageSizeList="15,20,50"
	editable="false" sortable="false" rowsDisplayed="15"
	generateScript="true" 
	resizeColWidth="true" 
	classic="true" 
	width="100%"
	height="400px"	
	minHeight="400"
	toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
		<ec:column width="30" property="_0" title="No."
			value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center" />
		<ec:column width="80" property="storageName" title="仓库名称" ellipsis="true" />
		<ec:column width="80" property="productCode" title="商品编码" ellipsis="true" />
		<ec:column width="177" property="productName" title="商品名称" ellipsis="true" />
		<ec:column width="177" property="productStardard" title="商品规格" ellipsis="true" />
		<ec:column width="60" property="unitName" title="单位" ellipsis="true" />
		<ec:column width="80" property="productMaxCount" title="上限" ellipsis="true" />
		<ec:column width="80" property="productMinCount" title="下限" ellipsis="true" />
		<ec:column width="80" property="factCount" title="实际库存" ellipsis="true" />
		<ec:column width="80" property="_1" title="包装单位" ellipsis="true" >
			<s:if test="#attr.item.unitPack == null || #attr.item.unitPack == ''">
				0
			</s:if>
			<s:else>
				${item.unitPack}
			</s:else>
		</ec:column>
	</ec:row>
</ec:table></div>
</div>
</body>
</html>