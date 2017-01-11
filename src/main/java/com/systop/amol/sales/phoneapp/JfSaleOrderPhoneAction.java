package com.systop.amol.sales.phoneapp;

import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.model.SalesDetail;
import com.systop.amol.sales.service.SalesDetailManager;
import com.systop.amol.sales.service.SalesManager;
import com.systop.amol.sales.service.SalesOrderManager;
import com.systop.amol.sales.utils.ReceivingState;
import com.systop.amol.user.agent.service.ReceiveAddressManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.*;
@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JfSaleOrderPhoneAction extends DefaultCrudAction<Sales, SalesOrderManager>{
	/** ajax校验微信用户手机号返回信息 */
	Map<String, Object> resultMessage = new HashMap<String, Object>();
	@Resource
	private CustomerManager customerManager;
	@Resource
	private SalesManager salesManager;
	@Resource
	private UserManager userManager;
	
	@Resource
	private ReceiveAddressManager receiveAddressManager;
	@Resource
	private SalesDetailManager salesDetailManager;
	
	private Integer userId;
	/** 分页，页码 */
	private String pageNumber = "1";
	/** 商品集合 */
	private List<SalesDetail> salesDetailList;

	/** 每页显示条数 */
	private String pageCount = "10";
	private List<Sales> list;
	/**
	 * 查看订单信息 请求参数：orderId 订单id 返回数据：json数组，数组中的数据对应看SalesDetail和Products
	 */
	@Override
	public String view() {
		String orderId = getRequest().getParameter("orderId");
		if (StringUtils.isNotBlank(orderId)) {
			salesDetailList = salesDetailManager.getDetails(Integer.valueOf(orderId));
			for (SalesDetail salesDetail : salesDetailList) {
				System.out.println(salesDetail.getProducts().productsJson());
				salesDetail.setIsReturn(salesDetail.getSales().getIsReturn());
				salesDetail.setProductsJSON(salesDetail.getProducts().productsJson());
				if(salesDetail.getSales().getAddress()!=null){
					salesDetail.setAddrId(salesDetail.getSales().getAddress().getId().toString());
					salesDetail.setAddress(salesDetail.getSales().getAddress().getAddress());
					salesDetail.setPhone(salesDetail.getSales().getAddress().getReceivePhone());
					salesDetail.setOrderName(salesDetail.getSales().getAddress().getReceiveName());
				}else{
					salesDetail.setAddrId("");
					salesDetail.setAddress("");
					salesDetail.setPhone("");
					salesDetail.setOrderName("");
				}
				salesDetail.setMerId(salesDetail.getSales().getMerId().toString());
				salesDetail.setSaleNo(salesDetail.getSales().getSalesNo());
				salesDetail.setAmount(salesDetail.getSales().getSamount());
				//salesDetail.setPayType(salesDetail.getSales().getPayment().toString());
				salesDetail.setOrderTime(salesDetail.getSales().getCreateTime());
			}
		}
		return VIEW;
	}
	/**
	 * 查询订单信息 json 数据，字段对应看Sales对象
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String index() {
		page = PageUtil.getPage(Integer.valueOf(pageNumber),
		Integer.valueOf(pageCount));
	StringBuffer sql = new StringBuffer(
		"from Sales s where s.status = ?  and s.salesType='jfSale' ");
	List args = new ArrayList();
	// 设置单子状态为"订单"
	args.add(SalesConstants.ORDERS);
	User user = userManager
		.get(Integer.valueOf(getRequest().getParameter("userId")));
	if (user != null) {
		getModel().setUser(user);
		sql.append(" and s.user.id = ? ");
		args.add(user.getId());
	}
	try {
		if (StringUtils.isNotBlank(getModel().getSalesNo())) {
		sql.append(" and s.salesNo like ? ");
		args.add(MatchMode.ANYWHERE.toMatchString(getModel().getSalesNo()));
		}
		sql.append(" order by s.createTime desc, s.salesNo desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		list = page.getData();
		for (Sales sales:list){
			if(null!=sales.getAddress()) {
				sales.setAddressForClient(sales.getAddress().getAddress());
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return INDEX;
	}
	/**
	 * 保存积分订单信息
	 * 提交参数：userId用户id；pid商品id，比如：12321,988123（英文逗号隔开的商品id字符串）；outprice商品单个的积分，比如：
	 * 30,20（英文逗号隔开的商品单价字符串）
	 * counts商品数量，比如：2,3（英文逗号隔开的单种商品数量字符串）；money商品积分，比如：60,60（
	 * 英文逗号隔开的单种商品价格字符串） allMoneys总积分数；
	 * 
	 * 返回json数据：{"result":true,"orderNo":"xxxxxxx","samount":30.01,"counts":2}
	 * result操作状态true成功，false失败；orderNo订单号，字符串；samount订单金额，浮点型；counts订单商品总数，整型
	 * 
	 * /salesOrderPhone/save.do
	 */
	@Override
	public String save() {
	try {
		User user = userManager.get(userId);
		if (null == user) {
		resultMessage.put("result", false);
		return "save";
		}
		getModel().setUser(userManager.get(userId));
		getModel().setStatus(SalesConstants.ORDERS);
		getModel().setCkzt(SalesConstants.SALES_NO);
		if (null != getModel().getCustomer()) {
		if (null != getModel().getCustomer().getId()
			&& 0 < getModel().getCustomer().getId()) {
			getModel().setCustomer(
				customerManager.get(getModel().getCustomer().getId()));
		} else {
			getModel().setCustomer(null);
		}
		}
		// 生成订单号
		getModel().setSalesNo(getManager().getOrderNumber(SalesConstants.ORDERS,
			/* user.getId() */32768));
		// 商品总数量
		int count = 0;
		// 保存订单明细
		// 商品id
		String[] pids = this.getRequest().getParameter("pid").split(",");
		// 商品实际出积分数
		String[] outprices = this.getRequest().getParameter("outprice")
			.split(",");
		// 单个商品的总数量
		String[] counts = this.getRequest().getParameter("counts").split(",");
		// 单个商品的总价
		String[] moneys = this.getRequest().getParameter("money").split(",");
		List<SalesDetail> sdlist = new ArrayList<SalesDetail>();
		for (int i = 0; i < pids.length; i++) {
		SalesDetail sd = new SalesDetail();
		sd.setAmount(Double.parseDouble(moneys[i]));
		sd.setCount(Integer.parseInt(counts[i]));
		sd.setTnorootl(sd.getCount());
		sd.setOutPrice(Double.parseDouble(outprices[i]));
		Products products = this.getManager().getDao().get(Products.class,Integer.parseInt(pids[i]));
		sd.setProducts(products);
		sd.setSales(getModel());
		count += Integer.parseInt(counts[i]);
		sdlist.add(sd);
		}
		// 订单中的商品剩余总数量
		// getModel().setTtno(count);
		// 订单中的商品总数量
//		getModel().setCount(count);
//		getModel().setTtno(count);
//		getModel().setSalesType("jfSale");
		//判断是否有收货地址
		if(null!=getRequest().getParameter("addId")&&!"".equals(getRequest().getParameter("addId"))){
			Integer addId=Integer.parseInt(getRequest().getParameter("addId"));
			getModel().setAddress(receiveAddressManager.get(addId));
		}else{
			//积分支付，没有填写收货地址，当面付积分，当面领货，app用户的积分订单收货状态是已收货
			getModel().setoState("1");
			getModel().setReceivingState(ReceivingState.RECEIVED_GOODS);
		}
		
		// 下单时间
		getModel().setCreateTime(new Date());
		// 应收金额
		String allMoneys = getRequest().getParameter("allMoneys");
		getModel().setSamount(Double.valueOf(allMoneys));
		//支付状态，现金直接成功
		getModel().setPayState(true);
		Integer merId=Integer.parseInt(getRequest().getParameter("merId"));
		User regionUser=userManager.get(merId);
		getModel().setMerUser(regionUser);
		if(user.getIntegral()<Double.valueOf(allMoneys).intValue()){
			resultMessage.put("result",false);
			resultMessage.put("msg","积分不足");
		}else{
			user.setIntegral(user.getIntegral()-Double.valueOf(allMoneys).intValue());
			userManager.update(user);
			this.getManager().save(getModel(),sdlist);
			//区域负责人增加积分
			int addJf=Double.valueOf(allMoneys).intValue();
			Integer regionUserIntegral = regionUser.getIntegral();
			Integer regionUserDhIntegral = regionUser.getDhIntegral();
			if (null != regionUserDhIntegral) {
				regionUser.setDhIntegral(regionUser.getDhIntegral() +addJf);
			} else {
				regionUser.setDhIntegral(addJf);
			}
			if (null != regionUserIntegral) {
				regionUser.setIntegral(regionUser.getIntegral() + addJf);
			} else {
				regionUser.setIntegral(addJf);
			}
			userManager.update(regionUser);
			Sales order = this.getManager().findObject(
				"from Sales o where o.salesNo = ?", getModel().getSalesNo());
			resultMessage.put("result", true);
			resultMessage.put("orderId", order.getId());// 订单id
			resultMessage.put("orderNo",getModel().getSalesNo());
			resultMessage.put("totalPrice",allMoneys);
			resultMessage.put("orderTime",getModel().getCreateTime());
		}
	} catch (Exception e) {
		resultMessage.put("result", false);
		e.printStackTrace();
	}
	return "save";
	}
	public Map<String,Object> getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(Map<String, Object> resultMessage) {
		this.resultMessage = resultMessage;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getPageCount() {
		return pageCount;
	}
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	public List<Sales> getList() {
		return list;
	}
	public void setList(List<Sales> list) {
		this.list = list;
	}

	public List<SalesDetail> getSalesDetailList() {
		return salesDetailList;
	}

	public void setSalesDetailList(List<SalesDetail> salesDetailList) {
		this.salesDetailList = salesDetailList;
	}
}
