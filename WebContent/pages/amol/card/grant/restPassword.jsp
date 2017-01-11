<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>重置密码</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">重置密码</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save.do" id="save" method="post">
	<s:hidden name="model.id" />
	<fieldset style="width: 70%; padding:10px 10px 10px 10px;">
	<legend>重置密码</legend>
	<table width="100%" align="center">
		<tr>
			<td align="right" width="150">代&nbsp;币&nbsp;卡&nbsp;号&nbsp;：</td>
			<td align="left" colspan="3">
				<s:textfield id="cardno" name="model.card.cardNo"  cssStyle="width:250px;" maxlength="22" cssClass="required"
				onkeyup="this.value=this.value.replace(/[^\d+(\d+)?$]/g,'')" />
				<font color="red">&nbsp;*</font>	
				<input type="button" id="btn1" class="button" value="提取卡号" onclick="checkCard()"/>
			</td>			
		</tr>
		
		<tr>
			<td align="right" width="150">身&nbsp;份&nbsp;证&nbsp;号&nbsp;：</td>
			<td align="left" colspan="3">
				<s:textfield id="idCard" name="idCard" cssStyle="width:250px;" readOnly="true" cssClass="selectCustomer"/>
				<span id="sfzh"></span>
                <font color="red">&nbsp;*</font>    
                         	
			</td>			
		</tr>
				
		<tr>
			<td align="right" width="150">持&nbsp;&nbsp;卡&nbsp;&nbsp;人&nbsp;&nbsp;：</td>
			<td align="left" colspan="3">
				<s:textfield name="customer" id="customer" cssStyle="width:250px" disabled="true" cssClass="required"/>
			</td>
		</tr>
	 
		<tr>
			<td align="right" width="150">
				密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码&nbsp;&nbsp;：
			</td>
			<td align="left" colspan="3">
				<s:password id="repwd" name="model.password" maxlength="6" cssStyle="width:250px;" cssClass="required passwordValidator"/>
				<font color="red">&nbsp;*</font>
				<span id="respwd">&nbsp;请输入六位密码</span>
			</td>
		</tr>
		
		<tr>
			<td align="right" width="150">
				确&nbsp;认&nbsp;密&nbsp;码&nbsp;：
			</td>
			<td align="left" colspan="3">
				<s:password id="doubleRepwd" name="doubleRepwd" maxlength="6" cssStyle="width:250px;" cssClass="required passwordCheck"/>
				<font color="red">&nbsp;*</font>
				<span id="pswd">&nbsp;请再次输入密码</span>
			</td>
		</tr>
	</table>
	</fieldset>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
              <s:submit value="保存" cssClass="button"/>&nbsp;&nbsp;
              <input type="button" value="返回" onclick="history.go(-1)" class="button"/>&nbsp;&nbsp;
			  <s:reset value="重置" cssClass="button" />
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
<script type="text/javascript">
//页面加载身份证号获取焦点
$(document).ready(function(){
	$("#cardno").focus();
});
$(function() {
	$.validator.addMethod("selectCustomer", function(value, element) {
		
        var re;
        var idCard = document.getElementById('idCard').value;
  		if(idCard.length == 0 ) {
  			re = "err";
  			document.getElementById('sfzh').innerHTML = '<font color="red">'+'请您先提取卡号！'+'</font>';  			
  		}else{
  			re = "ok";
  			document.getElementById('sfzh').innerHTML = '';		
  		}
        return re != "err";
    },"");
	}
);
//验证密码一致性
$(function() {
	$.validator.addMethod("passwordCheck", function(value, element) {
        var re;
        var pwd1 = document.getElementById('repwd').value;
  		var pwd2 = document.getElementById('doubleRepwd').value;
  		if(pwd1 != null & pwd2 != null & pwd1 != '' & pwd2 != '') {
  			if(pwd1 != pwd2) {
  				re = "err";
  				document.getElementById('pswd').innerHTML = '<font color="red">'+'两次密码不一致。'+'</font>';
  			}else{
  				re = "ok";
  				document.getElementById('pswd').innerHTML = '';		
  			}
  		}
        return re != "err";
    },"");
	}
);

//密码六位的验证
$(function() {
	$.validator.addMethod("passwordValidator", function(value, element) {      
		var res;
        var len = value.length;		
		if (len == 6){
	        res = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{3})$/);
		    $("#respwd").html("");
		}else{
	    	res = "err";
	    	$("#respwd").html("<font color='red'>请输入6位密码</font>");
	    }		
        return res != "err";
    },"");
});
//回车实现提取用户和代币卡功能
$('#cardno').keydown(function(e){
	e = window.event || e;	
	if(e.keyCode==13 || e.keyCode == 9){
		e.keyCode = 9;
		checkCard(); //处理事件
	}
}); 

//验证卡信息
function checkCard() {
	var cardno = document.getElementById('cardno').value;   
    if (cardno == null || cardno == '') {
    	alert("请您输入卡号！");
        return false;
     }
    
	Ext.Ajax.request({
		url : '/card/rest/selectCardno.do',
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
		   	  	 document.getElementById('idCard').value = "";
			     document.getElementById('customer').value = "";
		   	     document.getElementById('cardno').value = "";
		   	  	 document.getElementById('cardno').focus();
			     return false;
		    }
	        
	        if (jsonResult.result == 'notCard') {
	        	alert("您输入的卡号有误！请重新输入！");
		   	  	 document.getElementById('idCard').value = "";
			     document.getElementById('customer').value = "";
		   	     document.getElementById('cardno').value = "";
		   	  	 document.getElementById('cardno').focus();
			     return false;
		    }
	        document.getElementById('idCard').value = jsonResult.idCard;
		    document.getElementById('customer').value = jsonResult.customer;
		    document.getElementById('cardno').value = jsonResult.cardno;
		    document.getElementById('repwd').focus();
	    }
	  });
}
$(document).ready(function() {
	$("#save").validate();
});
</script>
</body>
</html>