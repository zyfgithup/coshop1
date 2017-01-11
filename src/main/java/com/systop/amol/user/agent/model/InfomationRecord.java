package com.systop.amol.user.agent.model;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/19.
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "infomation")
public class InfomationRecord extends BaseModel implements Cloneable{
    /** 主键 */
    private Integer id;
    //发送对象
    private User sendUser;
    //接收对象
    private User receiveUser;
    private Date createTime;
    /**'0':'活动消息','1':'店庆消息'
     * 2.支付完成更新订单推送 salesOrderPhone/updatePayState.do
     * 3.加油车到达目的地 phoneagentaction/toDestination.do
     * 4发送账单消息  phoneagentaction/toUserAccount.do
     *5.商家接单接口 phonagentaction/jycDriverQd.do
     */
    private String xxtype;
    /**'0':'短信推送','1':'app推送'
     * 3.接口推送
     */
    private String tstype;
    private String sendContent;

    private String createTimeStr;
    private String rsState;

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
    @JoinColumn(name = "send_userid")
    @JSON(serialize = false)
    public User getSendUser() {
        return sendUser;
    }

    public void setSendUser(User sendUser) {
        this.sendUser = sendUser;
    }
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_userid")
    @JSON(serialize = false)
    public User getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(User receiveUser) {
        this.receiveUser = receiveUser;
    }
    @Column(name = "create_time")
    @JSON(serialize = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "send_content")
    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }
    @Column(name = "xxtype")
    public String getXxtype() {
        return xxtype;
    }

    public void setXxtype(String xxtype) {
        this.xxtype = xxtype;
    }
    @Column(name = "tstype")
    public String getTstype() {
        return tstype;
    }

    public void setTstype(String tstype) {
        this.tstype = tstype;
    }

    @Transient
    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }
    @Column(name = "ts_state")
    public String getRsState() {
        return rsState;
    }

    public void setRsState(String rsState) {
        if(null==rsState){
            this.rsState = "1";
        }else{
            this.rsState = "0";
        }
    }
}
