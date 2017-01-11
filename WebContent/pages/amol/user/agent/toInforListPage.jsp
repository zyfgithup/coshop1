<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>消息列表</title>
    <%@include file="/common/taglibs.jsp"%>
    <%@include file="/common/ec.jsp"%>
    <%@include file="/common/extjs.jsp"%>
    <%@include file="/common/meta.jsp"%>
    <script type="text/javascript">
        function refresh() {
            ECSideUtil.reload('ec');
        }
        function remove(id){
            if (confirm("确认要删除规则吗?")){
                window.location.href="remove.do?model.id=" + id;
            }
        }
    </script>
    <!-- 地区 -->
    <script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox1.js"> </script>
    <script type="text/javascript" src="${ctx}/pages/amol/base/prosort/mersortCombox.js"></script>
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
</head>
<body>
<div class="x-panel">
    <div class="x-panel-header">消息列表</div>
    <div class="x-panel-body">
        <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit"
                  action="toInforList.do"
                  useAjax="false"
                  doPreload="false"
                  xlsFileName="消息列表.xls"
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
                <ec:column width="100" property="receiveUser.phone" title="手机号"/>
                <ec:column width="100" property="receiveUser.loginId" title="用户名"/>
                <ec:column width="60" property="_type" title="消息类别">
                    <c:if test="${item.xxtype == '0'}">
                        活动消息
                    </c:if>
                    <c:if test="${item.xxtype == '1'}">
                        店庆消息
                    </c:if>
                    <c:if test="${item.xxtype == '2'}">
                        支付完成消息
                    </c:if>
                    <c:if test="${item.xxtype == '3'}">
                        加油车到达目的地消息
                    </c:if>
                    <c:if test="${item.xxtype == '4'}">
                        发送账单消息
                    </c:if>
                    <c:if test="${item.xxtype == '5'}">
                        商家接单消息
                    </c:if>
                </ec:column>
                <ec:column width="60" property="_type" title="消息类别">
                    <c:if test="${item.tstype == '0'}">
                        短信推送
                    </c:if>
                    <c:if test="${item.tstype == '1'}">
                        app推送
                    </c:if>
                    <c:if test="${item.tstype == '3'}">
                        接口推送
                    </c:if>
                </ec:column>
                <ec:column width="300" property="sendContent" title="推送内容"/>
                <ec:column width="100" property="_pusht" title="推送状态"><font color="blue">已推送</font></ec:column>
            </ec:row>
        </ec:table>
    </div>
</div>
</body>
</html>