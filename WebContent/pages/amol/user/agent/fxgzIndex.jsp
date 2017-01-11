<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>返现规则</title>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function refresh() {
	  ECSideUtil.reload('ec');
	}
function remove(id){
    if (confirm("确认要删除规则吗?")){
	  window.location.href="remove.do?model.id=" + id;
    }
  }

</script>
<!-- 地区 -->
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox1.js"> </script>
<script type="text/javascript" src="${ctx}/pages/amol/base/prosort/mersortCombox.js"></script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
</head>
<body>
  <div class="x-panel">
    <div class="x-panel-header">返现规则</div>
    <div class="x-toolbar">
	    <s:form action="index" theme="simple">
	          <table width="100%">
        <tr>
        	<td>
        		<!-- <table>
			        <s:form action="index" theme="simple">
						<td align="right">商家名称：</td>
							<td align="left" colspan="3">
						      <span id='comboxmerWithTree' style="width: 145px;"></span>
						      <s:hidden id="name" name="model.user.name" cssClass="prosortCheck"/>
							</td>
			        	<td>
			        		<s:submit value="查询" cssClass="button"></s:submit>
			        	</td>
			         </s:form>
		         </table> -->
		     </td>
	         <td style="padding-right:10px;" align="right">
	           <table>
	          	  <tr>
			         <td><a href="${ctx}/merchant/fxgz/edit.do"><img src="${ctx}/images/icons/add.gif"/> 添加</a></td>
	         	  </tr>
	            </table>
	          </td>
        </tr>
      </table>
	     </s:form>
   </div>   
   <div class="x-panel-body">
     <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
	    action="index.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="返现规则.xls"
		pageSizeList="15,50,100" 
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
		  <ec:column width="100" property="_fxNum" title="返现比例">
			  ${item.fxNum}%
		  </ec:column>
		   <ec:column width="60" property="_type" title="返现类型">
			   <c:if test="${item.type == '0'}">
				充值返现
			   </c:if>
			   <c:if test="${item.type == '1'}">
				   分拥返现
			   </c:if>
		   </ec:column>
		 <ec:column width="350" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			<a href="delete.do?model.id=${item.id}" >废弃</a>
		 </ec:column>
	   </ec:row>
      </ec:table>
    </div>
  </div>
</body>
</html>