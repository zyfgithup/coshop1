<%@page import="com.systop.common.modules.security.user.model.User"%>
<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>会员管理</title>
    <%@include file="/common/taglibs.jsp"%>
    <%@include file="/common/ec.jsp"%>
    <%@include file="/common/extjs.jsp"%>
    <%@include file="/common/meta.jsp"%>
    <script type="text/javascript">
        function refresh() {
            ECSideUtil.reload('ec');
        }
        function remove(id){
            if (confirm("确认要删除该会员吗?")){
                window.location.href="remove.do?model.id=" + id;
            }
        }

    </script>
    <!-- 地区 -->
    <script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
    <script type="text/javascript" src="${ctx}/pages/amol/base/prosort/mersortCombox.js"></script>
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
    <script type="text/javascript">
        function reset(){
            $("#status").empty();
            $("#name").empty();
            $("#phone").empty();
            $("#regionId").empty();
            $("#prosortId").empty();
        }
        Ext.onReady(function() {
            var merstree = new ProsortTree({
                el : 'comboxmerWithTree',
                target : 'prosortId',
                //emptyText : '选择商家类型',
                url : '${ctx}/base/prosort/prosortTree.do?status=1&type=3',
                defValue : {
                }
            });
            merstree.init();
        });
     /*   Ext.onReady(function() {
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

        });*/
        var arr = new Array();
        var strs="";
        function updateArrays(id){
            strs="";
            if($("#"+id).is(":checked")){
                arr.push($("#"+id).val());
            }else{
                arr.remove($("#"+id).val());
            }
            for(var i=0;i<arr.length;i++){
                strs+=arr[i]+",";
            }
            if(!isEmpty($.trim(strs))){
                $("#plCtr").show();
            }else{
                $("#plCtr").hide();
            }
            strs=strs.substring(0,strs.length-1);
        }
        function isEmpty(_value){
            return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
        }
        function partCtr(){
            if (confirm("确认要编辑推送消息吗？")){
                window.location.href="editxx.do?count="+arr.length+"&strs=" + strs;
            }
        }
        function toMenus(){
            window.location.href="${ctx}/user/agent/getVipInfos.do";
//            $("#searchBtn").click();
        }
    </script>
</head>
<body>
<div class="x-panel">
    <div class="x-panel-header">会员管理</div>
    <div class="x-toolbar">
        <s:form action="getVipInfos" theme="simple">
            <table width="100%">
                <tr>
                    <td>
                        <table>
                            <s:form action="getVipInfos" theme="simple">
                                <td>
                                    状&nbsp;&nbsp;态：<s:select id="status" name="model.status" list='#{"1":"可用","0":"禁用"}' headerKey="" headerValue="全部" cssStyle="width:60px;"/>
                                </td>
                                <td>
                                    来&nbsp;&nbsp;源：<s:select id="resource" name="resource" list='#{"1":"会员","2":"第三方"}' headerKey="" headerValue="全部" cssStyle="width:60px;"/>
                                </td>
                                </tr><tr>
                                <td align="left">
                                    会员姓名：<s:textfield id="name"  name="model.name" size="35" cssStyle="height:19px; width: 100px"/>
                                </td>
                                <td align="left">
                                    手机号：<s:textfield id="phone"  name="model.phone" size="35" cssStyle="height:19px;width: 100px"/>
                                </td>
                                <td align="left">
                                    用户名：<s:textfield id="loginId"  name="model.loginId" size="35" cssStyle="height:19px;width: 100px"/>
                                </td>
                                  <%--  <td>
                                        地区：
                                    </td>
                                    <td>
                                        <span id='comboxWithTree' cssStyle="height:19px;width: 100px"></span>
                                    </td>--%>
                                    <td>
                                        <s:hidden id="regionId" name="regionId"/>
                                    </td>
                                <td align="right">会员类别：</td>
                                <td align="left" colspan="3">
                                    <span id='comboxmerWithTree' style="width: 100px;"></span>
                                    <s:hidden id="prosortId" name="prosortId" cssClass="prosortCheck"/>
                                </td>
                                <td>
                                    <s:submit value="查询" cssClass="button" id="searchBtn"/>
                                    <s:reset value="重置" cssClass="button" onclick="toMenus()"/>
                                </td>
                            </s:form>
                        </table>
                        <span id="plCtr" style="display: none"><a href="javascript:partCtr()"><img
                                src="${ctx}/images/icons/add.gif" />&nbsp;编辑消息&nbsp;</a></span>
                    </td>
                    <td style="padding-right:10px;" align="right">
                        <%--<table>
                            <tr>
                                <td><a href="${ctx}/user/agent/edit.do?type=add"><img src="${ctx}/images/icons/add.gif"/> 添加</a></td>
                            </tr>
                        </table>--%>
                    </td>
                </tr>
            </table>
        </s:form>
    </div>
    <div class="x-panel-body">
        <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit"
                  action="getVipInfos.do"
                  useAjax="false"
                  doPreload="false"
                  xlsFileName="会员管理.xls"
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
                <ec:column width="40" property="_1" title="选择" sortable="false" style="text-align:center">
                    <input type="checkbox" name="selectedItems" id="${item.id}" onclick="updateArrays(this.id)"  value="${item.id}" class="checkbox"/></ec:column>
                <ec:column width="200" property="region.name" title="所属地区"  ellipsis="true">
                    ${item.region.parent.parent.parent.name }&nbsp;&nbsp;${item.region.parent.parent.name }&nbsp;&nbsp;${item.region.parent.name }&nbsp;&nbsp;${item.region.name }
                </ec:column>
                <ec:column width="300" property="address" title="详细地址" ellipsis="true"/>
                <ec:column width="150" property="createTime" title="添加时间" style="text-align:center" format="yyyy-MM-dd HH:mm:ss" cell="date"/>
                <ec:column width="100" property="name" title="姓名" ellipsis="true"/>
                <ec:column width="100" property="_title" title="图片" style="text-align:center">
                    <a href="#"><img alt="${item.name }" src="${ctx }/${item.imageURL }" onmouseover="showPic(this.src,event)" onmouseout="hiddenPic()" width="26" height="26"/></a>
                </ec:column>
                <ec:column width="100" property="vipType.name" title="会员类型" ellipsis="true"/>
                <ec:column width="100" property="allMoney" title="用户余额" ellipsis="true" format="#,##0.00" cell="number"/>
                <ec:column width="60" property="_loginType" title="登录类型" ellipsis="true">
                    <c:if test="${item.loginType=='1'}">

                        本站会员
                    </c:if>
                    <c:if test="${item.loginType=='2'}">

                    ${item.loginLaiyuan}

                    </c:if>
                </ec:column>
                <ec:column width="70" property="loginId" title="用户名" style="text-align:center;" />
                <ec:column width="120" property="phone" title="手机"/>
                <ec:column width="60" property="_status" title="状态" viewsAllowed="html" style="text-align:center;">
                    <s:if test="#attr.item.status == 1">
                        <img src="${ctx}/images/icons/accept.gif" title="可用">
                    </s:if>
                    <s:elseif test="#attr.item.status == 0">
                        <img src="${ctx}/images/grid/clear.gif" title="禁用">
                    </s:elseif>
                </ec:column>
                <ec:column width="200" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
                        <%--<a href="edit.do?type=edit&model.id=${item.id}" >编辑</a> |--%>
                        <%--<c:if test="${item.ifRecommend=='1'}">
                        <a href="recomend.do?ifRcommend=0&model.id=${item.id}">取消推荐</a> |
                         </c:if>
                          <c:if test="${item.ifRecommend=='0'}">
                            <a href="recomend.do?ifRcommend=1&model.id=${item.id}">推荐</a> |
                         </c:if>--%>
                        <a href="deleteVip.do?model.id=${item.id}" >删除</a> |
                        <%--<a href="restPass.do?model.id=${item.id}" >重置密码</a> |
                        <a href="getValidation.do?model.id=${item.id}" >用户评价</a> |--%>
                        <s:if test="#attr.item.status == 0">
                            <a href="qyVip.do?model.id=${item.id}">启用</a> |
                        </s:if>
                        <s:else>
                            <a href="removeVip.do?model.id=${item.id}">禁用 </a> |
                        </s:else>
                    <a href="editVip.do?model.id=${item.id}">详细</a>
                </ec:column>
            </ec:row>
        </ec:table>
    </div>
</div>
</body>
</html>