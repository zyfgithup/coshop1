package com.systop.amol.base.yyy.model;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
@SuppressWarnings("serial")
@Entity
@Table(name = "stu_gzrecord")
public class YyyAward extends BaseModel{
	private Integer id;
	//跟踪内容
	private String gzContent;
	//创建时间
	private Date createTime;
	//所属学员
	private YyyEvent yyyEvent;
	//创建人
	private User createUser;

	private String showTime;

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
	@JSON(serialize = false)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	 @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	 @JoinColumn(name = "user_id")
	 @JSON(serialize = false)
	public User getCreateUser() {
		return createUser;
	}
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "yyy_event_id")
	@JSON(serialize = false)
	public YyyEvent getYyyEvent() {
		return yyyEvent;
	}

	public void setYyyEvent(YyyEvent yyyEvent) {
		this.yyyEvent = yyyEvent;
	}
	@Column(name = "gz_content")
	public String getGzContent() {
		return gzContent;
	}

	public void setGzContent(String gzContent) {
		this.gzContent = gzContent;
	}
	@Transient
	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
}
