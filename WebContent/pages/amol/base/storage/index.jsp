<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>仓库信息</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">

function remove(id){
	if (confirm("确认要删除该仓库吗?")){
		window.location.href="remove.do?model.id=" + id;
	}
}
function removeAo(){
	alert('删除失败,该仓库下已货物存储!');
}
</script>
<script type="text/javascript">
function validateStorageIsEmpty(){
	$.ajax({
		url : "${ctx}/base/storage/validateStorageIsEmpty.do",
		type : "POST",
		data :{},
		dataType : "json",
		success : function(result) {
			//如果执行成功，判断是否执行更新盈亏表操作
			if (result.success) {
				window.location.href="${ctx}/base/storage/second/index.do";
			}else{
				alert('当前用户没有仓库，请先进行仓库创建，再进行分销商仓库创建!');
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			Ext.MessageBox.show({
				title : '提示',
				minWidth : 220,
				msg : XMLHttpRequest + "@@" + textStatus + "@@@"
						+ errorThrown,//"发生异常，请与管理员联系！",
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
		}
	});
}

</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">经销商仓库管理</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
<table width="99%">
	<tr>
		<td><s:form action="index" theme="simple">
        		状&nbsp;&nbsp;态：<s:select name="conditionStatus"
				list='#{"1":"可用","0":"停用"}' headerKey="" headerValue="全部 " cssStyle="width:100px;"/>
        	仓库名称：<s:textfield name="conditionName" size="30" />&nbsp;&nbsp;
        	<s:submit value="查询" cssClass="button"></s:submit>
		</s:form></td>
		<td align="right">
		<table>
			<tr>
				<td><a href="edit.do" title="添加仓库信息"><img
					src="${ctx}/images/icons/add.gif" />添加经销商仓库</a></td>
				<td><span class="ytb-sep"></span></td>
				<td><a href="#" onclick="validateStorageIsEmpty()" title="分销商仓库信息"><img
					src="${ctx}/images/icons/add.gif" />分销商仓库</a></td>
			</tr>
		</table>
		</td>
	</tr>
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
		<ec:column width="30" property="_0" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center" />
		<ec:column width="170" property="name" title="仓库名称" ellipsis="true" />
		<ec:column width="80" property="createTime" style="text-align:center" cell="date" sortable="false" title="创建时间"/>
		<ec:column width="170" property="address" title="地址" ellipsis="true" />
		<ec:column width="170" property="descn" title="备注" ellipsis="true" />
		<ec:column width="40" property="status" title="状态" style="text-align:center">
			<s:if test="#attr.item.status == 0">
				<img src="${ctx}/images/grid/clear.gif" title="禁用">
			</s:if>
			<s:else>
				<img src="${ctx}/images/icons/accept.gif" title="可用">
			</s:else>
		</ec:column>
		<ec:column width="170" property="_1" title="操作"
			style="text-align:center" sortable="false">
			<a href="edit.do?model.id=${item.id}" title="修改仓库信息">编辑</a> |
			<s:if test="#attr.item.status == 0">
				<a href="editStatus.do?model.id=${item.id}&model.status=1" title="可用"><font
					color="green">可用</font></a> | <font color="#999999">停用</font>
			</s:if>
			<s:else>
				<font color="#999999">可用</font> | <a href="editStatus.do?model.id=${item.id}&model.status=0" title="停用"> 停用</a>
			</s:else>
			<a href="#" onclick="remove(${item.id})" title="删除仓库信息"> | 删除</a>
		</ec:column>
	</ec:row>
</ec:table></div>
</div>
</body>
</html>