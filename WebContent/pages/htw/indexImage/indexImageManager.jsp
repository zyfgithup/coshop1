<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>首页图片管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function remove(id){
	if (confirm("确认删除该信息吗？")) {
		window.location.href="${ctx}/indexImage/remove.do?pushMessageId=" + id;
    }	
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">首页图片管理</div>
	<div style="float: right;">
		<a href="${ctx}/user/agent/index.do"><img src="${ctx}/images/icons/add.gif" style="width: 12px; height: 12px;" />&nbsp;返回&nbsp;</a>
	</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
     <table width="99%">
        <tr>
          <s:form action="index.do" method="post">
          <td>
                                文件名称：<s:textfield name="model.title" id="name" cssStyle="width:110px;"/>
		    <s:submit value="查询" cssClass="button"  cssStyle="width:50px;"></s:submit>   
          </td>
        </s:form>
        </tr>
      </table>
</div>
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="首页图片信息.xls"
		pageSizeList="15,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="350px"
		minHeight="350"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
	<ec:row>
	    <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="130" property="title" title="文件名称"></ec:column>
		<ec:column width="200" property="content" title="内容" ellipsis="true"/>
		<ec:column width="150" property="createTime" title="创建时间" style="text-align:center" cell="date" format="yyyy-MM-dd HH:mm:ss"/>
		<!-- ec:column width="300" property="businessName" title="商品" ellipsis="true" style="text-align:center"/> -->
		<ec:column width="130" property="_title" title="图片">
			 <a href="${ctx }/${item.imageURL }"><img alt="${item.title }" src="${ctx }/${item.imageURL }" width="60" height="20"/></a>
		</ec:column>
		<ec:column width="70" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			<!--  a href="view.do?pushMessageId=${item.id}" title="查看">查看</a> | 
			<a href="edit.do?pushMessageId=${item.id}" title="修改">修改</a> |--> 
			<a href="javascript:remove(${item.id});" title="删除">删除</a>
		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>