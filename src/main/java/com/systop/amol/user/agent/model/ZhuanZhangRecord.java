package com.systop.amol.user.agent.model;

import com.systop.amol.sales.model.Sales;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/8.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "zhuan_zhang_record")
public class ZhuanZhangRecord extends BaseModel implements Cloneable{

    /** 主键 */
    private Integer id;
    //商家对象
    private User user;

    private Date createTime;
    private Double jyJe;
    private Double zzJe;

    private User toUser;

    private String content;

    //0充值返现 1 分拥返现 2 转账
    private String type;

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
    @Column(name="zzJe")
    public Double getZzJe() {
        return zzJe;
    }

    public void setZzJe(Double zzJe) {
        this.zzJe = zzJe;
    }
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "to_userId")
    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }
    @Column(name="content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id")
    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }
    @Column(name="type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name="jyje")
    public Double getJyJe() {
        return jyJe;
    }

    public void setJyJe(Double jyJe) {
        this.jyJe = jyJe;
    }
}
