<%@ page language="java" import="java.util.*,java.lang.management.*" pageEncoding="utf-8"%>  
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">  
<html>  
  <head>  
    <base href="<%=basePath%>">  
    <title>虚拟机监测</title>  
  </head>  
  <body>  
    <%   
    Runtime lRuntime = Runtime.getRuntime();   
    out.println("*** BEGIN MEMORY STATISTICS ***<br>");   
    out.println("Max   Memory: "+lRuntime.maxMemory()/1024/1024+"M<br>");   
    out.println("Total Memory: "+lRuntime.totalMemory()/1024/1024+"M<br>");   
    out.println("Free  Memory: "+lRuntime.freeMemory()/1024/1024+"M<br>");   
    out.println("Available Processors : "+lRuntime.availableProcessors()+"<br>");  
    out.println("*** END MEMORY STATISTICS ***");  
   out.println("<br>");  
     %>  
    <ul>  
    <li>Max   Memory:虚拟机最大能从服务器上挖到的内存数</li>  
    <li>Total Memory:虚拟机当前实际挖到的内存数</li>  
    <li>Free  Memory:虚拟机挖到但没有使用的内存数</li>  
    <li>Available Processors :服务器的CPU数目</li>  
    </ul>  
  </body>  
</html>  
