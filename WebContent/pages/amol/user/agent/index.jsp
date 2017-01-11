<%@page import="com.systop.common.modules.security.user.model.User"%>
<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>店铺管理</title>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
function refresh() {
	  ECSideUtil.reload('ec');
	}
function remove(id){
    if (confirm("确认要删除该商家吗?")){
	  window.location.href="remove.do?model.id=" + id;
    }
  }

</script>
<!-- 地区 -->
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<script type="text/javascript" src="${ctx}/pages/amol/base/prosort/mersortCombox.js"></script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
Ext.onReady(function() {
	var merstree = new ProsortTree({
		el : 'comboxmerWithTree',
		target : 'prosortId',
		//emptyText : '选择商家类型',
		url : '${ctx}/base/prosort/prosortTree.do?status=1&type=2',
		defValue : {
			id : '${model.productSort.id}',
			text : '${proSortName}'
		}
	});
	merstree.init();
});
Ext.onReady(function() {
	var pstree = new RegionTree({
		el : 'comboxWithTree',
		target : 'regionId',
		//emptyText : '选择地区',
		comboxWidth : 200,
    	treeWidth : 195,
		url : '${ctx}/admin/region/regionTree.do?regionId=null',
		defValue : {id:'${regionId }',text:'${regionNameCun }'}
	});
	pstree.init();	
	
});
function toMenus(){
	window.location.href="${ctx}/user/agent/index.do";
}
</script>
</head>
<body>
  <div class="x-panel">
    <div class="x-panel-header">店铺管理</div>
    <div class="x-toolbar">
	    <s:form action="index" theme="simple">
	          <table width="100%">
        <tr>
        	<td>
        		<table>
			        <s:form action="indexCun" theme="simple">
			        	<td>
			        		状&nbsp;&nbsp;态：<s:select name="status" list='#{"1":"可用","0":"禁用"}' headerKey="" headerValue="全部" cssStyle="width:100px;"/>
			        	</td>
			        	<td align="left">
			        		商店名称：<s:textfield id="name"  name="name" size="35" cssStyle="height:19px;"/>
			        	</td>
			        	<c:if test="${loginUser.type=='admin' }">
			        	<td>
			        		地区：
			        	</td>
			        	<td>
			        		<span id='comboxWithTree'></span>	
						</td>
						<td>
							<s:hidden id="regionId" name="regionId"/>
			        	</td></c:if>
						<td align="right">商家类别：</td>
							<td align="left" colspan="3">
						      <span id='comboxmerWithTree' style="width: 145px;"></span>
						      <s:hidden id="prosortId" name="model.productSort.id" cssClass="prosortCheck"/>
							</td>
			        	<td>
			        		<s:submit value="查询" cssClass="button"  id="searchBtn"></s:submit>
							<s:reset value="重置" cssClass="button" onclick="toMenus()"/>
			        	</td>
			         </s:form>
		         </table>
		     </td>
	         <td style="padding-right:10px;" align="right">
	           <table>  
	          	  <tr>	         
			         <td><a href="${ctx}/user/agent/edit.do?type=add"><img src="${ctx}/images/icons/add.gif"/> 添加</a></td>
	         	  </tr>
	            </table>
	          </td>
        </tr>
      </table>
	     </s:form>
   </div>   
   <div class="x-panel-body">
     <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
	    action="indexCun.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="商家管理.xls" 
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
		 <ec:column width="270" property="region.name" title="所属地区"  ellipsis="true">
		 	${item.region.parent.parent.parent.name }&nbsp;&nbsp;${item.region.parent.parent.name }&nbsp;&nbsp;${item.region.parent.name }&nbsp;&nbsp;${item.region.name }
		 </ec:column>
		 <ec:column width="125" property="name" title="商店名称" ellipsis="true"/>
		 <ec:column width="125" property="_" title="审核情况" ellipsis="true">
		 <c:if test="${item.ifRecommend=='1'}">
			 已通过
		 </c:if>
		  <c:if test="${item.ifRecommend=='0'}">
			 未通过
		 </c:if>
		 </ec:column>
		   <ec:column width="125" property="productSort.name" title="商铺类型" ellipsis="true"/>
		 <ec:column width="125" property="merSortNameStr" title="种类" ellipsis="true"/>
		 <ec:column width="100" property="shopOfUser.name" title="法人姓名"  ellipsis="true"/>
		 <ec:column width="70" property="shopOfUser.loginId" title="用户名" style="text-align:center;" />
		 <ec:column width="120" property="phone" title="电话"/>
		 <ec:column width="120" property="mobile" title="手机"/>
		 <ec:column width="150" property="address" title="地址" ellipsis="true"/>
		<%-- <ec:column width="80" property="allMoney" title="账户余额" ellipsis="true"/>
		 <ec:column width="80" property="incomeAll" title="总收入" ellipsis="true"/>
		 <ec:column width="80" property="integral" title="积分总数" ellipsis="true"/>
		 <ec:column width="80" property="dhIntegral" title="兑换积分总数" ellipsis="true"/>
		 <ec:column width="80" property="allSocres" title="评价分数" ellipsis="true"/>--%>
		 <ec:column width="80" property="_status" title="状态" viewsAllowed="html" style="text-align:center;">
		  <s:if test="#attr.item.status == 1">
		  	<img src="${ctx}/images/icons/accept.gif" title="可用">
		  </s:if>
		  <s:elseif test="#attr.item.status == 0">
		    <img src="${ctx}/images/grid/clear.gif" title="禁用">
		  </s:elseif>
		 </ec:column>
		 <ec:column width="350" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
		   <a href="edit.do?type=edit&model.id=${item.id}" >编辑</a> |
			<%--<c:if test="${item.ifRecommend=='1'}">
			<a href="recomend.do?ifRcommend=0&model.id=${item.id}">取消推荐</a> |
			 </c:if>
			  <c:if test="${item.ifRecommend=='0'}">
				<a href="recomend.do?ifRcommend=1&model.id=${item.id}">推荐</a> |
			 </c:if>--%>
			<a href="delete.do?model.id=${item.id}" >删除</a> |
			<%--<a href="restPass.do?model.id=${item.id}" >重置密码</a> |
			<a href="getValidation.do?model.id=${item.id}" >用户评价</a> |--%>
			 <c:if test="${loginUser.type=='agent'&&loginUser.fxsjb=='agent_level_county' }">
		  </c:if>
		  <c:if test="${loginUser.type=='agent'||loginUser.type=='admin' }">
			<s:if test="#attr.item.status == 0">
		    	<a href="unsealUser.do?model.id=${item.id}">启用</a> 	    	
			</s:if> 
			<s:else>
				<a href="remove.do?model.id=${item.id}">禁用 |</a>
			</s:else>
			</c:if>
			 <a href="${ctx}/indexImage/edit.do?userId=${item.id}">轮播图上传 |</a>
			 <a href="${ctx}/indexImage/index.do?userId=${item.id}">查看轮播图</a>
		 </ec:column>
	   </ec:row>
      </ec:table>
    </div>
  </div>
</body>
</html>