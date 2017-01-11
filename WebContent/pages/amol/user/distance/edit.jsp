<%@page import="com.systop.amol.user.AmolUserConstants"%>
<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail,com.systop.common.modules.security.user.model.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>

    <link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
    <%@include file="/common/extjs.jsp" %>
    <%@include file="/common/meta.jsp"%>
    <%@include file="/common/validator.jsp"%>
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
    <style type="text/css">
        .input{
            width: 150px;
        }
        .warn{
            color: red;
        }
    </style>
    <title>距离设置</title>
</head>
<body>
<div class="x-panel" style="width: 100%">
    <div class="x-panel-header">距离设置</div>
    <div>
        <%@ include file="/common/messages.jsp"%>
    </div>
    <div align="center" style="width: 100%">
        <s:form action="save.do"  id="save" validate="true" method="post" enctype ="multipart/form-data">
            <s:hidden name="model.id" id="uId" />
            <fieldset style="width: 75%; padding:10px 10px 10px 10px;">
                <legend>编辑距离</legend>
                <table width="100%" align="center" >
                    <tr>
                        <td align="right">距离：</td>
                        <td class="simple" align="left">
                            <s:textfield name="model.distance" cssClass="required" maxlength="40" cssStyle="width:197px;"></s:textfield >
                            <font color="red">*</font>
                        </td>
                    </tr>
                </table>
            </fieldset>
            <table width="100%" style="margin-bottom: 10px;">
                <tr>
                    <td align="center" class="font_white">
                        <s:submit value="保存" cssClass="button"/>&nbsp;&nbsp;
                        <input type="button" value="返回" onclick="history.go(-1)" class="button"/>
                    </td>
                </tr>
            </table>
        </s:form>
    </div>
</div>
<script type="text/javascript">
    function isEmpty(_value){
        return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
    }
</script>
</body>
</html>