/**
 * 增加一行商品
 */
function addRow()
  {
	var outStorageId=document.getElementById("outStorage").value;
	var inStorageId=document.getElementById("inStorage").value;
	if(!outStorageId || !inStorageId){
		alert('请选择出货库/入货库');
		return;
	}else if(outStorageId == inStorageId){
		alert('出货库/入货库选择相同，请重新选择！');
		return;
	}
	
	//  onmousemove="this.setCapture();" onmouseout="this.releaseCapture();"
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		 cus=window.showModalDialog(URL_PREFIX+"/pages/amol/stock/trac/spselector.jsp?storageId="+outStorageId,null,"dialogWidth=800px;resizable: Yes;");
		 if(cus!=null){
			 	var pidTab = document.getElementsByName("pid");
			 	for(var i=0;i<pidTab.length;i++){
			 		if(pidTab[i].value != null && pidTab[i].value != ''){
			 			if(cus.pid == pidTab[i].value){
			 				alert('商品已经存在，不允许重复添加');
			 				return;
			 			}
			 		} 
			 	}
			    var root = document.getElementById("tbody");
			    var allRows = root.getElementsByTagName('tr');
			    var cRow = allRows[1].cloneNode(true);
				cRow.style.display="";
			    root.appendChild(cRow);
			    var rows = root.rows.length ;
			    var tab = document.getElementsByName("pname") ;
			    tab[rows-2].value=cus.name;
			    var tab1 = document.getElementsByName("pid");
			    tab1[rows-2].value=cus.pid;  
			    var tab2 = document.getElementsByName("code") ;
			    tab2[rows-2].value=cus.code;
			    var tab11 = document.getElementsByName("countAll") ;
			    tab11[rows-2].value = cus.unitPack;
			    tab11[rows-2].title = "基本数量:" + cus.count + cus.unit;
			    var tab112 = document.getElementsByName("basicCount") ;//保存基本数量
			    tab112[rows-2].value = cus.count;
			    var tab3 = document.getElementsByName("stardard") ;
			    tab3[rows-2].value=cus.stardard;
			    var tab4 = document.getElementsByName("unit") ;
			    tab4[rows-2].value=cus.unit;
			    var tab41 = document.getElementsByName("unitid") ;
			    tab41[rows-2].value=cus.unitid;  
		 }
	}else{//火狐
		window.open(URL_PREFIX+"/pages/amol/stock/trac/spselector.jsp?storageId="+outStorageId,"","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
		this.returnAction=function(cus){
			if(cus!=null){
				var pidTab = document.getElementsByName("pid");
			 	for(var i=0;i<pidTab.length;i++){
			 		if(pidTab[i].value != null && pidTab[i].value != ''){
			 			if(cus.pid == pidTab[i].value){
			 				alert('商品已经存在，不允许重复添加');
			 				return;
			 			}
			 		} 
			 	}
				var root = document.getElementById("tbody");
				var allRows = root.getElementsByTagName('tr');
				var cRow = allRows[1].cloneNode(true);
				cRow.style.display="";
				root.appendChild(cRow);
				var rows = root.rows.length ;
				var tab = document.getElementsByName("pname") ;
				tab[rows-2].value=cus.name;
				var tab1 = document.getElementsByName("pid") ;
				tab1[rows-2].value=cus.pid;  
				var tab2 = document.getElementsByName("code") ;
				tab2[rows-2].value=cus.code;
				var tab11 = document.getElementsByName("countAll") ;
				tab11[rows-2].value = cus.unitPack;
			    tab11[rows-2].title = "基本数量:" + cus.count + cus.unit;
			    var tab112 = document.getElementsByName("basicCount") ;//保存基本数量
			    tab112[rows-2].value = cus.count ;
				var tab3 = document.getElementsByName("stardard") ;
			    tab3[rows-2].value=cus.stardard;
			    var tab4 = document.getElementsByName("unit") ;
			    tab4[rows-2].value=cus.unit;
			    var tab41 = document.getElementsByName("unitid") ;
			    tab41[rows-2].value=cus.unitid;
			}
		};
  }
} 

/**
 * 当出/入仓库改变时，删除所有商品
 */
function removeAllRow(){
	var root = document.getElementById("tbody");
	var allRows = root.getElementsByTagName('tr');
	var row=allRows.length;
	for(var i = 2; i <row; i++){
		root.removeChild(allRows[2]);
	}			
	
}

/**
 * 删除一行商品
 */
function removeRow(r)
{
    var root = r.parentNode;
    var allRows = root.getElementsByTagName('tr');
    if(allRows.length > 3){
    	root.removeChild(r);
    }
    else{
    	alert('不能删最后一行！');
    }
}

/**
 * 重新选择一行商品
 */
function edit(r){
	var outStorageId=document.getElementById("outStorage").value;
	var inStorageId=document.getElementById("inStorage").value;
	if(!outStorageId || !inStorageId){
		alert('请选择出货库/入货库');
		return;
	}
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/stock/trac/spselector.jsp?storageId="+outStorageId,null,"dialogWidth=800px;resizable: Yes;");
		if(cus!=null){
			var pidTab = document.getElementsByName("pid");
		 	for(var i=0;i<pidTab.length;i++){
		 		if(pidTab[i].value != null && pidTab[i].value != ''){
		 			if(cus.pid == pidTab[i].value){
		 				alert('商品已经存在，不允许重复添加');
		 				return;
		 			}
		 		} 
		 	}
			var tab = document.getElementsByName("pname") ;
			tab[r.rowIndex-1].value=cus.name;
			var tab1 = document.getElementsByName("pid") ;
			tab1[r.rowIndex-1].value=cus.pid;
			var tab2 = document.getElementsByName("code") ;
			tab2[r.rowIndex-1].value=cus.code;
			var tab11 = document.getElementsByName("countAll") ;
			tab11[r.rowIndex-1].value = cus.unitPack;
		    tab11[r.rowIndex-1].title = "基本数量:" + cus.count + cus.unit;
		    var tab112 = document.getElementsByName("basicCount") ;//保存基本数量
		    tab112[r.rowIndex-1].value = cus.count ;
			var tab12 = document.getElementsByName("outCount") ;
			tab12[r.rowIndex-1].value='';
			var tab22 = document.getElementsByName("sremark") ;
			tab22[r.rowIndex-1].value='';
			var tab3 = document.getElementsByName("stardard") ;
		    tab3[r.rowIndex-1].value=cus.stardard;
		    var tab4 = document.getElementsByName("unit") ;
		    tab4[r.rowIndex-1].value=cus.unit;
		    var tab41 = document.getElementsByName("unitid") ;
		    tab41[r.rowIndex-1].value=cus.unitid;
		}   
	}else{
		window.open(URL_PREFIX+"/pages/amol/stock/trac/spselector.jsp?storageId="+outStorageId,"","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	     this.returnAction=function(cus){
		    if(cus!=null){
		    	var pidTab = document.getElementsByName("pid");
			 	for(var i=0;i<pidTab.length;i++){
			 		if(pidTab[i].value != null && pidTab[i].value != ''){
			 			if(cus.pid == pidTab[i].value){
			 				alert('商品已经存在，不允许重复添加');
			 				return;
			 			}
			 		} 
			 	}
		    	var tab = document.getElementsByName("pname") ;
				tab[r.rowIndex-1].value=cus.name;
				var tab1 = document.getElementsByName("pid") ;
				tab1[r.rowIndex-1].value=cus.pid;  
				var tab2 = document.getElementsByName("code") ;
				tab2[r.rowIndex-1].value=cus.code;
				var tab11 = document.getElementsByName("countAll") ;
				tab11[r.rowIndex-1].value = cus.unitPack;
			    tab11[r.rowIndex-1].title = "基本数量:" + cus.count + cus.unit;
			    var tab112 = document.getElementsByName("basicCount") ;//保存基本数量
			    tab112[r.rowIndex-1].value = cus.count;
				var tab3 = document.getElementsByName("stardard") ;
			    tab3[r.rowIndex-1].value=cus.stardard;
			    var tab4 = document.getElementsByName("unit") ;
			    tab4[r.rowIndex-1].value=cus.unit;
			    var tab41 = document.getElementsByName("unitid") ;
			    tab41[r.rowIndex-1].value=cus.unitid;
		    }
	     };
	}
}

/**
 * 验证【调拨】数量输入的必须是数字，不能是字母。并且输入数量必须小于实际库存数量
 */
function yzsz(outCount){
	if(isNaN(outCount.value)|| outCount.value - 0 < 0){
		alert('请输入数字并且大于0');
		outCount.value = 0;
		outCount.focus();
		return;
	}
	if(outCount.value == 'NaN'){
		outCount.value = 0;
	}
	var tab = document.getElementsByName("countAll") ;
	var count = tab[outCount.parentNode.parentNode.rowIndex-1].value;
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
	 var counts = document.getElementsByName("counts") ;
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
function yzzs(outCount){ 
	var tab1 = document.getElementsByName("counts") ;
	var r=outCount.parentNode.parentNode;
	if(isNaN(outCount.value)||outCount.value-0<0){
		alert("请输入数字并且大于0！");
		outCount.value="0";
		tab1[r.rowIndex-1].value=0;
		return;
	} 
	var tab1 = document.getElementsByName("counts") ;
	var uids = document.getElementsByName("unitid") ;
	var uid=uids[r.rowIndex-1].value.split(",");
	var bl=uid[2];
    tab1[r.rowIndex-1].value=outCount.value*bl;
    if(parseInt(tab1[r.rowIndex-1].value)!=tab1[r.rowIndex-1].value){
	   	alert("基本单位数量必须为整数！");
	   	outCount.value="0";
	   	tab1[r.rowIndex-1].value=0;
		return;
    }
	//var tab = document.getElementsByName("countAll") ;
 	//var countAll = tab[outCount.parentNode.parentNode.rowIndex-1].value;
 	var tab = document.getElementsByName("basicCount") ;//保存基本数量
 	var count = tab[r.rowIndex-1].value;
 	if(parseInt(tab1[r.rowIndex-1].value) > parseInt(count)){
 		alert('调拨数量必须小于实际库存数量');
 		outCount.value = 0;
 		tab1[r.rowIndex-1].value = 0;
 		//outCount.focus();
 	}
    // 设置数量title
 	outCount.title = "基本数量:"+tab1[r.rowIndex-1].value;
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
		     
		        var html="<select name=\'unitid\' onchange=\'unitchange(this)\' style=\'width: 60px\'>";
		        for(var i=0;i<unit.length-1;i++){
		        	
		        	var u1=unit[i].split(",");
		           	html=html+"<option value=\'"+unit[i]+"\' ";
		           	if(rr.value==u1[1]){
		           		html=html+" selected=\'selected\' ";
		           	}
		           	html=html+ " >"+u1[1]+"</option>";
		           	 }
		        rr.parentNode.innerHTML=html+"</select><input name=\'unit\' type=\'hidden\'/>";
		        
		    }
	});
}

/**
 * 单位换算的时候计算基本数量（单位id）
 */
function unitchange(unitid){
 	var r=unitid.parentNode.parentNode;
 	var outCount = document.getElementsByName("outCount") ;
 	var countAll = document.getElementsByName("countAll") ;
 	var count = document.getElementsByName("counts") ;
 	var uid=unitid.value.split(",");
 	var bl=uid[2];
 	var sl=count[r.rowIndex-1].value/bl;
 	outCount[r.rowIndex-1].value=sl.toFixed(2);
}

/**
 * 点击保存提交以前的验证
 */
function saveyz(){
	var outStorageId=document.getElementById("outStorage").value;
	var inStorageId=document.getElementById("inStorage").value;
	if(!outStorageId || !inStorageId){
		alert('请选择出货库/入货库');
		return;
	}
	
	var tab = document.getElementsByName("pname") ;
	var tab1 = document.getElementsByName("outCount") ;
	
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
		   	if(tab1[i].value=="" || tab1[i].value-0==0 ){
		   		alert('请输入调拨商品数量或数量不能为0！');
		   		return false;
		   	}
       }
	
	if (confirm('您确定要提交本次库存【调拨】信息吗？')){
		var form=document.getElementById("save");
        form.submit();
	}
}