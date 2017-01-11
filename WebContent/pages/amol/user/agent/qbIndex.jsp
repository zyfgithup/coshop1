<%@page import="com.systop.common.modules.security.user.model.User"%>
<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>统计管理</title>
    <%@include file="/common/taglibs.jsp"%>
    <%@include file="/common/ec.jsp"%>
    <%@include file="/common/extjs.jsp"%>
    <%@include file="/common/meta.jsp"%>
    <script type="text/javascript">
        function refresh() {
            ECSideUtil.reload('ec');
        }
        function remove(id){
            if (confirm("确认要删除该商家吗?")){
                window.location.href="remove.do?model.id=" + id;
            }
        }

    </script>
    <!-- 地区 -->
    <script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
    <script type="text/javascript" src="${ctx}/pages/amol/base/prosort/mersortCombox.js"></script>
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
    <script type="text/javascript">
        Ext.onReady(function() {
            var merstree = new ProsortTree({
                el : 'comboxmerWithTree',
                target : 'prosortId',
                //emptyText : '选择商家类型',
                url : '${ctx}/base/prosort/prosortTree.do?status=1&type=2',
                defValue : {
                    id : '${model.productSort.id}',
                    text : '${proSortName}'
                }
            });
            merstree.init();
        });
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
        function getDetail(id,type){
            window.location.href = "${ctx}/user/agent/getDetal.do?id="+id+"&type="+type;
        }
        function toMenus(){
            window.location.href="${ctx}/user/agent/index.do";
        }
        function getUsersYe(income,fanxian,charge){
            if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
                var cus=window.showModalDialog("${ctx}/pages/amol/sales/order/seeRestMoney.jsp?income="+income+"&fanxian="+fanxian+"&charge="+charge,null,"dialogWidth=590px;resizable: Yes;");
                if(cus!=null){
                    var tab = document.getElementById("userId") ;
                    tab.value=cus.id;
                    var tab2 = document.getElementById("jycName") ;
                    tab2.value=cus.name;
                }
            }else{
                window.open("${ctx}/pages/amol/sales/order/seeRestMoney.jsp?income="+income+"&fanxian="+fanxian+"&charge="+charge,"","width=590px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
                this.returnAction=function(cus){
                    if(cus!=null){
                        var tab = document.getElementById("userId") ;
                        tab.value=cus.id;
                        var tab2 = document.getElementById("jycName") ;
                        tab2.value=cus.name;
                    }
                };
            }
        }
        function getUsers(id,type){
          if(type=="fx"){
              if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
                  var cus=window.showModalDialog("${ctx}/pages/amol/sales/order/seeMoney.jsp?id="+id+"&type="+type,null,"dialogWidth=590px;resizable: Yes;");
                  if(cus!=null){
                      var tab = document.getElementById("userId") ;
                      tab.value=cus.id;
                      var tab2 = document.getElementById("jycName") ;
                      tab2.value=cus.name;
                  }
              }else{
                  window.open("${ctx}/pages/amol/sales/order/seeMoney.jsp?id="+id+"&type="+type,"","width=590px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
                  this.returnAction=function(cus){
                      if(cus!=null){
                          var tab = document.getElementById("userId") ;
                          tab.value=cus.id;
                          var tab2 = document.getElementById("jycName") ;
                          tab2.value=cus.name;
                      }
                  };
              }
          }
            if(type=="sr"){
                if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
                    var cus=window.showModalDialog("${ctx}/pages/amol/sales/order/seeIncome.jsp?id="+id+"&type="+type,null,"dialogWidth=590px;resizable: Yes;");
                    if(cus!=null){
                        var tab = document.getElementById("userId") ;
                        tab.value=cus.id;
                        var tab2 = document.getElementById("jycName") ;
                        tab2.value=cus.name;
                    }
                }else{
                    window.open("${ctx}/pages/amol/sales/order/seeIncome.jsp?id="+id+"&type="+type,"","width=590px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
                    this.returnAction=function(cus){
                        if(cus!=null){
                            var tab = document.getElementById("userId") ;
                            tab.value=cus.id;
                            var tab2 = document.getElementById("jycName") ;
                            tab2.value=cus.name;
                        }
                    };
                }

            }
            if(type=="fy"){
                if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
                    var cus=window.showModalDialog("${ctx}/pages/amol/sales/order/seeFyIncome.jsp?id="+id+"&type="+type,null,"dialogWidth=590px;resizable: Yes;");
                    if(cus!=null){
                        var tab = document.getElementById("userId") ;
                        tab.value=cus.id;
                        var tab2 = document.getElementById("jycName") ;
                        tab2.value=cus.name;
                    }
                }else{
                    window.open("${ctx}/pages/amol/sales/order/seeFyIncome.jsp?id="+id+"&type="+type,"","width=590px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
                    this.returnAction=function(cus){
                        if(cus!=null){
                            var tab = document.getElementById("userId") ;
                            tab.value=cus.id;
                            var tab2 = document.getElementById("jycName") ;
                            tab2.value=cus.name;
                        }
                    };
                }

            }
        }
        function toMenus(){
            window.location.href="${ctx}/user/agent/qbIndex.do"
        }
        function getOrdersDetail(id,type){
            if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
                var cus=window.showModalDialog("${ctx}/pages/amol/sales/order/seeOrderMoney.jsp?id="+id+"&type="+type,null,"dialogWidth=590px;resizable: Yes;");
                if(cus!=null){
                    var tab = document.getElementById("userId") ;
                    tab.value=cus.id;
                    var tab2 = document.getElementById("jycName") ;
                    tab2.value=cus.name;
                }
            }else{
                window.open("${ctx}/pages/amol/sales/order/seeOrderMoney.jsp?id="+id+"&type="+type,"","width=590px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
                this.returnAction=function(cus){
                    if(cus!=null){
                        var tab = document.getElementById("userId") ;
                        tab.value=cus.id;
                        var tab2 = document.getElementById("jycName") ;
                        tab2.value=cus.name;
                    }
                };
            }
        }
    </script>
</head>
<body>
<div class="x-panel">
    <div class="x-panel-header">统计管理</div>
    <div class="x-toolbar">
        <s:form action="qbIndex" theme="simple">
            <table width="100%">
                <tr>
                    <td>
                        <table>
                            <s:form action="qbIndex" theme="simple">
                                <td>
                                    状&nbsp;&nbsp;态：<s:select name="status" list='#{"1":"可用","0":"禁用"}' headerKey="" headerValue="全部" cssStyle="width:100px;"/>
                                </td>
                              <%--  <td align="left">
                                    商家名称：<s:textfield id="name"  name="name" size="35" cssStyle="height:19px;"/>
                                </td>--%>
                                <td align="left">
                                    用户名：<s:textfield id="loginId"  name="model.loginId" size="35" cssStyle="height:19px;"/>
                                </td>
                               <%-- <c:if test="${loginUser.type=='admin' }">
                                    <td>
                                        地区：
                                    </td>
                                    <td>
                                        <span id='comboxWithTree'></span>
                                    </td>
                                    <td>
                                        <s:hidden id="regionId" name="regionId"/>
                                    </td></c:if>--%>
                               <%-- <td align="right">商家类别：</td>
                                <td align="left" colspan="3">
                                    <span id='comboxmerWithTree' style="width: 145px;"></span>
                                    <s:hidden id="prosortId" name="model.productSort.id" cssClass="prosortCheck"/>
                                </td>--%>
                                <td>
                                    <s:submit value="查询" cssClass="button" />
                                    <s:reset value="重置" cssClass="button" onclick="toMenus()"/>
                                </td>
                            </s:form>
                        </table>
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
                  action="qbIndex.do"
                  useAjax="false"
                  doPreload="false"
                  xlsFileName="钱包管理.xls"
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
               <%-- <ec:column width="270" property="region.name" title="所属地区"  ellipsis="true">
                    ${item.region.parent.parent.parent.name }&nbsp;&nbsp;${item.region.parent.parent.name }&nbsp;&nbsp;${item.region.parent.name }&nbsp;&nbsp;${item.region.name }
                </ec:column>
                <ec:column width="100" property="name" title="法人姓名"  ellipsis="true"/>--%>
                <ec:column width="70" property="loginId" title="用户名" style="text-align:center;" />
                <ec:column width="120" property="phone" title="电话"/>
                <%--<ec:column width="120" property="mobile" title="手机"/>--%>
                <%--<ec:column width="300" property="address" title="地址" ellipsis="true"/>--%>
              <%-- <ec:column width="80" property="_allMoney" title="账户余额" ellipsis="true" onclick=""  >

                <a style="color: blue"href="javascript:getUsersYe('${item.incomeAll}','${item.fxMoney}','${item.chargeAccount}')">${item.allMoney}</a>
            </ec:column>--%>

                <ec:column width="80" property="allMoney" title="账户余额" ellipsis="true" format="###,##0.00" cell="number" onclick="getUsersYe('${item.incomeAll}','${item.fxMoney}','${item.chargeAccount}')"  />

               <%-- <ec:column width="80" property="_chargeAccount" title="充值" ellipsis="true">

                    <a style="color: blue"href="javascript:getUsers('${item.id}','cz')">${item.chargeAccount}</a>
                </ec:column>--%>

                <ec:column width="80" property="chargeAccount" title="充值" ellipsis="true"  format="###,##0.00" cell="number" onclick="getUsers('${item.id}','cz')"/>
                <ec:column width="80" property="fxMoney" title="返现" ellipsis="true"  format="###,##0.00" cell="number" onclick="getUsers('${item.id}','fx')"/>
                <ec:column width="80" property="fyMoney" title="分拥" ellipsis="true"  format="###,##0.00" cell="number" onclick="getUsers('${item.id}','fy')"/>
                <ec:column width="80" property="jyddm" title="加油" ellipsis="true"  format="###,##0.00" cell="number" onclick="getOrdersDetail('${item.id}','jy')"/>
                <ec:column width="80" property="wxddm" title="维修" ellipsis="true"  format="###,##0.00" cell="number" onclick="getOrdersDetail('${item.id}','wx')"/>
                <ec:column width="80" property="bxddm" title="保险" ellipsis="true"  format="###,##0.00" cell="number" onclick="getOrdersDetail('${item.id}','bx')"/>

                <%--<ec:column width="80" property="_fxMoney" title="返现金额" ellipsis="true">
                    <a style="color: blue"href="javascript:getUsers('${item.id}','fx')">${item.fxMoney}</a>
                </ec:column>--%>
               <%-- <ec:column width="80" property="_fxMoney" title="分拥" ellipsis="true">
                    <a style="color: blue"href="javascript:getUsers('${item.id}','fy')">${item.fyMoney}</a>
                </ec:column>--%>
                <%--<ec:column width="80" property="_chargeAccount" title="加油" ellipsis="true">

                    <a style="color: blue"href="javascript:getOrdersDetail('${item.id}','jy')">${item.jyddm}</a>
                </ec:column>--%>
              <%--  <ec:column width="80" property="_chargeAccount" title="维修" ellipsis="true">

                    <a style="color: blue"href="javascript:getOrdersDetail('${item.id}','wx')">${item.wxddm}</a>
                </ec:column>
                <ec:column width="80" property="_chargeAccount" title="保险" ellipsis="true">

                    <a style="color: blue"href="javascript:getOrdersDetail('${item.id}','bx')">${item.bxddm}</a>
                </ec:column>--%>
           <%--      <ec:column width="80" property="_incomeAll" title="总收入" ellipsis="true">
                     <a style="color: blue"href="javascript:getUsers('${item.id}','sr')">${item.incomeAll}</a>
                 </ec:column>--%>
                <ec:column width="80" property="_status" title="状态" viewsAllowed="html" style="text-align:center;">
                    <s:if test="#attr.item.status == 1">
                        <img src="${ctx}/images/icons/accept.gif" title="可用">
                    </s:if>
                    <s:elseif test="#attr.item.status == 0">
                        <img src="${ctx}/images/grid/clear.gif" title="禁用">
                    </s:elseif>
                </ec:column>
         <%--       <ec:column width="350" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
                </ec:column>--%>
            </ec:row>
        </ec:table>
    </div>
</div>
</body>
</html>