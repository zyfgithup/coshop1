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
				${ model.receipts }
			</td>
			<td align="right">单&nbsp;据&nbsp;顺&nbsp;序&nbsp;号：</td>
			<td class="simple" width="350" align="left">&nbsp;
				${ model.serialNumber } 
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
				<fmt:formatDate value="${ model.createDate }" pattern="yyyy-MM-dd"/>				
			</td>	
			<td align="right">操&nbsp;&nbsp;&nbsp;&nbsp;作&nbsp;&nbsp;&nbsp;&nbsp;员：</td>
			<td class="simple" width="450" align="left">&nbsp; 
				${ model.user.name }
			</td>					
		</tr>

		<tr>
			<td align="right">资&nbsp;&nbsp;金&nbsp;&nbsp;类&nbsp;&nbsp;型：</td>
			<td class="simple" align="left">&nbsp; 
				${ model.fundsSort.name }
			</td>
			<td align="right">支&nbsp;&nbsp;&nbsp;&nbsp;票&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td class="simple" align="left">&nbsp; 
				${ model.checkNo }
			</td>
		</tr>
		
		<tr>
			<td align="right">对&nbsp;&nbsp;方&nbsp;&nbsp;单&nbsp;&nbsp;位：</td>
			<td class="simple" width="350" align="left">&nbsp;
				${ model.source }
			</td>
			<td align="right">总&nbsp;&nbsp;金&nbsp;&nbsp;额(元)：</td>
			<td class="simple" align="left">&nbsp;
				<fmt:formatNumber value="${ model.totalMoney }" pattern="#,##0.00"/> 
			</td>
		</tr>
		
		<tr>
				
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td class="simple" align="left" colspan="3">&nbsp;
				${ model.remark } 
			</td>	
		</tr>
		
	</table>
	</fieldset>
	<br/>
	<center>
		<table  width="98%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9" id="costSort">
		<tbody id="tbody">
			<tr align="center" height="23">
				<td bgcolor="#F3F4F3" align="center" height="23">类型</td>
				<td bgcolor="#F3F4F3" align="center" height="23">摘要</td>
				<td bgcolor="#F3F4F3" align="center" height="23">金额(元)</td>
			</tr>
			
			  <%
			   List<CostDetail> sd=(List<CostDetail>)request.getAttribute("costDetail");
			   if(sd != null){
			   for(CostDetail s:sd){
			   %>
				
			   <tr bgcolor="#FFFFFF"  align="center" height="23"> 
			     <td>
			        <input type="text" id='name'name='name' value="<%=s.getCostSort().getName()%>" style="color: #808080; border: 0px;" size="45" readonly="readonly"/>
			        <input type='hidden' readonly="readonly" name='costSortId' value="<%=s.getCostSort().getId()%>"/>
			        <input type='hidden' readonly="readonly" name='costDetailId' value="<%=s.getId()%>"/>
				 </td>
			     <td>
			     	<input type='text' name='note' readonly="readonly" value="<%=s.getNote()%>" id='note' style="color: #808080; border: 0px;" maxLength='50' size="70"/>
			     </td> 
			     <td>
			     	<input type='text' name='money' readonly="readonly" value="<fmt:formatNumber value="<%=s.getMoney()%>"  pattern="#,##0.00"/>" id='money' style="color: #808080; border: 0px;" size='20'/>
			     </td>
			   </tr>
				    
			   <%}} %>
			   </tbody>
		</table>
	</center>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			<input type="button" class="button" value="导出" onclick="javascript:exportExcel(${model.id})">
			  <!--<s:submit value="保存" cssClass="button" onclick="return saveValidate();" /> -->
			  &nbsp;&nbsp;
			  <input type="button" value="返回" onclick="history.go(-1)" class="button"/> 
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
	
	function exportExcel(id){
		 window.location = '${ctx}/finance/cost/exportExcel.do?model.id='+id;
	}	

</script>
</body>
</html>
