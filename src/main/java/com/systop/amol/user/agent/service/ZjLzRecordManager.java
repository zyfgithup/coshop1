package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.ZjLzRecord;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/10/27.
 */
@Service
public class ZjLzRecordManager extends BaseGenericsManager<ZjLzRecord> {

    @Autowired
    private JdbcTemplate jdbcTemplate;
}
