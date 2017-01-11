package com.systop.amol.database.service;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.systop.amol.base.customer.model.Customer;
import com.systop.amol.base.customer.service.CustomerManager;
import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.product.service.ProductsManager;
import com.systop.amol.base.prosort.model.ProductSort;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.amol.base.storage.model.Storage;
import com.systop.amol.base.storage.service.StorageManager;
import com.systop.amol.base.supplier.model.Supplier;
import com.systop.amol.base.supplier.service.SupplierManager;
import com.systop.amol.base.units.model.Units;
import com.systop.amol.base.units.model.UnitsItem;
import com.systop.amol.base.units.service.UnitsItemManager;
import com.systop.amol.base.units.service.UnitsManager;
import com.systop.amol.card.model.Card;
import com.systop.amol.card.model.CardGrant;
import com.systop.amol.card.model.CardSpend;
import com.systop.amol.card.model.CardUp;
import com.systop.amol.card.service.CardGrantManager;
import com.systop.amol.card.service.CardManager;
import com.systop.amol.card.service.CardSpendManager;
import com.systop.amol.card.service.CardUpManager;
import com.systop.amol.database.model.DataBackup;
import com.systop.amol.finance.model.Cost;
import com.systop.amol.finance.model.CostDetail;
import com.systop.amol.finance.model.CostSort;
import com.systop.amol.finance.model.FundsSort;
import com.systop.amol.finance.service.CostDetailManager;
import com.systop.amol.finance.service.CostManager;
import com.systop.amol.finance.service.CostSortManager;
import com.systop.amol.finance.service.FundsSortManager;
import com.systop.amol.init.service.InitDataManager;
import com.systop.amol.purchase.model.*;
import com.systop.amol.purchase.service.*;
import com.systop.amol.sales.SalesConstants;
import com.systop.amol.sales.model.*;
import com.systop.amol.sales.service.*;
import com.systop.amol.stock.StockConstants;
import com.systop.amol.stock.model.*;
import com.systop.amol.stock.service.*;
import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.dept.service.DeptManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.model.UserLoginHistory;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;
import com.systop.core.Constants;
import com.systop.core.service.BaseGenericsManager;
import org.apache.commons.lang.xwork.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 数据备份管理
 * 
 * @author songbaojie
 * 
 */
@Service
public class DataBackupManager extends BaseGenericsManager<DataBackup> {

	/**
	 * Log of the class
	 */
	private static final Logger logger = LoggerFactory.getLogger(DataBackupManager.class);
	
	@Autowired
	private InitDataManager initDataManager;
	/**Songbaojie START*/
	//基础
	//部门
	@Autowired
	private DeptManager deptManager;
	//商品类型
	@Autowired
	private ProductSortManager productSortManager;
	//仓库
	@Autowired
	private StorageManager storageManager;
	//库存
	@Autowired
	private StockManager stockManager;
	@Autowired
	private StockCheckManager stockCheckManager;
	@Autowired
	private StockCheckDetailManager stockCheckDetailManager;
	@Autowired
	private StockCheckLPManager stockCheckLPManager;
	@Autowired
	private StockTracDetailManager stockTracDetailManager;
	@Autowired
	private StockTracManager stockTracManager;
	@Autowired
	private StockInitManager stockInitManager;
	/**Songbaojie END*/
	
	@Autowired
	private PayManager payManager;
	@Autowired
	private ProductsManager productsManager;
	@Autowired
	private UnitsManager unitsManager;
	@Autowired
	private UnitsItemManager unitsItemManager;
	@Autowired
	private PayDetailManager payDetailManager;
	@Autowired
	private PayInitManager payInitManager;
	@Autowired
	private PurchaseManager purchaseManager;
	@Autowired
	private PayAbleManager payAbleManager;
	@Autowired
	private PurchaseDetailManager purchaseDetailManager;
	@Autowired
	BarcodeManager barcodeManager;
	@Autowired
	SalesDetailManager salesDetailManager;
	@Autowired
	ReceiveDetailManager receiveDetailManager;
	@Autowired
	ReceiveManager receiveManager;
	@Autowired
	SalesManager salesManager;
	@Autowired
	ReceiveInitManager receiveInitManager;
	
	@Autowired
	private CostManager costManager;
	@Autowired
	private CostDetailManager costDetailManager;
	@Autowired
	private FundsSortManager fundsSortManager;
	@Autowired
	private CostSortManager costSortManager;
	@Autowired
	private CardManager cardManager;
	@Autowired
	private CardSpendManager cardSpendManager;
	@Autowired
	private CardUpManager cardUpManager;
	@Autowired
	private CardGrantManager cardGrantManager;
	@Autowired
	private SupplierManager supplierManager;
	@Autowired
	private CustomerManager customerManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(DataBackup entity) {
		File file = new File(entity.getDataUrl()+entity.getDataFileName());
		if(file.exists()){
			if(file.isFile()){
				file.delete();
			}else{
				logger.error("删除编号："+entity.getId()+"失败！删除的不是文件");
			}
		}else{
			logger.error("删除编号："+entity.getId()+"失败！此文件不存在");
		}
		super.remove(entity);
	}

	/**
	 * 保存数据备份(数据库全部备份,Windwos系统)
	 * 
	 * @param entity
	 *            数据备份对象
	 * @return void
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveAllDataBackup(ServletContext sctx,DataBackup entity) {
		StringBuffer mysql = new StringBuffer();
		try {
			ComboPooledDataSource dataSource = (ComboPooledDataSource)StockConstants.getBean(sctx, "dataSource");
			mysql.append("mysqldump --user=");
			mysql.append(dataSource.getUser()); //用户名
			mysql.append(" --password=");
			mysql.append(dataSource.getPassword()); //密码
			if(StringUtils.isNotBlank(Constants.DATABASE_NAME)){
				mysql.append(" --opt " + Constants.DATABASE_NAME + "> "); //amol 数据库名	
			}else{
				mysql.append(" --opt amol> "); //amol 数据库名
			}
			mysql.append("\""+sctx.getRealPath(entity.getDataUrl())+File.separatorChar); //保存url
			mysql.append(entity.getDataFileName()+"\""); //保存文件名
            java.lang.Runtime.getRuntime().exec("cmd /c " + mysql.toString());
            super.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			//发生异常后删除该文件
			File file = new File(sctx.getRealPath(entity.getDataUrl()) + File.separatorChar +entity.getDataFileName());
			if(file.exists()){
				if(file.isFile()){
					file.delete();
				}
			}
			throw new ApplicationException("生成备份文件发生异常");
		}
	}
	
	/**
	 * 保存数据备份(数据库全部备份,Linux系统)
	 * 
	 * @param sctx 
	 * @param entity
	 *            数据备份对象
	 * @return void
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveAllDataBackupLinux(ServletContext sctx, DataBackup entity) {
		StringBuffer mysql = new StringBuffer();
		try {
			ComboPooledDataSource dataSource = (ComboPooledDataSource)StockConstants.getBean(sctx, "dataSource");
			mysql.append("mysqldump --user=");
			mysql.append(dataSource.getUser()); //用户名
			mysql.append(" --password=");
			mysql.append(dataSource.getPassword()); //密码
			if(StringUtils.isNotBlank(Constants.DATABASE_NAME)){
				mysql.append(" --opt " + Constants.DATABASE_NAME + "> "); //amol 数据库名	
			}else{
				mysql.append(" --opt amol> "); //amol 数据库名
			}
			mysql.append("\"" + sctx.getRealPath(entity.getDataUrl()) + File.separatorChar ); //保存url
			mysql.append(entity.getDataFileName()+"\""); //保存文件名
            java.lang.Runtime.getRuntime().exec(new String[] { "sh", "-c", mysql.toString()});
            super.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			//发生异常后删除该文件
			File file = new File(sctx.getRealPath(entity.getDataUrl()) + File.separatorChar + entity.getDataFileName());
			if(file.exists()){
				if(file.isFile()){
					file.delete();
				}
			}
			throw new ApplicationException("生成备份文件发生异常");
		}
	}
	
	/**
	 * 保存数据备份(经销商备份)
	 * 
	 * @param sctx 
	 * @param entity
	 *            数据备份对象
	 * @return void
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveDataBackup(ServletContext sctx,DataBackup entity) {
		try {
			generateDataBackupFile(sctx, entity);
			super.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			//发生异常后删除该文件
			File file = new File(sctx.getRealPath(entity.getDataUrl())+entity.getDataFileName());
			if(file.exists()){
				if(file.isFile()){
					file.delete();
				}
			}
			throw new ApplicationException("生成备份文件发生异常");
		}
	}
	
	/**
	 * 返回指定Id的user_role
	 */
	@SuppressWarnings("rawtypes")
	private List getUserRole(User user) {
		String sql = "select user_id,role_id from user_role where user_id in (select id from users where superior_id = '" + user.getId() + "')";
		List userRoleList = this.jdbcTemplate.queryForList(sql); 
		
		return userRoleList;
	}
	
	@SuppressWarnings("rawtypes")
	public String toInsertSql(User user){
		StringBuffer sql = new StringBuffer();
		List roles = getUserRole(user);
		Iterator it=roles.iterator();
		while(it.hasNext()){
		   sql.append("insert into user_role (user_id,role_id) VALUES(");
		   Map result=(Map)it.next();
		   String user_id = result.get("user_id").toString();
		   String role_id = result.get("role_id").toString();
		   sql.append(user_id);
		   sql.append(",");
		   sql.append(role_id);
		   sql.append(")");
		   sql.append("\n");
		}				
		return sql.toString();
	}
	
	/**
	 * 生成备份文件
	 * 
	 * @param sctx 
	 * @param entity
	 *            备份对象
	 * @return void
	 * @throws IOException
	 */
	public void generateDataBackupFile(ServletContext sctx, DataBackup entity) throws IOException {
		FileWriter fw = new FileWriter(sctx.getRealPath(entity.getDataUrl())+ File.separatorChar + entity.getDataFileName());
		BufferedWriter bw = new BufferedWriter(fw);
		try{
			
			//基础数据在最前方
			//部门
			List<Dept> listDepts = deptManager.query("from Dept d where d.user.id = ?", entity.getUser().getId());
			for(Dept d : listDepts){
				bw.write(d.toInsertSql());
				bw.newLine();
			}
			
			//用户登录记录
			@SuppressWarnings("unchecked")
			List<UserLoginHistory> histories = userManager.getDao().query("from UserLoginHistory ulh where ulh.user.id = ?", entity.getUser().getId());
			for (UserLoginHistory userLoginHistory : histories) {
				bw.write(userLoginHistory.toInsertSql());
				bw.newLine();
			}
			//用户角色
			bw.write(toInsertSql(entity.getUser()));
			//客户
			List<Customer> customers = customerManager.query("from Customer c where c.owner.id = ?", entity.getUser().getId());
			for (Customer customer : customers) {
				bw.write(customer.toInsertSql());
				bw.newLine();
			}
			//供应商
			List<Supplier> suppliers = supplierManager.query("from Supplier s where s.user.id = ? ", entity.getUser().getId()); 
			for (Supplier supplier : suppliers) {
				bw.write(supplier.toInsertSql());
				bw.newLine();
			}
			//商品类型
			List<ProductSort> listProductSorts = productSortManager.query("from ProductSort p where p.creator.id = ?", entity.getUser().getId());
			for(ProductSort p : listProductSorts){
				bw.write(p.toInsertSql());
				bw.newLine();
			}
		    // 计量单位
			List<Units> listUnit = unitsManager.query("from Units s where s.user.id = ? ", entity.getUser().getId());
			for (Units s : listUnit) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}
		    // 商品
			List<Products> listproducts = productsManager.query("from Products s where s.user.id = ? ", entity.getUser().getId());
			for (Products s : listproducts) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}
		    //计量单位换算 
			List<UnitsItem> listui = unitsItemManager.query("from UnitsItem s where s.user.id = ? ", entity.getUser().getId());
			for (UnitsItem s : listui) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}	
			//仓库
			List<Storage> listStorages = storageManager.query("from Storage s where s.creator.id = ? or s.creator.superior.id=?", entity.getUser().getId(),entity.getUser().getId());
			for(Storage s : listStorages){
				bw.write(s.toInsertSql());
				bw.newLine();
			}		
			//及时库存
			List<Stock> listStocks = stockManager.query("from Stock s where s.user.id = ? or s.user.superior.id=?", entity.getUser().getId(),entity.getUser().getId());
			for(Stock s : listStocks){
				bw.write(s.toInsertSql());
				bw.newLine();
			}
			//采购数据
			// 初始化数据
			List<PayInit> listPayInit = payInitManager.query("from PayInit s where s.user.id = ? ", entity.getUser().getId());
			for (PayInit s : listPayInit) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}
			// 订单
			List<Purchase> listp = purchaseManager.query("from Purchase s where s.user.id = ? or s.user.superior.id=?", entity.getUser().getId(),entity.getUser().getId());
			for (Purchase s : listp) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}
			// 订单详单
			List<PurchaseDetail> listpd = purchaseDetailManager.query("from PurchaseDetail s where s.purchase.user.id = ? or s.purchase.user.superior.id=?", entity.getUser().getId(),entity.getUser().getId());
			for (PurchaseDetail s : listpd) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}
			// 应付单
			List<Pay> listPay = payManager.query("from Pay s where s.user.id = ? or s.user.superior.id=?", entity.getUser().getId(),entity.getUser().getId());
			for (Pay s : listPay) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}
			// 应付详单
			List<PayDetail> listPayDetail = payDetailManager.query("from PayDetail s where s.pay.user.id = ? or s.pay.user.superior.id=?", entity.getUser().getId(),entity.getUser().getId());
			for (PayDetail s : listPayDetail) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}
			
			// 及时应付
			List<PayAble> listpa =payAbleManager.query("from PayAble s where s.user.id = ? ",entity.getUser().getId());
			for (PayAble s : listpa) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}		
			
			/**财务*/
			List<CostSort> costSorts = costSortManager.query("from CostSort cs where cs.user.id = ?", entity.getUser().getId());
			for (CostSort costSort : costSorts) {
				bw.write(costSort.toInsertSql());
				bw.newLine();
			}
			List<FundsSort> fundsSorts = fundsSortManager.query("from FundsSort fs where fs.user.id = ?", entity.getUser().getId());
			for (FundsSort fundsSort : fundsSorts) {
				bw.write(fundsSort.toInsertSql());
				bw.newLine();
			}
			List< Cost> costs = costManager.query("from Cost c where c.user.id = ? or c.user.id in (select u.id from User u where u.superior.id = ?)", entity.getUser().getId(),entity.getUser().getId());
			for (Cost cost : costs) {
				bw.write(cost.toInsertSql());
				bw.newLine();
			}
			List<CostDetail> costDetails = costDetailManager.query("from CostDetail cd where cd.cost.id in (select c.id from Cost c where c.user.id = ? or c.user.id in (select u.id from User u where u.superior.id = ?))", entity.getUser().getId(),entity.getUser().getId());
			for (CostDetail costDetail : costDetails) {
				bw.write(costDetail.toInsertSql());
				bw.newLine();
			}

			/**卡的管理*/
			List<Card> cards = cardManager.query("from Card c where c.id in (select cg.card.id from CardGrant cg where cg.customer.id in (select cu.id from Customer cu where cu.owner.id = '" + entity.getUser().getId() + "'))");
			for (Card card : cards) {
				bw.write(card.toUpdateSql());
				bw.newLine();
			}
			List<CardGrant> cardGrants = cardGrantManager.query("from CardGrant cg where cg.customer.id in (select c.id from Customer c where c.owner.id = '" + entity.getUser().getId() + "')");
			for (CardGrant cardGrant : cardGrants) {
				bw.write(cardGrant.toInsertSql());
				bw.newLine();
			}
			List<CardSpend> cardSpends = cardSpendManager.query("from CardSpend cs where cs.cardGrant.id in (select cg.id from CardGrant cg where cg.customer.id in (select c.id from Customer c where c.owner.id = '" + entity.getUser().getId() + "'))");
			for (CardSpend cardSpend : cardSpends) {
				bw.write(cardSpend.toInsertSql());
				bw.newLine();
			}
			List<CardUp> cardUps = cardUpManager.query("from CardUp cu where cu.cardGrant.id in (select cg.id from CardGrant cg where cg.customer.id in (select c.id from Customer c where c.owner.id = '" + entity.getUser().getId() + "'))");
			for (CardUp cardUp : cardUps) {
				bw.write(cardUp.toInsertSql());
				bw.newLine();
			}
			
		  // 销售数据
		  // 订单
			List<Sales> listSalseOrder = salesManager.query("from Sales s where s.status = "+SalesConstants.ORDERS+" and (s.user.superior.id='" + entity.getUser().getId() + "' or s.user.id='" + entity.getUser().getId() + "'))");
			for (Sales s : listSalseOrder) {
				bw.write(s.toInserSql());
				bw.newLine();
			}
			// 出库单
			List<Sales> listSalse = salesManager.query("from Sales s where s.status = "+SalesConstants.SALES+" and (s.user.superior.id='" + entity.getUser().getId() + "' or s.user.id='" + entity.getUser().getId() + "'))");
			for (Sales s : listSalse) {
				bw.write(s.toInserSql());
				bw.newLine();
			}
			// 退货单
			List<Sales> listSalseReturn = salesManager.query("from Sales s where s.status = "+SalesConstants.RETURNS+" and (s.user.superior.id='" + entity.getUser().getId() + "' or s.user.id='" + entity.getUser().getId() + "'))");
			for (Sales s : listSalseReturn) {
				bw.write(s.toInserSql());
				bw.newLine();
			}
		  // 期初应收单
			List<ReceiveInit> listReceiveInit = receiveInitManager.query("from ReceiveInit ri where ri.user.superior.id='" + entity.getUser().getId() + "' or ri.user.id='" + entity.getUser().getId() + "'))");
			for (ReceiveInit ri : listReceiveInit) {
				bw.write(ri.toInserSql());
				bw.newLine();
			}
	  	// 回款单
			List<Receive> listReceive = receiveManager.query("from Receive r where r.user.superior.id='" + entity.getUser().getId() + "' or r.user.id='" + entity.getUser().getId() + "'))");
			for (Receive r : listReceive) {
				bw.write(r.toInserSql());
				bw.newLine();
			}
		  // 回款详情
			List<ReceiveDetail> listrReceiveDetail = receiveDetailManager.query("from ReceiveDetail rd where rd.receive.user.superior.id='" + entity.getUser().getId() + "' or rd.receive.user.id='" + entity.getUser().getId() + "'))");
			for (ReceiveDetail rd : listrReceiveDetail) {
				bw.write(rd.toInserSql());
				bw.newLine();
			}
		  // 订单 详情
			List<SalesDetail> listSalesDetailOrder = salesDetailManager.query("from SalesDetail sd where sd.sales.status= "+SalesConstants.ORDERS+" and (sd.sales.user.superior.id='" + entity.getUser().getId() + "' or sd.sales.user.id='" + entity.getUser().getId() + "'))");
			for (SalesDetail sd : listSalesDetailOrder) {
				bw.write(sd.toInserSql());
				bw.newLine();
			}
		  // 出库单详情
			List<SalesDetail> listSalesDetailSales = salesDetailManager.query("from SalesDetail sd where sd.sales.status= "+SalesConstants.SALES+" and (sd.sales.user.superior.id='" + entity.getUser().getId() + "' or sd.sales.user.id='" + entity.getUser().getId() + "'))");
			for (SalesDetail sd : listSalesDetailSales) {
				bw.write(sd.toInserSql());
				bw.newLine();
			}
		  // 退货单详情
			List<SalesDetail> listSalesDetailReturn = salesDetailManager.query("from SalesDetail sd where sd.sales.status= "+SalesConstants.RETURNS+" and (sd.sales.user.superior.id='" + entity.getUser().getId() + "' or sd.sales.user.id='" + entity.getUser().getId() + "'))");
			for (SalesDetail sd : listSalesDetailReturn) {
				bw.write(sd.toInserSql());
				bw.newLine();
			}
			// 条形码
			List<Barcode> listBarcode= barcodeManager.query("from Barcode b where b.salesDetail.sales.user.id = ? or b.salesDetail.sales.user.superior.id = ? ", entity.getUser().getId(), entity.getUser().getId());
			for (Barcode b : listBarcode) {
				bw.write(b.toInserSql());
				bw.newLine();
			}
			
			
			/**库存*/
			//库存盘点
			//库存盘点单
			List<StockCheck> listStockChecks = stockCheckManager.query("from StockCheck s where s.user.id = ? or s.user.superior.id = ?", entity.getUser().getId(),entity.getUser().getId());
			for (StockCheck s : listStockChecks) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}
			//库存盘点单详单
			List<StockCheckDetail> listStockCheckDetails = stockCheckDetailManager.query("from StockCheckDetail s where s.user.id = ? or s.user.superior.id = ?", entity.getUser().getId(),entity.getUser().getId());
			for (StockCheckDetail s : listStockCheckDetails) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}
			//库存盘点单损益单
			List<StockCheckLP> listStockCheckLPs = stockCheckLPManager.query("from StockCheckLP s where s.user.id = ? or s.user.superior.id = ?", entity.getUser().getId(),entity.getUser().getId());
			for (StockCheckLP s : listStockCheckLPs) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}			
			//库存调拨
			//库存调拨单
			List<StockTrac> listStockTracs = stockTracManager.query("from StockTrac s where s.user.id = ? or s.user.superior.id = ?", entity.getUser().getId(),entity.getUser().getId());
			for (StockTrac s : listStockTracs) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}
			//库存调拨详单
			List<StockTracDetail> listStockTracDetails = stockTracDetailManager.query("from StockTracDetail s where s.user.id = ? or s.user.superior.id = ?", entity.getUser().getId(),entity.getUser().getId());
			for (StockTracDetail s : listStockTracDetails) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}
			//期初库存
			List<StockInit> listStockInits = stockInitManager.query("from StockInit s where s.user.id = ?", entity.getUser().getId());
			for (StockInit s : listStockInits) {
				bw.write(s.toInsertSql());
				bw.newLine();
			}
		}catch (Exception e) {
			bw.flush();
			bw.close();
			throw new ApplicationException(e.getMessage());
		}
		bw.flush();
		bw.close();
	}

	/**
	 * 数据库恢复
	 * 
	 * @param id 恢复数据ID
	 * @return void
	 * @throws IOException 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void restoreData(ServletContext sctx, Integer id) throws IOException, ApplicationException {
		logger.info("恢复START");
		if(id == null){
			throw new ApplicationException("恢复失败,当前恢复信息不存在!");
		}
		DataBackup entity = get(id);
		if(entity == null){
			throw new ApplicationException("恢复失败,当前恢复信息不存在!");
		}
		/******删除数据 START*********/
		logger.info("删除经销商:[" + entity.getUser().getId() +"]"+entity.getUser().getName() + "记录START");
		//删除经销商下所有数据
		//如果删除失败
		if(!initDataManager.initData(entity.getUser().getId())){
			logger.error("恢复数据失败!" );
			throw new ApplicationException("数据删除中发生异常!");
		}
		logger.info("删除经销商:[" + entity.getUser().getId() +"]"+entity.getUser().getName() + "记录END");
		/******删除数据 END***********/
		File file = new File(sctx.getRealPath(entity.getDataUrl()) + File.separatorChar + entity.getDataFileName());
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		try{
			String temp = br.readLine();
			while (temp != null) {
				//恢复数据
				jdbcTemplate.update(temp);
				temp = br.readLine();
			}
			br.close();
			fr.close();
		}catch (Exception e) {
			br.close();
			fr.close();
			logger.error("恢复数据失败：" + e.getMessage());
			throw new ApplicationException(e.getMessage());
		}
		logger.info("恢复END");
	}
	
	/**
	 *  根据SQL语句和用户执行删除操作sql语句
	 *  @param sql 操作sql语句
	 *  @param user 用户对象
	 *  @return void
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteDataByUser(String sql ,User user){
		jdbcTemplate.update(sql,
                new Object[]{user.getId()},
                new int[]{java.sql.Types.INTEGER }); 
	}	
}