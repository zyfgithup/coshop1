<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.ReceiveInit"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/msgDiv.jsp"%>
<title>应收期初</title>
<script type="text/javascript" src="${ctx }/pages/amol/sales/receiveInit/editjs.js"></script>
<script type="text/javascript">

/**
 * Excel期初应收数据导入
 */
function importData() {
	var importFrm = document.getElementById('importFrm');
    Ext.MessageBox.confirm('提示', '您确定要导入期初应收数据吗？', function(btn){
        if (btn == 'yes') {
        	loadDiv("正在导入期初数据，请稍等.......");
        	importFrm.action = 'importData.do';
        	importFrm.submit();
       }
    });
}

/**
 * 删除一行
 */
function removeRow(r,receiveInitId)
{
	if(confirm("是否删除？")){
		if(Number(receiveInitId) == 0){
			deleteView(r);
		}else{
			$.ajax({
				url: URL_PREFIX+'/receiveInit/delete.do',
				type: 'post',
				dataType: 'json',
				data: {receiveInitId : receiveInitId},
				success: function(rows, textStatus){
					if(rows != null && rows != "" && rows.length != 0){
						alert(rows);
					}else{
						if("success" == textStatus){
							deleteView(r);
						}
					}
				}
			});
		}
	}
}

function deleteView(r){
	alert("删除成功");
	var root = r.parentNode;
    var allRows = root.getElementsByTagName('tr');
    root.removeChild(r);
}
</script>
</head>
<body>


<div class="x-panel" style="width: 100%">
<div class="x-panel-header">应收期初信息</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">

<table width="98%">
	<tr>
		<td align="left"><s:form id="importFrm" namespace="/purchase/payinit" theme="simple" method="POST"
			enctype="multipart/form-data">
			<fieldset style="margin: 10px;"><legend>期初应收Excel数据导入</legend>
			<table align="left">
				<tr>
					<td>批量导入数据：</td>
					<td><s:file name="data" size="30" cssStyle="background-color: FFFFFF;" /></td>
					<td>&nbsp;&nbsp;
					   <a href="#" onclick="return importData();" class="button">数据导入</a>
					</td>
					<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;<a
						href="javascript:init();"><font
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
	<tr><td><div align="left"><input name="button" class="button" type="button" value="增加经销商应收" onclick="customer()"></div></td></tr>
	</table>
	<center>	
	<table width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9">
  <tbody id="tbody">
    <tr bgcolor="#F3F4F3" align="center" height="23">
       <td style="width:200px;" >地址</td>
       <td style="width:100px;">客户</td>
       <td style="width:100px;">身份证号</td>
       <td style="width:100px;">手机号</td>
       <td style="width:100px;">初始金额（元）</td>
       <td style="width:100px;">删除</td>
    </tr>
    <tr style="display: none" height="23" align="center" bgcolor="#FFFFFF">
      <td><input name="area" type="text" size="23"  readonly="readonly" style="color:#808080;"/></td>	 
      <td><input name="customerName" type="text" size="15"  readonly="readonly" style="color:#808080;"/>
      	<input type="hidden" name="customerId" />
      	<input type="hidden" name="ids"/>
      </td>
      <td><input name="idCard" type="text" size="20"  readonly="readonly" style="color:#808080;"/></td>
      <td><input name="mobile" type="text" size="15"  readonly="readonly" style="color:#808080;"/></td>
      <td><input name="amount" align="right" onkeyup="yzszss(this)" maxlength="10" type="text" size="25" value="0" style="text-align: right;" /></td>
      <td align="center"><input type="button" value="删除"  class="button" onclick="removeRow(this.parentNode.parentNode,0)"></td>
    </tr>
   <%
   List<ReceiveInit> riList = (List<ReceiveInit>)request.getAttribute("list");
   if(riList != null){
   for(ReceiveInit ri : riList){
   %>
   <tr bgcolor="#FFFFFF">
   	  <td>
   	  	<input type="hidden" name="ids" value="<%=ri.getId() %>"/>
   	  	<input name="area" type="text" size="23"  readonly="readonly" value="<%=ri.getCustomer().getRegion().getName() %>" style="color:#808080;"/>
   	  	
   	  </td>
      <td><input name="customerName" type="text" size="15" value="<%=ri.getCustomer().getName() %>" readonly="readonly" style="color:#808080;"/>
      <input type="hidden" name="customerId" value="<%=ri.getCustomer().getId() %>"/></td>
	  <td><input name="idCard" type="text" size="20" value="<%=ri.getCustomer().getIdCard() %>" readonly="readonly" style="color:#808080;"/></td>
      <td><input name="mobile" type="text" size="15" value="<%=ri.getCustomer().getMobile() %>" readonly="readonly" style="color:#808080;"/></td>
      <td>
      	<%
      		if(ri.getStatus().intValue() == com.systop.amol.sales.SalesConstants.INIT_NORMAL.intValue()){
      	%>
      			<input name="amount" align="right" onkeyup="yzszss(this)" maxlength="10" type="text" size="25" value="<%=ri.getAmount() %>" style="text-align: right;" />
      	<%
   			}else if(ri.getStatus().intValue() == com.systop.amol.sales.SalesConstants.INIT_LOCKING.intValue()){
      	%>
      			<input name="amount" align="right" readonly="readonly" onkeyup="yzszss(this)" maxlength="10" type="text" size="25" value="<%=ri.getAmount() %>" style="text-align: right;" />
      	<%
   			}
      	%>
      </td>
      <td align="center"><input type="button" value="删除" class="button" onclick="removeRow(this.parentNode.parentNode,<%=ri.getId() %>)"></td>
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
</body>
</html>