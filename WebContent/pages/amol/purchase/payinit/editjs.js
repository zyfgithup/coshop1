
/**
 * 选择供应商
 */
function supper(){
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
var cus=window.showModalDialog(URL_PREFIX+"/pages/amol/base/supplier/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
 if(cus!=null){
	 var tab1 = document.getElementsByName("sid") ;
	   

                 var root = document.getElementById("tbody");
			    var allRows = root.getElementsByTagName('tr');
			    var cRow = allRows[1].cloneNode(true);
				cRow.style.display="";
			    root.appendChild(cRow);
			    var rows = root.rows.length ;
			    var tab = document.getElementsByName("sname") ;
			    tab[rows-2].value=cus.name;
			    tab1[rows-2].value=cus.id;  
			    var tab2 = document.getElementsByName("address") ;
			    tab2[rows-2].value=cus.address;
			    var tab3 = document.getElementsByName("phone") ;
			    tab3[rows-2].value=cus.phone;
			    
  }
	}else{//火狐
		window.open(URL_PREFIX+"/pages/amol/base/supplier/selector.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	     this.returnAction=function(cus){
		    if(cus!=null){
		    	
		    	var root = document.getElementById("tbody");
			    var allRows = root.getElementsByTagName('tr');
			    var cRow = allRows[1].cloneNode(true);
				cRow.style.display="";
			    root.appendChild(cRow);
			    var rows = root.rows.length ;
			    var tab = document.getElementsByName("sname") ;
			    tab[rows-2].value=cus.name;
			    tab1[rows-2].value=cus.id;   
			    var tab2 = document.getElementsByName("address") ;
			    tab2[rows-2].value=cus.address;
			    var tab3 = document.getElementsByName("phone") ;
			    tab3[rows-2].value=cus.phone;
		    }
		    }
	     }
		    
}
//导出模板
function init(){
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
var cus=window.showModalDialog(URL_PREFIX+"/base/supplier/initindex.do",null,"dialogWidth=800px;resizable: Yes;");
 
	}else{//火狐
		window.open(URL_PREFIX+"/base/supplier/initindex.do","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	     this.returnAction=function(cus){
		    
		    }
	     }
		    
}
//验证现金为数字
function yzszss(price){
	if(isNaN(price.value)){
		alert("金额请输入数字！");
		price.value="0";
	}
}
/**
 * 删除一行
 */
function removeRow(r,id)
{
	
  
   var tab = document.getElementsByName("sname") ;
	var pName = tab[r.rowIndex-1].value;
	
	if(confirm('确定要删除【'+ pName +'】供应商吗？')){
	  if(id-0>0){
			delStock(r,id);
			}
		var root = r.parentNode;
		root.removeChild(r);		
	}

   
   
   
   
}
/**
 * 保存前的验证
 */
function saveyz(){
	//var tab = document.getElementsByName("sid") ;
	//if(tab.length==1){
	//	alert("供应商不能为空，请选择供应商！");
	//	return false;
	//}
	
    if(confirm('您确定保存应收初始化信息吗？')){
    	return true;
	  }else {
		return false;
	  }
}

/**
 * 删除供应商
 * @param r
 */
function delStock(r,id){

	$.ajax({
		type : "POST",
		async:false,
		url : URL_PREFIX+"/purchase/payinit/delsup.do",
		data : {
			"model.id" : id
		},
		dataType : "json",
		success : function(result) {
			//如果执行成功
			if (result.success) {
				return true;
			}else{
				alert(result.msg);
				return false;
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
			return false;
		}
	});
}