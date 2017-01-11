/**
 * pda销售商品页面js
 */

/**
 * 增加一行商品
 */
function addRow()
  {
	var tbody = document.getElementById("tbody");
	var tr = tbody.insertRow(tbody.rows.length);
	var td1 = tr.insertCell(0);
	var td2 = tr.insertCell(1);
	var td3 = tr.insertCell(2);
	var td4 = tr.insertCell(3);
	var td5 = tr.insertCell(4);
	td1.align = "left";
	td1.innerHTML = "<input name='productCode' type='text' size='10' />";
	td2.innerHTML = "<input name='outprice' type='text' size='4' maxLength='5' value='0' />";  
	td3.innerHTML = "<input name='counts' type='text' size='4' maxLength='9' value='0' onkeyup='yzzs(this)' />";
	td4.innerHTML = "<input name='money' type='text' size='5' value='0' readonly='readonly' style='color:#808080;'/>";
	td5.innerHTML = "<a href='#' onclick='removeRow(this.parentNode.parentNode);'>删</a>|<a href='#' onclick='scanCode(this.parentNode.parentNode);'>扫</a><input type='hidden' name='codes' id='codes'/>";
  } 

/**
 * 删除一行商品
 */
function removeRow(r)
{
    var root = r.parentNode;
    var allRows = root.getElementsByTagName('tr');
    if(allRows.length > 2){
    	root.removeChild(r);
        zjs();
    }
    else{
    	alert("不能删最后一行！");
  }
}

/**
 * 验证必须为整数
 */
function yzzs(count){ 
  if(isNaN(count.value)){
	alert("请输入数字！");
	count.value="0";
	return;
  }
  js(count.parentNode.parentNode);
}

/**
 * 及时一行中的金额
 */
function js(r){  
 
 //商品单价
 var outpriceTd = document.getElementsByName("outprice") ;
 var price = outpriceTd[r.rowIndex-1].value;

 //商品数量
 var countsTd = document.getElementsByName("counts") ;
 var count = countsTd[r.rowIndex-1].value;

 //当前商品总金额
 var moneyTd = document.getElementsByName("money") ;
 if(!isNaN(price) && !isNaN(count)){
	 var money = price * count;
	 moneyTd[r.rowIndex-1].value = money.toFixed(2) ;
 }
 
 //所有商品总金额
 zjs();   	 
}

/**
 * 计算所有商品总金额
 */
function zjs(){
 var moneyTd = document.getElementsByName("money") ;
 var amount=0;
 for(var i = 0; i < moneyTd.length; i++){
   amount += Number(moneyTd[i].value);
 }
 
 //应收金额
 var samount = document.getElementById("samount");
 samount.value = amount;
 
 //刷卡金额
 document.getElementById("cardamountId").value = amount;
}

/**
 * 扫码
 */
function scanCode(r){
	var countObject = document.getElementsByName("counts");
	var count = countObject[r.rowIndex-1].value;
	if(0 != count){
		var cus = window.showModalDialog(URL_PREFIX+"/pages/amol/pda/scanCode/scanCode.jsp?count="+count,null,"dialogWidth=55","dialogHeight=300px");
		if(cus!=null){
	    	var codeObject = document.getElementsByName("codes") ;
	    	codeObject[r.rowIndex-1].value = cus.str;
		}
	}else{
		alert("请输入商品数量！");
	}
}

/**
 * 保存出库单前验证数据的合法性
 */
function saveValidate(){
	var cardNo = document.getElementById("cardNo").value;
	var passwordId = document.getElementById("passwordId").value;
	var cardamountId = document.getElementById("cardamountId").value;
	var samount = document.getElementById("samount").value;

	if(cardNo == "" || cardNo.length != 32){
		alert("请扫描32位的代币卡号");
		return false;
	}
	
	if(passwordId == "" || passwordId.length != 6){
		alert("请输入6位的代币卡密码");
		return false;
	}
	
	if(Number(cardamountId) == 0){
		alert("刷卡金额不能为0");
		return false;
	}
	
	if(Number(samount) == Number(0)){
		alert("应收金额不能为0");
		return false;
	}
	
	return true;
}