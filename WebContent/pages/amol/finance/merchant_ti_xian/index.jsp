<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>

<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>
<script type="text/javascript">
	function remove(id){
		if(confirm("确定要删除该记录吗？")){
			window.location.href="remove.do?model.id=" + id;
		}
	}
</script>
<script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>

<title>提现记录</title>
</head>
<body>

<div class="x-panel">

<div class="x-panel-header" >
	<div style="float: left;">提现记录</div>
	<div style="float: right;">
		<a href="${ctx}/merchantTiXian/edit.do"><img src="${ctx}/images/icons/add.gif" style="width: 12px; height: 12px;" />&nbsp;申请提现&nbsp;</a>
	</div>
</div>
<div>
</div>

    <div class="x-toolbar">
     <s:form action="index" theme="simple">
      <table width="99%">
        <tr>
          <td>分销商：<s:textfield name="name" size="20"/>&nbsp;&nbsp;手机号：<s:textfield name="mobile" size="30"/>&nbsp;&nbsp;<s:submit value="查询" cssClass="button"></s:submit></td>
          <!-- td>时间：<input type="text" id="startDate" name="startTime" value='${startTime }' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate"  readonly="readonly"/>      
            &nbsp;至&nbsp;<input type="text" id="endDate" name="endTime" value='${endTime }' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate"  readonly="readonly"/>
        	&nbsp;&nbsp;<s:submit value="查询" cssClass="button"></s:submit>
          </td> -->
        </tr>
      </table>
     </s:form>
    </div>   
    <div class="x-panel-body">
    <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="index.do"
		useAjax="true" doPreload="false"
		maxRowsExported="10000000" 
		pageSizeList="15,20,50,100" 
		editable="false" 
		sortable="true"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="380px"	
		minHeight="380"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status"   
	>
	<ec:row>
	   <ec:column width="35" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="100" property="merchant.name" title="提现用户" ellipsis="true"/>
		<ec:column width="100" property="merchant.mobile" title="手机号" ellipsis="true"/>
		<ec:column width="100" property="merchant.phone" title="电话" ellipsis="true"/>
		<ec:column width="210" property="merchant.bankSort.name" title="开户行" ellipsis="true"/>
		<ec:column width="210" property="merchant.yhkh" title="银行账号" ellipsis="true"/>
		<ec:column width="160" property="_createTime" title="提现时间">
		
			<fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
		
		</ec:column>
		<ec:column width="100" property="tiXianMoney" title="提现金额" style="text-align:right;color:red" cell="number" format="0.00" />
		<ec:column width="100" property="balance" title="余额" style="text-align:right;color:red" cell="number" format="0.00" />
		<ec:column width="100" property="incomeAll" title="收入" style="text-align:right;color:red" cell="number" format="0.00" />
		
		<ec:column width="100" property="isSuccess_" title="提现是否成功">
		
			<c:if test="${item.isSuccess }">
				汇款成功
			</c:if>
			<c:if test="${!item.isSuccess }">
				未汇款
			</c:if>
		
		</ec:column>

		<ec:column width="160" property="_createTime" title="汇款时间">
		
			<fmt:formatDate value="${item.hkTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
		
		</ec:column>
		
		<ec:column width="100" property="employee.name" title="汇款人" ellipsis="true"/>
		
		<ec:column width="100" property="_2" title="操作" style="text-align:center"  sortable="false">
		   	<a href="view.do?model.id=${item.id}">查看</a>
		</ec:column>
	</ec:row>
	</ec:table>
	</div>
	</div>
</div>
</body>
</html>