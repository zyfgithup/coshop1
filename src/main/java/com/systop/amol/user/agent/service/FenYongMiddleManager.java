package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.FenYongMiddle;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/10/9.
 */
@Service
public class FenYongMiddleManager extends BaseGenericsManager<FenYongMiddle> {

    @Autowired
    private JdbcTemplate jdbcTemplate;
}
