package com.systop.amol.user.agent.model;

import com.systop.amol.base.express.model.ExpressCompany;
import com.systop.amol.sales.model.Sales;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "receive_address")
public class ReceiveAddress extends BaseModel implements Cloneable{
	
	/** 主键 */
	private Integer id;
	//
	private User user;

	private User merUser;
	private Date createTime;
	//默认收货地址设置
	//1默认收货地址 0是非默认收货地址
	private Integer ifMrAddr;
	
	private String address;
	
	private String postCode;
	
	private String receiveName;
	
	private String receivePhone;
	
	private String visible="1";

	private ExpressCompany expressCompany;

	private Set<Sales> set = new HashSet<Sales>();

	private Region region;

	private String ifHaveKp;

	private Integer userId;

	private Set<Sales> showSet = new HashSet<Sales>();

	private String comAddr;

	private String comfpbh;

	private String comName;

	private String comPhone;

	private String comBankName;

	private String cardNo;

	private  String saxNo;

	private String imagerURL;

	private String regionNames;

	private String nickName;

	private List<ForfpShow> fList = new ArrayList<ForfpShow>();

	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "id", nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JSON(serialize = false)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "if_mrAddr")
	public Integer getIfMrAddr() {
		return ifMrAddr;
	}

	public void setIfMrAddr(Integer ifMrAddr) {
		this.ifMrAddr = ifMrAddr;
	}
	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name = "post_code")
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	@Column(name = "receive_name")
	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	@Column(name = "receive_phone")
	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}
	@JSON(serialize = false)
	@Column(name = "visible")
	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JSON(serialize = false)
	@JoinColumn(name = "com_id")
	public ExpressCompany getExpressCompany() {
		return expressCompany;
	}
	public void setExpressCompany(ExpressCompany expressCompany) {
		this.expressCompany = expressCompany;
	}
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "address")
	@JSON(serialize = false)
	public Set<Sales> getSet() {
		return set;
	}
	public void setSet(Set<Sales> set) {
		this.set = set;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JSON(serialize = false)
	@JoinColumn(name = "region_id")
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JSON(serialize = false)
	@JoinColumn(name = "meruser_id")
	public User getMerUser() {
		return merUser;
	}

	public void setMerUser(User merUser) {
		this.merUser = merUser;
	}

	@Column(name="if_hava_kp")
	public String getIfHaveKp() {
		return ifHaveKp;
	}

	public void setIfHaveKp(String ifHaveKp) {
		this.ifHaveKp = ifHaveKp;
	}
	@Transient

	public Set<Sales> getShowSet() {
		return showSet;
	}

	public void setShowSet(Set<Sales> showSet) {
		this.showSet = showSet;
	}

	@Transient
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	@Column(name="com_addr")
	public String getComAddr() {
		return comAddr;
	}

	public void setComAddr(String comAddr) {
		this.comAddr = comAddr;
	}
	@Column(name="com_fpbh")
	public String getComfpbh() {
		return comfpbh;
	}

	public void setComfpbh(String comfpbh) {
		this.comfpbh = comfpbh;
	}
	@Column(name="com_name")
	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}
	@Column(name="com_phone")
	public String getComPhone() {
		return comPhone;
	}

	public void setComPhone(String comPhone) {
		this.comPhone = comPhone;
	}
	@Column(name="com_bankname")
	public String getComBankName() {
		return comBankName;
	}

	public void setComBankName(String comBankName) {
		this.comBankName = comBankName;
	}
	@Column(name="card_no")
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	@Column(name="sax_no")
	public String getSaxNo() {
		return saxNo;
	}

	public void setSaxNo(String saxNo) {
		this.saxNo = saxNo;
	}
	@Column(name="imageURL")
	public String getImagerURL() {
		return imagerURL;
	}

	public void setImagerURL(String imagerURL) {
		this.imagerURL = imagerURL;
	}

	@Transient
	public String getRegionNames() {
		return regionNames;
	}

	public void setRegionNames(String regionNames) {
		this.regionNames = regionNames;
	}
	@Transient
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	@Transient
	public List<ForfpShow> getfList() {
		return fList;
	}

	public void setfList(List<ForfpShow> fList) {
		this.fList = fList;
	}

}
