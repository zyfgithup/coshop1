<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>

<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/meta.jsp" %>

<title></title>
<script type="text/javascript">
function xdcx(){
	var form1=document.getElementById("form1");
	var sno=document.getElementById("sno");
	var sid=document.getElementById("sid");
	form1.action="${ctx}/purchase/returnsdetail/index.do?model.purchase.sno="+sno.value+"&model.purchase.supplier.name="+sid.value;
	form1.submit();
}
function red(id, sno){
    if(window.confirm("您确认要冲红该【" + sno + "】退货单吗？")){
        
        	window.location.href="${ctx}/purchase/returns/red.do?model.id=" + id;
        }
    
}
function removeAo(){
    alert("此退货单已经生成应付款单 ，不能冲红！");
}
</script>
</head>
<body>

<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">采购退货管理

	</div>
	<s:if test="model.user.beginningInit==1 || model.user.superior.beginningInit==1">
	<div style="float: right;">
	<a href="${ctx}/purchase/returns/edit.do"><img src="${ctx}/images/icons/add.gif" style="width: 12px; height: 12px;" />&nbsp;添加退货单&nbsp;</a>
	</div>
	</s:if>
</div>   
	<div><%@ include file="/common/messages.jsp"%></div>
 <div class="x-toolbar">
      <table width="99%">
        <tr>
          <s:form action="index" theme="simple" id="form1">
          <td> 
            <td>状态</td>
            <td><s:select list='statusMap' name="model.status" cssStyle="width:60px"></s:select></td>
            <td>供应商</td>
            <td><s:textfield name="model.supplier.name" id="sid" cssStyle="width:140px" /></td>
        	<td>单号 <s:textfield name="model.sno" size="16" id="sno"/></td>
            <td>开始日期 <input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
        	  结束日期 <input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>    	
        	<s:submit value="查询" cssClass="button"></s:submit>
        	<s:submit value="详单" cssClass="button" onclick="xdcx()" cssStyle="width:40px;"></s:submit>
            </td>
          </s:form>
        </tr>
      </table>
    </div>   
    <div class="x-panel-body">
    <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do"
		useAjax="true" doPreload="false"
		xlsFileName="采购退货单.xls"
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="380px"	
		minHeight="380"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  
	>
	<ec:row>
	   
		<ec:column width="30" property="_0" title="No." style="text-align:center" value="${GLOBALROWCOUNT}" sortable="false" />
		<ec:column width="100" property="sno" title="单号" style="text-align:center" />
		<ec:column width="75" property="sdate" title="下单日期" style="text-align:center"  cell="date"/>
		<ec:column width="150" property="supplier.name" title="供应商" />
		<ec:column width="70" property="samount" title="应退" style="text-align:right" format="###,##0.00"  cell="number"/>
		<ec:column width="75" property="spayamount" title="本次退款" style="text-align:right" format="###,##0.00"  cell="number"/>
		<ec:column width="70" property="spayTotal" title="实退" style="text-align:right" format="###,##0.00"  cell="number"/>
		<ec:column width="120" property="remark" title="备注" ellipsis="true"/>
	     <ec:column width="60" property="status" title="状态"  style="text-align:center">
		   <s:if test="#attr.item.status == 0">正常 </s:if>
		  <s:if test="#attr.item.status == 1"><font color="blue">已冲红</font></s:if>
		  <s:if test="#attr.item.status == 2"><font color="red">冲红单</font></s:if>
		</ec:column>
		<ec:column width="80" property="_2" title="操作" style="text-align:center"  sortable="false" viewsDenied="xls">
		   <a href="view.do?model.id=${item.id}">查看</a> | 
		   <c:if test="${item.status == 0 }">
			 <c:if test="${item.isover >0}">
			<!--    <a href="#" onclick="removeAo()"><font color="#999999">编辑</font></a>|-->
			  <a href="#" onclick="removeAo()"><font color="#999999">冲红</font></a>
			</c:if>
			<c:if test="${item.isover == 0}">
			<!--   <a href="edit.do?model.id=${item.id}">编辑 </a>|-->
		   <a href="#" onclick="red(${item.id},'${item.sno}')"><font color="red">冲红</font></a>
			</c:if>
			</c:if>
		   <c:if test="${item.status >0 }">
	        <a href="#"><font color="#999999">冲红</font></a>
		   </c:if>
		</ec:column>
	</ec:row>
	
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="4" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
								<fmt:formatNumber value="${sfTotal}" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
								<fmt:formatNumber value="${bcpayTotal}" pattern="#,##0.00"/></td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
								<fmt:formatNumber value="${payTotal}" pattern="#,##0.00"/></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</ec:extendrow>
	</s:if>
	
	</ec:table>
	</div>
	</div>
</div>
</body>
</html>