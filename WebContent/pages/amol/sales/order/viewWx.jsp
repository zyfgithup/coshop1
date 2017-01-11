<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail,com.systop.amol.base.units.model.UnitsItem,java.util.Set,java.util.Iterator"%>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
    <%@include file="/common/taglibs.jsp"%>
    <%@include file="/common/ec.jsp"%>
    <%@include file="/common/meta.jsp"%>
    <%@include file="/common/validator.jsp"%>
    <script type="text/javascript">
        function showPic(url,event){
            $("#layer").html("<img src='"+url+"' with='300' height='300'>");
            $("#layer").show();
        }
        function hiddenPic(){
            $("#layer").html("");
            $("#layer").hide();
        }
    </script>
    <title>维修订单详情</title>
</head>
<body>
<div class="x-panel">
    <div class="x-panel-header">维修订单详情</div>
    <div align="center" style="width: 100%">

        <fieldset style="width: 95%; padding:10px 10px 10px 10px;">
            <legend>详情</legend>
            <table width="100%">
                <tr>
                    <td align="right" width="15%" >销售单号：</td>
                    <td class="simple" width="35%" >
                        ${model.salesNo}
                        <s:hidden name="model.id" />
                    </td>
                    <td align="right" width="15%" >商&nbsp;家&nbsp;名：</td>
                    <td class="simple" width="35%">
                        ${model.merUser.name }
                    </td>
                </tr>
                <tr>
                    <td align="right"  >订单类型：</td>
                    <td class="simple" width="35%">
                        线上订单
                    </td>
                    <td align="right">账&nbsp;&nbsp;号：</td>
                    <td class="simple">
                        ${model.merUser.shopOfUser.loginId}
                    </td>
                </tr>
                <%--<c:if test="${payment.name =='支付宝'||payment.name =='微信'}">
                <tr>
                     <td align="right">收货姓名：</td>
                    <td class="simple" width="35%">
                        ${model.address.receiveName }
                    </td>
                    <td align="right">收货手机：</td>
                    <td class="simple">
                        ${model.address.receivePhone }
                    </td>
                </tr>
                <tr>
                     <td align="right" width="15%" >收货地址：</td>
                    <td class="simple" width="35%">
                        ${model.address.address }
                    </td>
                    <td align="right">收货邮编：</td>
                    <td class="simple">
                        ${model.address.postCode }
                    </td>
                </tr>

                </c:if>--%>
                <tr>
                    <td align="right">总金额：</td>
                    <td class="simple">
                        <fmt:formatNumber value="${model.samount}" pattern="#,##0.00"/>
                    </td>
                    <td align="right">支付方式：</td>
                    <td align="left" colspan="3">
                        ${model.payment.name}
                    </td>
                </tr>
                <tr>
                    <td align="right">工时费：</td>
                    <td class="simple">
                        ${model.timeMoneys}
                    </td>
                    <td align="right">配件总金额：</td>
                    <td align="left" colspan="3">
                        ${model.pjTotalMoney}
                    </td>
                </tr>
                <tr>
                    <td align="right">支付账号：</td>
                    <td class="simple">
                        ${model.user.loginId}
                    </td>
                    <td align="right">订单状态：</td>
                    <td align="left" colspan="3">
                        <c:if test="${model.status=='0'}">
                            已提交
                        </c:if>
                        <c:if test="${model.status=='-1'}">
                            已取消
                        </c:if>
                        <c:if test="${model.status=='1'}">
                            进行中
                        </c:if>
                        <c:if test="${model.status=='2'}">
                            完成
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td align="right">提交人：</td>
                    <td class="simple">
                        ${model.user.name}
                    </td>
                    <td align="right">提交人手机：</td>
                    <td align="left" colspan="3">
                        ${model.user.phone}
                    </td>
                </tr>
                <tr>
                    <td align="right">确认收货时间：</td>
                    <td class="simple">
                        ${model.createTime}
                    </td>
                    <td align="right">订单生效时间：</td>
                    <td align="left" colspan="3">
                        ${model.createTime}
                    </td>
                </tr>
            </table>
        </fieldset>

    </div>
    <div class="x-panel-body">
        <div id="layer"  style="z-index: 1;position: absolute;right: 500px;top: 20px;width: 300px;height: 200px;display: none;"></div>

        <c:if test="${type!='cz'}">
            <table width="95%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#D9D9D9">

                <tbody id="tbody">
                <tr bgcolor="#F3F4F3" align="center" height="23">
                    <td width="100" align="center">配件名称</td>
                    <td width="100" align="center">配件产地</td>
                    <td width="70" align="center">配件规格</td>
                    <td width="70" align="center">配件数量</td>
                    <td width="70" align="center">配件价格</td>
                </tr>
                <s:if test="purchaseDetail.size==0">
                </s:if>
                <%
                    List<SalesDetail> sd=(List<SalesDetail>)request.getAttribute("sd");
                    if(sd!=null){
                        for(SalesDetail s:sd){
                %>
                <tr height="23" bgcolor="#FFFFFF">
                    <td align="center"><%=s.getPjName()%>  </td>
                    <td align="center"><%=s.getGrowPlace()%></td>
                    <td align="center"><%=s.getStandard()%></td>
                    <td align="center"><%=s.getCount()%></td>
                    <td align="right"><fmt:formatNumber value="<%=s.getOutPrice()%>" pattern="#,##0.00"/></td>
                </tr>
                <%}} %>
                </tbody>
            </table>
        </c:if>


        <table width="100%" style="margin-bottom: 10px;">
            <tr>
                <td style="text-align: center;">
                    <input type="button" value="返回" onclick="history.go(-1)" class="button"/>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>