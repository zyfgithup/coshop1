<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>村分销商积分兑换汇总</title>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">村分销商积分兑换汇总</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form action="${ctx}/jfSalesOrder/index.do" method="post">
	            &nbsp;&nbsp;村分销商：<s:textfield name="name" size="10" id="name"/>
		      <input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button"/>
           </form>
         </td>
       </tr>
     </table>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="积分汇总.xls" 
		pageSizeList="10,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="10"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="320px"
		minHeight="320"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status" >
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="100" property="name" title="村分销商" ellipsis="true"/>
		<ec:column width="200" property="count" title="兑换积分总数" style="text-align:center"/>
		<ec:column width="200" property="count" title="兑换积分总数" style="text-align:center"/>
		<ec:column width="200" property="num" title="商品总数" style="text-align:center"/>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>