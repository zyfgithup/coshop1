<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>沾沾乐系统-PDA</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<LINK href="${ctx}/styles/style.css" type='text/css' rel='stylesheet'>
</head>
<body>
<table width="100%">
  <tr>
    <td >欢迎您：<stc:username></stc:username>登录农资PDA系统！</td>
    <td align="right">		
	  <a href="${ctx}/security/user/changePasswordPda.do" target="_self" style="color: #336699">个人信息</a>   
	  &nbsp;|&nbsp; 	
      <a href="${ctx}/j_acegi_logout" target="_self" style="color: #336699">注销&nbsp;</a>
	</td>
  </tr>
  <tr>
    <td height="25" style="border-bottom: 1px dashed #AAC0DD;padding-left: 2px;" colspan="2">
      <a href="${ctx}/pda/sales/editCard.do" target=_self class="button">出货</a>
      <a href="#" onclick="alert(22)" class="button">出货冲红</a>
      <a href="#" onclick="alert(22)" class="button">退货</a>
      <a href="#" onclick="alert(22)" class="button">退货冲红</a>
      <a href="#" onclick="alert(33)" class="button">查询</a>
    </td>
  </tr>
 </table> 

</body>
</html>
