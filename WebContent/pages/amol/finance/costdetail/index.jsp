<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>
<title></title>
</head>
<body>

<s:form action="remove" theme="simple" id="removeForm"></s:form>
<s:form action="unsealEmp" theme="simple" id="unsealForm"></s:form>
<div class="x-panel">
  <div class="x-panel-header">费用汇总管理</div>
    <div class="x-toolbar">
      <table width="99%">
        <tr>
          <td> 
        	<s:form action="index.do" theme="simple">
            	<s:hidden name="model.costSort.id" cssStyle="width:30px;"/>
        			单号：<s:textfield name="model.cost.receipts" cssStyle="width:100px;"/>&nbsp;&nbsp;
        			开始日期：<input id="startDate" name="startDate" size="12"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp;
        			结束日期：<input id="endDate" name="endDate" size="12" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>&nbsp;&nbsp; 	
        			<s:submit value="查询" cssClass="button"></s:submit>        	
         	</s:form>
          </td>
        </tr>
      </table>
    </div>   
    <div class="x-panel-body">
    <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do"
		useAjax="true" doPreload="false"
	    xlsFileName="收支费用汇总表.xls" 
		maxRowsExported="10000000" 
		pageSizeList="15,50,100" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="383px"	
		minHeight="383"
		toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"   
	>
	<ec:row>  
		<ec:column width="40" property="_0" title="No." value="${GLOBALROWCOUNT}" style="text-align: center" sortable="false" />
		<ec:column width="80" property="cost.receipts" title="单号" style="text-align: center"/>
		<ec:column width="60" property="cost.status" title="收支类别" style="text-align: center" mappingItem="costSortMap"/>
		<ec:column width="100" property="costSort.name" title="费用类型" ellipsis="true"/>	
		<ec:column width="80" property="cost.fundsSort.name" title="资金类型" style="text-align: center" />
		<ec:column width="80" property="_money" title="金额(元)" style="text-align: right">
		 <c:choose>
		    <c:when test="${item.cost.status eq '1'}">
               <fmt:formatNumber value="${item.money}" pattern="#,##0.00"/>
		    </c:when>		    
		    <c:otherwise>
               <font color="red">
               	<fmt:formatNumber value="${item.money}" pattern="#,##0.00"/>
               </font>
		    </c:otherwise>
	     </c:choose>	
		</ec:column>
		<ec:column width="80" property="cost.createDate" title="创建日期" cell="date" style="text-align: center"/>
	</ec:row>
	
	<s:if test="#attr.items.size()!=0">
		<ec:extendrow>
			<tr style="background-color: #ffe3ee">
				<td colspan="5" align="center" id="No1"
					style="font-weight: bold; border-top: 0px; border-bottom: 0px; border-left: 0px">收支合计(全部)</td>
				<td style="font-weight: bold; word-wrap: break-word; word-break: break-all; text-align: right">
					<fmt:formatNumber value="${costTotal}" pattern="#,##0.00"/>
				</td>
				<td style="border: 0px"></td>
			</tr>
		</ec:extendrow>
	</s:if>
	</ec:table>
	</div>
	</div>
</div>
</body>
</html>