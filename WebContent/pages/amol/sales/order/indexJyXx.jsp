<%@page import="com.systop.common.modules.region.RegionConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <title>加油消息</title>
    <%@include file="/common/taglibs.jsp"%>
    <%@include file="/common/ec.jsp"%>
    <%@include file="/common/extjs.jsp"%>
    <%@include file="/common/meta.jsp"%>
    <script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
    <script type="text/javascript"
            src="${ctx}/pages/admin/security/permission/permission.js"></script></div>
    <script type="text/javascript">
        function removeObj(id){
            window.location.href = "remove.do?model.id="+id;
        }
        Ext.onReady(function() {
            var pstree = new RegionTree({
                el : 'comboxWithTree',
                target : 'regionId',
                //emptyText : '选择地区',
                comboxWidth : 200,
                treeWidth : 195,
                url : '${ctx}/admin/region/regionTree.do?regionId=null',
                defValue : {id:'${regionId}',text:'${regionNameCun}'}
            });
            pstree.init();

        });
        function isEmpty(_value){
            return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
        }
        function partCtr(){
            if (confirm("确认要操作这些订单吗？")){
                window.location.href="plRemove.do?type=success&strs=" + strs;
            }
        }
        function red(id){
            if (confirm("确认要冲红吗？冲红后不能恢复！")) {
                window.location.href="${ctx}/salesOrder/redRed.do?model.id=" + id;
            }
        }
        function removeAo(){
            alert("此订单已经生成出库单 ，不能冲红！");
        }
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
        function toMenu(){
            window.location.href = "${ctx}/salesOrder/indexJyXx.do";
        }
    </script>
</head>
<body>
<div class="x-panel">
    <div class="x-panel-header" >
        <div style="float: left;">加油消息</div>
    </div>
    <div><%@ include file="/common/messages.jsp"%></div>
    <div class="x-toolbar">
        <form action="${ctx}/salesOrder/indexJyXx.do" method="post">
            <table>
                <tr><td>&nbsp;&nbsp;订单：<s:textfield name="model.salesNo" size="17" id="salesNo"/> </td>
                    <td>      用户：<s:textfield name="name" size="10" id="name"/>
                    </td>
                    <td>      手机号：<s:textfield name="mobile" size="10" id="mobile"/>
                    </td>

                </tr>
                <tr><td>开始日期：<input id="startDate" name="startDate" size="20"  value='${startDate}' onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate"  readonly="readonly"/></td>
                    <td>结束日期：<input id="endDate" name="endDate" size="20" value='${endDate}' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate"  readonly="readonly"/>  </td>
                    <%--<td>地区：<span id='comboxWithTree'></span><s:hidden id="regionId" name="regionId"/></td>--%>
                    <td><s:submit value="查询" cssClass="button" cssStyle="width:36px;"/>
                        <s:reset value="重置" cssClass="button" onclick="toMenu()"/>
                    </td>
                </tr>
            </table>
		<span id="plCtr" style="display: none"><a href="javascript:partCtr()"><img
                src="${ctx}/images/icons/add.gif" />&nbsp;批量删除&nbsp;</a></span>
        </form>
    </div>
    <div class="x-panel-body">
        <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit"
                  action="indexJyXx.do"
                  useAjax="false"
                  doPreload="false"
                  xlsFileName="加油消息.xls"
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
                  toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status"  >
            <ec:row>
                <!--2、加油用户，用户电话，加油种类，约定地址，发布时间，状态（是否被接单）-->
                <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
                <%--<ec:column width="40" property="_1" title="选择" sortable="false" style="text-align:center">
                    <input type="checkbox" name="selectedItems" id="${item.id}" onclick="updateArrays(this.id)"  value="${item.id}" class="checkbox"/></ec:column>--%>

                <ec:column width="100" property="user.loginId" title="用户" ellipsis="true"/>


                <ec:column width="100" property="user.phone" title="手机号" ellipsis="true"/>


                <ec:column width="100" property="remark" title="加油种类" ellipsis="true"/>

                <ec:column width="230" property="position" title="约定地址" ellipsis="true"/>



                <ec:column width="150" property="createTime" title="发布时间" style="text-align:center" format="yyyy-MM-dd HH:mm:ss" cell="date"/>
                <ec:column width="150" property="salesNo" title="订单号" style="text-align:center"/>
                <ec:column property="_0" title="分配加油车" style="text-align:center"
                           viewsAllowed="html" sortable="false">
                    <a href="turnOrderJyc.do?model.id=${item.id}" >分配选择</a>
                </ec:column>
            </ec:row>
        </ec:table>
    </div>
</div>
</body>
</html>