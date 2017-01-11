package com.systop.amol.base.jfproduct.model;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "jf_princeple")
public class JfPrinceple extends BaseModel {

	private Integer id;

	private String title;
	private String content;
	// 用户对象
	private User user;
	/**
	 <option value="1">支付帮助</option>
	 <option value="2">关于我们</option>
	 <option value="3">用户指南</option>
	 <option value="4">联系我们</option>
	 <option value="5">法律条款</option>
	 <option value="6">用户协议</option>
	 */
	private String wzType;

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
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JSON(serialize = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@Column(name = "jf_num")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name = "money")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "wz_type")
	public String getWzType() {
		return wzType;
	}

	public void setWzType(String wzType) {
		this.wzType = wzType;
	}
}
