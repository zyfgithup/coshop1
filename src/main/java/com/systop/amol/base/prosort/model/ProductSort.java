package com.systop.amol.base.prosort.model;

import com.systop.amol.app.push.model.PushMessage;
import com.systop.amol.base.yyy.model.YyyEvent;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

/**
 * 商品类型
 * 
 * @author songbaojie
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "products_sort")
public class ProductSort extends BaseModel {

	// 主键
	private Integer id;

	// 商品类型名称
	private String name;

	// 上级商品类型
	private ProductSort parentProsort;

	// 下级商品类型
	private List<ProductSort> childProductSorts = new ArrayList<ProductSort>();

	private Set<User> childUsers=new HashSet<User>(0);
	
	private Set<User> childBankUsers=new HashSet<User>(0);
	// 商品类型编号规则：两位数字，从1自动排；
	private String serialNo;

	// 创建者
	private User creator;

	// 创建时间
	private Date createTime;

	// 商品类型备注描述
	private String descn;

	private Set<YyyEvent> childYyyEvents=new HashSet<YyyEvent>(0);

	private Double price;


	/**
	 * 具有此角色的用户
	 */
	private Set<User> users = new HashSet<User>(0);

	// 状态
	// 1可用
	// 0禁用
	private String status;

	// 得到所有的商品类型
	private List<ProductSort> prosorts;

	//类别1为商品类型，  2 为商家类型 4是二手车类型
	private String type;
	private String imageURL;
	
	private String isYbl;
	
	private Integer rowNum;
	
	/** 是否在手机客户端隐藏该记录信息，null或非ProductSortConstants.HIDE_PHONE_CLIENT显示在客户端该记录，值ProductSortConstants.HIDE_PHONE_CLIENT在客户端不显示该记录 */
	private String hidePhoneClient;

	/** 积分app客户端显示标示 */
	private String showJfsc;

	/** 特价购app客户端显示标示 */
	private String showTjg;

	/**
	 * 默认构造
	 */
	public ProductSort() {
	}

	/**
	 * 为在Red5中应用中属性查询而加的构造
	 * 
	 * @param id
	 * @param parentProductType
	 */
	public ProductSort(Integer id, ProductSort parentProsort) {
		this.id = id;
		this.parentProsort = parentProsort;
	}

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

	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@JSON(serialize = false)
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	public ProductSort getParentProsort() {
		return parentProsort;
	}

	public void setParentProsort(ProductSort parentProsort) {
		this.parentProsort = parentProsort;
	}
	@JSON(serialize = false)
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "productSort")
	public Set<User> getChildUsers() {
		return childUsers;
	}

	public void setChildUsers(Set<User> childUsers) {
		this.childUsers = childUsers;
	}

	@JSON(serialize = false)
	@Column(name = "CreateTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@JSON(serialize = false)
	@Column(name = "descn")
	@Type(type = "text")
	public String getDescn() {
		return descn;
	}

	public void setDescn(String descn) {
		this.descn = descn;
	}
	@JSON(serialize = false)
	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@JSON(serialize = false)
	@Column(name = "status", columnDefinition = "char(1) default '1'")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	@JSON(serialize = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id")
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
	@JSON(serialize = false)
	@Column(name = "serial_no")
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	@Transient
	public List<ProductSort> getProsorts() {
		return prosorts;
	}

	public void setProsorts(List<ProductSort> prosorts) {
		this.prosorts = prosorts;
	}

	@Transient
//	@JSON(serialize = false)
	public boolean getHasChild() {
		return this.getChildProductSorts().size() > 0;
	}

	@Column(name = "imageURL")
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	@Column(name = "is_ybl")
	@JSON(serialize = false)
	public String getIsYbl() {
		return isYbl;
	}

	public void setIsYbl(String isYbl) {
		this.isYbl = isYbl;
	}
	@JSON(serialize = false)
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "bankSort")
	public Set<User> getChildBankUsers() {
		return childBankUsers;
	}

	public void setChildBankUsers(Set<User> childBankUsers) {
		this.childBankUsers = childBankUsers;
	}
	@Column(name = "row_num")
	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProductSort)) {
			return false;
		}
		ProductSort castOther = (ProductSort) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId())
				.isEquals();
	}
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}
	
	/**
	 * 返回model生成的inser语句
	 * @return String sql语句
	 */
	public String toInsertSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO `products_sort` (`id`, `CreateTime`, `descn`, `name`, `serial_no`, `status`, `creator_id`, `parent_id`) VALUES(");
		sql.append(id);
		sql.append(", '");
		sql.append(createTime);
		sql.append("', '");
		sql.append(descn);
		sql.append("', '");
		sql.append(name);
		sql.append("', '");
		sql.append(serialNo);
		sql.append("',");
		if(StringUtils.isNotBlank(status)){
			sql.append("'"); 
			sql.append(status);
			sql.append("'"); 
		}else{
			sql.append("default");
		}
		sql.append(", '");
		sql.append(creator.getId());
		sql.append("',");
		if(parentProsort != null && parentProsort.getId() != null){
			sql.append(parentProsort.getId());
		}else{
			sql.append("NULL");
		}
		sql.append(")");
		return sql.toString();
	}

	@Column(name = "hide_phone_client")
	@JSON(serialize = false)
	public String getHidePhoneClient() {
		return hidePhoneClient;
	}

	public void setHidePhoneClient(String hidePhoneClient) {
		this.hidePhoneClient = hidePhoneClient;
	}
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parentProsort")
	public List<ProductSort> getChildProductSorts() {
		return childProductSorts;
	}

	public void setChildProductSorts(List<ProductSort> childProductSorts) {
		this.childProductSorts = childProductSorts;
	}
	@Column(name = "show_jfsc")
	@JSON(serialize = false)
	public String getShowJfsc() {
		return showJfsc;
	}

	public void setShowJfsc(String showJfsc) {
		this.showJfsc = showJfsc;
	}
	@Column(name = "show_tjg")
	@JSON(serialize = false)
	public String getShowTjg() {
		return showTjg;
	}

	public void setShowTjg(String showTjg) {
		this.showTjg = showTjg;
	}
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "school")
	@JSON(serialize = false)
	public Set<YyyEvent> getChildYyyEvents() {
		return childYyyEvents;
	}
	public void setChildYyyEvents(Set<YyyEvent> childYyyEvents) {
		this.childYyyEvents = childYyyEvents;
	}
	@ManyToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "sorts", targetEntity = User.class)
	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	@Column(name = "price")
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
