package com.systop.amol.sales.model;

import com.systop.amol.base.customer.model.Customer;
import com.systop.amol.base.express.model.ExpressCompany;
import com.systop.amol.base.storage.model.Storage;
import com.systop.amol.card.model.CardGrant;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.utils.MerchantIsRutrn;
import com.systop.amol.sales.utils.Payment;
import com.systop.amol.sales.utils.ReceivingState;
import com.systop.amol.user.agent.model.ReceiveAddress;
import com.systop.amol.user.agent.model.TouBaoRen;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 销售订单 销售出库 销售退货
 * 
 * @author 王会璞
 * {"addressForClient":null,"cardamount":null,"cashConsumption":null,"ckzt":null,"count":4.5,"courierNumber":null,"createTime":"2016-08-04T08:29:32","id":5,"integral":null,"isReturn":null,"jfNumber":null,"jjMoney":null,"locX":104.003,"locY":34.322,"merId":232356,"merchantIsRutrn":null,"oState":null,"payState":true,"payment":null,"position":"北京市海淀区上地街建新街","realReturnMoney":null,"receivingState":null,"result_code":null,"return_code":null,"rttao":null,"salesNo":"No20160731","salesType":null,"samount":500.0,"sjMoney":null,"spayamount":500.0,"sqkfp":"1","status":"0","transaction_id":null,"ttno":null,"zjMoney":null}
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "sales")
public class Sales extends BaseModel implements Cloneable {

	/** 主键 */
	private Integer id;
	/** 销售单号 ,订单号*/
	private String salesNo;

	/** 用户  提交订单的用户*/
	private User user;
	/**订单类型 加油单，。。。。 */
	private String salesType;
	/** 员工 */
	private User employee;

	/** 添加时间 */
	private Date createTime;

	/** 订单状态 1未支付 2发送账单 3已经支付*/
	/** 3已评价，2未评价，1 订单提交 -1取消订单了 */
	/**
	 * 快速加油
	 * 0订单提交
	 * 1进行中（司机接单）
	 * -1已取消（取消订单）
	 * 2已完成（支付以后）
	 * */
	private String status;
	//是否已经开了发票 1 是已经开了发票  0或者null是还未开发票
	private String sqkfp;
	/** 如何客户是用代币卡下的订单，将代币卡号存储到该字段中 */
	private String orderCardNo = null;

	/** 支付方式 */
	private Payment payment;
	
	
	/** 商家是否同意退货,null app用户没有申请退货 */
	private MerchantIsRutrn merchantIsRutrn;
	
	/** 收货状态（app用户） */
	private ReceivingState receivingState;
	
	/** 快递公司 */
	private ExpressCompany expressCompany;
	
	/** 快递单号 */
	private String courierNumber;
	/** 客户 */
	private Customer customer;
	/** 订单中的代币卡消费 出库单中的代币卡消费 */
	private Double cardamount;
	/** 订单中的现金消费 出库单中的现金消费 */
	private Double cashConsumption;

	/** 订单中的应收金额 * */
	private Double samount;

	/** 订单中的实收金额*/
	private Double spayamount;
	
	/** 支付状态,null未支付，false支付失败，true支付成功 */
	private Boolean payState;
	/** 备注 */
	private String remark;

	/** 订单或出库单（如果本单是出库单该对象代表订单，如果该单是退货单该对象代表出库单） */
	private Sales single;

	/** 仓库 */
	private Storage storage;
	/**
	 * 订单中的商品剩余总数量 
	 * 出库单中的商品真实出库总数量
	 * */
	private Double ttno;
	/**
	 * 订单中的商品总数量 
	 * 出库单中的商品原始出库总数量
	 * 退货单中的退货总数量
	 * */
	private Double count;
	/** 积分 */
	private Integer integral;

	/**
	 * 当该单表示订单时，该字段代表出库状态 ，0货物未出库，1货物部分出库，2货物已出库
	 * 当该单表示出库单时，该字段代表退货状态，0未退货，1部分退货，2完全退货
	 * 当该单表示退货单时，该字段代表退货状态，0退款中，1退款成功，2退款失败
	 * */
	private String ckzt;
	
	/** 退货货款金额 */
	private Double rttao;
	/**
	 * 实退金额
	 */
	private Double realReturnMoney;
	/** 冲红 */
	private Integer redRed = SalesConstants.NORMAL;
	
	/** 是否有回款单为出库单回款 */
	private String receiveSalesReturn = SalesConstants.NOT_RE;
	
	/** 消费的代币卡 */
	private CardGrant cardGrant;
	
	private String isReturn="1";
	/** 发货状态:null未配送； 1已配送； 2已完成 */
	private String oState;
	//alipay_开头为支付宝相关字段存储信息
	private String alipay_trade_no;//支付宝交易号【支付宝】
	private String alipay_buyer_email;//买家支付宝账号【支付宝】
	private String alipay_gmt_create;//交易创建时间【支付宝】
	private String alipay_gmt_payment;//交易付款时间【支付宝】,【微信】new date()
	private String alipay_out_trade_no;//商户网站唯一订单号【支付宝】
	private String alipay_trade_status;//交易状态【支付宝】
	private String alipay_total_fee;//交易金额【支付宝】
	//微信支付回调保存的相关字段
	private String transaction_id;
	private String result_code;
	private String return_code;
	private ReceiveAddress address;

	private String addressForClient;
	/** 商户对象 */
	private User merUser;
	/** 商户id */
	private Integer merId;
	/** 商家收入 */
	private Double sjMoney;
	
	/** 返给直接合伙人的钱 */
	private Double zjMoney;
	/** 直接合伙人 */
	private User zjUser;
	
	/** 返给间接合伙人的钱 */
	private Double jjMoney;
	/** 间接合伙人 */
	private User jjUser;
	
	/** 消费app用户所得积分 */
	private Integer jfNumber;
	//经度
	private Double locX;
	//纬度
	private Double locY;
	//位置
	private String position;

	//商家电话
	private String userPhone;

	private Integer userId;

	private String nickName;

	private String imageURL;
	//加油车名称
	private String jycName;
	private String jycNo;
	private String createTimeStr;
	private String carType;
	private Double timeMoneys;
	private Double pjTotalMoney;
	private String merName;
	private List<SalesDetail> sdList;
	private String proName;
//户头像，昵称，电话就可以了
	private String yhtx;
	private String yhNickName;
	private String yhdh;

	private  String merSortStr;
	private String merSortNameStr;
	private String priceStr;

	private double pjfs;
	private Integer pjrs;
	private List<TouBaoRen> tbList = new ArrayList<TouBaoRen>();
	//商家评论用户
	private String sjplyh;
	//用户评论商家
	private String yhplsj;
	//商家评论用户分数
	private Integer sjplyhfs;
	//用户评论商家分数
	private Integer yhplsjfs;
	//商家评论用户内容
	private String sjplyhnr;
	//用户评论商家内容
	private String yhplsjnr;
	//法人代表
	private String legalName;
	//企业名称
	private String comName;
	//企业注册地
	private String comAddress;

	//油品价格
	private String ypPrice;

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "cardGrant_id")
	@JSON(serialize = false)
	public CardGrant getCardGrant() {
		return cardGrant;
	}

	public void setCardGrant(CardGrant cardGrant) {
		this.cardGrant = cardGrant;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "meruser_id")
	@JSON(serialize = false)
	public User getMerUser() {
		return merUser;
	}

	public void setMerUser(User merUser) {
		this.merUser = merUser;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "express_company_id")
	@JSON(serialize = false)
	public ExpressCompany getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(ExpressCompany expressCompany) {
		this.expressCompany = expressCompany;
	}

	/**
	 * 克隆一个与自己一样的对象
	 */
	@Override
	public Object clone() {
		Object o = null;
		try {
			o = (Sales) super.clone();// Object中的clone()识别出你要复制的是哪一个对象。
		} catch (CloneNotSupportedException e) {
			System.out.println(e.toString());
		}
		return o;
	}

	@JSON(serialize = false)
	public String getReceiveSalesReturn() {
		return receiveSalesReturn;
	}

	public void setReceiveSalesReturn(String receiveSalesReturn) {
		this.receiveSalesReturn = receiveSalesReturn;
	}

	@JSON(serialize = false)
	public Integer getRedRed() {
		return redRed;
	}

	public void setRedRed(Integer redRed) {
		this.redRed = redRed;
	}
	
	@Column(precision = 10, scale = 2)
	@JSON(serialize = false)
	public Double getCardamount() {
		return cardamount;
	}
	public void setCardamount(Double cardamount) {
		this.cardamount = cardamount;
	}
	@Column(precision = 10, scale = 2)
	@JSON(serialize = false)
	public Double getCashConsumption() {
		return cashConsumption;
	}
	public void setCashConsumption(Double cashConsumption) {
		this.cashConsumption = cashConsumption;
	}
	@Column(precision = 10, scale = 2)
	public Double getSamount() {
		return samount;
	}
	public void setSamount(Double samount) {
		this.samount = samount;
	}
	@Column(precision = 10, scale = 2)
	public Double getSpayamount() {
		return spayamount;
	}
	public void setSpayamount(Double spayamount) {
		this.spayamount = spayamount;
	}
	@Column(precision = 10, scale = 2)
	@JSON(serialize = false)
	public Double getRttao() {
		return rttao;
	}
	public void setRttao(Double rttao) {
		this.rttao = rttao;
	}

	@Column(length = 2)
	@JSON(serialize = false)
	public String getCkzt() {
		return ckzt;
	}

	public void setCkzt(String ckzt) {
		this.ckzt = ckzt;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}

	@JSON(serialize = false)
	public Double getTtno() {
		return ttno;
	}

	public void setTtno(Double ttno) {
		this.ttno = ttno;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "storage_id")
	@JSON(serialize = false)
	public Storage getStorage() {
		return storage;
	}
	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "single_id")
	@JSON(serialize = false)
	public Sales getSingle() {
		return single;
	}

	public void setSingle(Sales single) {
		this.single = single;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	@JSON(serialize = false)
	public User getEmployee() {
		return employee;
	}

	public void setEmployee(User employee) {
		this.employee = employee;
	}
	
	@Enumerated(EnumType.STRING)
	@JSON(serialize = false)
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "merchant_is_return")
	@JSON(serialize = false)
	public MerchantIsRutrn getMerchantIsRutrn() {
		return merchantIsRutrn;
	}

	public void setMerchantIsRutrn(MerchantIsRutrn merchantIsRutrn) {
		this.merchantIsRutrn = merchantIsRutrn;
	}

	@Enumerated(EnumType.STRING)
	@JSON(serialize = false)
	public ReceivingState getReceivingState() {
		return receivingState;
	}

	public void setReceivingState(ReceivingState receivingState) {
		this.receivingState = receivingState;
	}

	@Column(name = "sales_no")
	public String getSalesNo() {
		return salesNo;
	}

	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}

	@Column(name = "status", columnDefinition = "char(1) default '0'")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	@JSON(serialize = false)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="sales_type")
	public String getSalesType() {
		return salesType;
	}

	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}

	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "id", nullable = false)
	public Integer getId() {
		return id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JSON(serialize = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "remark", length = 2000)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "create_time")
	@JSON(serialize = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JSON(serialize = false)
	public String getOrderCardNo() {
		return orderCardNo;
	}

	public void setOrderCardNo(String orderCardNo) {
		this.orderCardNo = orderCardNo;
	}
	@JSON(serialize = false)
	public Double getRealReturnMoney() {
		return realReturnMoney;
	}

	public void setRealReturnMoney(Double realReturnMoney) {
		this.realReturnMoney = realReturnMoney;
	}
	
	@JSON(serialize = false)
	public String getAlipay_trade_no() {
		return alipay_trade_no;
	}

	public void setAlipay_trade_no(String alipay_trade_no) {
		this.alipay_trade_no = alipay_trade_no;
	}
	@JSON(serialize = false)
	public String getAlipay_buyer_email() {
		return alipay_buyer_email;
	}

	public void setAlipay_buyer_email(String alipay_buyer_email) {
		this.alipay_buyer_email = alipay_buyer_email;
	}
	@JSON(serialize = false)
	public String getAlipay_gmt_create() {
		return alipay_gmt_create;
	}

	public void setAlipay_gmt_create(String alipay_gmt_create) {
		this.alipay_gmt_create = alipay_gmt_create;
	}
	@JSON(serialize = false)
	public String getAlipay_gmt_payment() {
		return alipay_gmt_payment;
	}

	public void setAlipay_gmt_payment(String alipay_gmt_payment) {
		this.alipay_gmt_payment = alipay_gmt_payment;
	}
	@JSON(serialize = false)
	public String getAlipay_out_trade_no() {
		return alipay_out_trade_no;
	}

	public void setAlipay_out_trade_no(String alipay_out_trade_no) {
		this.alipay_out_trade_no = alipay_out_trade_no;
	}
	@JSON(serialize = false)
	public String getAlipay_trade_status() {
		return alipay_trade_status;
	}

	public void setAlipay_trade_status(String alipay_trade_status) {
		this.alipay_trade_status = alipay_trade_status;
	}
	@JSON(serialize = false)
	public String getAlipay_total_fee() {
		return alipay_total_fee;
	}

	public void setAlipay_total_fee(String alipay_total_fee) {
		this.alipay_total_fee = alipay_total_fee;
	}

	@Column(name = "pay_state")
	public Boolean getPayState() {
		return payState;
	}

	public void setPayState(Boolean payState) {
		this.payState = payState;
	}
	@Column(name = "integral")
	@JSON(serialize = false)
	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	@Column(name = "is_return")
	@JSON(serialize = false)
	public String getIsReturn() {
		return isReturn;
	}
	@Column(name = "o_state")
	@JSON(serialize = false)
	public String getoState() {
		return oState;
	}
	public void setoState(String oState) {
		this.oState = oState;
	}
	public void setIsReturn(String isReturn) {
		this.isReturn = isReturn;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	@JSON(serialize = false)
	public ReceiveAddress getAddress() {
		return address;
	}

	public void setAddress(ReceiveAddress address) {
		this.address = address;
	}
	@Column(name="transaction_id")
	@JSON(serialize = false)
	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	@Column(name="result_code")
	@JSON(serialize = false)
	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	@Column(name="return_code")
	@JSON(serialize = false)
	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	@Column(name="mer_id")
	public Integer getMerId() {
		return merId;
	}

	public void setMerId(Integer merId) {
		this.merId = merId;
	}


	@JSON(serialize = false)
	public Double getZjMoney() {
		return zjMoney;
	}

	public void setZjMoney(Double zjMoney) {
		this.zjMoney = zjMoney;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "zj_user_id")
	@JSON(serialize = false)
	public User getZjUser() {
		return zjUser;
	}

	public void setZjUser(User zjUser) {
		this.zjUser = zjUser;
	}
	@JSON(serialize = false)
	public Double getJjMoney() {
		return jjMoney;
	}

	public void setJjMoney(Double jjMoney) {
		this.jjMoney = jjMoney;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "jj_user_id")
	@JSON(serialize = false)
	public User getJjUser() {
		return jjUser;
	}

	public void setJjUser(User jjUser) {
		this.jjUser = jjUser;
	}
	@JSON(serialize = false)
	public Integer getJfNumber() {
		return jfNumber;
	}

	public void setJfNumber(Integer jfNumber) {
		this.jfNumber = jfNumber;
	}
	@JSON(serialize = false)
	public String getCourierNumber() {
		return courierNumber;
	}

	public void setCourierNumber(String courierNumber) {
		this.courierNumber = courierNumber;
	}
	@JSON(serialize = false)
	public Double getSjMoney() {
		return sjMoney;
	}

	public void setSjMoney(Double sjMoney) {
		this.sjMoney = sjMoney;
	}

	@Transient
	@JSON(serialize = false)
	public String getAddressForClient() {
		return addressForClient;
	}

	public void setAddressForClient(String addressForClient) {
		this.addressForClient = addressForClient;
	}

	public String toInserSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into sales(id,sales_no,user_id,employee_id,create_time,status,orderCardNo,payment,customer_id,cardamount,cashConsumption,samount,spayamount,remark,single_id,storage_id,ttno,count,ckzt,rttao,realReturnMoney,redRed,receiveSalesReturn,cardGrant_id) VALUES(");
		sql.append(id);
		sql.append(",'");
		sql.append(salesNo);
		sql.append("',");
		sql.append(user.getId());
		sql.append(",");
		if(employee != null){
			sql.append(employee.getId());
		}else{
			sql.append(employee);
		}
		sql.append(",'");
		sql.append(createTime);
		sql.append("','");
		sql.append(status);
		sql.append("',");
		sql.append(orderCardNo);
		sql.append(",");
		if(payment != null){
			sql.append("'"+payment+"'");
		}else{
			sql.append(payment);
		}
		sql.append(",");
		sql.append(customer.getId());
		sql.append(",");
		sql.append(cardamount);
		sql.append(",");
		sql.append(cashConsumption);
		sql.append(",");
		sql.append(samount);
		sql.append(",");
		sql.append(spayamount);
		sql.append(",'");
		sql.append(remark);
		sql.append("',");
		sql.append(oState);
		sql.append("',");
		if(single != null){
			sql.append(single.getId());
		}else{
			sql.append(single);
		}
		sql.append(",");
		if(storage != null){
			sql.append(storage.getId());
		}else{
			sql.append(storage);
		}
		sql.append(",");
		sql.append(ttno);
		sql.append(",");
		sql.append(count);
		sql.append(",");
		sql.append(ckzt);
		sql.append(",");
		sql.append(rttao);
		sql.append(",");
		sql.append(realReturnMoney);
		sql.append(",");
		sql.append(redRed);
		sql.append(",'");
		sql.append(integral);
		sql.append(",'");
		sql.append(receiveSalesReturn);
		sql.append("',");
		if(cardGrant != null){
			sql.append(cardGrant.getId());
		}else{
			sql.append(cardGrant);
		}
		sql.append(")");
		return sql.toString();
	}
	@Column(name="sq_kfp")
	public String getSqkfp() {
		return sqkfp;
	}

	public void setSqkfp(String sqkfp) {
		this.sqkfp = sqkfp;
	}


	@Column(name="position")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	@Column(name="loc_x")

	public Double getLocX() {
		return locX;
	}

	public void setLocX(Double locX) {
		this.locX = locX;
	}
	@Column(name="loc_y")
	public Double getLocY() {
		return locY;
	}

	public void setLocY(Double locY) {
		this.locY = locY;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	@Transient
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	@Transient
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	@Transient
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	@Transient
	public String getJycName() {
		return jycName;
	}

	public void setJycName(String jycName) {
		this.jycName = jycName;
	}
	@Transient
	public String getJycNo() {
		return jycNo;
	}

	public void setJycNo(String jycNo) {
		this.jycNo = jycNo;
	}
	@Transient
	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	@Transient
	public List<SalesDetail> getSdList() {
		return sdList;
	}

	public void setSdList(List<SalesDetail> sdList) {
		this.sdList = sdList;
	}

	@Column(name="car_type")
	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	@Column(name="time_money")
	public Double getTimeMoneys() {
		return timeMoneys;
	}

	public void setTimeMoneys(Double timeMoneys) {
		this.timeMoneys = timeMoneys;
	}
	@Column(name="pj_money")
	public Double getPjTotalMoney() {
		return pjTotalMoney;
	}

	public void setPjTotalMoney(Double pjTotalMoney) {
		this.pjTotalMoney = pjTotalMoney;
	}

	@Transient
	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	@Transient
	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	@Transient
	public String getYhtx() {
		return yhtx;
	}

	public void setYhtx(String yhtx) {
		this.yhtx = yhtx;
	}
	@Transient
	public String getYhNickName() {
		return yhNickName;
	}

	public void setYhNickName(String yhNickName) {
		this.yhNickName = yhNickName;
	}
	@Transient
	public String getYhdh() {
		return yhdh;
	}

	public void setYhdh(String yhdh) {
		this.yhdh = yhdh;
	}

	@Transient
	public List<TouBaoRen> getTbList() {
		return tbList;
	}

	public void setTbList(List<TouBaoRen> tbList) {
		this.tbList = tbList;
	}

	@Transient
	public String getMerSortStr() {
		return merSortStr;
	}

	public void setMerSortStr(String merSortStr) {
		this.merSortStr = merSortStr;
	}
	@Transient
	public String getMerSortNameStr() {
		return merSortNameStr;
	}

	public void setMerSortNameStr(String merSortNameStr) {
		this.merSortNameStr = merSortNameStr;
	}
	@Transient
	public double getPjfs() {
		return pjfs;
	}

	public void setPjfs(double pjfs) {
		this.pjfs = pjfs;
	}
	@Transient
	public Integer getPjrs() {
		return pjrs;
	}

	public void setPjrs(Integer pjrs) {
		this.pjrs = pjrs;
	}

	@Transient
	public String getSjplyh() {
		return sjplyh;
	}

	public void setSjplyh(String sjplyh) {
		this.sjplyh = sjplyh;
	}
	@Transient
	public String getYhplsj() {
		return yhplsj;
	}

	public void setYhplsj(String yhplsj) {
		this.yhplsj = yhplsj;
	}
	@Transient
	public Integer getSjplyhfs() {
		return sjplyhfs;
	}

	public void setSjplyhfs(Integer sjplyhfs) {
		this.sjplyhfs = sjplyhfs;
	}
	@Transient
	public Integer getYhplsjfs() {
		return yhplsjfs;
	}

	public void setYhplsjfs(Integer yhplsjfs) {
		this.yhplsjfs = yhplsjfs;
	}
	@Transient
	public String getSjplyhnr() {
		return sjplyhnr;
	}

	public void setSjplyhnr(String sjplyhnr) {
		this.sjplyhnr = sjplyhnr;
	}
	@Transient
	public String getYhplsjnr() {
		return yhplsjnr;
	}

	public void setYhplsjnr(String yhplsjnr) {
		this.yhplsjnr = yhplsjnr;
	}
	@Transient
	public String getPriceStr() {
		return priceStr;
	}

	public void setPriceStr(String priceStr) {
		this.priceStr = priceStr;
	}
	@Transient
	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}
	@Transient
	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}
	@Transient
	public String getComAddress() {
		return comAddress;
	}

	public void setComAddress(String comAddress) {
		this.comAddress = comAddress;
	}
	@Column(name="yp_price")
	public String getYpPrice() {
		return ypPrice;
	}

	public void setYpPrice(String ypPrice) {
		this.ypPrice = ypPrice;
	}
}
