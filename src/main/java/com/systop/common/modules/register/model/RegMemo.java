package com.systop.common.modules.register.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.systop.core.model.BaseModel;

/**
 * 注册说明信息
 * 
 * @author Nice
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "reg_memo", uniqueConstraints = {})
public class RegMemo extends BaseModel{

	/**  主键 */
	private Integer id;

	/** 注册说明具体内容 */
	private String content;

	/**  缺省构造器 */
	public RegMemo() {
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

	@Type(type = "text")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
