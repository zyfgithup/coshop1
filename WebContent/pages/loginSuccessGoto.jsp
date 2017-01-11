<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<body>
	<script language="javascript">
		var platform = window.navigator.platform;
		var loginPath;
		if (platform == "winCW"){
			loginPath = "pdaCenter.jsp";
		}else{
			loginPath = "layout.jsp";
		}
		window.location.href = loginPath;
	</script>
</body>
</html>
