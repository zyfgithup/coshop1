<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>反馈信息</title>
    <%@include file="/common/taglibs.jsp"%>
    <%@include file="/common/ec.jsp"%>
    <%@include file="/common/extjs.jsp"%>
    <%@include file="/common/meta.jsp"%>
    <script type="text/javascript">
        function refresh() {
            ECSideUtil.reload('ec');
        }
        function remove(id){
            if (confirm("确认要删除该反馈信息吗?")){
                window.location.href="remove.do?model.id=" + id;
            }
        }
        Ext.onReady(function() {
            var pstree = new RegionTree({
                el : 'comboxWithTree',
                target : 'regionId',
                //emptyText : '选择地区',
                comboxWidth : 200,
                treeWidth : 195,
                url : '${ctx}/admin/region/regionTree.do?regionId=null',
                defValue : {id:'${regionId }',text:'${regionNameCun }'}
            });
            pstree.init();

        });
    </script>
    <!-- 地区 -->
    <script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox1.js"> </script>
    <script type="text/javascript" src="${ctx}/pages/amol/base/prosort/mersortCombox.js"></script>
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
</head>
<body>
<div class="x-panel">
    <div class="x-panel-header">反馈信息</div>
    <div class="x-toolbar">
    </div>
    <div class="x-panel-body">
        <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit"
                  action="index.do"
                  useAjax="false"
                  doPreload="false"
                  xlsFileName="反馈.xls"
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
                <ec:column width="100" property="user.loginId" title="用户名"/>
                <ec:column width="150" property="createTime" title="反馈时间" style="text-align:center" format="yyyy-MM-dd HH:mm:ss" cell="date"/>
                <ec:column width="200" property="content" title="内容"/>
                <ec:column width="350" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
                    <a href="remove.do?model.id=${item.id}" >删除</a>
                </ec:column>
            </ec:row>
        </ec:table>
    </div>
</div>
</body>
</html>