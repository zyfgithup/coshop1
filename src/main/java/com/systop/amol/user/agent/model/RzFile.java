package com.systop.amol.user.agent.model;

import com.systop.core.model.BaseModel;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/16.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "rz_file")
public class RzFile extends BaseModel implements Cloneable{

    private Integer id;
    private RenZheng renZheng;
    private Date createTime;
    //1 身份证2 营业执照3车辆行驶证 4 驾驶证 5 危化品运输资格证6 税务登记证证明 7组织机构代码证 8其他资料证明9加油站经营证明
    private String fileType;
    private String imageUrl;
    private TouBaoRen touBaoRen;
    private CheLiang cheLiang;

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
    @Column(name = "imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "rz_id")
    public RenZheng getRenZheng() {
        return renZheng;
    }
    public void setRenZheng(RenZheng renZheng) {
        this.renZheng = renZheng;
    }
    @Column(name = "file_type")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    @Column(name = "create_time")
    @JSON(serialize = false)
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "tbr_id")
    @JSON(serialize = false)
    public TouBaoRen getTouBaoRen() {
        return touBaoRen;
    }

    public void setTouBaoRen(TouBaoRen touBaoRen) {
        this.touBaoRen = touBaoRen;
    }
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "cheliang_id")
    @JSON(serialize = false)
    public CheLiang getCheLiang() {
        return cheLiang;
    }

    public void setCheLiang(CheLiang cheLiang) {
        this.cheLiang = cheLiang;
    }
}
