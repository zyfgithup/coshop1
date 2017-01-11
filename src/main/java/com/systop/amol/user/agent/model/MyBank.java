package com.systop.amol.user.agent.model;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/8.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "my_bank")
public class MyBank extends BaseModel implements Cloneable{

    /** 主键 */
    private Integer id;
    //商家对象
    private User user;

    private Date createTime;

    private String ckrName;

    private String ckrPhone;

    private String idCard;

    private String bankNo;

    private String bankName;

    private String visible;

    private String comName;

    private String imageURL;
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
    @Column(name = "ckr_name")
    public String getCkrName() {
        return ckrName;
    }

    public void setCkrName(String ckrName) {
        this.ckrName = ckrName;
    }
    @Column(name = "ckr_phone")
    public String getCkrPhone() {
        return ckrPhone;
    }

    public void setCkrPhone(String ckrPhone) {
        this.ckrPhone = ckrPhone;
    }
    @Column(name = "id_card")
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    @Column(name = "bank_no")
    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }
    @Column(name = "bank_name")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "visible")
    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }
    @Column(name = "com_name")
    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }
    @Column(name = "imageURL")
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
