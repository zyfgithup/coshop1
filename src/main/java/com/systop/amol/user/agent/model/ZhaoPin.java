package com.systop.amol.user.agent.model;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/23.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "zhaopin")
public class ZhaoPin extends BaseModel implements Cloneable{

    /** 主键 */
    private Integer id;
    //商家对象
    private User user;

    private Date createTime;

    private String comName;
    private String zwName;
    private String zpNums;
    private String daiyu;
    private String relatePhone;
    private String email;
    private String comAddress;
    private String ypyaoqiu;
    private String stopDate;

    private String desn;
    private String visible;
    private String idea;
    private Integer hitTimes;
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
    @Column(name = "com_name")
    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }
    @Column(name = "zw_name")
    public String getZwName() {
        return zwName;
    }

    public void setZwName(String zwName) {
        this.zwName = zwName;
    }
    @Column(name = "zp_nums")
    public String getZpNums() {
        return zpNums;
    }

    public void setZpNums(String zpNums) {
        this.zpNums = zpNums;
    }
    @Column(name = "dai_yu")
    public String getDaiyu() {
        return daiyu;
    }

    public void setDaiyu(String daiyu) {
        this.daiyu = daiyu;
    }
    @Column(name = "relate_phone")
    public String getRelatePhone() {
        return relatePhone;
    }

    public void setRelatePhone(String relatePhone) {
        this.relatePhone = relatePhone;
    }
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Column(name = "com_address")
    public String getComAddress() {
        return comAddress;
    }

    public void setComAddress(String comAddress) {
        this.comAddress = comAddress;
    }
    @Column(name = "yao_qiu")
    public String getYpyaoqiu() {
        return ypyaoqiu;
    }

    public void setYpyaoqiu(String ypyaoqiu) {
        this.ypyaoqiu = ypyaoqiu;
    }
    @Column(name = "stop_date")
    public String getStopDate() {
        return stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }
    @Column(name = "desn")
    public String getDesn() {
        return desn;
    }

    public void setDesn(String desn) {
        this.desn = desn;
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
}
