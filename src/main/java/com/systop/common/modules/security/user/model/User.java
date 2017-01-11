package com.systop.common.modules.security.user.model;

import com.systop.amol.app.push.model.PushMessage;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.region.model.Region;
import com.systop.core.Constants;
import com.systop.core.model.BaseModel;
import com.systop.core.util.DateUtil;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * 用户表 The persistent class for the users database table.
 * 
 * @author BEA Workshop
 */
@Entity
@Table(name = "users", uniqueConstraints = {})
public class User extends BaseModel implements UserDetails, Serializable {

	private static final long serialVersionUID = 2979085264215825998L;

	/** Log */
	private static Log log = LogFactory.getLog(User.class);

	/** 主键 */
	private Integer id;
	
	/** 积分(app用户) */
	private Integer integral=0;
	
	/** 返给直接合伙人的钱 */
	private Double zjMoney;
	
	/** 返给间接合伙人的钱 */
	private Double jjMoney;
	/**
	 * 区域负责人积分
	 */
	private Integer dhIntegral;
	/*** 编号  加油车车牌号*/
	private String code;
	
	/** 法人姓名 */
	private String legalPersonName;

	/** 登录ID */
	private String loginId;
	private String nickName;

	/** 密码 */
	private String password;

	/** 注册时间 */
	private Date createTime;

	/** 姓名 */
	private String name;
	
	/** 身份证 */
	private String idCard;

	/** 性别 */
	private String sex;
	/** 民族 车牌号*/
	private String folk;
	/** 出生日期 */
	private Date birthday;
	/**
	 * 苹果端推送deviceToken
	 */

	/** 手机 */
	private String mobile;

	private Role role;

	/** 电话 */
	private String phone;
	/** 地址 */
	private String address;
	/** 邮编 */
	private String zip;
	/** 电子邮件 */
	private String email;
	/** 用户描述 */
	private String descn;
	/** 状态 */
	private String status;
	/** 是否系统用户 */
	private String isSys;
	/** 用户类别，app用户：app_user 加油车jyc  店铺：agent 会员:vip*/
	private String type;
	//vip类型 1.加油司机 2.普通用户
	private ProductSort vipType;
	//账号类别 1本站会员 2第三方登录
	private String loginType;
	//登录来源  qq或者微信
	private String loginLaiyuan;
	//1为合约用户
	private String ifHy;
	//充值方式
	private String fillType;
	/** 区域负责人、商家 */
	private String fxsjb;
	
	/** 上级经销商 */
	private User superior;
	
	/** 用戶是否在线，0（缺省）表示不在线，1表示在线。  */
	private String online;
	
	/** 最后登录IP */
	private String lastLoginIp;

	/** 最后登录时间 */
	private Date lastLoginTime;

	/** 登录次数 */
	private Integer loginTimes;

	/** 所属部门 */
	private Dept dept;
	
	/** 所属地区 */
	private Region region;
	
	/** 期初初始化设置是否完成,0表示未完成, 1表示已完成*/
	private String beginningInit;
	/**
	 * 安卓token·
	 */
	private String androidToken;
	/**
	 * 苹果token
	 */
	private String iosToken;
	/** 所具有的角色 */
	private Set<Role> roles = new HashSet<Role>(0);
	/** 所具有的种类 */
	private Set<ProductSort> sorts = new HashSet<ProductSort>(0);
	/**
	 * 下级经销商
	 * @author songbaojie
	 */
	private Set<User> childSuperiors = new HashSet<User>(0);
	
	/** 银行名称 */
	private String yhmc;
	
	/** 银行卡号 */
	private String yhkh;
	
	/** 所属银行 */
	private ProductSort bankSort;
	
	/** 所属银行id */
	private Integer bankId;
	
	/** 账户余额*/
	private Double allMoney;
	/**
	 * 返现金额 包含关联用户消费分拥的钱 和充值 返现 的钱
	 */
	private Double fxMoney;
	/** 收入的钱   就是用户买我的产品付钱到此账户*/
	private Double incomeAll;
	
	/** 最大提现金额 */
	private Double maxTiXianMoney;
	
	/** 冻结提现金额（从当前算起，向前推15天内的收） */
	private Double dongJieTiXianMoney;
	
	/** 用户地区id（不存储到数据中，用于显示在页面使用） */
	private Integer regionId;
	/** 用户地区名称（不存储到数据中，用于显示在页面使用） */
	private String regionName;
	//用于标识app用户是否已经申请开店 null未申请 1已经申请
	private String locX;
	private String locY;
	private String imageURL;
	private Double flMoney;
	private String visible;
	private User tjmUser;
	//店铺类别
	private ProductSort productSort;
	
	/*是否通过审核*/
	private String ifRecommend;
	
	//只用于存储评价分数用于展示
	private String allSocres;

	private Integer hitTimes;

	//用于存储商家类别字符串
	private String merSortStr;

	private String merSortNameStr;

	//所属用户
	private User shopOfUser;
	//支付密码
	private String payPass;
	//加油id   卖油商家类型id 46006272
	private Integer merId;
	//保险商家类型id 49414144
	private Integer bxMerId;
	//维修厂商家类型id 42631168
	private Integer wxMerId;

	//第三方登陆类型
	private String openType;
	//第三方登录id
	private String openId;
	private String accessToken;

	private List<String> myJyc = new ArrayList<String>();

	private String priceStr;

	private String ewmImageURl;

	private String bxyName;
	private List<Map<String,Object>> picList = new ArrayList<Map<String,Object>>();
	private List<PushMessage> pushList = new ArrayList<PushMessage>();
	//0未出车  1 出车
	private String jycstate;
	private String reigonIds;
	private String reigonNames;
	private List<Map<String,Object>> jsList = new ArrayList<Map<String,Object>>();
	private Integer superiorId;
	//审核意见
	private String shIea;
	private Double fyMoney;
	//我获得佣金 的消费者消费总金额
	private Double totalXf;
	private Double showDistance;
	//充值账户
	private Double chargeAccount;
	private Double jyddm;
	private Double wxddm;
	private Double bxddm;
	/** 缺省构造器 */
	public User() {
	}

	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "id", nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "login_id")
	public String getLoginId() {
		return this.loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	@JSON(serialize = false)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JSON(serialize = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "id_card", length = 20)
	@JSON(serialize = false)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	@JSON(serialize = false)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@JSON(serialize = false)
	public String getFolk() {
		return folk;
	}

	public void setFolk(String folk) {
		this.folk = folk;
	}

	@JSON(serialize = false)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@JSON(serialize = false)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column(name = "dh_integral")
	@JSON(serialize = false)
	public Integer getDhIntegral() {
		return dhIntegral;
	}

	public void setDhIntegral(Integer dhIntegral) {
		this.dhIntegral = dhIntegral;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@JSON(serialize = false)
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@JSON(serialize = false)
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	@JSON(serialize = false)
	public String getDescn() {
		return this.descn;
	}

	public void setDescn(String descn) {
		this.descn = descn;
	}
	
	@Column(columnDefinition = "char(1) default '1'")
	@JSON(serialize = false)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "is_sys", columnDefinition = "char(1) default '0'")
	@JSON(serialize = false)
	public String getIsSys() {
		return isSys;
	}
	
	public void setIsSys(String isSys) {
		this.isSys = isSys;
	}

	@Column(name = "type", length = 20)
	@JSON(serialize = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "superior_id")
	@JSON(serialize = false)
	public User getSuperior() {
		return superior;
	}

	public void setSuperior(User superior) {
		this.superior = superior;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "prosort_id")
	@JSON(serialize = false)
	public ProductSort getProductSort() {
		return productSort;
	}

	public void setProductSort(ProductSort productSort) {
		this.productSort = productSort;
	}

	@Column(name = "is_online", columnDefinition = "char(1) default '0'")
	@JSON(serialize = false)
	public String getOnline() {
		return online;
	}
	
	public void setOnline(String online) {
		this.online = online;
	}

	@Column(name = "last_login_ip")
	@JSON(serialize = false)
	public String getLastLoginIp() {
		return this.lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	@Column(name = "last_login_time")
	@JSON(serialize = false)
	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Transient
	@JSON(serialize = false)
	public String getLastLoginTimeStr() {
		return DateUtil.convertDateToString(lastLoginTime, "yyyy-MM-dd HH:mm");
	}

	@Column(name = "login_times")
	@JSON(serialize = false)
	public Integer getLoginTimes() {
		return this.loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_id")
	@JSON(serialize = false)
	public Dept getDept() {
		return this.dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}
	@Column(name="code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ManyToOne(cascade = {}, fetch=FetchType.LAZY)
	@JoinColumn(name = "region_id" )
	@JSON(serialize = false)
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	@Column(name = "beginning_init", columnDefinition = "char(1) default '0'")
	@JSON(serialize = false)
    public String getBeginningInit() {
		return beginningInit;
	}

	public void setBeginningInit(String beginningInit) {
		this.beginningInit = beginningInit;
	}
	
	@ManyToMany(targetEntity = Role.class, cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	@JSON(serialize = false)
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "superior")
	@JSON(serialize = false)
	public Set<User> getChildSuperiors() {
		return childSuperiors;
	}

	public void setChildSuperiors(Set<User> childSuperiors) {
		this.childSuperiors = childSuperiors;
	}


	/** 用户所具有的权限，可用通过角色-权限对应关系得到  */
	private transient GrantedAuthority[] authorities;

	/** @see {@link UserDetails#getAuthorities()} */
	@Transient
	@JSON(serialize = false)
	public GrantedAuthority[] getAuthorities() {
		return this.authorities;
	}
	/** @param authorities the authorities to set */
	public void setAuthorities(GrantedAuthority[] authorities) {
		log.info("Set GrantedAuthorities :" + Arrays.toString(authorities));
		this.authorities = authorities;
	}

	/** @see {@link UserDetails#getUsername()} */
	@Transient
	@JSON(serialize = false)
	public String getUsername() {
		return this.loginId;
	}
	/** @see {@link UserDetails#isAccountNonExpired()} */
	@Transient
	@JSON(serialize = false)
	public boolean isAccountNonExpired() {
		return true;
	}

	/** @see {@link UserDetails#isAccountNonLocked()} */
	@Transient
	@JSON(serialize = false)
	public boolean isAccountNonLocked() {
		return true;
	}

	/** {@link UserDetails#isCredentialsNonExpired()} */
	@Transient
	@JSON(serialize = false)
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**  {@link UserDetails#isEnabled()} */
	@Transient
	@JSON(serialize = false)
	public boolean isEnabled() {
		return StringUtils.equalsIgnoreCase(status, Constants.STATUS_AVAILABLE);
	}

	/** 是否有角色  */
	@Transient
	@JSON(serialize = false)
	public boolean getHasRoles() {
		return roles != null && roles.size() > 0;
	}

	/** 是否有下级经销商  */
	@Transient
	@JSON(serialize = false)
	public boolean getHasSuperiors() {
		return childSuperiors != null && childSuperiors.size() > 0;
	}
	
	@Column(name = "all_money")
	public Double getAllMoney() {
		return allMoney;
	}

	public void setAllMoney(Double allMoney) {
		this.allMoney = allMoney;
	}
	@Column(name = "income_all")
	@JSON(serialize = false)
	public Double getIncomeAll() {
		return incomeAll;
	}

	public void setIncomeAll(Double incomeAll) {
		this.incomeAll = incomeAll;
	}
	@JSON(serialize = false)
	public String getYhmc() {
		return yhmc;
	}
	public void setYhmc(String yhmc) {
		this.yhmc = yhmc;
	}
	@JSON(serialize = false)
	public String getYhkh() {
		return yhkh;
	}

	public void setYhkh(String yhkh) {
		this.yhkh = yhkh;
	}

	@JSON(serialize = false)
	@Column(name = "legal_person_name")
	public String getLegalPersonName() {
		return legalPersonName;
	}

	public void setLegalPersonName(String legalPersonName) {
		this.legalPersonName = legalPersonName;
	}

	/** @see Object#equals(Object) */
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof User)) {
			return false;
		}
		User castOther = (User) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
	}

	/** @see Object#hashCode() */
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/** @see Object#toString() */
	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}
	
	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	@Transient
	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	@Transient
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	public String getFxsjb() {
		return fxsjb;
	}

	public void setFxsjb(String fxsjb) {
		this.fxsjb = fxsjb;
	}
	@Column(name = "android_token")
	@JSON(serialize = false)
	public String getAndroidToken() {
		return androidToken;
	}

	public void setAndroidToken(String androidToken) {
		this.androidToken = androidToken;
	}
	@Column(name = "ios_token")
	@JSON(serialize = false)
	public String getIosToken() {
		return iosToken;
	}

	public void setIosToken(String iosToken) {
		this.iosToken = iosToken;
	}
	@Column(name = "loc_x")
	@JSON(serialize = false)
	public String getLocX() {
		return locX;
	}

	public void setLocX(String locX) {
		this.locX = locX;
	}
	@Column(name = "loc_y")
	@JSON(serialize = false)
	public String getLocY() {
		return locY;
	}

	public void setLocY(String locY) {
		this.locY = locY;
	}
	@Column(name = "image_url")
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	@Column(name = "fl_money")
	@JSON(serialize = false)
	public Double getFlMoney() {
		return flMoney;
	}

	public void setFlMoney(Double flMoney) {
		this.flMoney = flMoney;
	}
	
	@Column(name = "visible")
	@JSON(serialize = false)
	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}
	@Column(name = "if_recommend")
	@JSON(serialize = false)
	public String getIfRecommend() {
		return ifRecommend;
	}

	public void setIfRecommend(String ifRecommend) {
		this.ifRecommend = ifRecommend;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "bank_sort")
	@JSON(serialize = false)
	public ProductSort getBankSort() {
		return bankSort;
	}
	public void setBankSort(ProductSort bankSort) {
		this.bankSort = bankSort;
	}
	@Column(name = "zj_money")
	@JSON(serialize = false)
	public Double getZjMoney() {
		return zjMoney;
	}

	public void setZjMoney(Double zjMoney) {
		this.zjMoney = zjMoney;
	}
	@Column(name = "jj_money")
	@JSON(serialize = false)
	public Double getJjMoney() {
		return jjMoney;
	}

	public void setJjMoney(Double jjMoney) {
		this.jjMoney = jjMoney;
	}

	@Column(name = "all_score")
	@JSON(serialize = false)
	public String getAllSocres() {
		return allSocres;
	}

	public void setAllSocres(String allSocres) {
		this.allSocres = allSocres;
	}

	@Transient
	@JSON(serialize = false)
	public Double getMaxTiXianMoney() {
		return maxTiXianMoney;
	}

	public void setMaxTiXianMoney(Double maxTiXianMoney) {
		this.maxTiXianMoney = maxTiXianMoney;
	}

	@Transient
	@JSON(serialize = false)
	public Double getDongJieTiXianMoney() {
		return dongJieTiXianMoney;
	}

	public void setDongJieTiXianMoney(Double dongJieTiXianMoney) {
		this.dongJieTiXianMoney = dongJieTiXianMoney;
	}

	@Transient
	@JSON(serialize = false)
	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	@Column(name = "mersort_str")
	public String getMerSortStr() {
		return merSortStr;
	}
	public void setMerSortStr(String merSortStr) {
		this.merSortStr = merSortStr;
	}
	@Column(name = "mersortname_str")
	public String getMerSortNameStr() {
		return merSortNameStr;
	}

	public void setMerSortNameStr(String merSortNameStr) {
		this.merSortNameStr = merSortNameStr;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "shop_user_id")
	@JSON(serialize = false)
	public User getShopOfUser() {
		return shopOfUser;
	}

	public void setShopOfUser(User shopOfUser) {
		this.shopOfUser = shopOfUser;
	}


	@ManyToMany(targetEntity = Role.class, cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(name = "user_sort", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "sort_id") })
	@JSON(serialize = false)
	public Set<ProductSort> getSorts() {
		return sorts;
	}

	public void setSorts(Set<ProductSort> sorts) {
		this.sorts = sorts;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "vip_sort_id")
	@JSON(serialize = false)
	public ProductSort getVipType() {
		return vipType;
	}
	public void setVipType(ProductSort vipType) {
		this.vipType = vipType;
	}
	@Column(name = "login_type")
	@JSON(serialize = false)
	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	@Column(name = "login_laiyuan")
	@JSON(serialize = false)
	public String getLoginLaiyuan() {
		return loginLaiyuan;
	}

	public void setLoginLaiyuan(String loginLaiyuan) {
		this.loginLaiyuan = loginLaiyuan;
	}
	@Column(name = "if_hy")
	@JSON(serialize = false)
	public String getIfHy() {
		return ifHy;
	}

	public void setIfHy(String ifHy) {
		this.ifHy = ifHy;
	}
	@Column(name = "fill_type")
	@JSON(serialize = false)
	public String getFillType() {
		return fillType;
	}

	public void setFillType(String fillType) {
		this.fillType = fillType;
	}
	@Column(name = "pay_pass")
	public String getPayPass() {
		return payPass;
	}

	public void setPayPass(String payPass) {
		this.payPass = payPass;
	}

	@Transient
	public Integer getMerId() {
		return merId;
	}

	public void setMerId(Integer merId) {
		this.merId = merId;
	}
	@Column(name = "hit_times")
	public Integer getHitTimes() {
		return hitTimes;
	}

	public void setHitTimes(Integer hitTimes) {
		this.hitTimes = hitTimes;
	}
	@Transient
	public List<String> getMyJyc() {
		return myJyc;
	}
	public void setMyJyc(List<String> myJyc) {
		this.myJyc = myJyc;
	}
	@Column(name = "nick_name")
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	@Transient
	public String getPriceStr() {
		return priceStr;
	}

	public void setPriceStr(String priceStr) {
		this.priceStr = priceStr;
	}
	@Column(name="ewm_image")
	public String getEwmImageURl() {
		return ewmImageURl;
	}

	public void setEwmImageURl(String ewmImageURl) {
		this.ewmImageURl = ewmImageURl;
	}
	@Column(name="open_type")
	public String getOpenType() {
		return openType;
	}
	public void setOpenType(String openType) {
		this.openType = openType;
	}
	@Column(name="open_id")
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	@Column(name="access_token")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "tjm_user")
	@JSON(serialize = false)
	public User getTjmUser() {
		return tjmUser;
	}

	public void setTjmUser(User tjmUser) {
		this.tjmUser = tjmUser;
	}
	@Column(name="bxy_name")
	public String getBxyName() {
		return bxyName;
	}

	public void setBxyName(String bxyName) {
		this.bxyName = bxyName;
	}
	@Transient
	public List<Map<String, Object>> getPicList() {
		return picList;
	}

	public void setPicList(List<Map<String, Object>> picList) {
		this.picList = picList;
	}


	public Integer getBxMerId() {
		return bxMerId;
	}

	public void setBxMerId(Integer bxMerId) {
		this.bxMerId = bxMerId;
	}

	public Integer getWxMerId() {
		return wxMerId;
	}

	public void setWxMerId(Integer wxMerId) {
		this.wxMerId = wxMerId;
	}
	@Transient
	public List<PushMessage> getPushList() {
		return pushList;
	}

	public void setPushList(List<PushMessage> pushList) {
		this.pushList = pushList;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	@JSON(serialize = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	@Column(name="jyc_state")
	public String getJycstate() {
		return jycstate;
	}

	public void setJycstate(String jycstate) {
		this.jycstate = jycstate;
	}
	@Transient
	public String getReigonIds() {
		return reigonIds;
	}

	public void setReigonIds(String reigonIds) {
		this.reigonIds = reigonIds;
	}
	@Transient
	public String getReigonNames() {
		return reigonNames;
	}

	public void setReigonNames(String reigonNames) {
		this.reigonNames = reigonNames;
	}
	@Transient
	public List<Map<String, Object>> getJsList() {
		return jsList;
	}

	public void setJsList(List<Map<String, Object>> jsList) {
		this.jsList = jsList;
	}
	@Transient
	public Integer getSuperiorId() {
		return superiorId;
	}

	public void setSuperiorId(Integer superiorId) {
		this.superiorId = superiorId;
	}
	@Column(name="sh_idea")
	public String getShIea() {
		return shIea;
	}

	public void setShIea(String shIea) {
		this.shIea = shIea;
	}
	@Column(name="fx_money")
	public Double getFxMoney() {
		return fxMoney;
	}

	public void setFxMoney(Double fxMoney) {
		this.fxMoney = fxMoney;
	}
	@Column(name="fy_money")
	public Double getFyMoney() {
		return fyMoney;
	}

	public void setFyMoney(Double fyMoney) {
		this.fyMoney = fyMoney;
	}
	@Column(name="total_xf")
	public Double getTotalXf() {
		return totalXf;
	}

	public void setTotalXf(Double totalXf) {
		this.totalXf = totalXf;
	}

	@Transient
	public Double getShowDistance() {
		return showDistance;
	}

	public void setShowDistance(Double showDistance) {
		this.showDistance = showDistance;
	}

	@Column(name="charge_account")
	public Double getChargeAccount() {
		return chargeAccount;
	}

	public void setChargeAccount(Double chargeAccount) {
		this.chargeAccount = chargeAccount;
	}
	@Transient
	public Double getJyddm() {
		return jyddm;
	}

	public void setJyddm(Double jyddm) {
		this.jyddm = jyddm;
	}
	@Transient
	public Double getWxddm() {
		return wxddm;
	}

	public void setWxddm(Double wxddm) {
		this.wxddm = wxddm;
	}
	@Transient
	public Double getBxddm() {
		return bxddm;
	}

	public void setBxddm(Double bxddm) {
		this.bxddm = bxddm;
	}
}