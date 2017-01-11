<%@page import="com.systop.amol.user.AmolUserConstants"%>
<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="java.util.Date,java.util.List,com.systop.amol.sales.model.SalesDetail,com.systop.common.modules.security.user.model.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>

    <link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
    <%@include file="/common/extjs.jsp" %>
    <%@include file="/common/meta.jsp"%>
    <%@include file="/common/validator.jsp"%>
    <script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox1.js"> </script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=86a779fd4d8626be0c0ac346b2f68dc0"></script>
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
    <style type="text/css">
        .input{
            width: 150px;
        }
        .warn{
            color: red;
        }
    </style>
    <title>会员详细</title>
</head>
<body>
<div class="x-panel" style="width: 100%">
    <div class="x-panel-header">会员详细</div>
    <div>
        <%@ include file="/common/messages.jsp"%>
    </div>
    <div align="center" style="width: 100%">
        <s:form action="cunSave.do" onsubmit="return checkApp()" id="detailSave" validate="true" method="post" enctype ="multipart/form-data">
            <s:hidden name="model.id" id="uId" />
            <fieldset style="width: 75%; padding:10px 10px 10px 10px;">
                <legend>会员详细资料</legend>
                <table width="100%" align="center" >
                    <tr><span id="detail">会员详细资料</span>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:goToMyoOrder(${obj.id})">订单详情</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:fillAccount()">账户充值</a></tr>
                    <tr></tr>
                    <tr></tr>
                    <tr>
                    <td>用户名:${obj.loginId}</td>
                    <td>密码:${obj.password}</td>
                    <td>手机号:${obj.phone}</td>
                    <td>姓名:${obj.name}</td>
                </tr>
                    <tr></tr>
                    <tr>
                        <td>常用地址:${obj.address}</td>
                        <td>会员类别:${obj.vipType.name}</td>
                        <td>账户余额:${obj.allMoney}</td>
                        <td>信用金额:${obj.incomeAll}</td>
                    </tr>
                    <tr></tr>
                    <tr>
                        <td>用户类型:
                        <c:if test="${obj.ifHy=='1'}">
                        合约用户
                        </c:if>
                        <c:if test="${obj.ifHy!='1'}">
                            普通用户
                        </c:if>
                        </td>
                        <td colspan="3"><a href="javascript:showAccountDiv()">新建子账户</a>&nbsp;&nbsp;&nbsp;<a href="javascript:showXyJeDiv()">信用金额充值</a>&nbsp;&nbsp;&nbsp;<a href="startInitXyJe.do?userId=${obj.id}">初始化信用金额</a>
                        <c:if test="${obj.ifHy!='1'}">
                            &nbsp;&nbsp;&nbsp;<a href="setHyUser.do?userId=${obj.id}">设置为合约用户</a>
                        </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td>子账户：</td>
                        <td colspan="3">
                        <c:forEach items="${obj.childSuperiors}" var="user">
                            ${user.loginId}&nbsp;&nbsp;&nbsp;
                        </c:forEach>
                        </td>

                    </tr>
                    <%--<tr>
                        <td align="right">商家图片：</td>
                        <td align="left" >
                            <c:if test="${model.imageURL == null || model.imageURL == ''}">
                                <input id="fileId" type="file" name="attch" />
                            </c:if>
                            <c:if test="${model.imageURL != null && model.imageURL != ''}">
                                <a href="${ctx }/${model.imageURL }"><img alt="${model.name }" src="${ctx }/${model.imageURL }" width="137" height="75"/></a>
                                &nbsp;&nbsp;<input type="button" value="修改图片" class="button" onclick="updateImageUI();"/>
                            </c:if>
                            <input type="hidden" name="model.imageURL" value="${model.imageURL }"/>
                            <div id="imageId" style="display:none"><input id="fileId" type="file" name="attch" /></div>
                            <font color="red">【建议图片大小180*180或此大小的倍数】*</font>
                        </td>
                    </tr>--%>
                </table>
            </fieldset>
        </s:form>
    </div>
    <div align="center" style="width: 100%;display: none"  id="childDiv">
        <s:form action="saveChildAccount.do"  id="saveZzh"  method="post" enctype ="multipart/form-data">
            <fieldset style="width: 75%; padding:10px 10px 10px 10px;">
                <legend>填写子账户</legend>
                <table width="100%" align="center" >
                    <tr>
                        <input type="hidden" name="parentId" value="${obj.id}">
                        <td align="right">用&nbsp;户&nbsp;名：</td>
                        <td align="left"><s:textfield id="loginId"
                                                      name="loginId" cssStyle="width:145px;" /><span id="errorMsg"></span>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">密&nbsp;&nbsp;&nbsp;码：</td>
                        <td align="left" ><s:textfield id="password" cssStyle="width:300px;"
                                                       name="password" cssClass="required" /><font color="red">&nbsp;*</font>
                        </td>
                    </tr>

                    <tr>
                        <td align="right">司&nbsp;机&nbsp;名：</td>
                        <td align="left" ><s:textfield id="name" cssStyle="width:300px;"
                                                       name="name" />
                        </td>
                    </tr>

                    <tr>
                        <td align="right">手&nbsp;机&nbsp;号：</td>
                        <td align="left" ><s:textfield id="phone" cssStyle="width:145px;"
                                                       name="phone" />
                        </td>
                    </tr>
                    <tr>
                        <td align="right">车&nbsp;牌&nbsp;号：</td>
                        <td align="left" ><s:textfield id="folk" cssStyle="width:145px;"
                                                       name="folk" />
                        </td>
                    </tr>
                    <tr>
                        <td align="right">地&nbsp;&nbsp;&nbsp;址：</td>
                        <td align="left" ><s:textfield id="address" cssStyle="width:145px;"
                                                       name="address" />
                        </td>
                    </tr>
                    <tr>
                        <td align="right">账户金额：</td>
                        <td align="left" ><s:textfield id="allMoney" cssStyle="width:145px;"
                                                       name="allMoney" />
                        </td>
                    </tr>
                    <tr>
                        <td align="right">信用金额：</td>
                        <td align="left" ><s:textfield id="incomeAll" cssStyle="width:145px;"
                                                       name="incomeAll" />
                        </td>
                    </tr>
                       <tr>
                            <td align="right">头&nbsp;&nbsp;&nbsp;像：</td>
                            <td align="left" >
                                <%--<c:if test="${model.imageURL == null || model.imageURL == ''}">--%>
                                    <input id="fileId" type="file" name="attch" />
                                <%--</c:if>
                                <c:if test="${model.imageURL != null && model.imageURL != ''}">
                                    <a href="${ctx }/${model.imageURL }"><img alt="${model.name }" src="${ctx }/${model.imageURL }" width="137" height="75"/></a>
                                    &nbsp;&nbsp;<input type="button" value="修改图片" class="button" onclick="updateImageUI();"/>
                                </c:if>
                                <input type="hidden" name="model.imageURL" value="${model.imageURL }"/>
                                <div id="imageId" style="display:none"><input id="fileId" type="file" name="attch" /></div>--%>
                                <font color="red">【建议图片大小180*180或此大小的倍数】*</font>
                            </td>
                        </tr>
                </table>
            </fieldset>
            <table width="100%" style="margin-bottom: 10px;">
                <tr>
                    <td align="center" class="font_white">
                        <input type="button" value="保 存" onclick="jiaoyanyx()"/>&nbsp;&nbsp;
                        <%--<input type="button" value="返回" onclick="history.go(-1)" class="button"/>--%>
                    </td>
                </tr>
            </table>
        </s:form>
    </div>
    <div align="center" style="width: 100%;display: none"  id="fillJeDiv">
        <s:form action="fillXyJe.do"  id="fillXyJe" validate="true" method="post">
            <fieldset style="width: 75%; padding:10px 10px 10px 10px;">
                <legend>信用金额充值</legend>
                <table width="100%" align="center" >
                    <tr>
                        <input type="hidden" name="userId" value="${obj.id}">
                        <td align="right">当前信用金额：</td>
                        <td align="left">${obj.incomeAll}
                        </td>
                    </tr>
                    <tr>
                        <td align="right">充值金额：</td>
                        <td align="left"><s:textfield id="fillMoneyxyje"
                                                      name="fillMoney" cssStyle="width:145px;" />
                        </td>
                    </tr>
                </table>
            </fieldset>
            <table width="100%" style="margin-bottom: 10px;">
                <tr>
                    <td align="center" class="font_white">
                        <s:submit value="保存" cssClass="button" onclick="return xyjiaoyan()"/>&nbsp;&nbsp;
                            <%--<input type="button" value="返回" onclick="history.go(-1)" class="button"/>--%>
                    </td>
                </tr>
            </table>
        </s:form>
    </div>
    <div align="center" style="width: 100%;display: none"  id="fillAccount">
        <s:form action="fillAccount.do"  id="fillXyJe" validate="true" method="post">
            <fieldset style="width: 75%; padding:10px 10px 10px 10px;">
                <legend>账户充值</legend>
                <table width="100%" align="center" >
                    <tr>
                        <input type="hidden" name="userId" value="${obj.id}">
                        <td align="right">用户名：</td>
                        <td align="left">${obj.loginId}
                        </td>
                    </tr>
                    <tr>
                        <td align="right">用户当前余额：</td>
                        <td align="left">
                                ${obj.allMoney}
                        </td>
                    </tr>
                    <tr>
                        <td align="right">充值方式：</td>
                        <td align="left">
                            <select name="fillType">
                                <option value="cash">现金</option>
                                <option value="alipay">支付宝</option>
                                <option value="wxpay">微信</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">充值金额：</td>
                        <td align="left"><s:textfield id="fillMoneyAccount"
                                                      name="fillMoney" cssStyle="width:145px;" />
                        </td>
                    </tr>
                </table>
            </fieldset>
            <table width="100%" style="margin-bottom: 10px;">
                <tr>
                    <td align="center" class="font_white">
                        <s:submit value="保存" cssClass="button" onclick="return jiaoyan()"/>&nbsp;&nbsp;
                            <%--<input type="button" value="返回" onclick="history.go(-1)" class="button"/>--%>
                    </td>
                </tr>
            </table>
        </s:form>
    </div>
</div>
<script type="text/javascript">
    function jiaoyanyx(){
        $.ajax({
            url: '${ctx}/appuser/phoneMerchat/getUserByPhone.do',
            type: 'post',
            dataType: 'json',
            data: {"phone" : $("#loginId").val()},
            success: function(rst, textStatus){
                if(rst.msg == 'success'){
                    $("#saveZzh").submit();
                    return true;
                }else{
                    $("#errorMsg").html("<font color='red'>用户名已经存在</font>");
                    return false;
                }
            }
        });

    }
    function xyjiaoyan(){
        if(isEmpty($("#fillMoneyxyje").val())){
            return false;
        }else{
            return true;
        }
    }
    function jiaoyan(){
        if(isEmpty($("#fillMoneyAccount").val())){
            return false;
        }else{
            return true;
        }
    }
    function goToMyoOrder(id){
        window.location.href="${ctx}/salesOrder/index.do?userId="+id;
    }
    function showAccountDiv(){
        $("#phone").val("");
        $("#name").val("");
        $("#password").val("");
        $("#loginId").val("");
        $("#folk").val("");
        $("#address").val("");
        $("#allMoney").val("");
        $("#incomeAll").val("");
        $("#childDiv").show();
        $("#fillJeDiv").hide();
    }
    function showXyJeDiv(){
        $("#fillJeDiv").show();
        $("#childDiv").hide();
    }
    function fillAccount(){
        $("#detailSave").hide();
        $("#fillJeDiv").hide();
        $("#childDiv").hide();
        $("#fillAccount").show();
    }
    var array = new Array();
    var nameArray = new Array();
    var map;
    var addr;
    var data=new Object();
    var address;
    $(function(){
//		initMerSort();
        getAppUserNotsq1();
        if("${type}"!="edit"){
            getAppUserNotsq();
        }else{
            initMerSort("${obj.productSort.id}");
            $("#proSortId").val("${obj.productSort.id}");
            $("#appId").hide();
            $("#reg").hide();
        }
        var id=$.trim($("#uId").val());
        if(isEmpty(id)&&"${loginUser.type}"=="admin"){
            addr="北京";
        }else if(isEmpty(id)&&"${loginUser.type}"=="agent"){
            addr="${loginUser.region.name}";
        }
        else{
            addr="${model.region.name}";
            data.locX="${model.locX}";
            data.locY="${model.locY}";
            address="${model.address}";
        }
        showMap(addr);
        if(!isEmpty(id)){
            map.addOverlay(getMarker(data,"${ctx}/images/amol/base/dqsz.gif",address));
        }
        if(!isEmpty("${model.merSortStr}"&&!isEmpty("${model.merSortNameStr}"))) {
            initArrayAndSort();
        }
    });
    function initArrayAndSort(){
        var merSortStr = "";
        var merSortName = "";
        array = "${model.merSortStr}".split(",");
        nameArray = "${model.merSortNameStr}".split(",");
        for(var i = 0; i<nameArray.length;i++){
            merSortName = merSortName + nameArray[i]+",";
        }
        $("#merSortNameStr").val(merSortName.substring(0,merSortName.length-1));
        for(var i = 0; i<array.length;i++){
            merSortStr = merSortStr + array[i]+",";
        }
        $("#merSortStr").val(merSortStr.substring(0,merSortStr.length-1));
        for(var i=0;i<array.length;i++){
            $("#"+array[i]).attr("checked",true);
        }
    }
    function isEmpty(_value){
        return ((_value == undefined || _value == null || _value == "" || _value == "undefined") ? true : false);
    }
    //判断是否为空，为空返回 true 不为空返回false
    function showMap(addr){
        map = new BMap.Map("Map");
        if(isEmpty(addr)){
            map.centerAndZoom(new BMap.Point(data.locX, data.locY), 11);}
        else{
            map.centerAndZoom(addr, 11);
        }
        map.enableScrollWheelZoom();
        map.addEventListener("click", showInfo);
    }
    function getMarker(data, img, message){
        var point = new BMap.Point(data.locX, data.locY);
        var icon = new BMap.Icon(img, new BMap.Size(32, 32));
        var label = new BMap.Label("商家地点", { offset: new BMap.Size(30, -15) });
        var marker = new BMap.Marker(point, { icon: icon });
        var opts = new InfoWindow("商家地点");
        var infoWindow = new BMap.InfoWindow(message, opts);  // 创建信息窗口对象
        marker.setLabel(label);
        marker.addEventListener("click", function () {
            map.openInfoWindow(infoWindow, point); //开启信息窗口
        });
        return marker;
    }
    function InfoWindow(title) {
        this.width = 220;
        this.height = 90;
        this.title = title;
        this.enableMessage = false; //设置允许信息窗发送短息
    }
    function showInfo(e){
        var x = e.point.lng;
        var y = e.point.lat;
        map.clearOverlays();
        var point=new BMap.Point(x, y);
        var marker=new BMap.Marker(point);
        map.addOverlay(marker);
        var gc = new BMap.Geocoder();
        gc.getLocation(point,function(rs){
            var infoWindowTitle = '<div style="font-weight:bold;color:#CE5521;font-size:14px">位置信息</div>';
            var infoWindowHtml = [];
            infoWindowHtml.push("<div style='font-size:13px;font-family:\"Arial\",\"Tahoma\",\"微软雅黑\",\"雅黑\";'>地址：" + rs.address + "</br>");
            infoWindowHtml.push("坐标：[" + x + "," + y +"]</br></br>");
            infoWindowHtml.push("<input type='button' value='使用此位置' class='button' style='width:100%;' onclick='retLocation(\"" +rs.address +"\"," +x+","+y +")' /></div>");
            var infoWindow = new BMap.InfoWindow(infoWindowHtml.join(""), { title: infoWindowTitle});
            marker.openInfoWindow(infoWindow);
        });
    }

    function retLocation(address,locX,locY){
        $("#address").val(address);
        $("#locX").val(locX);
        $("#locY").val(locY);
    }

    function updateImageUI(){
        $("#imageId").toggle();
        //var imageIdObj = document.getElementById("imageId");
        //imageIdObj.innerHTML = "<input id='fileId' type='file' name='attch' />";
    }
    $(document).ready(function() {
        $("#save").validate();
    });
    var uid = $("#uId").val();
    if (uid != null && uid.length > 0){
        defPass = "*********";
        $('#pwd').val(defPass);
        $('#repwd').val(defPass);
    }
    /*
     Ext.onReady(function() {
     var merstree = new ProsortTree({
     el : 'comboxmerWithTree',
     target : 'prosortId',
     //emptyText : '选择商家类型',
     url : '${ctx}/base/prosort/prosortTree.do?status=1&type=2',
     defValue : {
     id : '${model.productSort.id}',
     text : '${model.productSort.name}'
     }

     });
     merstree.init();
     });
     Ext.onReady(function() {
     var bankTree = new BanksortTree({
     el : 'comboxmerBankWithTree',
     target : 'bankSortId',
     //emptyText : '选择商家类型',
     url : '${ctx}/base/prosort/prosortTree.do?status=1&type=3',
     defValue : {
     id : '${model.bankSort.id}',
     text : '${model.bankSort.name}'
     }

     });
     bankTree.init();
     });
     */
    //查找注册的未申请开店的app用户
    function getAppUserNotsq(){
        $.ajax({
            url:'${ctx}/appuser/manager/getAppList.do',
            type:'post',
            dataType:'json',
            async : false, //默认为true 异步
            error:function(){
//                alert('error');
            },
            success:function(data){
                //shopUser
                $("#shopUser").append("<option value=''>--请选择--</option>");
                for(var i=0;i<data.length;i++){
                    $("#shopUser").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
                }
                $("#inputShowAPP").attr("disabled","disabled");
                $("#regap1").hide();
                $("#reg1").hide();
            }
        });
    }
    function getAppUserNotsq1(){
        $.ajax({
            url:'${ctx}/base/prosort/prosortTree.do?status=1&type=2',
            type:'post',
            dataType:'json',
            async : false, //默认为true 异步
            error:function(){
//                alert('error');
            },
            success:function(data){
                //shopUser
                $("#proSortId").append("<option value=''>--请选择--</option>");
                for(var i=0;i<data.length;i++){
                    $("#proSortId").append("<option value='"+data[i].id+"' onclick='initMerSort("+data[i].id+")'>"+data[i].text+"</option>");
                }
            }
        });
    }
    function initMerSort(parentId){
        $.ajax({
            url:'${ctx}/base/prosort/getByParentId.do?parentId='+parentId,
            type:'post',
            dataType:'json',
            async : false, //默认为true 异步
            error:function(){
                alert('error');
            },
            success:function(data){
                array = new Array();
                nameArray = new Array();
                $("#merkindsSpan").html("");
                if(data!=null)
                {
                    for(var i = 0;i<data.length;i++){
                        $("#merkindsSpan").append(data[i].name+"<input type='checkbox' value='"+data[i].name+"' onclick='bqStr(this.id)' id="+data[i].id+">");
                    }
                }
            }
        });
    }
    function bqStr(id){
        var merSortStr = "";
        var merSortName = "";
        if($("#"+id).is(":checked")){
            array.push(id);
            nameArray.push($("#"+id).val());
        }else{
            array.remove(id);
            nameArray.remove($("#"+id).val());
        }
        for(var i = 0; i<nameArray.length;i++){
            merSortName = merSortName + nameArray[i]+",";
        }
        $("#merSortNameStr").val(merSortName.substring(0,merSortName.length-1));
        for(var i = 0; i<array.length;i++){
            merSortStr = merSortStr + array[i]+",";
        }
        $("#merSortStr").val(merSortStr.substring(0,merSortStr.length-1));
    }
    Ext.onReady(function() {
        var region = '${loginUser.region }';
        var paramet;
        if(null != region && "" != region){
            paramet = '${loginUser.region.id }';
        }else{
            paramet = null;
        }
        var pstree = new RegionTree({
            el : 'comboxregionWithTree',
            target : 'regionId',
            //emptyText : '选择地区',
            comboxWidth : 260,
            treeWidth : 255,
            url : '${ctx}/admin/region/regionTree.do?regionId='+paramet,
            defValue : {id:'${model.region.id}',text:'${model.region.name}'}
        });
        pstree.init();

    });
    if("${type != 'edit'}"){
        function checkApp(){
            if(isEmpty($("#shopUser").val())){
                alert("请选择申请开店的app用户");
                $("#shopUser").focus();
                return false;
            }
        }
    }
    $(function() {
        $.validator.addMethod("loginId", function(value, element){
            var reg = new RegExp("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9_\.\-])*$");
            if (!reg.test(value)){//首先判断非法字符
                $('#loginIdMsg').html('用户名包含非法字符!');
                return false;
            }else {//判断用户名是否存在
                $('#loginIdMsg').html('');
                var exist;
                $.ajax({
                    url: '${ctx}/security/user/checkName.do',
                    type: 'post',
                    async : false,
                    dataType: 'json',
                    data: {"model.id" : $("#uId").val(),"model.loginId" : value},
                    success: function(rst, textStatus){
                        exist = rst.exist;
                        if (exist) {
                            $('#loginIdMsg').html('<b>'+value+'</b>已存在!');
                        }else {
                            $('#loginIdMsg').html('');
                        }
                    }
                });
                return !exist;
            }
            return true;
        },"");

        $.validator.addMethod("idCard", function(value, element) {
            if (value.length != 15 && value.length != 18){
                $("#idCardMsg").html('身份证位数错误' + value.length );
                return false;
            }else{
                $("#idCardMsg").html('');
            }
            return true;
        },"");
    });
</script>
</body>
</html>