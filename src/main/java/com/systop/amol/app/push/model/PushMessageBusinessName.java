package com.systop.amol.app.push.model;

import java.util.Date;

import com.systop.amol.app.push.Status;
import com.systop.core.model.BaseModel;

/**
 * <p>
 * 	消息
 * </p>
 * @version 1.0
 * @author 王会璞
 */
public class PushMessageBusinessName extends BaseModel {

	private static final long serialVersionUID = 3057982448636855496L;

	private Integer id;
	
	/** 创建时间 */
	private Date createTime = new Date();
	
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
	/** 商家名称 */
	private String businessName;
	/** 消息状态 */
	private Status status = Status.UNSENT;
	/** 是否可见 * */
	private Boolean visible = true;
	/** 1：push消息，2首页显示信息 */
	private Integer type;
	/** 图片地址 */
	private String imageURL;
	
	public PushMessageBusinessName(){}
	
	public PushMessageBusinessName(Integer id, Date createTime, Date startTime,
			Date endTime,String title, String content, Integer businessId) {
		this.id = id;
		this.createTime = createTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.title = title;
		this.content = content;
		this.businessId = businessId;
	}

	public Integer getId() {
		return id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

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

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

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

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
}
