<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>补卡管理</title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">补卡管理</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save.do" id="cardReplaceFrom" method="post">
	<s:hidden name="model.id" />
	<fieldset style="width: 75%; padding:10px 10px 10px 10px;">
	<legend>补卡信息</legend>
	<table width="100%" align="center">
		<tr>
			<td align="right" width="200">身&nbsp;份&nbsp;证&nbsp;号&nbsp;：</td>
			<td align="left" colspan="3">
				<s:textfield id="idCard" name="idCard" maxlength="18" cssStyle="width:250px;"/>
                <font color="red">*</font> 
                <input type="button" id="btn1" class="button" value="提取用户" onclick="checkcustomer()"/>            	
			</td>			
		</tr>
				
		<tr>
			<td align="right" width="200">持&nbsp;&nbsp;卡&nbsp;&nbsp;人&nbsp;&nbsp;：</td>
			<td align="left" colspan="3">
				<s:textfield name="name" id="customerName" cssStyle="width:250px" disabled="true"/>
				<font color="red">*</font>	
			</td>
		</tr>
	    
	    <tr>
             <td align="right" width="200">继&nbsp;承&nbsp;卡&nbsp;号&nbsp;：</td>
             <td align="left" colspan="3">
			     <select id="cardId" name="oldCardno" style="width:250px">
					    <option value="" >--请选择--
					    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
				 </select>             
			   <font color="red">*</font>		
             </td>
        </tr>	
          
		<tr>
			<td align="right" width="200">代&nbsp;币&nbsp;卡&nbsp;号&nbsp;：</td>
			<td align="left" colspan="3">
				<s:textfield id="cardno" name="cardno" maxlength="22" cssStyle="width:250px;" cssClass="required"
				onkeyup="this.value=this.value.replace(/[^\d+(\d+)?$]/g,'')" />
				<font color="red">*</font>	
			</td>			
		</tr>
	</table>
	</fieldset>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
              <input class="button" type="button" value="保存" onclick="return saveCard()"/>&nbsp;&nbsp;
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
	$("#idCard").focus();
});

//回车或tab键实现提取代币卡功能
$('#idCard').keydown(function(e){
	e = window.event || e;
	if(e.keyCode==13 || e.keyCode == 9){
		e.keyCode=9;
		checkcustomer(); //处理事件
	}
}); 

//回车实现保存补卡功能
$('#cardno').keydown(function(e){
	e = window.event || e;
	if(e.keyCode==13 || e.keyCode == 9){
		saveCard(); //处理事件
	}
}); 
//根据身份证号提取用户
function checkcustomer() {
	var idCard = document.getElementById('idCard').value;	
    if (idCard == null || idCard == '') {
    	alert("请您输入身份证号！");
        return false;
     }
	Ext.Ajax.request({
		url : '/card/replace/checkcustomer.do',
		params : {
			'idCard' : idCard
		},
		method : 'POST',
		success : function(response) {
		    var jsonResult = Ext.util.JSON.decode(response.responseText);
		        if (jsonResult.result == 'error') {
		        	alert("请您重新登录系统"); 
			    	return false;
		        }
		        if (jsonResult.result == 'notExist') {
		        	 alert("系统没有提取到对应的客户信息！");  
			   	     document.getElementById('idCard').value = "";
			   	     document.getElementById('customerName').value = "";
			   	  	 document.getElementById('idCard').focus();
				     return false;
			        }
		        if (jsonResult.result == 'notVIP') {
		        	 alert("该客户不是会员客户");   
			   	     document.getElementById('idCard').value = "";
			   	  	 document.getElementById('customerName').value = "";
			   	 	 document.getElementById('idCard').focus();
				     return false;
			        }		        
			    document.getElementById('customerName').value = jsonResult.customerName;
			    document.getElementById('cardId').focus();
		    }
	});
	
	$.ajax({
		url: '${ctx}/card/replace/getCardsList.do',
		type: 'post',
		dataType: 'json',
		data: {idCard : idCard},
		success: function(rows, textStatus){
		   $('#cardId').empty();
		   $('<option value=\'\'>--请选择--</option>').appendTo('#cardId');
		   for (var i = 0; i < rows.length; i ++) {
		   	var row = rows[i];
		   	$('<option value=' + row.cardId + '>' + row.cardNo + '</option>').appendTo('#cardId');
		   }
		}
	});
	
}
//保存补卡信息
function saveCard() { 
	var idCard = document.getElementById('idCard').value;
	var customerName = document.getElementById('customerName').value;
	var cardId = document.getElementById('cardId').value;
	var oldCardno = document.getElementById("cardId").options[document.getElementById("cardId").options.selectedIndex].text;
	var cardno = document.getElementById('cardno').value;
    if (idCard == null || idCard == '') {
    	alert("请您输入身份证号！");
        return false;
     }
    
    if (customerName == null || customerName == '') {
    	alert("请先提取客户！");
        return false;
     }

    if (cardId == null || cardId == '') {
    	alert("请您选择需要继承的卡号！");
        return false;
     }
    
    if (cardno == null || cardno == '') {
    	alert("请您输入新的代币卡卡号！");
        return false;
     }
    
	Ext.Ajax.request({
		url : '/card/replace/checkCardno.do',
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
	        
	        if (jsonResult.result == 'notExist') {
	        	 alert("卡号【" + cardno + "】不存在！");  
		   	     document.getElementById('cardno').value = "";
			     return false;
		    }
	        
	        if (jsonResult.result == 'available') {
	        	 alert("卡号【" + cardno + "】已被使用！");	   
		   	     document.getElementById('cardno').value = "";
			     return false;
		     }
	        else {
	      	  //调用保存方法
	     	   var frm = document.getElementById('cardReplaceFrom');
	            frm.action = 'save.do';
	            if (confirm("新卡号【" + cardno + "】是否继承卡号为【" + oldCardno + "】的信息?")) {
	            	frm.submit();
	    		}	     	         	
	        }
	    }
	  });
}
</script>
</body>
</html>