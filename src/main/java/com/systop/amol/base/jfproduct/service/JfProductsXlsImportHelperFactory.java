package com.systop.amol.base.jfproduct.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.systop.core.util.XlsImportHelper;
import com.systop.core.util.XlsImportHelper.Converter;
import com.systop.core.util.XlsImportHelper.StringConverter;

/**
 * Excel数据导入工具类
 * @author ShangHua
 */
@Service
public class JfProductsXlsImportHelperFactory {
  /**
   * 私有构造器
   */
  private JfProductsXlsImportHelperFactory() {
  }

  /**
   * 与Sheet对应的Map
   */
  public static Map<String, Converter> properties = new HashMap<String, Converter>();
  static {
	  //名称  图片 规格  积分数  所属类别 描述
    properties.put("name", new StringConverter(0)); //供应商名称
    properties.put("ImageUrl", new StringConverter(1)); //应付金额
    properties.put("guige", new StringConverter(2)); //
    properties.put("integral", new StringConverter(3)); //
    properties.put("proSortId", new StringConverter(4)); //
    properties.put("remark", new StringConverter(5)); //
    properties.put("units", new StringConverter(6)); //
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
