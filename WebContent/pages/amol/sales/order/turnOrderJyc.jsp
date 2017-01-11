<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>

    <link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
    <%@include file="/common/extjs.jsp" %>
    <%@include file="/common/meta.jsp"%>
    <%@include file="/common/validator.jsp"%>
    <script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>

    <style type="text/css">
        .input{
            width: 150px;
        }

        .warn{
            color: red;
        }
    </style>


    <title>选择加油车</title>
</head>
<body>
<div class="x-panel" style="width: 100%">
    <div class="x-panel-header">选择加油车</div>
    <div>
        <%@ include file="/common/messages.jsp"%>
    </div>
    <div align="center" style="width: 100%">
        <s:form action="updateOrderJycJd.do" id="save" validate="true" method="post">
            <s:hidden name="model.id" id="uId" />
            <s:hidden name="selfEdit" />
            <fieldset style="width: 75%; padding:10px 10px 10px 10px;">
                <legend>选择加油车</legend>
                <table width="100%" align="center">
                    <tr>
                        <td align="right">用户名：</td>
                        <td class="simple" align="left">
                            <s:textfield id="loginId" readonly="true"  name="model.user.loginId" maxlength="14" cssStyle="width:197px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">手机号：</td>
                        <td class="simple" align="left">
                            <s:textfield id="phone" name="model.user.phone"  readonly="true" maxlength="14" cssStyle="width:197px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">加油种类：</td>
                        <td class="simple" align="left">
                            <s:textfield id="remark" name="model.remark"  readonly="true" maxlength="14" cssStyle="width:197px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">约定地址：</td>
                        <td class="simple" align="left">
                            <s:textfield id="position" name="model.position"  readonly="true" maxlength="14" cssStyle="width:197px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">选择加油车：</td>
                        <td class="simple" align="left">
                            <s:textfield id="jycName" name="turnUserIdName" cssClass="required"  readonly="true" maxlength="14" cssStyle="width:197px;" />
                            <input type="hidden" id="userId" name="userId"/>
                            <button onclick="getUsers('${model.id}')">选择</button>
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
    $(document).ready(function() {
        $("#save").validate();
    });
    function getUsers(id){
        if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
            var cus=window.showModalDialog("${ctx}/pages/amol/sales/order/selectjyc.jsp?id="+id,null,"dialogWidth=590px;resizable: Yes;");
            if(cus!=null){
                var tab = document.getElementById("userId") ;
                tab.value=cus.id;
                var tab2 = document.getElementById("jycName") ;
                tab2.value=cus.name;
            }
        }else{
            window.open("${ctx}/pages/amol/sales/order/selectjyc.jsp?id="+id,"","width=590px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
            this.returnAction=function(cus){
                if(cus!=null){
                    var tab = document.getElementById("userId") ;
                    tab.value=cus.id;
                    var tab2 = document.getElementById("jycName") ;
                    tab2.value=cus.name;
                }
            };
        }
    }
</script>
</body>
</html>