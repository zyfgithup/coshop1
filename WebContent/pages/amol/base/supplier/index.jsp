<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>供应商管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
function remove(id){
	if (confirm("确认要删除该供应商吗?")){
		window.location.href="remove.do?model.id=" + id;
	}
}

function hiddenDiv(divId){
  document.getElementById(divId).style.display="none";
}

</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">供应商管理</div>
<% 
	if(request.getAttribute("error") != null){
%>
	<div id="errorMessages" style="width:100%;border:1px solid red;">  
	   	<table width="100%" style="border:none;">
	    	<tr>
	    	  <td style="border:none;" width="%97">
	    	  	<img src="${ctx}/images/icons/warning.gif" class="icon" />
		         <%=request.getAttribute("error")==null?"":request.getAttribute("error") %>
	    	  </td>
	    	  <td  style="border:none;" align="right" valign="top">
	    	  	<a href="#" onclick="hiddenDiv('errorMessages')" title="关闭">
	    	  		<img src="${ctx}/images/icons/close.gif"/>
	    	  	</a>
    	  	  </td>
	    	</tr>
	    </table>    
	</div>
<%
	}
%>
<div class="x-toolbar">
      <table width="99%">
        <tr>
        	<td>
        		<table>
	        		<s:form action="index" theme="simple">        
				         <td>
				         	地区：
				         </td>
						  <td>    
					        <span id='comboxWithTree' style="width: 50px;"></span>              
							<s:hidden id="regionId" name="model.region.id" />
						  </td>
						  <td>供应商名称：<s:textfield id="name"  name="model.name"  cssStyle="width:180px;height:19px;"/></td>
					      <td>联系电话：<s:textfield  id="phone" name="model.phone"  cssStyle="width:100px;height:19px;"/></td>
				          <td><input type="submit" value="查询" class="button"/></td>
			         </s:form>
        		</table>
        	</td>
         
         <td style="padding-right:10px;" align="right">
         <table> 
           <tr>
             <td width="100"></td>
             <td><a href="${ctx}/base/supplier/edit.do"><img src="${ctx}/images/icons/add.gif"/>&nbsp;&nbsp;添加供应商</a></td>
           </tr>
         </table>
         </td>
       </tr>
     </table>
</div> 
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="供应商表.xls"
		pageSizeList="15,50,100" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="400px"
		minHeight="400"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status">
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
	    <ec:column width="100" property="region.name" title="地区" ellipsis="true"/>
		<ec:column width="100" property="name" title="供应商名称" ellipsis="true"/>
		<ec:column width="110" property="phone" title="电话" ellipsis="true"/>
		<ec:column width="110" property="mobile" title="手机" ellipsis="true"/>
		<ec:column width="100" property="email" title="电子邮件" ellipsis="true" />
		<ec:column width="110" property="address" title="地址" ellipsis="true"/>
		<ec:column width="40" property="_status" viewsAllowed="html" title="状态" style="text-align:center;">
		  <s:if test="#attr.item.status == 1">
		  	<img src="${ctx}/images/icons/accept.gif" title="可用">
		  </s:if>
		  <s:elseif test="#attr.item.status == 0">
		    <img src="${ctx}/images/grid/clear.gif" title="禁用">
		  </s:elseif>
		 </ec:column>
		
		<ec:column width="120" property="_0" viewsAllowed="html" title="操作" style="text-align:center" sortable="false">
			<a href="view.do?model.id=${item.id}" title="查看详情">详情</a> | 
			<a href="edit.do?model.id=${item.id}" title="修改供应商信息">编辑</a> | 
			<!--<a href="#" onclick="remove(${item.id})" title="删除供应商">删除</a>  	-->
			<s:if test="#attr.item.status == 0">
				<a href="#" onclick="unsealSupplier(${item.id},1)" title="启用">启用</a>		    	
			</s:if> 
			<s:else>
				<a href="#" onclick="unsealSupplier(${item.id},0)" title="禁用">禁用</a>	
			</s:else>
		</ec:column>
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

function unsealSupplier(id,state) {
	if(state == 0){
		if (confirm("确认要禁用该供应商吗？")){
			window.location.href = "unsealSupplier.do?model.id=" + id+"&model.status="+state;
		}
	}else{
		if (confirm("确认要启用该供应商吗？")){
			window.location.href = "unsealSupplier.do?model.id=" + id+"&model.status="+state;
		}
	}
}
</script>
</body>
</html>