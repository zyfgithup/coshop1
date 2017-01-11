<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List,com.systop.amol.finance.model.*"%>

<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<title>资金收入信息</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<script type="text/javascript">

//删除行
function removeRow(r){
    var root = r.parentNode;
    var allRows = root.getElementsByTagName('tr');
    if(allRows.length>2)
    {    	
    	root.removeChild(r);
    }
    total();
}

/**
 * 选择供应商
 */
function supper(){
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog("${ctx}/pages/amol/base/supplier/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
		if(cus!=null){
			   var tab = document.getElementById("sname") ;
		   tab.value=cus.name;
		   //var tab1 = document.getElementById("sid") ;
		   //tab1.value=cus.id;  
		 }
 	}else{
		window.open("${ctx}/pages/amol/base/supplier/selector.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	    this.returnAction=function(cus){
	    	if(cus!=null){
	    	   var tab = document.getElementById("sname") ;
	    	   tab.value=cus.name;
	    	   //var tab1 = document.getElementById("sid") ;
	    	   //tab1.value=cus.id;  
	    	 }
		};
 	}
}

/**
 * 选择客户
 */
function selectCostomer(){
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog("${ctx}/pages/amol/base/customer/selector.jsp",null,"dialogWidth=880px;resizable: Yes;");
	    if(cus!=null){
		   var tab = document.getElementById("sname") ;
		   tab.value=cus.name;
		}
 	}else{
		window.open("${ctx}/pages/amol/base/customer/selector.jsp","","width=880px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	    this.returnAction=function(cus){
	    	if(cus!=null){
	    		var tab = document.getElementById("sname") ;
	    		tab.value=cus.name;
	    	}
		};
 	}
}
function addRow(){
	var cus=window.showModalDialog("${ctx}/finance/costSort/index.do?type=${status}",null,"dialogWidth=800px;resizable: Yes;");
	if(cus!=null){
		if(cus.id == 0){
			alert("请正确选择类别！");
			return;
		}else{
			var tab1 = document.getElementsByName("costSortId") ;
		    for(var i=1;i<tab1.length;i++){
		    	if(tab1[i].value==cus.id){
		    		alert('此种类别已经存在，不能重复输入！');
		    		return ;
		    	}
		    }
			//alert(cus.name);
			var root = document.getElementById("tbody");
			var allRows = root.getElementsByTagName('tr');
			var cRow = allRows[2].cloneNode(true);
			cRow.style.display="";
			root.appendChild(cRow);
			var rows = root.rows.length ;
			
			var tab = document.getElementsByName("name") ;
		    tab[rows-3].value=cus.name;
	    	var tab1 = document.getElementsByName("costSortId") ;
	    	tab1[rows-3].value=cus.id;
		}
	} 
}
/**
 * 点选择类型时（行号）
 */
function edit(r){
	var cus=window.showModalDialog("${ctx}/finance/costSort/index.do?type=${status}",null,"dialogWidth=800px;resizable: Yes;");
	
	if(cus!=null){
		//alert(cus.id);
		if(cus.id == 0){
			alert("请正确选择类别！");
			return;
		}else{
	 		var tab = document.getElementsByName("name") ;
	    	tab[r.rowIndex-2].value=cus.name;
	    	var tab1 = document.getElementsByName("costSortId") ;
	    	tab1[r.rowIndex-2].value=cus.id;
		}
	}   
}

//金额的数字验证
function yzsz(money){
	if(isNaN(money.value)){
		alert("请输入数字！");
		money.value="0";
		return;
	}
	total();
}

//将总金额计算并填充到总金额文本框
function total(){
	var total = document.getElementsByName("money");
    var amount=0;
    for(var i=0;i<total.length;i++){
   		amount+=Number(total[i].value);
   	}
    var totalMoney=document.getElementById("totalMoney");
    totalMoney.value=amount;
}

//保存前验证输入的金额有没有为0的
function saveValidate(status){
	var tab = document.getElementsByName("name") ;
	var total = document.getElementsByName("money");
	//var empname = document.getElementById("empname");
	var createDate = document.getElementById("createDate");
	//if(empname.value == "" || empname.value == null){
		//alert("请选择经手人！");
		//return false;
	//}
	if(createDate.value == "" || createDate.value == null){
		if(status == 1){
			alert("请输入收入日期！");
		}else{
			alert("请输入支出日期！");
		}			
		return false;
	}
	if(tab.length == 1){
		alert("该单据为空，不允许保存！");
		return false;
	}
	for(var i=1;i<total.length;i++){
		
   		if(Number(total[i].value)-0== 0 || total[i].value==""){
   			alert("金额存在为0的记录，请认真核查！");
   		   return false;
   		}
   		
   	}
		
	if(status == 1){
		if (confirm("您确定要保存收入信息吗？")) {
		    var form=document.getElementById("save");
		    form.submit();
		}
	}else{
		if (confirm("您确定要保存支出信息吗？")) {
		    var form=document.getElementById("save");
		    form.submit();
		}
	}
	 
}
//选择往来单位信息
function units() {
	var types = document.getElementById("selecter").value;
	if(types == '0'){
		document.getElementById("sname").value = "";
	}else if (types == '1') {
		supper();
	}else if (types == '2'){
		selectCostomer();
	}else if (types == '3') {
		document.getElementById("sname").value = "其他";
	}
 	//选择后,让第一项被选中
  	//document.all.selecter.options[0].selected = true; 
}
</script>
</head>
<body>
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">
	<c:choose>
		<c:when test="${status == 1 }">
			<div class="x-panel-header">资金收入信息</div>           
		</c:when>		    
		<c:otherwise>
			<div class="x-panel-header">资金支出信息</div>             
		</c:otherwise>
  </c:choose>
</div>
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save.do" id="save" method="post" onsubmit="validateSubmit();">
	<s:hidden name="model.id" id="id"/>
	<s:hidden name="status"/>
	<fieldset style="width: 98%; padding:10px 10px 10px 10px;">
	<c:choose>
		<c:when test="${status == 1 }">
			<legend>资金收入信息</legend>          
		</c:when>		    
		<c:otherwise>
			<legend>资金支出信息 </legend>            
		</c:otherwise>
  </c:choose>
	
	<table width="100%" align="left">
		<tr>
			<td align="right" width="150">单 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td width="250" class="simple" align="left">&nbsp; 
				<s:textfield name="model.receipts" size="25" cssClass="required" cssStyle="color:#808080;" readonly="true"/> 
				<font color="red">&nbsp;*</font>
			</td>
			<!--<td align="right">经&nbsp;&nbsp;&nbsp;&nbsp;手&nbsp;&nbsp;&nbsp;&nbsp;人：</td>
			<td width="450" class="simple" align="left">&nbsp;
				<s:textfield cssStyle="color:#808080;" name="model.employee.name" 
				id="empname" maxLength="20" readonly="true" size="25" /> 
				<input name="button2" type="button" onclick="emp()" class="button" value="选择" />
				<s:hidden name="model.employee.id" id="empid" /> 				
			</td>  -->
			<td align="right">单&nbsp;据&nbsp;顺&nbsp;序&nbsp;号：</td>
			<td class="simple" width="350" align="left">&nbsp;
				<s:textfield name="model.serialNumber" size="25" cssStyle="color:#808080;" readonly="true"/>
			</td>
		</tr>

		<tr>
			<td align="right" width="150">
				<c:choose>
					<c:when test="${status == 1 }">
						收&nbsp;&nbsp;入&nbsp;&nbsp;日&nbsp;&nbsp;期：         
					</c:when>		    
					<c:otherwise>
						支&nbsp;&nbsp;出&nbsp;&nbsp;日&nbsp;&nbsp;期：            
					</c:otherwise>
  				</c:choose>
			</td>
			<td width="250" class="simple" align="left">&nbsp; 
				<s:textfield name="model.createDate" id="createDate" maxLength="20" size="25" cssClass="required" readonly="true"
				onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate" /> 
				<font color="red">&nbsp;*</font>
			</td>	
			<td align="right">操&nbsp;&nbsp;&nbsp;&nbsp;作&nbsp;&nbsp;&nbsp;&nbsp;员：</td>
			<td class="simple" width="450" align="left">&nbsp; 
				<s:textfield cssStyle="color:#808080;" cssClass="required" name="model.user.name" id="username" maxLength="20" size="25"
				readonly="true" />
				<s:hidden name="model.user.id" id="userid" /> 
			</td>					
		</tr>

		<tr>
			<td align="right">资&nbsp;&nbsp;金&nbsp;&nbsp;类&nbsp;&nbsp;型：</td>
			<td class="simple" align="left">&nbsp; 
				<s:select id="fundsSortMap" list="fundsSortMap" name="model.fundsSort.id"  
                   cssStyle="width:158px;"/>
			</td>
			<td align="right">支&nbsp;&nbsp;&nbsp;&nbsp;票&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td class="simple" align="left">&nbsp; 
				<s:textfield name="model.checkNo" id="checkNo" maxLength="35" size="25" />
			</td>
		</tr>
		
		<tr>
			<td align="right">对&nbsp;&nbsp;方&nbsp;&nbsp;单&nbsp;&nbsp;位：</td>
			<td class="simple" width="350" align="left">&nbsp;
			<s:textfield name="model.source" id="sname" maxLength="20" size="13"
				readonly="true"  cssStyle="color:#808080;"/> 
				
				<select id="selecter" name="selecter" onchange="units(this.options[this.options.selectedIndex].value)"> 
				<option value="0">请选择 
				<option value="1">供应商
				<option value="2">客户 
				<option value="3">其他				
				</select> 
				<input name="supplier" class="button" type="hidden" onclick="supper()" value="供应商" />
				<input name="costomer" class="button" type="hidden" onclick="selectCostomer()" value="客户" /> 
			</td>
			<td align="right">总&nbsp;&nbsp;金&nbsp;&nbsp;额(元)：</td>
			<td class="simple" align="left">&nbsp; 
				<input type="text" value="<fmt:formatNumber value="${model.totalMoney}"  pattern="#,##0.00"/>" style="width: 155px; text-align: right; " readonly name="model.totalMoney" id="totalMoney"/>
				<font color="red">&nbsp;*</font>
			</td>
		</tr>
		
		<tr>				
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td class="simple" align="left" colspan="3">&nbsp;
				<s:textfield name="model.remark" size="98"/>
			</td>	
		</tr>
		
	</table>
	</fieldset>
	<br/>
	<center>
		<table width="98%" id="costSort" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9">
		<tbody id="tbody">
		    <tr  height="23">
		    	<td colspan="6" height="23" bgcolor="#FFFFFF">
		    	<c:choose>
				    <c:when test="${status == 1 }">
		    			<input type="button" class="button" onclick="addRow();" value="添加资金收入"/>
				    </c:when>		    
				    <c:otherwise>
		    			<input type="button" class="button" onclick="addRow();" value="添加资金支出"/>
				    </c:otherwise>
			    </c:choose>	
		    	</td>
		    </tr>
			<tr>
				<td height="23" align="center" bgcolor="#F3F4F3">类型</td>
				<td height="23" align="center" bgcolor="#F3F4F3">摘要</td>
				<td height="23" align="center" bgcolor="#F3F4F3">金额(元)</td>
				<td height="23" align="center" bgcolor="#F3F4F3">操作</td>
			</tr>
			<tr style="display: none" align="center" height="23" align="center" bgcolor="#FFFFFF">
				<td align="center" bgcolor="#FFFFFF">
					<input type='text' name='name' id='name' readonly='readonly' size='32' style="color: #808080; border: 0px;"/>
					<input type='hidden' name='costSortId' />
					<input type='button' onclick='edit(this.parentNode.parentNode);' name='select' class='button' value='选择' />
				</td>
				<td align="center" bgcolor="#FFFFFF">
					<input type='text' name='note' id='notes' size='66'/>
				</td>
				<td align="center" bgcolor="#FFFFFF">
					<input type='text' name='money' value='0' id='money' onkeyup='yzsz(this);' style="text-align: right;" size='20'/>
				</td>
				<td align="center" bgcolor="#FFFFFF">
					<input type='button' value='删除' class='button' onclick='removeRow(this.parentNode.parentNode);'/>
				</td>
			</tr>
			  <%
			   List<CostDetail> sd=(List<CostDetail>)request.getAttribute("costDetail");
			   if(sd != null){
			   for(CostDetail s:sd){
			   %>
				
			   <tr bgcolor="#FFFFFF"  align="center" height="23"> 
			     <td  align="center" bgcolor="#FFFFFF">
			        <input type="text" id='name' name='name' value="<%=s.getCostSort().getName()%>" size="30" style="color: #808080; border: 0px;" readonly="readonly"/>
			        <input type='hidden' name='costSortId' value="<%=s.getCostSort().getId()%>"/>
			        <input type='hidden' name='costDetailId' value="<%=s.getId()%>"/>
			        <input type='button' onclick='edit(this.parentNode.parentNode);' name='select' class='button' value='选择' />
				 </td>
			     <td  align="center" bgcolor="#FFFFFF">
			     	<input type='text' name='note' value="<%=s.getNote()%>" id='note' maxLength='50' size="66"/>
			     </td> 
			     <td  align="center" bgcolor="#FFFFFF">
			     	<input type='text' name='money' value="<%=s.getMoney()%>" style="text-align: right;" onkeyup='yzsz(this);' id='money' maxLength='50' size='20'/>
			     </td>
			     <td  align="center" bgcolor="#FFFFFF">
			     	<input type='button' class="button" value='删除' onclick='removeRow(this.parentNode.parentNode);'/>
			     </td>
			   </tr>
				    
			   <%}} %>
			   </tbody>
		</table>
	</center>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
				<input type="button" value="保存" class="button" onclick="return saveValidate(${status});" />
			  <!--<s:submit value="保存" cssClass="button" onclick="return saveValidate();" /> -->
			  &nbsp;&nbsp;
			  <input type="button" value="返回" onclick="history.go(-1)" class="button"/> 
				<s:if test="model.red == 0">					  
			  		&nbsp;&nbsp;
				<input type="button" value="冲红" style= "color:red; " onclick="red(${model.id},${status},${model.receipts})" class="button"/> 
				</s:if>
			</td>
		</tr>
	</table>
</s:form>
</div>
</div>
</body>
<script type="text/javascript">
$(document).ready(function() {
	$("#save").validate();
});

function red(id,status,receipts) {
     if (confirm("确认要冲红单号为【" + receipts + "】的单据吗？冲红后不能恢复！")) {
        window.location.href = "${ctx}/finance/cost/red.do?model.id=" + id + "&status=" + status;
     }
}

</script>
</html>
