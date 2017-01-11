<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>

<html>
<head>
<title>客户信息</title>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
function remove(id){
	if (confirm("确认要删除该客户吗?")){
		window.location.href="remove.do?model.id=" + id;
	}
}

function removeAo(){
	alert("此客户下面已有代币卡信息，不能删除！");
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">客户信息</div>
</div>
<div class="x-toolbar">
      <table width="99%">
        <tr>
        <s:form action="initIndex" theme="simple"> 
	        &nbsp;类&nbsp;别：
	        <s:select name="model.type" cssStyle="width:60px" list='#{"1":"会员客户","0":"普通客户"}' headerKey="" headerValue="全部" />    
   
		           地区：
	        <span id='comboxWithTree' style="width: 10px;"></span>              
			<s:hidden id="regionId" name="model.region.id"/>
		  姓名：<s:textfield id="name"  name="model.name" size="8" cssStyle="height:19px;"/>
	      身份证号：<s:textfield id="idCard"  name="model.idCard" size="20" cssStyle="height:19px;"/>
	     <!--  <td>电话：<s:textfield  id="phone" name="model.phone" size="10" cssStyle="height:19px;"/></td> -->
          <input type="submit" value="查询" class="button"/>
         </s:form>
       </tr>
     </table>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="initIndex.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="客户应收.xls"
		pageSizeList="15,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="345px"
		minHeight="345"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status">
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" viewsAllowed="html" style="text-align:center"/>
		<ec:column width="85" property="region.name" viewsAllowed="html" title="地区" ellipsis="true"/>
		<ec:column width="75" property="name" title="客户姓名" style="text-align:center" ellipsis="true"/>
		<ec:column width="130" property="idCard" title="身份证号"/>
		<ec:column width="85" property="agent.name" viewsAllowed="html" title="从属经销商" style="text-align:center" ellipsis="true"/>
		<ec:column width="110" property="phone" viewsAllowed="html" title="电话" ellipsis="true"/>
		<ec:column width="60" property="type" viewsAllowed="html" title="类别" style="text-align:center">
			<s:if test="#attr.item.type == 0">
				<font color="green">普通客户</font>
			</s:if>
			<s:else>
				<font color="red">会员客户</font>
			</s:else>
		</ec:column>
		<ec:column width="60" property="_status" viewsAllowed="html" title="状态" style="text-align:center;">
		  <s:if test="#attr.item.status == 1">
		  	<img src="${ctx}/images/icons/accept.gif" title="可用">
		  </s:if>
		  <s:elseif test="#attr.item.status == 0">
		    <img src="${ctx}/images/grid/clear.gif" title="禁用">
		  </s:elseif>
		 </ec:column>
		 <ec:column width="80" property="_0" title="应收金额" ellipsis="true"/>
	</ec:row>
</ec:table>
</div>
</div>

<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"></script>
<script type="text/javascript">
Ext.onReady(function() {
	var pstree = new RegionTree({
		el : 'comboxWithTree',
		target : 'model.region.id',
		//emptyText : '选择商品类型',
		comboxWidth : 200,
    	treeWidth : 195,
		url : '${ctx}/admin/region/regionTree.do',
		defValue : {id:'${model.region.id}',text:'${model.region.name}'}
	});
	pstree.init();	
	
});
</script>
</body>
</html>