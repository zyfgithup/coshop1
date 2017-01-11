package com.systop.amol.stock.service;

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
public class StockInitXlsImportHelperFactory {
  /**
   * 私有构造器
   */
  private StockInitXlsImportHelperFactory() {
  }

  /**
   * 与Sheet对应的Map
   */
  public static Map<String, Converter> properties = new HashMap<String, Converter>();
  static {
	properties.put("code", new StringConverter(0)); //商品编码
    properties.put("name", new StringConverter(1)); //商品名称
    properties.put("supplier", new StringConverter(3)); //生产厂商
    properties.put("storage", new StringConverter(4)); //仓库名称
    properties.put("count", new StringConverter(5)); //数量
    properties.put("price", new StringConverter(7)); //单价
    properties.put("amount", new StringConverter(8)); //金额
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
