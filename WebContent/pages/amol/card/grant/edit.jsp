<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>发卡管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<script type="text/javascript">
//格式化当前日期
function currenDate () 
{ 
	var day = new Date(); 
	var Year = 0; 
	var Month = 0; 
	var Day = 0; 
	var CurrentDate = ""; 
	//初始化时间 
	Year= day.getFullYear();//ie火狐下都可以 
	Month= day.getMonth()+1; 
	Day = day.getDate();
	CurrentDate += Year + "-"; 
	if (Month >= 10 ) 
	{ 
		CurrentDate += Month + "-"; 
	} 
	else 
	{ 
		CurrentDate += "0" + Month + "-"; 
	} 
	if (Day >= 10 ) 
	{ 
		CurrentDate += Day ; 
	} 
	else 
	{ 
		CurrentDate += "0" + Day ; 
	} 
	return CurrentDate; 
} 
</script>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">发卡管理</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save.do" id="save" validate="true" method="post">
	<s:hidden name="model.id" id="cardGrantId" />
	<fieldset style="width: 90%; padding:10px 10px 10px 10px;">
	<legend>发卡信息</legend>
	<table width="100%" align="left">
		<tr>
			<td align="right" width="150">经&nbsp;销&nbsp;商：</td>
			<td align="left" id="j" >
				<s:textfield name="model.customer.owner.name" id="owner" maxlength="18" cssStyle="width:250px" cssClass="required" readOnly="true"/>								    	
			    <font color="red">&nbsp;*</font>
			    <input type="button" id="btn1" class="button" value="选择经销商" onclick="showAgent()"/>
			    <s:hidden id="ownerId" name="model.customer.owner.id"></s:hidden>
			</td>
		</tr>
		<tr>
			<td align="right" width="150">身份证号：</td>
			<td align="left" >
				<s:textfield name="model.customer.idCard" id="idCard" maxlength="18" cssClass="required" cssStyle="width:250px" />						    	
			    <font color="red">&nbsp;*</font>
			    <input type="button" id="btn1" class="button" value="  提取用户" onclick="showUser()"/>
			</td>
		</tr>
		
		<tr>
			<td align="right" width="150">客户名称：</td>
			<td align="left" >
				<s:textfield name="customerName" id="customerName"  cssStyle="width:250px" disabled="true"/>
				<s:hidden name="model.owner.id" />
			</td>			
		</tr>
		
		<tr>
			<td align="right" width="150">代币卡号：</td>
			<td align="left" >
				<s:textfield name="model.card.cardNo" id="cardno" cssStyle="width:250px;" maxlength="22" cssClass="required" 
					onkeyup="this.value=this.value.replace(/[^\d+(\d+)?$]/g,'')"/>				
				<font color="red">&nbsp;*【注：请认真核实您的卡号！】</font>
			</td>
		</tr>	

		<tr>
			<td align="right" width="150">存款单号：</td>
			<td align="left" >
				<s:textfield name="model.depositReceipt" id="depositReceipt" cssStyle="width:250px;" cssClass="required depositReceipt"/>				
				<font color="red">&nbsp;*</font>
				<span id="deposit">&nbsp;</span>
			</td>
		</tr>
		
		<tr>
			<td align="right" width="150">
				密&nbsp;&nbsp;码&nbsp;&nbsp;：
			</td>
			<td align="left" colspan="3">
				<s:password id="repwd" name="model.password" cssStyle="width:250px;" cssClass="required passwordValidator"/>
				<font color="red">&nbsp;*</font>
				<span id="respwd">&nbsp;请输入六位密码</span>
			</td>
		</tr>
		
		<tr>
			<td align="right" width="150">
				确认密码：
			</td>
			<td align="left" colspan="3">
				<s:password id="doubleRepwd" name="doubleRepwd" cssStyle="width:250px;" cssClass="required passwordCheck"/>
				<font color="red">&nbsp;*</font>
				<span id="pswd">&nbsp;请再次输入密码</span>	
			</td>
		</tr>
		
		<tr>
			<td align="right" width="150">失效日期：</td>
			<td align="left" colspan="3">
				<input type="text" name="model.endDate" style="width: 250px" id="endDate"
					value='<s:date name="endDate" format="yyyy-MM-dd"/>'
					onfocus="WdatePicker({minDate:currenDate(),skin:'whyGreen',dateFmt:'yyyy-MM-dd'})"
					class="Wdate required" /><font color="red">&nbsp;&nbsp;*【注：请认真填写失效日期，提交后不能修改！】</font>
			</td>
		</tr>	
							
		<tr>					
			<td align="right" width="150">信用额度：</td>
			<td align="left" >
				<s:textfield id="credit" name="model.credit" style="width: 250px" cssClass="required moneyValidator"
				onkeyup="this.value=this.value.replace(/[^\d+(\.\d+)?$]/g,'')" />
				<font color="red">&nbsp;*</font>
				<span id="money">&nbsp;</span>
			</td>
		</tr>
		
		<tr>					
			<td align="right" width="150">描&nbsp;&nbsp;&nbsp;&nbsp;述：</td>
			<td align="left" >
				<s:textarea id="" name="model.descn" rows="4" cols="33"/>
			</td>
		</tr>
	</table>
	</fieldset>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
				<table>
					<tr>
						<td width="100" align="left">
							<s:submit cssClass="button" value="保存" onclick="return saveValidate();"/>
						</td>
						<td width="100" align="left">
							<input type="button" value="返回" onclick="history.go(-1)" class="button"/>
						</td>
						<td width="300" align="left">
							<s:reset value="重置" cssClass="button" />
						</td>
				
					</tr>
				</table>
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
<script type="text/javascript">
//页面加载身份证号获取焦点
$(document).ready(function(){
	$("#owner").focus();
});

//密码框
<c:if test="${model.id != null}">
$(function() {
    $('#doubleRepwd').val("******");
    $('#repwd').val("******");
}); 
</c:if>
//回车实现提取经销商
$('#owner').keydown(function(e){
	e = window.event || e;
	if(e.keyCode==13 || e.keyCode == 9){
		e.keyCode=9;
		showAgent(); //处理事件
	}
}); 
//回车实现提取用户
$('#idCard').keydown(function(e){
	e = window.event || e;
	if(e.keyCode==13 || e.keyCode == 9){
		e.keyCode=9;
		showUser(); //处理事件
	}
}); 

//回车实现提取代币卡功能
$('#cardno').keydown(function(e){
	e = window.event || e;
	if(e.keyCode==13 || e.keyCode == 9){
		e.keyCode=9;
		showCard(); //处理事件
	}
}); 

//提取经销商
function showAgent() {
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog("${ctx}/pages/amol/user/agent/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
	    if(cus!=null){
	    	var tab = document.getElementById("owner") ;
			tab.value=cus.name;
			var tab1 = document.getElementById("ownerId") ;
			tab1.value=cus.id;
		}
 	}else{
		window.open("${ctx}/pages/amol/user/agent/selector.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	    this.returnAction=function(cus){
	    	if(cus!=null){
	    		var tab = document.getElementById("owner") ;
	    		tab.value=cus.name;
	    		var tab1 = document.getElementById("ownerId") ;
	    		tab1.value=cus.id;
	    	}
		};
 	}
	document.getElementById('idCard').value = "";
	document.getElementById('customerName').value = "";
	document.getElementById('idCard').focus();
}


//提取用户
function showUser() {
	var idCard = document.getElementById('idCard').value;
	var ownerId = document.getElementById('ownerId').value;
  	if (idCard == null || idCard == '') {
  	  alert("请您输入身份证号！");     
      return false;
   }
	Ext.Ajax.request({
		url : '/card/grant/selectCustomer.do',
		params : {
			'idCard' : idCard,
			'ownerId' :ownerId
		},
		method : 'POST',
		success : function(response) {
		    var jsonResult = Ext.util.JSON.decode(response.responseText);
		        if (jsonResult.result == 'error') {
		        	alert("请您重新登录系统！");	       
			    	return false;
		        }
		        if (jsonResult.result == 'agent') {
		        	alert("请选择经销商名称！");	   
			   	     //document.getElementById('idCard').value = "";
			   	  	 document.getElementById('customerName').value = "";
				     return false;
			        }
		        if (jsonResult.result == 'notExist') {
		        	alert("没有您输入的客户信息！"); 
			   	     document.getElementById('idCard').value = "";
			   	  	 document.getElementById('customerName').value = "";
				     return false;
			        }
		        //if (jsonResult.result == 'ownerCard') {
		        	// alert("该用户在此供应商下存在卡信息！");	   
			   	    // document.getElementById('idCard').value = "";
			   	  	// document.getElementById('customerName').value = "";
				     //return false;
			        //}
		        if (jsonResult.result == 'notVIP') {
		        	 alert("您不是会员客户！");	   
			   	     document.getElementById('idCard').value = "";
			   	 	 document.getElementById('customerName').value = "";
				     return false;
			        }
		        document.getElementById('idCard').value = jsonResult.idCard;
			    document.getElementById('customerName').value = jsonResult.customerName;
			    document.getElementById('cardno').focus();
		    }
	});
}

//验证代币卡是否有效
function showCard() { 
	var cardno = document.getElementById('cardno').value;
    if (cardno == null || cardno == '') {
    	alert("请您输入卡号！");
        return false;
     }
    
	Ext.Ajax.request({
		url : '/card/grant/selectCardno.do',
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
	        if (jsonResult.result == 'notCard') {
	        	 alert("请核实卡号！");	   
		   	     document.getElementById('cardno').value = "";
		   	  	 document.getElementById('cardno').focus();
			     return false;
		    }
	        if (jsonResult.result == 'notNull') {
	        	alert("您的卡已被占用！");	   
		   	     document.getElementById('cardno').value = "";
		   	  	 document.getElementById('cardno').focus();
			     return false;
		        }
	        	document.getElementById('cardno').value = jsonResult.cardno;
	        	//document.getElementById('depositReceipt').focus();
		}
	});
}

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

$(function() {

	$.validator.addMethod("depositReceipt", function(value, element){
		$("#deposit").html("");
		var exist;
	    $.ajax({
			url: '${ctx}/card/grant/check.do',
			type: 'post',
			async : false,
			dataType: 'json',
			data: {"model.id" : $("#cardGrantId").val(),"model.depositReceipt" : value},
			success: function(rows, textStatus){
				exist = rows.exist;
			
				if (exist) {
						$('#deposit').html('<b>存款单号为【'+value+'】已发代币卡!</b>');
		       	}else {
		       	    	$('#deposit').html('');
		       	}
			}
	   });
	    return !exist;
	},"");
});
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
)

$(function() {
	$.validator.addMethod("moneyValidator", function(value, element) {      
		var res;
        var value;
        if("" == value.length){
        	return true;
        }else if (value == 0){
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

function saveValidate(){
	var cardno = document.getElementById('cardno').value;
	if(confirm('您确定要保存卡号为'+cardno+'代币卡吗？请认真核实，保存后不允许修改！')){
    }else{
   	 return false;
    }
}

$(document).ready(function() {
	$("#save").validate();
});
</script>
</body>
</html>