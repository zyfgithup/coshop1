<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>推送管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function removeAo(){
	alert("该信息已经推送不能修改或删除！");
}
function push(id){
	if (confirm("确认推送该信息吗？")) {
		window.location.href="${ctx}/pushMessage/push.do?pushMessageId=" + id;
    }
}
function remove(id){
	if (confirm("确认删除该信息吗？")) {
		window.location.href="${ctx}/pushMessage/remove.do?pushMessageId=" + id;
    }	
}

function push(id,title,content){
	if (confirm("确认推送该信息吗？")) {
		window.location.href="${ctx}/pushMessage/push.do?pushMessageId=" + id+"&title="+title+"&content="+content;
    }	
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">推送管理</div>
	<div style="float: right;">
		<a href="${ctx}/pushMessage/edit.do"><img src="${ctx}/images/icons/add.gif" style="width: 12px; height: 12px;" />&nbsp;添加&nbsp;</a>
	</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
     <table width="99%">
        <tr>
          <s:form action="index.do" method="post">
          <td>
                                标题名称：<s:textfield name="model.title" id="name" cssStyle="width:110px;"/>
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
		xlsFileName="推送信息.xls" 
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
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status">
	<ec:row>
	    <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="130" property="title" title="标题"></ec:column>
		<ec:column width="200" property="content" title="内容" ellipsis="true"/>
		<ec:column width="150" property="createTime" title="创建时间" style="text-align:center"/>
		<ec:column width="200" property="_0" title="操作" style="text-align:center" sortable="false">
			<a href="view.do?pushMessageId=${item.id}" title="查看">查看</a> | 
			<c:if test="${item.status == 'SUCCESS'}">
			  <a href="javascript:removeAo();">
			     <font color="#999999">修改</font>
			  </a>|
			   <a href="javascript:removeAo();">
			     <font color="#999999">删除</font> | 
			  </a>
			</c:if>
			<c:if test="${item.status!='SUCCESS'}">
			  <a href="edit.do?pushMessageId=${item.id}" title="修改">修改</a> | 
			<a href="javascript:remove(${item.id});" title="删除">删除</a> |
			</c:if>
			<a href="javascript:push('${item.id}','${item.title}','${item.content}');" title="推送">推送</a>
		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>