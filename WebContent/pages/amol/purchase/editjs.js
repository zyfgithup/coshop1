
/**
 * 增加一行商品信息
 */
function addRow()
{   	var sid=document.getElementById("sid").value;
	   if(sid==0){
		alert("请选择供应商后再选择商品！");
		return ;
		}
  if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
  		var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/base/product/pselector.jsp?sid="+sid,null,"dialogWidth=800px;resizable: Yes;");
		  if(cus!=null){
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
		     var tab12 = document.getElementsByName("inprice") ;
		     tab12[rows-2].value=cus.inprice;
		     var tab13 = document.getElementsByName("pcode") ;
			    tab13[rows-2].value=cus.code; 
			    showUnits(tab3[rows-2]);
		  }
  }else{
		window.open(URL_PREFIX+"/pages/amol/base/product/pselector.jsp?sid="+sid,"","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
        this.returnAction=function(cus){
		    if(cus!=null){
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
		        var tab12 = document.getElementsByName("inprice") ;
		        tab12[rows-2].value=cus.inprice;
		        var tab13 = document.getElementsByName("pcode") ;
			    tab13[rows-2].value=cus.code; 
			    showUnits(tab3[rows-2]);
		    }
        };
  }

}


/**
 * 删除一行（行号）
 */
function removeRow(r)
{
  var root = r.parentNode;
  var allRows = root.getElementsByTagName('tr');
  if(allRows.length>3){
	  root.removeChild(r);
      zjs();
  }else{
      alert("不能删最后一行！");
  }
}

/**
 * 点选择商品时（行号）
 */
function edit(r){
	var sid=document.getElementById("sid").value;
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
	 var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/base/product/pselector.jsp?sid="+sid,null,"dialogWidth=800px;resizable: Yes;");
	 if(cus!=null){
		var tab = document.getElementsByName("pname") ;
		tab[r.rowIndex-1].value=cus.name;
		var tab13 = document.getElementsByName("pcode") ;
		tab13[r.rowIndex-1].value=cus.code;
		var tab1 = document.getElementsByName("pid") ;
		tab1[r.rowIndex-1].value=cus.id;  
		var tab2 = document.getElementsByName("stardard") ;
		tab2[r.rowIndex-1].value=cus.stardard;
		var tab3 = document.getElementsByName("unitname") ;
		tab3[r.rowIndex-1].parentNode.innerHTML="<input name=\'unitname\' type=\'text\' size=\'4\' value=\'"+ cus.unitname
		+"\' onfocus=\'showUnits(this)\' /><input type=\'hidden\' name=\'unitid\'  value=\'"+cus.unitid+"\' />";
		var tab12 = document.getElementsByName("inprice") ;
		tab12[r.rowIndex-1].value=cus.inprice;
		var counts = document.getElementsByName("count") ;
		counts[r.rowIndex-1].value=0;
		var ncounts = document.getElementsByName("ncount") ;
		ncounts[r.rowIndex-1].value=0;
		ncounts[r.rowIndex-1].title="";
		js(r);
		showUnits(tab3[r.rowIndex-1]);
		
	  }
	}else{
		window.open(URL_PREFIX+"/pages/amol/base/product/pselector.jsp?sid="+sid,"","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
          this.returnAction=function(cus){
	    if(cus!=null){
	    	var tab = document.getElementsByName("pname") ;
			tab[r.rowIndex-1].value=cus.name;
			var tab13 = document.getElementsByName("pcode") ;
			tab13[r.rowIndex-1].value=cus.code;
			var tab1 = document.getElementsByName("pid") ;
			tab1[r.rowIndex-1].value=cus.id;  
			var tab2 = document.getElementsByName("stardard") ;
			tab2[r.rowIndex-1].value=cus.stardard;
			var tab3 = document.getElementsByName("unitname") ;
			tab3[r.rowIndex-1].parentNode.innerHTML="<input name=\'unitname\' type=\'text\' size=\'4\' value=\'"+ cus.unitname
			+"\' onfocus=\'showUnits(this)\' /><input type=\'hidden\' name=\'unitid\'  value=\'"+cus.unitid+"\' />";
			var tab12 = document.getElementsByName("inprice") ;
			tab12[r.rowIndex-1].value=cus.inprice;
			var counts = document.getElementsByName("count") ;
			counts[r.rowIndex-1].value=0;
			var ncounts = document.getElementsByName("ncount") ;
			ncounts[r.rowIndex-1].value=0;
			ncounts[r.rowIndex-1].title="";
			js(r);
			showUnits(tab3[r.rowIndex-1]);
	    }
       };
	}
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
}

/**
 * 选择订单 
 */
function selectorder(){
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/purchase/findorder.jsp",null,"dialogWidth=800px;resizable: Yes;");
		if(cus!=null){	 
		window.location=URL_PREFIX+"/purchase/convertPurchase.do?oid="+cus; 
			 }
	}else{
		window.open(URL_PREFIX+"/pages/amol/purchase/findorder.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	    this.returnAction=function(cus){
		   if(cus!=null){
		    window.location=URL_PREFIX+"/purchase/convertPurchase.do?oid="+cus; 
		   }
	    };
	}
	
}

/**
 * 选择员工
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
		 };
 	}
}

/**
 * 验证单价输入的必须是数字，并且计算
 */
function yzsz(price){
	if(isNaN(price.value)||price.value-0<0){
		alert("请输入数字并且大于0！");
		price.value="0";
		//return;
	}
	js(price.parentNode.parentNode); 
}

/**
 * 在修改数量之前，把换算关系存上，以便修改基本数量
 */
function beforeyzzs(count){
	var r=count.parentNode.parentNode;
	 var uids = document.getElementsByName("unitid") ;
	 var uid=uids[r.rowIndex-1].value;
	 var u1=uid.split(",");
	if(u1.length<2){
	 var counts = document.getElementsByName("count") ;
	 if(counts[r.rowIndex-1].value==count.value || count.value-0 == 0){	
		 jbl=1;
	 }else{
      jbl=counts[r.rowIndex-1].value/count.value;
	 }
      uid=uid+",1,"+jbl;
      uids[r.rowIndex-1].value=uid;
	}
}

/**
 * 验证数量必须为数字，并且根据比率算出基本数量
 */
function yzzs(count){ 
	 var tab1 = document.getElementsByName("count") ;
	 var r=count.parentNode.parentNode;
	if(isNaN(count.value)||count.value-0<0){
		alert("请输入数字并且大于0！");
		count.value="0";
		tab1[r.rowIndex-1].value=0;
		js(count.parentNode.parentNode);
		return;
	} 
	 var tab1 = document.getElementsByName("count") ;
	 var uids = document.getElementsByName("unitid") ;
	 var uid=uids[r.rowIndex-1].value.split(",");
	 var bl=uid[2];
     tab1[r.rowIndex-1].value=count.value*bl;
     if(parseInt(tab1[r.rowIndex-1].value)!=tab1[r.rowIndex-1].value){
   	  alert("基本单位数量必须为整数！");
   	count.value="0";
   	tab1[r.rowIndex-1].value=0;
	js(count.parentNode.parentNode);
	return;
     }
     var orderno=document.getElementById("orderno");
     if(orderno!=null && orderno.value!=null && orderno.value!=""){
    	 var balances = document.getElementsByName("balance") ;
    	 if(tab1[r.rowIndex-1].value-balances[r.rowIndex-1].value>0){
    		 alert("入库数量不能超过订单剩余数量"+balances[r.rowIndex-1].value+"！");
    		 count.value="0";
    		 tab1[r.rowIndex-1].value=0; 
    	 }
     }
	js(count.parentNode.parentNode);
	 count.title = "基本数量:"+tab1[r.rowIndex-1].value;
}

/**
 * 验证浮点数
 */
function yzszss(price){
	if(isNaN(price.value)||price.value-0<0){
		 alert("金额请输入数字且大于0！");
		price.value="0";
		return;
	}
}

/**
 * 实付金额不能超过应付
 */
 function yforsf(sf){
	yzszss(sf);
   var samount=document.getElementById("samount");
   var spayamount=document.getElementById("spayamount");
   if(spayamount.value-samount.value>0){
	  alert("实付不能超过应付！");
	   spayamount.value=samount.value;
   }
}

/**
 * 根据单价数量计算金额（行号）
 */
function js(r){  
	 var tab = document.getElementsByName("inprice") ;
     var price=tab[r.rowIndex-1].value;
	 var tab1 = document.getElementsByName("ncount") ;
     var count=tab1[r.rowIndex-1].value;
	 var  tab2 = document.getElementsByName("money") ;
     if(!isNaN(price)&& !isNaN(count)){
    	 var m= price * count;
       	 tab2[r.rowIndex-1].value =m.toFixed(2) ;
     }
	zjs();
}

/**
 * 根据所有商品的金额，计算应收和实收
 */
function zjs(){
	var  tab2 = document.getElementsByName("money") ;
    var amount=0;
    for(var i=1;i<tab2.length;i++){
   		amount+=Number(tab2[i].value);
   	}
    var samount=document.getElementById("samount");
    samount.value=amount;
    pay();
   //  var spayamount=document.getElementById("spayamount");
   //  spayamount.value=amount; 
}

/**
 * 保存以前的验证
 */
function saveyz(){
	var tab = document.getElementsByName("pname") ;
	var tab1 = document.getElementsByName("money") ;
	if(tab.length==1){
		alert("商品不能为空，请选择商品！");
		return false;
	}
	 for(var i=1;i<tab.length;i++){
			   	if(tab[i]==null ||tab[i].value==""){
		   		alert("商品不能为空，请选择商品！");
		   		return false;
		   	} 
     }
	 for(var i=1;i<tab1.length;i++){
		   	if(Number(tab1[i].value)-0== 0 ||tab1[i].value==""){
		   		alert("请输入商品数量或单价，金额不能为0！");
		   		return false;
		   	}
     }
	 var samount=document.getElementById("samount");
	   var spayamount=document.getElementById("spayamount");
	   if(spayamount.value-samount.value>0){
		  alert("实付不能超过应付！");
		   spayamount.value=samount.value;
		   return false;
	   }
	   var storage=document.getElementById("storage");
	   if(storage!=null && storage.value==0){
		  alert("请选择仓库！");
		   return false;
	   }
	  if(window.confirm('您确定生成的采购入库单信息吗？')){
		 var tab = document.getElementsByName("inprice") ;    
		 var tab1 = document.getElementsByName("ncount") ;
		 var  tab2 = document.getElementsByName("money") ;
	     for(var i=1;i<tab.length;i++){
	    	 var price=tab[i].value;
	    	 var count=tab1[i].value;
	    	 tab2[i].value=price*count;
	     }  
	     var amount=0;
	     for(var i=1;i<tab2.length;i++){
	    	amount+=Number(tab2[i].value);
	     }
	     var samount=document.getElementById("samount");
	     samount.value=amount;
	    // pay();
      }else{
    	  return false;
      }
}

/**
 * ajax取得商品所有的单位信息（行号）
 */
function showUnits(rr){
	
	var r=rr.parentNode.parentNode;
	  var tab1 = document.getElementsByName("pid") ;
      var pid=tab1[r.rowIndex-1].value; 
		
	    if (pid == null || pid == '') {
	        alert("请选择商品！");
	        return false;
	     }
		Ext.Ajax.request({
			url : '/purchase/getUnitsItem.do', 
			params : {
				'productid' : pid
			},
			method : 'POST',
			success : function(response) {
			    var jsonResult = Ext.util.JSON.decode(response.responseText);
			        var  result=jsonResult.result ;
			       
			        var unit=result.split(":");
			     
			        var html="<select name=\'unitid\' onchange=\'unitchange(this)\' style=\'width: 50px\'>";
			        for(var i=0;i<unit.length-1;i++){
			        	
			        	var u1=unit[i].split(",");
			           	html=html+"<option value=\'"+unit[i]+"\' ";
			           	if(rr.value==u1[1]){
			           		html=html+" selected=\'selected\' ";
			           	}
			           	html=html+ " >"+u1[1]+"</option>";
			           	 }
			        rr.parentNode.innerHTML=html+"</select><input name=\'unitname\' type=\'hidden\'/>";
			        
			    }
		});
	
}

/**
 * 单位换算的时候计算基本数量（单位id）
 */
function unitchange(unitid){
 	var r=unitid.parentNode.parentNode;
 	var inprice = document.getElementsByName("inprice") ;
 	var ncount = document.getElementsByName("ncount") ;
 	var count = document.getElementsByName("count") ;
 	var uid=unitid.value.split(",");
 	var bl=uid[2];
 	inprice[r.rowIndex-1].value=uid[3];
 	var sl=count[r.rowIndex-1].value/bl;
 	ncount[r.rowIndex-1].value=sl.toFixed(2); 
 	js(unitid.parentNode.parentNode);
}

/**
 * 供应商改变后，删除商品
 */
function supplierEdit(){
	 var root = document.getElementById("tbody");
     var allRows = root.getElementsByTagName('tr');
     var row=allRows.length;
     for(var i = 2; i <row; i++){
    	 root.removeChild(allRows[2]);
      }
      var samount = document.getElementById("samount");
      samount.value = 0;
      pay();
}
/**
 * 付款类型
 */
function pay(){
	var paytype=document.getElementById("paytype").value;
	var samount=document.getElementById("samount").value;
	var spayamount=document.getElementById("spayamount");
	//预付款
	if(paytype==0){
		spayamount.value=0;
		spayamount.readOnly = false;
	}else{
		spayamount.readOnly = true;
		spayamount.value=samount;
	}
}