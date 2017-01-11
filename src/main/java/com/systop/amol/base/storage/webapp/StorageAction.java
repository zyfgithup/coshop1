package com.systop.amol.base.storage.webapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.systop.amol.base.storage.StorageConstants;
import com.systop.amol.base.storage.model.Storage;
import com.systop.amol.base.storage.service.StorageManager;
import com.systop.amol.base.storage.service.StorageSecondManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 仓库管理Action
 * 
 * @author songbaojie
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StorageAction extends DefaultCrudAction<Storage, StorageManager> {

	@Autowired
	private StorageSecondManager storageSecondManager;
	/**
	 * 仓库名称
	 */
	private String conditionName;

	/**
	 * 仓库状态
	 */
	private String conditionStatus;

	/**
	 * 异步请求，返回值
	 */
	private Map<String, Object> result;

	/**
	 * 根据条件进行检索仓库信息数据 如果条件不存在将显示全部信息
	 */
	@Override
	public String index() {

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		StringBuffer sql = new StringBuffer("from Storage s where 1=1 ");
		List args = new ArrayList();

		// 根据当前登录用户查询仓库
		User u = UserUtil.getPrincipal(getRequest());
		if (u != null) {
			sql.append(" and s.creator.id = ? ");
			args.add(u.getId());
		}

		if (StringUtils.isNotBlank(conditionStatus)) {
			sql.append(" and s.status = ? ");
			args.add(conditionStatus);
		}

		if (StringUtils.isNotBlank(conditionName)) {
			sql.append(" and s.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(conditionName));
		}
		sql.append(" order by s.createTime desc");
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}

	@Override
	public String remove() {
		User u = UserUtil.getPrincipal(getRequest());
		Assert.notNull(u, "用户不存在");
		if (getModel() != null && getModel().getId() != null) {
			Storage storage = getManager().get(getModel().getId());
			User user = getManager().getDao().get(User.class, u.getId());
			// 返回当前用户仓库数量
			Long count = getManager().getStorageCount(u);
			// 如果是经销商(user.getSuperior() == null )并且仓库数量是一个
			if (storage != null) {
				if (user.getSuperior() == null && count != null && count == 1) {
					// 得到分销商仓库
					Map map = storageSecondManager.getSecondStorageMapRun(u);
					if (map != null && map.size() > 0) {
						this.addActionMessage("当前用户下已经存在分销商仓库并且不允许删除唯一的仓库:["
								+ storage.getName() + "]请查证后再删除!");
						return SUCCESS;
					}
				}
				if (getManager().isNullProjectsByStorageId(getModel().getId())) {
					this.addActionMessage("仓库:[" + storage.getName()
							+ "]下存在货品,查证后再删除!");
				} else {
					super.remove();
				}
			} else {
				this.addActionMessage("删除失败,该仓库不存在!");
			}
		} else {
			this.addActionMessage("用户信息错误,请重新操作!");
		}
		return SUCCESS;
	}

	/**
	 * 选择仓库时使用
	 * 
	 * @return
	 */
	public String find() {
		index();
		return "find";
	}

	/**
	 * 编辑仓库状态
	 * 
	 * @return
	 */
	public String editStatus() {
		Storage storage = getManager().get(getModel().getId());
		storage.setStatus(getModel().getStatus());
		getManager().update(storage);
		return index();
	}

	/**
	 * 编辑仓库信息
	 */
	@Override
	public String edit() {
		// 创建仓库时没有选中状态，那么默认状态为 1
		if (getModel().getId() == null && getModel().getStatus() == null)
			getModel().setStatus(StorageConstants.RUN);
		return super.edit();
	}

	/**
	 * 保存仓库信息
	 */
	@Override
	public String save() {
		User u = UserUtil.getPrincipal(getRequest());
		if (u != null) {
			getModel().setCreator(u);
		} else {
			this.addActionMessage("该用户未正确登录,请重新登录后再次录入！");
		}
		if (hasActionMessages()) {
			return INPUT;
		}
		// 创建新的仓库是需要添加创建时间
		if (getModel() != null) {
			Serializable id = extractId(getModel());
			if (id == null) {
				getModel().setCreateTime(new Date());
			}
		}

		return super.save();
	}

	/**
	 * 异步请求，验证该登录用户下是否存在仓库
	 */
	public String validateStorageIsEmpty() {
		User user = UserUtil.getPrincipal(getRequest());
		Assert.notNull(user, "当前用户不存在");

		result = new HashMap<String, Object>();

		Map map = getManager().getStorageMap(user);
		if (map != null && map.size() > 0) {
			result.put("success", true);
		} else {
			result.put("success", false);
			result.put("msg", "当前用户未创建仓库，请先创建仓库");
		}
		return "jsonSuccess";
	}

	/**
	 * 返回状态Map
	 */
	public Map<String, String> getStatusMap() {
		return StorageConstants.STATUS_MAP;
	}

	public String showIndex() {
		index();
		return "showIndex";
	}

	public String getConditionName() {
		return conditionName;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public String getConditionStatus() {
		return conditionStatus;
	}

	public void setConditionStatus(String conditionStatus) {
		this.conditionStatus = conditionStatus;
	}

}
