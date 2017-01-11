package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.CaculateCCTimes;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/21.
 */
@Service
public class CaculateCCTimesManager extends BaseGenericsManager<CaculateCCTimes> {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public String getCaculateCCTimesByUser(Integer userId){
        String sql ="select * from caculate_cctimes where user_id="+userId+" and DATE_FORMAT(create_Time,'%Y-%m-%d')=CURDATE() order by create_time";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        float ccTimes = 0;
        if(null!=list && list.size()>0){
            if(list.size()==1){
                float ccTime = caculateTimes((Date)list.get(0).get("create_time"),new Date());
                ccTimes +=ccTime;
            }else{
                    for (int i=1;i<list.size();i+=2){
                        float ccTime = caculateTimes((Date)list.get(i-1).get("create_time"),(Date)list.get(i).get("create_time"));
                        ccTimes += ccTime;
                    }
                     //单数 现在是出车状态
                    if(list.size()%2==1){
                        float ccTime = caculateTimes((Date) list.get(list.size()-1).get("create_time"),new Date());
                        ccTimes += ccTime;
                    }
            }
        }
        DecimalFormat df = new DecimalFormat("0.00000");//格式化小数，不足的补0
        System.out.println(df.format(ccTimes));
        return df.format(ccTimes);
    }
    public int getCntOfMyOrders(Integer userId){
        String sql = " select count(*) from sales where meruser_id="+userId+" and DATE_FORMAT(create_Time,'%Y-%m-%d')=CURDATE() ";
        return jdbcTemplate.queryForInt(sql);
    }
    public Object getLiuShuiMyOrders(Integer userId){
        String sql = " select sum(spayamount) as ls from sales where meruser_id="+userId+" and DATE_FORMAT(create_Time,'%Y-%m-%d')=CURDATE() ";
        List<Map<String,Object>> list =jdbcTemplate.queryForList(sql);
        return list.get(0).get("ls");
    }
    public float caculateTimes(Date date1,Date date2){
        float hours =  ((float)(date2.getTime()-date1.getTime())/1000/60/60);
        return hours;
//        DecimalFormat df = new DecimalFormat("0.00000");//格式化小数，不足的补0
//        System.out.println(df.format(hours));
    }

}
