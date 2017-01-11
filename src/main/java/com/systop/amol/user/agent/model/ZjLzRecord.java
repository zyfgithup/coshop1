package com.systop.amol.user.agent.model;

import com.systop.amol.sales.model.Sales;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/27.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "zjlz_record")
public class ZjLzRecord extends BaseModel implements Cloneable{

    /** 主键 */
    private Integer id;
    //主动
    private User user;
    //被动
    private User bdUser;

    //交易时间
    private Date createTime;
    //交易金额
    private Double jyJe;
    //返现金额
    private Double fxJe;

    //交易类型
    /**
     *   充值cz
         返现fx
         提现tx
         分佣fy
         加油jy
         保险bx
         维修wx
     */
    private String jyType;

    //支付类型
    private String payType;

    //收支类型
    private String szType;

    private Sales sales;
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
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "bduser_id")
    @JSON(serialize = false)
    public User getBdUser() {
        return bdUser;
    }

    public void setBdUser(User bdUser) {
        this.bdUser = bdUser;
    }
    @Column(name = "jy_je")
    public Double getJyJe() {
        return jyJe;
    }

    public void setJyJe(Double jyJe) {
        this.jyJe = jyJe;
    }
    @Column(name = "jy_type")
    public String getJyType() {
        return jyType;
    }

    public void setJyType(String jyType) {
        this.jyType = jyType;
    }
    @Column(name = "pay_type")
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
    @Column(name = "sz_type")
    public String getSzType() {
        return szType;
    }

    public void setSzType(String szType) {
        this.szType = szType;
    }
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id")
    @JSON(serialize = false)
    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }
    @Column(name = "fx_je")
    public Double getFxJe() {
        return fxJe;
    }

    public void setFxJe(Double fxJe) {
        this.fxJe = fxJe;
    }
}
