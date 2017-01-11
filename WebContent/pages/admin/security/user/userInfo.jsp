<%@page import="com.systop.common.modules.security.user.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
<title>个人基本信息</title>
<%@include file="/common/meta.jsp"%>
</head>
<style type="text/css">
 table{
   margin: 5px;
   background-color: #ECF3F9;
   border-color: #FFFFFF;
 }
 td{
   height: 30px;
 }
</style>
<body>
<div class="x-panel">
  <div class="x-panel-header">个人信息</div>
  <div class="x-toolbar"></div>
  <div class="x-panel-body" >
    <br>
    <br>
	<table border="1" cellpadding="0" cellspacing="0" align="center">
	  <%User user = (User)request.getAttribute("user");%>
	  <tr>
	  	<td align="right" width="70">用&nbsp;&nbsp;户&nbsp;&nbsp;名：</td>
	  	<td width="150" colspan="3">&nbsp;<c:out value="<%=user.getLoginId()%>" default=""/></td>
	  </tr>
	  <tr>
	    <td align="right">用户类别：</td>
	  	<td colspan="3">
            &nbsp;<c:out value="${userType}" default=""/>
	    </td>
	  </tr>
	  <tr>
	  	<td align="right">名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：</td>
	  	<td colspan="3">&nbsp;<c:out value="<%=user.getName()%>" default=""/></td>
	  </tr> 
	  <tr>
	  	<td align="right">性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
	  	<td colspan="3">&nbsp;
	  	  <c:set var="sex" value="<%=user.getSex()%>"/>
		  <c:if test="${sex eq 'M'}">
			 男
		  </c:if>
		  <c:if test="${sex eq 'F'}">
			 女
		  </c:if>
	  	</td>
	  </tr>	 
	  <tr>
	   	<td align="right" width="70">身&nbsp;&nbsp;份&nbsp;&nbsp;证：</td>
	   	<td colspan="3">&nbsp;<c:out value="<%=user.getIdCard()%>" default=""/></td>
	  </tr>			
	  <tr>		
	   	<td align="right" width="70">所属地区：</td>
	   	<c:choose>
	   		<c:when test="${not empty user.region}">
	   			<td colspan="3">&nbsp;<c:out value="<%=user.getRegion().getName()%>" default=""/></td>
	  		</c:when>
	  		<c:otherwise>
	   			<td colspan="3">&nbsp;<c:out value="<%=user.getRegion()%>" default=""/></td>       
			</c:otherwise>
	  	</c:choose>
	  </tr> 
	  <tr>
	   	<td align="right" width="70">手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机：</td>
	   	<td width="150">&nbsp;<c:out value="<%=user.getMobile()%>" default=""/></td>
	   	<td align="right" width="70">固定电话：</td>
	   	<td width="150">&nbsp;<c:out value="<%=user.getPhone()%>" default=""/></td>
	  </tr>
	  <tr>
	   	<td align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
	   	<td>&nbsp;<c:out value="<%=user.getZip()%>" default=""/></td>
	   	<td align="right">电子邮件：</td>
	   	<td>&nbsp;<c:out value="<%=user.getEmail()%>" default=""/></td>
	  </tr>	  
	  <tr>
	   	<td align="right">通讯地址：</td>
	   	<td colspan="3">&nbsp;<c:out value="<%=user.getAddress()%>" default=""/></td>
	  </tr> 	   	  
	  <tr>
	  	<td colspan="4" align="center" style="border-top:1px dotted #2D6FA9; padding-top:5px;">
		  <a href="${ctx}/j_acegi_logout" target="_parent" title="退出系统">
		    <img src="${ctx}/images/icons/user.jpg" style="margin-right: 2px;">注&nbsp;销
	      </a>&nbsp;
	      <a href="${ctx}/security/user/showSelf.do?model.id=${user.id}" target="main" title="编辑个人信息">
		    <img src="${ctx}/images/icons/update-2.gif" style="margin-right: 2px;">编&nbsp;辑
	      </a>&nbsp;
	      <a href="${ctx}/pages/admin/security/user/user.jsp" target="main" title="返回">
		    <img src="${ctx}/images/grid/view-refresh.png" style="margin-right: 2px;">返&nbsp;回
	      </a>
	  	</td>
	  </tr>
	</table>
  </div>
</div>
</body>
</html>