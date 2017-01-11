<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@page import="java.util.Date,java.util.List,java.util.Set,java.util.Iterator"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
    <%@include file="/common/meta.jsp"%>
    <%@include file="/common/extjs.jsp" %>
    <%@include file="/common/validator.jsp"%>
    <title>编辑广告位</title>
</head>
<body>

<div class="x-panel" style="width: 100%">
    <div class="x-panel-header">编辑广告位</div>
    <div><%@ include file="/common/messages.jsp"%></div>
    <div align="center" style="width: 100%">
        <s:form action="save"  id="save" validate="true" method="POST" enctype ="multipart/form-data">
            <br/>
            <fieldset style="width: 95%; padding:10px 10px 10px 10px;">
                <legend>编辑</legend>
                <table width="90%">
                    <s:hidden name="model.id"/>
                    <tr>
                        <td  align="right">
                            名&nbsp;&nbsp;称：</td>
                        <td class="simple" align="left" colspan="1">
                            <s:textfield id="name" name="model.name"></s:textfield>
                        </td>
                    </tr>
                    <tr>
                        <td  align="right">
                            宽&nbsp;&nbsp;度：</td>
                        <td class="simple" align="left" colspan="1">
                            <s:textfield  id="width" name="model.width" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" ></s:textfield>
                        </td>
                    </tr>
                    <tr>
                        <td  align="right">
                            高&nbsp;&nbsp;度：</td>
                        <td class="simple" align="left" colspan="1">
                            <s:textfield id="height" name="model.height" onkeyup="this.value=this.value.replace(/[^\d]/g,'')"></s:textfield>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">核心广告：</td>
                        <td class="simple" align="left">
                            <c:if test="${ifCoreAdv=='1' }">
                            核心：<input type="radio" value="1" name="model.ifCoreAdv" checked="checked"/>&nbsp;&nbsp; 非核心：<input type="radio" value="0" name="model.ifCoreAdv" />
                            </c:if>
                            <c:if test="${ifCoreAdv!='1'}">
                                核心：<input type="radio" value="1" name="model.ifCoreAdv" />&nbsp;&nbsp;&nbsp;&nbsp; 非核心：<input type="radio" value="0" name="model.ifCoreAdv" checked="checked"/>
                            </c:if>
                    </tr>
                    <tr>
                        <td  align="right">
                            内&nbsp;&nbsp;容：</td>
                        <td class="simple" align="left" colspan="1">
                            <s:textarea rows="3" id="remark" name="model.remark" cols="50" ></s:textarea>
                        </td>
                    </tr>
                </table>
            </fieldset>

            <table width="100%" style="margin-bottom: 10px;">
                <tr>
                    <td align="center" class="font_white">
                        <s:submit value="保存" cssClass="button" onclick="return saveValidate();"/>&nbsp;&nbsp;
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