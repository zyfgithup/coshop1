<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>充值管理</title>
<%@include file="/common/ec.jsp"%>
<%@ include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">充值管理</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save.do" id="save" method="post">
	<s:hidden name="model.id" />
	<fieldset style="width: 80%; padding:10px 10px 10px 10px;">
	<legend>代币卡充值</legend>
	<table width="100%" align="left">
		<tr>
			<td align="right" width="150">代&nbsp;币&nbsp;卡&nbsp;号&nbsp;：</td>
			<td align="left" colspan="3">
				<s:if test="model.id == null">
					<s:textfield type="text" name="model.cardGrant.card.cardNo" id="cardno" style="width: 250px" maxlength="22" cssClass="required" 
					onkeyup="this.value=this.value.replace(/[^\d+(\d+)?$]/g,'')"/>
					<font color="red">&nbsp;*</font>
					<input type="button" id="btn1" class="button" value="提取客户" onclick="showCard()"/>
				</s:if>
				<s:if test="model.id != null">
					<s:textfield type="text" name="model.cardGrant.card.cardNo" style="width: 250px" readonly="true" />
					<font color="red">&nbsp;*</font>
				</s:if>
			</td>
		</tr>
		<tr>
			<td align="right" width="150">客&nbsp;户&nbsp;名&nbsp;称&nbsp;：</td>
			<td align="left" colspan="3">
				<s:if test="model.id == null">
					<s:textfield name="customerName" id="customerName"  cssStyle="width:250px" disabled="true"/>
				</s:if>
				<s:if test="model.id != null">
					<s:textfield type="text" name="model.cardGrant.customer.name" style="width: 250px" readonly="true" />
				</s:if>
				<s:hidden name="model.owner.id" />	
			</td>			
		</tr>
		
		<tr>
			<td align="right" width="150">
           		充值金额(元)：
           </td>
           <td align="left" colspan="3">
           		<s:textfield id="upMoney" name="model.upMoney" style="width: 250px" cssClass="required moneyValidator" 
           		onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')"/>
           		<font color="red">&nbsp;*</font>
           		<span id="money">&nbsp;</span>
			</td>			
		</tr>
	</table>
	</fieldset>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			  <s:if test="model.id == null">
			  <s:submit value="添加" cssClass="button"/>
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

//回车实现提取用户和代币卡功能
$('#cardno').keydown(function(e){
	e = window.event || e;	
	if(e.keyCode==13){
		e.keyCode = 9;
		showCard(); //处理事件
	}
}); 

//代币卡卡号获取焦点
$(document).ready(function(){
	$("#cardno").focus();
});

//验证卡信息
function showCard() { 
	var cardno = document.getElementById('cardno').value;
	//var customerName = document.getElementById('customerName').value;
    if (cardno == null || cardno == '') {
    	alert("请您先输入卡号！");
        return false;
     }
    
	Ext.Ajax.request({
		url : '/card/up/selectCardno.do',
		params : {
			'cardno' : cardno
		},
		method : 'POST',
		success : function(response) {
			var jsonResult = Ext.util.JSON.decode(response.responseText);
	        if (jsonResult.result == 'error') {
	        	alert("请您重新登录系统！");	       
		    	return false;
	        }
	        if (jsonResult.result == 'cardState') {
	        	 alert("您的卡已被注销！");
	        	 document.getElementById('cardno').focus();
		   	     document.getElementById('cardno').value = "";
		   	  	 document.getElementById('customerName').value = "";
			     return false;
		    }
	        if (jsonResult.result == 'fill') {
		   	     alert("您的卡已挂失！");
		   	  	 document.getElementById('cardno').focus();
		   	     document.getElementById('cardno').value = "";
		   	  	 document.getElementById('customerName').value = "";
			     return false;
		        }
	        if (jsonResult.result == 'notCard') {
	        	alert("请输入正确的卡号！");	   
	        	 document.getElementById('cardno').focus();
		   	     document.getElementById('cardno').value = "";
		   	  	 document.getElementById('customerName').value = "";
			     return false;
		        }
		    document.getElementById('cardno').value = jsonResult.cardno;
		    document.getElementById('customerName').value = jsonResult.customerName;
		    document.getElementById('upMoney').focus();
	    }
	});
    
}
$(function() {
	$.validator.addMethod("moneyValidator", function(value, element) {      
		var res;
        var value;		
		if (value == 0){
			res = "err";
	    	$("#money").html("<font color='red'>请输入大于0的数字</font>");
		}else if(isNaN(value)){
			res = "err";
	    	$("#money").html("<font color='red'>请输入数字</font>");
		}
		else{
	    	$("#money").html("");
	    }		
        return res != "err";
    },"");
});

$(document).ready(function() {
	$("#save").validate();
});
</script>
</body>
</html>