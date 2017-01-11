<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>库存盘点管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function remove(id){
    if(confirm('确认要删除该盘点单吗？')){
        window.location.href="${ctx}/stock/check/remove.do?model.id=" + id;
    }	
}
</script>
</head>
<body>
<div class="x-panel">
	<div class="x-panel-header" >
	<div style="float: left;">库存盘点</div>
	<s:if test="model.user.beginningInit== 1">
	<div style="float: right;font-weight:normal;">
		<a href="${ctx}/stock/check/detail/edit.do" title="添加盘点单">
		<img src="${ctx}/images/icons/add.gif" />添加盘点单</a>
	</div>
	</s:if>
	</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
   <table width="100%">
     <tr>
     <td> 
        <form action="${ctx}/stock/check/index.do" method="post">   
			盘点单号：<s:textfield name="model.checkNo" size="18" id="checkNo"/> 
			仓库：<s:select list="storageMap" name="model.storage.id" headerKey="" 
               headerValue="全部" cssStyle="width:100px;border: 1px solid ;"/>	                      
      		开始日期：<input id="startDate" name="startDate" size="12"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
      		结束日期：<input id="endDate" name="endDate" size="12" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;  	                      
       		<input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button"/>
         </form>
      </td>
    </tr>
  </table>
</div>
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
	   action="index.do"
		useAjax="true" doPreload="false"
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="true"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="400px"	
		minHeight="400"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
	   <ec:column width="30" property="_No" title="No."
			value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center" />
		<ec:column width="130" property="checkNo" title="盘点单号" style="text-align:center"/>
		<ec:column width="200" property="storage.name" ellipsis="true" title="仓库" />
		<ec:column width="100" property="createTime" title="创建时间" cell="date" style="text-align:center"/>
		<ec:column width="200" property="user.name" ellipsis="true" title="创建人" style="text-align:center"/>
		<ec:column width="170" property="_0" title="操作" style="text-align:center" sortable="false">
			<a href="${ctx}/stock/check/view.do?model.id=${item.id}" title="查看">详情</a> | 	
		    <s:if test="#attr.item.status == 0 || #attr.item.status == 2">
					<a href="${ctx}/stock/check/detail/edit.do?model.stockCheck.id=${item.id}" title="修改">编辑</a> |
			</s:if>
			<s:else>
                    <font color="silver" >编辑</font> |
			</s:else>	
		    <s:if test="#attr.item.status == 0 || #attr.item.status == 2">
		    	<a href="#" onclick="remove('${item.id}')" title="删除">删除</a>
		    </s:if>
		    <s:else>
				<font color="silver" >删除</font>		    	
		    </s:else>
		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>