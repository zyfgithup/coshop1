<%@page language="java" contentType="application/x-msdownload" pageEncoding="UTF-8"%>
<%
	com.systop.amol.database.model.DataBackup model = (com.systop.amol.database.model.DataBackup) request.getAttribute("model");
	//即将下载的文件的相对路径
	String filedownload = model.getDataUrl()+model.getDataFileName();
	//下载文件时显示的文件保存名称
	String filedisplay = model.getDataFileName();
	filedisplay = java.net.URLEncoder.encode(filedisplay, "UTF-8");
	response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
	try {
		RequestDispatcher dis = application
				.getRequestDispatcher(filedownload);
		if (dis != null) {
			dis.forward(request, response);
		}
		//response.flushBuffer();
		out.clear();
		out=pageContext.pushBody(); 
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		//response.flushBuffer();
	}
%>