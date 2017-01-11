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
	form1.action="${ctx}/purchase/orderdetail/index.do?model.purchase.sno="+sno.value+"&model.purchase.supplier.name="+sid.value;
	form1.submit();
}

function red(id, sno){
    if(confirm("您确认要冲红该【" + sno + "】订单吗？")){
        	window.location.href="${ctx}/purchase/order/red.do?model.id=" + id;
        }
}

function removeAo(){
    alert("此订单已经生成入库单 ，不能冲红！");
        
}
</script>
</head>
<body>

<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">采购订单管理
	
    </div>
	<s:if test="model.user.beginningInit==1 || model.user.superior.beginningInit==1">
	<div style="float: right;">
	<a href="${ctx}/purchase/order/edit.do"><img src="${ctx}/images/icons/add.gif" style="width: 12px; height: 12px;" />&nbsp;添加订单&nbsp;</a>
	</div>
	</s:if>
</div>  

<div><%@ include file="/common/messages.jsp"%></div>

<div class="x-toolbar">
     <table width="99%">
        <tr>
          <s:form action="index" theme="simple" id="form1">
          <td> 
          <table>
           <tr>
	          <td>订单类型：</td>
	          <td><s:select list='statusMap' name="model.status" cssStyle="width:80px;"  ></s:select></td>
	          <td>供&nbsp;&nbsp;应&nbsp;&nbsp;商：</td>
	          <td><s:textfield name="model.supplier.name" id="sid" cssStyle="width:150px" /></td>
	          <td>订&nbsp;&nbsp;单&nbsp;&nbsp;号：</td>
	          <td colspan="2"><s:textfield name="model.sno" cssStyle="width:150px;" id="sno" /></td>         
           </tr>
           <tr>
	       <td>入库状态：</td>
	       <td><s:select list='orderMap' name="model.isover" cssStyle="width:80px"></s:select></td>           
           <td>开始日期：</td>
           <td><input id="startDate" name="startDate" size="24"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/></td>
           <td>结束日期：</td>
           <td><input id="endDate" name="endDate" size="24" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/></td>
           </tr>
           </table>
           </td>
           <td align="right">
             <table>  
          	  <tr>
		         <td>
		         <s:submit value="查询" cssClass="button"  cssStyle="width:40px;"></s:submit>   
        	     <s:submit value="详单" cssClass="button" onclick="xdcx()" cssStyle="width:40px;"></s:submit>
        	     </td>
         	  </tr>
            </table>
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
		xlsFileName="采购订单.xls" 
		maxRowsExported="10000000" 
		pageSizeList="14,50,100" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="14"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="360px"	
		minHeight="360"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"    
	>
	<ec:row>
	      
		<ec:column width="30" property="_0" title="No." value="${GLOBALROWCOUNT}" style="text-align:center" sortable="false" />
		<ec:column width="100" property="sno" title="单号" style="text-align:center"/>
		<ec:column width="150" property="supplier.name" title="供应商" />
		<ec:column width="75" property="sdate" title="下单日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="70" property="samount" title="应付" style="text-align:right" format="###,##0.00"  cell="number"/>
		<ec:column width="75" property="isover" title="入库情况"  style="text-align:center">
		  <s:if test="#attr.item.isover == 1">
		  	<font color="red">完成</font>
		  </s:if>
		   <s:if test="#attr.item.isover == 0">
		  	<font color="red">未完成</font>
		  </s:if>
		   <s:if test="#attr.item.isover == 3">
		  	<font color="red">部分完成</font>
		  </s:if>
		</ec:column>
		
		<ec:column width="100" property="remark" title="备注" ellipsis="true"/>
		<ec:column width="60" property="status" title="状态"  style="text-align:center">
		  <s:if test="#attr.item.status == 0">正常 </s:if>
		  <s:if test="#attr.item.status == 1"><font color="blue">已冲红</font></s:if>
		  <s:if test="#attr.item.status == 2"><font color="red">冲红单</font></s:if>
		</ec:column>		
		<ec:column width="155" property="_2" title="操作" style="text-align:center"  sortable="false" viewsDenied="xls">
		   <a href="view.do?model.id=${item.id}">查看 </a>|
		   <c:if test="${item.status == 0}">
		   <c:if test="${item.isover ==0}">
			<!--   <a href="#" onclick="removeAo()"><font color="#999999">编辑</font></a>|-->
			<a href="#" onclick="javascript:red(${item.id},'${item.sno}')"><font color="red">冲红</font></a> 
		<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_EMPLOYEE_STOCK"> 
			|<a href="${ctx}/purchase/convertPurchase.do?oid=${item.id}">生成入库单</a>
			</stc:role>
			</c:if>
			<c:if test="${item.isover == 1}">
			<a href="#" onclick="removeAo()"><font color="#999999">冲红</font></a> 
				<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_EMPLOYEE_STOCK"> 
			|<a href="#"><font color="#999999">生成入库单</font></a>
			</stc:role>
			<!--  <a href="edit.do?model.id=${item.id}">编辑 </a>|-->
			</c:if>
			<c:if test="${item.isover == 3}">
			<a href="#" onclick="removeAo()"><font color="#999999">冲红</font></a> 
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_EMPLOYEE_STOCK"> 
			|<a href="${ctx}/purchase/convertPurchase.do?oid=${item.id}">生成入库单</a>
			</stc:role>
		   </c:if>
		   </c:if>
		   
		<c:if test="${item.status > 0}">  
		   <a href="#"><font color="#999999">冲红</font></a> 
		   <stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_EMPLOYEE_STOCK"> 
		   |<a href="#"><font color="#999999">生成入库单</font></a>
		   </stc:role>
		 </c:if>
		</ec:column>
	</ec:row>
	
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="4" align="right" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px;">应付合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right;">
				<fmt:formatNumber value="${payTotal}" pattern="#,##0.00"/> 	</td>
				<td>&nbsp;</td>
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