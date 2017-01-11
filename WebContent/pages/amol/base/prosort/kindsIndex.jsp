<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>类别种类管理</title>
    <%@include file="/common/ec.jsp"%>
    <%@include file="/common/extjs.jsp"%>
    <%@include file="/common/meta.jsp"%>
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
    <script type="text/javascript">
        function remove(id){
            if (confirm("确认要删除该种类吗？")){
                window.location.href="${ctx}/base/prosort/removeKinds.do?model.id=" + id;
            }
        }
        function showPic(url,event){
            $("#layer").html("<img src='"+url+"' with='300' height='300'>");
            $("#layer").show();
        }
        function hiddenPic(){
            $("#layer").html("");
            $("#layer").hide();
        }
    </script>
</head>
<body>
<div class="x-panel">
    <div class="x-panel-header">类别种类管理</div>
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
    <%--<div class="x-toolbar">
        <table >
            <tr>
                <s:form action="findKinds.do" theme="simple">
                    <td>种类名称</td>
                    <td><s:textfield name="name" size="10" /></td>
                    <td style="padding-left: 5px" align="left"><s:submit value="查询" cssClass="button"></s:submit>
                    </td>
                </s:form>
            </tr>
        </table>
    </div>--%>
    <div><%@ include file="/common/messages.jsp"%></div>
    <div class="x-panel-body">
        <!-- 悬浮图片 -->

        <ec:table items="items" var="item"
                  retrieveRowsCallback="limit" sortRowsCallback="limit" action="findKinds.do"
                  useAjax="true" doPreload="false" maxRowsExported="10000000"
                  pageSizeList="15,20,50,100" editable="false" sortable="false"
                  rowsDisplayed="15" generateScript="true" resizeColWidth="true"
                  classic="false" width="100%" height="380px" minHeight="380"
                  toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
            <ec:row>
                <ec:column width="30" property="_s" title="No."
                           value='${GLOBALROWCOUNT}' sortable="false" style="text-align:center" />
                <ec:column width="120" property="name" title="种类名称" ellipsis="true"/>
                <ec:column width="120" property="price" title="价格" ellipsis="true"/>
                <ec:column width="120" property="parentProsort.name" title="所属类别" ellipsis="true"/>
                <ec:column width="120" property="creator.name" title="创建者" ellipsis="true"/>
                <ec:column width="150" property="_0" title="操作"
                           style="text-align:center" sortable="false">
                        <a href="javascript:remove('${item.id}')">删除</a>
                </ec:column>
            </ec:row>

        </ec:table></div>
</div>
<script type="text/javascript" src="${ctx}/pages/amol/base/product/prosortCombox.js"></script>
</body>
</html>