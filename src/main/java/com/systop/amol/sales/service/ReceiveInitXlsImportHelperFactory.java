package com.systop.amol.sales.service;

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
public class ReceiveInitXlsImportHelperFactory {
  /**
   * 私有构造器
   */
  private ReceiveInitXlsImportHelperFactory() {
  }

  /**
   * 与Sheet对应的Map
   */
  public static Map<String, Converter> properties = new HashMap<String, Converter>();
  static {
    properties.put("name", new StringConverter(0)); //客户名称
    properties.put("idCard", new StringConverter(1)); //客户身份证号
    properties.put("amount", new StringConverter(2)); //应收金额
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
