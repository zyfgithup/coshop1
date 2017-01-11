package com.systop.amol.user.agent.model;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "yh_shenqing")
public class YhShenqing extends BaseModel implements Cloneable{

    /** 主键 */
    private Integer id;
    //提交申请人
    private User user;

    private Date createTime;

    private String createTimeStr;

    private String sex;

    private String name;

    private String age;

    //求职意向
    private String qzyx;

    private String relatePhone;

    //自我评价
    private String zwpj;

    //0 二手车 1 求职
    private String type;

    private ZhaoPin zhaoPin;

    private CheLiang cheLiang;

    private String nickName;
    private String imageURL;
    private List<ZhaoPin> zpList = new ArrayList<ZhaoPin>();
    private List<CheLiang> clList = new ArrayList<CheLiang>();


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
    @JSON(serialize = false)
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Transient
    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    @Column(name = "sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "age")
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
    @Column(name = "qzyx")
    public String getQzyx() {
        return qzyx;
    }

    public void setQzyx(String qzyx) {
        this.qzyx = qzyx;
    }
    @Column(name = "relate_phone")
    public String getRelatePhone() {
        return relatePhone;
    }

    public void setRelatePhone(String relatePhone) {
        this.relatePhone = relatePhone;
    }
    @Column(name = "zwpj")
    public String getZwpj() {
        return zwpj;
    }

    public void setZwpj(String zwpj) {
        this.zwpj = zwpj;
    }
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "zp_id")
    @JSON(serialize = false)
    public ZhaoPin getZhaoPin() {
        return zhaoPin;
    }
    public void setZhaoPin(ZhaoPin zhaoPin) {
        this.zhaoPin = zhaoPin;
    }
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "cl_id")
    @JSON(serialize = false)
    public CheLiang getCheLiang() {
        return cheLiang;
    }

    public void setCheLiang(CheLiang cheLiang) {
        this.cheLiang = cheLiang;
    }
    @Transient
    public String getNickName() {
        return nickName;
    }
    @Transient
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    @Transient
    public List<ZhaoPin> getZpList() {
        return zpList;
    }

    public void setZpList(List<ZhaoPin> zpList) {
        this.zpList = zpList;
    }
    @Transient
    public List<CheLiang> getClList() {
        return clList;
    }

    public void setClList(List<CheLiang> clList) {
        this.clList = clList;
    }
}
