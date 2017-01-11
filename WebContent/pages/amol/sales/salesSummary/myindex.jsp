<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>销售汇总</title>

<script type="text/javascript">

//提取经销商
function showAgent() {
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog("${ctx }/pages/amol/user/agent/selectorAgent.jsp",null,"dialogWidth=800px;resizable: Yes;");
	    if(cus!=null){
	    	var tab = document.getElementById("agent") ;
			tab.value=cus.name;
			var tab1 = document.getElementById("agentId") ;
			tab1.value=cus.id;
		}
 	}else{
		window.open("${ctx }/pages/amol/user/agent/selectorAgent.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	    this.returnAction=function(cus){
	    	if(cus!=null){
	    		var tab = document.getElementById("agent") ;
	    		tab.value=cus.name;
	    		var tab1 = document.getElementById("agentId") ;
	    		tab1.value=cus.id;
	    	}
		};
 	}
}

</script>
<!-- 地区 -->
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<script type="text/javascript">
Ext.onReady(function() {
	var pstree = new RegionTree({
		el : 'comboxWithTree',
		target : 'regionId',
		//emptyText : '选择地区',
		comboxWidth : 200,
    	treeWidth : 195,
		url : '${ctx}/admin/region/regionTree.do?regionId=<%=RegionConstants.HBS_ID %>',
		defValue : {id:'${regionId}',text:'${regionName}'}
	});
	pstree.init();	
	
});
function showPic(url,event){
	$("#layer").html("<img src='"+url+"' with='300' height='300'>");
	$("#layer").show();
}
function hiddenPic(){
	$("#layer").html("");
	$("#layer").hide();
}
</script>

</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">销售汇总</div>
	<div style="float: right;">
	</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
	<form action="${ctx}/salesSummary/myindex.do" method="post">
      <table>
        <tr>
          <td>
          	    <s:hidden name="prosortId"/>
          	  <%--   <span id="jxs">
          	    	<s:textfield name="distributorName" maxlength="18" id="agent" style="width:150px" readOnly="true"/>								    	
			    	<input type="button" id="btn1" class="button" value="选择分销商" style="width:70px;" onclick="showAgent()"/>
			    	<s:hidden name="distributorId" id="agentId"/>
			    </span> --%>
		  </td>
		  <td>
		  		地区：
		  </td>
		  <td align="left">
			    <span id='comboxWithTree'></span>
			    <s:hidden id="regionId" name="regionId"/>
		  <td>
		  <td>         
          	   <%--  商品编码：<s:textfield name="productCode" size="7" id="productCode"/> --%>
		        商品名称：<s:textfield name="productName" size="10" id="name"/>
         </td>
         <td>
         		支付方式：<s:select list='paymentMap' name="payment"></s:select>
         </td>
       </tr>
       <tr>
       	<td colspan="5">
       		开始日期：<input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
		       	结束日期：<input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;  	                      
		       	<select name="type"><option value="">全部</option><option value="1">平台</option><option value="2">自营</option></select>
		        <input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button"/>
       	
       	</td>
       </tr>
     </table>
    </form>
</div>   
<div class="x-panel-body">
<div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="myindex.do" 
		useAjax="false"
		doPreload="false" 
		xlsFileName="销售汇总.xls" 
		pageSizeList="15,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="false"	
		width="100%" 	
		height="450px"	
		minHeight="450"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
	<ec:row>
	    <ec:column width="50" property="_s" title="No." value="${GLOBALROWCOUNT}"  sortable="false" viewsAllowed="html" style="text-align:center"/>
		<ec:column width="100" property="_imageUrl" title="图片" style="text-align:center">
			 <a href="#"><img alt="${item.productName }" src="${ctx }/${item.imageUrl }" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" width="26" height="26"/></a>
		</ec:column>
		<%-- <ec:column width="150" property="code" title="编 号" style="text-align:center" ellipsis="true"/> --%>
		<ec:column width="150" property="productName" title="商 品" style="text-align:center" ellipsis="true"/>
		<ec:column width="150" property="_productName" title="商 品类别" style="text-align:center" ellipsis="true">
			<c:if test="${item.belong==0&&item.type==true }">
			
				平台团购
			
			</c:if>
			
			<c:if test="${item.belong==0&&item.type==false }">
			
				平台普通
			
			</c:if>
			
			<c:if test="${item.belong==1 }">
			
				商家自营
			
			</c:if>
		
		</ec:column>
		<ec:column width="150" property="stardard" title="规格" style="text-align:center" ellipsis="true"/>
		<!-- ec:column width="70" property="unitName" title="单位" cell="date" style="text-align:center" ellipsis="true"/> -->
		<ec:column width="100" property="ktNum" title="销售数量" style="text-align:right" cell="number" ellipsis="true"/>
		<ec:column width="165" property="outAmount" title="销售额" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true"/>
	</ec:row>
</ec:table>
</div>
</div>

</body>
</html>