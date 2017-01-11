<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>广告位置管理</title>
    <%@include file="/common/ec.jsp"%>
    <%@include file="/common/extjs.jsp"%>
    <%@include file="/common/meta.jsp"%>
    <script type="text/javascript">
        function remove(id){
            if (confirm("确认删除该广告位置吗？")) {
                window.location.href="${ctx}/advpostion/remove.do?model.id=" + id;
            }
        }
    </script>
</head>
<body>
<div class="x-panel">
    <div class="x-panel-header" >
        <div style="float: left;">广告位置管理</div>
        <div style="float: right;">
            <a href="edit.do"><img src="${ctx}/images/icons/add.gif" style="width: 12px; height: 12px;" />&nbsp;添加&nbsp;</a>
        </div>
    </div>
    <div><%@ include file="/common/messages.jsp"%></div>
    <div class="x-toolbar">
        <table width="99%">
            <tr>
                <s:form action="index.do" method="post">
                    <td>
                        名称：<s:textfield name="model.name" id="name" cssStyle="width:110px;"/>
                        <s:submit value="查询" cssClass="button"  cssStyle="width:50px;"></s:submit>
                    </td>
                </s:form>
            </tr>
        </table>
    </div>
    <div class="x-panel-body">
        <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit"
                  action="index.do"
                  useAjax="false"
                  doPreload="false"
                  xlsFileName="广告位置信息.xls"
                  pageSizeList="15,20,40"
                  editable="false"
                  sortable="false"
                  rowsDisplayed="15"
                  generateScript="true"
                  resizeColWidth="true"
                  classic="true"
                  width="100%"
                  height="350px"
                  minHeight="350"
                  toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
            <ec:row>
                <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
                <ec:column width="130" property="name" title="广告位名称"></ec:column>
                <ec:column width="200" property="width" title="宽度" ellipsis="true"/>
                <ec:column width="200" property="height" title="高度" ellipsis="true"/>
                <ec:column width="200" property="_core" title="是否为核心广告" ellipsis="true">
                    <c:if test="${item.ifCoreAdv=='0'}">
                        非核心广告
                    </c:if>
                    <c:if test="${item.ifCoreAdv=='1'}">
                        核心广告
                    </c:if>
                </ec:column>
                <ec:column width="200" property="remark" title="描述" ellipsis="true"/>
                <ec:column width="70" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
                    <a href="edit.do?model.id=${item.id}" title="修改">修改</a> |
                    <a href="javascript:remove(${item.id});" title="删除">删除</a>
                </ec:column>
            </ec:row>
        </ec:table>
    </div>
</div>
</body>
</html>