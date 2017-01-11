<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.systop.amol.sales.SalesConstants" %>
<%request.setAttribute("ctx", request.getContextPath()); %>
<%@ taglib prefix="stc" uri="/systop/common" %>
<div id="menu" style="display:none">
	<div id="system">
		<div style="padding-left:5px;">         
			<div style="padding-top:2px">
				<img src="${ctx}/images/icons/user.gif" class="icon">
				<a href="${ctx}/security/user/index.do" target="main">用户管理</a>
			</div>
			
			<stc:role ifAnyGranted="ROLE_ADMIN">   
			<div style="padding-top:2px">
				<img src="${ctx}/images/icons/role.gif" class="icon">
				<a href="${ctx}/security/role/index.do" target="main">角色管理</a>
			</div>
			<div style="padding-top:2px">
				<img src="${ctx}/images/icons/authority.gif" class="icon">
				<a href="${ctx}/security/permission/index.do" target="main">权限管理</a>
			</div>
			<div style="padding-top:2px"> 
				<img src="${ctx}/images/icons/resource.gif" class="icon">
				<a href="${ctx}/security/resource/index.do" target="main">资源管理</a>
			</div>
	        </stc:role>		
			<div style="padding-top:2px;"> 
				<img src="${ctx}/images/icons/resource.gif" class="icon">
				<a href="${ctx}/userHistory/userHistoryList.do" target="main">登录记录</a>
			</div>								
		</div>
	</div>
		
	<div id="base">
		<div style="padding-left:5px;">
		    
		    <stc:role ifAnyGranted="ROLE_ADMIN, ROLE_SYSTEM, ROLE_EMPLOYEE_SALE, ROLE_EMPLOYEE_SALE">
				<div style="padding-top:2px;">
					<img src="${ctx}/images/amol/base/dqsz.gif" class="icon">
					<a href="${ctx}/pages/admin/region/index.jsp" target="main">地区设置</a>
				</div>  
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/splx.gif" class="icon">
					<a href="${ctx}/pages/amol/base/prosort/index.jsp" target="main">商品类型</a>
				</div> 
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/zldw.gif" class="icon">
					<a href="${ctx}/base/units/index.do" target="main">计量单位</a>
				</div>
	 		    <div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/zyzl.gif" class="icon">
					<a href="${ctx}/pages/amol/dept/index.jsp" target="main">部门设置</a>
				</div>	
	  		    <div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/zyzl.gif" class="icon">
					<a href="${ctx}/base/employee/index.do" target="main">职员管理</a>
				</div>
				<div style="padding-top:2px">
					<img src="${ctx}/images/exticons/list-items.gif" class="icon">
					<a href="${ctx}/pushMessage/index.do" target="main">推送</a>
				</div>
				<div style="padding-top:2px">
					<img src="${ctx}/images/exticons/list-items.gif" class="icon">
					<a href="${ctx}/indexImage/index.do" target="main">首页图片轮播</a>
				</div>
				
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
					<a href="${ctx}/base/product/productTemplate/index.do" target="main">模板商品</a>
				</div> 
				
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
					<a href="${ctx}/base/product/index.do" target="main">平台商品管理</a>
				</div> 	
			</stc:role>
		    
		    <stc:role ifAnyGranted="ROLE_TOP_DEALER">
		    	<stc:role ifNotGranted="ROLE_ADMIN, ROLE_SYSTEM">
					<div style="padding-top:2px">
						<img src="${ctx}/images/amol/base/gysgl.gif" class="icon">
						<a href="${ctx}/user/agent/index.do" target="main">分销商管理【村】</a>
					</div>
				</stc:role>
			</stc:role>
			
		    <div style="padding-top:2px">
				<img src="${ctx}/images/amol/base/gysgl.gif" class="icon">
				<a href="${ctx}/base/supplier/index.do" target="main">供应商管理</a>
			</div>	
			
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
				<a href="${ctx}/base/product/distributor/indexPlatformProduct.do" target="main">平台商品</a>
			</div> 
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
				<a href="${ctx}/base/product/distributor/index.do" target="main">自营商品</a>
			</div>
			
			
		</div>
	</div>

	
	<div id="xsgl" >
		<div style="overflow:auto;overflow-x:hidden;" >
		
			<stc:role ifAnyGranted="ROLE_TOP_DEALER, ROLE_END_DEALER, ROLE_EMPLOYEE_SALE">
			    <div style="padding-top:2px;">
					<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
					<a href="${ctx}/salesOrder/index.do" target="main">销售订单【现金】</a>
				</div>
				
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/xsgl/xsth.gif" class="icon">
					<a href="${ctx}/salesReturns/index.do" target="main">销售退货【现金】</a>
				</div> 
				
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
					<a href="${ctx}/salesEnquiries/index.do?listType=<%=SalesConstants.ORDERS %>" target="main">销售查询【现金】</a>
				</div>
				
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
					<a href="${ctx}/salesOrder/cardSalesOrder/index.do" target="main">销售订单【网银】</a>
				</div>
				
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/xsgl/xsth.gif" class="icon">
					<a href="${ctx}/salesReturns/cardIndex.do" target="main">销售退货【网银】</a>
				</div> 
				
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
					<a href="${ctx}/salesEnquiries/cardIndex.do?listType=<%=SalesConstants.ORDERS %>" target="main">销售查询【网银】</a>
				</div>
				
				<!-- div style="padding-top:2px">
					<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
					<a href="${ctx}/salesSummary/indexDistributor.do" target="main">分销商销售</a>
				</div> -->
				
				<div style="padding-top:2px">
					<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
					<a href="${ctx}/pages/amol/sales/salesSummary/selector.jsp" target="main">销售汇总</a>
				</div>
			
			</stc:role>
			
		</div>
	</div>
	
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
							<div style="padding-top:2px">
								<img src="${ctx}/images/amol/finance/zjzc.gif" class="icon">
								<a href="${ctx}/merchantTiXian/edit.do" target="main">
									申请提现
								</a>
							</div>
							<div style="padding-top:2px">
								<img src="${ctx}/images/amol/finance/zjzc.gif" class="icon">
								<a href="${ctx}/merchantTiXian/index.do" target="main">
									提现记录
								</a>
							</div>
					</stc:role>
					
			</stc:role>
			
			
		 </div>
	</div>
							
</div>
