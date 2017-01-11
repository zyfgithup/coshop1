
/**
 * 选择客户
 */
function customer(){
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/base/customer/selector.jsp",null,"dialogWidth=55","dialogHeight=300px");
		if(cus!=null){
			    var tab1 = document.getElementsByName("customerId") ;
			    /*for(var i=1;i<tab1.length;i++){
			    	if(tab1[i].value==cus.id){
			    		alert('此客户已经存在，不能重复添加！');
			    		return ;
			    	}
			    }*/
                var root = document.getElementById("tbody");
			    var allRows = root.getElementsByTagName('tr');
			    var cRow = allRows[1].cloneNode(true);
				cRow.style.display="";
			    root.appendChild(cRow);
			    var rows = root.rows.length ;
			    var tab = document.getElementsByName("customerName") ;
			    tab[rows-2].value=cus.name;
			    tab1[rows-2].value=cus.id;  

			    var idCard = document.getElementsByName("idCard") ;
			    idCard[rows-2].value=cus.idCard;
			    
			    var tab = document.getElementsByName("mobile") ;
			    tab[rows-2].value=cus.mobile;
			    
			    var area = document.getElementsByName("area") ;
			    area[rows-2].value=cus.area;
		 }
	}else{//火狐
		 window.open(URL_PREFIX+"/pages/amol/base/customer/selector.jsp","","width=900px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	     this.returnAction=function(cus){
	    	 if(cus!=null){
		    	var tab1 = document.getElementsByName("customerId") ;
			    /*for(var i=1;i<tab1.length;i++){
			    	if(tab1[i].value==cus.id){
			    		alert('此客户已经存在，不能重复添加！');
			    		return ;
			    	}
			    }*/
		    	var root = document.getElementById("tbody");
			    var allRows = root.getElementsByTagName('tr');
			    var cRow = allRows[1].cloneNode(true);
				cRow.style.display="";
			    root.appendChild(cRow);
			    var rows = root.rows.length ;
			    var tab = document.getElementsByName("customerName") ;
			    tab[rows-2].value=cus.name;
			    tab1[rows-2].value=cus.id;   
		    }
	     };
	}
}
/**
 * 验证现金为数字
 */
function yzszss(price){
	if(isNaN(price.value)){
		alert("金额请输入数字！");
		price.value="0";
	}
}
/**
 * 保存前的验证
 */
function saveyz(){
	var tab = document.getElementsByName("customerId") ;
	if(tab.length==1){
		alert("客户不能为空，请选择客户！");
		return false;
	}
	var tab1 = document.getElementsByName("amount") ;
    for(var i=1;i<tab1.length;i++){
    	if(tab1[i].value-0<=0){
    		alert('金额必须大于0！');
    		return false;
    	}
    }
    if(confirm('您确定保存应收初始化信息吗？')){
	}else {
		return false;
	}
    return true;
}

//导出模板
function init(){
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/base/customer/initSelector.jsp",null,"dialogWidth=800px;resizable: Yes;");
	}else{//火狐
		window.open(URL_PREFIX+"/pages/amol/base/customer/initSelector.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	}
}