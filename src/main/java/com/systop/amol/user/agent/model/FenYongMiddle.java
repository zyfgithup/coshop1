package com.systop.amol.user.agent.model;

import com.systop.amol.sales.model.Sales;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/9.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "fen_yong_middel")
public class FenYongMiddle extends BaseModel implements Cloneable{

    /** 主键 */
    private Integer id;
    //消费者
    private User user;

    //获得优惠的人  就是分拥的人
    private User fyUser;

    private Date createTime;

    //消费金额
    private Double xiaofeiMoney;

    //分拥金额
    private Double fyMoney;

    //当时分拥比例
    private Double fyBl;

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
    @JoinColumn(name = "fyuser_id")
    public User getFyUser() {
        return fyUser;
    }

    public void setFyUser(User fyUser) {
        this.fyUser = fyUser;
    }
    @Column(name = "xiaofei_money")
    public Double getXiaofeiMoney() {
        return xiaofeiMoney;
    }

    public void setXiaofeiMoney(Double xiaofeiMoney) {
        this.xiaofeiMoney = xiaofeiMoney;
    }
    @Column(name = "fy_money")
    public Double getFyMoney() {
        return fyMoney;
    }

    public void setFyMoney(Double fyMoney) {
        this.fyMoney = fyMoney;
    }
    @Column(name = "fy_bl")
    public Double getFyBl() {
        return fyBl;
    }
    public void setFyBl(Double fyBl) {
        this.fyBl = fyBl;
    }
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id")
    public Sales getSales() {
        return sales;
    }
    public void setSales(Sales sales) {
        this.sales = sales;
    }
}
