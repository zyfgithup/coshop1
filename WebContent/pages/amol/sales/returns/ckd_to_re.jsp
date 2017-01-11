<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail,com.systop.amol.sales.utils.Payment,com.systop.amol.base.units.model.UnitsItem,java.util.Set,java.util.Iterator"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<title>销售退货单【现金】</title>
<script type="text/javascript" >

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
	alert("实退金额请输入数字！");
	price.value="";
	return false;
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
 var samount = document.getElementById("rttao");//退货货款金额
 samount.value = amount;
 
 var paymentSalesValue = document.getElementById("paymentSalesId").value;//出库时的消费方式
 if(paymentSalesValue == "CASH"){
	 var realReturnMoneyObject = document.getElementById("realReturnMoneyId");//实退金额
	 realReturnMoneyObject.value = amount;	 
 }
 
 var spayamountValue = document.getElementById("spayamountId").value;//实收金额（预收金额）
 var realReturn = document.getElementById("realReturn").value;//实退金额（出库单）
 if(realReturn != null && realReturn != "" && realReturn.length != 0){
	 spayamountValue = Number(spayamountValue) - Number(realReturn);//真实实收金额
 }
 var samountSales = document.getElementById("samountSales").value;//应收金额（出库单）
 var rttaoSales = document.getElementById("rttaoSales").value;//退货金额（退货货款金额【出库单】）
 var stje = Number(samountSales) - Number(rttaoSales) - Number(samount.value) - Number(spayamountValue);
 if(Number(stje) < 0){
	 stje = -(Math.round(stje*100)/100);
	 document.getElementById("realReturnMoneyId").value=stje;
 }
}

/**
 * 点击保存以前的验证
 */
function saveyz(){
	  
	var tab1 = document.getElementsByName("money") ;
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
     var samount = document.getElementById("rttao");//本次退货货款金额（应退金额）
     samount.value = amount;
     
     var spayamountValue = document.getElementById("spayamountId").value;//实收金额（预收金额）
     var realReturn = document.getElementById("realReturn").value;//实退金额（出库单）
     if(realReturn != null && realReturn != "" && realReturn.length != 0){
    	 spayamountValue = Number(spayamountValue) - Number(realReturn);//真实实收金额
     }
     var realReturnMoneyValue = document.getElementById("realReturnMoneyId").value;//本次实退金额
     
     var paymentSalesValue = document.getElementById("paymentSalesId").value;//出库时的消费方式
     if(paymentSalesValue == "CASH"){
    	 document.getElementById("realReturnMoneyId").value = amount;
     }
     
     var samountSales = document.getElementById("samountSales").value;//应收金额（出库单）
     var rttaoSales = document.getElementById("rttaoSales").value;//退货金额（退货货款金额【出库单】）
     var stje = Number(samountSales) - Number(rttaoSales) - Number(samount.value) - Number(spayamountValue);
     if(Number(stje) < 0){
    	 stje = -(Math.round(stje*100)/100);
    	 var rrmi = document.getElementById("realReturnMoneyId");
    	 rrmi.readOnly = false;
    	 rrmi.value=stje;
     }
     
     if(Number(realReturnMoneyValue) > Number(spayamountValue)){
	    	alert("实退金额不能超过【实收金额减去已退金额】！");
	    	return false;
	 }
     
     if(confirm('您确定生成退货单吗？')){
     }else{
    	 return false;
     }
     
     /*//判断是否对商品扫码了
     var codesObject = document.getElementsByName("codes");
 	 for(i=1;i<codesObject.length;i++){
 		if(codesObject[i].value == "" || codesObject[i].value.length == 0){
 			Ext.MessageBox.show({
 	             title:'提示',
 	             minWidth:220,
 	             msg:'<div style=\'width:180\';><br/>必须对退货商品扫码！！！</div>',
 	             buttons:Ext.MessageBox.OK,
 	             icon:Ext.MessageBox.INFO
 	         });
 	    	 return false;
 		} 
 	 }*/
 	 
 	/*var isSubmitValue = document.getElementById("isSubmit").value;
 	if(isSubmitValue == "false"){
 		Ext.MessageBox.show({
            title:'提示',
            minWidth:220,
            msg:'<div style=\'width:180\';><br/>您扫描的条形码商品不是您购买时的条形码商品！即您的退货商品不是您购买时的商品！</div>',
            buttons:Ext.MessageBox.OK,
            icon:Ext.MessageBox.INFO
        });
 		return false;
 	}*/
}

/**
 * 单位转换后基本数量也变化
 */
function unitchange(unitid){
 var r=unitid.parentNode.parentNode;
 var outprice = document.getElementsByName("outprice") ;
 var ncount = document.getElementsByName("ncount") ;
 var count = document.getElementsByName("counts") ;
 var unitname = document.getElementsByName("unitname");
 var uid = unitid.value.split(",");
 unitname[r.rowIndex - 1].value = uid[1];
 var bl = uid[2];
 outprice[r.rowIndex - 1].value = uid[3];
 var sl = count[r.rowIndex - 1].value/bl;
 ncount[r.rowIndex - 1].value = sl; 
 js(unitid.parentNode.parentNode);
}

//点击付款方式为卡消费时触发的事件
function selectfk(){
	//客户级别（普通客户    会员客户）
	var type = document.getElementById("customerType").value;
	//退款方式
	var fkfshkObject = document.getElementById("hk");
	var fkfshk = fkfshkObject.value;
	if(fkfshk == "TOKENCARD"){
		if(1 == type){
		}else{
			alert("您不是会员，不能使用代币卡退款方式！");
		    //将退款方式重置
		    fkfshkObject.selectedIndex=0;
		}
	}
}

function csh(){
	var paymentSalesValue = document.getElementById("paymentSalesId").value;//出库时的消费方式
    if(paymentSalesValue == "CASHADVANCE"){
   	 	document.getElementById("realReturnMoneyId").readOnly=false;
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
       	var index = r.rowIndex-2;
       	var deleteProducts = document.getElementById("deleteProduct");
       	deleteProducts.value = deleteProducts.value + index + ',';
        root.removeChild(r);
        zjs();
    }
    else{
    	alert("不能删最后一行！");
    }
}
</script>
</head>
<body onload="csh();">

<div class="x-panel" style="width: 100%">
<div class="x-panel-header">销售退货单【现金】</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save" id="save" validate="true" method="POST">
	<br/>
	<table align="center" width="95%">
		<tr>
			<td>
			<div align="left">
				出库单：
				<s:textfield name="stNo" readonly="true"></s:textfield>
				<input type="hidden" name="stId" value="${stId }"/>
				&nbsp;&nbsp;
				消费方式：${model.payment.name }
				<input type="hidden" id="paymentSalesId" value="${model.payment }"/>
				&nbsp;&nbsp;
				实收金额：
				${model.spayamount} 元
				&nbsp;&nbsp;
				已退金额：
				${model.realReturnMoney } 元
				<input type="hidden" id="samountSales" value="${model.samount }" />
				<input type="hidden" id="rttaoSales" value="${model.rttao }" />
				<input type="hidden" id="spayamountId" value="${model.spayamount }" />
				<input type="hidden" id="realReturn" value="${model.realReturnMoney }" />
			</div>
			</td>
		</tr>
	</table>
	<s:hidden name="model.id"  /> 
	<fieldset style="width: 95%; padding:10px 10px 10px 10px;">
	<legend>退货单【现金】</legend>
	<table width="100%" align="left" >
		<tr>
			<td align="right" >
				编 &nbsp;&nbsp;&nbsp;号：
			</td>
			<td width="200" class="simple" align="left">
			    <s:textfield  name="model.salesNo" maxLength="20" 
					cssStyle="width:155px;color:#808080;" cssClass="required" readonly="true"/>
           		<!-- font color="red">*</font-->
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
			<td align="right" >
				客&nbsp;&nbsp;&nbsp;&nbsp;户：
			</td>
			<td class="simple" align="left">
			   <s:hidden id="customerid" name="model.customer.id"></s:hidden>
			   <s:hidden id="customerIdCard" name="model.customer.idCard"></s:hidden>
			   <s:hidden id="customerType" name="model.customer.type"></s:hidden>
			   <s:textfield id="name" name="model.customer.name" cssStyle="width:155px;color:#808080;" readonly="true" maxLength="20" style="width: 155px;" cssClass="required"/>
			   <font color="red">*</font>
			</td>
			<td align="right">销&nbsp;售&nbsp;员：</td>
			<td align="left">
				<s:hidden name="model.employee.id" id="empid" />
				<s:textfield name="model.employee.name" id="empname"  maxLength="20" style="width: 155px;" readonly="true" cssStyle="width:155px;color:#808080;"/>
				<span id="ywy"></span>
			</td>
		</tr>
		<tr>
			<td align="right">退款方式：</td>
			<td align="left">
				<s:hidden name="model.payment"/>
				<input type="text" value="现金" style="width: 155px;" disabled="disabled"/>
			</td>
			<td align="right" >
				应退金额：
			</td>
			<td class="simple" align="left">
				<s:textfield id="rttao" name="rttaoThe" readonly="true" maxLength="20" cssStyle="width:156px;" cssClass="required"/>
				元
				<font color="red">*</font>
			</td>
		</tr>
		<tr>
			<td align="right" >
				操&nbsp;作&nbsp;员：
			</td>
			<td class="simple" align="left">
				<s:textfield  name="model.user.name" disabled="true" maxLength="20" cssStyle="width:155px;" />
			</td>
			<td align="right" >
				<font color='red'>本次实退金额：</font>
			</td>
			<td class="simple" align="left">
				<s:textfield id="realReturnMoneyId" name="realReturnMoney" onkeyup="yzszss(this)" readonly="true" maxLength="20" cssStyle="width:156px;" cssClass="required"/>
				元
				<font color="red">*</font>
			</td> 
		</tr>
		<tr> 
		    <td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td colspan="3" class="simple" align="left">
			  <s:textfield name="model.remark" cssStyle="width:565px;" />
			  <input type="hidden" name='deleteProduct' id='deleteProduct' value="${deleteProduct }"/>
			</td>
		</tr>
	  </table>
	</fieldset>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9">
  <tbody id="tbody">
    <tr bgcolor="#F3F4F3" align="center" height="23">
       <td style="width:200px;">商 品</td>
       <td style="width:100px;">规格</td>
       <td style="width:60px;">单位</td>
       <td style="width:60px;">单价</td>
       <td style="width:60px;">数量</td>
       <!-- td style="width:80px;">基本单位数量</td> -->
       <td style="width:60px;">金额</td>
       <td style="width:80px;">备注</td>
       <td style="width:120px;">删除</td>
    </tr>
    <tr style="display:none" bgcolor="#FfFFFF">
      <td>
          <input name="pname" type="text" width="10"  readonly="readonly" style="color:#808080;"/>
          <input type="hidden" name="pid" />
          <input type="hidden" name="OORSalesDetailId"/>
      </td>
      <td><input name="stardard" type="text" size="10" readonly="readonly" style="color:#808080;"/></td> 
      <td><input name="unitname" type="text" size="10"  onfocus="showUnits(this)"/><input type="hidden" name="unitid"/></td>
      <td><input name="outprice" type="text" size="6" value="0" onkeyup="yzsz(this)" /></td>
      <td>
      	<input name="ncount" type="text" size="6" value="0" onkeyup="yzzs(this)" onfocus="beforeyzzs(this)"/>
      	<input name="counts" type="hidden" size="10" value="0"  readonly="readonly" style="color:#808080;"/>
      </td>
      <td><input name="money" type="text" size="10" value="0" readonly="readonly" style="color:#808080;"/></td>
      <td><input name="sremark" type="text" size="15" /></td>
      <td>
      	  <input type='button' value='扫码' onclick='scanCode(this.parentNode.parentNode);' class='button'/>
		  <input type='hidden' name='codes' id='codes'/>
      </td>
    </tr>
   <s:if test="purchaseDetail.size==0">

   </s:if>
   <%
   List<SalesDetail> sd=(List<SalesDetail>)request.getAttribute("sd");
   if(sd!=null){
   for(SalesDetail s : sd){
   %>
   <tr align="center" bgcolor="#FfFFFF"> 
      <td>
          <%=s.getProducts().getName()%>
          <input type="hidden" name="pid" value="<%=s.getProducts().getId()%>"/>
          <input type="hidden" name="OORSalesDetailId" value="<%=s.getId()%>"/>
	  </td>
      <td>
      	  <%=s.getProducts().getStardard()%>&nbsp;
      </td> 
      <td>
           <input name="unitname" value="<%=s.getUnits().getName()%>" type="text" size="5" onfocus="showUnits(this)"/>
           <input type="hidden"  value="<%=s.getUnits().getId()%>" name="unitid"/>
      </td>
      <td><input name="outprice" value="<%=s.getOutPrice()%>" type="text" size="10" value="0" onkeyup="yzsz(this)"/></td>
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
      		<input name="ncount" value="<%=((float)s.getTnorootl())/u%>" type="text" size="10" value="0" onkeyup="yzzs(this);" onfocus="beforeyzzs(this)" />
            <input name="counts" value="<%=s.getTnorootl()%>" type="hidden" size="10" value="0"  readonly="readonly" style="color:#808080;"/>
      </td>
      <td><input name="money" value="<%=(((float)s.getTnorootl())/u) * s.getOutPrice()%>" type="text" size="10" value="0" readonly="readonly" style="color:#808080;"/></td>
      <td><input name="sremark" value="<%=s.getRemark()%>" type="text" size="10" /></td>
      <td>
      	  <!--  input type="hidden" name='yscodes' name='yscodes' value="<%=s.getCodes()%>"/>-->
          <input type="button" value="删除" onclick="removeRow(this.parentNode.parentNode)" class="button" style="width:40px;">
          <input type='button' value='扫码' onclick='scanCode(this.parentNode.parentNode);' class='button' style="width:40px;"/>
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
				<!-- s:hidden id="isSubmit"/-->
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
<script type="text/javascript">
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
			//扫码商品条形码
	    	var codeObject = document.getElementsByName("codes");
	    	codeObject[r.rowIndex-1].value = cus.str;
	    	
	    	/*//出库的商品条形码（原始商品条形码）
	    	var yscodesObject = document.getElementsByName("yscodes");
	    	var yscodesValue = yscodesObject[r.rowIndex-2].value;
	    	if(cus.str != yscodesValue){
	    		Ext.MessageBox.show({
	                title:'提示',
	                minWidth:220,
	                msg:'<div style=\'width:180\';><br/>您扫描的条形码商品不是您购买时的条形码商品！即您的退货商品不是您购买时的商品！</div>',
	                buttons:Ext.MessageBox.OK,
	                icon:Ext.MessageBox.INFO
	            });
	    		document.getElementById("isSubmit").value="false";
		    }else{
		    	document.getElementById("isSubmit").value="true";
		    }*/
		}
	}else{
		alert("请输入商品数量！");
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
			     
			        var html = "<select name=\'unitid\' onchange=\'unitchange(this)\' style=\'width: 80px\'>";
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
</script>
</body>
</html>