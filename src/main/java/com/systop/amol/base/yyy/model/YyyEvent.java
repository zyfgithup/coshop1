package com.systop.amol.base.yyy.model;

import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
@Entity
@Table(name = "stu_info")
public class YyyEvent extends BaseModel{
	private Integer id;
	//录入时间
	private Date createTime;
	//创建人【市场专员】
	private User createUser;
	private String stuName;
	private String stuSex;
	//所属学校
	private ProductSort school;
	//所属系部专业
	private ProductSort xbzy;
	//宿舍号
	private String domNum;
	//QQ号
	private String stuQq;
	//微信号
	private String wxNo;
	//手机号
	private  String stuPhone;
	//班主任/导员
	private String bzrName;
	//联系电话
	private String relatePhone;
	//担任职务
	private String trzw;
	//籍贯
	private Region region;
	private String imageURL;
	private Set<YyyAward> childYyyAwards = new HashSet<YyyAward>(0);
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
	@Column(name = "stu_name")
	@JSON(serialize = false)
	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	@Column(name = "stu_sex")
	public String getStuSex() {
		return stuSex;
	}

	public void setStuSex(String stuSex) {
		this.stuSex = stuSex;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id")
	@JSON(serialize = false)
	public ProductSort getSchool() {
		return school;
	}
	public void setSchool(ProductSort school) {
		this.school = school;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "xbzy_id")
	@JSON(serialize = false)
	public ProductSort getXbzy() {
		return xbzy;
	}

	public void setXbzy(ProductSort xbzy) {
		this.xbzy = xbzy;
	}
	@Column(name = "dom_num")
	public String getDomNum() {
		return domNum;
	}

	public void setDomNum(String domNum) {
		this.domNum = domNum;
	}
	@Column(name = "stu_qq")
	public String getStuQq() {
		return stuQq;
	}

	public void setStuQq(String stuQq) {
		this.stuQq = stuQq;
	}
	@Column(name = "stu_wx")
	public String getWxNo() {
		return wxNo;
	}

	public void setWxNo(String wxNo) {
		this.wxNo = wxNo;
	}
	@Column(name = "stu_phone")
	public String getStuPhone() {
		return stuPhone;
	}

	public void setStuPhone(String stuPhone) {
		this.stuPhone = stuPhone;
	}
	@Column(name = "bzr_name")
	public String getBzrName() {
		return bzrName;
	}

	public void setBzrName(String bzrName) {
		this.bzrName = bzrName;
	}
	@Column(name = "relate_phone")
	public String getRelatePhone() {
		return relatePhone;
	}

	public void setRelatePhone(String relatePhone) {
		this.relatePhone = relatePhone;
	}
	@Column(name = "stu_drzw")
	public String getTrzw() {
		return trzw;
	}

	public void setTrzw(String trzw) {
		this.trzw = trzw;
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
	@Column(name = "imageURL")
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	@JSON(serialize = false)
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "yyyEvent")
	public Set<YyyAward> getChildYyyAwards() {
		return childYyyAwards;
	}
	public void setChildYyyAwards(Set<YyyAward> childYyyAwards) {
		this.childYyyAwards = childYyyAwards;
	}
}
