
/**
 * 导出模板
 */
function initTemplate(){
	if (navigator.userAgent.indexOf("MSIE") > 0) {// IE判断
		var cus = window.showModalDialog(URL_PREFIX + "/base/product/stockInitTemplate.do", null,
					"dialogWidth=800px;resizable: Yes;");
	} else {// 火狐
		window.open(URL_PREFIX + "/base/product/stockInitTemplate.do",
					"",
					"width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
		this.returnAction = function(cus) {};
	}
}

/**
 * 增加一行商品
 */
function addRow()
  {
	//var storageId=document.getElementById("storageId").value;
	//获取选中项
	var ddlSelected = $('#storageId option:selected');
	//选中项的文本
	var storageText =ddlSelected.text();
	//选中项的值
	var storageId =ddlSelected.val();
	if(!storageId || storageId == ""){
		alert('请选择仓库！');
		return;
	}
	
	//  onmousemove="this.setCapture();" onmouseout="this.releaseCapture();"
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		 cus=window.showModalDialog(URL_PREFIX+"/pages/amol/base/product/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
		 if(cus!=null){
			 	var pidTab = document.getElementsByName("pid");
			 	var tabStatus = document.getElementsByName("status"); // 得到所有的状态
			 	for(var i=0;i<pidTab.length;i++){
			 		//判断商品ID是否为空，状态为0时不允许重复商品
			 		if(pidTab[i].value != null && pidTab[i].value != ''){
			 			if(cus.id == pidTab[i].value && tabStatus[i].value == 0){
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
			    var tab0 = document.getElementsByName("storageName") ;
			    tab0[rows-2].value=storageText;
			    var tab = document.getElementsByName("pname") ;
			    tab[rows-2].value=cus.name;
			    var tab1 = document.getElementsByName("pid");
			    tab1[rows-2].value=cus.id;  
			    var tab2 = document.getElementsByName("code") ;
			    tab2[rows-2].value=cus.code;
			    var tab3 = document.getElementsByName("stardard") ;
			    tab3[rows-2].value=cus.stardard;
			    var tab4 = document.getElementsByName("suppliers") ;
			    tab4[rows-2].value=cus.supplier;
			    var tab5 = document.getElementsByName("unit") ;
			    tab5[rows-2].value=cus.unitname;
			    var tab51 = document.getElementsByName("unitid") ;
			    tab51[rows-2].value=cus.unitid;  
			    var tab6 = document.getElementsByName("prices") ;
			    tab6[rows-2].value=cus.inprice;
		 }
	}else{//火狐
		window.open(URL_PREFIX+"/pages/amol/base/product/selector.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
		this.returnAction=function(cus){
			if(cus!=null){
				var pidTab = document.getElementsByName("pid");
				var tabStatus = document.getElementsByName("status"); // 得到所有的状态
			 	for(var i=0;i<pidTab.length;i++){
			 		if(pidTab[i].value != null && pidTab[i].value != ''){
			 			//判断商品ID是否为空，状态为0时不允许重复商品
			 			if(cus.id == pidTab[i].value && tabStatus[i].value == 0){
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
			    var tab0 = document.getElementsByName("storageName") ;
			    tab0[rows-2].value=storageText;
				var tab = document.getElementsByName("pname") ;
				tab[rows-2].value=cus.name;
				var tab1 = document.getElementsByName("pid") ;
				tab1[rows-2].value=cus.id;  
				var tab2 = document.getElementsByName("code") ;
				tab2[rows-2].value=cus.code;
				var tab3 = document.getElementsByName("stardard") ;
				tab3[rows-2].value=cus.stardard;
				var tab4 = document.getElementsByName("suppliers") ;
			    tab4[rows-2].value=cus.supplier;
			    var tab5 = document.getElementsByName("unit") ;
			    tab5[rows-2].value=cus.unitname;
			    var tab51 = document.getElementsByName("unitid") ;
			    tab51[rows-2].value=cus.unitid;  
			    var tab6 = document.getElementsByName("prices") ;
			    tab6[rows-2].value=cus.inprice;
			}
		};
  }
} 

/**
 * 仓库改变时，删除所有商品
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
 * 当仓库改变时查询商品
 */
function queryProByStorage(){
	//获取选中项
	var ddlSelected = $('#storageId option:selected');
	//选中项的文本
	//var ddlSelectedText=ddlSelected.text();
	//选中项的值
	var ddlSelectedVal=ddlSelected.val();
//	if(ddlSelectedVal == ''){
//		removeAllRow();
//		alert('请选择仓库');
//		return;
//	}
	var form=document.getElementById("query");
	form.action='edit.do';
    form.submit();
}

/**
 * 查询全部期初信息
 */
function queryProAll(){
	$("#storageId").find("option[value='']").attr("selected", "selected");
	var ddlSelected = $('#storageId option:selected');
	var form=document.getElementById("query");
	form.action='edit.do';
    form.submit();
}

/**
 * 删除一行商品
 */
function removeRow(r)
{
	var tab = document.getElementsByName("pname") ;
	var pName = tab[r.rowIndex-1].value;
	var temp = true;
	if(confirm('确定要删除【'+ pName +'】商品吗？')){
	    var tabStatus = document.getElementsByName("status") ; // 状态
	    var tabSiId = document.getElementsByName("siId") ; // 期初库存ID
		if(tabStatus[r.rowIndex-1].value == 0 && tabSiId[r.rowIndex-1].value != '' ){
			temp = delStock(r);
		}
		if(temp){
			var root = r.parentNode;
		    //var allRows = root.getElementsByTagName('tr');
			root.removeChild(r);			
		}
	}
}
function test1(){
	return true;
}
/**
 * 删除库存中商品
 * @param r
 */
function delStock(r){
	//商品ID
	var tab1 = document.getElementsByName("pid") ;
	var pId = tab1[r.rowIndex-1].value;
	//仓库ID
	var tab2 = document.getElementsByName("sId") ;
	var storageId = tab2[r.rowIndex-1].value;
	//期初库存ID
	var tabSiId = document.getElementsByName("siId") ; 
	var siId = tabSiId[r.rowIndex-1].value;
	
	var suc = true;
	$.ajax({
		type : "POST",
		async:false,
		url : URL_PREFIX+"/stock/init/delStockInitAndStock.do",
		data : {
			"model.storage.id" : storageId,
			"model.products.id" : pId,
			"model.id" : siId
		},
		dataType : "json",
		success : function(result) {
			//如果执行成功
			if (result.success) {
				suc = true;
			}else{
				alert(result.msg);
				suc = false;
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			Ext.MessageBox.show({
				title : '提示',
				minWidth : 220,
				msg : XMLHttpRequest + "@@" + textStatus + "@@@"
						+ errorThrown,//"发生异常，请与管理员联系！",
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
			suc = false;
		}
	});
	return suc;
}

/**
 * 重新选择一行商品
 */
function edit(r){
	//获取选中项
	var ddlSelected = $('#storageId option:selected');
	//选中项的文本
	var storageText =ddlSelected.text();
	//选中项的值
	var storageId =ddlSelected.val();
	if(!storageId || storageId == ""){
		alert('请选择仓库！');
		return;
	}
	var tabStatus = document.getElementsByName("status") ;
	if(tabStatus[r.rowIndex-1].value == 0){
		alert('该商品库存中已经存在不允许重新选择(可以添加新商品或删除该商品)');
		return;
	}
	alert(111);
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/base/product/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
		if(cus!=null){
			var pidTab = document.getElementsByName("pid");
			alert(111); 
		 	for(var i=0;i<pidTab.length;i++){
		 		alert(tabStatus[i].value);
		 		if(pidTab[i].value != null && pidTab[i].value != ''){
		 			if(cus.id == pidTab[i].value && tabStatus[i].value == 0){
		 				alert('商品已经存在，不允许重复添加');
		 				return;
		 			}
		 		} 
		 	}
		    var tab0 = document.getElementsByName("storageName") ;
		    tab0[r.rowIndex-1].value=storageText;
			var tab = document.getElementsByName("pname") ;
			tab[r.rowIndex-1].value=cus.name;
			var tab1 = document.getElementsByName("pid") ;
			tab1[r.rowIndex-1].value=cus.id;
			var tab2 = document.getElementsByName("code") ;
			tab2[r.rowIndex-1].value=cus.code;
			var tab3 = document.getElementsByName("stardard") ;
			tab3[r.rowIndex-1].value=cus.stardard;		
			var tab4 = document.getElementsByName("suppliers") ;
		    tab4[r.rowIndex-1].value=cus.supplier;
		    var tab5 = document.getElementsByName("unit") ;
		    tab5[r.rowIndex-1].value=cus.unitname;
		    var tab51 = document.getElementsByName("unitid") ;
		    tab51[r.rowIndex-1].value=cus.unitid;  
		    var tab6 = document.getElementsByName("prices") ;
		    tab6[r.rowIndex-1].value=cus.inprice;
		}   
	}else{
		window.open(URL_PREFIX+"/pages/amol/base/product/selector.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	     this.returnAction=function(cus){
		    if(cus!=null){
		    	var pidTab = document.getElementsByName("pid");
		    	var suppliers = document.getElementsByName("suppliers") ;
			 	for(var i=0;i<pidTab.length;i++){
			 		if(pidTab[i].value != null && pidTab[i].value != ''){
			 			if(cus.id == pidTab[i].value && cus.supplier == suppliers[i]){
			 				alert('商品已经存在，不允许重复添加');
			 				return;
			 			}
			 		} 
			 	}
			 	var tab0 = document.getElementsByName("storageName") ;
				tab0[r.rowIndex-1].value=storageText;
		    	var tab = document.getElementsByName("pname") ;
				tab[r.rowIndex-1].value=cus.name;
				var tab1 = document.getElementsByName("pid") ;
				tab1[r.rowIndex-1].value=cus.id;  
				var tab2 = document.getElementsByName("code") ;
				tab2[r.rowIndex-1].value=cus.code;
				var tab3 = document.getElementsByName("stardard") ;
				tab3[r.rowIndex-1].value=cus.stardard;			
				var tab4 = document.getElementsByName("suppliers") ;
			    tab4[r.rowIndex-1].value=cus.supplier;
			    var tab5 = document.getElementsByName("unit") ;
			    tab5[r.rowIndex-1].value=cus.unitname;
			    var tab51 = document.getElementsByName("unitid") ;
			    tab51[r.rowIndex-1].value=cus.unitid;  
			    var tab6 = document.getElementsByName("prices") ;
			    tab6[r.rowIndex-1].value=cus.inprice;
		    }
	     };
	}
}

/**
 * 验证数量输入的必须是数字，不能是字母。并且输入数量必须小于实际库存数量
 */
function yzsz(counts){
	/**if(isNaN(counts.value)|| counts.value - 0 < 0){
		alert('请输入数字并且大于0');
		counts.value = 0;
		counts.focus();
		return;
	}
	if(parseInt(counts.value) < 0){
		alert('库存数量不允许为负');
		counts.value = 0;
	}*/
	var r=counts.parentNode.parentNode;
	var tab1 = document.getElementsByName("counts");
	if(counts.name == "counts" && parseInt(tab1[r.rowIndex-1].value)!=tab1[r.rowIndex-1].value){
		alert("基本单位数量必须为整数！");
		counts.value="0";
		tab1[r.rowIndex-1].value=0;
	 }
	js(counts.parentNode.parentNode);
}
/**
 * 计算基础数量和金额
 * @param r 
 */
function js(r){
	var tab = document.getElementsByName("prices") ;
	var price = tab[r.rowIndex-1].value;
	var tab1 = document.getElementsByName("ncounts") ;
	var count = tab1[r.rowIndex-1].value;
	var tab2 = document.getElementsByName("amounts") ;
	if(!isNaN(price) && !isNaN(count)){
		var m= price * count;
		tab2[r.rowIndex-1].value =m.toFixed(2) ;
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
 	var inprice = document.getElementsByName("prices"); //单价
 	var ncount = document.getElementsByName("ncounts") ;
 	var count = document.getElementsByName("counts") ;
 	var uid=unitid.value.split(",");
 	var bl=uid[2];
 	inprice[r.rowIndex-1].value=uid[3];
 	var sl=count[r.rowIndex-1].value/bl;
 	ncount[r.rowIndex-1].value=sl.toFixed(2); 
 	js(unitid.parentNode.parentNode);
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
function yzzs(count){ 
	 var tab1 = document.getElementsByName("counts") ;
	 var r=count.parentNode.parentNode;
	/*验证不小于0
	 * if(isNaN(count.value)||count.value-0<0){
		alert("请输入数字并且大于0！");
		count.value="0";
		tab1[r.rowIndex-1].value=0;
		js(count.parentNode.parentNode);
		return;
	} */
	 var tab1 = document.getElementsByName("counts") ;
	 var uids = document.getElementsByName("unitid") ;
	 var uid=uids[r.rowIndex-1].value.split(",");
	 var bl=uid[2];
     tab1[r.rowIndex-1].value=count.value*bl;
     /*
     if(parseInt(tab1[r.rowIndex-1].value)!=tab1[r.rowIndex-1].value){
	   	alert("基本单位数量必须为整数！");
	   	count.value="0";
	   	tab1[r.rowIndex-1].value=0;
		js(count.parentNode.parentNode);
		return;
     }*/
     // 设置数量title
     count.title = "基本数量:"+tab1[r.rowIndex-1].value;
     js(count.parentNode.parentNode);
}

/**
 * 点击保存提交以前的验证
 */
function saveyz(){
	var storageId=document.getElementById("storageId").value;
	if(!storageId || storageId == ""){
		alert('请选择仓库！');
		return;
	}
	
	var tab = document.getElementsByName("pname") ;
	var tab1 = document.getElementsByName("counts") ;
	var tab2 = document.getElementsByName("prices") ;
	var tab3 = document.getElementsByName("amounts") ;
	
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
		if(tab1[i].value=="" || tab1[i].value == '0' ){
			alert('请输入库存数量,商品库存不能为空或不为0！');
			return false;
		}
	}
	
	for(var i = 1; i < tab2.length; i++){
		if(tab2[i].value=="" || tab2[i].value == '0'){
			alert('输入库存商品单价，单价不能为空或不为0！');
			return false;
		}
	}
	
	for(var i = 1; i < tab3.length; i++){
		if(tab3[i].value=="" || tab3[i].value=="0"){
			alert('库存商品金额不能为空或不为0！');
			return false;
		}
	}
	
	if (confirm('您确定要提交本次库存期初信息？')){
		var form=document.getElementById("save");
        form.submit();
	}
}