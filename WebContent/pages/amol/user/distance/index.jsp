<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>距离设置</title>
    <%@include file="/common/taglibs.jsp"%>
    <%@include file="/common/ec.jsp"%>
    <%@include file="/common/extjs.jsp"%>
    <%@include file="/common/meta.jsp"%>
    <script type="text/javascript">
        function refresh() {
            ECSideUtil.reload('ec');
        }
        function remove(id){
            if (confirm("确认要删除吗?")){
                window.location.href="deleete.do?model.id=" + id;
            }
        }
    </script>
    <!-- 地区 -->
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
</head>
<body>
<div class="x-panel">
    <div class="x-panel-header">距离设置</div>
    <div class="x-toolbar">
        <s:form action="index" theme="simple">
            <table width="100%">
                <tr>
                    <td style="padding-right:10px;" align="right">
                        <c:if test="${flag=='yes'}">
                        <table>
                            <tr>
                                <td><a href="edit.do"><img src="${ctx}/images/icons/add.gif"/> 添加</a></td>
                            </tr>
                        </table>
                        </c:if>
                    </td>
                </tr>
            </table>
        </s:form>
    </div>
    <div class="x-panel-body">
        <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit"
                  action="index.do"
                  useAjax="false"
                  doPreload="false"
                  xlsFileName="距离设置.xls"
                  pageSizeList="15,50,100"
                  editable="false"
                  sortable="false"
                  rowsDisplayed="15"
                  generateScript="true"
                  resizeColWidth="true"
                  classic="true"
                  width="100%"
                  height="350px"
                  minHeight="350"
                  toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status">
            <ec:row>
                <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
                <ec:column width="150" property="distance" title="距离"/>
                <ec:column width="150" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
                    <a href="edit.do?model.id=${item.id}" >编辑</a> |
                    <a href="remove.do?model.id=${item.id}" >删除</a>
                </ec:column>
            </ec:row>
        </ec:table>
    </div>
</div>
</body>
</html>