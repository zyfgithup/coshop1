package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.MyBank;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/8/8.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class MyBankManager  extends BaseGenericsManager<MyBank> {
    public MyBank findBank(String bankId) {
        return this.findObject("from MyBank o where o.id=?", Integer.parseInt(bankId));
    }

}
