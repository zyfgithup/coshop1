<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
<%@include file="/common/meta.jsp" %>
<title></title>
<%@ taglib prefix="stc" uri="/systop/common" %>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/welcome.css"/>
<style type="text/css">
<body { margin: 0px; padding: 0px;  }
</style>
</head>
<body>
<!-- 
<div class="col"> 
   <div class="block">
     <table width="100%" class="toptdborder">
	   <tr>
		 <td class="block-title">
		   <img src="${ctx}/images/icons/sound_1.gif" width="16" height="16">&nbsp;通知公告
		 </td>
		 <td class="block-title"  align="right">
			  <img src="${ctx}/images/icons/more2.gif" width="14" height="14">&nbsp;&nbsp;
		 </td>
	   </tr>
	 </table>
     <div class="block-body">
     </div>
   </div>
    <div class="block">
     <table width="100%" class="toptdborder">
	   <tr>
		 <td class="block-title">
		   <img src="${ctx}/images/icons/authority.gif" width="16" height="16">&nbsp;文章列表
		 </td>
		 <td class="block-title"  align="right">
			  <img src="${ctx}/images/icons/more2.gif" width="14" height="14">&nbsp;&nbsp;
		 </td>
	   </tr>
	 </table>
	 <div class="block-body">
     </div>
   </div>
   <stc:role ifAnyGranted="ROLE_CITY,ROLE_COUNTY">
   <div class="block">
     <table width="100%" class="toptdborder">
	   <tr>
		 <td class="block-title">
		   <img src="${ctx}/images/icons/authority.gif" width="16" height="16">&nbsp;近期短信
		 </td>
		 <td class="block-title"  align="right">
			  <img src="${ctx}/images/icons/more2.gif" width="14" height="14">&nbsp;&nbsp;
		 </td>
	   </tr>
	 </table>
	 <div class="block-body">
     </div>
   </div>
   </stc:role>
 </div>
  -->
</body>
</html>