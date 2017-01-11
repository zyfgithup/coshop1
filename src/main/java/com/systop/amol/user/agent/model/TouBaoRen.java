package com.systop.amol.user.agent.model;

import com.systop.amol.sales.model.Sales;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tb_ren")
public class TouBaoRen extends BaseModel implements Cloneable{

    /** 主键 */
    private Integer id;
    //商家对象
    private User user;

    private Date createTime;
    private String tbName;
    private String idCard;
    private List<RzFile> rzFiles;
    private String createTimeStr;
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
    @JSON(serialize = false)
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "tb_name")
    public String getTbName() {
        return tbName;
    }
    public void setTbName(String tbName) {
        this.tbName = tbName;
    }
    @Column(name = "id_card")
    public String getIdCard() {
        return idCard;
    }
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    @Transient
    public List<RzFile> getRzFiles() {
        return rzFiles;
    }

    public void setRzFiles(List<RzFile> rzFiles) {
        this.rzFiles = rzFiles;
    }
    @Transient
    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
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
}
