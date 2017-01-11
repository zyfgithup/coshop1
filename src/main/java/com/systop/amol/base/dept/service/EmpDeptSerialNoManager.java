package com.systop.amol.base.dept.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.amol.base.dept.model.EmpDept;
import com.systop.common.modules.dept.DeptConstants;
import com.systop.core.service.BaseGenericsManager;
import com.systop.core.util.StringUtil;

/**
 * 计算员工部门编号<br>
 * <ul>
 * <li>同级部门，从01开始，最多99</li>
 * <li>部门编号等于"上级部门编号,部门编号",例如， 行政部编号01，下属小车组编号为01,02</li>
 * </ul>
 * 
 * @author songbaojie
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class EmpDeptSerialNoManager extends BaseGenericsManager<EmpDept> {

	/**
	 * 返回给定部门的部门编号
	 * 
	 * @param parentDept
	 *            给定部门
	 * 
	 */
	public String getSerialNo(final EmpDept dept) {
		Assert.notNull(dept, "The given EmpDept must not be null.");
		String serialNo = null;
		if (dept.getParent() != null) { // 如果有上级部门
			List serialNos = getDao().query(
					"select d.serialNo from EmpDept d "
							+ "where d.parent = ? order by d.serialNo desc",
					dept.getParent());
			if (serialNos == null || serialNos.isEmpty()) { // 如果同级部门
				serialNo = buildFirstSerialNo(dept.getParent());
			} else { // 有同级级部门
				String maxSerialNo = (String) serialNos.get(0); // 同级部门最大编号
				// 找出同级部门最大编号的最后两位
				final String[] splited = org.springframework.util.StringUtils
						.commaDelimitedListToStringArray(maxSerialNo);
				if (splited == null || splited.length == 0) { // 同级部门没有编号
					serialNo = buildFirstSerialNo(dept.getParent());
				} else {// 计算当前部门编号
					Integer serial = StringUtil
							.getNumFromSerial(splited[splited.length - 1]);
					serialNo = dept.getParent().getSerialNo() + ","
							+ StringUtil.zeroPadding((serial + 1), 2);
				}
			}
		} else { // 如果没有上级部门
			serialNo = this.getTopDeptSerialNo();
			dept.setSerialNo(serialNo);
		}

		logger.debug("Create serial No." + serialNo);
		return serialNo;
	}

	/**
	 * 计算顶级部门编号
	 */
	public String getTopDeptSerialNo() {
		List serialNos = getDao().query(
				"select d.serialNo from EmpDept d "
						+ "where d.parent is null order by d.serialNo desc");
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
	private Integer getSerial(EmpDept dept, final Integer preSerial) {
		Integer serial = null;
		if (preSerial == null) {
			String serialNo = getSerialNo(dept);
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
		List<EmpDept> tops = getDao().query(
				"from EmpDept d where d.parent is null");
		Map serialMap = new HashMap(100); // 用于存放部门ID-SerialNo
		// 计算所有部门编号，并将部门ID-部门编号的对应关系存入serialMap
		Integer serial = null;
		for (EmpDept top : tops) {
			serial = this.getSerial(top, serial);
			// String serialNo = getSerialNo(top);
			top.setSerialNo(buildSerialNo(null, serial));
			serialMap.put(top.getId(), buildSerialNo(null, serial));
			// getDao().saveObject(top);
			if (top.getChilds().size() > 0) {
				this.updateChildrenSerialNo(top, serialMap);
			}
		}
		// 批量更新部门编号
		Set<Integer> ids = serialMap.keySet();
		for (Integer id : ids) {
			EmpDept dept = get(id);
			getDao().evict(dept);
			dept.setSerialNo((String) serialMap.get(id));
			getDao().getHibernateTemplate().update(dept);
		}
	}

	/**
	 * 更新所有子部门的SerialNo
	 */
	@Transactional
	public void updateChildrenSerialNo(EmpDept parent, Map serialMap) {
		logger.debug("Update " + parent.getName() + "'s children serial No.");
		Set<EmpDept> children = parent.getChilds();
		Integer serial = null;
		for (EmpDept child : children) {
			serial = this.getSerial(child, serial);
			child.setSerialNo(buildSerialNo(parent, serial));
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
	private static String buildFirstSerialNo(EmpDept parent) {
		if (parent == null) {
			return DeptConstants.FIRST_SERIAL_NO;
		} else {
			return new StringBuffer(100).append(parent.getSerialNo())
					.append(",").append(DeptConstants.FIRST_SERIAL_NO)
					.toString();
		}
	}

	/**
	 * 构建部门编号
	 */
	private static String buildSerialNo(EmpDept parent, Integer serial) {
		if (parent == null) {
			return StringUtil.zeroPadding(serial, 2);
		} else {
			return new StringBuffer(100).append(parent.getSerialNo())
					.append(",").append(StringUtil.zeroPadding(serial, 2))
					.toString();
		}
	}

}
