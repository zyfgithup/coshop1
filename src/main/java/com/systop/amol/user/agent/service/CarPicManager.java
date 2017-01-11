package com.systop.amol.user.agent.service;

import com.systop.amol.user.agent.model.CarPicture;
import com.systop.core.service.BaseGenericsManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
@Service
public class CarPicManager extends BaseGenericsManager<CarPicture> {

    public List<CarPicture> getListCarPics(Integer userId){
        return this.query("from CarPicture c where c.user.id=?",new Object[]{userId});
    }



}
