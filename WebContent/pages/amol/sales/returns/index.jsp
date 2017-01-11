<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>销售退货管理【现金】</title>
<script type="text/javascript">
function red(id){
	if(confirm("确认要冲红吗？")){
		window.location.href="${ctx}/salesReturns/redRed.do?model.id=" + id;
	}else{
	}
}

function removeAo(){
    alert("此退货单已冲红或该单是冲红单 ，所以不能再冲红！");
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">销售退货管理【现金】</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form action="${ctx}/salesReturns/index.do" method="post">
	            &nbsp;&nbsp;用户：<s:textfield name="name" size="10" id="name"/>
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
		useAjax="false"
		doPreload="false" 
		xlsFileName="退货单.xls" 
		pageSizeList="10,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="10"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="320px"
		minHeight="320"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="100" property="user.loginId" title="用户名" ellipsis="true"></ec:column>
		<ec:column width="100" property="user.name" title="姓名" ellipsis="true"/>
		<ec:column width="200" property="_regionName" title="所属地区"  ellipsis="true">
		 	${item.user.region.parent.parent.name }&nbsp;&nbsp;${item.user.region.parent.name }&nbsp;&nbsp;${item.user.region.name }
		 </ec:column>
		<ec:column width="200" property="salesNo" title="退货单号" style="text-align:center"/>
		<ec:column width="140" property="createTime" title="日期" style="text-align:center" cell="date"/>
		<ec:column width="100" property="rttao" title="退款金额" style="text-align:right" format="###,##0.00" cell="number"></ec:column>
		<ec:column width="170" property="remark" title="备注" style="text-align:center" ellipsis="true"/>
		<ec:column width="180" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
			 <a href="view.do?model.id=${item.id}" title="查看销售退货信息">详情</a>
			 <s:if test="#attr.item.ckzt == 0">
			 |
			 <a href="sure.do?flag=cash&model.id=${item.id}" title="确定退货操作">确定退货</a>|
			 <a href="cancel.do?flag=cash&model.id=${item.id}" title="驳回退货请求">驳回请求</a>
			 </s:if>
			 <c:if test="${attention=='notenough'}">
			 <font color="red">商户余额不足！</font>
			 </c:if>
	   </ec:column>
	</ec:row>
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="6" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px;">金额单位为“元”，退款合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;"><fmt:formatNumber value="${rttao }" pattern="#,##0.00"/></td>
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