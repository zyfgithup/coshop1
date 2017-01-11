package com.systop.amol.base.txmoneyset.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.base.txmoneyset.model.TxMoneySet;
import com.systop.core.service.BaseGenericsManager;

@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class TxMoneyManager extends BaseGenericsManager<TxMoneySet>{

    @Transactional
    public void save(TxMoneySet txMoney) {
        super.save(txMoney);
    }
    public List getTxMoneySet(){
        StringBuffer hql=new StringBuffer();
        hql.append("  from  TxMoneySet ");
        return this.getDao().query(hql.toString(), null);
    }

}
