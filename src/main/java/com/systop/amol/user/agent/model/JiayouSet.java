package com.systop.amol.user.agent.model;

import com.systop.common.modules.region.model.Region;
import com.systop.core.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/5.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "jiayou_set")
public class JiayouSet extends BaseModel implements Cloneable{

    /** 主键 */
    private Integer id;
    //商家对象
    private Region region;

    private Date createTime;
    private String kindName;
    //合约用户价格
    private Double hyPrice;
    //个人用户价格
    private Double grPrice;
    private String regionName;
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
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
    @Column(name = "kind_name")
    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }
    @Column(name = "hy_price")
    public Double getHyPrice() {
        return hyPrice;
    }

    public void setHyPrice(Double hyPrice) {
        this.hyPrice = hyPrice;
    }
    @Column(name = "gr_price")
    public Double getGrPrice() {
        return grPrice;
    }

    public void setGrPrice(Double grPrice) {
        this.grPrice = grPrice;
    }
    @Column(name = "region_name")
    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
