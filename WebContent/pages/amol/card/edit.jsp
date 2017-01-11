<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>代币卡管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">代币卡管理</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save.do" id="save" method="post">
	<s:hidden name="model.id" />
	<fieldset style="width: 70%; padding:10px 10px 10px 10px;">
	<legend>录入代币卡</legend>
	<table width="100%" align="left">
		<tr>
			<td align="right" width="140">代&nbsp;币&nbsp;卡&nbsp;号&nbsp;：</td>
			<td align="left" colspan="3">
				<s:textfield id="cardNo" name="model.cardNo" cssStyle="width:250px;" maxlength="22" cssClass="required, cardNoValidator"
				onkeyup="this.value=this.value.replace(/[^\d+(\d+)?$]/g,'')"/>
				<font color="red">&nbsp;*</font>
				<span id="cardno">请输入22位数字</span>				
			</td>
		</tr>
		
		<tr>
			<td align="right" width="140">
           	描&nbsp;&nbsp;&nbsp;&nbsp;述&nbsp;&nbsp;&nbsp;&nbsp;：
           </td>
           <td align="left" colspan="3">
           		<s:textarea id="" name="model.descn" rows="4" cols="33"/>
			</td>			
		</tr>
	</table>
	</fieldset>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			  <s:if test="model.id == null">
			  	<s:submit value="保存" cssClass="button"/>
		      </s:if>
			  <s:else>
      		  	<s:submit value="修改" cssClass="button"/>
    		  </s:else>
    		  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   		  
		      	<s:reset value="重置" cssClass="button" />
		      	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      	<input type="button" value="返回" onclick="history.go(-1)" class="button"/>
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
<script type="text/javascript">

$(function() {
	$.validator.addMethod("cardNoValidator", function(value, element) {      
		var res;
        var len = value.length;		
		if (len == 22){
	        re = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{3})$/);
		    $("#cardno").html("");
		}else{
	    	res = "err";
	    	$("#cardno").html("<font color='red'>请输入22位数字卡号</font>");
	    }		
        return res != "err";
    },"");
});

$(document).ready(function(){
	$("#cardNo").focus();
});

$(document).ready(function() {
	$("#save").validate();
});

</script>
</body>
</html>