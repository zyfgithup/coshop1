<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>文章管理</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">文章管理</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save.do" id="save" method="post">
	<s:hidden name="model.id" />
	<fieldset style="width: 40%; padding:10px 10px 10px 10px;">
	<legend>文章管理</legend>
	<table width="100%" align="left">
        <tr>
           <td align="right">
           标题：
           </td>
           <td align="left" colspan="3">
           		<s:textfield  id="title" name="model.title" cssClass="required"  ></s:textfield >
           		<font color="red">*</font>
			</td>
        </tr>		
  <tr>
		<tr>
			<td align="right">
				类型：
			</td>
			<td align="left" colspan="3">
				<s:select list="#{'1':'支付帮助','2':'关于我们','3':'用户指南','4':'联系我们','5':'法律条款','6':'用户协议'}" name="model.wzType" cssClass="required" cssStyle="width:200px;"></s:select>
				<font color="red">*</font>
			</td>
		</tr>
		<tr>
           <td align="right">
           内容：
           </td>
           <td align="left" colspan="3" rowspan="3">
			   <!-- 加载编辑器的容器 -->
			   <script id="container" name="model.content" type="text/plain">${model.content}</script>
			   <script type="text/javascript" src="${ctx }/ueditor/utf8-jsp/ueditor.config.js"></script>
			   <!-- 编辑器源码文件 -->
			   <script type="text/javascript" src="${ctx }/ueditor/utf8-jsp/ueditor.all.js"></script>
			   <!-- 实例化编辑器 -->
			   <script type="text/javascript">
				   var ue = UE.getEditor('container');
			   </script>
           		<font color="red">*</font>
			</td>
        </tr>
	</table>
	</fieldset>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			  <s:submit value="保存" 	cssClass="button" /> 
			  &nbsp;&nbsp;
			  <input type="button" value="返回" onclick="history.go(-1)" class="button"/>
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
<script type="text/javascript">


</script>
</body>
</html>