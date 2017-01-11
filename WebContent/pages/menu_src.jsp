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
  
			<div style="padding-top:2px">	
				<img src="${ctx}/images/icons/template.gif" class="icon">
                  <a href="${ctx}/register/regMemo/edit.do" target="main">注册说明</a>
			</div>				
			<div style="padding-top:2px;"> 
				<img src="${ctx}/images/icons/resource.gif" class="icon">
				<a href="${ctx}/userHistory/userHistoryList.do" target="main">登录记录</a>
			</div> 	
			<div style="padding-top:2px;"> 
				<img src="${ctx}/images/exticons/example.gif" class="icon">
				<a href="${ctx}/init/index.do" target="main"><font color="red">数据初始化</font></a>
			</div> 	
			<div style="padding-top:2px">
				<img src="${ctx}/images/exticons/list-items.gif" class="icon">
				<a href="${ctx}/databackup/all/index.do" target="main">数据备份</a>
			</div>											
		</div>
	</div>
	
	<div id="card">
		<div style="padding-left:5px;">
		    <div style="padding-top:2px;">
				<img src="${ctx}/images/amol/card.png" class="icon">
				<a href="${ctx}/card/index.do" target="main">卡的管理</a>
			</div>   															       
		    <div style="padding-top:2px">
				<img src="${ctx}/images/amol/card.png" class="icon">
				<a href="${ctx}/card/grant/index.do" target="main">发卡管理</a>
			</div>  
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/zjlx.png" class="icon">
				<a href="${ctx}/card/up/index.do" target="main">充值管理</a>
			</div>
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/card/grant/indexSales.do" target="main">消费查询</a>
				<!-- ${ctx}/card/spend/index.do -->
			</div> 	
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/sales/cardSales/queryBankIndex.do" target="main">应收查询</a>
			</div> 															       
		</div>
	</div>
	
	<div id="supplier">
		<div style="padding-left:5px;">
		    <div style="padding-top:2px;">
				<img src="${ctx}/images/amol/cggl/cgdd.gif" class="icon">
				<a href="${ctx}/purchase/selectAll/suplierindex.do" target="main">采购查询</a>
			</div>  			 		
		    <div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/barcode/index.do" target="main">货品流向</a>
			</div>  
		    <div style="padding-top:2px">
				<img src="${ctx}/images/amol/kcgl/kcpd.gif" class="icon">
				<a href="${ctx}/stock/stockIndex.do" target="main">库存状态</a>
			</div>  																			       														       
		</div>
	</div>
	
	<div id="base">
		<div style="padding-left:5px;">
		    <div style="padding-top:2px;">
				<img src="${ctx}/images/amol/base/dqsz.gif" class="icon">
				<a href="${ctx}/pages/admin/region/index.jsp" target="main">地区设置</a>
			</div>  
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/base/gysgl.gif" class="icon">
				<a href="${ctx}/user/agent/index.do" target="main">分销商管理</a>
			</div> 	
			
			<div style="padding-top:2px;">
				<img src="${ctx}/images/amol/base/khgl.gif" class="icon">
				<a href="${ctx}/base/customer/index.do" target="main">客户管理</a>
			</div>  
		    <div style="padding-top:2px">
				<img src="${ctx}/images/amol/base/gysgl.gif" class="icon">
				<a href="${ctx}/base/supplier/index.do" target="main">供应商管理</a>
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
				<img src="${ctx}/images/amol/base/spzl.gif" class="icon">
				<a href="${ctx}/base/product/index.do" target="main">商品资料</a>
			</div> 	
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER">
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/base/cksz.gif" class="icon">
				<a href="${ctx}/base/storage/index.do" target="main">仓库设置</a>
			</div> 
			</stc:role>
			
 		    <div style="padding-top:2px">
				<img src="${ctx}/images/amol/base/zyzl.gif" class="icon">
				<a href="${ctx}/pages/amol/dept/index.jsp" target="main">部门设置</a>
			</div>	
			
  		    <div style="padding-top:2px">
				<img src="${ctx}/images/amol/base/zyzl.gif" class="icon">
				<a href="${ctx}/base/employee/index.do" target="main">职员资料</a>
			</div>			
										 																			       
		</div>
	</div>

	<div id="cggl">
		<div style="padding-left:5px;">
		    <stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_EMPLOYEE_PURCHASE">  
		    <div style="padding-top:2px;">
				<img src="${ctx}/images/amol/cggl/cgdd.gif" class="icon">
				<a href="${ctx}/purchase/order/index.do" target="main">采购订单</a>
			</div>   
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_EMPLOYEE_STOCK">  
		    <div style="padding-top:2px">
				<img src="${ctx}/images/amol/cggl/cgrk.gif" class="icon">
				<a href="${ctx}/purchase/index.do" target="main">采购入库</a>
			</div>
		    <div style="padding-top:2px">
				<img src="${ctx}/images/amol/cggl/cgth.gif" class="icon">
				<a href="${ctx}/purchase/returns/index.do" target="main">采购退货</a>
			</div>  
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_EMPLOYEE_FINANCE">  
		    <div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/purchase/paytotal/index.do" target="main">应付明细</a>
			</div>  
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/purchase/payable/index.do" target="main">应付账款</a>
			</div>		
		    <div style="padding-top:2px">
				<img src="${ctx}/images/amol/cggl/yfkd.gif" class="icon">
				<a href="${ctx}/purchase/pay/index.do"" target="main">采购付款</a>
			</div>
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_EMPLOYEE_PURCHASE, ROLE_EMPLOYEE_STOCK">    		
		    <div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/purchase/selectAll/index.do" target="main">采购查询</a>
			</div> 
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/pages/amol/purchase/selectAll/selector.jsp" target="main">采购汇总</a>
			</div> 		
			</stc:role>	
									  																		       
		</div>
	</div>
	
	<div id="xsgl" >
		<div style="overflow:auto;overflow-x:hidden;" >
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_TOP_DEALER_GENERAL, ROLE_EMPLOYEE_SALE">   
		    <div style="padding-top:2px;">
				<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
				<a href="${ctx}/salesOrder/index.do" target="main">销售订单【现金】</a>
			</div>
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_TOP_DEALER_GENERAL, ROLE_EMPLOYEE_STOCK">   
		    <div style="padding-top:2px">
				<img src="${ctx}/images/amol/xsgl/xsck.gif" class="icon">
				<a href="${ctx}/sales/index.do" target="main">销售出库【现金】</a>
			</div>
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/xsgl/xsth.gif" class="icon">
				<a href="${ctx}/salesReturns/index.do" target="main">销售退货【现金】</a>
			</div> 
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_TOP_DEALER_GENERAL, ROLE_EMPLOYEE_FINANCE">   
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/sales/receiveAble.do" target="main">应收账款【现金】</a>
			</div>
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/xsgl/yskd.gif" class="icon">
				<a href="${ctx}/receive/index.do" target="main">销售回款【现金】</a>
			</div>
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_TOP_DEALER_GENERAL, ROLE_EMPLOYEE_SALE, ROLE_EMPLOYEE_STOCK">   
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/salesEnquiries/index.do?listType=<%=SalesConstants.ORDERS %>" target="main">销售查询【现金】</a>
			</div>
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_TOP_DEALER_GENERAL, ROLE_END_DEALER, ROLE_END_DEALER_GENERAL, ROLE_EMPLOYEE_SALE">
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/xsgl/xsdd.gif" class="icon">
				<a href="${ctx}/salesOrder/cardSalesOrder/index.do" target="main">销售订单【卡】</a>
			</div>
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_TOP_DEALER_GENERAL, ROLE_END_DEALER, ROLE_END_DEALER_GENERAL, ROLE_EMPLOYEE_STOCK">
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/xsgl/xsck.gif" class="icon">
				<a href="${ctx}/sales/cardSales/cardIndex.do" target="main">销售出库【卡】</a>
			</div>
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/xsgl/xsth.gif" class="icon">
				<a href="${ctx}/salesReturns/cardIndex.do" target="main">销售退货【卡】</a>
			</div> 
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_TOP_DEALER_GENERAL, ROLE_END_DEALER, ROLE_END_DEALER_GENERAL, ROLE_EMPLOYEE_FINANCE">
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/sales/receiveAbleCard.do" target="main">应收账款【卡】</a>
			</div>
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_TOP_DEALER_GENERAL, ROLE_EMPLOYEE_FINANCE">
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/xsgl/yskd.gif" class="icon">
				<a href="${ctx}/receive/cardIndex.do" target="main">销售回款【卡】</a>
			</div>
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_TOP_DEALER_GENERAL, ROLE_END_DEALER, ROLE_END_DEALER_GENERAL, ROLE_EMPLOYEE_SALE, ROLE_EMPLOYEE_STOCK,">
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/salesEnquiries/cardIndex.do?listType=<%=SalesConstants.ORDERS %>" target="main">销售查询【卡】</a>
			</div>
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_TOP_DEALER_GENERAL, ROLE_END_DEALER, ROLE_END_DEALER_GENERAL, ROLE_EMPLOYEE_SALE, ROLE_EMPLOYEE_STOCK, ROLE_EMPLOYEE_FINANCE">
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/card/grant/indexSales.do" target="main">消费查询【卡】</a>
				<!-- /card/spend/index.do?jxs='jxs' -->
			</div>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_TOP_DEALER_GENERAL, ROLE_EMPLOYEE_FINANCE">
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/salesSummary/indexDistributor.do" target="main">分销商销售【卡】</a>
			</div>
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER, ROLE_TOP_DEALER_GENERAL, ROLE_END_DEALER, ROLE_END_DEALER_GENERAL">
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/pages/amol/sales/salesSummary/selector.jsp" target="main">销售汇总</a>
			</div>
			</stc:role>
			
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">
				<a href="${ctx}/barcode/indexADealer.do" target="main">货品流向</a>
			</div> 
			</stc:role>
			
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_END_DEALER">
			<div>
				<img src="${ctx}/images/amol/kcgl/kcpd.gif" class="icon">  									  									  																		       
				<a href="${ctx}/stock/salesStockIndex.do" target="main">分销商即时库存</a>
			</div>
			</stc:role>
		</div>
	</div>
	<div id="kcgl">
		<div style="padding-left:5px;">
		    <div style="padding-top:2px;">
				<img src="${ctx}/images/amol/kcgl/kcpd.gif" class="icon">
				<a href="${ctx}/stock/check/index.do" target="main">库存盘点</a>
			</div>  					
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/kcgl/kcpd.gif" class="icon">
				<a href="${ctx}/stock/awoke/index.do" target="main">库存提醒</a>
			</div>
			<div>
				<img src="${ctx}/images/amol/kcgl/kcpd.gif" class="icon">  									  									  																		       
				<a href="${ctx}/stock/index.do" target="main">即时库存</a>
			</div>  									  																		       
			<div>
				<img src="${ctx}/images/amol/kcgl/kcpd.gif" class="icon">  									  									  																		       
				<a href="${ctx}/stock/trac/index.do" target="main">库存调拨</a>
			</div>
			<div>
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">  									  									  																		       
				<a href="${ctx}/stock/check/checkIndex.do" target="main">库存盘点查询</a>
			</div>
			<div>
				<img src="${ctx}/images/amol/finance/jsyf.png" class="icon">  									  									  																		       
				<a href="${ctx}/stock/trac/tracIndex.do" target="main">库存调拨查询</a>
			</div>  		
		</div>
	</div>

	<div id="finance">
		<div style="padding-left:5px;">
		    <stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER">
			<div style="padding-top:2px;">
				<img src="${ctx}/images/amol/finance/zjlx.png" class="icon">
				<a href="${ctx}/finance/fundsSort/index.do" target="main">资金类型</a>
			</div>
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/szlb.png" class="icon">
				<a href="${ctx}/finance/costSort/index.do" target="main">收支类别</a>
			</div>
			</stc:role>
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/zjsr.gif" class="icon">
				<a href="${ctx}/finance/cost/index.do?status=1" target="main">资金收入</a>
			</div>
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/zjzc.gif" class="icon">
				<a href="${ctx}/finance/cost/index.do?status=2" target="main">资金支出</a>
			</div>
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/finance/fyhz.png" class="icon">
				<a href="${ctx}/pages/amol/finance/costsort/selector.jsp" target="main">费用汇总</a>
			</div>							  																		       
		</div>
	</div>
	
	<div id="init">
		<div style="padding-left:5px;">
		    <stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER">		
			<div style="padding-top:2px;">
				<img src="${ctx}/images/amol/cggl/yfkd.gif" class="icon">
				<a href="${ctx}/purchase/payinit/init.do" target="main">期初应付</a>
			</div>
			</stc:role>
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/xsgl/yskd.gif" class="icon">
				<a href="${ctx}/receiveInit/init.do" target="main">期初应收</a>
			</div>
			<stc:role ifAnyGranted="ROLE_ADMIN, ROLE_TOP_DEALER">	
			<div style="padding-top:2px">
				<img src="${ctx}/images/amol/kcgl/kcpd.gif" class="icon">
				<a href="${ctx}/stock/init/init.do" target="main">期初库存</a>
			</div>
			</stc:role>	
			<div style="padding-top:2px">
				<img src="${ctx}/images/exticons/list-items.gif" class="icon">
				<a href="${ctx}/init/edit.do" target="main">期初设置</a>
			</div>
			<div style="padding-top:2px">
				<img src="${ctx}/images/exticons/list-items.gif" class="icon">
				<a href="${ctx}/databackup/index.do" target="main">数据备份</a>
			</div>							  																		       
		</div>
	</div>
							
</div>
