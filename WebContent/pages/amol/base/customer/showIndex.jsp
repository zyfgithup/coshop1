<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>客户管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
//商品对象
function customers(){
	this.id;
	this.name;
	this.cardGrants;
	this.type;
	this.idCard;
	this.mobile;
	this.area;
}
function closeWindow(id,name,cardGrants,type,idCard,mobile,area){
	var customer=new customers();
	customer.id=id;
	customer.name=name;
	customer.cardGrants=cardGrants;
	customer.type=type;
	customer.idCard=idCard;
	customer.mobile=mobile;
	customer.area=area;
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		window.returnValue = customer;
		window.close();
	 }else{
		window.parent.close();
		window.parent.opener.returnAction(customer);
	}
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">客户管理</div>
<div class="x-toolbar">
      <table width="100%">
        <tr>
          <td> 
          <s:form action="showIndex" theme="simple">
            <s:hidden name="model.region.id"/>
            
	                     类别：<s:select name="model.type"
				list='#{"1":"会员客户","0":"普通客户"}' headerKey="" headerValue="全部" />
				              
	                       名称：<s:textfield id="name" name="model.name" size="6"/>
	        
	                       身份证号：<s:textfield id="idCard" name="model.idCard" size="21" maxlength="18"/>
	        <s:submit value="查询" cssClass="button" cssStyle="width:40px;"></s:submit>
           </s:form>
         </td>
       </tr>
     </table>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="showIndex.do" 
		useAjax="false"
		doPreload="false" 
		pageSizeList="10,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="10"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="377px"
		minHeight="377"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row ondblclick="closeWindow('${item.id}','${item.name}','${item.cardGrants}','${item.type}','${item.idCard}','${item.mobile }','${item.region.name }')">
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="100" property="region.name" title="地区"/>
		<ec:column width="80" property="name" title="客户名称"/>
		<ec:column width="120" property="idCard" title="身份证号"/>
		<ec:column width="100" property="phone" title="电话" ellipsis="true"/>
		<ec:column width="80" property="address" title="地址" ellipsis="true"/>
		<ec:column width="60" property="status" title="类别"
			style="text-align:center">
			<s:if test="#attr.item.type == 0">
				<font color="green">普通客户</font>
			</s:if>
			<s:else>
				<font color="red">会员客户</font>
			</s:else>
		</ec:column>
	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>