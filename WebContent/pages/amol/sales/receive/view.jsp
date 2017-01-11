<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.ReceiveDetail"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>


<style type="text/css">

<!--

.aa {
	margin: 5px;
	padding: 5px;
	border: thin solid #00FFFF;
}
.Message {
	color: #FF0000;
	font-style: italic;
}
#mytable {
	border: 1px solid #A6C9E2;
	border-collapse: collapse;
}

#mytable td {
	border: 1px solid #A6C9E2;
	height: 26px;
}
-->
</style>
<title>回款单【代币卡】</title>
<script type="text/javascript">
function remove(id){
	if(confirm('确认要冲红该记录吗？')){
		window.location.href="${ctx}/receiveDetail/redRed.do?model.id=" + id;
    }else{
   	 return false;
    }
}

function removeAo(){
    alert("此记录已经被冲红 ，不能再冲红！");
}
</script>
</head>
<body>


<div class="x-panel" style="width: 100%">
<div class="x-panel-header">销售回款单【代币卡】</div>
<div><%@ include file="/common/messages.jsp"%></div>
<s:form action="exportExcel.do" id="ReceiveView" validate="true" method="POST">
<div align="center" style="width: 95%">
	<s:hidden name="model.id"  /> 
	<fieldset style="width: 94%; padding:10px 10px 10px 10px;">
	<legend>回款单【代币卡】</legend>
	<table width="100%" align="left" >
	    <tr>
			<td  align="right" >
				单&nbsp;&nbsp;&nbsp;&nbsp;号：			</td>
			<td  class="simple" align="left">
			${ model.receiveNumber }
		  </td>
			<td  align="right" >
				日&nbsp;&nbsp;&nbsp;&nbsp;期：
			</td>
			<td  class="simple" align="left">
			<fmt:formatDate value="${ model.createTime}" pattern="yyyy-MM-dd"/>
		  </td>
		</tr>
		
		<tr>
			<td align="right" >
				客&nbsp;&nbsp;&nbsp;&nbsp;户：
			</td>
			<td class="simple" align="left">
			${ model.customer.name }
			</td>
			<td align="right" >
				业&nbsp;务&nbsp;员：
			</td>
			<td class="simple" align="left">
			${ model.employee.name }
			</td>
		</tr> 
		<tr>
			<td align="right" >
				收款金额：
			</td> 
			<td class="simple" align="left">
				<fmt:formatNumber value="${model.spayamount }" pattern="#,##0.00"/>
			</td>
			<td align="right" >
				操&nbsp;作&nbsp;员：
			</td>
			<td class="simple" align="left">
				${model.user.name}
			</td> 
		</tr>
		<tr>
			<td align="right">
				备&nbsp;&nbsp;&nbsp;&nbsp;注：
			</td>
			<td class="simple" align="left" colspan="3">&nbsp;
				${ model.remark }
           		
			</td> 
		</tr>
		</table>
	</fieldset>

	<center>	

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9">
	
   <tbody id="tbody">
  <tr bgcolor="#F3F4F3"  align="center" height="23">
    <td width="120px">应收单号</td>
    <td width="120px">代币卡号</td>
    <td width="120px">日期</td>
    <td width="65px">应收金额</td>
    <td width="70px">已收金额</td>
    <td width="65px">退款金额</td>
    <td width="65px">未收金额</td>
    <td width="50px">操作</td>
  </tr>
   <% 
     List<ReceiveDetail> sd = (List<ReceiveDetail>)request.getAttribute("sd");
     if(sd!=null && sd.size()>0){
    	 
     for(ReceiveDetail s : sd){
     %>
     
      <tr height="23" bgcolor="#FFFFFF">
      	  <td>
      	  	 <%=s.getSales().getSalesNo()%>
             <input type="hidden" name="ckdId" value="<%=s.getId()%>"/>
          </td>
          <td style="text-align: center;">
             <%= s.getSales().getCardGrant().getCard().getCardNo() %>
          </td>
          <td style="text-align: center;">
             <%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(s.getCreateTime()) %>
          </td>
	      <td style="text-align: right">
	         <fmt:formatNumber value="<%=s.getSamount() %>" pattern="#,##0.00"/>
	      </td>
	      <td style="text-align: right">
	      	<fmt:formatNumber value="<%=s.getSpayamount() %>" pattern="#,##0.00"/>
	      </td>
	      <td style="text-align: right">
	      	 <fmt:formatNumber value="<%=s.getRttao() %>" pattern="#,##0.00"/>
	      </td>
	      <td style="text-align: right">
	      	<fmt:formatNumber value="<%=s.getSamount() - s.getRttao() - s.getSpayamount() %>" pattern="#,##0.00"/>
	      </td>
	      <td>
	      
	      	<c:if test="<%=s.getRedRed() == 1 %>">
				<a href="#" onclick="remove(<%=s.getId() %>)" title="冲红">冲红</a>
			</c:if>

			<c:if test="<%=s.getRedRed() > 1%>">  
		       <a href="#" onclick="removeAo()" title="冲红"><font color="#999999">冲红</font></a>
		    </c:if> 
	      </td>
      </tr>
     <%}}%>
  
    </tbody>
</table>
		
		</center>
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
				<input type="submit" class="button" value="导出">
				<s:reset value="返回" cssClass="button" onclick="javascript:history.back();"/>
			</td>
		</tr>
	</table>
</div></s:form></div>
</body>
</html>