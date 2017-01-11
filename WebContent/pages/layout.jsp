<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.systop.common.modules.security.user.UserUtil"%>
<%@page import="com.systop.common.modules.security.user.model.User,com.systop.common.modules.security.user.UserConstants"%>

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>

		<%@ include file="/common/taglibs.jsp"%>
<title>乾泰物流管理系统</title>
<%@include file="/common/meta.jsp"%>
<link href="${ctx}/pages/layout.css" type='text/css' rel='stylesheet'>
<%@include file="/common/extjs.jsp"%>
<script type="text/javascript" src="${ctx}/scripts/jquery/ui/jquery-ui-1.7.1.js"></script>
<script type="text/javascript" src="${ctx}/scripts/jquery/jgrowl/jquery.jgrowl_minimized.js"></script>
<script type="text/javascript" src="${ctx}/scripts/clock.js"></script>

<link rel="stylesheet" href="${ctx}/scripts/jquery/jgrowl/jquery.jgrowl.css" type="text/css"/>
<style type="text/css">
.userName{
	font-weight: bold;
	color: red;
}
.sysTitle{
	padding:5px 0px;
	font-family: 楷体_GB2312;
	font-weight: bold;
	font-size: 24px;
}
#top2_left { line-height: 25px; float: left; height: 25px; width: 550px; margin-left: 20px; font-weight: bold; color: #333; }
#top2_left span { color: #FFF; text-decoration: underline; }
#top2_right { float: right; height: 25px; width: 400px; line-height: 25px; }
#top_1 { height: 50px; background-image: url(${ctx}/images/amol/top_1_bg.gif); }
#top_2 { height: 25px; background-image: url(${ctx}/images/amol/top_2_bg.gif); line-height: 30px; }
#top1_left { height: 50px; width: 424px; float: left; }
#div_1_right { float: right; height: 50px; width: 360px; line-height: 50px; }
</style>

</head>
<body>
<script type="text/javascript">
	Ext.onReady(function() {
		Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
		var viewport = new Ext.Viewport( {
			layout : 'border',
			items : [ new Ext.BoxComponent( { // raw
						region : 'north',
						el : 'north',
						height : 75
					}), {
				region : 'west',
				id : 'west-panel',
				title : '&nbsp;',
				split : true,
				width : 155,
				minSize : 155,
				maxSize : 400,
				collapsible : true,
				margins : '0 0 0 5',
				layout : 'accordion',
				layoutConfig : {
					animate : true
				},
				items : [
				<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_SYSTEM, ROLE_TOP_DEALER, ROLE_END_DEALER, ROLE_EMPLOYEE_SALE">
				{
					title : '<span class="title">基础设置</span>',
					html : document.getElementById('base').innerHTML,
					border : false,
					iconCls : 'base'
				}
				</stc:role>
				<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_TOP_DEALER_GENERAL, ROLE_END_DEALER, ROLE_END_DEALER_GENERAL, ROLE_EMPLOYEE_SALE, ROLE_EMPLOYEE_STOCK, ROLE_EMPLOYEE_FINANCE">
					,
					{
						title : '<span class="title">业务管理</span>',
						html : document.getElementById('yygl').innerHTML,
						border : true,
						iconCls : 'xsgl'
					}
					/*,
				{
					title : '<span class="title">积分商城</span>',
					html : document.getElementById('jfsc').innerHTML,
					border : true,
					iconCls : 'xsgl'
				}*/
				/*,
				{
					title : '<span class="title">销售管理</span>',
					html : document.getElementById('xsgl').innerHTML,
					border : true,
					iconCls : 'xsgl'
				}*/
            /*  ,
				{
					title : '<span class="title">转账管理</span>',
					html : document.getElementById('finance').innerHTML,
					border : false,
					iconCls : 'finance'
				}*/
				</stc:role>

	            
	            <stc:role ifAnyGranted="ROLE_ADMIN, ROLE_SYSTEM">
	            ,
				{
					title : '<span class="title">系统设置</span>',
					html : document.getElementById('system').innerHTML,
					border : false,
					iconCls : 'system'
				}
				</stc:role>
				]
			},
			{
				region : 'center',
				contentEl : 'center',
				split : true,
				border : true,
				margins : '0 5 0 0'
			}]
		
		});
	});

</script>
<script type="text/javascript">
function Clock() {
	var date = new Date();
	this.year = date.getFullYear();
	this.month = date.getMonth() + 1;
	this.date = date.getDate();
	this.day = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")[date.getDay()];
	this.hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
	this.minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
	this.second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();

	this.toString = function() {
		return this.year + "年" + this.month + "月" + this.date + "日 " + this.hour + ":" + this.minute + ":" + this.second + " " + this.day; 
	};
	
	this.toSimpleDate = function() {
		return this.year + "-" + this.month + "-" + this.date;
	};
	
	this.toDetailDate = function() {
		return this.year + "-" + this.month + "-" + this.date + " " + this.hour + ":" + this.minute + ":" + this.second;
	};
	
	this.display = function(ele) {
		var clock = new Clock();
		ele.innerHTML = clock.toString();
		window.setTimeout(function() {clock.display(ele);}, 1000);
	};
}
</script>
<%@include file="/pages/menu.jsp"%>
<div id="west"></div>
<div id="north" align="left" style="margin:0px;">
<div id="top_1">
  <div id="top1_left"><img src="${ctx}/images/amol/logo.gif" width="424" height="50" /></div>
  <div id="div_1_right" align="right"><span id="clock"></span>&nbsp;&nbsp;</div>
</div>
<div id="top_2" >
  <div id="top2_left">
  	欢迎您：<span><stc:username></stc:username></span>
  <%--	<c:if test="${userInSession.fxsjb eq 'agent_level_county' }">
	  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;积分：<font color="orange"> <fmt:formatNumber value='${userInSession.dhIntegral }' pattern='#0'/>
  	</c:if>
  	<c:if test="${userInSession.fxsjb eq 'agent_level_village' }">
	  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;余额：<font color="orange"> <fmt:formatNumber value='${userInSession.allMoney }' pattern='#0.00'/>
  	</c:if>--%>
  </div>
  <div id="top2_right">
    <table width="226" border="0" align="right" cellpadding="0" cellspacing="0" style="margin-top: 5px;">
      <tr>
        <td width="23"><img src="${ctx}/images/amol/forumlink[1].gif" width="14" height="14" /></td>
        <td width="44"><a href="${ctx}/pages/main.jsp" target="main">首页</a></td>
        <td width="23"><img src="${ctx}/images/amol/online_admin[1].gif" width="16" height="16" /></td>
        <td width="62"><a href="${ctx}/pages/admin/security/user/user.jsp" target="main">个人信息</a></td>
        <td width="23"><img src="${ctx}/images/amol/lock_go.gif" width="16" height="16" /></td>
        <td width="51"><a href="${ctx}/j_acegi_logout" target="_self">注销</a></td>
      </tr>
    </table>
  </div>
</div>
</div>
<div id="center">
    <!-- 
	<iframe id="main" name="main" src="${ctx}/main/index.do"
		style="width: 100%; height: 96%; border: 0px;" frameborder="0">
	</iframe>
	-->
	<iframe id="main" name="main" src="${ctx}/pages/main.jsp" style="width: 100%; height: 96%; border: 0px;" frameborder="0"></iframe>
	<!-- 
	<iframe id="foot" name="foot" src="${ctx}/main/footIndex.do"
		style="width: 100%; height: 4%; border: 0px;" frameborder="0" scrolling="no">
	</iframe>
	 -->
</div>
<script type="text/javascript">
function checkCard(){
 	$.ajax({
 	    url: '${ctx}/card/grant/checkCard.do',
 	    type: 'post',
 	    dataType: 'json',
 	    error: function(){
 	    	//alert('check new msg error');
 	    },
 	    success: function(){
 	    	//alert('check');
 	    }
 	});
 }
window.setInterval("checkCard()",24*3600*1000);
checkCard();

var clock = new Clock();
clock.display(document.getElementById("clock"));

</script>
</body>
</html>
