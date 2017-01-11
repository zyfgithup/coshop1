<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>商品信息管理</title>
    <link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
    <%@include file="/common/extjs.jsp"%>
    <%@include file="/common/meta.jsp"%>
    <%@include file="/common/validator.jsp"%>
    <script type="text/javascript" src="${ctx}/pages/amol/base/product/prosortCombox.js"></script>
</head>
<body>
<div class="x-panel">
    <div class="x-panel-header">编辑广告信息</div>
    <div class="x-toolbar">
        <table width="100%">
            <tr>
                <td align="right">
                    <table>
                        <tr>
                            <td><a href="${ctx}/base/product/distributor/indexPlatformProduct.do"><img src="${ctx}/images/grid/filterArrow.gif" style="width: 12px; height: 12px;"/>&nbsp;返回&nbsp;&nbsp;</a></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    <div><%@ include file="/common/messages.jsp"%></div>
    <div class="x-panel-body">
        <s:form action="saveAdv" validate="true" method="POST" enctype ="multipart/form-data" theme="simple">
            <table width="78%" height="50%" align="center">
                <tr>
                    <td align="center"><s:hidden name="model.id" />
                        <table width="100%" height="50%" align="center">
                            <tr>
                                <td align="right">广告标题：</td>
                                <td align="left"><s:textfield id="title"
                                                              name="model.title" cssStyle="width:145px;" />
                                </td>
                            </tr>
                            <tr>
                                <td align="right">所属类型：</td>
                                <td align="left"><s:select list="#{'0':'首页轮播','3':'广告'}" name="model.type" cssClass="required" cssStyle="width:200px;"></s:select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">广告标题：</td>
                                <td align="left"><s:textfield id="title"
                                                              name="model.title" cssStyle="width:145px;" />
                                </td>
                            </tr>
                            <tr>
                                <td align="right">开始日期：</td>
                                <td align="left" ><input type="text" id="startTime" name="startTime" value='${startDate}'
                                                         onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate"  readonly="readonly"/> <font color="red">&nbsp;*</font>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">结束日期：</td>
                                <td align="left" ><input type="text" id="endTime" name="endTime" value='${endDate}'
                                                         onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate"  readonly="readonly"/> <font color="red">&nbsp;*</font>
                                </td>
                            </tr>

                            <tr>
                                <td align="right">广&nbsp;告&nbsp;位：</td>
                                <td align="left" >
                                    <s:select id="positionMap" list="positionMap"
                                                            name="model.advPosition.id" headerKey="" headerValue="请选择"
                                                            cssStyle="width:145 px;color:#808080;border: 1px solid #808080;"
                                                            cssClass="required" />
                                </td>
                            </tr>

                            <tr>
                                <td align="right">访问地址：</td>
                                <td align="left" ><s:textfield id="advUrl" cssStyle="width:145px;"
                                                               name="model.advUrl" />
                                </td>
                            </tr>
                            <tr>
                                <td align="right">内容：</td>
                                <td align="left" >
                                        <s:textfield id="content"
                                                     name="model.content"  cssStyle="width:145px;"/>
                            </tr>
                            <tr>
                                <td align="right">广告图片：</td>
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
                                    <font color="red">【建议图片大小400*300或此大小的倍数】*</font>
                                </td>
                            </tr>
                        </table>
                        </fieldset>
                        <table width="100%" style="margin-bottom: 10px;">
                            <tr>
                                <td colspan="2" align="center" class="font_white">
                                    <s:submit value="保存" cssClass="button"></s:submit>&nbsp;&nbsp;
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </s:form></div>
</div>
</body>
</html>