package com.systop.amol.app.update.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.systop.core.model.BaseModel;


/**
 * <p>
 * 	更新软件model
 * </p>
 * @version 1.0
 * @author 王会璞
 */
@Entity
@Table(name = "update_app")
public class UpdateApp extends BaseModel implements Cloneable {

	private static final long serialVersionUID = 5648078867379572156L;

	/** 主键 */
	private Integer id;
	
	private Date createTime;
	
	/** 版本号 **/
	private String versionCode;
	
	/** 版本名称 */
  private String versionName;
	
	/** app地址 */
	private String appURL;

	/** 是否可见 * */
	private Boolean visible = true;

	public UpdateApp(){
		
	}
	
	public UpdateApp(String versionCode, String versionName, String appURL) {
		this.versionCode = versionCode;
		this.versionName = versionName;
		this.appURL = appURL;
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

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getAppURL() {
		return appURL;
	}

	public void setAppURL(String appURL) {
		this.appURL = appURL;
	}

	@Column(nullable = false)
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}