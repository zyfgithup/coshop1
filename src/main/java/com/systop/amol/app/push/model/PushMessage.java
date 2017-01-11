package com.systop.amol.app.push.model;

import com.systop.amol.app.push.Status;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>
 * 	消息
 * </p>
 * @version 1.0
 * @author 王会璞
 */
@Entity
@Table(name = "push_message")
public class PushMessage extends BaseModel {

	private static final long serialVersionUID = 2728211882636782625L;

	private Integer id;
	
	/** 创建时间 */
	private Date createTime = new Date();
	/** 推送时间 */
	private Date pushMessageTime = new Date();
	/** 开始时间 */
	private Date startTime;
	/** 结束时间 */
	private Date endTime;
	/** 标题 */
	private String title;
	/** 内容 */
	private String content;
	/** 商家id */
	private Integer businessId;
	/** 所属地区 */
	private Region region;
	/** 消息状态 */
	private Status status = Status.UNSENT;
	/** 是否可见 * */
	private Boolean visible = true;
	/** 1：push消息，2首页显示信息 0首页轮播图 3广告 4维修厂产品图片 */
	private Integer type;
	/** 图片地址 */
	private String imageURL;

	private User picOfUser;

	private String advUrl;

	private AdvPosition advPosition;

	private ProductSort productSort;
	
	public PushMessage(){}
	


	public PushMessage(Integer id, Date createTime, Date pushMessageTime,
			Date startTime, Date endTime, String title, String content,
			Integer businessId, Region region, Status status, Boolean visible,
			Integer type, String imageURL) {
		super();
		this.id = id;
		this.createTime = createTime;
		this.pushMessageTime = pushMessageTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.title = title;
		this.content = content;
		this.businessId = businessId;
		this.region = region;
		this.status = status;
		this.visible = visible;
		this.type = type;
		this.imageURL = imageURL;
	}



	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "id", nullable = false)
	public Integer getId() {
		return id;
	}

	@JSON(serialize = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JSON(serialize = false)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JSON(serialize = false)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JSON(serialize = false)
	public Integer getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JSON(serialize = false)
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	@JSON(serialize = false)
	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Date getPushMessageTime() {
		return pushMessageTime;
	}

	public void setPushMessageTime(Date pushMessageTime) {
		this.pushMessageTime = pushMessageTime;
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
	@ManyToOne(cascade = {}, fetch=FetchType.LAZY)
	@JoinColumn(name = "user_id" )
	@JSON(serialize = false)
	public User getPicOfUser() {
		return picOfUser;
	}

	public void setPicOfUser(User picOfUser) {
		this.picOfUser = picOfUser;
	}
	@Column(name ="adv_url")
	public String getAdvUrl() {
		return advUrl;
	}

	public void setAdvUrl(String advUrl) {
		this.advUrl = advUrl;
	}
	@ManyToOne(cascade = {}, fetch=FetchType.LAZY)
	@JoinColumn(name = "advp_id" )
	@JSON(serialize = false)
	public AdvPosition getAdvPosition() {
		return advPosition;
	}

	public void setAdvPosition(AdvPosition advPosition) {
		this.advPosition = advPosition;
	}
	@ManyToOne(cascade = {}, fetch=FetchType.LAZY)
	@JoinColumn(name = "prosort_id" )
	@JSON(serialize = false)
	public ProductSort getProductSort() {
		return productSort;
	}

	public void setProductSort(ProductSort productSort) {
		this.productSort = productSort;
	}
}
