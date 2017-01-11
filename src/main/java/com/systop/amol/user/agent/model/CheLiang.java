package com.systop.amol.user.agent.model;

import com.systop.common.modules.region.model.Region;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "che_liang")
public class CheLiang extends BaseModel implements Cloneable{
//把车辆价格、表显里程、使用年数、过户次数改成字符串类型
    /** 主键 */
    private Integer id;
    //商家对象
    private User user;
    private Date createTime;
    private String clName;
    private Double clPrice;
    private String milinary;
    private Double usedYears;
    private String relatePhone;
    private String shangpaiDate;
    private String guohucishu;
    private Region region;
    private String bxStopDate;
    private String njStopDate;
    private String publisher;
    private List<RzFile> rzList;
    private String imageUrl;
    private String regionName;
    //0 未审核 -1 审核不通过 1审核通过
    private String visible;
    private String idea;
    private Integer hitTimes;
    private String clXh;
    private String grand;
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
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JSON(serialize = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "cl_name")
    public String getClName() {
        return clName;
    }

    public void setClName(String clName) {
        this.clName = clName;
    }
    @Column(name = "cl_price")
    public Double getClPrice() {
        return clPrice;
    }

    public void setClPrice(Double clPrice) {
        this.clPrice = clPrice;
    }
    @Column(name = "milinay")
    public String getMilinary() {
        return milinary;
    }

    public void setMilinary(String milinary) {
        this.milinary = milinary;
    }
    @Column(name = "used_years")
    public Double getUsedYears() {
        return usedYears;
    }

    public void setUsedYears(Double usedYears) {
        this.usedYears = usedYears;
    }
    @Column(name = "relate_phone")
    public String getRelatePhone() {
        return relatePhone;
    }

    public void setRelatePhone(String relatePhone) {
        this.relatePhone = relatePhone;
    }
    @Column(name = "shangpai_Date")
    public String getShangpaiDate() {
        return shangpaiDate;
    }

    public void setShangpaiDate(String shangpaiDate) {
        this.shangpaiDate = shangpaiDate;
    }
    @Column(name = "guohu_cishu")
    public String getGuohucishu() {
        return guohucishu;
    }

    public void setGuohucishu(String guohucishu) {
        this.guohucishu = guohucishu;
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
    @Column(name = "bxstop_date")
    public String getBxStopDate() {
        return bxStopDate;
    }

    public void setBxStopDate(String bxStopDate) {
        this.bxStopDate = bxStopDate;
    }
    @Column(name = "njstop_date")
    public String getNjStopDate() {
        return njStopDate;
    }

    public void setNjStopDate(String njStopDate) {
        this.njStopDate = njStopDate;
    }
    @Column(name = "pushlisher")
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    @Transient
    public List<RzFile> getRzList() {
        return rzList;
    }

    public void setRzList(List<RzFile> rzList) {
        this.rzList = rzList;
    }
    @Transient
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    @Transient
    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Column(name = "visible")
    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }
    @Column(name = "idea")
    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    @Column(name = "hit_times")
    public Integer getHitTimes() {
        return hitTimes;
    }

    public void setHitTimes(Integer hitTimes) {
        this.hitTimes = hitTimes;
    }
    @Column(name = "cl_xh")
    public String getClXh() {
        return clXh;
    }

    public void setClXh(String clXh) {
        this.clXh = clXh;
    }
    @Column(name = "grand")
    public String getGrand() {
        return grand;
    }

    public void setGrand(String grand) {
        this.grand = grand;
    }
}
