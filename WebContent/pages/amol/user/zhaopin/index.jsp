<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>招聘信息</title>
    <%@include file="/common/taglibs.jsp"%>
    <%@include file="/common/ec.jsp"%>
    <%@include file="/common/extjs.jsp"%>
    <%@include file="/common/meta.jsp"%>
    <script type="text/javascript">
        function refresh() {
            ECSideUtil.reload('ec');
        }
        function remove(id){
            if (confirm("确认要删除二手车吗?")){
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
    <div class="x-panel-header">招聘</div>
    <div class="x-toolbar">
        <s:form action="index" theme="simple">
            <table width="100%">
                <tr>
                    <td>
                        <table>
                            <s:form action="index" theme="simple">
                                <td align="left">
                                    用户名：<s:textfield id="loginId"  name="model.user.loginId" size="35" cssStyle="height:19px;"/>
                                </td>
                                <td>
                                    审核状态：<s:select name="model.visible" list='#{"0":"未审核","1":"通过","-1":"未通过"}' headerKey="" headerValue="全部" cssStyle="width:100px;"/>
                                </td>
                                <td>
                                    <s:submit value="查询" cssClass="button"></s:submit>
                                </td>
                            </s:form>
                        </table>
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
                  xlsFileName="招聘.xls"
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
                <ec:column width="100" property="_fblx" title="发布类型">

                    招聘信息
                </ec:column>
                <ec:column width="100" property="comName" title="公司名称"/>
                <ec:column width="100" property="zwName" title="职位名称"/>
                <ec:column width="100" property="zpNums" title="招聘数量"/>
                <ec:column width="100" property="relatePhone" title="联系电话"/>
                <ec:column width="100" property="_shzt" title="审核状态">

                    <c:if test="${item.visible =='1'}">

                        <font color="#adff2f">通过</font>

                    </c:if>
                    <c:if test="${item.visible =='0'|| item.visible ==null}">

                        <font color="#8a2be2">未审核</font>

                    </c:if>
                    <c:if test="${item.visible =='-1'}">

                        <font color="#8b0000">不通过</font>

                    </c:if>
                </ec:column>
                <ec:column width="350" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
                           <%--<c:if test="${item.visible=='1'}">
                            <a href="unsealUser.do?model.id=${item.id}">不通过</a>
                           </c:if>
                    <c:if test="${item.visible =='0'|| item.visible ==null|| item.visible =='-1'}">
                            <a href="remove.do?model.id=${item.id}">通过</a>
                    </c:if>--%>
                    <a href="lookFiles.do?model.id=${item.id}" >审核</a>
                </ec:column>
            </ec:row>
        </ec:table>
    </div>
</div>
</body>
</html>