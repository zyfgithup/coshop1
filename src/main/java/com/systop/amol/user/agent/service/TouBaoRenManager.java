package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.RzFile;
import com.systop.amol.user.agent.model.TouBaoRen;
import com.systop.core.service.BaseGenericsManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
@Service
public class TouBaoRenManager extends BaseGenericsManager<TouBaoRen> {

    @Autowired
    RzFileManager rzFileManager;
    @Transactional
    public void save(TouBaoRen touBaoRen, List<RzFile> rzFiles){
        this.save(touBaoRen);
        for (RzFile rz : rzFiles){
            rzFileManager.save(rz);
        }
    }
    public List<TouBaoRen> getList(Integer userId){
        return this.query("from TouBaoRen t where t.user.id = ?  order by t.createTime desc ",new Object[]{userId});
    }
    public List<TouBaoRen> getListBySaleId(Integer salesId){
        List<TouBaoRen> touBaoRenList = this.query("from TouBaoRen t where t.sales.id = ?  order by t.createTime desc ",new Object[]{salesId});
        for(TouBaoRen tb : touBaoRenList){
            tb.setRzFiles(rzFileManager.getFilesByTbId(tb.getId()));
        }
        return touBaoRenList;
    }
    public TouBaoRen getByIdCardAndOrderId(String idCard,String salesId){
        if(StringUtils.isNotBlank(idCard) && StringUtils.isNotBlank(salesId)) {
            return this.findObject(" from TouBaoRen t where t.idCard=? and t.sales.id=? ",idCard,Integer.parseInt(salesId) );
        }else{
            return null;
        }
    }
}
