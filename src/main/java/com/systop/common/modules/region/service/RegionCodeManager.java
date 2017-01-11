package com.systop.common.modules.region.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.common.modules.region.RegionConstants;
import com.systop.common.modules.region.model.Region;
import com.systop.core.service.BaseGenericsManager;
import com.systop.core.util.StringUtil;

/**
 * 计算部门编号<br>
 * <ul>
 * <li>同级部门，从01开始，最多99</li>
 * <li>部门编号等于"上级部门编号,部门编号",例如， 行政部编号01，下属小车组编号为01,02</li>
 * </ul>
 * 
 * @author songbaojie
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class RegionCodeManager extends BaseGenericsManager<Region> {

	/**
	 * 返回给定部门的部门编号
	 * 
	 * @param parentDept
	 *            给定部门
	 * 
	 */
	public String getCode(final Region region) {
		Assert.notNull(region, "The given Region must not be null.");
		String serialNo = null;
		if (region.getParent() != null) { // 如果有上级部门
			List serialNos = getDao().query(
					"select d.code from Region d "
							+ "where d.parent = ? order by d.code desc",
							region.getParent());
			if (serialNos == null || serialNos.isEmpty()) { // 如果同级部门
				serialNo = buildFirstSerialNo(region.getParent());
			} else { // 有同级级部门
				String maxSerialNo = (String) serialNos.get(0); // 同级部门最大编号
				// 找出同级部门最大编号的最后两位
				final String[] splited = org.springframework.util.StringUtils
						.commaDelimitedListToStringArray(maxSerialNo);
				if (splited == null || splited.length == 0) { // 同级部门没有编号
					serialNo = buildFirstSerialNo(region.getParent());
				} else {// 计算当前部门编号
					Integer serial = StringUtil
							.getNumFromSerial(splited[splited.length - 1]);
					serialNo = region.getParent().getCode() + ","
							+ StringUtil.zeroPadding((serial + 1), 2);
				}
			}
		} else { // 如果没有上级部门
			serialNo = this.getTopDeptSerialNo();
			region.setCode(serialNo);
		}

		logger.debug("Create serial No." + serialNo);
		return serialNo;
	}

	/**
	 * 计算顶级部门编号
	 */
	public String getTopDeptSerialNo() {
		List serialNos = getDao().query(
				"select d.code from Region d "
						+ "where d.parent is null order by d.code desc");
		// 没有部门
		if (serialNos == null || serialNos.isEmpty()) {
			return buildFirstSerialNo(null);
		}
		String maxSerialNo = (String) serialNos.get(0);
		// 部门编号为null
		if (StringUtils.isBlank(maxSerialNo)) {
			return buildFirstSerialNo(null);
		} else {
			Integer serial = StringUtil.getNumFromSerial(maxSerialNo);
			return StringUtil.zeroPadding((serial + 1), 2);
		}
	}

	/**
	 * 根据上级部门和当前部门的前一个编号，获得当前部门编号的数字表示 ， 用于重置所有部门编号:<br>
	 * 首先根据上级部门编号，计算同级部门的第一个编号，以后便再此基础上 +1即可。
	 */
	private Integer getSerial(Region dept, final Integer preSerial) {
		Integer serial = null;
		if (preSerial == null) {
			String serialNo = getCode(dept);
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
	 * 重设所有部门编号。
	 */
	@Transactional
	public void updateAllSerialNo() {
		List<Region> tops = getDao().query(
				"from Region d where d.parent is null");
		Map serialMap = new HashMap(100); // 用于存放部门ID-SerialNo
		// 计算所有部门编号，并将部门ID-部门编号的对应关系存入serialMap
		Integer serial = null;
		for (Region top : tops) {
			serial = this.getSerial(top, serial);
			// String serialNo = getSerialNo(top);
			top.setCode(buildSerialNo(null, serial));
			serialMap.put(top.getId(), buildSerialNo(null, serial));
			// getDao().saveObject(top);
			if (top.getChilds().size() > 0) {
				this.updateChildrenSerialNo(top, serialMap);
			}
		}
		// 批量更新部门编号
		Set<Integer> ids = serialMap.keySet();
		for (Integer id : ids) {
			Region dept = get(id);
			getDao().evict(dept);
			dept.setCode((String) serialMap.get(id));
			getDao().getHibernateTemplate().update(dept);
		}
	}

	/**
	 * 更新所有子部门的SerialNo
	 */
	@Transactional
	public void updateChildrenSerialNo(Region parent, Map serialMap) {
		logger.debug("Update " + parent.getName() + "'s children serial No.");
		Set<Region> children = parent.getChilds();
		Integer serial = null;
		for (Region child : children) {
			serial = this.getSerial(child, serial);
			child.setCode(buildSerialNo(parent, serial));
			serialMap.put(child.getId(), buildSerialNo(parent, serial));
			// getDao().saveObject(child);
			if (child.getChilds().size() > 0) {
				updateChildrenSerialNo(child, serialMap);
			}
		}
	}

	/**
	 * 构建第一个部门的编号
	 */
	private static String buildFirstSerialNo(Region parent) {
		if (parent == null) {
			return RegionConstants.FIRST_CODE;
		} else {
			return new StringBuffer(100).append(parent.getCode())
					.append(",").append(RegionConstants.FIRST_CODE)
					.toString();
		}
	}

	/**
	 * 构建部门编号
	 */
	private static String buildSerialNo(Region parent, Integer serial) {
		if (parent == null) {
			return StringUtil.zeroPadding(serial, 2);
		} else {
			return new StringBuffer(100).append(parent.getCode())
					.append(",").append(StringUtil.zeroPadding(serial, 2))
					.toString();
		}
	}
}
