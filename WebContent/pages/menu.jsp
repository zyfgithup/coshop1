<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.systop.amol.sales.SalesConstants,com.systop.amol.sales.utils.Payment,
	com.systop.core.Constants,com.systop.common.modules.security.user.model.User,
	com.systop.amol.user.AmolUserConstants,
	com.systop.common.modules.security.user.UserConstants" %>
<%@ page import="com.systop.common.modules.security.user.model.Permission" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%request.setAttribute("ctx", request.getContextPath()); %>
<script type="text/javascript">
	$(function(){
		var array = new Array();
		array = $("#resurceStr").val().split(",");
		for (var i=0;i<array.length;i++){
			$("#"+array[i]).show();
		}
	});
</script>
<input id="resurceStr" value="${sessionScope.resurceStr}">
<%@ taglib prefix="stc" uri="/systop/common" %>
<div id="menu" style="display:none">
	<div id="system">
		<div style="padding-left:5px;">         
			<div style="padding-top:2px;"  id="security/user/index.do">
				<img src="${ctx}/images/icons/user.gif" class="icon">
				<a href="${ctx}/security/user/index.do" target="main">区域管理员</a>
			</div>
			<div style="padding-top:2px;" style="display: none" id="userHistory/userHistoryList.do">
				<img src="${ctx}/images/icons/resource.gif" class="icon">
				<a href="${ctx}/userHistory/userHistoryList.do" target="main">登录记录</a>
			</div>								
		</div>
	</div>
	<div id="base">
		<div style="padding-left:5px;">
		    <stc:role ifAnyGranted="ROLE_ADMIN, ROLE_SYSTEM,ROLE_TOP_DEALER, ROLE_EMPLOYEE_SALE, ROLE_EMPLOYEE_SALE">
				<div style="padding-top:2px;display: none"  id="ssqsz">
					<img src="${ctx}/images/amol/base/dqsz.gif" class="icon">
					<a href="${ctx}/pages/admin/region/index.jsp" target="main">省市区设置</a>
				</div>
				<div style="padding-top:2px;display: none" id="gggl">
					<img src="${ctx}/images/exticons/list-items.gif" class="icon">
					<a href="${ctx}/indexImage/indexAdv.do" target="main">广告管理</a>
				</div>
				<div style="padding-top:2px;display: none" id="ggwgl">
					<img src="${ctx}/images/exticons/list-items.gif" class="icon">
					<a href="${ctx}/advpostion/index.do" target="main">广告位管理</a>
				</div>
				<div style="padding-top:2px;display: none" id="wzgl">
					<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
					<a href="${ctx}/base/jfpriceple/index.do" target="main">文章管理</a>
				</div>
				<div style="padding-top:2px;display: none" id="fxgz">
					<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
					<a href="${ctx}/merchant/fxgz/index.do" target="main">返现规则</a>
				</div>
				<%--<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/splx.gif" class="icon">
					<a href="${ctx}/databackup/all/index.do" target="main">数据备份</a>
				</div>--%>
				<div style="padding-top:2px;display: none" id="dplx">
					<img src="${ctx}/images/amol/base/splx.gif" class="icon">
					<a href="${ctx}/base/prosort/merTableIndex.do" target="main">店铺/二手车类型</a>
				</div>
				<div style="padding-top:2px;display: none" id="sjgl">
					<img src="${ctx}/images/amol/base/gysgl.gif" class="icon">
					<a href="${ctx}/user/agent/index.do" target="main">商家管理</a>
				</div>
				<div style="padding-top:2px;display: none" id="qbgl">
					<img src="${ctx}/images/amol/base/gysgl.gif" class="icon">
					<a href="${ctx}/user/agent/qbIndex.do" target="main">统计管理</a>
				</div>
			<%--	<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/zyzl.gif" class="icon">
					<a href="${ctx}/pages/amol/dept/index.jsp" target="main">部门设置</a>
				</div>--%>
				<div style="padding-top:2px;display: none"  id="yhgl">
					<img src="${ctx}/images/amol/base/zyzl.gif" class="icon">
					<a href="${ctx}/base/employee/index.do" target="main">用户管理</a>
				</div>
				<div style="padding-top:2px;display: none"  id="jsgl">
					<img src="${ctx}/images/amol/base/zyzl.gif" class="icon">
					<a href="${ctx}/security/role/index.do" target="main">角色管理</a>
				</div>
				<div style="padding-top:2px;display: none"  id="qxgl">
					<img src="${ctx}/images/amol/base/zyzl.gif" class="icon">
					<a href="${ctx}/security/permission/index.do" target="main">权限管理</a>
				</div>
				<div style="padding-top:2px;display: none"  id="zygl">
					<img src="${ctx}/images/amol/base/zyzl.gif" class="icon">
					<a href="${ctx}/security/resource/index.do" target="main">资源管理</a>
				</div>
				<div style="padding-top:2px;display: none" id="hylb">
					<img src="${ctx}/images/amol/base/splx.gif" class="icon">
					<a href="${ctx}/base/prosort/banksTableIndex.do" target="main">会员类型</a>
				</div>
					<div style="padding-top:2px;display: none" id="splb">
                        <img src="${ctx}/images/amol/base/splx.gif" class="icon">
                        <a href="${ctx}/pages/amol/base/prosort/index.jsp" target="main">商品类别</a>
                    </div>
                    <div style="padding-top:2px;display: none" id="jldw">
                        <img src="${ctx}/images/amol/base/zldw.gif" class="icon">
                        <a href="${ctx}/base/units/index.do" target="main">计量单位</a>
                    </div>
				<div style="padding-top:2px;display: none" id="jlsz">
					<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
					<a href="${ctx}/distanceset/index.do" target="main">距离设置</a>
				</div>
                    <div style="padding-top:2px;display: none" id="spgl">
                        <img src="${ctx}/images/amol/base/spzl.gif" class="icon">
                        <a href="${ctx}/base/product/distributor/index.do" target="main">商品管理</a>
                    </div>
				<div style="padding-top:2px;display: none" id="fkxx">
					<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
					<a href="${ctx}/fanKui/index.do" target="main">反馈信息</a>
				</div>
				<%--
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/splx.gif" class="icon">
					<a href="${ctx}/base/expressCompany/index.do" target="main">快递公司</a>
				</div>--%>
				<%-- <div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/gysgl.gif" class="icon">
					<a href="${ctx}/base/supplier/index.do" target="main">供应商管理</a>
				</div>
				<div style="padding-top:2px">
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/zldw.gif" class="icon">
					<a href="${ctx}/base/txmoneyset/index.do" target="main">提现金额设置</a>
				</div>--%>
	 		   <%--
				<div style="padding-top:2px">
					<img src="${ctx}/images/exticons/list-items.gif" class="icon">
					<a href="${ctx}/pushMessage/index.do" target="main">推送</a>
				</div> --%>
				<%--
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
					<a href="${ctx}/base/product/productTemplate/index.do" target="main">公用图片<!-- 模板商品 --></a>
				</div> --%>
				
				<%-- <div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
					<a href="${ctx}/base/product/index.do" target="main">平台商品管理</a>
				</div>
				
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
					<a href="${ctx}/base/product/groupPurchase/index.do" target="main">团购商品管理</a>
				</div> --%>
				
				<%--<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/gysgl.gif" class="icon">
					<a href="${ctx}/user/agent/indexCun.do" target="main">商家管理</a>
				</div>
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
					<a href="${ctx}/base/product/distributor/tjindex.do" target="main">特价商品管理</a>
				</div>
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
					<a href="${ctx}/base/jfproduct/index.do" target="main">积分商品管理</a>
				</div> --%>
			</stc:role>
			<%--<div style="padding-top:2px">
				<img src="${ctx}/images/amol/base/gysgl.gif" class="icon">
				<a href="${ctx}/appuser/manager/index.do" target="main">APP用户</a>
			</div>--%>
			<!-- 区域负责人、商家 -->
				<%
					User sessionUser = (User)session.getAttribute("userInSession");
					if(null != sessionUser && AmolUserConstants.AGENT_LEVEL_COUNTY.equals(sessionUser.getFxsjb())){ 
				%>
					<div style="padding-top:2px;display: none" id="spgl1">
						<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
						<a href="${ctx}/base/product/distributor/managerIndexProductCun.do" target="main">商品管理</a>
					</div>
						<%--<div style="padding-top:2px">
							<img src="${ctx}/images/amol/base/gysgl.gif" class="icon">
							<a href="${ctx}/user/agent/index.do" target="main">商家管理</a>
						</div>--%>
						<%--<div style="padding-top:2px">
							<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
							<a href="${ctx}/base/product/distributor/indexPlatformProduct.do" target="main">平台商品</a>
						</div> --%>
						<%--
						<div style="padding-top:2px">
							<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
							<a href="${ctx}/base/product/distributor/tjindex.do" target="main">特价商品管理</a>
						</div>--%>
						<%-- <div style="padding-top:2px">
							<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
							<a href="${ctx}/base/product/groupPurchase/gpIndex.do" target="main">团购商品管理</a>
						</div> --%>
				<%}else if(null != sessionUser && AmolUserConstants.AGENT_LEVEL_VILLAGE.equals(sessionUser.getFxsjb())){ %>
						<div style="padding-top:2px;display: none" id="spgl2">
							<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
							<a href="${ctx}/base/product/distributor/myIndex.do" target="main">商品管理</a>
						</div>
						<div style="padding-top:2px">
							<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
							<a href="${ctx}/base/product/distributor/myTjIndex.do" target="main">特价商品管理</a>
						</div>
				<%} %>
				<%--<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/gysgl.gif" class="icon">
					<a href="${ctx}/user/agent/indexCun.do" target="main">分销商管理【村】</a>
				</div>
				 <div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
					<a href="${ctx}/base/jfproduct/index.do" target="main">积分商品管理</a>
				</div> --%>
					<%--  --%>
		</div>
	</div>
	<div id="jfsc">
		<div style="padding-left:5px;">    
			<div style="padding-top:2px">
			<img src="${ctx}/images/amol/jfsc/cgdd.gif" class="icon">
			<a href="${ctx}/base/jfproduct/index.do" target="main">积分商品管理</a>
			</div>
		</div>
	 </div>
	<div id="yygl">
			<div style="padding-left:5px;">
				<div style="padding-top:2px;display: none" id="hygl">
					<img src="${ctx}/images/amol/jfsc/cgdd.gif" class="icon">
					<a href="${ctx}/user/agent/getVipInfos.do" target="main">会员管理</a>
				</div>
				<div style="padding-top:2px;display: none" id="rzgl">
					<img src="${ctx}/images/amol/jfsc/cgdd.gif" class="icon">
					<a href="${ctx}/merchant/rezheng/index.do" target="main">认证管理</a>
				</div>
				<div style="padding-top:2px;display: none" id="ddgl">
					<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
					<a href="${ctx}/salesOrder/index.do" target="main">订单管理</a>
				</div>
				<div style="padding-top:2px;display: none" id="jyxx">
					<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
					<a href="${ctx}/salesOrder/indexJyXx.do" target="main">加油消息</a>
				</div>
				<div style="padding-top:2px;display: none" id="czdd">
					<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
					<a href="${ctx}/salesOrder/tgIndex.do?salesType=g" target="main">充值订单</a>
				</div>
				<div style="padding-top:2px;display: none" id="jysz">
					<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
					<a href="${ctx}/jiayouset/index.do" target="main">加油设置</a>
				</div>
				<div style="padding-top:2px;display: none" id="xxlb">
					<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
					<a href="${ctx}/user/agent/toInforList.do" target="main">消息列表</a>
				</div>
				<div style="padding-top:2px;display: none" id="escxx">
					<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
					<a href="${ctx}/cheliang/index.do" target="main">二手车信息</a>
				</div>
				<div style="padding-top:2px;display: none" id="zpxx">
				<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
				<a href="${ctx}/web/zhaopin/index.do" target="main">招聘信息</a>
			</div>
				<div style="padding-top:2px;display: none" id="ddtj">
					<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
					<a href="${ctx}/salesOrder/indextj.do" target="main">订单统计</a>
				</div>
				<%--<div style="padding-top:2px">
					<img src="${ctx}/images/amol/jfsc/cgdd.gif" class="icon">
					<a href="${ctx}/base/yyyrecords/index.do" target="main">摇一摇记录查询</a>
				</div>--%>
			</div>
	 </div>
	<div id="xsgl">
		<div style="overflow:auto;overflow-x:hidden;" >
			<stc:role ifAnyGranted="ROLE_TOP_DEALER, ROLE_END_DEALER, ROLE_EMPLOYEE_SALE">
				<%--	<div style="padding-top:2px">
                        <img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
                        <a href="${ctx}/salesEnquiries/index.do?listType=<%=SalesConstants.ORDERS %>&payment=<%=Payment.CASH.toString() %>" target="main">销售查询【现金】</a>
                    </div>--%>
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
					
					<% 
						if(null != sessionUser && (UserConstants.USER_TYPE_SYS.equals(sessionUser.getIsSys()) || AmolUserConstants.AGENT_LEVEL_COUNTY.equals(sessionUser.getFxsjb()))){ 
					%>
							<a href="${ctx}/salesOrder/ptIndex.do?pment=WXPAY&payment=<%=Payment.ALIPAY.toString() %>" target="main">销售订单【网银】</a>
					
					<%  }else if(null != sessionUser && AmolUserConstants.AGENT_LEVEL_VILLAGE.equals(sessionUser.getFxsjb())){ %>
							<a href="${ctx}/salesOrder/index.do?pment=WXPAY&payment=<%=Payment.ALIPAY.toString() %>" target="main">销售订单【网银】</a>
					
					<%  } %>
					
				</div>
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/xsgl/xsth.gif" class="icon">
					<% 
						if(null != sessionUser && (UserConstants.USER_TYPE_SYS.equals(sessionUser.getIsSys()) || AmolUserConstants.AGENT_LEVEL_COUNTY.equals(sessionUser.getFxsjb()))){ 
					%>
							<a href="${ctx}/salesReturns/returnSalseManagerIndex.do" target="main">销售退货【网银】</a>
					<%  }else if(null != sessionUser && AmolUserConstants.AGENT_LEVEL_VILLAGE.equals(sessionUser.getFxsjb())){ %>
							<a href="${ctx}/salesReturns/cardIndex.do" target="main">销售退货【网银】</a>
					<%  } %>
				</div> 
				<%--<div style="padding-top:2px">
					<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
					<a href="${ctx}/salesEnquiries/cardIndex.do?pment=WXPAY&listType=<%=SalesConstants.ORDERS %>&payment=<%=Payment.ALIPAY.toString() %>" target="main">销售查询【网银】</a>
				</div>--%>
				<% if(null != sessionUser && (UserConstants.USER_TYPE_SYS.equals(sessionUser.getIsSys()) || AmolUserConstants.AGENT_LEVEL_COUNTY.equals(sessionUser.getFxsjb()))){ %>
				<div style="padding-top:2px;">
					<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
					<% 
						if(null != sessionUser && UserConstants.USER_TYPE_SYS.equals(sessionUser.getIsSys())){ 
					%>
							<a href="${ctx}/salesOrder/ptIndex.do?salesType=jfSale %>" target="main">销售订单【积分】</a>
					
					<%  }else if(null != sessionUser && AmolUserConstants.AGENT_LEVEL_COUNTY.equals(sessionUser.getFxsjb())){ %>
							<a href="${ctx}/salesOrder/index.do?salesType=jfSale" target="main">销售订单【积分】</a>
					
					<%  } %>
				</div>
				<% } %>
				<!-- div style="padding-top:2px">
					<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
					<a href="${ctx}/salesSummary/indexDistributor.do" target="main">分销商销售</a>
				</div> -->
				<%--<div style="padding-top:2px">
					<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
					<a href="${ctx}/pages/amol/sales/salesSummary/myselector.jsp" target="main">销售汇总</a>
				</div>--%>
			</stc:role>
			<stc:role ifAnyGranted="ROLE_ADMIN,ROLE_TOP_DEALER">
				<!-- <div style="padding-top:2px">
					<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
					<a href="${ctx}/jfSalesOrder/index.do" target="main">积分汇总</a>
				</div> -->
				<%-- <div style="padding-top:2px">
					<img src="${ctx}/images/amol/xsgl/xsth.gif" class="icon">
					<a href="${ctx}/salesReturns/groupCardIndex.do?salesType=g" target="main">销售退货【团购】</a>
				</div>  --%>
				<%-- <div style="padding-top:2px">
					<img src="${ctx}/images/amol/finance/jsyf.png" class="icon"> <a
					href="${ctx}/salesEnquiries/groupCardIndex.do?salesType=g&listType=<%=SalesConstants.ORDERS %>&payment=<%=Payment.ALIPAY.toString() %>"
					target="main">销售查询【团购】</a>
			</div> --%>
					<%-- <div style="padding-top:2px">
					<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
					<a href="${ctx}/salesOrder/groupSum.do" target="main">团购金额统计</a>
				</div>
				</stc:role>
				<stc:role ifAnyGranted="ROLE_ADMIN">
					<div style="padding-top:2px">
					<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
					<a href="${ctx}/salesOrder/moneySum.do" target="main">消费金额统计</a>
				</div>
			<div style="padding-top:2px">
					<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
					<a href="${ctx}/pages/amol/sales/salesSummary/selector.jsp" target="main">佣金分配统计</a>
				</div> --%>
			</stc:role>
		</div>
	</div>
	<%--
	
	<div id="finance">
		<div style="overflow:auto;overflow-x:hidden;" >
		
			<stc:role ifAnyGranted="ROLE_TOP_DEALER, ROLE_END_DEALER, ROLE_EMPLOYEE_SALE">
					
					<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_EMPLOYEE_SALE">
						<div style="padding-top:2px">
							<img src="${ctx}/images/amol/finance/zjzc.gif" class="icon">
							<a href="${ctx}/tiXianManager/index.do" target="main">
								提现记录
							</a>
						</div>
					</stc:role>
					
					<stc:role ifNotGranted="ROLE_ADMIN, ROLE_EMPLOYEE_SALE">
							<!-- <div style="padding-top:2px;display:none;">
								<img src="${ctx}/images/amol/finance/zjzc.gif" class="icon">
								<a href="${ctx}/merchantTiXian/edit.do" target="main">
									申请提现
								</a>
							</div> -->
							<div style="padding-top:2px">
								<img src="${ctx}/images/amol/finance/zjzc.gif" class="icon">
								<a href="${ctx}/merchantTiXian/index.do" target="main">
									提现记录
								</a>
							</div>
					</stc:role>
					
			</stc:role>
		 </div>
	</div>--%>
	
</div>
