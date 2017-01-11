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
    <title>发票信息</title>

</head>
<body>

<div class="x-panel" style="width: 100%">
    <div class="x-panel-header">发票信息</div>
    <div><%@ include file="/common/messages.jsp"%></div>
    <div align="center" style="width: 100%">
        <s:form action="save" id="save" validate="true" method="POST">
            <s:hidden name="model.id"  />
            <fieldset style="width: 95%; padding:10px 10px 10px 10px;">
                <legend>发票信息</legend>
                <table width="50%" align="left" >
                    <tr style="float: left">
                        <td>买家</td>
                    </tr>
                    <tr>
                        <td>名称:</td><td>${ra.user.loginId}</td>
                    </tr>
                    <tr>
                        <td>纳税人编号:</td><td>${ra.saxNo}</td>
                    </tr>
                    <tr>
                        <td>地址、电话:</td><td>${ra.comAddr}、${ra.comPhone}</td>
                    </tr>
                    <tr>
                        <td>开户行及帐号:</td><td>${ra.comBankName}、${ra.cardNo}</td>
                    </tr>

                </table>
                <table width="50%" align="left" >
                    <tr style="float: left">
                        <td>卖家</td>
                    </tr>
                    <tr>
                        <td>名称:</td><td>${ra.merUser.name}</td>
                    </tr>
                    <tr>
                        <td>纳税人编号:</td><td>${sj.saxNo}</td>
                    </tr>
                    <tr>
                        <td>地址、电话:</td><td>${sj.address}、${sj.phone}</td>
                    </tr>
                    <tr>
                        <td>开户行及帐号:</td><td>${sj.bankName}、${sj.cardNo}</td>
                    </tr>

                </table>
            </fieldset>
            <table width="100%" style="margin-bottom: 10px;">
                <tr>
                    <td align="center" class="font_white">
                        <s:reset value="返回" cssClass="button" onclick="javascript:history. back();"/>
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
</script>


</body>
</html>