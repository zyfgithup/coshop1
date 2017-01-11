<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header">编辑分销商仓库信息</div>
<div class="x-toolbar">
<table width="100%">
	<tr>
		<td align="right">
		<table>
			<tr>
				<td>
					<a href="${ctx}/base/storage/second/index.do" title="返回分销商仓库信息列表">
						<img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>返回分销商仓库信息列表
					</a>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
<table width="100%" border="0" cellpadding="5" cellspacing="1">
	<tr>
		<td><%@ include file="/common/messages.jsp"%></td>
	</tr>
</table>
<div class="x-panel-body"><s:form action="save" validate="true"
	theme="simple">
	<table width="68%" height="50%" align="center">
		<tr>
			<td align="center"><s:hidden name="model.id" /> 
			<fieldset style="margin: 10px;"><legend>编辑分销商仓库基本信息</legend>
			<table>
				<tr>
					<td align="right">仓库名称：</td>
					<td><s:textfield id="model.name"
						name="model.name" size="28" cssClass="nameValidator" />
					<span id="storageName"></span> <font color="red">&nbsp;*</font></td>
				</tr>
				<tr>
					<td align="right" width="100" >分  销  商：</td>
					<td class="simple">
						<s:if test="model.id != null ">
							<span onmousemove="this.setCapture();" onmouseout="this.releaseCapture();" > 
							 	<s:select name="model.creator.id" id="superiorId" 
									list="superiorsMap" headerKey="" headerValue="请选择分销商" cssClass="superiorCheck" cssStyle="width:172;color:#999999;"/>
							</span>
						</s:if>
						<s:else>
							<s:select name="model.creator.id" id="superiorId" 
								list="superiorsMap" headerKey="" headerValue="请选择分销商" cssClass="superiorCheck" cssStyle="width:172;"/>
						</s:else>
						<span id="superiorCheck"></span><font color="red">&nbsp;*</font>
					</td>
				</tr>
				<tr>
					<td align="right">仓库地址：</td>
					<td bgcolor="#FFFFFF"><s:textfield id="model.address"  name="model.address" cssStyle="width:300px;"/>
					</td>
				</tr>
				<tr>
					<td align="right">仓库描述：</td>
					<td bgcolor="#FFFFFF"><s:textarea id="descn" rows="5"
						cols="25" name="model.descn"
						cssStyle="border:1px #cbcbcb solid;z-index:auto; width:300px;"></s:textarea>
					</td>
				</tr>
			</table>
			</fieldset>
			<table width="100%" style="margin-bottom: 10px;">
				<tr>
					<td colspan="2" align="center" class="font_white"><s:submit
						value="保存" cssClass="button"></s:submit>&nbsp;&nbsp; <s:reset
						value="重置" cssClass="button" /></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</s:form></div>
</div>
<script type="text/javascript">
	$(function() {
		$.validator.addMethod("nameValidator", function(value, element) {
			var res;
			if (value.length > 255) {
				res = "err";
				$("#storageName").html(
						"<font color='red'>仓库名称应该长度小于255字符</font>");
			} else if (value == null || value.length == 0) {
				res = "err";
				$("#storageName").html("<font color='red'>仓库名称不允许为空</font>");
			} else {
				$("#storageName").html("");
			}
			return res != "err";
		}, "");
		$.validator.addMethod("superiorCheck", function(value, element) {
			var res;
			if (value == null || value.length == 0) {
				res = "err";
				$("#superiorCheck").html("<font color='red'>经销商不允许为空</font>");
			} else {
				$("#superiorCheck").html("");
			}
			return res != "err";
		}, "");
	});

	$(document).ready(function() {
		$("#save").validate();
	});
</script>
</body>
</html>