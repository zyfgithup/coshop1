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
    <title>认证资料</title>
</head>
<body >
<div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>

<div class="x-panel" style="width: 100%">
    <div class="x-panel-header">认证资料</div>
    <div>
        <%@ include file="/common/messages.jsp"%>
    </div>
    <div align="center" style="width: 100%">
        <s:form action="renzheng.do" id="renzheng" validate="true" method="post">
            <s:hidden name="type" id="type" />
            <s:hidden name="idea" id="idea" />
            <input type="hidden" value="${rz.id}" name="rzId"/>
            <fieldset style="width: 75%; padding:10px 10px 10px 10px;">
                <legend>认证资料</legend>
                <table align="center" >
                    <tr>
                        <td align="right">手机号：</td>
                        <td class="simple" align="left">
                                ${rz.user.loginId}
                        </td>
                    </tr>
                    <tr>
                        <td align="right">认证名：</td>
                        <td class="simple" align="left">
                                ${rz.user.name}
                        </td>
                    </tr>
                    <tr>
                        <td align="right">身份证：</td>
                        <c:forEach items="${one}" var="file">
                            <td class="simple" align="left">
                                <img src="${ctx}/${file.imageUrl}" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" alt="身份证资料" style="width: 30px;height: 30px">&nbsp;&nbsp;&nbsp;
                            </td>
                        </c:forEach>
                    </tr>
                    <c:if test="${rz.rzType=='1'||rz.rzType=='3'||rz.rzType=='4'||rz.rzType=='5'}">
                    <tr>
                        <td align="right">营业执照：</td>
                        <c:forEach items="${two}" var="file">
                            <td class="simple" align="left">
                                <img src="${ctx}/${file.imageUrl}" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" alt="营业执照资料" style="width: 30px;height: 30px">&nbsp;&nbsp;&nbsp;
                            </td>
                        </c:forEach>
                    </tr>
                    </c:if>
                    <c:if test="${rz.rzType=='2'}">
                        <tr>
                            <td align="right">行驶证：</td>
                            <c:forEach items="${three}" var="file">
                                <td class="simple" align="left">
                                    <img src="${ctx}/${file.imageUrl}" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" alt="行驶证资料" style="width: 30px;height: 30px">&nbsp;&nbsp;&nbsp;
                                </td>
                            </c:forEach>
                        </tr>
                        <tr>
                            <td align="right">驾驶证：</td>
                            <c:forEach items="${four}" var="file">
                                <td class="simple" align="left">
                                    <img src="${ctx}/${file.imageUrl}" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" alt="驾驶证资料" style="width: 30px;height: 30px">&nbsp;&nbsp;&nbsp;
                                </td>
                            </c:forEach>
                        </tr>
                        <tr>
                            <td align="right">危化品运输资格证：</td>
                            <c:forEach items="${five}" var="file">
                                <td class="simple" align="left">
                                    <img src="${ctx}/${file.imageUrl}" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" alt="行驶证资料" style="width: 30px;height: 30px">&nbsp;&nbsp;&nbsp;
                                </td>
                            </c:forEach>
                        </tr>
                    </c:if>
                    <c:if test="${rz.rzType=='3'}">
                        <tr>
                            <td align="right">税务登记证明：</td>
                            <c:forEach items="${six}" var="file">
                                <td class="simple" align="left">
                                    <img src="${ctx}/${file.imageUrl}" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" alt="营业执照资料" style="width: 30px;height: 30px">&nbsp;&nbsp;&nbsp;
                                </td>
                            </c:forEach>
                        </tr>
                    </c:if>
                </table>
                </tr>
            </fieldset>
            <table width="100%" style="margin-bottom: 10px;">
                <tr>
                    <td align="center" class="font_white">
                    认证意见：
                    <s:textfield name="rzIdea" id="rzIdea" cssClass="required" maxlength="40" cssStyle="width:197px"></s:textfield >
                        <input type="button" value="认证成功" cssClass="button" onclick="subRzResult('success')"/>&nbsp;&nbsp;
                        <input type="button" value="认证失败" cssClass="button" onclick="subRzResult('error')"/>&nbsp;&nbsp;
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
        $("#type").val(type);
        $("#idea").val($("#rzIdea").val());
        $("#renzheng").submit();
    }
    function RegCheck(id){
        if($("#"+id).val().indexOf(".")==-1){
            //$("#"+id).val("");
        }
        else if($("#"+id).val().split(".")[1].length!=2||$("#"+id).val().split(".")[0].length==0){
            $("#"+id).val("");
        }
        else{}
    }
    function isEmpty(_value){
        return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
    }

    function updateImageUI(){
        $("#imageId").toggle();
        //var imageIdObj = document.getElementById("imageId");
        //imageIdObj.innerHTML = "<input id='fileId' type='file' name='attch' />";
    }
    $(document).ready(function() {
        $("#save").validate();
    });
    var uid = $("#uId").val();
    if (uid != null && uid.length > 0){
        defPass = "*********";
        $('#pwd').val(defPass);
        $('#repwd').val(defPass);
    }
    $(function() {
        $.validator.addMethod("loginId", function(value, element){
            var reg = new RegExp("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9_\.\-])*$");
            if (!reg.test(value)){//首先判断非法字符
                $('#loginIdMsg').html('用户名包含非法字符!');
                return false;
            }else {//判断用户名是否存在
                $('#loginIdMsg').html('');
                var exist;
                $.ajax({
                    url: '${ctx}/security/user/checkName.do',
                    type: 'post',
                    async : false,
                    dataType: 'json',
                    data: {"model.id" : $("#uId").val(),"model.loginId" : value},
                    success: function(rst, textStatus){
                        exist = rst.exist;
                        if (exist) {
                            $('#loginIdMsg').html('<b>'+value+'</b>已存在!');
                        }else {
                            $('#loginIdMsg').html('');
                        }
                    }
                });
                return !exist;
            }
            return true;
        },"");

        $.validator.addMethod("idCard", function(value, element) {
            if (value.length != 15 && value.length != 18){
                $("#idCardMsg").html('身份证位数错误' + value.length );
                return false;
            }else{
                $("#idCardMsg").html('');
            }
            return true;
        },"");
    });
</script>
</body>
</html>