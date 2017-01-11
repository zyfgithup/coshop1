package com.systop.amol.base.yyy.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

@SuppressWarnings("serial")
@Entity
@Table(name = "yyy_record")
public class YyyRecords extends BaseModel{
	private Integer id;
	//创建时间
	private Date createTime;
	//摇一摇的app用户
	private User user;
	//摇一摇活动对像
	private YyyEvent yyyEvent;
	//中奖标识（1中奖了0没有中奖）
	private String isZj;
	private String userNo;
	private String proName;
	private Integer xhJf;
	private String imageUrl;
	//是否领取奖品1已经领取 0 未领取
	private String flag;
	private YyyAward yyyAward;
	private Region region;
	private String jx;
	@Id
	@GeneratedValue(generator = "hibseq")
	@GenericGenerator(name = "hibseq", strategy = "hilo")
	@Column(name = "Id", nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	 @JoinColumn(name = "yyy_id")
	@JSON(serialize = false)
	public YyyEvent getYyyEvent() {
		return yyyEvent;
	}
	public void setYyyEvent(YyyEvent yyyEvent) {
		this.yyyEvent = yyyEvent;
	}
	@JoinColumn(name = "is_zj")
	public String getIsZj() {
		return isZj;
	}
	public void setIsZj(String isZj) {
		this.isZj = isZj;
	}
	@JSON(serialize = false)
	@JoinColumn(name = "user_no")
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	
	@Transient
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	@Transient
	public Integer getXhJf() {
		return xhJf;
	}
	public void setXhJf(Integer xhJf) {
		this.xhJf = xhJf;
	}
	@Transient
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@Column(name="flag")
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "yyy_award_id")
	@JSON(serialize = false)
	public YyyAward getYyyAward() {
		return yyyAward;
	}
	public void setYyyAward(YyyAward yyyAward) {
		this.yyyAward = yyyAward;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id")
	@JSON(serialize = false)
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	@Transient
	public String getJx() {
		return jx;
	}
	public void setJx(String jx) {
		this.jx = jx;
	}
	
	
	
	
	
	
	

}
