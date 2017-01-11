package com.systop.amol.user.agent.model;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "ren_zheng")
public class RenZheng extends BaseModel implements Cloneable{

    /** 主键 */
    private Integer id;
    //商家对象
    private User user;
    private Date createTime;
    //认证类型 1 保险认证2 加油车3 企业4 实名认证5 维修厂6加油站认证
    private String rzType;
    private String name;
    private String phone;
    private String bankCard;
    private String address;
    //0 未认证  1认证中 2 已经认证
    private String iftg;
    private String shIdea;
    private List<RzFile> rzFileList;
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

    @Column(name = "rz_type")
    public String getRzType() {
        return rzType;
    }

    public void setRzType(String rzType) {
        this.rzType = rzType;
    }
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Column(name = "bank_card")
    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "if_tg")
    public String getIftg() {
        return iftg;
    }

    public void setIftg(String iftg) {
        this.iftg = iftg;
    }
    @Column(name = "sh_idea")
    public String getShIdea() {
        return shIdea;
    }

    public void setShIdea(String shIdea) {
        this.shIdea = shIdea;
    }

    @Transient
    public List<RzFile> getRzFileList() {
        return rzFileList;
    }

    public void setRzFileList(List<RzFile> rzFileList) {
        this.rzFileList = rzFileList;
    }
}
