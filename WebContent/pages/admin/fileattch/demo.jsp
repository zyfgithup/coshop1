<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/uploadify.jsp"%>
<script type="text/javascript" src="${ctx}/pages/admin/fileattch/fileattch.js"></script>
<LINK href="${ctx}/styles/style.css" type='text/css' rel='stylesheet'>
<LINK href="${ctx}/pages/admin/fileattch.css" type='text/css' rel='stylesheet'>
<title>上传演示</title>
</head>
<body>
<input type="file" id="systop_upload"/>&nbsp;注：<b><span style="color:#EB2A03">附加大小在100M以内!</span></b>
<div id="custom"><div id="systop-file-queue"></div></div>
<div id="systop-uploaded-files" style="width:690px; border-bottom: 1px dotted #000;">
    <input type="hidden" id="fileattchids" name="fileattchids"/>
</div>
<div id="systop_file_list"></div>
<script type="text/javascript">
  //渲染上传组件
  renderUploader();
  viewFileAttchList("1343489", true);
</script>
</body>
</html>