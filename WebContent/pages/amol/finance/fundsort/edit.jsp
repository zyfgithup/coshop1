<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp" %>
<%@include file="/common/validator.jsp"%>
</head>

<body>
<div class="x-panel">
<div class="x-panel-header">资金类型</div>
<div><%@ include file="/common/messages.jsp"%></div> 
	<div align="center">
	<s:form  action="save.do" id="save" method="post" theme="simple" validate="true">
	<s:hidden name="model.id"/>
	<fieldset style="width:510px; padding:10px 10px 10px 10px;">
    	<legend>编辑资金类型</legend>
        <table width="510px" align="center">
          <tr>
             <td align="right" width="90">名称：</td>
             <td align="left" width="400">
             	<s:textfield id="name" name="model.name" cssStyle="width:250px" cssClass="required"/><font color="red">&nbsp;*</font>
             </td>
          </tr>
        </table> 
    </fieldset>
    <table width="510px" style="margin-bottom:10px;">
		<tr>
			<td style="text-align:center;">
				<s:submit value="保存" cssClass="button" />
				<s:reset value="重置" cssClass="button"/>
				<input type="button" value="返回" onclick="history.go(-1)" class="button"/>
		   </td>
		</tr>
	</table>
	</s:form>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
	$("#save").validate();
});
</script>
</body>
</html>