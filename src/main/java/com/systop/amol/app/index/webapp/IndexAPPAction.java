package com.systop.amol.app.index.webapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.amol.app.index.model.Index;
import com.systop.amol.app.push.MessageConstants;
import com.systop.amol.app.push.model.PushMessage;
import com.systop.amol.app.push.service.PushMessageManager;
import com.systop.amol.base.product.service.ProductsManager;
import com.systop.amol.base.prosort.service.ProductSortManager;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * <p>
 * 	首页显示信息
 * </p>
 * @version 1.0
 * @author 王会璞
 * 
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IndexAPPAction extends
		DefaultCrudAction<PushMessage,PushMessageManager> {

	private static final long serialVersionUID = 5855587303581149082L;

	@Autowired
	private ProductsManager productsManager;
	
	@Autowired
	private ProductSortManager productSortManager;

	
	/** 首页显示信息 */
	private Index index = new Index();
	
	/**
	 * 首页显示图片信息
	 * 返回数据为json数组，数组中有三个数组，分别对应首页轮播图片、商品、商品类型数据集合
	 * @return
	 */
	public String index() {
		page = PageUtil.getPage(getPageNo(), 6);
		StringBuffer sql = new StringBuffer("from PushMessage o where o.type = ?");
		List args = new ArrayList();
		args.add(MessageConstants.PHOTO_CAROUSEL_IMG);
		page = getManager().pageQuery(page, sql.toString(), args.toArray());
		index.setPhotoCarouselImgList(page.getData());
		return "index";
	}

	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}
}
