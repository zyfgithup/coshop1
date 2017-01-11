package com.systop.amol.base.units.webapp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Controller;

import com.systop.amol.base.product.model.Products;
import com.systop.amol.base.units.model.Units;
import com.systop.amol.base.units.model.UnitsItem;
import com.systop.amol.base.units.service.UnitsItemManager;
import com.systop.amol.base.units.service.UnitsManager;
import com.systop.common.modules.security.user.LoginUserService;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.support.Page;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.JsonCrudAction;

/**
 * 商品计量单位换算管理Action
 * 
 * @author Administrator
 * 
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class UnitsItemAction extends
  JsonCrudAction<UnitsItem, UnitsItemManager> {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/**
	 * 计量单位管理
	 */
	@Autowired
	private UnitsManager unitsManager;

	//商品ID
	private Integer productId;
	
	//JSON数据
	private Map<String, String> jsonResult;
	
	@Autowired
	private LoginUserService loginUserService;
	//是否已经使用，如果使用后就不能修改换算关系。
	private Integer isedit;

	/**
	 * AjAX应用编辑页面，通过商品得到商品单位信息
	 * 
	 * @return
	 */
	public String getUnitsItem() {
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		int productid = Integer.parseInt(getRequest().getParameter("productid"));

		try {
			User user = UserUtil.getPrincipal(getRequest());
			List<UnitsItem> items = getManager().getUnitsItem(user.getId(),user.getId(), productid);
			StringBuffer st = new StringBuffer();
			for (UnitsItem ui : items) {
				st.append(ui.getUnits().getId() + "," + ui.getUnits().getName() + ","
						+ ui.getCount() + "," + ui.getInprice() + "," + ui.getOutprice() + ":");
			}
			st.substring(0, st.length() - 1);

			jsonResult.put("result", st.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "jsonResult";
	}
	
	/**
	 * 计量单位下拉列表
	 */
	public Map getUnitsMap() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			return unitsManager.getUnitsMap(user.getId());
		}
		return null;
	}

	/**
	 * 商品计量单位换算保存
	 */
	@Override
	public String save() {
		User user = UserUtil.getPrincipal(getRequest());
		if (user != null) {
			getModel().setUser(user);
		} else {
			this.addActionMessage("当前登录用户信息不存在，请您重新登录系统！");
			return INPUT;
		}

		// 设置商品基本单位
		if (getModel().getUnits().getId() != null) {
			Units units = getManager().getDao().get(Units.class, getModel().getUnits().getId());
			getModel().setUnits(units);

		}
		//设置商品对象
		if (productId != null) {
			Products prodects = getManager().getDao().get(Products.class, productId);
			getModel().setProducts(prodects);
		}
		try {
			getManager().getDao().getHibernateTemplate().clear();
			if(getModel().getConversion()==null){
				getModel().setConversion(0);
			}
			getManager().save(getModel());
			return SUCCESS;
		} catch (Exception e) {
			addActionError(e.getMessage());
			return INPUT;
		}
	}

	

	@Override
	public String edit() {
		if(getModel()==null || getModel().getId()==null){
			if (productId != null) {
				Products prodects = getManager().getDao().get(Products.class, productId);
				getModel().setProducts(prodects);
			}
			this.isedit=0;
			return super.edit();
		}
		if(this.isedit()){
			this.isedit=0;
		}else{
           this.isedit=1;			
		}
		return super.edit();
	}

	public Integer getIsedit() {
		return isedit;
	}

	public void setIsedit(Integer isedit) {
		this.isedit = isedit;
	}

	@Override
	public String remove() {
	
		if(this.isedit()){
			return super.remove();
			}else{
				this.addActionError("此单位换算已经使用，不能删除！");
				return this.index();
			}
	}
     public boolean isedit(){
    	 //采购
    	 int count= (Integer) jdbcTemplate.query(
 				"select count(*) from purchases p,purchase_details pd where p.id=pd.purchase_id "
+"and p.status=0 and pd.unit_id=? and p.user_id=? and pd.product_id=? ",
 				new Object[] { getModel().getUnits().getId(),getModel().getUser().getId(),getModel().getProducts().getId() },
 				new ResultSetExtractor() {
 					public Object extractData(ResultSet rs) throws SQLException {
 						rs.next();
 						return rs.getInt(1);
 					}
 				});
    	 if(count>0){
    		 return false;
    	 }
    	 //销售
    	  count= (Integer) jdbcTemplate.query(
  				"select count(*) from sales s,sales_details sd where s.id=sd.sales_id "
+"and s.redRed=1 and sd.unit_id=? and s.user_id=? and sd.products_id=?  ",
  				new Object[] { getModel().getUnits().getId(),getModel().getUser().getId(),getModel().getProducts().getId() },
  				new ResultSetExtractor() {
  					public Object extractData(ResultSet rs) throws SQLException {
  						rs.next();
  						return rs.getInt(1);
  					}
  				});
    	  
     	 if(count>0){
     		 return false;
     	 }
     	 //期初库存
     	 count= (Integer) jdbcTemplate.query(
   				"select count(*) from stock_inits s where  s.unit_id=? and s.user_id=? and s.product_id=? ",
   				new Object[] { getModel().getUnits().getId(),getModel().getUser().getId(),getModel().getProducts().getId() },
   				new ResultSetExtractor() {
   					public Object extractData(ResultSet rs) throws SQLException {
   						rs.next();
   						return rs.getInt(1);
   					}
   				});
      	 if(count>0){
      		 return false;
      	 }
    	 
    	 return true;
     }
	/**
	 * 查看当前用户当前商品的单位换算信息
	 */
	public String index() {
		User user = UserUtil.getPrincipal(getRequest());
		Page page = PageUtil.getPage(getPageNo(), getPageSize());
		String sql = "from UnitsItem c where c.user=? and c.products.id=?";
		List args = new ArrayList();
		args.add(user);
		args.add(productId);
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}

	/**
	 * 根据商品基本单位的参考进价和出价换算出新增换算单位的进价和出价
	 * @return
	 */
	public String calPrice() {
		jsonResult = Collections.synchronizedMap(new HashMap<String, String>());
		String productId = getRequest().getParameter("productId");	
		String count = getRequest().getParameter("count");
		User user = loginUserService.getLoginUser(getRequest());
		if (user == null) {
			jsonResult.put("result", "error");
			return "jsonResult";
		}	
		if (StringUtils.isNotBlank(productId)) {
			Products products = getManager().getDao().get(Products.class, Integer.valueOf(productId));
			if (products.getInprice() != null ) {
				Float inprice = Float.valueOf(count) * products.getInprice();
				if (inprice > 0 )
				jsonResult.put("inprice", new DecimalFormat("#0.00").format(inprice));
			}
			if (products.getOutprice() != null ) {
				Float outprice = Float.valueOf(count) * products.getOutprice();
				if (outprice > 0 )
				jsonResult.put("outprice", new DecimalFormat("#0.00").format(outprice));
			}
		}
		return "jsonResult";
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	public Map<String, String> getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(Map<String, String> jsonResult) {
		this.jsonResult = jsonResult;
	}

}
