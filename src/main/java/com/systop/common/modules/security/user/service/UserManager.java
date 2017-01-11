package com.systop.common.modules.security.user.service;

import com.systop.amol.base.txmoneyset.model.TxMoneySet;
import com.systop.amol.base.txmoneyset.service.TxMoneyManager;
import com.systop.amol.finance.model.TiXianRecord;
import com.systop.amol.finance.service.TiXianRecordManager;
import com.systop.amol.user.AmolUserConstants;
import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.security.user.UserConstants;
import com.systop.common.modules.security.user.dao.UserDao;
import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.model.UserLoginHistory;
import com.systop.common.modules.security.user.service.listener.UserChangeListener;
import com.systop.core.ApplicationException;
import com.systop.core.Constants;
import com.systop.core.service.BaseGenericsManager;
import com.systop.core.util.ReflectUtil;
import com.systop.core.util.ValidationUtil;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 用户管理类
 * 
 * @author Sam Lee
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class UserManager extends BaseGenericsManager<User> {
	/**
	 * 'ADMIN'账户，不能删除。
	 */
	public static final String ADMIN_USER = "admin";
	/**
	 * 用于加密密码
	 */
	private PasswordEncoder passwordEncoder;

	/**
	 * <code>User</code>对象持久化操作监听器，可以直接注入UserListener的实例 或完整的类名。
	 * 
	 * @see {@link com.systop.common.security.user.service. listener.UserChangeListener}
	 */
	private List<UserChangeListener> userChangeListeners;

	/** JdbcTemplate，用户获取密码 */
	private JdbcTemplate jdbcTemplate;

	@Autowired(required = true)
	private UserDao userDao;
	
	@Autowired
	TiXianRecordManager tiXianRecordManager;
	@Autowired
	TxMoneyManager txMoneyManager;
	
	
	public void setTiXianRecordManager(TiXianRecordManager tiXianRecordManager) {
		this.tiXianRecordManager = tiXianRecordManager;
	}

	public void setTxMoneyManager(TxMoneyManager txMoneyManager) {
		this.txMoneyManager = txMoneyManager;
	}

	/**
	 * 通过手机号得到user
	 * @param phone
	 * @return
	 */
	public User getUserPhone(String phone) {
		return this.findObject("from User u where u.loginId=? and u.visible='1' ", phone);
	}
	public User getUserByPhoneAndSort(String phone,String merSortId) {
		return this.findObject("from User u where u.loginId=? and u.productSort.id = ?", phone,Integer.parseInt(merSortId));
	}
	public User getUserByShopIdAndSort(String shopUserId,String merSortId) {
		if(StringUtils.isNotBlank(shopUserId) && StringUtils.isNotBlank(merSortId)){
			return this.findObject("from User u where u.shopOfUser.id=? and u.productSort.id = ?",Integer.parseInt(shopUserId),Integer.parseInt(merSortId));
		}
		return null;
	}
	public double getMoneyOfWitchTypeorders(Integer userId,String type){
		String sql = " select sum(a.spayamount) as allm from sales a left join users b on a.meruser_id = b.id where b.shop_user_id="+userId+" and a.sales_type='"+type+"' and pay_state='1'";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		System.out.println("----------------------sql="+sql);
		if(null!=list && list.size()>0){
			if(null!=list.get(0).get("allm")) {
				return Double.valueOf(String.valueOf(list.get(0).get("allm")));
			}
		}return 0.0;
	}
	public User getUserByOpentId(String openId) {
		return this.findObject("from User u where u.openId=?", openId);
	}
	/**
	 * 通过商家id得到user
	 * @param phone
	 * @return
	 */
	public User getUserById(int id) {
		return this.findObject("from User u where u.id=? and u.visible='1' ", id);
	}
	/*根据区域获得区域负责人
	 * */
	public User findUserByRegion(int regionId) {
		return this.findObject("from User o where o.type='agent' and o.fxsjb='agent_level_county' and  o.region.id = ?", regionId);
		}
	/**
	 * 通过regioncode查询村级分销点
	 * 一个村只有一个分销点
	 * @param orderNo
	 */
	public User findUser(String regionCode) {
	return this.findObject("from User o where o.type='agent' and o.region.code = ?", regionCode);
	}
	public List<User> getMerUserByappId(int userId) {
		return this.query("from User o where o.shopOfUser.id=?",userId);
	}
	/**
	 * 用户提现业务逻辑
	 * 
	 * @param tiXianMoney
	 *          提现金额
	 * @param merchant
	 *          商家
	 * @param tiXianResultMessage
	 *          提现操作返回信息
	 */
	@Transactional
	public TiXianRecord tiXian(double tiXianMoney, User merchant) {
		double txmoney=0;
		List<TxMoneySet> list=txMoneyManager.getTxMoneySet();
		for (TxMoneySet txMoneySet : list) {
			txmoney=txMoneySet.getMoney();
		}
		TiXianRecord tiXianResultMessage = new TiXianRecord();
		tiXianResultMessage.setMerchant(merchant);
		tiXianResultMessage.setCreateTime(new Date());
		tiXianResultMessage.setTiXianMoney(tiXianMoney);
		if (null == merchant.getAllMoney()) {
			tiXianResultMessage.setMessage("亲，您还没有收入");
			return tiXianResultMessage;
		}

		if (txmoney > tiXianMoney) {

			tiXianResultMessage.setMessage("亲，提现最低金额为" + txmoney
					+ "元");
			return tiXianResultMessage;
		}
		if (tiXianMoney > merchant.getMaxTiXianMoney().doubleValue()) {
			tiXianResultMessage.setMessage("您的提现金额" + tiXianMoney + "元，不能大于您的账户最大提现金额"
					+ merchant.getMaxTiXianMoney() + "元");
			return tiXianResultMessage;
		}
		tiXianResultMessage.setMessage("您的提现信息已提交，请注意查收您绑定在沾沾乐平台的银行卡账户");
		tiXianResultMessage.setSqtx(true);
		this.update(merchant);
		tiXianResultMessage.setBalance(merchant.getAllMoney());// 余额
		tiXianResultMessage.setIncomeAll(merchant.getIncomeAll());// 总收入
		// 保存提现记录
		tiXianRecordManager.save(tiXianResultMessage);

		return tiXianResultMessage;
	}
	//返回商家被评论总人数
	public Integer getCntValidate(Integer merId){
		String sql = "select count(distinct(appuser_id)) as count from merchant_validation where meruser_id="+merId;
		return jdbcTemplate.queryForInt(sql);
	}
	//返回加油车抢单数量
	public Integer getCntOfQd(Integer merId){
			String sql = "select count(*) from sales where status = '1' and meruser_id="+merId+" and spayamount is null and sales_type='jy' ";
			return  jdbcTemplate.queryForInt(sql);
	}
	//返回商家被评论的平均分数
	public double getAvgScore(Integer merId){
		String sql = "select avg(score) as avg from merchant_validation where meruser_id="+merId;
		Map<String,Object> map = jdbcTemplate.queryForMap(sql);
		double avgScore = 0.0;
		if(null!=map.get("avg")&&!"".equals(map.get("avg"))){
			avgScore = Double.valueOf(String.valueOf(map.get("avg")));
		}
		return avgScore;
	}
	/**
	 * 获得具有某一<code>Permission</code>的用户
	 * 
	 * @param perm
	 *          给定<code>Permission</code>
	 * @return List of User or empty list.
	 */
	public List<User> findUsersByPermission(Permission perm) {
		return userDao.findUsersByPermission(perm);
	}

	/**
	 * 返回给定用户<code>User</code>所具有的权限（<code>Permission</code>）
	 * 
	 * @param user
	 *          给定用户
	 * @return List of User or empty list.
	 */
	public List<Permission> findPermissionsByUser(User user) {
		return userDao.findPermissionsByUser(user);
	}

	public List<User> getMyJycs(String userId){
		return this.query("from User u where u.superior.id =?",new Object[]{Integer.parseInt(userId)});
	}
	/**
	 * 登录业务逻辑
	 * 根据登录名和密码得到<code>User</code>
	 * 
	 * @param loginId
	 *          登录名
	 * @param password
	 *          密码
	 * @return Instance of <code>User</code> or null.
	 */
	public User getUser(String loginId, String password) {
		List<User> users = getDao().query(
				"from User user where (user.loginId=? or user.nickName=?) and user.password=? and user.visible='1' and user.status='1' ",
				new Object[] { loginId,loginId, password });
		if (users.isEmpty()) {
			return null;
		}
		return users.get(0);
	}
	public List<User> getAppIosUser() {
		List<User> users = getDao().query(
				"from User user where user.type='app_user' and user.iosToken != null");
		return users;
	}
	public List<User> getMerSortById(int sortId) {
		List<User> users = getDao().query(
				"from User user where user.fxsjb='"+AmolUserConstants.AGENT_LEVEL_VILLAGE+"' and user.productSort.id= "+sortId+"");
		return users;
	}
	/**
	 * 根据用户登录名获得<code>User</code>对象
	 * 
	 * @param loginId
	 *          登录ID
	 * @return Instance of <code>User</code> or null.
	 */
	public User getUser(String loginId) {
		List<User> users = getDao().query("from User user where user.loginId=?",
				loginId);
		if (users.isEmpty()) {
			return null;
		}
		return users.get(0);
	}

	/**
	 * 更新用户的登录时间（当前时间）和IP地址
	 * 
	 * @param user
	 *          给定用户
	 * @param loginIp
	 *          给定用户登录所在IP
	 */
	@Transactional
	public void updateLoginInformation(User user, String loginIp) {
		if (user == null || user.getId() == null) {
			logger.error("No user login, return only.");
			return;
		}
		user.setLastLoginIp(loginIp);
		user.setLastLoginTime(new Date());
		user.setOnline(Constants.YES);
		if (user.getLoginTimes() == null) {
			user.setLoginTimes(1);
		} else {
			user.setLoginTimes(user.getLoginTimes().intValue() + 1);
		}

		update(user);
	}

	/**
	 * 添加用户登录历史记录
	 * 
	 * @param user
	 *          给定用户
	 * @param loginIp
	 *          给定用户登录所在IP
	 */
	@Transactional
	public void addUserLoginHistory(User user, String loginIp) {
		if (user == null || user.getId() == null) {
			logger.error("No user login, return only.");
			return;
		}
		if (loginIp.equals("0:0:0:0:0:0:0:1")) {
			loginIp = "127.0.0.1";
		}
		UserLoginHistory userLoginHistory = new UserLoginHistory(user, new Date(),
				loginIp, user.getDept());
		getDao().save(userLoginHistory);
	}

	/**
	 * @see BaseManager#save(java.lang.Object)
	 */
	@Override
	@Transactional
	public void save(User user) {
		save(user, true);
	}
	public void saveUser(User user){
		getDao().getHibernateTemplate().clear();
		getDao().saveOrUpdate(user);
	}
	/**
	 * 根据用户名更新用户的密码.
	 *
	 * @param newPwd
	 * @throws ApplicationException
	 *           如果给定的emailAddr不存在.
	 */
	/**
	 * 保存用户信息，并根据下面的情况决定是否加密密码:<br>
	 * 
	 * <pre>
	 * <ul>
	 *    <li>encodePassword属性不得为null.</li>
	 *    <li>如果是新建用户。</li>
	 *    <li>如果是修改用户，并且输入的密码为空字符串或UserConstants.DEFAULT_PASSWORD。
	 *    此时使用原来的密码</li>
	 * </ul>
	 * </pre>
	 * 
	 * @param user
	 *          <code>User</code> to save.
	 * @param encodePassword
	 *          true if encode password.
	 */
	@Transactional
	public void save(User user, boolean encodePassword) {
		if (user.getId() == null && user.getStatus() == null) { // 新建用户的状态为正常
			user.setCreateTime(new Date());
			user.setStatus(UserConstants.USER_STATUS_USABLE);
		}
		User oldUser = new User(); // 用于同步缓存
		oldUser.setLoginId(user.getLoginId());
		oldUser.setId(user.getId());
		boolean flag = true;
		// 验证重复的登录名
		/*if (getDao().exists(oldUser, new String[] { "loginId" })) {
			flag = false;
			throw new ApplicationException("您输入的登录名'" + oldUser.getLoginId()
					+ "'已经存在.");
		}*/
		// 如果是修改，并且输入的密码为空或UserConstants.DEFAULT_PASSWORD，则表示使用原来的密码
		if (user.getId() != null
				&& (user.getPassword().equals(UserConstants.DEFAULT_PASSWORD) || StringUtils
						.isBlank(user.getPassword()))) {
			String password = getOldPassword(user.getId());
			logger.debug("Use old password '{}'", password);
			user.setPassword(password);
			// 解决a different object with the same identifier value
			// was already associated with the session的问题
			getDao().evict(get(user.getId()));
			getDao().getHibernateTemplate().update(user);
		} else if(flag){
			if (passwordEncoder != null && encodePassword) { // 密码加密
				user.setPassword(passwordEncoder.encodePassword(user.getPassword(),
						null));
			}
			getDao().saveOrUpdate(user);
		}

		if (userChangeListeners != null) { // 执行UserChangeListener，实现缓存同步等功能
			for (Iterator itr = userChangeListeners.iterator(); itr.hasNext();) {
				UserChangeListener listener = getListener(itr.next());
				listener.onSave(user, oldUser);
			}
		}
	}

	/**
	 * 根据用户名更新用户的密码.
	 * 
	 * @param newPwd
	 * @throws ApplicationException
	 *           如果给定的emailAddr不存在.
	 */
	@Transactional
	public void updatePasswordByEmail(String emailAddr, String loginId,
			String newPwd) {
		Assert.hasText(emailAddr, "E-Mail must not be empty.");

		User user = findObject("from User u where u.loginId=?", loginId);
		if (user == null) {
			throw new ApplicationException("用户名''" + loginId + "不存在");
		}
		if (!StringUtils.equalsIgnoreCase(emailAddr, user.getEmail())) {
			throw new ApplicationException("注册邮箱地址不匹配.");
		}
		user.setPassword(newPwd);
		save(user, true);
	}

	/**
	 * @see BaseManager#remove(java.lang.Object)
	 */
	@Override
	@Transactional
	public void remove(User user) {
		if (Constants.STATUS_UNAVAILABLE.equals(user.getStatus())) {
			return;
		}
		// 确保LoginID存在
		if (StringUtils.isBlank(user.getLoginId())) {
			user = get(user.getId());
		}
		// 确保不能删除（禁用）Admin账户.
		if (ADMIN_USER.equals(user.getLoginId())) {
			logger.warn("不能删除或禁用admin账户，这是一个危险的动作哦。");
			return;
		}
		user.setStatus(Constants.STATUS_UNAVAILABLE);
		if (userChangeListeners != null) {
			for (Iterator itr = userChangeListeners.iterator(); itr.hasNext();) {
				UserChangeListener listener = getListener(itr.next());
				listener.onRemove(user);
			}
		}
	}

	/**
	 * 启用用户
	 * 
	 * @param user
	 */
	@Transactional
	public void unsealUser(User user) {
		if (Constants.STATUS_AVAILABLE.equals(user.getStatus())) {
			return;
		}
		user.setStatus(Constants.STATUS_AVAILABLE);
		if (userChangeListeners != null) {
			for (Iterator itr = userChangeListeners.iterator(); itr.hasNext();) {
				UserChangeListener listener = getListener(itr.next());
				listener.onSave(user, user);
			}
		}
	}

	/**
	 * 根据类别获得用户
	 * 
	 * @param type
	 * @return
	 */
	public List<User> getUserByType(String type) {
		String hql = "from User u where u.type = ? and u.status = '1' order by u.createTime desc";
		return getDao().query(hql, type);
	}
	public List<User> getFxsAppUser(String type) {
		StringBuffer hql =new StringBuffer( "from User u where ( u.type ='app_user' or u.type='vip') and  u.status = '1' order by u.createTime desc");
		return this.getDao().query(hql.toString());
	}
	public Map getCompanys() {
		List<User> coms = getUserByType(AmolUserConstants.COMPANY);
		Map companys = new HashMap();
		for (User u : coms) {
			companys.put(u.getId(), u.getName());
		}
		return companys;
	}

	/**
	 * @param userChangeListeners
	 *          the userChangeListeners to set
	 */
	@Autowired
	public void setUserChangeListeners(
			List<UserChangeListener> userChangeListeners) {
		this.userChangeListeners = userChangeListeners;
	}


	/**
	 * 返回指定UserId的密码
	 */
	private String getOldPassword(Integer userId) {
		return (String) jdbcTemplate.query("select password from users where id=?",
				new Object[] { userId }, new ResultSetExtractor() {

					public Object extractData(ResultSet rs) throws SQLException {
						rs.next();
						return rs.getString(1);
					}

				});
	}

	/**
	 * 修改密码
	 * 
	 * @param model
	 *          封装密码的User对象
	 * @param oldPassword
	 *          旧的密码
	 * @throws Exception
	 */
	@Transactional
	public void changePassword(User model, String oldPassword) {
		// 获得加密后的密码
		String encodePasswrod = passwordEncoder.encodePassword(oldPassword, null);
		// 判断输入的原密码和数据库中的原密码是否一致
		if (encodePasswrod.equals(getOldPassword(model.getId()))) {
			User user = getDao().get(User.class, model.getId());
			// 设置新密码
			user.setPassword(passwordEncoder.encodePassword(model.getPassword(), null));
			getDao().getHibernateTemplate().update(user);
			synchronousCache(user, user);
		} else {
			throw new ApplicationException("旧密码不正确!");
		}
	}

	/**
	 * 根据用户名修改密码,不进行密码有效验证,直接修改
	 * 
	 * @param userName
	 * @param newPass
	 */
	@Transactional
	public void changePasswords(String userName, String newPass) {
		// 获得加密后的密码
		String hql = "from User u where u.loginId = ?";
		User user = findObject(hql, userName);
		if (user != null && StringUtils.isNotBlank(newPass)) {
			String encodePasswrod = passwordEncoder.encodePassword(newPass, null);
			user.setPassword(encodePasswrod);
			getDao().getHibernateTemplate().update(user);
			synchronousCache(user, user);
		} else {
			throw new ApplicationException("修改密码未找到指定用户：" + userName);
		}
	}

	/**
	 * 前台注册用户修改个人信息
	 * 
	 * @see BaseManager#update(java.lang.Object)
	 */
	@Override
	@Transactional
	public void update(User user) {
		if (user != null) {
			getDao().update(user);
			synchronousCache(user, user);
		}
	}

	/**
	 * 得到loginId为{@link #ADMIN_USER}账户.
	 * 
	 * @return Instance of {@code User}
	 * @throws NoSuchUserException
	 *           如果{@link #ADMIN_USER}账户不存在
	 * @throws InvalidUserEmailException
	 *           如果{@link #ADMIN_USER}账户的email不合法
	 */
	public User getAdmin() throws NoSuchUserException, InvalidUserEmailException {
		User admin = getUser(ADMIN_USER);
		if (admin == null) {
			throw new NoSuchUserException("admin 账户不存在.");
		}
		if (!ValidationUtil.isValidEmail(admin.getEmail())) {
			throw new InvalidUserEmailException("admin 账户电子邮件不合法-" + admin.getEmail());
		}

		return admin;
	}

	/**
	 * 根据用户部门ID,该部门下以及子部门下的所有员工
	 * @param id 部门ID
	 */
	public List<User> getUsersByDeptId(Integer id) {
		Dept dept = getDao().get(Dept.class,id);
		List<User> list = null;
		if(dept != null){
			String hql = "from User u where u.dept.serialNo like ? ";
			list = getDao().query(hql, MatchMode.END.toMatchString(dept.getSerialNo()));
		}
		return list;
	}
	
	/**
	 * 根据地区查询所有用户
	 * @param id 部门ID
	 */
	public List<User> getUserByRegionCode(String code) {
		List<User> list = null;
			String hql = "from User u where u.region.code like ? ";
			list = getDao().query(hql, MatchMode.START.toMatchString(code));
		return list;
	}
	
	/**
	 * 返回部门以及子部门下的员工信息
	 * 
	 * @param dept
	 *          部门树形列表
	 */
	public Map getUserTree(Map dept, String roleName) {
		if (dept.isEmpty()) {
			return dept;
		}
		// 设置部门类型
		dept.put("type", dept.get("type"));
		// 得到部门ID
		Integer deptId = (Integer) dept.get("id");
		List<User> list;
		if (StringUtils.isBlank(roleName)) {
			list = query("from User u where u.dept.id=?", deptId);
		} else {
			list = query(
					"select u from User u inner join u.roles r where u.dept.id=? and r.name=?",
					deptId, roleName);
		}
		// 添加本部门员工
		List children = new ArrayList();
		for (User user : list) {
			Map map = new HashMap();
			map.put("id", user.getId());
			map.put("text", user.getName());
			map.put("type", "user");
			map.put("sex", user.getSex());
			map.put("leaf", true);
			children.add(map);
		}

		// 得到此部门下的所有子部门
		List<Map> childNodes = (List) dept.get("childNodes");
		if (childNodes != null) {
			for (Map map : childNodes) {
				map = getUserTree(map, roleName);
				children.add(map);
			}
		}
		if (!children.isEmpty()) {
			dept.put("children", children);
			dept.put("childNodes", children);
			dept.put("leaf", false);
		} else {
			dept.put("leaf", true);
		}
		return dept;
	}

	/**
	 * 同步缓存
	 * 
	 * @param user
	 *          新用户
	 * @param oldUser
	 *          旧用户
	 */
	private void synchronousCache(User user, User oldUser) {
		if (userChangeListeners != null) {
			// 执行UserChangeListener，实现缓存同步等功能
			for (Iterator itr = userChangeListeners.iterator(); itr.hasNext();) {
				UserChangeListener listener = getListener(itr.next());
				listener.onSave(user, oldUser);
			}
		}
	}

	/**
	 * 根据登陆名称取得用户
	 * 
	 * @param uId
	 * @param uName
	 */
	public List<User> getUserByName(Integer id, String loginId) {
		List<User> list = Collections.EMPTY_LIST;
		StringBuffer hql = new StringBuffer("from User u where u.loginId = ?");
		if (id != null) {
			hql.append(" and u.id != ?");
			list = query(hql.toString(), new Object[] { loginId, id });
		} else {
			list = query(hql.toString(), loginId);
		}
		return list;
	}

	/**
	 * 根据身份证获得用户
	 * 
	 * @param uId
	 * @param idCard
	 * @return
	 */
	public List<User> getUserByIdCard(Integer id, String idCard) {
		List<User> list = Collections.EMPTY_LIST;
		StringBuffer hql = new StringBuffer("from User u where u.idCard = ?");
		if (id != null) {
			hql.append(" and u.id != ?");
			list = query(hql.toString(), new Object[] { idCard, id });
		} else {
			list = query(hql.toString(), idCard);
		}
		return list;
	}

	/**
	 * 根据email取得用户
	 * 
	 * @param uId
	 * @param email
	 */
	public List<User> getUserByEmail(String uId, String email) {
		List<User> list = Collections.EMPTY_LIST;
		StringBuffer hql = new StringBuffer("from User u where u.email = ?");
		if (StringUtils.isNotBlank(uId)) {
			hql.append(" and u.id != ?");
			list = query(hql.toString(), new Object[] { email, Integer.valueOf(uId) });
		} else {
			list = query(hql.toString(), email);
		}
		return list;
	}

	/**
	 * 执行监听器
	 * 
	 * @see UserListener
	 */
	private UserChangeListener getListener(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof String) {
			return (UserChangeListener) ReflectUtil.newInstance((String) obj);
		} else {
			return (UserChangeListener) obj;
		}
	}

	/**
	 * 判断某个用户是否在线
	 * 
	 * @param user
	 *          用户实体
	 * @return 如果在线(online=1),返回true
	 */
	public boolean isUserOnline(User user) {
		if (user == null || user.getId() == null) {
			logger.warn("用户对象为null或者id为null，无法判断用户是否在线。");
			return false;
		}
		user = get(user.getId());
		return Constants.YES.equals(user.getOnline());
	}

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 销售者是否是经销商
	 * 
	 * @param userId
	 * @return
	 */
	public Boolean getOwner(String userId, Integer customerOwnerId) {
		// 如果是顶级经销商，他可以查看自己以及自己下属的分销商的所有销售信息情况
		if (StringUtils.isNotBlank(userId)) {
			User userSQL = this.get(new Integer(userId));
			if (userSQL.getSuperior() == null) {
				if (userSQL.getId().intValue() == customerOwnerId.intValue()) {
					return true;
				} else {
					return false;
				}
			} else {
				if (userSQL.getSuperior().getId().intValue() == customerOwnerId
						.intValue()) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * 根据地区id得到商家
	 * @param regionId 地区id
	 * @return
	 */
	public List<User> queryRegionUser(Integer regionId) {
	return this.query("from User o where o.region.id = ? and o.superior != null", regionId);
	}
}