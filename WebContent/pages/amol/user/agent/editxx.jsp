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
    <script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
    <script type="text/javascript" src="${ctx}/pages/amol/base/prosort/mersortCombox.js"></script>
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
    <style type="text/css">
        .input{
            width: 150px;
        }
        .warn{
            color: red;
        }
    </style>
    <title>编辑推送消息</title>
</head>
<body >
<div class="x-panel" style="width: 100%">
    <div class="x-panel-header">编辑推送消息</div>
    <div>
        <%@ include file="/common/messages.jsp"%>
    </div>
    <div align="center" style="width: 100%">
        <s:form action="saveTsRecord.do"  method="post">
            <fieldset style="width: 75%; padding:10px 10px 10px 10px;">
                <legend>编辑推送消息</legend>
                <table align="center" >
                    <input type="hidden" value="${strs}" name="tuIds">
                    <tr>
                        <td align="right">已选择用户：</td>
                        <td class="simple" align="left">
                            ${count}名
                        </td>
                        <td align="right">消息类型：</td>
                        <td class="simple" align="left">
                            <s:select list="#{'0':'活动消息','1':'店庆消息'}" name="xxtype" cssClass="required" cssStyle="width:200px;"></s:select>
                        </td>
                        <td align="right">消息推送方式：</td>
                        <td class="simple" align="left">
                            <s:select list="#{'0':'短信推送','1':'app推送'}" name="tstype" cssClass="required" cssStyle="width:200px;"></s:select>
                        </td>
                    </tr>
                       <td>消息内容： </td>
                        <td style="text-align:left;">
                            <s:textarea name="descn" id="descn" theme="simple" rows="7" cols="32"
                                        cssStyle="border:1px #cbcbcb solid;z-index:auto; width:200px;"/></td>
                    </tr>
                </table>
            </fieldset>
            <table width="100%" style="margin-bottom: 10px;">
                <tr>
                    <td align="center" class="font_white">
                        <s:submit value="发送" cssClass="button"/>&nbsp;&nbsp;
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