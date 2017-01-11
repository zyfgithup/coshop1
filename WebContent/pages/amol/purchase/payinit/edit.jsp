<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.purchase.model.PayInit"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/msgDiv.jsp"%>
<title>期初应付信息</title>
<script type="text/javascript" src="${ctx }/pages/amol/purchase/payinit/editjs.js"></script>
</head>
<body>


<div class="x-panel" style="width: 100%">
<div class="x-panel-header">期初应付信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<table width="98%">
	<tr>
		<td align="left"><s:form id="importFrm" namespace="/purchase/payinit" theme="simple" method="POST"
			enctype="multipart/form-data">
			<fieldset style="margin: 10px;"><legend>期初应付Excel数据导入</legend>
			<table align="left">
				<tr>
					<td>批量导入数据：</td>
					<td><s:file name="data" size="30" id="data" cssStyle="background-color: FFFFFF;"/></td>
					<td>&nbsp;&nbsp;
					   <a href="#" onclick="return importData();" class="button">数据导入</a>
					</td>
					<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;<a
						href="#" onclick="init()"><font
						color="blue">【模板下载】</font></a></td>				
				</tr>
				<tr>
					<s:if test="errorInfo.size() > 0">
						<td align="left"><font color="red">系统提示信息：</font></td>
						<td align="left" colspan="3"><s:iterator value="errorInfo">
							<font color="red"><s:property /></font>
							<br />
						</s:iterator></td>
					</s:if>
				</tr>
			</table>
			</fieldset>
		</s:form></td>
	</tr>
</table>


<s:form action="save" id="save" validate="true" method="POST">
	<table align="center" width="95%"> 
	<tr><td><div align="left"><input name="button" class="button" type="button" value="增加供应商应付" onclick="supper()"></div></td></tr>
	</table>
	<center>	

<table  width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9">
  <tbody id="tbody">
    <tr  bgcolor="#F3F4F3" align="center" height="23">
       <td style="width:250px;" >供应商</td>
       <td style="width:280px;" >地址</td>
       <td style="width:150px;" >电话</td>
       <td style="width:150px;">初始金额(元)</td>
       <td style="width:80px;">删除</td>
       
    </tr>
    <tr style="display:none" height="23" bgcolor="#FFFFFF"> 
      <td><input name="sname" type="text" size="32"  readonly="readonly" style="color:#808080;"/>
      <input type="hidden" name="sid" /></td>
      <td><input name="address" type="text" size="32"  readonly="readonly" style="color:#808080;"/></td>
      <td><input name="phone" type="text" size="20"  readonly="readonly" style="color:#808080;"/></td>
      <td><input name="amount" align="right" onblur="yzszss(this)" type="text" size="16" value="0" /></td>
      <td align="center"><input type="button" value="删除"  class="button" onclick="removeRow(this.parentNode.parentNode,0)">
      <input name="status1" type="hidden" value="0"/>
      </td>
    </tr>
   <%
   List<PayInit> sd=(List<PayInit>)request.getAttribute("list");
   if(sd!=null){
   for(PayInit s:sd){
   %>
   <tr height="23" bgcolor="#FFFFFF"> 
   <td><input name="sname" type="text" size="32" value="<%=s.getSupplier().getName() %>" readonly="readonly" style="color:#808080;"/>
      <input type="hidden" name="sid" value="<%=s.getSupplier().getId() %>"/></td>
       <td><input name="address" type="text" size="35" value="<%=s.getSupplier().getAddress() %>" readonly="readonly" style="color:#808080;"/></td>
      <td><input name="phone" type="text" size="16" value="<%=s.getSupplier().getPhone() %>" readonly="readonly" style="color:#808080;"/></td>
     <%if(s.getStatus()==1){ %>
      <td><input name="amount" align="right" readonly="readonly"  type="text" size="16" value="<%=s.getAmount() %>" /></td>
      <td align="center"> <input name="status1" type="hidden" value="1"/></td>
     <%}else{ %>
      <td><input name="amount" align="right"  onblur="yzszss(this)"  type="text" size="16" value="<%=s.getAmount() %>" /></td>
      <td align="center"><input type="button" value="删除" class="button" onclick="removeRow(this.parentNode.parentNode,<%=s.getId()%>)">
       <input name="status1" type="hidden" value="0"/>
      </td>
       <%} %>
    </tr> 
    <%}} %>
    </tbody>
</table>
		
 
	</center>	
	
	<table width="100%" style="margin-bottom: 10px;">
		<tr>
			<td align="center" class="font_white">
			    <s:submit value="保存" cssClass="button" onclick="return saveyz();"/>&nbsp;&nbsp;
				<s:reset value="返回" cssClass="button" onclick="javascript:history. back();"/>
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
<script type="text/javascript">

function importData() {
	
   var data=document.getElementById('data');
   if(data.value==null || data.value==""){
	   alert("请输入文件名！");
	   return;
   }
	var importFrm = document.getElementById('importFrm');
    Ext.MessageBox.confirm('提示', '您确定要导入期初应付数据吗？', function(btn){
        if (btn == 'yes') {
        	loadDiv("正在导入期初数据，请稍等.......");
        	importFrm.action = 'importData.do';
        	importFrm.submit();
       }
    });	
}

</script>
</body>
</html>