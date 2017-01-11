package com.systop.amol.base.prosort.service;

import com.systop.amol.base.prosort.ProductSortConstants;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.common.modules.dept.model.Dept;
import com.systop.core.service.BaseGenericsManager;
import com.systop.core.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 计算商品类型编号<br>
 * <ul>
 * <li>同级商品类型，从01开始，最多99</li>
 * <li>商品类型编号等于"上级类型编号,类型编号",例如， 类型编号01，下属的小组编号为01,02</li>
 * </ul>
 * 
 * @author songbaojie
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class ProductSortSerialNoManager extends BaseGenericsManager<Dept> {

	/**
	 * 返回给定商品类型的类型编号
	 * 
	 * @param parentProsort
	 *            给定商品类型
	 * 
	 */
	public String getSerialNo(final ProductSort prosort) {
		Assert.notNull(prosort, "The given ProductSort must not be null.");
		String serialNo = null;
		if (prosort.getParentProsort()!= null) { // 如果有上级类型
			List serialNos = getDao()
					.query("select ps.serialNo from ProductSort ps "
							+ "where ps.parentProsort = ? order by ps.serialNo desc",
							prosort.getParentProsort());
			if (serialNos == null || serialNos.isEmpty()) { // 如果同级类型
				serialNo = buildFirstSerialNo(prosort.getParentProsort());
			} else { // 有同级级类型
				String maxSerialNo = (String) serialNos.get(0); // 同级类型最大编号
				// 找出同级类型最大编号的最后两位
				final String[] splited = org.springframework.util.StringUtils
						.commaDelimitedListToStringArray(maxSerialNo);
				if (splited == null || splited.length == 0) { // 同级类型没有编号
					serialNo = buildFirstSerialNo(prosort.getParentProsort());
				} else {// 计算当前类型编号
					Integer serial = StringUtil
							.getNumFromSerial(splited[splited.length - 1]);
					serialNo = prosort.getParentProsort().getSerialNo() + ","
							+ StringUtil.zeroPadding((serial + 1), 2);
				}
			}
		} else { // 如果没有上级类型
			serialNo = this.getTopProductSortSerialNo();
			prosort.setSerialNo(serialNo);
		}

		logger.debug("Create serial No." + serialNo);
		return serialNo;
	}

	/**
	 * 计算顶级商品类型编号
	 */
	public String getTopProductSortSerialNo() {
		List serialNos = getDao()
				.query("select ps.serialNo from ProductSort ps "
						+ "where ps.parentProsort is null order by ps.serialNo desc");
		// 没有商品类型
		if (serialNos == null || serialNos.isEmpty()) {
			return buildFirstSerialNo(null);
		}
		String maxSerialNo = (String) serialNos.get(0);
		// 商品类型编号为null
		if (StringUtils.isBlank(maxSerialNo)) {
			return buildFirstSerialNo(null);
		} else {
			Integer serial = StringUtil.getNumFromSerial(maxSerialNo);
			return StringUtil.zeroPadding((serial + 1), 2);
		}
	}

	/**
	 * 根据上级商品类型和当前商品类型的前一个编号，获得当前类型编号的数字表示 ， 用于重置所有商品类型编号:<br>
	 * 首先根据上级商品类型编号，计算同级商品类型的第一个编号，以后便再此基础上 +1即可。
	 */
	private Integer getSerial(ProductSort productSort, final Integer preSerial) {
		Integer serial = null;
		if (preSerial == null) {
			String serialNo = getSerialNo(productSort);
			if (StringUtils.isNotBlank(serialNo)) {
				String[] splits = org.springframework.util.StringUtils
						.commaDelimitedListToStringArray(serialNo);
				if (splits != null && splits.length > 0) {
					serial = StringUtil
							.getNumFromSerial(splits[splits.length - 1]);
				} else {
					serial = 0;
				}
			}
		} else {
			serial = preSerial + 1;
		}

		return serial;
	}

	/**
	 * 重设所有类型编号。
	 */
	@Transactional
	public void updateAllSerialNo() {
		List<ProductSort> tops = getDao().query(
				"from ProductSort d where d.parentProsort is null");
		Map serialMap = new HashMap(100); // 用于存放类型ID-SerialNo
		// 计算所有类型编号，并将类型ID-类型编号的对应关系存入serialMap
		Integer serial = null;
		for (ProductSort prosort : tops) {
			serial = this.getSerial(prosort, serial);
			// String serialNo = getSerialNo(top);
			prosort.setSerialNo(buildSerialNo(null, serial));
			serialMap.put(prosort.getId(), buildSerialNo(null, serial));
			// getDao().saveObject(top);
			if (prosort.getChildProductSorts().size() > 0) {
				this.updateChildrenSerialNo(prosort, serialMap);
			}
		}
		// 批量更新类型编号
		Set<Integer> ids = serialMap.keySet();
		for (Integer id : ids) {
			Dept dept = get(id);
			getDao().evict(dept);
			dept.setSerialNo((String) serialMap.get(id));
			getDao().getHibernateTemplate().update(dept);
		}
	}

	/**
	 * 更新所有子商品类型的SerialNo
	 */
	@Transactional
	public void updateChildrenSerialNo(ProductSort parent, Map serialMap) {
		logger.debug("Update " + parent.getName() + "'s children serial No.");
		List<ProductSort> children = parent.getChildProductSorts();
		Integer serial = null;
		for (ProductSort child : children) {
			serial = this.getSerial(child, serial);
			child.setSerialNo(buildSerialNo(parent, serial));
			serialMap.put(child.getId(), buildSerialNo(parent, serial));
			// getDao().saveObject(child);
			if (child.getChildProductSorts().size() > 0) {
				updateChildrenSerialNo(child, serialMap);
			}
		}
	}

	/**
	 * 构建第一个商品类型的编号
	 */
	private static String buildFirstSerialNo(ProductSort parent) {
		if (parent == null) {
			return ProductSortConstants.FIRST_SERIAL_NO;
		} else {
			return new StringBuffer(100).append(parent.getSerialNo())
					.append(",").append(ProductSortConstants.FIRST_SERIAL_NO)
					.toString();
		}
	}

	/**
	 * 构建商品类型编号
	 */
	private static String buildSerialNo(ProductSort parent, Integer serial) {
		if (parent == null) {
			return StringUtil.zeroPadding(serial, 2);
		} else {
			return new StringBuffer(100).append(parent.getSerialNo())
					.append(",").append(StringUtil.zeroPadding(serial, 2))
					.toString();
		}
	}

}
