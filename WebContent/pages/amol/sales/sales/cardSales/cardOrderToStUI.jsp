<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail,com.systop.amol.base.units.model.UnitsItem,java.util.Set,java.util.Iterator"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<title>出库单</title>
<script type="text/javascript" >

//客户的类别（普通客户    会员客户）
var type = -1;

//客户身份证号     如果客户是会员客户并且用代币卡消费时，以该会员客户的身份证号查询该用户的代币卡信息
var idCard = "";

//CardGrant  的  id
var cardGrantId = "";

//CardGrant  代币卡余额
var balance = "";

//输入的秘密是否正确
var passwordTF = false;

/**
 * 增加一行商品
 */
function addRow()
{
	var orderNo = document.getElementById("orderNoId").value;
	if(orderNo == "" || orderNo.length == 0){
			var stockId = "";
			var stockObject = document.getElementById("storageId");
			if(stockObject != null && stockObject != "" && stockObject.length != 0){
				stockId = stockObject.value;
			}
			var cus=window.showModalDialog("${ctx}/pages/amol/base/product/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
			if(cus!=null){
				
				var userId = cus.userId;
				var productId = cus.id;
				
				$.ajax({
					url: '${ctx}/stock/isStock.do',
					type: 'post',
					dataType: 'json',
					data: {stockId : stockId,userId : userId,productId: productId},
					success: function(rows, textStatus){
						if(rows || stockId == ""){
							var root = document.getElementById("tbody");
						    var allRows = root.getElementsByTagName('tr');
						    var cRow = allRows[1].cloneNode(true);
							cRow.style.display="";
						    root.appendChild(cRow);
						    var rows = root.rows.length ;
						    var tab = document.getElementsByName("pname") ;
						    tab[rows-2].value=cus.name;
						    var tab1 = document.getElementsByName("pid") ;
						    tab1[rows-2].value=cus.id;  
						    var tab2 = document.getElementsByName("stardard") ;
						    tab2[rows-2].value=cus.stardard;
						    var tab3 = document.getElementsByName("unitname") ;
						    tab3[rows-2].value=cus.unitname;
						    var tab11 = document.getElementsByName("unitid") ;
						    tab11[rows-2].value=cus.unitid;  
						    var tab12 = document.getElementsByName("outprice") ;
						    tab12[rows-2].value=cus.outprice;
						}else{
							alert('该商品不再您选择的仓库中');
						}
					}
				});
		    }
	}else{
		alert("该出库单是由订单生成的，商品要与订单保持一致，不能再增加商品！");
	}
} 

/**
 * 删除一行商品
 */
function removeRow(r)
{
    var root = r.parentNode;
    var allRows = root.getElementsByTagName('tr')
    if(allRows.length > 3){
    	
    	var orderNo = document.getElementById("orderNoId").value;
        if(orderNo.length != 0){
        	var index = r.rowIndex-2;
        	var deleteProducts = document.getElementById("deleteProduct");
        	deleteProducts.value = deleteProducts.value + index + ',';
        }
    	
    	root.removeChild(r);
        zjs();
    }
    else{
	    alert("不能删最后一行！");
  }
}

/**
 * 重新选择商品
 */
function edit(r){
	var orderNo = document.getElementById("orderNoId").value;
	if(orderNo == "" || orderNo.length == 0){
		 var stockId = "";
		 var stockObject = document.getElementById("storageId");
		 if(stockObject != null && stockObject != "" && stockObject.length != 0){
			 stockId = stockObject.value;
		 }
		 var cus=window.showModalDialog("${ctx}/pages/amol/base/product/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
		 if(cus!=null){
		
		   	 var userId = cus.userId;
			 var productId = cus.id;
			 $.ajax({
					url: '${ctx}/stock/isStock.do',
					type: 'post',
					dataType: 'json',
					data: {stockId : stockId,userId : userId,productId: productId},
					success: function(rows, textStatus){
						if(rows || stockId == ""){
						var tab = document.getElementsByName("pname") ;
						tab[r.rowIndex-1].value=cus.name;
						var tab1 = document.getElementsByName("pid") ;
						tab1[r.rowIndex-1].value=cus.id;  
						var tab2 = document.getElementsByName("stardard") ;
						tab2[r.rowIndex-1].value=cus.stardard;
						var tab3 = document.getElementsByName("unitname") ;
						tab3[r.rowIndex-1].parentNode.innerHTML="<input name=\'unitname\' type=\'text\' size=\'5\' value=\'"+ cus.unitname
						+"\' onfocus=\'showUnits(this)\' /><input type=\'hidden\' name=\'unitid\'  value=\'"+cus.unitid+"\' />";
						var tab12 = document.getElementsByName("outprice") ;
						tab12[r.rowIndex-1].value=cus.outprice;
						var counts = document.getElementsByName("counts") ;
						counts[r.rowIndex-1].value=0;
						var ncounts = document.getElementsByName("ncount") ;
						ncounts[r.rowIndex-1].value=0;
						js(r);
						}else{
							alert("该商品不再您选择的仓库中！");
						}
					}
				});
		    }
	}else{
		alert("该出库单是由订单生成的，不能改变原订单中的商品！");
	}
}

/**
 * 选择员工
 */
function emp(){
	 var cus=window.showModalDialog("${ctx}/pages/amol/base/employee/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
      if(cus!=null){
	   	   var tab = document.getElementById("empname") ;
	      tab.value=cus.name;
	      var tab1 = document.getElementById("empid") ;
	      tab1.value=cus.id;  
	  }
}

/**
 * 验证输入的必须是数字，不能是字母
 */
function yzsz(price){
	if(isNaN(price.value)){
	    alert("请输入数字！");
		price.value="0";
		return;
	}
	js(price.parentNode.parentNode); 
}

/**
 * 在修改数量之前调用，记住单位间的换算关系
 */
function beforeyzzs(count){
 var r=count.parentNode.parentNode;
 var uids = document.getElementsByName("unitid") ;
 var uid=uids[r.rowIndex-1].value;
 var u1=uid.split(",");
 
if(u1.length<2){
 var counts = document.getElementsByName("counts") ;
 if(counts[r.rowIndex-1].value==count.value){	
   jbl=1;
 }else{
   jbl=counts[r.rowIndex-1].value/count.value;
 }
   uid=uid + ",1," + jbl;
   uids[r.rowIndex-1].value=uid; 
 }
}

/**
 * 验证必须为整数
 */
function yzzs(count){ 
 if(Number(balance) == 0){
	 alert("代币卡余额为零，不能消费！");
 }
 if(isNaN(count.value)){
	alert("请输入数字！");
	count.value="0";
	return false;
 } 
 var r=count.parentNode.parentNode;
 var tab1 = document.getElementsByName("counts") ;
 var uids = document.getElementsByName("unitid") ;
 var uid=uids[r.rowIndex-1].value.split(",");
 var bl=uid[2];
 tab1[r.rowIndex-1].value=count.value*bl;
 if(parseInt(tab1[r.rowIndex-1].value)!=tab1[r.rowIndex-1].value){
		alert("单位数量必须为整数！");
		count.value="0";
		tab1[r.rowIndex-1].value=0;
 }
 count.title="基本数量："+tab1[r.rowIndex-1].value;
 js(count.parentNode.parentNode);
}

/**
 * 验证数字，可以是小数
 */
function yzszss(price){
 if(isNaN(price.value)){
	alert("实收金额请输入数字！");
	price.value="0";
	return;
 }
}

/**
 * 及时一行中的金额
 */
function js(r){  
 var tab = document.getElementsByName("outprice") ;
 var price = tab[r.rowIndex-1].value;
 var tab1 = document.getElementsByName("ncount") ;
 var count = tab1[r.rowIndex-1].value;
 var tab2 = document.getElementsByName("money") ;
 if(!isNaN(price) && !isNaN(count)){
	 var m= price * count;
   	 tab2[r.rowIndex-1].value =m.toFixed(2) ;
 }   
zjs();   	 
}

/**
 * 计算总金额
 */
function zjs(){
	
	 var tab2 = document.getElementsByName("money") ;
	 var amount=0;
	 for(var i = 1; i < tab2.length; i++){
	   amount += Number(tab2[i].value);
	 }
	 
	 var samount = document.getElementById("samount");
	 samount.value = amount;    
 
    if(passwordTF){
		var fkfshk = document.getElementById("hk").value;
		if(balance.length != 0 && balance != ""){
			if(Number(balance) < Number(amount)){
				alert("代币卡消费金额不能大于代币卡余额！");
				ft = true;
			} 
			if(fkfshk == "CARD" || fkfshk == "CARDADVANCE"){
				 document.getElementById("cardamountId").value = amount;
			}
		}
	}
}

/**
 * 点击保存以前的验证
 */
function saveyz(){
	  
	var tab = document.getElementsByName("pname") ;
	var tab1 = document.getElementsByName("money") ;
	if(tab.length==1){
		alert('商品不能为空，请选择商品！');
		return false;
	}
	 for(var i=1;i<tab.length;i++){
			   	if(tab[i]==null ||tab[i].value==""){
			   	alert('商品不能为空，请选择商品！');	
		   		return false;
		   	} 
     }
	 for(var i=1;i<tab1.length;i++){
		   	if(tab1[i].value=="0" || tab1[i].value=="" || Number(tab1[i].value) == 0){
		   		alert('请输入商品数量或单价，数量不能为0！');
		   		return false;
		   	}
       }
	 var ncount = document.getElementsByName("ncount");
	 for(var i=1;i<ncount.length;i++){
		 if(parseInt(ncount[i].value)!=ncount[i].value){
				alert("单位数量必须为整数！（第"+i+"行）");
				ncount[i].value=0;
				return false;
		 }
	 }
	 var tab = document.getElementsByName("outprice") ;    
	 var tab1 = document.getElementsByName("ncount") ;
	 var  tab2 = document.getElementsByName("money") ;
     for(var i = 1; i < tab.length; i++){
    	 var price = tab[i].value;
    	 var count = tab1[i].value;
    	 tab2[i].value = price*count;
      }  
     var amount = 0;
     for(var i = 1; i < tab2.length; i++){
    	amount += Number(tab2[i].value);
     }
     var samount = document.getElementById("samount");
     samount.value = amount;
     
     var fkfshk = document.getElementById("hk");
     if(fkfshk == "CARD" || fkfshk == "CARDADVANCE"){
		 //代币卡消金额
		 var card = document.getElementById("cardamountId");
		 if(Number(card) == Number(amount)){
		 }else{
			 alert('选择代币卡或预划卡收款方式，需要收全款！');
			 return false;
		 }
     }

     if(passwordTF){
    	if(Number(balance) < Number(amount)){
    		alert('代币卡刷卡金额不能大于代币卡余额！');
    		return false;
    	}
     }else{
    	 alert('您没有输入代币卡密码或输入的密码有误！');
    	 return false;
     }
     
     var samount = document.getElementById("samount");
     if(Number(samount.value) == 0){
    	 alert("应收金额不能为零！");
     }
     
     //var codes = document.getElementById("codes").value;
     //alert(codes);
     //if(codes == "" || codes.length == 0){
    //	 if(confirm("您没有扫描条形码，是否扫描？")){
    //		 return false;
     //    }else{
     //    }
     //}
     
     if (confirm("您确定生成的销售出库单吗？")){
    	 return true;
  	 }else{
  		 return false;
  	 }
}

/**
 * 代币卡消费金额不能大于代币余额
 */
function cardMoney(){
	var ft = false;
	var cardamountValue = document.getElementById("cardamountId").value;
	if(Number(balance) < Number(cardamountValue)){
		alert('代币卡刷卡金额不能大于代币卡余额！');
		ft = true;
	}
	return ft;
}

/**
 * 点击单位后加载所有的单位信息
 */
function showUnits(rr){
	
	var r = rr.parentNode.parentNode;
	  var tab1 = document.getElementsByName("pid") ;
      var pid = tab1[r.rowIndex-1].value; 
		
	    if (pid == null || pid == '') {
	    	alert('请选择商品！');
	        return false;
	     }
		Ext.Ajax.request({
			url : '/purchase/getUnitsItemSales.do',
			params : {
				'productid' : pid
			},
			method : 'POST',
			success : function(response) {
			    var jsonResult = Ext.util.JSON.decode(response.responseText);
			        var  result = jsonResult.result ;
			       
			        var unit = result.split(":");
			     
			        var html = "<select name=\'unitid\' onchange=\'unitchange(this)\' style=\'width: 60px\'>";
			        for(var i = 0; i < unit.length - 1; i++){
			        	
			        	var u1 = unit[i].split(",");
			           	html = html + "<option value=\'" + unit[i] + "\' ";
			           	if(rr.value == u1[1]){
			           		html = html + " selected=\'selected\' ";
			           	}
			
			           	html = html + " >" + u1[1] + "</option>";
			           	 }
			        rr.parentNode.innerHTML=html+"</select><input name=\'unitname\' type=\'hidden\'/>";
			        
			    }
		});
	
}

/**
 * 单位转换后基本数量也变化
 */
function unitchange(unitid){
 var r=unitid.parentNode.parentNode;
 var outprice = document.getElementsByName("outprice") ;
 var ncount = document.getElementsByName("ncount") ;
 var count = document.getElementsByName("counts") ;
 var uid = unitid.value.split(",");
 var bl = uid[2];
 outprice[r.rowIndex - 1].value = uid[3];
 var sl = count[r.rowIndex - 1].value/bl;
 ncount[r.rowIndex - 1].value = sl; 
 js(unitid.parentNode.parentNode);
}

/**
 * 选择销售订单
 */
function selectorder(){
	var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/sales/order/cardOrder/selector.jsp",null,"dialogWidth=55","dialogHeight=300px");
    if(cus!=null){
    	window.location.href="cardOrderToEditUI.do?orderId=" + cus.id;
	}
}

/**
 * 用代币卡消费时，验证客户输入的代币卡密码是否正确
 */
function validatePassword(){
	
	//密码提示
	var isPasswordObject = document.getElementById("isPasswordId");
	
	//密码
	var passwordValue = document.getElementById("passwordId").value;
	if(passwordValue.length == 6){
		$.ajax({
			url: '${ctx}/card/grant/validatePassoword.do',
			type: 'post',
			dataType: 'json',
			data: {cardGrantId : cardGrantId,password : passwordValue},//CardGrantId  的id    password CardGrant的密码
			success: function(rows, textStatus){
				passwordTF = rows;
				if(rows){
					isPasswordObject.innerHTML = "<font color='green'>密码输入正确</font>";
					document.getElementById("balanceId").value = balance;//代币卡余额
					var cardMoney = document.getElementById("cardamountId");
					cardMoney.value = document.getElementById("samount").value;
				}else{
					document.getElementById("balanceId").value = "";//代币卡余额置空
					document.getElementById("passwordId").value = "";
					isPasswordObject.innerHTML = "<font color='red'>密码输入错误</font>";
					if(passwordValue != null && passwordValue != "" && passwordValue.length != 0){
				    	document.getElementById("passwordId").focus();
				    }
				}
			}
		});
	}else{
		document.getElementById("balanceId").value = "";//代币卡余额置空
		document.getElementById("passwordId").value = "";
	    isPasswordObject.innerHTML = "<font color='red'>请输入6位密码</font>";
	    if(passwordValue != null && passwordValue != "" && passwordValue.length != 0){
	    	document.getElementById("passwordId").focus();
	    }
	}
}

//计算实收金额，并且实收款单金额不能大于应收款单金额
function ssje(){
	//var spayamountObject = document.getElementById("spayamount");
	var cardamountValue = document.getElementById("cardamountId").value;
	var ss = Number(cardamountValue);
	//spayamountObject.value = ss;
	
	cardMoney();
	
	//应收款单金额
	var ys = document.getElementById("samount").value;
	if(ss > ys){
		alert('实收款金额不能大于应收款金额！');
		document.getElementById("cardamountId").value=0.0;
	}
}

/**
 * 通过代币卡号确定客户身份
 */
function validateCard(){
	var customeridObject = document.getElementById("customerid");//客户id
	var nameObject = document.getElementById("name");//客户姓名
	var balanceObject = document.getElementById("balanceId");//代币卡余额
	var orderCardNo = document.getElementById("cardNo").value;//代币卡卡号
	var passwordObject = document.getElementById("passwordId");//代币卡密码
	var userId = document.getElementById("userId").value;//经销商id
	var orderCustomerId = document.getElementById("orderCustomerId").value;
	/*
	if(orderCardNo.length != 22){
		initCard(customeridObject,nameObject,balanceObject,passwordObject);
		document.getElementById("cardNo").value='';
		alert("请扫描22位的代币卡");
		document.getElementById("isPasswordId").innerHTML = '';

	}*/
	if(orderCustomerId != null && orderCustomerId != "" && orderCustomerId.length != 0){
		$.ajax({
			url: '${ctx}/card/replace/getCard.do',
			type: 'post',
			dataType: 'json',
			data: {cardNo : orderCardNo,userId : userId,orderCustomerId : orderCustomerId},//orderCardNo代币卡号   userId经销商   orderCustomerId下订单的客户id
			success: function(rows, textStatus){
				if(rows == null){
					initCard(customeridObject,nameObject,balanceObject,passwordObject);
					document.getElementById("cardNo").value='';
					document.getElementById("cardNo").focus();
					alert("代币卡卡号不存在！");
					document.getElementById("isPasswordId").innerHTML = '';
					return false;
				}else{
					for (var i = 0; i < rows.length; i ++) {
					   	var row = rows[i];
					   	if(!row.available){
							initCard(customeridObject,nameObject,balanceObject,passwordObject);
							document.getElementById("cardNo").value='';
							document.getElementById("cardNo").focus();
							alert("代币卡状态不正常或持卡客户被禁用！");
							document.getElementById("isPasswordId").innerHTML = '';
							return false;
						}
					   	if(!row.jxs){
					   		initCard(customeridObject,nameObject,balanceObject,passwordObject);
							document.getElementById("cardNo").value='';
							document.getElementById("cardNo").focus();
							alert("该消费代币卡不属于此销售商品的经销商！");
							document.getElementById("isPasswordId").innerHTML = '';
							return false;
					   	}
					   	if(!row.orderCustomer){
					   		initCard(customeridObject,nameObject,balanceObject,passwordObject);
							document.getElementById("cardNo").value='';
							document.getElementById("cardNo").focus();
							alert("该刷卡人不是下销售订单的客户！");
							document.getElementById("isPasswordId").innerHTML = '';
							return false;
					   	}
					   	if(row.available && row.jxs && row.orderCustomer){
					   		initCard(customeridObject,nameObject,balanceObject,passwordObject);
					   		customeridObject.value=row.customerId;//客户id
					   		nameObject.value=row.customerName;//客户姓名
					   		cardGrantId = row.cardGrantId;//CardGrant 的 id
							balance = row.balance;//代币卡余额
						}
				    }
				}
			}
		});
	}else{
		$.ajax({
			url: '${ctx}/card/replace/getCard.do',
			type: 'post',
			dataType: 'json',
			data: {cardNo : orderCardNo,userId : userId},//orderCardNo代币卡号
			success: function(rows, textStatus){
				if(rows == null){
					initCard(customeridObject,nameObject,balanceObject,passwordObject);
					document.getElementById("cardNo").value='';
					document.getElementById("cardNo").focus();
					alert("代币卡卡号不存在！");
					document.getElementById("isPasswordId").innerHTML = '';
					return false;
				}else{
					for (var i = 0; i < rows.length; i ++) {
					   	var row = rows[i];
					   	if(!row.available){
							initCard(customeridObject,nameObject,balanceObject,passwordObject);
							document.getElementById("cardNo").value='';
							document.getElementById("cardNo").focus();
							alert("代币卡状态不正常或持卡客户被禁用！");
							document.getElementById("isPasswordId").innerHTML = '';
							return false;
						}
					   	if(!row.jxs){
					   		initCard(customeridObject,nameObject,balanceObject,passwordObject);
							document.getElementById("cardNo").value='';
							document.getElementById("cardNo").focus();
							alert("该消费代币卡不属于此销售商品的经销商！");
							document.getElementById("isPasswordId").innerHTML = '';
							return false;
					   	}
					   	if(row.available  && row.jxs){
					   		initCard(customeridObject,nameObject,balanceObject,passwordObject);
					   		customeridObject.value=row.customerId;//客户id
					   		nameObject.value=row.customerName;//客户姓名
					   		cardGrantId = row.cardGrantId;//CardGrant 的 id
							balance = row.balance;//代币卡余额
						}
				    }
				}
			}
		});
	}
}

//初始卡信息
function initCard(customeridObject,nameObject,balanceObject,passwordObject){
	customeridObject.value="";//客户id
	nameObject.value="";//客户姓名
	balanceObject.value = "";//代币卡余额
	passwordObject.value = "";//代币卡密码
	cardGrantId = "";//CardGrant 的 id
}

function csh(){
	var orderCustomerId = document.getElementById("orderCustomerId").value;
	if(orderCustomerId != null && orderCustomerId != "" && orderCustomerId.length != 0){
		cardGrantId = document.getElementById("orderCardGrantId").value;
		validateCard();
		$(document).ready(function(){
			$("#passwordId").focus();
		});
	}else{
		$(document).ready(function(){
			$("#cardNo").focus();
		});
	}
}
</script>
</head>
<body onload="csh();">
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">出库单信息【代币卡】</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save" id="save" validate="true" method="POST">
	<br/>
	<table align="center" width="95%">
		<tr>
			<td>
			<div align="left">
			<input name="button" type="button"	value="选择订单" class="button" onclick="selectorder()">
			<input type="text" name="orderNo" id="orderNoId" value="${orderNo }"  readonly="readonly"/>
			<input type="hidden" name="orderId" value="${orderId }"/>
			</div>
			</td>
		</tr>
	</table>
	<s:hidden name="model.id"  /> 
	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>出库单【代币卡】</legend>
	<table width="100%" align="left" >
		<tr>
			<td align="right" >
				编 &nbsp;&nbsp;&nbsp;号：
			</td>
			<td width="200" class="simple" align="left">
				<s:hidden id="userId" name="model.user.id"/>
				<input type="hidden" id="orderCustomerId" name="orderCustomerId" value="${orderCustomerId }"/>
				<input type="hidden" id="orderCardGrantId" name="orderCardGrantId" value="${orderCardGrantId }"/>
			    <s:textfield  name="model.salesNo" maxLength="20" 
					cssStyle="width:155px;color:#808080;" cssClass="required" readonly="true"/>
           		<!-- font color="red">*</font-->
			</td>
			<td  align="right" >
			         日&nbsp;&nbsp;&nbsp;&nbsp;期：</td>
			<td width="273" class="simple" align="left">
				<s:textfield  name="model.createTime" maxLength="20" cssStyle="width:155px;" cssClass="required" 
				onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" readonly="true"/>
           		<font color="red">*</font>
			</td>
		</tr>
		<tr>
			<td align="right" >
				客&nbsp;&nbsp;&nbsp;&nbsp;户：
			</td>
			<td class="simple" align="left">
			   <s:hidden id="customerid" name="model.customer.id"/>
			   <input type="text" id="name" name="model.customer.name" value="${model.customer.name }" readonly="readonly" maxLength="20" style="width: 155px;" class="required"/>
			   <font color="red">*</font>
			</td>
			<td align="right">销&nbsp;售&nbsp;员：</td>
			<td align="left">
				<s:hidden name="model.employee.id" id="empid" />
				<s:textfield name="model.employee.name" id="empname"  maxLength="20" style="width: 155px;" readonly="true" />
				<span id="ywy"></span>
				<input name="button2" class="button" type="button" onclick="emp()" value="选择"/>
			</td>
		</tr>
		<tr>
			<td align="right">应&nbsp;&nbsp;&nbsp;&nbsp;收：</td>
			<td align="left" colspan="1">
				<s:textfield  name="model.samount" id="samount" maxLength="20" size="25"
				  readonly="true" cssStyle="width:155px;color:#808080;" cssClass="required"/>
				<font color="red">*</font>
			</td>
			<td align="right" >
				操&nbsp;作&nbsp;员：
			</td>
			<td class="simple" align="left">
				<s:textfield  name="model.user.name" disabled="true" maxLength="20" cssStyle="width:155px;" />
			</td> 
		</tr>
		<tr>
			<td align="right">收款方式：</td>
			<td align="left" colspan="1">
				<s:select list="PaymentCardMap" id="hk" name="model.payment" value="model.payment" cssStyle="width:156px;"></s:select>
			</td>
			<td align="right"><c:if test="${storageMap != null }">仓&nbsp;&nbsp;&nbsp;&nbsp;库：</c:if></td>
			<td align="left" colspan="1">
			  <c:if test="${storageMap != null }">
					<s:select list="storageMap" name="model.storage.id" id="storageId" 
						cssStyle="width:156px;color:#808080;border: 1px solid #808080;"/>
			  </c:if>
			</td>
		</tr>
		
		<tr>
			<td align="right"><font color="blue">代&nbsp;币&nbsp;卡</font>：</td>
			<td align="left" colspan="1">
                <input type="text" id="cardNo" name="salesCardNo" value="${orderCardNo }"  maxlength="22" size="30"/>
				<input type="button" onclick="validateCard();" value="提取" class="button"/>
			</td>
			<td align="right" >
				<font color="blue">密&nbsp;&nbsp;&nbsp;&nbsp;码</font>：
			</td>
			<td class="simple" align="left">
				<s:password id="passwordId" name="cardPassword" onblur="validatePassword();" maxLength="6" size="27"></s:password>
				<span id="isPasswordId"></span>
			</td> 
		</tr>

		<tr>
			<td align="right"><font color="blue">剩余金额</font>：</td>
			<td align="left">
				<s:textfield id="balanceId" name="balance" readonly="true" maxLength="20" size="40"/>
			</td>
			<td align="right"><font color="blue">刷卡金额</font>：</td>
			<td align="left">
				<s:textfield id="cardamountId" name="model.cardamount" 
				onblur="cardMoney();" readonly="true" maxLength="20" size="25"/>
			</td>
		</tr>
		<tr> 
		    <td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td colspan="3" class="simple" align="left">
			  <s:textfield name="model.remark" cssStyle="width:593px;" />
			  <input type="hidden" name='deleteProduct' id='deleteProduct' value="${deleteProduct }"/>
			</td>
		</tr>
	  </table>
	</fieldset>

	<table align="center" width="95%"> 
	<tr>
		<td>
   				<div align="left"><input name="button" type="button" value="增加商品" class="button"  onclick="addRow()"></div>
		</td>
	</tr>
	</table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9"> 
  <tbody id="tbody">
    <tr bgcolor="#F3F4F3" align="center" height="23">
       <td style="width:100px;" colspan="2">商 品</td>
       <td style="width:100px;">规格</td>
       <td style="width:50px;">单位</td>
       <td style="width:60px;">单价</td>
       <td style="width:60px;">数量</td>
       <!-- td style="width:80px;">基本单位数量</td-->
       <td style="width:70px;">金额</td>
       <td style="width:80px;">备注</td>
       <td style="width:110px;">操作</td>
    </tr>
    <tr style="display:none" bgcolor="#FFFFFF"> 
      <td>
          <input name="pname" type="text" size="10"  readonly="readonly" style="color:#808080;"/>
          <input type="hidden" name="pid" />
          <input type="hidden" name="orderSalesDetailId"/>
      </td>
      <td><input name="button1"  type="button" onclick="edit(this.parentNode.parentNode)" value="选择" class="button"/></td>
      <td><input name="stardard" type="text" size="14" readonly="readonly" style="color:#808080;"/></td> 
      <td><input name="unitname" type="text" size="5"  onfocus="showUnits(this)"/><input type="hidden" name="unitid"/></td>
      <td><input name="outprice" type="text" size="5" value="0" onkeyup="yzsz(this)" /></td>
      <td>
      	<input name="ncount" type="text" size="6" value="0" onkeyup="yzzs(this);" onfocus="beforeyzzs(this)"/>
      	<input name="counts" type="hidden" size="10" value="0"  readonly="readonly" style="color:#808080;"/>
      </td>
      <td><input name="money" type="text" size="8" value="0" readonly="readonly" style="color:#808080;"/></td>
      <td><input name="sremark" type="text" size="15" /></td>
      <td align="center">
      	  <input type="button" value="删除" onclick="removeRow(this.parentNode.parentNode)" class="button" style="width: 40px;">
      	  <input type='button' value='扫码' onclick="scanCode(this.parentNode.parentNode);" class="button" style="width: 40px;"/>
		  <input type='hidden' name='codes' id='codes'/>
      </td>
    </tr>
   <%
   List<SalesDetail> sd=(List<SalesDetail>)request.getAttribute("sd");
   if(sd!=null){
   for(SalesDetail s : sd){
   %>
   <tr bgcolor="#FFFFFF"> 
      <td>
      	  <input name="pname" type="text" size="10" value="<%=s.getProducts().getName()%>" readonly="readonly" style="color:#808080;"/>
          <input type="hidden" name="pid" value="<%=s.getProducts().getId()%>"/>
          <input type="hidden" name="orderSalesDetailId" value="<%=s.getId()%>"/>
	  </td>
	  <td>
	  		<input name="button1" type="button" onclick="edit(this.parentNode.parentNode)" value="选择" class="button"/>
	  </td>
      <td><input name="stardard" value="<%=s.getProducts().getStardard()%>" type="text" size="14" readonly="readonly" style="color:#808080;"/></td> 
      <td>
           <input name="unitname" value="<%=s.getUnits().getName()%>" type="text" size="5" onfocus="showUnits(this)"/>
           <input type="hidden"  value="<%=s.getUnits().getId()%>" name="unitid"/>
      </td>
      <td><input name="outprice" value="<%=s.getOutPrice()%>" type="text" size="5" value="0" onkeyup="yzsz(this)"/></td>
      <td>
      	<% 
      	      int u = 0;//单位对应的基本单位数量
      	      Set<UnitsItem> unitsItemSet = s.getUnits().getUnitsItem();
    	      Iterator<UnitsItem> unitsItemIs = unitsItemSet.iterator();
    	      while(unitsItemIs.hasNext()){
    	    	UnitsItem unitsItem = unitsItemIs.next();
    	    	if(unitsItem.getProducts().getId().intValue() == s.getProducts().getId().intValue()){
    	    		u = unitsItem.getCount();
    	    		break;
    	    	}
    	      }
      	 %>
      	<input name="ncount" value="<%=((float)s.getTnorootl())/u%>" type="text" size="6" value="0" onkeyup="yzzs(this);" onfocus="beforeyzzs(this)" />
      	<input name="counts" value="<%=s.getTnorootl()%>" type="hidden" size="10" value="0"  readonly="readonly" style="color:#808080;"/>
      </td>
      <td><input name="money" value="<%=(((float)s.getTnorootl())/u) * s.getOutPrice()%>" type="text" size="8" value="0" readonly="readonly" style="color:#808080;"/></td>
      <td><input name="sremark" value="<%=s.getRemark()%>" type="text" size="15" /></td>
      <td align="center">
          <input type="button" value="删除" onclick="removeRow(this.parentNode.parentNode)" class="button" style="width: 40px;" >
          <input type='button' value='扫码' onclick="scanCode(this.parentNode.parentNode);" class='button' style="width: 40px;"/>
		  <input type='hidden' name='codes' id='codes'/>
      </td>
    </tr>
    <%}} %>
    </tbody>
</table>
		
		
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

//回车实现提取代币卡功能
$('#cardNo').keydown(function(e){
	e = window.event || e;
	if(e.keyCode==13 || e.keyCode == 9){
		e.keyCode=9;
		validateCard();
		//$('#card').focus();
	}
});

$('#passwordId').keydown(function(e){
	e = window.event || e;
	if(e.keyCode==13 || e.keyCode == 9){
		e.keyCode=9;
		validatePassword();
		//$('#card').focus();
	}
});
/** ready */
$(document).ready(function() {
	$("#save").validate();
});

/**
 * 扫码
 */
function scanCode(r){
	var countObject = document.getElementsByName("ncount") ;
	var count = countObject[r.rowIndex-1].value;
	
	var codesObject = document.getElementsByName("codes") ;
	var codes = codesObject[r.rowIndex-1].value;
	
	var unitnameObject = document.getElementsByName("unitname") ;
	var unitname = unitnameObject[r.rowIndex-1].value;
	unitname = encodeURI(unitname);
	if(0 != count){
		var cus = window.showModalDialog(URL_PREFIX+"/pages/amol/sales/scanCode/scanCode.jsp?count="+count+"&codes='"+codes+"'&unitname='"+unitname+"'",null,"dialogWidth=55","dialogHeight=300px");
		if(cus!=null){
	    	var codeObject = document.getElementsByName("codes") ;
	    	codeObject[r.rowIndex-1].value = cus.str;
		}
	}else{
		alert('请输入商品数量！');
	}
}
</script>
</body>
</html>