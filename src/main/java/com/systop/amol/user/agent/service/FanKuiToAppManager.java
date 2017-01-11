package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.FanKuiToApp;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/11/3.
 */
@Service
public class FanKuiToAppManager extends BaseGenericsManager<FanKuiToApp> {

    @Autowired
    private JdbcTemplate jdbcTemplate;
}
