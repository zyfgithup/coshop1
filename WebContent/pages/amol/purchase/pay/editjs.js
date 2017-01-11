/**
 * 得到此供应商的应付的入库单
 */
function addRow()
{ 

	var supperid = document.getElementById('sid').value;	
    if (supperid == null || supperid == '') {
        alert("请您选择供应商！");
        return false;
     }
 var frm=document.getElementById('save');
 frm.action="getPurchaseValue.do";
frm.submit();
}
/**
*选择供应商
*/
function supplier(){
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/base/supplier/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
 			if(cus!=null){
          var tab = document.getElementById("supname") ;
	      tab.value=cus.name;
	      var tab1 = document.getElementById("sid") ;
	      tab1.value=cus.id; 
 				 }
	}else{//火狐
		window.open(URL_PREFIX+"/pages/amol/base/supplier/selector.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	     this.returnAction=function(cus){
		    if(cus!=null){
		    	var tab = document.getElementById("supname") ;
	          tab.value=cus.name;
	          var tab1 = document.getElementById("sid") ;
	         tab1.value=cus.id; 
		    }
		    }
	     }
		 supplierEdit();  
		 addRow(); 
		 
}

/**
 * 得到员工
 */
function emp(){
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
	 var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/base/employee/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
      if(cus!=null){
	   	   var tab = document.getElementById("empname") ;
	      tab.value=cus.name;
	      var tab1 = document.getElementById("empid") ;
	      tab1.value=cus.id;  
		   }
	}else{
		window.open(URL_PREFIX+"/pages/amol/base/employee/selector.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	     this.returnAction=function(cus){
		    if(cus!=null){
		    	 var tab = document.getElementById("empname") ;
			      tab.value=cus.name;
			      var tab1 = document.getElementById("empid") ;
			      tab1.value=cus.id; 
		    }
		  }
		}
}

//验证现金为数字
function yzszss(price,yfje){
     if(price.value=="-"){
    	 return;
     }
	if(isNaN(price.value)){
		alert("本次付款请输入数字！");
		price.value="0";
		 var tab = document.getElementsByName("amount1") ;  	
	     var amount=0;
	     for(var i=0;i<tab.length;i++){
	    	amount+=Number(tab[i].value);
	    	 }
	     var samount=document.getElementById("linkAmount");
	      samount.value=Number(amount);
		return;
	}


	
	if(Number(price.value)-0!=0 && Number(yfje)>0 && (Number(price.value)-Number(yfje)>0 || Number(price.value)<0)){
		alert("本次付款不能超过未付金额！");
		price.value="0";    
		
		//return;
	}
	if(Number(price.value)-0!=0 && Number(yfje)<0 && (Number(price.value)-Number(yfje)<0||Number(price.value)>0 )){
		alert("本次付款不能超过未付金额！");
		price.value="0";    
		
		//return;
	}
	 var tab = document.getElementsByName("amount1") ;  	
     var amount=0;
     for(var i=0;i<tab.length;i++){
    	amount+=Number(tab[i].value);
    	 }
     var samount=document.getElementById("linkAmount");
      samount.value=Number(amount);
}
/**
 * 自动关联没有付款的入库单。
 */
function link(){
	 var nopay = document.getElementsByName("nopay") ;  
	 var amount1 = document.getElementsByName("amount1") ; 
	  var amount=document.getElementById("amount").value;
	  if(amount==""){
		  amount = 0;
	  }
	  for(var i=0;i<nopay.length;i++){
	    	 amount1[i].value="0";
	     } 
     for(var i=0;i<nopay.length;i++){
    	if(amount>0 && amount-nopay[i].value > 0){
    		 amount1[i].value = nopay[i].value;
    		 amount=amount-nopay[i].value;
    		 amount=Math.round(amount*100)/100;
    	 }else{
    		 amount1[i].value=Math.round(amount*100)/100;
    		 break;
    	 }    	
     }
     var tmpamount=0;
     for(var i=0;i<amount1.length;i++){
    	tmpamount+=Number(amount1[i].value);
    	 }
     var samount=document.getElementById("linkAmount");
      samount.value=Number(tmpamount);
}
/**
 * 付款金额数字验证
 */
function fkyz(fk){
	if(isNaN(fk.value)){
		alert("付款金额请输入数字！");
		fk.value="0";
		return;
    }
    if(fk.value-0<0){
		alert("付款金额应该大于0！");
		fk.value="0";
    }
}
/**
 * 保存前的验证
 */
function saveyz(){
 var tab = document.getElementsByName("amount1") ;  	
	     var amount=0;
	     for(var i=0;i<tab.length;i++){
	    	if(tab[i].value-0!=0){
	    	amount=1;
	    	 }
	    	 }
    
    if(amount-0 == 0){
    	alert("输入本次付款！");
    	return false;
    }
    var linkamount=document.getElementById("linkAmount");
    var amount=document.getElementById("amount");
    if(linkamount.value-amount.value!=0){
    	alert("关联金额和付款金额必须相等！");
    	return false;
    }
    if(confirm('您确定生成的采购付款单信息吗？')){
    	return true;
	  }else {
		return false;
	  }
}
/**
 * 供应商改变后，删除商品
 */
function supplierEdit(){
	 var root = document.getElementById("tbody");
     var allRows = root.getElementsByTagName('tr');
     var row=allRows.length;
     for(var i = 1; i <row; i++){
    	 root.removeChild(allRows[1]);
      }
     
      var samount = document.getElementById("linkAmount");
      samount.value = 0;
      var nopayamout = document.getElementById("nopayamout");
      nopayamout.innerHTML = 0;
      
}