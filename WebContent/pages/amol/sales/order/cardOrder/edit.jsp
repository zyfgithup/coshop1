<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<title>销售订单【代币卡】</title>
<script type="text/javascript" >

/**
 * 增加一行商品
 */
function addRow()
  {
    var cus=window.showModalDialog("${ctx}/pages/amol/base/product/pselector.jsp",null,"dialogWidth=800px;resizable: Yes;");
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
    var tab12 = document.getElementsByName("outprice") ;
    tab12[rows-2].value=cus.outprice;
    var tab13 = document.getElementsByName("code") ;
    tab13[rows-2].value=cus.code;
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
    	root.removeChild(r);
        zjs();
    }
    else{
    	alert("不能删最后一行！");
  }
}

/**
 * 重新选择一行商品
 */
function edit(r){
 var cus=window.showModalDialog("${ctx}/pages/amol/base/product/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
 if(cus!=null){
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
  if(isNaN(count.value)){
	alert("请输入数字！");
	count.value="0";
	return;
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
}

/**
 * 点击保存以前的验证
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
		   	if(tab1[i].value=="0" ||tab1[i].value=="" || Number(tab1[i].value) == 0){
		   		alert("请输入商品数量或单价，金额不能为0！");
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
     
     var name = document.getElementById("name").value ;
     

     
     if(confirm('您确定生成销售订单吗？')){
     }else{
    	 return false;
     }
}

/**
 * 点击单位后加载所有的单位信息
 */
function showUnits(rr){
	
	var r = rr.parentNode.parentNode;
	  var tab1 = document.getElementsByName("pid") ;
      var pid = tab1[r.rowIndex-1].value; 
		
	    if (pid == null || pid == '') {
	        alert("请选择商品！");
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
 * 通过代币卡号确定客户身份
 */
function validateCard(){
	var customeridObject = document.getElementById("customerid");//客户id
	var nameObject = document.getElementById("name");//客户姓名
	var orderCardNoObject = document.getElementById("orderCardNoId");//代币卡号Object
	var orderCardNo = orderCardNoObject.value;//代币卡卡号
	var userId = document.getElementById("userId").value;//经销商id
	/*
	if(orderCardNo.length != 32){
		initCard(customeridObject,nameObject);
		alert("请扫描22位的代币卡");
		return false;
	}*/
	$.ajax({
		url: '${ctx}/card/replace/getCard.do',
		type: 'post',
		dataType: 'json',
		data: {cardNo : orderCardNo,userId : userId},//orderCardNo代币卡号             经销商id 
		success: function(rows, textStatus){
			if(rows == null){
				initCard(customeridObject,nameObject,orderCardNoObject);
				alert("代币卡不存在");
			}else{
				for (var i = 0; i < rows.length; i ++) {
				   	var row = rows[i];
				   	if(!row.available){
				   		initCard(customeridObject,nameObject,orderCardNoObject);
						alert("代币卡状态不正常或持卡客户被禁用");
						return false;
					}
					if(!row.jxs){
						initCard(customeridObject,nameObject,orderCardNoObject);
						alert("该代币卡不属于本经销商");
						return false;
					}
				   	if(row.available && row.jxs){
				   		customeridObject.value=row.customerId;//客户id
				   		nameObject.value=row.customerName;//客户姓名
					}
			    }
			}
		}
	});
}
/**
 * 代币卡信息初始化
 */
function initCard(customeridObject,nameObject,orderCardNoObject){
	customeridObject.value = "";
	nameObject.value = "";
	orderCardNoObject.value = "";
	orderCardNoObject.focus();
}
</script>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">销售订单信息【代币卡】</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save" id="save" validate="true" method="POST">
	<s:hidden name="model.id"  /> 
	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>销售订单【代币卡】</legend>
	<table width="100%" align="left" >
		<tr>
			<td align="right" >
				编 &nbsp;&nbsp;&nbsp;号：
			</td>
			<td width="200" class="simple" align="left">
				<s:hidden id="userId" name="model.user.id"/>
			    <s:textfield  name="model.salesNo" maxLength="20" 
					cssStyle="width:155px;color:#808080;" cssClass="required" readonly="true"/>
           		<!--  font color="red">*</font>-->
			</td>
			<td  align="right" >
			         日&nbsp;&nbsp;&nbsp;&nbsp;期：</td>
			<td width="213" class="simple" align="left">
				<s:textfield  name="model.createTime" maxLength="20" cssStyle="width:155px;" cssClass="required" 
				onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" readonly="true"/>
           		<font color="red">*</font>
			</td>
		</tr>
		<tr>
			<td align="right">应&nbsp;&nbsp;&nbsp;&nbsp;收：</td>
			<td align="left" colspan="1">
				<s:textfield  name="model.samount" id="samount" maxLength="20" size="25"
				  readonly="true" cssStyle="width:155px;color:#808080;" cssClass="required"/>
				元
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
			<td align="right" >
				客&nbsp;&nbsp;&nbsp;&nbsp;户：
			</td>
			<td class="simple" align="left">
			   <s:hidden id="customerid" name="model.customer.id"/>
			   <s:textfield id="name" name="model.customer.name" readonly="true" maxLength="20" style="width: 155px;" cssClass="required"/>
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
			<td align="right" >
				代币卡号：
			</td>
			<td class="simple" align="left" colspan="3">
			   <s:textfield id="orderCardNoId" name="model.orderCardNo" maxLength="22" size="30"  cssClass="required"/>
			   <font color="red">*</font>
			   <input type="button" onclick="validateCard();" value="提取" class="button"/>
			</td>
		</tr>
		<tr> 
		<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td colspan="3" class="simple" align="left">
			<s:textfield name="model.remark" cssStyle="width:562px;" /></td>
			
		</tr>
	  </table>
	</fieldset>

	<table align="center" width="95%"> 
	<tr><td><div align="left"><input name="button" type="button" value="增加商品" class="button"  onclick="addRow()"></div></td></tr>
	</table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9">
  <tbody id="tbody">
    <tr bgcolor="#F3F4F3" align="center" height="23">
       <td style="width:70px;">商品编码</td>
       <td style="width:200px;" colspan="2">商 品</td>
       <td style="width:50px;">规格</td>
       <td style="width:80px;">单位</td>
       <td style="width:60px;">单价</td>
       <td style="width:60px;">数量</td>
       <!-- td style="width:100px;">基本单位数量</td-->
       <td style="width:80px;">金额</td>
       <td style="width:100px;">备注</td>
       <td style="width:80px;">操作</td>
    </tr>
    <tr style="display:none" bgcolor="#FFFFFF">
      <td><input name="code" type="text" width="10"  readonly="readonly" style="color:#808080;"/></td>
      <td><input name="pname" type="text" width="10"  readonly="readonly" style="color:#808080;"/>
      <input type="hidden" name="pid" /></td>
      <td><input name="button1"  type="button" onclick="edit(this.parentNode.parentNode)" value="选择" class="button"/></td>
      <td><input name="stardard" type="text" size="10" readonly="readonly" style="color:#808080;"/></td> 
      <td><input name="unitname" type="text" size="10"  onfocus="showUnits(this)"/><input type="hidden" name="unitid"/></td>
      <td><input name="outprice" type="text" size="6" value="0" onkeyup="yzsz(this)" /></td>
      <td>
      	<input name="ncount" type="text" size="6" value="0" onkeyup="yzzs(this)" onfocus="beforeyzzs(this)"/>
        <input name="counts" type="hidden" size="10" value="0"  readonly="readonly" style="color:#808080;"/>
      </td>
      <td><input name="money" type="text" size="10" value="0" readonly="readonly" style="color:#808080;"/></td>
      <td><input name="sremark" type="text" size="15" /></td>
      <td><input type="button" value="删除" onclick="removeRow(this.parentNode.parentNode)" class="button" ></td>
    </tr>
   <%
   List<SalesDetail> sd=(List<SalesDetail>)request.getAttribute("sd");
   if(sd!=null){
   for(SalesDetail s : sd){
   %>
   <tr bgcolor="#FFFFFF"> 
     <td ><input name="pname" type="text" size="10" value="<%=s.getProducts().getName()%>" readonly="readonly" style="color:#808080;"/><input type="hidden" name="pid" value="<%=s.getProducts().getId()%>"/>
	  </td><td><input name="button1"  type="button"   onclick="edit(this.parentNode.parentNode)" value="选择" class="button"/> </td>
      <td ><input name="stardard" value="<%=s.getProducts().getStardard()%>" type="text" size="10" readonly="readonly" style="color:#808080;"/></td> 
      <td ><input name="unitname" value="<%=s.getUnits().getName()%>" type="text" size="10" onfocus="showUnits(this)"/><input type="hidden"  value="<%=s.getUnits().getId()%>" name="unitid"/></td>
      <td><input name="outprice" value="<%=s.getOutPrice()%>" type="text" size="10" value="0" onkeyup="yzsz(this)"/></td>
      <td>
      	<input name="ncount" value="<%=s.getNcount()%>" type="text" size="10" value="0" onkeyup="yzzs(this)" onfocus="beforeyzzs(this)" />
      	<input name="counts" value="<%=s.getCount()%>" type="hidden" size="10" value="0"  readonly="readonly" style="color:#808080;"/>
      </td>
      <td><input name="money" value="<%=s.getAmount()%>" type="text" size="10" value="0" readonly="readonly" style="color:#808080;"/></td>
      <td><input name="sremark" value="<%=s.getRemark()%>" type="text" size="10" /></td>
      <td><input type="button" value="删除" onclick="removeRow(this.parentNode.parentNode)" class="button" ></td>
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
$(document).ready(function(){
	$("#orderCardNoId").focus();
});

//回车实现提取代币卡功能
$('#orderCardNoId').keydown(function(e){
	e = window.event || e;
	if(e.keyCode==13 || e.keyCode == 9){
		e.keyCode=9;
		validateCard();
		//$('#card').focus();
	}
});

/** ready */
	$(document).ready(function() {
		$("#save").validate();
	});

</script>


</body>
</html>