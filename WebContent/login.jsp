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
		if (platform == "WinCE"){
			loginPath = "loginPDA.jsp";
		}else{
			loginPath = "loginDEF.jsp";
		}
		
		var error_code = "<%=request.getParameter("login_error")%>";
		if (error_code.length > 0 && error_code != "null") {
			loginPath = loginPath + "?login_error=" + error_code;
		}
		window.location.href = loginPath;
	</script>


</body>
</html>
