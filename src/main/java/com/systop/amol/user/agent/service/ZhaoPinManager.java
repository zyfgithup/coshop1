package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.ZhaoPin;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/9/23.
 */
@Service
public class ZhaoPinManager extends BaseGenericsManager<ZhaoPin> {

    @Autowired
    private JdbcTemplate jdbcTemplate;
}
