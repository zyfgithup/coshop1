package com.systop.amol.base.storage.webapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 经销商对分销商的仓库管理Action
 * 
 * @author songbaojie
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StorageSecondAction extends
		DefaultCrudAction<Storage, StorageSecondManager> {

	/**
	 * 分销商仓库名称
	 */
	private String conditionName;

	/**
	 * 分销商仓库名称ID
	 */
	private String conditionId;

	@Autowired
	private StorageManager storageManager;
	
	/**
	 * 根据条件进行检索仓库信息数据 如果条件不存在将显示全部信息
	 */
	@Override
	public String index() {
		// 根据当前登录用户查询仓库
		User u = UserUtil.getPrincipal(getRequest());

		Assert.notNull(u, "用户不存在");

		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		List args = new ArrayList();
		StringBuffer sql = new StringBuffer(
				"from Storage s where s.creator in (from User u where u.superior.id = ? and u.type = 'agent')");
		args.add(u.getId());

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
			if (storage != null) {
				if (storageManager.isNullProjectsByStorageId(getModel().getId())) {
					this.addActionMessage("仓库:[" + storage.getName()
							+ "]下存在货品,查证后在删除!");
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
		Assert.notNull(u, "当前用户不存在");

		if (getModel() != null && getModel().getCreator() != null
				&& getModel().getCreator().getId() != null) {
			Serializable id = extractId(getModel());
			if (id == null) {
				// 创建新的仓库是需要添加创建时间
				getModel().setCreateTime(new Date());
				// 分销商仓库默认都为可用
				getModel().setStatus(StorageConstants.RUN);
			}
		} else {
			throw new ApplicationException("发生异常,请与管理员联系！");
		}
		if (hasActionMessages()) {
			return INPUT;
		}
		return super.save();
	}

	/**
	 * 当前用户的下级经销商
	 */
	public Map getSuperiorsMap() {
		User user = UserUtil.getPrincipal(getRequest());
		Map superiorsMap = new HashMap();
		if (user != null) {
			// 加载当前用户避免SESSION关闭
			User u = getManager().getDao().get(User.class, user.getId());
			Iterator<User> ite = u.getChildSuperiors().iterator();
			while (ite.hasNext()) {
				User temp = ite.next();
				if(temp.getType().equals(AmolUserConstants.AGENT))
					superiorsMap.put(temp.getId(), temp.getName());
			}
		}
		return superiorsMap;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public String getConditionId() {
		return conditionId;
	}

	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}

}
