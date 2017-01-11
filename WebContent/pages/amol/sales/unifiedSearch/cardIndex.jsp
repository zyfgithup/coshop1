<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<html>
<head>
<title>销售查询【网银】</title>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >
	<div style="float: left;">销售查询【网银】</div>
	<div style="float: right;">
	</div>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-toolbar">
   <form action="cardIndex.do" method="post">
          	&nbsp;&nbsp;单子类型:<s:select list='listTypeMap' name="listType"></s:select>  
	                      单号:<s:textfield name="model.salesNo" size="17" id="salesNo"/> 
	                      用户:<s:textfield name="model.user.name" size="5" id="name"/>
        	开始日期:<input id="startDate" name="startDate" size="11"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
       		结束日期:<input id="endDate" name="endDate" size="11" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"  readonly="readonly"/>
       		<input type="submit" id="role_index_0" value="&#26597;&#35810;" class="button" style="width:36px;"/>
   </form>
</div>
</div>
</body>
</html>