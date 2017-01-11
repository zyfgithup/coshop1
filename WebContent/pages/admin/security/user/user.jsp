<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>个人信息</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
</head>
<body>
<div class="x-panel">
  <div class="x-panel-header">个人信息</div>
  <div class="x-toolbar"></div>
  <div class="x-panel-body">
  <table height="15">
    <tr>
      <td></td>
    </tr>
  </table>
  <table width="60%" align="center">
    <tr>
      <td>
        <fieldset style="margin: 10px; height: 180px;"><legend>个人信息</legend>
          <div align="center" style="margin-top: 15px;">
            <a style="color: #0099FF;" href="${ctx}/security/user/changePassword.do">
              <img src="${ctx}/images/icons/modify.gif" style="margin-right: 2px;">修改密码</a>
          </div>
          <!-- -->
          <div align="center" style="margin-top: 15px;">
            <a style="color: #0099FF;" href="${ctx}/user/userInfo.do">
              <img src="${ctx}/images/icons/authority.gif" style="margin-right: 2px;">个人信息</a>
          </div>
           
  		</fieldset>
      </td>
    </tr>
  </table>
  </div>
</div>
</body>
</html>