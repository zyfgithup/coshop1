package com.systop.amol.finance.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.amol.base.prosort.ProductSortConstants;
import com.systop.amol.finance.model.CostSort;
import com.systop.core.service.BaseGenericsManager;
import com.systop.core.util.StringUtil;

/**
 * 计算收支类别编号<br>
 * <ul>
 * <li>同级收支类别，从01开始，最多99</li>
 * <li>收支类别编号等于"上级类别编号,类别编号",例如，类别编号01，下属的小组编号为01,02</li>
 * </ul>
 * 
 * @author songbaojie
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class CostSortSerialNoManager extends BaseGenericsManager<CostSort> {

	/**
	 * 返回给定收支类别的类别编号
	 * 
	 * @param spsort
	 *            给定收支类别
	 * 
	 */
	public String getSerialNo(final CostSort costSort) {
		Assert.notNull(costSort, "The given CostSort must not be null.");
		String serialNo = null;
		if (costSort.getParentSort() != null) { // 如果有上级类别
			List serialNos = getDao()
					.query("select s.serialNo from CostSort s "
							+ "where s.parentSort = ? order by s.serialNo desc",
							costSort.getParentSort());
			if (serialNos == null || serialNos.isEmpty()) { // 如果同级类别
				serialNo = buildFirstSerialNo(costSort.getParentSort());
			} else { // 有同级类别
				String maxSerialNo = (String) serialNos.get(0); // 同级类别最大编号
				// 找出同级类别最大编号的最后两位
				final String[] splited = org.springframework.util.StringUtils
						.commaDelimitedListToStringArray(maxSerialNo);
				if (splited == null || splited.length == 0) { // 同级类别没有编号
					serialNo = buildFirstSerialNo(costSort.getParentSort());
				} else {// 计算当前类别编号
					Integer serial = StringUtil
							.getNumFromSerial(splited[splited.length - 1]);
					serialNo = costSort.getParentSort().getSerialNo() + ","
							+ StringUtil.zeroPadding((serial + 1), 2);
				}
			}
		} else { // 如果没有上级类别
			serialNo = this.getTopCostSortSerialNo();
			costSort.setSerialNo(serialNo);
		}

		logger.debug("Create serial No." + serialNo);
		return serialNo;
	}

	/**
	 * 计算顶级收支类别编号
	 */
	public String getTopCostSortSerialNo() {
		List serialNos = getDao()
				.query("select s.serialNo from CostSort s "
						+ "where s.parentSort is null order by s.serialNo desc");
		// 没有收支类别
		if (serialNos == null || serialNos.isEmpty()) {
			return buildFirstSerialNo(null);
		}
		String maxSerialNo = (String) serialNos.get(0);
		// 收支类别编号为null
		if (StringUtils.isBlank(maxSerialNo)) {
			return buildFirstSerialNo(null);
		} else {
			Integer serial = StringUtil.getNumFromSerial(maxSerialNo);
			return StringUtil.zeroPadding((serial + 1), 2);
		}
	}

	/**
	 * 根据上级收支类别和当前收支类别的前一个编号，获得当前类别编号的数字表示 ， 用于重置所有收支类别编号:<br>
	 * 首先根据上级收支类别编号，计算同级收支类别的第一个编号，以后便再此基础上 +1即可。
	 */
	private Integer getSerial(CostSort costSort, final Integer preSerial) {
		Integer serial = null;
		if (preSerial == null) {
			String serialNo = getSerialNo(costSort);
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
	 * 重设所有收支类别编号。
	 */
	@Transactional
	public void updateAllSerialNo() {
		List<CostSort> tops = getDao().query(
				"from CostSort s where s.parentSort is null");
		Map serialMap = new HashMap(100); // 用于存放收支类别ID-SerialNo
		// 计算所有收支类别编号，并将收支类别ID-收支类别编号的对应关系存入serialMap
		Integer serial = null;
		for (CostSort costSort : tops) {
			serial = this.getSerial(costSort, serial);
			costSort.setSerialNo(buildSerialNo(null, serial));
			serialMap.put(costSort.getId(), buildSerialNo(null, serial));
			// getDao().saveObject(top);
			if (costSort.getChildCostSorts().size() > 0) {
				this.updateChildrenSerialNo(costSort, serialMap);
			}
		}
		// 批量更新收支类别编号
		Set<Integer> ids = serialMap.keySet();
		for (Integer id : ids) {
			CostSort costSort = get(id);
			getDao().evict(costSort);
			costSort.setSerialNo((String) serialMap.get(id));
			getDao().getHibernateTemplate().update(costSort);
		}
	}

	/**
	 * 更新所有子收支类别的SerialNo
	 */
	@Transactional
	public void updateChildrenSerialNo(CostSort parent, Map serialMap) {
		Set<CostSort> children = parent.getChildCostSorts();
		Integer serial = null;
		for (CostSort child : children) {
			serial = this.getSerial(child, serial);
			child.setSerialNo(buildSerialNo(parent, serial));
			serialMap.put(child.getId(), buildSerialNo(parent, serial));
			// getDao().saveObject(child);
			if (child.getChildCostSorts().size() > 0) {
				updateChildrenSerialNo(child, serialMap);
			}
		}
	}

	/**
	 * 构建第一个收支类别的编号
	 */
	private static String buildFirstSerialNo(CostSort parent) {
		if (parent == null) {
			return ProductSortConstants.FIRST_SERIAL_NO;
		} else {
			return new StringBuffer(100).append(parent.getSerialNo())
					.append(",").append(ProductSortConstants.FIRST_SERIAL_NO)
					.toString();
		}
	}

	/**
	 * 构建收支类别编号
	 */
	private static String buildSerialNo(CostSort parent, Integer serial) {
		if (parent == null) {
			return StringUtil.zeroPadding(serial, 2);
		} else {
			return new StringBuffer(100).append(parent.getSerialNo())
					.append(",").append(StringUtil.zeroPadding(serial, 2))
					.toString();
		}
	}

}
