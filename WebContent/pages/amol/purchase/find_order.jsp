<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>

<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>

<title></title>
<script type="text/javascript">

function closeWindow(id){
if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
window.returnValue = id;
	window.close();
}else{
		 window.parent.close();
		window.parent.opener.returnAction(id);
		
}
}
function view(id){
window.showModalDialog("${ctx}/purchase/order/view.do?isorder=1&model.id="+id,null,"dialogWidth=700px;resizable: Yes;");
}
</script>
</head>
<body>

<s:form action="remove" theme="simple" id="removeForm"></s:form>
<s:form action="unsealEmp" theme="simple" id="unsealForm"></s:form>
<div class="x-panel">
  <div class="x-panel-header">订单选择</div>
    <div class="x-toolbar">
      <table width="99%">
        <tr>
          <td> 
        <s:form action="find" theme="simple">
        	单号：<s:textfield name="model.no" size="10"/>
        	开始日期：<s:textfield name="startDate" size="10" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"/>&nbsp;&nbsp;
        	结束日期：<s:textfield name="endDate" size="10" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"/>&nbsp;&nbsp;
        	
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
		maxRowsExported="10000000" 
		pageSizeList="20,50,100" 
		editable="false" 
		sortable="true"	
		rowsDisplayed="20"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="400px"	
		minHeight="400"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status"   
	>
	<ec:row ondblclick="closeWindow('${item.id}')">
	   
		<ec:column width="40" property="_0" title="序号" value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="130" property="sno" title="单号" style="text-align:center"/>
		<ec:column width="80" property="sdate" title="日期" style="text-align:center"  cell="date"/>
		<ec:column width="150" property="supplier.name" title="供应商" ellipsis="true"/>
		<ec:column width="80" property="samount" title="应付" format="#####0.00" cell="number"/>
		<ec:column width="80" property="isover" title="是否入库" style="text-align:center">
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
		<ec:column width="140" property="remark" title="备注" />
	<ec:column width="80" property="_2" title="操作" style="text-align:center"  sortable="false">
		   <a onclick="view('${item.id}')">查看</a>
		</ec:column>
				
		
	</ec:row>
	</ec:table>
	</div>
	</div>
</div>


<div id="win" class="x-hidden">
    <div class="x-window-header">角色列表</div>
    <div id="role_grid"></div>
</div>
<script type="text/javascript" src="${ctx}/pages/admin/security/user/user.js">
</script>
<div id='load-mask'></div>
</body>
</html>