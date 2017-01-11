package com.systop.amol.user.agent.phone;

import com.systop.amol.user.agent.model.CaculateCCTimes;
import com.systop.amol.user.agent.model.JiayouSet;
import com.systop.amol.user.agent.service.CaculateCCTimesManager;
import com.systop.amol.user.agent.service.JiayouSetManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/21.
 */
@SuppressWarnings({ "serial", "unused", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneCaculateCCTimesAction extends DefaultCrudAction<CaculateCCTimes, CaculateCCTimesManager> {

    @Autowired
    private UserManager userManager;
    @Autowired
    private JiayouSetManager jiayouSetManager;
    private Map<String,Object> result = new HashedMap();
    public String ccPageData(){
        String id = getRequest().getParameter("jycId");
        User user = null;
        if(StringUtils.isNotBlank(id)){
            user = userManager.get(Integer.parseInt(id));
        }
        System.out.println("---------------------------user="+user);
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
      /*  if(null!=user && null!=user.getRegion()){
            list = jiayouSetManager.getByRegionId(user.getRegion().getId());
        }*/
        String ccTimes = getManager().getCaculateCCTimesByUser(user.getId());
        int count = getManager().getCntOfMyOrders(user.getId());
        Object ls = getManager().getLiuShuiMyOrders(user.getId());
        result.put("count",count);
        result.put("ccTimes",ccTimes.split("\\.")[0]+"."+ccTimes.split("\\.")[1].substring(0, 1));
        String jsPrice = "";
        if(null!=user.getMerSortStr() && !"".equals(user.getMerSortStr())){
            String[] strArray = user.getMerSortStr().split(",");
            for(String str : strArray){
                JiayouSet js = jiayouSetManager.get(Integer.parseInt(str));
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("name",js.getKindName());
                map.put("grPrice",js.getGrPrice());
                map.put("id",js.getId());
                list.add(map);
            }
        }
        result.put("jyList",list);
        result.put("jycstate",user.getJycstate());
        result.put("ls",ls);
        return "result";
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}
