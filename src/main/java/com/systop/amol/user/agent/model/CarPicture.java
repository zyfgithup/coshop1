package com.systop.amol.user.agent.model;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/17.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "car_pic")
public class CarPicture extends BaseModel implements Cloneable{

    /** 主键 */
    private Integer id;
    //加油车对象
    private User user;
    private Date createTime;
    private String imagerURL;
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
    @Column(name = "imagerURL")
    public String getImagerURL() {
        return imagerURL;
    }

    public void setImagerURL(String imagerURL) {
        this.imagerURL = imagerURL;
    }
}
