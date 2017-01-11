package com.systop.amol.base.product.service;

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
public class ProductsXlsImportHelperFactory {
  /**
   * 私有构造器
   */
  private ProductsXlsImportHelperFactory() {
  }

  /**
   * 与Sheet对应的Map
   */
  public static Map<String,Converter> properties = new HashMap<String, Converter>();
  static {
	  //名称  图片 规格   所属类别 描述  市场价  售价
	    properties.put("name", new StringConverter(0)); //供应商名称
	    properties.put("ImageUrl", new StringConverter(1)); //应付金额
	    properties.put("guige", new StringConverter(2)); //
	    properties.put("proSortId", new StringConverter(3)); //
	    properties.put("remark", new StringConverter(4)); //
	    properties.put("units", new StringConverter(5)); //
	    properties.put("scPrice", new StringConverter(6)); //
	    properties.put("outPrice", new StringConverter(7)); //
	    properties.put("upShelfNum", new StringConverter(8)); //
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
