<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx }/pages/amol/pda/sales/js/saveSales.js"></script>
</head>
<body>
<table width="100%">
 <tr>
   <td >欢迎您：<stc:username></stc:username>登录农资PDA系统！</td>
   <td align="right">		
   <a href="${ctx}/security/user/changePasswordPda.do"  style="color: #336699">个人信息</a>   
   &nbsp;|&nbsp; 	
   <a href="${ctx}/j_acegi_logout" target="_self" style="color: #336699">注销&nbsp;  </a>
</td>
 </tr>
 <tr>
   <td height="25" style="border-bottom: 1px dashed #AAC0DD;padding-left: 2px;" colspan="2">
     <a href="${ctx}/pda/sales/editCard.do" target="_self" class="button">出货</a>
     <a href="${ctx}/pda/sales/index.do" class="button">出货冲红</a>
     <a href="#" onclick="alert(22)" class="button">退货</a>
     <a href="#" onclick="alert(22)" class="button">退货冲红</a>
     <a href="${ctx}/pda/sales/index.do" class="button">查询</a>
   </td>
 </tr>
</table>

<div >
<div><%@ include file="/common/messages.jsp"%></div>
<div align="center" style="width: 100%">
<s:form action="save" id="save" onsubmit="return saveValidate();" validate="true" method="POST">
	<s:hidden name="model.id"  /> 
    <div style="float: left;">
	<table width="100%" align="left" >
		<tr>
			<td align="right" width="25%">
				出库单号：</td>
			<td class="simple" align="left" width="75%">
			    <s:textfield name="model.salesNo" readonly="true" cssStyle="width:200px;"/>
			</td>
	    </tr>
	    <tr>
			<td  align="right">下单日期：</td>
			<td class="simple" align="left">
			    <s:textfield name="model.createTime" readonly="true" cssStyle="width:200px;"/>
			</td>
		</tr>
		<tr>
			<td align="right">代币卡号：</td>
			<td align="left">
			    <s:textfield type="text" name="cardNo" id="cardNo" value="11111111111111111111111111111111" maxLength="32" cssStyle="width:200px;"/>
			</td>
		</tr>	
		<tr>			
			<td align="right">卡的密码：</td>
			<td align="left">
				<s:textfield id="passwordId" name="cardPassword" value="111111" maxLength="6" cssStyle="width:200px;"/>
				<span id="isPasswordId"></span>
			</td>
		</tr>
		<tr>
			<td align="right">刷卡金额：</td>
			<td align="left">
				<s:textfield type="text" id="cardamountId" name="model.cardamount"  maxLength="20" 
				readonly="true" cssStyle="width:200px;color:#808080;"/>
			</td>
		</tr>
		<tr>
			<td align="right">应收金额：</td>
			<td align="left" colspan="1">
				<s:textfield  type="text" name="model.samount" id="samount" maxLength="20" 
				  readonly="true" cssStyle="width:200px;color:#808080;" />
			</td>
		</tr>													
	  </table>
    </div>

    <div style="float: left;">
		<table align="center" width="95%"> 
		<tr>
			<td>
	   		    <div align="left"><input name="button" type="button" value="增加" class="button" onclick="addRow()"></div>
			</td>
		</tr>
		</table>
    </div>
    
    <div style="float: left;">
	<table border="1" align="center" width="95%" id="tbody"> 
	   <tbody id="tbody">
	      <tr bgcolor="#E6F4F7" align="center" height="18">
	         <td style="width:90px;">商 品</td>
	         <td style="width:60px;">单价</td>
	         <td style="width:60px;">数量</td>
	         <td style="width:60px;">金额</td>
	         <td style="width:100px;">操作</td>
	      </tr>
	   </tbody>
	</table>
	<table>
		<tr align="center" height="18">
	       <td>
	       		<input type="submit" value="保存" class="button"/>&nbsp;<input type="reset" value="重置" class="button"/>
	       </td>
	    </tr>
	</table>
    </div>
</s:form>
</div>
</div>
</body>
</html>