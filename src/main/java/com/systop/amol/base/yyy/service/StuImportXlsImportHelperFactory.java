package com.systop.amol.base.yyy.service;

import com.systop.core.util.XlsImportHelper;
import com.systop.core.util.XlsImportHelper.Converter;
import com.systop.core.util.XlsImportHelper.StringConverter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Excel数据导入工具类
 * @author ShangHua
 */
@Service
public class StuImportXlsImportHelperFactory {
    /**
     * 私有构造器
     */
    private StuImportXlsImportHelperFactory() {
    }

    /**
     * 与Sheet对应的Map
     */
    public static Map<String, Converter> properties = new HashMap<String, Converter>();
    static {
        //名称  图片 规格  积分数  所属类别 描述
        properties.put("stuName", new StringConverter(0)); //学员姓名
        properties.put("stuSex", new StringConverter(1)); //学员性别
        properties.put("school", new StringConverter(2)); //学校
        properties.put("xbzy", new StringConverter(3)); //系、部、专业
        properties.put("domNum", new StringConverter(4)); //宿舍号
        properties.put("stuQq", new StringConverter(5)); //QQ
        properties.put("wx", new StringConverter(6)); //微信
        properties.put("phone", new StringConverter(7)); //手机号
        properties.put("jg", new StringConverter(8)); //籍贯
        properties.put("bzr", new StringConverter(9)); //班主任
        properties.put("relatePhone", new StringConverter(10)); //电话
        properties.put("drzw", new StringConverter(11)); //担任职务
        properties.put("desn", new StringConverter(12)); //担任职务上

    }
    /**
     * 创建并返回XlsImportHelper
     */
    public static XlsImportHelper create() {
        XlsImportHelper xih = new XlsImportHelper();
        xih.setProperties(properties);
        return xih;
    }
}
