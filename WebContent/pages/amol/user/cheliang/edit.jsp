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
    <title>二手车资料</title>
</head>
<body >
<div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>

<div class="x-panel" style="width: 100%">
    <div class="x-panel-header">二手车资料</div>
    <div>
        <%@ include file="/common/messages.jsp"%>
    </div>

    <div align="center" style="width: 100%">
        <s:form action="renzheng.do" id="renzheng" validate="true" method="post">
            <s:hidden name="type" id="type" />
            <s:hidden name="idea" id="idea" />
            <input type="hidden" value="${cheLiang.id}" name="clId"/>
            <fieldset style="width: 75%; padding:10px 10px 10px 10px;">
                <legend>二手车资料</legend>
                <table align="center" >
                    <tr>
                        <td align="right">用户名：</td>
                        <td class="simple" align="left">
                                ${cheLiang.user.loginId}
                        </td>
                        <td align="right">车辆名称：</td>
                        <td class="simple" align="left">
                                ${cheLiang.clName}
                        </td>
                        <td align="right">价格：</td>
                        <td class="simple" align="left">
                                ${cheLiang.clPrice}
                        </td>
                        <td align="right">公里数：</td>
                        <td class="simple" align="left">
                                ${cheLiang.milinary}
                        </td>
                    </tr>
                    <tr>
                        <td align="right">发布信息类型：</td>
                        <td class="simple" align="left">
                                二手车信息
                        </td>
                        <td align="right">使用年限：</td>
                        <td class="simple" align="left">
                                ${cheLiang.usedYears}
                        </td>
                        <td align="right">联系电话：</td>
                        <td class="simple" align="left">
                                ${cheLiang.relatePhone}
                        </td>
                        <td align="right">上牌日期：</td>
                        <td class="simple" align="left">
                                ${cheLiang.shangpaiDate}
                        </td>
                    </tr>

                    <tr>
                        <td align="right">过户次数：</td>
                        <td class="simple" align="left">
                                ${cheLiang.guohucishu}
                        </td>
                        <td align="right">所在区域：</td>
                        <td class="simple" align="left">
                                ${cheLiang.region.parent.parent.name}&nbsp;&nbsp;&nbsp; ${cheLiang.region.parent.name}&nbsp;&nbsp;&nbsp; ${cheLiang.region.name}
                        </td>
                        <td align="right">保险截止：</td>
                        <td class="simple" align="left">
                                ${cheLiang.bxStopDate}
                        </td>
                        <td align="right">年检截止：</td>
                        <td class="simple" align="left">
                                ${cheLiang.njStopDate}
                        </td>
                    </tr>
                        <tr>
                            <td align="right">提交资料：</td>
                            <c:forEach items="${rzList}" var="file">
                                <td class="simple" align="left">
                                    <img src="${ctx}/${file.imageUrl}" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" alt="营业执照资料" style="width: 30px;height: 30px">&nbsp;&nbsp;&nbsp;
                                </td>
                            </c:forEach>
                        </tr>
                </table>
                </tr>
            </fieldset>
            <table width="100%" style="margin-bottom: 10px;">
                <tr>
                    <td align="center" class="font_white">
                        审核意见：
                        <s:textfield name="rzIdea" id="rzIdea" cssClass="required" maxlength="40" cssStyle="width:197px"></s:textfield >
                        <font style="color: red">必填</font>
                        <input type="button" value="通  过" cssClass="button" onclick="subRzResult('success')"/>&nbsp;&nbsp;
                        <input type="button" value="不通过" cssClass="button" onclick="subRzResult('error')"/>&nbsp;&nbsp;
                        <input type="button" value="返回" onclick="history.go(-1)" class="button"/>
                    </td>
                </tr>
            </table>
        </s:form>
    </div>
</div>
<script type="text/javascript">
    function showPic(url,event){
        $("#layer").html("<img src='"+url+"' with='300' height='300'>");
        $("#layer").show();
    }
    function hiddenPic(){
        $("#layer").html("");
        $("#layer").hide();
    }
    function subRzResult(type){
        if(type=='error'&&isEmpty($("#rzIdea").val())){
            alert("请输入审核意见");
            $("#rzIdea").focus();
        }else {
            $("#type").val(type);
            $("#idea").val($("#rzIdea").val());
            $("#renzheng").submit();
        }
    }
    function isEmpty(_value){
        return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
    }
</script>
</body>
</html>