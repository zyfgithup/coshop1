<%@page import="com.sun.org.apache.xalan.internal.xsltc.compiler.sym"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.systop.common.modules.security.user.model.User,com.systop.amol.user.AmolUserConstants,com.systop.common.modules.security.user.UserUtil"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>销售汇总【分销商】</title>
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

function view(productId){
	var distributorId = document.getElementById("agentId").value;
	window.location.href="${ctx }/salesSummary/view.do?productId=" + productId + "&distributorId="+distributorId;
}

function jxs(){
	var objectJxs = document.getElementById("jxs");
	<% 
		User user = UserUtil.getPrincipal(request); 
		if(user.getSuperior() != null && user.getType().equals(AmolUserConstants.AGENT)){
	%>
			objectJxs.style.display = "none";
			var distributorObject = document.getElementById("agentId");
			distributorObject.value = <%= user.getId() %>;
	<%}%>
}
</script>
</head>
<body onload="jxs();">
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">销售汇总【分销商】</div>
	<div style="float: right;">
	</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form action="${ctx}/salesSummary/indexDistributor.do" method="post">
          	    <s:hidden name="prosortId"/>
          	    <span id="jxs">
          	    	<s:textfield name="distributorName" maxlength="18" id="agent" style="width:150px" readOnly="true"/>								    	
			    	<input type="button" id="btn1" class="button" value="选择分销商" style="width:70px;" onclick="showAgent()"/>
			    	<s:hidden name="distributorId" id="agentId"/>
			    </span>
          	          商品编码:<s:textfield name="productCode" size="10" id="productCode"/>
		                     商品名称:<s:textfield name="productName" size="10" id="name"/>
		       	开始日期:<input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
		       	结束日期:<input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;  	                      
		        <input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button"/>
           </form>
         </td>
       </tr>
     </table>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="process" sortRowsCallback="process" 
		action="${ctx}/salesSummary/indexDistributor.do" 
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
		height="360px"	
		minHeight="360"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
	<ec:row>
	    <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="80" property="code" title="商品编码" style="text-align:center" ellipsis="true"/>
		<ec:column width="80" property="productName" title="商 品" style="text-align:center" ellipsis="true"/>
		<ec:column width="80" property="stardard" title="规格" style="text-align:center" ellipsis="true"/>
		<!-- ec:column width="70" property="unitName" title="单位" cell="date" style="text-align:center" ellipsis="true"/> -->
		<ec:column width="80" property="outUnitPack" title="出库数量" ellipsis="true"/>
		<ec:column width="80" property="outAmount" title="出库金额" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true"/>
		<ec:column width="80" property="returnUnitPack" title="退货数量" ellipsis="true"/>
		<ec:column width="70" property="returnAmount" title="退货金额" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true"/>
		<ec:column width="80" property="allUnitPack" title="汇总数量" ellipsis="true"/>
		<ec:column width="80" property="w" title="汇总金额" style="text-align:right" format="###,##0.00" cell="number" ellipsis="true">
		    ${item.outAmount - item.returnAmount }
		</ec:column>
		<ec:column width="70" property="_0" title="操作" style="text-align:center">
		   <a href="javascript:view(${item.productId });">详情</a>
		</ec:column>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="4" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px;">金额单位为“元”，合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;">${count }</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${amount }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;">${hanod }</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${rttao }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;">${count - hanod }</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${amount - rttao }" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;">&nbsp;</td>
			</tr>
		</ec:extendrow>
	</s:if>
</ec:table>
</div>
</div>
</body>
</html>