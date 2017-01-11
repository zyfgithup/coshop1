<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>销售回款管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
/**
 * 冲红
 */
function remove(id){
	if(confirm('您确定要冲红该回款单吗？！！如果冲红该回款单！那么由该回款单给予对应的应收单金额，都要从应收款单中去除！！')){
		window.location.href="${ctx}/receive/redRed.do?model.id=" + id;
    }else{
   	 return false;
    }
}

function removeAo(){
    alert("此回款单已经冲红 ，不能再冲红！");
}

/**
 * 选择未收费的出库单

function selectorder(){
	var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/sales/storehouse/selector.jsp",null,"dialogWidth=55","dialogHeight=300px");
	if(cus!=null){
    	window.location.href="outKToReSiUI.do?id=" + cus.id;
	}
} */

/**
 * 选择客户
 */
 function selectkh(){
    window.location.href="outCardKToReSiUI.do";
 } 
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">销售回款管理【代币卡】</div>
	<s:if test="beginningInit">
	<div style="float: right;">
	<a href="#" onclick="javascript:selectkh();"><img src="${ctx}/images/icons/add.gif" style="width: 12px; height: 12px;" />&nbsp;添加回款单&nbsp;</a>
	</div>
	</s:if>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form action="${ctx}/receive/cardIndex.do" method="post">
          		状态：<s:select list='statusMap' name="model.redRed"></s:select>          
		        <s:select list='receiveSalesMap' name="receiveSales"></s:select>          
		        <s:textfield name="model.receiveNumber" size="15" id="salesNo"/> 
		                     客户：<s:textfield name="model.customer.name" size="15" id="name"/>
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
		action="cardIndex.do"
		useAjax="true" doPreload="false"
		xlsFileName="销售回款单（代币卡）.xls"
		maxRowsExported="10000000" 
		pageSizeList="20,50,100" 
		editable="false" 
		sortable="true"	
		rowsDisplayed="20"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="350px"	
		minHeight="350"
	toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  
		>
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="150" property="receiveNumber" title="编号"/>
		<ec:column width="100" property="customer.name" title="客户名称" ellipsis="true"/>
		<ec:column width="100" property="createTime" title="日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="80" property="spayamount" title="收款金额" style="text-align:right" format="###,##0.00" cell="number"/>
		<ec:column width="80" property="redRed" title="状态" style="text-align:center">
		         <s:if test="#attr.item.redRed == 1">正常 </s:if>
		         <s:if test="#attr.item.redRed == 2">已冲红 </s:if>
		         <s:if test="#attr.item.redRed == 3">冲红单 </s:if>
		</ec:column>
		<ec:column width="160" property="remark" title="摘要"/>
		<ec:column width="100" property="_0" title="操作" style="text-align:center" sortable="false" viewsDenied="xls">
			<a href="view.do?singleId=${item.id}" >详情</a> | 
			<c:if test="${item.redRed == 1 }">
			    <a href="#" onclick="remove(${item.id})" title="冲红">冲红</a>  	
			</c:if>
		    <c:if test="${item.redRed > 1}">  
		       <a href="#" onclick="removeAo()" title="冲红"><font color="#999999">冲红</font></a>
		    </c:if> 
		</ec:column>
			
	</ec:row>
	
	
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="4" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">金额单位为“元”，合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right"><fmt:formatNumber value="${amountTotal }" pattern="#,##0.00"/></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</ec:extendrow>
	</s:if>
</ec:table>
</div>
</div>
</body>
</html>