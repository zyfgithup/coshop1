<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<title>选择仓库</title>
</head>
<body>
<table width="100%">
	<tr>
		<td>
		<div><iframe name="iframe_employee" id="iframe_employee"
			src="${ctx}/base/storage/showIndex.do"
			style="HEIGHT: 500px; WIDTH: 646px;" frameborder="0" scrolling="no"></iframe>
		</div>
		</td>
	</tr>
</table>
</body>
</html>