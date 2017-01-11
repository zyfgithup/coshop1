<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>

    <link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
    <%@include file="/common/extjs.jsp" %>
    <%@include file="/common/meta.jsp"%>
    <%@include file="/common/validator.jsp"%>
    <script type="text/javascript">

        function updateImageUI(){
            $("#imageId").toggle();
            //var imageIdObj = document.getElementById("imageId");
            //imageIdObj.innerHTML = "<input id='fileId' type='file' name='attch' />";
        }

    </script>
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>

    <title>编辑种类</title>
</head>
<body>
<div class="x-panel" style="width: 100%">
    <div class="x-panel-header">编辑种类</div>
    <div class="x-toolbar">
        <table width="100%">
            <tr>
                <td align="right">
                    <table>
                        <tr>
                            <td><a href="${ctx}/base/prosort/merTableIndex.do"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>&nbsp;返回&nbsp;&nbsp;</a></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    <div><%@ include file="/common/messages.jsp"%></div>
    <div align="center" style="width: 100%">
        <s:form action="KindsSave" id="save" validate="true" method="POST" enctype ="multipart/form-data" >
            <s:hidden name="model.id" id="uId" />
            <fieldset style="width: 60%; padding:10px 10px 10px 10px;">
                <legend>种类信息</legend>
                <table width="100%" align="center">
                    <tr>
                        <td align="right" width="300px">所属店铺种类：</td>
                        <td class="simple" align="left">
                            <span id='comboxWithTree' style="width: 120px;">${ps.name}</span>
                            <input name="type" type="hidden" value="2"/>
                            <input type="hidden"  name="pId" value="${ps.id}"/>
                        </td>
                    </tr>
                   <%-- <tr>
                        <td align="right">银行图片：</td>
                        <td align="left" >
                            <c:if test="${model.imageURL == null || model.imageURL == ''}">
                                <input id="fileId" type="file" name="attch" />
                            </c:if>
                            <c:if test="${model.imageURL != null && model.imageURL != ''}">
                                <a href="${ctx }/${model.imageURL }"><img alt="${model.name }" src="${ctx }/${model.imageURL }" width="137" height="75"/></a>
                                &nbsp;&nbsp;<input type="button" value="修改图片" class="button" onclick="updateImageUI();"/>
                            </c:if>
                            <input type="hidden" name="model.imageURL" value="${model.imageURL }"/>
                            <div id="imageId" style="display:none"><input id="fileId" type="file" name="attch" /></div>
                            <font color="red">【建议图片大小105*105或此大小的倍数】*</font>
                        </td>
                    </tr>--%>
                    <tr>
                        <td align="right">种类名称：</td>
                        <td class="simple" align="left" colspan="3">
                            <s:textfield  id="" name="proName" cssClass="required" cssStyle="width:140px;"></s:textfield >
                            <font color="red">*</font>
                        </td>
                        <td class="simple" align="left">
                        </td>
                    </tr>
                    <tr>
                        <td align="right">价&nbsp;&nbsp;&nbsp;&nbsp;格：</td>
                        <td class="simple" align="left" colspan="3">
                            <s:textfield  id="" name="price" cssClass="required" cssStyle="width:140px;"></s:textfield >
                            <font color="red">*</font>
                        </td>
                        <td class="simple" align="left">
                        </td>
                    </tr>
                    <tr>
                        <td align="right">描&nbsp;&nbsp;述：</td>
                        <td align="left" colspan="1">
                            <s:textarea id="remark" name="descn" cols="50" rows="4" />
                        </td>
                    </tr>
                </table>
            </fieldset>
            <table width="100%" style="margin-bottom: 10px;">
                <tr>
                    <td align="center" class="font_white">
                        <s:submit value="保存" cssClass="button"/>&nbsp;&nbsp;
                        <s:reset value="重置" cssClass="button"/>&nbsp;&nbsp;
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
</script>
<script type="text/javascript"
        src="${ctx}/pages/amol/base/product/prosortCombox.js"></script>
<script type="text/javascript">
    $.validator.addMethod("prosortStatusCheck", function(value, element) {
        var res;
        var costsort = document.getElementById('prosortStatus').value;
        if(costsort == '') {
            res = "err";
            document.getElementById('prosortStatusTip').innerHTML = '&nbsp;<font color="red">'+'未选择状态'+'</font>';
        }else{
            res = "od";
            document.getElementById('prosortStatusTip').innerHTML = '';
        }
        return res != "err";
    },"");

</script>
</body>
</html>