package com.systop.amol.app.push.model;

import com.systop.core.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/27.
 */
@Entity
@Table(name = "adv_position")
public class AdvPosition extends BaseModel {

    private Integer id;

    /** 创建时间 */
    private Date createTime = new Date();
    //广告位名称，广告位宽度、高度，广告位描述，是否核心广告位

    private String name;
    private Double width;
    private Double height;
    private String remark;
    //0非核心 1核心
    private String ifCoreAdv;

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
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "width")
    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }
    @Column(name = "height")
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIfCoreAdv() {
        return ifCoreAdv;
    }

    public void setIfCoreAdv(String ifCoreAdv) {
        this.ifCoreAdv = ifCoreAdv;
    }
}
