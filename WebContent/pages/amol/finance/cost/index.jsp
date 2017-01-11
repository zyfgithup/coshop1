<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.systop.common.modules.security.user.model.User"%>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>
<title></title>
</head>
<body>
<%User user = (User)request.getAttribute("user");%>
<div class="x-panel">
  <c:choose>
	<c:when test="${status == 1 }">
		<div class="x-panel-header">资金收入管理</div>           
	</c:when>		    
	<c:otherwise>
		<div class="x-panel-header">资金支出管理</div>             
	</c:otherwise>
  </c:choose>	
 
<div><%@ include file="/common/messages.jsp"%></div>
    <div class="x-toolbar">
      <table width="99%">
        <tr>
          <td> 
	        <s:form action="index" theme="simple">
	            <s:hidden name="status"/>
	        	单号：<s:textfield name="model.receipts" cssStyle="width:90px;"/>
	        	<c:if test="${user.type=='agent'}">
	        	职员编号：<s:textfield name="code" cssStyle="width:60px;"/>
	        	职员：<s:textfield name="employeeName" cssStyle="width:70px;"/>
	        	</c:if>
	        	状态:<s:select id="stateMap" list="stateMap" cssStyle="width:60px;" name="model.red" />
	        	开始日期：<input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
	        	结束日期：<input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>	
	        	<s:submit value="查询" cssClass="button"></s:submit>	        	
	         </s:form>
         </td>
         <td align="right">
           <table>  
          	  <tr>
		         <td>
		         	<s:if test="model.user.beginningInit==1">
						<c:choose>
						    <c:when test="${status == 1 }">
			                 <a href="${ctx}/finance/cost/edit.do?status=${status}">
			                    <img src="${ctx}/images/icons/add.gif"/>收入</a>
						    </c:when>		    
						    <c:otherwise>
				             <a href="${ctx}/finance/cost/edit.do?status=${status}">
			                    <img src="${ctx}/images/icons/add.gif"/>支出</a>
						    </c:otherwise>
					    </c:choose>	
					</s:if>		    	         
		         </td>
         	  </tr>
            </table>
          </td>
        </tr>
      </table>
    </div>   
    <div class="x-panel-body">
    <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do"
		useAjax="true" doPreload="false"
		xlsFileName="收支费用表.xls" 
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="375px"	
		minHeight="375"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status">
	<ec:row>  
		<ec:column width="40" property="_0" title="序号" value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="80" property="receipts" title="单号" />		
		<ec:column width="70" property="createDate" title="日期" style="text-align:center" format="yyyy-MM-dd" cell="date"/>
		<ec:column width="70" property="fundsSort.name" title="资金类型" ellipsis="true"/>
		<ec:column width="75" property="source" title="对方单位" ellipsis="true"/>
		<ec:column width="75" property="checkNo" title="支票号" ellipsis="true"/>
	    	<c:choose>
			    <c:when test="${status == 1}">
			       <ec:column width="80" property="totalMoney" style="text-align:right" format="#,##0.00" cell="number" title="总收入(元)" />
			    </c:when>		    
			    <c:otherwise>
			       <ec:column width="80" property="totalMoney" style="text-align:right" format="#,##0.00" cell="number" title="总支出(元)" />
			    </c:otherwise>
		    </c:choose>
		<ec:column width="70" property="user.name" title="操作人" />
		<ec:column width="140" property="remark" title="备注" ellipsis="true"/>
				
		<ec:column width="120" property="_2" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
		   <c:choose>
				<c:when test="${item.red == 0 }">
					<a href="view.do?model.id=${item.id}">查看 </a>|
				    <a href="edit.do?model.id=${item.id}&status=${status}">编辑</a>|
		   			<a href="#" onclick="red(${item.id},${status},${item.receipts})" title="冲红">
		   				<font color="red" >冲红</font>
		   			</a>	 				    	
				</c:when>
				<c:when test="${item.red == 1 }">
					<a href="view.do?model.id=${item.id}">查看 </a>|
				   <font color="silver" >编辑</font> |
			        <font color="silver" >冲红</font> 	 				    	
				</c:when>		    
				<c:otherwise>
				<a href="view.do?model.id=${item.id}">查看 </a>|
				    <font color="silver" >编辑</font> |
			        <font color="silver" >冲红</font>               
				</c:otherwise>
			</c:choose>  	
		</ec:column>
	</ec:row>
	
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="6" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">
					<c:choose>
					    <c:when test="${status == 1 }">
		                 	收入合计(全部)
					    </c:when>		    
					    <c:otherwise>
					    	支出合计(全部)
					    </c:otherwise>
				    </c:choose>	
				</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
					<fmt:formatNumber value="${costTotal}" pattern="#,##0.00"/>
				</td>
				<td style="border: 0px"></td>
				<td style="border: 0px"></td>
				<td style="border: 0px"></td>
				<td style="border: 0px"></td>
			</tr>
		</ec:extendrow>
	</s:if>
	</ec:table>
	</div>
	</div> 
</div>
<script type="text/javascript">

function red(id,status,receipts) {
     if (confirm("确认要冲红单号为【" + receipts + "】的单据吗？冲红后不能恢复！")) {
        window.location.href = "${ctx}/finance/cost/red.do?model.id=" + id + "&status=" + status;
     }
}

function refresh() {
  ECSideUtil.reload('ec');
}
</script>
</body>
</html>