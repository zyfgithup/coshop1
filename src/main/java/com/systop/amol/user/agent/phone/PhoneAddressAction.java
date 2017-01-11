package com.systop.amol.user.agent.phone;

import com.systop.amol.base.express.model.ExpressCompany;
import com.systop.amol.base.express.service.ExpressCompanyManager;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.sales.model.Sales;
import com.systop.amol.sales.model.SalesDetail;
import com.systop.amol.sales.service.SalesDetailManager;
import com.systop.amol.sales.service.SalesManager;
import com.systop.amol.user.agent.model.ForfpShow;
import com.systop.amol.user.agent.model.ReceiveAddress;
import com.systop.amol.user.agent.service.ReceiveAddressManager;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.region.service.RegionManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.*;
@SuppressWarnings({ "serial", "unused", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneAddressAction extends DefaultCrudAction<ReceiveAddress,ReceiveAddressManager>{
	@Autowired
	private UserManager userManager;
	@Autowired
	private SalesDetailManager salesDetailManager;
	@Autowired
	private SalesManager salesManager;
	@Autowired
	private RegionManager regionManager;
	@Autowired
	private ReceiveAddressManager receiveAddressManager;
	@Autowired
	private ExpressCompanyManager expressCompanyManager;
	private List<ReceiveAddress> list=new ArrayList<ReceiveAddress>();
	private Map<String,Object> message=new HashMap<String,Object>();
	private ReceiveAddress receiveAddress;
	private String pageNumber = "1";
	/** 每页显示条数 */
	private String pageCount = "10";
	public String getMrAddress(){
		String userId=getRequest().getParameter("userId");
		receiveAddress=getManager().getRsByUserId(Integer.parseInt(userId));
		return "getDetail";
	}
	public String delAddr(){
		String addId=getRequest().getParameter("addId");
		ReceiveAddress receiveAddress=getManager().getObjectById(Integer.parseInt(addId));
		receiveAddress.setVisible("0");
		getManager().update(receiveAddress);
		message.put("msg","删除收货地址成功！");
		return SUCCESS;
	}
	public String kaipiao(){
		String addId=getRequest().getParameter("addId");
		ReceiveAddress receiveAddress=getManager().getObjectById(Integer.parseInt(addId));
		receiveAddress.setIfHaveKp("1");
		receiveAddressManager.update(receiveAddress);
		message.put("msg","开票成功！");
		return SUCCESS;
	}
	public String index(){
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		String userId=getRequest().getParameter("userId");
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from ReceiveAddress rs where rs.user.id=? and rs.visible='1' order by rs.ifMrAddr desc");
		args.add(Integer.parseInt(userId));
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		list=page.getData();
		return "index";
	}
	public String getDetalAddr(){
		String addId=getRequest().getParameter("addId");
		 receiveAddress=getManager().getObjectById(Integer.parseInt(addId));
		Region region = receiveAddress.getRegion();
		String regionName = "";
		if(null!=region){
			if(null!=region.getParent() && null==region.getParent().getParent()){
				regionName = region.getParent().getName()+" "+region.getName();
			}
			if(null!=region.getParent() && null!=region.getParent().getParent()){
				regionName = region.getParent().getParent().getName() +" "+region.getParent().getName() +" "+region.getName();
			}
		}
		receiveAddress.setRegionNames(regionName);
		Set<Sales> set = receiveAddress.getSet();
		System.out.print("-----------------------------------"+set.size());
		List<ForfpShow> fList = new ArrayList<ForfpShow>();
		Iterator<Sales> it = set.iterator();
		ForfpShow ff = null;
		while(it.hasNext()){
			ff = new ForfpShow();
			Sales sale = it.next();
			ff.setSalesType(sale.getSalesType());
			/**
			 * 加油的就是“油品种类，加油量，油品单价，总计”，
			 * 维修的每个订单返回  “车型，配件总计，工时费，总计”四个，
			 * 保险返回“保险商品名称，单价，份数，总计”
			 */
			if(sale.getSalesType().equals("jy")){
				ff.setParam1(sale.getRemark());
				ff.setParam2(sale.getCount()+"");
				ff.setParam3(sale.getYpPrice()+"");
				ff.setParam4(sale.getSamount()+"");
			}
			if(sale.getSalesType().equals("bx")){
				List<SalesDetail> sdList = salesDetailManager.getDetails(sale.getId());
				Products products = sdList.get(0).getProducts();
				ff.setParam1(products.getName());
				ff.setParam2(products.getPresentPrice()+"");
				ff.setParam3(sale.getCount()+"");
				ff.setParam4(sale.getSamount()+"");
			}
			//维修的每个订单返回  “车型，配件总计，工时费，总计”四个，
			if(sale.getSalesType().equals("wx")){
				ff.setParam1(sale.getCarType());
				ff.setParam2(sale.getPjTotalMoney()+"");
				ff.setParam3(sale.getTimeMoneys()+"");
				ff.setParam4(sale.getSamount()+"");
			}
			fList.add(ff);
		}
		receiveAddress.setfList(fList);
		/*if(null!=receiveAddress){
			receiveAddress.setComAddr(receiveAddress.getExpressCompany().getAddress());
			receiveAddress.setComfpbh(receiveAddress.getExpressCompany().getSaxNo());
			receiveAddress.setComName(receiveAddress.getExpressCompany().getName());
			receiveAddress.setComPhone(receiveAddress.getExpressCompany().getPhone());
			receiveAddress.setComBankName(receiveAddress.getExpressCompany().getBankName());
			receiveAddress.setCardNo(receiveAddress.getExpressCompany().getCardNo());
			receiveAddress.setSaxNo(receiveAddress.getExpressCompany().getSaxNo());
		}*/
		return "getDetail";
	}
	public String gxMrAddr(){
		String addId=getRequest().getParameter("addId");
		String userId=getRequest().getParameter("userId");
		ReceiveAddress receiveAddress=getManager().getObjectById(Integer.parseInt(addId));
		receiveAddress.setIfMrAddr(1);
		getManager().update(receiveAddress);
		ReceiveAddress ylMrAddr=getManager().getRsByUAId(Integer.parseInt(userId), Integer.parseInt(addId));
		if(null!=ylMrAddr){
		ylMrAddr.setIfMrAddr(0);
		getManager().update(ylMrAddr);
		}
		message.put("msg","设置默认地址成功！");
		return SUCCESS;
	}
	public String addReceiveAddress(){
		ReceiveAddress ra = new ReceiveAddress();
		String orderIds = getRequest().getParameter("orderIds");
		String[] idArray = orderIds.split(",");
		Sales sale = null;
		Set<Sales> set = new HashSet<Sales>();
		for (String str : idArray ){
			sale = new Sales();
			sale = salesManager.get(Integer.parseInt(str));
			sale.setSqkfp("1");
			set.add(sale);
		}
		String returnStr="";
		String comId = getRequest().getParameter("comId");
		ExpressCompany ec = expressCompanyManager.get(Integer.parseInt(comId));
		ra.setComAddr(ec.getAddress());
		ra.setComName(ec.getName());
		ra.setComfpbh(ec.getSaxNo());
		ra.setSaxNo(ec.getSaxNo());
		ra.setComPhone(ec.getPhone());
		ra.setComBankName(ec.getBankName());
		ra.setCardNo(ec.getCardNo());
		String userId=getRequest().getParameter("userId");
		User user=userManager.getUserById(Integer.parseInt(userId));
		ra.setUser(user);
		String address=getRequest().getParameter("address");
		String merId=getRequest().getParameter("merId");
		String regionId=getRequest().getParameter("regionId");
		System.out.println("------------------->>"+address);
		String phone=getRequest().getParameter("phone");
		String name=getRequest().getParameter("name");
		String ifMrAddr=getRequest().getParameter("ifMrAddr");
		ra.setCreateTime(new Date());
		ra.setIfHaveKp("0");
		/*if(StringUtils.isNotBlank(ifMrAddr)){
			if(Integer.parseInt(ifMrAddr)==1){
				ReceiveAddress addr=getManager().getRsByUserId(Integer.parseInt(userId));
				if(null!=addr){
						addr.setIfMrAddr(0);
						getManager().update(addr);
				}
			}
			getModel().setIfMrAddr(Integer.parseInt(ifMrAddr));
		}*/
		if(StringUtils.isNotBlank(regionId)){
			Region region = regionManager.get(Integer.parseInt(regionId));
			ra.setRegion(region);
		}
		if(StringUtils.isNotBlank(merId)){
			User merUser = userManager.get(Integer.parseInt(merId));
			ra.setMerUser(merUser);
		}
		if(StringUtils.isNotBlank(address)){
			ra.setAddress(address);
		}
		if(null!=set && set.size()>0){
			ra.setSet(set);
		}
		ra.setVisible("1");
		if(StringUtils.isNotBlank(phone)){
			ra.setReceivePhone(phone);
		}
		if(StringUtils.isNotBlank(name)){
			ra.setReceiveName(name);
		}
		receiveAddressManager.save(ra);
		Iterator<Sales> it = set.iterator();
		while (it.hasNext()){
			Sales s = it.next();
			s.setAddress(ra);
			salesManager.update(s);
		}
		message.put("msg","添加发票成功！");
		return SUCCESS;
	}
	public String updateReceiveAddress(){
		String userId=getRequest().getParameter("userId");
		//User user=userManager.getUserById(Integer.parseInt(userId));
		String address=getRequest().getParameter("address");
		String postCode=getRequest().getParameter("postCode");
		String phone=getRequest().getParameter("phone");
		String name=getRequest().getParameter("name");
		String addId=getRequest().getParameter("addId");
		ReceiveAddress receiveAddress=getManager().getObjectById(Integer.parseInt(addId));
		receiveAddress.setId(null);
		receiveAddress.setAddress(address);
		receiveAddress.setPostCode(postCode);
		receiveAddress.setReceivePhone(phone);
		receiveAddress.setReceiveName(name);
		getManager().update(receiveAddress);
		message.put("msg","修改收货地址成功！");
		return SUCCESS;
	}
	//商家
	public String getKpInfoByType(){
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		String type = getRequest().getParameter("type");
		//传的是用户id
		String merId = getRequest().getParameter("merId");
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from ReceiveAddress rs where rs.merUser.shopOfUser.id=? and rs.ifHaveKp=? and rs.visible='1' ");
		args.add(Integer.parseInt(merId));
		args.add(type);
		hql.append(" order by rs.createTime desc");
		System.out.println("hql = "+hql);
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		list=page.getData();
		for (ReceiveAddress ra : list){
			ra.setShowSet(ra.getSet());
			if(null!=ra.getUser()){
			ra.setUserId(ra.getUser().getId());
			ra.setImagerURL(ra.getUser().getImageURL());
			ra.setNickName(ra.getUser().getNickName());
			}
			/*if(null!=ra.getMerUser()){
				ra.setImagerURL(ra.getMerUser().getShopOfUser().getImageURL());
				ra.setNickName(ra.getMerUser().getShopOfUser().getNickName());
			}*/

		}
		return INDEX;
	}
	
	public Map<String, Object> getMessage() {
		return message;
	}

	public void setMessage(Map<String, Object> message) {
		this.message = message;
	}
	public List<ReceiveAddress> getList() {
		return list;
	}
	public void setList(List<ReceiveAddress> list) {
		this.list = list;
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
	public ReceiveAddress getReceiveAddress() {
		return receiveAddress;
	}
	public void setReceiveAddress(ReceiveAddress receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	

}
