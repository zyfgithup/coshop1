package com.systop.amol.user.agent.model;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/7.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "charge_record")
public class ChargeRecord extends BaseModel implements Cloneable{

    /** 主键 */
    private Integer id;
    //商家对象
    private User user;

    private Date createTime;

    private String chargeType;
    //金额
    private Double money;
    //支出或者收入
    private String zcOrSr;
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
    @Column(name = "charge_type")
    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }
    @Column(name = "money")
    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
    @Column(name = "zc_sr")
    public String getZcOrSr() {
        return zcOrSr;
    }

    public void setZcOrSr(String zcOrSr) {
        this.zcOrSr = zcOrSr;
    }
}
